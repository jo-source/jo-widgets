/*
 * Copyright (c) 2012, David Bauknecht
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.spi.impl.javafx.widgets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.ComponentObservable;
import org.jowidgets.spi.impl.controller.FocusObservable;
import org.jowidgets.spi.impl.controller.IObservableCallback;
import org.jowidgets.spi.impl.controller.KeyObservable;
import org.jowidgets.spi.impl.controller.MouseButtonEvent;
import org.jowidgets.spi.impl.controller.MouseObservable;
import org.jowidgets.spi.impl.controller.PopupDetectionObservable;
import org.jowidgets.spi.impl.javafx.util.CursorConvert;
import org.jowidgets.spi.impl.javafx.util.MouseUtil;
import org.jowidgets.spi.impl.javafx.widgets.event.LazyKeyEventContentFactory;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class JavafxComponent implements IComponentSpi {

	private final Node node;

	private final PopupDetectionObservable popupDetectionObservable;
	private final FocusObservable focusObservable;
	private final KeyObservable keyObservable;
	private final MouseObservable mouseObservable;
	private final ComponentObservable componentObservable;
	private final EventHandler<KeyEvent> keyListener;

	private final StyleUtil styleDelegate;

	private EventHandler<MouseEvent> mouseListener;

	public JavafxComponent(final Node node) {
		this.node = node;
		this.popupDetectionObservable = new PopupDetectionObservable();
		this.focusObservable = new FocusObservable();
		this.mouseObservable = new MouseObservable();
		this.componentObservable = new ComponentObservable();
		this.styleDelegate = new StyleUtil(this.node);

		node.setOnMousePressed(new EventHandler<MouseEvent>() {

			//TODO DB find a better way
			@SuppressWarnings("deprecation")
			@Override
			public void handle(final MouseEvent event) {
				if (MouseEvent.impl_getPopupTrigger(event)) {
					popupDetectionObservable.firePopupDetected(new Position((int) event.getScreenX(), (int) event.getScreenY()));
				}
			}
		});

		node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@SuppressWarnings("deprecation")
			@Override
			public void handle(final MouseEvent event) {
				if (MouseEvent.impl_getPopupTrigger(event)) {
					popupDetectionObservable.firePopupDetected(new Position((int) event.getScreenX(), (int) event.getScreenY()));
				}
			}
		});

		getUiReference().setOnDragDone(new EventHandler<DragEvent>() {

			@Override
			public void handle(final DragEvent paramT) {
				componentObservable.firePositionChanged();
			}
		});

		getUiReference().layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(
				final ObservableValue<? extends Bounds> paramStringObservableValue,
				final Bounds oldValue,
				final Bounds newValue) {
				if (newValue.getHeight() != oldValue.getHeight() || newValue.getWidth() != oldValue.getWidth()) {
					componentObservable.fireSizeChanged();
				}
			}
		});

		getUiReference().focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(
				final ObservableValue<? extends Boolean> observableValue,
				final Boolean oldValue,
				final Boolean newValue) {
				if (newValue) {
					focusObservable.focusGained();
				}
				else {
					focusObservable.focusLost();
				}
			}
		});

		this.keyListener = new EventHandler<KeyEvent>() {

			@Override
			public void handle(final KeyEvent paramT) {
				if (paramT.getEventType() == KeyEvent.KEY_PRESSED) {
					keyObservable.fireKeyPressed(new LazyKeyEventContentFactory(paramT));
				}
				if (paramT.getEventType() == KeyEvent.KEY_RELEASED) {
					keyObservable.fireKeyReleased(new LazyKeyEventContentFactory(paramT));
				}
			}
		};

		final IObservableCallback keyObservableCallback = new IObservableCallback() {
			@Override
			public void onLastUnregistered() {
				getUiReference().removeEventHandler(KeyEvent.ANY, keyListener);
			}

			@Override
			public void onFirstRegistered() {
				getUiReference().addEventHandler(KeyEvent.ANY, keyListener);
			}
		};

		this.keyObservable = new KeyObservable(keyObservableCallback);

		final EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent event) {
				if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					mouseObservable.fireMouseExit(new Position((int) event.getX(), (int) event.getY()));
				}
				else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
					mouseObservable.fireMouseEnter(new Position((int) event.getX(), (int) event.getY()));
				}
				else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
					if (event.getClickCount() > 1) {
						final IMouseButtonEvent mouseEvent = getMouseEvent(event, 2);
						if (mouseEvent != null) {
							mouseObservable.fireMouseDoubleClicked(mouseEvent);
						}
					}
				}
				else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					final IMouseButtonEvent mouseEvent = getMouseEvent(event, 1);
					if (mouseEvent != null) {
						mouseObservable.fireMousePressed(mouseEvent);
					}
				}
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					final IMouseButtonEvent mouseEvent = getMouseEvent(event, 1);
					if (mouseEvent != null) {
						mouseObservable.fireMouseReleased(mouseEvent);
					}
				}
			}
		};
		getUiReference().addEventHandler(MouseEvent.ANY, handler);

	}

	private IMouseButtonEvent getMouseEvent(final MouseEvent event, final int clickCount) {
		if (event.getClickCount() != clickCount) {
			return null;
		}

		final MouseButton mouseButton = MouseUtil.getMouseButton(event);
		if (mouseButton == null) {
			return null;
		}
		final Position position = new Position((int) event.getX(), (int) event.getY());
		return new MouseButtonEvent(position, mouseButton, MouseUtil.getModifier(event));
	}

	protected PopupDetectionObservable getPopupDetectionObservable() {
		return popupDetectionObservable;
	}

	protected void setMouseListener(final EventHandler<MouseEvent> mouseListener) {
		getUiReference().removeEventHandler(MouseEvent.ANY, this.mouseListener);
		this.mouseListener = mouseListener;
		getUiReference().addEventFilter(MouseEvent.ANY, mouseListener);
	}

	@Override
	public Node getUiReference() {
		return node;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		getUiReference().setDisable(!enabled);

	}

	@Override
	public boolean isEnabled() {
		return !getUiReference().isDisable();
	}

	@Override
	public void redraw() {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public boolean requestFocus() {
		getUiReference().requestFocus();
		if (getUiReference().isFocused()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		styleDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		styleDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return styleDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return styleDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getUiReference().setCursor(CursorConvert.convert(cursor));
	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isVisible();
	}

	@Override
	public Dimension getSize() {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public void setSize(final Dimension size) {
		getUiReference().setManaged(false);
		getUiReference().resize(size.getWidth(), size.getHeight());

	}

	@Override
	public Position getPosition() {
		return new Position((int) getUiReference().getLayoutX(), (int) getUiReference().getLayoutY());
	}

	@Override
	public void setPosition(final Position position) {
		getUiReference().setLayoutX(position.getX());
		getUiReference().setLayoutY(position.getY());
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		keyObservable.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		keyObservable.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		mouseObservable.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		mouseObservable.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		componentObservable.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		componentObservable.removeComponentListener(componentListener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		this.focusObservable.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		this.focusObservable.removeFocusListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.removePopupDetectionListener(listener);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(getUiReference());
	}

}

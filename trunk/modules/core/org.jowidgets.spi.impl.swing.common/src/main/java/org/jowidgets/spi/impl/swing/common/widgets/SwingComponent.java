/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

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
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.ComponentObservable;
import org.jowidgets.spi.impl.controller.FocusObservable;
import org.jowidgets.spi.impl.controller.IObservableCallback;
import org.jowidgets.spi.impl.controller.KeyObservable;
import org.jowidgets.spi.impl.controller.MouseButtonEvent;
import org.jowidgets.spi.impl.controller.MouseMotionObservable;
import org.jowidgets.spi.impl.controller.MouseObservable;
import org.jowidgets.spi.impl.controller.PopupDetectionObservable;
import org.jowidgets.spi.impl.swing.common.util.ColorConvert;
import org.jowidgets.spi.impl.swing.common.util.CursorConvert;
import org.jowidgets.spi.impl.swing.common.util.DimensionConvert;
import org.jowidgets.spi.impl.swing.common.util.MouseUtil;
import org.jowidgets.spi.impl.swing.common.util.PositionConvert;
import org.jowidgets.spi.impl.swing.common.widgets.event.LazyKeyEventContentFactory;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class SwingComponent extends SwingWidget implements IComponentSpi {

	private final PopupDetectionObservable popupDetectionObservable;
	private final FocusObservable focusObservable;
	private final KeyObservable keyObservable;
	private final MouseObservable mouseObservable;
	private final MouseMotionObservable mouseMotionObservable;
	private final ComponentObservable componentObservable;
	private final KeyListener keyListener;

	private MouseListener mouseListener;

	public SwingComponent(final Component component) {
		super(component);
		this.popupDetectionObservable = new PopupDetectionObservable();
		this.focusObservable = new FocusObservable();
		this.mouseObservable = new MouseObservable();
		this.componentObservable = new ComponentObservable();

		this.mouseListener = new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupDetectionObservable.firePopupDetected(new Position(e.getX(), e.getY()));
				}
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupDetectionObservable.firePopupDetected(new Position(e.getX(), e.getY()));
				}
			}
		};
		component.addMouseListener(mouseListener);

		getUiReference().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				componentObservable.fireSizeChanged();
			}

			@Override
			public void componentMoved(final ComponentEvent e) {
				componentObservable.firePositionChanged();
			}
		});

		getUiReference().addFocusListener(new FocusListener() {
			@Override
			public void focusLost(final FocusEvent e) {
				focusObservable.focusLost();
			}

			@Override
			public void focusGained(final FocusEvent e) {
				focusObservable.focusGained();
			}
		});

		this.keyListener = new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				keyObservable.fireKeyReleased(new LazyKeyEventContentFactory(e));
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				keyObservable.fireKeyPressed(new LazyKeyEventContentFactory(e));
			}
		};

		final IObservableCallback keyObservableCallback = new IObservableCallback() {
			@Override
			public void onLastUnregistered() {
				getUiReference().removeKeyListener(keyListener);
			}

			@Override
			public void onFirstRegistered() {
				getUiReference().addKeyListener(keyListener);
			}
		};

		this.keyObservable = new KeyObservable(keyObservableCallback);

		getUiReference().addMouseListener(new MouseListener() {

			@Override
			public void mouseExited(final MouseEvent e) {
				mouseObservable.fireMouseExit(new Position(e.getX(), e.getY()));
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				mouseObservable.fireMouseEnter(new Position(e.getX(), e.getY()));
			}

			@Override
			public void mouseClicked(final MouseEvent e) {
				final IMouseButtonEvent mouseEvent = getMouseEvent(e, 2);
				if (mouseEvent != null) {
					mouseObservable.fireMouseDoubleClicked(mouseEvent);
				}
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				final IMouseButtonEvent mouseEvent = getMouseEvent(e, 1);
				if (mouseEvent != null) {
					mouseObservable.fireMousePressed(mouseEvent);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				final IMouseButtonEvent mouseEvent = getMouseEvent(e, 1);
				if (mouseEvent != null) {
					mouseObservable.fireMouseReleased(mouseEvent);
				}
			}

			private IMouseButtonEvent getMouseEvent(final MouseEvent event, final int clickCount) {
				if (event.getClickCount() != clickCount) {
					return null;
				}

				final MouseButton mouseButton = MouseUtil.getMouseButton(event);
				if (mouseButton == null) {
					return null;
				}
				final Position position = new Position(event.getX(), event.getY());
				return new MouseButtonEvent(position, mouseButton, MouseUtil.getModifier(event));
			}
		});

		final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

			@Override
			public void mouseMoved(final MouseEvent e) {
				final Position position = new Position(e.getX(), e.getY());
				mouseMotionObservable.fireMouseMoved(position);
			}

			@Override
			public void mouseDragged(final MouseEvent event) {
				final MouseButton mouseButton = MouseUtil.getMouseButton(event);
				if (mouseButton == null) {
					return;
				}
				final Position position = new Position(event.getX(), event.getY());
				final MouseButtonEvent mouseButtonEvent = new MouseButtonEvent(
					position,
					mouseButton,
					MouseUtil.getModifier(event));
				mouseMotionObservable.fireMouseDragged(mouseButtonEvent);
			}
		};

		final IObservableCallback mouseMotionObservableCallback = new IObservableCallback() {

			@Override
			public void onFirstRegistered() {
				getUiReference().addMouseMotionListener(mouseMotionListener);
			}

			@Override
			public void onLastUnregistered() {
				getUiReference().removeMouseMotionListener(mouseMotionListener);
			}
		};

		this.mouseMotionObservable = new MouseMotionObservable(mouseMotionObservableCallback);
	}

	protected PopupDetectionObservable getPopupDetectionObservable() {
		return popupDetectionObservable;
	}

	protected void setMouseListener(final MouseListener mouseListener) {
		getUiReference().removeMouseListener(this.mouseListener);
		this.mouseListener = mouseListener;
		getUiReference().addMouseListener(mouseListener);
	}

	@Override
	public void setComponent(final Component component) {
		getUiReference().removeMouseListener(mouseListener);
		super.setComponent(component);
		getUiReference().addMouseListener(mouseListener);
	}

	@Override
	public void redraw() {
		if (getUiReference() instanceof JComponent) {
			((JComponent) getUiReference()).revalidate();
		}
		else {
			getUiReference().validate();
		}
		getUiReference().repaint();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		if (enabled) {
			redraw();
		}
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getUiReference().setForeground(ColorConvert.convert(colorValue));
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().setBackground(ColorConvert.convert(colorValue));
	}

	@Override
	public IColorConstant getForegroundColor() {
		return ColorConvert.convert(getUiReference().getForeground());
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return ColorConvert.convert(getUiReference().getBackground());
	}

	public void setToolTipText(final String toolTip) {
		final Component uiReference = getUiReference();
		if (uiReference instanceof JComponent) {
			((JComponent) uiReference).setToolTipText(toolTip);
		}
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
		return DimensionConvert.convert(getUiReference().getSize());
	}

	@Override
	public void setSize(final Dimension size) {
		getUiReference().setSize(DimensionConvert.convert(size));
	}

	@Override
	public Position getPosition() {
		return PositionConvert.convert(getUiReference().getLocation());
	}

	@Override
	public void setPosition(final Position position) {
		getUiReference().setLocation(PositionConvert.convert(position));
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(getUiReference());
	}

	@Override
	public boolean requestFocus() {
		return getUiReference().requestFocusInWindow();
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
	public void addMouseMotionListener(final IMouseMotionListener listener) {
		mouseMotionObservable.addMouseMotionListener(listener);
	}

	@Override
	public void removeMouseMotionListener(final IMouseMotionListener listener) {
		mouseMotionObservable.removeMouseMotionListener(listener);
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

}

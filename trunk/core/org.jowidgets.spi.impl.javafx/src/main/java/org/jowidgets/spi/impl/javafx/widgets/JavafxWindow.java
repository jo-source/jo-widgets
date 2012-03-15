/*
 * Copyright (c) 2012, David Bauknecht
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
package org.jowidgets.spi.impl.javafx.widgets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IDisplayCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.controller.WindowObservable;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.IWindowSpi;

public class JavafxWindow implements IWindowSpi {

	private final Stage stage;
	private final Pane pane;
	private final WindowObservable windowObservableDelegate;
	private final JavafxContainer containerDelegate;
	private final IGenericWidgetFactory factory;

	public JavafxWindow(final IGenericWidgetFactory factory, final Stage stage, final boolean closeable) {
		this.factory = factory;
		this.stage = stage;
		this.pane = new Pane();
		this.stage.setScene(new Scene(pane));
		this.windowObservableDelegate = new WindowObservable();
		final EventHandler<WindowEvent> handler = new EventHandler<WindowEvent>() {

			@Override
			public void handle(final WindowEvent event) {
				if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
					windowObservableDelegate.fireWindowClosing();
				}
				else if (event.getEventType() == WindowEvent.WINDOW_SHOWN) {
					windowObservableDelegate.fireWindowActivated();
				}
				else if (event.getEventType() == WindowEvent.WINDOW_HIDDEN) {
					windowObservableDelegate.fireWindowClosed();
				}
			}
		};

		getUiReference().addEventHandler(WindowEvent.ANY, handler);
		getUiReference().iconifiedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(final ObservableValue<? extends Boolean> arg0, final Boolean oldValue, final Boolean newValue) {
				if (newValue) {
					windowObservableDelegate.fireWindowIconified();
				}
				else {
					windowObservableDelegate.fireWindowDeiconified();
				}
			}
		});
		containerDelegate = new JavafxContainer(factory, pane);
	}

	@Override
	public Stage getUiReference() {
		return stage;
	}

	@Override
	public Rectangle getParentBounds() {
		return new Rectangle(getPosition(), getSize());
	}

	@Override
	public void pack() {
		// TODO DB calculate size 
		// https://forums.oracle.com/forums/thread.jspa?messageID=10174045

	}

	@Override
	public void dispose() {
		// TODO DB Auto-generated method stub
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		windowObservableDelegate.addWindowListener(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		windowObservableDelegate.removeWindowListener(listener);
	}

	@Override
	public <WIDGET_TYPE extends IDisplayCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return factory.create(getUiReference(), descriptor);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		containerDelegate.setEnabled(enabled);

	}

	@Override
	public boolean isEnabled() {
		return containerDelegate.isEnabled();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(getUiReference());
	}

	@Override
	public void redraw() {
		containerDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		containerDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public boolean requestFocus() {
		getUiReference().requestFocus();
		return getUiReference().focusedProperty().getValue();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		containerDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().getScene().setFill(
				Color.rgb(
						colorValue.getDefaultValue().getRed(),
						colorValue.getDefaultValue().getGreen(),
						colorValue.getDefaultValue().getBlue()));
	}

	@Override
	public IColorConstant getForegroundColor() {
		return containerDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		final Color color = (Color) getUiReference().getScene().getFill();
		return new ColorValue((int) color.getRed(), (int) color.getGreen(), (int) color.getBlue());
	}

	@Override
	public void setCursor(final Cursor cursor) {
		if (cursor == Cursor.ARROW) {
			getUiReference().getScene().setCursor(javafx.scene.Cursor.MOVE);
		}
		else if (cursor == Cursor.DEFAULT) {
			getUiReference().getScene().setCursor(javafx.scene.Cursor.DEFAULT);
		}
		else if (cursor == Cursor.WAIT) {
			getUiReference().getScene().setCursor(javafx.scene.Cursor.WAIT);
		}
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			getUiReference().show();
		}
		else {
			getUiReference().hide();
		}
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isShowing();
	}

	@Override
	public Dimension getSize() {
		return new Dimension(
			getUiReference().widthProperty().getValue().intValue(),
			getUiReference().heightProperty().getValue().intValue());
	}

	@Override
	public void setSize(final Dimension size) {
		getUiReference().setHeight(size.getHeight());
		getUiReference().setWidth(size.getWidth());
	}

	@Override
	public Position getPosition() {
		return new Position((int) getUiReference().getX(), (int) getUiReference().getY());
	}

	@Override
	public void setPosition(final Position position) {
		getUiReference().setX(position.getX());
		getUiReference().setY(position.getY());
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		containerDelegate.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		containerDelegate.removeComponentListener(componentListener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		containerDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		containerDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		containerDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		containerDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener listener) {
		containerDelegate.addMouseListener(listener);
	}

	@Override
	public void removeMouseListener(final IMouseListener listener) {
		containerDelegate.removeMouseListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		containerDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		containerDelegate.removePopupDetectionListener(listener);
	}

	protected final JavafxContainer getContainerDelegate() {
		return containerDelegate;
	}

}

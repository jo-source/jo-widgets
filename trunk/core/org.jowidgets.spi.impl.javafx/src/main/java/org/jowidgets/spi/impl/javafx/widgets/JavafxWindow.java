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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
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
import org.jowidgets.spi.impl.javafx.layout.LayoutManagerImpl;
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
		this.pane = new LayoutManagerImpl();
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
		Dimension parentSize;
		Position parentPosition = new Position(0, 0);
		if (getUiReference().getOwner() != null) {
			parentPosition = new Position((int) getUiReference().getOwner().getX(), (int) getUiReference().getOwner().getY());
			parentSize = new Dimension(
				(int) getUiReference().getOwner().getWidth(),
				(int) getUiReference().getOwner().getHeight());
		}
		else {
			parentSize = new Dimension(
				(int) Screen.getPrimary().getVisualBounds().getWidth(),
				(int) Screen.getPrimary().getVisualBounds().getHeight());

		}

		return new Rectangle(parentPosition, parentSize);
	}

	@Override
	public void pack() {
		// TODO DB ugly thing to get size of stage before showing
		if (!getUiReference().isShowing()) {
			getUiReference().setOpacity(0);
			getUiReference().show();
			getUiReference().close();
			getUiReference().setOpacity(1);
		}

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
		//CHECKSTYLE:OFF
		for (final Node node : getUiReference().getScene().getRoot().getChildrenUnmodifiable()) {
			sysoutTemp(node);
			if (node instanceof Pane) {
				System.out.println("Pane: " + node + " has folgende");
				for (final Node node2 : ((Pane) node).getChildren()) {
					sysoutTemp(node2);
					if (node2 instanceof Pane) {
						System.out.println("Pane: " + node2 + " has folgende");
						for (final Node node3 : ((Pane) node2).getChildren()) {
							sysoutTemp(node3);
							if (node3 instanceof Pane) {
								System.out.println("Pane: " + node3 + " has folgende");
								for (final Node node4 : ((Pane) node3).getChildren()) {
									sysoutTemp(node4);
									if (node4 instanceof Pane) {
										System.out.println("Pane: " + node4 + " has folgende");
										for (final Node node5 : ((Pane) node4).getChildren()) {
											sysoutTemp(node5);
											if (node5 instanceof Pane) {
												System.out.println("Pane: " + node5 + " has folgende");
												for (final Node node6 : ((Pane) node5).getChildren()) {
													sysoutTemp(node6);
													if (node6 instanceof Pane) {
														System.out.println("Pane: " + node6 + " has folgende");
														for (final Node node7 : ((Pane) node6).getChildren()) {
															sysoutTemp(node7);
															if (node7 instanceof Pane) {
																System.out.println("Pane: " + node7 + " has folgende");
																for (final Node node8 : ((Pane) node7).getChildren()) {
																	sysoutTemp(node8);
																	if (node8 instanceof Pane) {
																		System.out.println("Pane: " + node8 + " has folgende");
																		for (final Node node9 : ((Pane) node8).getChildren()) {
																			sysoutTemp(node9);
																			if (node9 instanceof Pane) {

																			}
																			System.out.println("*** Ende ***");
																		}
																	}
																	System.out.println("*** Ende ***");
																}
															}
															System.out.println("*** Ende ***");
														}
													}
													System.out.println("*** Ende ***");
												}
											}
											System.out.println("*** Ende ***");
										}
									}
									System.out.println("*** Ende ***");
								}
							}
							System.out.println("*** Ende ***");
						}
					}
					System.out.println("*** Ende ***");
				}

			}

		}
		//CHECKSTYLE:ON
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isShowing();
	}

	@Override
	public Dimension getSize() {
		return new Dimension((int) getUiReference().getWidth(), (int) getUiReference().getHeight());
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

	private void sysoutTemp(final Node node) {
		//CHECKSTYLE:OFF
		if (node instanceof Control) {
			System.out.println("Node: "
				+ node
				+ ", Max : "
				+ new Dimension(
					(int) ((Control) node).maxWidth(Control.USE_PREF_SIZE),
					(int) ((Control) node).maxHeight(Control.USE_PREF_SIZE)));
			System.out.println("Node: "
				+ node
				+ ", Min : "
				+ new Dimension(
					(int) ((Control) node).minWidth(Control.USE_PREF_SIZE),
					(int) ((Control) node).minHeight(Control.USE_PREF_SIZE)));
			System.out.println("Node: "
				+ node
				+ ", Pref : "
				+ new Dimension(
					(int) ((Control) node).prefWidth(Control.USE_PREF_SIZE),
					(int) ((Control) node).prefHeight(Control.USE_PREF_SIZE)));
			System.out.println("Node: " + node + ", Size : " + ((Control) node).getWidth() + " " + ((Control) node).getHeight());
			System.out.println("Node: "
				+ node
				+ ", Skin : "
				+ ((Control) node).getSkin().getSkinnable()
				+ " node "
				+ ((Control) node).getSkin().getNode().getLayoutBounds());
			if (node instanceof TabPane) {
				System.out.println(((TabPane) node).getTabs());
			}
			if (node instanceof SplitPane) {
				System.out.println(((SplitPane) node).getItems());
			}
			System.out.println("-------------------------------------");
		}
		if (node instanceof Pane) {
			System.out.println("Pane: "
				+ node
				+ ", Max : "
				+ new Dimension(
					(int) ((Pane) node).maxWidth(Control.USE_PREF_SIZE),
					(int) ((Pane) node).maxHeight(Control.USE_PREF_SIZE)));
			System.out.println("Pane: "
				+ node
				+ ", Min : "
				+ new Dimension(
					(int) ((Pane) node).minWidth(Control.USE_PREF_SIZE),
					(int) ((Pane) node).minHeight(Control.USE_PREF_SIZE)));
			System.out.println("Pane: "
				+ node
				+ ", Pref : "
				+ new Dimension(
					(int) ((Pane) node).prefWidth(Control.USE_PREF_SIZE),
					(int) ((Pane) node).prefHeight(Control.USE_PREF_SIZE)));
			System.out.println("Pane: " + node + ", Size : " + ((Pane) node).getWidth() + " " + ((Pane) node).getHeight());
			System.out.println("-------------------------------------");

		}
		//CHECKSTYLE:ON
	}
}

/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.impl.widgets.common.wrapper;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.controller.IShowingStateListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.tools.controller.ShowingStateObservable;
import org.jowidgets.util.NullCompatibleEquivalence;

public abstract class AbstractComponentSpiWrapper extends WidgetSpiWrapper implements IComponentCommon {

	private final IPopupDetectionListener popupListener;
	private final Set<IFocusListener> focusListeners;
	private final ShowingStateListener showingStateListener;

	private ShowingStateObservable showingStateObservableLazy;
	private IMenuModel popupMenuModel;
	private IPopupMenu popupMenu;

	private boolean hasFocus;

	public AbstractComponentSpiWrapper(final IComponentSpi component) {
		super(component);
		this.showingStateListener = new ShowingStateListener();
		this.focusListeners = new LinkedHashSet<IFocusListener>();
		this.popupListener = new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				if (popupMenuModel != null) {
					if (popupMenu == null) {
						popupMenu = createPopupMenu();
					}
					if (popupMenu.getModel() != popupMenuModel) {
						popupMenu.setModel(popupMenuModel);
					}
					popupMenu.show(position);
				}
			}
		};

		component.addFocusListener(new IFocusListener() {
			@Override
			public void focusLost() {
				hasFocus = false;
				for (final IFocusListener focusListener : focusListeners) {
					focusListener.focusLost();
				}
			}

			@Override
			public void focusGained() {
				hasFocus = true;
				for (final IFocusListener focusListener : focusListeners) {
					focusListener.focusGained();
				}
			}
		});
	}

	public abstract IPopupMenu createPopupMenu();

	public abstract IWidget getParent();

	public boolean hasFocus() {
		return hasFocus;
	}

	@Override
	public void redraw() {
		getWidget().redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		getWidget().setRedrawEnabled(enabled);
	}

	@Override
	public IComponentSpi getWidget() {
		return (IComponentSpi) super.getWidget();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getWidget().setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getWidget().setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return getWidget().getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return getWidget().getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getWidget().setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		getWidget().setVisible(visible);
		if (showingStateObservableLazy != null) {
			showingStateObservableLazy.fireShowingStateChanged(isShowing());
		}
	}

	@Override
	public boolean isVisible() {
		return getWidget().isVisible();
	}

	public boolean isShowing() {
		if (isVisible()) {
			final IWidget parent = getParent();
			if (parent == null) {
				return true;
			}
			else if (parent instanceof IComponent) {
				return ((IComponent) parent).isShowing();
			}
		}
		return false;
	}

	@Override
	public Dimension getSize() {
		return getWidget().getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		if (!NullCompatibleEquivalence.equals(size, getSize())) {
			getWidget().setSize(size);
		}
	}

	@Override
	public Position getPosition() {
		return getWidget().getPosition();
	}

	public void setSize(final int width, final int height) {
		setSize(new Dimension(width, height));
	}

	public void setPosition(final int x, final int y) {
		setPosition(new Position(x, y));
	}

	@Override
	public void setPosition(final Position position) {
		if (!NullCompatibleEquivalence.equals(position, getPosition())) {
			getWidget().setPosition(position);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(getWidget().getPosition(), getWidget().getSize());
	}

	public void setBounds(final Rectangle bounds) {
		setPosition(bounds.getPosition());
		setSize(bounds.getSize());
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		getWidget().addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		getWidget().removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		getWidget().addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		getWidget().removeMouseListener(mouseListener);
	}

	@Override
	public void addMouseMotionListener(final IMouseMotionListener listener) {
		getWidget().addMouseMotionListener(listener);
	}

	@Override
	public void removeMouseMotionListener(final IMouseMotionListener listener) {
		getWidget().removeMouseMotionListener(listener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		getWidget().addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		getWidget().removeComponentListener(componentListener);
	}

	@Override
	public final void addFocusListener(final IFocusListener listener) {
		focusListeners.add(listener);
	}

	@Override
	public final void removeFocusListener(final IFocusListener listener) {
		focusListeners.remove(listener);
	}

	public final void addShowingStateListener(final IShowingStateListener listener) {
		getShowingStateObservable().addShowingStateListener(listener);
	}

	public final void removeShowingStateListener(final IShowingStateListener listener) {
		getShowingStateObservable().removeShowingStateListener(listener);
	}

	private ShowingStateObservable getShowingStateObservable() {
		if (showingStateObservableLazy == null) {
			showingStateObservableLazy = new ShowingStateObservable(isShowing());
			if (getThis() instanceof IControl) {
				final IWidget parent = getParent();
				if (parent instanceof IContainer) {
					((IContainer) parent).addShowingStateListener(showingStateListener);
				}
				final IControl thisControl = (IControl) getThis();
				thisControl.addParentListener(new IParentListener<IContainer>() {
					@Override
					public void parentChanged(final IContainer oldParent, final IContainer newParent) {
						if (oldParent != null) {
							oldParent.removeShowingStateListener(showingStateListener);
						}
						if (newParent != null) {
							newParent.addShowingStateListener(showingStateListener);
							if (showingStateObservableLazy != null) {
								showingStateObservableLazy.fireShowingStateChanged(isShowing());
							}
						}
					}
				});
			}
			else {
				final IWidget parent = getParent();
				if (parent instanceof IComponent) {
					((IComponent) parent).addShowingStateListener(showingStateListener);
				}
			}
		}
		return showingStateObservableLazy;
	}

	Object getThis() {
		return this;
	}

	protected ShowingStateObservable getShowingStateObservableLazy() {
		return showingStateObservableLazy;
	}

	@Override
	public boolean requestFocus() {
		return getWidget().requestFocus();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		getWidget().addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		getWidget().removePopupDetectionListener(listener);
	}

	public void setPopupMenu(final IMenuModel popupMenuModel) {
		if (popupMenuModel == null && this.popupMenuModel != null) {
			removePopupDetectionListener(popupListener);
		}
		else if (popupMenuModel != null && this.popupMenuModel == null) {
			addPopupDetectionListener(popupListener);
		}
		this.popupMenuModel = popupMenuModel;
	}

	public Position toScreen(final Position localPosition) {
		return Toolkit.toScreen(localPosition, (IComponent) this);
	}

	public Position toLocal(final Position screenPosition) {
		return Toolkit.toLocal(screenPosition, (IComponent) this);
	}

	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		final Position screenPosition = Toolkit.toScreen(componentPosition, (IComponent) component);
		return Toolkit.toLocal(screenPosition, (IComponent) this);
	}

	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		final Position screenPosition = Toolkit.toScreen(componentPosition, (IComponent) this);
		return Toolkit.toLocal(screenPosition, (IComponent) component);
	}

	private final class ShowingStateListener implements IShowingStateListener {
		@Override
		public void showingStateChanged(final boolean isShowing) {
			if (showingStateObservableLazy != null) {
				showingStateObservableLazy.fireShowingStateChanged(isShowing());
			}
		}
	}
}

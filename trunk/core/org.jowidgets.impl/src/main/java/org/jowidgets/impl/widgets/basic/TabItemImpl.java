/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.impl.widgets.basic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.controler.ITabItemListener;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.descriptor.ITabItemDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.impl.base.delegate.ComponentDelegate;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractContainerSpiWrapper;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.controler.ITabItemListenerSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ValueHolder;

public class TabItemImpl extends AbstractContainerSpiWrapper implements ITabItem {

	private ContainerDelegate containerDelegate;
	private final ComponentDelegate componentDelegate;

	private TabFolderImpl tabFolderImpl;

	private boolean detached;
	private boolean selected;
	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private Object detachedContent;

	private final Set<ITabItemListener> itemListeners;
	private final ITabItemListenerSpi tabItemListenerSpi;

	private final Set<IPopupDetectionListener> popupDetectionListeners;
	private final IPopupDetectionListener popupDetectionListener;

	private final Set<IPopupDetectionListener> tabPopupDetectionListeners;
	private final IPopupDetectionListener tabPopupDetectionListener;

	public TabItemImpl(final ITabItemSpi widget, final ITabItemDescriptor descriptor, final TabFolderImpl tabFolderImpl) {
		super(widget);
		this.tabFolderImpl = tabFolderImpl;
		this.detached = false;
		this.selected = false;
		this.itemListeners = new HashSet<ITabItemListener>();
		this.componentDelegate = new ComponentDelegate();

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

		setText(descriptor.getText());
		setToolTipText(descriptor.getToolTipText());
		setIcon(descriptor.getIcon());

		this.tabItemListenerSpi = new ITabItemListenerSpi() {

			@Override
			public void selected() {
				setSelected(true);
			}

			@Override
			public void onClose(final IVetoable vetoable) {
				final ValueHolder<Boolean> veto = new ValueHolder<Boolean>(false);

				final IVetoable innerVetoable = new IVetoable() {
					@Override
					public void veto() {
						veto.set(Boolean.TRUE);
					}
				};

				for (final ITabItemListener listener : itemListeners) {
					listener.onClose(innerVetoable);
					if (veto.get().booleanValue()) {
						vetoable.veto();
						break;
					}
				}

				if (!veto.get().booleanValue()) {
					getWidget().removeTabItemListener(tabItemListenerSpi);
					if (selected) {
						setSelected(false);
					}
				}

			}
		};
		widget.addTabItemListener(tabItemListenerSpi);

		this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();
		this.tabPopupDetectionListeners = new HashSet<IPopupDetectionListener>();

		this.popupDetectionListener = new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				for (final IPopupDetectionListener listener : popupDetectionListeners) {
					listener.popupDetected(position);
				}
			}
		};

		this.tabPopupDetectionListener = new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				for (final IPopupDetectionListener listener : tabPopupDetectionListeners) {
					listener.popupDetected(position);
				}
			}
		};

		widget.addPopupDetectionListener(popupDetectionListener);
		widget.addTabPopupDetectionListener(tabPopupDetectionListener);

		this.containerDelegate = new ContainerDelegate(widget, this);
	}

	@Override
	public ITabItemSpi getWidget() {
		return (ITabItemSpi) super.getWidget();
	}

	protected boolean isSelected() {
		return selected;
	}

	protected void setSelected(final boolean selected) {
		if (this.isSelected() != selected) {
			this.selected = selected;
			if (selected) {
				//un-select other items if this item is selected 
				for (final TabItemImpl item : tabFolderImpl.getItemsImpl()) {
					if (item != this && item.isSelected()) {
						item.setSelected(false);
					}
				}
			}
			for (final ITabItemListener listener : itemListeners) {
				listener.selectionChanged(selected);
			}
		}
	}

	protected void attach(final ITabItemSpi widget, final TabFolderImpl tabFolderImpl) {
		Assert.paramNotNull(widget, "widget");
		this.detached = false;
		setWidget(widget);
		this.tabFolderImpl = tabFolderImpl;
		this.containerDelegate = new ContainerDelegate(widget, this);
		widget.setText(text);
		widget.setToolTipText(toolTipText);
		widget.setIcon(icon);
		widget.addTabItemListener(tabItemListenerSpi);
		widget.addPopupDetectionListener(popupDetectionListener);
		widget.addTabPopupDetectionListener(tabPopupDetectionListener);
		widget.attachContent(detachedContent);
	}

	protected void detach() {
		if (detached) {
			throw new IllegalStateException("Item is already detached");
		}
		final ITabItemSpi widget = getWidget();
		detachedContent = widget.detachContent();
		widget.removeTabItemListener(tabItemListenerSpi);
		widget.removePopupDetectionListener(popupDetectionListener);
		widget.removeTabPopupDetectionListener(tabPopupDetectionListener);
		this.detached = true;
	}

	@Override
	public boolean isDetached() {
		return detached;
	}

	@Override
	public void addTabItemListener(final ITabItemListener listener) {
		checkDetached();
		itemListeners.add(listener);
	}

	@Override
	public void removeTabItemListener(final ITabItemListener listener) {
		checkDetached();
		itemListeners.remove(listener);
	}

	@Override
	public void setText(final String text) {
		checkDetached();
		this.text = text;
		getWidget().setText(text);
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		checkDetached();
		this.toolTipText = toolTipText;
		getWidget().setToolTipText(toolTipText);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		checkDetached();
		this.icon = icon;
		getWidget().setIcon(icon);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public boolean isReparentable() {
		checkDetached();
		return getWidget().isReparentable();
	}

	@Override
	public IComponent getParent() {
		checkDetached();
		return componentDelegate.getParent();
	}

	@Override
	public void setParent(final IComponent parent) {
		checkDetached();
		componentDelegate.setParent(parent);
	}

	@Override
	public List<IControl> getChildren() {
		checkDetached();
		return containerDelegate.getChildren();
	}

	@Override
	public boolean remove(final IControl control) {
		checkDetached();
		return containerDelegate.remove(control);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		checkDetached();
		return containerDelegate.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		checkDetached();
		return containerDelegate.add(factory, layoutConstraints);
	}

	@Override
	public void removeAll() {
		checkDetached();
		containerDelegate.removeAll();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		checkDetached();
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

	@Override
	public IPopupMenu createTabPopupMenu() {
		checkDetached();
		return new PopupMenuImpl(getWidget().createTabPopupMenu(), this);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		checkDetached();
		super.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		checkDetached();
		super.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		checkDetached();
		super.layoutEnd();
	}

	@Override
	public void redraw() {
		checkDetached();
		super.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		checkDetached();
		super.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		checkDetached();
		super.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		checkDetached();
		return super.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		checkDetached();
		return super.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		checkDetached();
		super.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		checkDetached();
		super.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		checkDetached();
		return super.isVisible();
	}

	@Override
	public Dimension getSize() {
		checkDetached();
		return super.getSize();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionListeners.add(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionListeners.remove(listener);
	}

	@Override
	public void addTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionListeners.add(listener);
	}

	@Override
	public void removeTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionListeners.remove(listener);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		checkDetached();
		super.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		checkDetached();
		return super.isEnabled();
	}

	private void checkDetached() {
		if (detached) {
			throw new IllegalStateException("The item is detached.");
		}
	}

}

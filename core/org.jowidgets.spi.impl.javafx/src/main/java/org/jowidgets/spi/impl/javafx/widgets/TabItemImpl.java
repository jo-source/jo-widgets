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

import java.util.HashSet;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.controller.PopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TabItemObservableSpi;
import org.jowidgets.spi.impl.javafx.image.JavafxImageRegistry;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.tbee.javafx.scene.layout.MigPane;

public class TabItemImpl extends TabItemObservableSpi implements ITabItemSpi {

	private final JavafxContainer javafxContainer;
	private final PopupDetectionObservable tabPopupDetectionObservable;
	private final HashSet<PopupMenuImpl> tabPopupMenus;
	private final MigPane tabContentContainer;
	private final Tab tab;
	private TabPane parentTabbedPane;
	private boolean detached;
	private String text;
	private String toolTipText;
	private IImageConstant icon;

	public TabItemImpl(final IGenericWidgetFactory widgetFactory, final TabPane uiReference, final boolean tabsCloseable) {
		this(widgetFactory, uiReference, tabsCloseable, null);
	}

	public TabItemImpl(
		final IGenericWidgetFactory genericWidgetFactory,
		final TabPane parentTabbedPane,
		final boolean closeable,
		final Integer index) {

		this.parentTabbedPane = parentTabbedPane;

		this.tabPopupDetectionObservable = new PopupDetectionObservable();
		this.tabPopupMenus = new HashSet<PopupMenuImpl>();

		this.tabContentContainer = new MigPane("", "[]", "[]");
		this.tab = new Tab();
		javafxContainer = new JavafxContainer(genericWidgetFactory, tabContentContainer);
		tab.setContent(tabContentContainer);

		if (index != null) {
			parentTabbedPane.getTabs().add(index.intValue(), tab);
		}
		else {
			parentTabbedPane.getTabs().add(tab);
		}

		tab.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(
				final ObservableValue<? extends Boolean> paramObservableValue,
				final Boolean oldValue,
				final Boolean newValue) {
				if (newValue) {
					fireSelected();
				}
			}
		});

	}

	@Override
	public Pane getUiReference() {
		return javafxContainer.getUiReference();
	}

	@Override
	public Rectangle getClientArea() {
		return javafxContainer.getClientArea();
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		return javafxContainer.computeDecoratedSize(clientAreaSize);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		javafxContainer.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return javafxContainer.isEnabled();
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		javafxContainer.setTabOrder(tabOrder);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return javafxContainer.add(index, descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return javafxContainer.add(index, creator, layoutConstraints);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return javafxContainer.remove(control);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return javafxContainer.createPopupMenu();
	}

	@Override
	public void redraw() {
		javafxContainer.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		javafxContainer.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		javafxContainer.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		javafxContainer.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return javafxContainer.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return javafxContainer.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		javafxContainer.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		javafxContainer.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return javafxContainer.isVisible();
	}

	@Override
	public Dimension getSize() {
		return javafxContainer.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		javafxContainer.setSize(size);
	}

	@Override
	public Position getPosition() {
		return javafxContainer.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		javafxContainer.setPosition(position);
	}

	@Override
	public boolean requestFocus() {
		return javafxContainer.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		javafxContainer.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		javafxContainer.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		javafxContainer.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		javafxContainer.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		javafxContainer.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		javafxContainer.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		javafxContainer.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		javafxContainer.removeComponentListener(componentListener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		javafxContainer.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		javafxContainer.removePopupDetectionListener(listener);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		javafxContainer.setLayout(layoutDescriptor);
		tab.setContent(javafxContainer.getUiReference());
	}

	@Override
	public void layoutBegin() {
		javafxContainer.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		javafxContainer.layoutEnd();
	}

	@Override
	public void removeAll() {
		javafxContainer.removeAll();
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		tab.setText(text);

	}

	@Override
	public void setToolTipText(final String text) {
		this.toolTipText = text;
		final Tooltip tool = new Tooltip(text);
		if (text == null || text.isEmpty()) {
			Tooltip.uninstall(getUiReference(), tool);
		}
		else {
			Tooltip.install(getUiReference(), tool);
		}
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		if (icon != null) {
			tab.setGraphic(JavafxImageRegistry.getInstance().getImage(icon));
		}
		else {
			tab.setGraphic(null);
		}

	}

	@Override
	public boolean isReparentable() {
		return true;
	}

	@Override
	public void addTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionObservable.addPopupDetectionListener(listener);
	}

	@Override
	public void removeTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionObservable.removePopupDetectionListener(listener);
	}

	@Override
	public IPopupMenuSpi createTabPopupMenu() {
		final PopupMenuImpl result = new PopupMenuImpl(tab);
		tabPopupMenus.add(result);
		return result;
	}

	public boolean isDetached() {
		return detached;
	}

	public void detach() {
		detached = true;
		parentTabbedPane.getTabs().remove(tab);
		//TODO DB 
		//parentTabbedPane.removeChangeListener(tabSelectionListener);
	}

	public void attach(final TabPane parentTabbedPane, final boolean closeable, final Integer index) {
		this.detached = false;
		this.parentTabbedPane = parentTabbedPane;

		if (index != null) {
			parentTabbedPane.getTabs().add(index.intValue(), tab);
		}
		else {
			parentTabbedPane.getTabs().add(tab);
		}

		setText(text);
		setToolTipText(toolTipText);
		setIcon(icon);
	}
}

/*
 * Copyright (c) 2010, grossmann, Lukas Gross
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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.IListItemListener;
import org.jowidgets.api.model.IListItemObservable;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IContainerItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IPopupActionItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.model.item.IToolBarItemModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.IToolBarContainerItem;
import org.jowidgets.api.widgets.IToolBarItem;
import org.jowidgets.api.widgets.IToolBarMenu;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ISeparatorToolBarItemDescriptor;
import org.jowidgets.api.widgets.descriptor.IToolBarButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.IToolBarContainerItemDescriptor;
import org.jowidgets.api.widgets.descriptor.IToolBarMenuDescriptor;
import org.jowidgets.api.widgets.descriptor.IToolBarPopupButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.IToolBarToggleButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.IContainerSetup;
import org.jowidgets.api.widgets.descriptor.setup.IItemSetup;
import org.jowidgets.api.widgets.descriptor.setup.IToolBarSetup;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.event.ListItemObservable;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.ToolBarSpiWrapper;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;
import org.jowidgets.spi.widgets.IToolBarItemSpi;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.util.Assert;

public class ToolBarImpl extends ToolBarSpiWrapper implements IToolBar, IListItemObservable {

	private final ControlDelegate controlDelegate;
	private final List<IToolBarItem> children;
	private final IListModelListener listModelListener;
	private final ListItemObservable itemObs;
	private IToolBarModel model;

	public ToolBarImpl(final IToolBarSpi widget, final IToolBarSetup setup) {
		super(widget);
		this.controlDelegate = new ControlDelegate();

		this.children = new LinkedList<IToolBarItem>();

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		this.itemObs = new ListItemObservable();
		this.listModelListener = new ListModelAdapter() {

			@Override
			public void afterChildRemoved(final int index) {
				remove(index);
				pack();
			}

			@Override
			public void afterChildAdded(final int index) {
				final IToolBarItemModel addedModel = getModel().getItems().get(index);
				addMenuModel(index, addedModel);
				pack();
			}
		};

		setModel(Toolkit.getModelFactoryProvider().getItemModelFactory().toolBar());
	}

	@Override
	public void setModel(final IToolBarModel model) {
		if (this.model != null) {
			this.model.removeListModelListener(listModelListener);
			removeAll();
		}
		this.model = model;
		for (final IToolBarItemModel childModel : model.getItems()) {
			addMenuModel(children.size(), childModel);
		}
		model.addListModelListener(listModelListener);
		pack();
	}

	@Override
	public IToolBarModel getModel() {
		return model;
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

	@Override
	public List<IToolBarItem> getChildren() {
		return new LinkedList<IToolBarItem>(children);
	}

	@Override
	public void remove(final int index) {
		final IToolBarItem item = children.get(index);
		if (item instanceof IDisposeable) {
			((IDisposeable) item).dispose();
		}
		children.remove(index);
		getWidget().remove(index);
	}

	@Override
	public boolean remove(final IToolBarItem item) {
		Assert.paramNotNull(item, "item");
		final int index = children.indexOf(item);
		if (index != -1) {
			remove(index);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void removeAll() {
		for (final IToolBarItem item : getChildren()) {
			remove(item);
		}
	}

	@Override
	public IToolBarItem addSeparator() {
		return addSeparator(null);
	}

	@Override
	public IToolBarItem addSeparator(final int index) {
		if (index < 0 || index > children.size()) {
			throw new IllegalArgumentException("Index must be between '0' and '" + children.size() + "'.");
		}
		return addSeparator(Integer.valueOf(index));
	}

	private IToolBarItem addSeparator(final Integer index) {
		if (index != null) {
			return addItem(index.intValue(), Toolkit.getBluePrintFactory().toolBarSeparator());
		}
		else {
			return addItem(Toolkit.getBluePrintFactory().toolBarSeparator());
		}
	}

	@Override
	public IToolBarButton addAction(final int index, final IAction action) {
		final IToolBarButton result = addItem(index, Toolkit.getBluePrintFactory().toolBarButton());
		result.setAction(action);
		return result;
	}

	@Override
	public IToolBarButton addAction(final IAction action) {
		final IToolBarButton result = addItem(Toolkit.getBluePrintFactory().toolBarButton());
		result.setAction(action);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IToolBarItem> WIDGET_TYPE addItem(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		final WIDGET_TYPE result = addItemInternal(null, descriptor);
		addToModel(null, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IToolBarItem> WIDGET_TYPE addItem(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		if (index < 0 || index > children.size()) {
			throw new IllegalArgumentException("Index must be between '0' and '" + children.size() + "'.");
		}
		final WIDGET_TYPE result = addItemInternal(Integer.valueOf(index), descriptor);
		addToModel(index, result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private <WIDGET_TYPE extends IToolBarItem> WIDGET_TYPE addItemInternal(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		Assert.paramNotNull(descriptor, "descriptor");

		WIDGET_TYPE result = null;

		if (IToolBarButtonDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IToolBarButtonSpi toolBarButtonSpi = getWidget().addToolBarButton(index);
			final IToolBarButton toolBarButton = new ToolBarButtonImpl(this, toolBarButtonSpi, (IItemSetup) descriptor);
			result = (WIDGET_TYPE) toolBarButton;
		}
		else if (IToolBarToggleButtonDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IToolBarToggleButtonSpi toolBarToggleButtonSpi = getWidget().addToolBarToggleButton(index);
			final IToolBarToggleButton toolBarToggleButton = new ToolBarToggleButtonImpl(
				this,
				toolBarToggleButtonSpi,
				(IItemSetup) descriptor);
			result = (WIDGET_TYPE) toolBarToggleButton;
		}
		else if (IToolBarPopupButtonDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IToolBarPopupButtonSpi toolBarPopupSpi = getWidget().addToolBarPopupButton(index);
			final IToolBarPopupButton toolBarPopupButton = new ToolBarPopupButtonImpl(
				this,
				toolBarPopupSpi,
				(IItemSetup) descriptor);
			result = (WIDGET_TYPE) toolBarPopupButton;
		}
		else if (IToolBarMenuDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IToolBarButtonSpi toolBarBurronSpi = getWidget().addToolBarButton(index);
			final IToolBarMenu toolBarMenu = new ToolBarMenuImpl(this, toolBarBurronSpi, (IItemSetup) descriptor);
			result = (WIDGET_TYPE) toolBarMenu;
		}
		else if (IToolBarContainerItemDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IToolBarContainerItemSpi toolBarContainerItemSpi = getWidget().addToolBarContainer(index);
			final IToolBarContainerItem toolBarContainerItem = new ToolBarContainerItemImpl(
				this,
				toolBarContainerItemSpi,
				(IContainerSetup) descriptor);
			result = (WIDGET_TYPE) toolBarContainerItem;
		}
		else if (ISeparatorToolBarItemDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IToolBarItemSpi toolBarSeparatorItemSpi = getWidget().addSeparator(index);
			final ToolBarSeparatorItemImpl toolBarSeparatorItem = new ToolBarSeparatorItemImpl(this, toolBarSeparatorItemSpi);
			result = (WIDGET_TYPE) toolBarSeparatorItem;
		}
		else {
			throw new IllegalArgumentException("Descriptor with type '" + descriptor.getClass().getName() + "' is not supported");
		}

		addToChildren(index, result);
		itemObs.fireItemAdded(result);
		return result;
	}

	private void addMenuModel(final int index, final IToolBarItemModel model) {
		Assert.paramNotNull(model, "model");
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		if (model instanceof IActionItemModel) {
			addItemInternal(index, bpf.toolBarButton()).setModel(model);
		}
		else if (model instanceof ICheckedItemModel) {
			addItemInternal(index, bpf.toolBarToggleButton()).setModel(model);
		}
		else if (model instanceof IPopupActionItemModel) {
			addItemInternal(index, bpf.toolBarPopupButton()).setModel(model);
		}
		else if (model instanceof IContainerItemModel) {
			addItemInternal(index, bpf.toolBarContainerItem()).setModel(model);
		}
		else if (model instanceof ISeparatorItemModel) {
			addItemInternal(index, bpf.toolBarSeparator()).setModel(model);
		}
		else if (model instanceof IMenuModel) {
			addItemInternal(index, bpf.toolBarMenu()).setModel(model);
		}
		else {
			throw new IllegalArgumentException("Model of type '" + model.getClass().getName() + "' is not supported.");
		}
	}

	private void addToChildren(final Integer index, final IToolBarItem item) {
		if (index != null) {
			children.add(index.intValue(), item);
		}
		else {
			children.add(item);
		}
	}

	private void addToModel(final Integer index, final IToolBarItem item) {
		model.removeListModelListener(listModelListener);
		if (index != null) {
			getModel().addItem(index.intValue(), item.getModel());
		}
		else {
			getModel().addItem(item.getModel());
		}
		model.addListModelListener(listModelListener);
	}

	@Override
	public void addItemContainerListener(final IListItemListener listener) {
		itemObs.addItemContainerListener(listener);
	}

	@Override
	public void removeItemContainerListener(final IListItemListener listener) {
		itemObs.removeItemContainerListener(listener);
	}
}

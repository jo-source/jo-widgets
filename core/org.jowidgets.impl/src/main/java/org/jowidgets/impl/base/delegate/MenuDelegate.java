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

package org.jowidgets.impl.base.delegate;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.ISelectableMenuItem;
import org.jowidgets.api.widgets.ISubMenu;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.IActionMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckedMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.IRadioMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.ISubMenuDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.IAccelerateableMenuItemSetup;
import org.jowidgets.api.widgets.descriptor.setup.IMenuItemSetup;
import org.jowidgets.api.widgets.descriptor.setup.ISelectableItemSetup;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.impl.model.item.CheckedItemModelBuilder;
import org.jowidgets.impl.model.item.RadioItemModelBuilder;
import org.jowidgets.impl.widgets.basic.ActionMenuItemImpl;
import org.jowidgets.impl.widgets.basic.IDisposeable;
import org.jowidgets.impl.widgets.basic.SelectableMenuItemImpl;
import org.jowidgets.impl.widgets.basic.SeparatorMenuItemImpl;
import org.jowidgets.impl.widgets.basic.SubMenuImpl;
import org.jowidgets.impl.widgets.common.wrapper.invoker.SelectableMenuItemSpiInvoker;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuSpi;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;
import org.jowidgets.spi.widgets.ISubMenuSpi;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.util.Assert;

public class MenuDelegate implements IDisposeable {

	private final IMenuSpi menuSpi;
	private final IMenu menu;
	private final List<IMenuItem> children;
	private final IListModelListener listModelListener;

	private IMenuModel model;

	public MenuDelegate(final IMenu menu, final IMenuSpi menuSpi, final IMenuModel model) {
		Assert.paramNotNull(menu, "menu");
		Assert.paramNotNull(menuSpi, "menuSpi");

		this.children = new LinkedList<IMenuItem>();
		this.menu = menu;
		this.menuSpi = menuSpi;

		this.listModelListener = new ListModelAdapter() {

			@Override
			public void afterChildRemoved(final int index) {
				removeItem(index);
			}

			@Override
			public void afterChildAdded(final int index) {
				final IMenuItemModel addedModel = getModel().getChildren().get(index);
				addChild(index, addedModel);
			}
		};

		setModel(model);
	}

	public IMenuItem addSeparator() {
		final IMenuItem result = new SeparatorMenuItemImpl(menu, menuSpi.addSeparator(null));
		children.add(result);
		addToModel(getModel().getChildren().size(), result);
		return result;
	}

	public IMenuItem addSeparator(final int index) {
		final IMenuItem result = new SeparatorMenuItemImpl(menu, menuSpi.addSeparator(index));
		children.add(index, result);
		addToModel(index, result);
		return result;
	}

	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItem(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		final WIDGET_TYPE result = addMenuItemInternal(null, descriptor);
		addToModel(getModel().getChildren().size(), result);
		return result;
	}

	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItem(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		if (index < 0 || index > children.size()) {
			throw new IllegalArgumentException("Index must be between '0' and '" + children.size() + "'.");
		}

		final WIDGET_TYPE result = addMenuItemInternal(Integer.valueOf(index), descriptor);
		addToModel(index, result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItemInternal(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		Assert.paramNotNull(descriptor, "descriptor");

		WIDGET_TYPE result = null;

		if (ISubMenuDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final ISubMenuSpi subMenuSpi = menuSpi.addSubMenu(index);
			final ISubMenu subMenuItem = new SubMenuImpl(subMenuSpi, menu, (IMenuItemSetup) descriptor);
			result = (WIDGET_TYPE) subMenuItem;
		}
		else if (IActionMenuItemDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IActionMenuItemSpi actionMenuItemSpi = menuSpi.addActionItem(index);
			final IActionMenuItem actionMenuItem = new ActionMenuItemImpl(
				menu,
				actionMenuItemSpi,
				(IAccelerateableMenuItemSetup) descriptor);
			result = (WIDGET_TYPE) actionMenuItem;
		}
		else if (ICheckedMenuItemDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final ISelectableMenuItemSpi selectableMenuItemSpi = menuSpi.addCheckedItem(index);
			final ISelectableMenuItem selectableMenuItem = new SelectableMenuItemImpl(
				menu,
				selectableMenuItemSpi,
				(ISelectableItemSetup) descriptor,
				new SelectableItemDelegate(
					new SelectableMenuItemSpiInvoker(selectableMenuItemSpi),
					new CheckedItemModelBuilder().build()));
			result = (WIDGET_TYPE) selectableMenuItem;
		}
		else if (IRadioMenuItemDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final ISelectableMenuItemSpi selectableMenuItemSpi = menuSpi.addRadioItem(index);
			final ISelectableMenuItem selectableMenuItem = new SelectableMenuItemImpl(
				menu,
				selectableMenuItemSpi,
				(ISelectableItemSetup) descriptor,
				new SelectableItemDelegate(
					new SelectableMenuItemSpiInvoker(selectableMenuItemSpi),
					new RadioItemModelBuilder().build()));
			result = (WIDGET_TYPE) selectableMenuItem;
		}
		else if (ISeparatorMenuItemDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IMenuItemSpi separatorMenuItemSpi = menuSpi.addSeparator(index);
			final IMenuItem separatorMenuItem = new SeparatorMenuItemImpl(menu, separatorMenuItemSpi);
			result = (WIDGET_TYPE) separatorMenuItem;
		}
		else {
			throw new IllegalArgumentException("Descriptor with type '"
				+ descriptor.getDescriptorInterface().getName()
				+ "' is not supported");
		}

		addToChildren(index, result);

		return result;
	}

	private void addToChildren(final Integer index, final IMenuItem menuItem) {
		if (index != null) {
			children.add(index.intValue(), menuItem);
		}
		else {
			children.add(menuItem);
		}
	}

	private void addToModel(final Integer index, final IMenuItem menuItem) {
		model.removeListModelListener(listModelListener);
		if (index != null) {
			getModel().addItem(index.intValue(), menuItem.getModel());
		}
		else {
			getModel().addItem(menuItem.getModel());
		}
		model.addListModelListener(listModelListener);
	}

	private void addChild(final int index, final IMenuItemModel model) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		if (model instanceof IRadioItemModel) {
			addMenuItemInternal(index, bpf.radioMenuItem()).setModel(model);
		}
		else if (model instanceof ICheckedItemModel) {
			addMenuItemInternal(index, bpf.checkedMenuItem()).setModel(model);
		}
		else if (model instanceof IActionItemModel) {
			addMenuItemInternal(index, bpf.menuItem()).setModel(model);
		}
		else if (model instanceof IMenuModel) {
			addMenuItemInternal(index, bpf.subMenu()).setModel(model);
		}
		else if (model instanceof ISeparatorItemModel) {
			addMenuItemInternal(index, bpf.menuSeparator()).setModel(model);
		}
	}

	public List<IMenuItem> getChildren() {
		return new LinkedList<IMenuItem>(children);
	}

	public boolean remove(final IMenuItem item) {
		Assert.paramNotNull(item, "item");

		final int index = children.indexOf(item);
		if (index != -1) {
			removeItem(index);
			return true;
		}
		else {
			return false;
		}
	}

	public void removeItem(final int index) {
		final IMenuItem item = children.get(index);
		if (item instanceof IDisposeable) {
			((IDisposeable) item).dispose();
		}
		children.remove(index);
		menuSpi.remove(index);
	}

	public void removeAll() {
		for (final IMenuItem item : getChildren()) {
			remove(item);
		}
	}

	public IActionMenuItem addAction(final int index, final IAction action) {
		final IActionMenuItem result = menu.addItem(index, Toolkit.getBluePrintFactory().menuItem());
		result.setAction(action);
		return result;
	}

	public IActionMenuItem addAction(final IAction action) {
		final IActionMenuItem result = menu.addItem(Toolkit.getBluePrintFactory().menuItem());
		result.setAction(action);
		return result;
	}

	public IMenuModel getModel() {
		return model;
	}

	public void setModel(final IMenuModel model) {
		if (this.model != null) {
			this.model.removeListModelListener(listModelListener);
			removeAll();
		}
		this.model = model;
		for (final IMenuItemModel childModel : model.getChildren()) {
			addChild(children.size(), childModel);
		}

		model.addListModelListener(listModelListener);
	}

	@Override
	public void dispose() {
		this.model.removeListModelListener(listModelListener);
		removeAll();
	}

}

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

package org.jowidgets.impl.model.item;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelBuilder;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IDecorator;

class MenuModelImpl extends ItemModelImpl implements IMenuModel {

	private final ListModelDelegate listModelDelegate;
	private final Set<IMenuModel> boundModels;
	private final List<IDecorator<IAction>> decorators;

	protected MenuModelImpl() {
		this(null, null, null, null, null, null, true);
	}

	protected MenuModelImpl(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final Accelerator accelerator,
		final Character mnemonic,
		final boolean enabled) {
		super(id, text, toolTipText, icon, accelerator, mnemonic, enabled);

		this.listModelDelegate = new ListModelDelegate();
		this.boundModels = new HashSet<IMenuModel>();
		this.decorators = new LinkedList<IDecorator<IAction>>();

		this.addListModelListener(new IListModelListener() {

			@Override
			public void childRemoved(final int index) {
				for (final IMenuModel boundModel : boundModels) {
					boundModel.removeItem(index);
				}
			}

			@Override
			public void childAdded(final int index) {
				for (final IMenuModel boundModel : boundModels) {
					boundModel.addItem(getChildren().get(index));
				}
			}
		});

		this.addListModelListener(new IListModelListener() {
			@Override
			public void childRemoved(final int index) {
				final IMenuItemModel itemModel = getChildren().get(index);
				if (itemModel instanceof IActionItemModel || itemModel instanceof IMenuModel) {
					for (final IDecorator<IAction> decorator : decorators) {
						removeDecorator(itemModel, decorator);
					}
				}
			}

			@Override
			public void childAdded(final int index) {
				final IMenuItemModel itemModel = getChildren().get(index);
				if (itemModel instanceof IActionItemModel || itemModel instanceof IMenuModel) {
					for (final IDecorator<IAction> decorator : decorators) {
						addDecorator(itemModel, decorator);
					}
				}
			}
		});
	}

	@Override
	public void addDecorator(final IDecorator<IAction> decorator) {
		Assert.paramNotNull(decorator, "decorator");
		decorators.add(decorator);
		for (final IMenuItemModel itemModel : getChildren()) {
			addDecorator(itemModel, decorator);
		}
	}

	@Override
	public void removeDecorator(final IDecorator<IAction> decorator) {
		Assert.paramNotNull(decorator, "decorator");
		decorators.remove(decorator);
		for (final IMenuItemModel itemModel : getChildren()) {
			removeDecorator(itemModel, decorator);
		}
	}

	private void addDecorator(final IMenuItemModel itemModel, final IDecorator<IAction> decorator) {
		if (itemModel instanceof IActionItemModel) {
			((IActionItemModel) itemModel).addDecorator(decorator);
		}
		else if (itemModel instanceof IMenuModel) {
			((IMenuModel) itemModel).addDecorator(decorator);
		}
	}

	private void removeDecorator(final IMenuItemModel itemModel, final IDecorator<IAction> decorator) {
		if (itemModel instanceof IActionItemModel) {
			((IActionItemModel) itemModel).removeDecorator(decorator);
		}
		else if (itemModel instanceof IMenuModel) {
			((IMenuModel) itemModel).removeDecorator(decorator);
		}
	}

	@Override
	public IMenuModel createCopy() {
		final MenuModelImpl result = new MenuModelImpl();
		result.setContent(this);
		return result;
	}

	@Override
	public void bind(final IMenuModel model) {
		Assert.paramNotNull(model, "model");
		model.removeAllItems();
		model.addItemsOfModel(this);
		boundModels.add(model);
	}

	@Override
	public void unbind(final IMenuModel model) {
		Assert.paramNotNull(model, "model");
		boundModels.remove(model);
	}

	protected void setContent(final IMenuModel source) {
		super.setContent(source);
		listModelDelegate.setContent(source);
	}

	@Override
	public final void addListModelListener(final IListModelListener listener) {
		listModelDelegate.addListModelListener(listener);
	}

	@Override
	public final void removeListModelListener(final IListModelListener listener) {
		listModelDelegate.removeListModelListener(listener);
	}

	@Override
	public IActionItemModel addAction(final IAction action) {
		return listModelDelegate.addAction(action);
	}

	@Override
	public IActionItemModel addAction(final int index, final IAction action) {
		return listModelDelegate.addAction(index, action);
	}

	@Override
	public IActionItemModel addActionItem() {
		return listModelDelegate.addActionItem();
	}

	@Override
	public IActionItemModel addActionItem(final String text) {
		return listModelDelegate.addActionItem(text);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText) {
		return listModelDelegate.addActionItem(text, toolTipText);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final IImageConstant icon) {
		return listModelDelegate.addActionItem(text, icon);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText, final IImageConstant icon) {
		return listModelDelegate.addActionItem(text, toolTipText, icon);
	}

	@Override
	public ICheckedItemModel addCheckedItem() {
		return listModelDelegate.addCheckedItem();
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text) {
		return listModelDelegate.addCheckedItem(text);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText) {
		return listModelDelegate.addCheckedItem(text, toolTipText);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final IImageConstant icon) {
		return listModelDelegate.addCheckedItem(text, icon);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText, final IImageConstant icon) {
		return listModelDelegate.addCheckedItem(text, toolTipText, icon);
	}

	@Override
	public IRadioItemModel addRadioItem() {
		return listModelDelegate.addRadioItem();
	}

	@Override
	public IRadioItemModel addRadioItem(final String text) {
		return listModelDelegate.addRadioItem(text);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText) {
		return listModelDelegate.addRadioItem(text, toolTipText);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final IImageConstant icon) {
		return listModelDelegate.addRadioItem(text, icon);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText, final IImageConstant icon) {
		return listModelDelegate.addRadioItem(text, toolTipText, icon);
	}

	@Override
	public IMenuModel addMenu() {
		return listModelDelegate.addMenu();
	}

	@Override
	public IMenuModel addMenu(final String text) {
		return listModelDelegate.addMenu(text);
	}

	@Override
	public IMenuModel addMenu(final String text, final String toolTipText) {
		return listModelDelegate.addMenu(text, toolTipText);
	}

	@Override
	public IMenuModel addMenu(final String text, final IImageConstant icon) {
		return listModelDelegate.addMenu(text, icon);
	}

	@Override
	public IMenuModel addMenu(final String text, final String toolTipText, final IImageConstant icon) {
		return listModelDelegate.addMenu(text, toolTipText, icon);
	}

	@Override
	public ISeparatorItemModel addSeparator() {
		return listModelDelegate.addSeparator();
	}

	@Override
	public ISeparatorItemModel addSeparator(final int index) {
		return listModelDelegate.addSeparator(index);
	}

	@Override
	public ISeparatorItemModel addSeparator(final String id) {
		return listModelDelegate.addSeparator(id);
	}

	@Override
	public void addBefore(final IMenuItemModel newItem, final String... idPath) {
		listModelDelegate.addBefore(newItem, idPath);
	}

	@Override
	public void addAfter(final IMenuItemModel newItem, final String... idPath) {
		listModelDelegate.addAfter(newItem, idPath);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final BUILDER_TYPE itemBuilder) {
		return listModelDelegate.addItem(itemBuilder);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final int index,
		final BUILDER_TYPE itemBuilder) {
		return listModelDelegate.addItem(index, itemBuilder);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel> MODEL_TYPE addItem(final MODEL_TYPE item) {
		return listModelDelegate.addItem(item);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		return listModelDelegate.addItem(index, item);
	}

	@Override
	public void addItemsOfModel(final IMenuModel menuModel) {
		Assert.paramNotNull(menuModel, "menuModel");
		for (final IMenuItemModel child : menuModel.getChildren()) {
			addItem(child);
		}
	}

	protected <MODEL_TYPE extends IItemModel> MODEL_TYPE addItemImpl(final int index, final MODEL_TYPE item) {
		return listModelDelegate.addItem(index, item);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public final List<IMenuItemModel> getChildren() {
		return new LinkedList(listModelDelegate.getChildren());
	}

	@Override
	public IMenuItemModel findItemByPath(final String... idPath) {
		return (IMenuItemModel) listModelDelegate.findItemByPath(idPath);
	}

	@Override
	public void removeItem(final IMenuItemModel item) {
		listModelDelegate.removeItem(item);
	}

	@Override
	public void removeItem(final int index) {
		listModelDelegate.removeItem(index);
	}

	@Override
	public void removeAllItems() {
		listModelDelegate.removeAllItems();
	}
}

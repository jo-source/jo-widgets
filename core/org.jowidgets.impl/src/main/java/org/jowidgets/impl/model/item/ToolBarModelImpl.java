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
import org.jowidgets.api.model.item.IContainerContentCreator;
import org.jowidgets.api.model.item.IContainerItemModel;
import org.jowidgets.api.model.item.IItemModelBuilder;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IPopupActionItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.model.item.IToolBarItemModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;

class ToolBarModelImpl implements IToolBarModel {

	private final ListModelDelegate listModelDelegate;
	private final Set<IToolBarModel> boundModels;

	protected ToolBarModelImpl() {
		super();
		this.listModelDelegate = new ListModelDelegate();
		this.boundModels = new HashSet<IToolBarModel>();

		this.addListModelListener(new IListModelListener() {

			@Override
			public void childRemoved(final int index) {
				for (final IToolBarModel boundModel : boundModels) {
					boundModel.removeItem(index);
				}
			}

			@Override
			public void childAdded(final int index) {
				for (final IToolBarModel boundModel : boundModels) {
					boundModel.addItem(getItems().get(index));
				}
			}
		});
	}

	@Override
	public ToolBarModelImpl createCopy() {
		final ToolBarModelImpl result = new ToolBarModelImpl();
		result.setContent(this);
		return result;
	}

	@Override
	public void bind(final IToolBarModel model) {
		Assert.paramNotNull(model, "model");
		model.removeAllItems();
		model.addItemsOfModel(this);
		boundModels.add(model);
	}

	@Override
	public void unbind(final IToolBarModel model) {
		Assert.paramNotNull(model, "model");
		boundModels.remove(model);
	}

	@Override
	public void addItemsOfModel(final IToolBarModel model) {
		Assert.paramNotNull(model, "model");
		for (final IToolBarItemModel item : model.getItems()) {
			addItem(item);
		}
	}

	protected void setContent(final IToolBarModel source) {
		listModelDelegate.setContent(source);
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		listModelDelegate.addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		listModelDelegate.removeListModelListener(listener);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel> MODEL_TYPE addItem(final MODEL_TYPE item) {
		return listModelDelegate.addItem(item);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		return listModelDelegate.addItem(index, item);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final BUILDER_TYPE itemBuilder) {
		return listModelDelegate.addItem(itemBuilder);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final int index,
		final BUILDER_TYPE itemBuilder) {
		return listModelDelegate.addItem(index, itemBuilder);
	}

	@Override
	public void addAfter(final IToolBarItemModel newItem, final String id) {
		listModelDelegate.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final IToolBarItemModel newItem, final String id) {
		listModelDelegate.addBefore(newItem, id);
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
	public IPopupActionItemModel addPopupAction(final IAction action, final IMenuModel popupMenu) {
		return addItem(new PopupActionItemModelBuilder().setAction(action).setPopupMenu(popupMenu));
	}

	@Override
	public IPopupActionItemModel addPopupAction(final int index, final IAction action, final IMenuModel popupMenu) {
		return addItem(index, new PopupActionItemModelBuilder().setAction(action).setPopupMenu(popupMenu));
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
	public IActionItemModel addActionItem(final IImageConstant icon, final String toolTipText) {
		return listModelDelegate.addActionItem(icon, toolTipText);
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
	public ICheckedItemModel addCheckedItem(final IImageConstant icon, final String toolTipText) {
		return listModelDelegate.addCheckedItem(icon, toolTipText);
	}

	@Override
	public ISeparatorItemModel addSeparator() {
		return listModelDelegate.addSeparator();
	}

	@Override
	public ISeparatorItemModel addSeparator(final String id) {
		return listModelDelegate.addSeparator(id);
	}

	@Override
	public ISeparatorItemModel addSeparator(final int index) {
		return listModelDelegate.addSeparator(index);
	}

	@Override
	public void removeItem(final IToolBarItemModel item) {
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

	@Override
	public void removeItemsOfModel(final IToolBarModel toolBarModel) {
		Assert.paramNotNull(toolBarModel, "toolBarModel");
		for (final IToolBarItemModel childModel : toolBarModel.getItems()) {
			removeItem(childModel);
		}
	}

	@Override
	public IPopupActionItemModel addPopupActionItem() {
		return addItem(new PopupActionItemModelBuilder());
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text) {
		return addItem(new PopupActionItemModelBuilder().setText(text));
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text, final String toolTipText) {
		return addItem(new PopupActionItemModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text, final IImageConstant icon) {
		return addItem(new PopupActionItemModelBuilder().setText(text).setIcon(icon));
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new PopupActionItemModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final IImageConstant icon, final String toolTipText) {
		return addItem(new PopupActionItemModelBuilder().setToolTipText(toolTipText).setIcon(icon));
	}

	@Override
	public IContainerItemModel addContainer() {
		return addItem(new ContainerItemModelBuilder());
	}

	@Override
	public IContainerItemModel addContainer(final IContainerContentCreator contentCreator) {
		return addItem(new ContainerItemModelBuilder().setContentCreator(contentCreator));
	}

	@Override
	public IToolBarItemModel findItemById(final String id) {
		return (IToolBarItemModel) listModelDelegate.findItemByPath(id);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public List<IToolBarItemModel> getItems() {
		return new LinkedList(listModelDelegate.getChildren());
	}

}

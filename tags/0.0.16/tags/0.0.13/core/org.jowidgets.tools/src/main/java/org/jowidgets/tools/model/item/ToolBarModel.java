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

package org.jowidgets.tools.model.item;

import java.util.List;

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
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;

public class ToolBarModel implements IToolBarModel {

	private final IToolBarModel model;

	public ToolBarModel() {
		this(createInstance());
	}

	private ToolBarModel(final IToolBarModel model) {
		Assert.paramNotNull(model, "model");
		this.model = model;
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		model.addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		model.removeListModelListener(listener);
	}

	@Override
	public void addItemsOfModel(final IToolBarModel model) {
		this.model.addItemsOfModel(model);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel> MODEL_TYPE addItem(final MODEL_TYPE item) {
		return model.addItem(item);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		return model.addItem(index, item);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final BUILDER_TYPE itemBuilder) {
		return model.addItem(itemBuilder);
	}

	@Override
	public <MODEL_TYPE extends IToolBarItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final int index,
		final BUILDER_TYPE itemBuilder) {
		return model.addItem(index, itemBuilder);
	}

	@Override
	public void addAfter(final IToolBarItemModel newItem, final String id) {
		model.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final IToolBarItemModel newItem, final String id) {
		model.addBefore(newItem, id);
	}

	@Override
	public IActionItemModel addAction(final IAction action) {
		return model.addAction(action);
	}

	@Override
	public IActionItemModel addAction(final int index, final IAction action) {
		return model.addAction(index, action);
	}

	@Override
	public IActionItemModel addActionItem() {
		return model.addActionItem();
	}

	@Override
	public IActionItemModel addActionItem(final String text) {
		return model.addActionItem(text);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText) {
		return model.addActionItem(text, toolTipText);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final IImageConstant icon) {
		return model.addActionItem(text, icon);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText, final IImageConstant icon) {
		return model.addActionItem(text, toolTipText, icon);
	}

	@Override
	public IPopupActionItemModel addPopupActionItem() {
		return model.addPopupActionItem();
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text) {
		return model.addPopupActionItem(text);
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text, final String toolTipText) {
		return model.addPopupActionItem(text, toolTipText);
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text, final IImageConstant icon) {
		return model.addPopupActionItem(text, icon);
	}

	@Override
	public IPopupActionItemModel addPopupActionItem(final String text, final String toolTipText, final IImageConstant icon) {
		return model.addPopupActionItem(text, toolTipText, icon);
	}

	@Override
	public IContainerItemModel addContainer() {
		return model.addContainer();
	}

	@Override
	public IPopupActionItemModel addPopupAction(final IAction action, final IMenuModel popupMenu) {
		return model.addPopupAction(action, popupMenu);
	}

	@Override
	public IPopupActionItemModel addPopupAction(final int index, final IAction action, final IMenuModel popupMenu) {
		return model.addPopupAction(index, action, popupMenu);
	}

	@Override
	public IContainerItemModel addContainer(final IContainerContentCreator contentCreator) {
		return model.addContainer(contentCreator);
	}

	@Override
	public ICheckedItemModel addCheckedItem() {
		return model.addCheckedItem();
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text) {
		return model.addCheckedItem(text);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText) {
		return model.addCheckedItem(text, toolTipText);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final IImageConstant icon) {
		return model.addCheckedItem(text, icon);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText, final IImageConstant icon) {
		return model.addCheckedItem(text, toolTipText, icon);
	}

	@Override
	public ISeparatorItemModel addSeparator() {
		return model.addSeparator();
	}

	@Override
	public ISeparatorItemModel addSeparator(final String id) {
		return model.addSeparator(id);
	}

	@Override
	public ISeparatorItemModel addSeparator(final int index) {
		return model.addSeparator(index);
	}

	@Override
	public void removeItem(final IToolBarItemModel item) {
		model.removeItem(item);
	}

	@Override
	public void removeItem(final int index) {
		model.removeItem(index);
	}

	@Override
	public void removeAllItems() {
		model.removeAllItems();
	}

	@Override
	public void removeItemsOfModel(final IToolBarModel toolBarModel) {
		model.removeItemsOfModel(toolBarModel);
	}

	@Override
	public IToolBarItemModel findItemById(final String id) {
		return model.findItemById(id);
	}

	@Override
	public List<IToolBarItemModel> getItems() {
		return model.getItems();
	}

	@Override
	public IToolBarModel createCopy() {
		return model.createCopy();
	}

	private static IToolBarModel createInstance() {
		return Toolkit.getModelFactoryProvider().getItemModelFactory().toolBar();
	}

}

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
import org.jowidgets.api.model.item.IItemModelBuilder;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IMenuModelBuilder;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;

public class MenuModel extends AbstractItemModelWrapper implements IMenuModel {

	public MenuModel() {
		this(builder());
	}

	public MenuModel(final String text) {
		this(builder().setText(text));
	}

	public MenuModel(final String text, final IImageConstant icon) {
		this(builder(text, icon));
	}

	public MenuModel(final String text, final String toolTipText) {
		this(builder(text, toolTipText));
	}

	public MenuModel(final String text, final String toolTipText, final IImageConstant icon) {
		this(builder(text, toolTipText, icon));
	}

	public MenuModel(final String id, final String text, final String toolTipText, final IImageConstant icon) {
		this(builder(id, text, toolTipText, icon));
	}

	public MenuModel(final IMenuModelBuilder builder) {
		super(builder.build());
	}

	@Override
	protected IMenuModel getItemModel() {
		return (IMenuModel) super.getItemModel();
	}

	@Override
	public List<IMenuItemModel> getChildren() {
		return getItemModel().getChildren();
	}

	@Override
	public IMenuItemModel findItemByPath(final String... idPath) {
		return getItemModel().findItemByPath(idPath);
	}

	@Override
	public IActionItemModel addAction(final IAction action) {
		return getItemModel().addAction(action);
	}

	@Override
	public IActionItemModel addAction(final int index, final IAction action) {
		return getItemModel().addAction(index, action);
	}

	@Override
	public IMenuModel addMenu() {
		return getItemModel().addMenu();
	}

	@Override
	public ISeparatorItemModel addSeparator() {
		return getItemModel().addSeparator();
	}

	@Override
	public ISeparatorItemModel addSeparator(final int index) {
		return getItemModel().addSeparator();
	}

	@Override
	public void addBefore(final IMenuItemModel newItem, final String... idPath) {
		getItemModel().addBefore(newItem, idPath);
	}

	@Override
	public void addAfter(final IMenuItemModel newItem, final String... idPath) {
		getItemModel().addAfter(newItem, idPath);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel> MODEL_TYPE addItem(final MODEL_TYPE item) {
		return getItemModel().addItem(item);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		return getItemModel().addItem(index, item);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final BUILDER_TYPE itemBuilder) {
		return getItemModel().addItem(itemBuilder);
	}

	@Override
	public <MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final int index,
		final BUILDER_TYPE itemBuilder) {
		return getItemModel().addItem(index, itemBuilder);
	}

	@Override
	public IActionItemModel addActionItem() {
		return getItemModel().addActionItem();
	}

	@Override
	public IActionItemModel addActionItem(final String text) {
		return getItemModel().addActionItem(text);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText) {
		return getItemModel().addActionItem(text, toolTipText);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final IImageConstant icon) {
		return getItemModel().addActionItem(text, icon);
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText, final IImageConstant icon) {
		return getItemModel().addActionItem(text, toolTipText, icon);
	}

	@Override
	public ICheckedItemModel addCheckedItem() {
		return getItemModel().addCheckedItem();
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text) {
		return getItemModel().addCheckedItem(text);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText) {
		return getItemModel().addCheckedItem(text, toolTipText);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final IImageConstant icon) {
		return getItemModel().addCheckedItem(text, icon);
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText, final IImageConstant icon) {
		return getItemModel().addCheckedItem(text, toolTipText, icon);
	}

	@Override
	public IRadioItemModel addRadioItem() {
		return getItemModel().addRadioItem();
	}

	@Override
	public IRadioItemModel addRadioItem(final String text) {
		return getItemModel().addRadioItem(text);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText) {
		return getItemModel().addRadioItem(text, toolTipText);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final IImageConstant icon) {
		return getItemModel().addRadioItem(text, icon);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText, final IImageConstant icon) {
		return getItemModel().addRadioItem(text, toolTipText, icon);
	}

	@Override
	public ISeparatorItemModel addSeparator(final String id) {
		return getItemModel().addSeparator(id);
	}

	@Override
	public IMenuModel addMenu(final String text) {
		return getItemModel().addMenu(text);
	}

	@Override
	public IMenuModel addMenu(final String text, final String toolTipText) {
		return getItemModel().addMenu(text, toolTipText);
	}

	@Override
	public IMenuModel addMenu(final String text, final IImageConstant icon) {
		return getItemModel().addMenu(text, icon);
	}

	@Override
	public IMenuModel addMenu(final String text, final String toolTipText, final IImageConstant icon) {
		return getItemModel().addMenu(text, toolTipText, icon);
	}

	@Override
	public void addItemsOfModel(final IMenuModel menuModel) {
		getItemModel().addItemsOfModel(menuModel);
	}

	@Override
	public void bind(final IMenuModel model) {
		getItemModel().bind(model);
	}

	@Override
	public void unbind(final IMenuModel model) {
		getItemModel().unbind(model);
	}

	@Override
	public void removeItem(final IMenuItemModel item) {
		getItemModel().removeItem(item);
	}

	@Override
	public void removeItem(final int index) {
		getItemModel().removeItem(index);
	}

	@Override
	public void removeAllItems() {
		getItemModel().removeAllItems();
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		getItemModel().addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		getItemModel().removeListModelListener(listener);
	}

	@Override
	public IMenuModel createCopy() {
		return getItemModel().createCopy();
	}

	public static IMenuModelBuilder builder() {
		return Toolkit.getModelFactoryProvider().getItemModelFactory().menuBuilder();
	}

	public static IMenuModelBuilder builder(final String text) {
		return builder().setText(text);
	}

	public static IMenuModelBuilder builder(final String text, final String toolTipText) {
		return builder(text).setToolTipText(toolTipText);
	}

	public static IMenuModelBuilder builder(final String text, final IImageConstant icon) {
		return builder().setText(text).setIcon(icon);
	}

	public static IMenuModelBuilder builder(final String text, final String toolTipText, final IImageConstant icon) {
		return builder(text, toolTipText).setIcon(icon);
	}

	public static IMenuModelBuilder builder(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon) {
		return builder(text, toolTipText, icon).setId(id);
	}

}

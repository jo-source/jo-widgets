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

import java.util.LinkedList;
import java.util.List;

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

class MenuModelImpl extends ItemModelImpl implements IMenuModel {

	private final ListModelDelegate listModelDelegate;

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
	}

	@Override
	public IMenuModel createCopy() {
		final MenuModelImpl result = new MenuModelImpl();
		result.setContent(this);
		return result;
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

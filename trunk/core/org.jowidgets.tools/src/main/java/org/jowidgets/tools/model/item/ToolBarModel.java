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
import org.jowidgets.api.model.item.IActionItemModelBuilder;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.ICheckedItemModelBuilder;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.IRadioItemModelBuilder;
import org.jowidgets.api.model.item.ISeparatorItemModel;
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
	public IActionItemModel addItem(final IActionItemModel item) {
		return model.addItem(item);
	}

	@Override
	public IActionItemModel addItem(final int index, final IActionItemModel item) {
		return model.addItem(index, item);
	}

	@Override
	public IActionItemModel addItem(final IActionItemModelBuilder itemBuilder) {
		return model.addItem(itemBuilder);
	}

	@Override
	public IActionItemModel addItem(final int index, final IActionItemModelBuilder itemBuilder) {
		return model.addItem(index, itemBuilder);
	}

	@Override
	public IRadioItemModel addItem(final IRadioItemModel item) {
		return model.addItem(item);
	}

	@Override
	public IRadioItemModel addItem(final int index, final IRadioItemModel item) {
		return model.addItem(index, item);
	}

	@Override
	public IRadioItemModel addItem(final IRadioItemModelBuilder itemBuilder) {
		return model.addItem(itemBuilder);
	}

	@Override
	public IRadioItemModel addItem(final int index, final IRadioItemModelBuilder itemBuilder) {
		return model.addItem(index, itemBuilder);
	}

	@Override
	public ICheckedItemModel addItem(final ICheckedItemModel item) {
		return model.addItem(item);
	}

	@Override
	public ICheckedItemModel addItem(final int index, final ICheckedItemModel item) {
		return model.addItem(index, item);
	}

	@Override
	public ICheckedItemModel addItem(final ICheckedItemModelBuilder itemBuilder) {
		return model.addItem(itemBuilder);
	}

	@Override
	public ICheckedItemModel addItem(final int index, final ICheckedItemModelBuilder itemBuilder) {
		return model.addItem(index, itemBuilder);
	}

	@Override
	public ISeparatorItemModel addItem(final ISeparatorItemModel item) {
		return model.addItem(item);
	}

	@Override
	public ISeparatorItemModel addItem(final int index, final ISeparatorItemModel item) {
		return model.addItem(index, item);
	}

	@Override
	public void addAfter(final IActionItemModel newItem, final String id) {
		model.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final IActionItemModel newItem, final String id) {
		model.addBefore(newItem, id);
	}

	@Override
	public void addAfter(final IRadioItemModel newItem, final String id) {
		model.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final IRadioItemModel newItem, final String id) {
		model.addBefore(newItem, id);
	}

	@Override
	public void addAfter(final ICheckedItemModel newItem, final String id) {
		model.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final ICheckedItemModel newItem, final String id) {
		model.addBefore(newItem, id);
	}

	@Override
	public void addAfter(final ISeparatorItemModel newItem, final String id) {
		model.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final ISeparatorItemModel newItem, final String id) {
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
	public IRadioItemModel addRadioItem() {
		return model.addRadioItem();
	}

	@Override
	public IRadioItemModel addRadioItem(final String text) {
		return model.addRadioItem(text);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText) {
		return model.addRadioItem(text, toolTipText);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final IImageConstant icon) {
		return model.addRadioItem(text, icon);
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText, final IImageConstant icon) {
		return model.addRadioItem(text, toolTipText, icon);
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
	public void removeItem(final IItemModel item) {
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
	public IItemModel findItemById(final String id) {
		return model.findItemById(id);
	}

	@Override
	public List<IItemModel> getChildren() {
		return model.getChildren();
	}

	@Override
	public IToolBarModel createCopy() {
		return model.createCopy();
	}

	private static IToolBarModel createInstance() {
		return Toolkit.getModelFactoryProvider().getItemModelFactory().toolBar();
	}

}

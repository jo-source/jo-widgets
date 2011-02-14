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
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IMenuModelBuilder;
import org.jowidgets.api.model.item.IMenuModelListener;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;

public class MenuModel extends AbstractItemModelWrapper implements IMenuModel {

	public MenuModel() {
		this(builder());
	}

	public MenuModel(final String id) {
		this(builder().setId(id));
	}

	public MenuModel(final String text, final IImageConstant icon) {
		this(builder().setText(text).setIcon(icon));
	}

	public MenuModel(final String id, final String text, final IImageConstant icon) {
		this(builder().setId(id).setText(text).setIcon(icon));
	}

	public MenuModel(final IMenuModelBuilder builder) {
		super(builder.build());
	}

	@Override
	public IMenuModel getItemModel() {
		return (IMenuModel) super.getItemModel();
	}

	@Override
	public List<IItemModel> getChildren() {
		return getItemModel().getChildren();
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
	public ISeparatorItemModel addSeparator() {
		return getItemModel().addSeparator();
	}

	@Override
	public ISeparatorItemModel addSeparator(final int index) {
		return getItemModel().addSeparator();
	}

	@Override
	public void addItem(final IItemModel item) {
		getItemModel().addItem(item);
	}

	@Override
	public void addItem(final int index, final IItemModel item) {
		getItemModel().addItem(index, item);
	}

	@Override
	public void removeItem(final IItemModel item) {
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
	public void addMenuModelListener(final IMenuModelListener listener) {
		getItemModel().addMenuModelListener(listener);
	}

	@Override
	public void removeMenuModelListener(final IMenuModelListener listener) {
		getItemModel().removeMenuModelListener(listener);
	}

	public static IMenuModelBuilder builder() {
		return Toolkit.getModelBuilderFactoryProvider().getItemModelBuilderFactory().menuModel();
	}

}

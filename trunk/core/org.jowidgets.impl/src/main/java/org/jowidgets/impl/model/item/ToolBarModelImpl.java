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

import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IActionItemModelBuilder;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.ICheckedItemModelBuilder;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.IRadioItemModelBuilder;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.model.item.IToolBarModel;

class ToolBarModelImpl extends MenuModelImpl implements IToolBarModel {

	protected ToolBarModelImpl() {
		super();
	}

	@Override
	public ToolBarModelImpl createCopy() {
		final ToolBarModelImpl result = new ToolBarModelImpl();
		result.setContent(this);
		return result;
	}

	@Override
	public IActionItemModel addItem(final IActionItemModel item) {
		return super.addItem(item);
	}

	@Override
	public IActionItemModel addItem(final int index, final IActionItemModel item) {
		return (IActionItemModel) addItem(index, (IItemModel) item);
	}

	@Override
	public IActionItemModel addItem(final IActionItemModelBuilder itemBuilder) {
		return super.addItem(itemBuilder);
	}

	@Override
	public IActionItemModel addItem(final int index, final IActionItemModelBuilder itemBuilder) {
		return super.addItem(index, itemBuilder);
	}

	@Override
	public IRadioItemModel addItem(final IRadioItemModel item) {
		return super.addItem(item);
	}

	@Override
	public IRadioItemModel addItem(final int index, final IRadioItemModel item) {
		return (IRadioItemModel) addItem(index, (IItemModel) item);
	}

	@Override
	public IRadioItemModel addItem(final IRadioItemModelBuilder itemBuilder) {
		return super.addItem(itemBuilder);
	}

	@Override
	public IRadioItemModel addItem(final int index, final IRadioItemModelBuilder itemBuilder) {
		return super.addItem(index, itemBuilder);
	}

	@Override
	public ICheckedItemModel addItem(final ICheckedItemModel item) {
		return super.addItem(item);
	}

	@Override
	public ICheckedItemModel addItem(final int index, final ICheckedItemModel item) {
		return (ICheckedItemModel) addItem(index, (IItemModel) item);
	}

	@Override
	public ICheckedItemModel addItem(final ICheckedItemModelBuilder itemBuilder) {
		return super.addItem(itemBuilder);
	}

	@Override
	public ICheckedItemModel addItem(final int index, final ICheckedItemModelBuilder itemBuilder) {
		return super.addItem(index, itemBuilder);
	}

	@Override
	public ISeparatorItemModel addItem(final ISeparatorItemModel item) {
		return super.addItem(item);
	}

	@Override
	public ISeparatorItemModel addItem(final int index, final ISeparatorItemModel item) {
		return (ISeparatorItemModel) addItem(index, (IItemModel) item);
	}

	@Override
	public void addAfter(final IActionItemModel newItem, final String id) {
		super.addAfter(newItem, id);

	}

	@Override
	public void addBefore(final IActionItemModel newItem, final String id) {
		super.addBefore(newItem, id);
	}

	@Override
	public void addAfter(final IRadioItemModel newItem, final String id) {
		super.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final IRadioItemModel newItem, final String id) {
		super.addBefore(newItem, id);
	}

	@Override
	public void addAfter(final ICheckedItemModel newItem, final String id) {
		super.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final ICheckedItemModel newItem, final String id) {
		super.addBefore(newItem, id);
	}

	@Override
	public void addAfter(final ISeparatorItemModel newItem, final String id) {
		super.addAfter(newItem, id);
	}

	@Override
	public void addBefore(final ISeparatorItemModel newItem, final String id) {
		super.addBefore(newItem, id);
	}

	@Override
	public IItemModel findItemById(final String id) {
		return super.findItemByPath(id);
	}

	@Override
	public <MODEL_TYPE extends IItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		checkItemType(item);
		return super.addItem(index, item);
	}

	private void checkItemType(final IItemModel item) {
		if ((item instanceof IMenuModel)) {
			throw new IllegalStateException("The item '"
				+ item
				+ "' is type of '"
				+ IMenuModel.class.getName()
				+ "'. A toolbar could could not contain menus."
				+ " This seems to be an illegal use of the implementation "
				+ "(e.g. by casting this object to the implementation type and adding "
				+ "a IMenuModel).");
		}
	}
}

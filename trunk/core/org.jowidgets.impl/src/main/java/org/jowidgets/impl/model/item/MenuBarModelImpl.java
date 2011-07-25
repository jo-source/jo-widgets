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

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IMenuModelBuilder;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;

class MenuBarModelImpl implements IMenuBarModel {

	private final ListModelDelegate listModelDelegate;
	private final Set<IMenuBarModel> boundModels;

	protected MenuBarModelImpl() {
		super();
		this.listModelDelegate = new ListModelDelegate();
		this.boundModels = new HashSet<IMenuBarModel>();

		this.addListModelListener(new IListModelListener() {

			@Override
			public void afterChildRemoved(final int index) {
				for (final IMenuBarModel boundModel : boundModels) {
					boundModel.removeMenu(index);
				}
			}

			@Override
			public void afterChildAdded(final int index) {
				for (final IMenuBarModel boundModel : boundModels) {
					boundModel.addMenu(getMenus().get(index));
				}
			}
		});
	}

	@Override
	public MenuBarModelImpl createCopy() {
		final MenuBarModelImpl result = new MenuBarModelImpl();
		result.setContent(this);
		return result;
	}

	protected void setContent(final IMenuBarModel source) {
		listModelDelegate.setContent(source);
	}

	@Override
	public void addAfter(final IMenuModel newMenu, final String id) {
		listModelDelegate.addAfter(newMenu, id);
	}

	@Override
	public void addBefore(final IMenuModel newMenu, final String id) {
		listModelDelegate.addBefore(newMenu, id);
	}

	@Override
	public void bind(final IMenuBarModel model) {
		Assert.paramNotNull(model, "model");
		model.removeAllMenus();
		model.addMenusOfModel(this);
		boundModels.add(model);
	}

	@Override
	public void unbind(final IMenuBarModel model) {
		Assert.paramNotNull(model, "model");
		boundModels.remove(model);
	}

	@Override
	public void addMenusOfModel(final IMenuBarModel model) {
		Assert.paramNotNull(model, "model");
		for (final IMenuModel menu : model.getMenus()) {
			addMenu(menu);
		}
	}

	@Override
	public IMenuModel addMenu(final IMenuModel menu) {
		return listModelDelegate.addItem(menu);
	}

	@Override
	public IMenuModel addMenu(final int index, final IMenuModel menu) {
		return listModelDelegate.addItem(index, menu);
	}

	@Override
	public IMenuModel addMenu(final IMenuModelBuilder menuBuilder) {
		return listModelDelegate.addItem(menuBuilder);
	}

	@Override
	public IMenuModel addMenu(final int index, final IMenuModelBuilder menuBuilder) {
		return listModelDelegate.addItem(index, menuBuilder);
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
	public IMenuModel findMenuById(final String id) {
		return (IMenuModel) listModelDelegate.findItemByPath(id);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public List<IMenuModel> getMenus() {
		return new LinkedList(listModelDelegate.getChildren());
	}

	@Override
	public void removeMenu(final IMenuModel item) {
		listModelDelegate.removeItem(item);
	}

	@Override
	public void removeMenu(final int index) {
		listModelDelegate.removeItem(index);
	}

	@Override
	public void removeAllMenus() {
		listModelDelegate.removeAllItems();
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		listModelDelegate.addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		listModelDelegate.removeListModelListener(listener);
	}

}

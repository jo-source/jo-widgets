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

import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IMenuModelBuilder;

public class MenuBarModel extends MenuModel implements IMenuBarModel {

	public MenuBarModel() {
		super();
	}

	@Override
	public MenuBarModel createCopy() {
		final MenuBarModel result = new MenuBarModel();
		result.setContent(this);
		return result;
	}

	@Override
	public void addAfter(final IMenuModel newMenu, final String id) {
		super.addAfter(newMenu, id);
	}

	@Override
	public void addBefore(final IMenuModel newMenu, final String id) {
		super.addBefore(newMenu, id);
	}

	@Override
	public IMenuModel addMenu(final IMenuModel menu) {
		return super.addItem(menu);
	}

	@Override
	public IMenuModel addMenu(final int index, final IMenuModel menu) {
		return (IMenuModel) addItem(index, (IItemModel) menu);
	}

	@Override
	public IMenuModel addMenu(final IMenuModelBuilder menuBuilder) {
		return super.addItem(menuBuilder);
	}

	@Override
	public IMenuModel addMenu(final int index, final IMenuModelBuilder menuBuilder) {
		return super.addItem(index, menuBuilder);
	}

	@Override
	public IMenuModel findMenuById(final String id) {
		return (IMenuModel) super.findItemByPath(id);
	}

	@Override
	public List<IMenuModel> getMenus() {
		final List<IMenuModel> result = new LinkedList<IMenuModel>();
		for (final IItemModel item : super.getChildren()) {
			if (item instanceof IMenuModel) {
				result.add((IMenuModel) item);
			}
			else {
				checkItemType(item);
			}
		}
		return result;
	}

	@Override
	public void removeMenu(final IMenuModel item) {
		super.removeItem(item);
	}

	@Override
	public void removeMenu(final int index) {
		super.removeItem(index);
	}

	@Override
	public void removeAllMenus() {
		super.removeAllItems();
	}

	@Override
	public <MODEL_TYPE extends IItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		checkItemType(item);
		return super.addItem(index, item);
	}

	private void checkItemType(final IItemModel item) {
		if (!(item instanceof IMenuModel)) {
			throw new IllegalStateException("The item '"
				+ item
				+ "' is not type of '"
				+ IMenuModel.class.getName()
				+ "' as expected. A menubar could only contain menus."
				+ " This seems to be an illegal use of the implementation "
				+ "(e.g. by casting this object to the implementation type and adding unallowed items"
				+ "like IActionItemModel, IRadioItemModel, ICheckedItemModel, ISeparatorItemModel).");
		}
	}
}

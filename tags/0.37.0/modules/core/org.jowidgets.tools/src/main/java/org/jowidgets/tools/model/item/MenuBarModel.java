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

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IMenuModelBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;

public class MenuBarModel implements IMenuBarModel {

	private final IMenuBarModel model;

	public MenuBarModel() {
		this(createInstance());
	}

	private MenuBarModel(final IMenuBarModel model) {
		Assert.paramNotNull(model, "model");
		this.model = model;
	}

	@Override
	public final void addListModelListener(final IListModelListener listener) {
		model.addListModelListener(listener);
	}

	@Override
	public final void removeListModelListener(final IListModelListener listener) {
		model.removeListModelListener(listener);
	}

	@Override
	public final void addAfter(final IMenuModel newMenu, final String id) {
		model.addAfter(newMenu, id);
	}

	@Override
	public final void addBefore(final IMenuModel newMenu, final String id) {
		model.addBefore(newMenu, id);
	}

	@Override
	public final void bind(final IMenuBarModel model) {
		this.model.bind(model);
	}

	@Override
	public final void unbind(final IMenuBarModel model) {
		this.model.unbind(model);
	}

	@Override
	public final void addMenusOfModel(final IMenuBarModel model) {
		this.model.addMenusOfModel(model);
	}

	@Override
	public final IMenuModel addMenu(final IMenuModel menu) {
		return model.addMenu(menu);
	}

	@Override
	public final IMenuModel addMenu(final int index, final IMenuModel menu) {
		return model.addMenu(index, menu);
	}

	@Override
	public final IMenuModel addMenu(final IMenuModelBuilder menuBuilder) {
		return model.addMenu(menuBuilder);
	}

	@Override
	public final IMenuModel addMenu(final int index, final IMenuModelBuilder menuBuilder) {
		return model.addMenu(index, menuBuilder);
	}

	@Override
	public final IMenuModel addMenu() {
		return model.addMenu();
	}

	@Override
	public final IMenuModel addMenu(final String text) {
		return model.addMenu(text);
	}

	@Override
	public final IMenuModel addMenu(final String text, final String toolTipText) {
		return model.addMenu(text, toolTipText);
	}

	@Override
	public final IMenuModel addMenu(final String text, final IImageConstant icon) {
		return model.addMenu(text, icon);
	}

	@Override
	public final IMenuModel addMenu(final String text, final String toolTipText, final IImageConstant icon) {
		return model.addMenu(text, toolTipText, icon);
	}

	@Override
	public final void removeMenu(final IMenuModel item) {
		model.removeMenu(item);
	}

	@Override
	public final void removeMenu(final int index) {
		model.removeMenu(index);
	}

	@Override
	public final void removeAllMenus() {
		model.removeAllMenus();
	}

	@Override
	public final IMenuModel findMenuById(final String id) {
		return model.findMenuById(id);
	}

	@Override
	public final List<IMenuModel> getMenus() {
		return model.getMenus();
	}

	@Override
	public IMenuBarModel createCopy() {
		return model.createCopy();
	}

	private static IMenuBarModel createInstance() {
		return Toolkit.getModelFactoryProvider().getItemModelFactory().menuBar();
	}

}

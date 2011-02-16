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
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelBuilder;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IMenuModelListener;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.util.Assert;

public class MenuModel extends ItemModel implements IMenuModel {

	private final Set<IMenuModelListener> menuModelListeners;

	private final List<IItemModel> children;

	protected MenuModel() {
		this(null, null, null, null, null, null, true);
	}

	protected MenuModel(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final Accelerator accelerator,
		final Character mnemonic,
		final boolean enabled) {
		super(id, text, toolTipText, icon, accelerator, mnemonic, enabled);
		menuModelListeners = new HashSet<IMenuModelListener>();
		this.children = new LinkedList<IItemModel>();
	}

	@Override
	public IMenuModel createCopy() {
		final MenuModel result = new MenuModel();
		result.setContent(this);
		return result;
	}

	protected void setContent(final IMenuModel source) {
		super.setContent(source);

		for (final IItemModel sourceChild : source.getChildren()) {
			addItem(sourceChild.createCopy());
		}
	}

	@Override
	public final void addMenuModelListener(final IMenuModelListener listener) {
		menuModelListeners.add(listener);
	}

	@Override
	public final void removeMenuModelListener(final IMenuModelListener listener) {
		menuModelListeners.remove(listener);
	}

	@Override
	public final List<IItemModel> getChildren() {
		return new LinkedList<IItemModel>(children);
	}

	@Override
	public IActionItemModel addAction(final IAction action) {
		return addItem(new ActionItemModelBuilder().setAction(action));
	}

	@Override
	public IActionItemModel addAction(final int index, final IAction action) {
		return addItem(index, new ActionItemModelBuilder().setAction(action));
	}

	@Override
	public IActionItemModel addActionItem() {
		return addItem(new ActionItemModelBuilder());
	}

	@Override
	public IActionItemModel addActionItem(final String text) {
		return addItem(new ActionItemModelBuilder().setText(text));
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText) {
		return addItem(new ActionItemModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	@Override
	public IActionItemModel addActionItem(final String text, final IImageConstant icon) {
		return addItem(new ActionItemModelBuilder().setText(text).setIcon(icon));
	}

	@Override
	public IActionItemModel addActionItem(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new ActionItemModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	@Override
	public ICheckedItemModel addCheckedItem() {
		return addItem(new CheckedItemModelBuilder());
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text) {
		return addItem(new CheckedItemModelBuilder().setText(text));
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText) {
		return addItem(new CheckedItemModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final IImageConstant icon) {
		return addItem(new CheckedItemModelBuilder().setText(text).setIcon(icon));
	}

	@Override
	public ICheckedItemModel addCheckedItem(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new CheckedItemModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	@Override
	public IRadioItemModel addRadioItem() {
		return addItem(new RadioItemModelBuilder());
	}

	@Override
	public IRadioItemModel addRadioItem(final String text) {
		return addItem(new RadioItemModelBuilder().setText(text));
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText) {
		return addItem(new RadioItemModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final IImageConstant icon) {
		return addItem(new RadioItemModelBuilder().setText(text).setIcon(icon));
	}

	@Override
	public IRadioItemModel addRadioItem(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new RadioItemModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	@Override
	public IMenuModel addMenu() {
		return addItem(new MenuModelBuilder());
	}

	@Override
	public IMenuModel addMenu(final String text) {
		return addItem(new MenuModelBuilder().setText(text));
	}

	@Override
	public IMenuModel addMenu(final String text, final String toolTipText) {
		return addItem(new MenuModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	@Override
	public IMenuModel addMenu(final String text, final IImageConstant icon) {
		return addItem(new MenuModelBuilder().setText(text).setIcon(icon));
	}

	@Override
	public IMenuModel addMenu(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new MenuModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	@Override
	public ISeparatorItemModel addSeparator() {
		return addItem(new SeparatorItemModelBuilder());
	}

	@Override
	public ISeparatorItemModel addSeparator(final int index) {
		return addItem(index, new SeparatorItemModelBuilder());
	}

	@Override
	public ISeparatorItemModel addSeparator(final String id) {
		return addItem(new SeparatorItemModelBuilder().setId(id));
	}

	@Override
	public <MODEL_TYPE extends IItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final BUILDER_TYPE itemBuilder) {
		return addItem(itemBuilder.build());
	}

	@Override
	public <MODEL_TYPE extends IItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final int index,
		final BUILDER_TYPE itemBuilder) {
		return addItem(index, itemBuilder.build());
	}

	@Override
	public <MODEL_TYPE extends IItemModel> MODEL_TYPE addItem(final MODEL_TYPE item) {
		addItem(children.size(), item);
		return item;
	}

	@Override
	public <MODEL_TYPE extends IItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		Assert.paramNotNull(item, "item");
		children.add(index, item);
		fireChildAdded(index);
		return item;
	}

	@Override
	public void removeItem(final IItemModel item) {
		final int index = children.indexOf(item);
		if (index != -1) {
			removeItem(index);
		}
	}

	@Override
	public void removeItem(final int index) {
		children.remove(index);
		fireChildRemoved(index);
	}

	@Override
	public void removeAllItems() {
		for (final IItemModel item : getChildren()) {
			removeItem(item);
		}
	}

	protected final void fireChildAdded(final int index) {
		for (final IMenuModelListener listener : menuModelListeners) {
			listener.childAdded(index);
		}
	}

	protected final void fireChildRemoved(final int index) {
		for (final IMenuModelListener listener : menuModelListeners) {
			listener.childRemoved(index);
		}
	}

}

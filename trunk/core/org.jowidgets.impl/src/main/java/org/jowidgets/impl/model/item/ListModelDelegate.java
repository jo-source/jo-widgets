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
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelBuilder;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;

class ListModelDelegate {

	private final Set<IListModelListener> listModelListeners;
	private final List<IItemModel> children;
	private final IItemModelListener itemModelListener;

	protected ListModelDelegate() {
		listModelListeners = new HashSet<IListModelListener>();
		this.children = new LinkedList<IItemModel>();

		this.itemModelListener = new IItemModelListener() {
			@Override
			public void itemChanged(final IItemModel item) {
				checkIds(item);
			}
		};
	}

	protected void setContent(final IMenuModel source) {
		setChildren(source.getChildren());
	}

	protected void setContent(final IToolBarModel source) {
		setChildren(source.getItems());
	}

	protected void setContent(final IMenuBarModel source) {
		setChildren(source.getMenus());
	}

	private void setChildren(final List<? extends IItemModel> children) {
		for (final IItemModel sourceChild : children) {
			addItem(sourceChild.createCopy());
		}
	}

	protected IActionItemModel addAction(final IAction action) {
		return addItem(new ActionItemModelBuilder().setAction(action));
	}

	protected IActionItemModel addAction(final int index, final IAction action) {
		return addItem(index, new ActionItemModelBuilder().setAction(action));
	}

	protected IActionItemModel addActionItem() {
		return addItem(new ActionItemModelBuilder());
	}

	protected IActionItemModel addActionItem(final String text) {
		return addItem(new ActionItemModelBuilder().setText(text));
	}

	protected IActionItemModel addActionItem(final String text, final String toolTipText) {
		return addItem(new ActionItemModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	protected IActionItemModel addActionItem(final String text, final IImageConstant icon) {
		return addItem(new ActionItemModelBuilder().setText(text).setIcon(icon));
	}

	protected IActionItemModel addActionItem(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new ActionItemModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	protected IActionItemModel addActionItem(final IImageConstant icon, final String toolTipText) {
		return addItem(new ActionItemModelBuilder().setToolTipText(toolTipText).setIcon(icon));
	}

	protected ICheckedItemModel addCheckedItem() {
		return addItem(new CheckedItemModelBuilder());
	}

	protected ICheckedItemModel addCheckedItem(final String text) {
		return addItem(new CheckedItemModelBuilder().setText(text));
	}

	protected ICheckedItemModel addCheckedItem(final String text, final String toolTipText) {
		return addItem(new CheckedItemModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	protected ICheckedItemModel addCheckedItem(final String text, final IImageConstant icon) {
		return addItem(new CheckedItemModelBuilder().setText(text).setIcon(icon));
	}

	protected ICheckedItemModel addCheckedItem(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new CheckedItemModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	protected ICheckedItemModel addCheckedItem(final IImageConstant icon, final String toolTipText) {
		return addItem(new CheckedItemModelBuilder().setToolTipText(toolTipText).setIcon(icon));
	}

	protected IRadioItemModel addRadioItem() {
		return addItem(new RadioItemModelBuilder());
	}

	protected IRadioItemModel addRadioItem(final String text) {
		return addItem(new RadioItemModelBuilder().setText(text));
	}

	protected IRadioItemModel addRadioItem(final String text, final String toolTipText) {
		return addItem(new RadioItemModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	protected IRadioItemModel addRadioItem(final String text, final IImageConstant icon) {
		return addItem(new RadioItemModelBuilder().setText(text).setIcon(icon));
	}

	protected IRadioItemModel addRadioItem(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new RadioItemModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	protected IMenuModel addMenu() {
		return addItem(new MenuModelBuilder());
	}

	protected IMenuModel addMenu(final String text) {
		return addItem(new MenuModelBuilder().setText(text));
	}

	protected IMenuModel addMenu(final String text, final String toolTipText) {
		return addItem(new MenuModelBuilder().setText(text).setToolTipText(toolTipText));
	}

	protected IMenuModel addMenu(final String text, final IImageConstant icon) {
		return addItem(new MenuModelBuilder().setText(text).setIcon(icon));
	}

	protected IMenuModel addMenu(final String text, final String toolTipText, final IImageConstant icon) {
		return addItem(new MenuModelBuilder().setText(text).setToolTipText(toolTipText).setIcon(icon));
	}

	protected ISeparatorItemModel addSeparator() {
		return addItem(new SeparatorItemModelBuilder());
	}

	protected ISeparatorItemModel addSeparator(final int index) {
		return addItem(index, new SeparatorItemModelBuilder());
	}

	protected ISeparatorItemModel addSeparator(final String id) {
		return addItem(new SeparatorItemModelBuilder().setId(id));
	}

	protected void addBefore(final IItemModel newItem, final String... idPath) {
		addByPath(newItem, true, idPath);
	}

	protected void addAfter(final IItemModel newItem, final String... idPath) {
		addByPath(newItem, false, idPath);
	}

	protected <MODEL_TYPE extends IItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final BUILDER_TYPE itemBuilder) {
		return addItem(itemBuilder.build());
	}

	protected <MODEL_TYPE extends IItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final int index,
		final BUILDER_TYPE itemBuilder) {
		return addItem(index, itemBuilder.build());
	}

	protected <MODEL_TYPE extends IItemModel> MODEL_TYPE addItem(final MODEL_TYPE item) {
		addItem(children.size(), item);
		return item;
	}

	protected <MODEL_TYPE extends IItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item) {
		Assert.paramNotNull(item, "item");
		if (item instanceof IMenuModel && this instanceof IMenuModel) {
			checkRecursion((IMenuModel) item, (IMenuModel) this);
		}
		checkIds(item);
		children.add(index, item);
		item.addItemModelListener(itemModelListener);
		fireAfterChildAdded(index);
		return item;
	}

	private void addByPath(final IItemModel newItem, final boolean before, final String... idPath) {
		Assert.paramNotNull(newItem, "newItem");
		Assert.paramAndElementsNotEmpty(idPath, "idPath");

		final String lastId = idPath[idPath.length - 1];

		//create the path to the last menu 
		final String[] menuPath = new String[idPath.length - 1];
		for (int i = 0; i < menuPath.length; i++) {
			menuPath[i] = idPath[i];
		}

		//find the menu to add the item to
		if (menuPath.length > 0) {
			final IItemModel itemForPath = findItemByPath(menuPath);
			if (itemForPath instanceof IMenuModel) {
				final IMenuModel parentMenu = (IMenuModel) itemForPath;
				if (before) {
					parentMenu.addBefore((IMenuItemModel) newItem, lastId);
				}
				else {
					parentMenu.addAfter((IMenuItemModel) newItem, lastId);
				}
			}
			else {
				throw new IllegalArgumentException("The id path '" + pathToString(idPath) + "' doesn't match for the menu.");
			}
		}
		else {
			//add the item to the this delegate at the propper index
			int index = 0;
			for (final IItemModel child : children) {
				if (NullCompatibleEquivalence.equals(child.getId(), lastId)) {
					if (before) {
						addItem(index, newItem);
					}
					else {
						addItem(index + 1, newItem);
					}
					return;
				}
				index++;
			}
			throw new IllegalArgumentException("The id path '" + pathToString(idPath) + "' doesn't match for the menu.");
		}
	}

	private String pathToString(final String... idPath) {
		final StringBuilder result = new StringBuilder();
		result.append("[");
		for (final String tanga : idPath) {
			result.append(tanga + ", ");
		}
		if (idPath.length > 0) {
			result.delete(result.length() - 2, result.length() - 1);
		}
		result.append("]");
		return result.toString();
	}

	protected final List<IItemModel> getChildren() {
		return children;
	}

	protected IItemModel findItemByPath(final String... idPath) {
		Assert.paramNotEmpty(idPath, "idPath");
		for (final IItemModel child : children) {
			if (NullCompatibleEquivalence.equals(child.getId(), idPath[0])) {
				if (idPath.length == 1) {
					return child;
				}
				else if (child instanceof IMenuModel) {
					final String[] newPath = new String[idPath.length - 1];
					for (int i = 0; i < newPath.length; i++) {
						newPath[i] = idPath[i + 1];
					}
					return ((IMenuModel) child).findItemByPath(newPath);
				}
				else {
					//this path can not match
					return null;
				}
			}
		}
		return null;
	}

	protected void removeItem(final IItemModel item) {
		final int index = children.indexOf(item);
		if (index != -1) {
			removeItem(index);
		}
	}

	protected void removeItem(final int index) {
		if (children.size() > index) {
			fireBeforeChildRemove(index);
		}
		final IItemModel removedItem = children.remove(index);
		if (removedItem != null) {
			removedItem.removeItemModelListener(itemModelListener);
			fireAfterChildRemoved(index);
		}
	}

	protected void removeAllItems() {
		for (final IItemModel item : new LinkedList<IItemModel>(children)) {
			removeItem(item);
		}
	}

	protected final void addListModelListener(final IListModelListener listener) {
		listModelListeners.add(listener);
	}

	protected final void removeListModelListener(final IListModelListener listener) {
		listModelListeners.remove(listener);
	}

	private void fireAfterChildAdded(final int index) {
		for (final IListModelListener listener : new LinkedList<IListModelListener>(listModelListeners)) {
			listener.afterChildAdded(index);
		}
	}

	private void fireBeforeChildRemove(final int index) {
		for (final IListModelListener listener : new LinkedList<IListModelListener>(listModelListeners)) {
			listener.beforeChildRemove(index);
		}
	}

	private void fireAfterChildRemoved(final int index) {
		for (final IListModelListener listener : new LinkedList<IListModelListener>(listModelListeners)) {
			listener.afterChildRemoved(index);
		}
	}

	private void checkIds(final IItemModel item) {
		if (item.getId() == null) {
			throw new IllegalArgumentException("Invalid item ID: The item '" + item + "' has no id.");
		}
		for (final IItemModel child : children) {
			if (child != item && NullCompatibleEquivalence.equals(item.getId(), child.getId())) {
				throw new IllegalArgumentException("Invalid item ID: The item '"
					+ item
					+ "' has the same id ("
					+ item.getId()
					+ ") as the child '"
					+ child
					+ " ("
					+ child.getId()
					+ ")"
					+ " of their parent menu '"
					+ this
					+ "'.");
			}
		}
	}

	private void checkRecursion(final IMenuModel menu1, final IMenuModel menu2) {
		if (NullCompatibleEquivalence.equals(menu1, menu2)) {
			throw new IllegalArgumentException("Menu Model Recursion: "
				+ "The added menu has the same id than this menu or has a child menu with the same id than this menu.");
		}

		for (final IItemModel child : menu1.getChildren()) {
			if (child instanceof IMenuModel) {
				checkRecursion((IMenuModel) child, menu2);
			}
		}
	}
}

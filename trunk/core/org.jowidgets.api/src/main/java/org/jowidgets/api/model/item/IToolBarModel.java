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

package org.jowidgets.api.model.item;

import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.IListModelObservable;
import org.jowidgets.common.image.IImageConstant;

public interface IToolBarModel extends IListModelObservable {

	void addItemsOfModel(IToolBarModel model);

	<MODEL_TYPE extends IToolBarItemModel> MODEL_TYPE addItem(final MODEL_TYPE item);

	<MODEL_TYPE extends IToolBarItemModel> MODEL_TYPE addItem(final int index, final MODEL_TYPE item);

	<MODEL_TYPE extends IToolBarItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		final BUILDER_TYPE itemBuilder);

	<MODEL_TYPE extends IToolBarItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
		int index,
		final BUILDER_TYPE itemBuilder);

	void addAfter(IToolBarItemModel newItem, String id);

	void addBefore(IToolBarItemModel newItem, String id);

	IActionItemModel addAction(IAction action);

	IActionItemModel addAction(final int index, IAction action);

	IPopupActionItemModel addPopupAction(IAction action, IMenuModel popupMenu);

	IPopupActionItemModel addPopupAction(final int index, IAction action, IMenuModel popupMenu);

	IActionItemModel addActionItem();

	IActionItemModel addActionItem(String text);

	IActionItemModel addActionItem(String text, String toolTipText);

	IActionItemModel addActionItem(String text, IImageConstant icon);

	IActionItemModel addActionItem(String text, String toolTipText, IImageConstant icon);

	IPopupActionItemModel addPopupActionItem();

	IPopupActionItemModel addPopupActionItem(String text);

	IPopupActionItemModel addPopupActionItem(String text, String toolTipText);

	IPopupActionItemModel addPopupActionItem(String text, IImageConstant icon);

	IPopupActionItemModel addPopupActionItem(String text, String toolTipText, IImageConstant icon);

	ICheckedItemModel addCheckedItem();

	ICheckedItemModel addCheckedItem(String text);

	ICheckedItemModel addCheckedItem(String text, String toolTipText);

	ICheckedItemModel addCheckedItem(String text, IImageConstant icon);

	ICheckedItemModel addCheckedItem(String text, String toolTipText, IImageConstant icon);

	IContainerItemModel addContainer();

	IContainerItemModel addContainer(IContainerContentCreator contentCreator);

	ISeparatorItemModel addSeparator();

	ISeparatorItemModel addSeparator(String id);

	ISeparatorItemModel addSeparator(int index);

	void removeItem(final IToolBarItemModel item);

	void removeItem(int index);

	void removeAllItems();

	IToolBarItemModel findItemById(String id);

	List<IToolBarItemModel> getItems();

	/**
	 * Makes a deep copy of the tool bar and its children.
	 * Registered listeners on items won't be copied.
	 * 
	 * @return A new instance that is a clone of this instance
	 */
	IToolBarModel createCopy();

}

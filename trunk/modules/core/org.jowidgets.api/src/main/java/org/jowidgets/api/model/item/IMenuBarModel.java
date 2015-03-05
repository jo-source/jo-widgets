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

import org.jowidgets.api.model.IListModelObservable;
import org.jowidgets.common.image.IImageConstant;

public interface IMenuBarModel extends IListModelObservable {

	void addMenu(IMenuModel menu);

	void addMenu(int index, IMenuModel menu);

	IMenuModel addMenu(IMenuModelBuilder menuBuilder);

	IMenuModel addMenu(int index, IMenuModelBuilder menuBuilder);

	IMenuModel addMenu();

	IMenuModel addMenu(String text);

	IMenuModel addMenu(String text, String toolTipText);

	IMenuModel addMenu(String text, IImageConstant icon);

	IMenuModel addMenu(String text, String toolTipText, IImageConstant icon);

	void addAfter(IMenuModel newMenu, String id);

	void addBefore(IMenuModel newMenu, String id);

	void addMenusOfModel(IMenuBarModel model);

	void removeMenu(final IMenuModel item);

	void removeMenu(int index);

	void removeAllMenus();

	List<IMenuModel> getMenus();

	IMenuModel findMenuById(String id);

	/**
	 * Binds the given model to this model with the following manner:
	 * 
	 * 1. All items of the given model will be removed.
	 * 2. All items of this model will be added to the given model.
	 * 3. All changes on this model will be done on the given model.
	 * 
	 * @param model The model to bind this model to
	 * 
	 * @throws IllegalArgumentException if the given model is null
	 */
	void bind(IMenuBarModel model);

	/**
	 * Unbind the given model. Changes on this model will no longer be propagated
	 * to the given model.
	 * 
	 * @param model the model to unbind, may be not bound already but must not be null
	 * 
	 * @throws IllegalArgumentException if the given model is null
	 */
	void unbind(IMenuBarModel model);

	/**
	 * Creates a deep copy of the model and its children.
	 * 
	 * Remark: Registered listeners won't be copied, so the result has no
	 * registered listeners for the first time.
	 * 
	 * @return A new instance that is a clone of this instance
	 */
	IMenuBarModel createCopy();

}

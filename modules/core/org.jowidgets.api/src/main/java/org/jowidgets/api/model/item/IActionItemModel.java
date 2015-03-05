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

import org.jowidgets.api.command.IAction;
import org.jowidgets.common.widgets.controller.IActionObservable;
import org.jowidgets.util.IDecorator;

public interface IActionItemModel extends IMenuItemModel, IToolBarItemModel, IActionObservable {

	/**
	 * Sets an action that should be bound to the model
	 * 
	 * @param action The action to bind, may be null
	 */
	void setAction(IAction action);

	/**
	 * Gets the bound action of the item
	 * 
	 * @return The bound action or null, if no action is bound
	 */
	IAction getAction();

	/**
	 * Adds a decorator to the model.
	 * 
	 * The decorator will decorate the action of the model.
	 * If action will be changed, the changed model will be decorated.
	 * 
	 * @param decorator The decorator to add
	 */
	void addDecorator(IDecorator<IAction> decorator);

	/**
	 * Removes a decorator from the model.
	 * 
	 * After removing a decorator, the current action, if set,
	 * will decorated again without the removed decorator.
	 * If the last decorator will be removed, the original (undecorated) action
	 * will be used.
	 * 
	 * @param decorator The decorator to add
	 */
	void removeDecorator(IDecorator<IAction> decorator);

	/**
	 * This fires an action performed event for the item.
	 * 
	 * This method is for internal use only.
	 */
	void actionPerformed();

	@Override
	IActionItemModel createCopy();

}

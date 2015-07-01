/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.api.widgets;

import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.common.widgets.IMenuCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

public interface IMenu extends IWidget, IMenuCommon {

    /**
     * Gets the model of the menu
     * 
     * @return The model, never null
     */
    IMenuModel getModel();

    /**
     * Sets the model of the menu
     * 
     * @param model The model to set, must not be null
     */
    void setModel(IMenuModel model);

    /**
     * Adds a menu item to the menu
     * 
     * @param descriptor The descriptor for the item to add, must not be null
     * 
     * @return The created menu item
     */
    <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addItem(IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

    /**
     * Adds a menu item to the menu at a specific index
     * 
     * @param index The index where to add the item
     * @param descriptor The descriptor for the item to add, must not be null
     * 
     * @return The created menu item
     */
    <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addItem(int index, IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

    /**
     * Adds an action item to the menu that will be bound to the given action
     * 
     * @param action The action to add, must not be null
     * 
     * @return The action item that was created
     */
    IActionMenuItem addAction(IAction action);

    /**
     * Adds an action item to the menu at a specific that will be bound to the given action
     * 
     * @param index The index where to add the item
     * @param action The action to add, must not be null
     * 
     * @return The action item that was created
     */
    IActionMenuItem addAction(int index, IAction action);

    /**
     * Adds a separator item to the menu
     * 
     * @return The separator item that was added
     */
    IMenuItem addSeparator();

    /**
     * Adds a separator item to the menu at a specific index
     * 
     * @param index The index where to add the separarot item
     * 
     * @return The separator item that was added
     */
    IMenuItem addSeparator(int index);

    /**
     * Removes a menu item form the menu
     * 
     * @param item The item to remove
     * 
     * @return True if the item was removed, false of the item is no child of the menu
     */
    boolean remove(IMenuItem item);

    /**
     * Removes all items from the menu
     */
    void removeAll();

    /**
     * Gets the menu items of the menu as a unmodifieable copy
     * 
     * @return The menu items of the menu, never null but may be empty
     */
    List<IMenuItem> getChildren();

}

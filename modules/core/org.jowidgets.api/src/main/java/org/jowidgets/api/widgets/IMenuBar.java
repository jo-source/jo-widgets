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

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.widgets.descriptor.IMainMenuDescriptor;
import org.jowidgets.common.widgets.IMenuBarCommon;

public interface IMenuBar extends IWidget, IMenuBarCommon {

    /**
     * Sets the model
     * 
     * @param model The model, must not be null
     */
    void setModel(IMenuBarModel model);

    /**
     * Gets the model
     * 
     * @return The model, never null
     */
    IMenuBarModel getModel();

    /**
     * Adds a menu to the menu bar
     * 
     * @param descriptor The descriptor of the menu
     * 
     * @return The added menu
     */
    IMainMenu addMenu(IMainMenuDescriptor descriptor);

    /**
     * Adds a menu to the menu bar at a given index
     * 
     * @param index The index to at the menu at
     * @param descriptor The descriptor of the menu
     * 
     * @return The added menu
     */
    IMainMenu addMenu(int index, IMainMenuDescriptor descriptor);

    /**
     * Adds a menu to the menu bar
     * 
     * @param name The name of the menu to add
     * 
     * @return The added menu
     */
    IMainMenu addMenu(String name);

    /**
     * Adds a menu to the menu bar
     * 
     * @param name The name of the menu
     * @param mnemonic The mnemonic of the mneu
     * 
     * @return The added menu
     */
    IMainMenu addMenu(String name, char mnemonic);

    /**
     * Adds a menu to the menu bar at a given index
     * 
     * @param index The index to at the menu at
     * @param name The name of the menu
     * 
     * @return The added menu
     */
    IMainMenu addMenu(int index, String name);

    /**
     * Gets a unmodifieable copy of the menus this menu bar contains
     * 
     * @return The list of menus, never null
     */
    List<IMenu> getMenus();

    /**
     * Removes a menu from the menu bar
     * 
     * The menu will be disposed by that
     * 
     * @param menu The menu to remove
     * 
     * @return true if the menu was removed, false if the given menu was no child of this menu bar
     */
    boolean remove(IMenu menu);

    /**
     * Removes a menu from the menu bar at a given index
     * 
     * @param index The index to remove the menu at
     */
    void remove(int index);

    /**
     * Removes all menus from the mneu bar
     */
    void removeAll();
}

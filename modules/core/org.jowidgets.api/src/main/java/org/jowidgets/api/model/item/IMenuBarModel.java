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

    /**
     * Adds a menu
     * 
     * @param menu The menu to add
     */
    void addMenu(IMenuModel menu);

    /**
     * Adds a menu at a given index
     * 
     * @param index The index where to add the menu
     * @param menu The menu to add
     */
    void addMenu(int index, IMenuModel menu);

    /**
     * Creates and adds a menu defined by a builder.
     * 
     * @param menuBuilder The builder that defines the menu
     * 
     * @return The added menu
     */
    IMenuModel addMenu(IMenuModelBuilder menuBuilder);

    /**
     * Creates and adds a menu defined by a builder at a given index
     * 
     * @param index The index where to add the menu
     * @param menuBuilder The builder that defines the menu
     * 
     * @return The added menu
     */
    IMenuModel addMenu(int index, IMenuModelBuilder menuBuilder);

    /**
     * Creates and add a menu
     * 
     * @return The created menu
     */
    IMenuModel addMenu();

    /**
     * Creates and add a menu
     * 
     * @param text The label text of the menu
     * 
     * @return The created menu
     */
    IMenuModel addMenu(String text);

    /**
     * Creates and add a menu
     * 
     * @param text The label text of the menu
     * @param toolTipText The tooltip text of the menu
     * 
     * @return The created menu
     */
    IMenuModel addMenu(String text, String toolTipText);

    /**
     * Creates and add a menu
     * 
     * @param text The label text of the menu
     * @param icon The icon of the menu
     * 
     * @return The created menu
     */
    IMenuModel addMenu(String text, IImageConstant icon);

    /**
     * Creates and add a menu
     * 
     * @param text The label text of the menu
     * @param toolTipText The tooltip text of the menu
     * @param icon The icon of the menu
     * 
     * @return The created menu
     */
    IMenuModel addMenu(String text, String toolTipText, IImageConstant icon);

    /**
     * Adds a menu after another menu
     * 
     * @param newMenu The menu to add
     * @param id The id of the menu to add the newMenu after
     * 
     * @throws IllegalArgumentException if no menu with the given id exists,
     *             use {@link #findMenuById(String)} to check if the menu exist
     */
    void addAfter(IMenuModel newMenu, String id);

    /**
     * Adds a menu before another menu
     * 
     * @param newMenu The menu to add
     * @param id The id of the menu to add the newMenu before
     * 
     * @throws IllegalArgumentException if no menu with the given id exists,
     *             use {@link #findMenuById(String)} to check if the menu exist
     */
    void addBefore(IMenuModel newMenu, String id);

    /**
     * Adds all menus of a given menu bar
     * 
     * @param model The menu bar which models should be added
     */
    void addMenusOfModel(IMenuBarModel model);

    /**
     * Removes a menu
     * 
     * @param item The menu to remove
     */
    void removeMenu(IMenuModel item);

    /**
     * Removes a menu at a given index
     * 
     * @param index The index where to remove the menu
     */
    void removeMenu(int index);

    /**
     * Removes all menus
     */
    void removeAllMenus();

    /**
     * Gets a unmodifieable copy of all menus
     * 
     * @return
     */
    List<IMenuModel> getMenus();

    /**
     * Searches for a menu with a given id
     * 
     * @param id The id of the menu to search for
     * 
     * @return The found menu or null if no menu with the id exists
     */
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

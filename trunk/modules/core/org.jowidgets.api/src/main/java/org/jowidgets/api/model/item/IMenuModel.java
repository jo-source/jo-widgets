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
import org.jowidgets.util.IDecorator;

public interface IMenuModel extends IItemModel, IMenuItemModel, IToolBarItemModel, IListModelObservable {

    /**
     * Adds an item to the model
     * 
     * @param item The item to add, must not be null
     */
    void addItem(final IMenuItemModel item);

    /**
     * Adds an item at a given index
     * 
     * @param index The index to add the item at
     * @param item The item to add
     */
    void addItem(final int index, final IMenuItemModel item);

    /**
     * Adds an item defined by an builder and returns the build item
     * 
     * @param itemBuilder The builder that defines the item
     * 
     * @return The added item
     */
    <MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
        final BUILDER_TYPE itemBuilder);

    /**
     * Adds an item at a given index defined by an builder and returns the build item
     * 
     * @param index The index to add the item at
     * @param itemBuilder The builder that defines the item
     * 
     * @return The added item
     */
    <MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> MODEL_TYPE addItem(
        int index,
        final BUILDER_TYPE itemBuilder);

    /**
     * Adds an item after another item defined by an path of item id
     * 
     * If the given path not exists, an IllegalArgumentException will be thrown
     * 
     * @param newItem The item to add
     * @param idPath The path where to add the item
     * 
     * @throws IllegalArgumentException
     */
    void addAfter(IMenuItemModel newItem, String... idPath);

    /**
     * Adds an item before another item defined by an path of item id
     * 
     * If the given path not exists, an IllegalArgumentException will be thrown
     * 
     * @param newItem The item to add
     * @param idPath The path where to add the item
     * 
     * @throws IllegalArgumentException
     */
    void addBefore(IMenuItemModel newItem, String... idPath);

    /**
     * Creates an adds an action item and binds it to the given action
     * 
     * @param action The action to use
     * 
     * @return The created action item
     */
    IActionItemModel addAction(IAction action);

    /**
     * Creates an adds an action item at a given index and binds it to the given action
     * 
     * @param index The index to add the item at
     * @param action The action to use
     * 
     * @return The created action item
     */
    IActionItemModel addAction(final int index, IAction action);

    /**
     * Creates and adds a action item
     * 
     * @return The created action item
     */
    IActionItemModel addActionItem();

    /**
     * Creates and adds a action item
     * 
     * @param text The label text of the action item
     * 
     * @return The created action item
     */
    IActionItemModel addActionItem(String text);

    /**
     * Creates and adds a action item
     * 
     * @param text The label text of the action item
     * @param toolTipText The tooltip text of the action item
     * 
     * @return The created action item
     */
    IActionItemModel addActionItem(String text, String toolTipText);

    /**
     * Creates and adds a action item
     * 
     * @param text The label text of the action item
     * @param icon The icon of the action item
     * 
     * @return The created action item
     */
    IActionItemModel addActionItem(String text, IImageConstant icon);

    /**
     * Creates and adds a action item
     * 
     * @param text The label text of the action item
     * @param toolTipText The tooltip text of the action item
     * @param icon The icon of the action item
     * 
     * @return The created action item
     */
    IActionItemModel addActionItem(String text, String toolTipText, IImageConstant icon);

    /**
     * Creates and adds a checked item
     * 
     * @return The created item
     */
    ICheckedItemModel addCheckedItem();

    /**
     * Creates and adds a checked item
     * 
     * @param text The label text of the item
     * 
     * @return The created item
     */
    ICheckedItemModel addCheckedItem(String text);

    /**
     * Creates and adds a checked item
     * 
     * @param text The label text of the item
     * @param toolTipText The tooltip text of the item
     * 
     * @return The created item
     */
    ICheckedItemModel addCheckedItem(String text, String toolTipText);

    /**
     * Creates and adds a checked item
     * 
     * @param text The label text of the item
     * @param icon The icon of the item
     * 
     * @return The created item
     */
    ICheckedItemModel addCheckedItem(String text, IImageConstant icon);

    /**
     * Creates and adds a checked item
     * 
     * @param text The label text of the item
     * @param toolTipText The tooltip text of the item
     * @param icon The icon of the item
     * 
     * @return The created item
     */
    ICheckedItemModel addCheckedItem(String text, String toolTipText, IImageConstant icon);

    /**
     * Creates and adds a radio item
     * 
     * @return The created item
     */
    IRadioItemModel addRadioItem();

    /**
     * Creates and adds a radio item
     * 
     * @param text The label text of the item
     * 
     * @return The created item
     */
    IRadioItemModel addRadioItem(String text);

    /**
     * Creates and adds a radio item
     * 
     * @param text The label text of the item
     * @param toolTipText The tooltip text of the item
     * 
     * @return The created item
     */
    IRadioItemModel addRadioItem(String text, String toolTipText);

    /**
     * Creates and adds a radio item
     * 
     * @param text The label text of the item
     * @param icon The icon of the item
     * 
     * @return The created item
     */
    IRadioItemModel addRadioItem(String text, IImageConstant icon);

    /**
     * Creates and adds a radio item
     * 
     * @param text The label text of the item
     * @param toolTipText The tooltip text of the item
     * @param icon The icon of the item
     * 
     * @return The created item
     */
    IRadioItemModel addRadioItem(String text, String toolTipText, IImageConstant icon);

    /**
     * Creates and adds a separator
     * 
     * @return The created item
     */
    ISeparatorItemModel addSeparator();

    /**
     * Creates and adds a separator
     * 
     * @param id The id of the separator
     * 
     * @return The created item
     */
    ISeparatorItemModel addSeparator(String id);

    /**
     * Creates and adds separator at a given index
     * 
     * @param index The index where to add the separator
     * 
     * @return The created item
     */
    ISeparatorItemModel addSeparator(int index);

    /**
     * Creates and add a submenu
     * 
     * @return The created submenu
     */
    IMenuModel addMenu();

    /**
     * Creates and add a submenu
     * 
     * @param text The label text of the sub menu
     * 
     * @return The created submenu
     */
    IMenuModel addMenu(String text);

    /**
     * Creates and add a submenu
     * 
     * @param text The label text of the sub menu
     * @param toolTipText The tooltip text of the submenu
     * 
     * @return The created submenu
     */
    IMenuModel addMenu(String text, String toolTipText);

    /**
     * Creates and add a submenu
     * 
     * @param text The label text of the sub menu
     * @param icon The icon of the submenu
     * 
     * @return The created submenu
     */
    IMenuModel addMenu(String text, IImageConstant icon);

    /**
     * Creates and add a submenu
     * 
     * @param text The label text of the sub menu
     * @param toolTipText The tooltip text of the submenu
     * @param icon The icon of the submenu
     * 
     * @return The created submenu
     */
    IMenuModel addMenu(String text, String toolTipText, IImageConstant icon);

    /**
     * Adds all items of a given model to this model
     * 
     * @param menuModel The model which items should be added
     */
    void addItemsOfModel(IMenuModel menuModel);

    /**
     * Adds an action decorator for all actions contained in this menu an all sub menus.
     * 
     * Remark: The decorator decorates the contained action and the actions
     * that will be added in the future
     * 
     * @param decorator The decorator to add, must not be null
     */
    void addDecorator(IDecorator<IAction> decorator);

    /**
     * Removes a decorator for all actions contained in this menu an all sub menus.
     * 
     * Remark: The decorator will only be removed for items that are still descendant
     * of this menu. In other words: When removing an item before removing the decorator,
     * the remove decorator invocation on the ancestor has no effect for the removed items.
     * 
     * @param decorator The decorator to remove, must not be null
     */
    void removeDecorator(IDecorator<IAction> decorator);

    /**
     * Removes an item from this menu
     * 
     * @param item The item to remove, must not be null
     */
    void removeItem(final IMenuItemModel item);

    /**
     * Removes an item at a given index
     * 
     * @param index The index where to remove the item
     */
    void removeItem(int index);

    /**
     * Removes all items from the menu
     */
    void removeAllItems();

    /**
     * Gets a unmodifieable copy of the menus items
     * 
     * @return The child items, never null but may be empty
     */
    List<IMenuItemModel> getChildren();

    /**
     * Searches for an item in this model defined by a given path
     * 
     * @param idPath The path of the items ids
     * 
     * @return The item if found, null otherwise
     */
    IMenuItemModel findItemByPath(String... idPath);

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
    void bind(IMenuModel model);

    /**
     * Unbind the given model. Changes on this model will no longer be propagated
     * to the given model.
     * 
     * @param model the model to unbind, may be not bound already but must not be null
     * 
     * @throws IllegalArgumentException if the given model is null
     */
    void unbind(IMenuModel model);

    @Override
    IMenuModel createCopy();

}

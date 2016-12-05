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

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;

public interface IItemModel extends IItemModelObservable {

    /**
     * Gets the id of the item
     * 
     * @return The id of the item, never null
     */
    String getId();

    /**
     * Sets the items label text
     * 
     * @param text The label text of the item, may be null
     */
    void setText(String text);

    /**
     * Gets the items label text
     * 
     * @return The label text of the item, may be null
     */
    String getText();

    /**
     * Sets the tooltip text for the item
     * 
     * @param toolTipText The tooltip text to set, may be null
     */
    void setToolTipText(String toolTipText);

    /**
     * Gets the tooltip text of the item
     * 
     * @return The tooltip text, may be null
     */
    String getToolTipText();

    /**
     * Sets the items icon
     * 
     * @param icon The icon to set, may be null
     */
    void setIcon(IImageConstant icon);

    /**
     * Gets the items icon
     * 
     * @return The icon, may be null
     */
    IImageConstant getIcon();

    /**
     * Sets the key accelerator that should be used for the item
     * 
     * @param accelerator The accelerator to set, may be null
     */
    void setAccelerator(Accelerator accelerator);

    /**
     * Sets the key accelerator that should be used for the item
     * 
     * @param key The character to use
     * @param modifier The modifier to use
     */
    void setAccelerator(VirtualKey key, Modifier... modifier);

    /**
     * Gets the key accelerator that should be used for the item
     * 
     * @return The key accelerator, may be null
     */
    Accelerator getAccelerator();

    /**
     * Sets the mnemonic character
     * 
     * @param mnemonic The mnemonic character to set, may be null
     */
    void setMnemonic(Character mnemonic);

    /**
     * Sets the mnemonic character
     * 
     * @param mnemonic The mnemonic character to set
     */
    void setMnemonic(char mnemonic);

    /**
     * Gets the mnemonic character
     * 
     * @return The mnemonic character, may be null
     */
    Character getMnemonic();

    /**
     * Sets the enabled state of the item.
     * 
     * Remark: Not all bound items may support to become disabled
     * 
     * @param enabled The enabled state to set
     */
    void setEnabled(boolean enabled);

    /**
     * Gets the enabled state of the item
     * 
     * Remark: Not all bound items may support to become disabled
     * 
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Sets the visible state of an item
     * 
     * Items that was set invisible will be hidden in their container
     * 
     * @param visible The visible state to set
     */
    void setVisible(boolean visible);

    /**
     * Gets the visible state of the item
     * 
     * @return True if visible, false otherwise
     */
    boolean isVisible();

    /**
     * Creates a deep copy of the item and its children.
     * 
     * Remark: Registered listeners on items won't be copied, so the result has no
     * registered listeners for the first time.
     * 
     * @return A new instance that is a clone of this instance
     */
    IItemModel createCopy();

}

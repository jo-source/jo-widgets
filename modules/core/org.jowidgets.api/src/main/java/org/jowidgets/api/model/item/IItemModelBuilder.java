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

public interface IItemModelBuilder<INSTANCE_TYPE, ITEM_TYPE> {

    /**
     * The the id if the item.
     * 
     * If no id will be set or null will set,
     * a UUID will be used
     * 
     * @param id The id to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setId(String id);

    /**
     * Sets the label text
     * 
     * @param text The label text to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setText(String text);

    /**
     * Sets the tooltip text
     * 
     * @param toolTipText The tooltip text to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setToolTipText(String toolTipText);

    /**
     * Sets the icon
     * 
     * @param icon The icon to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setIcon(IImageConstant icon);

    /**
     * Sets the key accelerator
     * 
     * @param accelerator The accelerator to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setAccelerator(Accelerator accelerator);

    /**
     * Sets the key accelerator
     * 
     * @param key The virtual key of the accelerator
     * @param modifier The modifier of the accelerator
     * 
     * @return This builder
     */
    INSTANCE_TYPE setAccelerator(VirtualKey key, Modifier... modifier);

    /**
     * Sets the key accelerator
     * 
     * @param key The key character of the accelerator
     * @param modifier The modifier of the accelerator
     * 
     * @return This builder
     */
    INSTANCE_TYPE setAccelerator(char key, Modifier... modifier);

    /**
     * Sets the mnemonic
     * 
     * @param mnemonic The mnemonic to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setMnemonic(Character mnemonic);

    /**
     * Sets the mnemonic
     * 
     * @param mnemonic The mnemonic to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setMnemonic(char mnemonic);

    /**
     * Sets the initial enabled state
     * 
     * The default value is true
     * 
     * @param enabled The enabled state to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setEnabled(boolean enabled);

    /**
     * Creates a new model based on the given setup
     * 
     * @return A new model
     */
    ITEM_TYPE build();

}

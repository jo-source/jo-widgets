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

package org.jowidgets.tools.model.item;

import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.IRadioItemModelBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;

public class RadioItemModel extends AbstractSelectableItemModelWrapper implements IRadioItemModel {

    /**
     * Creates a new checked item model
     */
    public RadioItemModel() {
        this(builder());
    }

    /**
     * Creates a new checked item model
     * 
     * @param text The label text to use
     */
    public RadioItemModel(final String text) {
        this(builder().setText(text));
    }

    /**
     * Creates a new checked item model
     * 
     * @param text The label text to use
     * @param icon The icon to use
     */
    public RadioItemModel(final String text, final IImageConstant icon) {
        this(builder(text, icon));
    }

    /**
     * Creates a new checked item model
     * 
     * @param text The label text to use
     * @param toolTipText The tooltip text to use
     */
    public RadioItemModel(final String text, final String toolTipText) {
        this(builder(text, toolTipText));
    }

    /**
     * Creates a new checked item model
     * 
     * @param text The label text to use
     * @param toolTipText The tooltip text to use
     * @param icon The icon to use
     */
    public RadioItemModel(final String text, final String toolTipText, final IImageConstant icon) {
        this(builder(text, toolTipText, icon));
    }

    /**
     * Creates a new checked item model
     * 
     * @param id The id to use
     * @param text The label text to use
     * @param toolTipText The tooltip text to use
     * @param icon The icon to use
     */
    public RadioItemModel(final String id, final String text, final String toolTipText, final IImageConstant icon) {
        this(builder(id, text, toolTipText, icon));
    }

    /**
     * Creates a checked item model defined by a builder
     * 
     * @param builder The builder that defines the model
     */
    public RadioItemModel(final IRadioItemModelBuilder builder) {
        super(builder.build());
    }

    @Override
    protected IRadioItemModel getItemModel() {
        return (IRadioItemModel) super.getItemModel();
    }

    @Override
    public IRadioItemModel createCopy() {
        return getItemModel().createCopy();
    }

    /**
     * Creates a new checked item model
     * 
     * @return A new checked item model
     */
    public static IRadioItemModel create() {
        return builder().build();
    }

    /**
     * Creates a new checked item model builder
     * 
     * @return A new checked item model builder
     */
    public static IRadioItemModelBuilder builder() {
        return Toolkit.getModelFactoryProvider().getItemModelFactory().radioItemBuilder();
    }

    /**
     * Creates a new checked item model builder
     * 
     * @param text The label text to set on the builder
     * 
     * @return A new checked item model builder
     */
    public static IRadioItemModelBuilder builder(final String text) {
        return builder().setText(text);
    }

    /**
     * Creates a new checked item model builder
     * 
     * @param text The label text to set on the builder
     * @param toolTipText The tooltip text to set on the builder
     * 
     * @return A new checked item model builder
     */
    public static IRadioItemModelBuilder builder(final String text, final String toolTipText) {
        return builder(text).setToolTipText(toolTipText);
    }

    /**
     * Creates a new checked item model builder
     * 
     * @param text The label text to set on the builder
     * @param icon The icon to set on the builder
     * 
     * @return A new checked item model builder
     */
    public static IRadioItemModelBuilder builder(final String text, final IImageConstant icon) {
        return builder().setText(text).setIcon(icon);
    }

    /**
     * Creates a new checked item model builder
     * 
     * @param text The label text to set on the builder
     * @param toolTipText The tooltip text to set on the builder
     * @param icon The icon to set on the builder
     * 
     * @return A new checked item model builder
     */
    public static IRadioItemModelBuilder builder(final String text, final String toolTipText, final IImageConstant icon) {
        return builder(text, toolTipText).setIcon(icon);
    }

    /**
     * Creates a new checked item model builder
     * 
     * @param id The id to set on builder
     * @param text The label text to set on the builder
     * @param toolTipText The tooltip text to set on the builder
     * @param icon The icon to set on the builder
     * 
     * @return A new checked item model builder
     */
    public static IRadioItemModelBuilder builder(
        final String id,
        final String text,
        final String toolTipText,
        final IImageConstant icon) {
        return builder(text, toolTipText, icon).setId(id);
    }

}

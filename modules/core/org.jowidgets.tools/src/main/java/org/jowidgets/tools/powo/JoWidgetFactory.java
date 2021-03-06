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

package org.jowidgets.tools.powo;

import org.jowidgets.api.widgets.descriptor.IActionMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckedMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.ILabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IProgressBarDescriptor;
import org.jowidgets.api.widgets.descriptor.IRadioMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.ISplitCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISubMenuDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

/**
 * @deprecated The idea of POWO's (Plain Old Widget Object's) has not been established.
 *             For that, POWO's will no longer be supported and may removed completely in middle term.
 *             Feel free to move them to your own open source project.
 */
@Deprecated
class JoWidgetFactory {

    @SuppressWarnings({"rawtypes", "unchecked"})
    Widget create(final IWidgetDescriptor descriptor) {

        Assert.paramNotNull(descriptor, "descriptor");

        if (descriptor instanceof IButtonDescriptor) {
            return new JoButton((IButtonDescriptor) descriptor);
        }
        else if (descriptor instanceof ICheckBoxDescriptor) {
            return new JoCheckBox((ICheckBoxDescriptor) descriptor);
        }
        else if (descriptor instanceof IComboBoxSelectionDescriptor) {
            return new JoComboBoxSelection((IComboBoxSelectionDescriptor) descriptor);
        }
        else if (descriptor instanceof IComboBoxDescriptor) {
            return new JoComboBox((IComboBoxDescriptor) descriptor);
        }
        else if (descriptor instanceof ICompositeDescriptor) {
            return new JoComposite((ICompositeDescriptor) descriptor);
        }
        else if (descriptor instanceof IIconDescriptor) {
            return new JoIcon((IIconDescriptor) descriptor);
        }
        else if (descriptor instanceof ILabelDescriptor) {
            return new JoLabel((ILabelDescriptor) descriptor);
        }
        else if (descriptor instanceof IProgressBarDescriptor) {
            return new JoProgressBar((IProgressBarDescriptor) descriptor);
        }
        else if (descriptor instanceof IScrollCompositeDescriptor) {
            return new JoScrollComposite((IScrollCompositeDescriptor) descriptor);
        }
        else if (descriptor instanceof ISplitCompositeDescriptor) {
            return new JoSplitComposite((ISplitCompositeDescriptor) descriptor);
        }
        else if (descriptor instanceof ITextLabelDescriptor) {
            return new JoTextLabel((ITextLabelDescriptor) descriptor);
        }
        else if (descriptor instanceof IToggleButtonDescriptor) {
            return new JoToggleButton((IToggleButtonDescriptor) descriptor);
        }
        else if (descriptor instanceof IActionMenuItemDescriptor) {
            return new JoActionMenuItem((IActionMenuItemDescriptor) descriptor);
        }
        else if (descriptor instanceof ICheckedMenuItemDescriptor) {
            return new JoCheckedMenuItem((ICheckedMenuItemDescriptor) descriptor);
        }
        else if (descriptor instanceof IRadioMenuItemDescriptor) {
            return new JoRadioMenuItem((IRadioMenuItemDescriptor) descriptor);
        }
        else if (descriptor instanceof ISubMenuDescriptor) {
            return new JoSubMenu((ISubMenuDescriptor) descriptor);
        }
        else if (descriptor instanceof ISeparatorMenuItemDescriptor) {
            return new JoSeparatorMenuItem((ISeparatorMenuItemDescriptor) descriptor);
        }
        else if (descriptor instanceof ISeparatorDescriptor) {
            return new Control(descriptor);
        }
        else {
            throw new IllegalArgumentException("Could not create a Widget from descriptor '"
                + descriptor.getDescriptorInterface().getName()
                + "' Descriptor is not yet supported for Powo's (Plain old widget objects)");
        }
    }

}

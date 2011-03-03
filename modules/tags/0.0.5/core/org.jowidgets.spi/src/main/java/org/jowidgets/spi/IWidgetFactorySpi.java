/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi;

import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IButtonSpi;
import org.jowidgets.spi.widgets.ICheckBoxSpi;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IIconSpi;
import org.jowidgets.spi.widgets.IProgressBarSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.ITextFieldSpi;
import org.jowidgets.spi.widgets.ITextLabelSpi;
import org.jowidgets.spi.widgets.IToggleButtonSpi;
import org.jowidgets.spi.widgets.setup.IButtonSetupSpi;
import org.jowidgets.spi.widgets.setup.ICheckBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.ICompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;
import org.jowidgets.spi.widgets.setup.IIconSetupSpi;
import org.jowidgets.spi.widgets.setup.IProgressBarSetupSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ISeparatorSetupSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextLabelSetupSpi;
import org.jowidgets.spi.widgets.setup.IToggleButtonSetupSpi;

public interface IWidgetFactorySpi {

	//create widgets from ui-reference

	boolean isConvertibleToFrame(Object uiReference);

	IFrameSpi createFrame(IGenericWidgetFactory factory, Object uiReference);

	boolean isConvertibleToComposite(Object uiReference);

	ICompositeSpi createComposite(IGenericWidgetFactory factory, Object uiReference);

	//create widgets from setup

	IFrameSpi createFrame(IGenericWidgetFactory factory, Object parentUiReference, IFrameSetupSpi setup);

	IFrameSpi createDialog(IGenericWidgetFactory factory, Object parentUiReference, IDialogSetupSpi setup);

	ICompositeSpi createComposite(IGenericWidgetFactory factory, Object parentUiReference, ICompositeSetupSpi setup);

	ISplitCompositeSpi createSplitComposite(IGenericWidgetFactory factory, Object parentUiReference, ISplitCompositeSetupSpi setup);

	IScrollCompositeSpi createScrollComposite(
		IGenericWidgetFactory factory,
		Object parentUiReference,
		IScrollCompositeSetupSpi setup);

	ITextFieldSpi createTextField(Object parentUiReference, ITextFieldSetupSpi setup);

	ITextLabelSpi createTextLabel(Object parentUiReference, ITextLabelSetupSpi setup);

	IIconSpi createIcon(Object parentUiReference, IIconSetupSpi setup);

	IButtonSpi createButton(Object parentUiReference, IButtonSetupSpi setup);

	IControlSpi createSeparator(Object parentUiReference, ISeparatorSetupSpi setup);

	ICheckBoxSpi createCheckBox(Object parentUiReference, ICheckBoxSetupSpi setup);

	IToggleButtonSpi createToggleButton(Object parentUiReference, IToggleButtonSetupSpi setup);

	IComboBoxSelectionSpi createComboBoxSelection(Object parentUiReference, IComboBoxSelectionSetupSpi setup);

	IComboBoxSpi createComboBox(Object parentUiReference, IComboBoxSetupSpi setup);

	IProgressBarSpi createProgressBar(Object parentUiReference, IProgressBarSetupSpi setup);

}

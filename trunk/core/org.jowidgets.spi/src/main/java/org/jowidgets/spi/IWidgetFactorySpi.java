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

import org.jowidgets.common.widgets.IContainerWidgetCommon;
import org.jowidgets.common.widgets.IFrameWidgetCommon;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IButtonWidgetSpi;
import org.jowidgets.spi.widgets.ICheckBoxWidgetSpi;
import org.jowidgets.spi.widgets.IComboBoxSelectionWidgetSpi;
import org.jowidgets.spi.widgets.IComboBoxWidgetSpi;
import org.jowidgets.spi.widgets.IContainerWidgetSpi;
import org.jowidgets.spi.widgets.IFrameWidgetSpi;
import org.jowidgets.spi.widgets.IIconWidgetSpi;
import org.jowidgets.spi.widgets.IScrollContainerWidgetSpi;
import org.jowidgets.spi.widgets.ITextInputWidgetSpi;
import org.jowidgets.spi.widgets.ITextLabelWidgetSpi;
import org.jowidgets.spi.widgets.IToggleButtonWidgetSpi;
import org.jowidgets.spi.widgets.IWidgetSpi;
import org.jowidgets.spi.widgets.setup.IButtonSetupSpi;
import org.jowidgets.spi.widgets.setup.ICheckBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.ICompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;
import org.jowidgets.spi.widgets.setup.IIconSetupSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ISeparatorSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextLabelSetupSpi;
import org.jowidgets.spi.widgets.setup.IToggleButtonSetupSpi;

public interface IWidgetFactorySpi {

	//create widgets from ui-reference

	boolean isConvertibleToFrame(Object uiReference);

	IFrameWidgetCommon createFrameWidget(IGenericWidgetFactory factory, Object uiReference);

	boolean isConvertibleToContainer(Object uiReference);

	IContainerWidgetCommon createContainerWidget(IGenericWidgetFactory factory, Object uiReference);

	//create widgets from setup

	IFrameWidgetSpi createFrameWidget(IGenericWidgetFactory factory, IFrameSetupSpi setup);

	IFrameWidgetSpi createDialogWidget(IGenericWidgetFactory factory, IWidget parent, IDialogSetupSpi setup);

	IContainerWidgetSpi createCompositeWidget(IGenericWidgetFactory factory, IWidget parent, ICompositeSetupSpi setup);

	IScrollContainerWidgetSpi createScrollCompositeWidget(
		IGenericWidgetFactory factory,
		IWidget parent,
		IScrollCompositeSetupSpi setup);

	ITextInputWidgetSpi createTextFieldWidget(IWidget parent, ITextFieldSetupSpi setup);

	ITextLabelWidgetSpi createTextLabelWidget(IWidget parent, ITextLabelSetupSpi setup);

	IIconWidgetSpi createIconWidget(IWidget parent, IIconSetupSpi setup);

	IButtonWidgetSpi createButtonWidget(final IWidget parent, IButtonSetupSpi setup);

	IWidgetSpi createSeparatorWidget(final IWidget parent, ISeparatorSetupSpi setup);

	ICheckBoxWidgetSpi createCheckBoxWidget(final IWidget parent, ICheckBoxSetupSpi setup);

	IToggleButtonWidgetSpi createToggleButtonWidget(final IWidget parent, IToggleButtonSetupSpi setup);

	IComboBoxSelectionWidgetSpi createComboBoxSelectionWidget(final IWidget parent, IComboBoxSelectionSetupSpi setup);

	IComboBoxWidgetSpi createComboBoxWidget(final IWidget parent, IComboBoxSetupSpi setup);

}

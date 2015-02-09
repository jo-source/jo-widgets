/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.test.api.widgets.blueprint.factory;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.test.api.widgets.blueprint.IActionMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICheckBoxBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICheckedMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IComboBoxBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IComboBoxSelectionBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IDialogBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IFrameBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IIconBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IMainMenuBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IRadioMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IScrollCompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISeparatorBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISeparatorMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISeparatorToolBarItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISplitCompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ISubMenuBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ITextFieldBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ITextLabelBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToggleButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarContainerItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarPopupButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IToolBarToggleButtonBluePrintUi;

public interface IBasicSimpleTestBluePrintFactory {
	<WIDGET_TYPE extends IWidget, BLUE_PRINT_TYPE extends ISetupBuilder<BLUE_PRINT_TYPE> & IWidgetDescriptor<WIDGET_TYPE>> BLUE_PRINT_TYPE bluePrint(
		final Class<BLUE_PRINT_TYPE> bluePrintType);

	IFrameBluePrintUi frame();

	IDialogBluePrintUi dialog();

	ICompositeBluePrintUi composite();

	IScrollCompositeBluePrintUi scrollComposite();

	ISplitCompositeBluePrintUi splitComposite();

	ITextLabelBluePrintUi textLabel();

	IIconBluePrintUi icon();

	ISeparatorBluePrintUi separator();

	ITextFieldBluePrintUi textField();

	IButtonBluePrintUi button();

	ICheckBoxBluePrintUi checkBox();

	IToggleButtonBluePrintUi toggleButton();

	<INPUT_TYPE> IComboBoxBluePrintUi<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter);

	<INPUT_TYPE> IComboBoxSelectionBluePrintUi<INPUT_TYPE> comboBoxSelection(
		final IObjectStringConverter<INPUT_TYPE> objectStringConverter);

	IActionMenuItemBluePrintUi menuItem();

	IRadioMenuItemBluePrintUi radioMenuItem();

	ICheckedMenuItemBluePrintUi checkedMenuItem();

	ISubMenuBluePrintUi subMenu();

	IMainMenuBluePrintUi mainMenu();

	ISeparatorMenuItemBluePrintUi menuSeparator();

	ISeparatorToolBarItemBluePrintUi toolBarSeparator();

	IToolBarBluePrintUi toolBar();

	IToolBarButtonBluePrintUi toolBarButton();

	IToolBarToggleButtonBluePrintUi toolBarToggleButton();

	IToolBarPopupButtonBluePrintUi toolBarPopupButton();

	IToolBarContainerItemBluePrintUi toolBarContainerItem();
}

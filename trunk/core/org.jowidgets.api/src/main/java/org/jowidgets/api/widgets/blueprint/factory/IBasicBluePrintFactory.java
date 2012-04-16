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
package org.jowidgets.api.widgets.blueprint.factory;

import java.util.List;

import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckedMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IIconBluePrint;
import org.jowidgets.api.widgets.blueprint.IMainMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.IRadioMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISubMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.common.image.IImageConstant;

public interface IBasicBluePrintFactory extends IBasicSimpleBluePrintFactory {

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////some convenience methods starting here///////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	IFrameBluePrint frame(final String title);

	IFrameBluePrint frame(final String title, final IImageConstant icon);

	IDialogBluePrint dialog(final String title);

	IDialogBluePrint dialog(final String title, final IImageConstant icon);

	ICompositeBluePrint compositeWithBorder();

	ICompositeBluePrint composite(final String borderTitle);

	IScrollCompositeBluePrint scrollCompositeWithBorder();

	IScrollCompositeBluePrint scrollComposite(final String borderTitle);

	ISplitCompositeBluePrint splitHorizontal();

	ISplitCompositeBluePrint splitVertical();

	IButtonBluePrint button(final String text);

	IButtonBluePrint button(final String text, final String toolTipText);

	IButtonBluePrint buttonOk();

	IButtonBluePrint buttonCancel();

	IIconBluePrint icon(final IImageConstant icon);

	ITextLabelBluePrint textLabel(final String text);

	ITextLabelBluePrint textLabel(final String text, final String tooltipText);

	IComboBoxBluePrint<String> comboBox();

	IComboBoxBluePrint<String> comboBox(String... elements);

	IComboBoxBluePrint<String> comboBox(List<String> elements);

	IComboBoxBluePrint<String> comboBoxString();

	IComboBoxBluePrint<Long> comboBoxLongNumber();

	IComboBoxBluePrint<Integer> comboBoxIntegerNumber();

	IComboBoxBluePrint<Short> comboBoxShortNumber();

	IComboBoxSelectionBluePrint<String> comboBoxSelection();

	IComboBoxSelectionBluePrint<String> comboBoxSelection(String... elements);

	IComboBoxSelectionBluePrint<String> comboBoxSelection(List<String> elements);

	<ENUM_TYPE extends Enum<?>> IComboBoxSelectionBluePrint<ENUM_TYPE> comboBoxSelection(final ENUM_TYPE... enumValues);

	IComboBoxSelectionBluePrint<String> comboBoxSelectionString();

	IComboBoxSelectionBluePrint<Long> comboBoxSelectionLongNumber();

	IComboBoxSelectionBluePrint<Integer> comboBoxSelectionIntegerNumber();

	IComboBoxSelectionBluePrint<Short> comboBoxSelectionShortNumber();

	IComboBoxSelectionBluePrint<Boolean> comboBoxSelectionBoolean();

	IActionMenuItemBluePrint menuItem(String text);

	IRadioMenuItemBluePrint radioMenuItem(String text);

	ICheckedMenuItemBluePrint checkedMenuItem(String text);

	ISubMenuBluePrint subMenu(String text);

	IMainMenuBluePrint mainMenu(String text);

}

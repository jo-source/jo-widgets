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

package org.jowidgets.tools.widgets.blueprint;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckedMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IIconBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IMainMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IRadioMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISubMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextSeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarContainerItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarPopupButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.builder.IComponentSetupBuilder;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

public class BluePrintFactoryWrapper implements IBluePrintFactory {

	private final IBluePrintFactory bluePrintFactory;

	public BluePrintFactoryWrapper(final IBluePrintFactory bluePrintFactory) {
		Assert.paramNotNull(bluePrintFactory, "bluePrintFactory");
		this.bluePrintFactory = bluePrintFactory;
	}

	@Override
	public ILabelBluePrint label() {
		return bluePrintFactory.label();
	}

	@Override
	public ITextSeparatorBluePrint textSeparator() {
		return bluePrintFactory.textSeparator();
	}

	@Override
	public IValidationLabelBluePrint validationLabel() {
		return bluePrintFactory.validationLabel();
	}

	@Override
	public <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IConverter<INPUT_TYPE> converter) {
		return bluePrintFactory.inputField(converter);
	}

	@Override
	public IMessageDialogBluePrint messageDialog() {
		return bluePrintFactory.messageDialog();
	}

	@Override
	public IQuestionDialogBluePrint questionDialog() {
		return bluePrintFactory.questionDialog();
	}

	@Override
	public IProgressBarBluePrint progressBar() {
		return bluePrintFactory.progressBar();
	}

	@Override
	public <INPUT_TYPE> IInputDialogBluePrint<INPUT_TYPE> inputDialog(final IInputContentCreator<INPUT_TYPE> contentCreator) {
		return bluePrintFactory.inputDialog(contentCreator);
	}

	@Override
	public <INPUT_TYPE> IInputCompositeBluePrint<INPUT_TYPE> inputComposite(final IInputContentCreator<INPUT_TYPE> contentCreator) {
		return bluePrintFactory.inputComposite(contentCreator);
	}

	@Override
	public IFrameBluePrint frame(final String title) {
		return bluePrintFactory.frame(title);
	}

	@Override
	public IFrameBluePrint frame(final String title, final IImageConstant icon) {
		return bluePrintFactory.frame(title, icon);
	}

	@Override
	public IDialogBluePrint dialog(final String title) {
		return bluePrintFactory.dialog(title);
	}

	@Override
	public IDialogBluePrint dialog(final String title, final IImageConstant icon) {
		return bluePrintFactory.dialog(title, icon);
	}

	@Override
	public ICompositeBluePrint compositeWithBorder() {
		return bluePrintFactory.compositeWithBorder();
	}

	@Override
	public ICompositeBluePrint composite(final String borderTitle) {
		return bluePrintFactory.composite();
	}

	@Override
	public IScrollCompositeBluePrint scrollCompositeWithBorder() {
		return bluePrintFactory.scrollCompositeWithBorder();
	}

	@Override
	public IScrollCompositeBluePrint scrollComposite(final String borderTitle) {
		return bluePrintFactory.scrollComposite(borderTitle);
	}

	@Override
	public ISplitCompositeBluePrint splitHorizontal() {
		return bluePrintFactory.splitHorizontal();
	}

	@Override
	public ISplitCompositeBluePrint splitVertical() {
		return bluePrintFactory.splitVertical();
	}

	@Override
	public IButtonBluePrint button(final String text) {
		return bluePrintFactory.button(text);
	}

	@Override
	public IButtonBluePrint button(final String text, final String toolTipText) {
		return bluePrintFactory.button(text, toolTipText);
	}

	@Override
	public IIconBluePrint icon(final IImageConstant icon) {
		return bluePrintFactory.icon(icon);
	}

	@Override
	public ITextLabelBluePrint textLabel(final String text) {
		return bluePrintFactory.textLabel(text);
	}

	@Override
	public ITextLabelBluePrint textLabel(final String text, final String tooltipText) {
		return bluePrintFactory.textLabel(text, tooltipText);
	}

	@Override
	public IComboBoxBluePrint<String> comboBox() {
		return bluePrintFactory.comboBox();
	}

	@Override
	public IComboBoxBluePrint<String> comboBox(final String... elements) {
		return bluePrintFactory.comboBox(elements);
	}

	@Override
	public IComboBoxBluePrint<String> comboBoxString() {
		return bluePrintFactory.comboBoxString();
	}

	@Override
	public IComboBoxBluePrint<Long> comboBoxLongNumber() {
		return bluePrintFactory.comboBoxLongNumber();
	}

	@Override
	public IComboBoxBluePrint<Integer> comboBoxIntegerNumber() {
		return bluePrintFactory.comboBoxIntegerNumber();
	}

	@Override
	public IComboBoxBluePrint<Short> comboBoxShortNumber() {
		return bluePrintFactory.comboBoxShortNumber();
	}

	@Override
	public IComboBoxSelectionBluePrint<String> comboBoxSelection() {
		return bluePrintFactory.comboBoxSelection();
	}

	@Override
	public IComboBoxSelectionBluePrint<String> comboBoxSelection(final String... elements) {
		return bluePrintFactory.comboBoxSelection(elements);
	}

	@Override
	public <ENUM_TYPE extends Enum<?>> IComboBoxSelectionBluePrint<ENUM_TYPE> comboBoxSelection(final ENUM_TYPE... enumValues) {
		return bluePrintFactory.comboBoxSelection(enumValues);
	}

	@Override
	public IComboBoxSelectionBluePrint<String> comboBoxSelectionString() {
		return bluePrintFactory.comboBoxSelectionString();
	}

	@Override
	public IComboBoxSelectionBluePrint<Long> comboBoxSelectionLongNumber() {
		return bluePrintFactory.comboBoxSelectionLongNumber();
	}

	@Override
	public IComboBoxSelectionBluePrint<Integer> comboBoxSelectionIntegerNumber() {
		return bluePrintFactory.comboBoxSelectionIntegerNumber();
	}

	@Override
	public IComboBoxSelectionBluePrint<Short> comboBoxSelectionShortNumber() {
		return bluePrintFactory.comboBoxSelectionShortNumber();
	}

	@Override
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>, BLUE_PRINT_TYPE extends IComponentSetupBuilder<BLUE_PRINT_TYPE> & IWidgetDescriptor<WIDGET_TYPE>> BLUE_PRINT_TYPE bluePrint(
		final Class<BLUE_PRINT_TYPE> bluePrintType,
		final Class<DESCRIPTOR_TYPE> descriptorType) {
		return bluePrintFactory.bluePrint(bluePrintType, descriptorType);
	}

	@Override
	public IFrameBluePrint frame() {
		return bluePrintFactory.frame();
	}

	@Override
	public IDialogBluePrint dialog() {
		return bluePrintFactory.dialog();
	}

	@Override
	public ICompositeBluePrint composite() {
		return bluePrintFactory.composite();
	}

	@Override
	public IScrollCompositeBluePrint scrollComposite() {
		return bluePrintFactory.scrollComposite();
	}

	@Override
	public ISplitCompositeBluePrint splitComposite() {
		return bluePrintFactory.splitComposite();
	}

	@Override
	public ITextLabelBluePrint textLabel() {
		return bluePrintFactory.textLabel();
	}

	@Override
	public IIconBluePrint icon() {
		return bluePrintFactory.icon();
	}

	@Override
	public ISeparatorBluePrint separator() {
		return bluePrintFactory.separator();
	}

	@Override
	public ITextFieldBluePrint textField() {
		return bluePrintFactory.textField();
	}

	@Override
	public IButtonBluePrint button() {
		return bluePrintFactory.button();
	}

	@Override
	public ICheckBoxBluePrint checkBox() {
		return bluePrintFactory.checkBox();
	}

	@Override
	public IToggleButtonBluePrint toggleButton() {
		return bluePrintFactory.toggleButton();
	}

	@Override
	public <INPUT_TYPE> IComboBoxBluePrint<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter) {
		return bluePrintFactory.comboBox(converter);
	}

	@Override
	public <INPUT_TYPE> IComboBoxSelectionBluePrint<INPUT_TYPE> comboBoxSelection(
		final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {
		return bluePrintFactory.comboBoxSelection(objectStringConverter);
	}

	@Override
	public IInputFieldBluePrint<String> inputFieldString() {
		return bluePrintFactory.inputFieldString();
	}

	@Override
	public IInputFieldBluePrint<Long> inputFieldLongNumber() {
		return bluePrintFactory.inputFieldLongNumber();
	}

	@Override
	public IInputFieldBluePrint<Integer> inputFieldIntegerNumber() {
		return bluePrintFactory.inputFieldIntegerNumber();
	}

	@Override
	public IInputFieldBluePrint<Short> inputFieldShortNumber() {
		return bluePrintFactory.inputFieldShortNumber();
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon) {
		return bluePrintFactory.label(icon);
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon, final String text) {
		return bluePrintFactory.label(icon, text);
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext) {
		return bluePrintFactory.label(icon, text, toolTiptext);
	}

	@Override
	public ITextSeparatorBluePrint textSeparator(final String text) {
		return bluePrintFactory.textSeparator(text);
	}

	@Override
	public ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText) {
		return bluePrintFactory.textSeparator(text, tooltipText);
	}

	@Override
	public IQuestionDialogBluePrint yesNoQuestion() {
		return bluePrintFactory.yesNoQuestion();
	}

	@Override
	public IQuestionDialogBluePrint yesNoCancelQuestion() {
		return bluePrintFactory.yesNoCancelQuestion();
	}

	@Override
	public IQuestionDialogBluePrint yesNoQuestion(final String question) {
		return bluePrintFactory.yesNoQuestion(question);
	}

	@Override
	public IQuestionDialogBluePrint yesNoCancelQuestion(final String question) {
		return bluePrintFactory.yesNoCancelQuestion(question);
	}

	@Override
	public IMessageDialogBluePrint infoDialog() {
		return bluePrintFactory.infoDialog();
	}

	@Override
	public IMessageDialogBluePrint warningDialog() {
		return bluePrintFactory.warningDialog();
	}

	@Override
	public IMessageDialogBluePrint errorDialog() {
		return bluePrintFactory.errorDialog();
	}

	@Override
	public IMessageDialogBluePrint infoDialog(final String message) {
		return bluePrintFactory.infoDialog(message);
	}

	@Override
	public IMessageDialogBluePrint warningDialog(final String message) {
		return bluePrintFactory.warningDialog(message);
	}

	@Override
	public IMessageDialogBluePrint errorDialog(final String message) {
		return bluePrintFactory.errorDialog(message);
	}

	@Override
	public IProgressBarBluePrint progressBar(final int minimum, final int maximum) {
		return bluePrintFactory.progressBar(minimum, maximum);
	}

	@Override
	public IProgressBarBluePrint progressBar(final int maximum) {
		return bluePrintFactory.progressBar(maximum);
	}

	@Override
	public IActionMenuItemBluePrint menuItem() {
		return bluePrintFactory.menuItem();
	}

	@Override
	public IActionMenuItemBluePrint menuItem(final String text) {
		return bluePrintFactory.menuItem(text);
	}

	@Override
	public IRadioMenuItemBluePrint radioMenuItem(final String text) {
		return bluePrintFactory.radioMenuItem(text);
	}

	@Override
	public ICheckedMenuItemBluePrint checkedMenuItem(final String text) {
		return bluePrintFactory.checkedMenuItem(text);
	}

	@Override
	public IRadioMenuItemBluePrint radioMenuItem() {
		return bluePrintFactory.radioMenuItem();
	}

	@Override
	public ICheckedMenuItemBluePrint checkedMenuItem() {
		return bluePrintFactory.checkedMenuItem();
	}

	@Override
	public ISubMenuBluePrint subMenu(final String text) {
		return bluePrintFactory.subMenu(text);
	}

	@Override
	public ISubMenuBluePrint subMenu() {
		return bluePrintFactory.subMenu();
	}

	@Override
	public IMainMenuBluePrint mainMenu() {
		return bluePrintFactory.mainMenu();
	}

	@Override
	public IMainMenuBluePrint mainMenu(final String text) {
		return bluePrintFactory.mainMenu(text);
	}

	@Override
	public IToolBarBluePrint toolBar() {
		return bluePrintFactory.toolBar();
	}

	@Override
	public IToolBarButtonBluePrint toolBarButton() {
		return bluePrintFactory.toolBarButton();
	}

	@Override
	public IToolBarToggleButtonBluePrint toolBarToggleButton() {
		return bluePrintFactory.toolBarToggleButton();
	}

	@Override
	public IToolBarPopupButtonBluePrint toolBarPopupButton() {
		return bluePrintFactory.toolBarPopupButton();
	}

	@Override
	public IToolBarContainerItemBluePrint toolBarContainerItem() {
		return bluePrintFactory.toolBarContainerItem();
	}

}

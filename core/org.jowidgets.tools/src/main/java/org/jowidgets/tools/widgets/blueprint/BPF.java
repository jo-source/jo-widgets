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

package org.jowidgets.tools.widgets.blueprint;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.login.ILoginInterceptor;
import org.jowidgets.api.model.levelmeter.ILevelMeterModel;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.password.IPasswordChangeExecutor;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICalendarBluePrint;
import org.jowidgets.api.widgets.blueprint.ICanvasBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckedMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputControlBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ICombinedCollectionInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IDirectoryChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.IExpandCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IFileChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IIconBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ILevelMeterBluePrint;
import org.jowidgets.api.widgets.blueprint.ILoginDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IMainMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IPasswordChangeDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IPopupDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IRadioMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorToolBarItemBluePrint;
import org.jowidgets.api.widgets.blueprint.ISliderBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISubMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.ITabFolderBluePrint;
import org.jowidgets.api.widgets.blueprint.ITabItemBluePrint;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextAreaBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextSeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarContainerItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarPopupButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.ITreeNodeBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationResultLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.setup.ICollectionInputControlSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;

public final class BPF {

	private BPF() {}

	public static void addDefaultsInitializer(
		@SuppressWarnings("rawtypes") final Class<? extends ISetupBuilder> setupBuilder,
		final IDefaultInitializer<?> defaultInitializer) {
		Toolkit.getBluePrintFactory().addDefaultsInitializer(setupBuilder, defaultInitializer);
	}

	public static IFrameBluePrint frame() {
		return Toolkit.getBluePrintFactory().frame();
	}

	public static IDialogBluePrint dialog() {
		return Toolkit.getBluePrintFactory().dialog();
	}

	public static IPopupDialogBluePrint popupDialog() {
		return Toolkit.getBluePrintFactory().popupDialog();
	}

	public static IFileChooserBluePrint fileChooser(final FileChooserType type) {
		return Toolkit.getBluePrintFactory().fileChooser(type);
	}

	public static IDirectoryChooserBluePrint directoryChooser() {
		return Toolkit.getBluePrintFactory().directoryChooser();
	}

	public static ICompositeBluePrint composite() {
		return Toolkit.getBluePrintFactory().composite();
	}

	public static IScrollCompositeBluePrint scrollComposite() {
		return Toolkit.getBluePrintFactory().scrollComposite();
	}

	public static ISplitCompositeBluePrint splitComposite() {
		return Toolkit.getBluePrintFactory().splitComposite();
	}

	public static ITextLabelBluePrint textLabel() {
		return Toolkit.getBluePrintFactory().textLabel();
	}

	public static IIconBluePrint icon() {
		return Toolkit.getBluePrintFactory().icon();
	}

	public static ISeparatorBluePrint separator() {
		return Toolkit.getBluePrintFactory().separator();
	}

	public static ITextFieldBluePrint textField() {
		return Toolkit.getBluePrintFactory().textField();
	}

	public static ITextAreaBluePrint textArea() {
		return Toolkit.getBluePrintFactory().textArea();
	}

	public static IButtonBluePrint button() {
		return Toolkit.getBluePrintFactory().button();
	}

	public static IButtonBluePrint buttonOk() {
		return Toolkit.getBluePrintFactory().buttonOk();
	}

	public static IButtonBluePrint buttonClose() {
		return Toolkit.getBluePrintFactory().buttonClose();
	}

	public static IButtonBluePrint buttonDetails() {
		return Toolkit.getBluePrintFactory().buttonDetails();
	}

	public static IButtonBluePrint buttonCancel() {
		return Toolkit.getBluePrintFactory().buttonCancel();
	}

	public static ICheckBoxBluePrint checkBox() {
		return Toolkit.getBluePrintFactory().checkBox();
	}

	public static IToggleButtonBluePrint toggleButton() {
		return Toolkit.getBluePrintFactory().toggleButton();
	}

	public static <INPUT_TYPE> IComboBoxBluePrint<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter) {
		return Toolkit.getBluePrintFactory().comboBox(converter);
	}

	public static <INPUT_TYPE> IComboBoxSelectionBluePrint<INPUT_TYPE> comboBoxSelection(
		final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {
		return Toolkit.getBluePrintFactory().comboBoxSelection(objectStringConverter);
	}

	public static IActionMenuItemBluePrint menuItem() {
		return Toolkit.getBluePrintFactory().menuItem();
	}

	public static IRadioMenuItemBluePrint radioMenuItem() {
		return Toolkit.getBluePrintFactory().radioMenuItem();
	}

	public static ICheckedMenuItemBluePrint checkedMenuItem() {
		return Toolkit.getBluePrintFactory().checkedMenuItem();
	}

	public static ISubMenuBluePrint subMenu() {
		return Toolkit.getBluePrintFactory().subMenu();
	}

	public static IMainMenuBluePrint mainMenu() {
		return Toolkit.getBluePrintFactory().mainMenu();
	}

	public static ISeparatorMenuItemBluePrint menuSeparator() {
		return Toolkit.getBluePrintFactory().menuSeparator();
	}

	public static ISeparatorToolBarItemBluePrint toolBarSeparator() {
		return Toolkit.getBluePrintFactory().toolBarSeparator();
	}

	public static IToolBarBluePrint toolBar() {
		return Toolkit.getBluePrintFactory().toolBar();
	}

	public static IToolBarButtonBluePrint toolBarButton() {
		return Toolkit.getBluePrintFactory().toolBarButton();
	}

	public static IToolBarToggleButtonBluePrint toolBarToggleButton() {
		return Toolkit.getBluePrintFactory().toolBarToggleButton();
	}

	public static IToolBarPopupButtonBluePrint toolBarPopupButton() {
		return Toolkit.getBluePrintFactory().toolBarPopupButton();
	}

	public static IToolBarMenuBluePrint toolBarMenu() {
		return Toolkit.getBluePrintFactory().toolBarMenu();
	}

	public static IToolBarContainerItemBluePrint toolBarContainerItem() {
		return Toolkit.getBluePrintFactory().toolBarContainerItem();
	}

	public static ITabFolderBluePrint tabFolder() {
		return Toolkit.getBluePrintFactory().tabFolder();
	}

	public static ITabItemBluePrint tabItem() {
		return Toolkit.getBluePrintFactory().tabItem();
	}

	public static ITreeBluePrint tree() {
		return Toolkit.getBluePrintFactory().tree();
	}

	public static ITreeNodeBluePrint treeNode() {
		return Toolkit.getBluePrintFactory().treeNode();
	}

	public static ITableBluePrint table(final ITableModel model) {
		return Toolkit.getBluePrintFactory().table(model);
	}

	public static ITableBluePrint table(final ITableColumnModel columnModel, final ITableDataModel dataModel) {
		return Toolkit.getBluePrintFactory().table(columnModel, dataModel);
	}

	public static ICalendarBluePrint calendar() {
		return Toolkit.getBluePrintFactory().calendar();
	}

	public static ISliderBluePrint slider() {
		return Toolkit.getBluePrintFactory().slider();
	}

	public static ILabelBluePrint label() {
		return Toolkit.getBluePrintFactory().label();
	}

	public static ITextSeparatorBluePrint textSeparator() {
		return Toolkit.getBluePrintFactory().textSeparator();
	}

	public static IInputComponentValidationLabelBluePrint inputComponentValidationLabel(final IInputComponent<?> inputComponent) {
		return Toolkit.getBluePrintFactory().inputComponentValidationLabel(inputComponent);
	}

	public static IInputComponentValidationLabelBluePrint inputComponentValidationLabel() {
		return Toolkit.getBluePrintFactory().inputComponentValidationLabel();
	}

	public static IValidationResultLabelBluePrint validationResultLabel() {
		return Toolkit.getBluePrintFactory().validationResultLabel();
	}

	public static <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IConverter<INPUT_TYPE> converter) {
		return Toolkit.getBluePrintFactory().inputField(converter);
	}

	public static <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IObjectStringConverter<INPUT_TYPE> converter) {
		return Toolkit.getBluePrintFactory().inputField(converter);
	}

	public static <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField() {
		return Toolkit.getBluePrintFactory().inputField();
	}

	public static IMessageDialogBluePrint messageDialog() {
		return Toolkit.getBluePrintFactory().messageDialog();
	}

	public static IQuestionDialogBluePrint questionDialog() {
		return Toolkit.getBluePrintFactory().questionDialog();
	}

	public static IProgressBarBluePrint progressBar() {
		return Toolkit.getBluePrintFactory().progressBar();
	}

	public static <INPUT_TYPE> IInputDialogBluePrint<INPUT_TYPE> inputDialog(final IInputContentCreator<INPUT_TYPE> contentCreator) {
		return Toolkit.getBluePrintFactory().inputDialog(contentCreator);
	}

	public static <INPUT_TYPE> IInputCompositeBluePrint<INPUT_TYPE> inputComposite(
		final IInputContentCreator<INPUT_TYPE> contentCreator) {
		return Toolkit.getBluePrintFactory().inputComposite(contentCreator);
	}

	public static ILoginDialogBluePrint loginDialog(final ILoginInterceptor interceptor) {
		return Toolkit.getBluePrintFactory().loginDialog(interceptor);
	}

	public static IPasswordChangeDialogBluePrint passwordChangeDialog(final IPasswordChangeExecutor executor) {
		return Toolkit.getBluePrintFactory().passwordChangeDialog(executor);
	}

	public static <ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
		final ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> widgetCreator) {
		return Toolkit.getBluePrintFactory().collectionInputControl(widgetCreator);
	}

	public static <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
		final ICollectionInputControlSetup<ELEMENT_TYPE> setup) {
		return Toolkit.getBluePrintFactory().collectionInputDialog(setup);
	}

	public static <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(
		final IConverter<ELEMENT_TYPE> converter) {
		return Toolkit.getBluePrintFactory().collectionInputField(converter);
	}

	public static <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(
		final IObjectStringConverter<ELEMENT_TYPE> converter) {
		return Toolkit.getBluePrintFactory().collectionInputField(converter);
	}

	public static <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField() {
		return Toolkit.getBluePrintFactory().collectionInputField();
	}

	public static <ELEMENT_TYPE> ICombinedCollectionInputFieldBluePrint<ELEMENT_TYPE> combinedCollectionInputField(
		final ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> elementTypeCreator,
		final ICustomWidgetCreator<IInputControl<? extends Collection<ELEMENT_TYPE>>> collectionTypeCreator) {
		return Toolkit.getBluePrintFactory().combinedCollectionInputField(elementTypeCreator, collectionTypeCreator);
	}

	public static IInputFieldBluePrint<String> inputFieldString() {
		return Toolkit.getBluePrintFactory().inputFieldString();
	}

	public static IInputFieldBluePrint<Long> inputFieldLongNumber() {
		return Toolkit.getBluePrintFactory().inputFieldLongNumber();
	}

	public static IInputFieldBluePrint<Integer> inputFieldIntegerNumber() {
		return Toolkit.getBluePrintFactory().inputFieldIntegerNumber();
	}

	public static IInputFieldBluePrint<Short> inputFieldShortNumber() {
		return Toolkit.getBluePrintFactory().inputFieldShortNumber();
	}

	public static IInputFieldBluePrint<Date> inputFieldDate(
		final DateFormat dateFormat,
		final String formatHint,
		final ITextMask mask) {
		return Toolkit.getBluePrintFactory().inputFieldDate(dateFormat, formatHint, mask);
	}

	public static IInputFieldBluePrint<Date> inputFieldDate(final DateFormat dateFormat, final String formatHint) {
		return Toolkit.getBluePrintFactory().inputFieldDate(dateFormat, formatHint);
	}

	public static IInputFieldBluePrint<Date> inputFieldDate() {
		return Toolkit.getBluePrintFactory().inputFieldDate();
	}

	public static IInputFieldBluePrint<Date> inputFieldDateTime() {
		return Toolkit.getBluePrintFactory().inputFieldDateTime();
	}

	public static IInputFieldBluePrint<Date> inputFieldTime() {
		return Toolkit.getBluePrintFactory().inputFieldTime();
	}

	public static ILabelBluePrint label(final IImageConstant icon) {
		return Toolkit.getBluePrintFactory().label(icon);
	}

	public static ILabelBluePrint label(final IImageConstant icon, final String text) {
		return Toolkit.getBluePrintFactory().label(icon, text);
	}

	public static ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext) {
		return Toolkit.getBluePrintFactory().label(icon, text, toolTiptext);
	}

	public static ITextSeparatorBluePrint textSeparator(final String text) {
		return Toolkit.getBluePrintFactory().textSeparator(text);
	}

	public static ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText) {
		return Toolkit.getBluePrintFactory().textSeparator(text, tooltipText);
	}

	public static IQuestionDialogBluePrint yesNoQuestion() {
		return Toolkit.getBluePrintFactory().yesNoQuestion();
	}

	public static IQuestionDialogBluePrint yesNoCancelQuestion() {
		return Toolkit.getBluePrintFactory().yesNoCancelQuestion();
	}

	public static IQuestionDialogBluePrint yesNoQuestion(final String question) {
		return Toolkit.getBluePrintFactory().yesNoQuestion(question);
	}

	public static IQuestionDialogBluePrint yesNoCancelQuestion(final String question) {
		return Toolkit.getBluePrintFactory().yesNoCancelQuestion(question);
	}

	public static IMessageDialogBluePrint infoDialog() {
		return Toolkit.getBluePrintFactory().infoDialog();
	}

	public static IMessageDialogBluePrint warningDialog() {
		return Toolkit.getBluePrintFactory().warningDialog();
	}

	public static IMessageDialogBluePrint errorDialog() {
		return Toolkit.getBluePrintFactory().errorDialog();
	}

	public static IMessageDialogBluePrint infoDialog(final String message) {
		return Toolkit.getBluePrintFactory().infoDialog(message);
	}

	public static IMessageDialogBluePrint warningDialog(final String message) {
		return Toolkit.getBluePrintFactory().warningDialog(message);
	}

	public static IMessageDialogBluePrint errorDialog(final String message) {
		return Toolkit.getBluePrintFactory().errorDialog(message);
	}

	public static IProgressBarBluePrint progressBar(final int minimum, final int maximum) {
		return Toolkit.getBluePrintFactory().progressBar(minimum, maximum);
	}

	public static IProgressBarBluePrint progressBar(final int maximum) {
		return Toolkit.getBluePrintFactory().progressBar(maximum);
	}

	public static <ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
		final IWidgetDescriptor<? extends IInputControl<ELEMENT_TYPE>> descriptor) {
		return Toolkit.getBluePrintFactory().collectionInputControl(descriptor);
	}

	public static <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
		final ICustomWidgetCreator<? extends IInputControl<ELEMENT_TYPE>> widgetCreator) {
		return Toolkit.getBluePrintFactory().collectionInputDialog(widgetCreator);
	}

	public static <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
		final IWidgetDescriptor<? extends IInputControl<ELEMENT_TYPE>> descriptor) {
		return Toolkit.getBluePrintFactory().collectionInputDialog(descriptor);
	}

	public static IFrameBluePrint frame(final String title) {
		return Toolkit.getBluePrintFactory().frame(title);
	}

	public static IFrameBluePrint frame(final String title, final IImageConstant icon) {
		return Toolkit.getBluePrintFactory().frame(title, icon);
	}

	public static IDialogBluePrint dialog(final String title) {
		return Toolkit.getBluePrintFactory().dialog(title);
	}

	public static IDialogBluePrint dialog(final String title, final IImageConstant icon) {
		return Toolkit.getBluePrintFactory().dialog(title, icon);
	}

	public static ICompositeBluePrint compositeWithBorder() {
		return Toolkit.getBluePrintFactory().compositeWithBorder();
	}

	public static ICompositeBluePrint composite(final String borderTitle) {
		return Toolkit.getBluePrintFactory().composite(borderTitle);
	}

	public static IScrollCompositeBluePrint scrollCompositeWithBorder() {
		return Toolkit.getBluePrintFactory().scrollCompositeWithBorder();
	}

	public static IScrollCompositeBluePrint scrollComposite(final String borderTitle) {
		return Toolkit.getBluePrintFactory().scrollComposite(borderTitle);
	}

	public static ISplitCompositeBluePrint splitHorizontal() {
		return Toolkit.getBluePrintFactory().splitHorizontal();
	}

	public static ISplitCompositeBluePrint splitVertical() {
		return Toolkit.getBluePrintFactory().splitVertical();
	}

	public static IButtonBluePrint button(final String text) {
		return Toolkit.getBluePrintFactory().button(text);
	}

	public static IButtonBluePrint button(final String text, final String toolTipText) {
		return Toolkit.getBluePrintFactory().button(text, toolTipText);
	}

	public static IIconBluePrint icon(final IImageConstant icon) {
		return Toolkit.getBluePrintFactory().icon(icon);
	}

	public static ITextLabelBluePrint textLabel(final String text) {
		return Toolkit.getBluePrintFactory().textLabel(text);
	}

	public static ITextLabelBluePrint textLabel(final String text, final String tooltipText) {
		return Toolkit.getBluePrintFactory().textLabel(text, tooltipText);
	}

	public static IComboBoxBluePrint<String> comboBox() {
		return Toolkit.getBluePrintFactory().comboBox();
	}

	public static IComboBoxBluePrint<String> comboBox(final String... elements) {
		return Toolkit.getBluePrintFactory().comboBox(elements);
	}

	public static IComboBoxBluePrint<String> comboBox(final List<String> elements) {
		return Toolkit.getBluePrintFactory().comboBox(elements);
	}

	public static IComboBoxBluePrint<String> comboBoxString() {
		return Toolkit.getBluePrintFactory().comboBoxString();
	}

	public static IComboBoxBluePrint<Long> comboBoxLongNumber() {
		return Toolkit.getBluePrintFactory().comboBoxLongNumber();
	}

	public static IComboBoxBluePrint<Integer> comboBoxIntegerNumber() {
		return Toolkit.getBluePrintFactory().comboBoxIntegerNumber();
	}

	public static IComboBoxBluePrint<Short> comboBoxShortNumber() {
		return Toolkit.getBluePrintFactory().comboBoxShortNumber();
	}

	public static IComboBoxSelectionBluePrint<String> comboBoxSelection() {
		return Toolkit.getBluePrintFactory().comboBoxSelection();
	}

	public static IComboBoxSelectionBluePrint<String> comboBoxSelection(final String... elements) {
		return Toolkit.getBluePrintFactory().comboBoxSelection(elements);
	}

	public static IComboBoxSelectionBluePrint<String> comboBoxSelection(final List<String> elements) {
		return Toolkit.getBluePrintFactory().comboBoxSelection(elements);
	}

	public static <ENUM_TYPE extends Enum<?>> IComboBoxSelectionBluePrint<ENUM_TYPE> comboBoxSelection(
		final ENUM_TYPE... enumValues) {
		return Toolkit.getBluePrintFactory().comboBoxSelection(enumValues);
	}

	public static <VALUE_TYPE> IComboBoxSelectionBluePrint<VALUE_TYPE> comboBoxSelection(final Collection<VALUE_TYPE> elements) {
		return Toolkit.getBluePrintFactory().comboBoxSelection(elements);
	}

	public static IComboBoxSelectionBluePrint<String> comboBoxSelectionString() {
		return Toolkit.getBluePrintFactory().comboBoxSelectionString();
	}

	public static IComboBoxSelectionBluePrint<Long> comboBoxSelectionLongNumber() {
		return Toolkit.getBluePrintFactory().comboBoxSelectionLongNumber();
	}

	public static IComboBoxSelectionBluePrint<Integer> comboBoxSelectionIntegerNumber() {
		return Toolkit.getBluePrintFactory().comboBoxSelectionIntegerNumber();
	}

	public static IComboBoxSelectionBluePrint<Short> comboBoxSelectionShortNumber() {
		return Toolkit.getBluePrintFactory().comboBoxSelectionShortNumber();
	}

	public static IComboBoxSelectionBluePrint<Boolean> comboBoxSelectionBoolean() {
		return Toolkit.getBluePrintFactory().comboBoxSelectionBoolean();
	}

	public static IActionMenuItemBluePrint menuItem(final String text) {
		return Toolkit.getBluePrintFactory().menuItem(text);
	}

	public static IRadioMenuItemBluePrint radioMenuItem(final String text) {
		return Toolkit.getBluePrintFactory().radioMenuItem(text);
	}

	public static ICheckedMenuItemBluePrint checkedMenuItem(final String text) {
		return Toolkit.getBluePrintFactory().checkedMenuItem(text);
	}

	public static ISubMenuBluePrint subMenu(final String text) {
		return Toolkit.getBluePrintFactory().subMenu(text);
	}

	public static IMainMenuBluePrint mainMenu(final String text) {
		return Toolkit.getBluePrintFactory().mainMenu(text);
	}

	public static IExpandCompositeBluePrint expandComposite() {
		return Toolkit.getBluePrintFactory().expandComposite();
	}

	public static ICanvasBluePrint canvas() {
		return Toolkit.getBluePrintFactory().canvas();
	}

	public static ILevelMeterBluePrint levelMeter(final ILevelMeterModel model) {
		return Toolkit.getBluePrintFactory().levelMeter(model);
	}
}

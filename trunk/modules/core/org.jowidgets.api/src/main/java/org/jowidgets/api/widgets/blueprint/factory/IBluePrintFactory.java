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

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.convert.ISliderViewerConverter;
import org.jowidgets.api.login.ILoginInterceptor;
import org.jowidgets.api.model.levelmeter.ILevelMeterModel;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.password.IPasswordChangeExecutor;
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
import org.jowidgets.api.widgets.blueprint.ISliderViewerBluePrint;
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
import org.jowidgets.api.widgets.blueprint.ITreeViewerBluePrint;
import org.jowidgets.api.widgets.blueprint.IUnitValueFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationResultLabelBluePrint;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.IInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.ICollectionInputControlSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitConverter;
import org.jowidgets.unit.api.IUnitSet;

public interface IBluePrintFactory extends IBaseBluePrintFactory {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////// basic widgets//////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    IFrameBluePrint frame();

    IDialogBluePrint dialog();

    IPopupDialogBluePrint popupDialog();

    IFileChooserBluePrint fileChooser(FileChooserType type);

    IDirectoryChooserBluePrint directoryChooser();

    ICompositeBluePrint composite();

    IScrollCompositeBluePrint scrollComposite();

    ISplitCompositeBluePrint splitComposite();

    ITextLabelBluePrint textLabel();

    IIconBluePrint icon();

    ISeparatorBluePrint separator();

    ITextFieldBluePrint textField();

    ITextAreaBluePrint textArea();

    IButtonBluePrint button();

    ICheckBoxBluePrint checkBox();

    IToggleButtonBluePrint toggleButton();

    <INPUT_TYPE> IComboBoxBluePrint<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter);

    <INPUT_TYPE> IComboBoxSelectionBluePrint<INPUT_TYPE> comboBoxSelection(
        final IObjectStringConverter<INPUT_TYPE> objectStringConverter);

    IActionMenuItemBluePrint menuItem();

    IRadioMenuItemBluePrint radioMenuItem();

    ICheckedMenuItemBluePrint checkedMenuItem();

    ISubMenuBluePrint subMenu();

    IMainMenuBluePrint mainMenu();

    ISeparatorMenuItemBluePrint menuSeparator();

    ISeparatorToolBarItemBluePrint toolBarSeparator();

    IToolBarBluePrint toolBar();

    IToolBarButtonBluePrint toolBarButton();

    IToolBarToggleButtonBluePrint toolBarToggleButton();

    IToolBarPopupButtonBluePrint toolBarPopupButton();

    IToolBarMenuBluePrint toolBarMenu();

    IToolBarContainerItemBluePrint toolBarContainerItem();

    ITabFolderBluePrint tabFolder();

    ITabItemBluePrint tabItem();

    ITreeBluePrint tree();

    <ROOT_NODE_VALUE_TYPE> ITreeViewerBluePrint<ROOT_NODE_VALUE_TYPE> treeViewer(ITreeNodeModel<ROOT_NODE_VALUE_TYPE> model);

    ITreeNodeBluePrint treeNode();

    ITableBluePrint table(ITableModel model);

    ITableBluePrint table(ITableColumnModel columnModel, ITableDataModel dataModel);

    ICalendarBluePrint calendar();

    ISliderBluePrint slider();

    ICanvasBluePrint canvas();

    ILevelMeterBluePrint levelMeter(ILevelMeterModel model);

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

    IButtonBluePrint buttonSave();

    IButtonBluePrint buttonClose();

    IButtonBluePrint buttonDetails();

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

    <VALUE_TYPE> IComboBoxSelectionBluePrint<VALUE_TYPE> comboBoxSelection(Collection<VALUE_TYPE> elements);

    IComboBoxSelectionBluePrint<String> comboBoxSelection(List<String> elements);

    <ENUM_TYPE extends Enum<?>> IComboBoxSelectionBluePrint<ENUM_TYPE> comboBoxSelection(ENUM_TYPE... enumValues);

    IComboBoxSelectionBluePrint<IUnit> comboBoxSelection(IUnitSet unitSet);

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////// composite widgets//////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ILabelBluePrint label();

    ITextSeparatorBluePrint textSeparator();

    IInputComponentValidationLabelBluePrint inputComponentValidationLabel(IInputComponent<?> inputComponent);

    IInputComponentValidationLabelBluePrint inputComponentValidationLabel();

    IValidationResultLabelBluePrint validationResultLabel();

    <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField();

    <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IConverter<INPUT_TYPE> converter);

    <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IObjectStringConverter<INPUT_TYPE> converter);

    <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField();

    <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField(
        Class<? extends UNIT_VALUE_TYPE> inputFieldType);

    <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField(
        IUnitSet unitSet,
        IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> converter,
        IInputFieldDescriptor<UNIT_VALUE_TYPE> inputField);

    <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField(
        IUnitSet unitSet,
        IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> converter,
        Class<? extends UNIT_VALUE_TYPE> inputFieldType);

    IMessageDialogBluePrint messageDialog();

    IQuestionDialogBluePrint questionDialog();

    IProgressBarBluePrint progressBar();

    <INPUT_TYPE> IInputDialogBluePrint<INPUT_TYPE> inputDialog(final IInputContentCreator<INPUT_TYPE> contentCreator);

    <INPUT_TYPE> IInputCompositeBluePrint<INPUT_TYPE> inputComposite(final IInputContentCreator<INPUT_TYPE> contentCreator);

    ILoginDialogBluePrint loginDialog(ILoginInterceptor interceptor);

    IPasswordChangeDialogBluePrint passwordChangeDialog(IPasswordChangeExecutor executor);

    <ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
        ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> widgetCreator);

    <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
        ICollectionInputControlSetup<ELEMENT_TYPE> setup);

    <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(IConverter<ELEMENT_TYPE> converter);

    <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(
        IObjectStringConverter<ELEMENT_TYPE> converter);

    <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField();

    <ELEMENT_TYPE> ICombinedCollectionInputFieldBluePrint<ELEMENT_TYPE> combinedCollectionInputField(
        ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> elementTypeCreator,
        ICustomWidgetCreator<IInputControl<? extends Collection<ELEMENT_TYPE>>> collectionTypeCreator);

    IExpandCompositeBluePrint expandComposite();

    <VALUE_TYPE> ISliderViewerBluePrint<VALUE_TYPE> sliderViewer();

    <VALUE_TYPE> ISliderViewerBluePrint<VALUE_TYPE> sliderViewer(ISliderViewerConverter<VALUE_TYPE> converter);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////// some input fields here/////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    IInputFieldBluePrint<String> inputFieldString();

    IInputFieldBluePrint<Long> inputFieldLongNumber();

    IInputFieldBluePrint<Integer> inputFieldIntegerNumber();

    IInputFieldBluePrint<Short> inputFieldShortNumber();

    IInputFieldBluePrint<Double> inputFieldDoubleNumber();

    IInputFieldBluePrint<Double> inputFieldDoubleNumber(int minFractionDigits, int maxFractionDigits);

    IInputFieldBluePrint<Date> inputFieldDate(DateFormat dateFormat, String formatHint, ITextMask mask);

    IInputFieldBluePrint<Date> inputFieldDate(DateFormat dateFormat, String formatHint);

    IInputFieldBluePrint<Date> inputFieldDate();

    IInputFieldBluePrint<Date> inputFieldDateTime();

    IInputFieldBluePrint<Date> inputFieldTime();

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////some convenience methods starting here///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    ILabelBluePrint label(final IImageConstant icon);

    ILabelBluePrint label(final IImageConstant icon, final String text);

    ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext);

    ITextSeparatorBluePrint textSeparator(final String text);

    ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText);

    IQuestionDialogBluePrint yesNoQuestion();

    IQuestionDialogBluePrint yesNoCancelQuestion();

    IQuestionDialogBluePrint yesNoQuestion(String question);

    IQuestionDialogBluePrint yesNoCancelQuestion(String question);

    IMessageDialogBluePrint infoDialog();

    IMessageDialogBluePrint warningDialog();

    IMessageDialogBluePrint errorDialog();

    IMessageDialogBluePrint infoDialog(String message);

    IMessageDialogBluePrint warningDialog(String message);

    IMessageDialogBluePrint errorDialog(String message);

    IProgressBarBluePrint progressBar(int minimum, int maximum);

    IProgressBarBluePrint progressBar(int maximum);

    <ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
        IWidgetDescriptor<? extends IInputControl<ELEMENT_TYPE>> descriptor);

    <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
        ICustomWidgetCreator<? extends IInputControl<ELEMENT_TYPE>> widgetCreator);

    <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
        IWidgetDescriptor<? extends IInputControl<ELEMENT_TYPE>> descriptor);

    ISliderViewerBluePrint<Double> sliderViewerDouble();

    ISliderViewerBluePrint<Double> sliderViewerDouble(double max);

    ISliderViewerBluePrint<Double> sliderViewerDouble(double min, double max);

    ISliderViewerBluePrint<Float> sliderViewerFloat();

    ISliderViewerBluePrint<Float> sliderViewerFloat(float max);

    ISliderViewerBluePrint<Float> sliderViewerFloat(float min, float max);

    ISliderViewerBluePrint<Integer> sliderViewerInteger(int max);

    ISliderViewerBluePrint<Integer> sliderViewerInteger(int min, int max);

    ISliderViewerBluePrint<Long> sliderViewerLong(long max);

    ISliderViewerBluePrint<Long> sliderViewerLong(long min, long max);
}

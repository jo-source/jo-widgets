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
package org.jowidgets.impl.widgets.basic.blueprint;

import java.util.Collection;
import java.util.List;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.model.levelmeter.ILevelMeterModel;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICalendarBluePrint;
import org.jowidgets.api.widgets.blueprint.ICanvasBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckedMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IDirectoryChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.IFileChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IIconBluePrint;
import org.jowidgets.api.widgets.blueprint.ILevelMeterBluePrint;
import org.jowidgets.api.widgets.blueprint.IMainMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.IPopupDialogBluePrint;
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
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintProxyFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.impl.base.blueprint.factory.BaseBluePrintFactory;
import org.jowidgets.impl.convert.DefaultObjectStringConverter;
import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitSet;
import org.jowidgets.util.Assert;
import org.jowidgets.util.CollectionUtils;

public abstract class AbstractBasicBluePrintFactory extends BaseBluePrintFactory implements IBluePrintFactory {

    public AbstractBasicBluePrintFactory(final IBluePrintProxyFactory bluePrintProxyFactor) {
        super(bluePrintProxyFactor);
    }

    @Override
    public final IFrameBluePrint frame() {
        return createProxy(IFrameBluePrint.class);
    }

    @Override
    public final IDialogBluePrint dialog() {
        return createProxy(IDialogBluePrint.class);
    }

    @Override
    public IPopupDialogBluePrint popupDialog() {
        return createProxy(IPopupDialogBluePrint.class);
    }

    @Override
    public IFileChooserBluePrint fileChooser(final FileChooserType type) {
        Assert.paramNotNull(type, "type");
        final IFileChooserBluePrint result = createProxy(IFileChooserBluePrint.class);
        result.setType(type);
        return result;
    }

    @Override
    public IDirectoryChooserBluePrint directoryChooser() {
        return createProxy(IDirectoryChooserBluePrint.class);
    }

    @Override
    public final ICompositeBluePrint composite() {
        return createProxy(ICompositeBluePrint.class);
    }

    @Override
    public final IScrollCompositeBluePrint scrollComposite() {
        return createProxy(IScrollCompositeBluePrint.class);
    }

    @Override
    public ISplitCompositeBluePrint splitComposite() {
        return createProxy(ISplitCompositeBluePrint.class);
    }

    @Override
    public final ITextLabelBluePrint textLabel() {
        return createProxy(ITextLabelBluePrint.class);
    }

    @Override
    public final IIconBluePrint icon() {
        return createProxy(IIconBluePrint.class);
    }

    @Override
    public final ISeparatorBluePrint separator() {
        return createProxy(ISeparatorBluePrint.class);
    }

    @Override
    public final ITextFieldBluePrint textField() {
        return createProxy(ITextFieldBluePrint.class);
    }

    @Override
    public ITextAreaBluePrint textArea() {
        return createProxy(ITextAreaBluePrint.class);
    }

    @Override
    public final IButtonBluePrint button() {
        return createProxy(IButtonBluePrint.class);
    }

    @Override
    public final ICheckBoxBluePrint checkBox() {
        return createProxy(ICheckBoxBluePrint.class);
    }

    @Override
    public final IToggleButtonBluePrint toggleButton() {
        return createProxy(IToggleButtonBluePrint.class);
    }

    @Override
    public final <INPUT_TYPE> IComboBoxBluePrint<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter) {

        final IComboBoxBluePrint<INPUT_TYPE> result = createProxy(IComboBoxBluePrint.class);

        return result.setObjectStringConverter(converter).setStringObjectConverter(converter);
    }

    @Override
    public final <INPUT_TYPE> IComboBoxSelectionBluePrint<INPUT_TYPE> comboBoxSelection(
        final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {

        final IComboBoxSelectionBluePrint<INPUT_TYPE> result = createProxy(IComboBoxSelectionBluePrint.class);

        return result.setObjectStringConverter(objectStringConverter);
    }

    @Override
    public IActionMenuItemBluePrint menuItem() {
        return createProxy(IActionMenuItemBluePrint.class);
    }

    @Override
    public IRadioMenuItemBluePrint radioMenuItem() {
        return createProxy(IRadioMenuItemBluePrint.class);
    }

    @Override
    public ICheckedMenuItemBluePrint checkedMenuItem() {
        return createProxy(ICheckedMenuItemBluePrint.class);
    }

    @Override
    public ISubMenuBluePrint subMenu() {
        return createProxy(ISubMenuBluePrint.class);
    }

    @Override
    public IMainMenuBluePrint mainMenu() {
        return createProxy(IMainMenuBluePrint.class);
    }

    @Override
    public IToolBarBluePrint toolBar() {
        return createProxy(IToolBarBluePrint.class);
    }

    @Override
    public IToolBarButtonBluePrint toolBarButton() {
        return createProxy(IToolBarButtonBluePrint.class);
    }

    @Override
    public IToolBarToggleButtonBluePrint toolBarToggleButton() {
        return createProxy(IToolBarToggleButtonBluePrint.class);
    }

    @Override
    public IToolBarPopupButtonBluePrint toolBarPopupButton() {
        return createProxy(IToolBarPopupButtonBluePrint.class);
    }

    @Override
    public IToolBarMenuBluePrint toolBarMenu() {
        return createProxy(IToolBarMenuBluePrint.class);
    }

    @Override
    public IToolBarContainerItemBluePrint toolBarContainerItem() {
        return createProxy(IToolBarContainerItemBluePrint.class);
    }

    @Override
    public ISeparatorMenuItemBluePrint menuSeparator() {
        return createProxy(ISeparatorMenuItemBluePrint.class);
    }

    @Override
    public ISeparatorToolBarItemBluePrint toolBarSeparator() {
        return createProxy(ISeparatorToolBarItemBluePrint.class);
    }

    @Override
    public ITabFolderBluePrint tabFolder() {
        return createProxy(ITabFolderBluePrint.class);
    }

    @Override
    public ITabItemBluePrint tabItem() {
        return createProxy(ITabItemBluePrint.class);
    }

    @Override
    public ITreeBluePrint tree() {
        return createProxy(ITreeBluePrint.class);
    }

    @Override
    public <ROOT_NODE_VALUE_TYPE> ITreeViewerBluePrint<ROOT_NODE_VALUE_TYPE> treeViewer(
        final ITreeNodeModel<ROOT_NODE_VALUE_TYPE> model) {
        final ITreeViewerBluePrint<ROOT_NODE_VALUE_TYPE> result = createProxy(ITreeViewerBluePrint.class);
        result.setRootNodeModel(model);
        return result;
    }

    @Override
    public ITreeNodeBluePrint treeNode() {
        return createProxy(ITreeNodeBluePrint.class);
    }

    @Override
    public ITableBluePrint table(final ITableModel model) {
        Assert.paramNotNull(model, "model");
        return table(model, model);
    }

    @Override
    public ITableBluePrint table(final ITableColumnModel columnModel, final ITableDataModel dataModel) {
        Assert.paramNotNull(columnModel, "columnModel");
        Assert.paramNotNull(dataModel, "dataModel");
        final ITableBluePrint result = createProxy(ITableBluePrint.class);
        result.setColumnModel(columnModel).setDataModel(dataModel);
        return result;
    }

    @Override
    public ICalendarBluePrint calendar() {
        return createProxy(ICalendarBluePrint.class);
    }

    @Override
    public ISliderBluePrint slider() {
        return createProxy(ISliderBluePrint.class);
    }

    @Override
    public ICanvasBluePrint canvas() {
        return createProxy(ICanvasBluePrint.class);
    }

    @Override
    public ILevelMeterBluePrint levelMeter(final ILevelMeterModel model) {
        final ILevelMeterBluePrint result = createProxy(ILevelMeterBluePrint.class);
        result.setModel(model);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////some convenience methods starting here///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final IFrameBluePrint frame(final String title) {
        return frame().setTitle(title);
    }

    @Override
    public final IFrameBluePrint frame(final String title, final IImageConstant icon) {
        return frame(title).setIcon(icon);
    }

    @Override
    public final IDialogBluePrint dialog(final String title) {
        return dialog().setTitle(title);
    }

    @Override
    public final IDialogBluePrint dialog(final String title, final IImageConstant icon) {
        return dialog(title).setIcon(icon);
    }

    @Override
    public final ICompositeBluePrint compositeWithBorder() {
        return composite().setBorder();
    }

    @Override
    public final ICompositeBluePrint composite(final String borderTitle) {
        return composite().setBorder(borderTitle);
    }

    @Override
    public ISplitCompositeBluePrint splitHorizontal() {
        return splitComposite().setOrientation(Orientation.HORIZONTAL);
    }

    @Override
    public ISplitCompositeBluePrint splitVertical() {
        return splitComposite().setOrientation(Orientation.VERTICAL);
    }

    @Override
    public final IButtonBluePrint button(final String text) {
        return button().setText(text);
    }

    @Override
    public final IButtonBluePrint button(final String text, final String toolTipText) {
        return button(text).setToolTipText(toolTipText);
    }

    @Override
    public IButtonBluePrint buttonOk() {
        return button(Messages.getString("BasicBluePrintFactory.ok"));
    }

    @Override
    public IButtonBluePrint buttonSave() {
        return button(Messages.getString("BasicBluePrintFactory.save"));
    }

    @Override
    public IButtonBluePrint buttonCancel() {
        return button(Messages.getString("BasicBluePrintFactory.cancel"));
    }

    @Override
    public IButtonBluePrint buttonClose() {
        return button(Messages.getString("BasicBluePrintFactory.close"));
    }

    @Override
    public IButtonBluePrint buttonDetails() {
        return button(Messages.getString("BasicBluePrintFactory.details"));
    }

    @Override
    public final IIconBluePrint icon(final IImageConstant icon) {
        return icon().setIcon(icon);
    }

    @Override
    public final ITextLabelBluePrint textLabel(final String text) {
        return textLabel().setText(text);
    }

    @Override
    public final ITextLabelBluePrint textLabel(final String text, final String tooltipText) {
        return textLabel(text).setToolTipText(tooltipText);
    }

    @Override
    public final IComboBoxBluePrint<String> comboBox() {
        return comboBox(Toolkit.getConverterProvider().string());
    }

    @Override
    public final IComboBoxBluePrint<String> comboBox(final String... elements) {
        return comboBox().setElements(elements);
    }

    @Override
    public IComboBoxBluePrint<String> comboBox(final List<String> elements) {
        return comboBox().setElements(elements);
    }

    @Override
    public final IComboBoxSelectionBluePrint<String> comboBoxSelection() {
        return comboBoxSelection(Toolkit.getConverterProvider().string());
    }

    @Override
    public final IComboBoxSelectionBluePrint<String> comboBoxSelection(final String... elements) {
        return comboBoxSelection().setElements(elements);
    }

    @Override
    public IComboBoxSelectionBluePrint<String> comboBoxSelection(final List<String> elements) {
        return comboBoxSelection().setElements(elements);
    }

    @Override
    public final <ENUM_TYPE extends Enum<?>> IComboBoxSelectionBluePrint<ENUM_TYPE> comboBoxSelection(
        final ENUM_TYPE... enumValues) {
        final IObjectStringConverter<ENUM_TYPE> converter = DefaultObjectStringConverter.getInstance();
        return comboBoxSelection(converter).setElements(enumValues);
    }

    @Override
    public <VALUE_TYPE> IComboBoxSelectionBluePrint<VALUE_TYPE> comboBoxSelection(final Collection<VALUE_TYPE> elements) {
        final IObjectStringConverter<VALUE_TYPE> converter = DefaultObjectStringConverter.getInstance();
        return comboBoxSelection(converter).setElements(elements);
    }

    @Override
    public IComboBoxSelectionBluePrint<IUnit> comboBoxSelection(final IUnitSet unitSet) {
        final IObjectStringConverter<IUnit> converter = Toolkit.getConverterProvider().unitConverter();
        return comboBoxSelection(converter).setElements(CollectionUtils.toCollection(unitSet));
    }

    @Override
    public IComboBoxBluePrint<String> comboBoxString() {
        return comboBox(Toolkit.getConverterProvider().string());
    }

    @Override
    public IComboBoxBluePrint<Long> comboBoxLongNumber() {
        return comboBox(Toolkit.getConverterProvider().longNumber());
    }

    @Override
    public IComboBoxBluePrint<Integer> comboBoxIntegerNumber() {
        return comboBox(Toolkit.getConverterProvider().integerNumber());
    }

    @Override
    public IComboBoxBluePrint<Short> comboBoxShortNumber() {
        return comboBox(Toolkit.getConverterProvider().shortNumber());
    }

    @Override
    public IComboBoxSelectionBluePrint<String> comboBoxSelectionString() {
        return comboBoxSelection(Toolkit.getConverterProvider().string());
    }

    @Override
    public IComboBoxSelectionBluePrint<Long> comboBoxSelectionLongNumber() {
        return comboBoxSelection(Toolkit.getConverterProvider().longNumber());
    }

    @Override
    public IComboBoxSelectionBluePrint<Integer> comboBoxSelectionIntegerNumber() {
        return comboBoxSelection(Toolkit.getConverterProvider().integerNumber());
    }

    @Override
    public IComboBoxSelectionBluePrint<Short> comboBoxSelectionShortNumber() {
        return comboBoxSelection(Toolkit.getConverterProvider().shortNumber());
    }

    @Override
    public IComboBoxSelectionBluePrint<Boolean> comboBoxSelectionBoolean() {
        final IComboBoxSelectionBluePrint<Boolean> result = comboBoxSelection(Toolkit.getConverterProvider().boolLong());
        result.setElements(Boolean.TRUE, Boolean.FALSE);
        return result;
    }

    @Override
    public final IScrollCompositeBluePrint scrollCompositeWithBorder() {
        return scrollComposite().setBorder();
    }

    @Override
    public final IScrollCompositeBluePrint scrollComposite(final String borderTitle) {
        return scrollComposite().setBorder(borderTitle);
    }

    @Override
    public IActionMenuItemBluePrint menuItem(final String text) {
        return menuItem().setText(text);
    }

    @Override
    public IRadioMenuItemBluePrint radioMenuItem(final String text) {
        return radioMenuItem().setText(text);
    }

    @Override
    public ICheckedMenuItemBluePrint checkedMenuItem(final String text) {
        return checkedMenuItem().setText(text);
    }

    @Override
    public ISubMenuBluePrint subMenu(final String text) {
        return subMenu().setText(text);
    }

    @Override
    public IMainMenuBluePrint mainMenu(final String text) {
        return mainMenu().setText(text);
    }

}

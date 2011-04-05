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

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.model.table.ITableModel;
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
import org.jowidgets.api.widgets.blueprint.IMainMenuBluePrint;
import org.jowidgets.api.widgets.blueprint.IRadioMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorToolBarItemBluePrint;
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
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.api.widgets.blueprint.factory.IBasicSimpleBluePrintFactory;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.impl.base.blueprint.factory.AbstractBluePrintFactory;
import org.jowidgets.util.Assert;

public class BasicSimpleBluePrintFactory extends AbstractBluePrintFactory implements IBasicSimpleBluePrintFactory {

	public BasicSimpleBluePrintFactory(
		final ISetupBuilderConvenienceRegistry setupBuilderConvenienceRegistry,
		final IDefaultsInitializerRegistry defaultInitializerRegistry) {
		super(setupBuilderConvenienceRegistry, defaultInitializerRegistry);
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

}

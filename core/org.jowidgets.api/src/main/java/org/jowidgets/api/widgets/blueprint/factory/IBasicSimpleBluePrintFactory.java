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

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICalendarBluePrint;
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
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.FileChooserType;

public interface IBasicSimpleBluePrintFactory extends IBaseBluePrintFactory {

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

	ITreeNodeBluePrint treeNode();

	ITableBluePrint table(ITableModel model);

	ITableBluePrint table(ITableColumnModel columnModel, ITableDataModel dataModel);

	ICalendarBluePrint calendar();

	ISliderBluePrint slider();

}

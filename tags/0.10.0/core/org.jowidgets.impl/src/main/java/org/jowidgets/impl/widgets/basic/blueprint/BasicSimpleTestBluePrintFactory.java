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

package org.jowidgets.impl.widgets.basic.blueprint;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.impl.base.blueprint.factory.AbstractBluePrintFactory;
import org.jowidgets.impl.widgets.composed.blueprint.convenience.registry.ComposedSetupConvenienceRegistry;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.registry.ComposedDefaultsInitializerRegistry;
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
import org.jowidgets.test.api.widgets.blueprint.factory.IBasicSimpleTestBluePrintFactory;

public class BasicSimpleTestBluePrintFactory extends AbstractBluePrintFactory implements IBasicSimpleTestBluePrintFactory {

	public BasicSimpleTestBluePrintFactory() {
		this(new ComposedSetupConvenienceRegistry(), new ComposedDefaultsInitializerRegistry());
	}

	public BasicSimpleTestBluePrintFactory(
		final ISetupBuilderConvenienceRegistry setupBuilderConvenienceRegistry,
		final IDefaultsInitializerRegistry defaultInitializerRegistry) {
		super(setupBuilderConvenienceRegistry, defaultInitializerRegistry);
	}

	@Override
	public IButtonBluePrintUi button() {
		return createProxy(IButtonBluePrintUi.class);
	}

	@Override
	public IFrameBluePrintUi frame() {
		return createProxy(IFrameBluePrintUi.class);
	}

	@Override
	public IDialogBluePrintUi dialog() {
		return createProxy(IDialogBluePrintUi.class);
	}

	@Override
	public ICompositeBluePrintUi composite() {
		return createProxy(ICompositeBluePrintUi.class);
	}

	@Override
	public IScrollCompositeBluePrintUi scrollComposite() {
		return createProxy(IScrollCompositeBluePrintUi.class);
	}

	@Override
	public ISplitCompositeBluePrintUi splitComposite() {
		return createProxy(ISplitCompositeBluePrintUi.class);
	}

	@Override
	public ITextLabelBluePrintUi textLabel() {
		return createProxy(ITextLabelBluePrintUi.class);
	}

	@Override
	public IIconBluePrintUi icon() {
		return createProxy(IIconBluePrintUi.class);
	}

	@Override
	public ISeparatorBluePrintUi separator() {
		return createProxy(ISeparatorBluePrintUi.class);
	}

	@Override
	public ITextFieldBluePrintUi textField() {
		return createProxy(ITextFieldBluePrintUi.class);
	}

	@Override
	public ICheckBoxBluePrintUi checkBox() {
		return createProxy(ICheckBoxBluePrintUi.class);
	}

	@Override
	public IToggleButtonBluePrintUi toggleButton() {
		return createProxy(IToggleButtonBluePrintUi.class);
	}

	@Override
	public <INPUT_TYPE> IComboBoxBluePrintUi<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter) {

		final IComboBoxBluePrintUi<INPUT_TYPE> result = createProxy(IComboBoxBluePrintUi.class);

		return result.setObjectStringConverter(converter).setStringObjectConverter(converter);
	}

	@Override
	public <INPUT_TYPE> IComboBoxSelectionBluePrintUi<INPUT_TYPE> comboBoxSelection(
		final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {

		final IComboBoxSelectionBluePrintUi<INPUT_TYPE> result = createProxy(IComboBoxSelectionBluePrintUi.class);

		return result.setObjectStringConverter(objectStringConverter);
	}

	@Override
	public IActionMenuItemBluePrintUi menuItem() {
		return createProxy(IActionMenuItemBluePrintUi.class);
	}

	@Override
	public IRadioMenuItemBluePrintUi radioMenuItem() {
		return createProxy(IRadioMenuItemBluePrintUi.class);
	}

	@Override
	public ICheckedMenuItemBluePrintUi checkedMenuItem() {
		return createProxy(ICheckedMenuItemBluePrintUi.class);
	}

	@Override
	public ISubMenuBluePrintUi subMenu() {
		return createProxy(ISubMenuBluePrintUi.class);
	}

	@Override
	public IMainMenuBluePrintUi mainMenu() {
		return createProxy(IMainMenuBluePrintUi.class);
	}

	@Override
	public ISeparatorMenuItemBluePrintUi menuSeparator() {
		return createProxy(ISeparatorMenuItemBluePrintUi.class);
	}

	@Override
	public ISeparatorToolBarItemBluePrintUi toolBarSeparator() {
		return createProxy(ISeparatorToolBarItemBluePrintUi.class);
	}

	@Override
	public IToolBarBluePrintUi toolBar() {
		return createProxy(IToolBarBluePrintUi.class);
	}

	@Override
	public IToolBarButtonBluePrintUi toolBarButton() {
		return createProxy(IToolBarButtonBluePrintUi.class);
	}

	@Override
	public IToolBarToggleButtonBluePrintUi toolBarToggleButton() {
		return createProxy(IToolBarToggleButtonBluePrintUi.class);
	}

	@Override
	public IToolBarPopupButtonBluePrintUi toolBarPopupButton() {
		return createProxy(IToolBarPopupButtonBluePrintUi.class);
	}

	@Override
	public IToolBarContainerItemBluePrintUi toolBarContainerItem() {
		return createProxy(IToolBarContainerItemBluePrintUi.class);
	}
}

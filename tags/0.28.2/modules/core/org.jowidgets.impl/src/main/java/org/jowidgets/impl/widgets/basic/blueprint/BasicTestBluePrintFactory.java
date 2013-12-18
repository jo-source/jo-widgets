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

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.test.api.widgets.blueprint.IActionMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IButtonBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICheckedMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ICompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IDialogBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IFrameBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IIconBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IMainMenuBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IRadioMenuItemBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.IScrollCompositeBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.ITextLabelBluePrintUi;
import org.jowidgets.test.api.widgets.blueprint.factory.IBasicTestBluePrintFactory;

public class BasicTestBluePrintFactory extends BasicSimpleTestBluePrintFactory implements IBasicTestBluePrintFactory {

	@Override
	public IFrameBluePrintUi frame(final String title) {
		return frame().setTitle(title);
	}

	@Override
	public IFrameBluePrintUi frame(final String title, final IImageConstant icon) {
		return frame().setTitle(title).setIcon(icon);
	}

	@Override
	public IDialogBluePrintUi dialog(final String title) {
		return dialog().setTitle(title);
	}

	@Override
	public IDialogBluePrintUi dialog(final String title, final IImageConstant icon) {
		return dialog().setTitle(title).setIcon(icon);
	}

	@Override
	public ICompositeBluePrintUi composite(final String borderTitle) {
		return composite().setBorder(borderTitle);
	}

	@Override
	public IScrollCompositeBluePrintUi scrollComposite(final String borderTitle) {
		return scrollComposite().setBorder(borderTitle);
	}

	@Override
	public IButtonBluePrintUi button(final String text) {
		return button().setText(text);
	}

	@Override
	public IButtonBluePrintUi button(final String text, final String toolTipText) {
		return button().setText(text).setToolTipText(toolTipText);
	}

	@Override
	public IIconBluePrintUi icon(final IImageConstant icon) {
		return icon().setIcon(icon);
	}

	@Override
	public ITextLabelBluePrintUi textLabel(final String text) {
		return textLabel().setText(text);
	}

	@Override
	public ITextLabelBluePrintUi textLabel(final String text, final String tooltipText) {
		return textLabel().setText(text).setToolTipText(tooltipText);
	}

	@Override
	public IActionMenuItemBluePrintUi menuItem(final String text) {
		return menuItem().setText(text);
	}

	@Override
	public IRadioMenuItemBluePrintUi radioMenuItem(final String text) {
		return radioMenuItem().setText(text);
	}

	@Override
	public ICheckedMenuItemBluePrintUi checkedMenuItem(final String text) {
		return checkedMenuItem().setText(text);
	}

	@Override
	public IMainMenuBluePrintUi mainMenu(final String text) {
		return mainMenu().setText(text);
	}

}

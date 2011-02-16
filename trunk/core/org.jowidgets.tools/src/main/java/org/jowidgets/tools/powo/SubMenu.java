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

package org.jowidgets.tools.powo;

import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.ISubMenu;
import org.jowidgets.api.widgets.blueprint.builder.IMenuItemSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.IMenuItemSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class SubMenu<WIDGET_TYPE extends ISubMenu, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & IMenuItemSetupBuilder<?> & IMenuItemSetup> extends
		Menu<WIDGET_TYPE, BLUE_PRINT_TYPE> implements ISubMenu {

	private final MenuItem<WIDGET_TYPE, BLUE_PRINT_TYPE> menuItemDelegate;

	SubMenu(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.menuItemDelegate = new MenuItem<WIDGET_TYPE, BLUE_PRINT_TYPE>(bluePrint);
	}

	@Override
	public void setText(final String text) {
		menuItemDelegate.setText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		menuItemDelegate.setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		menuItemDelegate.setIcon(icon);
	}

	@Override
	public void setMnemonic(final char mnemonic) {
		menuItemDelegate.setMnemonic(mnemonic);
	}

	@Override
	public IMenu getParent() {
		return menuItemDelegate.getParent();
	}

	@Override
	public String getText() {
		return menuItemDelegate.getText();
	}

	@Override
	public String getToolTipText() {
		return menuItemDelegate.getToolTipText();
	}

	@Override
	public IImageConstant getIcon() {
		return menuItemDelegate.getIcon();
	}

	@Override
	public void setModel(final IItemModel model) {
		//TODO MG model support
	}

}

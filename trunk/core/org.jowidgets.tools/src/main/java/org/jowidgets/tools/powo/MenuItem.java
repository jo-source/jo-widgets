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
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.blueprint.builder.IMenuItemSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.IMenuItemSetup;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class MenuItem<WIDGET_TYPE extends IMenuItem, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & IMenuItemSetupBuilder<?> & IMenuItemSetup> extends
		Item<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IMenuItem {

	MenuItem(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
	}

	@Override
	public void setMnemonic(final char mnemonic) {
		if (isInitialized()) {
			getWidget().setMnemonic(mnemonic);
		}
		else {
			getBluePrint().setMnemonic(mnemonic);
		}
	}

	@Override
	public IMenu getParent() {
		checkInitialized();
		return getWidget().getParent();
	}

	@Override
	public IItemModel getModel() {
		checkInitialized();
		return getWidget().getModel();
	}

	@Override
	public void setModel(final IItemModel model) {
		checkInitialized();
		getWidget().setModel(model);
	}

}

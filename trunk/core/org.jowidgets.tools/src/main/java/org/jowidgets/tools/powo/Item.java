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

import org.jowidgets.api.widgets.IItem;
import org.jowidgets.api.widgets.blueprint.builder.IItemSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.IItemSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class Item<WIDGET_TYPE extends IItem, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & IItemSetupBuilder<?> & IItemSetup> extends
		Widget<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IItem {

	Item(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
	}

	@Override
	public void setText(final String text) {
		if (isInitialized()) {
			getWidget().setText(text);
		}
		else {
			getBluePrint().setText(text);
		}
	}

	@Override
	public void setToolTipText(final String text) {
		if (isInitialized()) {
			getWidget().setToolTipText(text);
		}
		else {
			getBluePrint().setToolTipText(text);
		}
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		if (isInitialized()) {
			getWidget().setIcon(icon);
		}
		else {
			getBluePrint().setIcon(icon);
		}
	}

	@Override
	public String getText() {
		if (isInitialized()) {
			return getWidget().getText();
		}
		else {
			return getBluePrint().getText();
		}
	}

	@Override
	public String getToolTipText() {
		if (isInitialized()) {
			return getWidget().getToolTipText();
		}
		else {
			return getBluePrint().getToolTipText();
		}
	}

	@Override
	public IImageConstant getIcon() {
		if (isInitialized()) {
			return getWidget().getIcon();
		}
		else {
			return getBluePrint().getIcon();
		}
	}

}

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

import org.jowidgets.api.widgets.ICheckBox;
import org.jowidgets.api.widgets.blueprint.builder.ICheckBoxSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.ICheckBoxSetup;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class CheckBox<WIDGET_TYPE extends ICheckBox, BLUE_PRINT_TYPE extends IWidgetDescriptor<WIDGET_TYPE> & ICheckBoxSetup & ICheckBoxSetupBuilder<?>> extends
		InputControl<WIDGET_TYPE, BLUE_PRINT_TYPE, Boolean> implements ICheckBox {

	CheckBox(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
	}

	@Override
	public void setMarkup(final Markup markup) {
		if (isInitialized()) {
			getWidget().setMarkup(markup);
		}
		else {
			getBluePrint().setMarkup(markup);
		}
	}

	@Override
	public void setFontSize(final int size) {
		if (isInitialized()) {
			getWidget().setFontSize(size);
		}
		else {
			getBluePrint().setFontSize(size);
		}
	}

	@Override
	public void setFontName(final String fontName) {
		if (isInitialized()) {
			getWidget().setFontName(fontName);
		}
		else {
			getBluePrint().setFontName(fontName);
		}
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
	public String getText() {
		if (isInitialized()) {
			return getWidget().getText();
		}
		else {
			return getBluePrint().getText();
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
	public boolean isSelected() {
		if (isInitialized()) {
			return getWidget().isSelected();
		}
		else {
			return getBluePrint().getInitialState();
		}
	}

	@Override
	public void setSelected(final boolean selected) {
		if (isInitialized()) {
			getWidget().setSelected(selected);
		}
		else {
			getBluePrint().setInitialState(selected);
		}
	}

}

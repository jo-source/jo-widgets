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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.api.widgets.blueprint.convenience.impl;

import org.jo.widgets.api.image.IImageConstant;
import org.jo.widgets.api.widgets.blueprint.IButtonBluePrint;
import org.jo.widgets.api.widgets.blueprint.base.IBaseInputDialogBluePrint;
import org.jo.widgets.api.widgets.blueprint.convenience.IInputDialogBluePrintConvenience;
import org.jo.widgets.api.widgets.blueprint.factory.impl.BluePrintFactory;
import org.jo.widgets.api.widgets.descriptor.IButtonDescriptor;

public class InputDialogBluePrintConvenience extends AbstractBluePrintConvenience<IBaseInputDialogBluePrint<?, ?, ?>> implements
		IInputDialogBluePrintConvenience<IBaseInputDialogBluePrint<?, ?, ?>> {

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setOkButtonText(final String text) {
		getOkButtonBluePrint().setText(text);
		return getBluePrint();
	}

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setOkButtonToolTipText(final String toolTipText) {
		getOkButtonBluePrint().setToolTipText(toolTipText);
		return getBluePrint();
	}

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setOkButton(final String text, final String toolTipText) {
		return setOkButtonText(text).setOkButtonToolTipText(toolTipText);
	}

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setOkButtonIcon(final IImageConstant icon) {
		getOkButtonBluePrint().setIcon(icon);
		return getBluePrint();
	}

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setCancelButtonText(final String text) {
		getCancelButtonBluePrint().setText(text);
		return getBluePrint();
	}

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setCancelButtonToolTipText(final String toolTipText) {
		getCancelButtonBluePrint().setToolTipText(toolTipText);
		return getBluePrint();
	}

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setCancelButtonIcon(final IImageConstant icon) {
		getCancelButtonBluePrint().setIcon(icon);
		return getBluePrint();
	}

	private IButtonBluePrint getOkButtonBluePrint() {
		final IButtonBluePrint result = getButtonBluePrint(getBluePrint().getOkButton());
		getBluePrint().setOkButton(result);
		return result;
	}

	private IButtonBluePrint getCancelButtonBluePrint() {
		final IButtonBluePrint result = getButtonBluePrint(getBluePrint().getCancelButton());
		getBluePrint().setCancelButton(result);
		return result;
	}

	@Override
	public IBaseInputDialogBluePrint<?, ?, ?> setCancelButton(final String text, final String toolTipText) {
		return setCancelButtonText(text).setCancelButtonToolTipText(toolTipText);
	}

	private IButtonBluePrint getButtonBluePrint(final IButtonDescriptor buttonDescriptor) {
		if (buttonDescriptor == null) {
			return new BluePrintFactory().button();
		}
		else if (buttonDescriptor instanceof IButtonBluePrint) {
			return (IButtonBluePrint) buttonDescriptor;
		}
		else {
			return new BluePrintFactory().button().setDescriptor(buttonDescriptor);
		}
	}

}

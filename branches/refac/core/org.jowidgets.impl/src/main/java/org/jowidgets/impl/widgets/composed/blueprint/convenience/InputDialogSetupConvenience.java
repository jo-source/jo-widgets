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
package org.jowidgets.impl.widgets.composed.blueprint.convenience;

import org.jowidgets.api.image.IImageConstant;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.builder.IInputDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.convenience.IInputDialogSetupConvenience;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.impl.widgets.blueprint.convenience.AbstractSetupBuilderConvenience;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;

public class InputDialogSetupConvenience extends AbstractSetupBuilderConvenience<IInputDialogSetupBuilder<?, ?, ?>> implements
		IInputDialogSetupConvenience<IInputDialogSetupBuilder<?, ?, ?>> {

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setOkButtonText(final String text) {
		getOkButtonBluePrint().setText(text);
		return getBuilder();
	}

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setOkButtonToolTipText(final String toolTipText) {
		getOkButtonBluePrint().setToolTipText(toolTipText);
		return getBuilder();
	}

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setOkButton(final String text, final String toolTipText) {
		return setOkButtonText(text).setOkButtonToolTipText(toolTipText);
	}

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setOkButtonIcon(final IImageConstant icon) {
		getOkButtonBluePrint().setIcon(icon);
		return getBuilder();
	}

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setCancelButtonText(final String text) {
		getCancelButtonBluePrint().setText(text);
		return getBuilder();
	}

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setCancelButtonToolTipText(final String toolTipText) {
		getCancelButtonBluePrint().setToolTipText(toolTipText);
		return getBuilder();
	}

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setCancelButtonIcon(final IImageConstant icon) {
		getCancelButtonBluePrint().setIcon(icon);
		return getBuilder();
	}

	private IButtonBluePrint getOkButtonBluePrint() {
		final IButtonBluePrint result = getButtonBluePrint(getBuilder().getOkButton());
		getBuilder().setOkButton(result);
		return result;
	}

	private IButtonBluePrint getCancelButtonBluePrint() {
		final IButtonBluePrint result = getButtonBluePrint(getBuilder().getCancelButton());
		getBuilder().setCancelButton(result);
		return result;
	}

	@Override
	public IInputDialogSetupBuilder<?, ?, ?> setCancelButton(final String text, final String toolTipText) {
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

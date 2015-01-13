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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IToggleButton;
import org.jowidgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.common.image.IImageConstant;

public class JoToggleButton extends CheckBox<IToggleButton, IToggleButtonBluePrint> implements IToggleButton {

	public JoToggleButton(final String text) {
		super(Toolkit.getBluePrintFactory().toggleButton().setText(text));
	}

	public JoToggleButton(final String text, final String tooltipText) {
		super(Toolkit.getBluePrintFactory().toggleButton().setText(text).setToolTipText(tooltipText));
	}

	public JoToggleButton(final IToggleButtonDescriptor descriptor) {
		super(Toolkit.getBluePrintFactory().toggleButton().setSetup(descriptor));
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
	public IImageConstant getIcon() {
		if (isInitialized()) {
			return getWidget().getIcon();
		}
		else {
			return getBluePrint().getIcon();
		}
	}

	public static IToggleButtonBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().toggleButton();
	}

	public static IToggleButtonBluePrint bluePrint(final String text) {
		return Toolkit.getBluePrintFactory().toggleButton().setText(text);
	}

	public static IToggleButtonBluePrint bluePrint(final String text, final String tooltipText) {
		return Toolkit.getBluePrintFactory().toggleButton().setText(text).setToolTipText(tooltipText);
	}

}

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
package org.jowidgets.api.widgets.blueprint.builder;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;

public interface IValidationLabelSetupBuilder<INSTANCE_TYPE extends IValidationLabelSetupBuilder<?>> extends
		IComponentSetupBuilder<INSTANCE_TYPE> {

	INSTANCE_TYPE setShowValidationMessage(boolean showValidationMessage);

	INSTANCE_TYPE setOkMarkup(Markup markup);

	INSTANCE_TYPE setWarningMarkup(Markup markup);

	INSTANCE_TYPE setInfoErrorMarkup(Markup markup);

	INSTANCE_TYPE setErrorMarkup(Markup markup);

	INSTANCE_TYPE setMissingInputMarkup(Markup markup);

	INSTANCE_TYPE setOkColor(IColorConstant color);

	INSTANCE_TYPE setWarningColor(IColorConstant color);

	INSTANCE_TYPE setInfoErrorColor(IColorConstant color);

	INSTANCE_TYPE setErrorColor(IColorConstant color);

	INSTANCE_TYPE setMissingInputColor(IColorConstant color);

	INSTANCE_TYPE setOkIcon(IImageConstant icon);

	INSTANCE_TYPE setWarningIcon(IImageConstant icon);

	INSTANCE_TYPE setInfoErrorIcon(IImageConstant icon);

	INSTANCE_TYPE setErrorIcon(IImageConstant icon);

	INSTANCE_TYPE setMissingInputIcon(IImageConstant icon);

	INSTANCE_TYPE setMissingInputText(String text);

}

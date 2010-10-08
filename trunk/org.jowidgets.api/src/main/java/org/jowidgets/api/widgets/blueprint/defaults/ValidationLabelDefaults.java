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
package org.jowidgets.api.widgets.blueprint.defaults;

import org.jowidgets.api.color.defaults.Colors;
import org.jowidgets.api.image.defaults.Icons;
import org.jowidgets.api.look.Markup;
import org.jowidgets.api.widgets.blueprint.base.IBaseValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.defaults.anotations.IDefaultInitializer;

public class ValidationLabelDefaults implements IDefaultInitializer<IBaseValidationLabelBluePrint<?, ?>> {

	@Override
	public void initialize(final IBaseValidationLabelBluePrint<?, ?> bluePrint) {
		bluePrint.setOkMarkup(Markup.STRONG);
		bluePrint.setWarningMarkup(Markup.STRONG);
		bluePrint.setErrorMarkup(Markup.STRONG);
		bluePrint.setMissingInputMarkup(Markup.STRONG);

		bluePrint.setOkColor(Colors.DEFAULT);
		bluePrint.setErrorColor(Colors.ERROR);
		bluePrint.setWarningColor(Colors.DEFAULT);
		bluePrint.setMissingInputColor(Colors.DEFAULT);

		bluePrint.setOkIcon(Icons.OK);
		bluePrint.setWarningIcon(null);
		bluePrint.setErrorIcon(Icons.ERROR);
		bluePrint.setMissingInputIcon(null);

		// i18n
		bluePrint.setMissingInputText("");
	}

}

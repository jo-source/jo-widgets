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
package org.jo.widgets.api.widgets.blueprint.base;

import org.jo.widgets.api.widgets.IInputDialogWidget;
import org.jo.widgets.api.widgets.blueprint.convenience.IInputDialogBluePrintConvenience;
import org.jo.widgets.api.widgets.blueprint.convenience.anotations.ConvenienceMethods;
import org.jo.widgets.api.widgets.blueprint.convenience.impl.InputDialogBluePrintConvenience;
import org.jo.widgets.api.widgets.blueprint.defaults.InputDialogDefaults;
import org.jo.widgets.api.widgets.blueprint.defaults.anotations.DefaultsInitializer;
import org.jo.widgets.api.widgets.descriptor.IButtonDescriptor;

@DefaultsInitializer(InputDialogDefaults.class)
@ConvenienceMethods(InputDialogBluePrintConvenience.class)
public interface IBaseInputDialogBluePrint<INSTANCE_TYPE extends IBaseInputDialogBluePrint<?, ?, ?>, WIDGET_TYPE extends IInputDialogWidget<INPUT_TYPE>, INPUT_TYPE> extends
		IBaseTitledChildWindowBluePrint<INSTANCE_TYPE, WIDGET_TYPE>,
		IBaseInputCompositeBluePrint<INSTANCE_TYPE, WIDGET_TYPE, INPUT_TYPE>,
		IInputDialogBluePrintConvenience<INSTANCE_TYPE> {

	INSTANCE_TYPE setOkButton(final IButtonDescriptor buttonDescriptor);

	INSTANCE_TYPE setCancelButton(final IButtonDescriptor buttonDescriptor);

	IButtonDescriptor getOkButton();

	IButtonDescriptor getCancelButton();

}

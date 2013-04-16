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
package org.jowidgets.api.widgets.blueprint.factory;

import java.util.Collection;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.login.ILoginInterceptor;
import org.jowidgets.api.password.IPasswordChangeExecutor;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.ICollectionInputControlBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ICombinedCollectionInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IExpandCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ILoginDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IPasswordChangeDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextSeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationResultLabelBluePrint;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.setup.ICollectionInputControlSetup;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;

public interface ISimpleBluePrintFactory extends IBasicBluePrintFactory {

	ILabelBluePrint label();

	ITextSeparatorBluePrint textSeparator();

	IInputComponentValidationLabelBluePrint inputComponentValidationLabel(IInputComponent<?> inputComponent);

	IInputComponentValidationLabelBluePrint inputComponentValidationLabel();

	IValidationResultLabelBluePrint validationResultLabel();

	<INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField();

	<INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IConverter<INPUT_TYPE> converter);

	<INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IObjectStringConverter<INPUT_TYPE> converter);

	IMessageDialogBluePrint messageDialog();

	IQuestionDialogBluePrint questionDialog();

	IProgressBarBluePrint progressBar();

	<INPUT_TYPE> IInputDialogBluePrint<INPUT_TYPE> inputDialog(final IInputContentCreator<INPUT_TYPE> contentCreator);

	<INPUT_TYPE> IInputCompositeBluePrint<INPUT_TYPE> inputComposite(final IInputContentCreator<INPUT_TYPE> contentCreator);

	ILoginDialogBluePrint loginDialog(ILoginInterceptor interceptor);

	IPasswordChangeDialogBluePrint passwordChangeDialog(IPasswordChangeExecutor executor);

	<ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
		ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> widgetCreator);

	<ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
		ICollectionInputControlSetup<ELEMENT_TYPE> setup);

	<ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(IConverter<ELEMENT_TYPE> converter);

	<ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(
		IObjectStringConverter<ELEMENT_TYPE> converter);

	<ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField();

	<ELEMENT_TYPE> ICombinedCollectionInputFieldBluePrint<ELEMENT_TYPE> combinedCollectionInputField(
		ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> elementTypeCreator,
		ICustomWidgetCreator<IInputControl<? extends Collection<ELEMENT_TYPE>>> collectionTypeCreator);

	IExpandCompositeBluePrint expandComposite();

}

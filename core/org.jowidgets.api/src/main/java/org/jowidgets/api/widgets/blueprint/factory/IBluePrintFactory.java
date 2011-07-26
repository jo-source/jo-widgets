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

import java.text.DateFormat;
import java.util.Date;

import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.ICollectionInputControlBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextSeparatorBluePrint;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

public interface IBluePrintFactory extends ISimpleBluePrintFactory {

	////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////// some input fields here/////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////

	IInputFieldBluePrint<String> inputFieldString();

	IInputFieldBluePrint<Long> inputFieldLongNumber();

	IInputFieldBluePrint<Integer> inputFieldIntegerNumber();

	IInputFieldBluePrint<Short> inputFieldShortNumber();

	IInputFieldBluePrint<Date> inputFieldDate(DateFormat dateFormat, String formatHint, ITextMask mask);

	IInputFieldBluePrint<Date> inputFieldDate(DateFormat dateFormat, String formatHint);

	IInputFieldBluePrint<Date> inputFieldDate();

	IInputFieldBluePrint<Date> inputFieldDateTime();

	IInputFieldBluePrint<Date> inputFieldTime();

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////some convenience methods starting here///////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	ILabelBluePrint label(final IImageConstant icon);

	ILabelBluePrint label(final IImageConstant icon, final String text);

	ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext);

	ITextSeparatorBluePrint textSeparator(final String text);

	ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText);

	IQuestionDialogBluePrint yesNoQuestion();

	IQuestionDialogBluePrint yesNoCancelQuestion();

	IQuestionDialogBluePrint yesNoQuestion(String question);

	IQuestionDialogBluePrint yesNoCancelQuestion(String question);

	IMessageDialogBluePrint infoDialog();

	IMessageDialogBluePrint warningDialog();

	IMessageDialogBluePrint errorDialog();

	IMessageDialogBluePrint infoDialog(String message);

	IMessageDialogBluePrint warningDialog(String message);

	IMessageDialogBluePrint errorDialog(String message);

	IProgressBarBluePrint progressBar(int minimum, int maximum);

	IProgressBarBluePrint progressBar(int maximum);

	<ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
		IWidgetDescriptor<IInputControl<ELEMENT_TYPE>> descriptor);

}

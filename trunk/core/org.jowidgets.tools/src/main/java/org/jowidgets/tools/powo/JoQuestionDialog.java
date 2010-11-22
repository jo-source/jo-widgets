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
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IQuestionDialog;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.descriptor.IQuestionDialogDescriptor;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.IWindowCommon;
import org.jowidgets.util.Assert;

public class JoQuestionDialog extends Widget<IQuestionDialog, IQuestionDialogBluePrint> implements IQuestionDialog {

	public JoQuestionDialog(final IWindowCommon parent, final QuestionType questionType, final String message) {
		this(parent, bluePrint(questionType).setText(message));
	}

	public JoQuestionDialog(final IWindowCommon parent, final QuestionType questionType, final String title, final String message) {
		this(parent, bluePrint(questionType).setTitle(title).setText(message));
	}

	public JoQuestionDialog(
		final IWindowCommon parent,
		final QuestionType questionType,
		final String title,
		final IImageConstant icon,
		final String message) {
		this(parent, bluePrint(questionType).setTitle(title).setIcon(icon).setText(message));
	}

	public JoQuestionDialog(
		final IWindowCommon parent,
		final QuestionType questionType,
		final IImageConstant titleIcon,
		final String title,
		final IImageConstant icon,
		final String message) {
		this(parent, bluePrint(questionType).setTitleIcon(titleIcon).setTitle(title).setIcon(icon).setText(message));
	}

	public JoQuestionDialog(final IWindowCommon parent, final IQuestionDialogDescriptor descriptor) {
		super(bluePrint().setSetup(descriptor));
		initialize(Toolkit.getWidgetFactory().create(parent.getUiReference(), descriptor));
	}

	@Override
	public QuestionResult question() {
		return getWidget().question();
	}

	@Override
	public IDisplay getParent() {
		checkInitialized();
		return getWidget().getParent();
	}

	public static IQuestionDialogBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().questionDialog();
	}

	public static IQuestionDialogBluePrint bluePrint(final QuestionType questionType) {
		Assert.paramNotNull(questionType, "questionType");
		if (questionType == QuestionType.YES_NO) {
			return bluePrintYesNo();
		}
		else if (questionType == QuestionType.YES_NO_CANCEL) {
			return bluePrintYesNoCancel();
		}
		else {
			throw new IllegalArgumentException("QuestionType '" + questionType + "' is not known.");
		}
	}

	public static IQuestionDialogBluePrint bluePrintYesNo() {
		return Toolkit.getBluePrintFactory().yesNoQuestion();
	}

	public static IQuestionDialogBluePrint bluePrintYesNoCancel() {
		return Toolkit.getBluePrintFactory().yesNoCancelQuestion();
	}

	public static IQuestionDialogBluePrint bluePrintYesNo(final String question) {
		return Toolkit.getBluePrintFactory().yesNoQuestion(question);
	}

	public static IQuestionDialogBluePrint bluePrintYesNoCancel(final String question) {
		return Toolkit.getBluePrintFactory().yesNoCancelQuestion(question);
	}

	public enum QuestionType {
		YES_NO,
		YES_NO_CANCEL;
	}
}

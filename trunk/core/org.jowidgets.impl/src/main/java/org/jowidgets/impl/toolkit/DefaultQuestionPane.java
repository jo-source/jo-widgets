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

package org.jowidgets.impl.toolkit;

import org.jowidgets.api.toolkit.IQuestionPane;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IWindowWidget;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;

public class DefaultQuestionPane implements IQuestionPane {

	private final IGenericWidgetFactory genericWidgetFactory;
	private final IBluePrintFactory bluePrintFactory;
	private final ActiveWindowTracker activeWindowTracker;

	public DefaultQuestionPane(
		final IGenericWidgetFactory genericWidgetFactory,
		final IBluePrintFactory bluePrintFactory,
		final ActiveWindowTracker activeWindowTracker) {
		super();
		this.genericWidgetFactory = genericWidgetFactory;
		this.bluePrintFactory = bluePrintFactory;
		this.activeWindowTracker = activeWindowTracker;
	}

	@Override
	public QuestionResult askYesNoQuestion(final String question) {
		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoQuestion().setText(question);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoCancelQuestion(final String question) {
		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoCancelQuestion().setText(question);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoQuestion(final String title, final String question) {
		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoQuestion().setTitle(title).setText(question);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoCancelQuestion(final String title, final String question) {
		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoCancelQuestion().setTitle(title).setText(question);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoQuestion(final String title, final String question, final QuestionResult defaultResult) {
		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoQuestion().setTitle(title).setText(question);
		bp.setDefaultResult(defaultResult);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoCancelQuestion(final String title, final String question, final QuestionResult defaultResult) {
		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoCancelQuestion().setTitle(title).setText(question);
		bp.setDefaultResult(defaultResult);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoQuestion(
		final String title,
		final String question,
		final QuestionResult defaultResult,
		final IImageConstant icon) {

		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoQuestion().setTitle(title).setText(question);
		bp.setDefaultResult(defaultResult).setIcon(icon);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoCancelQuestion(
		final String title,
		final String question,
		final QuestionResult defaultResult,
		final IImageConstant icon) {

		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoCancelQuestion().setTitle(title).setText(question);
		bp.setDefaultResult(defaultResult).setIcon(icon);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoQuestion(
		final String title,
		final IImageConstant titleIcon,
		final String question,
		final QuestionResult defaultResult,
		final IImageConstant icon) {

		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoQuestion().setTitle(title).setTitleIcon(titleIcon);
		bp.setText(question).setDefaultResult(defaultResult).setIcon(icon);
		return askQuestion(bp);
	}

	@Override
	public QuestionResult askYesNoCancelQuestion(
		final String title,
		final IImageConstant titleIcon,
		final String question,
		final QuestionResult defaultResult,
		final IImageConstant icon) {

		final IQuestionDialogBluePrint bp = bluePrintFactory.yesNoCancelQuestion().setTitle(title).setTitleIcon(titleIcon);
		bp.setText(question).setDefaultResult(defaultResult).setIcon(icon);
		return askQuestion(bp);
	}

	private QuestionResult askQuestion(final IQuestionDialogBluePrint messageDialogBluePrint) {
		final IWindowWidget activeWindow = activeWindowTracker.getActiveWindow();
		if (activeWindow != null) {
			return activeWindow.createChildWindow(messageDialogBluePrint).question();
		}
		else {
			return genericWidgetFactory.create(messageDialogBluePrint).question();
		}
	}

}

/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.api.toolkit;

import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.common.image.IImageConstant;

public final class QuestionPane {

    private QuestionPane() {}

    public static IQuestionPane get() {
        return Toolkit.getQuestionPane();
    }

    public static QuestionResult askYesNoQuestion(final String question) {
        return get().askYesNoQuestion(question);
    }

    public static QuestionResult askYesNoCancelQuestion(final String question) {
        return get().askYesNoCancelQuestion(question);
    }

    public static QuestionResult askYesNoQuestion(final String title, final String question) {
        return get().askYesNoQuestion(title, question);
    }

    public static QuestionResult askYesNoCancelQuestion(final String title, final String question) {
        return get().askYesNoCancelQuestion(title, question);
    }

    public static QuestionResult askYesNoQuestion(final String title, final String question, final QuestionResult defaultResult) {
        return get().askYesNoQuestion(title, question, defaultResult);
    }

    public static QuestionResult askYesNoCancelQuestion(
        final String title,
        final String question,
        final QuestionResult defaultResult) {
        return get().askYesNoCancelQuestion(title, question, defaultResult);
    }

    public static QuestionResult askYesNoQuestion(
        final String title,
        final String question,
        final QuestionResult defaultResult,
        final IImageConstant icon) {
        return get().askYesNoQuestion(title, question, defaultResult, icon);
    }

    public static QuestionResult askYesNoCancelQuestion(
        final String title,
        final String question,
        final QuestionResult defaultResult,
        final IImageConstant icon) {
        return get().askYesNoCancelQuestion(title, question, defaultResult, icon);
    }

    public static QuestionResult askYesNoQuestion(
        final String title,
        final IImageConstant titleIcon,
        final String question,
        final QuestionResult defaultResult,
        final IImageConstant icon) {
        return get().askYesNoQuestion(title, titleIcon, question, defaultResult, icon);
    }

    public static QuestionResult askYesNoCancelQuestion(
        final String title,
        final IImageConstant titleIcon,
        final String question,
        final QuestionResult defaultResult,
        final IImageConstant icon) {
        return get().askYesNoCancelQuestion(title, titleIcon, question, defaultResult, icon);
    }

    public static QuestionResult askYesNoQuestion(
        final IExecutionContext executionContext,
        final String question,
        final QuestionResult defaultResult) {
        return get().askYesNoQuestion(executionContext, question, defaultResult);
    }
}

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
import org.jowidgets.common.image.IImageConstant;

public final class MessagePane {

    private MessagePane() {}

    public static IMessagePane get() {
        return Toolkit.getMessagePane();
    }

    public static void showInfo(final String message) {
        get().showInfo(message);
    }

    public static void showWarning(final String message) {
        get().showWarning(message);
    }

    public static void showError(final String message) {
        get().showError(message);
    }

    public static void showInfo(final String title, final String message) {
        get().showInfo(title, message);
    }

    public static void showWarning(final String title, final String message) {
        get().showWarning(title, message);
    }

    public static void showError(final String title, final String message) {
        get().showError(title, message);
    }

    public static void showInfo(final String title, final IImageConstant titleIcon, final String message) {
        get().showInfo(title, titleIcon, message);
    }

    public static void showInfo(final IExecutionContext executionContext, final String message) {
        get().showInfo(executionContext, message);
    }

    public static void showWarning(final String title, final IImageConstant titleIcon, final String message) {
        get().showWarning(title, titleIcon, message);
    }

    public static void showWarning(final IExecutionContext executionContext, final String message) {
        get().showWarning(executionContext, message);
    }

    public static void showError(final String title, final IImageConstant titleIcon, final String message) {
        get().showError(title, titleIcon, message);
    }

    public static void showError(final IExecutionContext executionContext, final String message) {
        get().showError(executionContext, message);
    }

    public static void showMessage(final String title, final String message, final IImageConstant messageIcon) {
        get().showMessage(title, message, messageIcon);
    }

    public static void showMessage(
        final String title,
        final IImageConstant titleIcon,
        final String message,
        final IImageConstant messageIcon) {
        get().showMessage(title, titleIcon, message, messageIcon);
    }

    public static void showMessage(
        final IExecutionContext executionContext,
        final String message,
        final IImageConstant messageIcon) {
        get().showMessage(executionContext, message, messageIcon);
    }
}

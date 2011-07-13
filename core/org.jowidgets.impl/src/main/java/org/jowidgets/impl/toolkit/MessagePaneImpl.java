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

import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.toolkit.IMessagePane;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.util.Assert;

class MessagePaneImpl implements IMessagePane {

	private final IGenericWidgetFactory genericWidgetFactory;
	private final IBluePrintFactory bluePrintFactory;
	private final WindowProvider activeWindowProvider;

	MessagePaneImpl(
		final IGenericWidgetFactory genericWidgetFactory,
		final IBluePrintFactory bluePrintFactory,
		final WindowProvider activeWindowProvider) {
		super();
		this.genericWidgetFactory = genericWidgetFactory;
		this.bluePrintFactory = bluePrintFactory;
		this.activeWindowProvider = activeWindowProvider;
	}

	@Override
	public void showInfo(final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.infoDialog().setText(message);
		showMessage(bp);
	}

	@Override
	public void showWarning(final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.warningDialog().setText(message);
		showMessage(bp);
	}

	@Override
	public void showError(final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.errorDialog().setText(message);
		showMessage(bp);
	}

	@Override
	public void showInfo(final String title, final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.infoDialog().setTitle(title).setText(message);
		showMessage(bp);
	}

	@Override
	public void showWarning(final String title, final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.warningDialog().setTitle(title).setText(message);
		showMessage(bp);
	}

	@Override
	public void showError(final String title, final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.errorDialog().setTitle(title).setText(message);
		showMessage(bp);
	}

	@Override
	public void showInfo(final String title, final IImageConstant titleIcon, final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.infoDialog().setTitle(title).setText(message);
		bp.setTitleIcon(titleIcon);
		showMessage(bp);
	}

	@Override
	public void showWarning(final String title, final IImageConstant titleIcon, final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.warningDialog().setTitle(title).setText(message);
		bp.setTitleIcon(titleIcon);
		showMessage(bp);
	}

	@Override
	public void showError(final String title, final IImageConstant titleIcon, final String message) {
		final IMessageDialogBluePrint bp = bluePrintFactory.errorDialog().setTitle(title).setText(message);
		bp.setTitleIcon(titleIcon);
		showMessage(bp);
	}

	@Override
	public void showMessage(final String title, final String message, final IImageConstant icon) {
		final IMessageDialogBluePrint bp = bluePrintFactory.messageDialog().setTitle(title).setText(message).setIcon(icon);
		showMessage(bp);
	}

	@Override
	public void showInfo(final IExecutionContext executionContext, final String message) {
		Assert.paramNotNull(executionContext, "executionContext");
		showInfo(executionContext.getAction().getText(), executionContext.getAction().getIcon(), message);
	}

	@Override
	public void showWarning(final IExecutionContext executionContext, final String message) {
		Assert.paramNotNull(executionContext, "executionContext");
		showWarning(executionContext.getAction().getText(), executionContext.getAction().getIcon(), message);
	}

	@Override
	public void showError(final IExecutionContext executionContext, final String message) {
		Assert.paramNotNull(executionContext, "executionContext");
		showError(executionContext.getAction().getText(), executionContext.getAction().getIcon(), message);
	}

	@Override
	public void showMessage(final IExecutionContext executionContext, final String message, final IImageConstant messageIcon) {
		Assert.paramNotNull(executionContext, "executionContext");
		showMessage(executionContext.getAction().getText(), executionContext.getAction().getIcon(), message, messageIcon);
	}

	@Override
	public void showMessage(
		final String title,
		final IImageConstant titleIcon,
		final String message,
		final IImageConstant messageIcon) {
		final IMessageDialogBluePrint bp = bluePrintFactory.messageDialog().setTitle(title).setText(message).setIcon(messageIcon);
		bp.setTitleIcon(titleIcon);
		showMessage(bp);
	}

	private void showMessage(final IMessageDialogBluePrint messageDialogBluePrint) {
		final IWindow activeWindow = activeWindowProvider.getActiveWindow();
		if (activeWindow != null) {
			activeWindow.createChildWindow(messageDialogBluePrint).showMessage();
		}
		else {
			genericWidgetFactory.create(messageDialogBluePrint).showMessage();
		}
	}

}

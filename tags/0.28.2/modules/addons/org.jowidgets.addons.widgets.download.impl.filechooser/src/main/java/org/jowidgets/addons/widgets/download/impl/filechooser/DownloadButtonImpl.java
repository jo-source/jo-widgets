/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.addons.widgets.download.impl.filechooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jowidgets.addons.widgets.download.api.IDownloadButton;
import org.jowidgets.addons.widgets.download.api.IDownloadButtonBluePrint;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.image.Icons;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFileChooser;
import org.jowidgets.api.widgets.blueprint.IFileChooserBluePrint;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.DialogResult;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.ButtonWrapper;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.io.FileUtils;

class DownloadButtonImpl extends ButtonWrapper implements IDownloadButton {

	private static final IMessage OVERRIDE_TITLE = Messages.getMessage("DownloadButtonImpl.override_title");
	private static final IMessage OVERRIDE_CONFIRMATION = Messages.getMessage("DownloadButtonImpl.override_confirmation");

	private final IButton button;

	private String urlString;
	private File lastFolder;

	public DownloadButtonImpl(final IButton button, final IDownloadButtonBluePrint bluePrint) {
		super(button);
		this.button = getWidget();

		button.addActionListener(new DownloadActionListener());
		button.setEnabled(false);

		if (!EmptyCheck.isEmpty(bluePrint.getUrl())) {
			setUrl(bluePrint.getUrl());
		}
	}

	@Override
	protected IButton getWidget() {
		return super.getWidget();
	}

	@Override
	public void setUrl(final String url) {
		try {
			new URL(url);
			this.urlString = url;
		}
		catch (final MalformedURLException e) {
			this.urlString = null;
		}
		button.setEnabled(!EmptyCheck.isEmpty(this.urlString));
	}

	@Override
	public String getUrl() {
		return urlString;
	}

	@Override
	public void download() {
		if (!EmptyCheck.isEmpty(urlString)) {
			try {
				download(new URL(urlString));
			}
			catch (final MalformedURLException e) {
			}
		}

	}

	private void download(final URL url) {
		final IFileChooserBluePrint fileChooserBp = BPF.fileChooser(FileChooserType.SAVE);
		final IFileChooser fileChooser = Toolkit.getActiveWindow().createChildWindow(fileChooserBp);

		final String fileName = url.getPath();
		if (!EmptyCheck.isEmpty(fileName)) {
			final File file = new File(fileName);
			final File simpleFile;
			if (lastFolder != null) {
				final String parent = lastFolder.getParent();
				if (parent != null) {
					simpleFile = new File(parent, file.getName());
				}
				else {
					simpleFile = new File(file.getName());
				}
			}
			else {
				simpleFile = new File(file.getName());
			}
			fileChooser.setSelectedFile(simpleFile);
		}
		if (DialogResult.OK == fileChooser.open()) {
			final List<File> selectedFiles = fileChooser.getSelectedFiles();
			if (!EmptyCheck.isEmpty(selectedFiles)) {
				this.lastFolder = selectedFiles.iterator().next();
				download(url, lastFolder);
			}
		}
	}

	private void download(final URL url, final File file) {
		if (file.exists()) {
			final String title = OVERRIDE_TITLE.get();
			final String question = OVERRIDE_CONFIRMATION.get();
			final QuestionResult questionResult = Toolkit.getQuestionPane().askYesNoQuestion(title, question);
			if (QuestionResult.NO == questionResult) {
				return;
			}
		}
		try {
			download(url.openStream(), file);
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void download(final InputStream inputStream, final File file) {
		//TODO MG use a dialog that shows the progress and that can be canceled
		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
		final Thread worker = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileUtils.inputStreamToFile(inputStream, file);
				}
				catch (final Exception e) {
					final String title = "Failed";
					final String message = "Download failed!\n" + e.getLocalizedMessage();
					showMessageInUiThread(uiThreadAccess, Icons.ERROR, title, message);
				}
				finally {
					FileUtils.tryCloseSilent(inputStream);
				}

				final String title = "Finished";
				final String message = "File downloaded successfully!";
				showMessageInUiThread(uiThreadAccess, Icons.INFO, title, message);
			}
		});

		worker.setDaemon(true);
		worker.start();
	}

	private void showMessageInUiThread(
		final IUiThreadAccess uiThreadAccess,
		final IImageConstant icon,
		final String title,
		final String message) {
		uiThreadAccess.invokeLater(new Runnable() {
			@Override
			public void run() {
				Toolkit.getMessagePane().showMessage(title, message, icon);
			}
		});
	}

	@Override
	public void setAction(final IAction action) {
		throw new UnsupportedOperationException("setAction() is not possible for a DownloadButton");
	}

	private class DownloadActionListener implements IActionListener {
		@Override
		public void actionPerformed() {
			download();
		}
	}

}

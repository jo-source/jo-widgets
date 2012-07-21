/*
 * Copyright (c) 2012, grossmann, waheckma
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

package org.jowidgets.addons.widgets.ole.document.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocument;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocumentSetupBuilder;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.event.ChangeObservable;
import org.jowidgets.util.event.IChangeListener;
import org.jowidgets.util.io.FileUtils;
import org.jowidgets.util.io.ITempFileFactory;

class OleDocumentImpl extends ControlWrapper implements IOleDocument {

	private final String progId;
	private final ChangeObservable documentChangeObservable;
	private final ITempFileFactory tempFileFactory;

	public OleDocumentImpl(final IOleControl oleControl, final IOleDocumentSetupBuilder<?> setup) {
		super(oleControl);
		Assert.paramNotNull(setup.getTempFileFactory(), "setup.getTempFileFactory()");
		Assert.paramNotNull(setup.getProgId(), "setup.getProgId()");

		this.progId = setup.getProgId();
		this.tempFileFactory = setup.getTempFileFactory();
		this.documentChangeObservable = new ChangeObservable();

		if (progId != null) {
			oleControl.setDocument(progId);
		}
	}

	@Override
	protected IOleControl getWidget() {
		return (IOleControl) super.getWidget();
	}

	@Override
	public boolean saveDocument(final File file, final boolean includeOleInfo) {
		Assert.paramNotNull(file, "file");
		return getWidget().saveCurrentDocument(file, includeOleInfo);
	}

	@Override
	public boolean saveDocument(final OutputStream outputStream) {
		Assert.paramNotNull(outputStream, "outputStream");
		File tempFile = null;
		try {
			tempFile = createTempFile();
			final boolean saved = getWidget().saveCurrentDocument(tempFile, true);
			if (saved) {
				FileUtils.fileToOutputStream(tempFile, outputStream);
				return true;
			}
			else {
				return false;
			}
		}
		finally {
			deleteFile(tempFile);
		}
	}

	@Override
	public void openNewDocument() {
		if (progId != null) {
			getWidget().setDocument(progId);
		}
		else {
			getWidget().clearDocument();
		}
		documentChangeObservable.fireChangedEvent();
	}

	@Override
	public void openDocument(final File file) {
		Assert.paramNotNull(file, "file");
		if (progId != null) {
			getWidget().setDocument(progId, file);
		}
		else {
			getWidget().setDocument(file);
		}
		documentChangeObservable.fireChangedEvent();
	}

	@Override
	public void openDocument(final InputStream inputStream) {
		Assert.paramNotNull(inputStream, "inputStream");
		File tempFile = null;
		try {
			tempFile = createTempFile();
			FileUtils.inputStreamToFile(inputStream, tempFile);
			openDocument(tempFile);
		}
		finally {
			deleteFile(tempFile);
		}
	}

	@Override
	public boolean isDirty() {
		return getWidget().isDirty();
	}

	@Override
	public IOleControl getOleControl() {
		return getWidget();
	}

	@Override
	public void addDocumentChangeListener(final IChangeListener changeListener) {
		documentChangeObservable.addChangeListener(changeListener);
	}

	@Override
	public void removeDocumentChangeListener(final IChangeListener changeListener) {
		documentChangeObservable.removeChangeListener(changeListener);

	}

	private File createTempFile() {
		return tempFileFactory.create("OleDocumentTemp", "");
	}

	private static void deleteFile(final File file) {
		if (file != null && file.exists()) {
			if (!file.delete()) {
				throw new RuntimeException("Tempfile could not be deleted");
			}
		}
	}

}

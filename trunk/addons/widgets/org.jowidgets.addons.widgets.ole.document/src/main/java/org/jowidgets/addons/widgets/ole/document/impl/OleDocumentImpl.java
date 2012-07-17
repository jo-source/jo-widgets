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

import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocument;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocumentSetupBuilder;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;
import org.jowidgets.util.event.ChangeObservable;
import org.jowidgets.util.event.IChangeListener;
import org.jowidgets.util.io.FileUtils;
import org.jowidgets.util.io.ITempFileFactory;

class OleDocumentImpl extends ControlWrapper implements IOleDocument {

	private final String progId;
	private final IMutableValue<IOleContext> mutableOleContext;
	private final ChangeObservable documentChangeObservable;
	private final ITempFileFactory tempFileFactory;
	private File tempDocumentStateFile;

	public OleDocumentImpl(final IOleControl oleControl, final IOleDocumentSetupBuilder<?> setup) {
		super(oleControl);
		Assert.paramNotNull(setup.getTempFileFactory(), "setup.getTempFileFactory()");
		Assert.paramNotNull(setup.getProgId(), "setup.getProgId()");

		this.progId = setup.getProgId();
		this.mutableOleContext = oleControl.getContext();
		this.tempFileFactory = setup.getTempFileFactory();
		this.documentChangeObservable = new ChangeObservable();

		mutableOleContext.addMutableValueListener(new IMutableValueListener<IOleContext>() {
			@Override
			public void changed(final IValueChangedEvent<IOleContext> event) {
				oleContextChanged(event);
			}
		});

		oleContextChanged(null);
	}

	@Override
	protected IOleControl getWidget() {
		return (IOleControl) super.getWidget();
	}

	private void oleContextChanged(final IValueChangedEvent<IOleContext> event) {
		final IOleContext oleContext = mutableOleContext.getValue();

		if (oleContext != null) {
			//TODO WH null prog ids must be supported
			if (progId != null) {
				if (tempDocumentStateFile != null && tempDocumentStateFile.exists()) {
					oleContext.setDocument(progId, tempDocumentStateFile);
				}
				else {
					oleContext.setDocument(progId);
				}
			}
		}
		else if (event != null) {
			final IOleContext oldContext = event.getOldValue();

			if (oldContext != null && !oldContext.isDisposed()) {
				if (tempDocumentStateFile == null) {
					tempDocumentStateFile = createTempFile();
				}
				oldContext.saveCurrentDocument(tempDocumentStateFile, true);
			}
		}
	}

	@Override
	public boolean saveDocument(final File file, final boolean includeOleInfo) {
		Assert.paramNotNull(file, "file");
		if (mutableOleContext.getValue() != null) {
			return mutableOleContext.getValue().saveCurrentDocument(file, includeOleInfo);
		}
		else {
			if (tempDocumentStateFile != null && tempDocumentStateFile.exists()) {
				FileUtils.copyFile(tempDocumentStateFile, file);
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean saveDocument(final OutputStream outputStream) {
		Assert.paramNotNull(outputStream, "outputStream");
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			return saveDocumentWithContext(outputStream, oleContext);
		}
		else {
			if (tempDocumentStateFile != null && tempDocumentStateFile.exists()) {
				FileUtils.fileToOutputStream(tempDocumentStateFile, outputStream);
				return true;
			}
			return false;
		}
	}

	private boolean saveDocumentWithContext(final OutputStream outputStream, final IOleContext oleContext) {
		File tempFile = null;
		try {
			tempFile = createTempFile();
			final boolean saved = oleContext.saveCurrentDocument(tempFile, true);
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
		deleteTempDocumentStateFile();
		if (mutableOleContext.getValue() != null && progId != null) {
			mutableOleContext.getValue().setDocument(progId);
		}
		documentChangeObservable.fireChangedEvent();
	}

	@Override
	public void openDocument(final File file) {
		Assert.paramNotNull(file, "file");
		deleteTempDocumentStateFile();
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			if (progId != null) {
				oleContext.setDocument(progId, file);
			}
			else {
				oleContext.setDocument(file);
			}
		}
		else {
			tempDocumentStateFile = createTempFile();
			FileUtils.copyFile(file, tempDocumentStateFile);
		}
		documentChangeObservable.fireChangedEvent();
	}

	@Override
	public void openDocument(final InputStream inputStream) {
		Assert.paramNotNull(inputStream, "inputStream");
		deleteFile(tempDocumentStateFile);
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
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
		else {
			tempDocumentStateFile = createTempFile();
			FileUtils.inputStreamToFile(inputStream, tempDocumentStateFile);
		}
	}

	@Override
	public void dispose() {
		deleteFile(tempDocumentStateFile);
		super.dispose();
	}

	@Override
	public boolean isDirty() {
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			return oleContext.isDirty();
		}
		return false;
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

	private void deleteTempDocumentStateFile() {
		deleteFile(tempDocumentStateFile);
		tempDocumentStateFile = null;
	}

	private static void deleteFile(final File file) {
		if (file != null && file.exists()) {
			if (!file.delete()) {
				throw new RuntimeException("Tempfile could not be deleted");
			}
		}
	}

	private File createTempFile() {
		return tempFileFactory.create("OleDocumentTemp", ".doc");

	}
}

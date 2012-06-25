/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.addons.widgets.office.impl.ole;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.jowidgets.addons.widgets.ole.api.IOfficeControl;
import org.jowidgets.addons.widgets.ole.api.IOfficeControlSetupBuilder;
import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.IFactory;
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;
import org.jowidgets.util.event.IChangeListener;

class OfficeControlImpl extends ControlWrapper implements IOfficeControl {

	private final String progId;
	private final IMutableValue<IOleContext> mutableOleContext;
	private final IFactory<File> tempFileFactory;

	private File tempDocumentStateFile;

	public OfficeControlImpl(final IOleControl oleControl, final IOfficeControlSetupBuilder<?> setup, final String progId) {
		super(oleControl);
		this.progId = progId;
		this.mutableOleContext = oleControl.getContext();
		if (setup.getTempFileFactory() != null) {
			tempFileFactory = setup.getTempFileFactory();
		}
		else {
			tempFileFactory = new DefaultTempFileFactory();
		}
		mutableOleContext.addMutableValueListener(new IMutableValueListener<IOleContext>() {
			@Override
			public void changed(final IValueChangedEvent<IOleContext> event) {
				oleContextChanged(event);
			}
		});
		oleContextChanged(null);
	}

	private void oleContextChanged(final IValueChangedEvent<IOleContext> event) {
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			if (tempDocumentStateFile != null) {
				oleContext.setDocument(progId, tempDocumentStateFile);
				//TODO WH delete tempFile
			}
			else {
				oleContext.setDocument(progId);
			}
		}
		else if (event != null) {
			final IOleContext oldContext = event.getOldValue();
			if (oldContext != null) {
				tempDocumentStateFile = tempFileFactory.create();
				oldContext.saveCurrentDocumet(tempDocumentStateFile, true);
			}
		}
	}

	@Override
	protected IOleControl getWidget() {
		return (IOleControl) super.getWidget();
	}

	@Override
	public void openNewDocument() {

	}

	@Override
	public void openDocument(final File file) {

	}

	@Override
	public void saveDocument(final File file) {

	}

	@Override
	public void openDocument(final InputStream inputStream) {

	}

	@Override
	public void saveDocument(final OutputStream outputStream) {

	}

	@Override
	public void setToolbarVisible(final boolean visible) {

	}

	@Override
	public void isDirty() {

	}

	@Override
	public void addDocumentChangeListener(final IChangeListener changeListener) {

	}

	private final class DefaultTempFileFactory implements IFactory<File> {
		@Override
		public File create() {
			//TODO WH write file to user temp
			return new File(UUID.randomUUID().toString());
		}
	}
}

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

package org.jowidgets.addons.widgets.ole.document.tools;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocument;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.IMutableValue;

public class OleDocumentWrapper extends ControlWrapper implements IOleDocument {

	private final IOleDocument oleDocument;

	public OleDocumentWrapper(final IOleDocument oleDocument) {
		super(oleDocument);
		this.oleDocument = oleDocument;
	}

	@Override
	protected IOleDocument getWidget() {
		return (IOleDocument) super.getWidget();
	}

	@Override
	public void openNewDocument() {
		oleDocument.openNewDocument();
	}

	@Override
	public void openDocument(final File file) {
		oleDocument.openDocument(file);
	}

	@Override
	public boolean saveDocument(final File file, final Boolean includeOleInfo) {
		return oleDocument.saveDocument(file, includeOleInfo);
	}

	@Override
	public void openDocument(final InputStream inputStream) {
		oleDocument.openDocument(inputStream);
	}

	@Override
	public void saveDocument(final OutputStream outputStream) {
		oleDocument.saveDocument(outputStream);
	}

	@Override
	public boolean isDirty() {
		return oleDocument.isDirty();
	}

	@Override
	public IMutableValue<IOleContext> getContext() {
		return oleDocument.getContext();
	}

}

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

package org.jowidgets.addons.widgets.mediaplayer.impl.ole;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.jowidgets.addons.widgets.mediaplayer.api.IMediaPlayer;
import org.jowidgets.addons.widgets.mediaplayer.api.IMediaPlayerSetupBuilder;
import org.jowidgets.addons.widgets.ole.api.IOleAutomation;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.io.FileUtils;
import org.jowidgets.util.io.ITempFileFactory;

class MediaPlayerImpl extends ControlWrapper implements IMediaPlayer {

	private static final String PROPERTY_URL = "URL";
	private static final String PROPERTY_CONTROLS = "controls";
	private static final String METHOD_STOP = "stop";
	private static final String METHOD_CLOSE = "close";
	private static final String WM_PLAYER_PROG_ID = "WMPlayer.OCX";

	private final ITempFileFactory tempFileFactory;
	private File tmpFile;

	public MediaPlayerImpl(final IOleControl oleControl, final IMediaPlayerSetupBuilder<?> setup) {
		super(oleControl);

		this.tempFileFactory = setup.getTempFileFactory();
		oleControl.setDocument(WM_PLAYER_PROG_ID);
	}

	@Override
	protected IOleControl getWidget() {
		return (IOleControl) super.getWidget();
	}

	@Override
	public void clear() {
		final IOleControl oleControl = getWidget();
		final IOleAutomation controls = oleControl.getAutomation().getProperty("controls");
		controls.invoke("stop");
		oleControl.getAutomation().invoke(METHOD_CLOSE);
		oleControl.setDocument(WM_PLAYER_PROG_ID);
		controls.dispose();

		deleteTempFile();
	}

	@Override
	public void open(final String url) {
		Assert.paramNotNull(url, "url");
		getWidget().getAutomation().setProperty(PROPERTY_URL, url);
	}

	@Override
	public void open(final File file) {
		Assert.paramNotNull(file, "file");
		try {
			open(file.toURI().toURL().toString());
		}
		catch (final MalformedURLException e) {
			throw new RuntimeException("Can not resolve file url", e);
		}
	}

	@Override
	public void open(final InputStream inputStream) {
		Assert.paramNotNull(inputStream, "inputStream");
		deleteTempFile();
		final File tmp = tempFileFactory.create("MediaPlayerTemp", ".wmv");
		FileUtils.inputStreamToFile(inputStream, tmp);
		open(tmp);
		tmpFile = tmp;
	}

	@Override
	public void dispose() {
		final IOleControl oleControl = getWidget();
		final IOleAutomation controls = oleControl.getAutomation().getProperty(PROPERTY_CONTROLS);
		controls.invoke(METHOD_STOP);
		oleControl.getAutomation().invoke(METHOD_CLOSE);
		controls.dispose();
		deleteTempFile();
		super.dispose();
	}

	private void deleteTempFile() {
		if (tmpFile != null) {
			tmpFile.delete();
		}
	}

}

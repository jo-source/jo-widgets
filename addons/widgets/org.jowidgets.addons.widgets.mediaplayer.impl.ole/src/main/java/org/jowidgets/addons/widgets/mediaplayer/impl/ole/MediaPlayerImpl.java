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

import org.jowidgets.addons.widgets.mediaplayer.api.IMediaPlayer;
import org.jowidgets.addons.widgets.mediaplayer.api.IMediaPlayerSetupBuilder;
import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;

class MediaPlayerImpl extends ControlWrapper implements IMediaPlayer {

	private static final String PROPERTY_URL = "URL";
	private static final String WM_PLAYER_PROG_ID = "WMPlayer.OCX";

	private final IMutableValue<IOleContext> mutableOleContext;

	private String filename;

	public MediaPlayerImpl(final IOleControl oleControl, final IMediaPlayerSetupBuilder<?> setup) {
		super(oleControl);
		this.mutableOleContext = oleControl.getContext();
		oleControl.getContext().addMutableValueListener(new IMutableValueListener<IOleContext>() {
			@Override
			public void changed(final IValueChangedEvent<IOleContext> event) {
				oleContextChanged();
			}
		});
		oleContextChanged();
	}

	private void oleContextChanged() {
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			oleContext.setDocument(WM_PLAYER_PROG_ID);
			if (filename != null) {
				open(filename);
			}
		}
	}

	@Override
	public void clear() {
		filename = null;
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			oleContext.setDocument(WM_PLAYER_PROG_ID);
		}
	}

	@Override
	public void open(final String filename) {
		Assert.paramNotNull(filename, "filename");
		this.filename = filename;
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			oleContext.getAutomation().setProperty(PROPERTY_URL, filename);
		}
	}

	@Override
	public void open(final File file) {
		Assert.paramNotNull(file, "file");
		open(file.getAbsolutePath());
	}

	@Override
	public void open(final InputStream inputStream) {
		//TODO MG implement this with help of a temp file
	}

}

/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import org.jowidgets.common.types.DialogResult;
import org.jowidgets.spi.widgets.IDirectoryChooserSpi;
import org.jowidgets.spi.widgets.setup.IDirectoryChooserSetupSpi;
import org.jowidgets.util.Assert;

public class DirectoryChooserImpl implements IDirectoryChooserSpi {

	private final JFileChooser fileChooser;
	private final Component parent;

	public DirectoryChooserImpl(final Object parentUiReference, final IDirectoryChooserSetupSpi setup) {
		this.parent = (Component) parentUiReference;
		this.fileChooser = new JFileChooser();

		if (setup.getTitle() != null) {
			fileChooser.setDialogTitle(setup.getTitle());
		}

		fileChooser.setMultiSelectionEnabled(false);

		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
	}

	@Override
	public Object getUiReference() {
		return fileChooser;
	}

	@Override
	public void setDirectory(final File file) {
		Assert.paramNotNull(file, "file");
		fileChooser.setSelectedFile(file);
	}

	@Override
	public DialogResult open() {
		final int result = fileChooser.showOpenDialog(parent);

		if (result == JFileChooser.APPROVE_OPTION) {
			return DialogResult.OK;
		}
		else {
			return DialogResult.CANCEL;
		}
	}

	@Override
	public File getDirectory() {
		return fileChooser.getSelectedFile();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (!enabled) {
			throw new IllegalArgumentException("Could not disable a file chooser");
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

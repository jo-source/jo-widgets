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
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jowidgets.common.types.DialogResult;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.types.IFileChooserFilter;
import org.jowidgets.spi.widgets.IFileChooserSpi;
import org.jowidgets.spi.widgets.setup.IFileChooserSetupSpi;
import org.jowidgets.util.Assert;

public class FileChooserImpl implements IFileChooserSpi {

	private final JFileChooser fileChooser;
	private final Component parent;
	private final FileChooserType fileChooserType;

	public FileChooserImpl(final Object parentUiReference, final IFileChooserSetupSpi setup) {
		this.parent = (Component) parentUiReference;
		this.fileChooserType = setup.getType();
		this.fileChooser = new JFileChooser();

		if (setup.getTitle() != null) {
			fileChooser.setDialogTitle(setup.getTitle());
		}

		if (setup.getType() == FileChooserType.OPEN_FILE_LIST) {
			fileChooser.setMultiSelectionEnabled(true);
		}
		else {
			fileChooser.setMultiSelectionEnabled(false);
		}

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);

		final List<IFileChooserFilter> filterList = setup.getFilterList();
		if (filterList != null && filterList.size() > 0) {
			FileFilter firstFileFilter = null;
			for (final IFileChooserFilter filter : filterList) {

				final String[] extensions = new String[filter.getExtensions().size()];
				int index = 0;
				for (final String extension : filter.getExtensions()) {
					extensions[index] = extension;
					index++;
				}
				final FileFilter fileFilter = new FileNameExtensionFilter(filter.getFilterName(), extensions);
				fileChooser.addChoosableFileFilter(fileFilter);
				if (firstFileFilter == null) {
					firstFileFilter = fileFilter;
				}
			}

			fileChooser.setFileFilter(firstFileFilter);
		}
	}

	@Override
	public Object getUiReference() {
		return fileChooser;
	}

	@Override
	public void setSelectedFile(final File file) {
		Assert.paramNotNull(file, "file");
		fileChooser.setSelectedFile(file);
	}

	@Override
	public DialogResult open() {
		int result = JFileChooser.CANCEL_OPTION;
		if (fileChooserType == FileChooserType.OPEN_FILE || fileChooserType == FileChooserType.OPEN_FILE_LIST) {
			result = fileChooser.showOpenDialog(parent);
		}
		else if (fileChooserType == FileChooserType.SAVE) {
			result = fileChooser.showSaveDialog(parent);
		}
		else {
			throw new IllegalStateException("The file chooser type '" + fileChooserType + "' is not supported");
		}

		if (result != JFileChooser.APPROVE_OPTION) {
			return DialogResult.OK;
		}
		else {
			return DialogResult.CANCEL;
		}
	}

	@Override
	public List<File> getSelectedFiles() {
		return Arrays.asList(fileChooser.getSelectedFiles());
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

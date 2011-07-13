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

package org.jowidgets.spi.impl.swt.widgets;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.types.DialogResult;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.types.IFileChooserFilter;
import org.jowidgets.spi.widgets.IFileChooserSpi;
import org.jowidgets.spi.widgets.setup.IFileChooserSetupSpi;
import org.jowidgets.util.Assert;

public class FileChooserImpl implements IFileChooserSpi {

	private static final String WILD_CARD = "*.";

	private final FileDialog fileDialog;

	public FileChooserImpl(final Object parentUiReference, final IFileChooserSetupSpi setup) {
		fileDialog = new FileDialog((Shell) parentUiReference, getStyle(setup));
		if (setup.getTitle() != null) {
			fileDialog.setText(setup.getTitle());
		}

		final List<IFileChooserFilter> filterList = setup.getFilterList();
		if (filterList != null) {
			final String[] filterNames = new String[filterList.size()];
			final String[] filterExtensions = new String[filterList.size()];

			int index = 0;
			for (final IFileChooserFilter filter : filterList) {
				filterNames[index] = filter.getFilterName();
				final StringBuilder extensions = new StringBuilder();
				for (final String extension : filter.getExtensions()) {
					extensions.append(WILD_CARD + extension + ";");
				}
				if (filter.getExtensions().size() > 0) {
					extensions.replace(extensions.length() - 1, extensions.length(), "");
				}
				filterExtensions[index] = extensions.toString();
				index++;
			}

			fileDialog.setFilterNames(filterNames);
			fileDialog.setFilterExtensions(filterExtensions);
		}
	}

	@Override
	public Object getUiReference() {
		return fileDialog;
	}

	@Override
	public void setSelectedFile(final File file) {
		Assert.paramNotNull(file, "file");
		fileDialog.setFilterPath(file.getAbsolutePath());
	}

	@Override
	public DialogResult open() {
		final String result = fileDialog.open();
		if (result != null) {
			return DialogResult.OK;
		}
		else {
			return DialogResult.CANCEL;
		}
	}

	@Override
	public List<File> getSelectedFiles() {
		final List<File> result = new LinkedList<File>();
		for (final String fileName : fileDialog.getFileNames()) {
			final String filterPath = fileDialog.getFilterPath();
			if (filterPath != null && !filterPath.trim().isEmpty()) {
				final File path = new File(filterPath);
				result.add(new File(path, fileName));
			}
			else {
				result.add(new File(fileName));
			}
		}
		return result;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (!enabled) {
			throw new IllegalArgumentException("Can not disable a file chooser");
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private static int getStyle(final IFileChooserSetupSpi setup) {
		int result = SWT.NONE;
		if (setup.getType() == FileChooserType.SAVE) {
			result = result | SWT.SAVE;
		}
		if (setup.getType() == FileChooserType.OPEN_FILE) {
			result = result | SWT.OPEN;
		}
		if (setup.getType() == FileChooserType.OPEN_FILE_LIST) {
			result = result | SWT.OPEN | SWT.MULTI;
		}
		return result;
	}
}

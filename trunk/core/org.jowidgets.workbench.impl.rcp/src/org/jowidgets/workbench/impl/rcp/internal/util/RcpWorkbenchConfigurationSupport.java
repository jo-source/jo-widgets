/*
 * Copyright (c) 2011, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.impl.rcp.internal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.eclipse.core.internal.runtime.DataArea;
import org.eclipse.core.runtime.IPath;

@SuppressWarnings("restriction")
public class RcpWorkbenchConfigurationSupport {
	private static final String WORKBENCH_BUNDLE = "org.eclipse.ui.workbench";
	private static final String WORKBENCH_FILENAME = "workbench.xml";
	private static final String WORKBENCH_FILE_ENCODING = "UTF-8";

	private final File workbenchFile;

	public RcpWorkbenchConfigurationSupport() {
		final DataArea dataArea = new DataArea();
		final IPath path = dataArea.getStateLocation(WORKBENCH_BUNDLE).append(WORKBENCH_FILENAME);
		workbenchFile = path.toFile();
	}

	public void setConfiguration(final String xml) {
		if (xml == null) {
			workbenchFile.delete();
			return;
		}
		try {
			final Writer writer = new OutputStreamWriter(new FileOutputStream(workbenchFile), WORKBENCH_FILE_ENCODING);
			try {
				writer.write(xml);
			}
			finally {
				writer.close();
			}
		}
		catch (final IOException e) {
			workbenchFile.delete();
		}
	}

	public String getConfiguration() {
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(workbenchFile),
				WORKBENCH_FILE_ENCODING));
			final StringBuilder builder = new StringBuilder();
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
					builder.append('\n');
				}
			}
			finally {
				reader.close();
			}
			return builder.toString();
		}
		catch (final IOException e) {
			return null;
		}
	}
}

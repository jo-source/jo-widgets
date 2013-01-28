/*
 * Copyright (c) 2012, grossmann, WHeckmann
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

package org.jowidgets.util.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.jowidgets.util.Assert;

public final class FileUtils {

	private FileUtils() {}

	public static void inputStreamToFile(final InputStream inputStream, final File file) {
		Assert.paramNotNull(inputStream, "inputStream");
		Assert.paramNotNull(file, "file");

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			inputStreamToOutputStream(inputStream, outputStream);
		}
		catch (final Exception e) {
			throw new RuntimeException(e) {

				private static final long serialVersionUID = -71819256116172748L;

				@Override
				public String getLocalizedMessage() {
					return e.getLocalizedMessage();
				}
			};
		}
		finally {
			tryCloseSilent(outputStream);
		}

	}

	public static void fileToOutputStream(final File file, final OutputStream outputStream) {
		Assert.paramNotNull(file, "file");
		Assert.paramNotNull(outputStream, "outputStream");

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			inputStreamToOutputStream(inputStream, outputStream);
		}
		catch (final Exception e) {
			throw new RuntimeException(e) {

				private static final long serialVersionUID = -71819256116172748L;

				@Override
				public String getLocalizedMessage() {
					return e.getLocalizedMessage();
				}
			};
		}
		finally {
			tryCloseSilent(inputStream);
		}
	}

	public static void copyFile(final File source, final File target) {
		Assert.paramNotNull(source, "source");
		Assert.paramNotNull(target, "target");

		FileChannel sourceChannel = null;
		FileChannel targetChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			targetChannel = new FileOutputStream(target).getChannel();
			targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		}
		catch (final IOException e) {
			throw new RuntimeException(e) {

				private static final long serialVersionUID = -71819256116172748L;

				@Override
				public String getLocalizedMessage() {
					return e.getLocalizedMessage();
				}
			};
		}
		finally {
			tryCloseSilent(sourceChannel);
			tryCloseSilent(targetChannel);
		}
	}

	public static void inputStreamToOutputStream(final InputStream inputStream, final OutputStream outputStream) {
		IoUtils.inputStreamToOutputStream(inputStream, outputStream);
	}

	public static String getRelativePath(final File rootPath, final File path) {
		Assert.paramNotNull(rootPath, "rootPath");
		Assert.paramNotNull(path, "path");
		return getRelativePath(rootPath.getAbsolutePath(), path.getAbsolutePath());
	}

	public static String getRelativePath(final String rootPath, final String path) {
		Assert.paramNotNull(rootPath, "rootPath");
		Assert.paramNotNull(path, "path");
		final String regExp = getRegExp(File.separator);
		final String[] rootPathSplit = rootPath.split(regExp);
		final String[] pathSplit = path.split(regExp);
		if (pathSplit.length < rootPathSplit.length) {
			throw new IllegalArgumentException("The root path is not a root path of the path");
		}
		for (int i = 0; i < rootPathSplit.length; i++) {
			if (!rootPathSplit[i].equals(pathSplit[i])) {
				throw new IllegalArgumentException("The root path is not a root path of the path");
			}
		}
		final StringBuilder result = new StringBuilder();
		for (int i = rootPathSplit.length; i < pathSplit.length; i++) {
			result.append(pathSplit[i]);
			if (i < pathSplit.length - 1) {
				result.append(File.separator);
			}
		}
		return result.toString();
	}

	private static String getRegExp(final String separator) {
		if ("\\".equals(separator)) {
			return "\\" + separator;
		}
		else {
			return separator;
		}
	}

	public static void tryCloseSilent(final Closeable closeable) {
		IoUtils.tryCloseSilent(closeable);
	}

}

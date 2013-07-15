/*
 * Copyright (c) 2010, grossmann
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

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class FileUtilsTest {

	private static final String SEP = File.separator;

	@Test
	public void getRelativePathTest1() {
		final String rootPath = "a" + SEP + "b" + SEP + "c";
		final String path = "a" + SEP + "b" + SEP + "c" + SEP + "d" + SEP + "e";
		final String relativePath = FileUtils.getRelativePath(rootPath, path);
		Assert.assertEquals("d" + SEP + "e", relativePath);
	}

	@Test
	public void getRelativePathTest2() {
		final String rootPath = "a" + SEP + "b" + SEP + "c" + SEP;
		final String path = "a" + SEP + "b" + SEP + "c" + SEP + "d" + SEP + "e";
		final String relativePath = FileUtils.getRelativePath(rootPath, path);
		Assert.assertEquals("d" + SEP + "e", relativePath);
	}

	@Test
	public void getRelativePathTest3() {
		final String rootPath = SEP + "a" + SEP + "b" + SEP + "c" + SEP;
		final String path = SEP + "a" + SEP + "b" + SEP + "c" + SEP + "d" + SEP + "e";
		final String relativePath = FileUtils.getRelativePath(rootPath, path);
		Assert.assertEquals("d" + SEP + "e", relativePath);
	}

	@Test
	public void getRelativePathTest4() {
		final String rootPath = "a" + SEP + "b" + SEP + "c";
		final String path = "a" + SEP + "b" + SEP + "c" + SEP + "d";
		final String relativePath = FileUtils.getRelativePath(rootPath, path);
		Assert.assertEquals("d", relativePath);
	}

	@Test
	public void getRelativePathTest5() {
		final String rootPath = "a" + SEP + "b" + SEP + "c";
		final String path = "a" + SEP + "b" + SEP + "c" + SEP;
		final String relativePath = FileUtils.getRelativePath(rootPath, path);
		Assert.assertEquals("", relativePath);
	}

	@Test
	public void getRelativePathTest6() {
		final String rootPath = "a" + SEP + "b" + SEP + "c";
		final String path = "a" + SEP + "b" + SEP + "c";
		final String relativePath = FileUtils.getRelativePath(rootPath, path);
		Assert.assertEquals("", relativePath);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRelativePathTestFailure1() {
		final String rootPath = "a" + SEP + "b" + SEP + "c";
		final String path = "a" + SEP + "b" + SEP;
		FileUtils.getRelativePath(rootPath, path);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRelativePathTestFailure2() {
		final String rootPath = "a" + SEP + "b" + SEP + "c";
		final String path = "x" + SEP + "b" + SEP + "c" + SEP + "d" + SEP + "e";
		FileUtils.getRelativePath(rootPath, path);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRelativePathTestFailure3() {
		final String rootPath = "a" + SEP + "b" + SEP + "c";
		final String path = "a" + SEP + "b" + SEP + "x" + SEP + "d" + SEP + "e";
		FileUtils.getRelativePath(rootPath, path);
	}
}

/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.addons.testtool;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jowidgets.util.Assert;

// TODO LG maybe its better to use XStream instead of JAXB. Remove workarounds for JAXB.
@XmlRootElement(name = "UserTestData")
public class TestDataXmlPersister implements IPersister {

	private static final String DEFAULT_FILEPATH = File.separator + "resources" + File.separator + "test";
	private static final String FILE_EXTENSION = ".xml";
	private final String filePath;
	@XmlElement
	private final LinkedList<TestDataObject> testObjects;

	// This constructor is necessary for JAXB
	public TestDataXmlPersister() {
		this(DEFAULT_FILEPATH);
	}

	public TestDataXmlPersister(final String filePath) {
		Assert.paramNotEmpty(filePath, "filePath");
		this.filePath = filePath;
		this.testObjects = new LinkedList<TestDataObject>();
	}

	@Override
	public List<TestDataObject> load(final String fileName) {
		final TestDataXmlPersister result = JAXB.unmarshal(
				new File(getDirectory() + File.separator + fileName + FILE_EXTENSION),
				this.getClass());
		return result.getTestData();
	}

	@Override
	public void save(final List<TestDataObject> list, final String fileName) {
		// Workaround for JAXB to save the given list with its children.
		testObjects.addAll(list);
		JAXB.marshal(this, new File(getDirectory() + File.separator + fileName + FILE_EXTENSION));
		testObjects.removeAll(testObjects);
	}

	private File getDirectory() {
		final File dir = new File(System.getProperty("user.dir") + filePath);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException("Couldn't create directory for user tests!");
			}
		}
		return dir;
	}

	// Needed to get the TestDataObjects from the loaded TestDataXmlPersister class.
	private List<TestDataObject> getTestData() {
		return testObjects;
	}
}

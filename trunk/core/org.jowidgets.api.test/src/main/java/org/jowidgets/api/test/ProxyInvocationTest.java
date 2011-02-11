/*
 * Copyright (c) 2011, marstaller
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

package org.jowidgets.api.test;

import junit.framework.JUnit4TestAdapter;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.jowidgets.api.test.blueprint.DummyBluePrintFactory;
import org.jowidgets.api.test.blueprint.builder.IHierarchy1stSetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy2nd1SetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy2nd2SetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy3rdSetupBuilder;
import org.jowidgets.api.test.blueprint.convenience.DummySetupBuilderConvenienceRegistry;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy1stDefaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy2nd1Defaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy2nd2Defaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy3rdDefaults;
import org.jowidgets.api.test.blueprint.defaults.registry.DummyDefaultsInitializerRegistry;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class ProxyInvocationTest {

	private static final IBluePrintFactory BLUE_PRINT_FACTORY = Toolkit.getBluePrintFactory();
	private DummyBluePrintFactory dummyBluePrintFactory;
	private IMocksControl easyMock;

	private Hierarchy1stDefaults hierarchy1stDefaultMock;
	private Hierarchy2nd1Defaults hierarchy2nd1DefaultMock;
	private Hierarchy2nd2Defaults hierarchy2nd2DefaultMock;
	private Hierarchy3rdDefaults hierarchy3rdDefaultMock;

	@Before
	public void setUp() {
		easyMock = EasyMock.createControl();
		hierarchy1stDefaultMock = easyMock.createMock(Hierarchy1stDefaults.class);
		hierarchy3rdDefaultMock = easyMock.createMock(Hierarchy3rdDefaults.class);
		hierarchy2nd1DefaultMock = easyMock.createMock(Hierarchy2nd1Defaults.class);
		hierarchy2nd2DefaultMock = easyMock.createMock(Hierarchy2nd2Defaults.class);
		dummyBluePrintFactory = new DummyBluePrintFactory(BLUE_PRINT_FACTORY);
		dummyBluePrintFactory.addSetupBuilderConvenienceRegistry(new DummySetupBuilderConvenienceRegistry());
		dummyBluePrintFactory.setDefaultsInitializerRegistry(new DummyDefaultsInitializerRegistry(
			hierarchy1stDefaultMock,
			hierarchy2nd1DefaultMock,
			hierarchy2nd2DefaultMock,
			hierarchy3rdDefaultMock));
	}

	@After
	public void tearDown() {
		easyMock.verify();
	}

	@Test
	public void testHierarchy1st() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));

		EasyMock.replay(hierarchy1stDefaultMock);

		dummyBluePrintFactory.hierarchy1st();
	}

	@Test
	public void testHierarchy2nd1() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd1DefaultMock.initialize(EasyMock.isA(IHierarchy2nd1SetupBuilder.class));

		EasyMock.replay(hierarchy2nd1DefaultMock);

		dummyBluePrintFactory.hierarchy2nd1();
	}

	@Test
	public void testHierarchy2nd2() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));

		EasyMock.replay(hierarchy2nd2DefaultMock);

		dummyBluePrintFactory.hierarchy2nd2();
	}

	@Test
	public void testHierarchy3rd() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd1DefaultMock.initialize(EasyMock.isA(IHierarchy2nd1SetupBuilder.class));
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));
		hierarchy3rdDefaultMock.initialize(EasyMock.isA(IHierarchy3rdSetupBuilder.class));

		EasyMock.replay(hierarchy3rdDefaultMock);

		dummyBluePrintFactory.hierarchy3rd();
	}

	public static junit.framework.Test suite() {
		final JUnit4TestAdapter suite = new JUnit4TestAdapter(ProxyInvocationTest.class);
		return suite;
	}

}

/*
 * Copyright (c) 2011, Benjamin Marstaller
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

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.jowidgets.api.test.blueprint.DummyBluePrintFactory;
import org.jowidgets.api.test.blueprint.IHierarchy2nd2BluePrint;
import org.jowidgets.api.test.blueprint.IHierarchy3rdBluePrint;
import org.jowidgets.api.test.blueprint.builder.IHierarchy1stSetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy2nd1SetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy2nd2SetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy2nd3SetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy3rdSetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy4thSetupBuilder;
import org.jowidgets.api.test.blueprint.convenience.Hierarchy2nd2Convenience;
import org.jowidgets.api.test.blueprint.convenience.Hierarchy3rdConvenience;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy1stDefaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy2nd1Defaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy2nd2Defaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy2nd3Defaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy3rdDefaults;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy3rdDefaultsOverriden;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy4thDefaults;
import org.jowidgets.api.test.blueprint.defaults.registry.DummyDefaultsInitializerRegistry;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BluePrintFactoryTest {

	private static final IBluePrintFactory BLUE_PRINT_FACTORY = Toolkit.getBluePrintFactory();
	private DummyBluePrintFactory dummyBluePrintFactory;
	private IMocksControl easyMock;

	private Hierarchy1stDefaults hierarchy1stDefaultMock;
	private Hierarchy2nd1Defaults hierarchy2nd1DefaultMock;
	private Hierarchy2nd2Defaults hierarchy2nd2DefaultMock;
	private Hierarchy3rdDefaults hierarchy3rdDefaultMock;
	private Hierarchy3rdDefaultsOverriden hierarchy3rdDefaultOveriddenMock;
	private Hierarchy4thDefaults hierarchy4thDefaultMock;
	private IDefaultsInitializerRegistry defaultsInitializerRegistry;
	private Hierarchy2nd2Convenience setupBuilderConvenience2nd2Mock;
	private Hierarchy3rdConvenience setupBuilderConvenience3rdMock;
	private Hierarchy2nd3Defaults hierarchy2nd3DefaultMock;

	@Before
	public void setUp() {
		defaultsInitializerRegistry = BLUE_PRINT_FACTORY.getDefaultsInitializerRegistry();

		easyMock = EasyMock.createStrictControl();

		hierarchy1stDefaultMock = easyMock.createMock(Hierarchy1stDefaults.class);
		hierarchy3rdDefaultMock = easyMock.createMock(Hierarchy3rdDefaults.class);
		hierarchy4thDefaultMock = easyMock.createMock(Hierarchy4thDefaults.class);
		hierarchy3rdDefaultOveriddenMock = easyMock.createMock(Hierarchy3rdDefaultsOverriden.class);
		hierarchy2nd1DefaultMock = easyMock.createMock(Hierarchy2nd1Defaults.class);
		hierarchy2nd2DefaultMock = easyMock.createMock(Hierarchy2nd2Defaults.class);
		hierarchy2nd3DefaultMock = easyMock.createMock(Hierarchy2nd3Defaults.class);

		setupBuilderConvenience2nd2Mock = easyMock.createMock(Hierarchy2nd2Convenience.class);
		setupBuilderConvenience3rdMock = easyMock.createMock(Hierarchy3rdConvenience.class);

		dummyBluePrintFactory = new DummyBluePrintFactory(BLUE_PRINT_FACTORY);
		dummyBluePrintFactory.setSetupBuilderConvenience(IHierarchy2nd2SetupBuilder.class, setupBuilderConvenience2nd2Mock);
		dummyBluePrintFactory.setSetupBuilderConvenience(IHierarchy3rdSetupBuilder.class, setupBuilderConvenience3rdMock);
		dummyBluePrintFactory.setDefaultsInitializerRegistry(new DummyDefaultsInitializerRegistry(
			hierarchy1stDefaultMock,
			hierarchy2nd1DefaultMock,
			hierarchy2nd2DefaultMock,
			hierarchy2nd3DefaultMock,
			hierarchy3rdDefaultMock,
			hierarchy4thDefaultMock));
		dummyBluePrintFactory.getSetupBuilderConvenienceRegistry();
	}

	@After
	public void tearDown() {
		dummyBluePrintFactory.setDefaultsInitializerRegistry(defaultsInitializerRegistry);
		easyMock.verify();
	}

	@Test
	public void testInitializerHierarchy1st() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.hierarchy1st();
	}

	@Test
	public void testInitializerHierarchy2nd1() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd1DefaultMock.initialize(EasyMock.isA(IHierarchy2nd1SetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.hierarchy2nd1();
	}

	@Test
	public void testInitializerHierarchy2nd2() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.hierarchy2nd2();
	}

	@Test
	public void testInitializerHierarchy2nd3() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd3DefaultMock.initialize(EasyMock.isA(IHierarchy2nd3SetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.hierarchy2nd3();
	}

	@Test
	public void testInitializerHierarchy3rd() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));
		hierarchy3rdDefaultMock.initialize(EasyMock.isA(IHierarchy3rdSetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.hierarchy3rd();
	}

	@Test
	public void testInitializerHierarchy3rdAdded() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));
		hierarchy3rdDefaultMock.initialize(EasyMock.isA(IHierarchy3rdSetupBuilder.class));
		hierarchy3rdDefaultOveriddenMock.initialize(EasyMock.isA(IHierarchy3rdSetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.addDefaultsInitializer(IHierarchy3rdSetupBuilder.class, hierarchy3rdDefaultOveriddenMock);
		dummyBluePrintFactory.hierarchy3rd();
	}

	@Test
	public void testInitializerHierarchy3rdOveridden() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));
		hierarchy3rdDefaultOveriddenMock.initialize(EasyMock.isA(IHierarchy3rdSetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.setDefaultsInitializer(IHierarchy3rdSetupBuilder.class, hierarchy3rdDefaultOveriddenMock);
		dummyBluePrintFactory.hierarchy3rd();
	}

	@Test
	public void testInitializerHierarchy4th() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd1DefaultMock.initialize(EasyMock.isA(IHierarchy2nd1SetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));
		hierarchy3rdDefaultMock.initialize(EasyMock.isA(IHierarchy3rdSetupBuilder.class));
		hierarchy2nd3DefaultMock.initialize(EasyMock.isA(IHierarchy2nd3SetupBuilder.class));
		hierarchy4thDefaultMock.initialize(EasyMock.isA(IHierarchy4thSetupBuilder.class));

		easyMock.replay();

		dummyBluePrintFactory.hierarchy4th();
	}

	@Test
	public void testConvenience2nd2() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));
		EasyMock.expect(setupBuilderConvenience2nd2Mock.introduce(EasyMock.isA(String.class))).andReturn(null);

		easyMock.replay();

		final IHierarchy2nd2BluePrint hierarchy2nd2 = dummyBluePrintFactory.hierarchy2nd2();
		hierarchy2nd2.introduce("");
	}

	@Test
	public void testConvenience3rd() {
		hierarchy1stDefaultMock.initialize(EasyMock.isA(IHierarchy1stSetupBuilder.class));
		hierarchy2nd2DefaultMock.initialize(EasyMock.isA(IHierarchy2nd2SetupBuilder.class));
		hierarchy3rdDefaultMock.initialize(EasyMock.isA(IHierarchy3rdSetupBuilder.class));
		EasyMock.expect(setupBuilderConvenience3rdMock.introduce(EasyMock.isA(String.class))).andReturn(null);

		easyMock.replay();

		final IHierarchy3rdBluePrint hierarchy3rd = dummyBluePrintFactory.hierarchy3rd();
		hierarchy3rd.introduce("");
	}

}

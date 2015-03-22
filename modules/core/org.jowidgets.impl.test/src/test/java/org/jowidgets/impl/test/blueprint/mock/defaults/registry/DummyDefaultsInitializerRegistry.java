/*
 * Copyright (c) 2011, bemarsta
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

/**
 * 
 */
package org.jowidgets.impl.test.blueprint.mock.defaults.registry;

import org.jowidgets.api.widgets.blueprint.factory.IBluePrintProxyFactory;
import org.jowidgets.impl.test.blueprint.mock.builder.IHierarchy1stSetupBuilder;
import org.jowidgets.impl.test.blueprint.mock.builder.IHierarchy2nd1SetupBuilder;
import org.jowidgets.impl.test.blueprint.mock.builder.IHierarchy2nd2SetupBuilder;
import org.jowidgets.impl.test.blueprint.mock.builder.IHierarchy2nd3SetupBuilder;
import org.jowidgets.impl.test.blueprint.mock.builder.IHierarchy3rdSetupBuilder;
import org.jowidgets.impl.test.blueprint.mock.builder.IHierarchy4thSetupBuilder;
import org.jowidgets.impl.test.blueprint.mock.defaults.Hierarchy1stDefaults;
import org.jowidgets.impl.test.blueprint.mock.defaults.Hierarchy2nd1Defaults;
import org.jowidgets.impl.test.blueprint.mock.defaults.Hierarchy2nd2Defaults;
import org.jowidgets.impl.test.blueprint.mock.defaults.Hierarchy2nd3Defaults;
import org.jowidgets.impl.test.blueprint.mock.defaults.Hierarchy3rdDefaults;
import org.jowidgets.impl.test.blueprint.mock.defaults.Hierarchy4thDefaults;

public class DummyDefaultsInitializerRegistry {

    public DummyDefaultsInitializerRegistry(
        final IBluePrintProxyFactory bppf,
        final Hierarchy1stDefaults hierarchyOneDefaultInitializer,
        final Hierarchy2nd1Defaults hierarchyTwoOneDefaultInitializer,
        final Hierarchy2nd2Defaults hierarchyTwoTwoDefaultInitializer,
        final Hierarchy2nd3Defaults hierarchyTwoThreeDefaultInitializer,
        final Hierarchy3rdDefaults hierarchyThreeDefaultInitializer,
        final Hierarchy4thDefaults hierarchyFourDefaultInitializer) {
        super();
        bppf.addDefaultsInitializer(IHierarchy1stSetupBuilder.class, hierarchyOneDefaultInitializer);
        bppf.addDefaultsInitializer(IHierarchy2nd1SetupBuilder.class, hierarchyTwoOneDefaultInitializer);
        bppf.addDefaultsInitializer(IHierarchy2nd2SetupBuilder.class, hierarchyTwoTwoDefaultInitializer);
        bppf.addDefaultsInitializer(IHierarchy2nd3SetupBuilder.class, hierarchyTwoThreeDefaultInitializer);
        bppf.addDefaultsInitializer(IHierarchy3rdSetupBuilder.class, hierarchyThreeDefaultInitializer);
        bppf.addDefaultsInitializer(IHierarchy4thSetupBuilder.class, hierarchyFourDefaultInitializer);
    }

}

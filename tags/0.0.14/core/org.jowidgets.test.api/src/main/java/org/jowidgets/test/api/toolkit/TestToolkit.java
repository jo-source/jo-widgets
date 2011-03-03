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

package org.jowidgets.test.api.toolkit;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.test.api.widgets.IFrameUi;
import org.jowidgets.test.api.widgets.blueprint.factory.IBasicSimpleTestBluePrintFactory;
import org.jowidgets.test.api.widgets.descriptor.IFrameDescriptorUi;

public final class TestToolkit {

	private static ITestToolkit toolkit;

	private TestToolkit() {

	}

	public static synchronized ITestToolkit getInstance() {
		if (toolkit == null) {
			final ServiceLoader<ITestToolkit> toolkitProviderLoader = ServiceLoader.load(ITestToolkit.class);
			final Iterator<ITestToolkit> iterator = toolkitProviderLoader.iterator();

			if (!iterator.hasNext()) {
				throw new IllegalStateException("No implementation found for '" + ITestToolkit.class.getName() + "'");
			}

			TestToolkit.toolkit = iterator.next();

			if (iterator.hasNext()) {
				throw new IllegalStateException("More than one implementation found for '" + ITestToolkit.class.getName() + "'");
			}

		}
		return toolkit;
	}

	public static IGenericWidgetFactory getWidgetFactory() {
		return getInstance().getWidgetFactory();
	}

	public static IBasicSimpleTestBluePrintFactory getBluePrintFactory() {
		return getInstance().getBluePrintFactory();
	}

	public static IFrameUi createRootFrame(final IFrameDescriptorUi descriptor) {
		return getInstance().createRootFrame(descriptor);
	}

	public static IFrameUi createRootFrame(final IFrameDescriptorUi descriptor, final IApplicationLifecycle lifecycle) {
		return getInstance().createRootFrame(descriptor, lifecycle);
	}
}

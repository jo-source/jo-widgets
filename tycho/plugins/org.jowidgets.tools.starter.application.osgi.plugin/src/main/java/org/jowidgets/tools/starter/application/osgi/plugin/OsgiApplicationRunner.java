/*
 * Copyright (c) 2014, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.tools.starter.application.osgi.plugin;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.tools.starter.classloading.BundleContextClassLoaderRegisterer;
import org.jowidgets.util.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class OsgiApplicationRunner implements BundleActivator {

	private final IApplication application;
	private final String[] includePath;

	public OsgiApplicationRunner(final IApplication application) {
		this(application, new String[] {});
	}

	public OsgiApplicationRunner(final IApplication application, final String[] includePath) {
		Assert.paramNotNull(application, "application");
		Assert.paramNotNull(includePath, "packageNames");
		this.application = application;
		this.includePath = new String[includePath.length + 1];
		this.includePath[0] = "org.jowidgets";
		for (int i = 0; i < includePath.length; i++) {
			this.includePath[i + 1] = includePath[i];
		}
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		BundleContextClassLoaderRegisterer.registerClassLoaders(context, includePath);

		final Bundle bundle = context.getBundle();
		context.addBundleListener(new BundleListener() {
			@Override
			public void bundleChanged(final BundleEvent event) {
				if (event.getBundle().equals(bundle) && BundleEvent.STARTED == event.getType()) {
					Toolkit.getApplicationRunner().run(application);
				}
			}
		});
	}

	@Override
	public void stop(final BundleContext context) throws Exception {}

}

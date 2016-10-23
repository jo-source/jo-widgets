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
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.util.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class OsgiApplicationRunner implements BundleActivator {

	private final IApplication application;

	private IApplicationLifecycle lifecycle;

	@Deprecated
	/**
	 * Creates a new OsgiAplication runner.
	 * 
	 * @param application The application to run
	 * @param includePath The include path
	 * 
	 * @deprecated Do no longer use the ClassLoadingActivator, instead use the
	 *             org.jowidgets.classloading.weaving.plugin and autostart this before any
	 *             "Shared-Classloader-Contribution" consuming bundle has been started, e.g.
	 *             start level 2.
	 */
	public OsgiApplicationRunner(final IApplication application, final String[] includePath) {
		this(application);
	}

	public OsgiApplicationRunner(final IApplication application) {
		Assert.paramNotNull(application, "application");
		this.application = application;
	}

	@Override
	public void start(final BundleContext context) throws Exception {

		final Bundle bundle = context.getBundle();
		context.addBundleListener(new BundleListener() {
			@Override
			public void bundleChanged(final BundleEvent event) {
				if (event.getBundle().equals(bundle) && BundleEvent.STARTED == event.getType()) {
					bundleStartet();
				}
			}
		});
	}

	private void bundleStartet() {
		Toolkit.getApplicationRunner().run(new IApplication() {
			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				OsgiApplicationRunner.this.lifecycle = lifecycle;
				application.start(lifecycle);
			}
		});
		System.exit(0);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		final IApplicationLifecycle currentLifecycle = lifecycle;
		if (currentLifecycle != null) {
			currentLifecycle.finish();
		}
	}

}

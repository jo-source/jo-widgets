/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.tools.starter.classloading;

import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.util.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * @deprecated Do no longer use the BundleContextClassLoaderRegisterer, instead use the
 *             org.jowidgets.classloading.weaving.plugin and autostart this before any
 *             "Shared-Classloader-Contribution" consuming bundle has been started, e.g.
 *             start level 2.
 */
@Deprecated
public final class BundleContextClassLoaderRegisterer {

	private BundleContextClassLoaderRegisterer() {}

	public static void registerClassLoaders(final BundleContext bundleContext, final String includePath) {
		Assert.paramNotNull(includePath, "includePath");
		registerClassLoaders(bundleContext, new String[] {includePath});
	}

	public static void registerClassLoaders(final BundleContext bundleContext, final String[] includePaths) {

		Assert.paramNotNull(bundleContext, "bundleContext");

		final Bundle[] bundles = bundleContext.getBundles();
		for (int i = 0; i < bundles.length; i++) {
			final Bundle bundle = bundles[i];
			if (includeBundle(bundle, includePaths)) {
				SharedClassLoader.addClassLoader(new BundleClassLoader(bundle));
			}
		}

		bundleContext.addBundleListener(new BundleListener() {

			@Override
			public void bundleChanged(final BundleEvent event) {
				if (event.getType() == BundleEvent.STARTED) {
					final Bundle bundle = event.getBundle();
					if (includeBundle(bundle, includePaths)) {
						SharedClassLoader.addClassLoader(new BundleClassLoader(bundle));
					}
				}

			}
		});
	}

	private static boolean includeBundle(final Bundle bundle, final String[] includePaths) {
		Assert.paramNotNull(bundle, "bundle");
		if (includePaths != null) {
			for (final String includePath : includePaths) {
				if (matches(bundle.getSymbolicName(), includePath)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean matches(final String bundleName, final String path) {
		if (bundleName.startsWith(path)) {
			return true;
		}
		else {
			return false;
		}
	}
}

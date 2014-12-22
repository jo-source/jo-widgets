/*
 * Copyright (c) 2013, grossmann
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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.jowidgets.classloading.api.IClassLoader;
import org.jowidgets.util.Assert;
import org.osgi.framework.Bundle;

public final class BundleClassLoader implements IClassLoader {

	private final Bundle bundle;
	private final long bundleId;

	public BundleClassLoader(final Bundle bundle) {
		Assert.paramNotNull(bundle, "bundle");
		this.bundle = bundle;
		this.bundleId = bundle.getBundleId();
	}

	@Override
	public Class<?> findClass(final String name) throws ClassNotFoundException {
		return bundle.loadClass(name);
	}

	@Override
	public URL findResource(final String name) {
		return bundle.getResource(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<URL> findResources(final String name) throws IOException {
		return bundle.getResources(name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bundleId ^ (bundleId >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BundleClassLoader)) {
			return false;
		}
		final BundleClassLoader other = (BundleClassLoader) obj;
		if (bundleId != other.bundleId) {
			return false;
		}
		return true;
	}

}

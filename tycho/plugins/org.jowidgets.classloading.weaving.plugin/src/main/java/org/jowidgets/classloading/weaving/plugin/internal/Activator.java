/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.classloading.weaving.plugin.internal;

import java.util.Dictionary;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.tools.starter.classloading.BundleClassLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public final class Activator extends AbstractUIPlugin {

	private static final String SHARED_CLASSLOADER_CONTRIBUTION = "Shared-Classloader-Contribution";
	private static final String TRUE_STRING = "true";

	@Override
	public void start(final BundleContext context) throws Exception {
		for (final Bundle bundle : context.getBundles()) {
			if (hasSharedClassloaderContribution(bundle)) {
				SharedClassLoader.addClassLoader(new BundleClassLoader(bundle));
			}
		}
	}

	private static boolean hasSharedClassloaderContribution(final Bundle bundle) {
		@SuppressWarnings("unchecked")
		final Dictionary<Object, Object> headers = bundle.getHeaders();
		final Object shared = headers.get(SHARED_CLASSLOADER_CONTRIBUTION);
		if (shared != null && shared.equals(TRUE_STRING)) {
			return true;
		}
		else {
			return false;
		}
	}

}

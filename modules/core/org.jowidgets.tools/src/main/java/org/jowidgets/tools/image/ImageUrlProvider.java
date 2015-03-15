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

package org.jowidgets.tools.image;

import java.io.File;
import java.net.URL;

import org.jowidgets.common.image.IImageUrlProvider;
import org.jowidgets.util.cache.Cacheable;
import org.jowidgets.util.cache.ICacheable;
import org.jowidgets.util.cache.ICacheableListener;

public class ImageUrlProvider extends UrlImageDescriptor implements IImageUrlProvider, ICacheable {

	private final Cacheable cacheable = new Cacheable();

	/**
	 * Creates a new ImageUrlProvider from a file
	 * 
	 * @param file A File used to create the url from
	 * 
	 * @throws IllegalArgumentException if the url is malformed
	 */
	public ImageUrlProvider(final File file) {
		super(file);
	}

	/**
	 * Creates a new ImageUrlProvider
	 * 
	 * @param url A String defining the url
	 * 
	 * @throws IllegalArgumentException if the url is malformed
	 */
	public ImageUrlProvider(final String url) {
		super(url);
	}

	/**
	 * Creates a new ImageUrlProvider
	 * 
	 * @param url The url to use, must not be null
	 */
	public ImageUrlProvider(final URL url) {
		super(url);
	}

	@Override
	public final void addCacheableListener(final ICacheableListener listener) {
		cacheable.addCacheableListener(listener);
	}

	@Override
	public final void removeCacheableListener(final ICacheableListener listener) {
		cacheable.removeCacheableListener(listener);
	}

	@Override
	public final void release() {
		cacheable.release();
	}

}

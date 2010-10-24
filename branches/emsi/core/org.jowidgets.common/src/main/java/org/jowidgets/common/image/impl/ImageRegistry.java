/*
 * Copyright (c) 2010, Manuel Woelker, Michael Grossmann
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
package org.jowidgets.common.image.impl;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IImageHandle;
import org.jowidgets.common.image.IImageHandleProvider;
import org.jowidgets.common.image.IImageRegistry;

public class ImageRegistry implements IImageRegistry {

	private final IImageHandleProvider imageHandleProvider;
	private final Map<Object, IImageHandle> imageMap = new HashMap<Object, IImageHandle>();

	public ImageRegistry(final IImageHandleProvider imageService) {
		this.imageHandleProvider = imageService;
	}

	@Override
	public <T extends Enum<T> & IImageConstant> void registerImageConstants(final Class<T> enumClass) {
		for (final T value : enumClass.getEnumConstants()) {
			register(value, imageHandleProvider.getImageHandle(value.getDefaultUrl()));
		}
	}

	public synchronized void register(final Object key, final IImageHandle imageHandle) {
		imageMap.put(key, imageHandle);
	}

	public synchronized IImageHandle getImageHandle(final Object key) {
		return imageMap.get(key);
	}

}

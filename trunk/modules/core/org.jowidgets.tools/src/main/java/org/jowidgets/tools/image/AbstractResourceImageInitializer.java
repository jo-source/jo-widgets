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

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.util.Assert;

public abstract class AbstractResourceImageInitializer {

	private final ClassLoader resourceClassLoader;
	private final IImageRegistry imageRegistry;
	private final String resourcesPath;

	/**
	 * Creates a new instance
	 * 
	 * @param resourceClass A class that has the same class loader then the resources that holds the image file, e.g. the concrete
	 *            class that is derived from this class
	 * @param imageRegistry The image registry to use
	 * @param resourcesPath The root path of the resources
	 */
	public AbstractResourceImageInitializer(
		final Class<?> resourceClass,
		final IImageRegistry imageRegistry,
		final String resourcesPath) {
		this(resourceClass.getClassLoader(), imageRegistry, resourcesPath);
	}

	/**
	 * Creates a new instance
	 * 
	 * @param resourceClassLoader The class loader to access the resources that holds the image file
	 * @param imageRegistry The image registry to use
	 * @param resourcesPath The root path of the resources
	 */
	public AbstractResourceImageInitializer(
		final ClassLoader resourceClassLoader,
		final IImageRegistry imageRegistry,
		final String resourcesPath) {

		Assert.paramNotNull(resourceClassLoader, "resourceClassLoader");
		Assert.paramNotNull(imageRegistry, "imageRegistry");
		Assert.paramNotNull(resourcesPath, "resourcesPath");

		this.resourceClassLoader = resourceClassLoader;
		this.imageRegistry = imageRegistry;
		this.resourcesPath = resourcesPath;
	}

	/**
	 * Register a image from the resources
	 * 
	 * @param key The key of the image to register
	 * @param resourceName The name of the resource (will be appended to the resource path)
	 */
	protected void registerResourceImage(final IImageConstant key, final String resourceName) {
		Assert.paramNotNull(key, "resourceName");
		final URL url = resourceClassLoader.getResource(resourcesPath + resourceName);
		imageRegistry.registerImageConstant(key, url);
	}

	/**
	 * Registers a image with help of another image key
	 * 
	 * @param key The key to register
	 * @param substitude The (already available) image to use as substitude
	 */
	protected void registerSubstitudeIcon(final IImageConstant key, final IImageConstant substitude) {
		Assert.paramNotNull(key, "resourceName");
		Assert.paramNotNull(substitude, "substitude");
		imageRegistry.registerImageConstant(key, substitude);
	}

	/**
	 * Check if all keys of the given enum are available in the registry
	 * 
	 * @param imageEnumType The enum to checks
	 */
	protected void checkEnumAvailability(final Class<? extends Enum<? extends IImageConstant>> imageEnumType) {
		Assert.paramNotNull(imageEnumType, "imageEnumType");
		final List<IImageConstant> unavailableKeys = getUnavailableKeys(imageEnumType);
		if (!unavailableKeys.isEmpty()) {
			throw new IllegalArgumentException("The following keys are unavailable in the image registry: "
				+ unavailableKeys
				+ "");
		}
	}

	/**
	 * Gets all unavailable keys of a given enum
	 * 
	 * @param imageEnumType The enum to get the unavailable keys for
	 * 
	 * @return The unavailable keys, never null but may be empty if all keys are available
	 */
	protected List<IImageConstant> getUnavailableKeys(final Class<? extends Enum<? extends IImageConstant>> imageEnumType) {
		final List<IImageConstant> result = new LinkedList<IImageConstant>();
		for (final Enum<? extends IImageConstant> key : imageEnumType.getEnumConstants()) {
			final IImageConstant imageConstant = (IImageConstant) key;
			if (!imageRegistry.isImageAvailable(imageConstant)) {
				result.add(imageConstant);
			}
		}
		return result;
	}

	/**
	 * Does the image registration
	 */
	public abstract void doRegistration();

}

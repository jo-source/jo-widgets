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
package org.jowidgets.common.image;

import java.net.URL;

public interface IImageRegistry {

    /**
     * Registers an image constant with an image descriptor value
     * 
     * @param key The key to use, must not be null
     * @param descriptor The descriptor to use, must not be null
     */
    void registerImageConstant(IImageConstant key, IImageDescriptor descriptor);

    /**
     * Registers an image constant with an image provider value
     * 
     * @param key The key to use, must not be null
     * @param descriptor The provider to use, must not be null
     */
    void registerImageConstant(IImageConstant key, IImageProvider provider);

    /**
     * Registers an image constant with an url value
     * 
     * @param key The key to use, must not be null
     * @param descriptor The url to use, must not be null
     */
    void registerImageConstant(IImageConstant key, URL url);

    /**
     * Registers an image constant with an substitude image constant
     * 
     * The substitude must be available {@link #isImageAvailable(IImageConstant)} and
     * the value of the substitude becomes the new image for the key.
     * 
     * @param key The key to use, must not be null
     * @param substitude The substitude to use, must not be null
     */
    void registerImageConstant(IImageConstant key, IImageConstant substitude);

    /**
     * Registers an image constant key with a image handle value
     * 
     * A image handle provides native (ui framework dependent) information, so
     * for writing spi independent code, this method normally will not be used.
     * 
     * @param key The key to use
     * @param imageHandle The image handle to use
     */
    void registerImageConstant(IImageConstant key, IImageHandle imageHandle);

    /**
     * Gets the image handle for a image constant.
     * 
     * A image handle provides native (ui framework dependent) information, so
     * for writing spi independent code, this method normally will not be used.
     * 
     * @param key The key to get the handle for
     * 
     * @return The image handle or null, if no handle was registered or can be created with the key.
     */
    IImageHandle getImageHandle(IImageConstant key);

    /**
     * Unregisters a image constant.
     * 
     * This also disposes the image handle (and native image) if initialized and
     * not already disposed.
     * 
     * Be sure that the image handle currently registered for the key is no longer be used.
     * 
     * If no image is registered for the key, the method returns without any effect
     * 
     * @param key The image constant to unregister
     */
    void unRegisterImageConstant(IImageConstant key);

    /**
     * Unregisters an image. This also disposes the image. After that the image can not be used anymore
     * 
     * @param image The image to unregister
     */
    void unRegisterImage(IImageCommon image);

    /**
     * Checks if a image is available for a given key
     * 
     * A image is available if the key is registered or if a image can be created from
     * the key (e.g. because the key is a image provider).
     * 
     * If an image is not available, the use of the key for anay widget may lead to an exception
     * 
     * @param key The key to check if image is available for
     * 
     * @return true if image is available, false if not
     */
    boolean isImageAvailable(IImageConstant key);

    /**
     * Checks if an image constant is already registered.
     * 
     * Remark: A key may be available {@link #isImageAvailable(IImageConstant)} even if it has not been registered yet
     * 
     * @param key The key to check
     * 
     * @return True if the image was already registered
     */
    boolean isImageRegistered(IImageConstant key);

    /**
     * Checks if an image is already initialized.
     * 
     * A image is initialized if it is registered and the native image has already been created
     * 
     * @param key The key to check
     * 
     * @return True if the image was already initialized
     */
    boolean isImageInitialized(IImageConstant key);

    /**
     * Registers all images of a image url provider enum
     * 
     * @param enumClass The enum to register
     * 
     * @deprecated The registration of an image enum is not necessary,
     *             because an image provider will be registered automatically
     *             at first use
     */
    @Deprecated
    <T extends Enum<?> & IImageUrlProvider> void registerImageEnum(final Class<T> enumClass);

    /**
     * Registers an IImageStreamProvider
     * 
     * @param provider The provider to register
     * 
     * @deprecated The registration of an image provider is not necessary,
     *             because an image provider will be registered automatically
     *             at first use
     */
    @Deprecated
    void registerImageProvider(IImageProvider provider);

    /**
     * Registers an IImageUrlProvider
     * 
     * @param provider The provider to register
     * 
     * @deprecated The registration of an image provider is not necessary,
     *             because an image provider will be registered automatically
     *             at first use
     */
    @Deprecated
    void registerImageUrl(IImageUrlProvider provider);

    /**
     * Registers an IImageStreamProvider
     * 
     * @param provider The provider to register
     * 
     * @deprecated The registration of an image provider is not necessary,
     *             because an image provider will be registered automatically
     *             at first use
     */
    @Deprecated
    void registerImageStream(IImageStreamProvider provider);

}

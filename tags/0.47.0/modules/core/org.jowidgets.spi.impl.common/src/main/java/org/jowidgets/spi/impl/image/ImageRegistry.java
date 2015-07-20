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
package org.jowidgets.spi.impl.image;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jowidgets.common.image.IImageCommon;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IImageDescriptor;
import org.jowidgets.common.image.IImageHandle;
import org.jowidgets.common.image.IImageHandleFactory;
import org.jowidgets.common.image.IImageProvider;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.image.IImageStreamProvider;
import org.jowidgets.common.image.IImageUrlProvider;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.cache.ICacheable;
import org.jowidgets.util.cache.ICacheableListener;

@SuppressWarnings("deprecation")
public class ImageRegistry implements IImageRegistry {

    private final Map<Object, IImageHandle> imageMap = new HashMap<Object, IImageHandle>();
    private final IImageHandleFactory imageHandleFactory;

    protected ImageRegistry(final IImageHandleFactory imageHandleFactory) {
        Assert.paramNotNull(imageHandleFactory, "imageHandleFactory");
        this.imageHandleFactory = imageHandleFactory;
    }

    @Override
    public synchronized void registerImageConstant(final IImageConstant key, final IImageHandle imageHandle) {
        Assert.paramNotNull(key, "key");
        Assert.paramNotNull(imageHandle, "imageHandle");

        final IImageHandle registeredHandle = imageMap.get(key);

        if (imageHandle == registeredHandle) {
            //already registered
            return;
        }

        if (isImageInitialized(key)) {//do not allow substitution for already used images
            throw new IllegalArgumentException(createInitializedExceptionMessage(key, imageHandle, registeredHandle));
        }

        if (key instanceof ICacheable) {
            ((ICacheable) key).addCacheableListener(new CacheableListener(key));
        }

        imageMap.put(key, imageHandle);
    }

    @Override
    public void registerImageConstant(final IImageConstant key, final IImageDescriptor descriptor) {
        Assert.paramNotNull(key, "key");
        Assert.paramNotNull(descriptor, "descriptor");
        registerImageConstant(key, imageHandleFactory.createImageHandle(descriptor));
    }

    @Override
    public void registerImageConstant(final IImageConstant key, final IImageProvider provider) {
        Assert.paramNotNull(key, "key");
        Assert.paramNotNull(provider, "provider");
        registerImageConstant(key, (IImageDescriptor) provider);
    }

    @Override
    public void registerImageConstant(final IImageConstant key, final URL url) {
        Assert.paramNotNull(key, "key");
        Assert.paramNotNull(url, "url (for key '" + key + "')");
        registerImageConstant(key, new UrlImageDescriptorImpl(url));
    }

    @Override
    public void registerImageConstant(final IImageConstant key, final IImageConstant substitude) {
        Assert.paramNotNull(key, "key");
        Assert.paramNotNull(substitude, "substitude");
        final IImageHandle substitudeHandle = getImageHandle(substitude);
        if (substitudeHandle == null) {
            throw new IllegalArgumentException("Substitude is not registered");
        }
        registerImageConstant(key, substitudeHandle);
    }

    @Override
    public synchronized IImageHandle getImageHandle(final IImageConstant key) {
        IImageHandle result = imageMap.get(key);
        if (result == null) {
            result = getOrCreateImageHandleForKeyIfPossible(key);
            if (result != null) {
                registerImageConstant(key, result);
            }
        }
        return result;
    }

    @Override
    public synchronized void unRegisterImageConstant(final IImageConstant key) {
        Assert.paramNotNull(key, "key");

        if (key instanceof ICacheable) {
            //this works because CacheableListener has a equals and hashCode implementation
            //that make it work:-)
            ((ICacheable) key).removeCacheableListener(new CacheableListener(key));
        }

        //ensure that images will be disposed completely
        if (key instanceof IImageCommon) {
            unRegisterImage((IImageCommon) key);
            return;
        }

        final IImageHandle registeredHandle = imageMap.remove(key);
        if (registeredHandle == null) {
            //not registered, do nothing
            return;
        }

        if (registeredHandle != null && registeredHandle instanceof ImageHandle<?>) {
            final ImageHandle<?> imageHandleImpl = (ImageHandle<?>) registeredHandle;
            if (!imageHandleImpl.isDisposed() && imageHandleImpl.isInitialized()) {
                imageHandleImpl.dispose();
            }
        }

    }

    @Override
    public synchronized void unRegisterImage(final IImageCommon image) {
        Assert.paramNotNull(image, "image");

        if (image instanceof ICacheable) {
            //this works because CacheableListener has a equals and hashCode implementation
            //that make it work:-)
            ((ICacheable) image).removeCacheableListener(new CacheableListener(image));
        }

        imageMap.remove(image);
        image.dispose();
    }

    @Override
    public boolean isImageAvailable(final IImageConstant key) {
        Assert.paramNotNull(key, "key");
        return (key instanceof IImageDescriptor) || isImageRegistered(key);
    }

    @Override
    public boolean isImageRegistered(final IImageConstant key) {
        Assert.paramNotNull(key, "key");
        return imageMap.containsKey(key);
    }

    @Override
    public boolean isImageInitialized(final IImageConstant key) {
        return isImageRegistered(key) && imageMap.get(key).isInitialized();
    }

    protected IImageHandleFactory getImageHandleFactory() {
        return imageHandleFactory;
    }

    private synchronized IImageHandle getOrCreateImageHandleForKeyIfPossible(final IImageConstant key) {
        IImageHandle result = imageMap.get(key);
        if (result == null && key instanceof IImageDescriptor) {
            result = imageHandleFactory.createImageHandle((IImageDescriptor) key);
        }
        return result;
    }

    private String createInitializedExceptionMessage(
        final IImageConstant key,
        final IImageHandle imageHandle,
        final IImageHandle registeredHandle) {
        return "Image subtitution failed: For the image constant: '"
            + key
            + "' the following ImageHandle was already created and initialized: "
            + registeredHandle
            + "'. This means that some widget may still use the native image of the previosly created image handle. So the new ImageHandle ("
            + imageHandle
            + ") can not be registered , until the the old handle was disposed. "
            + "To do so, use the method: 'unregisterImageConstant(key)' first, if you are shure, "
            + "that the native image of the old handle is no longer be used."
            + "To avoid this problem completely, "
            + "only substitude image constants inside a IToolkitInterceptor to ensure, "
            + "that image handles wasn't alerady used. "
            + "This exception was introduced with jowidgets version 0.43.0."
            + " If you haven't had this exception with older versions, "
            + "nevertheless your code may have a potential memory leak";
    }

    @Override
    @Deprecated
    public void registerImageProvider(final IImageProvider imageProvider) {
        Assert.paramNotNull(imageProvider, "imageProvider");
        registerImageConstant(imageProvider, (IImageDescriptor) imageProvider);
    }

    @Override
    @Deprecated
    public void registerImageUrl(final IImageUrlProvider imageUrlProvider) {
        Assert.paramNotNull(imageUrlProvider, "imageUrlProvider");
        registerImageProvider(imageUrlProvider);
    }

    @Override
    @Deprecated
    public void registerImageStream(final IImageStreamProvider imageStreamProvider) {
        Assert.paramNotNull(imageStreamProvider, "imageStreamProvider");
        registerImageProvider(imageStreamProvider);
    }

    @Override
    @Deprecated
    public <T extends Enum<?> & IImageUrlProvider> void registerImageEnum(final Class<T> enumClass) {
        for (final IImageUrlProvider imageUrlProvider : enumClass.getEnumConstants()) {
            registerImageUrl(imageUrlProvider);
        }
    }

    public void clear() {
        imageMap.clear();
    }

    private final class CacheableListener implements ICacheableListener {

        private final IImageConstant key;

        private CacheableListener(final IImageConstant key) {
            Assert.paramNotNull(key, "key");
            this.key = key;
        }

        @Override
        public void onRelease() {
            unRegisterImageConstant(key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof CacheableListener)) {
                return false;
            }
            final CacheableListener other = (CacheableListener) obj;
            return NullCompatibleEquivalence.equals(key, other.key);
        }

    }

}

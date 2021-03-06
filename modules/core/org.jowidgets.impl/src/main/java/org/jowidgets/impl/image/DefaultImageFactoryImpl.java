/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.impl.image;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.jowidgets.api.image.IBufferedImage;
import org.jowidgets.api.image.IImage;
import org.jowidgets.api.image.IImageFactory;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.spi.image.IImageFactorySpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;

public final class DefaultImageFactoryImpl implements IImageFactory {

    private final IImageFactorySpi factorySpi;
    private final IImageRegistry imageRegistry;

    public DefaultImageFactoryImpl(final IImageFactorySpi factorySpi, final IImageRegistry imageRegistry) {
        Assert.paramNotNull(factorySpi, "factorySpi");
        Assert.paramNotNull(imageRegistry, "imageRegistry");
        this.imageRegistry = imageRegistry;
        this.factorySpi = factorySpi;
    }

    @Override
    public IImage createImage(final File file) {
        Assert.paramNotNull(file, "file");
        try {
            return createImage(file.toURI().toURL());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IImage createImage(final URL url) {
        return new ImageImpl(factorySpi.createImage(url), imageRegistry);
    }

    @Override
    public IImage createImage(final IFactory<InputStream> inputStream) {
        return new ImageImpl(factorySpi.createImage(inputStream), imageRegistry);
    }

    @Override
    public IBufferedImage createBufferedImage(final int width, final int height) {
        return new BufferedImageImpl(factorySpi.createBufferedImage(width, height), imageRegistry);
    }

}

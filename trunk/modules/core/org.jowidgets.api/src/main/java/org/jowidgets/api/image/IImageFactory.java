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

package org.jowidgets.api.image;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.jowidgets.util.IFactory;

public interface IImageFactory {

    /**
     * Creates an image from an file.
     * 
     * The resulting image is also an image constant that will be registered at the image registry,
     * so it can be used as image constant, until the image will be disposed.
     * 
     * Remark: If the resulting image will be disposed, it will be unregistered from
     * the image registry
     * 
     * @param file The file that holds the image
     * 
     * @return The created image
     */
    IImage createImage(File file);

    /**
     * Creates an image from an url.
     * 
     * The resulting image is also an image constant that will be registered at the image registry,
     * so it can be used as image constant, until the image will be disposed.
     * 
     * Remark: If the resulting image will be disposed, it will be unregistered from
     * the image registry
     * 
     * @param url The url to create the image from
     * 
     * @return The created image
     */
    IImage createImage(URL url);

    /**
     * Creates an image from an input stream factory.
     * 
     * The resulting image is also an image constant that will be registered at the image registry,
     * so it can be used as image constant, until the image will be disposed.
     * 
     * Remark: If the resulting image will be disposed, it will be unregistered from
     * the image registry
     * 
     * @param inputStream A factory for an input stream
     * 
     * @return The created image
     */
    IImage createImage(IFactory<InputStream> inputStream);

    /**
     * Creates an buffered image with defined with and height.
     * 
     * The resulting image is also an image constant that will be registered at the image registry,
     * so it can be used as image constant, until the image will be disposed.
     * 
     * Remark: If the resulting image will be disposed, it will be unregistered from
     * the image registry
     * 
     * @param width The width of the image
     * @param height The height of the image
     * 
     * @return The created image
     */
    IBufferedImage createBufferedImage(int width, int height);

}

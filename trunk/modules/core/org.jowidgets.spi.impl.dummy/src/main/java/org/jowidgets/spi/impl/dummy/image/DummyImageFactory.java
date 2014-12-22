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

package org.jowidgets.spi.impl.dummy.image;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import org.jowidgets.common.image.IImageDescriptor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.spi.graphics.IGraphicContextSpi;
import org.jowidgets.spi.image.IBufferedImageSpi;
import org.jowidgets.spi.image.IImageFactorySpi;
import org.jowidgets.spi.image.IImageSpi;
import org.jowidgets.spi.impl.dummy.dummyui.UIDImage;
import org.jowidgets.spi.impl.image.AbstractImageSpiImpl;
import org.jowidgets.spi.impl.image.ImageHandle;
import org.jowidgets.spi.impl.image.StreamImageDecriptorImpl;
import org.jowidgets.spi.impl.image.UrlImageDescriptorImpl;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;

public final class DummyImageFactory implements IImageFactorySpi {

	private final DummyImageHandleFactory imageHandleFactory;

	public DummyImageFactory(final DummyImageHandleFactory imageHandleFactory) {
		Assert.paramNotNull(imageHandleFactory, "imageHandleFactory");
		this.imageHandleFactory = imageHandleFactory;
	}

	@Override
	public IBufferedImageSpi createBufferedImage(final int width, final int height) {
		return new BufferedImageSpi(new UIDImage("BuffredImageDummy - " + UUID.randomUUID().toString()));
	}

	@Override
	public IImageSpi createImage(final IFactory<InputStream> inputStream) {
		Assert.paramNotNull(inputStream, "inputStream");
		final IImageDescriptor descriptor = new StreamImageDecriptorImpl(inputStream);
		return new ImageSpiImpl<UIDImage>(imageHandleFactory.createImageHandle(descriptor));
	}

	@Override
	public IImageSpi createImage(final URL url) {
		Assert.paramNotNull(url, "url");
		final IImageDescriptor descriptor = new UrlImageDescriptorImpl(url);
		return new ImageSpiImpl<UIDImage>(imageHandleFactory.createImageHandle(descriptor));
	}

	private class ImageSpiImpl<IMAGE_TYPE extends UIDImage> extends AbstractImageSpiImpl<IMAGE_TYPE> {

		ImageSpiImpl(final ImageHandle<IMAGE_TYPE> imageHandle) {
			super(imageHandle);
		}

		@Override
		public Dimension getSize() {
			checkDisposed();
			return new Dimension(-1, -1);
		}

	}

	private final class BufferedImageSpi extends ImageSpiImpl<UIDImage> implements IBufferedImageSpi {

		public BufferedImageSpi(final UIDImage image) {
			super(new ImageHandle<UIDImage>(image));
		}

		@Override
		public synchronized IGraphicContextSpi getGraphicContext() {
			//TODO graphics context dummy must be implemented
			return null;
		}

	}

}

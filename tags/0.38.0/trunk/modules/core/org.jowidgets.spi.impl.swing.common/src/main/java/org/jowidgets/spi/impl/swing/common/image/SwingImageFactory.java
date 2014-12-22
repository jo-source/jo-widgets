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

package org.jowidgets.spi.impl.swing.common.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import org.jowidgets.common.image.IImageDescriptor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.spi.graphics.IGraphicContextSpi;
import org.jowidgets.spi.image.IBufferedImageSpi;
import org.jowidgets.spi.image.IImageFactorySpi;
import org.jowidgets.spi.image.IImageSpi;
import org.jowidgets.spi.impl.image.AbstractImageSpiImpl;
import org.jowidgets.spi.impl.image.ImageHandle;
import org.jowidgets.spi.impl.image.StreamImageDecriptorImpl;
import org.jowidgets.spi.impl.image.UrlImageDescriptorImpl;
import org.jowidgets.spi.impl.swing.common.graphics.GraphicContextSpiImpl;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;

public final class SwingImageFactory implements IImageFactorySpi {

	private final SwingImageHandleFactory imageHandleFactory;

	public SwingImageFactory(final SwingImageHandleFactory imageHandleFactory) {
		Assert.paramNotNull(imageHandleFactory, "imageHandleFactory");
		this.imageHandleFactory = imageHandleFactory;
	}

	@Override
	public IBufferedImageSpi createBufferedImage(final int width, final int height) {
		return new BufferedImageSpi(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
	}

	@Override
	public IImageSpi createImage(final IFactory<InputStream> inputStream) {
		Assert.paramNotNull(inputStream, "inputStream");
		final IImageDescriptor descriptor = new StreamImageDecriptorImpl(inputStream);
		return new ImageSpiImpl<Image>(imageHandleFactory.createImageHandle(descriptor));
	}

	@Override
	public IImageSpi createImage(final URL url) {
		Assert.paramNotNull(url, "url");
		final IImageDescriptor descriptor = new UrlImageDescriptorImpl(url);
		return new ImageSpiImpl<Image>(imageHandleFactory.createImageHandle(descriptor));
	}

	private class ImageSpiImpl<IMAGE_TYPE extends Image> extends AbstractImageSpiImpl<IMAGE_TYPE> {

		ImageSpiImpl(final ImageHandle<IMAGE_TYPE> imageHandle) {
			super(imageHandle);
		}

		@Override
		public Dimension getSize() {
			checkDisposed();
			final Image image = getImageHandle().getImage();
			return new Dimension(image.getWidth(null), image.getHeight(null));
		}

	}

	private final class BufferedImageSpi extends ImageSpiImpl<BufferedImage> implements IBufferedImageSpi {

		private BufferedImage image;
		private GraphicContextSpiImpl graphicContext;

		public BufferedImageSpi(final BufferedImage image) {
			super(new ImageHandle<BufferedImage>(image));
			this.image = image;
		}

		@Override
		public synchronized IGraphicContextSpi getGraphicContext() {
			if (graphicContext == null) {
				graphicContext = createGraphicContext();
			}
			return graphicContext;
		}

		private GraphicContextSpiImpl createGraphicContext() {
			return new GraphicContextSpiImpl(image.createGraphics(), new Rectangle(new Position(0, 0), getSize()));
		}

		@Override
		public void dispose() {
			super.dispose();
			image = null;
		}

	}

}

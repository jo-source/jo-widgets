/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.image;

import javafx.scene.image.ImageView;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IImageHandle;
import org.jowidgets.spi.image.IImageHandleFactorySpi;
import org.jowidgets.spi.impl.image.IImageFactory;
import org.jowidgets.spi.impl.image.ImageHandle;

public class JavafxImageHandleFactorySpi extends JavafxImageHandleFactory implements IImageHandleFactorySpi {

	private final JavafxImageRegistry javafxImageRegistry;

	public JavafxImageHandleFactorySpi(final JavafxImageRegistry javafxImageRegistry) {
		super();
		this.javafxImageRegistry = javafxImageRegistry;
	}

	@Override
	public IImageHandle infoIcon() {
		return new ImageHandle<ImageView>(new IImageFactory<ImageView>() {
			@Override
			public ImageView createImage() {
				return null;
			}
		});
	}

	@Override
	public IImageHandle questionIcon() {
		return new ImageHandle<ImageView>(new IImageFactory<ImageView>() {
			@Override
			public ImageView createImage() {
				return null;
			}
		});
	}

	@Override
	public IImageHandle warningIcon() {
		return new ImageHandle<ImageView>(new IImageFactory<ImageView>() {
			@Override
			public ImageView createImage() {
				return null;
			}
		});
	}

	@Override
	public IImageHandle errorIcon() {
		return new ImageHandle<ImageView>(new IImageFactory<ImageView>() {
			@Override
			public ImageView createImage() {
				return null;
			}
		});
	}

	@Override
	public IImageHandle createImageHandle(final IImageConstant imageConstant, final int width, final int height) {
		return new ImageHandle<ImageView>(new IImageFactory<ImageView>() {
			@Override
			public ImageView createImage() {
				final ImageView templateImage = javafxImageRegistry.getImage(imageConstant);
				templateImage.setScaleX(width);
				templateImage.setScaleY(height);
				templateImage.setSmooth(true);
				return templateImage;
			}
		});
	}

}

/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.spi.impl.swt.common.image;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IImageHandle;
import org.jowidgets.spi.image.IImageHandleFactorySpi;
import org.jowidgets.spi.impl.image.IImageFactory;
import org.jowidgets.spi.impl.image.ImageHandle;
import org.jowidgets.spi.impl.swt.common.image.util.AntiAliasingFilter;

public class SwtImageHandleFactorySpi extends SwtImageHandleFactory implements IImageHandleFactorySpi {

	private final SwtImageRegistry swtImageRegistry;

	public SwtImageHandleFactorySpi(final SwtImageRegistry swtImageRegistry) {
		super();
		this.swtImageRegistry = swtImageRegistry;
	}

	@Override
	public IImageHandle infoIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
			}
		});
	}

	@Override
	public IImageHandle questionIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return Display.getDefault().getSystemImage(SWT.ICON_QUESTION);
			}
		});
	}

	@Override
	public IImageHandle warningIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return Display.getDefault().getSystemImage(SWT.ICON_WARNING);
			}
		});
	}

	@Override
	public IImageHandle errorIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return Display.getDefault().getSystemImage(SWT.ICON_ERROR);
			}
		});
	}

	@Override
	public IImageHandle createImageHandle(final IImageConstant imageConstant, final int width, final int height) {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				final Image originalImage = swtImageRegistry.getImage(imageConstant);
				if (originalImage != null) {

					//RWT does not support the smooth scale	
					try {
						return smoothScale(originalImage, width, height);
					}
					catch (final LinkageError e) {
						//low pass filter works only with direct palette
						if (originalImage.getImageData().palette.isDirect) {
							final ImageData filteredImageData = AntiAliasingFilter.filter(
									originalImage.getImageData(),
									width,
									height);
							return new Image(Display.getDefault(), filteredImageData.scaledTo(width, height));
						}
						//else scale without anti aliasing
						else {
							return new Image(Display.getDefault(), originalImage.getImageData().scaledTo(width, height));
						}
					}

				}
				return null;
			}
		});

	}

	private Image smoothScale(final Image originalImage, final int width, final int height) {
		final ImageData imageData = originalImage.getImageData();

		//create a temporary image to scale with anti aliasing
		final Image scaledSmoothImageTmp = new Image(Display.getDefault(), width, height);
		final GC gc = new GC(scaledSmoothImageTmp);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(originalImage, 0, 0, imageData.width, imageData.height, 0, 0, width, height);

		//clone the image data of the scaled smooth image(
		//getImageData() is not enough because the fields can not be overridden)
		final ImageData scaledSmoothImageData = (ImageData) scaledSmoothImageTmp.getImageData().clone();

		//workaround, because drawImage does lost the mask data (tested with WinXP)
		//scale the image with the 'grainy' method and use only its alpha masks
		final ImageData scaledGrainyImageData = originalImage.getImageData().scaledTo(width, height);
		scaledSmoothImageData.maskPad = scaledGrainyImageData.maskPad;
		scaledSmoothImageData.maskData = scaledGrainyImageData.maskData;
		scaledSmoothImageData.alpha = scaledGrainyImageData.alpha;
		scaledSmoothImageData.alphaData = scaledGrainyImageData.alphaData;
		scaledSmoothImageData.transparentPixel = scaledGrainyImageData.transparentPixel;
		scaledSmoothImageData.type = scaledGrainyImageData.type;

		//dispose the temps
		gc.dispose();
		scaledSmoothImageTmp.dispose();

		//return the result
		return new Image(Display.getDefault(), scaledSmoothImageData);
	}

}

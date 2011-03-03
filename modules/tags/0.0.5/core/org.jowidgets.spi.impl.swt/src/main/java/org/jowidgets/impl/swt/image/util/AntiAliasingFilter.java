/*
 * Copyright (c) 2010, grossmann
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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.impl.swt.image.util;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public final class AntiAliasingFilter {

	private AntiAliasingFilter() {};

	/**
	 * Filters an image as preliminary for an scale function. If an image must be scaled to an lower resolution,
	 * it must be low pass filtered to respect the nyquist theorem. After it was scaled, it could be
	 * scaled (resampled) with an lower resolution (frequency).
	 * 
	 * Remark: This algorithm is only used in case of RWT. To simplify matters this implementation filters
	 * in the 'position space' an not in the 'frequency space'.
	 * Feel free to implement this method with help of convolution and FFT and send me the patch.
	 * With respect to the performance, the maximal possible gauss matrix is 25x25. For scaled factors bigger
	 * than 1/25 aliasing could still occur.
	 * 
	 * @param originalData The image data than should be resized.
	 * @param resultWidth the width of the image after scaling
	 * @param resultHeight the height of the image after scaling
	 * @return The image data of the low pass filtered image, if the new image is smaller than the original, otherwise
	 *         the original
	 */
	public static ImageData filter(final ImageData originalData, final int resultWidth, final int resultHeight) {

		final Double[][] filter = getFilter(originalData.width, originalData.height, resultWidth, resultHeight);

		if (filter != null) {

			final ImageData result = (ImageData) originalData.clone();
			final HighResolutionPixel[][] highResDataSource = createHighResolutionData(result);
			final HighResolutionPixel[][] highResDataDest = new HighResolutionPixel[originalData.width][originalData.height];

			final int filterLength = filter.length;

			final int offset = (filter.length - 1) / 2;

			for (int x = 0; x < result.width; x++) {
				for (int y = 0; y < result.height; y++) {

					HighResolutionPixel pixel = new HighResolutionPixel();

					for (int filterX = 0; filterX < filterLength; filterX++) {
						for (int filterY = 0; filterY < filterLength; filterY++) {
							int xPos = x + filterX - offset;
							int yPos = y + filterY - offset;

							//mirror the picture if filter mask is beyond image
							if (xPos < 0) {
								xPos = x + (filterLength - filterX) - offset;
							}
							if (xPos >= result.width) {
								xPos = x + (filterX - filterLength) - offset;
							}

							if (yPos < 0) {
								yPos = y + (filterLength - filterY) - offset;
							}
							if (yPos >= result.height) {
								yPos = y + (filterY - filterLength) - offset;
							}

							pixel = pixel.add(highResDataSource[xPos][yPos].multiply(filter[filterX][filterY]));

						}
					}

					highResDataDest[x][y] = pixel;
				}
			}

			setLowResolutionData(result, highResDataDest);

			return result;
		}

		return originalData;
	}

	private static Double[][] getFilter(
		final int originalWidth,
		final int originalHeight,
		final int resultWidth,
		final int resultHeight) {

		final double scaleFacX = ((double) originalWidth) / resultWidth;
		final double scaleFacY = ((double) originalHeight) / resultHeight;

		final double scaleFac = Math.max(scaleFacX, scaleFacY);
		if (scaleFac > 1) {
			return GaussMatrix.getGaussMatrix((int) Math.min(Math.round(scaleFac) + 2, GaussMatrix.MAX_DEPTH));
		}

		return null;
	}

	private static HighResolutionPixel[][] createHighResolutionData(final ImageData imageData) {
		final HighResolutionPixel[][] result = new HighResolutionPixel[imageData.width][imageData.height];
		for (int x = 0; x < imageData.width; x++) {
			for (int y = 0; y < imageData.height; y++) {
				result[x][y] = new HighResolutionPixel(imageData.getPixel(x, y), imageData.palette);
			}
		}
		return result;
	}

	private static void setLowResolutionData(final ImageData imageData, final HighResolutionPixel[][] highResolutionData) {
		for (int x = 0; x < imageData.height; x++) {
			for (int y = 0; y < imageData.width; y++) {
				imageData.setPixel(
						x,
						y,
						highResolutionData[x][y].getLowResolutionPixel(imageData.getPixel(x, y), imageData.palette));
			}
		}

	}

}

class HighResolutionPixel {

	private final double red;
	private final double green;
	private final double blue;

	public HighResolutionPixel(final int originalPixel, final PaletteData paletteData) {
		this.red = paletteData.getRGB(originalPixel).red;
		this.green = paletteData.getRGB(originalPixel).green;
		this.blue = paletteData.getRGB(originalPixel).blue;
	}

	public HighResolutionPixel() {
		this(0.0, 0.0, 0.0);
	}

	public HighResolutionPixel(final double red, final double green, final double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public HighResolutionPixel add(final HighResolutionPixel highResolutionPixel) {

		final double newRed = red + highResolutionPixel.red;
		final double newGreen = green + highResolutionPixel.green;
		final double newBlue = blue + highResolutionPixel.blue;

		return new HighResolutionPixel(newRed, newGreen, newBlue);
	}

	public HighResolutionPixel multiply(final Double factor) {
		return new HighResolutionPixel(red * factor, green * factor, blue * factor);
	}

	public HighResolutionPixel divide(final int divisor) {
		return new HighResolutionPixel(red / divisor, green / divisor, blue / divisor);
	}

	public int getLowResolutionPixel(final int originalPixel, final PaletteData paletteData) {

		final int result = paletteData.getPixel(new RGB((int) red, (int) green, (int) blue));

		final int mask = 0xFFFFFFFF & ~paletteData.redMask & ~paletteData.greenMask & ~paletteData.blueMask;

		final int clearedOriginal = originalPixel & mask;
		final int modifiedOriginal = clearedOriginal | result;

		return modifiedOriginal;
	}
}

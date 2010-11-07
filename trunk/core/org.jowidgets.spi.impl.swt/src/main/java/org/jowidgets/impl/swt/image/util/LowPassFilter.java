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

public final class LowPassFilter {

	private LowPassFilter() {};

	public static ImageData filter(final ImageData originalData, final int resultWidth, final int resultHeight) {
		final ImageData result = (ImageData) originalData.clone();
		final HighResolutionPixel[][] highResDataSource = createHighResolutionData(result);
		final HighResolutionPixel[][] highResDataDest = new HighResolutionPixel[originalData.width][originalData.height];

		final Double[][] filter = getFilter(result.width, result.height, resultWidth, resultHeight);
		final int filterLengthX = filter.length;
		final int filterLengthY = filter[0].length;
		final int offsetX = (filterLengthX - 1) / 2;
		final int offsetY = (filterLengthY - 1) / 2;

		for (int x = 0; x < result.width; x++) {
			for (int y = 0; y < result.height; y++) {

				HighResolutionPixel pixel = new HighResolutionPixel();

				for (int filterX = 0; filterX < filterLengthX; filterX++) {
					for (int filterY = 0; filterY < filterLengthY; filterY++) {
						int xPos = x + filterX - offsetX;
						int yPos = y + filterY - offsetY;

						//mirror the picture if filter mask is beyond image
						if (xPos < 0) {
							xPos = x + (filterLengthX - filterX) - offsetX;
						}
						if (xPos >= result.width) {
							xPos = x + (filterX - filterLengthX) - offsetX;
						}

						if (yPos < 0) {
							yPos = y + (filterLengthY - filterY) - offsetY;
						}
						if (yPos >= result.height) {
							yPos = y + (filterY - filterLengthY) - offsetY;
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

	private static Double[][] getFilter(
		final int originalWidth,
		final int originalHeight,
		final int resultWidth,
		final int resultHeight) {
		//Simple 4x4 GaussFilter for testing
		final int size = 4;
		final Double[][] result = new Double[][] {
				{1.0, 3.0, 3.0, 1.0}, {3.0, 9.0, 9.0, 3.0}, {3.0, 9.0, 9.0, 3.0}, {1.0, 3.0, 3.0, 1.0}};

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				result[x][y] = result[x][y] / (64);
			}
		}
		return result;
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

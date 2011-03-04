/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.impl.swing.image;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.UIManager;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IImageHandle;
import org.jowidgets.common.image.impl.IImageFactory;
import org.jowidgets.common.image.impl.ImageHandle;
import org.jowidgets.spi.image.IImageHandleFactorySpi;

public class SwingImageHandleFactorySpi extends SwingImageHandleFactory implements IImageHandleFactorySpi {

	private final SwingImageRegistry swingImageRegistry;

	public SwingImageHandleFactorySpi(final SwingImageRegistry swingImageRegistry) {
		super();
		this.swingImageRegistry = swingImageRegistry;
	}

	@Override
	public IImageHandle infoIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return icon2Image(UIManager.getIcon("OptionPane.informationIcon"));
			}
		});
	}

	@Override
	public IImageHandle questionIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return icon2Image(UIManager.getIcon("OptionPane.questionIcon"));
			}
		});
	}

	@Override
	public IImageHandle warningIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return icon2Image(UIManager.getIcon("OptionPane.warningIcon"));
			}
		});
	}

	@Override
	public IImageHandle errorIcon() {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				return icon2Image(UIManager.getIcon("OptionPane.errorIcon"));
			}
		});
	}

	private Image icon2Image(final Icon icon) {
		final BufferedImage bufferedImage = new BufferedImage(
			icon.getIconWidth(),
			icon.getIconHeight(),
			BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
		return bufferedImage;
	}

	@Override
	public IImageHandle createImageHandle(final IImageConstant imageConstant, final int width, final int height) {
		return new ImageHandle<Image>(new IImageFactory<Image>() {
			@Override
			public Image createImage() {
				final Image templateImage = swingImageRegistry.getImage(imageConstant);
				return templateImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			}
		});
	}

}

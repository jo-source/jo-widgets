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
package org.jowidgets.spi.impl.swing.common.image;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IImageHandleFactory;
import org.jowidgets.spi.impl.image.ImageHandle;
import org.jowidgets.spi.impl.image.ImageRegistry;

public final class SwingImageRegistry extends ImageRegistry {

    private static final SwingImageRegistry INSTANCE = new SwingImageRegistry(new SwingImageHandleFactory());

    private SwingImageRegistry(final IImageHandleFactory imageHandleFactory) {
        super(imageHandleFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized ImageHandle<Image> getImageHandle(final IImageConstant key) {
        return (ImageHandle<Image>) super.getImageHandle(key);
    }

    public synchronized Image getImage(final IImageConstant key) {
        if (key == null) {
            return null;
        }
        final ImageHandle<Image> imageHandle = getImageHandle(key);
        if (imageHandle != null) {
            return imageHandle.getImage();
        }
        else {
            throw new IllegalArgumentException("No image found for the image constant '" + key + "'");
        }
    }

    public synchronized ImageIcon getImageIcon(final IImageConstant key) {
        if (key == null) {
            return null;
        }
        else {
            return new ImageIcon(getImage(key));
        }
    }

    public static SwingImageRegistry getInstance() {
        return INSTANCE;
    }

}

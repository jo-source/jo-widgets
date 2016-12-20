/*
 * Copyright (c) 2016, MGrossmann
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

package org.jowidgets.nattable.impl.plugin.movetojo;

import org.eclipse.swt.graphics.Color;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.color.IColorCache;
import org.jowidgets.util.Assert;

/**
 * This class provides a color from a color constant.
 * 
 * The color will be accessed from color cache only on first access
 * and after that the previously accessed value will be returned.
 */
public final class ColorProvider {

    private final IColorCache colorCache;
    private final IColorConstant colorConstant;

    private Color color;

    /**
     * Creates a new color provider defined by rgb values
     * 
     * @param red The red component
     * @param green The green component
     * @param blue The blue component
     */
    public ColorProvider(final int red, final int green, final int blue) {
        this(new ColorValue(red, green, blue));
    }

    /**
     * Creates a new color provider
     * 
     * @param colorConstant The color contant to use, may be null
     */
    public ColorProvider(final IColorConstant colorConstant) {
        this(ColorCache.getInstance(), colorConstant);
    }

    /**
     * Creates a new color provider
     * 
     * @param colorCache The color cache to use, must not be null
     * @param colorConstant The color contant to use, may be null
     */
    public ColorProvider(final IColorCache colorCache, final IColorConstant colorConstant) {
        Assert.paramNotNull(colorCache, "colorCache");
        this.colorCache = colorCache;
        this.colorConstant = colorConstant;
    }

    /**
     * Gets the corresponding color or null, if the constant is null
     * 
     * @return the corresponding color or null, if the constant is null
     */
    public Color get() {
        if (colorConstant == null) {
            return null;
        }

        if (color == null) {
            color = colorCache.getColor(colorConstant);
        }
        return color;
    }

}

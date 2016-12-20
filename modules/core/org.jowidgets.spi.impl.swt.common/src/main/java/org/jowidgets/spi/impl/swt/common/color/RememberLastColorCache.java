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

package org.jowidgets.spi.impl.swt.common.color;

import org.eclipse.swt.graphics.Color;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.color.IColorCache;
import org.jowidgets.util.Assert;

public final class RememberLastColorCache implements IColorCache {

    private final IColorCache original;

    private IColorConstant lastColorConstant;
    private Color lastColor;

    /**
     * Creates a new instance with the default color cache as original
     */
    public RememberLastColorCache() {
        this(ColorCache.getInstance());
    }

    /**
     * Creates a new instance with a given color cache
     * 
     * @param original The original color cache, must not be null
     */
    public RememberLastColorCache(final IColorCache original) {
        Assert.paramNotNull(original, "original");
        this.original = original;
    }

    @Override
    public Color getColor(final IColorConstant colorConstant) {
        if (colorConstant == null) {
            return null;
        }
        else if (!colorConstant.equals(lastColorConstant)) {
            lastColorConstant = colorConstant;
            lastColor = original.getColor(colorConstant);
        }
        return lastColor;
    }

}

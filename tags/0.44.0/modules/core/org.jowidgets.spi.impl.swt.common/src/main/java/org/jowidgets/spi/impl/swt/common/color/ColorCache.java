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
package org.jowidgets.spi.impl.swt.common.color;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.util.cache.ICacheableListener;

public final class ColorCache implements IColorCache {

    private static final IColorCache INSTANCE = new ColorCache();

    private final Map<IColorConstant, Color> colorMap = new HashMap<IColorConstant, Color>();

    private ColorCache() {}

    @Override
    public synchronized Color getColor(final IColorConstant colorConstant) {
        if (colorConstant != null) {
            final ColorValue colorValue = colorConstant.getDefaultValue();
            Color color = colorMap.get(colorValue);
            if (color == null) {
                color = new Color(Display.getDefault(), colorValue.getRed(), colorValue.getGreen(), colorValue.getBlue());
                colorMap.put(colorValue, color);
                colorValue.addCacheableListener(new CachaeableListener(colorValue));
            }
            return color;
        }
        else {
            return null;
        }
    }

    public static IColorCache getInstance() {
        return INSTANCE;
    }

    private final class CachaeableListener implements ICacheableListener {

        private final ColorValue colorValue;

        private CachaeableListener(final ColorValue colorValue) {
            this.colorValue = colorValue;
        }

        @Override
        public void onRelease() {
            final Color removedColor = colorMap.remove(colorValue);
            if (removedColor != null && !removedColor.isDisposed()) {
                removedColor.dispose();
            }
            colorValue.removeCacheableListener(this);
        }

    }

}

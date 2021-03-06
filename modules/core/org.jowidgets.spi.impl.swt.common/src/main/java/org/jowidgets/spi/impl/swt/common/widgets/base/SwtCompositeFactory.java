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

package org.jowidgets.spi.impl.swt.common.widgets.base;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.logging.api.ILogger;
import org.jowidgets.logging.api.LoggerProvider;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;

public final class SwtCompositeFactory {

    private static final ILogger LOGGER = LoggerProvider.get(SwtCompositeFactory.class);

    private static PatchedCompositeReflectionAccessor accessor;
    private static boolean patchFailed = false;

    private SwtCompositeFactory() {}

    public static Composite create(final Composite parent, final int style) {
        if (!patchFailed && SwtOptions.hasCompositeMinSizeWorkaround()) {
            final PatchedCompositeReflectionAccessor currentAccssor = getPatchedCompositeReflectionAccessor();
            if (currentAccssor != null) {
                return new PatchedComposite(parent, style, currentAccssor);
            }
            else {
                return new Composite(parent, style);
            }
        }
        else {
            return new Composite(parent, style);
        }
    }

    private static PatchedCompositeReflectionAccessor getPatchedCompositeReflectionAccessor() {
        if (accessor == null) {
            try {
                accessor = createPatchedCompositeReflectionAccessor();
            }
            catch (final Exception e) {
                patchFailed = true;
                LOGGER.error("Composite min size patch can not be applied", e);
                return null;
            }
        }
        return accessor;
    }

    private static synchronized PatchedCompositeReflectionAccessor createPatchedCompositeReflectionAccessor() throws NoSuchMethodException,
            NoSuchFieldException {
        return new PatchedCompositeReflectionAccessor();
    }
}

/*
 * Copyright (c) 2016, grossmann
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

final class PatchedCompositeReflectionAccessor {

    private final Method runSkinMethod;
    private final Method computeSizeMethod;
    private final Method minimumSizeMethod;
    private final Field stateField;

    PatchedCompositeReflectionAccessor() throws NoSuchMethodException, NoSuchFieldException {
        this.runSkinMethod = Display.class.getDeclaredMethod("runSkin");
        runSkinMethod.setAccessible(true);

        this.computeSizeMethod = Layout.class.getDeclaredMethod(
                "computeSize",
                Composite.class,
                int.class,
                int.class,
                boolean.class);
        computeSizeMethod.setAccessible(true);

        this.minimumSizeMethod = Composite.class.getDeclaredMethod("minimumSize", int.class, int.class, boolean.class);
        minimumSizeMethod.setAccessible(true);

        this.stateField = Widget.class.getDeclaredField("state");
        stateField.setAccessible(true);
    }

    Method getRunSkinMethod() {
        return runSkinMethod;
    }

    Method getComputeSizeMethod() {
        return computeSizeMethod;
    }

    Method getMinimumSizeMethod() {
        return minimumSizeMethod;
    }

    Field getStateField() {
        return stateField;
    }

}

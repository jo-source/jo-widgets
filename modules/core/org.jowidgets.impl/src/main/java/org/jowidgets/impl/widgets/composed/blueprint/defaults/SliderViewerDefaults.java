/*
 * Copyright (c) 2014, grossmann
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
package org.jowidgets.impl.widgets.composed.blueprint.defaults;

import org.jowidgets.api.controller.IMouseButtonEventMatcher;
import org.jowidgets.api.widgets.blueprint.builder.ISliderViewerSetupBuilder;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.util.ObservableValue;

public class SliderViewerDefaults implements IDefaultInitializer<ISliderViewerSetupBuilder<?, ?>> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void initialize(final ISliderViewerSetupBuilder<?, ?> builder) {
        builder.setMinimum(0);
        builder.setMaximum(1000);
        builder.setTickSpacing(100);
        builder.setObservableValue(new ObservableValue());
        builder.setDefaultValueMatcher(new IMouseButtonEventMatcher() {
            @Override
            public boolean matches(final IMouseButtonEvent event, final boolean doubleClick) {
                if (!doubleClick
                    && event.getMouseButton() == MouseButton.LEFT
                    && event.getModifiers().contains(Modifier.CTRL)
                    && event.getModifiers().size() == 1) {
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }
}

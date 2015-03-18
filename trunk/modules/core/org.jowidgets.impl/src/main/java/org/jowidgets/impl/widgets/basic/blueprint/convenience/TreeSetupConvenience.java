/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.impl.widgets.basic.blueprint.convenience;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.types.TreeAutoCheckPolicy;
import org.jowidgets.api.widgets.blueprint.builder.ITreeSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.convenience.ITreeSetupConvenience;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.tools.widgets.blueprint.convenience.AbstractSetupBuilderConvenience;

public class TreeSetupConvenience extends AbstractSetupBuilderConvenience<ITreeSetupBuilder<?>> implements
        ITreeSetupConvenience<ITreeSetupBuilder<?>> {

    @Override
    public ITreeSetupBuilder<?> multiSelection() {
        return getBuilder().setSelectionPolicy(SelectionPolicy.MULTI_SELECTION);
    }

    @Override
    public ITreeSetupBuilder<?> singleSelection() {
        return getBuilder().setSelectionPolicy(SelectionPolicy.SINGLE_SELECTION);
    }

    @Override
    public ITreeSetupBuilder<?> setAutoCheckMode(final boolean autoCheckMode) {
        if (autoCheckMode) {
            getBuilder().setAutoCheckPolicy(TreeAutoCheckPolicy.MULTI_PATH);
        }
        else {
            getBuilder().setAutoCheckPolicy(TreeAutoCheckPolicy.OFF);
        }
        return getBuilder();
    }

    @Override
    public ITreeSetupBuilder<?> setDefaultIcons() {
        final ITreeSetupBuilder<?> builder = getBuilder();
        builder.setDefaultInnerIcon(IconsSmall.FOLDER);
        builder.setDefaultLeafIcon(IconsSmall.PAGE_WHITE);
        return null;
    }

    @Override
    public ITreeSetupBuilder<?> setClassicSelectionColors() {
        getBuilder().setSelectedForegroundColor(new ColorValue(255, 255, 255));
        getBuilder().setSelectedBackgroundColor(new ColorValue(51, 153, 255));
        getBuilder().setSelectedBorderColor(new ColorValue(60, 60, 60));

        getBuilder().setDisabledSelectedForegroundColor(new ColorValue(130, 130, 130));
        getBuilder().setDisabledSelectedBackgroundColor(new ColorValue(255, 255, 255));
        getBuilder().setDisabledSelectedBorderColor(new ColorValue(130, 130, 130));
        return getBuilder();
    }

    @Override
    public ITreeSetupBuilder<?> setWinSelectionColors() {
        getBuilder().setSelectedForegroundColor(new ColorValue(0, 0, 0));
        getBuilder().setSelectedBackgroundColor(new ColorValue(203, 232, 246));
        getBuilder().setSelectedBorderColor(new ColorValue(38, 160, 218));

        getBuilder().setDisabledSelectedForegroundColor(new ColorValue(130, 130, 130));
        getBuilder().setDisabledSelectedBackgroundColor(new ColorValue(255, 255, 255));
        getBuilder().setDisabledSelectedBorderColor(new ColorValue(130, 130, 130));
        return getBuilder();
    }

}

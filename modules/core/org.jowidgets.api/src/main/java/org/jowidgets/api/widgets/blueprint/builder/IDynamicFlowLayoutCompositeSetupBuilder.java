/*
 * Copyright (c) 2015, Michael Grossmann
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
package org.jowidgets.api.widgets.blueprint.builder;

import org.jowidgets.common.types.Orientation;

public interface IDynamicFlowLayoutCompositeSetupBuilder<INSTANCE_TYPE extends IDynamicFlowLayoutCompositeSetupBuilder<?>> extends
        IComponentSetupBuilder<INSTANCE_TYPE> {

    /**
     * Sets the orientation of the controls flow layout
     * 
     * @param orientation The orientation
     * 
     * @return This builder
     */
    INSTANCE_TYPE setOrientation(Orientation orientation);

    /**
     * Sets the layout growing attribute
     * 
     * If set to true, the composite grows in the opposite direction than the composite is oriented
     * 
     * If orientation is horizontal, the composite grows vertical
     * If orientation is vertical, the composite grows horizontal
     * 
     * @param growing The growing attribute
     * 
     * @return This builder
     */
    INSTANCE_TYPE setLayoutGrowing(boolean growing);

    /**
     * Sets the layout min size.
     * 
     * This defines the minWidth or the minHeight depending on the orientation.
     * 
     * If orientation is horizontal, the the parameter has effect to the composites height
     * If orientation is vertical, the the parameter has effect to the composites width
     * 
     * @param size The size to use
     * 
     * @return This builder
     */
    INSTANCE_TYPE setLayoutMinSize(Integer size);

    /**
     * Sets the layout preferred size.
     * 
     * This defines the preferredWidth or the preferredHeight depending on the orientation.
     * 
     * If orientation is horizontal, the the parameter has effect to the composites height
     * If orientation is vertical, the the parameter has effect to the composites width
     * 
     * @param size The size to use
     * 
     * @return This builder
     */
    INSTANCE_TYPE setLayoutPreferredSize(Integer size);

    /**
     * Sets the layout max size.
     * 
     * This defines the maxWidth or the maxHeight depending on the orientation.
     * 
     * If orientation is horizontal, the the parameter has effect to the composites height
     * If orientation is vertical, the the parameter has effect to the composites width
     * 
     * @param size The size to use
     * 
     * @return This builder
     */
    INSTANCE_TYPE setLayoutMaxSize(Integer size);

    /**
     * Sets the gap between the components cells.
     * If no gap is defined, a default gap will be used.
     * 
     * @param gap The gap in pixel to set or null to use the default gap
     * 
     * @return This builder
     */
    INSTANCE_TYPE setGap(Integer gap);

    /**
     * Sets the left margin, the default value is 0
     * 
     * @param margin The margin to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setLeftMargin(int margin);

    /**
     * Sets the right margin, the default value is 0
     * 
     * @param margin The margin to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setRightMargin(int margin);

    /**
     * Sets the top margin, the default value is 0
     * 
     * @param margin The margin to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setTopMargin(int margin);

    /**
     * Sets the bottom margin, the default value is 0
     * 
     * @param margin The margin to set
     * 
     * @return This builder
     */
    INSTANCE_TYPE setBottomMargin(int margin);

}

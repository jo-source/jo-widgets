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

package org.jowidgets.common.widgets;

import org.jowidgets.common.types.Dimension;

public interface IControlCommon extends IComponentCommon {

    /**
     * Sets the tooltip text for the control
     * 
     * @param toolTip The tooltip text or null if the control should not show any tooltip
     */
    void setToolTipText(String toolTip);

    /**
     * Sets the layout constraints of the control
     * 
     * @param layoutConstraints The layout constraints, may be null
     */
    void setLayoutConstraints(Object layoutConstraints);

    /**
     * Gets the layout constraints of the control
     * 
     * @return The layout constraints or null if no layout constraints was set before
     */
    Object getLayoutConstraints();

    /**
     * Gets the minimal size of the control.
     * 
     * @return The minimal size of the control, never null
     */
    Dimension getMinSize();

    /**
     * Gets the preferred size of the control.
     * 
     * @return The preferred size of the control, never null
     */
    Dimension getPreferredSize();

    /**
     * Gets the maximal size of the control.
     * 
     * @return The maximal size of the control, never null
     */
    Dimension getMaxSize();

}

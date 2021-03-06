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

package org.jowidgets.spi;

import java.util.List;

import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.spi.image.IImageFactorySpi;
import org.jowidgets.spi.image.IImageHandleFactorySpi;

public interface IWidgetsServiceProvider {

    IImageRegistry getImageRegistry();

    IImageHandleFactorySpi getImageHandleFactory();

    IImageFactorySpi getImageFactory();

    IWidgetFactorySpi getWidgetFactory();

    /**
     * These widgets are optional and may be not supported on all platforms
     * 
     * @return The optional widgets factory or null if no optional widgets are supported
     */
    IOptionalWidgetsFactorySpi getOptionalWidgetFactory();

    IUiThreadAccessCommon createUiThreadAccess();

    IApplicationRunner createApplicationRunner();

    /**
     * Gets the UI reference (e.g. Shell for SWT, Window for Swing, ...) of the active window.
     * 
     * @return The UI reference of the active window or null, if now window is active
     */
    Object getActiveWindowUiReference();

    /**
     * Gets the UI reference (e.g. Shell for SWT, Window for Swing, ...) for all known windows
     * 
     * @return The UI reference for all known windows or an empty list
     */
    List<Object> getAllWindowsUiReference();

    /**
     * Transforms a local component position to a screen position
     * 
     * @param localPosition Local position relative to the component
     * @param component The component
     * @return screen position
     */
    Position toScreen(Position localPosition, IComponentCommon component);

    /**
     * Transforms a screen position to a local component position
     * 
     * @param screenPosition Screen position
     * @param component The component
     * @return local position relative to the component
     */
    Position toLocal(Position screenPosition, IComponentCommon component);

    /**
     * @return The system clipboard
     */
    IClipboardSpi getClipboard();

}

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
package org.jowidgets.spi;

import org.jowidgets.spi.widgets.ICalendarSpi;
import org.jowidgets.spi.widgets.IDirectoryChooserSpi;
import org.jowidgets.spi.widgets.IFileChooserSpi;
import org.jowidgets.spi.widgets.setup.ICalendarSetupSpi;
import org.jowidgets.spi.widgets.setup.IDirectoryChooserSetupSpi;
import org.jowidgets.spi.widgets.setup.IFileChooserSetupSpi;

public interface IOptionalWidgetsFactorySpi {

    /**
     * @return true, if this spi supports a file chooser, false otherwise
     */
    boolean hasFileChooser();

    /**
     * @param parentUiReference
     * @param setup
     * @return The file chooser or null if {@link IOptionalWidgetsFactorySpi#hasFileChooser()} returns true
     */
    IFileChooserSpi createFileChooser(Object parentUiReference, IFileChooserSetupSpi setup);

    /**
     * @return true, if this spi supports a directory chooser, false otherwise
     */
    boolean hasDirectoryChooser();

    /**
     * @param parentUiReference
     * @param setup
     * @return The directory chooser or null if {@link IOptionalWidgetsFactorySpi#hasDirectoryChooser()} returns true
     */
    IDirectoryChooserSpi createDirectoryChooser(Object parentUiReference, IDirectoryChooserSetupSpi setup);

    /**
     * @return true, if this spi supports a calendar, false otherwise
     */
    boolean hasCalendar();

    /**
     * @param parentUiReference
     * @param setup
     * @return The calendar or null if {@link IOptionalWidgetsFactorySpi#hasCalendar()} returns true
     */
    ICalendarSpi createCalendar(Object parentUiReference, ICalendarSetupSpi setup);
}

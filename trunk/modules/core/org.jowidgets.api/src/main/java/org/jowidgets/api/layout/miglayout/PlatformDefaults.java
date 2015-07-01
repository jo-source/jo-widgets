/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.api.layout.miglayout;

public final class PlatformDefaults {

    public static final int WINDOWS_XP = IPlatformDefaults.WINDOWS_XP;
    public static final int MAC_OSX = IPlatformDefaults.MAC_OSX;
    public static final int GNOME = IPlatformDefaults.GNOME;

    /**
     * I value indicating that the size of the font for the container of the component
     * will be used as a base for calculating the logical pixel size. This is much as how
     * Windows calculated DLU (dialog units).
     * 
     * @see net.miginfocom.layout.UnitValue#LPX
     * @see net.miginfocom.layout.UnitValue#LPY
     * @see #setLogicalPixelBase(int)
     */
    public static final int BASE_FONT_SIZE = IPlatformDefaults.BASE_FONT_SIZE;

    /**
     * I value indicating that the screen DPI will be used as a base for calculating the
     * logical pixel size.
     * <p>
     * This is the default value.
     * 
     * @see net.miginfocom.layout.UnitValue#LPX
     * @see net.miginfocom.layout.UnitValue#LPY
     * @see #setLogicalPixelBase(int)
     * @see #setVerticalScaleFactor(Float)
     * @see #setHorizontalScaleFactor(Float)
     */
    public static final int BASE_SCALE_FACTOR = IPlatformDefaults.BASE_SCALE_FACTOR;

    /**
     * I value indicating that the size of a logical pixel should always be a real pixel
     * and thus no compensation will be made.
     * 
     * @see net.miginfocom.layout.UnitValue#LPX
     * @see net.miginfocom.layout.UnitValue#LPY
     * @see #setLogicalPixelBase(int)
     */
    public static final int BASE_REAL_PIXEL = IPlatformDefaults.BASE_REAL_PIXEL;

    private PlatformDefaults() {}

    public static IPlatformDefaults getInstance() {
        return MigLayoutToolkit.getPlatformDefaults();
    }

    /**
     * Returns the platform that the JRE is running on currently.
     * 
     * @return The platform that the JRE is running on currently. E.g. {@link #MAC_OSX}, {@link #WINDOWS_XP}, or {@link #GNOME}.
     */
    public static int getCurrentPlatform() {
        return getInstance().getCurrentPlatform();
    }

    /**
     * Set the defaults to the default for the platform
     * 
     * @param plaf The platform. <code>PlatformDefaults.WINDOWS_XP</code>, <code>PlatformDefaults.MAC_OSX</code>, or
     *            <code>PlatformDefaults.GNOME</code>.
     */
    public static void setPlatform(final int plaf) {
        getInstance().setPlatform(plaf);
    }

    /**
     * Returns the current platform
     * 
     * @return <code>PlatformDefaults.WINDOWS</code> or <code>PlatformDefaults.MAC_OSX</code>
     */
    public static int getPlatform() {
        return getInstance().getPlatform();
    }

    public static int getDefaultDPI() {
        return getInstance().getDefaultDPI();
    }

    /**
     * Sets the default platform DPI. Normally this is set in the {@link #setPlatform(int)} for the different platforms
     * but it can be tweaked here. For instance SWT on Mac does this.
     * <p>
     * Note that this is not the actual current DPI, but the base DPI for the toolkit.
     * 
     * @param dpi The base DPI. If null the default DPI is reset to the platform base DPI.
     */
    public static void setDefaultDPI(final Integer dpi) {
        getInstance().setDefaultDPI(dpi);
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @return The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see ComponentWrapper#getHorizontalScreenDPI()
     */
    public static Float getHorizontalScaleFactor() {
        return getInstance().getHorizontalScaleFactor();
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @param f The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see ComponentWrapper#getHorizontalScreenDPI()
     */
    public static void setHorizontalScaleFactor(final Float f) {
        getInstance().setHorizontalScaleFactor(f);
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @return The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see ComponentWrapper#getVerticalScreenDPI()
     */
    public static Float getVerticalScaleFactor() {
        return getInstance().getVerticalScaleFactor();
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @param f The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see ComponentWrapper#getVerticalScreenDPI()
     */
    public static void setVerticalScaleFactor(final Float f) {
        getInstance().setVerticalScaleFactor(f);
    }

    /**
     * What base value should be used to calculate logical pixel sizes.
     * 
     * @return The current base. Default is {@link #BASE_SCALE_FACTOR}
     * @see #BASE_FONT_SIZE
     * @see # BASE_SCREEN_DPI_FACTOR
     * @see #BASE_REAL_PIXEL
     */
    public static int getLogicalPixelBase() {
        return getInstance().getLogicalPixelBase();
    }

    /**
     * What base value should be used to calculate logical pixel sizes.
     * 
     * @param base The new base. Default is {@link #BASE_SCALE_FACTOR}
     * @see #BASE_FONT_SIZE
     * @see # BASE_SCREEN_DPI_FACTOR
     * @see #BASE_REAL_PIXEL
     */
    public static void setLogicalPixelBase(final int base) {
        getInstance().setLogicalPixelBase(base);
    }

    /**
     * Returns the order for the typical buttons in a standard button bar. It is one letter per button type.
     * 
     * @return The button order.
     * @see #setButtonOrder(String)
     */
    public static String getButtonOrder() {
        return getInstance().getButtonOrder();
    }

    /**
     * Sets the order for the typical buttons in a standard button bar. It is one letter per button type.
     * <p>
     * Letter in upper case will get the minimum button width that the {@link #getMinimumButtonWidth()} specifies and letters in
     * lower case will get the width the current look&feel specifies.
     * <p>
     * Gaps will never be added to before the first component or after the last component. However, '+' (push) will be applied
     * before and after as well, but with a minimum size of 0 if first/last so there will not be a gap before or after.
     * <p>
     * If gaps are explicitly set on buttons they will never be reduced, but they may be increased.
     * <p>
     * These are the characters that can be used:
     * <ul>
     * <li><code>'L'</code> - Buttons with this style tag will staticall end up on the left end of the bar.
     * <li><code>'R'</code> - Buttons with this style tag will staticall end up on the right end of the bar.
     * <li><code>'H'</code> - A tag for the "help" button that normally is supposed to be on the right.
     * <li><code>'E'</code> - A tag for the "help2" button that normally is supposed to be on the left.
     * <li><code>'Y'</code> - A tag for the "yes" button.
     * <li><code>'N'</code> - A tag for the "no" button.
     * <li><code>'X'</code> - A tag for the "next >" or "forward >" button.
     * <li><code>'B'</code> - A tag for the "< back>" or "< previous" button.
     * <li><code>'I'</code> - A tag for the "finish".
     * <li><code>'A'</code> - A tag for the "apply" button.
     * <li><code>'C'</code> - A tag for the "cancel" or "close" button.
     * <li><code>'O'</code> - A tag for the "ok" or "done" button.
     * <li><code>'U'</code> - All Uncategorized, Other, or "Unknown" buttons. Tag will be "other".
     * <li><code>'+'</code> - A glue push gap that will take as much space as it can and at least an "unrelated" gap. (Platform
     * dependant)
     * <li><code>'_'</code> - (underscore) An "unrelated" gap. (Platform dependant)
     * </ul>
     * <p>
     * Even though the style tags are normally applied to buttons this works with all components.
     * <p>
     * The normal style for MAC OS X is <code>"L_HE+U+FBI_NYCOA_R"</code>, for Windows is <code>"L_E+U+FBI_YNOCAH_R"</code>, and
     * for GNOME is <code>"L_HE+UNYACBXIO_R"</code>.
     * 
     * @param order The new button order for the current platform.
     */
    public static void setButtonOrder(final String order) {
        getInstance().setButtonOrder(order);
    }

    /**
     * Returns the percentage used for alignment for labels (0 is left, 50 is center and 100 is right).
     * 
     * @return The percentage used for alignment for labels
     */
    public static float getLabelAlignPercentage() {
        return getInstance().getLabelAlignPercentage();
    }

    /**
     * Returns how many times the defaults has been changed. This can be used as a light weight check to
     * see if layout caches needs to be refreshed.
     * 
     * @return How many times the defaults has been changed.
     */
    public static int getModCount() {
        return getInstance().getModCount();
    }

    /**
     * Tells all layout manager instances to revalidate and recalculated everything.
     */
    public static void invalidate() {
        getInstance().invalidate();
    }

    /**
     * Returns the current default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @return The current default unit.
     * @see net.miginfocom.layout.UnitValue#PIXEL
     * @see net.miginfocom.layout.UnitValue#LPX
     */
    public static int getDefaultHorizontalUnit() {
        return getInstance().getDefaultHorizontalUnit();
    }

    /**
     * Sets the default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @param unit The new default unit.
     * @see net.miginfocom.layout.UnitValue#PIXEL
     * @see net.miginfocom.layout.UnitValue#LPX
     */
    public static void setDefaultHorizontalUnit(final int unit) {
        getInstance().setDefaultHorizontalUnit(unit);
    }

    /**
     * Returns the current default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @return The current default unit.
     * @see net.miginfocom.layout.UnitValue#PIXEL
     * @see net.miginfocom.layout.UnitValue#LPY
     */
    public static int getDefaultVerticalUnit() {
        return getInstance().getDefaultVerticalUnit();
    }

    /**
     * Sets the default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @param unit The new default unit.
     * @see net.miginfocom.layout.UnitValue#PIXEL
     * @see net.miginfocom.layout.UnitValue#LPY
     */
    public static void setDefaultVerticalUnit(final int unit) {
        getInstance().setDefaultVerticalUnit(unit);
    }

    /**
     * The default alignment for rows. Pre v3.5 this was <code>false</code> but now it is <code>true</code>.
     * 
     * @return The current value. Default is <code>true</code>.
     * @since 3.5
     */
    public static boolean getDefaultRowAlignmentBaseline() {
        return getInstance().getDefaultRowAlignmentBaseline();
    }

    /**
     * The default alignment for rows. Pre v3.5 this was <code>false</code> but now it is <code>true</code>.
     * 
     * @param b The new value. Default is <code>true</code> from v3.5.
     * @since 3.5
     */
    public static void setDefaultRowAlignmentBaseline(final boolean b) {
        getInstance().setDefaultRowAlignmentBaseline(b);
    }
}

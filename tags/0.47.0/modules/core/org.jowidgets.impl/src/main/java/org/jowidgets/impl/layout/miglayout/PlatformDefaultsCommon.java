/*
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com), 
 * modifications by Nikolaus Moll
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
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
 * 
 * @version 1.0
 * @author Mikael Grev, MiG InfoCom AB
 *         Date: 2006-sep-08
 * @author Xxxx Xxxx, Xxxx  - Gnome support
 *         Date: 2008-jan-16

 */
package org.jowidgets.impl.layout.miglayout;

import java.util.HashMap;

import javax.swing.SwingConstants;

import org.jowidgets.api.layout.miglayout.IPlatformDefaults;

/**
 * Currently handles Windows, Mac OS X, and GNOME spacing.
 */
final class PlatformDefaultsCommon implements IPlatformDefaults {
    private int defaultHUnit = UnitValueToolkitCommon.LPX;
    private int defaultVUnit = UnitValueToolkitCommon.LPY;

    private IInCellGapProviderCommon gapProvider = null;

    private volatile int modificationCount = 0;

    private final UnitValueCommon lpx4 = new UnitValueCommon(4, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx6 = new UnitValueCommon(6, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx7 = new UnitValueCommon(7, UnitValueToolkitCommon.LPX, null);
    //	private final UnitValue lpx8 = new UnitValue(8, UnitValueToolkit.LPX, null);
    private final UnitValueCommon lpx9 = new UnitValueCommon(9, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx10 = new UnitValueCommon(10, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx11 = new UnitValueCommon(11, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx12 = new UnitValueCommon(12, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx14 = new UnitValueCommon(14, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx16 = new UnitValueCommon(16, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx18 = new UnitValueCommon(18, UnitValueToolkitCommon.LPX, null);
    private final UnitValueCommon lpx20 = new UnitValueCommon(20, UnitValueToolkitCommon.LPX, null);

    private final UnitValueCommon lpy4 = new UnitValueCommon(4, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy6 = new UnitValueCommon(6, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy7 = new UnitValueCommon(7, UnitValueToolkitCommon.LPY, null);
    //	private static final UnitValue lpy8 = new UnitValue(8, UnitValueToolkit.LPY, null);
    private final UnitValueCommon lpy9 = new UnitValueCommon(9, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy10 = new UnitValueCommon(10, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy11 = new UnitValueCommon(11, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy12 = new UnitValueCommon(12, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy14 = new UnitValueCommon(14, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy16 = new UnitValueCommon(16, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy18 = new UnitValueCommon(18, UnitValueToolkitCommon.LPY, null);
    private final UnitValueCommon lpy20 = new UnitValueCommon(20, UnitValueToolkitCommon.LPY, null);

    private int currentPlatform = WINDOWS_XP;

    // Used for holding values.
    private final UnitValueCommon[] panelIns = new UnitValueCommon[4];
    private final UnitValueCommon[] dialogIns = new UnitValueCommon[4];

    private String buttonFormat = null;

    private final HashMap<String, UnitValueCommon> horDefs = new HashMap<String, UnitValueCommon>(32);
    private final HashMap<String, UnitValueCommon> verDefs = new HashMap<String, UnitValueCommon>(32);
    private BoundSizeCommon defVGap = null;
    private BoundSizeCommon defHGap = null;
    private BoundSizeCommon relatedX = null;
    private BoundSizeCommon relatedY = null;

    @SuppressWarnings("unused")
    private BoundSizeCommon unrelatedX = null;

    @SuppressWarnings("unused")
    private BoundSizeCommon unrelatedY = null;

    private UnitValueCommon buttonWidth = null;

    private Float horScale = null;
    private Float verScale = null;

    private int lpBase = BASE_SCALE_FACTOR;

    private Integer baseDpiForces = null;
    private int baseDpi = 96;

    private boolean dra = true;

    PlatformDefaultsCommon() {
        setPlatform(getCurrentPlatform());
        modificationCount = 0;
    }

    /**
     * Returns the platform that the JRE is running on currently.
     * 
     * @return The platform that the JRE is running on currently. E.g. {@link #MAC_OSX}, {@link #WINDOWS_XP}, or {@link #GNOME}.
     */
    @Override
    public int getCurrentPlatform() {
        final String os = System.getProperty("os.name");
        if (os.startsWith("Mac OS")) {
            return MAC_OSX;
        }
        else if (os.startsWith("Linux")) {
            return GNOME;
        }
        else {
            return WINDOWS_XP;
        }
    }

    /**
     * Set the defaults to the default for the platform
     * 
     * @param plaf The platform. <code>PlatformDefaults.WINDOWS_XP</code>, <code>PlatformDefaults.MAC_OSX</code>, or
     *            <code>PlatformDefaults.GNOME</code>.
     */
    @Override
    public void setPlatform(final int plaf) {
        switch (plaf) {
            case WINDOWS_XP:
                setRelatedGap(lpx4, lpy4);
                setUnrelatedGap(lpx7, lpy9);
                setParagraphGap(lpx14, lpy14);
                setIndentGap(lpx9, lpy9);
                setGridCellGap(lpx4, lpy4);

                setMinimumButtonWidth(new UnitValueCommon(75, UnitValueToolkitCommon.LPX, null));
                setButtonOrder("L_E+U+YNBXOCAH_R");
                setDialogInsets(lpy11, lpx11, lpy11, lpx11);
                setPanelInsets(lpy7, lpx7, lpy7, lpx7);
                break;
            case MAC_OSX:
                setRelatedGap(lpx4, lpy4);
                setUnrelatedGap(lpx7, lpy9);
                setParagraphGap(lpx14, lpy14);
                setIndentGap(lpx10, lpy10);
                setGridCellGap(lpx4, lpy4);

                setMinimumButtonWidth(new UnitValueCommon(68, UnitValueToolkitCommon.LPX, null));
                setButtonOrder("L_HE+U+NYBXCOA_R");
                setDialogInsets(lpy14, lpx20, lpy20, lpx20);
                setPanelInsets(lpy16, lpx16, lpy16, lpx16);

                // setRelatedGap(LPX8, LPY8);
                // setUnrelatedGap(LPX12, LPY12);
                // setParagraphGap(LPX16, LPY16);
                // setIndentGap(LPX10, LPY10);
                // setGridCellGap(LPX8, LPY8);
                //
                // setMinimumButtonWidth(new UnitValue(68, UnitValueToolkit.LPX, null));
                // setButtonOrder("L_HE+U+NYBXCOA_R");
                // setDialogInsets(LPY14, LPX20, LPY20, LPX20);
                // setPanelInsets(LPY16, LPX16, LPY16, LPX16);
                break;
            case GNOME:
                setRelatedGap(lpx6, lpy6); // GNOME HIG 8.2.3
                setUnrelatedGap(lpx12, lpy12); // GNOME HIG 8.2.3
                setParagraphGap(lpx18, lpy18); // GNOME HIG 8.2.3
                setIndentGap(lpx12, lpy12); // GNOME HIG 8.2.3
                setGridCellGap(lpx6, lpy6); // GNOME HIG 8.2.3

                // GtkButtonBox, child-min-width property default value
                setMinimumButtonWidth(new UnitValueCommon(85, UnitValueToolkitCommon.LPX, null));
                setButtonOrder("L_HE+UNYACBXIO_R"); // GNOME HIG 3.4.2, 3.7.1
                setDialogInsets(lpy12, lpx12, lpy12, lpx12); // GNOME HIG 3.4.3
                setPanelInsets(lpy6, lpx6, lpy6, lpx6); // ???
                break;
            default:
                throw new IllegalArgumentException("Unknown platform: " + plaf);
        }
        currentPlatform = plaf;
        baseDpi = baseDpiForces != null ? baseDpiForces : getPlatformDPI(plaf);
    }

    private int getPlatformDPI(final int plaf) {
        switch (plaf) {
            case WINDOWS_XP:
            case GNOME:
                return 96;
            case MAC_OSX:
                try {
                    return System.getProperty("java.version").compareTo("1.6") < 0 ? 72 : 96; // Default DPI was 72 prior to JSE 1.6
                }
                catch (final Throwable t) {
                    return 72;
                }
            default:
                throw new IllegalArgumentException("Unknown platform: " + plaf);
        }
    }

    /**
     * Returns the current platform
     * 
     * @return <code>PlatformDefaults.WINDOWS</code> or <code>PlatformDefaults.MAC_OSX</code>
     */
    @Override
    public int getPlatform() {
        return currentPlatform;
    }

    @Override
    public int getDefaultDPI() {
        return baseDpi;
    }

    /**
     * Sets the default platform DPI. Normally this is set in the {@link #setPlatform(int)} for the different platforms
     * but it can be tweaked here. For instance SWT on Mac does this.
     * <p>
     * Note that this is not the actual current DPI, but the base DPI for the toolkit.
     * 
     * @param dpi The base DPI. If null the default DPI is reset to the platform base DPI.
     */
    @Override
    public void setDefaultDPI(final Integer dpi) {
        baseDpi = dpi != null ? dpi : getPlatformDPI(currentPlatform);
        baseDpiForces = dpi;
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @return The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see IComponentWrapperCommon#getHorizontalScreenDPI()
     */
    @Override
    public Float getHorizontalScaleFactor() {
        return horScale;
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @param f The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see IComponentWrapperCommon#getHorizontalScreenDPI()
     */
    @Override
    public void setHorizontalScaleFactor(final Float f) {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        if (layoutUtil.equals(horScale, f) == false) {
            horScale = f;
            modificationCount++;
        }
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @return The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see IComponentWrapperCommon#getVerticalScreenDPI()
     */
    @Override
    public Float getVerticalScaleFactor() {
        return verScale;
    }

    /**
     * The forced scale factor that all screen relative units (e.g. millimeters, inches and logical pixels) will be multiplied
     * with. If <code>null</code> this will default to a scale that will scale the current screen to the default screen resolution
     * (72 DPI for Mac and 92 DPI for Windows).
     * 
     * @param f The forced scale or <code>null</code> for default scaling.
     * @see #getHorizontalScaleFactor()
     * @see IComponentWrapperCommon#getVerticalScreenDPI()
     */
    @Override
    public void setVerticalScaleFactor(final Float f) {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        if (layoutUtil.equals(verScale, f) == false) {
            verScale = f;
            modificationCount++;
        }
    }

    /**
     * What base value should be used to calculate logical pixel sizes.
     * 
     * @return The current base. Default is {@link #BASE_SCALE_FACTOR}
     * @see #BASE_FONT_SIZE
     * @see #BASE_SCALE_FACTOR
     * @see #BASE_REAL_PIXEL
     */
    @Override
    public int getLogicalPixelBase() {
        return lpBase;
    }

    /**
     * What base value should be used to calculate logical pixel sizes.
     * 
     * @param base The new base. Default is {@link #BASE_SCALE_FACTOR}
     * @see #BASE_FONT_SIZE
     * @see #BASE_SCALE_FACTOR
     * @see #BASE_REAL_PIXEL
     */
    @Override
    public void setLogicalPixelBase(final int base) {
        if (lpBase != base) {
            if (base < BASE_FONT_SIZE || base > BASE_SCALE_FACTOR) {
                throw new IllegalArgumentException("Unrecognized base: " + base);
            }

            lpBase = base;
            modificationCount++;
        }
    }

    /**
     * Sets gap value for components that are "related".
     * 
     * @param x The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     * @param y The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     */
    public void setRelatedGap(final UnitValueCommon x, final UnitValueCommon y) {
        setUnitValue(new String[] {"r", "rel", "related"}, x, y);

        relatedX = new BoundSizeCommon(x, x, null, "rel:rel");
        relatedY = new BoundSizeCommon(y, y, null, "rel:rel");
    }

    /**
     * Sets gap value for components that are "unrelated".
     * 
     * @param x The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     * @param y The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     */
    public void setUnrelatedGap(final UnitValueCommon x, final UnitValueCommon y) {
        setUnitValue(new String[] {"u", "unrel", "unrelated"}, x, y);

        unrelatedX = new BoundSizeCommon(x, x, null, "unrel:unrel");
        unrelatedY = new BoundSizeCommon(y, y, null, "unrel:unrel");
    }

    /**
     * Sets paragraph gap value for components.
     * 
     * @param x The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     * @param y The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     */
    public void setParagraphGap(final UnitValueCommon x, final UnitValueCommon y) {
        setUnitValue(new String[] {"p", "para", "paragraph"}, x, y);
    }

    /**
     * Sets gap value for components that are "intended".
     * 
     * @param x The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     * @param y The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     */
    public void setIndentGap(final UnitValueCommon x, final UnitValueCommon y) {
        setUnitValue(new String[] {"i", "ind", "indent"}, x, y);
    }

    /**
     * Sets gap between two cells in the grid. Note that this is not a gap between component IN a cell, that has to be set
     * on the component constraints. The value will be the min and preferred size of the gap.
     * 
     * @param x The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     * @param y The value that will be transformed to pixels. If <code>null</code> the current value will not change.
     */
    public void setGridCellGap(final UnitValueCommon x, final UnitValueCommon y) {
        if (x != null) {
            defHGap = new BoundSizeCommon(x, x, null, null);
        }

        if (y != null) {
            defVGap = new BoundSizeCommon(y, y, null, null);
        }

        modificationCount++;
    }

    /**
     * Sets the recommended minimum button width.
     * 
     * @param width The recommended minimum button width.
     */
    public void setMinimumButtonWidth(final UnitValueCommon width) {
        buttonWidth = width;
        modificationCount++;
    }

    /**
     * Returns the recommended minimum button width depending on the current set platform.
     * 
     * @return The recommended minimum button width depending on the current set platform.
     */
    public UnitValueCommon getMinimumButtonWidth() {
        return buttonWidth;
    }

    /**
     * Returns the unit value associated with the unit. (E.i. "related" or "indent"). Must be lower case.
     * 
     * @param unit The unit string.
     * @return The unit value associated with the unit. <code>null</code> for unrecognized units.
     */
    public UnitValueCommon getUnitValueX(final String unit) {
        return horDefs.get(unit);
    }

    /**
     * Returns the unit value associated with the unit. (E.i. "related" or "indent"). Must be lower case.
     * 
     * @param unit The unit string.
     * @return The unit value associated with the unit. <code>null</code> for unrecognized units.
     */
    public UnitValueCommon getUnitValueY(final String unit) {
        return verDefs.get(unit);
    }

    /**
     * Sets the unit value associated with a unit string. This may be used to store values for new unit strings
     * or modify old. Note that if a built in unit (such as "related") is modified all versions of it must be
     * set (I.e. "r", "rel" and "related"). The build in values will be reset to the default ones if the platform
     * is re-set.
     * 
     * @param unitStrings The unit strings. E.g. "mu", "myunit". Will be converted to lower case and trimmed. Not
     *            <code>null</code>.
     * @param x The value for the horizontal dimension. If <code>null</code> the value is not changed.
     * @param y The value for the vertical dimension. Might be same object as for <code>x</code>. If <code>null</code> the value
     *            is not changed.
     */
    public void setUnitValue(final String[] unitStrings, final UnitValueCommon x, final UnitValueCommon y) {
        for (final String unitString : unitStrings) {
            final String s = unitString.toLowerCase().trim();
            if (x != null) {
                horDefs.put(s, x);
            }
            if (y != null) {
                verDefs.put(s, y);
            }
        }
        modificationCount++;
    }

    /**
     * Understands ("r", "rel", "related") OR ("u", "unrel", "unrelated") OR ("i", "ind", "indent") OR ("p", "para", "paragraph").
     */
    int convertToPixels(
        final float value,
        final String unit,
        final boolean isHor,
        final float ref,
        final IContainerWrapperCommon parent,
        final IComponentWrapperCommon comp) {
        final UnitValueCommon uv = (isHor ? horDefs : verDefs).get(unit);
        return uv != null ? Math.round(value * uv.getPixels(ref, parent, comp)) : UnitConverterCommon.UNABLE;
    }

    /**
     * Returns the order for the typical buttons in a standard button bar. It is one letter per button type.
     * 
     * @return The button order.
     * @see #setButtonOrder(String)
     */
    @Override
    public String getButtonOrder() {
        return buttonFormat;
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
    @Override
    public void setButtonOrder(final String order) {
        buttonFormat = order;
        modificationCount++;
    }

    /**
     * Returns the tag (used in the {@link CCCommon}) for a char. The char is same as used in {@link #getButtonOrder()}.
     * 
     * @param c The char. Must be lower case!
     * @return The tag that corresponds to the char or <code>null</code> if the char is unrecognized.
     */
    static String getTagForChar(final char c) {
        switch (c) {
            case 'o':
                return "ok";
            case 'c':
                return "cancel";
            case 'h':
                return "help";
            case 'e':
                return "help2";
            case 'y':
                return "yes";
            case 'n':
                return "no";
            case 'a':
                return "apply";
            case 'x':
                return "next"; // a.k.a forward
            case 'b':
                return "back"; // a.k.a. previous
            case 'i':
                return "finish";
            case 'l':
                return "left";
            case 'r':
                return "right";
            case 'u':
                return "other";
            default:
                return null;
        }
    }

    /**
     * Returns the platform recommended inter-cell gap in the horizontal (x) dimension..
     * 
     * @return The platform recommended inter-cell gap in the horizontal (x) dimension..
     */
    public BoundSizeCommon getGridGapX() {
        return defHGap;
    }

    /**
     * Returns the platform recommended inter-cell gap in the vertical (x) dimension..
     * 
     * @return The platform recommended inter-cell gap in the vertical (x) dimension..
     */
    public BoundSizeCommon getGridGapY() {
        return defVGap;
    }

    /**
     * Returns the default dialog inset depending of the current platform.
     * 
     * @param side top == 0, left == 1, bottom = 2, right = 3.
     * @return The inset. Never <code>null</code>.
     */
    public UnitValueCommon getDialogInsets(final int side) {
        return dialogIns[side];
    }

    /**
     * Sets the default insets for a dialog. Values that are null will not be changed.
     * 
     * @param top The top inset. May be <code>null</code>.
     * @param left The left inset. May be <code>null</code>.
     * @param bottom The bottom inset. May be <code>null</code>.
     * @param right The right inset. May be <code>null</code>.
     */
    public void setDialogInsets(
        final UnitValueCommon top,
        final UnitValueCommon left,
        final UnitValueCommon bottom,
        final UnitValueCommon right) {
        if (top != null) {
            dialogIns[0] = top;
        }

        if (left != null) {
            dialogIns[1] = left;
        }

        if (bottom != null) {
            dialogIns[2] = bottom;
        }

        if (right != null) {
            dialogIns[3] = right;
        }

        modificationCount++;
    }

    /**
     * Returns the default panel inset depending of the current platform.
     * 
     * @param side top == 0, left == 1, bottom = 2, right = 3.
     * @return The inset. Never <code>null</code>.
     */
    public UnitValueCommon getPanelInsets(final int side) {
        return panelIns[side];
    }

    /**
     * Sets the default insets for a dialog. Values that are null will not be changed.
     * 
     * @param top The top inset. May be <code>null</code>.
     * @param left The left inset. May be <code>null</code>.
     * @param bottom The bottom inset. May be <code>null</code>.
     * @param right The right inset. May be <code>null</code>.
     */
    public void setPanelInsets(
        final UnitValueCommon top,
        final UnitValueCommon left,
        final UnitValueCommon bottom,
        final UnitValueCommon right) {
        if (top != null) {
            panelIns[0] = top;
        }

        if (left != null) {
            panelIns[1] = left;
        }

        if (bottom != null) {
            panelIns[2] = bottom;
        }

        if (right != null) {
            panelIns[3] = right;
        }

        modificationCount++;
    }

    /**
     * Returns the percentage used for alignment for labels (0 is left, 50 is center and 100 is right).
     * 
     * @return The percentage used for alignment for labels
     */
    @Override
    public float getLabelAlignPercentage() {
        return currentPlatform == MAC_OSX ? 1f : 0f;
    }

    /**
     * Returns the default gap between two components that <b>are in the same cell</b>.
     * 
     * @param comp The component that the gap is for. Never <code>null</code>.
     * @param adjacentComp The adjacent component if any. May be <code>null</code>.
     * @param adjacentSide What side the <code>adjacentComp</code> is on. {@link javax.swing.SwingUtilities#TOP} or
     *            {@link javax.swing.SwingUtilities#LEFT} or {@link javax.swing.SwingUtilities#BOTTOM} or
     *            {@link javax.swing.SwingUtilities#RIGHT}.
     * @param tag The tag string that the component might be tagged with in the component constraints. May be <code>null</code>.
     * @param isLTR If it is left-to-right.
     * @return The default gap between two components or <code>null</code> if there should be no gap.
     */
    BoundSizeCommon getDefaultComponentGap(
        final IComponentWrapperCommon comp,
        final IComponentWrapperCommon adjacentComp,
        final int adjacentSide,
        final String tag,
        final boolean isLTR) {
        if (gapProvider != null) {
            return gapProvider.getDefaultGap(comp, adjacentComp, adjacentSide, tag, isLTR);
        }

        if (adjacentComp == null) {
            return null;
        }

        // if (adjacentComp == null || adjacentSide == SwingConstants.LEFT || adjacentSide == SwingConstants.TOP)
        //	return null;

        return (adjacentSide == SwingConstants.LEFT || adjacentSide == SwingConstants.RIGHT) ? relatedX : relatedY;
    }

    /**
     * Returns the current gap provider or <code>null</code> if none is set and "related" should always be used.
     * 
     * @return The current gap provider or <code>null</code> if none is set and "related" should always be used.
     */
    public IInCellGapProviderCommon getGapProvider() {
        return gapProvider;
    }

    /**
     * Sets the current gap provider or <code>null</code> if none is set and "related" should always be used.
     * 
     * @param provider The current gap provider or <code>null</code> if none is set and "related" should always be used.
     */
    public void setGapProvider(final IInCellGapProviderCommon provider) {
        gapProvider = provider;
    }

    /**
     * Returns how many times the defaults has been changed. This can be used as a light weight check to
     * see if layout caches needs to be refreshed.
     * 
     * @return How many times the defaults has been changed.
     */
    @Override
    public int getModCount() {
        return modificationCount;
    }

    /**
     * Tells all layout manager instances to revalidate and recalculated everything.
     */
    @Override
    public void invalidate() {
        modificationCount++;
    }

    /**
     * Returns the current default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @return The current default unit.
     * @see UnitValueCommon#PIXEL
     * @see UnitValueCommon#LPX
     */
    @Override
    public int getDefaultHorizontalUnit() {
        return defaultHUnit;
    }

    /**
     * Sets the default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @param unit The new default unit.
     * @see UnitValueCommon#PIXEL
     * @see UnitValueCommon#LPX
     */
    @Override
    public void setDefaultHorizontalUnit(final int unit) {
        if (unit < UnitValueToolkitCommon.PIXEL || unit > UnitValueToolkitCommon.LABEL_ALIGN) {
            throw new IllegalArgumentException("Illegal Unit: " + unit);
        }

        if (defaultHUnit != unit) {
            defaultHUnit = unit;
            modificationCount++;
        }
    }

    /**
     * Returns the current default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @return The current default unit.
     * @see UnitValueCommon#PIXEL
     * @see UnitValueCommon#LPY
     */
    @Override
    public int getDefaultVerticalUnit() {
        return defaultVUnit;
    }

    /**
     * Sets the default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
     * 
     * @param unit The new default unit.
     * @see UnitValueCommon#PIXEL
     * @see UnitValueCommon#LPY
     */
    @Override
    public void setDefaultVerticalUnit(final int unit) {
        if (unit < UnitValueToolkitCommon.PIXEL || unit > UnitValueToolkitCommon.LABEL_ALIGN) {
            throw new IllegalArgumentException("Illegal Unit: " + unit);
        }

        if (defaultVUnit != unit) {
            defaultVUnit = unit;
            modificationCount++;
        }
    }

    /**
     * The default alignment for rows. Pre v3.5 this was <code>false</code> but now it is <code>true</code>.
     * 
     * @return The current value. Default is <code>true</code>.
     * @since 3.5
     */
    @Override
    public boolean getDefaultRowAlignmentBaseline() {
        return dra;
    }

    /**
     * The default alignment for rows. Pre v3.5 this was <code>false</code> but now it is <code>true</code>.
     * 
     * @param b The new value. Default is <code>true</code> from v3.5.
     * @since 3.5
     */
    @Override
    public void setDefaultRowAlignmentBaseline(final boolean b) {
        dra = b;
    }
}

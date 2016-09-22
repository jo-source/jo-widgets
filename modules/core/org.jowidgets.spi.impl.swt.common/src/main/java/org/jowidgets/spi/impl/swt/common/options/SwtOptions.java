/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.spi.impl.swt.common.options;

import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;

public final class SwtOptions {

    private static boolean inputVerification = true;
    private static boolean classicTabs = false;
    private static SplitlayoutMode splitLayoutMode = SplitlayoutMode.ON_MOUSE_MOVE;
    private static boolean nativeMigLayout = true;
    private static boolean nativeTextAreaScrollBars = false;
    private static Long clipbaordPollingMillis = Long.valueOf(1000L);

    //not fixed in swt 4.3 (tested with win)
    private static boolean textFieldTruncateWorkaround = true;

    //not fixed in swt 4.3 (tested with win)
    private static boolean comboTruncateWorkaround = true;

    //Fixes https://bugs.eclipse.org/bugs/show_bug.cgi?id=118659
    //fixed since swt 3.7 M4 (tested with win)
    private static boolean compositeMinSizeWorkaround = false;

    //Fixes https://bugs.eclipse.org/bugs/show_bug.cgi?id=39934
    //fixed since swt 4.3 (tested with win 10)
    private static boolean scrollCompositeIncrementWorkaround = false;

    //Fixes https://bugs.eclipse.org/bugs/show_bug.cgi?id=345411
    //Fixed since 3.7.1
    private static boolean tablePackWorkaround = false;

    private static boolean useDecoupledUiThreadAccess = false;

    private static IColorConstant tableSelectedForegroundColor;
    private static IColorConstant tableSelectedBackgroundColor;

    private static IColorConstant treeSelectedForegroundColor;
    private static IColorConstant treeSelectedBackgroundColor;
    private static IColorConstant treeSelectedBorderColor;
    private static IColorConstant treeDisabledSelectedForegroundColor;
    private static IColorConstant treeDisabledSelectedBackgroundColor;
    private static IColorConstant treeDisabledSelectedBorderColor;

    private SwtOptions() {}

    public static boolean hasClassicTabs() {
        return classicTabs;
    }

    public static void setClassicTabs(final boolean classicTabs) {
        SwtOptions.classicTabs = classicTabs;
    }

    public static SplitlayoutMode getSplitLayoutMode() {
        return splitLayoutMode;
    }

    public static void setSplitLayoutMode(final SplitlayoutMode splitLayoutMode) {
        SwtOptions.splitLayoutMode = splitLayoutMode;
    }

    public static boolean hasInputVerification() {
        return inputVerification;
    }

    public static void setInputVerification(final boolean inputVerification) {
        SwtOptions.inputVerification = inputVerification;
    }

    public static boolean hasNativeMigLayout() {
        return nativeMigLayout;
    }

    public static void setNativeMigLayout(final boolean nativeMigLayout) {
        SwtOptions.nativeMigLayout = nativeMigLayout;
    }

    public static boolean hasNativeTextAreaScrollBars() {
        return nativeTextAreaScrollBars;
    }

    public static void setNativeTextAreaScrollBars(final boolean nativeTextAreaScrollBars) {
        SwtOptions.nativeTextAreaScrollBars = nativeTextAreaScrollBars;
    }

    public static IColorConstant getTableSelectedForegroundColor() {
        return tableSelectedForegroundColor;
    }

    public static void setTableSelectedForegroundColor(final IColorConstant tableSelectedForegroundColor) {
        SwtOptions.tableSelectedForegroundColor = tableSelectedForegroundColor;
    }

    public static IColorConstant getTableSelectedBackgroundColor() {
        return tableSelectedBackgroundColor;
    }

    public static void setTableSelectedBackgroundColor(final IColorConstant tableSelectedBackgroundColor) {
        SwtOptions.tableSelectedBackgroundColor = tableSelectedBackgroundColor;
    }

    public static IColorConstant getTreeSelectedForegroundColor() {
        return treeSelectedForegroundColor;
    }

    public static void setTreeSelectedForegroundColor(final IColorConstant treeSelectedForegroundColor) {
        SwtOptions.treeSelectedForegroundColor = treeSelectedForegroundColor;
    }

    public static IColorConstant getTreeSelectedBackgroundColor() {
        return treeSelectedBackgroundColor;
    }

    public static void setTreeSelectedBackgroundColor(final IColorConstant treeSelectedBackgroundColor) {
        SwtOptions.treeSelectedBackgroundColor = treeSelectedBackgroundColor;
    }

    public static IColorConstant getTreeSelectedBorderColor() {
        return treeSelectedBorderColor;
    }

    public static void setTreeSelectedBorderColor(final IColorConstant treeSelectedBorderColor) {
        SwtOptions.treeSelectedBorderColor = treeSelectedBorderColor;
    }

    public static IColorConstant getTreeDisabledSelectedForegroundColor() {
        return treeDisabledSelectedForegroundColor;
    }

    public static void setTreeDisabledSelectedForegroundColor(final IColorConstant treeDisabledSelectedForegroundColor) {
        SwtOptions.treeDisabledSelectedForegroundColor = treeDisabledSelectedForegroundColor;
    }

    public static IColorConstant getTreeDisabledSelectedBackgroundColor() {
        return treeDisabledSelectedBackgroundColor;
    }

    public static void setTreeDisabledSelectedBackgroundColor(final IColorConstant treeDisabledSelectedBackgroundColor) {
        SwtOptions.treeDisabledSelectedBackgroundColor = treeDisabledSelectedBackgroundColor;
    }

    public static IColorConstant getTreeDisabledSelectedBorderColor() {
        return treeDisabledSelectedBorderColor;
    }

    public static void setTreeDisabledSelectedBorderColor(final IColorConstant treeDisabledSelectedBorderColor) {
        SwtOptions.treeDisabledSelectedBorderColor = treeDisabledSelectedBorderColor;
    }

    public static boolean hasTextFieldTruncateWorkaround() {
        return textFieldTruncateWorkaround;
    }

    public static void setTextFieldTruncateWorkaround(final boolean textFieldTruncateWorkaround) {
        SwtOptions.textFieldTruncateWorkaround = textFieldTruncateWorkaround;
    }

    public static boolean hasComboTruncateWorkaround() {
        return comboTruncateWorkaround;
    }

    public static void setComboTruncateWorkaround(final boolean comboTruncateWorkaround) {
        SwtOptions.comboTruncateWorkaround = comboTruncateWorkaround;
    }

    public static boolean hasCompositeMinSizeWorkaround() {
        return compositeMinSizeWorkaround;
    }

    /**
     * Fixes https://bugs.eclipse.org/bugs/show_bug.cgi?id=118659
     * 
     * The default is false, the bug was fixed since 3.7 M4
     * 
     * @param compositeMinSizeWorkaround true to enable the workaround
     */
    public static void setCompositeMinSizeWorkaround(final boolean compositeMinSizeWorkaround) {
        SwtOptions.compositeMinSizeWorkaround = compositeMinSizeWorkaround;
    }

    public static boolean hasScrollCompositeIncrementWorkaround() {
        return scrollCompositeIncrementWorkaround;
    }

    public static boolean isTablePackWorkaround() {
        return tablePackWorkaround;
    }

    /**
     * Fixes https://bugs.eclipse.org/bugs/show_bug.cgi?id=345411
     * Table.pack() makes column width too small by one pixel
     * 
     * The default is false, the bug was fixed since swt 3.7.1
     * 
     * @param tablePackWorkaround true to enable the workaround
     */
    public static void setTablePackWorkaround(final boolean tablePackWorkaround) {
        SwtOptions.tablePackWorkaround = tablePackWorkaround;
    }

    /**
     * Fixes https://bugs.eclipse.org/bugs/show_bug.cgi?id=39934
     * The default is false fixed in swt 4.3 (tested with win 10)
     * 
     * @param scrollCompositeIncrementWorkaround True to enable the workaround
     */
    public static void setScrollCompositeIncrementWorkaround(final boolean scrollCompositeIncrementWorkaround) {
        SwtOptions.scrollCompositeIncrementWorkaround = scrollCompositeIncrementWorkaround;
    }

    /**
     * Some customers mention that selected and unselected colors can hardly be distinguished on win7 and win8
     * clients. With this mode, the selection color is like under winxp
     */
    public static void setClassicTableSelectionColors() {
        setTableSelectedForegroundColor(new ColorValue(255, 255, 255));
        setTableSelectedBackgroundColor(new ColorValue(51, 153, 255));
    }

    /**
     * With this colors the tree selection colors are the same like classic table selection colors
     */
    public static void setClassicTreeSelectionColors() {
        setTreeSelectedForegroundColor(new ColorValue(255, 255, 255));
        setTreeSelectedBackgroundColor(new ColorValue(51, 153, 255));
        setTreeSelectedBorderColor(new ColorValue(60, 60, 60));

        setTreeDisabledSelectedForegroundColor(new ColorValue(130, 130, 130));
        setTreeDisabledSelectedBackgroundColor(new ColorValue(255, 255, 255));
        setTreeDisabledSelectedBorderColor(new ColorValue(130, 130, 130));
    }

    /**
     * With this colors the tree selection colors are the same like in windows
     */
    public static void setWinTreeSelectionColors() {
        setTreeSelectedForegroundColor(new ColorValue(0, 0, 0));
        setTreeSelectedBackgroundColor(new ColorValue(203, 232, 246));
        setTreeSelectedBorderColor(new ColorValue(38, 160, 218));

        setTreeDisabledSelectedForegroundColor(new ColorValue(130, 130, 130));
        setTreeDisabledSelectedBackgroundColor(new ColorValue(255, 255, 255));
        setTreeDisabledSelectedBorderColor(new ColorValue(130, 130, 130));
    }

    public static Long getClipbaordPollingMillis() {
        return clipbaordPollingMillis;
    }

    /**
     * Sets the clipboard polling millis. If set to null, clipboard polling is deactivated.
     * 
     * @param clipbaordPolling The polling interval in millis or null
     */
    public static void setClipbaordPollingMillis(final Long clipbaordPolling) {
        SwtOptions.clipbaordPollingMillis = clipbaordPolling;
    }

    public static boolean isUseDecoupledUiThreadAccess() {
        return useDecoupledUiThreadAccess;
    }

    /**
     * This option can be used to use a decouples swt ui thread access.
     * 
     * The default is false due to backward compatibility
     * 
     * In the current version 4.3 of swt there is a bug that leads to long blocking (> 200 ms)
     * when invoking Display.asyncExec().
     * The example org.jowidgets.examples.swt.UiThreadAccessRuntimeExamplePlainSwt reproduces this bug.
     * 
     * When this option is activated, all invocations of uiThreadAccess.invokeLater() will be enqueued
     * to a SingleThreadAccess instead of being invoked directly on the display.
     * 
     * This will not solve the problem that the ui event will be invoked a little later but that the non
     * ui thread will no longer block.
     * 
     * @param useDecoupledUiThreadAccess enable or disables this option
     */
    public static void setUseDecoupledUiThreadAccess(final boolean useDecoupledUiThreadAccess) {
        SwtOptions.useDecoupledUiThreadAccess = useDecoupledUiThreadAccess;
    }

}

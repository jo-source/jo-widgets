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

	//fixed within swt 3.7 (tested with win)
	private static boolean comboTruncateWorkaround = false;

	private static IColorConstant tableSelectedForegroundColor;
	private static IColorConstant tableSelectedBackgroundColor;

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

	/**
	 * Some customers mention that selected and unselected colors can hardly be distinguished on win7 and win8
	 * clients. With this mode, the selection color in like under winxp
	 */
	public static void setClassicTableSelectionColors() {
		setTableSelectedForegroundColor(new ColorValue(255, 255, 255));
		setTableSelectedBackgroundColor(new ColorValue(51, 153, 255));
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

}

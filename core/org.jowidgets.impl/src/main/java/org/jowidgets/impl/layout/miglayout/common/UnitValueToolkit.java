/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.impl.layout.miglayout.common;

import java.util.ArrayList;
import java.util.HashMap;

import org.jowidgets.impl.layout.miglayout.MigLayoutToolkit;

public final class UnitValueToolkit {
	/**
	 * An operation indicating a static value.
	 */
	public static final int STATIC = 100;

	/**
	 * An operation indicating a addition of two sub units.
	 */
	public static final int ADD = 101; // Must have "sub-unit values"

	/**
	 * An operation indicating a subtraction of two sub units
	 */
	public static final int SUB = 102; // Must have "sub-unit values"

	/**
	 * An operation indicating a multiplication of two sub units.
	 */
	public static final int MUL = 103; // Must have "sub-unit values"

	/**
	 * An operation indicating a division of two sub units.
	 */
	public static final int DIV = 104; // Must have "sub-unit values"

	/**
	 * An operation indicating the minimum of two sub units
	 */
	public static final int MIN = 105; // Must have "sub-unit values"

	/**
	 * An operation indicating the maximum of two sub units
	 */
	public static final int MAX = 106; // Must have "sub-unit values"

	/**
	 * An operation indicating the middle value of two sub units
	 */
	public static final int MID = 107; // Must have "sub-unit values"

	/**
	 * A unit indicating pixels.
	 */
	public static final int PIXEL = 0;

	/**
	 * A unit indicating logical horizontal pixels.
	 */
	public static final int LPX = 1;

	/**
	 * A unit indicating logical vertical pixels.
	 */
	public static final int LPY = 2;

	/**
	 * A unit indicating millimeters.
	 */
	public static final int MM = 3;

	/**
	 * A unit indicating centimeters.
	 */
	public static final int CM = 4;

	/**
	 * A unit indicating inches.
	 */
	public static final int INCH = 5;

	/**
	 * A unit indicating percent.
	 */
	public static final int PERCENT = 6;

	/**
	 * A unit indicating points.
	 */
	public static final int PT = 7;

	/**
	 * A unit indicating screen percentage width.
	 */
	public static final int SPX = 8;

	/**
	 * A unit indicating screen percentage height.
	 */
	public static final int SPY = 9;

	/**
	 * A unit indicating alignment.
	 */
	public static final int ALIGN = 12;

	/**
	 * A unit indicating minimum size.
	 */
	public static final int MIN_SIZE = 13;

	/**
	 * A unit indicating preferred size.
	 */
	public static final int PREF_SIZE = 14;

	/**
	 * A unit indicating maximum size.
	 */
	public static final int MAX_SIZE = 15;

	/**
	 * A unit indicating botton size.
	 */
	public static final int BUTTON = 16;

	/**
	 * A unit indicating linking to x.
	 */
	public static final int LINK_X = 18; // First link

	/**
	 * A unit indicating linking to y.
	 */
	public static final int LINK_Y = 19;

	/**
	 * A unit indicating linking to width.
	 */
	public static final int LINK_W = 20;

	/**
	 * A unit indicating linking to height.
	 */
	public static final int LINK_H = 21;

	/**
	 * A unit indicating linking to x2.
	 */
	public static final int LINK_X2 = 22;

	/**
	 * A unit indicating linking to y2.
	 */
	public static final int LINK_Y2 = 23;

	/**
	 * A unit indicating linking to x position on screen.
	 */
	public static final int LINK_XPOS = 24;

	/**
	 * A unit indicating linking to y position on screen.
	 */
	public static final int LINK_YPOS = 25; // Last link

	/**
	 * A unit indicating a lookup.
	 */
	public static final int LOOKUP = 26;

	/**
	 * A unit indicating label alignment.
	 */
	public static final int LABEL_ALIGN = 27;

	static final int IDENTITY = -1;

	//CHECKSTYLE:OFF
	final UnitValue ZERO = new UnitValue(0, null, PIXEL, true, STATIC, null, null, "0px");
	final UnitValue TOP = new UnitValue(0, null, PERCENT, false, STATIC, null, null, "top");
	final UnitValue LEADING = new UnitValue(0, null, PERCENT, true, STATIC, null, null, "leading");
	final UnitValue LEFT = new UnitValue(0, null, PERCENT, true, STATIC, null, null, "left");
	final UnitValue CENTER = new UnitValue(50, null, PERCENT, true, STATIC, null, null, "center");
	final UnitValue TRAILING = new UnitValue(100, null, PERCENT, true, STATIC, null, null, "trailing");
	final UnitValue RIGHT = new UnitValue(100, null, PERCENT, true, STATIC, null, null, "right");
	final UnitValue BOTTOM = new UnitValue(100, null, PERCENT, false, STATIC, null, null, "bottom");
	final UnitValue LABEL = new UnitValue(0, null, LABEL_ALIGN, false, STATIC, null, null, "label");

	final UnitValue INF = new UnitValue(LayoutUtil.INF, null, PIXEL, true, STATIC, null, null, "inf");

	final UnitValue BASELINE_IDENTITY = new UnitValue(0, null, IDENTITY, false, STATIC, null, null, "baseline");
	//CHECKSTYLE:ON

	private final HashMap<String, Integer> unitMap = new HashMap<String, Integer>(32);

	private final ArrayList<UnitConverter> converteres = new ArrayList<UnitConverter>();

	public UnitValueToolkit() {
		unitMap.put("px", PIXEL);
		unitMap.put("lpx", LPX);
		unitMap.put("lpy", LPY);
		unitMap.put("%", PERCENT);
		unitMap.put("cm", CM);
		unitMap.put("in", INCH);
		unitMap.put("spx", SPX);
		unitMap.put("spy", SPY);
		unitMap.put("al", ALIGN);
		unitMap.put("mm", MM);
		unitMap.put("pt", PT);
		unitMap.put("min", MIN_SIZE);
		unitMap.put("minimum", MIN_SIZE);
		unitMap.put("p", PREF_SIZE);
		unitMap.put("pref", PREF_SIZE);
		unitMap.put("max", MAX_SIZE);
		unitMap.put("maximum", MAX_SIZE);
		unitMap.put("button", BUTTON);
		unitMap.put("label", LABEL_ALIGN);
	}

	public HashMap<String, Integer> getUnitMap() {
		return unitMap;
	}

	public ArrayList<UnitConverter> getConverters() {
		return converteres;
	}

	/**
	 * Adds a global unit converter that can convert from some <code>unit</code> to pixels.
	 * <p>
	 * This converter will be asked before the platform converter so the values for it (e.g. "related" and "unrelated") can be
	 * overridden. It is however not possible to override the built in ones (e.g. "mm", "pixel" or "lp").
	 * 
	 * @param conv The converter. Not <code>null</code>.
	 */
	public synchronized void addGlobalUnitConverter(final UnitConverter conv) {
		if (conv == null) {
			throw new NullPointerException();
		}
		converteres.add(conv);
	}

	/**
	 * Removed the converter.
	 * 
	 * @param unit The converter.
	 * @return If there was a converter found and thus removed.
	 */
	public synchronized boolean removeGlobalUnitConverter(final UnitConverter unit) {
		return converteres.remove(unit);
	}

	/**
	 * Returns the global converters currently registered. The platform converter will not be in this list.
	 * 
	 * @return The converters. Never <code>null</code>.
	 */
	public synchronized UnitConverter[] getGlobalUnitConverters() {
		return converteres.toArray(new UnitConverter[converteres.size()]);
	}

	/**
	 * Returns the current default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
	 * 
	 * @return The current default unit.
	 * @see #PIXEL
	 * @see #LPX
	 * @deprecated Use {@link PlatformDefaults#getDefaultHorizontalUnit()} and {@link PlatformDefaults#getDefaultVerticalUnit()}
	 *             instead.
	 */
	@Deprecated
	public int getDefaultUnit() {
		return MigLayoutToolkit.getMigPlatformDefaults().getDefaultHorizontalUnit();
	}

	/**
	 * Sets the default unit. The default unit is the unit used if no unit is set. E.g. "width 10".
	 * 
	 * @param unit The new default unit.
	 * @see #PIXEL
	 * @see #LPX
	 * @deprecated Use {@link PlatformDefaults#setDefaultHorizontalUnit(int)} and
	 *             {@link PlatformDefaults#setDefaultVerticalUnit(int)} instead.
	 */
	@Deprecated
	public void setDefaultUnit(final int unit) {
		MigLayoutToolkit.getMigPlatformDefaults().setDefaultHorizontalUnit(unit);
		MigLayoutToolkit.getMigPlatformDefaults().setDefaultVerticalUnit(unit);
	}

}

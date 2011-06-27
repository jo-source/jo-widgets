//CHECKSTYLE:OFF

package org.jowidgets.impl.layout.miglayout.common;

/*
 * License (BSD):
 * ==============
 *
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com)
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
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * @version 1.0
 * @author Mikael Grev, MiG InfoCom AB
 *         Date: 2006-sep-08
 */

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;

import org.jowidgets.impl.layout.miglayout.MigLayoutToolkit;

public final class UnitValue implements Serializable {

	private final transient float value;
	private final transient int unit;
	private final transient int oper;
	private final transient String unitStr;
	private transient String linkId = null; // Should be final, but initializes in a sub method.
	private final transient boolean isHor;
	private final transient UnitValue[] subUnits;

	// Pixel
	public UnitValue(final float value) // If hor/ver does not matter.
	{
		this(value, null, UnitValueToolkit.PIXEL, true, UnitValueToolkit.STATIC, null, null, value + "px");
	}

	public UnitValue(final float value, final int unit, final String createString) // If hor/ver does not matter.
	{
		this(value, null, unit, true, UnitValueToolkit.STATIC, null, null, createString);
	}

	UnitValue(final float value, final String unitStr, final boolean isHor, final int oper, final String createString) {
		this(value, unitStr, -1, isHor, oper, null, null, createString);
	}

	UnitValue(final boolean isHor, final int oper, final UnitValue sub1, final UnitValue sub2, final String createString) {
		this(0, "", -1, isHor, oper, sub1, sub2, createString);
		if (sub1 == null || sub2 == null)
			throw new IllegalArgumentException("Sub units is null!");
	}

	UnitValue(
		final float value,
		final String unitStr,
		final int unit,
		final boolean isHor,
		final int oper,
		final UnitValue sub1,
		final UnitValue sub2,
		final String createString) {
		if (oper < UnitValueToolkit.STATIC || oper > UnitValueToolkit.MID)
			throw new IllegalArgumentException("Unknown Operation: " + oper);

		if (oper >= UnitValueToolkit.ADD && oper <= UnitValueToolkit.MID && (sub1 == null || sub2 == null))
			throw new IllegalArgumentException(oper + " Operation may not have null sub-UnitValues.");

		this.value = value;
		this.oper = oper;
		this.isHor = isHor;
		this.unitStr = unitStr;
		this.unit = unitStr != null ? parseUnitString() : unit;
		this.subUnits = sub1 != null && sub2 != null ? new UnitValue[] {sub1, sub2} : null;

		final LayoutUtil layoutUtil = MigLayoutToolkit.getLayoutUtil();
		layoutUtil.putCCString(this, createString); // "this" escapes!! Safe though.
	}

	/**
	 * Returns the size in pixels rounded.
	 * 
	 * @param refValue The reference value. Normally the size of the parent. For unit {@link #ALIGN} the current size of the
	 *            component should be sent in.
	 * @param parent The parent. May be <code>null</code> for testing the validity of the value, but should normally not and are
	 *            not
	 *            required to return any usable value if <code>null</code>.
	 * @param comp The component, if any, that the value is for. Might be <code>null</code> if the value is not
	 *            connected to any component.
	 * @return The size in pixels.
	 */
	public final int getPixels(final float refValue, final ContainerWrapper parent, final ComponentWrapper comp) {
		return Math.round(getPixelsExact(refValue, parent, comp));
	}

	private static final float[] SCALE = new float[] {25.4f, 2.54f, 1f, 0f, 72f};

	/**
	 * Returns the size in pixels.
	 * 
	 * @param refValue The reference value. Normally the size of the parent. For unit {@link #ALIGN} the current size of the
	 *            component should be sent in.
	 * @param parent The parent. May be <code>null</code> for testing the validity of the value, but should normally not and are
	 *            not
	 *            required to return any usable value if <code>null</code>.
	 * @param comp The component, if any, that the value is for. Might be <code>null</code> if the value is not
	 *            connected to any component.
	 * @return The size in pixels.
	 */
	public final float getPixelsExact(final float refValue, final ContainerWrapper parent, final ComponentWrapper comp) {
		if (parent == null)
			return 1;

		if (oper == UnitValueToolkit.STATIC) {
			switch (unit) {
				case UnitValueToolkit.PIXEL:
					return value;

				case UnitValueToolkit.LPX:
				case UnitValueToolkit.LPY:
					return parent.getPixelUnitFactor(unit == UnitValueToolkit.LPX) * value;

				case UnitValueToolkit.MM:
				case UnitValueToolkit.CM:
				case UnitValueToolkit.INCH:
				case UnitValueToolkit.PT:
					float f = SCALE[unit - UnitValueToolkit.MM];
					final Float s = isHor
							? MigLayoutToolkit.getPlatformDefaults().getHorizontalScaleFactor()
							: MigLayoutToolkit.getPlatformDefaults().getVerticalScaleFactor();
					if (s != null)
						f *= s.floatValue();
					return (isHor ? parent.getHorizontalScreenDPI() : parent.getVerticalScreenDPI()) * value / f;

				case UnitValueToolkit.PERCENT:
					return value * refValue * 0.01f;

				case UnitValueToolkit.SPX:
				case UnitValueToolkit.SPY:
					return (unit == UnitValueToolkit.SPX ? parent.getScreenWidth() : parent.getScreenHeight()) * value * 0.01f;

				case UnitValueToolkit.ALIGN:
					final Integer st = MigLayoutToolkit.getLinkHandler().getValue(
							parent.getLayout(),
							"visual",
							isHor ? MigLayoutToolkit.getLinkHandler().X : MigLayoutToolkit.getLinkHandler().Y);
					final Integer sz = MigLayoutToolkit.getLinkHandler().getValue(
							parent.getLayout(),
							"visual",
							isHor ? MigLayoutToolkit.getLinkHandler().WIDTH : LinkHandler.HEIGHT);
					if (st == null || sz == null)
						return 0;
					return value * (Math.max(0, sz.intValue()) - refValue) + st.intValue();

				case UnitValueToolkit.MIN_SIZE:
					if (comp == null)
						return 0;
					return isHor ? comp.getMinimumWidth(comp.getHeight()) : comp.getMinimumHeight(comp.getWidth());

				case UnitValueToolkit.PREF_SIZE:
					if (comp == null)
						return 0;
					return isHor ? comp.getPreferredWidth(comp.getHeight()) : comp.getPreferredHeight(comp.getWidth());

				case UnitValueToolkit.MAX_SIZE:
					if (comp == null)
						return 0;
					return isHor ? comp.getMaximumWidth(comp.getHeight()) : comp.getMaximumHeight(comp.getWidth());

				case UnitValueToolkit.BUTTON:
					return MigLayoutToolkit.getPlatformDefaults().getMinimumButtonWidth().getPixels(refValue, parent, comp);

				case UnitValueToolkit.LINK_X:
				case UnitValueToolkit.LINK_Y:
				case UnitValueToolkit.LINK_W:
				case UnitValueToolkit.LINK_H:
				case UnitValueToolkit.LINK_X2:
				case UnitValueToolkit.LINK_Y2:
				case UnitValueToolkit.LINK_XPOS:
				case UnitValueToolkit.LINK_YPOS:
					final Integer v = MigLayoutToolkit.getLinkHandler().getValue(
							parent.getLayout(),
							getLinkTargetId(),
							unit - (unit >= UnitValueToolkit.LINK_XPOS ? UnitValueToolkit.LINK_XPOS : UnitValueToolkit.LINK_X));
					if (v == null)
						return 0;

					if (unit == UnitValueToolkit.LINK_XPOS)
						return parent.getScreenLocationX() + v.intValue();
					if (unit == UnitValueToolkit.LINK_YPOS)
						return parent.getScreenLocationY() + v.intValue();

					return v.intValue();

				case UnitValueToolkit.LOOKUP:
					final float res = lookup(refValue, parent, comp);
					if (res != UnitConverter.UNABLE)
						return res;

				case UnitValueToolkit.LABEL_ALIGN:
					return MigLayoutToolkit.getPlatformDefaults().getLabelAlignPercentage() * refValue;

				case UnitValueToolkit.IDENTITY:
			}
			throw new IllegalArgumentException("Unknown/illegal unit: " + unit + ", unitStr: " + unitStr);
		}

		if (subUnits != null && subUnits.length == 2) {
			final float r1 = subUnits[0].getPixelsExact(refValue, parent, comp);
			final float r2 = subUnits[1].getPixelsExact(refValue, parent, comp);
			switch (oper) {
				case UnitValueToolkit.ADD:
					return r1 + r2;
				case UnitValueToolkit.SUB:
					return r1 - r2;
				case UnitValueToolkit.MUL:
					return r1 * r2;
				case UnitValueToolkit.DIV:
					return r1 / r2;
				case UnitValueToolkit.MIN:
					return r1 < r2 ? r1 : r2;
				case UnitValueToolkit.MAX:
					return r1 > r2 ? r1 : r2;
				case UnitValueToolkit.MID:
					return (r1 + r2) * 0.5f;
			}
		}

		throw new IllegalArgumentException("Internal: Unknown Oper: " + oper);
	}

	private float lookup(final float refValue, final ContainerWrapper parent, final ComponentWrapper comp) {
		float res = UnitConverter.UNABLE;
		final ArrayList<UnitConverter> converters = MigLayoutToolkit.getUnitValueToolkit().getConverters();
		for (int i = converters.size() - 1; i >= 0; i--) {
			res = converters.get(i).convertToPixels(value, unitStr, isHor, refValue, parent, comp);
			if (res != UnitConverter.UNABLE)
				return res;
		}
		return MigLayoutToolkit.getPlatformDefaults().convertToPixels(value, unitStr, isHor, refValue, parent, comp);
	}

	private int parseUnitString() {
		final int len = unitStr.length();
		if (len == 0)
			return isHor
					? MigLayoutToolkit.getPlatformDefaults().getDefaultHorizontalUnit()
					: MigLayoutToolkit.getPlatformDefaults().getDefaultVerticalUnit();

		final Integer u = MigLayoutToolkit.getUnitValueToolkit().getUnitMap().get(unitStr);
		if (u != null)
			return u.intValue();

		if (unitStr.equals("lp"))
			return isHor ? UnitValueToolkit.LPX : UnitValueToolkit.LPY;

		if (unitStr.equals("sp"))
			return isHor ? UnitValueToolkit.SPX : UnitValueToolkit.SPY;

		if (lookup(0, null, null) != UnitConverter.UNABLE) // To test so we can fail fast
			return UnitValueToolkit.LOOKUP;

		// Only link left. E.g. "otherID.width"

		final int pIx = unitStr.indexOf('.');
		if (pIx != -1) {
			linkId = unitStr.substring(0, pIx);
			final String e = unitStr.substring(pIx + 1);

			if (e.equals("x"))
				return UnitValueToolkit.LINK_X;
			if (e.equals("y"))
				return UnitValueToolkit.LINK_Y;
			if (e.equals("w") || e.equals("width"))
				return UnitValueToolkit.LINK_W;
			if (e.equals("h") || e.equals("height"))
				return UnitValueToolkit.LINK_H;
			if (e.equals("x2"))
				return UnitValueToolkit.LINK_X2;
			if (e.equals("y2"))
				return UnitValueToolkit.LINK_Y2;
			if (e.equals("xpos"))
				return UnitValueToolkit.LINK_XPOS;
			if (e.equals("ypos"))
				return UnitValueToolkit.LINK_YPOS;
		}

		throw new IllegalArgumentException("Unknown keyword: " + unitStr);
	}

	final boolean isLinked() {
		return linkId != null;
	}

	final boolean isLinkedDeep() {
		if (subUnits == null)
			return linkId != null;

		for (int i = 0; i < subUnits.length; i++) {
			if (subUnits[i].isLinkedDeep())
				return true;
		}

		return false;
	}

	final String getLinkTargetId() {
		return linkId;
	}

	final UnitValue getSubUnitValue(final int i) {
		return subUnits[i];
	}

	final int getSubUnitCount() {
		return subUnits != null ? subUnits.length : 0;
	}

	public final UnitValue[] getSubUnits() {
		return subUnits != null ? subUnits.clone() : null;
	}

	public final int getUnit() {
		return unit;
	}

	public final String getUnitString() {
		return unitStr;
	}

	public final int getOperation() {
		return oper;
	}

	public final float getValue() {
		return value;
	}

	public final boolean isHorizontal() {
		return isHor;
	}

	@Override
	final public String toString() {
		return getClass().getName()
			+ ". Value="
			+ value
			+ ", unit="
			+ unit
			+ ", unitString: "
			+ unitStr
			+ ", oper="
			+ oper
			+ ", isHor: "
			+ isHor;
	}

	/**
	 * Returns the creation string for this object. Note that {@link LayoutUtil#setDesignTime(ContainerWrapper, boolean)} must be
	 * set to <code>true</code> for the creation strings to be stored.
	 * 
	 * @return The constraint string or <code>null</code> if none is registered.
	 */
	public final String getConstraintString() {
		final LayoutUtil layoutUtil = MigLayoutToolkit.getLayoutUtil();
		return layoutUtil.getCCString(this);
	}

	@Override
	public final int hashCode() {
		return (int) (value * 12345) + (oper >>> 5) + unit >>> 17;
	}

	static {
		final LayoutUtil layoutUtil = MigLayoutToolkit.getLayoutUtil();
		layoutUtil.setDelegate(UnitValue.class, new PersistenceDelegate() {
			@Override
			protected Expression instantiate(final Object oldInstance, final Encoder out) {
				final UnitValue uv = (UnitValue) oldInstance;
				final String cs = uv.getConstraintString();
				if (cs == null)
					throw new IllegalStateException("Design time must be on to use XML persistence. See LayoutUtil.");

				return new Expression(oldInstance, ConstraintParser.class, "parseUnitValueOrAlign", new Object[] {
						uv.getConstraintString(), (uv.isHorizontal() ? Boolean.TRUE : Boolean.FALSE), null});
			}
		});
	}

	// ************************************************
	// Persistence Delegate and Serializable combined.
	// ************************************************

	private static final long serialVersionUID = 1L;

	private Object readResolve() throws ObjectStreamException {
		return MigLayoutToolkit.getLayoutUtil().getSerializedObject(this);
	}

	private void writeObject(final ObjectOutputStream out) throws IOException {
		if (getClass() == UnitValue.class) {
			MigLayoutToolkit.getLayoutUtil().writeAsXML(out, this);
		}
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		final LayoutUtil layoutUtil = MigLayoutToolkit.getLayoutUtil();
		layoutUtil.setSerializedObject(this, layoutUtil.readAsXML(in));
	}
}

/*
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com)
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
package org.jowidgets.impl.layout.miglayout;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;

final class UnitValueCommon implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final float[] SCALE = new float[] {25.4f, 2.54f, 1f, 0f, 72f};

    private final transient float value;
    private final transient int unit;
    private final transient int oper;
    private final transient String unitStr;
    private transient String linkId = null; // Should be final, but initializes in a sub method.
    private final transient boolean isHor;
    private final transient UnitValueCommon[] subUnits;

    // Pixel
    UnitValueCommon(final float value) // If hor/ver does not matter.
    {
        this(value, null, UnitValueToolkitCommon.PIXEL, true, UnitValueToolkitCommon.STATIC, null, null, value + "px");
    }

    UnitValueCommon(final float value, final int unit, final String createString) // If hor/ver does not matter.
    {
        this(value, null, unit, true, UnitValueToolkitCommon.STATIC, null, null, createString);
    }

    UnitValueCommon(final float value, final String unitStr, final boolean isHor, final int oper, final String createString) {
        this(value, unitStr, -1, isHor, oper, null, null, createString);
    }

    UnitValueCommon(
        final boolean isHor,
        final int oper,
        final UnitValueCommon sub1,
        final UnitValueCommon sub2,
        final String createString) {
        this(0, "", -1, isHor, oper, sub1, sub2, createString);
        if (sub1 == null || sub2 == null) {
            throw new IllegalArgumentException("Sub units is null!");
        }
    }

    UnitValueCommon(
        final float value,
        final String unitStr,
        final int unit,
        final boolean isHor,
        final int oper,
        final UnitValueCommon sub1,
        final UnitValueCommon sub2,
        final String createString) {
        if (oper < UnitValueToolkitCommon.STATIC || oper > UnitValueToolkitCommon.MID) {
            throw new IllegalArgumentException("Unknown Operation: " + oper);
        }

        if (oper >= UnitValueToolkitCommon.ADD && oper <= UnitValueToolkitCommon.MID && (sub1 == null || sub2 == null)) {
            throw new IllegalArgumentException(oper + " Operation may not have null sub-UnitValues.");
        }

        this.value = value;
        this.oper = oper;
        this.isHor = isHor;
        this.unitStr = unitStr;
        this.unit = unitStr != null ? parseUnitString() : unit;
        this.subUnits = sub1 != null && sub2 != null ? new UnitValueCommon[] {sub1, sub2} : null;

        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
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
    public int getPixels(final float refValue, final IContainerWrapperCommon parent, final IComponentWrapperCommon comp) {
        return Math.round(getPixelsExact(refValue, parent, comp));
    }

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
    public float getPixelsExact(final float refValue, final IContainerWrapperCommon parent, final IComponentWrapperCommon comp) {
        if (parent == null) {
            return 1;
        }

        if (oper == UnitValueToolkitCommon.STATIC) {
            switch (unit) {
                case UnitValueToolkitCommon.PIXEL:
                    return value;

                case UnitValueToolkitCommon.LPX:
                case UnitValueToolkitCommon.LPY:
                    return parent.getPixelUnitFactor(unit == UnitValueToolkitCommon.LPX) * value;

                case UnitValueToolkitCommon.MM:
                case UnitValueToolkitCommon.CM:
                case UnitValueToolkitCommon.INCH:
                case UnitValueToolkitCommon.PT:
                    float f = SCALE[unit - UnitValueToolkitCommon.MM];
                    final Float s = isHor
                            ? MigLayoutToolkitImpl.getMigPlatformDefaults().getHorizontalScaleFactor()
                            : MigLayoutToolkitImpl.getMigPlatformDefaults().getVerticalScaleFactor();
                    if (s != null) {
                        f *= s;
                    }
                    return (isHor ? parent.getHorizontalScreenDPI() : parent.getVerticalScreenDPI()) * value / f;

                case UnitValueToolkitCommon.PERCENT:
                    return value * refValue * 0.01f;

                case UnitValueToolkitCommon.SPX:
                case UnitValueToolkitCommon.SPY:
                    return (unit == UnitValueToolkitCommon.SPX ? parent.getScreenWidth() : parent.getScreenHeight())
                        * value
                        * 0.01f;

                case UnitValueToolkitCommon.ALIGN:
                    final Integer st = MigLayoutToolkitImpl.getMigLinkHandler()
                            .getValue(parent.getLayout(), "visual", isHor ? LinkHandlerCommon.X : LinkHandlerCommon.Y);
                    final Integer sz = MigLayoutToolkitImpl.getMigLinkHandler()
                            .getValue(parent.getLayout(), "visual", isHor ? LinkHandlerCommon.WIDTH : LinkHandlerCommon.HEIGHT);
                    if (st == null || sz == null) {
                        return 0;
                    }
                    return value * (Math.max(0, sz) - refValue) + st;

                case UnitValueToolkitCommon.MIN_SIZE:
                    if (comp == null) {
                        return 0;
                    }
                    return isHor ? comp.getMinimumWidth(comp.getHeight()) : comp.getMinimumHeight(comp.getWidth());

                case UnitValueToolkitCommon.PREF_SIZE:
                    if (comp == null) {
                        return 0;
                    }
                    return isHor ? comp.getPreferredWidth(comp.getHeight()) : comp.getPreferredHeight(comp.getWidth());

                case UnitValueToolkitCommon.MAX_SIZE:
                    if (comp == null) {
                        return 0;
                    }
                    return isHor ? comp.getMaximumWidth(comp.getHeight()) : comp.getMaximumHeight(comp.getWidth());

                case UnitValueToolkitCommon.BUTTON:
                    return MigLayoutToolkitImpl.getMigPlatformDefaults()
                            .getMinimumButtonWidth()
                            .getPixels(refValue, parent, comp);

                case UnitValueToolkitCommon.LINK_X:
                case UnitValueToolkitCommon.LINK_Y:
                case UnitValueToolkitCommon.LINK_W:
                case UnitValueToolkitCommon.LINK_H:
                case UnitValueToolkitCommon.LINK_X2:
                case UnitValueToolkitCommon.LINK_Y2:
                case UnitValueToolkitCommon.LINK_XPOS:
                case UnitValueToolkitCommon.LINK_YPOS:
                    final Integer v = MigLayoutToolkitImpl.getMigLinkHandler()
                            .getValue(parent.getLayout(), getLinkTargetId(), unit
                                - (unit >= UnitValueToolkitCommon.LINK_XPOS
                                        ? UnitValueToolkitCommon.LINK_XPOS : UnitValueToolkitCommon.LINK_X));
                    if (v == null) {
                        return 0;
                    }

                    if (unit == UnitValueToolkitCommon.LINK_XPOS) {
                        return parent.getScreenLocationX() + v;
                    }
                    if (unit == UnitValueToolkitCommon.LINK_YPOS) {
                        return parent.getScreenLocationY() + v;
                    }

                    return v;

                case UnitValueToolkitCommon.LOOKUP:
                    final float res = lookup(refValue, parent, comp);
                    if (res != UnitConverterCommon.UNABLE) {
                        return res;
                    }

                    //CHECKSTYLE:OFF
                    //Fall through is not possible because unit may not be LOOKUP and LABEL_ALIGN at the same time
                case UnitValueToolkitCommon.LABEL_ALIGN:
                    return MigLayoutToolkitImpl.getMigPlatformDefaults().getLabelAlignPercentage() * refValue;
                //CHECKSTYLE:ON

                case UnitValueToolkitCommon.IDENTITY:
                default:
                    break;
            }
            throw new IllegalArgumentException("Unknown/illegal unit: " + unit + ", unitStr: " + unitStr);
        }

        if (subUnits != null && subUnits.length == 2) {
            final float r1 = subUnits[0].getPixelsExact(refValue, parent, comp);
            final float r2 = subUnits[1].getPixelsExact(refValue, parent, comp);
            switch (oper) {
                case UnitValueToolkitCommon.ADD:
                    return r1 + r2;
                case UnitValueToolkitCommon.SUB:
                    return r1 - r2;
                case UnitValueToolkitCommon.MUL:
                    return r1 * r2;
                case UnitValueToolkitCommon.DIV:
                    return r1 / r2;
                case UnitValueToolkitCommon.MIN:
                    return r1 < r2 ? r1 : r2;
                case UnitValueToolkitCommon.MAX:
                    return r1 > r2 ? r1 : r2;
                case UnitValueToolkitCommon.MID:
                    return (r1 + r2) * 0.5f;
                default:
                    break;
            }
        }

        throw new IllegalArgumentException("Internal: Unknown Oper: " + oper);
    }

    private float lookup(final float refValue, final IContainerWrapperCommon parent, final IComponentWrapperCommon comp) {
        float res = UnitConverterCommon.UNABLE;
        final ArrayList<UnitConverterCommon> converters = MigLayoutToolkitImpl.getMigUnitValueToolkit().getConverters();
        for (int i = converters.size() - 1; i >= 0; i--) {
            res = converters.get(i).convertToPixels(value, unitStr, isHor, refValue, parent, comp);
            if (res != UnitConverterCommon.UNABLE) {
                return res;
            }
        }
        return MigLayoutToolkitImpl.getMigPlatformDefaults().convertToPixels(value, unitStr, isHor, refValue, parent, comp);
    }

    private int parseUnitString() {
        final int len = unitStr.length();
        if (len == 0) {
            return isHor
                    ? MigLayoutToolkitImpl.getMigPlatformDefaults().getDefaultHorizontalUnit()
                    : MigLayoutToolkitImpl.getMigPlatformDefaults().getDefaultVerticalUnit();
        }

        final Integer u = MigLayoutToolkitImpl.getMigUnitValueToolkit().getUnitMap().get(unitStr);
        if (u != null) {
            return u;
        }

        if (unitStr.equals("lp")) {
            return isHor ? UnitValueToolkitCommon.LPX : UnitValueToolkitCommon.LPY;
        }

        if (unitStr.equals("sp")) {
            return isHor ? UnitValueToolkitCommon.SPX : UnitValueToolkitCommon.SPY;
        }

        if (lookup(0, null, null) != UnitConverterCommon.UNABLE) {
            return UnitValueToolkitCommon.LOOKUP;
        }

        // Only link left. E.g. "otherID.width"

        final int pIx = unitStr.indexOf('.');
        if (pIx != -1) {
            linkId = unitStr.substring(0, pIx);
            final String e = unitStr.substring(pIx + 1);

            if (e.equals("x")) {
                return UnitValueToolkitCommon.LINK_X;
            }
            if (e.equals("y")) {
                return UnitValueToolkitCommon.LINK_Y;
            }
            if (e.equals("w") || e.equals("width")) {
                return UnitValueToolkitCommon.LINK_W;
            }
            if (e.equals("h") || e.equals("height")) {
                return UnitValueToolkitCommon.LINK_H;
            }
            if (e.equals("x2")) {
                return UnitValueToolkitCommon.LINK_X2;
            }
            if (e.equals("y2")) {
                return UnitValueToolkitCommon.LINK_Y2;
            }
            if (e.equals("xpos")) {
                return UnitValueToolkitCommon.LINK_XPOS;
            }
            if (e.equals("ypos")) {
                return UnitValueToolkitCommon.LINK_YPOS;
            }
        }

        throw new IllegalArgumentException("Unknown keyword: " + unitStr);
    }

    boolean isLinked() {
        return linkId != null;
    }

    boolean isLinkedDeep() {
        if (subUnits == null) {
            return linkId != null;
        }

        for (final UnitValueCommon subUnit : subUnits) {
            if (subUnit.isLinkedDeep()) {
                return true;
            }
        }

        return false;
    }

    String getLinkTargetId() {
        return linkId;
    }

    UnitValueCommon getSubUnitValue(final int i) {
        return subUnits[i];
    }

    int getSubUnitCount() {
        return subUnits != null ? subUnits.length : 0;
    }

    UnitValueCommon[] getSubUnits() {
        return subUnits != null ? subUnits.clone() : null;
    }

    int getUnit() {
        return unit;
    }

    String getUnitString() {
        return unitStr;
    }

    int getOperation() {
        return oper;
    }

    float getValue() {
        return value;
    }

    boolean isHorizontal() {
        return isHor;
    }

    @Override
    public String toString() {
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
     * Returns the creation string for this object. Note that
     * {@link LayoutUtilCommon#setDesignTime(IContainerWrapperCommon, boolean)} must be
     * set to <code>true</code> for the creation strings to be stored.
     * 
     * @return The constraint string or <code>null</code> if none is registered.
     */
    public String getConstraintString() {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        return layoutUtil.getCCString(this);
    }

    @Override
    public int hashCode() {
        return (int) (value * 12345) + (oper >>> 5) + unit >>> 17;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    static {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        if (layoutUtil.hasBeans()) {
            layoutUtil.setDelegate(UnitValueCommon.class, new PersistenceDelegate() {
                @Override
                protected Expression instantiate(final Object oldInstance, final Encoder out) {
                    final UnitValueCommon uv = (UnitValueCommon) oldInstance;
                    final String cs = uv.getConstraintString();
                    if (cs == null) {
                        throw new IllegalStateException("Design time must be on to use XML persistence. See LayoutUtil.");
                    }

                    return new Expression(
                        oldInstance,
                        ConstraintParserCommon.class,
                        "parseUnitValueOrAlign",
                        new Object[] {uv.getConstraintString(), (uv.isHorizontal() ? Boolean.TRUE : Boolean.FALSE), null});
                }
            });
        }
    }

    // ************************************************
    // Persistence Delegate and Serializable combined.
    // ************************************************

    private Object readResolve() throws ObjectStreamException {
        return MigLayoutToolkitImpl.getMigLayoutUtil().getSerializedObject(this);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        if (getClass() == UnitValueCommon.class) {
            MigLayoutToolkitImpl.getMigLayoutUtil().writeAsXML(out, this);
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        layoutUtil.setSerializedObject(this, layoutUtil.readAsXML(in));
    }
}

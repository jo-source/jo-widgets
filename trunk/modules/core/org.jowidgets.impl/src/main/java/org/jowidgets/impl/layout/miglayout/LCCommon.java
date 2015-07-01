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
 */
package org.jowidgets.impl.layout.miglayout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

/**
 * Contains the constraints for an instance of the {@link LCCommon} layout manager.
 */
final class LCCommon implements Externalizable {
    // See the corresponding set/get method for documentation of the property!

    private int wrapAfter = LayoutUtilCommon.INF;

    private Boolean leftToRight = null;

    private UnitValueCommon[] insets = null; // Never null elememts but if unset array is null

    private UnitValueCommon alignX = null;
    private UnitValueCommon alignY = null;

    private BoundSizeCommon gridGapX = null;
    private BoundSizeCommon gridGapY = null;

    private BoundSizeCommon width = BoundSizeCommon.NULL_SIZE;
    private BoundSizeCommon height = BoundSizeCommon.NULL_SIZE;

    private BoundSizeCommon packW = BoundSizeCommon.NULL_SIZE;
    private BoundSizeCommon packH = BoundSizeCommon.NULL_SIZE;

    private float pwAlign = 0.5f;
    private float phAlign = 1.0f;

    private int debugMillis = 0;

    private int hideMode = 0;

    private boolean noCache = false;

    private boolean flowX = true;

    private boolean fillX = false;
    private boolean fillY = false;

    private boolean topToBottom = true;

    private boolean noGrid = false;

    private boolean visualPadding = true;

    /**
     * Empty constructor.
     */
    public LCCommon() {}

    // ************************************************************************
    // * JavaBean get/set methods.
    // ************************************************************************

    /**
     * If components have sizes or positions linked to the bounds of the parent in some way (as for instance the <code>"%"</code>
     * unit has) the cache
     * must be turned off for the panel. If components does not get the correct or expected size or position try to set this
     * property to <code>true</code>.
     * 
     * @return <code>true</code> means no cache and slightly slower layout.
     */
    public boolean isNoCache() {
        return noCache;
    }

    /**
     * If components have sizes or positions linked to the bounds of the parent in some way (as for instance the <code>"%"</code>
     * unit has) the cache
     * must be turned off for the panel. If components does not get the correct or expected size or position try to set this
     * property to <code>true</code>.
     * 
     * @param b <code>true</code> means no cache and slightly slower layout.
     */
    public void setNoCache(final boolean b) {
        this.noCache = b;
    }

    /**
     * If the laid out components' bounds in total is less than the final size of the container these align values will be used to
     * align the components
     * in the parent. <code>null</code> is default and that means top/left alignment. The relative distances between the
     * components will not be affected
     * by this property.
     * 
     * @return The current alignment.
     */
    public UnitValueCommon getAlignX() {
        return alignX;
    }

    /**
     * If the laid out components' bounds in total is less than the final size of the container these align values will be used to
     * align the components
     * in the parent. <code>null</code> is default and that means top/left alignment. The relative distances between the
     * components will not be affected
     * by this property.
     * 
     * @param uv The new alignment. Use {@link ConstraintParserCommon#parseAlignKeywords(String, boolean)} to create the
     *            {@link UnitValueCommon}. May be <code>null</code>.
     */
    public void setAlignX(final UnitValueCommon uv) {
        this.alignX = uv;
    }

    /**
     * If the laid out components' bounds in total is less than the final size of the container these align values will be used to
     * align the components
     * in the parent. <code>null</code> is default and that means top/left alignment. The relative distances between the
     * components will not be affected
     * by this property.
     * 
     * @return The current alignment.
     */
    public UnitValueCommon getAlignY() {
        return alignY;
    }

    /**
     * If the laid out components' bounds in total is less than the final size of the container these align values will be used to
     * align the components
     * in the parent. <code>null</code> is default and that means top/left alignment. The relative distances between the
     * components will not be affected
     * by this property.
     * 
     * @param uv The new alignment. Use {@link ConstraintParserCommon#parseAlignKeywords(String, boolean)} to create the
     *            {@link UnitValueCommon}. May be <code>null</code>.
     */
    public void setAlignY(final UnitValueCommon uv) {
        this.alignY = uv;
    }

    /**
     * If <code>&gt; 0</code> the debug decorations will be repainted every <code>millis</code>. No debug information if
     * <code>&lt;= 0</code> (default).
     * 
     * @return The current debug repaint interval.
     */
    public int getDebugMillis() {
        return debugMillis;
    }

    /**
     * If <code>&gt; 0</code> the debug decorations will be repainted every <code>millis</code>. No debug information if
     * <code>&lt;= 0</code> (default).
     * 
     * @param millis The new debug repaint interval.
     */
    public void setDebugMillis(final int millis) {
        this.debugMillis = millis;
    }

    /**
     * If the layout should always claim the whole bounds of the laid out container even if the preferred size is smaller.
     * 
     * @return <code>true</code> means fill. <code>false</code> is default.
     */
    public boolean isFillX() {
        return fillX;
    }

    /**
     * If the layout should always claim the whole bounds of the laid out container even if the preferred size is smaller.
     * 
     * @param b <code>true</code> means fill. <code>false</code> is default.
     */
    public void setFillX(final boolean b) {
        this.fillX = b;
    }

    /**
     * If the layout should always claim the whole bounds of the laid out container even if the preferred size is smaller.
     * 
     * @return <code>true</code> means fill. <code>false</code> is default.
     */
    public boolean isFillY() {
        return fillY;
    }

    /**
     * If the layout should always claim the whole bounds of the laid out container even if the preferred size is smaller.
     * 
     * @param b <code>true</code> means fill. <code>false</code> is default.
     */
    public void setFillY(final boolean b) {
        this.fillY = b;
    }

    /**
     * The default flow direction. Normally (which is <code>true</code>) this is horizontal and that means that the "next"
     * component
     * will be put in the cell to the right (or to the left if left-to-right is false).
     * 
     * @return <code>true</code> is the default flow horizontally.
     * @see #setLeftToRight(Boolean)
     */
    public boolean isFlowX() {
        return flowX;
    }

    /**
     * The default flow direction. Normally (which is <code>true</code>) this is horizontal and that means that the "next"
     * component
     * will be put in the cell to the right (or to the left if left-to-right is false).
     * 
     * @param b <code>true</code> is the default flow horizontally.
     * @see #setLeftToRight(Boolean)
     */
    public void setFlowX(final boolean b) {
        this.flowX = b;
    }

    /**
     * If non-<code>null</code> (<code>null</code> is default) these value will be used as the default gaps between the columns in
     * the grid.
     * 
     * @return The default grid gap between columns in the grid. <code>null</code> if the platform default is used.
     */
    public BoundSizeCommon getGridGapX() {
        return gridGapX;
    }

    /**
     * If non-<code>null</code> (<code>null</code> is default) these value will be used as the default gaps between the columns in
     * the grid.
     * 
     * @param x The default grid gap between columns in the grid. If <code>null</code> the platform default is used.
     */
    public void setGridGapX(final BoundSizeCommon x) {
        this.gridGapX = x;
    }

    /**
     * If non-<code>null</code> (<code>null</code> is default) these value will be used as the default gaps between the rows in
     * the grid.
     * 
     * @return The default grid gap between rows in the grid. <code>null</code> if the platform default is used.
     */
    public BoundSizeCommon getGridGapY() {
        return gridGapY;
    }

    /**
     * If non-<code>null</code> (<code>null</code> is default) these value will be used as the default gaps between the rows in
     * the grid.
     * 
     * @param y The default grid gap between rows in the grid. If <code>null</code> the platform default is used.
     */
    public void setGridGapY(final BoundSizeCommon y) {
        this.gridGapY = y;
    }

    /**
     * How a component that is hidden (not visible) should be treated by default.
     * 
     * @return The mode:<br>
     *         0 == Normal. Bounds will be calculated as if the component was visible.<br>
     *         1 == If hidden the size will be 0, 0 but the gaps remain.<br>
     *         2 == If hidden the size will be 0, 0 and gaps set to zero.<br>
     *         3 == If hidden the component will be disregarded completely and not take up a cell in the grid..
     */
    public int getHideMode() {
        return hideMode;
    }

    /**
     * How a component that is hidden (not visible) should be treated.
     * 
     * @param mode The mode:<br>
     *            0 == Normal. Bounds will be calculated as if the component was visible.<br>
     *            1 == If hidden the size will be 0, 0 but the gaps remain.<br>
     *            2 == If hidden the size will be 0, 0 and gaps set to zero.<br>
     *            3 == If hidden the component will be disregarded completely and not take up a cell in the grid..
     */
    public void setHideMode(final int mode) {
        if (mode < 0 || mode > 3) {
            throw new IllegalArgumentException("Wrong hideMode: " + mode);
        }

        this.hideMode = mode;
    }

    /**
     * The insets for the layed out panel. The insets will be an empty space around the components in the panel. <code>null</code>
     * values
     * means that the default panel insets for the platform is used. See
     * {@link PlatformDefaultsCommon#setDialogInsets(net.miginfocom.layout.UnitValueCommon, net.miginfocom.layout.UnitValueCommon, net.miginfocom.layout.UnitValueCommon, net.miginfocom.layout.UnitValueCommon)}
     * .
     * 
     * @return The insets. Of length 4 (top, left, bottom, right) or <code>null</code>. The elements (1 to 4) may be
     *         <code>null</code>. The array is a copy and can be used freely.
     * @see net.miginfocom.layout.ConstraintParserCommon#parseInsets(String, boolean)
     */
    public UnitValueCommon[] getInsets() {
        return insets != null ? new UnitValueCommon[] {insets[0], insets[1], insets[2], insets[3]} : null;
    }

    /**
     * The insets for the layed out panel. The insets will be an empty space around the components in the panel. <code>null</code>
     * values
     * means that the default panel insets for the platform is used. See
     * {@link PlatformDefaultsCommon#setDialogInsets(net.miginfocom.layout.UnitValueCommon, net.miginfocom.layout.UnitValueCommon, net.miginfocom.layout.UnitValueCommon, net.miginfocom.layout.UnitValueCommon)}
     * .
     * 
     * @param ins The new insets. Must be of length 4 (top, left, bottom, right) or <code>null</code>. The elements (1 to 4) may
     *            be <code>null</code> to use
     *            the platform default for that side. The array is copied for storage.
     * @see net.miginfocom.layout.ConstraintParserCommon#parseInsets(String, boolean)
     */
    public void setInsets(final UnitValueCommon[] ins) {
        this.insets = ins != null ? new UnitValueCommon[] {ins[0], ins[1], ins[2], ins[3]} : null;
    }

    /**
     * If the layout should be forced to be left-to-right or right-to-left. A value of <code>null</code> is default and
     * means that this will be picked up from the {@link java.util.Locale} that the container being layed out is reporting.
     * 
     * @return <code>Boolean.TRUE</code> if force left-to-right. <code>Boolean.FALSE</code> if force tight-to-left.
     *         <code>null</code> for the default "let the current Locale decide".
     */
    public Boolean getLeftToRight() {
        return leftToRight;
    }

    /**
     * If the layout should be forced to be left-to-right or right-to-left. A value of <code>null</code> is default and
     * means that this will be picked up from the {@link java.util.Locale} that the container being layed out is reporting.
     * 
     * @param b <code>Boolean.TRUE</code> to force left-to-right. <code>Boolean.FALSE</code> to force tight-to-left.
     *            <code>null</code> for the default "let the current Locale decide".
     */
    public void setLeftToRight(final Boolean b) {
        this.leftToRight = b;
    }

    /**
     * If the whole layout should be non grid based. It is the same as setting the "nogrid" property on every row/column in the
     * grid.
     * 
     * @return <code>true</code> means not grid based. <code>false</code> is default.
     */
    public boolean isNoGrid() {
        return noGrid;
    }

    /**
     * If the whole layout should be non grid based. It is the same as setting the "nogrid" property on every row/column in the
     * grid.
     * 
     * @param b <code>true</code> means no grid. <code>false</code> is default.
     */
    public void setNoGrid(final boolean b) {
        this.noGrid = b;
    }

    /**
     * If the layout should go from the default top-to-bottom in the grid instead of the optinal bottom-to-top.
     * 
     * @return <code>true</code> for the default top-to-bottom.
     */
    public boolean isTopToBottom() {
        return topToBottom;
    }

    /**
     * If the layout should go from the default top-to-bottom in the grid instead of the optinal bottom-to-top.
     * 
     * @param b <code>true</code> for the default top-to-bottom.
     */
    public void setTopToBottom(final boolean b) {
        this.topToBottom = b;
    }

    /**
     * If visual padding should be automatically used and compensated for by this layout instance.
     * 
     * @return <code>true</code> if visual padding.
     */
    public boolean isVisualPadding() {
        return visualPadding;
    }

    /**
     * If visual padding should be automatically used and compensated for by this layout instance.
     * 
     * @param b <code>true</code> turns on visual padding.
     */
    public void setVisualPadding(final boolean b) {
        this.visualPadding = b;
    }

    /**
     * Returns after what cell the grid should always auto wrap.
     * 
     * @return After what cell the grid should always auto wrap. If <code>0</code> the number of columns/rows in the
     *         {@link net.miginfocom.layout.ACCommon} is used. <code>LayoutUtil.INF</code> is used for no auto wrap.
     */
    public int getWrapAfter() {
        return wrapAfter;
    }

    /**
     * Sets after what cell the grid should always auto wrap.
     * 
     * @param count After what cell the grid should always auto wrap. If <code>0</code> the number of columns/rows in the
     *            {@link net.miginfocom.layout.ACCommon} is used. <code>LayoutUtil.INF</code> is used for no auto wrap.
     */
    public void setWrapAfter(final int count) {
        this.wrapAfter = count;
    }

    /**
     * Returns the "pack width" for the <b>window</b> that this container is located in. When the size of this container changes
     * the size of the window will be corrected to be within this BoundsSize. It can be used to set the minimum and/or maximum
     * size of the window
     * as well as the size window should optimally get. This optimal size is normaly its "preferred" size which is why "preferred"
     * is the normal value to set here.
     * <p>
     * ":push" can be appended to the bound size to only push the size bigger and never shrink it if the preferred size gets
     * smaller.
     * <p>
     * E.g. "pref", "100:pref", "pref:700", "300::700", "pref:push"
     * 
     * @return The current value. Never <code>null</code>. Check if not set with <code>.isUnset()</code>.
     * @since 3.5
     */
    public BoundSizeCommon getPackWidth() {
        return packW;
    }

    /**
     * Sets the "pack width" for the <b>window</b> that this container is located in. When the size of this container changes
     * the size of the window will be corrected to be within this BoundsSize. It can be used to set the minimum and/or maximum
     * size of the window
     * as well as the size window should optimally get. This optimal size is normaly its "preferred" size which is why "preferred"
     * is the normal value to set here.
     * <p>
     * ":push" can be appended to the bound size to only push the size bigger and never shrink it if the preferred size gets
     * smaller.
     * <p>
     * E.g. "pref", "100:pref", "pref:700", "300::700", "pref:push"
     * 
     * @param size The new pack size. If <code>null</code> it will be corrected to an "unset" BoundSize.
     * @since 3.5
     */
    public void setPackWidth(final BoundSizeCommon size) {
        packW = size != null ? size : BoundSizeCommon.NULL_SIZE;
    }

    /**
     * Returns the "pack height" for the <b>window</b> that this container is located in. When the size of this container changes
     * the size of the window will be corrected to be within this BoundsSize. It can be used to set the minimum and/or maximum
     * size of the window
     * as well as the size window should optimally get. This optimal size is normaly its "preferred" size which is why "preferred"
     * is the normal value to set here.
     * <p>
     * ":push" can be appended to the bound size to only push the size bigger and never shrink it if the preferred size gets
     * smaller.
     * <p>
     * E.g. "pref", "100:pref", "pref:700", "300::700", "pref:push"
     * 
     * @return The current value. Never <code>null</code>. Check if not set with <code>.isUnset()</code>.
     * @since 3.5
     */
    public BoundSizeCommon getPackHeight() {
        return packH;
    }

    /**
     * Sets the "pack height" for the <b>window</b> that this container is located in. When the size of this container changes
     * the size of the window will be corrected to be within this BoundsSize. It can be used to set the minimum and/or maximum
     * size of the window
     * as well as the size window should optimally get. This optimal size is normaly its "preferred" size which is why "preferred"
     * is the normal value to set here.
     * <p>
     * ":push" can be appended to the bound size to only push the size bigger and never shrink it if the preferred size gets
     * smaller.
     * <p>
     * E.g. "pref", "100:pref", "pref:700", "300::700", "pref:push"
     * 
     * @param size The new pack size. If <code>null</code> it will be corrected to an "unset" BoundSize.
     * @since 3.5
     */
    public void setPackHeight(final BoundSizeCommon size) {
        packH = size != null ? size : BoundSizeCommon.NULL_SIZE;
    }

    /**
     * If there is a resize of the window due to packing (see {@link #setPackHeight(BoundSizeCommon)} this value, which is between
     * 0f
     * and 1f,
     * decides where the extra/surpurflous size is placed. 0f means that the window will resize so that the upper part moves up
     * and the
     * lower side stays in the same place. 0.5f will expand/reduce the window equally upwards and downwards. 1f will do the
     * opposite of 0f
     * of course.
     * 
     * @return The pack alignment. Always between 0f and 1f, inclusive.
     * @since 3.5
     */
    public float getPackHeightAlign() {
        return phAlign;
    }

    /**
     * If there is a resize of the window due to packing (see {@link #setPackHeight(BoundSizeCommon)} this value, which is between
     * 0f
     * and 1f,
     * decides where the extra/surpurflous size is placed. 0f means that the window will resize so that the upper part moves up
     * and the
     * lower side stays in the same place. 0.5f will expand/reduce the window equally upwards and downwards. 1f will do the
     * opposite of 0f
     * of course.
     * 
     * @param align The pack alignment. Always between 0f and 1f, inclusive. Values outside this will be truncated.
     * @since 3.5
     */
    public void setPackHeightAlign(final float align) {
        phAlign = Math.max(0f, Math.min(1f, align));
    }

    /**
     * If there is a resize of the window due to packing (see {@link #setPackHeight(BoundSizeCommon)} this value, which is between
     * 0f
     * and 1f,
     * decides where the extra/surpurflous size is placed. 0f means that the window will resize so that the left part moves left
     * and the
     * right side stays in the same place. 0.5f will expand/reduce the window equally to the right and lefts. 1f will do the
     * opposite of 0f
     * of course.
     * 
     * @return The pack alignment. Always between 0f and 1f, inclusive.
     * @since 3.5
     */
    public float getPackWidthAlign() {
        return pwAlign;
    }

    /**
     * If there is a resize of the window due to packing (see {@link #setPackHeight(BoundSizeCommon)} this value, which is between
     * 0f
     * and 1f,
     * decides where the extra/surpurflous size is placed. 0f means that the window will resize so that the left part moves left
     * and the
     * right side stays in the same place. 0.5f will expand/reduce the window equally to the right and lefts. 1f will do the
     * opposite of 0f
     * of course.
     * 
     * @param align The pack alignment. Always between 0f and 1f, inclusive. Values outside this will be truncated.
     * @since 3.5
     */
    public void setPackWidthAlign(final float align) {
        pwAlign = Math.max(0f, Math.min(1f, align));
    }

    /**
     * Returns the minimum/preferred/maximum size for the container that this layout constraint is set for. Any of these
     * sizes that is not <code>null</code> will be returned directly instead of determining the correspondig size through
     * asking the components in this container.
     * 
     * @return The width for the container that this layout constraint is set for. Not <code>null</code> but
     *         all sizes can be <code>null</code>.
     * @since 3.5
     */
    public BoundSizeCommon getWidth() {
        return width;
    }

    /**
     * Sets the minimum/preferred/maximum size for the container that this layout constraint is set for. Any of these
     * sizes that is not <code>null</code> will be returned directly instead of determining the correspondig size through
     * asking the components in this container.
     * 
     * @param size The width for the container that this layout constraint is set for. <code>null</code> is translated to
     *            a bound size containing only null sizes.
     * @since 3.5
     */
    public void setWidth(final BoundSizeCommon size) {
        this.width = size != null ? size : BoundSizeCommon.NULL_SIZE;
    }

    /**
     * Returns the minimum/preferred/maximum size for the container that this layout constraint is set for. Any of these
     * sizes that is not <code>null</code> will be returned directly instead of determining the correspondig size through
     * asking the components in this container.
     * 
     * @return The height for the container that this layout constraint is set for. Not <code>null</code> but
     *         all sizes can be <code>null</code>.
     * @since 3.5
     */
    public BoundSizeCommon getHeight() {
        return height;
    }

    /**
     * Sets the minimum/preferred/maximum size for the container that this layout constraint is set for. Any of these
     * sizes that is not <code>null</code> will be returned directly instead of determining the correspondig size through
     * asking the components in this container.
     * 
     * @param size The height for the container that this layout constraint is set for. <code>null</code> is translated to
     *            a bound size containing only null sizes.
     * @since 3.5
     */
    public void setHeight(final BoundSizeCommon size) {
        this.height = size != null ? size : BoundSizeCommon.NULL_SIZE;
    }

    // ************************************************************************
    // * Builder methods.
    // ************************************************************************

    /**
     * Short for, and thus same as, <code>.pack("pref", "pref")</code>.
     * <p>
     * Same functionality as {@link #setPackHeight(BoundSizeCommon)} and
     * {@link #setPackWidth(net.miginfocom.layout.BoundSizeCommon)} only this method returns <code>this</code> for chaining
     * multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @since 3.5
     */
    public LCCommon pack() {
        return pack("pref", "pref");
    }

    /**
     * Sets the pack width and height.
     * <p>
     * Same functionality as {@link #setPackHeight(BoundSizeCommon)} and
     * {@link #setPackWidth(net.miginfocom.layout.BoundSizeCommon)} only this method returns <code>this</code> for chaining
     * multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param width The pack width. May be <code>null</code>.
     * @param height The pack height. May be <code>null</code>.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @since 3.5
     */
    public LCCommon pack(final String width, final String height) {
        setPackWidth(width != null ? ConstraintParserCommon.parseBoundSize(width, false, false) : BoundSizeCommon.NULL_SIZE);
        setPackHeight(height != null ? ConstraintParserCommon.parseBoundSize(height, false, false) : BoundSizeCommon.NULL_SIZE);
        return this;
    }

    /**
     * Sets the pack width and height alignment.
     * <p>
     * Same functionality as {@link #setPackHeightAlign(float)} and {@link #setPackWidthAlign(float)} only this method returns
     * <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param alignX The pack width alignment. 0.5f is default.
     * @param alignY The pack height alignment. 0.5f is default.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @since 3.5
     */
    public LCCommon packAlign(final float alignX, final float alignY) {
        setPackWidthAlign(alignX);
        setPackHeightAlign(alignY);
        return this;
    }

    /**
     * Sets a wrap after the number of columns/rows that is defined in the {@link net.miginfocom.layout.ACCommon}.
     * <p>
     * Same functionality as {@link #setWrapAfter(int 0)} only this method returns <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon wrap() {
        setWrapAfter(0);
        return this;
    }

    /**
     * Same functionality as {@link #setWrapAfter(int)} only this method returns <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param count After what cell the grid should always auto wrap. If <code>0</code> the number of columns/rows in the
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon wrapAfter(final int count) {
        setWrapAfter(count);
        return this;
    }

    /**
     * Same functionality as {@link #setNoCache(boolean true)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon noCache() {
        setNoCache(true);
        return this;
    }

    /**
     * Same functionality as {@link #setFlowX(boolean false)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon flowY() {
        setFlowX(false);
        return this;
    }

    /**
     * Same functionality as {@link #setFlowX(boolean true)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon flowX() {
        setFlowX(true);
        return this;
    }

    /**
     * Same functionality as {@link #setFillX(boolean true)} and {@link #setFillY(boolean true)} conmbined.T his method returns
     * <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon fill() {
        setFillX(true);
        setFillY(true);
        return this;
    }

    /**
     * Same functionality as {@link #setFillX(boolean true)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon fillX() {
        setFillX(true);
        return this;
    }

    /**
     * Same functionality as {@link #setFillY(boolean true)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon fillY() {
        setFillY(true);
        return this;
    }

    /**
     * Same functionality as {@link #setLeftToRight(Boolean)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param b <code>true</code> for forcing left-to-right. <code>false</code> for forcing right-to-left.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon leftToRight(final boolean b) {
        setLeftToRight(b ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    /**
     * Same functionality as setLeftToRight(false) only this method returns <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @since 3.7.2
     */
    public LCCommon rightToLeft() {
        setLeftToRight(Boolean.FALSE);
        return this;
    }

    /**
     * Same functionality as {@link #setTopToBottom(boolean false)} only this method returns <code>this</code> for chaining
     * multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon bottomToTop() {
        setTopToBottom(false);
        return this;
    }

    /**
     * Same functionality as {@link #setTopToBottom(boolean true)} only this method returns <code>this</code> for chaining
     * multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @since 3.7.2
     */
    public LCCommon topToBottom() {
        setTopToBottom(true);
        return this;
    }

    /**
     * Same functionality as {@link #setNoGrid(boolean true)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon noGrid() {
        setNoGrid(true);
        return this;
    }

    /**
     * Same functionality as {@link #setVisualPadding(boolean false)} only this method returns <code>this</code> for chaining
     * multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon noVisualPadding() {
        setVisualPadding(false);
        return this;
    }

    /**
     * Sets the same inset (expressed as a <code>UnitValue</code>, e.g. "10px" or "20mm") all around.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param allSides The unit value to set for all sides. May be <code>null</code> which means that the default panel insets
     *            for the platform is used.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setInsets(UnitValueCommon[])
     */
    public LCCommon insetsAll(final String allSides) {
        final UnitValueCommon insH = ConstraintParserCommon.parseUnitValue(allSides, true);
        final UnitValueCommon insV = ConstraintParserCommon.parseUnitValue(allSides, false);
        insets = new UnitValueCommon[] {insV, insH, insV, insH}; // No setter to avoid copy again
        return this;
    }

    /**
     * Same functionality as <code>setInsets(ConstraintParser.parseInsets(s, true))</code>. This method returns <code>this</code>
     * for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param s The string to parse. E.g. "10 10 10 10" or "20". If less than 4 groups the last will be used for the missing.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setInsets(UnitValueCommon[])
     */
    public LCCommon insets(final String s) {
        insets = ConstraintParserCommon.parseInsets(s, true);
        return this;
    }

    /**
     * Sets the different insets (expressed as a <code>UnitValue</code>s, e.g. "10px" or "20mm") for the corresponding sides.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param top The top inset. E.g. "10px" or "10mm" or "related". May be <code>null</code> in which case the default inset for
     *            this
     *            side for the platform will be used.
     * @param left The left inset. E.g. "10px" or "10mm" or "related". May be <code>null</code> in which case the default inset
     *            for this
     *            side for the platform will be used.
     * @param bottom The bottom inset. E.g. "10px" or "10mm" or "related". May be <code>null</code> in which case the default
     *            inset for this
     *            side for the platform will be used.
     * @param right The right inset. E.g. "10px" or "10mm" or "related". May be <code>null</code> in which case the default inset
     *            for this
     *            side for the platform will be used.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setInsets(UnitValueCommon[])
     */
    public LCCommon insets(final String top, final String left, final String bottom, final String right) {
        insets = new UnitValueCommon[] { // No setter to avoid copy again
                ConstraintParserCommon.parseUnitValue(top, false), ConstraintParserCommon.parseUnitValue(left, true),
                ConstraintParserCommon.parseUnitValue(bottom, false), ConstraintParserCommon.parseUnitValue(right, true)};
        return this;
    }

    /**
     * Same functionality as <code>setAlignX(ConstraintParser.parseUnitValueOrAlign(unitValue, true))</code> only this method
     * returns <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param align The align keyword or for instance "100px". E.g "left", "right", "leading" or "trailing".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setAlignX(UnitValueCommon)
     */
    public LCCommon alignX(final String align) {
        setAlignX(ConstraintParserCommon.parseUnitValueOrAlign(align, true, null));
        return this;
    }

    /**
     * Same functionality as <code>setAlignY(ConstraintParser.parseUnitValueOrAlign(align, false))</code> only this method returns
     * <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param align The align keyword or for instance "100px". E.g "top" or "bottom".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setAlignY(UnitValueCommon)
     */
    public LCCommon alignY(final String align) {
        setAlignY(ConstraintParserCommon.parseUnitValueOrAlign(align, false, null));
        return this;
    }

    /**
     * Sets both the alignX and alignY as the same time.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param ax The align keyword or for instance "100px". E.g "left", "right", "leading" or "trailing".
     * @param ay The align keyword or for instance "100px". E.g "top" or "bottom".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #alignX(String)
     * @see #alignY(String)
     */
    public LCCommon align(final String ax, final String ay) {
        if (ax != null) {
            alignX(ax);
        }

        if (ay != null) {
            alignY(ay);
        }

        return this;
    }

    /**
     * Same functionality as <code>setGridGapX(ConstraintParser.parseBoundSize(boundsSize, true, true))</code> only this method
     * returns <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param boundsSize The <code>BoundSize</code> of the gap. This is a minimum and/or preferred and/or maximum size. E.g.
     *            <code>"50:100:200"</code> or <code>"100px"</code>.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setGridGapX(BoundSizeCommon)
     */
    public LCCommon gridGapX(final String boundsSize) {
        setGridGapX(ConstraintParserCommon.parseBoundSize(boundsSize, true, true));
        return this;
    }

    /**
     * Same functionality as <code>setGridGapY(ConstraintParser.parseBoundSize(boundsSize, true, false))</code> only this method
     * returns <code>this</code> for chaining multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param boundsSize The <code>BoundSize</code> of the gap. This is a minimum and/or preferred and/or maximum size. E.g.
     *            <code>"50:100:200"</code> or <code>"100px"</code>.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setGridGapY(BoundSizeCommon)
     */
    public LCCommon gridGapY(final String boundsSize) {
        setGridGapY(ConstraintParserCommon.parseBoundSize(boundsSize, true, false));
        return this;
    }

    /**
     * Sets both grid gaps at the same time. see {@link #gridGapX(String)} and {@link #gridGapY(String)}.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param gapx The <code>BoundSize</code> of the gap. This is a minimum and/or preferred and/or maximum size. E.g.
     *            <code>"50:100:200"</code> or <code>"100px"</code>.
     * @param gapy The <code>BoundSize</code> of the gap. This is a minimum and/or preferred and/or maximum size. E.g.
     *            <code>"50:100:200"</code> or <code>"100px"</code>.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #gridGapX(String)
     * @see #gridGapY(String)
     */
    public LCCommon gridGap(final String gapx, final String gapy) {
        if (gapx != null) {
            gridGapX(gapx);
        }

        if (gapy != null) {
            gridGapY(gapy);
        }

        return this;
    }

    /**
     * Same functionality as {@link #setDebugMillis(int repaintMillis)} only this method returns <code>this</code> for chaining
     * multiple calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param repaintMillis The new debug repaint interval.
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setDebugMillis(int)
     */
    public LCCommon debug(final int repaintMillis) {
        setDebugMillis(repaintMillis);
        return this;
    }

    /**
     * Same functionality as {@link #setHideMode(int mode)} only this method returns <code>this</code> for chaining multiple
     * calls.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
     * 
     * @param mode The mode:<br>
     *            0 == Normal. Bounds will be calculated as if the component was visible.<br>
     *            1 == If hidden the size will be 0, 0 but the gaps remain.<br>
     *            2 == If hidden the size will be 0, 0 and gaps set to zero.<br>
     *            3 == If hidden the component will be disregarded completely and not take up a cell in the grid..
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     * @see #setHideMode(int)
     */
    public LCCommon hideMode(final int mode) {
        setHideMode(mode);
        return this;
    }

    /**
     * The minimum width for the container. The value will override any value that is set on the container itself.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
     * 
     * @param width The width expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon minWidth(final String width) {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        setWidth(layoutUtil.derive(getWidth(), ConstraintParserCommon.parseUnitValue(width, true), null, null));
        return this;
    }

    /**
     * The width for the container as a min and/or preferred and/or maximum width. The value will override any value that is set
     * on
     * the container itself.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
     * 
     * @param width The width expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon width(final String width) {
        setWidth(ConstraintParserCommon.parseBoundSize(width, false, true));
        return this;
    }

    /**
     * The maximum width for the container. The value will override any value that is set on the container itself.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
     * 
     * @param width The width expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon maxWidth(final String width) {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        setWidth(layoutUtil.derive(getWidth(), null, null, ConstraintParserCommon.parseUnitValue(width, true)));
        return this;
    }

    /**
     * The minimum height for the container. The value will override any value that is set on the container itself.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
     * 
     * @param height The height expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon minHeight(final String height) {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        setHeight(layoutUtil.derive(getHeight(), ConstraintParserCommon.parseUnitValue(height, false), null, null));
        return this;
    }

    /**
     * The height for the container as a min and/or preferred and/or maximum height. The value will override any value that is set
     * on
     * the container itself.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcontainers.com.
     * 
     * @param height The height expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon height(final String height) {
        setHeight(ConstraintParserCommon.parseBoundSize(height, false, false));
        return this;
    }

    /**
     * The maximum height for the container. The value will override any value that is set on the container itself.
     * <p>
     * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcontainers.com.
     * 
     * @param height The height expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
     * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
     */
    public LCCommon maxHeight(final String height) {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        setHeight(layoutUtil.derive(getHeight(), null, null, ConstraintParserCommon.parseUnitValue(height, false)));
        return this;
    }

    // ************************************************
    // Persistence Delegate and Serializable combined.
    // ************************************************

    private Object readResolve() throws ObjectStreamException {
        return MigLayoutToolkitImpl.getMigLayoutUtil().getSerializedObject(this);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        layoutUtil.setSerializedObject(this, layoutUtil.readAsXML(in));
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (getClass() == LCCommon.class) {
            MigLayoutToolkitImpl.getMigLayoutUtil().writeAsXML(out, this);
        }
    }
}

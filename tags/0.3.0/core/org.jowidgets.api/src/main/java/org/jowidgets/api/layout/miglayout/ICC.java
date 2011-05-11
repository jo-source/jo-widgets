/*
 * Copyright (c) 2011, Nikolaus Moll and Mikael Grev, MiG InfoCom AB.
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
 * 
 * Comments by Mikael Grev, MiG InfoCom AB
 */
package org.jowidgets.api.layout.miglayout;

/**
 * A simple value holder for one component's constraint.
 */
public interface ICC {
	/**
	 * Specifies that the component should be put in the end group <code>s</code> and will thus share the same ending
	 * coordinate as them within the group.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s A name to associate on the group that should be the same for other rows/columns in the same group.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC endGroupX(final String s);

	/**
	 * Specifies that the component should be put in the size group <code>s</code> and will thus share the same size
	 * as them within the group.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s A name to associate on the group that should be the same for other rows/columns in the same group.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC sizeGroupX(final String s);

	/**
	 * The minimum size for the component. The value will override any value that is set on the component itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size The size expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC minWidth(final String size);

	/**
	 * The size for the component as a min and/or preferref and/or maximum size. The value will override any value that is set on
	 * the component itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size The size expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC width(final String size);

	/**
	 * The maximum size for the component. The value will override any value that is set on the component itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size The size expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC maxWidth(final String size);

	/**
	 * The horizontal gap before and/or after the component. The gap is towards cell bounds and/or other component bounds.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param before The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @param after The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC gapX(final String before, final String after);

	/**
	 * Same functionality as <code>getHorizontal().setAlign(ConstraintParser.parseUnitValue(unitValue, true))</code> only this
	 * method
	 * returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param align The align keyword or for instance "100px". E.g "left", "right", "leading" or "trailing".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC alignX(final String align);

	/**
	 * The grow priority compared to other components in the same cell.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The grow priority.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC growPrioX(final int p);

	/**
	 * Grow priority for the component horizontally and optionally vertically.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param widthHeight The new shrink weight and height. 1-2 arguments, never null.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC growPrio(final int... widthHeight);

	/**
	 * Grow weight for the component horizontally. It default to weight <code>100</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #growX(float)
	 */
	ICC growX();

	/**
	 * Grow weight for the component horizontally.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The new grow weight.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC growX(final float w);

	/**
	 * grow weight for the component horizontally and optionally vertically.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param widthHeight The new shrink weight and height. 1-2 arguments, never null.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC grow(final float... widthHeight);

	/**
	 * The shrink priority compared to other components in the same cell.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The shrink priority.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC shrinkPrioX(final int p);

	/**
	 * Shrink priority for the component horizontally and optionally vertically.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param widthHeight The new shrink weight and height. 1-2 arguments, never null.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC shrinkPrio(final int... widthHeight);

	/**
	 * Shrink weight for the component horizontally.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The new shrink weight.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC shrinkX(final float w);

	/**
	 * Shrink weight for the component horizontally and optionally vertically.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param widthHeight The new shrink weight and height. 1-2 arguments, never null.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC shrink(final float... widthHeight);

	/**
	 * The end group that this componet should be placed in.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s The name of the group. If <code>null</code> that means no group (default)
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC endGroupY(final String s);

	/**
	 * The end group(s) that this componet should be placed in.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param xy The end group for x and y repsectively. 1-2 arguments, not null.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC endGroup(final String... xy);

	/**
	 * The size group that this componet should be placed in.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s The name of the group. If <code>null</code> that means no group (default)
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC sizeGroupY(final String s);

	/**
	 * The size group(s) that this componet should be placed in.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param xy The size group for x and y repsectively. 1-2 arguments, not null.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC sizeGroup(final String... xy);

	/**
	 * The minimum size for the component. The value will override any value that is set on the component itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size The size expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC minHeight(final String size);

	/**
	 * The size for the component as a min and/or preferref and/or maximum size. The value will override any value that is set on
	 * the component itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size The size expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC height(final String size);

	/**
	 * The maximum size for the component. The value will override any value that is set on the component itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size The size expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC maxHeight(final String size);

	/**
	 * The vertical gap before (normally above) and/or after (normally below) the component. The gap is towards cell bounds and/or
	 * other component bounds.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param before The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @param after The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC gapY(final String before, final String after);

	/**
	 * Same functionality as <code>getVertical().setAlign(ConstraintParser.parseUnitValue(unitValue, true))</code> only this
	 * method
	 * returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param align The align keyword or for instance "100px". E.g "top" or "bottom".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC alignY(final String align);

	/**
	 * The grow priority compared to other components in the same cell.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The grow priority.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC growPrioY(final int p);

	/**
	 * Grow weight for the component vertically. Defaults to <code>100</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #growY(Float)
	 */
	ICC growY();

	/**
	 * Grow weight for the component vertically.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The new grow weight.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC growY(final Float w);

	/**
	 * The shrink priority compared to other components in the same cell.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The shrink priority.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC shrinkPrioY(final int p);

	/**
	 * Shrink weight for the component horizontally.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The new shrink weight.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC shrinkY(final float w);

	/**
	 * How this component, if hidden (not visible), should be treated.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param mode The mode. Default to the mode in the {@link ILC.miginfocom.layout.LC}.
	 *            0 == Normal. Bounds will be calculated as if the component was visible.<br>
	 *            1 == If hidden the size will be 0, 0 but the gaps remain.<br>
	 *            2 == If hidden the size will be 0, 0 and gaps set to zero.<br>
	 *            3 == If hidden the component will be disregarded completely and not take up a cell in the grid..
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC hideMode(final int mode);

	/**
	 * The id used to reference this component in some constraints.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s The id or <code>null</code>. May consist of a groupID and an componentID which are separated by a dot: ".". E.g.
	 *            "grp1.id1".
	 *            The dot should never be first or last if present.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 */
	ICC id(final String s);

	/**
	 * Same functionality as {@link #setTag(String tag)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param tag The new tag. May be <code>null</code>.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setTag(String)
	 */
	ICC tag(final String tag);

	/**
	 * Set the cell(s) that the component should occupy in the grid. Same functionality as {@link #setCellX(int col)} and
	 * {@link #setCellY(int row)} together with {@link #setSpanX(int width)} and {@link #setSpanY(int height)}. This method
	 * returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param colRowWidthHeight cellX, cellY, spanX, spanY repectively. 1-4 arguments, not null.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setCellX(int)
	 * @see #setCellY(int)
	 * @see #setSpanX(int)
	 * @see #setSpanY(int)
	 * @since 3.7.2. Replacing cell(int, int) and cell(int, int, int, int)
	 */
	ICC cell(final int... colRowWidthHeight);

	/**
	 * Same functionality as <code>spanX(cellsX).spanY(cellsY)</code> which means this cell will span cells in both x and y.
	 * This method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * Since 3.7.2 this takes an array/vararg whereas it previously only took two specific values, xSpan and ySpan.
	 * 
	 * @param cells spanX and spanY, when present, and in that order.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setSpanY(int)
	 * @see #setSpanX(int)
	 * @see #spanY()
	 * @see #spanX()
	 * @since 3.7.2 Replaces span(int, int).
	 */
	ICC span(final int... cells);

	/**
	 * Corresponds exactly to the "gap left right top bottom" keyword.
	 * 
	 * @param args Same as for the "gap" keyword. Length 1-4, never null buf elements can be null.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC gap(final String... args);

	/**
	 * Sets the horizontal gap before the component.
	 * <p>
	 * Note! This is currently same as gapLeft(). This might change in 4.x.
	 * 
	 * @param boundsSize The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC gapBefore(final String boundsSize);

	/**
	 * Sets the horizontal gap before the component.
	 * <p>
	 * Note! This is currently same as gapLeft(). This might change in 4.x.
	 * 
	 * @param boundsSize The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC gapAfter(final String boundsSize);

	/**
	 * Sets the gap above the component.
	 * 
	 * @param boundsSize The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC gapTop(final String boundsSize);

	/**
	 * Sets the gap above the component.
	 * 
	 * @param boundsSize The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC gapLeft(final String boundsSize);

	/**
	 * Sets the gap above the component.
	 * 
	 * @param boundsSize The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC gapBottom(final String boundsSize);

	/**
	 * Sets the gap above the component.
	 * 
	 * @param boundsSize The size of the gap expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px!".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ICC gapRight(final String boundsSize);

	/**
	 * Same functionality as {@link #setSpanY(int LayoutUtil.INF)} which means this cell will span the rest of the column.
	 * This method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setSpanY(int)
	 * @see #spanY()
	 */
	ICC spanY();

	/**
	 * Same functionality as {@link #setSpanY(int cells)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param cells The number of cells to span (i.e. merge).
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setSpanY(int)
	 */
	ICC spanY(final int cells);

	/**
	 * Same functionality as {@link #setSpanX(int LayoutUtil.INF)} which means this cell will span the rest of the row.
	 * This method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setSpanX(int)
	 * @see #spanX()
	 */
	ICC spanX();

	/**
	 * Same functionality as {@link #setSpanX(int cells)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param cells The number of cells to span (i.e. merge).
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setSpanY(int)
	 */
	ICC spanX(final int cells);

	/**
	 * Same functionality as <code>pushX().pushY()</code> which means this cell will push in both x and y dimensions.
	 * This method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setPushX(Float)
	 * @see #setPushX(Float)
	 * @see #pushY()
	 * @see #pushX()
	 */
	ICC push();

	/**
	 * Same functionality as <code>pushX(weightX).pushY(weightY)</code> which means this cell will push in both x and y
	 * dimensions.
	 * This method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param weightX The weight used in the push.
	 * @param weightY The weight used in the push.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setPushY(Float)
	 * @see #setPushX(Float)
	 * @see #pushY()
	 * @see #pushX()
	 */
	ICC push(final Float weightX, final Float weightY);

	/**
	 * Same functionality as {@link #setPushY(Float))} which means this cell will push the rest of the column.
	 * This method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setPushY(Float)
	 * @see #pushY()
	 */
	ICC pushY();

	/**
	 * Same functionality as {@link #setPushY(Float weight)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param weight The weight used in the push.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setPushY(Float)
	 */
	ICC pushY(final Float weight);

	/**
	 * Same functionality as {@link #setPushX(Float)} which means this cell will push the rest of the row.
	 * This method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setPushX(Float)
	 * @see #pushX()
	 */
	ICC pushX();

	/**
	 * Same functionality as {@link #setPushX(Float weight)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param weight The weight used in the push.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setPushY(Float)
	 */
	ICC pushX(final Float weight);

	/**
	 * Same functionality as {@link #setSplit(int parts)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param parts The number of parts (i.e. component slots) the cell should be divided into.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setSplit(int)
	 */
	ICC split(final int parts);

	/**
	 * Same functionality as split(LayoutUtil.INF), which means split until one of the keywords that breaks the split is found for
	 * a component after this one (e.g. wrap, newline and skip).
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setSplit(int)
	 * @since 3.7.2
	 */
	ICC split();

	/**
	 * Same functionality as {@link #setSkip(int)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param cells How many cells in the grid that should be skipped <b>before</b> the component that this constraint belongs to
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setSkip(int)
	 */
	ICC skip(final int cells);

	/**
	 * Same functionality as skip(1).
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setSkip(int)
	 * @since 3.7.2
	 */
	ICC skip();

	/**
	 * Same functionality as {@link #setExternal(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setExternal(boolean)
	 */
	ICC external();

	/**
	 * Same functionality as {@link #setFlowX(Boolean .TRUE)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setFlowX(Boolean)
	 */
	ICC flowX();

	/**
	 * Same functionality as {@link #setFlowX(Boolean .FALSE)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setFlowX(Boolean)
	 */
	ICC flowY();

	/**
	 * Same functionality as {@link #growX()} and {@link #growY()}.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #growX()
	 * @see #growY()
	 */
	ICC grow();

	/**
	 * Same functionality as {@link #setNewline(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setNewline(boolean)
	 */
	ICC newline();

	/**
	 * Same functionality as {@link #setNewlineGapSize(BoundSize)} only this method returns <code>this</code> for chaining
	 * multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param gapSize The gap size that will override the gap size in the row/colum constraints if <code>!= null</code>. E.g.
	 *            "5px" or "unrel".
	 *            If <code>null</code> or <code>""</code> the newline size will be set to the default size and turned on. This is
	 *            different compared to {@link #setNewlineGapSize(BoundSize)}.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setNewlineGapSize(BoundSize)
	 */
	ICC newline(final String gapSize);

	/**
	 * Same functionality as {@link #setWrap(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setWrap(boolean)
	 */
	ICC wrap();

	/**
	 * Same functionality as {@link #setWrapGapSize(BoundSize)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param gapSize The gap size that will override the gap size in the row/colum constraints if <code>!= null</code>. E.g.
	 *            "5px" or "unrel".
	 *            If <code>null</code> or <code>""</code> the wrap size will be set to the default size and turned on. This is
	 *            different compared to {@link #setWrapGapSize(BoundSize)}.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setWrapGapSize(BoundSize)
	 */
	ICC wrap(final String gapSize);

	/**
	 * Same functionality as {@link #setDockSide(int 0)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setDockSide(int)
	 */
	ICC dockNorth();

	/**
	 * Same functionality as {@link #setDockSide(int 1)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setDockSide(int)
	 */
	ICC dockWest();

	/**
	 * Same functionality as {@link #setDockSide(int 2)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setDockSide(int)
	 */
	ICC dockSouth();

	/**
	 * Same functionality as {@link #setDockSide(int 3)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setDockSide(int)
	 */
	ICC dockEast();

	/**
	 * Sets the x-coordinate for the component. This is used to set the x coordinate position to a specific value. The component
	 * bounds is still precalculated to the grid cell and this method should be seen as a way to correct the x position.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param x The x position as a UnitValue. E.g. "10" or "40mm" or "container.x+10".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setPos(UnitValue[])
	 * @see #setBoundsInGrid(boolean)
	 */
	ICC x(final String x);

	/**
	 * Sets the y-coordinate for the component. This is used to set the y coordinate position to a specific value. The component
	 * bounds is still precalculated to the grid cell and this method should be seen as a way to correct the y position.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param y The y position as a UnitValue. E.g. "10" or "40mm" or "container.x+10".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setPos(UnitValue[])
	 * @see #setBoundsInGrid(boolean)
	 */
	ICC y(final String y);

	/**
	 * Sets the x2-coordinate for the component (right side). This is used to set the x2 coordinate position to a specific value.
	 * The component
	 * bounds is still precalculated to the grid cell and this method should be seen as a way to correct the x position.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param x2 The x2 side's position as a UnitValue. E.g. "10" or "40mm" or "container.x2 - 10".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setPos(UnitValue[])
	 * @see #setBoundsInGrid(boolean)
	 */
	ICC x2(final String x2);

	/**
	 * Sets the y2-coordinate for the component (bottom side). This is used to set the y2 coordinate position to a specific value.
	 * The component
	 * bounds is still precalculated to the grid cell and this method should be seen as a way to correct the y position.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param y2 The y2 side's position as a UnitValue. E.g. "10" or "40mm" or "container.x2 - 10".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setPos(UnitValue[])
	 * @see #setBoundsInGrid(boolean)
	 */
	ICC y2(final String y2);

	/**
	 * Same functionality as {@link #x(String x)} and {@link #y(String y)} toghether.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param x The x position as a UnitValue. E.g. "10" or "40mm" or "container.x+10".
	 * @param y The y position as a UnitValue. E.g. "10" or "40mm" or "container.x+10".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setPos(UnitValue[])
	 */
	ICC pos(final String x, final String y);

	/**
	 * Same functionality as {@link #x(String x)}, {@link #y(String y)}, {@link #y2(String y)} and {@link #y2(String y)}
	 * toghether.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param x The x position as a UnitValue. E.g. "10" or "40mm" or "container.x+10".
	 * @param y The y position as a UnitValue. E.g. "10" or "40mm" or "container.x+10".
	 * @param x2 The x2 side's position as a UnitValue. E.g. "10" or "40mm" or "container.x2 - 10".
	 * @param y2 The y2 side's position as a UnitValue. E.g. "10" or "40mm" or "container.x2 - 10".
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setPos(UnitValue[])
	 */
	ICC pos(final String x, final String y, final String x2, final String y2);

	/**
	 * Same functionality as {@link #setPadding(UnitValue[])} but the unit values as absolute pixels. This method returns
	 * <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param top The top padding that will be added to the y coordinate at the last stage in the layout.
	 * @param left The top padding that will be added to the x coordinate at the last stage in the layout.
	 * @param bottom The top padding that will be added to the y2 coordinate at the last stage in the layout.
	 * @param right The top padding that will be added to the x2 coordinate at the last stage in the layout.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setTag(String)
	 */
	ICC pad(final int top, final int left, final int bottom, final int right);

	/**
	 * Same functionality as <code>setPadding(ConstraintParser.parseInsets(pad, false))}</code> only this method returns
	 * <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param pad The string to parse. E.g. "10 10 10 10" or "20". If less than 4 groups the last will be used for the missing.
	 * @return <code>this</code> so it is possible to chain calls. E.g.
	 *         <code>new ComponentConstraint().noGrid().gap().fill()</code>.
	 * @see #setTag(String)
	 */
	ICC pad(final String pad);
}

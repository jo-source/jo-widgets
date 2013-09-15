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

public interface IAC {
	/**
	 * Sets the total number of rows/columns to <code>size</code>. If the number of rows/columns is already more
	 * than <code>size</code> nothing will happen.
	 * 
	 * @param size The total number of rows/columns
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC count(int size);

	/**
	 * Specifies that the current row/column should not be grid-like. The while row/colum will have its components layed out
	 * in one single cell. It is the same as to say that the cells in this column/row will all be merged (a.k.a spanned).
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC noGrid();

	/**
	 * Specifies that the indicated rows/columns should not be grid-like. The while row/colum will have its components layed out
	 * in one single cell. It is the same as to say that the cells in this column/row will all be merged (a.k.a spanned).
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC noGrid(final int... indexes);

	/**
	 * Sets the current row/column to <code>i</code>. If the current number of rows/columns is less than <code>i</code> a call
	 * to {@link #count(int)} will set the size accordingly.
	 * <p>
	 * The next call to any of the constraint methods (e.g. {@link net.miginfocom.layout.AC#noGrid}) will be carried out on this
	 * new row/column.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param i The new current row/column.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC index(final int i);

	/**
	 * Specifies that the current row/column's component should grow by default. It does not affect the size of the row/column.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC fill();

	/**
	 * Specifies that the indicated rows'/columns' component should grow by default. It does not affect the size of the
	 * row/column.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC fill(final int... indexes);

	/**
	 * Specifies that the current row/column should be put in the size group <code>s</code> and will thus share the same size
	 * constraints as the other components in the group.
	 * <p>
	 * Same as <code>sizeGroup("")</code>
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	IAC sizeGroup();

	/**
	 * Specifies that the current row/column should be put in the size group <code>s</code> and will thus share the same size
	 * constraints as the other components in the group.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s A name to associate on the group that should be the same for other rows/columns in the same group.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC sizeGroup(final String s);

	/**
	 * Specifies that the indicated rows/columns should be put in the size group <code>s</code> and will thus share the same size
	 * constraints as the other components in the group.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s A name to associate on the group that should be the same for other rows/columns in the same group.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC sizeGroup(final String s, final int... indexes);

	/**
	 * Specifies the current row/column's min and/or preferred and/or max size. E.g. <code>"10px"</code> or
	 * <code>"50:100:200"</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s The minimum and/or preferred and/or maximum size of this row. The string will be interpreted
	 *            as a <b>BoundSize</b>. For more info on how <b>BoundSize</b> is formatted see the documentation.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC size(final String s);

	/**
	 * Specifies the indicated rows'/columns' min and/or preferred and/or max size. E.g. <code>"10px"</code> or
	 * <code>"50:100:200"</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size The minimum and/or preferred and/or maximum size of this row. The string will be interpreted
	 *            as a <b>BoundSize</b>. For more info on how <b>BoundSize</b> is formatted see the documentation.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC size(final String size, final int... indexes);

	/**
	 * Specifies the gap size to be the default one <b>AND</b> moves to the next column/row. The method is called
	 * <code>.gap()</code> rather the more natural <code>.next()</code> to indicate that it is very much related to the other
	 * <code>.gap(..)</code> methods.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC gap();

	/**
	 * Specifies the gap size to <code>size</code> <b>AND</b> moves to the next column/row.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size minimum and/or preferred and/or maximum size of the gap between this and the next row/column.
	 *            The string will be interpreted as a <b>BoundSize</b>. For more info on how <b>BoundSize</b> is formatted see the
	 *            documentation.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC gap(final String size);

	/**
	 * Specifies the indicated rows'/columns' gap size to <code>size</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param size minimum and/or preferred and/or maximum size of the gap between this and the next row/column.
	 *            The string will be interpreted as a <b>BoundSize</b>. For more info on how <b>BoundSize</b> is formatted see the
	 *            documentation.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC gap(final String size, final int... indexes);

	/**
	 * Specifies the current row/column's columns default alignment <b>for its components</b>. It does not affect the positioning
	 * or size of the columns/row itself. For columns it is the horizonal alignment (e.g. "left") and for rows it is the vertical
	 * alignment (e.g. "top").
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param side The default side to align the components. E.g. "top" or "left", or "leading" or "trailing" or "bottom" or
	 *            "right".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC align(final String side);

	/**
	 * Specifies the indicated rows'/columns' columns default alignment <b>for its components</b>. It does not affect the
	 * positioning
	 * or size of the columns/row itself. For columns it is the horizonal alignment (e.g. "left") and for rows it is the vertical
	 * alignment (e.g. "top").
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param side The default side to align the components. E.g. "top" or "left", or "before" or "after" or "bottom" or "right".
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC align(final String side, final int... indexes);

	/**
	 * Specifies the current row/column's grow priority.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The new grow priority.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC growPrio(final int p);

	/**
	 * Specifies the indicated rows'/columns' grow priority.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The new grow priority.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC growPrio(final int p, final int... indexes);

	/**
	 * Specifies the current row/column's grow weight within columns/rows with the <code>grow priority</code> 100f.
	 * <p>
	 * Same as <code>grow(100f)</code>
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	IAC grow();

	/**
	 * Specifies the current row/column's grow weight within columns/rows with the same <code>grow priority</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The new grow weight.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC grow(final float w);

	/**
	 * Specifies the indicated rows'/columns' grow weight within columns/rows with the same <code>grow priority</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The new grow weight.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC grow(final float w, final int... indexes);

	/**
	 * Specifies the current row/column's shrink priority.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The new shrink priority.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC shrinkPrio(final int p);

	/**
	 * Specifies the indicated rows'/columns' shrink priority.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param p The new shrink priority.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 */
	IAC shrinkPrio(final int p, final int... indexes);

	/**
	 * Specifies that the current row/column's shrink weight withing the columns/rows with the <code>shrink priority</code> 100f.
	 * <p>
	 * Same as <code>shrink(100f)</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the White Paper or Cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	IAC shrink();

	/**
	 * Specifies that the current row/column's shrink weight withing the columns/rows with the same <code>shrink priority</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the White Paper or Cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The shrink weight.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	IAC shrink(final float w);

	/**
	 * Specifies the indicated rows'/columns' shrink weight withing the columns/rows with the same <code>shrink priority</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the White Paper or Cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The shrink weight.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	IAC shrink(final float w, final int... indexes);

	/**
	 * Specifies that the current row/column's shrink weight withing the columns/rows with the same <code>shrink priority</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the White Paper or Cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The shrink weight.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 * @deprecated in 3.7.2. Use {@link #shrink(float)} instead.
	 */
	@Deprecated
	IAC shrinkWeight(final float w);

	/**
	 * Specifies the indicated rows'/columns' shrink weight withing the columns/rows with the same <code>shrink priority</code>.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the White Paper or Cheat Sheet at www.migcomponents.com.
	 * 
	 * @param w The shrink weight.
	 * @param indexes The index(es) (0-based) of the columns/rows that should be affected by this constraint.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new AxisConstraint().noGrid().gap().fill()</code>.
	 * @deprecated in 3.7.2. Use {@link #shrink(float, int...)} instead.
	 */
	@Deprecated
	IAC shrinkWeight(final float w, final int... indexes);

}

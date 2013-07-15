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
 * Contains the constraints for an instance of the {@link ILC} layout manager.
 */
public interface ILC {

	/**
	 * Short for, and thus same as, <code>.pack("pref", "pref")</code>.
	 * <p>
	 * Same functionality as {@link #setPackHeight(BoundSize)} and {@link #setPackWidth(net.miginfocom.layout.BoundSize)} only
	 * this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.5
	 */
	ILC pack();

	/**
	 * Sets the pack width and height.
	 * <p>
	 * Same functionality as {@link #setPackHeight(BoundSize)} and {@link #setPackWidth(net.miginfocom.layout.BoundSize)} only
	 * this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param width The pack width. May be <code>null</code>.
	 * @param height The pack height. May be <code>null</code>.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.5
	 */
	ILC pack(final String width, final String height);

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
	ILC packAlign(final float alignX, final float alignY);

	/**
	 * Sets a wrap after the number of columns/rows that is defined in the {@link net.miginfocom.layout.AC}.
	 * <p>
	 * Same functionality as {@link #setWrapAfter(int 0)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC wrap();

	/**
	 * Same functionality as {@link #setWrapAfter(int)} only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param count After what cell the grid should always auto wrap. If <code>0</code> the number of columns/rows in the
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC wrapAfter(final int count);

	/**
	 * Same functionality as {@link #setNoCache(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC noCache();

	/**
	 * Same functionality as {@link #setFlowX(boolean false)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC flowY();

	/**
	 * Same functionality as {@link #setFlowX(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC flowX();

	/**
	 * Same functionality as {@link #setFillX(boolean true)} and {@link #setFillY(boolean true)} conmbined.T his method returns
	 * <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC fill();

	/**
	 * Same functionality as {@link #setFillX(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC fillX();

	/**
	 * Same functionality as {@link #setFillY(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC fillY();

	/**
	 * Same functionality as {@link #setLeftToRight(Boolean)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param b <code>true</code> for forcing left-to-right. <code>false</code> for forcing right-to-left.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC leftToRight(final boolean b);

	/**
	 * Same functionality as setLeftToRight(false) only this method returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ILC rightToLeft();

	/**
	 * Same functionality as {@link #setTopToBottom(boolean false)} only this method returns <code>this</code> for chaining
	 * multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC bottomToTop();

	/**
	 * Same functionality as {@link #setTopToBottom(boolean true)} only this method returns <code>this</code> for chaining
	 * multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @since 3.7.2
	 */
	ILC topToBottom();

	/**
	 * Same functionality as {@link #setNoGrid(boolean true)} only this method returns <code>this</code> for chaining multiple
	 * calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC noGrid();

	/**
	 * Same functionality as {@link #setVisualPadding(boolean false)} only this method returns <code>this</code> for chaining
	 * multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC noVisualPadding();

	/**
	 * Sets the same inset (expressed as a <code>UnitValue</code>, e.g. "10px" or "20mm") all around.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param allSides The unit value to set for all sides. May be <code>null</code> which means that the default panel insets
	 *            for the platform is used.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setInsets(UnitValue[])
	 */
	ILC insetsAll(final String allSides);

	/**
	 * Same functionality as <code>setInsets(ConstraintParser.parseInsets(s, true))</code>. This method returns <code>this</code>
	 * for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param s The string to parse. E.g. "10 10 10 10" or "20". If less than 4 groups the last will be used for the missing.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setInsets(UnitValue[])
	 */
	ILC insets(final String s);

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
	 * @see #setInsets(UnitValue[])
	 */
	ILC insets(final String top, final String left, final String bottom, final String right);

	/**
	 * Same functionality as <code>setAlignX(ConstraintParser.parseUnitValueOrAlign(unitValue, true))</code> only this method
	 * returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param align The align keyword or for instance "100px". E.g "left", "right", "leading" or "trailing".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setAlignX(UnitValue)
	 */
	ILC alignX(final String align);

	/**
	 * Same functionality as <code>setAlignY(ConstraintParser.parseUnitValueOrAlign(align, false))</code> only this method returns
	 * <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param align The align keyword or for instance "100px". E.g "top" or "bottom".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setAlignY(UnitValue)
	 */
	ILC alignY(final String align);

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
	ILC align(final String ax, final String ay);

	/**
	 * Same functionality as <code>setGridGapX(ConstraintParser.parseBoundSize(boundsSize, true, true))</code> only this method
	 * returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param boundsSize The <code>BoundSize</code> of the gap. This is a minimum and/or preferred and/or maximum size. E.g.
	 *            <code>"50:100:200"</code> or <code>"100px"</code>.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setGridGapX(BoundSize)
	 */
	ILC gridGapX(final String boundsSize);

	/**
	 * Same functionality as <code>setGridGapY(ConstraintParser.parseBoundSize(boundsSize, true, false))</code> only this method
	 * returns <code>this</code> for chaining multiple calls.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcomponents.com.
	 * 
	 * @param boundsSize The <code>BoundSize</code> of the gap. This is a minimum and/or preferred and/or maximum size. E.g.
	 *            <code>"50:100:200"</code> or <code>"100px"</code>.
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 * @see #setGridGapY(BoundSize)
	 */
	ILC gridGapY(final String boundsSize);

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
	ILC gridGap(final String gapx, final String gapy);

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
	ILC debug(final int repaintMillis);

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
	ILC hideMode(final int mode);

	/**
	 * The minimum width for the container. The value will override any value that is set on the container itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
	 * 
	 * @param width The width expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC minWidth(final String width);

	/**
	 * The width for the container as a min and/or preferred and/or maximum width. The value will override any value that is set
	 * on the container itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
	 * 
	 * @param width The width expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC width(final String width);

	/**
	 * The maximum width for the container. The value will override any value that is set on the container itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
	 * 
	 * @param width The width expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC maxWidth(final String width);

	/**
	 * The minimum height for the container. The value will override any value that is set on the container itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or Cheat Sheet at www.migcontainers.com.
	 * 
	 * @param height The height expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC minHeight(final String height);

	/**
	 * The height for the container as a min and/or preferred and/or maximum height. The value will override any value that is set
	 * on the container itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcontainers.com.
	 * 
	 * @param height The height expressed as a <code>BoundSize</code>. E.g. "50:100px:200mm" or "100px".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC height(final String height);

	/**
	 * The maximum height for the container. The value will override any value that is set on the container itself.
	 * <p>
	 * For a more thorough explanation of what this constraint does see the white paper or cheat Sheet at www.migcontainers.com.
	 * 
	 * @param height The height expressed as a <code>UnitValue</code>. E.g. "100px" or "200mm".
	 * @return <code>this</code> so it is possible to chain calls. E.g. <code>new LayoutConstraint().noGrid().gap().fill()</code>.
	 */
	ILC maxHeight(final String height);

}

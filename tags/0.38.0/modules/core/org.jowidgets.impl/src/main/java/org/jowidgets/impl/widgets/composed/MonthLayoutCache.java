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

package org.jowidgets.impl.widgets.composed;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;

class MonthLayoutCache {

	private int separatorHeight = -1;
	private Dimension headerMaxSize;
	private Dimension maxDaySize;
	private Dimension preferredSize;
	private Rectangle[][] dayButtonsBounds;
	private Rectangle[] headerButtonsBounds;

	private Rectangle dayLabelBounds;
	private Rectangle dayLabelBorderBounds;
	private Rectangle headerLabelBounds;
	private Rectangle headerLabelBorderBounds;
	private Rectangle separatorBounds;

	private String[] headerButtonsNames;

	protected Dimension getHeaderMaxSize() {
		return headerMaxSize;
	}

	protected void setHeaderMaxSize(final Dimension headerMaxSize) {
		this.headerMaxSize = headerMaxSize;
	}

	protected Dimension getMaxDaySize() {
		return maxDaySize;
	}

	protected void setMaxDaySize(final Dimension maxDaySize) {
		this.maxDaySize = maxDaySize;
	}

	protected Dimension getPreferredSize() {
		return preferredSize;
	}

	protected void setPreferredSize(final Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}

	protected Rectangle[][] getDayButtonsBounds() {
		return dayButtonsBounds;
	}

	protected void setDayButtonsBounds(final Rectangle[][] dayButtonsBounds) {
		this.dayButtonsBounds = dayButtonsBounds;
	}

	protected Rectangle[] getHeaderButtonsBounds() {
		return headerButtonsBounds;
	}

	protected void setHeaderButtonsBounds(final Rectangle[] headerButtonsBounds) {
		this.headerButtonsBounds = headerButtonsBounds;
	}

	protected Rectangle getDayLabelBounds() {
		return dayLabelBounds;
	}

	protected void setDayLabelBounds(final Rectangle dayLabelBounds) {
		this.dayLabelBounds = dayLabelBounds;
	}

	protected Rectangle getDayLabelBorderBounds() {
		return dayLabelBorderBounds;
	}

	protected void setDayLabelBorderBounds(final Rectangle dayLabelBorderBounds) {
		this.dayLabelBorderBounds = dayLabelBorderBounds;
	}

	protected Rectangle getHeaderLabelBounds() {
		return headerLabelBounds;
	}

	protected void setHeaderLabelBounds(final Rectangle headerLabelBounds) {
		this.headerLabelBounds = headerLabelBounds;
	}

	protected Rectangle getHeaderLabelBorderBounds() {
		return headerLabelBorderBounds;
	}

	protected void setHeaderLabelBorderBounds(final Rectangle headerLabelBorderBounds) {
		this.headerLabelBorderBounds = headerLabelBorderBounds;
	}

	protected Rectangle getSeparatorBounds() {
		return separatorBounds;
	}

	protected void setSeparatorBounds(final Rectangle separatorBounds) {
		this.separatorBounds = separatorBounds;
	}

	protected String[] getHeaderButtonsNames() {
		return headerButtonsNames;
	}

	protected void setHeaderButtonsNames(final String[] headerButtonsNames) {
		this.headerButtonsNames = headerButtonsNames;
	}

	protected int getSeparatorHeight() {
		return separatorHeight;
	}

	protected void setSeparatorHeight(final int separatorHeight) {
		this.separatorHeight = separatorHeight;
	}

}

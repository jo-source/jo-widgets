/*
 * Copyright (c) 2011, nimoll
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

package org.jowidgets.impl.layout.miglayout;

import org.jowidgets.api.layout.miglayout.ILC;
import org.jowidgets.impl.layout.miglayout.common.LC;

public class LCWrapper implements ILC {

	private final LC lc;

	public LCWrapper() {
		this.lc = new LC();
	}

	public LC getLC() {
		return lc;
	}

	@Override
	public ILC pack() {
		lc.pack();
		return this;
	}

	@Override
	public ILC pack(final String width, final String height) {
		lc.pack(width, height);
		return this;
	}

	@Override
	public ILC packAlign(final float alignX, final float alignY) {
		lc.packAlign(alignX, alignY);
		return this;
	}

	@Override
	public ILC wrap() {
		lc.wrap();
		return this;
	}

	@Override
	public ILC wrapAfter(final int count) {
		lc.wrapAfter(count);
		return this;
	}

	@Override
	public ILC noCache() {
		lc.noCache();
		return this;
	}

	@Override
	public ILC flowY() {
		lc.flowY();
		return this;
	}

	@Override
	public ILC flowX() {
		lc.flowX();
		return this;
	}

	@Override
	public ILC fill() {
		lc.fill();
		return this;
	}

	@Override
	public ILC fillX() {
		lc.fillX();
		return this;
	}

	@Override
	public ILC fillY() {
		lc.fillY();
		return this;
	}

	@Override
	public ILC leftToRight(final boolean b) {
		lc.leftToRight(b);
		return this;
	}

	@Override
	public ILC rightToLeft() {
		lc.rightToLeft();
		return this;
	}

	@Override
	public ILC bottomToTop() {
		lc.bottomToTop();
		return this;
	}

	@Override
	public ILC topToBottom() {
		lc.topToBottom();
		return this;
	}

	@Override
	public ILC noGrid() {
		lc.noGrid();
		return this;
	}

	@Override
	public ILC noVisualPadding() {
		lc.noVisualPadding();
		return this;
	}

	@Override
	public ILC insetsAll(final String allSides) {
		lc.insetsAll(allSides);
		return this;
	}

	@Override
	public ILC insets(final String s) {
		lc.insets(s);
		return this;
	}

	@Override
	public ILC insets(final String top, final String left, final String bottom, final String right) {
		lc.insets(top, left, bottom, right);
		return this;
	}

	@Override
	public ILC alignX(final String align) {
		lc.alignX(align);
		return this;
	}

	@Override
	public ILC alignY(final String align) {
		lc.alignY(align);
		return this;
	}

	@Override
	public ILC align(final String ax, final String ay) {
		lc.align(ax, ay);
		return this;
	}

	@Override
	public ILC gridGapX(final String boundsSize) {
		lc.gridGapX(boundsSize);
		return this;
	}

	@Override
	public ILC gridGapY(final String boundsSize) {
		lc.gridGapY(boundsSize);
		return this;
	}

	@Override
	public ILC gridGap(final String gapx, final String gapy) {
		lc.gridGap(gapx, gapy);
		return this;
	}

	@Override
	public ILC debug(final int repaintMillis) {
		lc.debug(repaintMillis);
		return this;
	}

	@Override
	public ILC hideMode(final int mode) {
		lc.hideMode(mode);
		return this;
	}

	@Override
	public ILC minWidth(final String width) {
		lc.minWidth(width);
		return this;
	}

	@Override
	public ILC width(final String width) {
		lc.width(width);
		return this;
	}

	@Override
	public ILC maxWidth(final String width) {
		lc.maxWidth(width);
		return this;
	}

	@Override
	public ILC minHeight(final String height) {
		lc.minHeight(height);
		return this;
	}

	@Override
	public ILC height(final String height) {
		lc.height(height);
		return this;
	}

	@Override
	public ILC maxHeight(final String height) {
		lc.maxHeight(height);
		return this;
	}

}

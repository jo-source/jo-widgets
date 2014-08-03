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

import org.jowidgets.api.layout.miglayout.ICC;
import org.jowidgets.impl.layout.miglayout.common.CC;

public class CCWrapper implements ICC {

	private final CC cc;

	public CCWrapper() {
		this.cc = new CC();
	}

	public CC getCC() {
		return cc;
	}

	@Override
	public ICC endGroupX(final String s) {
		cc.endGroupX(s);
		return this;
	}

	@Override
	public ICC sizeGroupX(final String s) {
		cc.sizeGroupX(s);
		return this;
	}

	@Override
	public ICC minWidth(final String size) {
		cc.minWidth(size);
		return this;
	}

	@Override
	public ICC width(final String size) {
		cc.width(size);
		return this;
	}

	@Override
	public ICC maxWidth(final String size) {
		cc.maxWidth(size);
		return this;
	}

	@Override
	public ICC gapX(final String before, final String after) {
		cc.gapX(before, after);
		return this;
	}

	@Override
	public ICC alignX(final String align) {
		cc.alignX(align);
		return this;
	}

	@Override
	public ICC growPrioX(final int p) {
		cc.growPrioX(p);
		return this;
	}

	@Override
	public ICC growPrio(final int... widthHeight) {
		cc.growPrio(widthHeight);
		return this;
	}

	@Override
	public ICC growX() {
		cc.growX();
		return this;
	}

	@Override
	public ICC growX(final float w) {
		cc.growX(w);
		return this;
	}

	@Override
	public ICC grow(final float... widthHeight) {
		cc.grow(widthHeight);
		return this;
	}

	@Override
	public ICC shrinkPrioX(final int p) {
		cc.shrinkPrioX(p);
		return this;
	}

	@Override
	public ICC shrinkPrio(final int... widthHeight) {
		cc.shrinkPrio(widthHeight);
		return this;
	}

	@Override
	public ICC shrinkX(final float w) {
		cc.shrinkX(w);
		return this;
	}

	@Override
	public ICC shrink(final float... widthHeight) {
		cc.shrink(widthHeight);
		return this;
	}

	@Override
	public ICC endGroupY(final String s) {
		cc.endGroupY(s);
		return this;
	}

	@Override
	public ICC endGroup(final String... xy) {
		cc.endGroup(xy);
		return this;
	}

	@Override
	public ICC sizeGroupY(final String s) {
		cc.sizeGroupY(s);
		return this;
	}

	@Override
	public ICC sizeGroup(final String... xy) {
		cc.sizeGroup(xy);
		return this;
	}

	@Override
	public ICC minHeight(final String size) {
		cc.minHeight(size);
		return this;
	}

	@Override
	public ICC height(final String size) {
		cc.minHeight(size);
		return this;
	}

	@Override
	public ICC maxHeight(final String size) {
		cc.maxHeight(size);
		return this;
	}

	@Override
	public ICC gapY(final String before, final String after) {
		cc.gapY(before, after);
		return this;
	}

	@Override
	public ICC alignY(final String align) {
		cc.alignY(align);
		return this;
	}

	@Override
	public ICC growPrioY(final int p) {
		cc.growPrioY(p);
		return this;
	}

	@Override
	public ICC growY() {
		cc.growY();
		return this;
	}

	@Override
	public ICC growY(final Float w) {
		cc.growY(w);
		return this;
	}

	@Override
	public ICC shrinkPrioY(final int p) {
		cc.shrinkPrioY(p);
		return this;
	}

	@Override
	public ICC shrinkY(final float w) {
		cc.shrinkY(w);
		return this;
	}

	@Override
	public ICC hideMode(final int mode) {
		cc.hideMode(mode);
		return this;
	}

	@Override
	public ICC id(final String s) {
		cc.id(s);
		return this;
	}

	@Override
	public ICC tag(final String tag) {
		cc.tag(tag);
		return this;
	}

	@Override
	public ICC cell(final int... colRowWidthHeight) {
		cc.cell(colRowWidthHeight);
		return this;
	}

	@Override
	public ICC span(final int... cells) {
		cc.span(cells);
		return this;
	}

	@Override
	public ICC gap(final String... args) {
		cc.gap(args);
		return this;
	}

	@Override
	public ICC gapBefore(final String boundsSize) {
		cc.gapBefore(boundsSize);
		return this;
	}

	@Override
	public ICC gapAfter(final String boundsSize) {
		cc.gapAfter(boundsSize);
		return this;
	}

	@Override
	public ICC gapTop(final String boundsSize) {
		cc.gapTop(boundsSize);
		return this;
	}

	@Override
	public ICC gapLeft(final String boundsSize) {
		cc.gapLeft(boundsSize);
		return this;
	}

	@Override
	public ICC gapBottom(final String boundsSize) {
		cc.gapBottom(boundsSize);
		return this;
	}

	@Override
	public ICC gapRight(final String boundsSize) {
		cc.gapRight(boundsSize);
		return this;
	}

	@Override
	public ICC spanY() {
		cc.spanY();
		return this;
	}

	@Override
	public ICC spanY(final int cells) {
		cc.spanY(cells);
		return this;
	}

	@Override
	public ICC spanX() {
		cc.spanX();
		return this;
	}

	@Override
	public ICC spanX(final int cells) {
		cc.spanX(cells);
		return this;
	}

	@Override
	public ICC push() {
		cc.push();
		return this;
	}

	@Override
	public ICC push(final Float weightX, final Float weightY) {
		cc.push(weightX, weightY);
		return this;
	}

	@Override
	public ICC pushY() {
		cc.pushY();
		return this;
	}

	@Override
	public ICC pushY(final Float weight) {
		cc.pushY(weight);
		return this;
	}

	@Override
	public ICC pushX() {
		cc.pushX();
		return this;
	}

	@Override
	public ICC pushX(final Float weight) {
		cc.pushX(weight);
		return this;
	}

	@Override
	public ICC split(final int parts) {
		cc.split(parts);
		return this;
	}

	@Override
	public ICC split() {
		cc.split();
		return this;
	}

	@Override
	public ICC skip(final int cells) {
		cc.skip(cells);
		return this;
	}

	@Override
	public ICC skip() {
		cc.skip();
		return this;
	}

	@Override
	public ICC external() {
		cc.external();
		return this;
	}

	@Override
	public ICC flowX() {
		cc.flowX();
		return this;
	}

	@Override
	public ICC flowY() {
		cc.flowY();
		return this;
	}

	@Override
	public ICC grow() {
		cc.grow();
		return this;
	}

	@Override
	public ICC newline() {
		cc.newline();
		return this;
	}

	@Override
	public ICC newline(final String gapSize) {
		cc.newline(gapSize);
		return this;
	}

	@Override
	public ICC wrap() {
		cc.wrap();
		return this;
	}

	@Override
	public ICC wrap(final String gapSize) {
		cc.wrap(gapSize);
		return this;
	}

	@Override
	public ICC dockNorth() {
		cc.dockNorth();
		return this;
	}

	@Override
	public ICC dockWest() {
		cc.dockWest();
		return this;
	}

	@Override
	public ICC dockSouth() {
		cc.dockSouth();
		return this;
	}

	@Override
	public ICC dockEast() {
		cc.dockEast();
		return this;
	}

	@Override
	public ICC x(final String x) {
		cc.x(x);
		return this;
	}

	@Override
	public ICC y(final String y) {
		cc.y(y);
		return this;
	}

	@Override
	public ICC x2(final String x2) {
		cc.x2(x2);
		return this;
	}

	@Override
	public ICC y2(final String y2) {
		cc.y2(y2);
		return this;
	}

	@Override
	public ICC pos(final String x, final String y) {
		cc.pos(x, y);
		return this;
	}

	@Override
	public ICC pos(final String x, final String y, final String x2, final String y2) {
		cc.pos(x, y, x2, y2);
		return this;
	}

	@Override
	public ICC pad(final int top, final int left, final int bottom, final int right) {
		cc.pad(top, left, bottom, right);
		return this;
	}

	@Override
	public ICC pad(final String pad) {
		cc.pad(pad);
		return this;
	}

}

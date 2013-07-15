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

import org.jowidgets.api.layout.miglayout.IAC;
import org.jowidgets.impl.layout.miglayout.common.AC;

public class ACWrapper implements IAC {

	private final AC ac;

	public ACWrapper() {
		this.ac = new AC();
	}

	public AC getAC() {
		return ac;
	}

	@Override
	public IAC count(final int size) {
		ac.count(size);
		return this;
	}

	@Override
	public IAC noGrid() {
		ac.noGrid();
		return this;
	}

	@Override
	public IAC noGrid(final int... indexes) {
		ac.noGrid(indexes);
		return this;
	}

	@Override
	public IAC index(final int i) {
		ac.index(i);
		return this;
	}

	@Override
	public IAC fill() {
		ac.fill();
		return this;
	}

	@Override
	public IAC fill(final int... indexes) {
		ac.fill(indexes);
		return this;
	}

	@Override
	public IAC sizeGroup() {
		ac.sizeGroup();
		return this;
	}

	@Override
	public IAC sizeGroup(final String s) {
		ac.sizeGroup(s);
		return this;
	}

	@Override
	public IAC sizeGroup(final String s, final int... indexes) {
		ac.sizeGroup(s, indexes);
		return this;
	}

	@Override
	public IAC size(final String s) {
		ac.size(s);
		return this;
	}

	@Override
	public IAC size(final String size, final int... indexes) {
		ac.size(size, indexes);
		return this;
	}

	@Override
	public IAC gap() {
		ac.gap();
		return this;
	}

	@Override
	public IAC gap(final String size) {
		ac.gap(size);
		return this;
	}

	@Override
	public IAC gap(final String size, final int... indexes) {
		ac.gap(size, indexes);
		return this;
	}

	@Override
	public IAC align(final String side) {
		ac.align(side);
		return this;
	}

	@Override
	public IAC align(final String side, final int... indexes) {
		ac.align(side, indexes);
		return this;
	}

	@Override
	public IAC growPrio(final int p) {
		ac.growPrio(p);
		return this;
	}

	@Override
	public IAC growPrio(final int p, final int... indexes) {
		ac.growPrio(p, indexes);
		return this;
	}

	@Override
	public IAC grow() {
		ac.grow();
		return this;
	}

	@Override
	public IAC grow(final float w) {
		ac.grow(w);
		return this;
	}

	@Override
	public IAC grow(final float w, final int... indexes) {
		ac.grow(w, indexes);
		return this;
	}

	@Override
	public IAC shrinkPrio(final int p) {
		ac.shrinkPrio(p);
		return this;
	}

	@Override
	public IAC shrinkPrio(final int p, final int... indexes) {
		ac.shrinkPrio(p, indexes);
		return this;
	}

	@Override
	public IAC shrink() {
		ac.shrink();
		return this;
	}

	@Override
	public IAC shrink(final float w) {
		ac.shrink(w);
		return this;
	}

	@Override
	public IAC shrink(final float w, final int... indexes) {
		ac.shrink(w, indexes);
		return this;
	}

	@Deprecated
	@Override
	public IAC shrinkWeight(final float w) {
		ac.shrinkWeight(w);
		return this;
	}

	@Deprecated
	@Override
	public IAC shrinkWeight(final float w, final int... indexes) {
		ac.shrinkWeight(w, indexes);
		return this;
	}

}

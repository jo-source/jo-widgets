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

package org.jowidgets.tools.layout;

import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

/**
 * This class provides some often used MigLayoutDescriptors and constraints
 */
public final class MigLayoutFactory {

	/**
	 * Component constraints that grows in x and y direction with min with and height of 0
	 */
	public static final String GROWING_CELL_CONSTRAINTS = "growx, growy, w 0::, h 0::";

	private static final MigLayoutDescriptor GROWING_CELL_LAYOUT = new MigLayoutDescriptor("[grow, 0::]", "[grow, 0::]");
	private static final MigLayoutDescriptor GROWING_INNER_CELL_LAYOUT = new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0");

	private MigLayoutFactory() {}

	/**
	 * Gets a layout descriptor with one cell that grows in both directions and has a min size 0
	 * 
	 * @return A layout descriptor with one cell that grows in both directions and has a min size 0
	 */
	public static MigLayoutDescriptor growingCellLayout() {
		return GROWING_CELL_LAYOUT;
	}

	/**
	 * Gets a layout descriptor with one cell that grows in both directions and has a min size 0 and with no margin
	 * 
	 * @return A layout descriptor with one cell that grows in both directions and has a min size 0 and with no margin
	 */
	public static MigLayoutDescriptor growingInnerCellLayout() {
		return GROWING_INNER_CELL_LAYOUT;
	}

}

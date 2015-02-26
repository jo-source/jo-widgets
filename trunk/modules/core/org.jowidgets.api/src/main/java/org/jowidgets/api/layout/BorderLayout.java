/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.api.layout;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.layout.ILayouter;

/**
 * Accessor for 'BorderLayout'
 * 
 * A border layout lays out a container that can consist of five regions:
 * left, right, top, bottom, and center.
 * 
 * Each region can contain at most one control, and regions can be empty.
 * 
 * The center region will grow in x and y dimension, the other regions will be arranged
 * around the center.
 * 
 * When adding a control to a container with a border layout, use the {@link BorderLayoutConstraints} to
 * define the region to add to.
 */
public final class BorderLayout {

	/**
	 * The center region
	 */
	public static final BorderLayoutConstraints CENTER = BorderLayoutConstraints.CENTER;

	/**
	 * The left region
	 */
	public static final BorderLayoutConstraints LEFT = BorderLayoutConstraints.LEFT;

	/**
	 * The right region
	 */
	public static final BorderLayoutConstraints RIGHT = BorderLayoutConstraints.RIGHT;

	/**
	 * The top region
	 */
	public static final BorderLayoutConstraints TOP = BorderLayoutConstraints.TOP;

	/**
	 * The bottom region
	 */
	public static final BorderLayoutConstraints BOTTOM = BorderLayoutConstraints.BOTTOM;

	private BorderLayout() {}

	/**
	 * Gets a layout factory for an 'BorderLayout'
	 * 
	 * @return A layout factory that produces 'BorderLayout'
	 */
	public static ILayoutFactory<ILayouter> get() {
		return Toolkit.getLayoutFactoryProvider().borderLayout();
	}

	/**
	 * Gets a builder for an layout factory of an 'BorderLayout'
	 * 
	 * @return A builder for an layout factory of an 'BorderLayout'
	 */
	public static IBorderLayoutFactoryBuilder builder() {
		return Toolkit.getLayoutFactoryProvider().borderLayoutBuilder();
	}

}

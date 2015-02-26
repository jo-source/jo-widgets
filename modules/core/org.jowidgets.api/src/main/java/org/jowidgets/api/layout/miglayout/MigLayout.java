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

package org.jowidgets.api.layout.miglayout;

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.util.Assert;

/**
 * Accessor for 'MigLayout'
 */
public final class MigLayout {

	private MigLayout() {}

	/**
	 * Gets a layout factory for an default 'MigLayout'
	 * 
	 * @return A layout factory that produces 'MigLayout', never null
	 */
	public static ILayoutFactory<IMigLayout> get() {
		return Toolkit.getLayoutFactoryProvider().migLayout();
	}

	/**
	 * Creates a layout factory for default 'MigLayout' with given constraints
	 * 
	 * @param layoutConstraints The layout constraints to use
	 * 
	 * @return A layout factory that produces 'MigLayout', never null
	 */
	public static ILayoutFactory<IMigLayout> create(final String layoutConstraints) {
		return builder().constraints(layoutConstraints).build();
	}

	/**
	 * Creates a layout factory for 'MigLayout' with given constraints
	 * 
	 * @param rowConstraints The row constraints to use
	 * @param columnConstraints The column constraints to use
	 * 
	 * @return A layout factory that produces 'MigLayout', never null
	 */
	public static ILayoutFactory<IMigLayout> create(final String rowConstraints, final String columnConstraints) {
		return builder().rowConstraints(rowConstraints).columnConstraints(columnConstraints).build();
	}

	/**
	 * Creates a layout factory for 'MigLayout' with given constraints
	 * 
	 * @param layoutConstraints The layout constraints to use
	 * @param rowConstraints The row constraints to use
	 * @param columnConstraints The column constraints to use
	 * 
	 * @return A layout factory that produces 'MigLayout', never null
	 */
	public static ILayoutFactory<IMigLayout> create(
		final String layoutConstraints,
		final String rowConstraints,
		final String columnConstraints) {

		final IMigLayoutFactoryBuilder builder = builder();
		builder.constraints(layoutConstraints);
		builder.rowConstraints(rowConstraints);
		builder.columnConstraints(columnConstraints);
		return builder.build();
	}

	/**
	 * Creates a layout factory for 'MigLayout' with given
	 * constraints defined by a {@link MigLayoutDescriptor}
	 * 
	 * @param descriptor The mig layout descriptor to use
	 * 
	 * @return A layout factory that produces 'MigLayout', never null
	 */
	public static ILayoutFactory<IMigLayout> create(final MigLayoutDescriptor descriptor) {
		Assert.paramNotNull(descriptor, "descriptor");
		return builder().descriptor(descriptor).build();
	}

	/**
	 * Gets a builder for an layout factory of an 'FlowLayout'
	 * 
	 * @return A builder for an layout factory of an 'FlowLayout'
	 */
	public static IMigLayoutFactoryBuilder builder() {
		return Toolkit.getLayoutFactoryProvider().migLayoutBuilder();
	}

}

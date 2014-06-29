/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.unit.api;

import org.jowidgets.util.Assert;
import org.jowidgets.util.collection.IUnmodifiableArray;
import org.jowidgets.util.collection.IUnmodifiableArrayBuilder;
import org.jowidgets.util.collection.UnmodifiableArray;
import org.jowidgets.util.collection.UnmodifieableArrayWrapper;

public final class UnitSet {

	private UnitSet() {}

	public static IUnitSetBuilder builder() {
		return new UnitSetBuilderImpl();
	}

	private static final class UnitSetBuilderImpl implements IUnitSetBuilder {

		private final IUnmodifiableArrayBuilder<IUnit> arrayBuilder = UnmodifiableArray.builder();

		@Override
		public IUnitSetBuilder add(final IUnit unit) {
			Assert.paramNotNull(unit, "unit");
			arrayBuilder.add(unit);
			return this;
		}

		@Override
		public IUnitSetBuilder add(final IUnitBuilder unitBuilder) {
			Assert.paramNotNull(unitBuilder, "unitBuilder");
			return add(unitBuilder.build());
		}

		@Override
		public IUnitSetBuilder add(final String abbreviation, final String name, final double conversionFactor) {
			return add(Unit.create(abbreviation, name, conversionFactor));
		}

		@Override
		public IUnitSetBuilder add(final String abbreviation, final String name, final long conversionFactor) {
			return add(Unit.create(abbreviation, name, conversionFactor));
		}

		@Override
		public IUnitSetBuilder add(final String abbreviation, final double conversionFactor) {
			return add(Unit.create(abbreviation, conversionFactor));
		}

		@Override
		public IUnitSetBuilder add(final String abbreviation, final long conversionFactor) {
			return add(Unit.create(abbreviation, conversionFactor));
		}

		@Override
		public IUnitSetBuilder add(final String abbreviation, final String name) {
			return add(Unit.create(abbreviation, name));
		}

		@Override
		public IUnitSetBuilder add(final String abbreviation) {
			return add(Unit.create(abbreviation));
		}

		@Override
		public IUnitSet build() {
			return new UnitSetImpl(arrayBuilder.build());
		}

	}

	private static final class UnitSetImpl extends UnmodifieableArrayWrapper<IUnit> implements IUnitSet {

		public UnitSetImpl(final IUnmodifiableArray<IUnit> original) {
			super(original);
		}

		@Override
		public int hashCode() {
			//do not use hash code from super, because unit sets should not be equal if they are similar
			return System.identityHashCode(this);
		}

		@Override
		public boolean equals(final Object obj) {
			//do not use equals from super, because unit sets should not be equal if they are similar
			return this == obj;
		}

	}

}

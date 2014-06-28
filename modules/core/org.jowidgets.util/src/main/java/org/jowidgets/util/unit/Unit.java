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

package org.jowidgets.util.unit;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IProvider;

public final class Unit {

	private Unit() {}

	public static IUnit create(final String abbreviation) {
		return builder().abbreviation(abbreviation).build();
	}

	public static IUnit create(final String abbreviation, final String name) {
		return builder().abbreviation(abbreviation).name(name).build();
	}

	public static IUnit create(final String abbreviation, final long conversionFactor) {
		return builder().abbreviation(abbreviation).conversionFactor(conversionFactor).build();
	}

	public static IUnit create(final String abbreviation, final double conversionFactor) {
		return builder().abbreviation(abbreviation).conversionFactor(conversionFactor).build();
	}

	public static IUnit create(final String abbreviation, final String name, final long conversionFactor) {
		return builder().abbreviation(abbreviation).name(name).conversionFactor(conversionFactor).build();
	}

	public static IUnit create(final String abbreviation, final String name, final double conversionFactor) {
		return builder().abbreviation(abbreviation).name(name).conversionFactor(conversionFactor).build();
	}

	public static IUnitBuilder builder() {
		return new UnitBuilderImpl();
	}

	private static final class UnitBuilderImpl implements IUnitBuilder {

		private IProvider<String> abbreviation;
		private IProvider<String> name = new MessageProvider(null);
		private double conversionFactor = 1.0;

		@Override
		public IUnitBuilder abbreviation(final IProvider<String> abbreviation) {
			Assert.paramNotNull(abbreviation, "abbreviation");
			this.abbreviation = abbreviation;
			return this;
		}

		@Override
		public IUnitBuilder name(final IProvider<String> name) {
			Assert.paramNotNull(name, "name");
			this.name = name;
			return this;
		}

		@Override
		public IUnitBuilder abbreviation(final String abbreviation) {
			Assert.paramNotEmpty(abbreviation, "abbreviation");
			return abbreviation(new MessageProvider(abbreviation));
		}

		@Override
		public IUnitBuilder name(final String name) {
			return abbreviation(new MessageProvider(name));
		}

		@Override
		public IUnitBuilder conversionFactor(final double conversionFactor) {
			this.conversionFactor = conversionFactor;
			return this;
		}

		@Override
		public IUnitBuilder conversionFactor(final long conversionFactor) {
			this.conversionFactor = conversionFactor;
			return this;
		}

		@Override
		public IUnit build() {
			return new UnitImpl(abbreviation, name, conversionFactor);
		}

	}

	private static final class UnitImpl implements IUnit {

		private final IProvider<String> abbreviation;
		private final IProvider<String> name;
		private final double conversionFactor;

		public UnitImpl(final IProvider<String> abbreviation, final IProvider<String> name, final double conversionFactor) {
			Assert.paramNotNull(abbreviation, "abbreviation");
			Assert.paramNotNull(name, "name");

			this.abbreviation = abbreviation;
			this.name = name;
			this.conversionFactor = conversionFactor;
		}

		@Override
		public String getAbbreviation() {
			return abbreviation.get();
		}

		@Override
		public String getName() {
			return name.get();
		}

		@Override
		public double getConversionFactor() {
			return conversionFactor;
		}

		@Override
		public String toString() {
			return "UnitImpl [abbreviation=" + abbreviation + ", name=" + name + ", conversionFactor=" + conversionFactor + "]";
		}

	}

	private static final class MessageProvider implements IProvider<String> {

		private final String message;

		private MessageProvider(final String message) {
			this.message = message;
		}

		@Override
		public String get() {
			return message;
		}

		@Override
		public String toString() {
			return "MessageProvider [message=" + message + "]";
		}

	}

}

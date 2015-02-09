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

package org.jowidgets.unit.tools.units;

import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitBuilder;
import org.jowidgets.unit.api.IUnitSet;
import org.jowidgets.unit.api.IUnitSetBuilder;
import org.jowidgets.unit.api.Unit;
import org.jowidgets.unit.api.UnitSet;

public final class HertzUnitSet {

	public static final IUnit H = h();
	public static final IUnit KH = kh();
	public static final IUnit MH = mh();
	public static final IUnit GH = gh();

	private static final IUnitSet INSTANCE = createInstance();

	private HertzUnitSet() {}

	private static IUnit h() {
		final IUnitBuilder builder = Unit.builder();
		builder.abbreviation(Messages.getMessage("HertzUnitSet.h_abbreviation"));
		builder.name(Messages.getMessage("HertzUnitSet.h_name"));
		return builder.build();
	}

	private static IUnit kh() {
		final IUnitBuilder builder = Unit.builder();
		builder.abbreviation(Messages.getMessage("HertzUnitSet.kh_abbreviation"));
		builder.name(Messages.getMessage("HertzUnitSet.kh_name"));
		builder.conversionFactor(1000);
		return builder.build();
	}

	private static IUnit mh() {
		final IUnitBuilder builder = Unit.builder();
		builder.abbreviation(Messages.getMessage("HertzUnitSet.mh_abbreviation"));
		builder.name(Messages.getMessage("HertzUnitSet.mh_name"));
		builder.conversionFactor(1000 * 1000);
		return builder.build();
	}

	private static IUnit gh() {
		final IUnitBuilder builder = Unit.builder();
		builder.abbreviation(Messages.getMessage("HertzUnitSet.gh_abbreviation"));
		builder.name(Messages.getMessage("HertzUnitSet.gh_name"));
		builder.conversionFactor(1000 * 1000 * 1000);
		return builder.build();
	}

	private static IUnitSet createInstance() {
		final IUnitSetBuilder builder = UnitSet.builder();
		builder.add(H);
		builder.add(KH);
		builder.add(MH);
		builder.add(GH);
		return builder.build();
	}

	public static IUnitSet instance() {
		return INSTANCE;
	}

}

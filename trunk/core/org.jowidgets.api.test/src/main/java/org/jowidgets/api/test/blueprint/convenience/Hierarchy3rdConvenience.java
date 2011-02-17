/*
 * Copyright (c) 2011, Benjamin Marstaller
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

/**
 * 
 */
package org.jowidgets.api.test.blueprint.convenience;

import org.jowidgets.api.test.blueprint.builder.IHierarchy2nd1SetupBuilder;
import org.jowidgets.api.test.blueprint.builder.IHierarchy3rdSetupBuilder;
import org.jowidgets.tools.widgets.blueprint.convenience.AbstractSetupBuilderConvenience;

public class Hierarchy3rdConvenience extends
		AbstractSetupBuilderConvenience<IHierarchy3rdSetupBuilder<IHierarchy3rdSetupBuilder<?>>> implements
		IHierarchy2nd1Convenience<IHierarchy2nd1SetupBuilder<?>> {

	public static final String ADDITIONAL_PART = " Intro von 3";
	public static final String SUB_PART_2_1 = " sub-part 2.1";
	public static final String SUB_PART_2_2 = " sub-part 2.2";
	public static final String PART_3 = " part 3";

	@Override
	public IHierarchy3rdSetupBuilder<IHierarchy3rdSetupBuilder<?>> introduce(final String title) {
		getBuilder().setIntro(title + " Intro von 3");
		getBuilder().setIntroPartTwoOne(title + SUB_PART_2_1);
		getBuilder().setIntroPartTwoTwo(title + SUB_PART_2_2);
		getBuilder().setIntroPartThree(title + PART_3);
		return getBuilder();
	}
}

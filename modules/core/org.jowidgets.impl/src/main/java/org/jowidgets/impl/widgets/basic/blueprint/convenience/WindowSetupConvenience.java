/*
 * Copyright (c) 2010, Michael Grossmann, Nikolaus Moll
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.basic.blueprint.convenience;

import org.jowidgets.api.types.AutoCenterPolicy;
import org.jowidgets.api.types.AutoPackPolicy;
import org.jowidgets.api.types.AutoPositionCorrectionPolicy;
import org.jowidgets.api.widgets.blueprint.builder.IWindowSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.convenience.IWindowSetupConvenience;
import org.jowidgets.tools.widgets.blueprint.convenience.AbstractSetupBuilderConvenience;

public class WindowSetupConvenience extends AbstractSetupBuilderConvenience<IWindowSetupBuilder<?>> implements
		IWindowSetupConvenience<IWindowSetupBuilder<?>> {

	@Override
	public IWindowSetupBuilder<?> autoPackOnce() {
		return getBuilder().setAutoPackPolicy(AutoPackPolicy.ONCE);
	}

	@Override
	public IWindowSetupBuilder<?> autoPackAlways() {
		return getBuilder().setAutoPackPolicy(AutoPackPolicy.ALWAYS);
	}

	@Override
	public IWindowSetupBuilder<?> autoPackOff() {
		return getBuilder().setAutoPackPolicy(AutoPackPolicy.OFF);
	}

	@Override
	public IWindowSetupBuilder<?> autoPositionCorrectionOnce() {
		return getBuilder().setAutoPositionCorrectionPolicy(AutoPositionCorrectionPolicy.OFF);
	}

	@Override
	public IWindowSetupBuilder<?> autoPositionCorrectionAlways() {
		return getBuilder().setAutoPositionCorrectionPolicy(AutoPositionCorrectionPolicy.OFF);
	}

	@Override
	public IWindowSetupBuilder<?> autoPositionCorrectionOff() {
		return getBuilder().setAutoPositionCorrectionPolicy(AutoPositionCorrectionPolicy.OFF);
	}

	@Override
	public IWindowSetupBuilder<?> autoCenterOnce() {
		return getBuilder().setAutoCenterPolicy(AutoCenterPolicy.ONCE);
	}

	@Override
	public IWindowSetupBuilder<?> autoCenterAlways() {
		return getBuilder().setAutoCenterPolicy(AutoCenterPolicy.ALWAYS);
	}

	@Override
	public IWindowSetupBuilder<?> autoCenterOff() {
		return getBuilder().setAutoCenterPolicy(AutoCenterPolicy.OFF);
	}
}

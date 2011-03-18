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

package org.jowidgets.examples.common.demo;

import java.util.List;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IInputComposite;
import org.jowidgets.api.widgets.blueprint.IInputCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public final class DemoForm1Creator {

	private DemoForm1Creator() {}

	public static IInputComposite<List<String>> createDemoForm1(final IContainer parentContainer) {
		return createDemoForm1(parentContainer, true);
	}

	public static IInputComposite<List<String>> createDemoForm1(final IContainer parentContainer, final boolean scrolledContent) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		//layout for the composite
		parentContainer.setLayout(new MigLayoutDescriptor("[grow, 0::]", "[grow, 0::]"));

		//define the blue print for the input composite
		final IInputCompositeBluePrint<List<String>> inputCompositeBp = bpf.inputComposite(new DemoForm1ContentCreator());
		inputCompositeBp.setContentScrolled(scrolledContent);
		inputCompositeBp.setMissingInputText("Please fill out all mandatory (*) fields");
		inputCompositeBp.setMissingInputIcon(IconsSmall.INFO);

		//add the input composite to the parent composite
		return parentContainer.add(inputCompositeBp, "growx, growy, w 0::, h 0::");
	}
}

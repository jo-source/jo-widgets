/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.tools.powo;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ISplitCompositeDescriptor;
import org.jowidgets.common.types.Orientation;

public class JoSplitComposite extends Control<ISplitComposite, ISplitCompositeBluePrint> implements ISplitComposite {

	private final JoContainer first;
	private final JoContainer second;

	public JoSplitComposite(final Orientation orientation) {
		this(Toolkit.getBluePrintFactory().splitComposite().setOrientation(orientation));
	}

	public JoSplitComposite() {
		this(Toolkit.getBluePrintFactory().splitHorizontal());
	}

	public JoSplitComposite(final ISplitCompositeDescriptor descriptor) {
		super(Toolkit.getBluePrintFactory().splitComposite().setSetup(descriptor));
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final ICompositeBluePrint firstBp = bpF.composite();
		firstBp.setBorder(descriptor.getFirstBorder()).setLayout(descriptor.getFirstLayout());
		this.first = new JoContainer(firstBp);

		final ICompositeBluePrint secondBp = bpF.composite();
		secondBp.setBorder(descriptor.getSecondBorder()).setLayout(descriptor.getSecondLayout());
		this.second = new JoContainer(secondBp);
	}

	@Override
	void initialize(final ISplitComposite widget) {
		super.initialize(widget);

		//maybe the layout was changed on first or second before initialization, so
		//write them back to avoid keep using the old layout.
		//this must be done before first and second will be initialized
		widget.getFirst().setLayout(first.getBluePrint().getLayout());
		widget.getSecond().setLayout(second.getBluePrint().getLayout());

		first.initialize(widget.getFirst());
		second.initialize(widget.getSecond());
	}

	@Override
	public JoContainer getFirst() {
		return first;
	}

	@Override
	public JoContainer getSecond() {
		return second;
	}

	public static ISplitCompositeBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().splitComposite();
	}

}

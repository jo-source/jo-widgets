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

package org.jowidgets.examples.common.demo;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.IContainerCommon;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public class DemoSplitComposite {

	public DemoSplitComposite(final IContainerCommon parentContainer) {

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final ILayoutDescriptor fillLayoutDescriptor = new MigLayoutDescriptor("0[grow]0", "0[grow]0");

		parentContainer.setLayout(fillLayoutDescriptor);

		final ISplitComposite split1 = parentContainer.add(
				bpF.splitHorizontal().setWeight(0.2).disableSecondBorder().resizeSecondPolicy(),
				"growx, growy");

		final IComposite split1First = split1.getFirst();
		final IComposite split1Second = split1.getSecond();

		split1First.add(bpF.textLabel("Content1"), "align center");
		final ISplitComposite split2 = split1Second.add(
				bpF.splitHorizontal().setWeight(1 - 0.2 / 0.8).disableFirstBorder().resizeFirstPolicy(),
				"growx, growy");

		final IComposite split2First = split2.getFirst();
		final IComposite split2Second = split2.getSecond();

		final ISplitComposite split3 = split2First.add(bpF.splitVertical().resizeFirstPolicy(), "growx, growy");
		split2Second.add(bpF.textLabel("Content2"), "align center");

		final IComposite split3First = split3.getFirst();
		final IComposite split3Second = split3.getSecond();

		split3First.add(bpF.textLabel("Content3"), "align center");
		split3Second.add(bpF.textLabel("Content4"), "align center");

	}
}

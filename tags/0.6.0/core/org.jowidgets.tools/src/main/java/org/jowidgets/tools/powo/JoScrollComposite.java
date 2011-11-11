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
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.common.types.Border;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.util.Assert;

public class JoScrollComposite extends Composite<IScrollComposite, IScrollCompositeBluePrint> implements
		IScrollComposite {

	JoScrollComposite(final IScrollComposite widget) {
		this(bluePrint());
		Assert.paramNotNull(widget, "widget");
		initialize(widget);
	}

	public JoScrollComposite() {
		this(bluePrint());
	}

	public JoScrollComposite(final ILayoutDescriptor layout) {
		this(bluePrint(layout));
	}

	public JoScrollComposite(final IScrollCompositeDescriptor descriptor) {
		super(bluePrint().setSetup(descriptor));
	}

	public static JoScrollComposite toJoScrollComposite(final IScrollComposite widget) {
		Assert.paramNotNull(widget, "widget");
		if (widget instanceof JoScrollComposite) {
			return (JoScrollComposite) widget;
		}
		else {
			return new JoScrollComposite(widget);
		}
	}

	public static IScrollCompositeBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().scrollComposite();
	}

	public static IScrollCompositeBluePrint bluePrint(final ILayoutDescriptor layout) {
		Assert.paramNotNull(layout, "layout");
		return bluePrint().setLayout(layout);
	}

	public static IScrollCompositeBluePrint bluePrint(final String borderTitle) {
		return bluePrint().setBorder(new Border(borderTitle));
	}

	public static IScrollCompositeBluePrint bluePrint(final boolean border) {
		return border ? Toolkit.getBluePrintFactory().scrollCompositeWithBorder() : bluePrint();
	}

}

/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.api.widgets.factory.impl.internal;

import org.jowidgets.api.color.IColorConstant;
import org.jowidgets.api.color.defaults.Colors;
import org.jowidgets.api.look.AlignmentHorizontal;
import org.jowidgets.api.look.Markup;
import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.ITextLabelWidget;
import org.jowidgets.api.widgets.ITextSeparatorWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.factory.impl.BluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextSeparatorDescriptor;
import org.jowidgets.api.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.util.Assert;

public class TextSeparatorWidget implements ITextSeparatorWidget {

	private final ICompositeWidget compositeWidget;
	private final ITextLabelWidget textLabelWidget;

	public TextSeparatorWidget(final ICompositeWidget compositeWidget, final ITextSeparatorDescriptor descriptor) {
		super();
		Assert.paramNotNull(compositeWidget, "compositeWidget");
		Assert.paramNotNull(descriptor, "descriptor");

		this.compositeWidget = compositeWidget;

		final BluePrintFactory bpF = new BluePrintFactory();

		final ITextLabelDescriptor textLabelDescriptor = bpF.textLabel().setDescriptor(descriptor);

		if (AlignmentHorizontal.LEFT.equals(descriptor.getAlignment())) {
			this.compositeWidget.setLayout(new MigLayoutDescriptor("0[][grow]", "0[]0"));
			textLabelWidget = compositeWidget.add(textLabelDescriptor, "");
			compositeWidget.add(bpF.separator(), "growx");
		}
		else if (AlignmentHorizontal.RIGHT.equals(descriptor.getAlignment())) {
			this.compositeWidget.setLayout(new MigLayoutDescriptor("0[grow][]", "0[]0"));
			compositeWidget.add(bpF.separator(), "growx");
			textLabelWidget = compositeWidget.add(textLabelDescriptor, "");
		}
		else if (AlignmentHorizontal.CENTER.equals(descriptor.getAlignment())) {
			this.compositeWidget.setLayout(new MigLayoutDescriptor("0[grow][][grow]", "0[]0"));
			compositeWidget.add(bpF.separator(), "growx");
			textLabelWidget = compositeWidget.add(textLabelDescriptor, "");
			compositeWidget.add(bpF.separator(), "growx");
		}
		else {
			throw new IllegalArgumentException("Alignment '" + descriptor.getAlignment() + "' is unknown.");
		}

		if (descriptor.getForegroundColor() == null) {
			setForegroundColor(Colors.STRONG);
		}
	}

	@Override
	public void setText(final String text) {
		textLabelWidget.setText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		textLabelWidget.setToolTipText(text);
	}

	@Override
	public IWidget getParent() {
		return compositeWidget.getParent();
	}

	@Override
	public Object getUiReference() {
		return compositeWidget.getUiReference();
	}

	@Override
	public void redraw() {
		compositeWidget.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		textLabelWidget.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		textLabelWidget.setBackgroundColor(colorValue);
	}

	@Override
	public void setMarkup(final Markup markup) {
		textLabelWidget.setMarkup(markup);
	}

}

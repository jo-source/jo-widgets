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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.api.widgets.factory.impl.internal;

import org.jo.widgets.api.color.IColorConstant;
import org.jo.widgets.api.image.IImageConstant;
import org.jo.widgets.api.look.Markup;
import org.jo.widgets.api.util.ColorSettingsInvoker;
import org.jo.widgets.api.widgets.ICompositeWidget;
import org.jo.widgets.api.widgets.IIconWidget;
import org.jo.widgets.api.widgets.ILabelWidget;
import org.jo.widgets.api.widgets.ITextLabelWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.blueprint.factory.impl.BluePrintFactory;
import org.jo.widgets.api.widgets.descriptor.IIconDescriptor;
import org.jo.widgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jo.widgets.api.widgets.descriptor.base.IBaseLabelDescriptor;
import org.jo.widgets.api.widgets.layout.MigLayoutDescriptor;

public class LabelWidget implements ILabelWidget {

	private final IIconWidget iconWidget;
	private final ITextLabelWidget textLabelWidget;
	private final ICompositeWidget compositeWidget;

	public LabelWidget(final ICompositeWidget compositeWidget, final IBaseLabelDescriptor<ILabelWidget> descriptor) {

		super();

		this.compositeWidget = compositeWidget;
		this.compositeWidget.setLayout(new MigLayoutDescriptor("0[][]0", "0[]0"));

		final BluePrintFactory bpF = new BluePrintFactory();

		final IIconDescriptor iconDescriptor = bpF.icon(descriptor.getIcon());
		this.iconWidget = compositeWidget.add(iconDescriptor, "w 0::");

		final ITextLabelDescriptor textLabelDescriptor = bpF.textLabel().setDescriptor(descriptor);
		this.textLabelWidget = compositeWidget.add(textLabelDescriptor, "");

		ColorSettingsInvoker.setColors(descriptor, this);
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

	@Override
	public void setIcon(final IImageConstant icon) {
		iconWidget.setIcon(icon);
	}

}

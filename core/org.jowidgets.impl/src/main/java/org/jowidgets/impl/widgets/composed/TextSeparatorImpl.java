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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextSeparatorDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.ITextLabelCommon;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.util.Assert;

public class TextSeparatorImpl extends CompositeBasedControl implements ITextLabel {

	private final IComposite composite;
	private final ITextLabelCommon textLabelWidget;
	private String text;

	public TextSeparatorImpl(final IComposite composite, final ITextSeparatorDescriptor descriptor) {
		super(composite);
		Assert.paramNotNull(composite, "composite");
		Assert.paramNotNull(descriptor, "descriptor");

		this.composite = composite;

		final BluePrintFactory bpF = new BluePrintFactory();

		final ITextLabelDescriptor textLabelDescriptor = bpF.textLabel().setSetup(descriptor);

		if (AlignmentHorizontal.LEFT.equals(descriptor.getAlignment())) {
			this.composite.setLayout(new MigLayoutDescriptor("0[][grow]", "0[]0"));
			textLabelWidget = composite.add(textLabelDescriptor, "");
			composite.add(bpF.separator(), "growx");
		}
		else if (AlignmentHorizontal.RIGHT.equals(descriptor.getAlignment())) {
			this.composite.setLayout(new MigLayoutDescriptor("0[grow][]", "0[]0"));
			composite.add(bpF.separator(), "growx");
			textLabelWidget = composite.add(textLabelDescriptor, "");
		}
		else if (AlignmentHorizontal.CENTER.equals(descriptor.getAlignment())) {
			this.composite.setLayout(new MigLayoutDescriptor("0[grow][][grow]", "0[]0"));
			composite.add(bpF.separator(), "growx");
			textLabelWidget = composite.add(textLabelDescriptor, "");
			composite.add(bpF.separator(), "growx");
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
		this.text = text;
		textLabelWidget.setText(text);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setToolTipText(final String text) {
		textLabelWidget.setToolTipText(text);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		textLabelWidget.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return textLabelWidget.isEnabled();
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
	public IColorConstant getForegroundColor() {
		return textLabelWidget.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return textLabelWidget.getBackgroundColor();
	}

	@Override
	public void setMarkup(final Markup markup) {
		textLabelWidget.setMarkup(markup);
	}

	@Override
	public void setFontSize(final int size) {
		textLabelWidget.setFontSize(size);
	}

	@Override
	public void setFontName(final String fontName) {
		textLabelWidget.setFontName(fontName);
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		//TODO MG this might not work, popup must be set on inner widgets
		//For that, inner widgets must be api widgets 
		composite.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		composite.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		composite.removePopupDetectionListener(listener);
	}

}

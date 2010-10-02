/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the jo-widgets.org nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
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
package org.jo.widgets.impl.swing.factory.internal;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JToggleButton;

import org.jo.widgets.api.image.IImageConstant;
import org.jo.widgets.api.look.Markup;
import org.jo.widgets.api.util.ColorSettingsInvoker;
import org.jo.widgets.api.widgets.IToggleButtonWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.descriptor.base.IBaseCheckBoxDescriptor;
import org.jo.widgets.impl.swing.internal.image.SwingImageRegistry;
import org.jo.widgets.impl.swing.util.AlignmentConvert;
import org.jo.widgets.impl.swing.util.FontProvider;
import org.jo.widgets.util.Assert;

public class ToggleButtonWidget extends AbstractSwingInputWidget<Boolean>
		implements IToggleButtonWidget {

	private final SwingImageRegistry imageRegistry;
	private boolean hasInput;

	public ToggleButtonWidget(final SwingImageRegistry imageRegistry,
			final IWidget parent,
			final IBaseCheckBoxDescriptor<IToggleButtonWidget> descriptor) {
		this(imageRegistry, parent, new JToggleButton(), descriptor);
	}

	public ToggleButtonWidget(final SwingImageRegistry imageRegistry,
			final IWidget parent, final JToggleButton toggleButton,
			final IBaseCheckBoxDescriptor<IToggleButtonWidget> descriptor) {
		super(parent, toggleButton, null);

		this.imageRegistry = imageRegistry;
		this.hasInput = false;

		setText(descriptor.getText());
		setToolTipText(descriptor.getToolTipText());
		setIcon(descriptor.getIcon());
		setMarkup(descriptor.getMarkup());
		getUiReference().setHorizontalAlignment(
				AlignmentConvert.convert(descriptor.getAlignment()));
		ColorSettingsInvoker.setColors(descriptor, this);

		getUiReference().addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				hasInput = true;
				fireContentChanged(this);
			}
		});
	}

	@Override
	public JToggleButton getUiReference() {
		return (JToggleButton) super.getUiReference();
	}

	@Override
	public Boolean getValue() {
		return Boolean.valueOf(isSelected());
	}

	@Override
	public void setValue(final Boolean value) {
		Assert.paramNotNull(value, "value");
		setSelected(value.booleanValue());
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEnabled(editable);
	}

	@Override
	public boolean isMandatory() {
		return false;
	}

	@Override
	public boolean hasInput() {
		return hasInput;
	}

	@Override
	public void setMarkup(final Markup markup) {
		final JToggleButton toggleButton = getUiReference();
		toggleButton.setFont(FontProvider.deriveFont(toggleButton.getFont(),
				markup));
	}

	@Override
	public void setText(final String text) {
		getUiReference().setText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		getUiReference().setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		getUiReference().setIcon(imageRegistry.getImageIcon(icon));
	}

	@Override
	public boolean isSelected() {
		return getUiReference().isSelected();
	}

	@Override
	public void setSelected(final boolean selected) {
		getUiReference().setSelected(selected);
	}

}

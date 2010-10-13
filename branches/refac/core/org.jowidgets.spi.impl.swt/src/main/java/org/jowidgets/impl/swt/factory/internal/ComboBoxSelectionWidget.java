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
package org.jowidgets.impl.swt.factory.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.util.ColorSettingsInvoker;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.impl.swt.internal.color.IColorCache;
import org.jowidgets.spi.widgets.IComboBoxWidgetSpi;
import org.jowidgets.spi.widgets.descriptor.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.util.Assert;

public class ComboBoxSelectionWidget<INPUT_TYPE> extends AbstractSwtInputWidget<INPUT_TYPE> implements
		IComboBoxWidgetSpi<INPUT_TYPE> {

	private final ArrayList<INPUT_TYPE> content;
	private final IObjectStringConverter<INPUT_TYPE> objectStringConverter;

	public ComboBoxSelectionWidget(
		final IWidget parent,
		final IColorCache colorCache,
		final IComboBoxSelectionSetupSpi<?, INPUT_TYPE> descriptor) {
		this(colorCache, new Combo((Composite) parent.getUiReference(), SWT.NONE | SWT.READ_ONLY), descriptor);
	}

	public ComboBoxSelectionWidget(
		final IColorCache colorCache,
		final Combo combo,
		final IComboBoxSelectionSetupSpi<?, INPUT_TYPE> descriptor) {
		super(colorCache, combo);

		this.content = new ArrayList<INPUT_TYPE>();
		this.objectStringConverter = descriptor.getObjectStringConverter();

		ColorSettingsInvoker.setColors(descriptor, this);
		setElements(descriptor.getElements());

		getUiReference().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				fireInputChanged(getUiReference());
				setToolTip();
			}
		});

	}

	@Override
	public Combo getUiReference() {
		return (Combo) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEnabled(editable);
	}

	@Override
	public void setValue(final INPUT_TYPE value) {
		final Combo combo = getUiReference();
		final int index = content.indexOf(value);
		if (index != -1) {
			combo.select(index);
		}
		else {
			throw new IllegalArgumentException("The value '" + value + "' is not element of this combobox");
		}
	}

	@Override
	public INPUT_TYPE getValue() {
		final Combo combo = getUiReference();
		final int selectedIndex = combo.getSelectionIndex();
		if (selectedIndex >= 0) {
			return content.get(selectedIndex);
		}
		return null;
	}

	@Override
	public List<INPUT_TYPE> getElements() {
		return Collections.unmodifiableList(content);
	}

	@Override
	public void setElements(final List<INPUT_TYPE> elements) {
		Assert.paramNotNull(elements, "elements");
		final Combo combo = getUiReference();

		combo.removeAll();
		content.clear();

		for (final INPUT_TYPE element : elements) {
			combo.add(objectStringConverter.convertToString(element));
			content.add(element);
		}

		if (elements.size() > 0) {
			combo.select(0);
		}
	}

	private void setToolTip() {
		final Combo combo = getUiReference();

		if (combo.getItemCount() > 0) {
			final INPUT_TYPE value = getValue();
			combo.setToolTipText(objectStringConverter.getDescription(value));
		}
		else {
			combo.setToolTipText(null);
		}
	}

}

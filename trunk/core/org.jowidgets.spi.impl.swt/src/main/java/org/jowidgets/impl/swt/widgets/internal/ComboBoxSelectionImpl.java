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
package org.jowidgets.impl.swt.widgets.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.util.Assert;

public class ComboBoxSelectionImpl extends AbstractInputComponent implements IComboBoxSelectionSpi {

	public ComboBoxSelectionImpl(final Object parentUiReference, final IComboBoxSelectionSetupSpi setup) {
		this(new Combo((Composite) parentUiReference, SWT.NONE | SWT.READ_ONLY), setup);
	}

	public ComboBoxSelectionImpl(final Combo combo, final IComboBoxSelectionSetupSpi setup) {
		super(combo);

		setElements(setup.getElements());

		getUiReference().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				fireInputChanged(getUiReference());
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
	public String[] getElements() {
		return getUiReference().getItems();
	}

	@Override
	public void setElements(final String[] elements) {
		Assert.paramNotNull(elements, "elements");
		getUiReference().setItems(elements);
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectionIndex();
	}

	@Override
	public void setSelectedIndex(final int index) {
		getUiReference().select(index);
	}

	@Override
	public void setTooltipText(final String toolTiptext) {
		getUiReference().setToolTipText(toolTiptext);
	}

}

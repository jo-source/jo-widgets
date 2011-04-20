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
package org.jowidgets.spi.impl.swing.widgets;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;

public class ComboBoxSelectionImpl extends AbstractInputControl implements IComboBoxSelectionSpi {

	public ComboBoxSelectionImpl(final IComboBoxSelectionSetupSpi setup) {
		super(new JComboBox(new DefaultComboBoxModel(setup.getElements())));

		addItemListener();
	}

	@Override
	public JComboBox getUiReference() {
		return (JComboBox) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEnabled(editable);
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectedIndex();
	}

	@Override
	public void setSelectedIndex(final int index) {
		getUiReference().setSelectedIndex(index);
	}

	@Override
	public void setTooltipText(final String tooltipText) {
		getUiReference().setToolTipText(tooltipText);
	}

	@Override
	public String[] getElements() {
		final ComboBoxModel model = getUiReference().getModel();
		final String[] result = new String[model.getSize()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (String) model.getElementAt(i);
		}
		return result;
	}

	@Override
	public void setElements(final String[] elements) {
		getUiReference().setModel(new DefaultComboBoxModel(elements));
	}

	private void addItemListener() {
		final JComboBox comboBox = getUiReference();
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getID() == ItemEvent.ITEM_STATE_CHANGED && e.getStateChange() == ItemEvent.SELECTED) {
					fireInputChanged();
				}
			}
		});

	}

}

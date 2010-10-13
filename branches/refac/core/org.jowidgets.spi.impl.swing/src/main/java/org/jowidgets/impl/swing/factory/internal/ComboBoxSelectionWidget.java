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
package org.jowidgets.impl.swing.factory.internal;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.MutableComboBoxModel;

import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.util.ColorSettingsInvoker;
import org.jowidgets.api.widgets.setup.IComboBoxSelectionSetupCommon;
import org.jowidgets.spi.widgets.IComboBoxWidgetSpi;
import org.jowidgets.util.Assert;

public class ComboBoxSelectionWidget<INPUT_TYPE> extends AbstractSwingInputWidget<INPUT_TYPE> implements
		IComboBoxWidgetSpi<INPUT_TYPE> {

	public ComboBoxSelectionWidget(final IComboBoxSelectionSetupCommon<?, INPUT_TYPE> descriptor) {
		super(new JComboBox(new DefaultComboBoxModel(descriptor.getElements().toArray())));

		ColorSettingsInvoker.setColors(descriptor, this);

		final IObjectStringConverter<INPUT_TYPE> objectStringConverter = descriptor.getObjectStringConverter();

		setRenderer(objectStringConverter);
		addItemListener(objectStringConverter);

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
	public void setValue(final INPUT_TYPE content) {
		getUiReference().setSelectedItem(content);
	}

	@SuppressWarnings("unchecked")
	@Override
	public INPUT_TYPE getValue() {
		final Object selectedItem = getUiReference().getSelectedItem();
		if (selectedItem != null) {
			return (INPUT_TYPE) selectedItem;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<INPUT_TYPE> getElements() {
		final List<INPUT_TYPE> result = new LinkedList<INPUT_TYPE>();
		final ComboBoxModel model = getUiReference().getModel();
		for (int i = 0; i < model.getSize(); i++) {
			result.add((INPUT_TYPE) model.getElementAt(i));
		}
		return result;
	}

	@Override
	public void setElements(final List<INPUT_TYPE> elements) {
		Assert.paramNotNull(elements, "elements");
		final MutableComboBoxModel model = (MutableComboBoxModel) getUiReference().getModel();

		// remove all elements
		for (int i = 0; i < model.getSize(); i++) {
			model.removeElementAt(0);
		}

		// then add the new elements
		for (final INPUT_TYPE value : elements) {
			model.addElement(value);
		}
	}

	private void setRenderer(final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {
		getUiReference().setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = -4789973603601120367L;

			@Override
			public Component getListCellRendererComponent(
				final JList list,
				final Object value,
				final int index,
				final boolean isSelected,
				final boolean cellHasFocus) {

				final Component result;

				if (value != null) {
					@SuppressWarnings("unchecked")
					final INPUT_TYPE input = (INPUT_TYPE) value;

					final String valueAsString = objectStringConverter.convertToString(input);

					result = super.getListCellRendererComponent(list, valueAsString, index, isSelected, cellHasFocus);

					if (result instanceof JComponent) {
						((JComponent) result).setToolTipText(objectStringConverter.getDescription(input));
					}
				}
				else {
					result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}

				return result;
			}

		});
	}

	private void addItemListener(final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {
		final JComboBox comboBox = getUiReference();
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getID() == ItemEvent.ITEM_STATE_CHANGED && e.getStateChange() == ItemEvent.SELECTED) {
					fireInputChanged(getUiReference());
					comboBox.setToolTipText(objectStringConverter.getDescription(getValue()));
				}
			}
		});

	}

}

/*
 * Copyright (c) 2014, Michael
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

package org.jowidgets.examples.common.workbench.demo4.control;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.workbench.demo4.model.ByteValue;
import org.jowidgets.examples.common.workbench.demo4.model.ByteValue.ByteUnit;
import org.jowidgets.tools.controller.FocusObservable;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.AbstractInputControl;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.ValidationResult;

public final class ByteValueControl extends AbstractInputControl<ByteValue> {

	private final IInputField<Integer> valueField;
	private final IComboBox<ByteUnit> unitCmb;
	private final FocusObservable focusObservable;

	private boolean lastFocus;

	public ByteValueControl(final IComposite composite) {
		super(composite);

		this.focusObservable = new FocusObservable();

		composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0[]0", "0[]0"));

		final IComboBoxSelectionBluePrint<ByteUnit> unitCmbBp = BPF.comboBoxSelection(ByteUnit.values());
		unitCmbBp.autoCompletionOff().setValue(ByteUnit.MB);
		this.unitCmb = composite.add(unitCmbBp, "growx");

		final int height = unitCmb.getPreferredSize().getHeight();
		this.valueField = composite.add(0, BPF.inputFieldIntegerNumber(), "growx, w 0::, h " + height + "!");

		this.lastFocus = hasFocus();

		final IFocusListener focusListener = new FocusListener();
		valueField.addFocusListener(focusListener);
		unitCmb.addFocusListener(focusListener);
	}

	@Override
	public boolean hasModifications() {
		return valueField.hasModifications() || unitCmb.hasModifications();
	}

	@Override
	public void resetModificationState() {
		valueField.resetModificationState();
		unitCmb.resetModificationState();
	}

	@Override
	public void setEditable(final boolean editable) {
		valueField.setEditable(editable);
		unitCmb.setEditable(editable);
	}

	@Override
	public boolean isEditable() {
		return valueField.isEditable() && unitCmb.isEditable();
	}

	@Override
	public void setValue(final ByteValue value) {
		if (value != null) {
			valueField.setValue(Integer.valueOf(value.getValue()));
			unitCmb.setValue(value.getUnit());
		}
		else {
			valueField.setValue(null);
			unitCmb.setValue(ByteUnit.GB);
		}
	}

	@Override
	public ByteValue getValue() {
		final Integer numericValue = valueField.getValue();
		final ByteUnit unit = unitCmb.getValue();
		if (numericValue != null && unit != null) {
			return new ByteValue(numericValue, unit);
		}
		else {
			return null;
		}
	}

	@Override
	protected IValidationResult createValidationResult() {
		return ValidationResult.ok();
	}

	@Override
	public boolean requestFocus() {
		final boolean result = valueField.requestFocus();
		valueField.selectAll();
		return result;
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		valueField.addKeyListener(listener);
		unitCmb.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		valueField.removeKeyListener(listener);
		unitCmb.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener listener) {
		valueField.addMouseListener(listener);
		unitCmb.addMouseListener(listener);
	}

	@Override
	public void removeMouseListener(final IMouseListener listener) {
		valueField.removeMouseListener(listener);
		unitCmb.removeMouseListener(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		focusObservable.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		focusObservable.removeFocusListener(listener);
	}

	@Override
	public Dimension getPreferredSize() {
		final Dimension valueSize = valueField.getPreferredSize();
		final Dimension unitSize = unitCmb.getPreferredSize();
		return new Dimension(valueSize.getWidth() + unitSize.getWidth() + 2, unitSize.getHeight());
	}

	@Override
	public boolean hasFocus() {
		return valueField.hasFocus() || unitCmb.hasFocus();
	}

	private void focusChanged() {
		final boolean currentFocus = hasFocus();
		if (currentFocus != lastFocus) {
			lastFocus = currentFocus;
			if (currentFocus) {
				focusObservable.focusGained();
			}
			else {
				focusObservable.focusLost();
			}
		}
	}

	private final class FocusListener implements IFocusListener {

		@Override
		public void focusGained() {
			Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
				@Override
				public void run() {
					focusChanged();
				}
			});

		}

		@Override
		public void focusLost() {
			Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
				@Override
				public void run() {
					focusChanged();
				}
			});
		}

	}

}

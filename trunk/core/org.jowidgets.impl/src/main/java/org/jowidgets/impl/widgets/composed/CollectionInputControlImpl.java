/*
 * Copyright (c) 2011, Michael Grossmann, Nikolaus Moll
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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.layout.tablelayout.ITableLayout;
import org.jowidgets.api.layout.tablelayout.ITableLayoutBuilder;
import org.jowidgets.api.layout.tablelayout.ITableLayoutBuilder.ColumnMode;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputComponentValidationLabel;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ICollectionInputControlDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.impl.layout.ListLayout;
import org.jowidgets.impl.layout.tablelayout.TableRowLayout;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.tools.widgets.wrapper.CompositeWrapper;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;

public class CollectionInputControlImpl<INPUT_TYPE> extends ControlWrapper implements IInputControl<Collection<INPUT_TYPE>> {

	private final IBluePrintFactory bpf;
	private final ITableLayout tableCommon;
	private final IInputComponentValidationLabelBluePrint validationLabelBp;
	private final IButtonBluePrint removeButtonBp;
	private final Dimension removeButtonSize;
	private final ICustomWidgetCreator<IInputControl<INPUT_TYPE>> widgetCreator;
	private final Dimension validationLabelSize;
	private final ValuesContainer valuesContainer;
	private final IComposite addValueComposite;
	private final IButton addButton;

	private final InputObservable inputObservable;
	private final ValidationCache validationCache;
	private final CompoundValidator<Collection<INPUT_TYPE>> compoundValidator;
	private int lastHashCode;
	private boolean programmaticUpdate;

	public CollectionInputControlImpl(final IComposite composite, final ICollectionInputControlDescriptor<INPUT_TYPE> setup) {
		super(composite);
		this.bpf = Toolkit.getBluePrintFactory();
		this.inputObservable = new InputObservable();
		this.compoundValidator = new CompoundValidator<Collection<INPUT_TYPE>>();

		//Get some settings from setup
		this.removeButtonBp = bpf.button().setSetup(setup.getRemoveButton());
		this.removeButtonSize = setup.getRemoveButtonSize();
		this.widgetCreator = setup.getElementWidgetCreator();
		final IButtonBluePrint addButtonBp = bpf.button().setSetup(setup.getAddButton());

		if (setup.getValidationLabel() != null) {
			this.validationLabelBp = bpf.inputComponentValidationLabel();
			validationLabelBp.setSetup(setup.getValidationLabel());
		}
		else {
			this.validationLabelBp = null;
		}
		this.validationLabelSize = setup.getValidationLabelSize();

		final int columns = getColumnCount(setup);
		final int maxButtonWidth = Math.max(setup.getRemoveButtonSize().getWidth(), setup.getAddButtonSize().getWidth());

		final ITableLayoutBuilder rowLayoutCommonBuilder = Toolkit.getLayoutFactoryProvider().tableLayoutBuilder();
		rowLayoutCommonBuilder.layoutMinRows(2);
		rowLayoutCommonBuilder.columnCount(columns);
		rowLayoutCommonBuilder.gap(3);
		rowLayoutCommonBuilder.gapAfterColumn(columns - 1, 8);

		rowLayoutCommonBuilder.columnMode(2, ColumnMode.GROWING);
		rowLayoutCommonBuilder.fixedColumnWidth(1, maxButtonWidth);
		if (columns > 3) {
			rowLayoutCommonBuilder.fixedColumnWidth(3, 20);
		}
		this.tableCommon = rowLayoutCommonBuilder.build();

		//TODO NM proper handling of the non mandatory setup params 
		//(validation label and constraints may be null so do not render them)

		//TODO NM re-implement this example code 
		composite.setLayout(Toolkit.getLayoutFactoryProvider().listLayout());
		valuesContainer = new ValuesContainer(composite.add(bpf.composite()));
		valuesContainer.addRow();

		this.addValueComposite = composite.add(bpf.composite());
		addValueComposite.setLayout(tableCommon.rowBuilder().ignoreInCalculations(true).build());
		this.addButton = addValueComposite.add(addButtonBp, "index 1");
		addButton.setPreferredSize(setup.getAddButtonSize());
		addButton.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				valuesContainer.addRow().inputControl.requestFocus();
			}

		});

		if (setup.getValidator() != null) {
			compoundValidator.addValidator(setup.getValidator());
		}

		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				return compoundValidator.validate(getValue());
			}
		});

		tableCommon.validate();

		resetModificationState();
	}

	private int getColumnCount(final ICollectionInputControlDescriptor<INPUT_TYPE> setup) {
		if (setup.getValidationLabel() == null) {
			return 3;
		}
		else {
			return 4;
		}
	}

	private void updateLayout() {
		if (addValueComposite != null) {
			getParent().layoutBegin();
			getParent().layoutEnd();
		}
	}

	@Override
	public void setEditable(final boolean editable) {
		valuesContainer.setEditable(editable);
		addButton.setEnabled(editable);
	}

	@Override
	public void addValidator(final IValidator<Collection<INPUT_TYPE>> validator) {
		compoundValidator.addValidator(validator);
	}

	@Override
	public boolean hasModifications() {
		return lastHashCode == valuesContainer.getValue().hashCode();
	}

	@Override
	public void resetModificationState() {
		lastHashCode = valuesContainer.getValue().hashCode();
	}

	@Override
	public IValidationResult validate() {
		return validationCache.validate();
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.addValidationConditionListener(listener);
	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.removeValidationConditionListener(listener);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	private void fireInputChanged() {
		if (!programmaticUpdate) {
			inputObservable.fireInputChanged();
		}
	}

	@Override
	public void setValue(final Collection<INPUT_TYPE> value) {
		programmaticUpdate = true;
		valuesContainer.setValue(value);
		programmaticUpdate = false;
	}

	@Override
	public Collection<INPUT_TYPE> getValue() {
		return valuesContainer.getValue();
	}

	private final class Row extends CompositeWrapper {

		private final IInputControl<INPUT_TYPE> inputControl;
		private final IButton removeButton;
		private final IInputComponentValidationLabel validationLabel;
		private final TableRowLayout layout;
		private final ITextLabel valueIndex;

		private Row(final IComposite container) {
			super(container);
			layout = new TableRowLayout(container, tableCommon, false);
			setLayout(layout);

			final String userIndex = String.valueOf(valuesContainer.getValueCount() + 1);

			valueIndex = add(bpf.textLabel(userIndex));

			// TODO i18n
			removeButton = add(removeButtonBp.setToolTipText("Remove entry " + userIndex));
			removeButton.setPreferredSize(removeButtonSize);

			inputControl = add(widgetCreator);
			inputControl.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(final IKeyEvent event) {
					final int index = valuesContainer.indexOf(Row.this);

					if (VirtualKey.ENTER.equals(event.getVirtualKey())) {
						final int newIndex;
						if (event.getModifier().contains(Modifier.SHIFT)) {
							newIndex = index;
						}
						else {
							newIndex = index + 1;
						}
						final Row row = valuesContainer.addRow(newIndex);
						row.inputControl.requestFocus();
					}
					else if (VirtualKey.BACK_SPACE.equals(event.getVirtualKey())
						|| VirtualKey.DELETE.equals(event.getVirtualKey())) {
						boolean removeControl = (inputControl.getValue() == null);
						if (!removeControl) {
							final Object value = inputControl.getValue();
							if (value instanceof String) {
								if ("".equals(value)) {
									removeControl = true;
								}
							}
						}
						if (removeControl) {
							int delta = 0;
							if (VirtualKey.BACK_SPACE.equals(event.getVirtualKey())) {
								delta = -1;
							}

							valuesContainer.removeRow(index);
							final Row row = valuesContainer.getRow(Math.min(
									Math.max(0, index + delta),
									valuesContainer.getValueCount() - 1));
							if (row != null) {
								row.inputControl.requestFocus();
							}
						}
					}
				}

			});
			inputControl.addInputListener(new IInputListener() {

				@Override
				public void inputChanged() {
					fireInputChanged();
				}
			});

			removeButton.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					final int index = valuesContainer.indexOf(Row.this);
					valuesContainer.removeRow(index);
				}

			});

			if (validationLabelBp != null) {
				validationLabel = add(validationLabelBp.setInputComponent(inputControl));
				validationLabel.setPreferredSize(validationLabelSize);
			}
			else {
				validationLabel = null;
			}

			setTabOrder(Collections.singletonList(inputControl));
		}

		public void setValueIndex(final int index) {
			valueIndex.setText(String.valueOf(index));
			layout.invalidateControl(valueIndex);
		}

		public void setEditable(final boolean editable) {
			inputControl.setEditable(editable);
			removeButton.setEnabled(editable);
		}

		public void removeLayout() {
			layout.remove();
		}

		private INPUT_TYPE getValue() {
			return inputControl.getValue();
		}

		private void setValue(final INPUT_TYPE value) {
			inputControl.setValue(value);
		}

		private IControl getControl() {
			return getWidget();
		}
	}

	private class ValuesContainer extends CompositeWrapper {

		private final List<Row> rows;
		private final List<IControl> controls;

		public ValuesContainer(final IComposite widget) {
			super(widget);
			this.rows = new LinkedList<Row>();
			controls = new LinkedList<IControl>();
			setLayout(new ListLayout(this, new IColorConstant[0]));
		}

		public Collection<INPUT_TYPE> getValue() {
			final List<INPUT_TYPE> result = new LinkedList<INPUT_TYPE>();
			for (final Row row : rows) {
				result.add(row.getValue());
			}
			return result;
		}

		public int getValueCount() {
			return rows.size();
		}

		public int indexOf(final Row row) {
			return rows.indexOf(row);
		}

		public void setValue(final Collection<INPUT_TYPE> value) {
			clear();
			for (final INPUT_TYPE currentValue : value) {
				addRow().setValue(currentValue);
			}
		}

		@Override
		public List<IControl> getChildren() {
			return controls;
		}

		@Override
		public void redraw() {
			final int lastHash = tableCommon.getLayoutHashCode();
			tableCommon.validate();
			final boolean changed = (lastHash != tableCommon.getLayoutHashCode());
			if (changed) {
				for (final Row row : rows) {
					row.redraw();
				}
				addValueComposite.redraw();
			}
			layoutBegin();
			layoutEnd();
			super.redraw();
		}

		public void setEditable(final boolean editable) {
			for (final Row row : rows) {
				row.setEditable(editable);
			}
		}

		public Row addRow() {
			final Row row = new Row(add(bpf.composite()));
			rows.add(row);
			controls.add(row.getControl());
			updateLayout();

			fireInputChanged();
			return row;
		}

		public Row addRow(final int index) {
			layoutBegin();
			final Row result = new Row(add(index, bpf.composite(), null));

			rows.add(index, result);
			controls.add(index, result.getControl());
			for (int i = index; i < rows.size(); i++) {
				final Row row = rows.get(i);
				row.setValueIndex(i + 1);
			}

			layoutEnd();
			updateLayout();

			fireInputChanged();
			return result;
		}

		public void removeRow(final int index) {
			layoutBegin();
			final Row removedRow = rows.get(index);
			rows.remove(index);
			controls.remove(index);

			for (int i = index; i < rows.size(); i++) {
				final Row row = rows.get(i);
				row.setValueIndex(i + 1);
			}
			removedRow.removeLayout();
			remove(removedRow.getControl());
			layoutEnd();
			updateLayout();

			fireInputChanged();
		}

		public Row getRow(final int index) {
			if (index < 0 || index >= rows.size()) {
				return null;
			}

			return rows.get(index);
		}

		public void clear() {
			layoutBegin();
			for (final Row row : rows) {
				row.removeLayout();
				remove(row.getControl());
			}
			layoutEnd();
			rows.clear();
			updateLayout();

			fireInputChanged();
		}
	}
}

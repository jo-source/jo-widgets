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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputComponentValidationLabel;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ICollectionInputControlDescriptor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IKeyEvent;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.widgets.composed.CollectionInputControlImpl.RowLayout.ColumnMode;
import org.jowidgets.impl.widgets.composed.CollectionInputControlImpl.RowLayout.RowLayoutCommon;
import org.jowidgets.tools.controller.FocusAdapter;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.widgets.wrapper.CompositeWrapper;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class CollectionInputControlImpl<INPUT_TYPE> extends ControlWrapper implements IInputControl<Collection<INPUT_TYPE>> {

	private final IBluePrintFactory bpf;
	private final RowLayoutCommon rowLayoutCommon;
	private final IInputComponentValidationLabelBluePrint validationLabelBp;
	private final IButtonBluePrint removeButtonBp;
	private final Dimension removeButtonSize;
	private final ICustomWidgetCreator<IInputControl<INPUT_TYPE>> widgetCreator;
	private final Dimension validationLabelSize;
	private final List<Row> rows;
	private final IContainer rowContainer;
	private final IComposite addRowComposite;

	public CollectionInputControlImpl(final IComposite composite, final ICollectionInputControlDescriptor<INPUT_TYPE> setup) {
		super(composite);
		this.bpf = Toolkit.getBluePrintFactory();
		this.rows = new LinkedList<Row>();

		//Get some settings from setup
		this.removeButtonBp = bpf.button().setSetup(setup.getRemoveButton());
		this.removeButtonSize = setup.getRemoveButtonSize();
		this.widgetCreator = setup.getElementWidgetCreator();
		final IButtonBluePrint addButtonBp = bpf.button().setSetup(setup.getAddButton());
		this.validationLabelBp = bpf.inputComponentValidationLabel();
		validationLabelBp.setSetup(setup.getValidationLabel());
		this.validationLabelSize = setup.getValidationLabelSize();

		final int columns = getColumnCount(setup);

		this.rowLayoutCommon = new RowLayoutCommon(columns, 0);
		rowLayoutCommon.setGap(3);
		rowLayoutCommon.setGapAfterColumn(columns - 1, 8);
		rowLayoutCommon.setColumnMode(2, ColumnMode.Growing);
		rowLayoutCommon.setFixedWidth(1, Math.max(setup.getRemoveButtonSize().getWidth(), setup.getAddButtonSize().getWidth()));
		if (columns > 3) {
			rowLayoutCommon.setFixedWidth(3, 20);
		}

		//TODO NM proper handling of the non mandatory setup params 
		//(validation label and constraints may be null so do not render them)

		//TODO NM re-implement this example code 
		composite.setLayout(new ListLayout(composite));
		rowContainer = composite.add(bpf.composite());
		rowContainer.setLayout(new ListLayout(rowContainer));

		for (int i = 0; i < 10; i++) {
			addRow();
		}

		this.addRowComposite = composite.add(bpf.composite());
		addRowComposite.setLayout(new RowLayout(addRowComposite, rowLayoutCommon));
		final IButton addButton = addRowComposite.add(addButtonBp, 1);
		addButton.setPreferredSize(setup.getAddButtonSize());
		addButton.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				addRow();
			}

		});

		rowLayoutCommon.calculateLayout();
	}

	private int getColumnCount(final ICollectionInputControlDescriptor<INPUT_TYPE> setup) {
		if (setup.getValidationLabel() == null) {
			return 3;
		}
		else {
			return 4;
		}
	}

	private Row addRow() {
		final Row row = new Row(rowContainer.add(bpf.composite()), rows.size());
		rows.add(row);
		updateLayout();
		return row;
	}

	private Row addRow(final int index) {
		Row lastRow = addRow();
		for (int i = rows.size() - 1; i > index; i--) {
			final Row previousRow = rows.get(i - 1);
			final INPUT_TYPE value = previousRow.getValue();
			previousRow.setValue(null);
			lastRow.setValue(value);
			lastRow = previousRow;
		}
		return lastRow;
	}

	private void removeRow(final int index) {
		Row lastRow = rows.get(index);
		for (int i = index + 1; i < rows.size(); i++) {
			final Row previousRow = rows.get(i);
			final INPUT_TYPE value = previousRow.getValue();
			previousRow.setValue(null);
			lastRow.setValue(value);
			lastRow = previousRow;
		}
		lastRow.removeLayout();
		rowContainer.remove(lastRow.getControl());
		rows.remove(rows.size() - 1);

		updateLayout();
	}

	private Row getRow(final int index) {
		if (index < 0 || index >= rows.size()) {
			return null;
		}

		return rows.get(index);
	}

	private void clear() {
		rowContainer.layoutBegin();
		for (final Row row : rows) {
			row.removeLayout();
			rowContainer.remove(row.getControl());
		}
		rowContainer.layoutEnd();
		rows.clear();
		updateLayout();
	}

	private void updateLayout() {
		if (addRowComposite != null) {
			rowLayoutCommon.invalidate();
			getParent().layoutBegin();
			getParent().layoutEnd();
			rowContainer.redraw();
		}
	}

	@Override
	public void setEditable(final boolean editable) {

	}

	@Override
	public void addValidator(final IValidator<Collection<INPUT_TYPE>> validator) {

	}

	@Override
	public boolean hasModifications() {
		return false;
	}

	@Override
	public void resetModificationState() {}

	@Override
	public IValidationResult validate() {
		return ValidationResult.ok();
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {

	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {

	}

	@Override
	public void addInputListener(final IInputListener listener) {

	}

	@Override
	public void removeInputListener(final IInputListener listener) {

	}

	@Override
	public void setValue(final Collection<INPUT_TYPE> value) {
		clear();
		for (final INPUT_TYPE currentValue : value) {
			addRow().setValue(currentValue);
		}
	}

	@Override
	public Collection<INPUT_TYPE> getValue() {
		return null;
	}

	private final class Row extends CompositeWrapper {

		private final IInputControl<INPUT_TYPE> inputControl;
		private final IInputComponentValidationLabel validationLabel;
		private final RowLayout layout;

		private Row(final IComposite container, final int index) {
			super(container);
			layout = new RowLayout(container, rowLayoutCommon);
			setLayout(layout);

			final String userIndex = String.valueOf(index + 1);

			add(bpf.textLabel(userIndex));

			// TODO i18n
			final IButton removeButton = add(removeButtonBp.setToolTipText("Remove entry " + userIndex));
			removeButton.setPreferredSize(removeButtonSize);

			inputControl = add(widgetCreator);
			inputControl.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(final IKeyEvent event) {
					if (VirtualKey.ENTER.equals(event.getVirtualKey())) {
						final Row row = addRow(index + 1);
						row.inputControl.requestFocus();
					}
					else if (VirtualKey.BACK_SPACE.equals(event.getVirtualKey())) {
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
							removeRow(index);
							final Row row = getRow(Math.max(0, index - 1));
							if (row != null) {
								row.inputControl.requestFocus();
							}
						}
					}
				}

			});

			removeButton.addFocusListener(new FocusAdapter() {

				@Override
				public void focusGained() {
					inputControl.requestFocus();
					removeButton.redraw();
				}

			});
			removeButton.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					removeRow(index);
				}

			});

			if (validationLabelBp != null) {
				validationLabel = add(validationLabelBp.setInputComponent(inputControl));
				validationLabel.setPreferredSize(validationLabelSize);
			}
			else {
				validationLabel = null;
			}
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

	public static final class RowLayout implements ILayouter {
		public enum ColumnMode {
			Preferred,
			Growing,
			Fixed
		};

		public static final class RowLayoutCommon {
			private final int columnCount;
			private final int verticalGap;
			private final int[] widths;
			private final int[] gaps;
			private final ColumnMode[] modes;
			private int layoutHashCode;
			private int availableWidth;
			private final LinkedList<RowLayout> layouters;
			private Dimension preferredSize;

			public RowLayoutCommon(final int columnCount, final int verticalGap) {
				this.columnCount = columnCount;
				this.verticalGap = verticalGap;
				layouters = new LinkedList<RowLayout>();
				modes = new ColumnMode[columnCount];
				widths = new int[columnCount];
				gaps = new int[columnCount + 1];

				for (int i = 0; i < modes.length; i++) {
					modes[i] = ColumnMode.Preferred;
				}

				invalidate();
			}

			private void addRowLayout(final RowLayout rowLayout) {
				layouters.add(rowLayout);
				invalidate();
			}

			private void removeRowLayout(final RowLayout rowLayout) {
				layouters.remove(rowLayout);
				invalidate();
			}

			public void invalidate() {
				availableWidth = 0;
				layoutHashCode = 0;
			}

			public int getAvailableWidth() {
				return availableWidth;
			}

			public int getLayoutHashCode() {
				if (layoutHashCode == 0) {
					layoutHashCode = 17;
					layoutHashCode = 31 * layoutHashCode + availableWidth;
					for (int i = 0; i < widths.length; i++) {
						layoutHashCode = 31 * layoutHashCode + widths[i];
					}
					for (int i = 0; i < gaps.length; i++) {
						layoutHashCode = 31 * layoutHashCode + gaps[i];
					}
				}

				return layoutHashCode;
			}

			public void setFixedWidth(final int column, final int width) {
				widths[column] = width;
				modes[column] = ColumnMode.Fixed;
				invalidate();
			}

			public void setGap(final int gap) {
				for (int i = 0; i < gaps.length; i++) {
					gaps[i] = gap;
				}
				invalidate();
			}

			public void setGapBeforceColumn(final int column, final int gap) {
				gaps[column] = gap;
				invalidate();
			}

			public void setGapAfterColumn(final int column, final int gap) {
				gaps[column + 1] = gap;
				invalidate();
			}

			public void setColumnMode(final int column, final ColumnMode mode) {
				modes[column] = mode;
				invalidate();
			}

			private void calculateLayout() {
				if (layouters.size() < 2) {
					return;
				}

				final RowLayout rowLayout = layouters.get(0);
				final Rectangle clientArea = rowLayout.container.getClientArea();
				availableWidth = clientArea.getWidth();

				for (int i = 0; i < widths.length; i++) {
					if (modes[i] != ColumnMode.Fixed) {
						widths[i] = 0;
					}
				}

				int minHeight = 0;
				for (final RowLayout layouter : layouters) {
					for (int i = 0; i < columnCount; i++) {
						final IControl control = layouter.getChild(i);
						if (control == null) {
							continue;
						}
						final Dimension size = layouter.getPreferredSize(control);
						widths[i] = Math.max(widths[i], size.getWidth());
						minHeight = Math.max(minHeight, size.getHeight());
					}
				}

				int currentWidth = 0;
				for (final int width : widths) {
					currentWidth = currentWidth + width;
				}
				for (final int gap : gaps) {
					currentWidth = currentWidth + gap;
				}

				final int usedWidth = currentWidth;
				if (availableWidth > currentWidth) {
					final int additionalWidth = availableWidth - currentWidth;
					final List<Integer> growingColumns = new LinkedList<Integer>();
					for (int i = 0; i < widths.length; i++) {
						if (modes[i] == ColumnMode.Growing) {
							growingColumns.add(Integer.valueOf(i));
						}
					}

					int columnAdditional = additionalWidth / growingColumns.size();
					for (final Integer index : growingColumns) {
						widths[index] = widths[index] + columnAdditional;
						currentWidth = currentWidth + columnAdditional;
						columnAdditional = Math.min(columnAdditional, availableWidth - currentWidth);
					}
				}

				preferredSize = new Dimension(usedWidth, minHeight + 2 * verticalGap);
				layoutHashCode = 0;
			}

			public Dimension getPreferredSize() {
				return preferredSize;
			}
		}

		private final IContainer container;
		private final RowLayoutCommon rowLayoutCommon;
		private final HashMap<IControl, Dimension> preferredSizes;
		private final HashMap<Integer, IControl> controls;
		private int layoutHashCode;

		private RowLayout(final IContainer container, final RowLayoutCommon rowLayoutCommon) {
			this.container = container;
			this.rowLayoutCommon = rowLayoutCommon;
			this.preferredSizes = new HashMap<IControl, Dimension>();
			this.controls = new HashMap<Integer, IControl>();
			this.layoutHashCode = 0;
			// TODO NM remove again
			rowLayoutCommon.addRowLayout(this);
		}

		public IControl getChild(final int index) {
			if (controls.containsKey(Integer.valueOf(index))) {
				return controls.get(index);
			}

			final List<IControl> children = container.getChildren();
			boolean manualPositioning = false;
			for (final IControl control : children) {
				if (control.getLayoutConstraints() == null) {
					continue;
				}
				if (control.getLayoutConstraints() instanceof Integer) {
					manualPositioning = true;
					break;
				}
			}

			IControl result = null;
			if (manualPositioning) {
				for (final IControl control : children) {
					if (control.getLayoutConstraints() == null) {
						continue;
					}
					if (control.getLayoutConstraints() instanceof Integer) {
						final int currentIndex = (Integer) control.getLayoutConstraints();
						if (currentIndex == index) {
							result = control;
						}
						controls.put(currentIndex, control);
					}
				}
			}
			else {
				int currentIndex = 0;
				for (final IControl control : children) {
					controls.put(currentIndex, control);
					if (currentIndex == index) {
						result = control;
					}
					currentIndex++;
				}
			}

			return result;
		}

		private Dimension getPreferredSize(final IControl control) {
			if (!preferredSizes.containsKey(control)) {
				final Dimension size = control.getPreferredSize();
				if (size.getHeight() > 0) {
					preferredSizes.put(control, size);
				}
				return size;
			}
			return preferredSizes.get(control);
		}

		@Override
		public void layout() {
			final Rectangle clientArea = container.getClientArea();
			if (clientArea.getWidth() != rowLayoutCommon.getAvailableWidth()) {
				rowLayoutCommon.calculateLayout();
			}
			if (layoutHashCode == rowLayoutCommon.getLayoutHashCode()) {
				return;
			}
			layoutHashCode = rowLayoutCommon.getLayoutHashCode();

			int x = rowLayoutCommon.gaps[0];
			for (int index = 0; index < rowLayoutCommon.columnCount; index++) {
				final IControl control = getChild(index);
				final int width = rowLayoutCommon.widths[index];

				if (control != null) {
					final Dimension size = getPreferredSize(control);
					final int y = clientArea.getY() + (clientArea.getHeight() - size.getHeight()) / 2;

					control.setSize(width, size.getHeight());
					control.setPosition(x, y);
				}

				x = x + width + rowLayoutCommon.gaps[index + 1];
			}
		}

		@Override
		public Dimension getMinSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			return rowLayoutCommon.getPreferredSize();
		}

		@Override
		public Dimension getMaxSize() {
			return getPreferredSize();
		}

		@Override
		public void invalidate() {
			preferredSizes.clear();
			final Rectangle clientArea = container.getClientArea();
			if (clientArea.getWidth() != rowLayoutCommon.getAvailableWidth()) {
				rowLayoutCommon.invalidate();
			}
		}

		public void remove() {
			rowLayoutCommon.removeRowLayout(this);
		}
	}

	private static final class ListLayout implements ILayouter {

		private final IContainer container;
		private final HashMap<IControl, Dimension> preferredSizes;
		private Dimension preferredSize;

		private ListLayout(final IContainer container) {
			this.container = container;
			this.preferredSizes = new HashMap<IControl, Dimension>();
		}

		@Override
		public void layout() {
			final Rectangle clientArea = container.getClientArea();
			final int x = clientArea.getX();
			int y = clientArea.getY();
			final int width = clientArea.getWidth();

			for (final IControl control : container.getChildren()) {
				final Dimension size = getPreferredSize(control);

				control.setPosition(x, y);
				control.setSize(width, size.getHeight());
				y = y + size.getHeight();
			}
		}

		@Override
		public Dimension getMinSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			if (preferredSize == null) {
				calculateSizes();
			}
			return preferredSize;
		}

		@Override
		public Dimension getMaxSize() {
			return getPreferredSize();
		}

		@Override
		public void invalidate() {
			preferredSizes.clear();
			preferredSize = null;
		}

		private void calculateSizes() {
			//final Rectangle clientArea = container.getClientArea();
			int preferredWidth = 0;
			int preferredHeight = 0;
			for (final IControl control : container.getChildren()) {
				final boolean controlVisible = control.isVisible();
				if (!control.isEnabled()) {
					if (controlVisible) {
						control.setVisible(false);
					}
					continue;
				}

				if (!controlVisible) {
					control.setVisible(true);
				}

				final Dimension size = getPreferredSize(control);
				preferredWidth = Math.max(preferredWidth, size.getWidth());
				preferredHeight = preferredHeight + size.getHeight();
			}
			preferredSize = container.computeDecoratedSize(new Dimension(preferredWidth, preferredHeight));
		}

		private Dimension getPreferredSize(final IControl control) {
			if (!preferredSizes.containsKey(control)) {
				final Dimension size = control.getPreferredSize();
				if (size == null) {
					return new Dimension(0, 0);
				}
				if (size.getHeight() > 0) {
					preferredSizes.put(control, size);
				}
				return size;
			}

			return preferredSizes.get(control);
		}
	}
}

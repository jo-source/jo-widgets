/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.examples.common.workbench.demo1;

import java.util.List;

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputComposite;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.examples.common.demo.DemoForm1Creator;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractView;
import org.jowidgets.tools.command.EnabledChecker;
import org.jowidgets.tools.controler.TableDataModelAdapter;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo4 extends AbstractView implements IView {

	public static final String ID = ViewDemo4.class.getName();
	public static final String DEFAULT_LABEL = "Person";
	public static final String DEFAULT_TOOLTIP = "Person View";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.APPLICATION_FORM;

	public ViewDemo4(final IViewContext context, final ISimpleTableModel tableModel) {
		super(ID);

		final IInputComposite<List<String>> form = DemoForm1Creator.createDemoForm1(context.getContainer());
		form.setEditable(false);

		tableModel.addDataModelListener(new TableDataModelAdapter() {
			@Override
			public void selectionChanged() {
				final int selectedRow = tableModel.getFirstSelectedRow();
				if (selectedRow != -1 && tableModel.getRowTexts(selectedRow).size() == 9) {
					form.setValue(tableModel.getRowTexts(selectedRow));
					form.setEditable(true);
				}
				else {
					form.setValue(null);
					form.setEditable(false);
				}
			}

		});

		final IActionBuilder actionBuilder = Toolkit.getActionBuilderFactory().create();

		actionBuilder.setText("Save").setIcon(SilkIcons.DISK);

		final ICommandExecutor executor = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				final int selectedIndex = tableModel.getFirstSelectedRow();
				final List<String> formValue = form.getValue();
				if (form.validate().isOk() && !form.isEmpty() && selectedIndex > -1) {
					tableModel.setRowTexts(selectedIndex, formValue);
					form.setValue(formValue);
				}
			}
		};

		final EnabledChecker enabledChecker = new EnabledChecker() {

			@Override
			public IEnabledState getEnabledState() {
				if (tableModel.getColumnCount() != 9) {
					return EnabledState.disabled("This demo dialog only works, if all columns are shown in the table");
				}
				if (tableModel.getFirstSelectedRow() == -1) {
					return EnabledState.disabled("There is nothing selected");
				}
				final ValidationResult validationResult = form.validate();
				if (!validationResult.isOk()) {
					return EnabledState.disabled(validationResult.getWorstFirstMessage().getMessageText());
				}
				if (form.isEmpty()) {
					return EnabledState.disabled("Form is not filled out completely!");
				}
				return EnabledState.ENABLED;
			}

		};

		actionBuilder.setCommand(executor, enabledChecker);

		form.addInputListener(new IInputListener() {
			@Override
			public void inputChanged(final Object source) {
				enabledChecker.fireEnabledStateChanged();
			}
		});

		tableModel.addColumnModelListener(new ITableColumnModelListener() {

			@Override
			public void columnsStructureChanged() {
				enabledChecker.fireEnabledStateChanged();
			}

			@Override
			public void columnsRemoved(final int[] columnIndices) {
				enabledChecker.fireEnabledStateChanged();
			}

			@Override
			public void columnsAdded(final int[] columnIndices) {
				enabledChecker.fireEnabledStateChanged();
			}

			@Override
			public void columnsChanged(final int[] columnIndices) {}
		});

		tableModel.addDataModelListener(new TableDataModelAdapter() {
			@Override
			public void selectionChanged() {
				enabledChecker.fireEnabledStateChanged();
			}
		});

		context.getToolBar().addAction(actionBuilder.build());
	}
}

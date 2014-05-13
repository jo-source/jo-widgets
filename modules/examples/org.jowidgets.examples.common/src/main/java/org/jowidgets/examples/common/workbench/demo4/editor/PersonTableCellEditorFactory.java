/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.examples.common.workbench.demo4.editor;

import java.util.Date;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.widgets.editor.ITableCellEditor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.examples.common.workbench.demo4.control.ByteValueControl;
import org.jowidgets.examples.common.workbench.demo4.control.RoleInputControlCreator;
import org.jowidgets.examples.common.workbench.demo4.model.BeanTableModel;
import org.jowidgets.examples.common.workbench.demo4.model.ByteValue;
import org.jowidgets.examples.common.workbench.demo4.model.Gender;
import org.jowidgets.examples.common.workbench.demo4.model.Person;
import org.jowidgets.examples.common.workbench.demo4.model.Role;
import org.jowidgets.tools.editor.AbstractTableCellEditor;
import org.jowidgets.tools.editor.AbstractTableCellEditorFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.Assert;

public final class PersonTableCellEditorFactory extends AbstractTableCellEditorFactory<ITableCellEditor> {

	private final BeanTableModel<Person> model;

	public PersonTableCellEditorFactory(final BeanTableModel<Person> model) {
		Assert.paramNotNull(model, "model");
		this.model = model;
	}

	@Override
	public ITableCellEditor create(
		final ITableCell cell,
		final int row,
		final int column,
		final ICustomWidgetFactory widgetFactory) {

		if (column == 0) {
			return new NameEditor(widgetFactory);
		}
		else if (column == 1) {
			return new DayOfBirthEditor(widgetFactory);
		}
		else if (column == 2) {
			return new GenderEditor(widgetFactory);
		}
		else if (column == 3) {
			return new QuotaEditor(widgetFactory);
		}
		else if (column == 4) {
			return new RolesEditor(widgetFactory);
		}

		return null;
	}

	public static int getMaxHeight() {
		final IFrame fakeFrame = Toolkit.createRootFrame(BPF.frame());
		final IComboBox<Gender> comboBox = fakeFrame.add(BPF.comboBoxSelection(Gender.values()));
		final int result = comboBox.getPreferredSize().getHeight() + 1;
		fakeFrame.dispose();
		return result;
	}

	private final class NameEditor extends AbstractTableCellEditor {

		private final IInputField<String> editor;

		@SuppressWarnings("unchecked")
		private NameEditor(final ICustomWidgetFactory widgetFactory) {
			super(widgetFactory.create(BPF.inputFieldString()));
			this.editor = (IInputField<String>) super.getWidget();
		}

		@Override
		public void startEditing(final ITableCell cell, final int row, final int column) {
			editor.setValue(model.getBean(row).getName());
			editor.selectAll();
		}

		@Override
		public void stopEditing(final ITableCell cell, final int row, final int column) {
			model.getBean(row).setName(editor.getValue());
			model.fireDataChanged();
		}

	}

	private final class DayOfBirthEditor extends AbstractTableCellEditor {

		private final IInputField<Date> editor;

		@SuppressWarnings("unchecked")
		private DayOfBirthEditor(final ICustomWidgetFactory widgetFactory) {
			super(widgetFactory.create(BPF.inputFieldDate()));
			this.editor = (IInputField<Date>) super.getWidget();
		}

		@Override
		public void startEditing(final ITableCell cell, final int row, final int column) {
			editor.setValue(model.getBean(row).getDayOfBirth());
			editor.selectAll();
		}

		@Override
		public void stopEditing(final ITableCell cell, final int row, final int column) {
			model.getBean(row).setDayOfBirth(editor.getValue());
			model.fireDataChanged();
		}

	}

	private final class GenderEditor extends AbstractTableCellEditor {

		private final IInputControl<Gender> editor;

		@SuppressWarnings("unchecked")
		private GenderEditor(final ICustomWidgetFactory widgetFactory) {
			super(widgetFactory.create(BPF.comboBoxSelection(Gender.values())));
			this.editor = (IInputControl<Gender>) getWidget();
		}

		@Override
		public void startEditing(final ITableCell cell, final int row, final int column) {
			editor.setValue(model.getBean(row).getGender());
		}

		@Override
		public void stopEditing(final ITableCell cell, final int row, final int column) {
			model.getBean(row).setGender(editor.getValue());
			model.fireDataChanged();
		}

	}

	private final class QuotaEditor extends AbstractTableCellEditor {

		private final IInputControl<ByteValue> editor;

		@SuppressWarnings("unchecked")
		private QuotaEditor(final ICustomWidgetFactory widgetFactory) {
			super(new ByteValueControl(widgetFactory.create(BPF.composite())));
			this.editor = (IInputControl<ByteValue>) getWidget();
		}

		@Override
		public void startEditing(final ITableCell cell, final int row, final int column) {
			editor.setValue(model.getBean(row).getQuota());
		}

		@Override
		public void stopEditing(final ITableCell cell, final int row, final int column) {
			model.getBean(row).setQuota(editor.getValue());
			model.fireDataChanged();
		}

	}

	private final class RolesEditor extends AbstractTableCellEditor {

		private final IInputControl<List<Role>> editor;

		@SuppressWarnings("unchecked")
		private RolesEditor(final ICustomWidgetFactory widgetFactory) {
			super(new RoleInputControlCreator().create(widgetFactory));
			this.editor = (IInputControl<List<Role>>) getWidget();
		}

		@Override
		public void startEditing(final ITableCell cell, final int row, final int column) {
			editor.setValue(model.getBean(row).getRoles());
		}

		@Override
		public void stopEditing(final ITableCell cell, final int row, final int column) {
			model.getBean(row).setRoles(editor.getValue());
			model.fireDataChanged();
		}

	}

}

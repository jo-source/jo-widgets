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

package org.jowidgets.tools.editor;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.common.widgets.controller.ITableCellEvent;
import org.jowidgets.common.widgets.editor.ITableCellEditor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.Assert;

public class TextFieldCellEditorFactory extends AbstractTableCellEditorFactory<ITableCellEditor> {

    private final Set<ITableCellEditorListener> listeners;

    public TextFieldCellEditorFactory() {
        this.listeners = new LinkedHashSet<ITableCellEditorListener>();
    }

    @Override
    public ITableCellEditor create(
        final ITableCell cell,
        final int row,
        final int column,
        final ICustomWidgetFactory widgetFactory) {

        final InputVerifier inputVerifier = new InputVerifier();
        final IInputField<String> inputField = widgetFactory.create(BPF.inputFieldString().setInputVerifier(inputVerifier));
        final TextFieldEditor result = new TextFieldEditor(inputField);
        inputVerifier.setIndices(result);
        return result;
    }

    public void addTableCellEditorListener(final ITableCellEditorListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.add(listener);
    }

    public void removeTableCellEditorListener(final ITableCellEditorListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.remove(listener);
    }

    private final class TextFieldEditor extends AbstractTableCellEditor implements ICellIndicesProvider {

        private final IInputField<String> editor;
        private int row;
        private int column;

        @SuppressWarnings("unchecked")
        private TextFieldEditor(final IInputField<String> editor) {
            super(editor);
            this.editor = (IInputField<String>) super.getWidget();
        }

        @Override
        public void startEditing(final ITableCell cell, final int row, final int column) {
            editor.setValue(cell.getText());
            editor.selectAll();
            this.row = row;
            this.column = column;
        }

        @Override
        public void stopEditing(final ITableCell cell, final int row, final int column) {
            for (final ITableCellEditorListener listener : new LinkedList<ITableCellEditorListener>(listeners)) {
                listener.editFinished(new TableCellEditEvent(row, column, editor.getText()));
            }
        }

        @Override
        public void cancelEditing(final ITableCell cell, final int row, final int column) {
            for (final ITableCellEditorListener listener : new LinkedList<ITableCellEditorListener>(listeners)) {
                listener.editCanceled(new TableCellEditEvent(row, column, editor.getText()));
            }
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public int getColumn() {
            return column;
        }

    }

    private final class InputVerifier implements IInputVerifier {

        private ICellIndicesProvider indices;

        @Override
        public boolean verify(final String currentValue, final String input, final int start, final int end) {
            final VetoHolder vetoHolder = new VetoHolder();
            for (final ITableCellEditorListener listener : new LinkedList<ITableCellEditorListener>(listeners)) {
                listener.onEdit(vetoHolder, new TableCellEditEvent(indices.getRow(), indices.getColumn(), currentValue));
                if (vetoHolder.hasVeto()) {
                    break;
                }
            }
            return !vetoHolder.hasVeto();
        }

        private void setIndices(final ICellIndicesProvider indices) {
            this.indices = indices;
        }

    }

    private final class TableCellEditEvent implements ITableCellEditEvent {

        private final int rowIndex;
        private final int columnIndex;
        private final String currentText;

        private TableCellEditEvent(final int rowIndex, final int columnIndex, final String currentText) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.currentText = currentText;
        }

        @Override
        public int getRowIndex() {
            return rowIndex;
        }

        @Override
        public int getColumnIndex() {
            return columnIndex;
        }

        @Override
        public String getCurrentText() {
            return currentText;
        }

    }

    private interface ICellIndicesProvider {

        int getRow();

        int getColumn();

    }

    public interface ITableCellEditEvent extends ITableCellEvent {

        String getCurrentText();

    }

    public interface ITableCellEditorListener {

        void onEdit(IVetoable veto, ITableCellEditEvent event);

        void editFinished(ITableCellEditEvent event);

        void editCanceled(ITableCellEvent event);

    }

}

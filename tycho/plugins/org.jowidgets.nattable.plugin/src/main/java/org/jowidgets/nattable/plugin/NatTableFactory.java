/*
 * Copyright (c) 2010, grossmann, Nikolaus Moll
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

package org.jowidgets.nattable.plugin;

import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.descriptor.ITableDescriptor;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.widgets.editor.EditActivation;
import org.jowidgets.common.widgets.editor.ITableCellEditor;
import org.jowidgets.common.widgets.editor.ITableCellEditorFactory;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.ITableBluePrintSpi;
import org.jowidgets.impl.widgets.basic.TableImpl;
import org.jowidgets.impl.widgets.basic.TableModelSpiAdapter;
import org.jowidgets.nattable.plugin.layer.NatTableLayers;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;

public class NatTableFactory implements IWidgetFactory<ITable, ITableDescriptor> {

	private final SwtImageRegistry imageRegistry;
	private final IGenericWidgetFactory genericWidgetFactory;
	private final ISpiBluePrintFactory bpF;

	public NatTableFactory(
		final SwtImageRegistry imageRegistry,
		final IGenericWidgetFactory genericWidgetFactory,
		final ISpiBluePrintFactory bpF) {

		Assert.paramNotNull(imageRegistry, "imageRegistry");
		Assert.paramNotNull(genericWidgetFactory, "genericWidgetFactory");
		Assert.paramNotNull(bpF, "bpF");

		this.imageRegistry = imageRegistry;
		this.genericWidgetFactory = genericWidgetFactory;
		this.bpF = bpF;
	}

	@Override
	public ITable create(final Object parentUiReference, final ITableDescriptor descriptor) {
		final ITableBluePrintSpi bp = bpF.table();
		bp.setSetup(descriptor);

		final ITableColumnModel columnModel = descriptor.getColumnModel();
		final ITableDataModel dataModel = descriptor.getDataModel();
		final TableModelSpiAdapter modelSpiAdapter = new TableModelSpiAdapter(columnModel, dataModel);

		bp.setColumnModel(modelSpiAdapter);
		bp.setDataModel(modelSpiAdapter);

		final ITableCellEditorFactory<? extends ITableCellEditor> editor = bp.getEditor();
		if (editor != null) {
			bp.setEditor(new TableCellEditorFactoryDecorator(editor, modelSpiAdapter));
		}

		//TODO only this differs really from original factory so reduce redundant code ----BEGIN OF INDIVIDUAL CODE
		final NatTableLayers nattableLayers = new NatTableLayers(bp.getDataModel(), bp.getColumnModel(), bp.getSelectionPolicy());
		final ITableSpi tableSpi = new NatTableImplSpi(
			nattableLayers,
			genericWidgetFactory,
			parentUiReference,
			bp,
			imageRegistry);

		//final ITableSpi tableSpi = new SwtTableImplSpi(genericWidgetFactory, parentUiReference, tableBpSpi, imageRegistry);
		//TODO only this differs really from original factory so reduce redundant code ----END OF INDIVIDUAL CODE

		modelSpiAdapter.setTable(tableSpi);
		return new TableImpl(tableSpi, descriptor, modelSpiAdapter);
	}

	private final class TableCellEditorFactoryDecorator implements ITableCellEditorFactory<ITableCellEditor> {

		private final TableModelSpiAdapter modelSpiAdapter;
		private final ITableCellEditorFactory<? extends ITableCellEditor> original;

		TableCellEditorFactoryDecorator(
			final ITableCellEditorFactory<? extends ITableCellEditor> original,
			final TableModelSpiAdapter modelSpiAdapter) {
			this.modelSpiAdapter = modelSpiAdapter;
			this.original = original;
		}

		@Override
		public ITableCellEditor create(
			final ITableCell cell,
			final int row,
			final int column,
			final ICustomWidgetFactory widgetFactory) {
			final ITableCellEditor result = original.create(cell, row, modelSpiAdapter.convertViewToModel(column), widgetFactory);
			if (result instanceof IControl) {
				return new DecoratedTableCellEditor((IControl) result, result, modelSpiAdapter);
			}
			else {
				return result;
			}
		}

		@Override
		public EditActivation getActivation(
			final ITableCell cell,
			final int row,
			final int column,
			final boolean editMode,
			final long editModeStopped) {
			return original.getActivation(cell, row, modelSpiAdapter.convertViewToModel(column), editMode, editModeStopped);
		}

	}

	private final class DecoratedTableCellEditor extends ControlWrapper implements ITableCellEditor {

		private final TableModelSpiAdapter modelSpiAdapter;
		private final ITableCellEditor original;

		private DecoratedTableCellEditor(
			final IControl widget,
			final ITableCellEditor original,
			final TableModelSpiAdapter modelSpiAdapter) {
			super(widget);
			this.modelSpiAdapter = modelSpiAdapter;
			this.original = original;
		}

		@Override
		public void startEditing(final ITableCell cell, final int row, final int column) {
			original.startEditing(cell, row, modelSpiAdapter.convertViewToModel(column));
		}

		@Override
		public void stopEditing(final ITableCell cell, final int row, final int column) {
			original.stopEditing(cell, row, modelSpiAdapter.convertViewToModel(column));
			getWidget().dispose();
		}

		@Override
		public void cancelEditing(final ITableCell cell, final int row, final int column) {
			original.cancelEditing(cell, row, modelSpiAdapter.convertViewToModel(column));
			getWidget().dispose();
		}

	}
}

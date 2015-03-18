/*
 * Copyright (c) 2013, grossmann
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
package org.jowidgets.examples.common.snipped;

import java.util.Arrays;
import java.util.Comparator;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.model.table.AbstractTableModel;
import org.jowidgets.tools.model.table.DefaultTableColumn;
import org.jowidgets.tools.model.table.TableCell;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.Assert;

public final class ColorTableSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        //create a frame with fill layout
        final IFrameBluePrint frameBp = BPF.frame().setTitle("Colors table");
        frameBp.setSize(new Dimension(400, 600));
        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
        frame.setLayout(FillLayout.get());

        //Create the table
        final ITable table = frame.add(BPF.table(new ColorTableModel(Colors.class)));
        table.setRowHeight(20);

        //set the root frame visible
        frame.setVisible(true);

        //pack the table after it has become visible
        table.pack();
    }

    private final class ColorTableModel extends AbstractTableModel {

        private final IColorConstant[] colors;

        private ColorTableModel(final Class<? extends Enum<? extends IColorConstant>> colorsClass) {
            this((IColorConstant[]) Assert.getParamNotNull(colorsClass, "colorsClass").getEnumConstants());
        }

        private ColorTableModel(final IColorConstant[] colors) {
            Assert.paramNotNull(colors, "colors");

            this.colors = new IColorConstant[colors.length];
            System.arraycopy(colors, 0, this.colors, 0, colors.length);
            Arrays.sort(this.colors, new Comparator<IColorConstant>() {
                @Override
                public int compare(final IColorConstant o1, final IColorConstant o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public ITableColumn getColumn(final int columnIndex) {
            final IDefaultTableColumnBuilder columnBuilder = DefaultTableColumn.builder();
            if (columnIndex == 0) {
                columnBuilder.setAlignment(AlignmentHorizontal.CENTER);
                return columnBuilder.setText("Color").build();
            }
            else if (columnIndex == 1) {
                return columnBuilder.setText("Name").build();
            }
            else {
                return null;
            }
        }

        @Override
        public int getRowCount() {
            return colors.length;
        }

        @Override
        public ITableCell getCell(final int rowIndex, final int columnIndex) {
            final ITableCellBuilder builder = TableCell.builder();
            if (columnIndex == 0) {
                return builder.setBackgroundColor(colors[rowIndex]).build();
            }
            else if (columnIndex == 1) {
                return builder.setText(colors[rowIndex].toString()).build();
            }
            else {
                return null;
            }

        }

    }
}

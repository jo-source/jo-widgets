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

package org.jowidgets.tools.model.table;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.jowidgets.common.model.ITableCell;
import org.jowidgets.tools.controller.TableDataModelAdapter;
import org.jowidgets.util.ValueHolder;
import org.junit.Assert;
import org.junit.Test;

public class AbstractTableModelTest {

    @Test
    public void testSetSelection() {
        final TableModel tableModel = new TableModel();

        final ValueHolder<Collection<Integer>> currentSelection = new ValueHolder<Collection<Integer>>();
        final AtomicInteger listenerInvocations = new AtomicInteger(0);

        tableModel.addDataModelListener(new TableDataModelAdapter() {

            @Override
            public void selectionChanged() {
                listenerInvocations.incrementAndGet();
                currentSelection.set(tableModel.getSelection());
            }

        });

        Assert.assertEquals(0, listenerInvocations.get());

        final List<Integer> selection1 = new LinkedList<Integer>();
        selection1.add(1);
        selection1.add(2);
        selection1.add(3);

        tableModel.setSelection(selection1);
        Assert.assertEquals(1, listenerInvocations.get());
        Assert.assertEquals(3, tableModel.getSelection().size());
        Assert.assertTrue(tableModel.getSelection().contains(1));
        Assert.assertTrue(tableModel.getSelection().contains(2));
        Assert.assertTrue(tableModel.getSelection().contains(3));

        //check if there will no event be fired if selection changed
        tableModel.setSelection(selection1);
        Assert.assertEquals(1, listenerInvocations.get());

        //use linked hash set for selection
        final Set<Integer> selection2 = new LinkedHashSet<Integer>();
        selection2.add(1);
        selection2.add(2);
        selection2.add(3);

        //check that selection has not been changed
        tableModel.setSelection(selection2);
        Assert.assertEquals(1, listenerInvocations.get());

        //use hash set for selection
        final Set<Integer> selection3 = new HashSet<Integer>();
        selection3.add(1);
        selection3.add(2);
        selection3.add(3);

        //check that selection has not been changed
        tableModel.setSelection(selection3);
        Assert.assertEquals(1, listenerInvocations.get());

        //use linked hash set  in different order for selection
        final Set<Integer> selection4 = new LinkedHashSet<Integer>();
        selection4.add(3);
        selection4.add(2);
        selection4.add(1);

        //check that selection has not been changed
        tableModel.setSelection(selection4);
        Assert.assertEquals(1, listenerInvocations.get());

    }

    private class TableModel extends AbstractTableDataModel {

        @Override
        public int getRowCount() {
            return 100;
        }

        @Override
        public ITableCell getCell(final int rowIndex, final int columnIndex) {
            return TableCell.builder("" + rowIndex + " / " + columnIndex).build();
        }

    }
}

/*
 * Copyright (c) 2017, herrg
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

package org.jowidgets.nattable.impl.plugin.layer;

import static org.eclipse.nebula.widgets.nattable.selection.SelectionUtils.isControlOnly;

import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.selection.SelectCellCommandHandler;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.swt.graphics.Rectangle;
import org.jowidgets.util.Interval;

/**
 * Specific implementation of {@link SelectCellCommandHandler} that overrides the default behavior
 * of nat table selection with a windows conform version.
 */
final class JoSelectCellCommandHandler extends SelectCellCommandHandler {

    private final JoSelectionLayer selectionLayer;

    JoSelectCellCommandHandler(final JoSelectionLayer selectionLayer) {
        super(selectionLayer);
        this.selectionLayer = selectionLayer;
    }

    /**
     * Toggles the selection state of the given row and column.
     */
    @Override
    protected void toggleCell(
        final int columnPosition,
        final int rowPosition,
        final boolean withShiftMask,
        final boolean withControlMask,
        final boolean forcingEntireCellIntoViewport) {
        boolean selectCell = true;
        if (isControlOnly(withShiftMask, withControlMask)) {
            if (this.selectionLayer.isCellPositionSelected(columnPosition, rowPosition)) {
                final ILayerCell cell = this.selectionLayer.getCellByPosition(columnPosition, rowPosition);
                final Rectangle cellRect = new Rectangle(
                    cell.getOriginColumnPosition(),
                    cell.getOriginRowPosition(),
                    cell.getColumnSpan(),
                    cell.getRowSpan());

                this.selectionLayer.clearSelection(cellRect);

                //In windows the selection anchor will even be changed on deselect with ctrl!
                //The anchor and last selected must be reset after clear, because clear deletes
                //the last selection and anchor if inside cleared rectangle
                this.selectionLayer.setLastSelectedRegion(cellRect);
                this.selectionLayer.moveSelectionAnchor(cellRect.x, cellRect.y);

                selectCell = false;
            }
        }
        if (selectCell) {
            selectCell(columnPosition, rowPosition, withShiftMask, withControlMask);
        }
    }

    @Override
    public void selectCell(
        final int columnPosition,
        final int rowPosition,
        final boolean withShiftMask,
        final boolean withControlMask) {

        if (!withShiftMask && !withControlMask) {
            this.selectionLayer.clear(false);
        }

        final ILayerCell cell = this.selectionLayer.getCellByPosition(columnPosition, rowPosition);

        if (cell != null) {
            this.selectionLayer.setLastSelectedCell(cell.getOriginColumnPosition(), cell.getOriginRowPosition());

            if (this.selectionLayer.getSelectionModel().isMultipleSelectionAllowed()
                && withShiftMask
                && this.selectionLayer.getLastSelectedRegion() != null
                && this.selectionLayer.hasRowSelection()
                && (this.selectionLayer.getSelectionAnchor().rowPosition != SelectionLayer.NO_SELECTION)
                && (this.selectionLayer.getSelectionAnchor().columnPosition != SelectionLayer.NO_SELECTION)) {

                final Rectangle lastSelected = copyRectangle(this.selectionLayer.getLastSelectedRegion());

                final Interval<Integer> newSelection = createOrderedInterval(
                        this.selectionLayer.getSelectionAnchor().rowPosition,
                        cell.getOriginRowPosition());

                lastSelected.height = calcDistance(newSelection) + 1;
                lastSelected.y = newSelection.getLeftBoundary();

                if (!withControlMask) {
                    selectionLayer.getSelectionModel().clearSelection();
                }

                this.selectionLayer.setLastSelectedRegion(lastSelected);
                this.selectionLayer.addSelection(lastSelected);
            }
            else {
                this.selectionLayer.setLastSelectedRegion(null);
                final Rectangle selection = new Rectangle(
                    cell.getOriginColumnPosition(),
                    cell.getOriginRowPosition(),
                    cell.getColumnSpan(),
                    cell.getRowSpan());

                this.selectionLayer.addSelection(selection);
            }
        }
    }

    private int calcDistance(final Interval<Integer> interval) {
        return Math.abs(interval.getRightBoundary() - interval.getLeftBoundary());
    }

    private Interval<Integer> createOrderedInterval(final int a, final int b) {
        return new Interval<Integer>(Math.min(a, b), Math.max(a, b));
    }

    private Rectangle copyRectangle(final Rectangle original) {
        return new Rectangle(original.x, original.y, original.width, original.height);
    }

}

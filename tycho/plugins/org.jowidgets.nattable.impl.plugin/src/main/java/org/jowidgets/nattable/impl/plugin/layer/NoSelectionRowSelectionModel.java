/*
 * Copyright (c) 2016, herrg
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.nebula.widgets.nattable.coordinate.Range;
import org.eclipse.nebula.widgets.nattable.layer.event.IStructuralChangeEvent;
import org.eclipse.nebula.widgets.nattable.selection.IRowSelectionModel;
import org.eclipse.swt.graphics.Rectangle;

final class NoSelectionRowSelectionModel implements IRowSelectionModel<Integer> {

	@Override
	public List<Integer> getSelectedRowObjects() {
		return Collections.emptyList();
	}

	@Override
	public void clearSelection(final Integer rowObject) {}

	@Override
	public void handleLayerEvent(final IStructuralChangeEvent event) {}

	@Override
	public Class<IStructuralChangeEvent> getLayerEventClass() {
		return IStructuralChangeEvent.class;
	}

	@Override
	public void setMultipleSelectionAllowed(final boolean multipleSelectionAllowed) {}

	@Override
	public boolean isRowPositionSelected(final int rowPosition) {
		return false;
	}

	@Override
	public boolean isRowPositionFullySelected(final int rowPosition, final int rowWidth) {
		return false;
	}

	@Override
	public boolean isMultipleSelectionAllowed() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean isColumnPositionSelected(final int columnPosition) {
		return false;
	}

	@Override
	public boolean isColumnPositionFullySelected(final int columnPosition, final int columnHeight) {
		return false;
	}

	@Override
	public boolean isCellPositionSelected(final int columnPosition, final int rowPosition) {
		return false;
	}

	@Override
	public List<Rectangle> getSelections() {
		return Collections.emptyList();
	}

	@Override
	public Set<Range> getSelectedRowPositions() {
		return Collections.emptySet();
	}

	@Override
	public int getSelectedRowCount() {
		return 0;
	}

	@Override
	public int[] getSelectedColumnPositions() {
		return new int[0];
	}

	@Override
	public int[] getFullySelectedRowPositions(final int rowWidth) {
		return new int[0];
	}

	@Override
	public int[] getFullySelectedColumnPositions(final int columnHeight) {
		return new int[0];
	}

	@Override
	public void clearSelection(final int columnPosition, final int rowPosition) {}

	@Override
	public void clearSelection(final Rectangle removedSelection) {}

	@Override
	public void clearSelection() {}

	@Override
	public void addSelection(final int columnPosition, final int rowPosition) {}

	@Override
	public void addSelection(final Rectangle range) {}
}

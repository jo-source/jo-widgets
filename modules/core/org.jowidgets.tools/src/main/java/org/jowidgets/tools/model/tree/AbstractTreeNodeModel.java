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

package org.jowidgets.tools.model.tree;

import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.types.CheckedState;
import org.jowidgets.util.Assert;

public abstract class AbstractTreeNodeModel<VALUE_TYPE> extends TreeNodeModelObservable implements ITreeNodeModel<VALUE_TYPE> {

	private CheckedState checkedState;

	private boolean selected;
	private boolean expanded;

	public AbstractTreeNodeModel() {
		this.checkedState = CheckedState.UNCHECKED;
	}

	@Override
	public final boolean isSelected() {
		return selected;
	}

	@Override
	public final void setSelected(final boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			fireSelectionChanged();
		}
	}

	@Override
	public final boolean isExpanded() {
		return expanded;
	}

	@Override
	public CheckedState getCheckedState() {
		return checkedState;
	}

	@Override
	public void setCheckedState(final CheckedState state) {
		Assert.paramNotNull(state, "state");
		if (!this.checkedState.equals(state)) {
			this.checkedState = state;
			fireCheckedChanged();
		}
	}

	@Override
	public final void setExpanded(final boolean expanded) {
		if (this.expanded != expanded) {
			this.expanded = expanded;
			fireExpansionChanged();
		}
	}

}

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

package org.jowidgets.impl.event;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.controler.ITreeSelectionEvent;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.util.Assert;

public final class TreeSelectionEvent implements ITreeSelectionEvent {

	private final SelectionPolicy selectionPolicy;
	private final List<ITreeNode> selected;
	private final List<ITreeNode> unselected;

	public TreeSelectionEvent(
		final SelectionPolicy selectionPolicy,
		final List<ITreeNode> selected,
		final List<ITreeNode> unselected) {

		Assert.paramNotNull(selectionPolicy, "selectionPolicy");
		Assert.paramNotNull(selected, "selected");
		Assert.paramNotNull(unselected, "unselected");

		this.selectionPolicy = selectionPolicy;
		this.selected = selected;
		this.unselected = unselected;
	}

	@Override
	public List<ITreeNode> getSelected() {
		return new LinkedList<ITreeNode>(selected);
	}

	@Override
	public List<ITreeNode> getUnselected() {
		return new LinkedList<ITreeNode>(unselected);
	}

	@Override
	public ITreeNode getSelectedSingle() {
		checkSelectionPolicy();
		if (selected.size() > 0) {
			return selected.get(0);
		}
		return null;
	}

	@Override
	public ITreeNode getUnselectedSingle() {
		checkSelectionPolicy();
		if (selected.size() > 0) {
			return unselected.get(0);
		}
		return null;
	}

	private void checkSelectionPolicy() {
		if (selectionPolicy != SelectionPolicy.SINGLE_SELECTION) {
			throw new UnsupportedOperationException("getSelectedSingle() is only supported for 'SingleSelectionPolicy'");
		}
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append(TreeSelectionEvent.class.getSimpleName() + " unselected: {");
		for (final ITreeNode unselectedNode : unselected) {
			result.append(unselectedNode + ", ");
		}
		if (unselected.size() > 0) {
			result.replace(result.length() - 2, result.length(), "} ");
		}
		else {
			result.append("} ");
		}

		result.append(TreeSelectionEvent.class.getSimpleName() + " selected: {");
		for (final ITreeNode selectedNode : selected) {
			result.append(selectedNode + ", ");
		}
		if (selected.size() > 0) {
			result.replace(result.length() - 2, result.length(), "} ");
		}
		else {
			result.append("} ");
		}

		return result.toString();
	}

}

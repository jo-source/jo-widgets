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

package org.jowidgets.api.widgets;

import java.util.List;

import org.jowidgets.api.widgets.descriptor.ITreeNodeDescriptor;

public interface ITreeContainer {

	ITreeNode addNode();

	ITreeNode addNode(int index);

	ITreeNode addNode(ITreeNodeDescriptor descriptor);

	ITreeNode addNode(int index, ITreeNodeDescriptor descriptor);

	void removeNode(ITreeNode node);

	void removeNode(int index);

	void removeAllNodes();

	List<ITreeNode> getChildren();

	ITreeContainer getParentContainer();

	void setAllChildrenExpanded(boolean expanded);

	/**
	 * Sets all children expanded or collapsed until a pivot level is reached
	 * 
	 * @param pivotLevel The pivot level to use, assuming this node has level 0, the children level 1 and so on
	 * 
	 *            If expanded is true, all nodes of this level and below will be expanded
	 *            If expanded is false, all node of this level and above will be collapsed
	 *            If expanded is null, the expansion is unbound
	 * 
	 * @param expanded If true, nodes will be expanded, I false nodes will be collapsed
	 * 
	 * @throws IllegalArgumentException if the level is not greater or equal 0
	 */
	void setAllChildrenExpanded(Integer pivotLevel, boolean expanded);

	void setAllChildrenChecked(boolean checked);

	int getLevel();

}

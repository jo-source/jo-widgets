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

package org.jowidgets.examples.common.tree.demo2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.types.CheckedState;
import org.jowidgets.tools.model.tree.TreeNodeModelAdapter;

public final class RootNodeModel extends StringNodeModel {

	private static final int LEVEL_1_COUNT = 5;
	private static final String LEVEL_1_PREFIX = "Level 1, Node ";

	private static final int LEVEL_2_COUNT = 3;
	private static final String LEVEL_2_PREFIX = "Level 2, Node ";

	private static final int LEVEL_3_COUNT = 3;
	private static final String LEVEL_3_PREFIX = "Level 3, Node ";

	private static final int LEVEL_4_COUNT = 10;
	private static final String LEVEL_4_PREFIX = "Level 4, Node ";

	private static final int LEVEL_5_COUNT = 205;
	private static final String LEVEL_5_PREFIX = "Level 5, Node ";

	RootNodeModel() {
		super();
		setChildren(createChildren());
		setCheckedState(CheckedState.CHECKED);
	}

	private List<ITreeNodeModel<String>> createChildren() {
		final List<ITreeNodeModel<String>> result = new ArrayList<ITreeNodeModel<String>>(LEVEL_1_COUNT);
		for (int i = 0; i < LEVEL_1_COUNT; i++) {
			final TreeNodeModelAdapter childCheckedListener = new TreeNodeModelAdapter() {
				@Override
				public void checkedChanged() {
					fireCheckedChanged();
				}
			};

			final ITreeNodeModel<String> level1Node = createLevel1Node(i, childCheckedListener);
			level1Node.addTreeNodeModelListener(childCheckedListener);
			result.add(level1Node);
		}
		return result;
	}

	private ITreeNodeModel<String> createLevel1Node(final int index, final TreeNodeModelAdapter checkedListener) {
		final List<ITreeNodeModel<String>> children = new ArrayList<ITreeNodeModel<String>>(LEVEL_2_COUNT);
		for (int i = 0; i < LEVEL_2_COUNT; i++) {
			final ITreeNodeModel<String> childNode = createLevel2Node(i, checkedListener);
			childNode.addTreeNodeModelListener(checkedListener);
			children.add(childNode);
		}
		return new StringNodeModel(LEVEL_1_PREFIX + index, children);
	}

	private ITreeNodeModel<String> createLevel2Node(final int index, final TreeNodeModelAdapter checkedListener) {
		final List<ITreeNodeModel<String>> children = new ArrayList<ITreeNodeModel<String>>(LEVEL_3_COUNT);
		for (int i = 0; i < LEVEL_3_COUNT; i++) {
			final ITreeNodeModel<String> childNode = createLevel3Node(i, checkedListener);
			childNode.addTreeNodeModelListener(checkedListener);
			children.add(childNode);
		}
		return new StringNodeModel(LEVEL_2_PREFIX + index, children);
	}

	private ITreeNodeModel<String> createLevel3Node(final int index, final TreeNodeModelAdapter checkedListener) {
		final List<ITreeNodeModel<String>> children = new ArrayList<ITreeNodeModel<String>>(LEVEL_4_COUNT);
		for (int i = 0; i < LEVEL_4_COUNT; i++) {
			final ITreeNodeModel<String> childNode = createLevel4Node(i, checkedListener);
			childNode.addTreeNodeModelListener(checkedListener);
			children.add(childNode);
		}
		return new StringNodeModel(LEVEL_3_PREFIX + index, children);
	}

	private ITreeNodeModel<String> createLevel4Node(final int index, final TreeNodeModelAdapter checkedListener) {
		final List<ITreeNodeModel<String>> children = new ArrayList<ITreeNodeModel<String>>(LEVEL_5_COUNT);
		for (int i = 0; i < LEVEL_5_COUNT; i++) {
			final ITreeNodeModel<String> childNode = createLevel5Node(i, checkedListener);
			childNode.addTreeNodeModelListener(checkedListener);
			children.add(childNode);
		}
		return new StringNodeModel(LEVEL_4_PREFIX + index, children);
	}

	private ITreeNodeModel<String> createLevel5Node(final int index, final TreeNodeModelAdapter checkedListener) {
		final List<ITreeNodeModel<String>> children = Collections.emptyList();
		final StringNodeModel result = new StringNodeModel(LEVEL_5_PREFIX + index, children);
		result.addTreeNodeModelListener(checkedListener);
		return result;
	}

}

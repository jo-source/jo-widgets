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

import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.model.tree.ITreeNodeRenderer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.tools.model.item.CheckedItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.tree.AbstractTreeNodeModel;
import org.jowidgets.tools.model.tree.DefaultTreeNodeRenderer;

class StringNodeModel extends AbstractTreeNodeModel<String> implements ITreeNodeModel<String> {

	private final ITreeNodeRenderer<String> renderer;
	private final String data;
	private final ICheckedItemModel checkableItem;
	private final ArrayList<ITreeNodeModel<String>> children;

	@SuppressWarnings("unchecked")
	StringNodeModel() {
		this(null, Collections.EMPTY_LIST);
	}

	StringNodeModel(final String data, final List<ITreeNodeModel<String>> children) {
		this.data = data;
		this.checkableItem = createCheckableItem();
		this.renderer = new TreeNodeRenderer();
		this.children = new ArrayList<ITreeNodeModel<String>>(children);
	}

	private ICheckedItemModel createCheckableItem() {
		final CheckedItemModel result = new CheckedItemModel("Checkable");
		result.setSelected(isCheckable());
		result.addItemModelListener(new IItemModelListener() {
			@Override
			public void itemChanged(final IItemModel item) {
				setCheckable(result.isSelected());
			}
		});
		return result;
	}

	@Override
	public final ITreeNodeRenderer<String> getRenderer() {
		return renderer;
	}

	@Override
	public final String getData() {
		return data;
	}

	@Override
	public void setCheckable(final boolean checkable) {
		super.setCheckable(checkable);
		if (checkableItem.isSelected() != checkable) {
			checkableItem.setSelected(checkable);
		}
	}

	@Override
	public int getChildrenCount() {
		return children.size();
	}

	@Override
	public ITreeNodeModel<?> getChildNode(final int index) {
		return children.get(index);
	}

	protected void setChildren(final List<ITreeNodeModel<String>> children) {
		this.children.clear();
		this.children.addAll(children);
		fireChildrenChanged();
	}

	private final class TreeNodeRenderer extends DefaultTreeNodeRenderer<String> {

		@Override
		public void nodeCreated(final String value, final ITreeNode node) {
			super.nodeCreated(value, node);
			node.setPopupMenu(createNodeMenu(node));
		}

		private IMenuModel createNodeMenu(final ITreeNode node) {
			final MenuModel result = new MenuModel();
			result.addItem(checkableItem);
			return result;
		}

	}

}

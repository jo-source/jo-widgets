/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.spi.impl.dummy.widgets;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.spi.impl.dummy.dummyui.UIDObservable;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTreeItem;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITreeNodeSpi;

public class TreeNodeImpl implements ITreeNodeSpi {

	private final UIDTreeItem item;
	private final UIDObservable treeObs;
	private final TreeImpl parentTree;
	private boolean selected;

	public TreeNodeImpl(final TreeImpl parentTree, final UIDTreeItem parentItem, final Integer index) {
		this.treeObs = new UIDObservable();
		this.parentTree = parentTree;
		this.item = parentItem;
		this.selected = false;
	}

	@Override
	public UIDTreeItem getUiReference() {
		return item;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (!enabled) {
			throw new UnsupportedOperationException("Tree items could not be disabled");
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setText(final String text) {
		if (text != null) {
			item.setText(text);
		}
		else {
			item.setText(String.valueOf(""));
		}
	}

	@Override
	public void setToolTipText(final String text) {
		item.setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		item.setIcon(icon);
	}

	@Override
	public void setMarkup(final Markup markup) {
		item.setMarkup(markup);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		item.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		item.setBackgroundColor(colorValue);
	}

	@Override
	public void setExpanded(final boolean expanded) {
		item.setExpanded(expanded);
	}

	@Override
	public boolean isExpanded() {
		return item.isExpanded();
	}

	@Override
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		treeObs.addTreeNodeListener(listener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		treeObs.removeTreeNodeListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		treeObs.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		treeObs.removePopupDetectionListener(listener);
	}

	@Override
	public ITreeNodeSpi addNode(final Integer index) {
		final TreeNodeImpl result = new TreeNodeImpl(parentTree, getUiReference(), index);
		parentTree.registerNode(result);
		return result;
	}

	@Override
	public void removeNode(final int index) {
		parentTree.removeNode(index);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(parentTree.getUiReference());
	}

}

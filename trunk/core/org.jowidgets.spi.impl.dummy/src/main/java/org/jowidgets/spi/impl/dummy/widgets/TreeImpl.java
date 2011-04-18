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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.spi.impl.dummy.dummyui.UIDObservable;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTree;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTreeItem;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.controler.ITreeSelectionListenerSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;

public class TreeImpl implements ITreeSpi, ITreeNodeSpi {

	private final UIDTree tree;
	private final UIDObservable popupObs;
	private final List<TreeNodeImpl> items;
	private Position position;
	private Dimension size;

	public TreeImpl(final ITreeSetupSpi setup) {
		this.tree = new UIDTree();
		this.popupObs = new UIDObservable();
		this.items = new LinkedList<TreeNodeImpl>();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(getUiReference());
	}

	@Override
	public UIDTree getUiReference() {
		return tree;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		getUiReference().setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return getUiReference().isEnabled();
	}

	@Override
	public void redraw() {
		getUiReference().redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getUiReference().setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return getUiReference().getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return getUiReference().getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getUiReference().setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isVisible();
	}

	@Override
	public Dimension getSize() {
		return size;
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupObs.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupObs.removePopupDetectionListener(listener);
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		getUiReference().setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return getUiReference().getLayoutConstraints();
	}

	@Override
	public void addTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
		popupObs.addTreeSelectionListener(listener);
	}

	@Override
	public void removeTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
		popupObs.removeTreeSelectionListener(listener);
	}

	@Override
	public ITreeNodeSpi addNode(final Integer index) {
		final TreeNodeImpl result = new TreeNodeImpl(this, new UIDTreeItem(), index);
		items.add(result);
		return result;
	}

	@Override
	public void removeNode(final int index) {
		items.remove(index);
	}

	@Override
	public ITreeNodeSpi getRootNode() {
		return this;
	}

	@Override
	public List<ITreeNodeSpi> getSelectedNodes() {
		final List<ITreeNodeSpi> results = new LinkedList<ITreeNodeSpi>();
		for (final TreeNodeImpl item : items) {
			if (item.isSelected()) {
				results.add(item);
			}
		}
		return results;
	}

	public void registerNode(final TreeNodeImpl result) {
		items.add(result);
	}

	@Override
	public void setMarkup(final Markup markup) {
		throw new UnsupportedOperationException("setMarkup is not possible on the root node");
	}

	@Override
	public void setExpanded(final boolean expanded) {
		throw new UnsupportedOperationException("setExpanded is not possible on the root node");
	}

	@Override
	public boolean isExpanded() {
		throw new UnsupportedOperationException("isExpanded is not possible on the root node");
	}

	@Override
	public void setSelected(final boolean selected) {
		throw new UnsupportedOperationException("setSelected is not possible on the root node");
	}

	@Override
	public boolean isSelected() {
		throw new UnsupportedOperationException("isSelected is not possible on the root node");
	}

	@Override
	public void setText(final String text) {
		throw new UnsupportedOperationException("setText is not possible on the root node");
	}

	@Override
	public void setToolTipText(final String text) {
		throw new UnsupportedOperationException("setToolTipText is not possible on the root node");
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		throw new UnsupportedOperationException("setIcon is not possible on the root node");
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		throw new UnsupportedOperationException("addTreeNodeListener is not possible on the root node");
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		throw new UnsupportedOperationException("removeTreeNodeListener is not possible on the root node");
	}

	@Override
	public void setSize(final Dimension size) {
		this.size = size;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void setPosition(final Position position) {
		this.position = position;
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		getUiReference().setRedrawEnabled(enabled);
	}

	@Override
	public boolean requestFocus() {
		return getUiReference().requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		getUiReference().addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		getUiReference().removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		getUiReference().addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		getUiReference().removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		getUiReference().addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		getUiReference().removeMouseListener(mouseListener);
	}

}

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

package org.jowidgets.spi.impl.swt.widgets;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.TreeNodeObservable;
import org.jowidgets.spi.impl.swt.color.ColorCache;
import org.jowidgets.spi.impl.swt.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.util.FontProvider;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.util.Assert;

public class TreeNodeImpl extends TreeNodeObservable implements ITreeNodeSpi {

	private final Set<IPopupDetectionListener> popupDetectionListeners;

	private final TreeImpl parentTree;
	private final TreeItem item;
	private String toolTipText;

	public TreeNodeImpl(final TreeImpl parentTree, final TreeItem parentItem, final Integer index) {
		super();
		Assert.paramNotNull(parentTree, "parentTree");

		this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();

		this.parentTree = parentTree;

		if (index != null) {
			if (parentItem == null) {
				this.item = new TreeItem(parentTree.getUiReference(), SWT.NONE, index.intValue());
			}
			else {
				this.item = new TreeItem(parentItem, SWT.NONE, index.intValue());
			}
		}
		else {
			if (parentItem == null) {
				this.item = new TreeItem(parentTree.getUiReference(), SWT.NONE);
			}
			else {
				this.item = new TreeItem(parentItem, SWT.NONE);
			}
		}
	}

	@Override
	public TreeItem getUiReference() {
		return item;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (!enabled) {
			throw new UnsupportedOperationException("Tree items can not be disabled");
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setText(final String text) {
		if (text != null) {
			getUiReference().setText(text);
		}
		else {
			getUiReference().setText(String.valueOf(""));
		}
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		final Image oldImage = getUiReference().getImage();
		final Image newImage = SwtImageRegistry.getInstance().getImage(icon);
		if (oldImage != newImage) {
			getUiReference().setImage(newImage);
		}
	}

	@Override
	public void setMarkup(final Markup markup) {
		final Font newFont = FontProvider.deriveFont(item.getFont(), markup);
		item.setFont(newFont);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getUiReference().setForeground(ColorCache.getInstance().getColor(colorValue));
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().setBackground(ColorCache.getInstance().getColor(colorValue));
	}

	@Override
	public void setExpanded(final boolean expanded) {
		item.setExpanded(expanded);
		fireExpandedChanged(expanded);
	}

	@Override
	public boolean isExpanded() {
		return item.getExpanded();
	}

	@Override
	public void setSelected(final boolean selected) {
		parentTree.setSelected(this, selected);
	}

	@Override
	public boolean isSelected() {
		for (final TreeItem selectedItem : parentTree.getUiReference().getSelection()) {
			if (selectedItem == this.item) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionListeners.add(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionListeners.remove(listener);
	}

	protected void firePopupDetected(final Position position) {
		for (final IPopupDetectionListener listener : popupDetectionListeners) {
			listener.popupDetected(position);
		}
	}

	@Override
	public ITreeNodeSpi addNode(final Integer index) {
		final TreeNodeImpl result = new TreeNodeImpl(parentTree, item, index);
		parentTree.registerItem(result.getUiReference(), result);
		return result;
	}

	@Override
	public void removeNode(final int index) {
		final TreeItem child = getUiReference().getItem(index);
		if (child != null) {
			parentTree.unRegisterItem(child);
			child.dispose();
		}
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(parentTree.getUiReference());
	}

}

/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets;

import java.util.HashSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.TreeNodeObservable;
import org.jowidgets.spi.impl.javafx.image.JavafxImageRegistry;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.util.Assert;

public class TreeNodeImpl extends TreeNodeObservable implements ITreeNodeSpi {

	private final TreeItem<String> node;
	private final TreeImpl parentTree;
	private final HashSet<IPopupDetectionListener> popupDetectionListeners;
	private String text;
	private Markup markup;
	private IColorConstant colorValueForeground;
	private IColorConstant colorValueBackground;

	public TreeNodeImpl(final TreeImpl parentTree, final TreeItem<String> node) {
		super();
		Assert.paramNotNull(parentTree, "parentTree");
		this.parentTree = parentTree;
		this.node = node;
		this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();

		this.node.expandedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(
				final ObservableValue<? extends Boolean> paramObservableValue,
				final Boolean oldValue,
				final Boolean newValue) {
				parentTree.getNodeMap().get(node).fireExpandedChanged(newValue);
			}
		});

	}

	@Override
	public TreeItem<String> getUiReference() {
		return node;
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
		getUiReference().setValue(text);
	}

	@Override
	public void setToolTipText(final String text) {
		this.text = text;
	}

	public String getToolTipText() {
		return text;
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		if (icon != null) {
			getUiReference().setGraphic(JavafxImageRegistry.getInstance().getImageHandle(icon).getImage());
		}
		else {
			getUiReference().setGraphic(null);
		}
	}

	@Override
	public void setMarkup(final Markup markup) {
		this.markup = markup;
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		this.colorValueForeground = colorValue;
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		colorValueBackground = colorValue;
	}

	public Markup getMarkup() {
		return markup;
	}

	public IColorConstant getForegroundColor() {
		return colorValueForeground;
	}

	public IColorConstant getBackgroundColor() {
		return colorValueBackground;
	}

	@Override
	public void setExpanded(final boolean expanded) {
		getUiReference().setExpanded(expanded);
	}

	@Override
	public boolean isExpanded() {
		return getUiReference().isExpanded();
	}

	@Override
	public void setSelected(final boolean selected) {
		if (selected) {
			parentTree.getUiReference().getSelectionModel().select(getUiReference());
		}
	}

	@Override
	public boolean isSelected() {
		if (parentTree.getUiReference().getSelectionModel().getSelectedItems().contains(getUiReference())) {
			return true;
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

	@Override
	public ITreeNodeSpi addNode(final Integer index) {
		final TreeItem<String> item = new TreeItem<String>();
		final TreeNodeImpl result = new TreeNodeImpl(parentTree, item);
		getUiReference().getChildren().add(item);

		parentTree.registerNode(item, result);
		return result;
	}

	@Override
	public void removeNode(final int index) {

		final TreeItem<String> child = node.getChildren().get(index);
		getUiReference().getChildren().remove(child);
		if (child != null) {
			parentTree.unRegisterNode(child);
		}
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(parentTree.getUiReference());
	}

	protected void firePopupDetected(final Position position) {
		for (final IPopupDetectionListener listener : popupDetectionListeners) {
			listener.popupDetected(position);
		}

	}
}

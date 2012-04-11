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

package org.jowidgets.spi.impl.javafx.widgets.base;

import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ContextMenuEvent;

import org.jowidgets.common.types.Position;
import org.jowidgets.spi.impl.controller.PopupDetectionObservable;
import org.jowidgets.spi.impl.javafx.widgets.StyleDelegate;
import org.jowidgets.spi.impl.javafx.widgets.TreeNodeImpl;

public class TreeNodeRenderer extends TreeCell<String> {

	private final StyleDelegate styleUtil;
	private final Map<TreeItem<String>, TreeNodeImpl> nodes;

	public TreeNodeRenderer(
		final Map<TreeItem<String>, TreeNodeImpl> nodes,
		final PopupDetectionObservable popupDetectionObservable) {
		this.nodes = nodes;
		styleUtil = new StyleDelegate(this);
		this.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(final ContextMenuEvent event) {
				final Position position = new Position((int) event.getScreenX(), (int) event.getScreenY());
				if (getTreeItem() != null) {
					nodes.get(getTreeItem()).firePopupDetected(position);
				}
				else {
					popupDetectionObservable.firePopupDetected(position);
				}
			}

		});
	}

	@Override
	public void updateItem(final String item, final boolean empty) {
		if (getTreeItem() != null) {
			final TreeNodeImpl node = getNode(getTreeItem());

			if (node.getBackgroundColor() != null) {
				styleUtil.setBackgroundColor(node.getBackgroundColor());
			}

			if (node.getForegroundColor() != null) {
				styleUtil.setForegroundColor(node.getForegroundColor());
			}

			if (node.getToolTipText() != null && (!(node.getToolTipText().isEmpty()))) {
				Tooltip.install(this, new Tooltip(node.getToolTipText()));
			}
			else {
				Tooltip.uninstall(this, new Tooltip(node.getToolTipText()));
			}
			setText(item);
		}
		super.updateItem(item, empty);
	}

	private TreeNodeImpl getNode(final TreeItem<String> item) {
		return nodes.get(item);
	}
}

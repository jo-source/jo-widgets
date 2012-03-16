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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.spi.impl.controller.TreeSelectionObservableSpi;
import org.jowidgets.spi.impl.javafx.widgets.base.TreeNodeRenderer;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.controller.ITreeSelectionListenerSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;

public class TreeImpl extends JavafxControl implements ITreeSpi {
	private final Map<TreeItem<String>, TreeNodeImpl> nodes;
	private final TreeItem<String> javaFXRoot;
	private final ITreeNodeSpi rootNodeSpi;
	private final TreeSelectionObservableSpi treeObservable;
	private List<TreeItem<String>> lastSelection;

	public TreeImpl(final ITreeSetupSpi setup) {
		super(createControl(setup), false);
		treeObservable = new TreeSelectionObservableSpi();
		javaFXRoot = getUiReference().getRoot();
		rootNodeSpi = new TreeNodeImpl(this, javaFXRoot);
		nodes = new HashMap<TreeItem<String>, TreeNodeImpl>();
		this.lastSelection = new LinkedList<TreeItem<String>>();

		getUiReference().setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
			@Override
			public TreeCell<String> call(final TreeView<String> p) {
				return new TreeNodeRenderer(nodes, getPopupDetectionObservable());
			}
		});

		getUiReference().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(
				final ObservableValue<? extends Object> paramObservableValue,
				final Object oldValue,
				final Object newValue) {
				fireSelectionChange();
			}
		});
	}

	private void fireSelectionChange() {

		final List<TreeItem<String>> newSelection = new LinkedList<TreeItem<String>>();
		final ObservableList<TreeItem<String>> selectionItems = getUiReference().getSelectionModel().getSelectedItems();
		if (selectionItems != null) {
			for (final TreeItem<String> selectionItem : selectionItems) {
				newSelection.add(selectionItem);
			}
		}

		for (final TreeItem<String> wasSelected : lastSelection) {
			if (!newSelection.contains(wasSelected)) {
				if (wasSelected != null) {
					nodes.get(wasSelected).fireSelectionChanged(false);
				}
			}
		}

		for (final TreeItem<String> isSelected : newSelection) {
			if (!lastSelection.contains(isSelected)) {
				if (isSelected != null) {
					nodes.get(isSelected).fireSelectionChanged(true);
				}
			}
		}

		lastSelection = newSelection;
		treeObservable.fireSelectionChanged();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreeView<String> getUiReference() {
		return (TreeView<String>) super.getUiReference();
	};

	@Override
	public void addTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
		treeObservable.addTreeSelectionListener(listener);
	}

	@Override
	public void removeTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
		treeObservable.removeTreeSelectionListener(listener);
	}

	@Override
	public ITreeNodeSpi getRootNode() {
		return rootNodeSpi;
	}

	@Override
	public List<ITreeNodeSpi> getSelectedNodes() {

		final List<ITreeNodeSpi> result = new LinkedList<ITreeNodeSpi>();
		final ObservableList<TreeItem<String>> selectedItems = getUiReference().getSelectionModel().getSelectedItems();
		if (selectedItems != null) {
			for (final TreeItem<String> selectionNode : selectedItems) {
				result.add(nodes.get(selectionNode));
			}
		}
		return result;
	}

	protected void registerNode(final TreeItem<String> item, final TreeNodeImpl nodeImpl) {
		nodes.put(item, nodeImpl);
	}

	protected void unRegisterNode(final TreeItem<String> item) {
		nodes.remove(item);
	}

	protected Map<TreeItem<String>, TreeNodeImpl> getNodeMap() {
		return nodes;
	}

	private static Control createControl(final ITreeSetupSpi setup) {

		final TreeItem<String> rootNode = new TreeItem<String>();
		rootNode.setExpanded(true);
		final TreeView<String> tree = new TreeView<String>(rootNode);
		tree.setShowRoot(false);

		if (SelectionPolicy.MULTI_SELECTION == setup.getSelectionPolicy()) {
			tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		}
		else if (SelectionPolicy.SINGLE_SELECTION == setup.getSelectionPolicy()) {
			tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		}
		else {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		return tree;
	}
}

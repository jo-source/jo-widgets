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

package org.jowidgets.examples.common.dnd;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.dnd.IDragDataResponse;
import org.jowidgets.api.dnd.IDragEvent;
import org.jowidgets.api.dnd.IDragSource;
import org.jowidgets.api.dnd.IDragSourceListener;
import org.jowidgets.api.dnd.IDropEvent;
import org.jowidgets.api.dnd.IDropResponse;
import org.jowidgets.api.dnd.IDropTarget;
import org.jowidgets.api.dnd.IDropTargetListener;
import org.jowidgets.api.dnd.ITreeDropLocation;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.dnd.DropMode;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class TreeDragAndDropExample implements IApplication {

    private static final int LEVEL_ONE_COUNT = 3;
    private static final int LEVEL_TWO_COUNT = 3;
    private static final int LEVEL_TREE_COUNT = 3;

    public void start() {
        DemoIconsInitializer.initialize();
        Toolkit.getInstance().getApplicationRunner().run(this);
    }

    @Override
    public void start(final IApplicationLifecycle lifecycle) {
        final IFrame rootFrame = Toolkit.createRootFrame(BPF.frame().setTitle("Drag and Drop Tree"), lifecycle);
        rootFrame.setLayout(MigLayoutFactory.growingInnerCellLayout());

        final ISplitComposite splitComposite = rootFrame.add(BPF.splitHorizontal(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

        final ITree tree1 = addTree(splitComposite.getFirst());
        addDragSource(tree1);
        addDropTarget(tree1);

        final ITree tree2 = addTree(splitComposite.getSecond());
        addDropTarget(tree2);

        rootFrame.setSize(800, 800);
        rootFrame.setVisible(true);
    }

    private void addDragSource(final ITree tree) {
        final IDragSource dragSource = tree.getDragSource();
        dragSource.setActions(DropAction.MOVE, DropAction.COPY);
        dragSource.setTransferTypes(TransferType.STRING_TYPE);

        dragSource.addDragSourceListener(new IDragSourceListener() {

            private ITreeNode dragItem;

            @Override
            public void dragStart(final IDragEvent event, final IVetoable veto) {
                dragItem = tree.getNodeAt(event.getPosition());
            }

            @Override
            public void dragSetData(
                final IDragEvent event,
                final IVetoable veto,
                final TransferType<?> transferType,
                final IDragDataResponse dragData) {

                if (dragItem != null && TransferType.STRING_TYPE.equals(transferType)) {
                    dragData.setData(dragItem.getText());
                }
            }

            @Override
            public void dragFinished(final IDragEvent event, final DropAction dropAction) {
                if (DropAction.MOVE.equals(dropAction) && dragItem != null) {
                    dragItem.dispose();
                }
            }
        });

    }

    //CHECKSTYLE:OFF
    private void addDropTarget(final ITree tree) {
        final IDropTarget dropTarget = tree.getDropTarget();
        dropTarget.setActions(DropAction.COPY, DropAction.MOVE);
        dropTarget.setTransferTypes(TransferType.STRING_TYPE);
        dropTarget.setDefaultDropMode(DropMode.SELECT_OR_INSERT);

        dropTarget.addDropTargetListener(new IDropTargetListener() {

            @Override
            public void dropAccept(final IDropEvent event, final IDropResponse response) {
                System.out.println("DROP ACCEPT: " + event);
            }

            @Override
            public void drop(final IDropEvent event) {
                System.out.println("DROP: " + event);
                if (TransferType.STRING_TYPE.equals(event.getTransferType())) {
                    final String text = (String) event.getData();

                    final ITreeNode treeNode = ((ITreeDropLocation) event.getDropSelection()).getTreeNode();
                    if (treeNode != null) {
                        final ITreeNode parentNode = treeNode.getParent();
                        if (parentNode != null) {
                            final int indexOfItem = getIndexOfItem(parentNode, treeNode);
                            if (indexOfItem != -1) {
                                final ITreeNode newNode = parentNode.addNode(indexOfItem + 1);
                                newNode.setText(text);
                            }
                        }
                    }
                }

            }

            private int getIndexOfItem(final ITreeNode parent, final ITreeNode node) {
                int result = 0;
                for (final ITreeNode child : parent.getChildren()) {
                    if (child == node) {
                        return result;
                    }
                    result++;
                }
                return -1;
            }

            @Override
            public void dragOver(final IDropEvent event, final IDropResponse response) {
                System.out.println("DRAG OVER: " + event);
            }

            @Override
            public void dragOperationChanged(final IDropEvent event, final IDropResponse response) {
                System.out.println("DRAG OPERATION CHANGED: " + event);
            }

            @Override
            public void dragExit() {
                System.out.println("DRAG EXIT");
            }

            @Override
            public void dragEnter(final IDropEvent event, final IDropResponse response) {
                System.out.println("DRAG ENTER: " + event);
            }

        });

    }

    //CHECKSTYLE:ON

    private ITree addTree(final IContainer container) {
        container.setLayout(MigLayoutFactory.growingInnerCellLayout());
        final ITree tree = container.add(BPF.tree(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
        for (int i = 0; i < LEVEL_ONE_COUNT; i++) {
            final ITreeNode levelOneNode = addNode(tree, "Level A, Node ", i);
            for (int j = 0; j < LEVEL_TWO_COUNT; j++) {
                final ITreeNode levelTwoNode = addNode(levelOneNode, "Level B, Node ", j);
                for (int k = 0; k < LEVEL_TREE_COUNT; k++) {
                    addNode(levelTwoNode, "Level C, Node ", k);
                }
                levelTwoNode.setExpanded(true);
            }
            levelOneNode.setExpanded(true);
        }

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final IMouseButtonEvent event) {
                final ITreeNode nodeAtPosition = tree.getNodeAt(event.getPosition());
                if (nodeAtPosition != null) {
                    //CHECKSTYLE:OFF
                    System.out.println("NODE AT POSITION: " + nodeAtPosition.getText());
                    //CHECKSTYLE:ON
                }
                else {
                    //CHECKSTYLE:OFF
                    System.out.println("NULL");
                    //CHECKSTYLE:ON
                }
            }

        });

        return tree;
    }

    private ITreeNode addNode(final ITreeContainer parent, final String prefix, final int index) {
        final ITreeNode node = parent.addNode();
        node.setText(prefix + " " + index);
        return node;
    }

}

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

package org.jowidgets.examples.swt;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;

//CHECKSTYLE:OFF
public class SwtDragAndDropPractice2 implements IApplication {

    private static final int LEVEL_ONE_COUNT = 3;
    private static final int LEVEL_TWO_COUNT = 3;
    private static final int LEVEL_TREE_COUNT = 3;

    @Override
    public void start(final IApplicationLifecycle lifecycle) {
        final IFrame rootFrame = Toolkit.createRootFrame(BPF.frame().setTitle("Dran and Drop Tree"), lifecycle);
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

    private void addDragSource(final ITree joTree) {
        final Tree tree = (Tree) joTree.getUiReference();

        final int operations = DND.DROP_MOVE | DND.DROP_COPY;
        final DragSource dragSource = new DragSource(tree, operations);

        final Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
        dragSource.setTransfer(types);

        dragSource.addDragListener(new DragSourceListener() {

            private TreeItem dragItem;

            @Override
            public void dragStart(final DragSourceEvent event) {
                dragItem = tree.getItem(new Point(event.x, event.y));
            }

            @Override
            public void dragSetData(final DragSourceEvent event) {
                if (dragItem != null && TextTransfer.getInstance().isSupportedType(event.dataType)) {
                    event.data = dragItem.getText();
                }
            }

            @Override
            public void dragFinished(final DragSourceEvent event) {
                if (event.detail == DND.DROP_MOVE && dragItem != null) {
                    dragItem.dispose();
                }
            }
        });
    }

    private void addDropTarget(final ITree joTree) {
        final Tree tree = (Tree) joTree.getUiReference();

        final int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_NONE;
        final DropTarget target = new DropTarget(tree, operations);

        target.setTransfer(new Transfer[] {TextTransfer.getInstance()});

        target.addDropListener(new DropTargetListener() {

            @Override
            public void dropAccept(final DropTargetEvent event) {
                System.out.println("DROP ACCEPT: " + event);
            }

            @Override
            public void drop(final DropTargetEvent event) {
                System.out.println("DROP: " + event);
                if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
                    final String text = (String) event.data;

                    final TreeItem item = (TreeItem) event.item;
                    System.out.println("ITEM: " + item.getText());

                    final Point point = tree.toControl(new Point(event.x, event.y));
                    final Position position = new Position(point.x, point.y);

                    final ITreeNode treeNode = joTree.getNodeAt(position);
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
            public void dragOver(final DropTargetEvent event) {
                event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_INSERT_AFTER | DND.FEEDBACK_EXPAND;
            }

            @Override
            public void dragOperationChanged(final DropTargetEvent event) {
                System.out.println("DRAG OPERATION CHANGED: " + event);
            }

            @Override
            public void dragLeave(final DropTargetEvent event) {
                System.out.println("DRAG LEAVE: " + event);
            }

            @Override
            public void dragEnter(final DropTargetEvent event) {
                System.out.println("DRAG ENTER: " + event);
            }
        });

    }

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
                    System.out.println("NODE AT POSITION: " + nodeAtPosition.getText());
                }
                else {
                    System.out.println("NULL");
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

    public static void main(final String[] args) {
        Toolkit.getInstance().getApplicationRunner().run(new SwtDragAndDropPractice2());
    }
}

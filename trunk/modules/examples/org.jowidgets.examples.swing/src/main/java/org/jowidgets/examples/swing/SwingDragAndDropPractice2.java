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

package org.jowidgets.examples.swing;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JTree.DropLocation;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

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
public class SwingDragAndDropPractice2 implements IApplication {

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
		final JTree tree = (JTree) ((JScrollPane) joTree.getUiReference()).getViewport().getComponent(0);

		final DragSource ds = new DragSource();

		final DragGestureListener dragGestureListener = new DragGestureListener() {

			@Override
			public void dragGestureRecognized(final DragGestureEvent dge) {
				final Point dragOrigin = dge.getDragOrigin();

				System.out.println("COMPONENT: " + dge.getComponent());
				System.out.println("ORIGIN: " + dragOrigin);
				System.out.println("SOURCE: " + dge.getSource());

				final ITreeNode treeNode = joTree.getNodeAt(new Position(dragOrigin.x, dragOrigin.y));
				if (treeNode != null) {
					final StringSelection transfer = new StringSelection(treeNode.getText());

					final DragSourceListener dragSourceListener = new DragSourceListener() {

						@Override
						public void dropActionChanged(final DragSourceDragEvent dsde) {
							System.out.println("DROP ACTION CHANGED: " + dsde);
						}

						@Override
						public void dragOver(final DragSourceDragEvent dsde) {
							//System.out.println("DRAG OVER: " + dsde);

						}

						@Override
						public void dragExit(final DragSourceEvent dse) {
							System.out.println("DRAG EXIT: " + dse);
						}

						@Override
						public void dragEnter(final DragSourceDragEvent dsde) {
							System.out.println("DRAG ENTER: " + dsde);
						}

						@Override
						public void dragDropEnd(final DragSourceDropEvent dsde) {
							System.out.println("DRAG DROP END: " + dsde.getDropSuccess());
						}
					};

					dge.startDrag(null, transfer, dragSourceListener);
				}

			}
		};

		@SuppressWarnings("unused")
		final DragGestureRecognizer dragGestureRecognizer = ds.createDefaultDragGestureRecognizer(
				tree,
				DnDConstants.ACTION_COPY_OR_MOVE,
				dragGestureListener);

	}

	@SuppressWarnings({"serial", "unused"})
	private void addDropTarget(final ITree joTree) {
		final JTree tree = (JTree) ((JScrollPane) joTree.getUiReference()).getViewport().getComponent(0);

		tree.setDropMode(DropMode.ON_OR_INSERT);
		tree.setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(final TransferHandler.TransferSupport support) {
				if (!support.isDataFlavorSupported(DataFlavor.stringFlavor) || !support.isDrop()) {
					return false;
				}

				final JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
				System.out.println(dropLocation);
				return dropLocation.getPath() != null;
			}

			@Override
			public boolean importData(final JComponent comp, final Transferable transferable) {
				if (transferable != null) {
					try {
						final Object data = transferable.getTransferData(DataFlavor.stringFlavor);
						System.out.println("DATA: " + data);
						return true;
					}
					catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				return false;
			}

		});

		final DropTargetListener dtl = new DropTargetListener() {

			@Override
			public void dropActionChanged(final DropTargetDragEvent dtde) {
				System.out.println("DROP ACTION CHANGED: " + dtde.getDropAction());
				dtde.acceptDrag(DnDConstants.ACTION_MOVE);

			}

			@Override
			public void drop(final DropTargetDropEvent dtde) {
				final Transferable transferable = dtde.getTransferable();
				if (transferable != null) {
					try {
						final Object data = transferable.getTransferData(DataFlavor.stringFlavor);
						System.out.println("DATA: " + data);
						dtde.dropComplete(true);
					}
					catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			@Override
			public void dragOver(final DropTargetDragEvent dtde) {
				dtde.acceptDrag(DnDConstants.ACTION_MOVE);
				final DropLocation dropLocation = tree.getDropLocation();
				if (dropLocation != null) {
					System.out.println("dropLocation: "
						+ dropLocation.getChildIndex()
						+ " / "
						+ dropLocation.getPath().getLastPathComponent());
				}
			}

			@Override
			public void dragExit(final DropTargetEvent dte) {
				System.out.println("DRAG EXIT: " + dte);
			}

			@Override
			public void dragEnter(final DropTargetDragEvent dtde) {
				System.out.println("DRAG ENTER: " + dtde);
				dtde.acceptDrag(DnDConstants.ACTION_MOVE);
			}
		};

		//final DropTarget dropTarget = new DropTarget(tree, DnDConstants.ACTION_COPY_OR_MOVE, dtl);
		//dropTarget.setActive(isActive)

		try {
			//tree.getDropTarget().addDropTargetListener(dtl);
		}
		catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//tree.setDropTarget(dropTarget);
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

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		Toolkit.getInstance().getApplicationRunner().run(new SwingDragAndDropPractice2());
	}
}

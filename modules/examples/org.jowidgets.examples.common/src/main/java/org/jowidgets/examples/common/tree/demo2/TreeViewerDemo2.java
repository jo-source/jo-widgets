/*
 * Copyright (c) 2014, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.examples.common.tree.demo2;

import org.jowidgets.api.command.CollapseTreeAction;
import org.jowidgets.api.command.ExpandTreeAction;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.CheckedState;
import org.jowidgets.api.types.TreeAutoCheckPolicy;
import org.jowidgets.api.types.TreeViewerCreationPolicy;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.ITreeViewer;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.ITreeViewerBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.tree.TreeNodeModelAdapter;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class TreeViewerDemo2 implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        final IFrameBluePrint frameBp = BPF.frame();
        frameBp.setSize(new Dimension(800, 600)).setTitle("Tree Viewer Demo");

        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
        frame.setLayout(new MigLayoutDescriptor("wrap", "0[grow, 0::]0", "0[]0[]0[grow, 0::]0"));

        final IToolBar toolBar = frame.add(BPF.toolBar());
        final IToolBarModel toolBarModel = toolBar.getModel();
        frame.add(BPF.separator(), "growx, w 0::");

        final RootNodeModel rootModel = new RootNodeModel();
        final ITreeViewerBluePrint<String> treeViewerBp = BPF.treeViewer(rootModel);
        treeViewerBp.setCreationPolicy(TreeViewerCreationPolicy.CREATE_ON_EXPAND);
        treeViewerBp.setPageSize(100);
        treeViewerBp.setChecked(true).setAutoCheckPolicy(TreeAutoCheckPolicy.SINGLE_PATH);
        treeViewerBp.setSelectionPolicy(SelectionPolicy.SINGLE_SELECTION);
        treeViewerBp.setWinSelectionColors();
        final ITreeViewer<String> tree = frame.add(treeViewerBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

        toolBarModel.addAction(CollapseTreeAction.create(tree));
        toolBarModel.addAction(ExpandTreeAction.builder(tree).setPivotLevel(1).build());
        toolBar.pack();

        rootModel.addTreeNodeModelListener(new TreeNodeModelAdapter() {
            @Override
            public void checkedChanged() {
                printCheckedNodes(rootModel);
                //CHECKSTYLE:OFF
                System.out.println();
                System.out.println("--------------------------------------------");
                //CHECKSTYLE:ON
            }

        });

        //set the root frame visible

        frame.setVisible(true);
    }

    private static void printCheckedNodes(final ITreeNodeModel<?> model) {
        if (model.getCheckedState() != CheckedState.UNCHECKED) {
            //CHECKSTYLE:OFF
            System.out.print(model.getData() + " -- ");
            //CHECKSTYLE:ON
            for (int i = 0; i < model.getChildrenCount(); i++) {
                printCheckedNodes(model.getChildNode(i));
            }
        }
    }

    public void start() {
        DemoIconsInitializer.initialize();
        Toolkit.getInstance().getApplicationRunner().run(this);
    }
}

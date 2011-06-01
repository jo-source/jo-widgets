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

package org.jowidgets.addons.testtool.internal.view;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.addons.testtool.internal.ITestTool;
import org.jowidgets.addons.testtool.internal.TestDataObject;
import org.jowidgets.addons.testtool.internal.UserAction;
import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.tools.command.EnabledChecker;

public class TestToolViewToolbar {
	private static final IActionBuilderFactory ABF = Toolkit.getActionBuilderFactory();
	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	public TestToolViewToolbar(final IFrame frame, final ITestTool testTool) {
		final IToolBar toolBar = frame.add(BPF.toolBar(), "north, wrap");
		final EnabledChecker playEnabledChecker = new EnabledChecker();
		final EnabledChecker stopEnabledChecker = new EnabledChecker();
		final EnabledChecker recordEnabledChecker = new EnabledChecker();

		final IActionBuilder playBuilder = ABF.create();
		final IToolBarButton playButton = toolBar.addItem(BPF.toolBarButton());
		playEnabledChecker.setEnabledState(EnabledState.ENABLED);
		final ICommandExecutor playCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				stopEnabledChecker.setEnabledState(EnabledState.ENABLED);
				recordEnabledChecker.setEnabledState(EnabledState.DISABLED);
				playEnabledChecker.setEnabledState(EnabledState.DISABLED);
				testTool.activateReplayMode();
				testTool.replay(getTableContent(), 500);
			}
		};
		playBuilder.setCommand(playCommand, playEnabledChecker);
		playButton.setAction(playBuilder.setText("play").build());

		final IActionBuilder stopBuilder = ABF.create();
		final IToolBarButton stopButton = toolBar.addItem(BPF.toolBarButton());
		stopEnabledChecker.setEnabledState(EnabledState.DISABLED);
		final ICommandExecutor stopCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				playEnabledChecker.setEnabledState(EnabledState.ENABLED);
				recordEnabledChecker.setEnabledState(EnabledState.ENABLED);
				stopEnabledChecker.setEnabledState(EnabledState.DISABLED);
				testTool.deactivateReplayAndRecord();
			}
		};
		stopBuilder.setCommand(stopCommand, stopEnabledChecker);
		stopButton.setAction(stopBuilder.setText("stop").build());

		final IActionBuilder recordBuilder = ABF.create();
		final IToolBarButton recordButton = toolBar.addItem(BPF.toolBarButton());
		recordEnabledChecker.setEnabledState(EnabledState.ENABLED);
		final ICommandExecutor recordCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				playEnabledChecker.setEnabledState(EnabledState.DISABLED);
				stopEnabledChecker.setEnabledState(EnabledState.ENABLED);
				recordEnabledChecker.setEnabledState(EnabledState.DISABLED);
				testTool.activateRecordMode();
			}
		};
		recordBuilder.setCommand(recordCommand, recordEnabledChecker);
		recordButton.setAction(recordBuilder.setText("record").build());

		toolBar.addSeparator();

		final IActionBuilder deleteBuilder = ABF.create();
		final IToolBarButton deleteButton = toolBar.addItem(BPF.toolBarButton());
		final ICommandExecutor deleteCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				for (final Integer value : TestToolViewTable.getTableModel().getSelection()) {
					TestToolViewTable.getTableModel().removeRow(value.intValue());
				}
			}
		};
		deleteBuilder.setCommand(deleteCommand);
		deleteButton.setAction(deleteBuilder.setText("delete").build());

		final IActionBuilder deleteAllBuilder = ABF.create();
		final IToolBarButton deleteAllButton = toolBar.addItem(BPF.toolBarButton());
		final ICommandExecutor deleteAllCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				TestToolViewTable.getTableModel().removeAllRows();
			}
		};
		deleteAllBuilder.setCommand(deleteAllCommand);
		deleteAllButton.setAction(deleteAllBuilder.setText("delete all").build());

		toolBar.addSeparator();

		final IActionBuilder showBuilder = ABF.create();
		final IToolBarButton showButton = toolBar.addItem(BPF.toolBarButton().setText("show"));
		final ICommandExecutor showCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				// TODO LG show the current widget, when a table item is selected
			}
		};
		showBuilder.setCommand(showCommand);
		showButton.setAction(showBuilder.setText("show").build());
	}

	private List<TestDataObject> getTableContent() {
		final List<TestDataObject> result = new LinkedList<TestDataObject>();
		for (int rowIndex = 0; rowIndex < TestToolViewTable.getTableModel().getRowCount(); rowIndex++) {
			final TestDataObject obj = new TestDataObject();
			obj.setType(TestToolViewTable.getTableModel().getCell(rowIndex, 1).getText());
			final String action = TestToolViewTable.getTableModel().getCell(rowIndex, 2).getText();
			obj.setAction(UserAction.valueOf(action));
			obj.setId(TestToolViewTable.getTableModel().getCell(rowIndex, 4).getText());
			result.add(obj);
		}
		return result;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}

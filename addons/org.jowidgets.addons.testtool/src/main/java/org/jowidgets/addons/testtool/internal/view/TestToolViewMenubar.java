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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.addons.testtool.internal.ITestTool;
import org.jowidgets.addons.testtool.internal.TestDataObject;
import org.jowidgets.addons.testtool.internal.UserAction;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFileChooser;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFileChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.DialogResult;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.tools.model.item.MenuModel;

public class TestToolViewMenubar {

	private static final String DEFAULT_FILEPATH = System.getProperty("user.dir")
		+ File.separator
		+ "resources"
		+ File.separator
		+ "testtool";
	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	public TestToolViewMenubar(final IFrame parent, final ITestTool testTool) {
		final IMenuBarModel menuBarModel = parent.getMenuBarModel();
		final IMenuModel fileModel = new MenuModel("File");
		fileModel.setMnemonic('F');

		final IActionItemModel saveActionItem = fileModel.addActionItem("Save Test As...");
		saveActionItem.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				final List<TestDataObject> list = new LinkedList<TestDataObject>();
				if (!(TestToolViewTable.getTableModel().getRowCount() <= 0)) {
					for (int rowIndex = 0; rowIndex < TestToolViewTable.getTableModel().getRowCount(); rowIndex++) {
						final TestDataObject obj = new TestDataObject();
						obj.setType(TestToolViewTable.getTableModel().getCell(rowIndex, 1).getText());
						final String action = TestToolViewTable.getTableModel().getCell(rowIndex, 2).getText();
						obj.setAction(UserAction.valueOf(action));
						obj.setId(TestToolViewTable.getTableModel().getCell(rowIndex, 4).getText());
						list.add(obj);
					}
					final IFileChooserBluePrint fileChooserBp = BPF.fileChooser(FileChooserType.SAVE);
					final IFileChooser fileChooser = parent.createChildWindow(fileChooserBp);
					final DialogResult result = fileChooser.open();
					if (result == DialogResult.OK) {
						for (final File file : fileChooser.getSelectedFiles()) {
							testTool.save(list, file.getName());
							Toolkit.getMessagePane().showInfo("Test successfully saved!");
						}
					}
				}
				else {
					Toolkit.getMessagePane().showWarning("There is nothing to save.");
				}
			}
		});
		final IActionItemModel loadActionItem = fileModel.addActionItem("Load Test...");
		loadActionItem.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				final IFileChooserBluePrint fileChooserBf = BPF.fileChooser(FileChooserType.OPEN_FILE);
				final IFileChooser fileChooser = parent.createChildWindow(fileChooserBf);
				fileChooser.setSelectedFile(new File(DEFAULT_FILEPATH));
				final DialogResult result = fileChooser.open();
				if (result == DialogResult.OK) {
					for (final File file : fileChooser.getSelectedFiles()) {
						List<TestDataObject> results = new LinkedList<TestDataObject>();
						String fileName = file.getName();
						if (file.getName().contains(".xml")) {
							fileName = file.getName();
							fileName = fileName.replace(".xml", "");
						}
						results = testTool.load(fileName);
						if (results != null) {
							for (final TestDataObject item : results) {
								TestToolViewTable.getTableModel().addRow(
										Integer.toString(TestToolViewTable.getTableModel().getRowCount()),
										item.getType(),
										item.getAction().name(),
										getWidgetProperty(item),
										item.getId());
							}
							Toolkit.getMessagePane().showInfo("Loading tests was successfull!");
						}
						else {
							Toolkit.getMessagePane().showError("couldn't find tests for the given filename");
						}
					}
				}
			}
		});

		fileModel.addSeparator();
		final IActionItemModel exitActionItem = fileModel.addActionItem("Exit");
		exitActionItem.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				System.exit(0);
			}
		});
		menuBarModel.addMenu(fileModel);
	}

	private String getWidgetProperty(final TestDataObject item) {
		final String id = item.getId();
		return id.substring(id.lastIndexOf(":") + 1, id.length());
	}

	@Override
	public String toString() {
		return super.toString();
	}
}

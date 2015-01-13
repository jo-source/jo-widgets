/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.examples.common.demo;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IDirectoryChooser;
import org.jowidgets.api.widgets.IFileChooser;
import org.jowidgets.api.widgets.blueprint.IDirectoryChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.IFileChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.DialogResult;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.types.IFileChooserFilter;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.types.FileChooserFilter;

public final class DemoFileChooserComposite {

	public DemoFileChooserComposite(final IContainer parentContainer) {

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		parentContainer.setLayout(new MigLayoutDescriptor("[300::, grow]", ""));

		final IButton openFileButton = parentContainer.add(
				bpf.button("Open file demo", "Allows to open a single file"),
				"grow, sg bg, wrap");

		openFileButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				openFileChooser(FileChooserType.OPEN_FILE, "open file demo");
			}
		});

		final IButton openFilesButton = parentContainer.add(
				bpf.button("Open files demo", "Allows to open a multiple files"),
				"grow, sg bg, wrap");

		openFilesButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				openFileChooser(FileChooserType.OPEN_FILE_LIST, "Open files demo");
			}
		});

		final IButton saveFileButton = parentContainer.add(
				bpf.button("Save file demo", "Allows to save a single file"),
				"grow, sg bg, wrap");

		saveFileButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				openFileChooser(FileChooserType.SAVE, "Save a file demo");
			}
		});

		final IButton openDirectoryButton = parentContainer.add(
				bpf.button("Select directory demo", "Allows to select an directory"),
				"grow, sg bg, wrap");

		openDirectoryButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				openDirectoryChooser();
			}
		});

	}

	private void openFileChooser(final FileChooserType type, final String title) {
		if (Toolkit.getSupportedWidgets().hasFileChooser()) {
			final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

			final List<IFileChooserFilter> filterList = new LinkedList<IFileChooserFilter>();
			filterList.add(new FileChooserFilter("All Files (*.*)", "*"));
			filterList.add(new FileChooserFilter("Batch Files (*.bat)", "bat"));
			filterList.add(new FileChooserFilter("Image Files (*.jpg; *.gif; *.png; *.bpm)", "jpg", "gif", "png", "bpm"));

			final IFileChooserBluePrint fileChooserBp = bpf.fileChooser(type).setFilterList(filterList).setTitle(title);
			final IFileChooser fileChooser = Toolkit.getActiveWindow().createChildWindow(fileChooserBp);
			fileChooser.setSelectedFile(new File("C:/projects/jo-widgets"));
			final DialogResult result = fileChooser.open();
			if (result == DialogResult.OK) {

				for (final File file : fileChooser.getSelectedFiles()) {
					//CHECKSTYLE:OFF
					System.out.println(file.getAbsolutePath());
					//CHECKSTYLE:ON
				}
				final IFileChooserFilter selectedFilter = fileChooser.getSelectedFilter();
				if (selectedFilter != null) {
					//CHECKSTYLE:OFF
					System.out.println("FILTER: " + selectedFilter.getFilterName());
					//CHECKSTYLE:ON
				}
			}
		}
		else {
			Toolkit.getMessagePane().showInfo("File chooser is not supported by the spi implementation");
		}
	}

	private void openDirectoryChooser() {
		if (Toolkit.getSupportedWidgets().hasDirectoryChooser()) {
			final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

			final IDirectoryChooserBluePrint directoryChooserBp = bpf.directoryChooser().setTitle("Open directory demo");
			final IDirectoryChooser directoryChooser = Toolkit.getActiveWindow().createChildWindow(directoryChooserBp);
			final DialogResult result = directoryChooser.open();
			if (result == DialogResult.OK) {
				//CHECKSTYLE:OFF
				System.out.println(directoryChooser.getDirectory().getAbsolutePath());
				//CHECKSTYLE:ON
			}
		}
		else {
			Toolkit.getMessagePane().showInfo("Directory chooser is not supported by the spi implementation");
		}
	}

	public void foo() {}
}

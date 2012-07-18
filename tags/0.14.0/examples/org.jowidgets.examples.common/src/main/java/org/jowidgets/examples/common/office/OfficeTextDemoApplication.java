/*
 * Copyright (c) 2012, grossmann, waheckma
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

package org.jowidgets.examples.common.office;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.addons.widgets.office.api.IOfficeControl;
import org.jowidgets.addons.widgets.office.api.OfficeBPF;
import org.jowidgets.api.color.Colors;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFileChooser;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.IFileChooserBluePrint;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.builder.ITreeSetupBuilder;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.DialogResult;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.common.types.IFileChooserFilter;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.types.FileChooserFilter;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.event.IChangeListener;

public final class OfficeTextDemoApplication implements IApplication {

	private final String title;
	private IButton saveFileButton;

	public OfficeTextDemoApplication(final String title) {
		this.title = title;
	}

	public void start() {
		Toolkit.getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		initializeSilkIcons();

		//Frame 1
		final IFrame frame = Toolkit.createRootFrame(BPF.frame().setTitle(title).autoPackOff(), lifecycle);
		frame.setBackgroundColor(Colors.WHITE);
		frame.setSize(1024, 768);
		frame.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "[][]0[grow, 0::]0"));

		saveFileButton = frame.add(
				BPF.button("", "Allows to save a file, with a fileselector").setIcon(SilkIcons.DISK).setEnabled(false),
				"split 3");
		//add buttons
		final IButton openFileButton = frame.add(BPF.button("", "Allows to open a file, with a fileselector").setIcon(
				SilkIcons.FOLDER));

		final ITextControl filenameField = frame.add(BPF.textField(), "span,growx, wrap");
		frame.add(BPF.separator(), "growx, h 0::, wrap");

		//add ole content
		final IOfficeControl officeControl = frame.add(OfficeBPF.text().setToolbarVisible(false), "span,growx,height 1000");

		officeControl.addDirtyStateListener(new IChangeListener() {
			@Override
			public void changed() {
				setSaveFileButtonEnable(officeControl.getOleControl().getContext().getValue().isDirty());
			}
		});

		filenameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final IKeyEvent event) {
				if (event.getVirtualKey() == VirtualKey.ENTER) {

					final File file = new File(filenameField.getText());
					if (file.exists()) {
						officeControl.openDocument(file);
					}
					else {
						final String path = openFileChooser(FileChooserType.OPEN_FILE_LIST, "Open Olefile demo");
						if (path != null) {
							officeControl.openDocument(new File(path));
							filenameField.setText(path);
						}
					}
				}
			}
		});

		openFileButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final String path = openFileChooser(FileChooserType.OPEN_FILE_LIST, "Open Olefile demo");
				if (path != null) {
					officeControl.openDocument(new File(path));
					filenameField.setText(path);
				}
			}
		});

		saveFileButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final String path = openFileChooser(FileChooserType.SAVE, "Save Olefile demo");
				if (path != null) {
					officeControl.saveDocument(new File(path), true);
					filenameField.setText(path);
				}
			}
		});

		frame.setVisible(true);
	}

	private void setSaveFileButtonEnable(final boolean value) {
		if (saveFileButton != null) {
			saveFileButton.setEnabled(value);
		}
	}

	private String openFileChooser(final FileChooserType type, final String title) {
		if (Toolkit.getSupportedWidgets().hasFileChooser()) {
			final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

			final List<IFileChooserFilter> filterList = new LinkedList<IFileChooserFilter>();
			filterList.add(new FileChooserFilter("Word Files (*.doc, *.docx)", "doc", "docx"));
			final IFileChooserBluePrint fileChooserBp = bpf.fileChooser(type).setFilterList(filterList).setTitle(title);
			final IFileChooser fileChooser = Toolkit.getActiveWindow().createChildWindow(fileChooserBp);
			final DialogResult result = fileChooser.open();
			if (result == DialogResult.OK) {
				String path = fileChooser.getSelectedFiles().get(0).toString();
				if (!path.toLowerCase().endsWith(".doc") && !path.toLowerCase().endsWith(".docx")) {
					path += ".doc";
				}
				return path;
			}
		}
		else {
			Toolkit.getMessagePane().showInfo("File chooser is not supported by the spi implementation");
		}
		return null;
	}

	private static void initializeSilkIcons() {
		Toolkit.getImageRegistry().registerImageEnum(SilkIcons.class);
		Toolkit.getImageRegistry().registerImageConstant(IconsSmall.OK, SilkIcons.TICK);

		Toolkit.getBluePrintFactory().addDefaultsInitializer(
				ITreeBluePrint.class,
				new IDefaultInitializer<ITreeSetupBuilder<?>>() {
					@Override
					public void initialize(final ITreeSetupBuilder<?> setupBuilder) {
						setupBuilder.setDefaultInnerIcon(SilkIcons.DISK);
						setupBuilder.setDefaultInnerIcon(SilkIcons.FOLDER);
					}
				});
	}
}

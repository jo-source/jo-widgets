/*
 * Copyright (c) 2011, Nikolaus Moll
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

import javax.swing.UIManager;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.ITabFolderBluePrint;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.TabPlacement;
import org.jowidgets.examples.common.workbench.demo1.WorkbenchDemo1;
import org.jowidgets.spi.impl.swing.options.SwingOptions;
import org.jowidgets.workbench.impl.WorkbenchRunner;

public final class SwingWorkbenchDemo1JoTabsBottomStarter {

	private SwingWorkbenchDemo1JoTabsBottomStarter() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		SwingOptions.setJoWidgetsTabLayout(true);
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		bpf.addDefaultsInitializer(ITabFolderBluePrint.class, createTabDefaults());
		new WorkbenchRunner().run(new WorkbenchDemo1());
	}

	private static IDefaultInitializer<ITabFolderBluePrint> createTabDefaults() {
		return new IDefaultInitializer<ITabFolderBluePrint>() {
			@Override
			public void initialize(final ITabFolderBluePrint tabFolderBp) {
				tabFolderBp.setTabPlacement(TabPlacement.BOTTOM);
			}
		};
	}

}

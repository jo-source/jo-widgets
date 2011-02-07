/*
 * Copyright (c) 2011, M. Grossmann, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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
package org.jowidgets.workbench.impl.rcp.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.workbench.api.IWorkbenchApplication;

public final class WorkbenchApplicationTree extends Composite {

	private final Composite folderComposite;
	private IToolBar toolBar;
	private IPopupMenu menu;

	public WorkbenchApplicationTree(final Composite parent, final IWorkbenchApplication application) {
		super(parent, SWT.NONE);
		folderComposite = new Composite(parent, SWT.NONE);
		folderComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		final IComposite joComposite = Toolkit.getWidgetWrapperFactory().createComposite(folderComposite);
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		if (application.hasToolBar()) {
			toolBar = joComposite.add(bpf.toolBar(), null);
		}

		if (application.hasMenu()) {
			final IToolBar menuBar = joComposite.add(bpf.toolBar(), null);
			final IToolBarPopupButton menuButton = menuBar.addItem(bpf.toolBarPopupButton());
			menu = menuBar.createPopupMenu();
			menuButton.addPopupDetectionListener(new IPopupDetectionListener() {
				@Override
				public void popupDetected(final Position position) {
					menu.show(position);
				}
			});
		}
	}

	public Composite getFolderComposite() {
		return folderComposite;
	}

	public IToolBar getToolBar() {
		return toolBar;
	}

	public IMenu getJoMenu() {
		return menu;
	}

}

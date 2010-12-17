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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.descriptor.setup.IAccelerateableMenuItemSetup;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.impl.command.CommandExecuter;
import org.jowidgets.impl.command.IActionWidget;
import org.jowidgets.impl.widgets.common.wrapper.ActionMenuItemSpiWrapper;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;

public class ActionMenuItemImpl extends ActionMenuItemSpiWrapper implements IActionMenuItem, IActionWidget, IDisposeable {

	private final IMenu parent;
	private CommandExecuter commandExecuter;
	private boolean visibleOnScreen;

	public ActionMenuItemImpl(
		final IMenu parent,
		final IActionMenuItemSpi actionMenuItemSpi,
		final IAccelerateableMenuItemSetup setup) {
		super(actionMenuItemSpi);

		this.visibleOnScreen = false;

		this.parent = parent;

		setText(setup.getText());
		setToolTipText(setup.getToolTipText());
		setIcon(setup.getIcon());

		if (setup.getAccelerator() != null) {
			setAccelerator(setup.getAccelerator());
		}

		if (setup.getMnemonic() != null) {
			setMnemonic(setup.getMnemonic().charValue());
		}

		parent.addMenuListener(new IMenuListener() {

			@Override
			public void menuActivated() {
				visibleOnScreen = true;
				initializeCommandExecuterWhenVisible();
			}

			@Override
			public void menuDeactivated() {
				visibleOnScreen = false;
			}

		});
	}

	@Override
	public IMenu getParent() {
		return parent;
	}

	@Override
	public void setAction(final IAction action) {
		setText(action.getText());
		setToolTipText(action.getToolTipText());
		setIcon(action.getIcon());
		setEnabled(action.isEnabled());

		if (action.getAccelerator() != null) {
			setAccelerator(action.getAccelerator());
		}

		if (action.getMnemonic() != null) {
			setMnemonic(action.getMnemonic().charValue());
		}

		disposeCommandExecuter();
		commandExecuter = new CommandExecuter(action, this);
		initializeCommandExecuterWhenVisible();

		//TODO must be removed
		System.out.println("TODO");
		addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				commandExecuter.execute();
			}
		});

	}

	@Override
	public void dispose() {
		disposeCommandExecuter();
	}

	private void disposeCommandExecuter() {
		if (commandExecuter != null) {
			commandExecuter.dispose();
			commandExecuter = null;
		}
	}

	private void initializeCommandExecuterWhenVisible() {
		if (commandExecuter != null && visibleOnScreen) {
			commandExecuter.initialize();
		}
	}

}

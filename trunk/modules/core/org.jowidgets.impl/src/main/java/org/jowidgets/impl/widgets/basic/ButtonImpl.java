/*
 * Copyright (c) 2010, grossmann, Lukas Gross
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
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.command.ActionExecuter;
import org.jowidgets.impl.command.ActionWidgetSync;
import org.jowidgets.impl.command.IActionWidget;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractButtonSpiWrapper;
import org.jowidgets.spi.widgets.IButtonSpi;
import org.jowidgets.test.api.widgets.IButtonUi;
import org.jowidgets.test.spi.widgets.IButtonUiSpi;

public class ButtonImpl extends AbstractButtonSpiWrapper implements IButtonUi, IActionWidget {

	private final ControlDelegate controlDelegate;

	private ActionWidgetSync actionWidgetSync;
	private ActionExecuter actionExecuter;

	public ButtonImpl(final IButtonSpi buttonSpi, final IButtonDescriptor descriptor) {
		super(buttonSpi);
		this.controlDelegate = new ControlDelegate(buttonSpi, this);
		setEnabled(descriptor.isEnabled());
		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

		if (descriptor.getFontSize() != null) {
			setFontSize(Integer.valueOf(descriptor.getFontSize()));
		}
		if (descriptor.getFontName() != null) {
			setFontName(descriptor.getFontName());
		}

		addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				if (actionExecuter != null) {
					actionExecuter.execute();
				}
			}
		});

		setTextCache(descriptor.getText());
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public void addParentListener(final IParentListener<IContainer> listener) {
		controlDelegate.addParentListener(listener);
	}

	@Override
	public void removeParentListener(final IParentListener<IContainer> listener) {
		controlDelegate.removeParentListener(listener);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		controlDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		controlDelegate.removeDisposeListener(listener);
	}

	@Override
	public boolean isDisposed() {
		return controlDelegate.isDisposed();
	}

	@Override
	public void dispose() {
		disposeActionWidgetSync();
		controlDelegate.dispose();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return controlDelegate.createPopupMenu();
	}

	@Override
	public void setAction(final IAction action) {
		//dispose the old sync if exists
		disposeActionWidgetSync();

		actionWidgetSync = new ActionWidgetSync(action, this);
		actionWidgetSync.setActive(true);

		actionExecuter = new ActionExecuter(action, this);
	}

	private void disposeActionWidgetSync() {
		if (actionWidgetSync != null) {
			actionWidgetSync.dispose();
			actionWidgetSync = null;
		}
	}

	@Override
	public void push() {
		if (getWidget() instanceof IButtonUiSpi) {
			final IButtonUiSpi widget = (IButtonUiSpi) getWidget();
			widget.push();
		}
	}

	@Override
	public boolean isTestable() {
		return true;
	}

}

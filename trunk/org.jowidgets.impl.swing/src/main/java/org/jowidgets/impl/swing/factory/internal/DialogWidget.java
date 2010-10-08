/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.impl.swing.factory.internal;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JDialog;

import org.jowidgets.api.look.AutoCenterPolicy;
import org.jowidgets.api.look.AutoPackPolicy;
import org.jowidgets.api.util.ColorSettingsInvoker;
import org.jowidgets.api.widgets.IDialogWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.base.IBaseDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.base.IBaseTitledChildWindowDescriptor;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swing.internal.image.SwingImageRegistry;
import org.jowidgets.impl.swing.util.ComponentUtil;
import org.jowidgets.impl.swing.widgets.SwingWindowWidget;

public class DialogWidget extends SwingWindowWidget implements IDialogWidget {

	private final IWidget parent;
	private final IBaseTitledChildWindowDescriptor<?> settings;
	private boolean wasVisible;

	public DialogWidget(
		final IGenericWidgetFactory factory,
		final SwingImageRegistry imageRegistry,
		final IWidget parent,
		final IBaseDialogDescriptor<?> descriptor) {
		super(factory, new JDialog((Window) parent.getUiReference()), descriptor.getAutoPackPolicy());

		this.parent = parent;
		this.settings = descriptor;
		this.wasVisible = false;

		getUiReference().setTitle(descriptor.getTitle());
		getUiReference().setModal(true);

		setIcon(descriptor.getIcon(), imageRegistry);
		setLayout(descriptor.getLayout());
		ColorSettingsInvoker.setColors(descriptor, this);
	}

	@Override
	public JDialog getUiReference() {
		return (JDialog) super.getUiReference();
	}

	@Override
	public void centerLocation() {
		final JDialog dialog = getUiReference();
		dialog.setLocation(ComponentUtil.getCenterLocation((Component) getParent().getUiReference(), dialog));

	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			if (AutoPackPolicy.ALLWAYS.equals(settings.getAutoPackPolicy())) {
				pack();
			}
			else if (!wasVisible && AutoPackPolicy.ONCE.equals(settings.getAutoPackPolicy())) {
				pack();
			}
			if (AutoCenterPolicy.ALLWAYS.equals(settings.getAutoCenterPolicy())) {
				centerLocation();
			}
			else if (!wasVisible && AutoCenterPolicy.ONCE.equals(settings.getAutoCenterPolicy())) {
				centerLocation();
			}
			wasVisible = true;
		}
		getUiReference().setVisible(visible);
	}

	@Override
	public IWidget getParent() {
		return parent;
	}

}

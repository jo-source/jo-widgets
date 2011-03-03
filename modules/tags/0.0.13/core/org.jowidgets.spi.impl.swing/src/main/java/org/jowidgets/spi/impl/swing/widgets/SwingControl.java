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
package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.spi.widgets.IControlSpi;

public class SwingControl extends SwingComponent implements IControlSpi {

	public SwingControl(final Component component) {
		super(component);
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		final MigLayout migLayout = getParentMigLayout();
		if (migLayout != null) {
			migLayout.setComponentConstraints(getUiReference(), layoutConstraints);
		}
		else {
			throw new IllegalStateException("MigLayout expected");
		}
	}

	@Override
	public Object getLayoutConstraints() {
		final MigLayout migLayout = getParentMigLayout();
		if (migLayout != null) {
			return migLayout.getComponentConstraints(getUiReference());
		}
		else {
			throw new IllegalStateException("MigLayout expected");
		}
	}

	private MigLayout getParentMigLayout() {
		final Container container = getUiReference().getParent();
		if (container != null) {
			final LayoutManager layout = container.getLayout();
			if (layout instanceof MigLayout) {
				return (MigLayout) layout;
			}
		}
		return null;
	}

}

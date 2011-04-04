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

package org.jowidgets.tools.powo;

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.descriptor.IDialogDescriptor;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;

public class JoDialog extends Window<IFrame, IDialogBluePrint> implements IFrame {

	private JoMenuBar menuBar;
	private IMenuBarModel menuBarModel;
	private IButton defaultButton;

	JoDialog(final IFrame widget) {
		this(bluePrint());
		Assert.paramNotNull(widget, "widget");
		initialize(widget);
	}

	public JoDialog(final String title) {
		super(Toolkit.getBluePrintFactory().dialog(title));
	}

	public JoDialog(final IWindow parent) {
		this(parent, Toolkit.getBluePrintFactory().dialog());
	}

	public JoDialog(final IWindow parent, final String title) {
		this(parent, Toolkit.getBluePrintFactory().dialog(title));
	}

	public JoDialog(final IWindow parent, final IDialogDescriptor setup) {
		super(parent, Toolkit.getBluePrintFactory().dialog().setSetup(setup));
	}

	public JoDialog(final IDialogDescriptor setup) {
		super(Toolkit.getBluePrintFactory().dialog().setSetup(setup));
	}

	public static IDialogBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().dialog();
	}

	public static IDialogBluePrint bluePrint(final String title) {
		return bluePrint().setTitle(title);
	}

	public static IDialogBluePrint bluePrint(final String title, final IImageConstant icon) {
		return bluePrint(title).setIcon(icon);
	}

	@Override
	void initialize(final IFrame widget) {
		super.initialize(widget);
		if (menuBar != null) {
			menuBar.initialize(createMenuBar());
		}
		if (menuBarModel != null) {
			widget.setMenuBar(menuBarModel);
		}
		if (defaultButton != null) {
			getWidget().setDefaultButton(defaultButton);
		}
	}

	public final void setMenuBar(final JoMenuBar menuBar) {
		Assert.paramNotNull(menuBar, "menuBar");
		if (isInitialized()) {
			menuBar.initialize(createMenuBar());
		}
		else {
			if (menuBarModel != null) {
				throw new UnsupportedOperationException("This frame has already a menu bar model and is not yet initialized. "
					+ "Uninitialized JoFrame's must not have a JoMenuBar and a menu model at the same time. This might be "
					+ "supported in future releases.");
			}
			this.menuBar = menuBar;
		}
	}

	@Override
	public IMenuBar createMenuBar() {
		if (isInitialized()) {
			return getWidget().createMenuBar();
		}
		else {
			if (menuBarModel != null) {
				throw new UnsupportedOperationException("This frame has already a menu bar model and is not yet initialized. "
					+ "Uninitialized JoFrame's must not have a JoMenuBar and a menu model at the same time. This might be "
					+ "supported in future releases.");
			}
			menuBar = new JoMenuBar();
			return menuBar;
		}
	}

	@Override
	public IMenuBarModel getMenuBarModel() {
		if (isInitialized()) {
			return getWidget().getMenuBarModel();
		}
		else {
			if (menuBarModel == null) {
				setMenuBar(Toolkit.getModelFactoryProvider().getItemModelFactory().menuBar());
			}
			return menuBarModel;
		}
	}

	@Override
	public void setMenuBar(final IMenuBarModel menuBarModel) {
		if (isInitialized()) {
			getWidget().setMenuBar(menuBarModel);
		}
		else {
			if (menuBar != null) {
				throw new UnsupportedOperationException(
					"This frame has already a menu bar (JoMenuBar) and is not yet initialized. "
						+ "Uninitialized JoFrame's must not have a JoMenuBar and a menu model at the same time. This might be "
						+ "supported in future releases.");
			}
			this.menuBarModel = menuBarModel;
		}
	}

	@Override
	public void setDefaultButton(final IButton defaultButton) {
		if (isInitialized()) {
			getWidget().setDefaultButton(defaultButton);
		}
		else {
			this.defaultButton = defaultButton;
		}
	}

	public static JoDialog toJoDialog(final IFrame widget) {
		Assert.paramNotNull(widget, "widget");
		if (widget instanceof JoDialog) {
			return (JoDialog) widget;
		}
		else {
			return new JoDialog(widget);
		}
	}
}

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

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarItemModel;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarMenu;
import org.jowidgets.api.widgets.descriptor.setup.IItemSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.impl.base.delegate.ItemModelBindingDelegate;
import org.jowidgets.impl.base.delegate.ToolBarItemDiposableDelegate;
import org.jowidgets.impl.model.item.MenuModelBuilder;
import org.jowidgets.impl.widgets.common.wrapper.ToolBarItemSpiWrapper;
import org.jowidgets.impl.widgets.common.wrapper.invoker.ToolBarMenuSpiInvoker;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;

public class ToolBarMenuImpl extends ToolBarItemSpiWrapper implements IToolBarMenu {

	private final IToolBar parent;
	private final IPopupMenu popupMenu;
	private final ToolBarItemDiposableDelegate disposableDelegate;

	public ToolBarMenuImpl(final IToolBar parent, final IToolBarButtonSpi toolBarButtonSpi, final IItemSetup setup) {
		super(toolBarButtonSpi, new ItemModelBindingDelegate(
			new ToolBarMenuSpiInvoker(toolBarButtonSpi, setup.getIcon()),
			new MenuModelBuilder().build()));

		this.parent = parent;
		this.disposableDelegate = new ToolBarItemDiposableDelegate(this, getItemModelBindingDelegate());

		setText(setup.getText());
		setToolTipText(setup.getToolTipText());
		setIcon(setup.getIcon());

		this.popupMenu = parent.createPopupMenu();
		this.popupMenu.setModel(getModel());

		toolBarButtonSpi.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final Position position = toolBarButtonSpi.getPosition();
				final Dimension size = toolBarButtonSpi.getSize();

				if (popupMenu != null) {
					popupMenu.show(new Position(position.getX(), position.getY() + size.getHeight()));
				}
			}
		});

	}

	@Override
	public IToolBarButtonSpi getWidget() {
		return (IToolBarButtonSpi) super.getWidget();
	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			popupMenu.dispose();
			disposableDelegate.dispose();
		}
	}

	@Override
	public boolean isDisposed() {
		return disposableDelegate.isDisposed();
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		disposableDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		disposableDelegate.removeDisposeListener(listener);
	}

	@Override
	public IToolBar getParent() {
		return parent;
	}

	@Override
	public IPopupMenu getPopupMenu() {
		return popupMenu;
	}

	@Override
	public IMenuModel getModel() {
		return (IMenuModel) getItemModelBindingDelegate().getModel();
	}

	@Override
	public void setModel(final IMenuModel model) {
		getItemModelBindingDelegate().setModel(model);
		popupMenu.setModel(model);
	}

	@Override
	public void setModel(final IToolBarItemModel model) {
		if (model instanceof IMenuModel) {
			setModel((IMenuModel) model);
		}
		else {
			throw new IllegalArgumentException("Model type '" + IMenuModel.class.getName() + "' expected");
		}
	}

}

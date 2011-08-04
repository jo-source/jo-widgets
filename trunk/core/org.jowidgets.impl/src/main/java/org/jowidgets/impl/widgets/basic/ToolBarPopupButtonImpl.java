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

import org.jowidgets.api.command.ActionStyle;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IPopupActionItemModel;
import org.jowidgets.api.model.item.IToolBarItemModel;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.descriptor.setup.IItemSetup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.impl.base.delegate.ItemDelegate;
import org.jowidgets.impl.command.ActionExecuter;
import org.jowidgets.impl.command.ActionWidgetSync;
import org.jowidgets.impl.command.IActionWidget;
import org.jowidgets.impl.model.item.PopupActionItemModelBuilder;
import org.jowidgets.impl.widgets.common.wrapper.ToolBarPopupButtonSpiWrapper;
import org.jowidgets.impl.widgets.common.wrapper.invoker.ToolBarItemSpiInvoker;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;

public class ToolBarPopupButtonImpl extends ToolBarPopupButtonSpiWrapper implements
		IToolBarPopupButton,
		IActionWidget,
		IDisposeable {

	private final IToolBar parent;
	private final IPopupDetectionListener popupListener;
	private final IItemModelListener modelListener;

	private IMenuModel popupMenuModel;
	private IPopupMenu popupMenu;

	private ActionWidgetSync actionWidgetSync;
	private ActionExecuter actionExecuter;
	private IAction action;

	public ToolBarPopupButtonImpl(
		final IToolBar parent,
		final IToolBarPopupButtonSpi toolBarPopupButtonSpi,
		final IItemSetup setup) {
		super(toolBarPopupButtonSpi, new ItemDelegate(
			new ToolBarItemSpiInvoker(toolBarPopupButtonSpi),
			new PopupActionItemModelBuilder().build()));

		this.parent = parent;

		setText(setup.getText());
		setToolTipText(setup.getToolTipText());
		setIcon(setup.getIcon());

		this.modelListener = new IItemModelListener() {
			@Override
			public void itemChanged(final IItemModel item) {
				if (getModel().getAction() != action) {
					setActionValue(action, ActionStyle.OMIT_TEXT);
				}
				if (getModel().getPopupMenu() != popupMenuModel) {
					setPopupMenuValue(popupMenuModel);
				}
			}
		};

		getModel().addItemModelListener(modelListener);

		addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				if (actionExecuter != null) {
					actionExecuter.execute();
				}
			}
		});

		this.popupListener = new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				if (popupMenuModel != null) {
					if (popupMenu == null) {
						popupMenu = parent.createPopupMenu();
					}
					if (popupMenu.getModel() != popupMenuModel) {
						popupMenu.setModel(popupMenuModel);
					}
					popupMenu.show(position);
				}
			}
		};

	}

	@Override
	public IToolBar getParent() {
		return parent;
	}

	@Override
	public void setAction(final IAction action) {
		setAction(action, ActionStyle.OMIT_TEXT);
	}

	@Override
	public void setAction(final IAction action, final ActionStyle style) {
		setActionValue(action, style);
		getModel().removeItemModelListener(modelListener);
		getModel().setAction(action);
		getModel().addItemModelListener(modelListener);
	}

	@Override
	public void dispose() {
		disposeActionWidgetSync();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenuModel) {
		setPopupMenuValue(popupMenuModel);
		getModel().removeItemModelListener(modelListener);
		getModel().setPopupMenu(popupMenuModel);
		getModel().addItemModelListener(modelListener);
	}

	private void setActionValue(final IAction action, final ActionStyle style) {
		if (this.action != action) {
			//dispose the old sync if exists
			disposeActionWidgetSync();

			actionWidgetSync = new ActionWidgetSync(action, style, this);
			actionWidgetSync.setActive(true);

			actionExecuter = new ActionExecuter(action, this);

			this.action = action;
		}
	}

	private void setPopupMenuValue(final IMenuModel popupMenuModel) {
		if (this.popupMenuModel != popupMenuModel) {
			if (popupMenuModel == null && this.popupMenuModel != null) {
				removePopupDetectionListener(popupListener);
			}
			else if (popupMenuModel != null && this.popupMenuModel == null) {
				addPopupDetectionListener(popupListener);
			}
			this.popupMenuModel = popupMenuModel;
		}
	}

	private void disposeActionWidgetSync() {
		if (actionWidgetSync != null) {
			actionWidgetSync.dispose();
			actionWidgetSync = null;
		}
	}

	@Override
	public IPopupActionItemModel getModel() {
		return (IPopupActionItemModel) getItemDelegate().getModel();
	}

	@Override
	public void setModel(final IPopupActionItemModel model) {
		if (getModel() != null) {
			getModel().removeItemModelListener(modelListener);
		}
		getItemDelegate().setModel(model);
		setActionValue(model.getAction(), ActionStyle.OMIT_TEXT);
		setPopupMenuValue(model.getPopupMenu());
		model.addItemModelListener(modelListener);
	}

	@Override
	public void setModel(final IToolBarItemModel model) {
		if (model instanceof IPopupActionItemModel) {
			setModel((IPopupActionItemModel) model);
		}
		else {
			throw new IllegalArgumentException("Model type '" + IPopupActionItemModel.class.getName() + "' expected");
		}
	}

}

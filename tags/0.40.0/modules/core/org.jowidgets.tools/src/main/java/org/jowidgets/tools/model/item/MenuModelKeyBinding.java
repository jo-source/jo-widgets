/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.tools.model.item;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IKeyObservable;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;

public final class MenuModelKeyBinding {

	private final Collection<IMenuModel> menus;
	private final IKeyObservable sourceKeyObservable;
	private final IWidget sourceWidget;

	private final IKeyListener keyListener;
	private final IListModelListener listModelListener;

	private final Map<Accelerator, IAction> actions;

	private Runnable actionsMapUpdater;

	public MenuModelKeyBinding(final IMenuModel menu, final IKeyObservable sourceKeyObservable, final IWidget source) {
		this(Collections.singleton(menu), sourceKeyObservable, source);
	}

	public MenuModelKeyBinding(
		final Collection<? extends IMenuModel> menus,
		final IKeyObservable sourceKeyObservable,
		final IWidget source) {
		Assert.paramNotNull(menus, "menus");
		Assert.paramNotNull(sourceKeyObservable, "sourceKeyObservable");
		Assert.paramNotNull(source, "source");

		this.menus = new LinkedList<IMenuModel>(menus);
		this.sourceKeyObservable = sourceKeyObservable;
		this.sourceWidget = source;

		this.actions = new HashMap<Accelerator, IAction>();

		this.keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(final IKeyEvent event) {
				final VirtualKey virtualKey = event.getVirtualKey();
				if (VirtualKey.UNDEFINED != virtualKey) {
					final Accelerator pressedAccelerator = getAccelerator(event);
					if (pressedAccelerator != null) {
						final IAction action = actions.get(pressedAccelerator);
						if (action != null && action.isEnabled()) {
							final IExecutionContext executionContext = new ExecutionContext(action);
							try {
								action.execute(executionContext);
							}
							catch (final Exception e) {
								try {
									final IExceptionHandler exceptionHandler = action.getExceptionHandler();
									if (exceptionHandler != null) {
										exceptionHandler.handleException(executionContext, e);
									}
									else {
										throw e;
									}
								}
								catch (final Exception e1) {
									final UncaughtExceptionHandler uncaughtExceptionHandler = Thread.currentThread().getUncaughtExceptionHandler();
									if (uncaughtExceptionHandler != null) {
										uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e1);
									}
								}
							}
						}
					}
				}
			}
		};

		this.listModelListener = new ListModelAdapter() {

			@Override
			public void afterChildRemoved(final int index) {
				updateActionsMapLater();
			}

			@Override
			public void afterChildAdded(final int index) {
				updateActionsMapLater();
			}
		};

		updateActionsMap();
	}

	private static Accelerator getAccelerator(final IKeyEvent event) {
		final VirtualKey virtualKey = event.getVirtualKey();
		final Set<Modifier> modifier = event.getModifier();
		if (virtualKey != null && virtualKey != VirtualKey.UNDEFINED) {
			return new Accelerator(virtualKey, modifier);
		}
		else {
			final Character character = event.getCharacter();
			if (character != null) {
				return new Accelerator(virtualKey, modifier);
			}
		}
		return null;
	}

	public void addMenu(final IMenuModel menu) {
		menus.add(menu);
		updateActionsMapLater();
	}

	public void dispose() {
		disposeActionsMap();
		sourceKeyObservable.removeKeyListener(keyListener);
	}

	private void updateActionsMapLater() {
		if (actionsMapUpdater == null) {
			actionsMapUpdater = new Runnable() {
				@Override
				public void run() {
					updateActionsMap();
					actionsMapUpdater = null;
				}
			};
			Toolkit.getUiThreadAccess().invokeLater(actionsMapUpdater);
		}
	}

	private void updateActionsMap() {
		actions.clear();
		sourceKeyObservable.removeKeyListener(keyListener);
		for (final IMenuModel menu : menus) {
			updateActionsMap(menu);
		}
		if (!actions.isEmpty()) {
			sourceKeyObservable.addKeyListener(keyListener);
		}
	}

	private void updateActionsMap(final IMenuModel menu) {
		for (final IMenuItemModel item : menu.getChildren()) {
			if (item instanceof IActionItemModel) {
				final IAction action = ((IActionItemModel) item).getAction();
				if (action != null) {
					final Accelerator accelerator = action.getAccelerator();
					if (accelerator != null) {
						actions.put(accelerator, action);
					}
				}
			}
			else if (item instanceof IMenuModel) {
				updateActionsMap((IMenuModel) item);
			}
		}
		menu.addListModelListener(listModelListener);
	}

	private void disposeActionsMap() {
		actions.clear();
		for (final IMenuModel menu : menus) {
			disposeActionsMap(menu);
		}
	}

	private void disposeActionsMap(final IMenuModel menu) {
		menu.removeListModelListener(listModelListener);
		for (final IMenuItemModel item : menu.getChildren()) {
			if (item instanceof IMenuModel) {
				disposeActionsMap((IMenuModel) item);
			}
		}
	}

	private final class ExecutionContext implements IExecutionContext {

		private final IAction action;

		private ExecutionContext(final IAction action) {
			Assert.paramNotNull(action, "action");
			this.action = action;
		}

		@Override
		public <VALUE_TYPE> VALUE_TYPE getValue(final ITypedKey<VALUE_TYPE> key) {
			return null;
		}

		@Override
		public IAction getAction() {
			return action;
		}

		@Override
		public IWidget getSource() {
			return sourceWidget;
		}

	}
}

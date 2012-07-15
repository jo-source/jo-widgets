/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.impl.model.item;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionChangeListener;
import org.jowidgets.api.command.IActionChangeObservable;
import org.jowidgets.api.model.item.ActionItemVisibilityAspectPlugin;
import org.jowidgets.api.model.item.IActionItemVisibilityAspect.RequestContext;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.command.ActionChangeAdapter;
import org.jowidgets.tools.controller.ActionObservable;
import org.jowidgets.util.IDecorator;
import org.jowidgets.util.priority.IPriorityValue;
import org.jowidgets.util.priority.LowHighPriority;
import org.jowidgets.util.priority.PrioritizedResultCreator;

abstract class AbstractActionItemModelImpl extends ItemModelImpl {

	private final ActionObservable actionObservable;
	private final List<IDecorator<IAction>> decorators;
	private final IActionChangeListener actionChangeListener;
	private final ActionItemVisibilityAspectComposite visibilityAspect;

	private IAction action;
	private IAction decoratedAction;
	private boolean decoratorsDirty;

	protected AbstractActionItemModelImpl() {
		this(null, null, null, null, null, null, true, null, null);
	}

	protected AbstractActionItemModelImpl(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final Accelerator accelerator,
		final Character mnemonic,
		final boolean enabled,
		final IAction action,
		final ActionItemVisibilityAspectComposite visibilityAspect) {
		super(id, text, toolTipText, icon, accelerator, mnemonic, enabled);

		this.decorators = new LinkedList<IDecorator<IAction>>();
		this.actionObservable = new ActionObservable();
		this.action = action;
		this.decoratedAction = action;
		this.decoratorsDirty = false;
		this.visibilityAspect = visibilityAspect;
		this.actionChangeListener = new ActionChangeAdapter() {
			@Override
			public void enabledChanged() {
				final boolean oldVisible = isVisible();
				setVisibilityFromAspects(RequestContext.ACTION_AND_ENABLED_STATE);
				if (oldVisible != isVisible()) {
					fireItemChanged();
				}
			}
		};

		setActionImpl(action);
	}

	protected void setContent(final AbstractActionItemModelImpl source) {
		super.setContent(source);
		setActionImpl(source.getAction());
	}

	public IAction getAction() {
		return getDecoratedAction();
	}

	private IAction getDecoratedAction() {
		if (action == null) {
			return null;
		}
		if (decoratorsDirty) {
			decoratedAction = action;
			for (final IDecorator<IAction> decorator : decorators) {
				decoratedAction = decorator.decorate(decoratedAction);
			}
			decoratorsDirty = false;
		}
		return decoratedAction;
	}

	public void setAction(final IAction action) {
		setActionImpl(action);
		fireItemChanged();
	}

	private void setActionImpl(final IAction action) {
		if (this.action != null) {
			final IActionChangeObservable actionChangeObservable = this.action.getActionChangeObservable();
			if (actionChangeObservable != null) {
				actionChangeObservable.removeActionChangeListener(actionChangeListener);
			}
		}
		if (action != null) {
			final IActionChangeObservable actionChangeObservable = action.getActionChangeObservable();
			if (actionChangeObservable != null) {
				actionChangeObservable.addActionChangeListener(actionChangeListener);
			}
		}
		this.action = action;
		setVisibilityFromAspects(RequestContext.ACTION);
	}

	private void setVisibilityFromAspects(final RequestContext requestContext) {
		final PrioritizedResultCreator<Boolean, LowHighPriority> resultCreator;
		resultCreator = new PrioritizedResultCreator<Boolean, LowHighPriority>(LowHighPriority.HIGH);

		if (visibilityAspect != null) {
			resultCreator.addResult(visibilityAspect.getVisibility(getAction(), requestContext));
		}
		if (!resultCreator.hasMaxPrio()) {
			resultCreator.addResult(ActionItemVisibilityAspectPlugin.getVisibility(getAction(), requestContext));
		}

		final IPriorityValue<Boolean, LowHighPriority> visibility = resultCreator.getResult();
		if (visibility != null) {
			final Boolean visibiliyValue = visibility.getValue();
			if (visibiliyValue != null) {
				setVisibleImpl(visibiliyValue.booleanValue(), false);
			}
		}
	}

	public void addDecorator(final IDecorator<IAction> decorator) {
		decorators.add(decorator);
		decoratorsDirty = true;
		fireItemChanged();
	}

	public void removeDecorator(final IDecorator<IAction> decorator) {
		decorators.remove(decorator);
		decoratorsDirty = true;
		fireItemChanged();
	}

	public void addActionListener(final IActionListener listener) {
		actionObservable.addActionListener(listener);
	}

	public void removeActionListener(final IActionListener listener) {
		actionObservable.removeActionListener(listener);
	}

	public void actionPerformed() {
		actionObservable.fireActionPerformed();
	}

}

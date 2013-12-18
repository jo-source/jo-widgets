/*
 * Copyright (c) 2012, grossmann
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IActionItemVisibilityAspect;
import org.jowidgets.api.model.item.IActionItemVisibilityAspect.RequestContext;
import org.jowidgets.util.priority.IPriorityValue;
import org.jowidgets.util.priority.LowHighPriority;
import org.jowidgets.util.priority.PrioritizedResultCreator;

public final class ActionItemVisibilityAspectComposite {

	private final List<IActionItemVisibilityAspect> allAspects;
	private final List<IActionItemVisibilityAspect> enabledStateAspects;

	public ActionItemVisibilityAspectComposite(final List<IActionItemVisibilityAspect> visibilityAspects) {
		this.allAspects = new LinkedList<IActionItemVisibilityAspect>();
		this.enabledStateAspects = new LinkedList<IActionItemVisibilityAspect>();
		this.allAspects.addAll(visibilityAspects);

		for (final IActionItemVisibilityAspect aspect : allAspects) {
			if (aspect.getRequestContext() == RequestContext.ACTION_AND_ENABLED_STATE) {
				enabledStateAspects.add(aspect);
			}
		}
	}

	public IPriorityValue<Boolean, LowHighPriority> getVisibility(final IAction action, final RequestContext requestContext) {
		if (requestContext == RequestContext.ACTION_AND_ENABLED_STATE) {
			return getVisibility(action, allAspects);
			//TODO MG this wont work, because checks that only belongs to the action must be made
			//at enabled state change to
			//return getVisibility(action, enabledStatePlugins);
		}
		else if (requestContext == RequestContext.ACTION) {
			return getVisibility(action, allAspects);
		}
		else {
			throw new IllegalArgumentException("RequestContext '" + requestContext + "' is not supported");
		}
	}

	private IPriorityValue<Boolean, LowHighPriority> getVisibility(
		final IAction action,
		final List<IActionItemVisibilityAspect> aspects) {
		if (aspects.size() > 0) {
			final PrioritizedResultCreator<Boolean, LowHighPriority> resultCreator;
			resultCreator = new PrioritizedResultCreator<Boolean, LowHighPriority>(LowHighPriority.HIGH);
			final Iterator<IActionItemVisibilityAspect> iterator = aspects.iterator();
			while (iterator.hasNext() && !resultCreator.hasMaxPrio()) {
				resultCreator.addResult(iterator.next().getVisibility(action));
			}
			return resultCreator.getResult();
		}
		else {
			return null;
		}
	}
}

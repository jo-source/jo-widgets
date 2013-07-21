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

package org.jowidgets.api.model.item;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IActionItemVisibilityAspect.RequestContext;
import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.util.Assert;
import org.jowidgets.util.priority.IPriorityValue;
import org.jowidgets.util.priority.LowHighPriority;
import org.jowidgets.util.priority.PrioritizedResultCreator;

public final class ActionItemVisibilityAspectPlugin {

	private static PluginComposite pluginComposite;

	private ActionItemVisibilityAspectPlugin() {}

	public static IPriorityValue<Boolean, LowHighPriority> getVisibility(final IAction action, final RequestContext requestContext) {
		Assert.paramNotNull(requestContext, "requestContext");
		return getPluginCompositeImpl().getVisibility(action, requestContext);
	}

	public static void registerPlugin(final IActionItemVisibilityAspectPlugin plugin) {
		Assert.paramNotNull(plugin, "plugin");
		getPluginCompositeImpl().addPlugin(plugin);
	}

	private static synchronized PluginComposite getPluginCompositeImpl() {
		if (pluginComposite == null) {
			pluginComposite = new PluginComposite();
		}
		return pluginComposite;
	}

	private static final class PluginComposite {

		private final List<IActionItemVisibilityAspectPlugin> allPlugins;
		private final List<IActionItemVisibilityAspectPlugin> enabledStatePlugins;

		private PluginComposite() {
			this.allPlugins = new LinkedList<IActionItemVisibilityAspectPlugin>();
			this.enabledStatePlugins = new LinkedList<IActionItemVisibilityAspectPlugin>();
			this.allPlugins.addAll(getRegisteredPlugins());
			sortPlugins(allPlugins);

			for (final IActionItemVisibilityAspectPlugin plugin : allPlugins) {
				if (plugin.getVisibilityAspect().getRequestContext() == RequestContext.ACTION_AND_ENABLED_STATE) {
					enabledStatePlugins.add(plugin);
				}
			}
			sortPlugins(enabledStatePlugins);
		}

		private IPriorityValue<Boolean, LowHighPriority> getVisibility(final IAction action, final RequestContext requestContext) {
			if (requestContext == RequestContext.ACTION_AND_ENABLED_STATE) {
				return getVisibility(action, allPlugins);
				//TODO MG this wont work, because checks that only belongs to the action must be made
				//at enabled state change to
				//return getVisibility(action, enabledStatePlugins);
			}
			else if (requestContext == RequestContext.ACTION) {
				return getVisibility(action, allPlugins);
			}
			else {
				throw new IllegalArgumentException("RequestContext '" + requestContext + "' is not supported");
			}
		}

		private IPriorityValue<Boolean, LowHighPriority> getVisibility(
			final IAction action,
			final List<IActionItemVisibilityAspectPlugin> plugins) {
			if (plugins.size() > 0) {
				final PrioritizedResultCreator<Boolean, LowHighPriority> resultCreator;
				resultCreator = new PrioritizedResultCreator<Boolean, LowHighPriority>(LowHighPriority.HIGH);
				final Iterator<IActionItemVisibilityAspectPlugin> iterator = plugins.iterator();
				while (iterator.hasNext() && !resultCreator.hasMaxPrio()) {
					resultCreator.addResult(iterator.next().getVisibilityAspect().getVisibility(action));
				}
				return resultCreator.getResult();
			}
			else {
				return null;
			}
		}

		private void addPlugin(final IActionItemVisibilityAspectPlugin plugin) {
			allPlugins.add(plugin);
			sortPlugins(allPlugins);

			if (plugin.getVisibilityAspect().getRequestContext() == RequestContext.ACTION_AND_ENABLED_STATE) {
				enabledStatePlugins.add(plugin);
				sortPlugins(enabledStatePlugins);
			}
		}

		private void sortPlugins(final List<IActionItemVisibilityAspectPlugin> plugins) {
			Collections.sort(plugins, new Comparator<IActionItemVisibilityAspectPlugin>() {
				@Override
				public int compare(
					final IActionItemVisibilityAspectPlugin plugin1,
					final IActionItemVisibilityAspectPlugin plugin2) {
					if (plugin1 != null && plugin2 != null) {
						//add plugins with higher order before plugins with lower order
						//because evaluation end, if max prio is reached
						return plugin1.getOrder() - plugin2.getOrder();
					}
					return 0;
				}
			});
		}

		private List<IActionItemVisibilityAspectPlugin> getRegisteredPlugins() {
			final List<IActionItemVisibilityAspectPlugin> result = new LinkedList<IActionItemVisibilityAspectPlugin>();
			final ServiceLoader<IActionItemVisibilityAspectPlugin> service = ServiceLoader.load(
					IActionItemVisibilityAspectPlugin.class,
					SharedClassLoader.getCompositeClassLoader());
			if (service != null) {
				final Iterator<IActionItemVisibilityAspectPlugin> iterator = service.iterator();
				while (iterator.hasNext()) {
					result.add(iterator.next());
				}
			}
			return result;
		}
	}
}

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

package org.jowidgets.impl.widgets.blueprint.convenience;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenience;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.common.widgets.builder.IWidgetSetupBuilder;
import org.jowidgets.util.Assert;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SetupBuilderConvenienceRegistry implements ISetupBuilderConvenienceRegistry {

	private final Map map;

	public SetupBuilderConvenienceRegistry() {
		this.map = new HashMap();
	}

	@Override
	public void register(final Class<? extends IWidgetSetupBuilder> setupBuilder, final ISetupBuilderConvenience<?> convenienceImpl) {
		Assert.paramNotNull(setupBuilder, "setupBuilder");
		Assert.paramNotNull(convenienceImpl, "convenienceImpl");
		List list = (List) map.get(setupBuilder);
		if (list == null) {
			list = new LinkedList<ISetupBuilderConvenience<?>>();
			map.put(setupBuilder, list);
		}
		list.add(convenienceImpl);
	}

	@Override
	public List<ISetupBuilderConvenience<IWidgetSetupBuilder<?>>> getRegistered(final Class<? extends IWidgetSetupBuilder> setupBuilder) {
		Assert.paramNotNull(setupBuilder, "setupBuilder");
		List<ISetupBuilderConvenience<IWidgetSetupBuilder<?>>> list = (List<ISetupBuilderConvenience<IWidgetSetupBuilder<?>>>) map.get(setupBuilder);
		if (list == null) {
			list = new LinkedList<ISetupBuilderConvenience<IWidgetSetupBuilder<?>>>();
		}
		return list;
	}

}

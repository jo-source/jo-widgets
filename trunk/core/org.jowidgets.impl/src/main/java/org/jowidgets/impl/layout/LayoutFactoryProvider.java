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

package org.jowidgets.impl.layout;

import org.jowidgets.api.layout.IBorderLayoutFactoryBuilder;
import org.jowidgets.api.layout.IFillLayoutFactoryBuilder;
import org.jowidgets.api.layout.IFlowLayoutFactoryBuilder;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.layout.ILayoutFactoryProvider;
import org.jowidgets.api.layout.miglayout.IMigLayout;
import org.jowidgets.api.layout.miglayout.IMigLayoutConstraintsFactory;
import org.jowidgets.api.layout.miglayout.IMigLayoutFactoryBuilder;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.layout.miglayout.MigLayoutConstraintsFactory;
import org.jowidgets.impl.layout.miglayout.MigLayoutFactoryBuilder;

public class LayoutFactoryProvider implements ILayoutFactoryProvider {

	private static final ILayoutFactory<ILayouter> NULL_LAYOUT_FACTORY = createNullLayoutFactory();
	private static final ILayoutFactory<ILayouter> PREFERRED_SIZE_LAYOUT_FACTORY = createPreferredSizeLayoutFactory();

	private IMigLayoutConstraintsFactory migLayoutConstraintsFactory;

	@Override
	public ILayoutFactory<ILayouter> nullLayout() {
		return NULL_LAYOUT_FACTORY;
	}

	@Override
	public ILayoutFactory<ILayouter> preferredSizeLayout() {
		return PREFERRED_SIZE_LAYOUT_FACTORY;
	}

	@Override
	public ILayoutFactory<ILayouter> flowLayout() {
		return flowLayoutBuilder().build();
	}

	@Override
	public IFlowLayoutFactoryBuilder flowLayoutBuilder() {
		return new FlowLayoutFactoryBuilder();
	}

	@Override
	public ILayoutFactory<ILayouter> fillLayout() {
		return fillLayoutBuilder().build();
	}

	@Override
	public IFillLayoutFactoryBuilder fillLayoutBuilder() {
		return new FillLayoutFactoryBuilder();
	}

	@Override
	public ILayoutFactory<ILayouter> borderLayout() {
		return borderLayoutBuilder().build();
	}

	@Override
	public IBorderLayoutFactoryBuilder borderLayoutBuilder() {
		return new BorderLayoutFactoryBuilder();
	}

	@Override
	public ILayoutFactory<IMigLayout> migLayout() {
		return migLayoutBuilder().build();
	}

	@Override
	public IMigLayoutFactoryBuilder migLayoutBuilder() {
		return new MigLayoutFactoryBuilder();
	}

	@Override
	public IMigLayoutConstraintsFactory migLayoutConstraintsFactory() {
		if (migLayoutConstraintsFactory == null) {
			migLayoutConstraintsFactory = new MigLayoutConstraintsFactory();
		}
		return migLayoutConstraintsFactory;
	}

	private static ILayoutFactory<ILayouter> createNullLayoutFactory() {
		return new ILayoutFactory<ILayouter>() {
			@Override
			public ILayouter create(final IContainer container) {
				return new NullLayout(container);
			}
		};
	}

	private static ILayoutFactory<ILayouter> createPreferredSizeLayoutFactory() {
		return new ILayoutFactory<ILayouter>() {
			@Override
			public ILayouter create(final IContainer container) {
				return new PreferredSizeLayout(container);
			}
		};
	}

}

/*
 * Copyright (c) 2011, Nikolaus Moll
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
package org.jowidgets.impl.layout.miglayout;

import java.util.List;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.impl.layout.miglayout.common.ComponentWrapper;
import org.jowidgets.impl.layout.miglayout.common.ContainerWrapper;

final class JoMigContainerWrapper extends JoMigComponentWrapper implements ContainerWrapper {
	JoMigContainerWrapper(final IContainer c) {
		super(c);
	}

	@Override
	public ComponentWrapper[] getComponents() {
		final IContainer c = getComponent();
		final List<IControl> cons = c.getChildren();
		final ComponentWrapper[] cws = new ComponentWrapper[cons.size()];
		for (int i = 0; i < cws.length; i++) {
			cws[i] = new JoMigComponentWrapper(cons.get(i));
		}
		return cws;
	}

	@Override
	public IContainer getComponent() {
		return (IContainer) super.getComponent();
	}

	@Override
	public int getComponentCount() {
		return getComponent().getChildren().size();
	}

	@Override
	public Object getLayout() {
		//return getComponent().getLayout();
		//throw new IllegalStateException();
		return this;
	}

	@Override
	public boolean isLeftToRight() {
		// TODO NM implement
		return true;
	}

	@Override
	public void paintDebugCell(final int x, final int y, final int width, final int height) {
		// not implemented yet
	}

	@Override
	public int getComponetType(final boolean disregardScrollPane) {
		return TYPE_CONTAINER;
	}

	@Override
	public int getLayoutHashCode() {
		int h = super.getLayoutHashCode();

		if (isLeftToRight()) {
			h |= (1 << 26);
		}

		return h;
	}
}

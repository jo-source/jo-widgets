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

package org.jowidgets.spi.impl.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.spi.impl.swing.util.DimensionConvert;

public class LayoutManagerImpl implements LayoutManager2 {

	private final ILayouter layouter;
	private final Map<Component, Object> components;

	public LayoutManagerImpl(final ILayouter layouter) {
		this.layouter = layouter;
		this.components = new HashMap<Component, Object>();
	}

	@Override
	public void addLayoutComponent(final String name, final Component comp) {
		components.put(comp, name);
	}

	@Override
	public void removeLayoutComponent(final Component comp) {
		components.remove(comp);
	}

	@Override
	public void addLayoutComponent(final Component comp, final Object constraints) {
		components.put(comp, constraints);
	}

	public Object getLayoutConstraints(final Component component) {
		return components.get(component);
	}

	@Override
	public Dimension preferredLayoutSize(final Container parent) {
		return DimensionConvert.convert(layouter.getPreferredSize());
	}

	@Override
	public Dimension minimumLayoutSize(final Container parent) {
		return DimensionConvert.convert(layouter.getMinSize());
	}

	@Override
	public Dimension maximumLayoutSize(final Container target) {
		return DimensionConvert.convert(layouter.getMaxSize());
	}

	@Override
	public void layoutContainer(final Container parent) {
		layouter.layout();
	}

	@Override
	public float getLayoutAlignmentX(final Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(final Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(final Container target) {
		layouter.invalidate();
	}

}

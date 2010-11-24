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

package org.jowidgets.impl.swt.widgets.internal.layout;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.jowidgets.util.Assert;

public class LayoutWrapper extends Layout {

	private final Layout layout;
	private final Method computeSizeMethod;
	private final Method layoutMethod;
	private final Set<ILayoutListener> layoutListeners;

	public LayoutWrapper(final Layout layout) {
		super();
		Assert.paramNotNull(layout, "layout");
		this.layout = new FormLayout();
		this.layoutListeners = new HashSet<ILayoutListener>();
		try {
			this.computeSizeMethod = layout.getClass().getDeclaredMethod(
					"computeSize",
					Composite.class,
					int.class,
					int.class,
					boolean.class);
			this.computeSizeMethod.setAccessible(true);

			this.layoutMethod = layout.getClass().getDeclaredMethod("layout", Composite.class, boolean.class);
			this.layoutMethod.setAccessible(true);

		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addLayoutListener(final ILayoutListener layoutListener) {
		layoutListeners.add(layoutListener);
	}

	public void removeLayoutListener(final ILayoutListener layoutListener) {
		layoutListeners.remove(layoutListener);
	}

	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		fireBeforeComputeSize();
		try {
			return (Point) computeSizeMethod.invoke(layout, composite, wHint, hHint, flushCache);
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		fireBeforeLayout();
		try {
			layoutMethod.invoke(layout, composite, flushCache);
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
		fireAfterLayout();
	}

	private void fireBeforeComputeSize() {
		for (final ILayoutListener listener : layoutListeners) {
			listener.beforeComputeSize();
		}
	}

	private void fireBeforeLayout() {
		for (final ILayoutListener listener : layoutListeners) {
			listener.beforeLayout();
		}
	}

	private void fireAfterLayout() {
		for (final ILayoutListener listener : layoutListeners) {
			listener.afterLayout();
		}
	}

}

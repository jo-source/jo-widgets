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

import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.impl.layout.miglayout.common.ComponentWrapper;
import org.jowidgets.impl.layout.miglayout.common.ContainerWrapper;

class JoMigComponentWrapper implements ComponentWrapper {

	// TODO NM change screen size when Jo Widgets supports screen information
	private static Dimension screenSize;

	private static boolean isUseVisualPadding;

	private final IComponent component;
	private int compType = TYPE_UNSET;

	JoMigComponentWrapper(final IComponent component) {
		this.component = component;
	}

	@Override
	public final int getBaseline(final int width, final int height) {
		return -1;
	}

	@Override
	public IComponent getComponent() {
		return component;
	}

	@Override
	public final float getPixelUnitFactor(final boolean isHor) {
		// TODO NM improve
		return 1f;
	}

	@Override
	public final int getX() {
		return component.getPosition().getX();
	}

	@Override
	public final int getY() {
		return component.getPosition().getY();
	}

	@Override
	public final int getWidth() {
		return component.getSize().getWidth();
	}

	@Override
	public final int getHeight() {
		return component.getSize().getHeight();
	}

	@Override
	public final int getScreenLocationX() {
		return component.toScreen(new Position(0, 0)).getX();
	}

	@Override
	public final int getScreenLocationY() {
		return component.toScreen(new Position(0, 0)).getY();
	}

	@Override
	public final int getMinimumHeight(final int sz) {
		if (component instanceof IControl) {
			final IControl control = (IControl) component;
			return control.getMinSize().getHeight();
		}

		throw new IllegalStateException("Unkown minimum size of " + component.getClass().getName());
	}

	@Override
	public final int getMinimumWidth(final int sz) {
		if (component instanceof IControl) {
			final IControl control = (IControl) component;
			return control.getMinSize().getWidth();
		}

		throw new IllegalStateException("Unkown minimum size of " + component.getClass().getName());
	}

	@Override
	public final int getPreferredHeight(final int sz) {
		if (component instanceof IControl) {
			final IControl control = (IControl) component;
			return control.getPreferredSize().getHeight();
		}

		throw new IllegalStateException("Unkown preferred size of " + component.getClass().getName());
	}

	@Override
	public final int getPreferredWidth(final int sz) {
		if (component instanceof IControl) {
			final IControl control = (IControl) component;
			return control.getPreferredSize().getWidth();
		}

		throw new IllegalStateException("Unkown preferred size of " + component.getClass().getName());
	}

	@Override
	public final int getMaximumHeight(final int sz) {
		if (component instanceof IControl) {
			final IControl control = (IControl) component;
			return control.getMaxSize().getHeight();
		}

		throw new IllegalStateException("Unkown maximum size of " + component.getClass().getName());
	}

	@Override
	public final int getMaximumWidth(final int sz) {
		if (component instanceof IControl) {
			final IControl control = (IControl) component;
			return control.getMaxSize().getWidth();
		}

		throw new IllegalStateException("Unkown maximum size of " + component.getClass().getName());
	}

	@Override
	public final ContainerWrapper getParent() {
		return new JoMigContainerWrapper((IContainer) component.getParent());
	}

	@Override
	public int getHorizontalScreenDPI() {
		// TODO NM improve
		return 72;
	}

	@Override
	public int getVerticalScreenDPI() {
		// TODO NM improve
		return 72;
	}

	private IWindow getTopLevelWindow() {
		IWidget item = component;
		while (item.getParent() != null) {
			item = item.getParent();
		}

		if (item instanceof IWindow) {
			return (IWindow) item;
		}
		else {
			return null;
		}
	}

	@Override
	public final int getScreenWidth() {
		// TODO NM improve
		if (screenSize == null) {
			final IWindow topLevelWindow = getTopLevelWindow();
			if (topLevelWindow == null) {
				return -1;
			}

			screenSize = topLevelWindow.getParentBounds().getSize();
		}

		return screenSize.getWidth();
	}

	@Override
	public final int getScreenHeight() {
		// TODO NM improve
		if (screenSize == null) {
			final IWindow topLevelWindow = getTopLevelWindow();
			if (topLevelWindow == null) {
				return -1;
			}

			screenSize = topLevelWindow.getParentBounds().getSize();
		}

		return screenSize.getHeight();
	}

	@Override
	public final boolean hasBaseline() {
		return false;
	}

	@Override
	public final String getLinkId() {
		return null;
	}

	@Override
	public final void setBounds(final int x, final int y, final int width, final int height) {
		component.setPosition(x, y);
		component.setSize(width, height);
	}

	@Override
	public boolean isVisible() {
		return component.isVisible();
	}

	@Override
	public final int[] getVisualPadding() {
		return null;
	}

	public static boolean isUseVisualPadding() {
		return isUseVisualPadding;
	}

	public static void setUseVisualPadding(final boolean b) {
		isUseVisualPadding = b;
	}

	@Override
	public int getLayoutHashCode() {
		//if (c.isDisposed())
		//	return -1;

		final Dimension size = component.getSize();
		final Dimension preferredSize;
		if (component instanceof IControl) {
			preferredSize = ((IControl) component).getPreferredSize();
		}
		else {
			preferredSize = new Dimension(0, 0);
		}
		int h = preferredSize.getWidth() + (preferredSize.getHeight() << 12) + (size.getWidth() << 22) + (size.getHeight() << 16);

		if (component.isVisible()) {
			h |= (1 << 25);
		}

		final String id = getLinkId();
		if (id != null) {
			h += id.hashCode();
		}
		return h;
	}

	@Override
	public final void paintDebugOutline() {
		// not supported yet
	}

	@Override
	public int getComponetType(final boolean disregardScrollPane) {
		if (compType == TYPE_UNSET) {
			compType = ComponentTypeUtil.getType(component);
		}

		return compType;
	}

	@Override
	public final int hashCode() {
		return component.hashCode();
	}

	@Override
	public final boolean equals(final Object o) {
		if (o == null || o instanceof JoMigComponentWrapper == false) {
			return false;
		}

		return getComponent().equals(((JoMigComponentWrapper) o).getComponent());
	}

}

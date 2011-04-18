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

package org.jowidgets.spi.impl.controler;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IMouseButtonEvent;
import org.jowidgets.common.widgets.controler.IMouseEvent;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IMouseObservable;

public class MouseObservable implements IMouseObservable {

	private final Set<IMouseListener> listeners;

	public MouseObservable() {
		this.listeners = new HashSet<IMouseListener>();
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		this.listeners.add(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		this.listeners.remove(mouseListener);
	}

	public void fireMousePressed(final Position position, final MouseButton mouseButton, final Set<Modifier> modifiers) {
		fireMousePressed(new MouseButtonEvent(position, mouseButton, modifiers));
	}

	public void fireMouseReleased(final Position position, final MouseButton mouseButton, final Set<Modifier> modifiers) {
		fireMouseReleased(new MouseButtonEvent(position, mouseButton, modifiers));
	}

	public void fireMouseDoubleClicked(final Position position, final MouseButton mouseButton, final Set<Modifier> modifiers) {
		fireMouseDoubleClicked(new MouseButtonEvent(position, mouseButton, modifiers));
	}

	public void fireMouseEnter(final Position position) {
		fireMouseEnter(new MouseEvent(position));
	}

	public void fireMouseExit(final Position position) {
		fireMouseExit(new MouseEvent(position));
	}

	public void fireMousePressed(final IMouseButtonEvent mouseEvent) {
		for (final IMouseListener listener : listeners) {
			listener.mousePressed(mouseEvent);
		}
	}

	public void fireMouseReleased(final IMouseButtonEvent mouseEvent) {
		for (final IMouseListener listener : listeners) {
			listener.mouseReleased(mouseEvent);
		}
	}

	public void fireMouseDoubleClicked(final IMouseButtonEvent mouseEvent) {
		for (final IMouseListener listener : listeners) {
			listener.mouseDoubleClicked(mouseEvent);
		}
	}

	public void fireMouseEnter(final IMouseEvent mouseEvent) {
		for (final IMouseListener listener : listeners) {
			listener.mouseEnter(mouseEvent);
		}
	}

	public void fireMouseExit(final IMouseEvent mouseEvent) {
		for (final IMouseListener listener : listeners) {
			listener.mouseExit(mouseEvent);
		}
	}

}

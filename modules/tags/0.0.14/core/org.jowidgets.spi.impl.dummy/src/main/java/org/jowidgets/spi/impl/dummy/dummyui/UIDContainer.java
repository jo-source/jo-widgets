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

package org.jowidgets.spi.impl.dummy.dummyui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.types.Border;

public class UIDContainer extends UIDComponent {

	private final List<UIDComponent> components;
	private final List<UIDComponent> componentsUnmodifieable;
	private Object layout;
	private Border border;

	public UIDContainer() {
		super();
		this.components = new LinkedList<UIDComponent>();
		this.componentsUnmodifieable = Collections.unmodifiableList(components);
	}

	public UIDComponent add(final UIDComponent component) {
		components.add(component);
		return component;
	}

	public void add(final UIDComponent component, final Object constraints) {
		add(component);
	}

	public void setLayout(final Object layout) {
		this.layout = layout;
	}

	public Object getLayout() {
		return layout;
	}

	public void removeAll() {
		components.clear();
	}

	public boolean remove(final UIDComponent component) {
		return components.remove(component);
	}

	public void setBorder(final Border border) {
		this.border = border;
	}

	public List<UIDComponent> getComponents() {
		return componentsUnmodifieable;
	}

	public Border getBorder() {
		return border;
	}

	public void remove(final int index) {
		components.remove(index);
	}

}

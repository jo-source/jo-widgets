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

package org.jowidgets.tools.widgets.wrapper;

import java.util.List;

import org.jowidgets.api.widgets.IComboBox;

public class ComboBoxWrapper<VALUE_TYPE> extends InputControlWrapper<VALUE_TYPE> implements IComboBox<VALUE_TYPE> {

	public ComboBoxWrapper(final IComboBox<VALUE_TYPE> widget) {
		super(widget);
	}

	@Override
	protected IComboBox<VALUE_TYPE> getWidget() {
		return (IComboBox<VALUE_TYPE>) super.getWidget();
	}

	@Override
	public List<VALUE_TYPE> getElements() {
		return getWidget().getElements();
	}

	@Override
	public void setElements(final List<? extends VALUE_TYPE> elements) {
		getWidget().setElements(elements);
	}

	@Override
	public void setElements(final VALUE_TYPE... elements) {
		getWidget().setElements(elements);
	}

}

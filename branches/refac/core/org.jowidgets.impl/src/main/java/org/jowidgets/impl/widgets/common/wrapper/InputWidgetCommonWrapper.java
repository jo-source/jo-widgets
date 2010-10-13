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

package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.api.widgets.IInputWidgetCommon;
import org.jowidgets.api.widgets.controler.IInputListener;

public class InputWidgetCommonWrapper<VALUE_TYPE> extends WidgetCommonWrapper implements IInputWidgetCommon<VALUE_TYPE> {

	public InputWidgetCommonWrapper(final IInputWidgetCommon<VALUE_TYPE> widget) {
		super(widget);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IInputWidgetCommon<VALUE_TYPE> getWidget() {
		return (IInputWidgetCommon<VALUE_TYPE>) super.getWidget();
	}

	@Override
	public void setValue(final VALUE_TYPE content) {
		getWidget().setValue(content);
	}

	@Override
	public VALUE_TYPE getValue() {
		return getWidget().getValue();
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		getWidget().addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		getWidget().removeInputListener(listener);
	}

	@Override
	public void setEditable(final boolean editable) {
		getWidget().setEditable(editable);
	}

}

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

package org.jowidgets.tools.powo;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.blueprint.builder.IComponentSetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class Control<WIDGET_TYPE extends IControl, BLUE_PRINT_TYPE extends IWidgetDescriptor<WIDGET_TYPE> & IComponentSetupBuilder<?>> extends
		Component<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IControl {

	private Object layoutConstraints;

	public Control(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
	}

	@Override
	void initialize(final WIDGET_TYPE widget) {
		super.initialize(widget);
		if (layoutConstraints != null) {
			getWidget().setLayoutConstraints(layoutConstraints);
		}
	}

	@Override
	public IContainer getParent() {
		checkInitialized();
		return getWidget().getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		checkInitialized();
		getWidget().setParent(parent);
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		if (isInitialized()) {
			getWidget().setLayoutConstraints(layoutConstraints);
		}
		else {
			this.layoutConstraints = layoutConstraints;
		}
	}

	@Override
	public Object getLayoutConstraints() {
		if (isInitialized()) {
			return getWidget().getLayoutConstraints();
		}
		else {
			return layoutConstraints;
		}
	}
}

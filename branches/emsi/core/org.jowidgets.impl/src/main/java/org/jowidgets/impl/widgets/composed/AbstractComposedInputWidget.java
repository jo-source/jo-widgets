/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jowidgets.api.validation.IValidateable;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.impl.base.AbstractInputWidget;
import org.jowidgets.util.Assert;

public abstract class AbstractComposedInputWidget<VALUE_TYPE> extends AbstractInputWidget<VALUE_TYPE> {

	private final Map<IInputWidget<?>, String> contextMap;
	private final List<IInputWidget<?>> subWidgets;

	private final IInputListener subWidgetListener;

	public AbstractComposedInputWidget(final boolean mandatory) {
		this(null, mandatory);
	}

	public AbstractComposedInputWidget(final IValidator<VALUE_TYPE> validator, final boolean mandatory) {
		super(validator, mandatory);

		this.contextMap = new HashMap<IInputWidget<?>, String>();
		this.subWidgets = new LinkedList<IInputWidget<?>>();

		this.subWidgetListener = new IInputListener() {

			@Override
			public void inputChanged(final Object source) {
				fireInputChanged(source);
			}

		};

		this.addValidatable(new IValidateable() {

			@Override
			public ValidationResult validate() {
				final ValidationResult result = new ValidationResult();
				for (final IInputWidget<?> subWidget : getSubWidgets()) {
					ValidationResult subResult = subWidget.validate();
					final String contextLabel = contextMap.get(subWidget);
					if (contextLabel != null) {
						subResult = subResult.copyAndSetContext(contextLabel);
					}
					result.addValidationResult(subResult);
				}
				return result;
			}
		});
	}

	@Override
	public void setEditable(final boolean editable) {
		for (final IInputWidget<?> inputWidget : getSubWidgets()) {
			inputWidget.setEditable(editable);
		}
	}

	protected final List<IInputWidget<?>> getSubWidgets() {
		return new LinkedList<IInputWidget<?>>(subWidgets);
	}

	protected final void registerSubInputWidget(final IInputWidget<?> subWidget) {
		registerSubInputWidget(null, subWidget);
	}

	protected final void registerSubInputWidget(final String contextLabel, final IInputWidget<?> subWidget) {
		Assert.paramNotNull(subWidget, "subWidget");
		subWidgets.add(subWidget);
		if (contextLabel != null) {
			contextMap.put(subWidget, contextLabel);
		}
		subWidget.addInputListener(subWidgetListener);
	}

	protected final void unRegisterSubInputWidget(final IInputWidget<?> subWidget) {
		Assert.paramNotNull(subWidget, "subWidget");
		subWidgets.remove(subWidget);
		contextMap.remove(subWidget);
		subWidget.removeInputListener(subWidgetListener);
	}

}

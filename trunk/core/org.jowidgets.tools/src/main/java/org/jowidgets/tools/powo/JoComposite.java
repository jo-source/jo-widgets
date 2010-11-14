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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.util.Tuple;

public class JoComposite extends ChildWidget<ICompositeWidget, ICompositeBluePrint> implements ICompositeWidget {

	@SuppressWarnings("rawtypes")
	private final List<Tuple<Widget, Object>> preWidgets;
	private final WidgetFactory preWidgetFactory;

	public JoComposite(final ICompositeWidget widget) {
		this(Toolkit.getBluePrintFactory().composite());
		initialize(widget);
	}

	public JoComposite() {
		this(Toolkit.getBluePrintFactory().composite());
	}

	@SuppressWarnings("rawtypes")
	public JoComposite(final ICompositeDescriptor descriptor) {
		super(Toolkit.getBluePrintFactory().composite().setSetup(descriptor));
		this.preWidgets = new LinkedList<Tuple<Widget, Object>>();
		this.preWidgetFactory = new WidgetFactory();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	final void initialize(final ICompositeWidget widget) {
		super.initialize(widget);
		for (final Tuple<Widget, Object> preWidgetTuple : preWidgets) {
			final Widget preWidget = preWidgetTuple.getFirst();
			final Object layoutConstraints = preWidgetTuple.getSecond();
			final IWidget newWidget = widget.add(preWidget.getDescriptor(), layoutConstraints);
			preWidget.initialize(newWidget);
		}
	}

	public final void add(final Widget<?, ?> widget) {
		add(widget, "");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public final void add(final Widget<?, ?> widget, final Object layoutConstraints) {
		if (isInitialized()) {
			final IWidget newWidget = getWidget().add(widget.getDescriptor(), layoutConstraints);
			final Widget warWidget = widget;
			warWidget.initialize(newWidget);
		}
		else {
			preWidgets.add(new Tuple<Widget, Object>(widget, layoutConstraints));
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public <WIDGET_TYPE extends IWidget> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		if (isInitialized()) {
			return getWidget().add(descriptor, layoutConstraints);
		}
		else {
			final Widget<WIDGET_TYPE, ?> powo = preWidgetFactory.create(descriptor);
			preWidgets.add(new Tuple<Widget, Object>(powo, layoutConstraints));
			return (WIDGET_TYPE) powo;
		}
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		if (isInitialized()) {
			getWidget().setLayout(layoutDescriptor);
		}
		else {
			getBluePrint().setLayout(layoutDescriptor);
		}
	}

	@Override
	public void layoutBegin() {
		checkInitialized();
		getWidget().layoutBegin();
	}

	@Override
	public void layoutEnd() {
		checkInitialized();
		getWidget().layoutEnd();
	}

	@Override
	public void removeAll() {
		checkInitialized();
		getWidget().removeAll();
	}

	@Override
	public <WIDGET_TYPE extends IWidget> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		checkInitialized();
		return getWidget().add(factory, layoutConstraints);
	}

}

/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 04.10.2010
 */
package org.jo.widgets.api.widgets.impl.internal;

import org.jo.widgets.api.color.IColorConstant;
import org.jo.widgets.api.widgets.IChildWidget;
import org.jo.widgets.api.widgets.ICompositeWidget;
import org.jo.widgets.api.widgets.IInputWidget;
import org.jo.widgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jo.widgets.api.widgets.blueprint.factory.impl.BluePrintFactory;
import org.jo.widgets.api.widgets.content.IInputContentContainer;
import org.jo.widgets.api.widgets.content.IInputContentCreator;
import org.jo.widgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;
import org.jo.widgets.api.widgets.factory.ICustomWidgetFactory;
import org.jo.widgets.api.widgets.layout.ILayoutDescriptor;
import org.jo.widgets.util.Assert;

public class InnerCompositeContentContainer implements IInputContentContainer {

	private final IInputContentContainer outerContainer;
	private final ICompositeWidget compositeWidget;

	public InnerCompositeContentContainer(final IInputContentContainer outerContainer, final ICompositeWidget compositeWidget) {
		Assert.paramNotNull(outerContainer, "outerContainer");
		this.outerContainer = outerContainer;
		this.compositeWidget = compositeWidget;
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		compositeWidget.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		compositeWidget.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		compositeWidget.layoutBegin();
	}

	@Override
	public void removeAll() {
		compositeWidget.removeAll();
	}

	@Override
	public <WIDGET_TYPE extends IChildWidget> WIDGET_TYPE add(
		final IBaseWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {

		return compositeWidget.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IChildWidget> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		return compositeWidget.add(factory, layoutConstraints);
	}

	@Override
	public Object getUiReference() {
		return compositeWidget.getUiReference();
	}

	@Override
	public void redraw() {
		compositeWidget.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		compositeWidget.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		compositeWidget.setBackgroundColor(colorValue);
	}

	@Override
	public void registerInputWidget(final String contextLabel, final IInputWidget<?> inputWidget) {
		outerContainer.registerInputWidget(contextLabel, inputWidget);
	}

	@Override
	public void unRegisterInputWidget(final IInputWidget<?> inputWidget) {
		outerContainer.unRegisterInputWidget(inputWidget);
	}

	@Override
	public void addSubContent(final IInputContentCreator<?> subContentCreator, final Object layoutConstraints) {
		final ICompositeBluePrint compositeBp = new BluePrintFactory().composite();
		final ICompositeWidget innerComposite = add(compositeBp, layoutConstraints);
		final InnerCompositeContentContainer innerContainer = new InnerCompositeContentContainer(outerContainer, innerComposite);
		subContentCreator.createContent(innerContainer);
	}

	@Override
	public void fireContentChanged(final Object source) {
		outerContainer.fireContentChanged(source);
	}

}

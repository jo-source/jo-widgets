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
package org.jowidgets.impl.widgets.composed.internal;

import java.util.List;

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.validation.IValidateable;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.types.Border;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.tools.widgets.base.AbstractInputComponent;

public class InputContentContainer<INPUT_TYPE> extends AbstractInputComponent<INPUT_TYPE> implements IInputContentContainer {

	private final IComposite compositeWidget;
	private final IInputContentCreator<INPUT_TYPE> content;

	public InputContentContainer(
		final IContainer parent,
		final IInputContentCreator<INPUT_TYPE> content,
		final boolean scrollableContent,
		final Border border,
		final INPUT_TYPE value) {
		super(createCompositeWidget(parent, scrollableContent, border), true);

		compositeWidget = (IComposite) getWidget();

		this.content = content;

		addValidatable(new IValidateable() {

			@Override
			public ValidationResult validate() {
				return content.validate();
			}
		});

		content.createContent(this);
		if (value != null) {
			content.setValue(value);
		}
		// do not add initialization after this, because this is given to the
		// create content method
	}

	@Override
	public void setValue(final INPUT_TYPE value) {
		content.setValue(value);
	}

	@Override
	public INPUT_TYPE getValue() {
		return content.getValue();
	}

	@Override
	public void registerInputWidget(final String label, final IInputComponent<?> inputWidget) {
		super.registerInputWidget(inputWidget, label);
	}

	@Override
	public void unRegisterInputWidget(final IInputComponent<?> inputWidget) {
		super.unRegisterInputWidget(inputWidget);
	}

	@Override
	public void fireInputChanged() {
		super.fireInputChanged();
	}

	@Override
	public boolean isEmpty() {
		boolean anyFilledOut = false;

		//empty if there is any mandatory field empty
		for (final IInputComponent<?> subWidget : getRegisteredWidgets()) {
			anyFilledOut = anyFilledOut || !subWidget.isEmpty();
			if (subWidget.isMandatory() && subWidget.isEmpty()) {
				return true;
			}
		}

		//or if not at least one field is filled out
		return !anyFilledOut;
	}

	@Override
	public void addSubContent(final IInputContentCreator<?> subContentCreator, final Object layoutConstraints) {
		final ICompositeBluePrint compositeBp = new BluePrintFactory().composite();
		final IComposite innerComposite = add(compositeBp, layoutConstraints);
		final InnerCompositeContentContainer innerContainer = new InnerCompositeContentContainer(this, innerComposite);
		subContentCreator.createContent(innerContainer);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return compositeWidget.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return compositeWidget.add(creator, layoutConstraints);
	}

	@Override
	public void setLayout(final ILayoutFactory<?> layoutFactory) {
		compositeWidget.setLayout(layoutFactory);
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
		compositeWidget.layoutEnd();
	}

	@Override
	public void removeAll() {
		compositeWidget.removeAll();
	}

	@Override
	public boolean remove(final IControl control) {
		return compositeWidget.remove(control);
	}

	@Override
	public List<IControl> getChildren() {
		return compositeWidget.getChildren();
	}

	@Override
	public Dimension getClientAreaSize() {
		return compositeWidget.getClientAreaSize();
	}

	private static IComposite createCompositeWidget(final IContainer parent, final boolean scrollableContent, final Border border) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		if (scrollableContent) {
			final IScrollCompositeBluePrint scrollCompositeBluePrint = bpF.scrollComposite();
			scrollCompositeBluePrint.setBorder(border);
			return parent.add(scrollCompositeBluePrint, "growx, growy, h 0::,w 0::, wrap");
		}
		else {
			final ICompositeBluePrint compositeBluePrint = bpF.composite();
			compositeBluePrint.setBorder(border);
			return parent.add(compositeBluePrint, "growx, growy, wrap");
		}
	}

}

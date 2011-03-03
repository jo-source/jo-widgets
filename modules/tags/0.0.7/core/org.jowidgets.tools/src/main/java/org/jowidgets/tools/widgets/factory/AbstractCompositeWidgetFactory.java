/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 24.11.2010
 */
package org.jowidgets.tools.widgets.factory;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;

public abstract class AbstractCompositeWidgetFactory<WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> implements
		IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();
	private static final IGenericWidgetFactory GWF = Toolkit.getWidgetFactory();

	protected abstract WIDGET_TYPE createWidget(IComposite composite, DESCRIPTOR_TYPE descriptor);

	@Override
	public WIDGET_TYPE create(final Object parentUiReference, final DESCRIPTOR_TYPE descriptor) {

		final IComposite compositeWidget = GWF.create(parentUiReference, BPF.composite());

		if (compositeWidget == null) {
			throw new IllegalStateException("Could not create widget with descriptor interface class '"
				+ ICompositeDescriptor.class
				+ "' from '"
				+ IGenericWidgetFactory.class.getName()
				+ "'");
		}

		return createWidget(compositeWidget, descriptor);
	}

}

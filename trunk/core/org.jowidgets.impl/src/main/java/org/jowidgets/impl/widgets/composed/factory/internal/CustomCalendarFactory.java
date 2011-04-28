/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 24.11.2010
 */
package org.jowidgets.impl.widgets.composed.factory.internal;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ICalendar;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ICalendarDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.widgets.composed.CustomCalendarImpl;

public class CustomCalendarFactory implements IWidgetFactory<ICalendar, ICalendarDescriptor> {

	@Override
	public ICalendar create(final Object parentUiReference, final ICalendarDescriptor descriptor) {

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
		final IGenericWidgetFactory gwF = Toolkit.getWidgetFactory();

		final IComposite compositeWidget = gwF.create(parentUiReference, bpF.composite());

		return new CustomCalendarImpl(compositeWidget, descriptor);
	}

}

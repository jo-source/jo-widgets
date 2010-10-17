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
package org.jowidgets.impl.swt.widgets.internal;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.jowidgets.api.color.IColorConstant;
import org.jowidgets.api.util.ColorSettingsInvoker;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.api.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.api.widgets.layout.ILayoutDescriptor;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.util.ScrollBarSettingsConvert;
import org.jowidgets.impl.swt.widgets.SwtContainerWidget;
import org.jowidgets.spi.widgets.IScrollContainerWidgetSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;

public class ScrollCompositeWidget implements IScrollContainerWidgetSpi {

	private final SwtContainerWidget outerCompositeWidget;
	private final CompositeWidget innerCompositeWidget;

	private boolean mustChangeScrollCompositeMinSize;

	public ScrollCompositeWidget(
		final IGenericWidgetFactory factory,
		final IColorCache colorCache,
		final IWidget parent,
		final IScrollCompositeSetupSpi setup) {

		this.mustChangeScrollCompositeMinSize = true;

		final MigLayout growingMigLayout = new MigLayout("", "0[grow, 0::]0", "0[grow, 0::]0");
		final String growingCellConstraints = "grow, w 0::,h 0::";

		final Composite outerComposite = new HackyMinSizeComposite((Composite) parent.getUiReference(), SWT.NONE);
		this.outerCompositeWidget = new SwtContainerWidget(factory, colorCache, outerComposite);
		outerComposite.setLayout(growingMigLayout);

		final int style = ScrollBarSettingsConvert.convert(setup);
		final ScrolledComposite scrolledComposite = new ScrolledComposite(outerComposite, style);
		final SwtContainerWidget scrolledWidget = new SwtContainerWidget(factory, colorCache, scrolledComposite);
		scrolledComposite.setLayout(growingMigLayout);
		scrolledComposite.setLayoutData(growingCellConstraints);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(setup.isAllwaysShowBars());

		this.innerCompositeWidget = new CompositeWidget(factory, colorCache, scrolledWidget, setup);
		final Composite innerComposite = this.innerCompositeWidget.getUiReference();
		scrolledComposite.setContent(innerComposite);
		innerComposite.setLayoutData(growingCellConstraints);

		outerComposite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(final ControlEvent e) {
				if (mustChangeScrollCompositeMinSize) {
					scrolledComposite.setRedraw(false);
					scrolledComposite.setMinSize(innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					scrolledComposite.setRedraw(true);
					mustChangeScrollCompositeMinSize = false;
				}
			}

			@Override
			public void controlMoved(final ControlEvent e) {

			}
		});
		ColorSettingsInvoker.setColors(setup, this);
	}

	@Override
	public final void setLayout(final ILayoutDescriptor layout) {
		innerCompositeWidget.setLayout(layout);
	}

	@Override
	public Composite getUiReference() {
		return outerCompositeWidget.getUiReference();
	}

	@Override
	public void redraw() {
		outerCompositeWidget.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		innerCompositeWidget.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		innerCompositeWidget.setBackgroundColor(colorValue);
	}

	@Override
	public final <WIDGET_TYPE extends IWidget> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {
		mustChangeScrollCompositeMinSize = true;
		return innerCompositeWidget.add(descriptor, cellConstraints);
	}

	@Override
	public final <WIDGET_TYPE extends IWidget> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object cellConstraints) {
		mustChangeScrollCompositeMinSize = true;
		return innerCompositeWidget.add(factory, cellConstraints);
	}

	@Override
	public void layoutBegin() {
		outerCompositeWidget.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		outerCompositeWidget.layoutEnd();
	}

	@Override
	public void removeAll() {
		mustChangeScrollCompositeMinSize = true;
		innerCompositeWidget.removeAll();
	}

	// This following seems to be strange but its the only idea i have at the
	// moment to get a behavior of a scroll pane
	// known from swing!
	// If the UI-reference of this widget has not a min size like that w 0::, h
	// 0::, the scroll bars do never
	// appear.
	// Even if the min size could be set from outside (what is still possible),
	// api users should not bother
	// with that strange default behavior, so if min size is not set in
	// MigLayout constraints from outside,
	// this will be done in the HackyMinSizeComposite
	private class HackyMinSizeComposite extends Composite {

		public HackyMinSizeComposite(final Composite parent, final int style) {
			super(parent, style);
		}

		@Override
		public void setLayoutData(final Object layoutData) {
			final Layout layout = getLayout();
			if (layout instanceof MigLayout) {

				String layoutDataString = null;
				if (layoutData instanceof String) {
					layoutDataString = (String) layoutData;
					if (!layoutDataString.contains("w ") && !layoutDataString.contains(("width "))) {
						layoutDataString += ",w 0::";
					}
					if (!layoutDataString.contains("h ") && !layoutDataString.contains(("height "))) {
						layoutDataString += ",h 0::";
					}
				}
				else {
					layoutDataString = "w 0::, h 0::";
				}
				super.setLayoutData(layoutDataString);
				return;
			}
			super.setLayoutData(layoutData);
		}

	}

}

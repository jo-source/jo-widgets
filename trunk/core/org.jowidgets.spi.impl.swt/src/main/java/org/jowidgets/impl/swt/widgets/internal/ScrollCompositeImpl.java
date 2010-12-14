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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.impl.swt.util.BorderToComposite;
import org.jowidgets.impl.swt.util.ScrollBarSettingsConvert;
import org.jowidgets.impl.swt.widgets.SwtContainer;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;

public class ScrollCompositeImpl implements IScrollCompositeSpi {

	private final SwtContainer outerContainer;
	private final SwtContainer innerContainer;

	public ScrollCompositeImpl(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final IScrollCompositeSetupSpi setup) {

		final MigLayout growingMigLayout = new MigLayout("", "0[grow, 0::]0", "0[grow, 0::]0");
		final String growingCellConstraints = "grow, w 0::,h 0::";

		final Composite outerComposite;
		if (setup.getBorder() != null && setup.getBorder().getTitle() != null) {
			outerComposite = BorderToComposite.convert((Composite) parentUiReference, setup.getBorder());
		}
		else {
			outerComposite = new HackyMinSizeComposite((Composite) parentUiReference, SWT.NONE);
		}

		outerComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		this.outerContainer = new SwtContainer(factory, outerComposite);
		outerComposite.setLayout(growingMigLayout);

		int style = ScrollBarSettingsConvert.convert(setup);
		if (setup.getBorder() != null && setup.getBorder().getTitle() == null) {
			style = style | SWT.BORDER;
		}
		final ScrolledComposite scrolledComposite = new ScrolledComposite(outerComposite, style);
		final SwtContainer scrolledWidget = new SwtContainer(factory, scrolledComposite);
		scrolledComposite.setLayout(growingMigLayout);
		scrolledComposite.setLayoutData(growingCellConstraints);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(setup.isAllwaysShowBars());
		scrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);

		final Composite innerComposite = new Composite(scrolledWidget.getUiReference(), SWT.NONE);
		innerComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.innerContainer = new SwtContainer(factory, innerComposite);
		this.innerContainer.setLayout(setup.getLayout());
		scrolledComposite.setContent(innerComposite);
		innerComposite.setLayoutData(growingCellConstraints);

		try {
			innerComposite.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(final PaintEvent e) {
					scrolledComposite.setMinSize(innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			});
		}
		catch (final NoSuchMethodError error) {
			//TODO RWT has no paint listener
		}

	}

	@Override
	public final void setLayout(final ILayoutDescriptor layout) {
		innerContainer.setLayout(layout);
	}

	@Override
	public Composite getUiReference() {
		return outerContainer.getUiReference();
	}

	@Override
	public void setVisible(final boolean visible) {
		outerContainer.setVisible(visible);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		outerContainer.setEnabled(enabled);
		innerContainer.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return outerContainer.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return outerContainer.isVisible();
	}

	@Override
	public Dimension getSize() {
		return outerContainer.getSize();
	}

	@Override
	public void redraw() {
		outerContainer.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		outerContainer.setForegroundColor(colorValue);
		innerContainer.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		outerContainer.setBackgroundColor(colorValue);
		innerContainer.setBackgroundColor(colorValue);
	}

	@Override
	public void setCursor(final Cursor cursor) {
		outerContainer.setCursor(cursor);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return innerContainer.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return innerContainer.getBackgroundColor();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return innerContainer.createPopupMenu();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		innerContainer.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		innerContainer.removePopupDetectionListener(listener);
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {
		final WIDGET_TYPE result = innerContainer.add(descriptor, cellConstraints);
		return result;
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object cellConstraints) {
		return innerContainer.add(factory, cellConstraints);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return innerContainer.remove(control);
	}

	@Override
	public void layoutBegin() {
		outerContainer.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		outerContainer.layoutEnd();
	}

	@Override
	public void removeAll() {
		innerContainer.removeAll();
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

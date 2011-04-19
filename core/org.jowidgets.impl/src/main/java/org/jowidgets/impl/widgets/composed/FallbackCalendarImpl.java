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

import java.util.Date;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.ICalendar;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.descriptor.setup.ICalendarSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.IComponentListener;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IMouseButtonEvent;
import org.jowidgets.common.widgets.controler.IMouseEvent;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.tools.controler.MouseAdapter;

public class FallbackCalendarImpl implements ICalendar {

	private final IComposite composite;

	public FallbackCalendarImpl(final IComposite composite, final ICalendarSetup setup) {

		super();

		this.composite = composite;

		createContent(composite);

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);
	}

	private void createContent(final IContainer container) {
		final BluePrintFactory bpf = new BluePrintFactory();

		container.setBackgroundColor(Colors.WHITE);
		container.setLayout(new MigLayoutDescriptor("wrap", "0[]0[]0[]0[]0[]0[]0[]0", "0[]2[]2[]0[]0[]0[]0[]0[]0"));

		container.add(bpf.textLabel(" Mon ").alignCenter(), "alignx center, aligny center, sgx lgh");
		container.add(bpf.textLabel(" Tue ").alignCenter(), "alignx center, aligny center, sgx lgh");
		container.add(bpf.textLabel(" Wed ").alignCenter(), "alignx center, aligny center, sgx lgh");
		container.add(bpf.textLabel(" Thu ").alignCenter(), "alignx center, aligny center, sgx lgh");
		container.add(bpf.textLabel(" Fri ").alignCenter(), "alignx center, aligny center, sgx lgh");
		container.add(bpf.textLabel(" Sat ").alignCenter(), "alignx center, aligny center, sgx lgh");
		container.add(bpf.textLabel(" Sun ").alignCenter(), "alignx center, aligny center, sgx lgh");

		container.add(bpf.separator(), "growx, span 7");

		int day = 27;
		boolean gray = true;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				final IComposite outerCellComposite;
				if (i == 3 && j == 5) {
					outerCellComposite = container.add(bpf.composite().setBorder(), "alignx center, aligny center, grow, sgx cg");

				}
				else if (i == 3 && j == 4) {
					outerCellComposite = container.add(
							bpf.composite().setBorder().setBackgroundColor(Colors.DEFAULT_TABLE_EVEN_BACKGROUND_COLOR),
							"alignx center, aligny center, grow, sgx cg");
				}
				else {
					outerCellComposite = container.add(bpf.composite(), "alignx center, aligny center, grow, sgx cg");
				}

				outerCellComposite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));

				final IComposite innerCellComposite = outerCellComposite.add(
						bpf.composite(),
						"alignx center, aligny center, sgx lgh");

				innerCellComposite.setLayout(new MigLayoutDescriptor("fill", "0[]0", "0[]0"));

				final ITextLabel label = innerCellComposite.add(
						bpf.textLabel().setText(("" + (day))).alignRight(),
						"alignx right, aligny center, sg lg");

				//label.setBackgroundColor(Colors.STRONG);

				if (gray) {
					label.setForegroundColor(Colors.DISABLED);
				}

				day++;
				if (day == 32) {
					gray = !gray;
					day = 1;
				}

				final IMouseListener mouseListener = new MouseAdapter() {

					@Override
					public void mouseReleased(final IMouseButtonEvent event) {
						//CHECKSTYLE:OFF
						System.out.println("Pressed: " + event);
						//CHECKSTYLE:ON
					}

					@Override
					public void mouseExit(final IMouseEvent event) {
						//CHECKSTYLE:OFF
						System.out.println("Exit: " + event);
						//CHECKSTYLE:ON
					}

					@Override
					public void mouseEnter(final IMouseEvent event) {
						//CHECKSTYLE:OFF
						System.out.println("Enter: " + event);
						//CHECKSTYLE:ON
					}

				};
				outerCellComposite.addMouseListener(mouseListener);
				label.addMouseListener(mouseListener);
			}
		}
	}

	@Override
	public void setDate(final Date date) {
		// TODO Auto-generated method stub	
	}

	@Override
	public Date getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		// TODO Auto-generated method stub	
	}

	@Override
	public IContainer getParent() {
		return composite.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		composite.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return composite.isReparentable();
	}

	@Override
	public Object getUiReference() {
		return composite.getUiReference();
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		composite.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return composite.getLayoutConstraints();
	}

	@Override
	public void redraw() {
		composite.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		composite.setRedrawEnabled(enabled);
	}

	@Override
	public void setCursor(final Cursor cursor) {
		composite.setCursor(cursor);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		composite.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		composite.setBackgroundColor(colorValue);
		composite.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return composite.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return composite.getBackgroundColor();
	}

	@Override
	public void setVisible(final boolean visible) {
		composite.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return composite.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		composite.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return composite.isEnabled();
	}

	@Override
	public Dimension getSize() {
		return composite.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		composite.setSize(size);
	}

	@Override
	public Position getPosition() {
		return composite.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		composite.setPosition(position);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return composite.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return composite.toLocal(screenPosition);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return composite.fromComponent(component, componentPosition);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return composite.toComponent(componentPosition, component);
	}

	@Override
	public boolean requestFocus() {
		return composite.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		composite.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		composite.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		composite.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		composite.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		composite.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		composite.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		composite.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		composite.removeComponentListener(componentListener);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return composite.createPopupMenu();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		//TODO MG this might not work, popup must be set on all sub components 
		composite.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		composite.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		composite.removePopupDetectionListener(listener);
	}

}

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
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ICalendar;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.setup.ICalendarSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IMouseButtonEvent;
import org.jowidgets.common.widgets.controler.IMouseEvent;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.tools.controler.InputObservable;
import org.jowidgets.tools.controler.MouseAdapter;

public class FallbackCalendarImpl extends CompositeBasedControl implements ICalendar {

	private static final String MONDAY = "Mon";
	private static final String TUESDAY = "Tue";
	private static final String WEDNESDAY = "Wed";
	private static final String THURSDAY = "Thu";
	private static final String FRIDAY = "Fri";
	private static final String SATURDAY = "Sat";
	private static final String SUNDAY = "Sun";

	private final InputObservable inputObservable;
	private final DayButton[][] dayButtons;

	public FallbackCalendarImpl(final IComposite composite, final ICalendarSetup setup) {
		super(composite);
		this.inputObservable = new InputObservable();
		this.dayButtons = new DayButton[6][7];

		createContent(composite);

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);
	}

	private void createContent(final IContainer container) {
		final BluePrintFactory bpf = new BluePrintFactory();

		container.setBackgroundColor(Colors.WHITE);
		container.setLayout(new MigLayoutDescriptor("hidemode 3, wrap", "0[]0[]0[]0[]0[]0[]0[]0", "0[]2[]2[]0[]0[]0[]0[]0[]0"));

		final String headerConstraints = "alignx center, aligny center, sg lgh";
		container.add(bpf.textLabel(" " + MONDAY + " ").alignCenter(), headerConstraints);
		container.add(bpf.textLabel(" " + TUESDAY + " ").alignCenter(), headerConstraints);
		container.add(bpf.textLabel(" " + WEDNESDAY + " ").alignCenter(), headerConstraints);
		container.add(bpf.textLabel(" " + THURSDAY + " ").alignCenter(), headerConstraints);
		container.add(bpf.textLabel(" " + FRIDAY + " ").alignCenter(), headerConstraints);
		container.add(bpf.textLabel(" " + SATURDAY + " ").alignCenter(), headerConstraints);
		container.add(bpf.textLabel(" " + SUNDAY + " ").alignCenter(), headerConstraints);

		container.add(bpf.separator(), "growx, span 7");

		int day = 27;
		boolean gray = true;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				final DayButton dayButton = new DayButton(container);

				dayButtons[i][j] = dayButton;

				dayButton.setText("" + (day));

				if (i == 3 && j == 4) {
					dayButton.setBorder(true);
				}

				if (i == 2 && j == 2) {
					dayButton.setBorder(true);
					dayButton.setBackgroundColor(Colors.DEFAULT_TABLE_EVEN_BACKGROUND_COLOR);
				}

				if (gray) {
					dayButton.setForegroundColor(Colors.DISABLED);
				}

				day++;
				if (day == 32) {
					gray = !gray;
					day = 1;
				}
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
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	private final class DayButton {

		private static final String OUTER_CONSTRAINTS = "hidemode 3, alignx center, aligny center, grow, sg cg";
		private static final String INNER_CONSTRAINTS = "alignx center, aligny center, sg ig";
		private static final String LABEL_CONSTRAINTS = "alignx right, aligny center, sg lg";

		private final IComposite outerComposite;
		private final IComposite innerComposite;
		private final ITextLabel label;

		private final IComposite outerCompositeBorder;
		private final IComposite innerCompositeBorder;
		private final ITextLabel labelBorder;

		private DayButton(final IContainer container) {

			final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

			this.outerCompositeBorder = container.add(bpf.composite().setBorder(), OUTER_CONSTRAINTS);
			this.outerComposite = container.add(bpf.composite(), OUTER_CONSTRAINTS);
			outerCompositeBorder.setVisible(false);

			final MigLayoutDescriptor outerLayout = new MigLayoutDescriptor("0[grow]0", "0[grow]0");
			outerCompositeBorder.setLayout(outerLayout);
			outerComposite.setLayout(outerLayout);

			this.innerCompositeBorder = outerCompositeBorder.add(bpf.composite(), INNER_CONSTRAINTS);
			this.innerComposite = outerComposite.add(bpf.composite(), INNER_CONSTRAINTS);

			final MigLayoutDescriptor innerLayout = new MigLayoutDescriptor("0[]0", "0[]0");
			innerCompositeBorder.setLayout(innerLayout);
			innerComposite.setLayout(innerLayout);

			labelBorder = innerCompositeBorder.add(bpf.textLabel().alignRight(), LABEL_CONSTRAINTS);
			label = innerComposite.add(bpf.textLabel().alignRight(), LABEL_CONSTRAINTS);

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

			outerCompositeBorder.addMouseListener(mouseListener);
			outerComposite.addMouseListener(mouseListener);
			innerCompositeBorder.addMouseListener(mouseListener);
			innerComposite.addMouseListener(mouseListener);
			labelBorder.addMouseListener(mouseListener);
			label.addMouseListener(mouseListener);

		}

		public void setBorder(final boolean border) {
			outerCompositeBorder.setVisible(border);
			outerComposite.setVisible(!border);
		}

		public void setForegroundColor(final IColorConstant color) {
			label.setForegroundColor(color);
			labelBorder.setForegroundColor(color);
		}

		public void setBackgroundColor(final IColorConstant color) {
			outerCompositeBorder.setBackgroundColor(color);
			outerComposite.setBackgroundColor(color);
		}

		public void setText(final String text) {
			label.setText(text);
			labelBorder.setText(text);
		}
	}

}

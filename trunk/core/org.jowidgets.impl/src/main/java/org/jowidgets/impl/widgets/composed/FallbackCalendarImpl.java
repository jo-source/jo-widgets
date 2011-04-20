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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ICalendar;
import org.jowidgets.api.widgets.IComposite;
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
import org.jowidgets.util.Assert;

public class FallbackCalendarImpl extends CompositeBasedControl implements ICalendar {

	private static final Calendar CALENDAR = new GregorianCalendar();
	private static final Locale LOCALE = Locale.getDefault();

	private final InputObservable inputObservable;
	private final DayButton[][] dayButtons;

	private Date date;

	public FallbackCalendarImpl(final IComposite composite, final ICalendarSetup setup) {
		super(composite);
		this.inputObservable = new InputObservable();
		this.dayButtons = new DayButton[6][7];

		createContent(composite);

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		if (setup.getDate() != null) {
			setDate(setup.getDate());
		}
		else {
			setDate(new Date());
		}
	}

	private void createContent(final IComposite composite) {
		final BluePrintFactory bpf = new BluePrintFactory();

		composite.setBackgroundColor(Colors.WHITE);

		composite.setLayout(new MigLayoutDescriptor(
			"hidemode 3, wrap",
			"0[28::]0[28::]0[28::]0[28::]0[28::]0[28::]0[28::]0",
			"0[]2[]2[]0[]0[]0[]0[]0[]0"));

		int dayOfWeek = CALENDAR.getFirstDayOfWeek();

		final String headerConstraints = "alignx center, aligny center, sg lgh";
		for (int i = 0; i < 7; i++) {
			composite.add(bpf.textLabel(getShortDayLabel(dayOfWeek)).alignCenter(), headerConstraints);
			dayOfWeek = getNextDayOfWeek(dayOfWeek);
		}

		composite.add(bpf.separator(), "growx, span 7");

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				dayButtons[i][j] = new DayButton(composite);
			}
		}
	}

	@Override
	public void setDate(final Date date) {
		Assert.paramNotNull(date, "date");

		final Calendar iteratingCalendar = new GregorianCalendar();
		final Calendar calendar = new GregorianCalendar();
		final Calendar current = new GregorianCalendar();

		iteratingCalendar.setTime(date);
		calendar.setTime(date);
		current.setTime(new Date());

		iteratingCalendar.set(Calendar.DAY_OF_MONTH, 1);

		final int firstDayOfWeek = iteratingCalendar.getFirstDayOfWeek();
		int dayOfWeek = iteratingCalendar.get(Calendar.DAY_OF_WEEK);

		while (dayOfWeek > firstDayOfWeek) {
			iteratingCalendar.add(Calendar.DAY_OF_MONTH, -1);
			dayOfWeek = iteratingCalendar.get(Calendar.DAY_OF_WEEK);
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				final DayButton dayButton = dayButtons[i][j];
				dayButton.setText("" + iteratingCalendar.get(Calendar.DAY_OF_MONTH));
				if (iteratingCalendar.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
					dayButton.setForegroundColor(Colors.DISABLED);
				}
				if (iteratingCalendar.get(Calendar.MONTH) == current.get(Calendar.MONTH)
					&& iteratingCalendar.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH)) {
					dayButton.setBorder(true);
				}
				if (iteratingCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
					&& iteratingCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
					dayButton.setBorder(true);
					dayButton.setBackgroundColor(Colors.SELECTED_BACKGROUND);
				}
				iteratingCalendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		this.date = date;
	}

	@Override
	public Date getDate() {
		return date;
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

		private final IComposite rootComposite;

		private final IComposite outerComposite;
		private final IComposite innerComposite;
		private final ITextLabel label;

		private final IComposite outerCompositeBorder;
		private final IComposite innerCompositeBorder;
		private final ITextLabel labelBorder;

		private DayButton(final IComposite composite) {

			final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

			this.rootComposite = composite.add(bpf.composite(), OUTER_CONSTRAINTS);
			this.rootComposite.setLayout(new MigLayoutDescriptor("hidemode 3", "0[grow]0", "0[grow]0"));

			this.outerCompositeBorder = rootComposite.add(bpf.composite().setBorder(), OUTER_CONSTRAINTS);
			this.outerComposite = rootComposite.add(bpf.composite(), OUTER_CONSTRAINTS);
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

			final IMouseListener outerListener = new MouseAdapter() {

				@Override
				public void mouseReleased(final IMouseButtonEvent event) {
					//CHECKSTYLE:OFF
					System.out.println("Pressed: " + event);
					//CHECKSTYLE:ON
				}

				@Override
				public void mouseExit(final IMouseEvent event) {
					//CHECKSTYLE:OFF
					setBorder(false);
					System.out.println("Exit: " + event);
					//CHECKSTYLE:ON
				}

				@Override
				public void mouseEnter(final IMouseEvent event) {
					//CHECKSTYLE:OFF
					setBorder(true);
					System.out.println("Enter: " + event);
					//CHECKSTYLE:ON
				}

			};

			final IMouseListener innerListener = new MouseAdapter() {

				@Override
				public void mouseReleased(final IMouseButtonEvent event) {
					//CHECKSTYLE:OFF
					System.out.println("Pressed: " + event);
					//CHECKSTYLE:ON
				}

				@Override
				public void mouseExit(final IMouseEvent event) {
					//CHECKSTYLE:OFF
					setBorder(event);
					System.out.println("Exit: " + event);
					//CHECKSTYLE:ON
				}

				@Override
				public void mouseEnter(final IMouseEvent event) {
					//CHECKSTYLE:OFF
					setBorder(event);
					System.out.println("Enter: " + event);
					//CHECKSTYLE:ON
				}

			};

			outerCompositeBorder.addMouseListener(outerListener);
			outerComposite.addMouseListener(outerListener);
			innerCompositeBorder.addMouseListener(innerListener);
			innerComposite.addMouseListener(innerListener);
			labelBorder.addMouseListener(innerListener);
			label.addMouseListener(innerListener);

		}

		public void setBorder(final IMouseEvent event) {
			final int mouseX = event.getPosition().getX();
			final int mouseY = event.getPosition().getY();

			final int x = outerCompositeBorder.getPosition().getX();
			final int y = outerCompositeBorder.getPosition().getY();

			final int width = outerCompositeBorder.getSize().getWidth();
			final int height = outerCompositeBorder.getSize().getHeight();

			if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
				setBorder(true);
			}
			else {
				setBorder(false);
			}
		}

		public void setBorder(final boolean border) {
			rootComposite.layoutBegin();
			outerCompositeBorder.setVisible(border);
			outerComposite.setVisible(!border);
			rootComposite.layoutEnd();
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

	private int getNextDayOfWeek(final int dayOfWeek) {
		if (dayOfWeek == 7) {
			return 1;
		}
		else {
			return dayOfWeek + 1;
		}
	}

	private static String getShortDayLabel(final int day) {
		CALENDAR.set(Calendar.DAY_OF_WEEK, day);
		final String result = CALENDAR.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, LOCALE);
		if (result == null) {
			return getFallbackLabel(day);
		}
		return result;
	}

	private static String getFallbackLabel(final int day) {
		if (Calendar.MONDAY == day) {
			return "Mon";
		}
		else if (Calendar.TUESDAY == day) {
			return "Tue";
		}
		else if (Calendar.WEDNESDAY == day) {
			return "Wed";
		}
		else if (Calendar.THURSDAY == day) {
			return "Thu";
		}
		else if (Calendar.FRIDAY == day) {
			return "Fri";
		}
		else if (Calendar.SATURDAY == day) {
			return "Sat";
		}
		else if (Calendar.SUNDAY == day) {
			return "Sun";
		}
		return null;
	}

}

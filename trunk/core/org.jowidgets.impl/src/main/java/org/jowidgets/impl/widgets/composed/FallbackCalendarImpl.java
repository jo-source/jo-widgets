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
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ICalendar;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.descriptor.setup.ICalendarSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IMouseEvent;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.composed.MonthComposite.IMouseoverListener;
import org.jowidgets.tools.controler.InputObservable;
import org.jowidgets.tools.controler.MouseAdapter;
import org.jowidgets.tools.powo.JoButton;
import org.jowidgets.tools.powo.JoComposite;
import org.jowidgets.tools.powo.JoTextLabel;
import org.jowidgets.util.Assert;

public class FallbackCalendarImpl extends CompositeBasedControl implements ICalendar {

	private static final Calendar CALENDAR = new GregorianCalendar();
	private static final Locale LOCALE = Locale.getDefault();

	private static final int G_X = 16;
	private static final int G_Y = 8;

	private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

	private final InputObservable inputObservable;
	private final JoComposite composite;
	private final MonthComposite[] monthComposites;
	private final ITextLabel[] monthLabels;
	private final CalendarLayouter layouter;

	private final IButton navBackwardButton;
	private final IButton navPrevButton;
	private final IButton navNextButton;
	private final IButton navForwardButton;

	private Date date;

	public FallbackCalendarImpl(final IComposite composite, final ICalendarSetup setup) {
		super(composite);
		this.inputObservable = new InputObservable();
		this.monthComposites = new MonthComposite[12];
		this.monthLabels = new ITextLabel[12];

		this.composite = JoComposite.toJoComposite(composite);
		composite.setBackgroundColor(Colors.WHITE);
		this.layouter = new CalendarLayouter();
		this.composite.setLayout(layouter);

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		if (setup.getDate() != null) {
			this.date = setup.getDate();
		}
		else {
			this.date = new Date();
		}

		navBackwardButton = this.composite.add(new JoButton(IconsSmall.NAVIGATION_BACKWARD_TINY));
		navNextButton = this.composite.add(new JoButton(IconsSmall.NAVIGATION_NEXT_TINY));
		navPrevButton = this.composite.add(new JoButton(IconsSmall.NAVIGATION_PREVIOUS_TINY));
		navForwardButton = this.composite.add(new JoButton(IconsSmall.NAVIGATION_FORWARD_TINY));
		getMonthComposite(0);

		navPrevButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				interateMonth(-1);
			}
		});

		navNextButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				interateMonth(1);
			}
		});

		navBackwardButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				interateYear(-1);
			}
		});

		navForwardButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				interateYear(1);
			}
		});

		this.composite.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExit(final IMouseEvent event) {
				clearMouseOvers();
			}

			@Override
			public void mouseEnter(final IMouseEvent event) {
				clearMouseOvers();
			}

			private void clearMouseOvers() {
				for (int i = 0; i < monthComposites.length; i++) {
					if (monthComposites[i] != null) {
						monthComposites[i].clearMouseOver();
					}
				}
			}

		});
	}

	private void interateMonth(final int offset) {
		this.composite.layoutBegin();
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(monthComposites[0].getDate());
		calendar.add(Calendar.MONTH, offset);
		for (int i = 0; i < monthComposites.length; i++) {
			if (monthComposites[i] != null) {
				monthComposites[i].setDate(calendar.getTime(), date);
			}
			if (monthLabels[i] != null) {
				monthLabels[i].setText(getMonthDisplayName(calendar));
			}
			calendar.add(Calendar.MONTH, 1);
		}
		this.composite.layoutEnd();
	}

	private void interateYear(final int offset) {
		this.composite.layoutBegin();
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(monthComposites[0].getDate());
		calendar.add(Calendar.YEAR, offset);
		for (int i = 0; i < monthComposites.length; i++) {
			if (monthComposites[i] != null) {
				monthComposites[i].setDate(calendar.getTime(), date);
			}
			if (monthLabels[i] != null) {
				monthLabels[i].setText(getMonthDisplayName(calendar));
			}
			calendar.add(Calendar.MONTH, 1);
		}
		this.composite.layoutEnd();
	}

	@Override
	public void setDate(final Date date) {
		Assert.paramNotNull(date, "date");

		monthComposites[0].setDate(date, date);

		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		for (int i = 1; i < monthComposites.length; i++) {
			calendar.add(Calendar.MONTH, 1);
			if (monthComposites[i] != null) {
				monthComposites[i].setDate(calendar.getTime(), date);
			}
			if (monthLabels[i] != null) {
				monthLabels[i].setText(getMonthDisplayName(calendar));
			}
		}
		this.date = date;
		this.composite.layoutBegin();
		this.composite.layoutEnd();
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

	private MonthComposite getMonthComposite(final int index) {
		if (monthComposites[index] == null) {
			if (index == 0) {
				monthComposites[index] = new MonthComposite(this.date, date);
			}
			else {
				final Date prevDate = getMonthComposite(index - 1).getDate();
				final Calendar calendar = new GregorianCalendar();
				calendar.setTime(prevDate);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.MONTH, 1);
				monthComposites[index] = new MonthComposite(calendar.getTime(), date);
			}
			this.composite.add(monthComposites[index]);

			monthComposites[index].addInputListener(new IInputListener() {
				@Override
				public void inputChanged() {
					final Date selectedDate = monthComposites[index].getSelectedDate();
					if (selectedDate != null) {
						for (int i = 0; i < monthComposites.length; i++) {
							if (i != index && monthComposites[i] != null) {
								monthComposites[i].clearSelection();
							}
						}
						date = selectedDate;
						inputObservable.fireInputChanged();
					}
				}
			});

			monthComposites[index].addMouseOverListener(new IMouseoverListener() {
				@Override
				public void onMouseOver() {
					for (int i = 0; i < monthComposites.length; i++) {
						if (i != index && monthComposites[i] != null) {
							monthComposites[i].clearMouseOver();
						}
					}
				}
			});
		}
		return monthComposites[index];
	}

	private ITextLabel getMonthLabel(final int index) {
		if (monthLabels[index] == null) {
			final MonthComposite monthComposite = getMonthComposite(index);
			final Date monthDate = monthComposite.getDate();
			final Calendar calendar = new GregorianCalendar();
			calendar.setTime(monthDate);
			final String labelString = getMonthDisplayName(calendar);
			final JoTextLabel label = this.composite.add(new JoTextLabel(labelString));
			monthLabels[index] = label;
		}
		return monthLabels[index];
	}

	private class CalendarLayouter implements ILayouter {

		private Dimension preferredSize;

		@Override
		public void layout() {
			final Rectangle clientArea = composite.getClientArea();
			int x = clientArea.getX();
			int y = clientArea.getY() + G_Y;

			//layout the left nav buttons
			final Dimension navButtonSize = new Dimension(17, 17);
			final int buttonOffsetY = (getMonthLabel(0).getPreferredSize().getHeight() - navButtonSize.getHeight()) / 2;

			navBackwardButton.setSize(navButtonSize);
			navBackwardButton.setPosition(x, y + buttonOffsetY);

			x = x + navButtonSize.getWidth();
			navPrevButton.setSize(navButtonSize);
			navPrevButton.setPosition(x, y + buttonOffsetY);

			//layout the months
			final int prefHeight = getPreferredSize().getHeight();
			x = clientArea.getX();
			int maxX = x;
			for (int i = 0; i < monthComposites.length; i++) {
				final ITextLabel label = getMonthLabel(i);
				final MonthComposite monthComposite = getMonthComposite(i);

				final Dimension monthSize = monthComposite.getPreferredSize();
				final Dimension labelSize = label.getPreferredSize();

				boolean hasMoreMonth = false;

				if (x + monthSize.getWidth() <= clientArea.getX() + clientArea.getWidth()) {
					hasMoreMonth = true;
				}
				else if (y + 2 * prefHeight <= clientArea.getHeight()) {
					x = clientArea.getX();
					y = y + prefHeight + G_Y;
					hasMoreMonth = true;
				}
				if (hasMoreMonth) {
					//layout label
					final int offsetX = monthSize.getWidth() / 2 - labelSize.getWidth() / 2;
					label.setSize(labelSize);
					label.setPosition(x + offsetX, y);
					label.setVisible(true);

					final int offsetY = labelSize.getHeight() + G_Y;

					//layout month composite
					monthComposite.setSize(monthSize);
					monthComposite.setPosition(x, y + offsetY);
					monthComposite.setVisible(true);

					maxX = Math.max(maxX, x + monthSize.getWidth());
					x = x + monthSize.getWidth() + G_X;
				}
				else {
					monthComposite.setVisible(false);
					label.setVisible(false);
					if (i < monthComposites.length - 1 && monthComposites[i + 1] == null) {
						break;
					}
				}
			}

			//layout the rigth nav buttons
			y = clientArea.getY() + G_Y;

			x = maxX - navButtonSize.getWidth();
			navForwardButton.setSize(navButtonSize);
			navForwardButton.setPosition(x, y + buttonOffsetY);

			x = x - navButtonSize.getWidth();
			navNextButton.setSize(navButtonSize);
			navNextButton.setPosition(x, y + buttonOffsetY);
		}

		@Override
		public Dimension getMinSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			if (preferredSize == null) {
				preferredSize = calcPreferredSize();
			}
			return preferredSize;
		}

		@Override
		public Dimension getMaxSize() {
			return MAX_SIZE;
		}

		@Override
		public void invalidate() {}

		private Dimension calcPreferredSize() {
			final ITextLabel label = getMonthLabel(0);
			final MonthComposite monthComposite = getMonthComposite(0);

			final Dimension monthSize = monthComposite.getPreferredSize();
			final Dimension labelSize = label.getPreferredSize();

			final int width = monthSize.getWidth();
			final int height = monthSize.getHeight() + 2 * G_Y + Math.max(17, labelSize.getHeight());
			return composite.computeDecoratedSize(new Dimension(width, height));
		}
	}

	private static String getMonthDisplayName(final Calendar calendar) {
		return getMonthDisplayName(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
	}

	private static String getMonthDisplayName(final int month) {
		CALENDAR.set(Calendar.MONTH, month);
		final String result = CALENDAR.getDisplayName(Calendar.MONTH, Calendar.LONG, LOCALE);
		if (result == null) {
			return getFallbackDisplayName(month);
		}
		return result;
	}

	private static String getFallbackDisplayName(final int month) {
		if (Calendar.JANUARY == month) {
			return "January";
		}
		else if (Calendar.FEBRUARY == month) {
			return "February";
		}
		else if (Calendar.MARCH == month) {
			return "March";
		}
		else if (Calendar.APRIL == month) {
			return "April";
		}
		else if (Calendar.MAY == month) {
			return "May";
		}
		else if (Calendar.JUNE == month) {
			return "June";
		}
		else if (Calendar.JULY == month) {
			return "July";
		}
		else if (Calendar.AUGUST == month) {
			return "August";
		}
		else if (Calendar.SEPTEMBER == month) {
			return "September";
		}
		else if (Calendar.OCTOBER == month) {
			return "October";
		}
		else if (Calendar.NOVEMBER == month) {
			return "November";
		}
		else if (Calendar.DECEMBER == month) {
			return "December";
		}
		return null;
	}
}

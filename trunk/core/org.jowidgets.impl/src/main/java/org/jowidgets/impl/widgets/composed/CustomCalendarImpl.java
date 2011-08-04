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
import org.jowidgets.api.toolkit.Toolkit;
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
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.powo.JoButton;
import org.jowidgets.tools.powo.JoComposite;
import org.jowidgets.tools.powo.JoTextLabel;
import org.jowidgets.util.Assert;

public class CustomCalendarImpl extends CompositeBasedControl implements ICalendar {

	private static final Calendar CALENDAR = new GregorianCalendar();
	private static final Locale LOCALE = Locale.getDefault();

	private static final int MAX_MONTH = 12;
	private static final int G_X = 16;
	private static final int G_Y = 8;

	private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

	private final InputObservable inputObservable;
	private final JoComposite outerComposite;
	private final JoComposite innerComposite;
	private final MonthComposite[] monthComposites;
	private final ITextLabel[] monthLabels;
	private final CalendarLayouter layouter;

	private final IButton navBackwardButton;
	private final IButton navPrevButton;
	private final IButton navNextButton;
	private final IButton navForwardButton;

	private final MonthLayoutCache layoutCache;

	private Date date;
	private MonthComposite mouseoverComposite;

	public CustomCalendarImpl(final IComposite composite, final ICalendarSetup setup) {
		super(composite);

		this.layoutCache = new MonthLayoutCache();

		this.inputObservable = new InputObservable();
		this.monthComposites = new MonthComposite[MAX_MONTH];
		this.monthLabels = new ITextLabel[MAX_MONTH];

		this.outerComposite = JoComposite.toJoComposite(composite);

		this.innerComposite = new JoComposite(JoComposite.bluePrint().setBorder());
		this.innerComposite.setLayout(Toolkit.getLayoutFactoryProvider().nullLayout());
		this.outerComposite.add(innerComposite);

		composite.setBackgroundColor(Colors.WHITE);

		this.layouter = new CalendarLayouter();
		this.outerComposite.setLayout(layouter);

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		if (setup.getDate() != null) {
			this.date = setup.getDate();
		}
		else {
			this.date = new Date();
		}

		navBackwardButton = this.innerComposite.add(new JoButton(IconsSmall.NAVIGATION_BACKWARD_TINY));
		navNextButton = this.innerComposite.add(new JoButton(IconsSmall.NAVIGATION_NEXT_TINY));
		navPrevButton = this.innerComposite.add(new JoButton(IconsSmall.NAVIGATION_PREVIOUS_TINY));
		navForwardButton = this.innerComposite.add(new JoButton(IconsSmall.NAVIGATION_FORWARD_TINY));

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

		this.innerComposite.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExit(final IMouseEvent event) {
				clearMouseOvers();
			}

			@Override
			public void mouseEnter(final IMouseEvent event) {
				clearMouseOvers();
			}

			private void clearMouseOvers() {
				if (mouseoverComposite != null) {
					mouseoverComposite.clearMouseOver();
					mouseoverComposite = null;
				}
			}

		});
	}

	private void interateMonth(final int offset) {
		this.layouter.setLayoutNeeded(true);
		this.outerComposite.layoutBegin();
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
		this.outerComposite.layoutEnd();
	}

	private void interateYear(final int offset) {
		this.layouter.setLayoutNeeded(true);
		this.outerComposite.layoutBegin();
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
		this.outerComposite.layoutEnd();
	}

	@Override
	public void setDate(final Date date) {
		Assert.paramNotNull(date, "date");
		this.layouter.setLayoutNeeded(true);
		this.outerComposite.layoutBegin();
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
		this.outerComposite.layoutEnd();
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
				monthComposites[index] = new MonthComposite(this.date, date, layoutCache);
			}
			else {
				final Date prevDate = getMonthComposite(index - 1).getDate();
				final Calendar calendar = new GregorianCalendar();
				calendar.setTime(prevDate);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.MONTH, 1);
				monthComposites[index] = new MonthComposite(calendar.getTime(), date, layoutCache);
			}
			this.innerComposite.add(monthComposites[index]);

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
					if (mouseoverComposite != null && mouseoverComposite != monthComposites[index]) {
						mouseoverComposite.clearMouseOver();
					}
					mouseoverComposite = monthComposites[index];
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
			final JoTextLabel label = this.innerComposite.add(new JoTextLabel(labelString));
			monthLabels[index] = label;
		}
		return monthLabels[index];
	}

	private class CalendarLayouter implements ILayouter {

		private Dimension preferredSize;

		@SuppressWarnings("unused")
		private int rows;
		private int cols;
		private int cells;

		private boolean layoutNeeded;

		CalendarLayouter() {
			layoutNeeded = true;
		}

		void setLayoutNeeded(final boolean layoutNeeded) {
			this.layoutNeeded = layoutNeeded;
		}

		@Override
		public void layout() {

			outerComposite.setRedrawEnabled(false);

			final Rectangle outerClientArea = outerComposite.getClientArea();

			final Dimension lastSize = innerComposite.getSize();

			final Dimension prefSize = getPreferredSize();

			final boolean moreColsPossible = (lastSize.getWidth() + prefSize.getWidth() + G_X) <= outerClientArea.getWidth() + 6;
			final boolean moreRowsPossible = (lastSize.getHeight() + prefSize.getHeight() + G_Y) <= outerClientArea.getHeight() + 6;

			final boolean toManyCols = (lastSize.getWidth()) > outerClientArea.getWidth();
			final boolean toManyRows = (lastSize.getHeight()) > outerClientArea.getHeight();

			int x = 0;
			int y = 0;

			if (layoutNeeded
				|| cells == 0
				|| moreColsPossible
				|| (moreRowsPossible && !(cells == MAX_MONTH))
				|| toManyCols
				|| toManyRows) {

				layoutNeeded = false;

				innerComposite.setSize(outerComposite.getClientArea().getSize());
				final Rectangle clientArea = innerComposite.getClientArea();
				x = clientArea.getX();
				y = clientArea.getY() + G_Y;

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
				int maxY = y;
				rows = 1;
				cols = 1;
				cells = 0;
				int column = 1;
				for (int i = 0; i < monthComposites.length; i++) {
					final ITextLabel label = getMonthLabel(i);
					final MonthComposite monthComposite = getMonthComposite(i);

					final Dimension monthSize = monthComposite.getPreferredSize();
					final Dimension labelSize = label.getPreferredSize();

					boolean layoutMonth = i == 0;

					if (x + monthSize.getWidth() <= clientArea.getX() + clientArea.getWidth()) {
						if (!layoutMonth) {
							column++;
						}
						layoutMonth = true;
						cols = Math.max(cols, column);
					}
					else if (i != 0 && y + 2 * prefHeight <= clientArea.getHeight()) {
						x = clientArea.getX();
						y = y + prefHeight + G_Y;
						rows++;
						column = 1;
						layoutMonth = true;
					}
					if (layoutMonth) {
						cells++;
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
						maxY = y + prefHeight;
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

				final Dimension innerMaxSize = innerComposite.computeDecoratedSize(new Dimension(maxX, maxY));

				final int innerWidth = Math.min(innerMaxSize.getWidth(), outerClientArea.getWidth());
				final int innerHeight = Math.min(innerMaxSize.getHeight(), outerClientArea.getHeight());

				innerComposite.setSize(Math.max(prefSize.getWidth(), innerWidth), Math.max(prefSize.getHeight(), innerHeight));

			}

			final Dimension innerSize = innerComposite.getSize();

			x = outerClientArea.getX();
			y = outerClientArea.getY();

			if (outerClientArea.getWidth() - innerSize.getWidth() > 2) {
				x = x + (outerClientArea.getWidth() - innerSize.getWidth()) / 2;
			}
			if (outerClientArea.getHeight() - innerSize.getHeight() > 2) {
				y = y + (outerClientArea.getHeight() - innerSize.getHeight()) / 2;
			}

			innerComposite.setPosition(x, y);

			outerComposite.setRedrawEnabled(true);

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
			return outerComposite.computeDecoratedSize(innerComposite.computeDecoratedSize(new Dimension(width, height)));
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

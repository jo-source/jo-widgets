/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
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
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IInputObservable;
import org.jowidgets.common.widgets.controler.IMouseButtonEvent;
import org.jowidgets.common.widgets.controler.IMouseEvent;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.tools.controler.InputObservable;
import org.jowidgets.tools.controler.MouseAdapter;
import org.jowidgets.tools.powo.JoComposite;

public class MonthComposite extends JoComposite implements IInputObservable {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	private static final Calendar CALENDAR = new GregorianCalendar();
	private static final Locale LOCALE = Locale.getDefault();

	private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

	private static final int M_X = 0;
	private static final int M_Y = 1;

	private static final int MIN_M_X = 6;
	private static final int MIN_M_Y = 0;

	private static final int G_X = 1;
	private static final int G_Y = 1;

	private final InputObservable inputObservable;
	private final Set<IMouseoverListener> mouseoverListeners;

	private final CalendarButton[] headerButtons;
	private final CalendarButton[][] dayButtons;
	private final IControl separator;
	private final ILayouter layouter;
	private Date date;

	private CalendarButton mouseoverButton;
	private CalendarButton selectedButton;
	private CalendarButton todayButton;

	public MonthComposite(final Date date, final Date selectedDate) {

		this.inputObservable = new InputObservable();
		this.mouseoverListeners = new HashSet<IMouseoverListener>();

		this.headerButtons = new CalendarButton[7];
		this.dayButtons = new CalendarButton[6][7];

		this.layouter = new CalendarPanelLayouter(this);
		setLayout(layouter);

		createHeader();
		this.separator = add(BPF.separator());
		createDays();
		setDate(date, selectedDate);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExit(final IMouseEvent event) {
				if (mouseoverButton != null) {
					mouseoverButton.setMouseover(false);
				}
			}

			@Override
			public void mouseEnter(final IMouseEvent event) {
				if (mouseoverButton != null) {
					mouseoverButton.setMouseover(false);
				}
			}

		});

		setBackgroundColor(Colors.WHITE);

	}

	public Date getDate() {
		return date;
	}

	public Date getSelectedDate() {
		if (selectedButton != null) {
			return selectedButton.date;
		}
		else {
			return null;
		}
	}

	public void clearSelection() {
		if (selectedButton != null) {
			selectedButton.setSelected(false);
		}
		selectedButton = null;
	}

	public void clearMouseOver() {
		if (mouseoverButton != null) {
			mouseoverButton.setMouseover(false);
		}
		mouseoverButton = null;
	}

	public void addMouseOverListener(final IMouseoverListener mouseoverListener) {
		this.mouseoverListeners.add(mouseoverListener);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	private void createHeader() {
		int dayOfWeek = CALENDAR.getFirstDayOfWeek();
		for (int i = 0; i < 7; i++) {
			headerButtons[i] = new CalendarButton(this);
			headerButtons[i].setContent(getShortDayLabel(dayOfWeek), null);
			dayOfWeek = getNextDayOfWeek(dayOfWeek);
		}
	}

	private void createDays() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				dayButtons[i][j] = new CalendarButton(this);
			}
		}
	}

	public void setDate(final Date date, final Date selectedDate) {
		this.date = date;

		clearSelection();
		clearMouseOver();

		if (todayButton != null) {
			todayButton.setBorder(false);
			todayButton = null;
		}

		final Calendar iteratingCalendar = new GregorianCalendar();
		final Calendar calendar = new GregorianCalendar();
		final Calendar current = new GregorianCalendar();
		Calendar selectedCalendar = null;
		if (selectedDate != null) {
			selectedCalendar = new GregorianCalendar();
			selectedCalendar.setTime(selectedDate);
		}
		iteratingCalendar.setTime(date);
		calendar.setTime(date);
		current.setTime(new Date());

		iteratingCalendar.set(Calendar.DAY_OF_MONTH, 1);

		final int firstDayOfWeek = iteratingCalendar.getFirstDayOfWeek();
		int dayOfWeek = iteratingCalendar.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek < firstDayOfWeek) {
			while (dayOfWeek < firstDayOfWeek) {
				iteratingCalendar.add(Calendar.DAY_OF_MONTH, 1);
				dayOfWeek = iteratingCalendar.get(Calendar.DAY_OF_WEEK);
			}
		}
		else if (dayOfWeek > firstDayOfWeek) {
			while (dayOfWeek > firstDayOfWeek) {
				iteratingCalendar.add(Calendar.DAY_OF_MONTH, -1);
				dayOfWeek = iteratingCalendar.get(Calendar.DAY_OF_WEEK);
			}
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				final CalendarButton dayButton = dayButtons[i][j];
				dayButton.setContent("" + iteratingCalendar.get(Calendar.DAY_OF_MONTH), iteratingCalendar.getTime());

				if (iteratingCalendar.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
					dayButton.setForegroundColor(Colors.DISABLED);
				}
				else {
					dayButton.setForegroundColor(null);
				}
				if (selectedCalendar != null
					&& iteratingCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
					&& iteratingCalendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH)
					&& iteratingCalendar.get(Calendar.DAY_OF_MONTH) == selectedCalendar.get(Calendar.DAY_OF_MONTH)
					&& iteratingCalendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR)) {
					dayButton.setSelected(true);
				}
				if (iteratingCalendar.get(Calendar.MONTH) == current.get(Calendar.MONTH)
					&& iteratingCalendar.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH)
					&& iteratingCalendar.get(Calendar.YEAR) == current.get(Calendar.YEAR)) {
					dayButton.setToday();
				}
				iteratingCalendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
	}

	private void fireOnMouseOver() {
		for (final IMouseoverListener listener : mouseoverListeners) {
			listener.onMouseOver();
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

	private final class CalendarPanelLayouter implements ILayouter {

		private final MonthComposite calendarPanel;

		private Dimension prefSize;
		private Dimension dayMaxSize;
		private Dimension headerMaxSize;
		private int separatorHeight;

		private boolean layoutet;

		CalendarPanelLayouter(final MonthComposite calendarPanel) {
			this.calendarPanel = calendarPanel;
			this.separatorHeight = -1;
		}

		@Override
		public void layout() {
			if (layoutet) {
				return;
			}

			final Rectangle clientArea = calendarPanel.getClientArea();

			final int prefWidth = getPreferredSize().getWidth();

			int x = clientArea.getX();
			int y = clientArea.getY();

			final Dimension dayMax = getDayMaxSize();
			final Dimension headerMax = getHeaderMaxSize();

			final int marginX = Math.max(MIN_M_X, M_X + Math.abs(headerMax.getWidth() - dayMax.getWidth()));
			final int marginY = Math.max(MIN_M_Y, M_Y + Math.abs(headerMax.getHeight() - dayMax.getHeight()));

			final int sizeX = 2 * marginX + dayMax.getWidth();
			final int sizeY = 2 * marginY + dayMax.getHeight();

			//layout the header buttons
			for (int i = 0; i < 7; i++) {
				final CalendarButton headerButton = headerButtons[i];
				headerButton.setPosition(new Position(x, y));
				headerButton.layout(marginX, marginY, sizeX, sizeY, headerMax);

				x += sizeX + G_X;
			}
			x = clientArea.getX();
			y += sizeY + G_Y;

			//layout the separator
			separator.setSize(prefWidth, getSeparatorHeight());
			separator.setPosition(x, y);

			y += getSeparatorHeight() + G_Y;

			//layout the days
			for (int i = 0; i < 6; i++) {
				x = clientArea.getX();
				for (int j = 0; j < 7; j++) {
					final CalendarButton day = dayButtons[i][j];
					day.setPosition(new Position(x, y));
					day.layout(marginX, marginY, sizeX, sizeY, dayMax);

					x += sizeX + G_X;
				}
				y += sizeY + G_Y;
			}

			layoutet = true;
		}

		@Override
		public Dimension getMinSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			if (prefSize == null) {
				this.prefSize = calcPrefSize();
			}
			return prefSize;
		}

		@Override
		public Dimension getMaxSize() {
			return MAX_SIZE;
		}

		@Override
		public void invalidate() {}

		private Dimension calcPrefSize() {
			final Dimension dayMax = getDayMaxSize();
			final Dimension headerMax = getHeaderMaxSize();

			final int marginX = Math.max(MIN_M_X, M_X + Math.abs(headerMax.getWidth() - dayMax.getWidth()));
			final int marginY = Math.max(MIN_M_Y, M_Y + Math.abs(headerMax.getHeight() - dayMax.getHeight()));

			final Dimension labelSize = getDayMaxSize();
			final int width = 14 * marginX + 7 * labelSize.getWidth() + 6 * G_X;
			final int height = 14 * marginY + 7 * labelSize.getHeight() + 7 * G_Y + getSeparatorHeight();
			return calendarPanel.computeDecoratedSize(new Dimension(width, height));
		}

		private Dimension getDayMaxSize() {
			if (dayMaxSize == null) {
				this.dayMaxSize = calcDayMaxSize();
			}
			return dayMaxSize;
		}

		private Dimension getHeaderMaxSize() {
			if (headerMaxSize == null) {
				this.headerMaxSize = calcHeaderMaxSize();
			}
			return headerMaxSize;
		}

		private Dimension calcDayMaxSize() {
			int width = 0;
			int height = 0;

			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					final CalendarButton day = dayButtons[i][j];
					final Dimension size = day.getLabelPreferredSize();
					width = Math.max(width, size.getWidth());
					height = Math.max(height, size.getHeight());
				}
			}

			return new Dimension(width, height);
		}

		private Dimension calcHeaderMaxSize() {
			int width = 0;
			int height = 0;

			for (int i = 0; i < 7; i++) {
				final CalendarButton header = headerButtons[i];
				final Dimension size = header.getLabelPreferredSize();
				width = Math.max(width, size.getWidth());
				height = Math.max(height, size.getHeight());
			}

			return new Dimension(width, height);
		}

		private int getSeparatorHeight() {
			if (separatorHeight == -1) {
				this.separatorHeight = calcSeparatorHeight();
			}
			return separatorHeight;
		}

		private int calcSeparatorHeight() {
			return separator.getPreferredSize().getHeight();
		}
	}

	private final class CalendarButton {

		private final IContainer container;

		private final IComposite composite;
		private final IComposite compositeBorder;

		private final ITextLabel label;
		private final ITextLabel labelBorder;

		private Date date;

		private final IMouseListener compositeListener;
		private final IMouseListener labelListener;

		private Dimension labelPreferredSize;

		private boolean hasBorder;
		private boolean mouseover;
		private boolean isToday;
		private IColorConstant foregroundColor;

		CalendarButton(final IContainer container) {
			this.container = container;

			this.composite = container.add(BPF.composite());
			this.compositeBorder = container.add(BPF.compositeWithBorder());

			this.hasBorder = false;
			this.mouseover = false;
			this.isToday = false;

			composite.setLayout(Toolkit.getLayoutFactoryProvider().nullLayout());
			compositeBorder.setLayout(Toolkit.getLayoutFactoryProvider().nullLayout());

			this.label = composite.add(BPF.textLabel().alignRight());
			this.labelBorder = compositeBorder.add(BPF.textLabel().alignRight());

			compositeBorder.setVisible(false);

			this.compositeListener = new MouseAdapter() {

				@Override
				public void mousePressed(final IMouseButtonEvent event) {
					setSelected(true);
					clearMouseOver();
					if (date != null) {
						inputObservable.fireInputChanged();
					}
				}

				@Override
				public void mouseExit(final IMouseEvent event) {
					if (isInsideCompositeEvent(event)) {
						setMouseover(true);
						fireOnMouseOver();
					}
					else {
						setMouseover(false);
					}
				}

				@Override
				public void mouseEnter(final IMouseEvent event) {
					setMouseover(true);
					fireOnMouseOver();
				}

			};

			this.labelListener = new MouseAdapter() {

				@Override
				public void mousePressed(final IMouseButtonEvent event) {
					setSelected(true);
					clearMouseOver();
					if (date != null) {
						inputObservable.fireInputChanged();
					}
				}

				@Override
				public void mouseExit(final IMouseEvent event) {
					if (isInsideLabelEvent(event)) {
						setMouseover(true);
						fireOnMouseOver();
					}
					else {
						setMouseover(false);
					}
				}

				@Override
				public void mouseEnter(final IMouseEvent event) {
					setMouseover(true);
					fireOnMouseOver();
				}

			};

			composite.addMouseListener(compositeListener);
			label.addMouseListener(labelListener);
		}

		Dimension getLabelPreferredSize() {
			if (labelPreferredSize == null) {
				labelPreferredSize = label.getPreferredSize();
			}
			return labelPreferredSize;
		}

		protected boolean isMouseover() {
			return mouseover;
		}

		private boolean isInsideCompositeEvent(final IMouseEvent event) {
			return isInside(container.fromComponent(composite, event.getPosition()));
		}

		private boolean isInsideLabelEvent(final IMouseEvent event) {
			return isInside(container.fromComponent(label, event.getPosition()));
		}

		private boolean isInside(final Position pos) {
			final int mouseX = pos.getX();
			final int mouseY = pos.getY();

			final int x = composite.getPosition().getX();
			final int y = composite.getPosition().getY();

			final int width = composite.getSize().getWidth();
			final int height = composite.getSize().getHeight();

			if (mouseX > x + 2 && mouseX < x + width - 2 && mouseY > y + 2 && mouseY < y + height - 2) {
				return true;
			}
			else {
				return false;
			}
		}

		void layout(final int marginX, final int marginY, final int sizeX, final int sizeY, final Dimension labelMaxSize) {
			composite.setSize(sizeX, sizeY);
			compositeBorder.setSize(sizeX, sizeY);

			label.setSize(labelMaxSize);
			labelBorder.setSize(labelMaxSize);

			final int x = sizeX - marginX - labelMaxSize.getWidth();
			final int y = sizeY - marginY - labelMaxSize.getHeight();

			final Rectangle clientArea = composite.getClientArea();
			final Rectangle clientAreaBorder = compositeBorder.getClientArea();

			final int borderX = (clientArea.getWidth() - clientAreaBorder.getWidth()) / 2;
			final int borderY = (clientArea.getHeight() - clientAreaBorder.getHeight()) / 2;

			label.setPosition(clientArea.getX() + x, clientArea.getY() + y);
			labelBorder.setPosition(clientAreaBorder.getX() + x - borderX, clientAreaBorder.getY() + y - borderY);

		}

		void setContent(final String text, final Date date) {
			label.setText(text);
			labelBorder.setText(text);
			this.date = date;
		}

		void setPosition(final Position position) {
			composite.setPosition(position);
			compositeBorder.setPosition(position);
		}

		void setToday() {
			this.isToday = true;
			todayButton = this;
			setBorder(true);
			setForegroundColor(Colors.SELECTED_BACKGROUND);
			if (selectedButton == this) {
				label.setForegroundColor(Colors.WHITE);
				labelBorder.setForegroundColor(Colors.WHITE);
			}
		}

		void setBorder(final boolean border) {
			this.hasBorder = border;
			setBorderImpl(border);
		}

		void setMouseover(final boolean mouseover) {
			if (mouseoverButton != this && mouseoverButton != null && mouseoverButton.isMouseover()) {
				mouseoverButton.setMouseover(false);
			}

			if (date != null && this.mouseover != mouseover && !hasBorder) {
				this.mouseover = mouseover;
				setBorderImpl(mouseover);
				if (mouseover) {
					mouseoverButton = this;
				}
			}

		}

		void setSelected(final boolean selected) {
			if (date != null) {
				if (selectedButton != null && selectedButton != this) {
					selectedButton.setSelected(false);
				}
				if (selected) {
					setBorder(true);
					composite.setBackgroundColor(Colors.SELECTED_BACKGROUND);
					compositeBorder.setBackgroundColor(Colors.SELECTED_BACKGROUND);
					label.setForegroundColor(Colors.WHITE);
					labelBorder.setForegroundColor(Colors.WHITE);
					selectedButton = this;
				}
				else {
					if (!isToday) {
						setBorder(false);
					}
					composite.setBackgroundColor(Colors.WHITE);
					compositeBorder.setBackgroundColor(Colors.WHITE);
					label.setForegroundColor(foregroundColor);
					labelBorder.setForegroundColor(foregroundColor);
					selectedButton = null;
				}

			}
		}

		private void setBorderImpl(final boolean border) {
			if (border) {
				composite.removeMouseListener(compositeListener);
				label.removeMouseListener(labelListener);
				compositeBorder.setVisible(true);
				composite.setVisible(false);
				compositeBorder.addMouseListener(compositeListener);
				labelBorder.addMouseListener(labelListener);
			}
			else {
				compositeBorder.removeMouseListener(compositeListener);
				labelBorder.removeMouseListener(labelListener);
				compositeBorder.setVisible(false);
				composite.setVisible(true);
				composite.addMouseListener(compositeListener);
				label.addMouseListener(labelListener);
			}
		}

		void setForegroundColor(final IColorConstant color) {
			label.setForegroundColor(color);
			labelBorder.setForegroundColor(color);
			this.foregroundColor = color;
		}

	}

	interface IMouseoverListener {
		void onMouseOver();
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

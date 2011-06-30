/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.spi.impl.swing.widgets.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class JoWidgetsTabLookAndFeel extends BasicTabbedPaneUI {

	private static final int ARC = 2;

	private Color selectedColor;

	public JoWidgetsTabLookAndFeel() {
		super();
	}

	@Override
	protected void installDefaults() {
		selectedColor = Color.WHITE;
		UIManager.getDefaults().put("TabbedPane.selected", selectedColor);
		super.installDefaults();
		lightHighlight = Color.gray;
	}

	private Rectangle getTopRight() {
		if (tabPane.getTabCount() > 0) {
			Rectangle result = rects[0];
			for (int i = 1; i < rects.length; i++) {
				if (rects[i].x + rects[i].width >= result.x + result.width && rects[i].y <= result.y) {
					result = rects[i];
				}
			}
			return result;
		}

		final Insets insets = tabPane.getInsets();
		return new Rectangle(insets.left, insets.top, 0, 0);
	}

	private int calculateTabHeight() {
		final Insets tabInsets = getTabInsets(0, 0);
		return getFontMetrics().getHeight() + tabInsets.top + tabInsets.bottom + 2;
	}

	@Override
	protected int calculateTabAreaHeight(final int tabPlacement, final int horizRunCount, final int maxTabHeight) {
		// TODO NM calculate padding (5)
		return Math.max(0, super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight) - 5);
	}

	private boolean isMultiRow() {
		if (tabPane.getTabCount() < 2) {
			return false;
		}

		final int y = rects[0].y;
		for (int i = 1; i < tabPane.getTabCount(); i++) {
			if (rects[i].y != y) {
				return true;
			}
		}
		return false;
	}

	private boolean isInFirstRow(final int tabIndex) {
		if (tabPane.getTabCount() < 2) {
			return true;
		}

		final int y = rects[tabIndex].y;
		for (int i = 0; i < tabPane.getTabCount(); i++) {
			if (rects[i].y < y) {
				return false;
			}
		}

		return true;
	}

	private boolean isLastInRow(final int tabIndex) {
		final int x = rects[tabIndex].x;
		final int y = rects[tabIndex].y;
		for (int i = 0; i < tabPane.getTabCount(); i++) {
			if ((rects[i].y == y) && (rects[i].x > x)) {
				return false;
			}
		}

		return true;
	}

	private boolean isFirstInRow(final int tabIndex) {
		final int x = rects[tabIndex].x;
		final int y = rects[tabIndex].y;
		for (int i = 0; i < tabPane.getTabCount(); i++) {
			if ((rects[i].y == y) && (rects[i].x < x)) {
				return false;
			}
		}

		return true;
	}

	private boolean isAfterSelected(final int tabIndex) {
		final Rectangle boundsSelected = rects[tabPane.getSelectedIndex()];
		final Rectangle boundsTab = rects[tabIndex];

		return (boundsTab.y == boundsSelected.y) && (boundsTab.x == boundsSelected.x + boundsSelected.width);
	}

	@Override
	protected void paintTabBorder(
		final Graphics g,
		final int tabPlacement,
		final int tabIndex,
		final int x,
		final int y,
		final int w,
		final int h,
		final boolean isSelected) {
		g.setColor(lightHighlight);

		final boolean isInFirstRow = isInFirstRow(tabIndex);
		final boolean isFirstInRow = isFirstInRow(tabIndex);
		final boolean isLastInRow = isLastInRow(tabIndex);
		final boolean isAfterSelected = isAfterSelected(tabIndex);

		final boolean isMultiRow = isMultiRow();

		final boolean hasLeftArc = isFirstInRow || isSelected || !isInFirstRow;
		final boolean hasRightArc = isSelected || !isInFirstRow || isLastInRow;

		switch (tabPlacement) {
			case BOTTOM:
				// TODO NM implement
				g.drawLine(x, y, x, y + h - 4); // left
				g.drawLine(x, y + h - 3, x + 2, y + h - 1);
				g.drawLine(x + 2, y + h - 1, x + w - 4, y + h - 1); // bottom              
				g.drawLine(x + w - 3, y + h - 1, x + w - 1, y + h - 3);
				g.drawLine(x + w - 1, y, x + w - 1, y + h - 3); // right
				break;
			case TOP:
			default:
				final int y1 = y;
				final int y2 = y + h;
				final int x1 = x;
				final int x2 = x + w;
				if (hasLeftArc) {
					g.drawLine(x1, y1 + ARC, x1 + ARC, y1); // arc
					g.drawLine(x1, y1 + ARC, x1, y2); // left down
					if (isInFirstRow && !isFirstInRow) {
						g.drawLine(x1, y1, x1 + ARC, y1);
					}
				}
				else {
					g.drawLine(x1, y1, x1 + ARC, y1); // top connector
					if (!isAfterSelected) {
						g.drawLine(x1, y1, x1, y2);
					}
				}
				if (hasRightArc) {
					g.drawLine(x2 - ARC, y1, x2, y1 + ARC); // arc
					g.drawLine(x2, y1 + ARC, x2, y2); // right down
				}
				else {
					if (isLastInRow && !isMultiRow) {
						g.drawLine(x2, y1, x2, y2);
					}
				}

				g.drawLine(x1 + ARC, y1, x2 - ARC, y1); // top line              
				if (isInFirstRow) {
					g.drawLine(x2 - ARC, y1, x2, y1); // top connector
				}
		}
	}

	@Override
	protected void paintTabBackground(
		final Graphics g,
		final int tabPlacement,
		final int tabIndex,
		final int x,
		final int y,
		final int w,
		final int h,
		final boolean isSelected) {
		g.setColor(isSelected ? selectedColor : tabPane.getBackgroundAt(tabIndex));

		switch (tabPlacement) {
			case BOTTOM:
				g.fillRect(x + 1, y, w - 1, h - 1);
				break;
			case TOP:
			default:
				g.fillRect(x + 1, y + 1, w - 1, h - 1);
		}
	}

	@Override
	protected void paintContentBorderTopEdge(
		final Graphics g,
		final int tabPlacement,
		final int selectedIndex,
		final int x,
		final int y,
		final int w,
		final int h) {
		g.setColor(darkShadow);
		if (tabPlacement == BOTTOM || selectedIndex < 0) {
			g.drawLine(x, y, x + w - 2, y);
		}
		else {
			final Rectangle bounds = getTabBounds(selectedIndex, calcRect);
			g.drawLine(x, y, bounds.x, y);
			if (bounds.x + bounds.width < x + w - 2) {
				g.setColor(lightHighlight);
				g.drawLine(bounds.x + bounds.width, y, x + w - 2, y);
			}
		}
	}

	@Override
	protected void paintContentBorderBottomEdge(
		final Graphics g,
		final int tabPlacement,
		final int selectedIndex,
		final int x,
		final int y,
		final int w,
		final int h) {
		g.setColor(lightHighlight);
		final int lineY = y + h - 2;
		if (tabPlacement == TOP || selectedIndex < 0) {
			g.drawLine(x, lineY, x + w - 2, lineY);
		}
		else {
			final Rectangle bounds = getTabBounds(selectedIndex, calcRect);
			g.drawLine(x, lineY, bounds.x, lineY);
			if (bounds.x + bounds.width < x + w - 2) {
				g.drawLine(bounds.x + bounds.width, lineY, x + w - 2, lineY);
			}
		}
	}

	@Override
	public void paint(final Graphics g, final JComponent c) {
		super.paint(g, c);
		final int tabPlacement = tabPane.getTabPlacement();
		final Insets insets = tabPane.getInsets();
		if (tabPlacement == BOTTOM) {
			if (tabPane.getTabCount() > 0) {
				final Rectangle lastTab = getTopRight();

				final int border = 2;

				final int edgeX = Math.max(insets.left + border, lastTab.x + lastTab.width - border);
				final int edgeW = tabPane.getWidth() - insets.right - edgeX - 2;
				final int edgeY = lastTab.y;
				final int edgeH = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight - 1);

				g.setColor(lightHighlight);
				g.drawLine(edgeX, edgeY + edgeH, edgeX + edgeW - 3, edgeY + edgeH); // bottom              
				g.drawLine(edgeX + edgeW - 2, edgeY + edgeH, edgeX + edgeW, edgeY + edgeH - 2);
				g.drawLine(edgeX + edgeW, edgeY, edgeX + edgeW, edgeY + edgeH - 2); // right              
			}
			else {
				// empty tabpane
				final int y = insets.top + calculateTabHeight();
				g.drawLine(insets.left, y, tabPane.getWidth() - insets.right, y);
			}
		}
		if (tabPlacement == TOP) {
			if (tabPane.getTabCount() > 0) {
				final Rectangle lastTab = getTopRight();

				final int border = 2;

				final int edgeX = Math.max(insets.left + border, lastTab.x + lastTab.width - border);
				final int edgeW = tabPane.getWidth() - insets.right - edgeX - 2;
				final int edgeY = lastTab.y;
				final int edgeH = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight - 1);

				g.setColor(lightHighlight);
				g.drawLine(edgeX, edgeY, edgeX + edgeW - 3, edgeY); // top              
				g.drawLine(edgeX + edgeW - 2, edgeY, edgeX + edgeW, edgeY + 2);
				g.drawLine(edgeX + edgeW, edgeY + 3, edgeX + edgeW, edgeY + edgeH + 2); // right              
			}
			else {
				// empty tabpane
				final int y = insets.top + calculateTabHeight() - 2;
				g.drawLine(insets.left, y, tabPane.getWidth() - insets.right, y);

				g.setColor(tabPane.getBackground());
				final int arcY = insets.top - 2;
				g.fillRect(insets.left, arcY, 2, 2);
				g.fillRect(tabPane.getWidth() - insets.right - insets.left - 2, arcY, 2, 2);

				g.setColor(lightHighlight);
				g.drawLine(insets.left, arcY + 2, insets.left + 2, arcY);
				g.drawLine(tabPane.getWidth() - insets.right - insets.left - 2 - 1, arcY, tabPane.getWidth()
					- insets.right
					- insets.left
					- 1, arcY + 2);
			}
		}
	}

	@Override
	protected int getTabLabelShiftY(final int tabPlacement, final int tabIndex, final boolean isSelected) {
		return super.getTabLabelShiftY(tabPlacement, tabIndex, false);
	}

	@Override
	protected LayoutManager createLayoutManager() {
		return new EqualTabTabbedPaneLayout();
	}

	public class EqualTabTabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {

		@Override
		public void calculateLayoutInfo() {
			super.calculateLayoutInfo();
			if (tabPane.getTabCount() == 0) {
				return;
			}

			final Insets insets = tabPane.getInsets();

			if (tabPane.getTabPlacement() == JTabbedPane.TOP) {
				int shiftY = Integer.MAX_VALUE;
				int shiftX = Integer.MAX_VALUE;
				for (int i = 0; i < tabPane.getTabCount(); i++) {
					shiftY = Math.min(shiftY, rects[i].y - 1);
					shiftX = Math.min(shiftX, rects[i].x - insets.left);
				}
				for (int i = 0; i < tabPane.getTabCount(); i++) {
					rects[i].y -= shiftY;
					rects[i].x -= shiftX;
				}
			}
			else {
				int shiftY = Integer.MIN_VALUE;
				int shiftX = Integer.MAX_VALUE;
				for (int i = 0; i < tabPane.getTabCount(); i++) {
					shiftY = Math.max(shiftY, rects[i].y - 1);
					shiftX = Math.min(shiftX, rects[i].x - insets.left);
				}
				for (int i = 0; i < tabPane.getTabCount(); i++) {
					rects[i].y += 2; // Padding
					rects[i].x -= shiftX;
				}
			}
		}

		@Override
		protected void padSelectedTab(final int tabPlacement, final int selectedIndex) {
			// do nothing
		}

	}
}

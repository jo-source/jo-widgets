/*
 * Copyright (c) 2010, Michael Hengler, Benjamin Marstaller
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

package org.jowidgets.spi.impl.swt.widgets.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.spi.impl.swt.options.SplitlayoutMode;
import org.jowidgets.spi.impl.swt.options.SwtOptions;
import org.jowidgets.spi.impl.swt.util.OrientationConvert;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class JoSashForm extends Composite {

	protected static final int DRAG_MINIMUM = 40;

	private int sashWidth;
	private final int sashStyle;
	private Sash sash = null;
	private Color background;
	private Color foreground;
	private SplitResizePolicy resizePolicy;
	private int firstWeight;
	private int secondWeight;
	private final JoSashFormLayout layout;
	private Control first;
	private Control second;
	private Listener sashListener;

	public JoSashForm(final Composite parent, final ISplitCompositeSetupSpi setup) {
		super(parent, getStyle(setup));
		sashWidth = 3;
		firstWeight = 500;
		secondWeight = 500;
		layout = new JoSashFormLayout();
		setLayout(layout);

		resizePolicy = setup.getResizePolicy();
		sashStyle = getSashStyle(setup);

		sashListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (event.detail == SWT.DRAG) {
					return;
				}
				onDragSash(event);
			}
		};
		sash = new Sash(this, sashStyle);
		sash.setBackground(background);
		sash.setForeground(foreground);
		sash.setToolTipText(getToolTipText());
		sash.addListener(SWT.Selection, sashListener);
	}

	private static int getStyle(final ISplitCompositeSetupSpi setup) {
		final Orientation orientation = setup.getOrientation();
		final int style = OrientationConvert.convert(orientation);
		final int mask = SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	private int getSashStyle(final ISplitCompositeSetupSpi setup) {
		final Orientation orientation = setup.getOrientation();
		int style;
		if (Orientation.HORIZONTAL == orientation) {
			style = SWT.VERTICAL;
		}
		else {
			style = SWT.HORIZONTAL;
		}
		if (SwtOptions.getSplitLayoutMode().equals(SplitlayoutMode.ON_MOUSE_MOVE)) {
			style |= SWT.SMOOTH;
		}
		return style;
	}

	public int getOrientation() {
		checkWidget();
		return (sashStyle & SWT.VERTICAL) != 0 ? SWT.HORIZONTAL : SWT.VERTICAL;
	}

	@Override
	public int getStyle() {
		return sashStyle;
	}

	protected int getFirstWeight() {
		checkWidget();
		return firstWeight;
	}

	protected int getSecondWeight() {
		checkWidget();
		return secondWeight;
	}

	protected int getSashWidth() {
		return sashWidth;
	}

	private void onDragSash(final Event event) {
		event.doit = checkSizes((Sash) event.widget, event);
	}

	private boolean checkSizes(final Sash sash, final Event event) {

		if (sash == null) {
			return false;
		}

		if (second == null) {
			first.setBounds(getClientArea());
			return false;
		}

		final Rectangle b1 = first.getBounds();
		final Rectangle b2 = second.getBounds();

		final Rectangle sashBounds = sash.getBounds();

		final boolean doit = true;

		if (getOrientation() == SWT.HORIZONTAL) {
			final int totalWidth = getClientArea().width;
			final int shift = event.x - sashBounds.x;
			b1.width += shift;
			b2.x += shift;
			b2.width -= shift;
			if (b1.width < DRAG_MINIMUM) {
				b1.width = DRAG_MINIMUM;
				b2.x = b1.x + b1.width + sashBounds.width;
				b2.width = totalWidth - b2.x;
				event.x = b1.x + b1.width;
			}
			if (b2.width < DRAG_MINIMUM) {
				b1.width = totalWidth - DRAG_MINIMUM - sashBounds.width;

				b2.x = b1.x + b1.width + sashBounds.width;
				b2.width = DRAG_MINIMUM;
				event.x = b1.x + b1.width;
			}
		}
		else {
			final int totalHeight = getClientArea().height;
			final int shift = event.y - sashBounds.y;
			b1.height += shift;
			b2.y += shift;
			b2.height -= shift;
			if (b1.height < DRAG_MINIMUM) {
				b1.height = DRAG_MINIMUM;
				b2.y = b1.y + b1.height + sashBounds.height;
				b2.height = totalHeight - b2.y;
				event.y = b1.y + b1.height;
			}
			if (b2.height < DRAG_MINIMUM) {
				b1.height = totalHeight - DRAG_MINIMUM - sashBounds.height;
				b2.y = b1.y + b1.height + sashBounds.height;
				b2.height = DRAG_MINIMUM;
				event.y = b1.y + b1.height;
			}
		}

		first.setBounds(b1);
		sash.setBounds(event.x, event.y, sashBounds.width, sashBounds.height);
		second.setBounds(b2);

		if (layout.getSave() > 0) {
			if (getOrientation() == SWT.HORIZONTAL) {
				if (getResizePolicy() == SplitResizePolicy.RESIZE_FIRST) {
					layout.setSave(second.getBounds().width);
				}
				else if (getResizePolicy() == SplitResizePolicy.RESIZE_SECOND) {
					layout.setSave(first.getBounds().width);
				}
			}
			else {
				if (getResizePolicy() == SplitResizePolicy.RESIZE_FIRST) {
					layout.setSave(second.getBounds().height);
				}
				else if (getResizePolicy() == SplitResizePolicy.RESIZE_SECOND) {
					layout.setSave(first.getBounds().height);
				}
			}

			correctWeights();
		}
		return doit;
	}

	private void correctWeights() {
		if (second == null) {
			return;
		}

		final Rectangle area = this.getClientArea();

		if (this.getOrientation() == SWT.HORIZONTAL) {
			firstWeight = (int) (1000000 * (first.getBounds().width + (float) this.sashWidth / 2) / area.width);
			secondWeight = (int) (1000000 * (second.getBounds().width + (float) this.sashWidth / 2) / area.width);
		}
		else {
			firstWeight = (int) (1000000 * (first.getBounds().height + (float) this.sashWidth / 2) / area.height);
			secondWeight = (int) (1000000 * (second.getBounds().height + (float) this.sashWidth / 2) / area.height);
		}
	}

	@Override
	public void setBackground(final Color color) {
		super.setBackground(color);
		background = color;
		if (sash != null) {
			sash.setBackground(background);
		}
	}

	@Override
	public void setForeground(final Color color) {
		super.setForeground(color);
		foreground = color;
		if (sash != null) {
			sash.setForeground(foreground);
		}
	}

	public void setSashWidth(final int width) {
		checkWidget();
		if (sashWidth == width) {
			return;
		}
		sashWidth = width;
		layout(false);
	}

	@Override
	public void setToolTipText(final String string) {
		super.setToolTipText(string);
		if (sash != null) {
			sash.setToolTipText(string);
		}
	}

	public void setWeight(final double weight) {
		checkWidget();
		if (weight < 0 || weight > 1) {
			throw new IllegalArgumentException("Weight must be between 0.0 and 1.0 but is '" + weight + "'");
		}
		this.firstWeight = (int) (weight * 1000);
		this.secondWeight = (int) (1000 - weight * 1000);

		layout(false);
	}

	public Sash getSash() {
		return sash;
	}

	public SplitResizePolicy getResizePolicy() {
		return resizePolicy;
	}

	public Control getFirst() {
		if (first == null) {
			if (getChildren().length > 1) {
				first = getChildren()[1];
			}
		}
		return first;
	}

	public Control getSecond() {
		if (second == null) {
			if (getChildren().length > 1) {
				second = getChildren()[2];
			}
		}
		return second;
	}

}

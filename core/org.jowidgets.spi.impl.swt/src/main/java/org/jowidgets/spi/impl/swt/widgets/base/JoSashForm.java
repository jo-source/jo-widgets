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

package org.jowidgets.spi.impl.swt.widgets.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
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

	private static final int SPLIT_MINIMUM = 200;

	private Double weight;
	private int sashSize;
	private Sash sash;

	private final ISashOrientationUtil sashUtil;

	private Control first;
	private Control second;

	private Point firstMinSize;
	private Point secondMinSize;

	private JoSashFormLayout layout;

	private final SplitResizePolicy resizePolicy;

	public JoSashForm(final Composite parent, final ISplitCompositeSetupSpi setup) {
		super(parent, getSashFormStyle(setup));
		resizePolicy = setup.getResizePolicy();
		sashUtil = getSashUtil(setup);
		layout = new JoSashFormLayout(this, sashUtil);
		setLayout(layout);

		sash = new Sash(this, getSashStyle(setup));
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (event.detail == SWT.DRAG) {
					return;
				}
				onDragSash(event);
			}
		});

		firstMinSize = new Point(SPLIT_MINIMUM, SPLIT_MINIMUM);
		secondMinSize = new Point(SPLIT_MINIMUM, SPLIT_MINIMUM);
	}

	public void setWeight(final double weight) {
		if ((weight < 0) || (weight > 1)) {
			throw new IllegalArgumentException("Illegal weight (must be between 0 and 1, is " + weight + ")");
		}
		this.weight = weight;
		layout(true);
	}

	public double getWeight() {
		if (weight == null) {
			// if no weight is set return preferred weight
			return getPreferredWeight();
		}
		return weight;
	}

	public void setSashSize(final int sashSize) {
		if (this.sashSize == sashSize) {
			return;
		}

		this.sashSize = sashSize;
		layout(true);
	}

	public int getSashSize() {
		return sashSize;
	}

	/**
	 * User has dragged sash
	 * 
	 * @param event
	 */
	private void onDragSash(final Event event) {
		final Rectangle area = getClientArea();
		final Rectangle firstBounds = first.getBounds();
		final Rectangle secondBounds = second.getBounds();
		final Rectangle sashBounds = sash.getBounds();

		final int firstPos = sashUtil.getPosition(firstBounds);
		int firstSize = sashUtil.getSize(firstBounds);
		int secondPos = sashUtil.getPosition(secondBounds);
		int secondSize = sashUtil.getSize(secondBounds);

		final int totalSize = sashUtil.getSize(area);
		final int targetPos = sashUtil.getEventPos(event);
		final int sashPos = firstSize + firstPos; // sashBounds do not always contain the correct position
		final int shift = targetPos - sashPos;

		firstSize += shift;
		secondPos += shift;
		secondSize -= shift;
		if (firstSize < sashUtil.getSize(firstMinSize)) {
			firstSize = sashUtil.getSize(firstMinSize);
			secondPos = firstPos + firstSize + sashSize;
			secondSize = totalSize - secondPos;
		}
		if (secondSize < sashUtil.getSize(secondMinSize)) {
			firstSize = Math.max(sashUtil.getSize(firstMinSize), totalSize - sashUtil.getSize(secondMinSize) - sashSize);

			secondPos = firstPos + firstSize + sashSize;
			secondSize = sashUtil.getSize(secondMinSize);
		}

		final int newSashPos = firstPos + firstSize;

		sashUtil.updateBounds(firstBounds, area, firstPos, firstSize);
		sashUtil.updateBounds(sashBounds, area, newSashPos, sashSize);
		sashUtil.updateBounds(secondBounds, area, secondPos, secondSize);

		first.setBounds(firstBounds);
		sash.setBounds(sashBounds);
		second.setBounds(secondBounds);

		// remember weight
		weight = (double) firstSize / totalSize;

		event.doit = (targetPos == newSashPos);
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
			if (getChildren().length > 2) {
				second = getChildren()[2];
			}
		}
		return second;
	}

	public Sash getSash() {
		return sash;
	}

	public SplitResizePolicy getResizePolicy() {
		return resizePolicy;
	}

	Point getFirstMinSize() {
		return firstMinSize;
	}

	Point getSecondMinSize() {
		return secondMinSize;
	}

	private double getPreferredWeight() {
		if (first == null) {
			return 0;
		}
		if (second == null) {
			return 1;
		}

		final int firstSize = sashUtil.getSize(first.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		final int secondSize = sashUtil.getSize(first.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		final int totalSize = firstSize + secondSize;

		return (double) firstSize / totalSize;
	}

	private static int getSashFormStyle(final ISplitCompositeSetupSpi setup) {
		final Orientation orientation = setup.getOrientation();
		final int style = OrientationConvert.convert(orientation);
		final int mask = SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	private static int getSashStyle(final ISplitCompositeSetupSpi setup) {
		final Orientation orientation = setup.getOrientation();
		int style;
		if (Orientation.HORIZONTAL == orientation) {
			style = SWT.VERTICAL;
		}
		else if (Orientation.VERTICAL == orientation) {
			style = SWT.HORIZONTAL;
		}
		else {
			throw new IllegalStateException("Wrong Orientation is set");
		}

		if (SwtOptions.getSplitLayoutMode().equals(SplitlayoutMode.ON_MOUSE_MOVE)) {
			style |= SWT.SMOOTH;
		}
		return style;
	}

	private static ISashOrientationUtil getSashUtil(final ISplitCompositeSetupSpi setup) {
		if (setup.getOrientation() == Orientation.HORIZONTAL) {
			return SashOrientationUtil.HORIZONTAL;
		}
		else if (setup.getOrientation() == Orientation.VERTICAL) {
			return SashOrientationUtil.VERTICAL;
		}
		else {
			throw new IllegalStateException("Wrong Orientation is set");
		}
	}
}

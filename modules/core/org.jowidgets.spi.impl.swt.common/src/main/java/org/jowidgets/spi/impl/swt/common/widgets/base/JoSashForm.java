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

package org.jowidgets.spi.impl.swt.common.widgets.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.spi.impl.swt.common.options.SplitlayoutMode;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;
import org.jowidgets.spi.impl.swt.common.util.OrientationConvert;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class JoSashForm extends Composite {

    private static final int SPLIT_MINIMUM = 0;

    private double weight = -1;
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
        sash.setToolTipText(getToolTipText());
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
        setWeight(weight, true);
    }

    void setWeight(final double weight, final boolean doLayout) {
        if ((weight < 0) || (weight > 1)) {
            throw new IllegalArgumentException("Illegal weight (must be between 0 and 1, is " + weight + ")");
        }
        this.weight = weight;
        layout.resetRemeberedSize();

        if (doLayout) {
            layout(true);
        }
    }

    public double getWeight() {
        if (weight < -1) {
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
        final int totalSize = sashUtil.getSize(area);
        final int targetPos = sashUtil.getEventPos(event);
        final int firstSize = Math.max(targetPos, sashUtil.getSize(firstMinSize));
        final int secondSize = totalSize - sashSize - firstSize;

        event.doit = ((targetPos >= sashUtil.getSize(firstMinSize))
            && (targetPos + sashSize <= totalSize - sashUtil.getSize(secondMinSize)));

        if ((firstSize != sashUtil.getSize(first.getBounds())) && (event.doit) && (event.detail != SWT.DRAG)) {
            weight = (double) firstSize / (totalSize - sashSize);
            setChildrenBounds(area, firstSize, secondSize);
        }
    }

    void setChildrenBounds(final Rectangle area, final int firstSize, final int secondSize) {
        final Rectangle firstBounds = sashUtil.createBounds(area, sashUtil.getPosition(area), firstSize);
        final Rectangle sashBounds = sashUtil.createBounds(area, sashUtil.getPosition(firstBounds) + firstSize, sashSize);
        final Rectangle secondBounds = sashUtil.createBounds(area, sashUtil.getPosition(sashBounds) + sashSize, secondSize);

        first.setBounds(firstBounds);
        if (!sash.getBounds().equals(sashBounds)) {
            sash.setBounds(sashBounds);
        }
        if (!second.getBounds().equals(secondBounds)) {
            second.setBounds(secondBounds);
        }
    }

    private Control getChild(int childIndex) {
        final Control[] children = getChildren();
        int index = 0;
        while (index < children.length) {
            // ignore the sash
            if (children[index] == this.sash) {
                childIndex++;
            }
            else if (index == childIndex) {
                return children[index];
            }
            index++;
        }

        return null;
    }

    public Control getFirst() {
        if (first == null) {
            first = getChild(0);
        }
        return first;
    }

    public Control getSecond() {
        if (second == null) {
            second = getChild(1);
        }
        return second;
    }

    public Sash getSash() {
        return sash;
    }

    public SplitResizePolicy getResizePolicy() {
        return resizePolicy;
    }

    public void setMinSizes(final Point firstMinSize, final Point secondMinSize) {
        this.firstMinSize = firstMinSize;
        this.secondMinSize = secondMinSize;
        layout(true);
    }

    public Point getFirstMinSize() {
        return firstMinSize;
    }

    public Point getSecondMinSize() {
        return secondMinSize;
    }

    @Override
    public void setBackground(final Color color) {
        super.setBackground(color);
        sash.setBackground(color);
    }

    @Override
    public void setForeground(final Color color) {
        super.setForeground(color);
        sash.setForeground(color);
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
        final int style;
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
            return style | SWT.SMOOTH;
        }
        else {
            return style;
        }

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

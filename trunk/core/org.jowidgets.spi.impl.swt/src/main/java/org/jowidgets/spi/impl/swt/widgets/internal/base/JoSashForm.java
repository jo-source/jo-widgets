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

package org.jowidgets.spi.impl.swt.widgets.internal.base;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.spi.impl.swt.util.OrientationConvert;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class JoSashForm extends Composite {

	static final int DRAG_MINIMUM = 40;

	private final Listener sashListener;
	private boolean initialized = false;
	private int sashWidth = 3;
	private int sashStyle;
	private Sash sash = null;
	private Color background = null;
	private Color foreground = null;
	private SplitResizePolicy stretchFactor;
	private int[] weights = new int[2];
	private JoSashFormLayout layout = null;
	private List<Control> controls = new ArrayList<Control>();;
	private Control maxControl = null;

	public JoSashForm(final Composite parent, final ISplitCompositeSetupSpi setup) {
		super(parent, checkStyle(setup));
		layout = new JoSashFormLayout();
		super.setLayout(layout);
		sashStyle = getSashStyle(setup);

		sashListener = new Listener() {
			@Override
			public void handleEvent(final Event e) {
				onDragSash(e);
			}
		};
	}

	static int checkStyle(final ISplitCompositeSetupSpi setup) {
		final Orientation orientation = setup.getOrientation();
		final int style = OrientationConvert.convert(orientation);
		final int mask = SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	private int getSashStyle(final ISplitCompositeSetupSpi setup) {
		//		setResizePolicyInternal(setup);
		stretchFactor = setup.getResizePolicy();
		final Orientation orientation = setup.getOrientation();
		final int style = OrientationConvert.convert(orientation);
		int returnValue = ((style & SWT.VERTICAL) != 0) ? SWT.HORIZONTAL : SWT.VERTICAL;
		if ((style & SWT.BORDER) != 0) {
			returnValue |= SWT.BORDER;
		}
		returnValue |= SWT.SMOOTH;
		return returnValue;
	}

	//	private void setResizePolicyInternal(final ISplitCompositeSetupSpi setup) {
	//		final SplitResizePolicy resizePolicy = setup.getResizePolicy();
	//		if (resizePolicy == SplitResizePolicy.RESIZE_FIRST) {
	//			stretchFactor = FIRST;
	//		}
	//		else if (resizePolicy == SplitResizePolicy.RESIZE_SECOND) {
	//			stretchFactor = SECOND;
	//		}
	//		else if (resizePolicy == SplitResizePolicy.RESIZE_BOTH) {
	//			stretchFactor = BOTH;
	//		}
	//	}

	public Sash createSash() {
		disposeSashes();
		final Sash returnSash = new Sash(this, sashStyle);
		returnSash.setBackground(background);
		returnSash.setForeground(foreground);
		returnSash.setToolTipText(getToolTipText());
		returnSash.addListener(SWT.Selection, sashListener);
		return returnSash;
	}

	private void disposeSashes() {
		sash = null;
		for (final Object child : this.getChildren()) {
			if (child instanceof Sash) {
				((Sash) child).dispose();
			}
		}
	}

	public int getOrientation() {
		checkWidget();
		return (sashStyle & SWT.VERTICAL) != 0 ? SWT.HORIZONTAL : SWT.VERTICAL;
	}

	public int getSashWidth() {
		return sashWidth;
	}

	public int getCheckedSashWidth() {
		checkWidget();
		return sashWidth;
	}

	@Override
	public int getStyle() {
		int style = super.getStyle();
		style |= getOrientation() == SWT.VERTICAL ? SWT.VERTICAL : SWT.HORIZONTAL;
		if ((sashStyle & SWT.SMOOTH) != 0) {
			style |= SWT.SMOOTH;
		}
		return style;
	}

	public Control getMaximizedControl() {
		checkWidget();
		return this.getMaxControl();
	}

	public int[] getWeights() {
		checkWidget();

		return weights;
	}

	public List<Control> getControls(final boolean onlyVisible) {
		final Control[] children = getChildren();
		final List<Control> result = new ArrayList<Control>();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Sash) {
				continue;
			}
			if (onlyVisible && !children[i].getVisible()) {
				continue;
			}

			result.add(children[i]);
		}
		return result;
	}

	public void onDragSash(final Event event) {
		event.doit = checkSizes((Sash) event.widget, event);
	}

	public boolean checkSizes(final Sash sash, final Event event) {

		if (sash == null) {
			return false;
		}

		if (controls.size() < 2) {
			controls.get(0).setBounds(getClientArea());
			return false;
		}

		final Control c1 = controls.get(0);
		final Control c2 = controls.get(1);

		final Rectangle b1 = c1.getBounds();
		final Rectangle b2 = c2.getBounds();

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

		c1.setBounds(b1);
		sash.setBounds(event.x, event.y, sashBounds.width, sashBounds.height);
		c2.setBounds(b2);

		if (layout.getSave() > 0) {
			if (getOrientation() == SWT.HORIZONTAL) {
				if (getStretchFactor() == SplitResizePolicy.RESIZE_FIRST) {
					layout.setSave(c2.getBounds().width);
				}
				else if (getStretchFactor() == SplitResizePolicy.RESIZE_SECOND) {
					layout.setSave(c1.getBounds().width);
				}
			}
			else {
				if (getStretchFactor() == SplitResizePolicy.RESIZE_FIRST) {
					layout.setSave(c2.getBounds().height);
				}
				else if (getStretchFactor() == SplitResizePolicy.RESIZE_SECOND) {
					layout.setSave(c1.getBounds().height);
				}
			}

			correctWeights();
		}
		return doit;
	}

	private void correctWeights() {
		if (controls.size() < 2) {
			return;
		}

		final Rectangle area = this.getClientArea();
		final Control first = controls.get(0);
		final Control second = controls.get(1);

		if (this.getOrientation() == SWT.HORIZONTAL) {
			this.weights[0] = (int) (1000000 * (first.getBounds().width + (float) this.sashWidth / 2) / area.width);
			this.weights[1] = (int) (1000000 * (second.getBounds().width + (float) this.sashWidth / 2) / area.width);
		}
		else {
			this.weights[0] = (int) (1000000 * (first.getBounds().height + (float) this.sashWidth / 2) / area.height);
			this.weights[1] = (int) (1000000 * (second.getBounds().height + (float) this.sashWidth / 2) / area.height);
		}
	}

	public List<Control> getControls() {
		return controls;
	}

	public void setControls(final List<Control> controls) {
		this.controls = controls;
	}

	public void setOrientation(final int orientation) {
		checkWidget();
		if (getOrientation() == orientation) {
			return;
		}
		if (orientation != SWT.HORIZONTAL && orientation != SWT.VERTICAL) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		sashStyle &= ~(SWT.HORIZONTAL | SWT.VERTICAL);
		sashStyle |= orientation == SWT.VERTICAL ? SWT.HORIZONTAL : SWT.VERTICAL;

		sash = null;
		sash = createSash();

		layout(false);
	}

	@Override
	public void setBackground(final Color color) {
		super.setBackground(color);
		background = color;
		if (sash == null) {
			sash.setBackground(background);
		}
	}

	@Override
	public void setForeground(final Color color) {
		super.setForeground(color);
		foreground = color;
		if (sash == null) {
			sash.setForeground(foreground);
		}
	}

	@Override
	public void setLayout(final Layout layout) {
		checkWidget();
		return;
	}

	public void setMaximizedControl(final Control control) {
		checkWidget();
		if (control == null) {
			if (getMaxControl() != null) {
				this.setMaxControl(null);
				layout(false);
				if (sash == null) {
					sash.setVisible(true);
				}
			}
			return;
		}

		if (sash == null) {
			sash.setVisible(false);
		}
		setMaxControl(control);
		layout(false);
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
		if (sash == null) {
			sash.setToolTipText(string);
		}
	}

	public void setWeights(final int[] weights) {
		checkWidget();
		if (weights == null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		int total = 0;
		for (int i = 0; i < weights.length; i++) {
			if (weights[i] < 0) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			total += weights[i];
		}
		if (total == 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.weights = weights;

		layout(false);
	}

	public Sash getSash() {
		return sash;
	}

	public void setSash(final Sash sash) {
		this.sash = sash;
	}

	public void setStretchFactor(final SplitResizePolicy factor) {
		this.stretchFactor = factor;
	}

	public void setMaxControl(final Control maxControl) {
		this.maxControl = maxControl;
	}

	public Control getMaxControl() {
		return maxControl;
	}

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param initialized the initialized to set
	 */
	public void setInitialized(final boolean initialized) {
		this.initialized = initialized;
	}

	public SplitResizePolicy getStretchFactor() {
		return stretchFactor;
	}

}

/*
 * Copyright (c) 2010, grossmann
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.spi.impl.swt.util.BorderToComposite;
import org.jowidgets.spi.impl.swt.widgets.internal.layout.ILayoutListener;
import org.jowidgets.spi.impl.swt.widgets.internal.layout.LayoutWrapper;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class JoSashForm extends Composite {

	private static final int LIMIT = 20;

	private Composite first;
	private Composite second;

	private double currentWeight;
	private int lastFirst = -1;
	private int lastSecond = -1;

	private Sash sash;
	private FormData sashData;
	private final SplitResizePolicy resizePolicy;
	private final int sashWidth;
	private boolean handleSashSelectionEvent;

	public JoSashForm(final Composite parent, final ISplitCompositeSetupSpi setup) {
		super(parent, SWT.NONE);

		this.handleSashSelectionEvent = false;
		this.currentWeight = setup.getWeight();
		this.resizePolicy = setup.getResizePolicy();
		this.sashWidth = setup.getDividerSize() + 1;

		if (Orientation.HORIZONTAL == setup.getOrientation()) {
			initHorizontal(setup);
		}
		else {
			initVertical(setup);
		}
	}

	private void initHorizontal(final ISplitCompositeSetupSpi setup) {
		this.first = BorderToComposite.convert(this, setup.getFirstBorder());
		sash = new Sash(this, SWT.NONE | SWT.VERTICAL);
		this.second = BorderToComposite.convert(this, setup.getSecondBorder());

		//sets the layout for this composite
		final LayoutWrapper layout = new LayoutWrapper(new FormLayout());
		this.setLayout(layout);
		layout.addLayoutListener(new ILayoutListener() {

			@Override
			public void beforeComputeSize() {}

			@Override
			public void beforeLayout() {
				if (!handleSashSelectionEvent) {

					final Composite parent = JoSashForm.this.getParent();
					final int parentWidth = parent.getClientArea().width - parent.getBorderWidth();
					final Rectangle sashRect = sash.getBounds();
					final int maxX = parentWidth - sashWidth - LIMIT;

					int x;
					if (SplitResizePolicy.RESIZE_BOTH == resizePolicy) {
						x = (int) (currentWeight * parentWidth);
					}
					else if (SplitResizePolicy.RESIZE_FIRST == resizePolicy) {
						if (lastSecond == -1) {
							x = (int) (currentWeight * parentWidth);
							lastSecond = parentWidth - x;
						}
						else {
							x = parentWidth - lastSecond;
						}
					}
					else {
						if (lastFirst == -1) {
							x = (int) (currentWeight * parentWidth);
							lastFirst = x;
						}
						else {
							x = lastFirst;
						}
					}

					x = Math.max(Math.min(x, maxX), LIMIT);
					if (x != sashRect.x) {
						sashData.left = new FormAttachment(0, x);
						sashData.right = new FormAttachment(0, x + sashWidth);
						lastFirst = x;
						lastSecond = parentWidth - x;
					}
				}
			}

			@Override
			public void afterLayout() {}
		});

		//sets the layout of the first composite
		final FormData firstData = new FormData();
		firstData.left = new FormAttachment(0, 0);
		firstData.right = new FormAttachment(sash, 0);
		firstData.top = new FormAttachment(0, 0);
		firstData.bottom = new FormAttachment(100, 0);
		first.setLayoutData(firstData);

		//sets the layout of the sash
		sashData = new FormData();
		sashData.top = new FormAttachment(0, 0);
		sashData.bottom = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				final Composite parent = JoSashForm.this.getParent();
				final int parentWidth = parent.getClientArea().width - parent.getBorderWidth();
				final Rectangle sashRect = sash.getBounds();
				final int maxX = parentWidth - sashWidth - LIMIT;

				e.x = Math.max(Math.min(e.x, maxX), LIMIT);
				if (e.x != sashRect.x) {
					sashData.left = new FormAttachment(0, e.x);
					sashData.right = new FormAttachment(0, e.x + sashWidth);
					currentWeight = ((double) e.x) / (parentWidth);
					lastFirst = e.x;
					lastSecond = parentWidth - e.x;

					handleSashSelectionEvent = true;
					JoSashForm.this.layout();
					handleSashSelectionEvent = false;
				}
			}
		});

		//sets the layout of the second composite
		final FormData secondData = new FormData();
		secondData.left = new FormAttachment(sash, 0);
		secondData.right = new FormAttachment(100, 0);
		secondData.top = new FormAttachment(0, 0);
		secondData.bottom = new FormAttachment(100, 0);
		second.setLayoutData(secondData);
	}

	private void initVertical(final ISplitCompositeSetupSpi setup) {
		this.first = BorderToComposite.convert(this, setup.getFirstBorder());
		sash = new Sash(this, SWT.NONE | SWT.HORIZONTAL);
		this.second = BorderToComposite.convert(this, setup.getSecondBorder());

		//sets the layout for this composite
		final LayoutWrapper layout = new LayoutWrapper(new FormLayout());
		this.setLayout(layout);
		layout.addLayoutListener(new ILayoutListener() {

			@Override
			public void beforeComputeSize() {}

			@Override
			public void beforeLayout() {
				if (!handleSashSelectionEvent) {

					final Composite parent = JoSashForm.this.getParent();
					final int parentHeight = parent.getClientArea().height - parent.getBorderWidth();
					final Rectangle sashRect = sash.getBounds();
					final int maxY = parentHeight - sashWidth - LIMIT;

					int y;
					if (SplitResizePolicy.RESIZE_BOTH == resizePolicy) {
						y = (int) (currentWeight * parentHeight);
					}
					else if (SplitResizePolicy.RESIZE_FIRST == resizePolicy) {
						if (lastSecond == -1) {
							y = (int) (currentWeight * parentHeight);
							lastSecond = parentHeight - y;
						}
						else {
							y = parentHeight - lastSecond;
						}
					}
					else {
						if (lastFirst == -1) {
							y = (int) (currentWeight * parentHeight);
							lastFirst = y;
						}
						else {
							y = lastFirst;
						}
					}

					y = Math.max(Math.min(y, maxY), LIMIT);
					if (y != sashRect.y) {
						sashData.top = new FormAttachment(0, y);
						sashData.bottom = new FormAttachment(0, y + sashWidth);
						lastFirst = y;
						lastSecond = parentHeight - y;
					}
				}
			}

			@Override
			public void afterLayout() {}
		});

		//sets the layout of the first composite
		final FormData firstData = new FormData();
		firstData.left = new FormAttachment(0, 0);
		firstData.right = new FormAttachment(100, 0);
		firstData.top = new FormAttachment(0, 0);
		firstData.bottom = new FormAttachment(sash, 0);
		first.setLayoutData(firstData);

		//sets the layout of the sash
		sashData = new FormData();
		sashData.left = new FormAttachment(0, 0);
		sashData.right = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				final Composite parent = JoSashForm.this.getParent();
				final int parentHeight = parent.getClientArea().height - parent.getBorderWidth();
				final Rectangle sashRect = sash.getBounds();
				final int maxY = parentHeight - sashWidth - LIMIT;

				e.y = Math.max(Math.min(e.y, maxY), LIMIT);
				if (e.y != sashRect.y) {
					sashData.top = new FormAttachment(0, e.y);
					sashData.bottom = new FormAttachment(0, e.y + sashWidth);
					currentWeight = ((double) e.y) / (parentHeight);
					lastFirst = e.y;
					lastSecond = parentHeight - e.y;

					handleSashSelectionEvent = true;
					JoSashForm.this.layout();
					handleSashSelectionEvent = false;
				}
			}
		});

		//sets the layout of the second composite
		final FormData secondData = new FormData();
		secondData.left = new FormAttachment(0, 0);
		secondData.right = new FormAttachment(100, 0);
		secondData.top = new FormAttachment(sash, 0);
		secondData.bottom = new FormAttachment(100, 0);
		second.setLayoutData(secondData);
	}

	public Composite getFirst() {
		return first;
	}

	public Composite getSecond() {
		return second;
	}

}

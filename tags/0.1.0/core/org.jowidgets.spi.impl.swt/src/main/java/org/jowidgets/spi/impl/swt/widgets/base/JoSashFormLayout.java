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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Sash;
import org.jowidgets.common.types.SplitResizePolicy;

class JoSashFormLayout extends Layout {

	private boolean initialized;
	private Point oldSize = null;
	private int save = 0;

	JoSashFormLayout() {
		initialized = false;
	}

	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		return new Point(composite.getParent().getClientArea().width, composite.getParent().getClientArea().height);
	}

	@Override
	protected boolean flushCache(final Control control) {
		return true;
	}

	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final JoSashForm sashForm = (JoSashForm) composite;
		final Rectangle area = sashForm.getClientArea();
		if (area.width <= 1 || area.height <= 1) {
			return;
		}

		final Control first = sashForm.getFirst();
		final Control second = sashForm.getSecond();

		if (first == null && second == null) {
			return;
		}

		else if (first != null && second == null) {
			first.setBounds(area);
			return;
		}

		if (!initialized) {
			initialize(sashForm);
			initialized = true;
			return;
		}

		if (sashForm.getResizePolicy() == SplitResizePolicy.RESIZE_FIRST) {
			stretchFirst(sashForm);
		}
		else if (sashForm.getResizePolicy() == SplitResizePolicy.RESIZE_SECOND) {
			stretchSecond(sashForm);
		}
		else {
			stretchBoth(sashForm);
		}
	}

	private void stretchBoth(final JoSashForm sashForm) {
		final Control first = sashForm.getFirst();
		final Control second = sashForm.getSecond();
		final Rectangle area = sashForm.getClientArea();
		final int firstWeight = sashForm.getFirstWeight();
		final int total = firstWeight + sashForm.getSecondWeight();

		if (sashForm.getOrientation() == SWT.HORIZONTAL) {

			int x = 0;
			int newSize = first.getBounds().width;

			final int toAdd = (int) ((area.width - oldSize.x) * ((float) firstWeight / (float) total) + ((float) sashForm.getSashWidth() / 2));
			newSize += toAdd;

			if (newSize < JoSashForm.DRAG_MINIMUM) {
				newSize = (int) (((float) JoSashForm.DRAG_MINIMUM - (float) sashForm.getSashWidth()) / 2);
			}

			first.setBounds(area.x, area.y, newSize, area.height);

			x += first.getBounds().width;

			final Sash sash = sashForm.getSash();
			if (sash == null) {
				return;
			}
			sash.setBounds(x, area.y, sashForm.getSashWidth(), area.height);

			x += sashForm.getSashWidth();

			newSize = area.width - first.getBounds().width - sashForm.getSashWidth();

			if (newSize < JoSashForm.DRAG_MINIMUM) {
				newSize = (int) (((float) JoSashForm.DRAG_MINIMUM - (float) sashForm.getSashWidth()) / 2);
			}

			second.setBounds(x, area.y, newSize, area.height);

		}
		else {

			int y = 0;
			int newSize = first.getBounds().height;

			final int toAdd = (int) ((area.height - oldSize.y) * ((float) firstWeight / (float) total) + ((float) sashForm.getSashWidth() / 2));
			newSize += toAdd;

			if (newSize < JoSashForm.DRAG_MINIMUM) {
				newSize = (int) (((float) JoSashForm.DRAG_MINIMUM - (float) sashForm.getSashWidth()) / 2);
			}

			first.setBounds(area.x, area.y, area.width, newSize);
			y += first.getBounds().height;

			final Sash sash = sashForm.getSash();
			if (sash == null) {
				return;
			}

			sash.setBounds(area.x, y, area.width, sashForm.getSashWidth());

			y += sashForm.getSashWidth();

			newSize = area.height - first.getBounds().height - sashForm.getSashWidth();

			if (newSize < JoSashForm.DRAG_MINIMUM) {
				newSize = (int) (((float) JoSashForm.DRAG_MINIMUM - (float) sashForm.getSashWidth()) / 2);
			}

			second.setBounds(area.x, y, area.width, newSize);
		}
		oldSize = new Point(area.width, area.height);
	}

	private void stretchFirst(final JoSashForm sashForm) {
		final Control first = sashForm.getFirst();
		final Control second = sashForm.getSecond();
		final Rectangle area = sashForm.getClientArea();
		int newWidth = 0;
		int secondSize;

		if (sashForm.getSash() == null) {
			return;
		}
		final Sash sash = sashForm.getSash();

		if (sashForm.getOrientation() == SWT.HORIZONTAL) {
			int x = area.x;

			if (save <= 0) {
				newWidth = area.width - sashForm.getSashWidth() - second.getBounds().width;
			}
			else {
				newWidth = area.width - sashForm.getSashWidth() - save;
			}

			if (newWidth < JoSashForm.DRAG_MINIMUM) {
				newWidth = ((JoSashForm.DRAG_MINIMUM));

				if (save <= 0) {
					save = second.getBounds().width;
				}
				secondSize = area.width - sashForm.getSashWidth() - newWidth;
			}
			else {
				if (save > 0) {
					secondSize = save;
					save = 0;
				}
				else {
					secondSize = second.getBounds().width;
				}

			}

			first.setBounds(x, area.y, newWidth, area.height);

			x += first.getBounds().width + sashForm.getSashWidth();

			second.setBounds(x, area.y, secondSize, area.height);

			sash.setBounds(first.getBounds().width, area.y, sashForm.getSashWidth(), area.height);

		}
		else {
			int y = area.y;

			if (save <= 0) {
				newWidth = area.height - sashForm.getSashWidth() - second.getBounds().height;
			}
			else {
				newWidth = area.height - sashForm.getSashWidth() - save;
			}

			if (newWidth < JoSashForm.DRAG_MINIMUM) {
				newWidth = (int) (((float) JoSashForm.DRAG_MINIMUM - (float) sashForm.getSashWidth()) / 2);
				if (save <= 0) {
					save = second.getBounds().height;
				}
				secondSize = area.height - sashForm.getSashWidth() - newWidth;
			}
			else {
				if (save > 0) {
					secondSize = save;
					save = 0;
				}
				else {
					secondSize = second.getBounds().height;
				}

			}

			first.setBounds(area.x, area.y, area.width, newWidth);
			y += first.getBounds().height + sashForm.getSashWidth();
			second.setBounds(area.x, y, area.width, second.getBounds().height);

			sash.setBounds(area.x, first.getBounds().height, area.width, sashForm.getSashWidth());
		}

	}

	private void stretchSecond(final JoSashForm sashForm) {
		final Control first = sashForm.getFirst();
		final Control second = sashForm.getSecond();
		final Rectangle area = sashForm.getClientArea();
		int newWidth = 0;
		int firstSize = 0;

		final Sash sash = sashForm.getSash();
		if (sash == null) {
			return;
		}

		if (sashForm.getOrientation() == SWT.HORIZONTAL) {
			int x = area.x;
			if (save <= 0) {
				newWidth = area.width - sashForm.getSashWidth() - first.getBounds().width;
			}
			else {
				newWidth = area.width - sashForm.getSashWidth() - save;
			}

			if (newWidth < JoSashForm.DRAG_MINIMUM) {
				newWidth = JoSashForm.DRAG_MINIMUM;
				if (save <= 0) {
					save = first.getBounds().width;
				}
				firstSize = area.width - sashForm.getSashWidth() - JoSashForm.DRAG_MINIMUM;
			}
			else {
				if (save > 0) {
					firstSize = save;
					save = 0;
				}
				else {
					firstSize = first.getBounds().width;
				}

			}
			first.setBounds(area.x, area.y, firstSize, area.height);

			x = area.x + first.getBounds().width + sashForm.getSashWidth();

			second.setBounds(x, area.y, newWidth, area.height);

			sash.setBounds(first.getBounds().width, area.y, sashForm.getSashWidth(), area.height);
		}
		else {
			int y = area.y;
			if (save <= 0) {
				newWidth = area.height - sashForm.getSashWidth() - first.getBounds().height;
			}
			else {
				newWidth = area.height - sashForm.getSashWidth() - save;
			}

			if (newWidth < JoSashForm.DRAG_MINIMUM) {
				newWidth = JoSashForm.DRAG_MINIMUM;
				if (save <= 0) {
					save = first.getBounds().height;
				}
				firstSize = area.height - sashForm.getSashWidth() - JoSashForm.DRAG_MINIMUM;
			}
			else {
				if (save > 0) {
					firstSize = save;
					save = 0;
				}
				else {
					firstSize = first.getBounds().height;
				}
			}
			first.setBounds(area.x, area.y, area.width, firstSize);

			y = area.x + first.getBounds().height + sashForm.getSashWidth();

			second.setBounds(area.x, y, area.width, newWidth);

			sash.setBounds(area.x, first.getBounds().height, area.width, sashForm.getSashWidth());
		}

	}

	private void initialize(final JoSashForm sashForm) {
		final Rectangle area = sashForm.getClientArea();
		final int firstWeight = sashForm.getFirstWeight();
		final int secondWeight = sashForm.getSecondWeight();
		final int total = firstWeight + secondWeight;
		final Control first = sashForm.getFirst();
		final Control second = sashForm.getSecond();
		if (first != null && second == null) {
			first.setBounds(area);
			return;
		}

		int newValue = 0;
		if (sashForm.getOrientation() == SWT.HORIZONTAL) {
			int x = 0;
			newValue = (int) ((area.width * ((float) firstWeight / (float) total)) - ((float) sashForm.getSashWidth() / 2));
			first.setBounds(area.x, area.y, newValue, area.height);
			x += first.getBounds().width;

			final Sash sash = sashForm.getSash();
			if (sash == null) {
				return;
			}

			sash.setBounds(x, area.y, sashForm.getSashWidth(), area.height);

			x += sashForm.getSashWidth();

			newValue = (int) ((area.width * (((float) secondWeight / (float) total))) - ((float) sashForm.getSashWidth() / 2));
			second.setBounds(x, area.y, newValue, area.height);
		}
		else if (sashForm.getOrientation() == SWT.VERTICAL) {
			int y = 0;
			newValue = (int) ((area.height * ((float) firstWeight / (float) total)) - ((float) sashForm.getSashWidth() / 2));
			first.setBounds(area.x, area.y, area.width, newValue);
			y += first.getBounds().height;

			final Sash sash = sashForm.getSash();
			if (sash == null) {
				return;
			}

			sash.setBounds(area.x, y, area.width, sashForm.getSashWidth());

			y += sashForm.getSashWidth();
			newValue = (int) ((area.height * (((float) secondWeight / (float) total))) - ((float) sashForm.getSashWidth() / 2));
			second.setBounds(area.x, y, area.width, newValue);
		}
		else {
			throw new IllegalStateException("Wrong Orientation is set");
		}
		oldSize = new Point(area.width, area.height);
	}

	public int getSave() {
		return save;
	}

	public void setSave(final int save) {
		this.save = save;
	}
}

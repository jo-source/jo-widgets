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

package org.jowidgets.impl.model.table;

import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.util.event.ChangeObservable;
import org.jowidgets.util.event.IChangeListener;

class DefaultTableColumn implements IDefaultTableColumn {

	private final ChangeObservable changeObservable;

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private int width;
	private AlignmentHorizontal alignment;

	DefaultTableColumn(
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final int width,
		final AlignmentHorizontal alignment) {

		this.changeObservable = new ChangeObservable();

		this.text = text;
		this.toolTipText = toolTipText;
		this.icon = icon;
		this.width = width;
		this.alignment = alignment;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		changeObservable.fireChangedEvent();
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		changeObservable.fireChangedEvent();
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		changeObservable.fireChangedEvent();
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(final int width) {
		this.width = width;
		changeObservable.fireChangedEvent();
	}

	@Override
	public AlignmentHorizontal getAlignment() {
		return alignment;
	}

	@Override
	public void setAlignment(final AlignmentHorizontal alignment) {
		this.alignment = alignment;
		changeObservable.fireChangedEvent();
	}

	@Override
	public void addChangeListener(final IChangeListener listener) {
		changeObservable.addChangeListener(listener);
	}

	@Override
	public void removeChangeListener(final IChangeListener listener) {
		changeObservable.removeChangeListener(listener);
	}

}

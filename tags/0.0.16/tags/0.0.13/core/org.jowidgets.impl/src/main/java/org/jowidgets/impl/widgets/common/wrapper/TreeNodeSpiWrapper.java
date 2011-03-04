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

package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.ITreeNodeCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.spi.widgets.ITreeNodeSpi;

public class TreeNodeSpiWrapper extends ItemSpiWrapper implements ITreeNodeCommon {

	public TreeNodeSpiWrapper(final ITreeNodeSpi component) {
		super(component);
	}

	@Override
	public ITreeNodeSpi getWidget() {
		return (ITreeNodeSpi) super.getWidget();
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		getWidget().addTreeNodeListener(listener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		getWidget().removeTreeNodeListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		getWidget().addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		getWidget().removePopupDetectionListener(listener);
	}

	@Override
	public void setMarkup(final Markup markup) {
		getWidget().setMarkup(markup);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getWidget().setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getWidget().setBackgroundColor(colorValue);
	}

	@Override
	public void setExpanded(final boolean expanded) {
		if (isExpanded() != expanded) {
			getWidget().setExpanded(expanded);
		}
	}

	@Override
	public boolean isExpanded() {
		return getWidget().isExpanded();
	}

	@Override
	public void setSelected(final boolean selected) {
		if (isSelected() != selected) {
			getWidget().setSelected(selected);
		}
	}

	@Override
	public boolean isSelected() {
		return getWidget().isSelected();
	}

}

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

package org.jowidgets.impl.widgets.basic;

import java.io.File;
import java.util.List;

import org.jowidgets.api.widgets.IFileChooser;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.common.types.DialogResult;
import org.jowidgets.impl.base.delegate.DisplayDelegate;
import org.jowidgets.impl.widgets.common.wrapper.WidgetSpiWrapper;
import org.jowidgets.spi.widgets.IFileChooserSpi;

public class FileChooserImpl extends WidgetSpiWrapper implements IFileChooser {

	private final DisplayDelegate displayDelegate;

	public FileChooserImpl(final IFileChooserSpi widget) {
		super(widget);
		this.displayDelegate = new DisplayDelegate();
	}

	@Override
	public IFileChooserSpi getWidget() {
		return (IFileChooserSpi) super.getWidget();
	}

	@Override
	public void setParent(final IWindow parent) {
		displayDelegate.setParent(parent);
	}

	@Override
	public IWindow getParent() {
		return displayDelegate.getParent();
	}

	@Override
	public void setSelectedFile(final File file) {
		getWidget().setSelectedFile(file);
	}

	@Override
	public DialogResult open() {
		return getWidget().open();
	}

	@Override
	public List<File> getSelectedFiles() {
		return getWidget().getSelectedFiles();
	}

}

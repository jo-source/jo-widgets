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

package org.jowidgets.tools.widgets.wrapper;

import java.util.List;

import org.jowidgets.api.controller.ITabFolderListener;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.descriptor.ITabItemDescriptor;

public class TabFolderWrapper extends ControlWrapper implements ITabFolder {

	public TabFolderWrapper(final ITabFolder widget) {
		super(widget);
	}

	@Override
	protected ITabFolder getWidget() {
		return (ITabFolder) super.getWidget();
	}

	@Override
	public void addTabFolderListener(final ITabFolderListener listener) {
		getWidget().addTabFolderListener(listener);
	}

	@Override
	public void removeTabFolderListener(final ITabFolderListener listener) {
		getWidget().removeTabFolderListener(listener);
	}

	@Override
	public void removeItem(final int index) {
		getWidget().removeItem(index);
	}

	@Override
	public void setSelectedItem(final int index) {
		getWidget().setSelectedItem(index);
	}

	@Override
	public int getSelectedIndex() {
		return getWidget().getSelectedIndex();
	}

	@Override
	public ITabItem addItem(final ITabItemDescriptor descriptor) {
		return getWidget().addItem(descriptor);
	}

	@Override
	public ITabItem addItem(final int index, final ITabItemDescriptor descriptor) {
		return getWidget().addItem(index, descriptor);
	}

	@Override
	public void removeItem(final ITabItem item) {
		getWidget().removeItem(item);
	}

	@Override
	public void removeAllItems() {
		getWidget().removeAllItems();
	}

	@Override
	public ITabItem getItem(final int index) {
		return getWidget().getItem(index);
	}

	@Override
	public int getIndex(final ITabItem item) {
		return getWidget().getIndex(item);
	}

	@Override
	public List<ITabItem> getItems() {
		return getWidget().getItems();
	}

	@Override
	public void setSelectedItem(final ITabItem item) {
		getWidget().setSelectedItem(item);
	}

	@Override
	public ITabItem getSelectedItem() {
		return getWidget().getSelectedItem();
	}

	@Override
	public void detachItem(final ITabItem item) {
		getWidget().detachItem(item);
	}

	@Override
	public void attachItem(final ITabItem item) {
		getWidget().attachItem(item);
	}

	@Override
	public void attachItem(final int index, final ITabItem item) {
		getWidget().attachItem(index, item);
	}

	@Override
	public void changeItemIndex(final ITabItem tabItem, final int newIndex) {
		getWidget().changeItemIndex(tabItem, newIndex);
	}
}

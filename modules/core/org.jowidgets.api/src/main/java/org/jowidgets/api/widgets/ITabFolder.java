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

package org.jowidgets.api.widgets;

import java.util.List;

import org.jowidgets.api.controller.ITabFolderObservable;
import org.jowidgets.api.widgets.descriptor.ITabItemDescriptor;
import org.jowidgets.common.widgets.ITabFolderCommon;

public interface ITabFolder extends IControl, ITabFolderCommon, ITabFolderObservable {

    ITabItem addItem(ITabItemDescriptor descriptor);

    ITabItem addItem(int index, ITabItemDescriptor descriptor);

    void removeItem(ITabItem item);

    void removeAllItems();

    ITabItem getItem(int index);

    int getIndex(ITabItem item);

    List<ITabItem> getItems();

    void setSelectedItem(ITabItem item);

    ITabItem getSelectedItem();

    void detachItem(ITabItem item);

    void attachItem(ITabItem item);

    void attachItem(int index, ITabItem item);

    void changeItemIndex(ITabItem tabItem, int newIndex);

}

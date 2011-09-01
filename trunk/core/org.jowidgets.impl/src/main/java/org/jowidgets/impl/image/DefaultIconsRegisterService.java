/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.image;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.common.image.IImageHandleFactory;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.image.IconsCommon;
import org.jowidgets.common.image.IconsSmallCommon;

public class DefaultIconsRegisterService extends ImageConstantRegisterService {

	public DefaultIconsRegisterService(final IImageRegistry imageRegistry, final IImageHandleFactory imageHandleFactory) {
		super("images/icons/", imageRegistry, imageHandleFactory);
	}

	public void registerImages() {
		registerImage(IconsSmall.EMPTY, "empty.png");

		registerImage(IconsSmall.OK, "ok.png");
		registerImage(IconsSmall.POPUP_ARROW, "popup_arrow.png");

		registerImage(IconsCommon.FALLBACK_INFO, "empty.png");
		registerImage(IconsCommon.FALLBACK_ERROR, "empty.png");
		registerImage(IconsCommon.FALLBACK_QUESTION, "empty.png");
		registerImage(IconsCommon.FALLBACK_WARNING, "empty.png");

		registerImage(IconsSmallCommon.CLOSE, "close.png");
		registerImage(IconsSmallCommon.CLOSE_MOUSEOVER, "close_mouseover.png");

		registerImage(IconsSmall.NAVIGATION_FIRST_TINY, "nav_first_tiny.gif");
		registerImage(IconsSmall.NAVIGATION_NEXT_TINY, "nav_next_tiny.gif");
		registerImage(IconsSmall.NAVIGATION_PREVIOUS_TINY, "nav_prev_tiny.gif");
		registerImage(IconsSmall.NAVIGATION_LAST_TINY, "nav_last_tiny.gif");
		registerImage(IconsSmall.NAVIGATION_FORWARD_TINY, "nav_forward_tiny.gif");
		registerImage(IconsSmall.NAVIGATION_BACKWARD_TINY, "nav_backward_tiny.gif");

		registerImage(IconsSmall.TABLE_SORT_DESC, "table_sort_desc.gif");
		registerImage(IconsSmall.TABLE_SORT_ASC, "table_sort_asc.gif");

		registerImage(IconsSmall.ADD, "add.gif");
		registerImage(IconsSmall.ADD_ALL, "add_all.gif");
		registerImage(IconsSmall.SUB, "sub.gif");

		registerImage(IconsSmall.DELETE_TINY, "delete_tiny.png");
		registerImage(IconsSmall.DELETE_GREY_TINY, "delete_grey_tiny.png");

		registerImage(IconsSmall.SETTINGS, "settings.gif");

		registerImage(IconsSmall.EDIT, "edit.png");

		registerImage(IconsSmall.FILTER, "filter.png");
		registerImage(IconsSmall.FILTER_INCLUDING, "filter_including.png");
		registerImage(IconsSmall.FILTER_EXCLUDING, "filter_excluding.png");
		registerImage(IconsSmall.FILTER_EDIT, "filter_edit.png");
		registerImage(IconsSmall.FILTER_DISABLED, "filter_disabled.png");
		registerImage(IconsSmall.FILTER_ENABLED, "filter_enabled.png");
		registerImage(IconsSmall.FILTER_DELETE, "filter_delete.png");
	}
}

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

package org.jowidgets.addons.icons.silkicons;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.IToolkit;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.util.Assert;

public final class SilkIconsSubstitude {

	private SilkIconsSubstitude() {}

	/**
	 * Substitudes some jowidgets default icons by silk icons for the default toolkit
	 * 
	 * @param tookit The toolkit to initialize the silk icons for, must not be null
	 */
	public static void substitude() {
		substitude(Toolkit.getInstance());
	}

	/**
	 * Substitudes some jowidgets default icons by silk icons for a given toolkit
	 * 
	 * @param tookit The toolkit to use , must not be null
	 */
	public static void substitude(final IToolkit toolkit) {
		Assert.paramNotNull(toolkit, "toolkit");

		final IImageRegistry imageRegistry = toolkit.getImageRegistry();
		imageRegistry.registerImageConstant(IconsSmall.OK, SilkIcons.TICK);
		imageRegistry.registerImageConstant(IconsSmall.OK_GREYED, SilkIcons.TICK_GREYED);
		imageRegistry.registerImageConstant(IconsSmall.DISK, SilkIcons.DISK);
		imageRegistry.registerImageConstant(IconsSmall.FOLDER, SilkIcons.FOLDER);
		imageRegistry.registerImageConstant(IconsSmall.PAGE_WHITE, SilkIcons.PAGE_WHITE);
		imageRegistry.registerImageConstant(IconsSmall.REFRESH, SilkIcons.ARROW_REFRESH);
		imageRegistry.registerImageConstant(IconsSmall.UNDO, SilkIcons.ARROW_UNDO);
		imageRegistry.registerImageConstant(IconsSmall.CANCEL, SilkIcons.CANCEL);
	}
}

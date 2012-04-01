/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.addons.widgets.browser.impl.swt;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserBluePrint;
import org.jowidgets.addons.widgets.browser.api.IMainBrowser;
import org.jowidgets.addons.widgets.browser.api.IMainBrowserBluePrint;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.util.FutureValue;
import org.jowidgets.util.IFutureValue;

public final class SwtBrowserFactory {

	private SwtBrowserFactory() {}

	public static IBrowser createBrowser(final IControl control, final Composite swtComposite, final IBrowserBluePrint bluePrint) {
		return createBrowser(control, new FutureValue<Composite>(swtComposite), bluePrint);
	}

	public static IBrowser createBrowser(
		final IControl control,
		final IFutureValue<Composite> swtComposite,
		final IBrowserBluePrint bluePrint) {
		return new BrowserImpl(control, swtComposite, bluePrint);
	}

	public static IMainBrowser createMainBrowser(
		final IControl control,
		final Composite swtComposite,
		final IMainBrowserBluePrint bluePrint) {
		return createMainBrowser(control, new FutureValue<Composite>(swtComposite), bluePrint);
	}

	public static IMainBrowser createMainBrowser(
		final IControl control,
		final IFutureValue<Composite> swtComposite,
		final IMainBrowserBluePrint bluePrint) {
		return new MainBrowserImpl(control, swtComposite, bluePrint);
	}

}

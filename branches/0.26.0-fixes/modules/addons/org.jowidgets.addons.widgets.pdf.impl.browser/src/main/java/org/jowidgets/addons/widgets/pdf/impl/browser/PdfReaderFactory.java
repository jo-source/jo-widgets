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

package org.jowidgets.addons.widgets.pdf.impl.browser;

import org.jowidgets.addons.widgets.browser.api.BrowserBPF;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserBluePrint;
import org.jowidgets.addons.widgets.pdf.api.IPdfReader;
import org.jowidgets.addons.widgets.pdf.api.IPdfReaderBluePrint;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.factory.IWidgetFactory;

final class PdfReaderFactory implements IWidgetFactory<IPdfReader, IPdfReaderBluePrint> {

	@Override
	public IPdfReader create(final Object parentUiReference, final IPdfReaderBluePrint bluePrint) {
		final IBrowserBluePrint browserBp = BrowserBPF.browser();
		browserBp.setSetup(bluePrint);
		final IBrowser browser = Toolkit.getWidgetFactory().create(parentUiReference, browserBp);
		return new PdfReaderImpl(browser, bluePrint);
	}

}

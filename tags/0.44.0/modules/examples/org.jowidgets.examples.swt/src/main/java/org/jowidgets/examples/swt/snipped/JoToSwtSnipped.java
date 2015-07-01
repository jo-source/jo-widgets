/*
 * Copyright (c) 2015, grossmann
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
package org.jowidgets.examples.swt.snipped;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class JoToSwtSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {
        //create the root frame
        final IFrame frame = Toolkit.createRootFrame(BPF.frame("JoToSwt Snipped"), lifecycle);
        frame.setSize(1024, 768);
        frame.setLayout(FillLayout.get());

        //create a regular jo composite
        final IComposite joComposite = frame.add(BPF.composite());

        //get the native ui reference which must be a swt composite
        //because swt SPI impl is used
        final Composite swtComposite = (Composite) joComposite.getUiReference();
        swtComposite.setLayout(new org.eclipse.swt.layout.FillLayout());

        //create a swt browser
        final Browser browser = new Browser(swtComposite, SWT.NONE);
        browser.setUrl("http://www.jowidgets.org/");

        //set the root frame visible
        frame.setVisible(true);
    }

    public static void main(final String[] args) throws Exception {
        Toolkit.getApplicationRunner().run(new JoToSwtSnipped());
        System.exit(0);
    }
}

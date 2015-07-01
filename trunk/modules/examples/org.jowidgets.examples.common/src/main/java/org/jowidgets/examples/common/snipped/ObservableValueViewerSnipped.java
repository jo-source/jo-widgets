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
package org.jowidgets.examples.common.snipped;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ISliderViewerBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.ObservableValue;

public final class ObservableValueViewerSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        //create the root frame
        final IFrameBluePrint frameBp = BPF.frame("Observable value viewer snipped");
        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
        frame.setLayout(new MigLayoutDescriptor("wrap", "[][][]", "[]"));

        //create panorama observable value 
        final IObservableValue<Double> panorama = new ObservableValue<Double>(0.0d);

        //add panorama label
        final ITextLabelBluePrint labelBp = BPF.textLabel("Panorama");
        labelBp.setForegroundColor(Colors.STRONG).setMarkup(Markup.STRONG);
        frame.add(labelBp);

        //add panorama slider
        final ISliderViewerBluePrint<Double> sliderBp = BPF.sliderViewerDouble(-1.0d, 1.0d);
        sliderBp.setObservableValue(panorama);
        frame.add(sliderBp, "growx, w 150!");

        //add panorama input field
        final IInputFieldBluePrint<Double> inputFieldBp = BPF.inputFieldDoubleNumber();
        inputFieldBp.setObservableValue(panorama);
        frame.add(inputFieldBp, "w 50!");

        //set the root frame visible
        frame.setVisible(true);
    }
}

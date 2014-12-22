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

package org.jowidgets.examples.common.demo;

import org.jowidgets.api.convert.ILinearSliderConverterBuilder;
import org.jowidgets.api.convert.ISliderViewerConverter;
import org.jowidgets.api.convert.LinearSliderConverter;
import org.jowidgets.api.widgets.ISliderViewer;
import org.jowidgets.api.widgets.blueprint.ISliderViewerBluePrint;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.IObservableValueListener;

final class DemoSliderViewerFrame extends JoFrame {

	DemoSliderViewerFrame() {
		super("Slider viewer demo");

		setLayout(new MigLayoutDescriptor("wrap", "0[grow, 0::]0", "0[]10[]10[]10[]0"));

		//slider1 - for demonstration purpose, see next sliders creation for more convenient way
		final ISliderViewerBluePrint<Double> sliderBp1 = BPF.sliderViewer();
		sliderBp1.setDefaultValue(0.5d);//left click with CTRL on slider to reset default value
		sliderBp1.setMinimum(50).setMaximum(1000);
		sliderBp1.setTickSpacing((1000 - 50) / 10);
		sliderBp1.setConverter(new ISliderViewerConverter<Double>() {
			@Override
			public int getSliderValue(final int sliderMin, final int sliderMax, final Double modelValue) {
				return (int) (sliderMin + ((sliderMax - sliderMin) * modelValue.doubleValue()));
			}

			@Override
			public Double getModelValue(final int sliderMin, final int sliderMax, final int sliderValue) {
				return Double.valueOf((1.0d / (sliderMax - sliderMin)) * (sliderValue - sliderMin));
			}
		});
		final ISliderViewer<Double> slider1 = add(sliderBp1, "growx, w 0::");
		slider1.getObservableValue().addValueListener(new ObservableValueListener(slider1));

		//slider2 - uses default double slider with min=0.0d and max=1.0d
		final ISliderViewerBluePrint<Double> sliderBp2 = BPF.sliderViewer();
		sliderBp2.setDefaultValue(0.0d);
		//use the linear converter explicit, see next slider how to use implicit
		sliderBp2.setConverter(LinearSliderConverter.create());
		final ISliderViewer<Double> slider2 = add(sliderBp2, "growx, w 0::");
		slider2.getObservableValue().addValueListener(new ObservableValueListener(slider2));

		//slider3 - uses double slider with min=12.0d and max=16.0d
		final ISliderViewerBluePrint<Double> sliderBp3 = BPF.sliderViewerDouble(12.0d, 16.0d);
		sliderBp3.setDefaultValue(14.0d);
		final ISliderViewer<Double> slider3 = add(sliderBp3, "growx, w 0::");
		slider3.getObservableValue().addValueListener(new ObservableValueListener(slider3));

		//slider4 - uses double slider with min=0.1d and max=10.0d, pivot=(0.5, 1.0)
		final ILinearSliderConverterBuilder<Double> converterBuilder = LinearSliderConverter.builder();
		converterBuilder.setMinValue(0.1d);
		converterBuilder.setMaxValue(10.0d);
		converterBuilder.setPivotValue(1.0d);
		final ISliderViewerBluePrint<Double> sliderBp4 = BPF.sliderViewer(converterBuilder.build());
		sliderBp4.setDefaultValue(1.0d);
		final ISliderViewer<Double> slider4 = add(sliderBp4, "growx, w 0::");
		slider4.getObservableValue().addValueListener(new ObservableValueListener(slider4));
	}

	private static final class ObservableValueListener implements IObservableValueListener<Double> {

		private final ISliderViewer<Double> slider;

		private ObservableValueListener(final ISliderViewer<Double> slider) {
			this.slider = slider;
		}

		@Override
		public void changed(final IObservableValue<Double> observableValue, final Double value) {
			//CHECKSTYLE:OFF
			System.out.println("ViewerValue: " + value + " / SliderValue:" + slider.getSlider().getValue());
			//CHECKSTYLE:ON
		}

	}
}

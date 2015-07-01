/*
 * Copyright (c) 2014, grossmann
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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.controller.IMouseButtonEventMatcher;
import org.jowidgets.api.convert.ISliderViewerConverter;
import org.jowidgets.api.widgets.ISlider;
import org.jowidgets.api.widgets.ISliderViewer;
import org.jowidgets.api.widgets.descriptor.ISliderViewerDescriptor;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;
import org.jowidgets.tools.widgets.wrapper.AbstractInputControl;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.binding.Bind;
import org.jowidgets.util.binding.IBinding;
import org.jowidgets.util.binding.IBindingConverter;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.ValidationResult;

public class SliderViewerImpl<VALUE_TYPE> extends AbstractInputControl<VALUE_TYPE> implements ISliderViewer<VALUE_TYPE> {

    private final IObservableValue<VALUE_TYPE> observableValue;
    private final IBinding binding;
    private final VALUE_TYPE defaultValue;

    public SliderViewerImpl(final ISlider slider, final ISliderViewerDescriptor<VALUE_TYPE> setup) {

        super(slider);

        Assert.paramNotNull(setup.getConverter(), "setup.getConverter()");
        Assert.paramNotNull(setup.getObservableValue(), "setup.getObservableValue()");

        this.observableValue = setup.getObservableValue();

        this.defaultValue = setup.getDefaultValue();
        final IMouseButtonEventMatcher defaultValueMatcher = setup.getDefaultValueMatcher();
        if (defaultValue != null) {
            slider.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(final IMouseButtonEvent event) {
                    if (defaultValueMatcher.matches(event, false)) {
                        setValue(defaultValue);
                    }
                }

                @Override
                public void mouseDoubleClicked(final IMouseButtonEvent event) {
                    if (defaultValueMatcher.matches(event, true)) {
                        setValue(defaultValue);
                    }
                }
            });
        }

        final ISliderViewerConverter<VALUE_TYPE> viewerConverter = setup.getConverter();

        VisibiliySettingsInvoker.setVisibility(setup, this);
        ColorSettingsInvoker.setColors(setup, this);

        //default value overrides setup value if null
        final VALUE_TYPE initialValue;
        if (defaultValue != null && setup.getValue() == null) {
            initialValue = defaultValue;
        }
        else {
            initialValue = setup.getValue();
        }

        //observable value overrides setup value
        if (initialValue != null && observableValue.getValue() == null) {
            setValue(initialValue);
        }
        else if (observableValue.getValue() == null) {
            observableValue.setValue(viewerConverter.getModelValue(
                    slider.getMinimum(),
                    slider.getMaximum(),
                    slider.getSelection()));
        }

        this.binding = Bind.bind(observableValue, slider.getObservableValue(), new IBindingConverter<VALUE_TYPE, Integer>() {
            @Override
            public Integer convertSource(final VALUE_TYPE sourceValue) {
                int sliderValue = viewerConverter.getSliderValue(slider.getMinimum(), slider.getMaximum(), sourceValue);
                sliderValue = Math.max(slider.getMinimum(), sliderValue);
                sliderValue = Math.min(slider.getMaximum(), sliderValue);
                return Integer.valueOf(sliderValue);
            }

            @Override
            public VALUE_TYPE convertDestination(final Integer destinationValue) {
                return viewerConverter.getModelValue(slider.getMinimum(), slider.getMaximum(), destinationValue);
            }
        });

        resetModificationState();
    }

    @Override
    protected ISlider getWidget() {
        return (ISlider) super.getWidget();
    }

    @Override
    public IObservableValue<VALUE_TYPE> getObservableValue() {
        return observableValue;
    }

    @Override
    public boolean hasModifications() {
        return getWidget().hasModifications();
    }

    @Override
    public void resetModificationState() {
        getWidget().resetModificationState();
    }

    @Override
    protected IValidationResult createValidationResult() {
        return ValidationResult.ok();
    }

    @Override
    public void setValue(final VALUE_TYPE value) {
        observableValue.setValue(value);
    }

    @Override
    public VALUE_TYPE getValue() {
        return observableValue.getValue();
    }

    @Override
    public void setEditable(final boolean editable) {
        getWidget().setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return getWidget().isEditable();
    }

    @Override
    public ISlider getSlider() {
        return getWidget();
    }

    @Override
    public void dispose() {
        binding.dispose();
        super.dispose();
    }

}

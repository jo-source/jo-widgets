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
package org.jowidgets.impl.widgets.composed.blueprint;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.convert.ISliderViewerConverter;
import org.jowidgets.api.convert.LinearSliderConverter;
import org.jowidgets.api.image.Icons;
import org.jowidgets.api.login.ILoginInterceptor;
import org.jowidgets.api.password.IPasswordChangeExecutor;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.ICollectionInputControlBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ICombinedCollectionInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IDynamicFlowLayoutCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IExpandCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ILoginDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IPasswordChangeDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ISliderViewerBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextSeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.IUnitValueFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationResultLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintProxyFactory;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.IInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.ICollectionInputControlSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.impl.base.blueprint.factory.BluePrintProxyFactoryImpl;
import org.jowidgets.impl.widgets.basic.blueprint.AbstractBasicBluePrintFactory;
import org.jowidgets.impl.widgets.composed.blueprint.convenience.registry.ComposedSetupConvenienceRegistry;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.registry.ComposedDefaultsInitializerRegistry;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.unit.api.IUnitConverter;
import org.jowidgets.unit.api.IUnitSet;
import org.jowidgets.util.Assert;

public class BluePrintFactory extends AbstractBasicBluePrintFactory implements IBluePrintFactory {

    public BluePrintFactory() {
        this(new ComposedSetupConvenienceRegistry(), new ComposedDefaultsInitializerRegistry());
    }

    public BluePrintFactory(final IDefaultsInitializerRegistry defaultInitializerRegistry) {
        this(new ComposedSetupConvenienceRegistry(), defaultInitializerRegistry);
    }

    public BluePrintFactory(final ISetupBuilderConvenienceRegistry setupBuilderConvenienceRegistry) {
        this(setupBuilderConvenienceRegistry, new ComposedDefaultsInitializerRegistry());
    }

    public BluePrintFactory(
        final ISetupBuilderConvenienceRegistry setupBuilderConvenienceRegistry,
        final IDefaultsInitializerRegistry defaultInitializerRegistry) {
        this(new BluePrintProxyFactoryImpl(setupBuilderConvenienceRegistry, defaultInitializerRegistry));
    }

    public BluePrintFactory(final IBluePrintProxyFactory bluePrintProxyFactory) {
        super(bluePrintProxyFactory);
    }

    @Override
    public ILabelBluePrint label() {
        return createProxy(ILabelBluePrint.class);
    }

    @Override
    public ITextSeparatorBluePrint textSeparator() {
        return createProxy(ITextSeparatorBluePrint.class);
    }

    @Override
    public IValidationResultLabelBluePrint validationResultLabel() {
        return createProxy(IValidationResultLabelBluePrint.class);
    }

    @Override
    public IInputComponentValidationLabelBluePrint inputComponentValidationLabel(final IInputComponent<?> inputComponent) {
        Assert.paramNotNull(inputComponent, "inputComponent");
        final IInputComponentValidationLabelBluePrint result = createProxy(IInputComponentValidationLabelBluePrint.class);
        result.setInputComponent(inputComponent);
        return result;
    }

    @Override
    public IInputComponentValidationLabelBluePrint inputComponentValidationLabel() {
        return createProxy(IInputComponentValidationLabelBluePrint.class);
    }

    @Override
    public IMessageDialogBluePrint messageDialog() {
        return createProxy(IMessageDialogBluePrint.class);
    }

    @Override
    public IQuestionDialogBluePrint questionDialog() {
        return createProxy(IQuestionDialogBluePrint.class);
    }

    @Override
    public IProgressBarBluePrint progressBar() {
        return createProxy(IProgressBarBluePrint.class);
    }

    @Override
    public <INPUT_TYPE> IInputDialogBluePrint<INPUT_TYPE> inputDialog(final IInputContentCreator<INPUT_TYPE> contentCreator) {
        Assert.paramNotNull(contentCreator, "contentCreator");

        final IInputDialogBluePrint<INPUT_TYPE> inputDialogBluePrint;
        inputDialogBluePrint = createProxy(IInputDialogBluePrint.class);

        inputDialogBluePrint.setContentCreator(contentCreator);
        return inputDialogBluePrint;
    }

    @Override
    public <INPUT_TYPE> IInputCompositeBluePrint<INPUT_TYPE> inputComposite(final IInputContentCreator<INPUT_TYPE> contentCreator) {
        Assert.paramNotNull(contentCreator, "contentCreator");

        final IInputCompositeBluePrint<INPUT_TYPE> inputCompositeBluePrint = createProxy(IInputCompositeBluePrint.class);

        inputCompositeBluePrint.setContentCreator(contentCreator);
        return inputCompositeBluePrint;
    }

    @Override
    public <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IConverter<INPUT_TYPE> converter) {
        Assert.paramNotNull(converter, "converter");
        final IInputFieldBluePrint<INPUT_TYPE> result = createProxy(IInputFieldBluePrint.class);
        return result.setConverter(converter);
    }

    @Override
    public <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IObjectStringConverter<INPUT_TYPE> converter) {
        Assert.paramNotNull(converter, "converter");
        final IInputFieldBluePrint<INPUT_TYPE> result = createProxy(IInputFieldBluePrint.class);
        return result.setConverter(converter);
    }

    @Override
    public <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField() {
        final IInputFieldBluePrint<INPUT_TYPE> result = createProxy(IInputFieldBluePrint.class);
        final IObjectStringConverter<INPUT_TYPE> converter = Toolkit.getConverterProvider().toStringConverter();
        return result.setConverter(converter);
    }

    @Override
    public <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField() {
        final IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> result = createProxy(IUnitValueFieldBluePrint.class);
        return result;
    }

    @Override
    public <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField(
        final Class<? extends UNIT_VALUE_TYPE> inputFieldType) {
        Assert.paramNotNull(inputFieldType, "inputFieldType");

        final IConverter<UNIT_VALUE_TYPE> converter = Toolkit.getConverterProvider().getConverter(inputFieldType);
        if (converter == null) {
            throw new IllegalArgumentException("No converter found for type '" + inputFieldType.getName() + "'.");
        }

        final IInputFieldBluePrint<UNIT_VALUE_TYPE> inputField = inputField();
        inputField.setConverter(converter);

        final IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> result = unitValueField();
        result.setUnitValueInputField(inputField);
        return result;
    }

    @Override
    public <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField(
        final IUnitSet unitSet,
        final IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> converter,
        final IInputFieldDescriptor<UNIT_VALUE_TYPE> inputField) {
        final IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> result = unitValueField();
        result.setUnitSet(unitSet).setUnitConverter(converter).setUnitValueInputField(inputField);
        return result;
    }

    @Override
    public <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitValueField(
        final IUnitSet unitSet,
        final IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> converter,
        final Class<? extends UNIT_VALUE_TYPE> inputFieldType) {
        final IUnitValueFieldBluePrint<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> result = unitValueField(inputFieldType);
        result.setUnitSet(unitSet).setUnitConverter(converter);
        return result;
    }

    @Override
    public ILoginDialogBluePrint loginDialog(final ILoginInterceptor loginInterceptor) {
        Assert.paramNotNull(loginInterceptor, "loginInterceptor");
        final ILoginDialogBluePrint result = createProxy(ILoginDialogBluePrint.class);
        result.setInterceptor(loginInterceptor);
        return result;
    }

    @Override
    public IPasswordChangeDialogBluePrint passwordChangeDialog(final IPasswordChangeExecutor executor) {
        Assert.paramNotNull(executor, "executor");
        final IPasswordChangeDialogBluePrint result = createProxy(IPasswordChangeDialogBluePrint.class);
        result.setExecutor(executor);
        return result;
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
        final ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> widgetCreator) {
        Assert.paramNotNull(widgetCreator, "widgetCreator");
        final ICollectionInputControlBluePrint<ELEMENT_TYPE> result = createProxy(ICollectionInputControlBluePrint.class);
        result.setElementWidgetCreator(widgetCreator);
        return result;
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
        final ICollectionInputControlSetup<ELEMENT_TYPE> setup) {
        Assert.paramNotNull(setup, "setup");
        final ICollectionInputDialogBluePrint<ELEMENT_TYPE> result = createProxy(ICollectionInputDialogBluePrint.class);
        result.setCollectionInputControlSetup(setup);
        return result;
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(
        final IConverter<ELEMENT_TYPE> converter) {
        Assert.paramNotNull(converter, "converter");
        final ICollectionInputFieldBluePrint<ELEMENT_TYPE> result = createProxy(ICollectionInputFieldBluePrint.class);
        result.setConverter(converter);
        return result;
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField(
        final IObjectStringConverter<ELEMENT_TYPE> converter) {
        Assert.paramNotNull(converter, "converter");
        final ICollectionInputFieldBluePrint<ELEMENT_TYPE> result = createProxy(ICollectionInputFieldBluePrint.class);
        result.setConverter(converter);
        return result;
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputFieldBluePrint<ELEMENT_TYPE> collectionInputField() {
        final ICollectionInputFieldBluePrint<ELEMENT_TYPE> result = createProxy(ICollectionInputFieldBluePrint.class);
        final IObjectStringConverter<ELEMENT_TYPE> converter = Toolkit.getConverterProvider().toStringConverter();
        result.setConverter(converter);
        return result;
    }

    @Override
    public <ELEMENT_TYPE> ICombinedCollectionInputFieldBluePrint<ELEMENT_TYPE> combinedCollectionInputField(
        final ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>> elementTypeCreator,
        final ICustomWidgetCreator<IInputControl<? extends Collection<ELEMENT_TYPE>>> collectionTypeCreator) {
        Assert.paramNotNull(elementTypeCreator, "elementTypeCreator");
        Assert.paramNotNull(collectionTypeCreator, "collectionTypeCreator");
        final ICombinedCollectionInputFieldBluePrint<ELEMENT_TYPE> result = createProxy(ICombinedCollectionInputFieldBluePrint.class);
        result.setElementTypeControlCreator(elementTypeCreator);
        result.setCollectionTypeControlCreator(collectionTypeCreator);
        return result;
    }

    @Override
    public IExpandCompositeBluePrint expandComposite() {
        return createProxy(IExpandCompositeBluePrint.class);
    }

    @Override
    public <VALUE_TYPE> ISliderViewerBluePrint<VALUE_TYPE> sliderViewer() {
        return createProxy(ISliderViewerBluePrint.class);
    }

    @Override
    public <VALUE_TYPE> ISliderViewerBluePrint<VALUE_TYPE> sliderViewer(final ISliderViewerConverter<VALUE_TYPE> converter) {
        final ISliderViewerBluePrint<VALUE_TYPE> result = sliderViewer();
        result.setConverter(converter);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////input fields here///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public IDynamicFlowLayoutCompositeBluePrint dynamicFlowLayoutComposite() {
        return createProxy(IDynamicFlowLayoutCompositeBluePrint.class);
    }

    @Override
    public IInputFieldBluePrint<String> inputFieldString() {
        return inputField(Toolkit.getConverterProvider().string());
    }

    @Override
    public IInputFieldBluePrint<Long> inputFieldLongNumber() {
        return inputField(Toolkit.getConverterProvider().longNumber());
    }

    @Override
    public IInputFieldBluePrint<Short> inputFieldShortNumber() {
        return inputField(Toolkit.getConverterProvider().shortNumber());
    }

    @Override
    public IInputFieldBluePrint<Integer> inputFieldIntegerNumber() {
        return inputField(Toolkit.getConverterProvider().integerNumber());
    }

    @Override
    public IInputFieldBluePrint<Double> inputFieldDoubleNumber() {
        return inputField(Toolkit.getConverterProvider().doubleNumber());
    }

    @Override
    public IInputFieldBluePrint<Double> inputFieldDoubleNumber(final int minFractionDigits, final int maxFractionDigits) {
        return inputField(Toolkit.getConverterProvider().doubleNumber(minFractionDigits, maxFractionDigits));
    }

    @Override
    public IInputFieldBluePrint<Date> inputFieldDate(final DateFormat dateFormat, final String formatHint, final ITextMask mask) {
        return inputField(Toolkit.getConverterProvider().date(dateFormat, formatHint, mask));
    }

    @Override
    public IInputFieldBluePrint<Date> inputFieldDate(final DateFormat dateFormat, final String formatHint) {
        return inputField(Toolkit.getConverterProvider().date(dateFormat, formatHint));
    }

    @Override
    public IInputFieldBluePrint<Date> inputFieldDate() {
        return inputField(Toolkit.getConverterProvider().date());
    }

    @Override
    public IInputFieldBluePrint<Date> inputFieldDateTime() {
        return inputField(Toolkit.getConverterProvider().dateTime());
    }

    @Override
    public IInputFieldBluePrint<Date> inputFieldTime() {
        return inputField(Toolkit.getConverterProvider().time());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////some convenience methods starting here///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ILabelBluePrint label(final IImageConstant icon) {
        return label().setIcon(icon);
    }

    @Override
    public ILabelBluePrint label(final IImageConstant icon, final String text) {
        return label().setIcon(icon).setText(text);
    }

    @Override
    public ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext) {
        return label().setIcon(icon).setText(text).setToolTipText(toolTiptext);
    }

    @Override
    public ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText) {
        return textSeparator(text).setToolTipText(tooltipText);
    }

    @Override
    public ITextSeparatorBluePrint textSeparator(final String text) {
        return textSeparator().setText(text);
    }

    @Override
    public IMessageDialogBluePrint infoDialog() {
        return messageDialog().setIcon(Icons.INFO);
    }

    @Override
    public IMessageDialogBluePrint warningDialog() {
        return messageDialog().setIcon(Icons.WARNING);
    }

    @Override
    public IMessageDialogBluePrint errorDialog() {
        return messageDialog().setIcon(Icons.ERROR);
    }

    @Override
    public IMessageDialogBluePrint infoDialog(final String message) {
        return infoDialog().setText(message);
    }

    @Override
    public IMessageDialogBluePrint warningDialog(final String message) {
        return warningDialog().setText(message);
    }

    @Override
    public IMessageDialogBluePrint errorDialog(final String message) {
        return errorDialog().setText(message);
    }

    @Override
    public IQuestionDialogBluePrint yesNoQuestion() {
        return questionDialog();
    }

    @Override
    public IQuestionDialogBluePrint yesNoCancelQuestion() {
        return questionDialog().setCancelButton(BPF.buttonCancel());
    }

    @Override
    public IQuestionDialogBluePrint yesNoQuestion(final String question) {
        return yesNoQuestion().setText(question);
    }

    @Override
    public IQuestionDialogBluePrint yesNoCancelQuestion(final String question) {
        return yesNoCancelQuestion().setText(question);
    }

    @Override
    public IProgressBarBluePrint progressBar(final int minimum, final int maximum) {
        return progressBar(maximum).setMinimum(minimum);
    }

    @Override
    public IProgressBarBluePrint progressBar(final int maximum) {
        return progressBar().setIndeterminate(false).setMaximum(maximum);
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputControlBluePrint<ELEMENT_TYPE> collectionInputControl(
        final IWidgetDescriptor<? extends IInputControl<ELEMENT_TYPE>> descriptor) {
        Assert.paramNotNull(descriptor, "descriptor"); //$NON-NLS-1$
        return collectionInputControl(new ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>>() {
            @Override
            public IInputControl<ELEMENT_TYPE> create(final ICustomWidgetFactory widgetFactory) {
                return widgetFactory.create(descriptor);
            }
        });
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
        final ICustomWidgetCreator<? extends IInputControl<ELEMENT_TYPE>> widgetCreator) {
        @SuppressWarnings("unchecked")
        final ICollectionInputControlSetup<ELEMENT_TYPE> collectionInputControlSetup = collectionInputControl((ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>>) widgetCreator);
        return collectionInputDialog(collectionInputControlSetup);
    }

    @Override
    public <ELEMENT_TYPE> ICollectionInputDialogBluePrint<ELEMENT_TYPE> collectionInputDialog(
        final IWidgetDescriptor<? extends IInputControl<ELEMENT_TYPE>> descriptor) {
        final ICollectionInputControlSetup<ELEMENT_TYPE> collectionInputControlSetup = collectionInputControl(descriptor);
        return collectionInputDialog(collectionInputControlSetup);
    }

    @Override
    public ISliderViewerBluePrint<Double> sliderViewerDouble() {
        final ISliderViewerBluePrint<Double> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create());
    }

    @Override
    public ISliderViewerBluePrint<Double> sliderViewerDouble(final double max) {
        final ISliderViewerBluePrint<Double> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(max));
    }

    @Override
    public ISliderViewerBluePrint<Double> sliderViewerDouble(final double min, final double max) {
        final ISliderViewerBluePrint<Double> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(min, max));
    }

    @Override
    public ISliderViewerBluePrint<Float> sliderViewerFloat() {
        final ISliderViewerBluePrint<Float> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(1.0f));
    }

    @Override
    public ISliderViewerBluePrint<Float> sliderViewerFloat(final float max) {
        final ISliderViewerBluePrint<Float> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(max));
    }

    @Override
    public ISliderViewerBluePrint<Float> sliderViewerFloat(final float min, final float max) {
        final ISliderViewerBluePrint<Float> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(min, max));
    }

    @Override
    public ISliderViewerBluePrint<Integer> sliderViewerInteger(final int max) {
        final ISliderViewerBluePrint<Integer> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(max));
    }

    @Override
    public ISliderViewerBluePrint<Integer> sliderViewerInteger(final int min, final int max) {
        final ISliderViewerBluePrint<Integer> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(min, max));
    }

    @Override
    public ISliderViewerBluePrint<Long> sliderViewerLong(final long max) {
        final ISliderViewerBluePrint<Long> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(max));
    }

    @Override
    public ISliderViewerBluePrint<Long> sliderViewerLong(final long min, final long max) {
        final ISliderViewerBluePrint<Long> result = sliderViewer();
        return result.setConverter(LinearSliderConverter.create(min, max));
    }

}

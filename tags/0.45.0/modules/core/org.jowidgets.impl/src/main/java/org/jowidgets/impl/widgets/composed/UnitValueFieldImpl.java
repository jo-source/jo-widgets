/*
 * Copyright (c) 2014, Michael Grossmann
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

import java.util.Map;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.IUnitValueField;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.descriptor.setup.IUnitValueFieldSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.FocusObservable;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.controller.KeyObservable;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;
import org.jowidgets.tools.widgets.wrapper.AbstractInputControl;
import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitConverter;
import org.jowidgets.unit.api.IUnitSet;
import org.jowidgets.unit.api.IUnitValue;
import org.jowidgets.unit.tools.UnitValue;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public final class UnitValueFieldImpl<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> extends AbstractInputControl<BASE_VALUE_TYPE> implements
        IUnitValueField<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> {

    private final IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitConverter;
    private final IUnitSet unitSet;
    private final IUnit defaultUnit;

    private final IInputField<UNIT_VALUE_TYPE> valueField;
    private final IComboBox<IUnit> unitCmb;
    private final FocusObservable focusObservable;
    private final KeyObservable keyObservable;

    private boolean lastFocus;

    public UnitValueFieldImpl(final IComposite composite, final IUnitValueFieldSetup<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> setup) {
        super(composite);
        Assert.paramNotNull(setup.getUnitSet(), "setup.getUnitSet()");
        Assert.paramNotNull(setup.getUnitConverter(), "setup.getUnitConverter()");

        this.unitSet = setup.getUnitSet();
        this.unitConverter = setup.getUnitConverter();

        if (setup.getDefaultUnit() != null) {
            this.defaultUnit = setup.getDefaultUnit();
        }
        else {
            this.defaultUnit = unitSet.get(0);
        }

        this.focusObservable = new FocusObservable();
        this.keyObservable = new KeyObservable();

        final Integer unitComboMinSize = setup.getUnitComboMinSize();
        final String unitCmbColumnC;
        final String unitCmbCC;
        if (unitComboMinSize != null) {
            unitCmbColumnC = unitComboMinSize + "::";
            unitCmbCC = ",w " + unitCmbColumnC;
        }
        else {
            unitCmbColumnC = "";
            unitCmbCC = "";
        }

        composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0[" + unitCmbColumnC + "]0", "0[grow]0"));

        final IComboBoxSelectionBluePrint<IUnit> unitCmbBp = BPF.comboBoxSelection(unitSet);
        unitCmbBp.autoCompletionOff().setValue(defaultUnit);

        this.unitCmb = composite.add(unitCmbBp, "growy, sgy hg" + unitCmbCC);

        this.valueField = composite.add(0, setup.getUnitValueInputField(), "grow, w 0::, sgy hg");

        this.lastFocus = hasFocus();

        final IFocusListener focusListener = new FocusListener();
        valueField.addFocusListener(focusListener);
        unitCmb.addFocusListener(focusListener);

        final IValidationConditionListener validationConditionListener = new ValidationConditionListener();
        valueField.addValidationConditionListener(validationConditionListener);
        unitCmb.addValidationConditionListener(validationConditionListener);

        final IInputListener inputListener = new InputListener();
        valueField.addInputListener(inputListener);
        unitCmb.addInputListener(inputListener);

        final Map<VirtualKey, IUnit> unitKeyMapping = setup.getUnitKeyMapping();

        valueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final IKeyEvent event) {

                if (unitKeyMapping != null) {
                    final IUnit unit = unitKeyMapping.get(event.getVirtualKey());
                    if (unit != null) {
                        unitCmb.setValue(unit);
                    }
                }

                final boolean shift = event.getModifier().contains(Modifier.SHIFT);
                final boolean right = VirtualKey.TAB.equals(event.getVirtualKey()) && !shift;

                if (right) {
                    unitCmb.requestFocus();
                    unitCmb.select();
                }
                else {
                    keyObservable.fireKeyPressed(event);
                }

            }
        });

        unitCmb.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final IKeyEvent event) {

                if (unitKeyMapping != null) {
                    final IUnit unit = unitKeyMapping.get(event.getVirtualKey());
                    if (unit != null) {
                        unitCmb.setValue(unit);
                    }
                }

                final boolean shift = event.getModifier().contains(Modifier.SHIFT);
                final boolean left = VirtualKey.TAB.equals(event.getVirtualKey()) && shift;

                final boolean enter = VirtualKey.ENTER.equals(event.getVirtualKey());
                final boolean esc = VirtualKey.ESC.equals(event.getVirtualKey());

                if (left) {
                    valueField.requestFocus();
                }
                else if (!((enter || esc) && unitCmb.isPopupVisible())) {
                    keyObservable.fireKeyPressed(event);
                }
            }
        });

        if (setup.getValidator() != null) {
            addValidator(setup.getValidator());
        }

        if (!setup.isEditable()) {
            setEditable(false);
        }

        VisibiliySettingsInvoker.setVisibility(setup, this);
        ColorSettingsInvoker.setColors(setup, this);

        if (setup.getValue() != null) {
            setValue(setup.getValue());
        }

        resetModificationState();

    }

    @Override
    public boolean hasModifications() {
        return valueField.hasModifications() || unitCmb.hasModifications();
    }

    @Override
    public void resetModificationState() {
        valueField.resetModificationState();
        unitCmb.resetModificationState();
    }

    @Override
    public void setEditable(final boolean editable) {
        valueField.setEditable(editable);
        unitCmb.setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return valueField.isEditable() && unitCmb.isEditable();
    }

    @Override
    public void setValue(final BASE_VALUE_TYPE value) {
        setUnitValue(unitConverter.toUnitValue(value));
    }

    @Override
    public BASE_VALUE_TYPE getValue() {
        return unitConverter.toBaseValue(getUnitValue());
    }

    @Override
    public IUnitValue<UNIT_VALUE_TYPE> getUnitValue() {
        final UNIT_VALUE_TYPE numericValue = valueField.getValue();
        final IUnit unit = unitCmb.getValue();
        if (numericValue != null && unit != null) {
            return new UnitValue<UNIT_VALUE_TYPE>(numericValue, unit);
        }
        else {
            return null;
        }
    }

    @Override
    public void setUnitValue(final IUnitValue<UNIT_VALUE_TYPE> unitValue) {
        if (unitValue != null) {
            valueField.setValue(unitValue.getValue());
            unitCmb.setValue(unitValue.getUnit());
        }
        else {
            valueField.setValue(null);
            unitCmb.setValue(defaultUnit);
        }
    }

    @Override
    protected IValidationResult createValidationResult() {
        final IValidator<IUnitValue<UNIT_VALUE_TYPE>> unitValidator = unitConverter.getValidator();
        if (unitValidator != null) {
            final IValidationResult validationResult = unitValidator.validate(getUnitValue());
            return validationResult;
        }
        return ValidationResult.ok();
    }

    @Override
    public boolean requestFocus() {
        final boolean result = valueField.requestFocus();
        valueField.selectAll();
        return result;
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        keyObservable.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        valueField.removeKeyListener(listener);
        unitCmb.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener listener) {
        valueField.addMouseListener(listener);
        unitCmb.addMouseListener(listener);
    }

    @Override
    public void removeMouseListener(final IMouseListener listener) {
        valueField.removeMouseListener(listener);
        unitCmb.removeMouseListener(listener);
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        focusObservable.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        focusObservable.removeFocusListener(listener);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        valueField.setForegroundColor(colorValue);
        unitCmb.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        valueField.setBackgroundColor(colorValue);
        unitCmb.setBackgroundColor(colorValue);
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension valueSize = valueField.getPreferredSize();
        final Dimension unitSize = unitCmb.getPreferredSize();
        return new Dimension(valueSize.getWidth() + unitSize.getWidth() + 2, unitSize.getHeight());
    }

    @Override
    public boolean hasFocus() {
        return valueField.hasFocus() || unitCmb.hasFocus();
    }

    @Override
    public void select() {
        valueField.select();
    }

    private void focusChanged() {
        final boolean currentFocus = hasFocus();
        if (currentFocus != lastFocus) {
            lastFocus = currentFocus;
            if (currentFocus) {
                focusObservable.focusGained();
            }
            else {
                focusObservable.focusLost();
            }
        }
    }

    private final class FocusListener implements IFocusListener {

        @Override
        public void focusGained() {
            Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
                @Override
                public void run() {
                    focusChanged();
                }
            });

        }

        @Override
        public void focusLost() {
            Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
                @Override
                public void run() {
                    focusChanged();
                }
            });
        }

    }

    private final class ValidationConditionListener implements IValidationConditionListener {

        @Override
        public void validationConditionsChanged() {
            setValidationCacheDirty();
        }

    }

    private final class InputListener implements IInputListener {

        @Override
        public void inputChanged() {
            fireInputChanged();
        }

    }
}

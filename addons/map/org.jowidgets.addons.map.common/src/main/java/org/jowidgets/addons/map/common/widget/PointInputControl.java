/*
 * Copyright (c) 2011, H.Westphal
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

package org.jowidgets.addons.map.common.widget;

import org.jowidgets.addons.map.common.IAvailableCallback;
import org.jowidgets.addons.map.common.IDesignationListener;
import org.jowidgets.addons.map.common.IMap;
import org.jowidgets.addons.map.common.IMapContext;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;

import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;

final class PointInputControl extends ControlWrapper implements IPointInputControl {

	private static final String PLACEMARK_ID = PointInputControl.class.getSimpleName();

	private final InputObservable inputObservable = new InputObservable();;
	private final CompoundValidator<Point> compoundValidator = new CompoundValidator<Point>();
	private final ValidationCache validationCache;

	private IMapContext mapContext;
	private Point value;
	private Point lastUnmodifiedValue;
	private boolean editable;

	PointInputControl(final Object parentUiReference, final IPointInputControlBlueprint descriptor) {
		super(Toolkit.getWidgetFactory().create(
				parentUiReference,
				Toolkit.getBluePrintFactory().bluePrint(IMapWidgetBlueprint.class)));

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

		setValue(descriptor.getValue());
		setEditable(descriptor.isEditable());

		final IValidator<Point> validator = descriptor.getValidator();
		if (validator != null) {
			addValidator(validator);
		}

		validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				return compoundValidator.validate(getValue());
			}
		});

		resetModificationState();
		validationCache.setDirty();

		((IMap) getWidget()).initialize(new IAvailableCallback() {
			@Override
			public void onAvailable(final IMapContext mapContext) {
				PointInputControl.this.mapContext = mapContext;
				updateValue(true);
				setEditable(editable);
			}
		});
	}

	@Override
	public void setValue(final Point value) {
		setValue(value, true);
	}

	private void setValue(final Point value, final boolean flyTo) {
		if (!NullCompatibleEquivalence.equals(this.value, value)) {
			this.value = value;
			inputObservable.fireInputChanged();
			validationCache.setDirty();
			updateValue(flyTo);
		}
	}

	private void updateValue(final boolean flyTo) {
		if (mapContext != null) {
			mapContext.removeFeature(PLACEMARK_ID);
			if (value != null) {
				final Placemark placemark = KmlFactory.createPlacemark();
				placemark.setId(PLACEMARK_ID);
				placemark.setGeometry(value);
				mapContext.addFeature(placemark);
				if (flyTo) {
					mapContext.flyTo(PLACEMARK_ID, 250000);
				}
			}
		}
	}

	@Override
	public Point getValue() {
		return value;
	}

	@Override
	public void setEditable(final boolean editable) {
		if (mapContext != null) {
			if (editable && !mapContext.isDesignationRunning()) {
				mapContext.startDesignation(Point.class, new IDesignationListener<Point>() {
					@Override
					public void onDesignation(final Point object) {
						setValue(object, false);
					}
				});
			}
			else if (!editable && mapContext.isDesignationRunning()) {
				mapContext.endDesignation();
			}
		}
		this.editable = editable;
	}

	@Override
	public IValidationResult validate() {
		return validationCache.validate();
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.addValidationConditionListener(listener);
	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.removeValidationConditionListener(listener);
	}

	@Override
	public void addValidator(final IValidator<Point> validator) {
		compoundValidator.addValidator(validator);
	}

	@Override
	public boolean hasModifications() {
		return !NullCompatibleEquivalence.equals(lastUnmodifiedValue, getValue());
	}

	@Override
	public void resetModificationState() {
		lastUnmodifiedValue = value;
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

}

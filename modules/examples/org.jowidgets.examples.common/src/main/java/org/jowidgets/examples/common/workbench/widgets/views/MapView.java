/*
 * Copyright (c) 2012, H. Westphal, grossmann
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

package org.jowidgets.examples.common.workbench.widgets.views;

import org.jowidgets.addons.map.common.IAvailableCallback;
import org.jowidgets.addons.map.common.IDesignationListener;
import org.jowidgets.addons.map.common.IMapContext;
import org.jowidgets.addons.map.common.widget.IMapWidget;
import org.jowidgets.addons.map.common.widget.IMapWidgetBlueprint;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;

public class MapView extends AbstractHowToView implements IView {

	public static final String ID = MapView.class.getName();
	public static final String DEFAULT_LABEL = "Map";

	public MapView(final IViewContext context) {
		super(context);
	}

	@Override
	public void createViewContent(final IContainer container, final IBluePrintFactory bpFactory) {
		//set the layout
		container.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));

		final IMapWidget map = container.add(
				Toolkit.getBluePrintFactory().bluePrint(IMapWidgetBlueprint.class),
				"growx, growy, w 0::, h 0::");
		map.setLanguage("en");
		map.initialize(new IAvailableCallback() {
			@Override
			public void onAvailable(final IMapContext mapContext) {
				final Placemark placemark = KmlFactory.createPlacemark();
				placemark.setId("placemark1");
				placemark.setName("A Placemarker!");
				placemark.setDescription("This is the placemarker description");
				final Point point = placemark.createAndSetPoint();
				point.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);
				point.addToCoordinates(9, 51);
				mapContext.addFeature(placemark);
				mapContext.flyTo("placemark1", 500000);

				final int[] count = {0};
				mapContext.startDesignation(Point.class, new IDesignationListener<Point>() {
					@Override
					public void onDesignation(final Point point) {
						final Placemark placemark = KmlFactory.createPlacemark();
						placemark.setName("No. " + ++count[0]);
						placemark.setGeometry(point);
						mapContext.addFeature(placemark);
						if (count[0] == 10) {
							mapContext.endDesignation();
						}
					}
				});
			}
		});

	}
}

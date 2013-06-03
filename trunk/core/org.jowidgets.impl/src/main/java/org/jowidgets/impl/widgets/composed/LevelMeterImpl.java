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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.api.graphics.IPaintListener;
import org.jowidgets.api.model.levelmeter.ILevelListener;
import org.jowidgets.api.model.levelmeter.ILevelMeterModel;
import org.jowidgets.api.widgets.ICanvas;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ILevelMeter;
import org.jowidgets.api.widgets.descriptor.setup.ILevelMeterSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;

public final class LevelMeterImpl extends ControlWrapper implements ILevelMeter {

	private static final String UPPER_SCALE_LETTERING = "0db";
	private static final String LOWER_SCALE_LETTERING = "-" + Character.toString('\u221E');
	private static final int GAP_BETWEEN_SCALE_TEXT_AND_SCALE_LINE = 2;
	private static final int LENGTH_OF_LONGER_SCALE_LINE = 7;
	private static final int LENGTH_OF_SMALLER_SCALE_LINE = 3;
	private static final int GAP_BETWEEN_SCALE_AND_BAR = 7;
	private static final int UPPER_GAP_TO_CANVAS = 0;
	private static final int LOWER_GAP_TO_CANVAS = 10;

	private final ILevelMeterModel model;

	private final ILevelMeterSetup setup;

	public LevelMeterImpl(final IComposite composite, final ILevelMeterSetup setup) {
		super(composite);

		this.model = setup.getModel();
		this.setup = setup;

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		composite.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final ICanvas canvas = composite.add(BPF.canvas(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		model.addLevelListener(new ILevelListener() {
			@Override
			public void levelChanged(final double oldValue, final double newValue) {
				canvas.redraw();
			}
		});

		canvas.addPaintListener(new IPaintListener() {
			@Override
			public void paint(final IGraphicContext gc) {
				paintCanvas(gc);
			}
		});

	}

	@Override
	public ILevelMeterModel getModel() {
		return model;
	}

	private void paintCanvas(final IGraphicContext gc) {
		final int gapSize = setup.getGapSize();
		final int boxSize = setup.getBoxSize();

		final int canvasHeight = gc.getBounds().getHeight();
		final int canvasWidth = gc.getBounds().getWidth();
		final int baseline = canvasHeight - LOWER_GAP_TO_CANVAS;
		final int overallBoxSize = canvasHeight - UPPER_GAP_TO_CANVAS - LOWER_GAP_TO_CANVAS;
		//clear canvas
		gc.setForegroundColor(Colors.DISABLED);
		gc.clearRectangle(0, 0, canvasWidth, canvasHeight);

		//calculate box data depending on canvas height
		final int numberOfBoxes = calculateNumberOfBoxes(overallBoxSize, boxSize, gapSize);
		final int numberOfActiveBoxes = (int) Math.round(numberOfBoxes * model.getLevel());
		final int numberOfLowPeakBoxes = (int) Math.round(numberOfBoxes * setup.getHighPeakThreshold());
		final int numberOfHighPeakBoxes = (int) Math.round(numberOfBoxes * setup.getClipPeakThreshold());

		int letteringWidth = 0;

		//draw scale of configured to do so
		if (setup.isLetteringVisible()) {
			final int heightOfOneScaleSector = overallBoxSize / 10;
			final int lengthOfLongestScaleLettering = gc.getTextWidth(UPPER_SCALE_LETTERING);
			letteringWidth = lengthOfLongestScaleLettering
				+ GAP_BETWEEN_SCALE_TEXT_AND_SCALE_LINE
				+ LENGTH_OF_LONGER_SCALE_LINE
				+ GAP_BETWEEN_SCALE_AND_BAR;
			for (int i = 0; i < 11; i++) {
				if (i == 0 || i == 5 || i == 10) {
					gc.setLineWidth(2);
					gc.drawLine(lengthOfLongestScaleLettering + GAP_BETWEEN_SCALE_TEXT_AND_SCALE_LINE, baseline
						- 1
						- (i * heightOfOneScaleSector), lengthOfLongestScaleLettering
						+ GAP_BETWEEN_SCALE_TEXT_AND_SCALE_LINE
						+ LENGTH_OF_LONGER_SCALE_LINE, baseline - 1 - (i * heightOfOneScaleSector));
				}
				else {
					gc.setLineWidth(1);
					gc.drawLine(lengthOfLongestScaleLettering + GAP_BETWEEN_SCALE_TEXT_AND_SCALE_LINE + 2, baseline
						- 1
						- (i * heightOfOneScaleSector), lengthOfLongestScaleLettering
						+ GAP_BETWEEN_SCALE_TEXT_AND_SCALE_LINE
						+ 2
						+ LENGTH_OF_SMALLER_SCALE_LINE, baseline - 1 - (i * heightOfOneScaleSector));
				}
				if (i == 0) {
					final int fontHeight = gc.getFontMetrics().getHeight();
					gc.drawText(LOWER_SCALE_LETTERING, 0, baseline - fontHeight / 2 - 2 - (i * heightOfOneScaleSector));
				}
				if (i == 10) {
					final int fontHeight = gc.getFontMetrics().getHeight();
					gc.drawText(UPPER_SCALE_LETTERING, 0, baseline - fontHeight / 2 - 2 - (i * heightOfOneScaleSector));
				}
			}
		}

		IColorConstant boxColor = setup.getLowPeakColor();

		//draw boxes
		for (int i = 0; i < numberOfBoxes; i++) {
			if (i + 1 > numberOfLowPeakBoxes) {
				boxColor = setup.getHighPeakColor();
			}
			if (i + 1 > numberOfHighPeakBoxes) {
				boxColor = setup.getClipPeakColor();
			}
			if (i + 1 > numberOfActiveBoxes) {
				boxColor = setup.getBaseBoxColor();
			}
			final int y = baseline - (gapSize + boxSize) - i * (gapSize + boxSize);
			gc.setForegroundColor(gc.getBackgroundColor());
			gc.fillRectangle(letteringWidth, y, canvasWidth, gapSize);
			gc.setForegroundColor(boxColor);
			gc.fillRectangle(letteringWidth, y + gapSize, canvasWidth, boxSize);
		}

	}

	private int calculateNumberOfBoxes(final int canvasHeight, final int boxSize, final int gapSize) {
		return canvasHeight / (boxSize + gapSize) - 1;
	}
}

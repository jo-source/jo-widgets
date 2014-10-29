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
package org.jowidgets.examples.common.image;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jowidgets.api.animation.AnimationRunner;
import org.jowidgets.api.animation.IAnimationRunner;
import org.jowidgets.api.color.Colors;
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.api.graphics.IPaintListener;
import org.jowidgets.api.image.IBufferedImage;
import org.jowidgets.api.image.IImage;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ICanvas;
import org.jowidgets.api.widgets.ICheckBox;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.ISlider;
import org.jowidgets.api.widgets.blueprint.ISliderBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.ICallback;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.Tuple;
import org.jowidgets.util.ValueHolder;
import org.jowidgets.util.io.IoUtils;

public final class ImageAnimationDemo1 implements IApplication {

	private static final Random RANDOM = new Random();

	private static final String ENVELOPE_1_RESOURCE_NAME = "envelopes/envelope1.tupleArray";
	private static final String ENVELOPE_2_RESOURCE_NAME = "envelopes/envelope2.tupleArray";

	private static final int IMAGE_HEIGHT = 120;
	private static final int IMAGE_WIDTH = 28699;
	private static final int BORDER_SIZE = 5;

	private static final int CANVAS_COUNT = 3;

	private static final int SLIDER_MAX = 10000;

	private static final int DEFAULT_ANIMATION_STEP_SIZE = 1;
	private static final int DEFAULT_ANIMATION_DELAY = 20;

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create containers
		final ValueHolder<Boolean> changed = new ValueHolder<Boolean>(false);
		final ValueHolder<Double> scaleFactor = new ValueHolder<Double>(1.0d);
		final ValueHolder<Integer> offset = new ValueHolder<Integer>(0);
		final ValueHolder<Integer> stepSize = new ValueHolder<Integer>(DEFAULT_ANIMATION_STEP_SIZE);
		final ValueHolder<Integer> animationDelay = new ValueHolder<Integer>(DEFAULT_ANIMATION_DELAY);

		//Create root frame
		final IFrame rootFrame = Toolkit.createRootFrame(
				BPF.frame("Image animation demo 1").setMinPackSize(new Dimension(1024, 200)),
				lifecycle);
		rootFrame.setLayout(MigLayoutFactory.growingInnerCellLayout());

		//create scroll composite
		final IScrollComposite container = rootFrame.add(BPF.scrollComposite(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		container.setLayout(new MigLayoutDescriptor("wrap", "[grow, 0::]", "[120!]" + createCanvasRowLayout()));

		//Create control's container
		final IContainer controlContainer = container.add(BPF.composite(), "growx, growy, w 0::, h 0::");
		controlContainer.setLayout(new MigLayoutDescriptor("wrap", "[]14[]14[]14[]14[]", "0[]0[grow, 0::]0"));

		//Create controls header
		controlContainer.add(BPF.textLabel("Scale"));
		final ICheckBox autoOffsetCb = controlContainer.add(BPF.checkBox().setText("Offset (auto)"), "aligny b");
		autoOffsetCb.setSelected(false);
		controlContainer.add(BPF.textLabel("Step size"));
		controlContainer.add(BPF.textLabel("Animation delay"));
		final ICheckBox scrollingCb = controlContainer.add(BPF.checkBox().setText("Scrolling"), "aligny b");
		scrollingCb.setSelected(false);

		//Create scale slider
		final ISliderBluePrint scaleSliderBp = BPF.slider().setMinimum(0).setMaximum(SLIDER_MAX).setVertical();
		scaleSliderBp.setTickSpacing(SLIDER_MAX / 10);
		final ISlider scaleSlider = controlContainer.add(scaleSliderBp, "growy, h 0::");
		scaleSlider.setSelection(0);
		scaleSlider.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				final int value = scaleSlider.getSelection();
				scaleFactor.set(new Double(0.1 + (double) (value) / (SLIDER_MAX / 20)));
				//CHECKSTYLE:OFF
				System.out.println(scaleFactor.get());
				//CHECKSTYLE:ON
				changed.set(true);
			}
		});

		//Create offset slider
		final ISliderBluePrint offsetSliderBp = BPF.slider().setMinimum(0).setMaximum(SLIDER_MAX).setVertical();
		offsetSliderBp.setTickSpacing(SLIDER_MAX / 10);
		final ISlider offsetSlider = controlContainer.add(offsetSliderBp, "growy, h 0::");
		offsetSlider.setSelection(0);
		offsetSlider.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				final int value = offsetSlider.getSelection();
				offset.set(new Integer(value / 10));
				changed.set(true);
			}
		});

		//Create step size slider
		final ISliderBluePrint stepSizeSliderBp = BPF.slider().setMinimum(0).setMaximum(SLIDER_MAX).setVertical();
		stepSizeSliderBp.setTickSpacing(SLIDER_MAX / 10);
		final ISlider stepSizeSlider = controlContainer.add(stepSizeSliderBp, "growy, h 0::");
		stepSizeSlider.setSelection(1);
		stepSizeSlider.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				final int value = stepSizeSlider.getSelection();
				stepSize.set(new Integer(1 + (value / 100)));
				//CHECKSTYLE:OFF
				System.out.println(stepSize.get());
				//CHECKSTYLE:ON
			}
		});

		//Create animation delay slider
		final ISliderBluePrint animationDelaySliderBp = BPF.slider().setMinimum(0).setMaximum(SLIDER_MAX).setVertical();
		animationDelaySliderBp.setTickSpacing(SLIDER_MAX / 10);
		final ISlider animationDelaySlider = controlContainer.add(animationDelaySliderBp, "growy, h 0::");
		animationDelaySlider.setSelection(1);
		animationDelaySlider.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				final int value = animationDelaySlider.getSelection();
				animationDelay.set(new Integer(20 + (value / 50)));
				//CHECKSTYLE:OFF
				System.out.println(animationDelay.get());
				//CHECKSTYLE:ON
			}
		});

		//create canvas list
		final List<ICanvas> canvasList = new ArrayList<ICanvas>(CANVAS_COUNT);
		for (int i = 0; i < CANVAS_COUNT - 2; i++) {
			canvasList.add(createCanvas(container, createImage(null), scaleFactor, offset, scrollingCb, false));
		}
		if (CANVAS_COUNT > 1) {
			canvasList.add(createCanvas(container, createImage(ENVELOPE_2_RESOURCE_NAME), scaleFactor, offset, scrollingCb, false));
		}
		if (CANVAS_COUNT > 0) {
			canvasList.add(createCanvas(container, createImage(ENVELOPE_1_RESOURCE_NAME), scaleFactor, offset, scrollingCb, true));
		}

		rootFrame.setVisible(true);

		final Animation animation = new Animation(
			offsetSlider,
			autoOffsetCb,
			canvasList,
			stepSize,
			animationDelay,
			scrollingCb,
			changed);
		animation.start();

		rootFrame.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				animation.stop();
			}
		});
	}

	private static String createCanvasRowLayout() {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < CANVAS_COUNT; i++) {
			result.append("[]0");
		}
		return result.substring(0, result.lastIndexOf("0"));
	}

	private ICanvas createCanvas(
		final IContainer parent,
		final IImage image,
		final ValueHolder<Double> scaleFactor,
		final ValueHolder<Integer> offset,
		final ICheckBox scrollingCb,
		final boolean buttomBorder) {
		final ICanvas result = parent.add(BPF.canvas(), "growx, w 0::, h " + IMAGE_HEIGHT + "!");
		result.addPaintListener(new IPaintListener() {

			private Integer lastOffset = null;
			private Double lastScaleFactor = null;

			@Override
			public void paint(final IGraphicContext gc) {

				if (!scrollingCb.isSelected()
					|| !NullCompatibleEquivalence.equals(scaleFactor.get(), lastScaleFactor)
					|| !NullCompatibleEquivalence.equals(offset.get(), lastOffset)) {

					final long currentTimeMillis = System.currentTimeMillis();

					final int width = gc.getBounds().getWidth();
					final int height = gc.getBounds().getHeight();

					gc.clearRectangle(0, 0, width, height);

					//draw the envelope image
					try {
						final Dimension size = image.getSize();
						final Double currentScaleFactor = scaleFactor.get();
						final Integer currentOffset = offset.get();
						gc.drawImage(
								image,
								0,
								0,
								size.getWidth(),
								size.getHeight(),
								currentOffset,
								0,
								(int) (size.getWidth() / currentScaleFactor),
								height);
					}
					catch (final Exception e) {
						//TODO ignore for now
					}

					//draw the borders
					gc.setForegroundColor(Colors.DISABLED);
					gc.fillRectangle(0, 0, 1, height);
					gc.fillRectangle(0, 0, width - 1, 1);
					gc.fillRectangle(width - 1, 0, 1, height);
					if (buttomBorder) {
						gc.fillRectangle(0, height - 1, width - 1, 1);
					}

					//draw the marker
					gc.setForegroundColor(Colors.ERROR);
					gc.fillRectangle(width / 2, 0, 2, height);

					lastOffset = offset.get();
					lastScaleFactor = scaleFactor.get();
					//CHECKSTYLE:OFF
					System.out.println("Render time millis: " + (System.currentTimeMillis() - currentTimeMillis));
					//CHECKSTYLE:ON
				}
			}
		});
		return result;
	}

	private IImage createImage(final String resourceName) {
		final IBufferedImage image = Toolkit.getImageFactory().createBufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT);
		final IGraphicContext gc = image.getGraphicContext();

		gc.setBackgroundColor(Colors.WHITE);
		gc.clearRectangle(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		gc.setForegroundColor(new ColorValue(0, 0, 98));

		final int innerHeight = IMAGE_HEIGHT - (2 * BORDER_SIZE);
		final int baseLine = IMAGE_HEIGHT - BORDER_SIZE;
		final double scale = innerHeight / 2;

		double minValue = -1.0d;
		double maxValue = 1.0d;
		try {
			ObjectInputStream ois = null;
			if (resourceName != null) {
				final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
				ois = new ObjectInputStream(inputStream);
			}

			for (int i = 0; i < IMAGE_WIDTH; i++) {
				if (ois != null) {
					try {
						@SuppressWarnings("unchecked")
						final Tuple<Double, Double> tuple = (Tuple<Double, Double>) ois.readObject();
						minValue = tuple.getFirst();
						maxValue = tuple.getSecond();
					}
					catch (final Exception e) {
						IoUtils.tryCloseSilent(ois);
						ois = null;
					}
				}
				else {
					minValue = -1.0d * RANDOM.nextDouble();
					maxValue = RANDOM.nextDouble();
				}

				final int maxY = (int) (baseLine - ((maxValue + 1.0) * scale));
				final int minY = (int) (baseLine - ((minValue + 1.0) * scale));
				if (minY != maxY) {
					gc.fillRectangle(i, maxY, 1, Math.abs(maxY - minY));
				}
				else {
					gc.drawPoint(i, minY);
				}
			}
			IoUtils.tryCloseSilent(ois);
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}

		return image;
	}

	private final class Animation implements Runnable, ICallback<Void> {

		private final List<ICanvas> canvasList;
		private final ValueHolder<Integer> stepSize;
		private final ValueHolder<Integer> animationDelay;
		private final ValueHolder<Boolean> changed;
		private final ISlider offsetSlider;
		private final ICheckBox scrollingCb;
		private final ICheckBox autoOffsetCb;

		private final IAnimationRunner animationRunner;

		private int animationStep;

		private Animation(
			final ISlider offsetSlider,
			final ICheckBox autoOffsetCb,
			final List<ICanvas> canvasList,
			final ValueHolder<Integer> stepSize,
			final ValueHolder<Integer> animationDelay,
			final ICheckBox scrollingCb,
			final ValueHolder<Boolean> changed) {
			this.offsetSlider = offsetSlider;
			this.autoOffsetCb = autoOffsetCb;
			this.scrollingCb = scrollingCb;
			this.canvasList = canvasList;
			this.stepSize = stepSize;
			this.animationDelay = animationDelay;
			this.changed = changed;
			this.animationStep = DEFAULT_ANIMATION_STEP_SIZE;
			this.animationRunner = AnimationRunner.builder().setDelay(DEFAULT_ANIMATION_DELAY).build();

			animationRunner.run(this, this);
		}

		public void stop() {
			animationRunner.stop();
		}

		private void start() {
			animationRunner.start();
		}

		@Override
		public void run() {

			animationStep = stepSize.get();
			animationRunner.setDelay(animationDelay.get());

			if (autoOffsetCb.isSelected()) {
				final int selection = offsetSlider.getSelection();

				if (selection + animationStep > SLIDER_MAX) {
					offsetSlider.setSelection(0);
				}
				else {
					offsetSlider.setSelection(selection + animationStep);
				}
			}

			if (changed.get()) {
				for (final ICanvas canvas : canvasList) {
					canvas.redraw();
				}
				changed.set(false);
			}

			if (scrollingCb.isSelected()) {
				final long currentTime = System.currentTimeMillis();
				for (final ICanvas canvas : canvasList) {
					final Dimension size = canvas.getSize();
					canvas.scroll(0, 0, size.getWidth() - animationStep, size.getHeight(), animationStep, 0);
				}
				//CHECKSTYLE:OFF
				System.out.println("SCROLL TIME: " + (System.currentTimeMillis() - currentTime));
				//CHECKSTYLE:ON
			}

		}

		@Override
		public void call(final Void parameter) {
			animationRunner.run(this, this);
		}

	}
}

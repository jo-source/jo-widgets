/*
 * Copyright (c) 2010, grossmann
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

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.validation.ValidationResult;

//CHECKSTYLE:OFF
public class DemoListInputFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	@SuppressWarnings("unchecked")
	public DemoListInputFrame() {
		super("Text area demo");

		setLayout(Toolkit.getLayoutFactoryProvider().fillLayout());
		final IScrollComposite scrollComposite = add(BPF.scrollComposite(), "");

		scrollComposite.setLayout(new MigLayoutDescriptor("wrap", "[][]0[grow, 0::][20!]", "[]10[]0[]0[]0[]0[]0[]0[]0[]"));

		scrollComposite.add(BPF.validationResultLabel(), "span 4").setResult(
				ValidationResult.create().withError("Entry 4: Must be a propper value"));

		for (int i = 0; i < 8; i++) {

			final String userIndex = "" + (i + 1);

			scrollComposite.add(BPF.textLabel(userIndex));

			scrollComposite.add(BPF.button().setIcon(IconsSmall.SUB).setToolTipText("Remove entry " + userIndex), "w 21!, h 21!");

			scrollComposite.add(getControlBp(), "grow, w 0::");

			if (i == 3 || i == 6) {
				scrollComposite.add(BPF.validationResultLabel().setShowValidationMessage(false)).setResult(
						ValidationResult.create().withError("Must be a propper value"));
			}
			else {
				scrollComposite.add(BPF.composite(), "w 0!, h 0!");
			}

		}

		scrollComposite.add(BPF.textLabel("" + 9).setForegroundColor(Colors.DISABLED));

		scrollComposite.add(BPF.button().setIcon(IconsSmall.ADD).setToolTipText("Add new entry"), "w 21!, h 21!");

		//add(BPF.button().setIcon(IconsSmall.ADD), "");
	}

	//	private IWidgetDescriptor getControlBp() {
	//		return BPF.comboBoxSelection("Germany", "Spain", "Italy", "USA");
	//	}

	@SuppressWarnings("rawtypes")
	private IWidgetDescriptor getControlBp() {
		return BPF.comboBox("Germany", "Spain", "Italy", "USA", "Frankreich", "Portugal", "Bayern");
	}

	//	private IWidgetDescriptor getControlBp() {
	//		return BPF.inputFieldDate();
	//	}

	//	private IWidgetDescriptor getControlBp() {
	//		return BPF.inputFieldIntegerNumber();
	//	}

}

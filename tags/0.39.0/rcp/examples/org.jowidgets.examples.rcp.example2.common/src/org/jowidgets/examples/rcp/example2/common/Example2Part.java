/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.examples.rcp.example2.common;

import javax.annotation.PostConstruct;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.IProvider;

public final class Example2Part {

	@PostConstruct
	public void createComposite(final IProvider<IComposite> compositeProvider) {
		
		final IComposite composite = compositeProvider.get();

		composite.setLayout(new MigLayoutDescriptor("wrap", "[][grow]", "[]"));

		final ITextLabelBluePrint labelBp = BPF.textLabel().alignRight();

		final String labelCC = "alignx r";
		final String fieldCC = "growx, w 0::";

		composite.add(labelBp.setText("Name"), labelCC);
		composite.add(BPF.inputFieldString(), fieldCC);

		composite.add(labelBp.setText("Lastname"), labelCC);
		composite.add(BPF.inputFieldString(), fieldCC);

		composite.add(labelBp.setText("Day of birth"), labelCC);
		composite.add(BPF.inputFieldDate(), fieldCC);

		composite.add(labelBp.setText("Civil status"), labelCC);
		composite.add(BPF.comboBoxSelection(CivilStatus.values()), fieldCC);

		composite.add(labelBp.setText("Number of children"), labelCC);
		composite.add(BPF.inputFieldIntegerNumber(), fieldCC);
	}

}

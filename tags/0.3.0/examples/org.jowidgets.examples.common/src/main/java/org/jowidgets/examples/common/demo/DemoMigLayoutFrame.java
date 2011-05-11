/*
 * Copyright (c) 2011, Nikolaus Moll
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

import org.jowidgets.api.layout.ILayoutFactoryProvider;
import org.jowidgets.api.layout.miglayout.IMigLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.tools.powo.JoFrame;

public class DemoMigLayoutFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	public DemoMigLayoutFrame() {
		super("MigLayout demo");

		final ILayoutFactoryProvider lfp = Toolkit.getLayoutFactoryProvider();

		final IMigLayout layout = setLayout(lfp.migLayoutBuilder().columnConstraints("[]15[grow,fill]15[grow]").build());

		add(BPF.textLabel("Last Name"), "");
		add(BPF.textField(), "");
		add(BPF.textLabel("First Name"), "split"); // split divides the cell
		add(BPF.textField(), "growx, wrap");
		add(BPF.textLabel("Phone"), "");
		add(BPF.textField(), "");
		add(BPF.textLabel("Email"), "split");
		add(BPF.textField(), "growx, wrap");
		add(BPF.textLabel("Address 1"), "");
		add(BPF.textField(), "span, growx"); // span merges cells
		add(BPF.textLabel("Address 2"), "");
		add(BPF.textField(), "span, growx");
		add(BPF.textLabel("City"), "");
		add(BPF.textField(), "wrap"); // wrap continues on next line
		add(BPF.textLabel("State"), "");
		add(BPF.textField(), "");
		add(BPF.textLabel("Postal Code"), "split");
		add(BPF.textField(), "growx, wrap");
		add(BPF.textLabel("Country"), "");
		add(BPF.textField(), "wrap 15");

		add(BPF.button("New"), "span, split, align right");
		add(BPF.button("Delete"), "");
		add(BPF.button("Edit"), "");
		add(BPF.button("Save"), "");
		add(BPF.button("Cancel"), "wrap push");

		setSize(800, 600);
		setMinSize(computeDecoratedSize(layout.getMinSize()));
	}
}

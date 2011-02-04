/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.test.api.widgets.blueprint.factory;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.jowidgets.test.api.widgets.blueprint.IButtonBluePrintUi;

public class TestBluePrintFactory {

	private static ITestBluePrintFactory bluePrintFactory;

	private TestBluePrintFactory() {}

	public static synchronized ITestBluePrintFactory getInstance() {
		if (bluePrintFactory == null) {
			final ServiceLoader<ITestBluePrintFactory> bPFLoader = ServiceLoader.load(ITestBluePrintFactory.class);
			final Iterator<ITestBluePrintFactory> iterator = bPFLoader.iterator();

			if (!iterator.hasNext()) {
				throw new IllegalStateException("No implementation found for '" + ITestBluePrintFactory.class.getName() + "'");
			}

			bluePrintFactory = iterator.next();

			if (iterator.hasNext()) {
				throw new IllegalStateException("More than one implementation found for '"
					+ ITestBluePrintFactory.class.getName()
					+ "'");
			}
		}
		return bluePrintFactory;
	}

	public static IButtonBluePrintUi button() {
		return getInstance().button();
	}
}

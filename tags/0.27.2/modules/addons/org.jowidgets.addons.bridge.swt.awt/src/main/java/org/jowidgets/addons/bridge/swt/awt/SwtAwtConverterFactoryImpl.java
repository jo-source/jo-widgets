/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.addons.bridge.swt.awt;

import java.awt.Container;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IConverter;

final class SwtAwtConverterFactoryImpl implements ISwtAwtConverterFactory {

	@Override
	public IConverter<IComposite, Container> createCompositeConverter() {
		return new IConverter<IComposite, Container>() {
			@Override
			public Container convert(final IComposite source) {
				Assert.paramNotNull(source, "source");
				Assert.paramHasType(source.getUiReference(), Composite.class, "source.getUiReference()");
				source.setLayout(MigLayoutFactory.growingInnerCellLayout());
				final ISwtAwtControl result;
				result = SwtAwtControlFactory.getInstance().createSwtAwtControl(source.getUiReference());
				result.setLayoutConstraints(MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
				return result.getAwtContainer();
			}
		};
	}

}

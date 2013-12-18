/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.tools.content;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

public class SingleControlContent<INPUT_TYPE> implements IInputContentCreator<INPUT_TYPE> {

	private final IInputContentCreator<INPUT_TYPE> contentCreator;

	public SingleControlContent(
		final String label,
		final IWidgetDescriptor<? extends IInputControl<INPUT_TYPE>> descriptor,
		final int fieldMinWidth) {
		contentCreator = Toolkit.getInputContentCreatorFactory().singleControlContent(label, descriptor, fieldMinWidth);
	}

	public SingleControlContent(final String label, final IWidgetDescriptor<? extends IInputControl<INPUT_TYPE>> descriptor) {
		contentCreator = Toolkit.getInputContentCreatorFactory().singleControlContent(label, descriptor);
	}

	@Override
	public void createContent(final IInputContentContainer contentContainer) {
		contentCreator.createContent(contentContainer);
	}

	@Override
	public void setValue(final INPUT_TYPE value) {
		contentCreator.setValue(value);
	}

	@Override
	public INPUT_TYPE getValue() {
		return contentCreator.getValue();
	}

}

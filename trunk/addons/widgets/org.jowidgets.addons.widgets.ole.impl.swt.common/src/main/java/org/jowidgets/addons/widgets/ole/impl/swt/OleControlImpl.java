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

package org.jowidgets.addons.widgets.ole.impl.swt;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.addons.widgets.ole.api.IOleControlSetupBuilder;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.spi.impl.controller.FocusObservable;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;
import org.jowidgets.util.MutableValue;

class OleControlImpl extends ControlWrapper implements IOleControl {

	private final MutableValue<IOleContext> context;
	private final FocusObservable focusObservable;
	private final FocusListenerDelegate focusListenerDelegate;

	OleControlImpl(
		final IControl control,
		final IMutableValue<Composite> swtCompositeValue,
		final IOleControlSetupBuilder<?> setup) {
		super(control);

		this.context = new MutableValue<IOleContext>();
		this.focusObservable = new FocusObservable();
		this.focusListenerDelegate = new FocusListenerDelegate();

		swtCompositeValue.addMutableValueListener(new IMutableValueListener<Composite>() {
			@Override
			public void changed(final IValueChangedEvent<Composite> event) {
				swtCompositeChanged(event.getSource());
			}
		});

		swtCompositeChanged(swtCompositeValue);
	}

	private void swtCompositeChanged(final IMutableValue<Composite> swtCompositeValue) {
		if (swtCompositeValue.getValue() != null) {
			final OleContextImpl oleContext = new OleContextImpl(swtCompositeValue.getValue());
			oleContext.addFocusListener(focusListenerDelegate);
			context.setValue(oleContext);
		}
		else {
			context.setValue(null);
		}
	}

	@Override
	public IMutableValue<IOleContext> getContext() {
		return context;
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		focusObservable.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		focusObservable.removeFocusListener(listener);
	}

	private final class FocusListenerDelegate implements IFocusListener {
		@Override
		public void focusLost() {
			focusObservable.focusLost();
		}

		@Override
		public void focusGained() {
			focusObservable.focusGained();
		}
	};

}

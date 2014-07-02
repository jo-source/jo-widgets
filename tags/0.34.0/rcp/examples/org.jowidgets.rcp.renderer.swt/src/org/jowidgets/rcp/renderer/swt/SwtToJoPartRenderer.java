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

package org.jowidgets.rcp.renderer.swt;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.spi.impl.swt.addons.SwtToJo;
import org.jowidgets.util.IProvider;

@SuppressWarnings("restriction")
public class SwtToJoPartRenderer extends ContributedPartRenderer {

	@Override
	public Object createWidget(final MUIElement element, final Object parent) {
		if (!(element instanceof MPart) || !(parent instanceof Composite)) {
			return null;
		}

		final Composite parentComposite = (Composite) parent;
		final MPart part = (MPart) element;

		//Create a part composite that is also a IProvider<IComposite>
		//Unfortunately an IComposite can not be implemented by ContributedPartComposite because of some method naming clashes
		//Unfortunately an IComposite can not be returned because some the super renderer and the StackPartRenderer assumes that
		//SWT Controls will be created
		final ContributedPartComposite result = new ContributedPartComposite(part, parentComposite, SWT.NONE);
		final IComposite joComposite = SwtToJo.create(result);
		result.setJoComposite(joComposite);

		//bind the widget
		bindWidget(element, result);

		// Create a context for this part
		final IEclipseContext context = part.getContext();
		context.set(IProvider.class.getName(), result);

		//Register at the contribution factory
		final IContributionFactory contributionFactory = (IContributionFactory) context.get(IContributionFactory.class.getName());
		final Object newPart = contributionFactory.create(part.getContributionURI(), context);
		part.setObject(newPart);

		return result;
	}

	private final class ContributedPartComposite extends Composite implements IProvider<IComposite> {

		private final MPart part;

		private IComposite joComposite;
		private boolean onSetFocus = false;

		private ContributedPartComposite(final MPart part, final Composite parent, final int style) {
			super(parent, style);
			this.part = part;
		}

		private void setJoComposite(final IComposite joComposite) {
			this.joComposite = joComposite;
		}

		@Override
		public IComposite get() {
			return joComposite;
		}

		@Override
		public boolean setFocus() {
			if (!onSetFocus) {
				try {
					onSetFocus = true;
					if (part.getObject() != null && isEnabled()) {
						part.getContext().get(IPresentationEngine.class).focusGui(part);
						return true;
					}
					return super.setFocus();
				}
				finally {
					onSetFocus = false;
				}
			}
			else {
				return true;
			}
		}

	}

}

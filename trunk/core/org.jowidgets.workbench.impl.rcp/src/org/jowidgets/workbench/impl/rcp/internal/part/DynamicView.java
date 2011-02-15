/*
 * Copyright (c) 2011, M. Grossmann, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.impl.rcp.internal.part;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.impl.rcp.internal.ImageHelper;
import org.jowidgets.workbench.impl.rcp.internal.ViewContext;

public final class DynamicView extends ViewPart {

	public static final String ID = "org.jowidgets.workbench.impl.rcp.internal.part.dynamicView"; //$NON-NLS-1$

	@Override
	public void createPartControl(final Composite parent) {
		final String viewId = ((IViewSite) getSite()).getSecondaryId();
		final IView view = PartRegistry.getInstance().getView(viewId);
		setPartName(view.getLabel());
		setTitleImage(ImageHelper.getImage(view.getIcon(), null));
		setTitleToolTip(view.getTooltip());

		final IComposite composite = Toolkit.getWidgetWrapperFactory().createComposite(parent);
		final ViewContext viewContext = new ViewContext(composite);
		getViewSite().getPage().addPartListener(new IPartListener2() {
			@Override
			public void partVisible(final IWorkbenchPartReference partRef) {
				final IWorkbenchPart part = partRef.getPart(false);
				if (part == DynamicView.this) {
					view.initialize(viewContext);
					getViewSite().getPage().removePartListener(this);
				}
			}

			@Override
			public void partOpened(final IWorkbenchPartReference partRef) {
				final IWorkbenchPart part = partRef.getPart(false);
				// needed for lazy initializing detached views, since partVisible() is not fired for them apparently
				if (part == DynamicView.this
					&& part.getSite().getShell() != PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()) {
					view.initialize(viewContext);
					getViewSite().getPage().removePartListener(this);
				}
			}

			@Override
			public void partInputChanged(final IWorkbenchPartReference partRef) {}

			@Override
			public void partHidden(final IWorkbenchPartReference partRef) {}

			@Override
			public void partDeactivated(final IWorkbenchPartReference partRef) {}

			@Override
			public void partClosed(final IWorkbenchPartReference partRef) {}

			@Override
			public void partBroughtToTop(final IWorkbenchPartReference partRef) {}

			@Override
			public void partActivated(final IWorkbenchPartReference partRef) {}
		});
	}

	@Override
	public void setFocus() {}

}

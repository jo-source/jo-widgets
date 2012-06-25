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

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.ole.api.IInvocationParameter;
import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.OleCommand;
import org.jowidgets.addons.widgets.ole.api.OleCommandOption;
import org.jowidgets.util.Assert;

class OleContextImpl implements IOleContext {

	private final OleFrame oleFrame;

	private OleControlSite oleControlSiteLazy;
	private OleAutomation oleAutomationLazy;

	OleContextImpl(final Composite swtComposite) {
		swtComposite.setLayout(new FillLayout());
		this.oleFrame = new OleFrame(swtComposite, SWT.NONE);
	}

	@Override
	public Object invoke(final String methodName, final IInvocationParameter... parameters) {
		Assert.paramNotEmpty(methodName, "methodName");
		Assert.paramNotNull(parameters, "parameters");

		final OleAutomation oleAutomation = getOleAutomation();

		final String[] parameterNames = new String[parameters.length];
		final Variant[] variants = new Variant[parameters.length];
		int index = 0;
		for (final IInvocationParameter parameter : parameters) {
			parameterNames[index] = parameter.getParameterName();
			variants[index] = createVariant(parameter.getParameter());
			index++;
		}

		final int[] paramterNamesIds = oleAutomation.getIDsOfNames(parameterNames);
		final int[] methodNameIds = oleAutomation.getIDsOfNames(new String[] {methodName});

		return getVariantResult(oleAutomation.invoke(methodNameIds[0], variants, paramterNamesIds));
	}

	@Override
	public void execute(final OleCommand command, final Object in, final OleCommandOption... options) {
		Assert.paramNotNull(command, "command");

		final OleControlSite controlSite = getOleControlSite();
		final int commandId = getCommandId(command);
		final Variant variantIn = in != null ? createVariant(in) : null;
		controlSite.exec(commandId, getCommandOptions(options), variantIn, null);
	}

	@Override
	public void setDocument(final String progId) {
		setDocument(progId, null);
	}

	@Override
	public void setDocument(final File file) {
		setDocument(null, file);
	}

	@Override
	public void setDocument(final String progId, final File file) {
		if (oleControlSiteLazy != null) {
			oleControlSiteLazy.dispose();
		}
		if (progId != null && file != null) {
			oleControlSiteLazy = new OleControlSite(oleFrame, SWT.NONE, progId, file);
		}
		else if (progId != null) {
			oleControlSiteLazy = new OleControlSite(oleFrame, SWT.NONE, progId);
		}
		else if (file != null) {
			oleControlSiteLazy = new OleControlSite(oleFrame, SWT.NONE, file);
		}
		if (oleControlSiteLazy != null) {
			oleControlSiteLazy.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
		}
	}

	@Override
	public void clearDocument() {
		oleAutomationLazy.dispose();
		getOleControlSite().dispose();
		oleControlSiteLazy = null;
		oleAutomationLazy = null;
	}

	@Override
	public void saveCurrentDocumet(final File file, final boolean includeOleInfo) {
		getOleControlSite().save(file, includeOleInfo);
	}

	@Override
	public boolean isDirty() {
		if (oleControlSiteLazy != null) {
			return oleControlSiteLazy.isDirty();
		}
		return false;
	}

	private OleAutomation getOleAutomation() {
		if (oleAutomationLazy == null) {
			oleAutomationLazy = new OleAutomation(getOleControlSite());
		}
		return oleAutomationLazy;
	}

	private OleControlSite getOleControlSite() {
		if (oleControlSiteLazy == null) {
			throw new IllegalStateException("There is no document set");
		}
		return oleControlSiteLazy;
	}

	private int getCommandOptions(final OleCommandOption... options) {
		int result = 0;
		if (options != null) {
			final Set<OleCommandOption> optionsSet = new HashSet<OleCommandOption>(Arrays.asList(options));
			if (optionsSet.contains(OleCommandOption.DODEFAULT)) {
				result = result | OLE.OLECMDEXECOPT_DODEFAULT;
			}
			if (optionsSet.contains(OleCommandOption.DONTPROMPTUSER)) {
				result = result | OLE.OLECMDEXECOPT_DONTPROMPTUSER;
			}
			if (optionsSet.contains(OleCommandOption.PROMPTUSER)) {
				result = result | OLE.OLECMDEXECOPT_PROMPTUSER;
			}
			if (optionsSet.contains(OleCommandOption.SHOWHELP)) {
				result = result | OLE.OLECMDEXECOPT_SHOWHELP;
			}
		}
		return result;
	}

	private int getCommandId(final OleCommand command) {
		if (command == OleCommand.OPEN) {
			return OLE.OLECMDID_OPEN;
		}
		else if (command == OleCommand.NEW) {
			return OLE.OLECMDID_NEW;
		}
		//TODO WH handle other commands
		else {
			throw new IllegalArgumentException("Command '" + command + "' is not supported.");
		}
	}

	private Object getVariantResult(final Variant variant) {
		//TODO MG get Object result for type
		//if (variant != null) {
		//	short type = variant.getType();			
		//}
		return variant;
	}

	private Variant createVariant(final Object object) {
		if (object instanceof String) {
			return new Variant((String) object);
		}
		else if (object instanceof Integer) {
			return new Variant(((Integer) object).intValue());
		}
		else if (object instanceof Long) {
			return new Variant(((Long) object).longValue());
		}
		else if (object instanceof Double) {
			return new Variant(((Double) object).doubleValue());
		}
		else if (object instanceof Float) {
			return new Variant(((Float) object).floatValue());
		}
		else if (object instanceof Boolean) {
			return new Variant(((Boolean) object).booleanValue());
		}
		//TODO WH support all relevant types of vairant
		else {
			throw new IllegalArgumentException("parameter type '" + object.getClass().getName() + "' is not supported");
		}
	}

}

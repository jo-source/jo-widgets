/*
 * Copyright (c) 2012, grossmann, waheckma
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
import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.internal.ole.win32.IUnknown;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.ole.api.IInvocationParameter;
import org.jowidgets.addons.widgets.ole.api.IOleAutomation;
import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.OleCommand;
import org.jowidgets.addons.widgets.ole.api.OleCommandOption;
import org.jowidgets.util.Assert;

class OleContextImpl implements IOleContext {

	private final OleFrame oleFrame;

	private OleControlSite oleControlSiteLazy;

	private OleAutomationImpl oleAutomationLazy;

	OleContextImpl(final Composite swtComposite) {
		swtComposite.setLayout(new FillLayout());
		this.oleFrame = new OleFrame(swtComposite, SWT.NONE);
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
		if (oleAutomationLazy != null) {
			oleAutomationLazy.dispose();
		}
		getOleControlSite().dispose();
		oleControlSiteLazy = null;
		oleAutomationLazy = null;
	}

	@Override
	public boolean saveCurrentDocument(final File file, final boolean includeOleInfo) {
		return getOleControlSite().save(file, includeOleInfo);
	}

	@Override
	public boolean isDirty() {
		if (oleControlSiteLazy != null) {
			return oleControlSiteLazy.isDirty();
		}
		return false;
	}

	@Override
	public IOleAutomation getAutomation() {
		if (oleAutomationLazy == null) {
			oleAutomationLazy = new OleAutomationImpl(new OleAutomation(getOleControlSite()));
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
		else if (command == OleCommand.SAVE) {
			return OLE.OLECMDID_SAVE;
		}
		else if (command == OleCommand.SAVEAS) {
			return OLE.OLECMDID_SAVEAS;
		}
		else if (command == OleCommand.SAVECOPYAS) {
			return OLE.OLECMDID_SAVECOPYAS;
		}
		else if (command == OleCommand.PRINT) {
			return OLE.OLECMDID_PRINT;
		}
		else if (command == OleCommand.PRINTPREVIEW) {
			return OLE.OLECMDID_PRINTPREVIEW;
		}
		else if (command == OleCommand.PAGESETUP) {
			return OLE.OLECMDID_PAGESETUP;
		}
		else if (command == OleCommand.SPELL) {
			return OLE.OLECMDID_SPELL;
		}
		else if (command == OleCommand.PROPERTIES) {
			return OLE.OLECMDID_PROPERTIES;
		}
		else if (command == OleCommand.CUT) {
			return OLE.OLECMDID_CUT;
		}
		else if (command == OleCommand.COPY) {
			return OLE.OLECMDID_COPY;
		}
		else if (command == OleCommand.PASTE) {
			return OLE.OLECMDID_PASTE;
		}
		else if (command == OleCommand.PASTERSPECIAL) {
			return OLE.OLECMDID_PASTESPECIAL;
		}
		else if (command == OleCommand.UNDO) {
			return OLE.OLECMDID_UNDO;
		}
		else if (command == OleCommand.REDO) {
			return OLE.OLECMDID_REDO;
		}
		else if (command == OleCommand.SELECTALL) {
			return OLE.OLECMDID_SELECTALL;
		}
		else if (command == OleCommand.CLEARSELECTION) {
			return OLE.OLECMDID_CLEARSELECTION;
		}
		else if (command == OleCommand.ZOOM) {
			return OLE.OLECMDID_ZOOM;
		}
		else if (command == OleCommand.GETZOOMRANGE) {
			return OLE.OLECMDID_GETZOOMRANGE;
		}
		else if (command == OleCommand.UPDATECOMMANDS) {
			return OLE.OLECMDID_UPDATECOMMANDS;
		}
		else if (command == OleCommand.REFRESH) {
			return OLE.OLECMDID_REFRESH;
		}
		else if (command == OleCommand.STOP) {
			return OLE.OLECMDID_STOP;
		}
		else if (command == OleCommand.HIDETOOLBARS) {
			return OLE.OLECMDID_HIDETOOLBARS;
		}
		else if (command == OleCommand.SETPROGRESSMAX) {
			return OLE.OLECMDID_SETPROGRESSMAX;
		}
		else if (command == OleCommand.SETPROGRESSPOS) {
			return OLE.OLECMDID_SETPROGRESSPOS;
		}
		else if (command == OleCommand.SETPROGRESSTEXT) {
			return OLE.OLECMDID_SETPROGRESSTEXT;
		}
		else if (command == OleCommand.SETTITLE) {
			return OLE.OLECMDID_SETTITLE;
		}
		else if (command == OleCommand.SETDOWNLOADSTATE) {
			return OLE.OLECMDID_SETDOWNLOADSTATE;
		}
		else if (command == OleCommand.STOPDOWNLOAD) {
			return OLE.OLECMDID_STOPDOWNLOAD;
		}
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
		else if (object instanceof Short) {
			return new Variant(((Short) object).shortValue());
		}
		else if (object instanceof Boolean) {
			return new Variant(((Boolean) object).booleanValue());
		}
		else if (object instanceof Byte) {
			return new Variant(((Byte) object).byteValue());
		}
		else if (object instanceof Character) {
			return new Variant(((Character) object).charValue());
		}
		else if (object instanceof IDispatch) {
			return new Variant(((IDispatch) object));
		}
		else if (object instanceof IUnknown) {
			return new Variant(((IUnknown) object));
		}
		else {
			throw new IllegalArgumentException("parameter type '" + object.getClass().getName() + "' is not supported");
		}
	}

	private final class OleAutomationImpl implements IOleAutomation {

		private final OleAutomation oleAutomation;

		public OleAutomationImpl(final OleAutomation oleAutomation) {
			this.oleAutomation = oleAutomation;
		}

		@Override
		public Object invoke(final String methodName, final IInvocationParameter... parameters) {
			Assert.paramNotEmpty(methodName, "methodName");
			Assert.paramNotNull(parameters, "parameters");

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
		public boolean setProperty(final String propertyName, final Object... parameters) {
			Assert.paramNotEmpty(parameters, "parameters");
			final int[] propertyNameIds = oleAutomation.getIDsOfNames(new String[] {propertyName});
			if (propertyNameIds != null && propertyNameIds.length == 1) {
				if (parameters.length > 1) {
					final Variant[] variants = new Variant[parameters.length];
					for (int i = 0; i < parameters.length; i++) {
						variants[i] = createVariant(parameters[i]);
					}
					return oleAutomation.setProperty(propertyNameIds[0], variants);
				}
				else {
					return oleAutomation.setProperty(propertyNameIds[0], createVariant(parameters[0]));
				}
			}
			else {
				return false;
			}
		}

		public void dispose() {
			oleAutomation.dispose();
		}

	}

}

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

package org.jowidgets.addons.widgets.ole.impl.swt.common;

import org.eclipse.swt.internal.ole.win32.TYPEATTR;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleFunctionDescription;
import org.eclipse.swt.ole.win32.OlePropertyDescription;

final class OleAutomationTypeInfoUtil {

	private OleAutomationTypeInfoUtil() {}

	static String getTypeInfo(final OleAutomation auto) {
		final StringBuilder result = new StringBuilder();
		final TYPEATTR typeattr = auto.getTypeInfoAttributes();
		if (typeattr != null) {
			if (typeattr.cFuncs > 0) {
				result.append("Functions :\n");
			}
			for (int i = 0; i < typeattr.cFuncs; i++) {
				final OleFunctionDescription description = auto.getFunctionDescription(i);
				result.append(getInvokeKind(description.invokeKind)
					+ " (id = "
					+ description.id
					+ ") : "
					+ "\n\tSignature   : "
					+ getTypeName(description.returnType)
					+ " "
					+ description.name
					+ "("
					+ getFunctionDescription(description)
					+ ")"
					+ "\n\tDescription : "
					+ description.documentation
					+ "\n\tHelp File   : "
					+ description.helpFile
					+ "\n");
			}

			if (typeattr.cVars > 0) {
				result.append("\n\nVariables  :\n");
			}
			for (int i = 0; i < typeattr.cVars; i++) {
				final OlePropertyDescription data = auto.getPropertyDescription(i);
				result.append("PROPERTY (id = "
					+ data.id
					+ ") :"
					+ "\n\tName : "
					+ data.name
					+ "\n\tType : "
					+ getTypeName(data.type)
					+ "\n");
			}
		}
		return result.toString();
	}

	private static String getFunctionDescription(final OleFunctionDescription description) {
		final StringBuilder result = new StringBuilder();
		final int firstOptionalArgIndex = description.args.length - description.optionalArgCount;
		for (int i = 0; i < description.args.length; i++) {
			result.append("[");
			if (i >= firstOptionalArgIndex) {
				result.append("optional, ");
			}
			result.append(getDirection(description.args[i].flags)
				+ "] "
				+ getTypeName(description.args[i].type)
				+ " "
				+ description.args[i].name);
			if (i < description.args.length - 1) {
				result.append(", ");
			}
		}
		return result.toString();
	}

	private static String getTypeName(final int type) {
		switch (type) {
			case OLE.VT_BOOL:
				return "boolean";
			case OLE.VT_R4:
				return "float";
			case OLE.VT_R8:
				return "double";
			case OLE.VT_I4:
				return "int";
			case OLE.VT_DISPATCH:
				return "IDispatch";
			case OLE.VT_UNKNOWN:
				return "IUnknown";
			case OLE.VT_I2:
				return "short";
			case OLE.VT_BSTR:
				return "String";
			case OLE.VT_VARIANT:
				return "Variant";
			case OLE.VT_CY:
				return "Currency";
			case OLE.VT_DATE:
				return "Date";
			case OLE.VT_UI1:
				return "unsigned char";
			case OLE.VT_UI4:
				return "unsigned int";
			case OLE.VT_USERDEFINED:
				return "UserDefined";
			case OLE.VT_HRESULT:
				return "int";
			case OLE.VT_VOID:
				return "void";

			case OLE.VT_BYREF | OLE.VT_BOOL:
				return "boolean *";
			case OLE.VT_BYREF | OLE.VT_R4:
				return "float *";
			case OLE.VT_BYREF | OLE.VT_R8:
				return "double *";
			case OLE.VT_BYREF | OLE.VT_I4:
				return "int *";
			case OLE.VT_BYREF | OLE.VT_DISPATCH:
				return "IDispatch *";
			case OLE.VT_BYREF | OLE.VT_UNKNOWN:
				return "IUnknown *";
			case OLE.VT_BYREF | OLE.VT_I2:
				return "short *";
			case OLE.VT_BYREF | OLE.VT_BSTR:
				return "String *";
			case OLE.VT_BYREF | OLE.VT_VARIANT:
				return "Variant *";
			case OLE.VT_BYREF | OLE.VT_CY:
				return "Currency *";
			case OLE.VT_BYREF | OLE.VT_DATE:
				return "Date *";
			case OLE.VT_BYREF | OLE.VT_UI1:
				return "unsigned char *";
			case OLE.VT_BYREF | OLE.VT_UI4:
				return "unsigned int *";
			case OLE.VT_BYREF | OLE.VT_USERDEFINED:
				return "UserDefined *";
			default:
				break;
		}
		return "unknown " + type;
	}

	private static String getDirection(final int direction) {
		String dirString = "";
		boolean comma = false;
		if ((direction & OLE.IDLFLAG_FIN) != 0) {
			dirString += "in";
			comma = true;
		}
		if ((direction & OLE.IDLFLAG_FOUT) != 0) {
			if (comma) {
				dirString += ", ";
			}
			dirString += "out";
			comma = true;
		}
		if ((direction & OLE.IDLFLAG_FLCID) != 0) {
			if (comma) {
				dirString += ", ";
			}
			dirString += "lcid";
			comma = true;
		}
		if ((direction & OLE.IDLFLAG_FRETVAL) != 0) {
			if (comma) {
				dirString += ", ";
			}
			dirString += "retval";
		}

		return dirString;
	}

	private static String getInvokeKind(final int invKind) {
		switch (invKind) {
			case OLE.INVOKE_FUNC:
				return "METHOD";
			case OLE.INVOKE_PROPERTYGET:
				return "PROPERTY GET";
			case OLE.INVOKE_PROPERTYPUT:
				return "PROPERTY PUT";
			case OLE.INVOKE_PROPERTYPUTREF:
				return "PROPERTY PUT BY REF";
			default:
				break;
		}
		return "unknown " + invKind;
	}

}

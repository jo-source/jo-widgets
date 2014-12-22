/*
 * Copyright (c) 2014, grossman
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

package org.jowidgets.spi.impl.swt.common.dnd;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.impl.swt.common.clipboard.ObjectTransfer;

final class DragDropUtil {

	private static final Transfer TEXT_TRANSFER = TextTransfer.getInstance();
	private static final Transfer OBJECT_TRANSFER = ObjectTransfer.getInstance();

	private DragDropUtil() {}

	static Transfer[] createTransfers(final Collection<TransferTypeSpi> supportedTypes) {
		final Set<Transfer> result = new LinkedHashSet<Transfer>();
		for (final TransferTypeSpi type : supportedTypes) {
			if (String.class.equals(type.getJavaType())) {
				result.add(TEXT_TRANSFER);
			}
			else {
				result.add(OBJECT_TRANSFER);
			}
			if (result.size() == 2) {
				break;
			}
		}
		return result.toArray(new Transfer[result.size()]);
	}

	static int createOperations(final Set<DropAction> actions) {
		int result = 0;
		if (actions.contains(DropAction.NONE)) {
			result = result | DND.DROP_NONE;
		}
		if (actions.contains(DropAction.DEFAULT)) {
			result = result | DND.DROP_DEFAULT;
		}
		if (actions.contains(DropAction.COPY)) {
			result = result | DND.DROP_COPY;
		}
		if (actions.contains(DropAction.MOVE)) {
			result = result | DND.DROP_MOVE;
		}
		if (actions.contains(DropAction.LINK)) {
			result = result | DND.DROP_LINK;
		}
		return result;
	}

	static Set<DropAction> createActions(final int operations) {
		final Set<DropAction> result = new HashSet<DropAction>();
		if ((operations & DND.DROP_NONE) != 0) {
			result.add(DropAction.NONE);
		}
		if ((operations & DND.DROP_DEFAULT) != 0) {
			result.add(DropAction.DEFAULT);
		}
		if ((operations & DND.DROP_COPY) != 0) {
			result.add(DropAction.COPY);
		}
		if ((operations & DND.DROP_MOVE) != 0) {
			result.add(DropAction.MOVE);
		}
		if ((operations & DND.DROP_LINK) != 0) {
			result.add(DropAction.LINK);
		}
		return result;
	}

	static DropAction getDropAction(final int detail) {
		if (detail == DND.DROP_NONE) {
			return DropAction.NONE;
		}
		else if (detail == DND.DROP_DEFAULT) {
			return DropAction.DEFAULT;
		}
		else if (detail == DND.DROP_COPY) {
			return DropAction.COPY;
		}
		else if (detail == DND.DROP_MOVE) {
			return DropAction.MOVE;
		}
		else if (detail == DND.DROP_LINK) {
			return DropAction.LINK;
		}
		else {
			return DropAction.NONE;
		}
	}

	static TransferTypeSpi getStringTransferType(final Collection<TransferTypeSpi> supportedTypes) {
		for (final TransferTypeSpi type : supportedTypes) {
			if (String.class.equals(type.getJavaType())) {
				return type;
			}
		}
		return null;
	}

	static Collection<TransferTypeSpi> getTransferObjectTypes(final Collection<TransferTypeSpi> supportedTypes) {
		final List<TransferTypeSpi> result = new LinkedList<TransferTypeSpi>();
		for (final TransferTypeSpi type : supportedTypes) {
			if (!String.class.equals(type.getJavaType())) {
				result.add(type);
			}
		}
		return result;
	}

}

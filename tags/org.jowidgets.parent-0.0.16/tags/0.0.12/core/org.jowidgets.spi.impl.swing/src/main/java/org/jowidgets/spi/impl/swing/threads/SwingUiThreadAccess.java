/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 04.09.2010
 */
package org.jowidgets.spi.impl.swing.threads;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.util.Assert;

public class SwingUiThreadAccess implements IUiThreadAccessCommon {

	@Override
	public void invokeLater(final Runnable runnable) {
		Assert.paramNotNull(runnable, "runnable");
		SwingUtilities.invokeLater(runnable);
	}

	@Override
	public void invokeAndWait(final Runnable runnable) throws InterruptedException {
		Assert.paramNotNull(runnable, "runnable");
		try {
			SwingUtilities.invokeAndWait(runnable);
		}
		catch (final InvocationTargetException e) {
			throw new RuntimeException("InvocationTargetException while invoke and wait", e);
		}
	}

	@Override
	public boolean isUiThread() {
		return SwingUtilities.isEventDispatchThread();
	}

}

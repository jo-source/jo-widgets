/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 04.09.2010
 */
package org.jowidgets.spi.impl.dummy.threads;

import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.spi.impl.dummy.application.DummyApplicationRunner;
import org.jowidgets.util.Assert;

public class DummyUiThreadAccess implements IUiThreadAccessCommon {

	@Override
	public void invokeLater(final Runnable runnable) {
		Assert.paramNotNull(runnable, "runnable");
		DummyApplicationRunner.invokeLater(runnable);
	}

	@Override
	public void invokeAndWait(final Runnable runnable) throws InterruptedException {
		Assert.paramNotNull(runnable, "runnable");
		DummyApplicationRunner.invokeAndWait(runnable);
	}

	@Override
	public boolean isUiThread() {
		return DummyApplicationRunner.isEventDispatcherThread(Thread.currentThread());
	}

}

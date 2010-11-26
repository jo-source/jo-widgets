/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 04.09.2010
 */
package org.jowidgets.impl.mock.threads;

import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.impl.mock.application.MockApplicationRunner;
import org.jowidgets.util.Assert;

public class MockUiThreadAccess implements IUiThreadAccessCommon {

	@Override
	public void invokeLater(final Runnable runnable) {
		Assert.paramNotNull(runnable, "runnable");
		MockApplicationRunner.invokeLater(runnable);
	}

	@Override
	public void invokeAndWait(final Runnable runnable) throws InterruptedException {
		Assert.paramNotNull(runnable, "runnable");
		MockApplicationRunner.invokeAndWait(runnable);
	}

	@Override
	public boolean isUiThread() {
		return MockApplicationRunner.isEventDispatcherThread(Thread.currentThread());
	}

}

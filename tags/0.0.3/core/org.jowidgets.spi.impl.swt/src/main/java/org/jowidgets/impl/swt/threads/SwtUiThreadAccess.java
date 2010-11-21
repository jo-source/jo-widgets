/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 04.09.2010
 */
package org.jowidgets.impl.swt.threads;

import org.eclipse.swt.widgets.Display;
import org.jowidgets.common.threads.IUiThreadAccess;
import org.jowidgets.util.Assert;

public class SwtUiThreadAccess implements IUiThreadAccess {

	private Display display;

	public SwtUiThreadAccess() {
		this(null);
	}

	public SwtUiThreadAccess(final Display display) {
		super();
		this.display = display;
	}

	@Override
	public void invokeLater(final Runnable runnable) {
		Assert.paramNotNull(runnable, "runnable");
		getDisplay().asyncExec(runnable);
	}

	@Override
	public void invokeAndWait(final Runnable runnable) throws InterruptedException {
		Assert.paramNotNull(runnable, "runnable");
		getDisplay().syncExec(runnable);
	}

	@Override
	public boolean isUiThread() {
		final Display currentDisplay = Display.getCurrent();
		return currentDisplay != null && currentDisplay == getDisplay();
	}

	private Display getDisplay() {
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

}

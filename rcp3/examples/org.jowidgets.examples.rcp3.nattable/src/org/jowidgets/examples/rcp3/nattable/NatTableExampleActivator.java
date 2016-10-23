package org.jowidgets.examples.rcp3.nattable;

import org.jowidgets.tools.starter.application.osgi.plugin.OsgiApplicationRunner;

public class NatTableExampleActivator extends OsgiApplicationRunner {

	public NatTableExampleActivator() {
		super(new NatTableExample());
	}

}

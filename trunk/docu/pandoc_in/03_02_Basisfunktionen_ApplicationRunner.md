## Der Application Runner {#application_runner}

Der Application Runner dienst zum Starten einer jowidgets standalone Applikation. Standalone bedeutet, dass die initialen Widgets sowie der Event Dispatcher Thread über die Jowidget API erzeugt werden. 

Die Schnittstelle `IApplicationRunner` sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.common.application;

public interface IApplicationRunner {

	void run(IApplication application);

}
~~~

Eine Implementierung erhält man vom Toolkit. Die Methode `run()` blockiert, bis die Applikation beendet wurde. 

Die Schnittstelle `IApplication` wird _selbst_ implementiert. Siehe dazu auch [HelloWorldApplication - Der common Ui Code](#hello_world_common_code)

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.common.application;

public interface IApplication {

	void start(final IApplicationLifecycle lifecycle);

}
~~~

In der `start()` Methode wird ein `IApplicationLifecycle` übergeben. 

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.common.application;

public interface IApplicationLifecycle {

	void finish();

}
~~~

Wird auf dem `IApplicationLifecycle` die Methode `finish()` aufgerufen, wird die Applikation beendet. Dabei werden alle Widgets des zugehörigen Toolkit disposed.

Wie man jowidgets (ohne ApplicationRunner) in nativen (Swing, Swt, Rwt, ...) Code integriert findet sich im Abschnitt [Jowidgets Code in native Projekte integrieren](#integrate_jowidgets_in_native_code).



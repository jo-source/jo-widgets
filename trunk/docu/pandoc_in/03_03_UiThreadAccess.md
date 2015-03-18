## Der Ui Thread Access {#ui_thread_access}

Wie in anderen UI Frameworks (wie Swing oder Swt) gibt es auch in jowidgets einen _UI Thread_, in welchem alle UI Events _dispatched_ werden. Alle Modifikationen an Widgets oder deren _Models_ müssen in diesem Thread erfolgen. Reagiert man auf Events, welche durch Nutzereingaben ausgelöst wurden, befindet man sich bereits automatisch im UI Thread. Dabei ist, wie auch bei anderen UI Frameworks, darauf zu achten, den UI Thread nicht für lange Berechnungen zu blockieren, weil ansonsten keine Events mehr verarbeitet werden können und die Oberfläche für diese Zeit _einfriert_. 

Operationen die potentiell lange dauern können, wie Beispielsweise das Aufrufen eines Service, sollten daher immer in einem eigenen Thread stattfinden. 

Will man aus einem anderen Thread heraus wieder Methoden auf UI Elementen aufrufen, bietet die Schnittstelle `IUiThreadAccess` dafür die folgenden Methoden:

~~~ 
	void invokeLater(Runnable runnable);
	
	void invokeAndWait(Runnable runnable) throws InterruptedException;
~~~

Die Methode `invokeLater()` führt das übergebene `Runnable` zu einem späteren Zeitpunkt im Ui Thread aus. Nachdem die Methode zurückkehrt, kann man jedoch nicht davon ausgehen, dass das Runnable bereits ausgeführt wurde.

Die Methode `invokeAndWait()` führt auch das Runnable im Ui Thread aus, blockiert aber so lange, bis das `Runnable` ausgeführt wurde. Diese Methode ist mit höchster Vorsicht zu verwenden. Bei falscher Verwendung erzeugt man dadurch sehr schnell einen _Deadlock_. Statt blockierenden Methodenaufrufen sollte man besser mit Callback's arbeiten.

Beide Methoden können in jedem beliebigen Thread aufgerufen werden.

Um zu prüfen, ob der aktuelle Thread der UI Thread ist, kann man die folgende Methode verwenden:

~~~ 
	boolean isUiThread();
~~~

Der Aufruf:

~~~
   IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
~~~

muss immer im UI Thread erfolgen. Dies liegt unter anderem daran, dass es im Web Kontext mehrere UI Threads (einer pro User Session) existieren, und ein beliebiger Thread nicht wissen kann, welcher UI Thread verwendet werden soll.

In der Praxis übergibt man die `IUiThreadAccess` Instanz einfach an den anderen Thread. Folgendes Beispiel zeigt, wie man das einfach mit Hilfe des Ausführungsstacks machen kann:

~~~{.java .numberLines startFrom="1"} 
	//Add an listener to the button that does a long lasting calculation
	button.addActionListener(new IActionListener() {
		@Override
		public void actionPerformed() {

			//this will be invoked in the ui thread
			final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();

			//execute the service calculation in an service thread
			executorService.execute(new Runnable() {

				@Override
				public void run() {
					//this will be invoked in the service thread
					final ServiceResult result = longLastingService.doLongLastingCalculation();

					uiThreadAccess.invokeLater(new Runnable() {
						@Override
						public void run() {
							//this will be executed in the ui thread
							serviceResultWidget.showResult(result);
						}
					});
				}

			});
		}
	});
~~~

Hinweis: Das obige Beispiel wurde bewusst vereinfacht. Es fehlen u.A. wichtige Aspekte wie Fehlerbehandlung oder das Abbrechen der Berechnung.



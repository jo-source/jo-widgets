### Die Schnittstelle IWindow{#window_interface}

Die Schnittstelle `IWindow` stellt gemeinsamen Funktionen für Fenster bereits. `IWindow` erweitert [`IDisplay`](#display_interface) und somit auch [`IWidget`](#widget_interface). Es folgt eine kurze Übersicht der wichtigsten Methoden:

#### Kind Fenster

Ein Kind Fenster lässt sich mit Hilfe der folgenden Methode erzeugen: 

~~~
	<WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		WIDGET_TYPE createChildWindow(final DESCRIPTOR_TYPE descriptor);
~~~

Ein Kind Fenster wird mit Hilfe eines BluePrint (IWidgetDescriptor) erzeugt. Dieses wird der Methode `createChildWindow()` übergeben, welche das erzeugte Fenster zurück gibt.

Im folgenden Beispiel wird so ein [FileChooser](#file_chooser) zum Öffnen einer Datei erstellt:

~~~{.java .numberLines startFrom="1"}
	IFileChooserBluePrint fileChooserBp = BPF.fileChooser(FileChooserType.OPEN_FILE);
	final IFileChooser fileChooser = frame.createChildWindow(fileChooserBp);
	final DialogResult result = fileChooser.open();
	if (DialogResult.OK.equals(result)) {
		System.out.println(fileChooser.getSelectedFiles());
	}
~~~

Die folgende Methode liefert die Kind Fenster eines Fensters.

~~~
	List<IDisplay> getChildWindows();
~~~

Dabei handelt es sich um eine nicht modifizierbare Kopie aller aktuell vorhandenen Kind Fenster. Fenster die bereits disposed wurden, sind nicht enthalten.

Im obigen Beispiel würde ein Aufruf von `getChildWindows()` zwischen Zeile 2 und Zeile 3 eine Referenz auf den erzeugten File Chooser enthalten, und bei einem Aufruf nach Zeile 3 nicht mehr, da der Aufruf `open()` blockiert, bis der File Chooser wieder geschlossen wird, und anschließend das FileChooser Fenster automatisch disposed wurde.

#### Window Listener

Die folgenden Methoden können verwendet werden um `IWindowListener` zu registrieren oder zu deregistrieren:

~~~
	void addWindowListener(IWindowListener listener);

	void removeWindowListener(IWindowListener listener);
~~~
	
Ein `IWindowListener` hat die folgenden Methoden:

~~~
	void windowActivated();

	void windowDeactivated();

	void windowIconified();

	void windowDeiconified();

	void windowClosing(IVetoable vetoable);

	void windowClosed();
~~~

In einer Fenster basierten Anwendung kann zur selben Zeit genau ein Fenster aktiv sein. Die Methoden `windowActivated()` und `windowDeactivated()` informieren über eine Änderung des `active` Status. Die Methode Toolkit.getActiveWindow() liefert das gerade aktive Fenster. 

Die Methode `windowClosing(IVetoable vetoable)` wird aufgerufen, bevor ein Fenster geschlossen werden soll. Mit Hilfe des `vetoable` hat man die Möglichkeit, das Schließen zu verhindern. Das folgende Beispiel soll dies verdeutlichen:

~~~{.java .numberLines startFrom="1"}
	frame.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(final IVetoable vetoable) {
			final String msg = "Close Window?";
			final QuestionResult questionResult = QuestionPane.askYesNoQuestion(msg, msg);
			if (!QuestionResult.YES.equals(questionResult)) {
				vetoable.veto();
			}
		}
	});
~~~

Beim Schließen des Fensters wird ein [QuestionDialog](#question_dialog) angezeigt, um den Nutzer zu fragen, ob das Fenster wirklich geschlossen werden soll. Falls nicht wird in Zeile 7 ein _Veto eingelegt_, wodurch das Fenster nicht geschlossen wird. Die folgende Abbildung zeigt das Ergebnis nachdem das `X` zum Schließen gedrückt wurde:

![Close Window Veto Beispiel](images/close_veto_example.gif "Close Window Veto Beispiel")

Die Methode `windowClosed()` wird aufgerufen, nachdem das Fenster geschlossen wurden. __Achtung:__ Wenn ein Fenster geschlossen, bedeutet dies nicht automatisch, das das Fenster dann auch disposed ist. 

Die Klasse `org.jowidgets.tools.controller.WindowAdapter` implementiert die `IWindowListener` Schnittstelle mit leeren Methodenrümpfen und kann verwendet werden, falls nicht alle Methoden implementiert werden sollen.
	
	
#### Packen von Fenstern

Die Methode pack

~~~
	void pack();
~~~ 

sorgt dafür, das für das Fenster die `PreferredSize` berechnet und gesetzt wird. Anschließend wird auf dem zugehörigen Container ein `layout()` durchgeführt. 

Mit Hilfe der folgenden Methoden kann das `pack()` beeinflußt werden:

~~~
	void setMinPackSize(Dimension size);

	void setMaxPackSize(Dimension size);
~~~ 

Die `MinPackSize` legt eine minimale Größe fest, die das Fenster mindestens (trotz pack) haben soll. Dadurch kann zum Beispiel verhindert werden, dass ein Fenster mit _dynamisch_ erzeugten Inhalt zu klein angezeigt wird.

Die `MaxPackSize` legt eine maximale Größe fest, die das Fenster höchsten (trotz pack) haben soll. Dadurch kann zum Beispiel verhindert werden, dass ein Fenster mit _dynamisch_ erzeugten Inhalt zu groß angezeigt wird.

#### Weitere Utilities

Die folgende Methode:

~~~
	Rectangle getParentBounds();
~~~

liefert die `ParentBounds`. Ist das Fenster ein Root Fenster (`getParent() == null`) werden die Bounds des Bildschirm zurückgegeben. Ist es ein Kind Fenster, werden die Bounds des Vaters zurückgegeben. Die Methode liefert somit nie `null` zurück. Dies kann zum Beispiel hilfreich sein, um ein Fenster relativ zum Vater anzuzeigen, ohne vorab überprüfen zum müssen, ob es sich um ein Root Fenster oder ein Kind Fenster handelt.

Die folgende Methode:

~~~
	void centerLocation();
~~~

setzt die Position des Fensters so, das es zentriert zu den `ParentBounds` angezeigt wird. Root Fenster werden somit zentriert auf dem Bildschirm angezeigt, Kind Fenster zentriert bezüglich des Vater Fensters. Dabei haben beide Fenster dann den gleichen Mittelpunkt.



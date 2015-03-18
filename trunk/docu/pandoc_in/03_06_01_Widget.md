### Die Schnittstelle IWidget{#widget_interface}

Alle Widgets implementieren die Schnittstelle `org.jowidgets.api.widgets.IWidget`. Es folgt eine kurze Übersicht über die wichtigsten Methoden:

#### Ui Referenz

~~~
	Object getUiReference();
~~~

Liefert die UI Referenz des Widgets. Der Typ hängt von der verwendeten SPI Implementierung ab. Beispielsweise wird für ein `org.jowidgets.api.widgets.IButton` bei Verwendung der Swing Spi Implementierung ein `javax.swing.JButton` und bei der Verwendung der SWT SPI Implementierung ein `org.eclipse.swt.widgets.Button` zurückgegeben.

Die Ui Referenz kann zum Beispiel verwendet werden, um _native_ Widgets oder Funktionen _nativer_ Widgets zu verwenden, welche in jowidgets nicht vorhanden sind. Allerdings hat man dann nicht mehr die Möglichkeit, den Code mit anderen nativen Ui Technologien zu verwenden. 

Tipp: Wird solch eine natives Widget oder eine Funktion mehrfach verwendet, empfiehlt es sich, eine eigene Widget Schnittstelle dafür zu definieren. (Siehe dazu [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries)). Die Widget Implementierung kann dann vorerst nur für die benötigte _native_ Technologie implementiert werden. Soll ein Modul, welches dieses Widget verwendet, später auch mit einer anderen Technologie verwendet werden, muss nur die Implementierung des Widgets angepasst werden, und nicht das Modul. 

#### Enablement

~~~
	void setEnabled(boolean enabled);

	boolean isEnabled();
~~~

Ein Widget das _disabled_ ist nimmt keine Nutzereingaben mehr an. Per default sind alle Widgets initial _enabled_.

#### Widget Parent

~~~
	IWidget getParent();
~~~

Liefert das übergeordnete Widget oder null, falls das Widget ein _Root Element_ ist.

#### Widget Root

~~~
	IWidget getRoot();
~~~

Für zusammengesetzte (Composite) Widgets wird das _Root Widget_ zurückgegeben, also zum Beispiel ein org.jowidgets.api.widgets.IComposite. Ansonsten wird das Widget selbst zurückgegeben.

#### Dispose Management

~~~
	void dispose();

	boolean isDisposed();
	
	void addDisposeListener(IDisposeListener listener);

	void removeDisposeListener(IDisposeListener listener);
~~~

Mit Hilfe der Methode `dispose()` kann ein Widgets _disposed_ werden, wenn man es nicht mehr benötigt. Ein Widget, das _disposed_ wurde, kann nicht mehr verwendet werden. Wird zum Beispiel ein Fenster _disposed_ wird es auch geschlossen. Wird ein Control disposed, wird es auch aus seinem Container entfernt. Mit Hilfe eines `IDisposeListener` kann man sich als Observer registrieren, um über das _Dispose_ eines Widgets informiert zu werden.




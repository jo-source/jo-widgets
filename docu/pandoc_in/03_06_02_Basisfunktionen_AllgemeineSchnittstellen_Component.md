### Die Schnittstelle IComponent{#component_interface}

Die Schnittstelle `IComponent` stellt die gemeinsamen Funktionen für Fenster und Controls bereit. Die Schnittstelle `IComponent` erweitert [`IWidget`](#widget_interface). 

Es folgt eine Übersicht der wichtigsten Methoden:

#### Neuzeichnen

~~~
	void redraw();
~~~

Markiert die Komponente, dass sie zu nächstmöglichen Zeitpunkt neu gezeichnet werden muss.

~~~
	void setRedrawEnabled(boolean enabled);
~~~

Deaktiviert, bzw. aktiviert das Neuzeichnen einer Komponente (und deren Kinder, falls vorhanden). Nachdem das Neuzeichnen deaktiviert wurde, werden alle anstehenden Paint Events verworfen, bis wieder `setRedrawEnabled(true)` aufgerufen wird. 

Diese Funktion kann zum Beispiel verwendet werden, um _Flackereffekte_ zu vermeiden, während ein `Container` umstrukturiert wird, also Kinder hinzukommen oder entfernt werden.

Das Deaktivieren mittels `setRedrawEnabled(false)` ist ein _Hinweis_ der nicht für alle Platformen sowie SPI Implementierungen verfügbar ist. Beispielsweise hat diese Funktion unter Swing keinen Effekt. Glücklicherweise gibt es unter Swing aber auch keine Probleme mit _Flackern_, wenn Container umorganisiert werden.

Ein Aufruf von `setRedrawEnabled(true)` markiert zudem die Komponente, dass sie neu gezeichnet werden muss (siehe Methode redraw()).

#### Eingabefokus

In einer Fenster basierten Anwendung kann zur selben Zeit genau ein Control den Eingabefokus haben. Dieses Control empfängt dann die Tastatureingaben des Benutzers. Um den Eingabefokus zu erhalten, kann die folgenden Methode verwendet werden:

~~~
	boolean requestFocus();
~~~

Der Aufruf der Methode garantiert nicht, dass der Eingabefokus tatsächlich zugewiesen wurde. 

Mittels folgender Methode kann überprüft werden, ob eine Komponente den Eingabefokus besitzt.

~~~
	boolean hasFocus();
~~~

Um sich benachrichtigen zu lassen, dass ein Fokuswechsel stattgefunden hat, kann ein `IFocusListener` verwendet werden:

~~~
	void addFocusListener(IFocusListener listener);

	void removeFocusListener(IFocusListener listener);
~~~

Dieser hat die Methoden:

~~~
	void focusGained();

	void focusLost();
~~~

#### Tastatureingaben(#container_key_events)

Um Tastatureingaben zur erhalten kann ein `IKeyListener` verwendet werden:

~~~
	void addKeyListener(IKeyListener listener);

	void removeKeyListener(IKeyListener listener);
~~~

Dieser hat die folgenden Methoden:

~~~
	void keyPressed(final IKeyEvent event);

	void keyReleased(final IKeyEvent event);
~~~

Die Klasse `org.jowidgets.tools.controller.KeyAdapter` implementiert die `IKeyListener` Schnittstelle mit leeren Methodenrümpfen und kann verwendet werden, falls nicht alle Methoden implementiert werden sollen.

Die Schnittstelle `IKeyEvent` hat die folgenden Methoden:

~~~
	VirtualKey getVirtualKey();

	Character getCharacter();

	Character getResultingCharacter();

	Set<Modifier> getModifier();
~~~

Die `enum` `VirtualKey` enthält die F-Tasten, Steuertasten wie ENTER, BACKSPACE, TAB, PFEILTASTEN, etc, sowie Zahlen und Buchstaben.
Eine vollständige Übersicht über alle VirtualKeys findet sich in der API Spezifikation: [http://www.jowidgets.org/api_doc/org/jowidgets/common/types/VirtualKey.html](http://www.jowidgets.org/api_doc/org/jowidgets/common/types/VirtualKey.html)

Die Methode `getCharacter()` liefert das zugehörige Zeichen der Taste __ohne__ Anwendung der Modifier. Auf einer deutschen Tastatur liefert somit `SHIFT + 1` das Zeichen `1` und __nicht__ das Zeichen `!`. 

Dagegen liefert die Methode `getResultingCharacter()` das zugehörige Zeichen __mit__ Anwendung der Modifier. Im vorigen Beispiel also anstatt `1` das Zeichen `!`.

Die Methode `getModifier()` liefert ein `Set` der verwendeten Modifier Tasten (CTRL, SHIFT, ALT). 

#### Mauseingaben

Um einfache Mauseingaben zur erhalten, kann ein IMouseListener verwendet werden:

~~~
	void addMouseListener(IMouseListener listener);

	void removeMouseListener(IMouseListener listener);
~~~

Ein `IMouseListener` hat die folgenden Methoden: 

~~~
	void mousePressed(IMouseButtonEvent event);

	void mouseReleased(IMouseButtonEvent event);

	void mouseDoubleClicked(IMouseButtonEvent event);

	void mouseEnter(IMouseEvent event);

	void mouseExit(IMouseEvent event);
~~~

Ein `IMouseEvent` liefert die Position des Ereignisses im Koordinatensystem der zugehörigen Komponente:

~~~
	Position getPosition();
~~~

Ein `IMouseButtonEvent` liefert zusätzlich den Button sowie die Tastatur Modifier.

~~~
	MouseButton getMouseButton();

	Set<Modifier> getModifiers();
~~~

Die Klasse `org.jowidgets.tools.controller.MouseAdapter` implementiert die `IMouseListener` Schnittstelle mit leeren Methodenrümpfen und kann verwendet werden, falls nicht alle Methoden implementiert werden sollen.

Um Ereignisse zu Mausbewegungen zur erhalten, kann ein `IMouseMotionListener` verwendet werden:

~~~
	void addMouseMotionListener(IMouseMotionListener listener);

	void removeMouseMotionListener(IMouseMotionListener listener);
~~~

Dieser hat die folgenden Methoden:

~~~
	void mouseMoved(IMouseEvent event);

	void mouseDragged(IMouseButtonEvent event);
~~~

Die Methode `mouseMoved()` wird bei jeder Änderung der Mausposition aufgerufen, die Methode `mouseDragged()`, wenn dabei eine Maustaste _gedrückt_ wird.

Die Klasse `org.jowidgets.tools.controller.MouseMotionAdapter` implementiert die `IMouseMotionListener` Schnittstelle mit leeren Methodenrümpfen und kann verwendet werden, falls nicht alle Methoden implementiert werden sollen.

#### Popup Events

Popup Events werden ausgelöst, wenn der Benutzer ein Kontextmenü (Popup Menü) öffnen möchte. Die notwendigen _Popup Trigger_ können auf unterschiedlichen Betriebssystemen variieren. Unter Windows geschieht dies zum Beispiel durch einen `Rechtsklick` mit der Maus. Um Popup Events zu erhalten, kann ein `IPopupDetectionListener` verwendet werden:

~~~
	void addPopupDetectionListener(IPopupDetectionListener listener);

	void removePopupDetectionListener(IPopupDetectionListener listener);
~~~

Dieser hat die folgende Methode:

~~~
	void popupDetected(Position position);
~~~

Die Methode übergibt die Position im Koordinatensystem der Komponente. 

#### Popup Menus

Mit Hilfe der folgenden Methode kann ein [Popup Menü](#popup_menu) erzeugt werden:

~~~
	IPopupMenu createPopupMenu();
~~~

Ein Popup Menu implementiert die Schnittstelle [`IMenu`](#menu_interface), somit kann man zum Beispiel mit Hilfe der Methode `addItem()` Menüeinträge hinzufügen. Das Menü kann mit Hilfe der Methode `show()` angezeigt werden:

~~~
	void show(Position position);
~~~

Dabei ist die Position im Koordinatensystem der Komponente anzugeben, für die das Popup Menü erzeugt wurde.

Folgendes Beispiel zeigt die Verwendung eines PopMenüs:

~~~{.java .numberLines startFrom="1"} 
	final IPopupMenu menu = frame.createPopupMenu();
	
	final ISelectableMenuItem option = menu.addItem(BPF.checkedMenuItem("My option"));
	option.addItemListener(new IItemStateListener() {
		@Override
		public void itemStateChanged() {
			System.out.println("My option selected: " + option.isSelected());
		}
	});
	
	frame.addPopupDetectionListener(new IPopupDetectionListener() {
		@Override
		public void popupDetected(final Position position) {
			menu.show(position);
		}
	});
~~~

Popup Menüs können auch mit Hilfe von [Menu Models](#menu_models) erzeugt werden. Die Methode: 

~~~
	void setPopupMenu(IMenuModel model);
~~~

erzeugt ein Popup Menü und bindet es an das übergebene Menu Model. Sobald ein Popup Event ausgelöst wurde, wird das zugehörige Popup Menü angezeigt. Das obige Beispiel sieht dann wie folgt aus:

~~~{.java .numberLines startFrom="1"} 
	final MenuModel menu = new MenuModel();

	final ICheckedItemModel option = menu.addCheckedItem("My option");
	option.addItemListener(new IItemStateListener() {
		@Override
		public void itemStateChanged() {
			System.out.println("My option selected: " + option.isSelected());
		}
	});

	frame.setPopupMenu(menu);
~~~

Die Verwendung von MenuModels hat mehrere Vorteile. Im obigen Beispiel spart man sich zum einen den folgenden _Boilerplate Code_:

~~~{.java .numberLines startFrom="1"} 
	frame.addPopupDetectionListener(new IPopupDetectionListener() {
		@Override
		public void popupDetected(final Position position) {
			menu.show(position);
		}
	});
~~~

Zum Anderen kann ein MenuModel wiederverwendet werden. Im folgenden wird das selbe PopMenu auch im Hauptmenü einer Menübar hinzugefügt:

~~~{.java .numberLines startFrom="1"} 
	menu.setText("Main Menu");
	
	final MenuBarModel menuBar = new MenuBarModel();
	menuBar.addMenu(menu);
	
	frame.setMenuBar(menuBar);
~~~

Weitere Information finden sich auch in den Abschnitten [Menüs und Items](#menus_and_items) sowie [Menü und Item Models](#menu_models).
 

#### Farben

Mit Hilfe der folgenden Methoden kann die Vorder- und Hintergrundfarbe umgesetzt werden:

~~~
	void setForegroundColor(final IColorConstant colorValue);

	void setBackgroundColor(final IColorConstant colorValue);
~~~

Nicht alle Komponenten unterstützen diese Funktion für alle SPI Implementierungen und auf allen Betriebssystemen. So kann zum Beispiel unter Windows und SWT nicht die Hintergrundfarbe eines Buttons geändert werden. Durch das Setzen von `null` wird die Default Farbe verwendet.

Um die aktuell gesetzte Farbe zu erhalten, können folgende Methoden verwendet werden:

~~~
	IColorConstant getForegroundColor();

	IColorConstant getBackgroundColor();
~~~

Wurde zuvor keine spezielle Farbe gesetzt und ist somit die Default Farbe aktiv, wird 'null' zurückgegeben.

Zu allgemeinen Verwendung von Farben siehe auch den Abschnitt über [Farben](#colors).


#### Mauszeiger

Mit Hilfe der folgenden Methode kann der Mauszeiger (Cursor) geändert werden:

~~~
	void setCursor(final Cursor cursor);
~~~

#### Der `visible` Status

Der `visible` Status einer Komponente legt fest, ob eine Komponente _potentiell_ sichtbar ist. Der Status kann mit den folgenden Methoden gesetzt und abgefragt werden:

~~~
	void setVisible(final boolean visible);

	boolean isVisible();
~~~

Die Verwendung kann für Fenster und Controls unterschiedlich sein. Ein Fenster wird durch `setVisible(true)` auf dem Bildschirm angezeigt und durch `setVisible(false)` versteckt.   

Bei Controls hängt es vom verwendeten [LayoutManager](#layouting) ab, wie damit verfahren wird. [Mig Layout](#mig_layout) bietet zum Beispiel verschiedene _hidemodes_ für den Umgang mit nicht sichtbaren Controls an.

Der Status `visible==true` sagt nicht aus, dass die Komponente tatsächlich auf dem Bildschirm _sichtbar_ ist. So werden zum Beispiel Controls in nicht sichtbaren `TabItems` (siehe auch [TabFolder](#tabFolder)) mit Status `visible==true` nicht angezeigt. Minimierte Fenster haben, auch wenn sie nicht auf dem Bildschirm sichtbar sind, den Status `visible==true`.

#### Der `showing` Status

Um Informationen über die Sichtbarkeit auf dem Bildschirm zu erlangen, kann der `showing` Status verwendet werden. Dieser wird mittels der folgenden Methode abgefragt:

~~~
	boolean isShowing();
~~~

Der Status ist wie folgt definiert:

Eine _Root_ Komponente hat den Status `showing==true` wenn `visible==true` ist. 
Eine Kind Komponente hat den Status `showing==true` wenn sie den Status `visible==true` __und__ wenn ihr Vater den Status `showing==true` hat. 

Unter der Annahme, das der verwendetet [LayoutManager](#layouting) Controls mit dem Status `visible==false` nicht anzeigt, gilt dann folgendes:

* `showing==false` -> Die Komponente ist definitiv nicht am Bildschirm sichtbar

* `showing==true` -> Die Komponente ist eventuell am Bildschirm sichtbar. (Sie könnte jedoch durch andere Fenster verdeckt sein oder sich im nicht sichtbaren Bereich eines [ScrollComposite](#scrollComposite) befinden und somit dennoch nicht angezeigt werden.)

Der Status `showing==false` kann etwa ausgenutzt werden, um die Performance zu optimieren, indem für nicht dargestellte Controls _Berechnungen_ ausgelassen werden. 

Um sich über Änderungen des `showing` Status informieren zu lassen, kann ein `IShowingStateListener` verwendet werden:

~~~
	void addShowingStateListener(IShowingStateListener listener);

	void removeShowingStateListener(IShowingStateListener listener);
~~~

Dieser hat die folgende Methode: 

~~~
	void showingStateChanged(boolean isShowing);
~~~

#### Größe und Position

Mit folgenden Methoden kann die Größe eine Komponente gesetzt und ausgelesen werden:

~~~
	void setSize(final Dimension size);
	
	void setSize(int width, int height);
	
	Dimension getSize();
~~~

Folgende Methoden dienen zum Setzen und Auslesen der Position:

~~~
	void setPosition(final Position position);
	
	void setPosition(int x, int y);
	
	Position getPosition();
~~~

Bei Controls ist die Position relativ zum Usprung ihrer Vaterkomponente definiert. 

Für Fenster handelt es sich bei der Position um Bildschirmkoordinaten. Dies gilt auch für Kindfenster wie zum Beispiel einen Dialog. Dessen Koordinaten sind __nicht__ relativ zum Vaterfenster zu verstehen.

Der Ursprung eines Koordinatensystems zur Positionsangabe ist immer oben links. Y - Werte verlaufen also von _oben_ nach _unten_. 

Um die Position und Größe einer Komponente _in Einem_ zu setzen, können die folgenden Methoden verwendet werden:

~~~
	void setBounds(Rectangle bounds);
	
	Rectangle getBounds();
~~~

Die Klassen `Dimension`, `Position` und `Rectangle` wurden _immutable_ entworfen. Das bedeutet, dass ihre Koordinaten im Nachhinein nicht modifiziert werden können.

#### Koordinatenumrechung

Zur Umrechnung von Bildschirmkoordinaten in lokale Koordinaten (bezüglich der Vaterkomponente) oder zurück können die folgenden Methoden verwendet werden:

~~~
	Position toLocal(final Position screenPosition);
	
	Position toScreen(final Position localPosition);
~~~

Zur Umrechnung von Koordinaten in ein Koordinatensystem einer anderen Komponente oder zurück können die folgenden Methoden verwendet werden:

~~~
	Position fromComponent(final IComponentCommon component, final Position componentPosition);

	Position toComponent(final Position componentPosition, final IComponentCommon component);
~~~


 
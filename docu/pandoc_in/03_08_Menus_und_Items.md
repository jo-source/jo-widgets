## Menüs und Items{#menus_and_items}

Der folgende Abschnitt gibt eine Einführung in die Verwendung von nativen Menüs und Menü Items in jowidgets. Menüs können auch mit Hilfe von [Menü und Item Models](#menu_models) erstellt werden, was einige Vorteile mit sich bringt. Der hier beschriebene native Ansatz ist mit dem anderer UI Frameworks vergleichbar. 

### Menu Bar{#menu_bar}

Eine Menu Bar ist eine _Menüleiste_ für ein [Frame](#frame_widget). Folgende Abbildung zeigt ein Frame mit einer Menu Bar, welche ein __File__ und ein __Edit__ Menü enthält:

![Menu Bar Beispiel](images/menu_bar_example.gif "Menu Bar Beispiel")

Eine Menu Bar erhält man für ein Frame mit Hilfe der folgende Methode: 
 
~~~
	IMenuBar createMenuBar();
~~~ 

Falls noch keine Menu Bar existiert, wird eine neue erzeugt und zurückgegeben. Andernfalls wir die zuletzt erzeugte (welche noch nicht disposed wurde) zurückgegeben. Es folgt eine kurze Beschreibung der Menu Bar Methoden:

#### Menu Bar Model

Mit Hilfe der folgenden Methoden kann das Model gesetzt und ausgelesen werden. Siehe auch [Menü und Item Models](#menu_models).

~~~
	void setModel(IMenuBarModel model);

	IMenuBarModel getModel();
~~~

#### Hinzufügen von Menüs

Mit Hilfe der folgenden Methoden kann ein [Main Menu](#main_menu) hinzugefügt werden:

~~~
	IMainMenu addMenu(IMainMenuDescriptor descriptor);

	IMainMenu addMenu(int index, IMainMenuDescriptor descriptor);

	IMainMenu addMenu(String name);

	IMainMenu addMenu(String name, char mnemonic);

	IMainMenu addMenu(int index, String name);
~~~

Die ersten beiden Methoden verwenden einen Descriptor (BluePrint) zum Hinzufügen. Bei den anderen Methoden handelt es sich um Convenience Methoden, mit welchen der Menu Name und Mnemonic direkt angeben werden kann. Wird ein `index` angegeben, wird das Menü an der entsprechenden Stelle eingefügt, ansonsten am Ende.

Das folgende Beispiel soll die Verwendung verdeutlichen:

~~~{.java .numberLines startFrom="1"}
	final IMenuBar menuBar = frame.createMenuBar();

	final IMainMenu fileMenu = menuBar.addMenu(BPF.mainMenu().setText("File"));
	final IMainMenu editMenu = menuBar.addMenu(BPF.mainMenu().setText("Edit"));
~~~

Mit Hilfe der Convenience Methoden kann man das auch kürzer schreiben:

~~~{.java .numberLines startFrom="1"}
	final IMenuBar menuBar = frame.createMenuBar();

	final IMainMenu fileMenu = menuBar.addMenu("File");
	final IMainMenu editMenu = menuBar.addMenu("Edit");
~~~

Folgender Aufruf fügt ein Menü zwischen __File__ und __Edit__ ein:

~~~{.java .numberLines startFrom="1"}
	final IMainMenu searchMenu = menuBar.addMenu(1, "Search");
~~~

Die folgende Abbildung zeigt das Gesamtergebnis:

![Menu Bar Beispiel 2](images/menu_bar_example_2.gif "Menu Bar Beispiel 2")

#### Entfernen von Menüs

Ein Menü kann entweder mit Hilfe der folgenden Methoden entfernt werden:

~~~
	boolean remove(IMenu menu);

	void remove(final int index);

	void removeAll();
~~~

oder indem auf dem Menü welches entfernt werden soll, die dispose() Methode aufgerufen wird.

Der Aufruf: 

~~~
	menuBar.remove(searchMenu);
~~~

hat also den gleichen Effekt wie:

~~~
	searchMenu.dispose();
~~~

#### Zugriff auf die Menüs einer Menu Bar

Mit Hilfe der folgenden Methode erhält man eine Liste der vorhandenen Menüs.

~~~
	List<IMenu> getMenus();
~~~

Dabei handelt es sich um eine nicht modifizierbare Kopie der derzeit vorhandenen Menüs.














### Die Schnittstelle IMenu{#menu_interface}

Die Schnittstelle IMenu liefert die Basisfunktionen für das [Main Menu](#main_menu) das [Sub Menu](#sub_menu) sowie das [Popup Menu](#popup_menu). Es folgt eine kurze Beschreibung der wichtigsten Methoden:

#### Menu Model

Mit Hilfe der folgenden Methoden kann das Model gesetzt und ausgelesen werden. Siehe auch [Menü und Item Models](#menu_models).

~~~
	void setModel(IMenuModel model);
	
	IMenuModel getModel();
~~~

#### Hinzufügen von Items

Mit Hilfe der folgenden Methoden können Items zu einem Menü hinzugefügt werden:

~~~
	<WIDGET_TYPE extends IMenuItem> WIDGET_TYPE 
		addItem(IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

	<WIDGET_TYPE extends IMenuItem> WIDGET_TYPE 
		addItem(int index, IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

	IActionMenuItem addAction(IAction action);

	IActionMenuItem addAction(int index, IAction action);

	IMenuItem addSeparator();

	IMenuItem addSeparator(int index);
~~~

Die ersten beiden Methoden verwenden dazu einen Descriptor (BluePrint). Folgende Items können so hinzugefügt werden:

* [Action Menü Item](#action_menu_item)
* [Checked Menu Item](#checked_menu_item)
* [Radio Menu Item](#radio_menu_item)
* [Separator Menu Item](#separator_menu_item)
* [Sub Menu](#sub_menu)

Die Methoden `addAction()` fügen ein [Action Menu Item](#action_menu_item) hinzu, welches an die übergebene `IAction` (siehe [Actions and Commands](#actions_and_commands)) gebunden wird. Mit Hilfe von `addSeparator()` kann ein [Separator Menu Item] mit verkürzter Schreibweise hinzugefügt werden. Wird ein `index` angegeben, wird das Item an der entsprechenden Stelle eingefügt, ansonsten am Ende.

Das folgende Beispiel demonstriert die Verwendung:

~~~{.java .numberLines startFrom="1"}
	IMainMenu menu = menuBar.addMenu("Menu");
	IActionMenuItem action1 = menu.addItem(BPF.menuItem("Action1"));
	IActionMenuItem action2 = menu.addItem(BPF.menuItem("Action1"));
	menu.addItem(BPF.menuSeparator());
	ISelectableMenuItem opt1 = menu.addItem(BPF.checkedMenuItem("Option1").setSelected(true));
	ISelectableMenuItem opt2 = menu.addItem(BPF.checkedMenuItem("Option2"));
	menu.addSeparator();
	ISelectableMenuItem radio1 = menu.addItem(BPF.radioMenuItem("Radio1").setSelected(true));
	ISelectableMenuItem radio2 = menu.addItem(BPF.radioMenuItem("Radio2"));
	ISelectableMenuItem radio3 = menu.addItem(BPF.radioMenuItem("Radio3"));
	menu.addSeparator();
	ISubMenu subMenu = menu.addItem(BPF.subMenu("Sub Menu"));
	IActionMenuItem subaction subMenu.addItem(BPF.menuItem("Subaction"));
~~~

In Zeile 4 wird eine [Separator](#separator_menu_item) mit Hilfe des BluePrint hinzugefügt, in Zeile 7 und 11 mit Hilfe der Convenience Methode `addSeparator()`.

Das Ergebnis sieht wie folgt aus:

![Menu Beispiel 1](images/menu_example_1.gif "Menu Beispiel 1")
 
 
 
 
 
 
### Die Schnittstelle IMenuItem{#menu_item_interface}

Die Schnittstelle `IMenuItem` liefert die Basisfunktionen für alle Menu Items. Dazu zählen das [Action Menü Item](#action_menu_item), [Checked Menu Item](#checked_menu_item), [Radio Menu Item](#radio_menu_item), [Separator Menu Item](#separator_menu_item) und das [Sub Menu](#sub_menu). Ein `IMenuItem` ist von [`IItem`](#item_interface) und somit von [`IWidget`](#widget_interface) abgeleitet. 

Es folgt eine kurze Beschreibung der wichtigsten Methoden:
 
~~~
	IMenuItemModel getModel();

	void setModel(IMenuItemModel model);

	void setMnemonic(char mnemonic);
~~~
 
Mit Hilfe von `getModel()` und `setModel()` wird das Model gesetzt oder geholt (siehe auch [Menü und Item Models](#menu_models)). Der Mnemonic definiert das Tastenkürzel, mit welchen das Item in Kombination mit der Taste ALT (z.B. unter Windows) geöffnet bzw. ausgeführt werden kann. Auf manchen Plattformen wird das Mnemonic Zeichen unterstrichen dargestellt, falls es im Label Text vorkommt.
 
 
 
 
 
 
### Main Menu{#main_menu}

Ein Main Menu ist ein einzelnes Hauptmenü in einer [Menu Bar](#menu_bar). Die Schnittstelle `IMainMenu` hat neben den von [`IMenu`](#menu_interface) und [`IWidget`](#widget_interface) geerbten Methoden die folgenden weiteren:

~~~
	void setText(String text);

	void setMnemonic(char mnemonic);
~~~

Beim `text` handelt es sich um den Label Text des Menüs. Der Mnemonic definiert das Tastenkürzel, mit welchen das Menü in Kombination mit der Taste ALT (z.B. unter Windows) geöffnet werden kann. Auf manchen Plattformen wird das Mnemonic Zeichen unterstrichen dargestellt, falls es im Menü Label Text vorkommt.

#### Main Menu BluePrint

Ein Main Menu kann (u.A.) mit Hilfe eines `IMainMenuBluePrint` erzeugt werden. Die Klasse `BPF` liefert die folgenden Methoden für die Erzeugung eines BluePrint:

~~~
	public static IMainMenuBluePrint mainMenu();
	
	public static IMainMenuBluePrint mainMenu(final String text) {
~~~

Die zweite Methode ermöglicht das gleichzeitige setzen des Label Textes auf dem BluePrint bei der Erzeugung.

Ein `IMainMenuBluePrint` hat die folgenden Methoden zur Konfiguration:  

~~~
	IMainMenuBluePrint setText(String text);

	IMainMenuBluePrint setMnemonic(Character mnemonic);
~~~

Diese definieren, analog zu den Methoden auf `IMainMenu` den Label Text und das Mnemonic.


### Sub Menu{#sub_menu}

Ein Sub Menu ist ein Untermenü eines [`IMenu`](#menu_interface) und ist somit [`IMenu`](#menu_interface) und [`IMenuItem`](#menu_item) zugleich. Die Schnittstelle `ISubMenu` hat neben den von [`IMenu`](#menu_interface), [`IMenuItem`](#menu_item_interface), [`IItem`](#item_interface), [`IWidget`](#widget_interface) und [`ISubMenu`](#subMenu) geerbten __keine__ weiteren Methoden:


#### Sub Menu BluePrint

Ein Sub Menu kann (u.A.) mit Hilfe eines `ISubMenuBluePrint` erzeugt werden. Die Klasse `BPF` liefert die folgenden Methoden für die Erzeugung eines BluePrint:

~~~
	public static ISubMenuBluePrint subMenu() {...}
	
	public static ISubMenuBluePrint subMenu(final String text) {...}
~~~

Die zweite Methode ermöglicht das gleichzeitige setzen des Label Textes auf dem BluePrint bei der Erzeugung.

Ein `ISubMenuBluePrint` hat die folgenden Methoden zur Konfiguration:  

~~~
	ISubMenuBluePrint setText(String text);

	ISubMenuBluePrint setToolTipText(String toolTipText);

	ISubMenuBluePrint setIcon(IImageConstant icon);

	ISubMenuBluePrint setMnemonic(Character mnemonic);
~~~

Mit den ersten drei Methoden kann, analog zu einen [`IItem`](#item_interface) der Label Text, das Tooltip und das Icon gesetzt werden. Mit Hilfe der Methode `setMnemonic()` lässt sich das Mnemonic (vgl. [`IMenuItem`](#menu_item_interface)) festgelegt.





### Popup Menu{#popup_menu}

Ein Popup Menu ist ein Kontexmenü, welches von einer [Komponente](#component_interface) mit Hilfe der folgenden Methode erzeugt werden kann:

~~~
	IPopupMenu createPopupMenu();
~~~

Dabei können für eine Komponente beliebig viele Popup Menüs erzeugt werden.

Die Schnittstelle `IPopupMenu` hat neben den von [`IMenu`](#menu_interface) und [`IWidget`](#widget_interface) geerbten Methoden die folgende weitere zur Anzeige des Menüs:

~~~
	void show(Position position);
~~~

Die `position` muss dabei bezüglich des Koordinatensystems angegeben werden, welches das PopupMenu erzeugt hat und darf nicht `null` sein.


Folgendes Beispiel zeigt die Verwendung eines PopMenüs:

~~~{.java .numberLines startFrom="1"} 
	//create the popup menu
	IPopupMenu menu = frame.createPopupMenu();

	//add items to the menu
	IActionMenuItem action1 = menu.addItem(BPF.menuItem("Action1"));
	IActionMenuItem action2 = menu.addItem(BPF.menuItem("Action1"));
	menu.addSeparator();
	ISelectableMenuItem option1 = menu.addItem(BPF.checkedMenuItem("Option1").setSelected(true));
	ISelectableMenuItem option2 = menu.addItem(BPF.checkedMenuItem("Option2"));
	menu.addSeparator();
	ISelectableMenuItem radio1 = menu.addItem(BPF.radioMenuItem("Radio1").setSelected(true));
	ISelectableMenuItem radio2 = menu.addItem(BPF.radioMenuItem("Radio2"));
	ISelectableMenuItem radio3 = menu.addItem(BPF.radioMenuItem("Radio3"));
	menu.addSeparator();
	ISubMenu subMenu = menu.addItem(BPF.subMenu("Sub Menu"));
	subMenu.addItem(BPF.menuItem("Subaction"));

	//add a popup detection listener that shows the menu
	frame.addPopupDetectionListener(new IPopupDetectionListener() {
		@Override
		public void popupDetected(final Position position) {
			menu.show(position);
		}
	});
~~~

In Zeile 2 wird ein neues PopupMenu erzeugt. In den Zeilen 5 - 16 werden diesem Items hinzugefügt. In Zeile 19 wird ein PopupDetectionListener hinzugefügt, der das Menü bei einem PopupEvent sichtbar macht. Die folgende Abbildung zeigt das Ergebnis:

![Popup Menu Beispiel](images/popup_menu_example.gif "Popup Menu Beispiel")


### Action Menu Item{#action_menu_item}

Ein Action Menu Item ist ein [Menu Item](#menu_item_interface), welches beim _Anklicken_ ein Action Event auslöst. Es folgt eine Beschreibung der wichtigsten Methoden der Schnittstelle `IActionMenuItem`: 

#### Menu Model

Mit Hilfe der folgenden Methoden kann das Model gesetzt und ausgelesen werden. Siehe auch [Menü und Item Models](#menu_models).

~~~
	void setModel(IActionItemModel model);
	
	IActionItemModel getModel();
~~~

#### Action Events

Mit Hilfe der folgenden Methoden kann man sich als Listener registrieren, um Action Events zu erhalten:

~~~
	void addActionListener(IActionListener actionListener);

	void removeActionListener(IActionListener actionListener);
~~~

Ein `IActionListener` hat die folgende Methode:

~~~
	void actionPerformed();
~~~

Diese wird aufgerufen, wenn das Item die Aktion ausführen soll, zum Beispiel durch _Anklicken_ oder die Verwendung eines [Tastaturkürzels](#key_accelerator). Anmerkung: Dies wird als Action Event bezeichnet, auch wenn der `IActionListener` kein explizites Event Objekt übergeben bekommt.

#### Action Binding

Mit der folgenden Methode kann eine `IAction` gebunden werden:

~~~
	void setAction(IAction action);
~~~ 

Weitere Details finden sich im Abschnitt [Actions und Commands](#actions_and_commands)

#### Tastaturkürzel{#key_accelerator}

Mit Hilfe der folgenden Methode kann das Tastaturkürzel (Key Accelerator) festgelegt werden, mit welchen die Aktion (ohne Maus) ausgeführt werden soll:

~~~
	void setAccelerator(Accelerator accelerator);
~~~ 

Ist das zugehörige Item in einem [Main Menu](#main_menu) eines aktiven [Frames](#frame_widget), wird das Tastaturkürzel automatisch ausgewertet und ein Action Event gefeuert, wenn die entsprechende Tastenkombination gedrückt wird. 

Bei Action Items in Popup Menüs ist das __nicht der Fall__. Dort muss man sich, z.B. mit Hilfe eines [KeyListener](#container_key_events), selbst um das Auslösen der Aktionen kümmern.  

#### Action Item BluePrint

Ein Action Item kann (u.A.) mit Hilfe eines `IActionMenuItemBluePrint` erzeugt werden. Die Klasse `BPF` liefert die folgenden Methoden für die Erzeugung eines BluePrint:

~~~
	public static IActionMenuItemBluePrint menuItem() {...}

	public static IActionMenuItemBluePrint menuItem(final String text) {...}
~~~

Die zweite Methode ermöglicht das gleichzeitige setzen des Label Textes auf dem BluePrint bei der Erzeugung.

Ein `IActionMenuItemBluePrint` hat die folgenden Methoden zur Konfiguration:  

~~~
	IActionMenuItemBluePrint setText(String text);

	IActionMenuItemBluePrint setToolTipText(String toolTipText);

	IActionMenuItemBluePrint setIcon(IImageConstant icon);
	
	IActionMenuItemBluePrint setMnemonic(Character mnemonic);
	
	IActionMenuItemBluePrint setAccelerator(Accelerator accelerator);
~~~

Diese können analog zu den Methoden der Schnittstelle `IActionMenuItem` verwendet werden.

Das folgende Beispiel demonstriert die Verwendung von Action Menu Items:

~~~{.java .numberLines startFrom="1"}
	final IActionMenuItemBluePrint copyItemBp = BPF.menuItem();
	copyItemBp
		.setText("Copy")
		.setToolTipText("Copies the selection into the clipboard")
		.setIcon(IconsSmall.COPY)
		.setAccelerator(new Accelerator(VirtualKey.C, Modifier.CTRL));
		
	final IActionMenuItem copyItem = editMenu.addItem(copyItemBp);
	copyItem.addActionListener(new IActionListener() {
		@Override
		public void actionPerformed() {
			System.out.println("Perform copy");
		}
	});

	final IActionMenuItemBluePrint pasteItemBp = BPF.menuItem();
	pasteItemBp
		.setText("Paste")
		.setToolTipText("Pastes the clipboard into the selection")
		.setIcon(IconsSmall.PASTE)
		.setAccelerator(new Accelerator(VirtualKey.V, Modifier.CTRL));
		
	final IActionMenuItem pasteItem = editMenu.addItem(pasteItemBp);
	pasteItem.addActionListener(new IActionListener() {
		@Override
		public void actionPerformed() {
			System.out.println("Perform paste");
		}
	});
~~~

__Bemerkung:__ Für größeren Anwendungen wird anstatt der obigen Vorgehensweise die Verwendung von [Actions](#actions_and_commands) empfohlen.

### Die Schnittstelle ISelectableMenuItem{#selectable_menu_item_interface}

### Checked Menu Item{#checked_menu_item}

### Radio Menu Item{#radio_menu_item}

### Separator Menu Item{#separator_menu_item}
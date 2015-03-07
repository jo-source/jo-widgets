## Menü und Item Models{#menu_models}

Im Abschnitt [Menus und Items](#menus_and_items) wurde die native Verwendung von Menus und Menu Items in jowidgets beschrieben. Dort existiert eine Menü oder Menü Item Instanz genau ein Mal an einer Stelle. Eine Wiederverwendung auf Instanzebene ist __nicht__ möglich^[In Swt und Swing ist das auch nicht möglich!]. 

Auf Klassenebene gibt es bestimmte Einschränkungen bei der Wiederverwendung. Angenommen man möchte ein Menü einer Menu Bar oder Teile davon, wie zum Beispiel ein [Checked Menu Item](#checked_menu_item), noch mal an einer anderen Stelle, zum Beispiel in einen [Popup Menu](#popup_menu) haben, kommt man nicht umher, das gleiche Menü oder Item ein zweites mal zu erzeugen. Will man das Menü nachträglich anpassen, zum Beispiel weil ein Plugin eine _Menu Contribution_ macht, muss man beide Instanzen nachträglich anpassen. Für das Checked Menu Item müssten beide Instanzen, z.B. über ein Presentation Model, aneinander gebunden werden.

Menu und Items Models wurden entworfen, um Menüs und Menü Items zu beschreiben bzw. deren aktuellen Zustand widerzuspiegeln. Ein solches Model (eine Instanz) kann dann an (mehrere) konkrete Items gebunden werden. Änderungen auf dem Model, wie zum Beispiel das Einfügen neuer Menü Items oder das Ändern des `selected` State eines Checked Menu Item, werden dann an allen Stellen synchron gehalten.

Item Models haben eine eindeutige ID. Dadurch ist es zum Beispiel möglich, Menu Contributions wie `addBefore(item, idWhereToAdd)` oder `addAfter(item, idWhereToAdd)` umzusetzen, ohne eine Referenz auf das Item (_where to add_) haben zu müssen ^[Der Label Text sollte dafür auf keinen Fall verwendet werden da dieser internationalisiert sein kann].

Item Models haben einen `visible` Status. Dadurch ist es zum Beispiel möglich, einzelne Aktionen auszublenden, ohne dass das Item Model aus seinem Menü entfernt werden muss (wodurch sich die Struktur ändern würde, was im Zusammenhang mit Menü Contributions wieder zu Problemen führen könnte). Man kann für Action Items [Sichbarkeitsaspekte](#action_item_visibility_aspect) injizieren, was u.A. im Zusammenhang mit Rechtemanagement hilfreich ist. 
 
Item Models beschränken sich nicht ausschließlich auf Menüs. So ist es für bestimmte Items möglich, diese gleichzeitig in einer [Toolbar]({#toolbar_model) und in einem Menu zu haben. Beispielsweise wird ein [Checked Item Model](#checked_item_model) in einem Menu als [Checked Menu Item](#checked_menu_item) und in einer [Toolbar](#toolbar_widget) als [ToolBarToggleButton](#tool_bar_toogle_button) dargestellt. Ein [Action Item Model](#action_item_model) wird in einer Toolbar mit Hilfe eines [ToolBarButton](#tool_bar_button) und in einem Menu als [Action Menu Item](#action_menu_item) angezeigt. Ein [Menu Model](#menu_model) kann sowohl für ein [Main Menu](#main_menu), ein [Sub Menu](#sub_menu) als auch für ein [Toolbar Menu](#tool_bar_menu) verwendet werden.


### Einführendes Beispiel

Die Verwendung von Item Models soll vorab anhand eines Beispiels verdeutlicht werden (das komplette `ItemModelSnipped` findet sich [hier](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/ItemModelSnipped.java)). 

Das Rahmenprogramm sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public final class ItemModelSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create a root frame
		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(400, 300)).setTitle("Menu and Item Models");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

		//Create the menu bar
		final IMenuBarModel menuBar = frame.getMenuBarModel();
		
		//Use a border layout, add toolbar and composite
		frame.setLayout(BorderLayout.builder().gap(0).build());
		final IToolBarModel toolBar = frame.add(BPF.toolBar(), BorderLayout.TOP).getModel();
		final IComposite composite = frame.add(BPF.composite().setBorder(), BorderLayout.CENTER);

		//set the root frame visible
		frame.setVisible(true);
	}
}
~~~

Es wird ein Frame verwendet, welches mittels eines [Border Layout](#border_layout) eine [Toolbar](#toolbar_widget) und ein [Composite](#composite) darstellt. Zudem wird eine [Menu Bar](#menu_bar) erzeugt. 

Der folgende Code beinhaltet die Erzeugung der Items und Menüs:

~~~{.java .numberLines startFrom="1"}
	//create a checked item for filter
	final CheckedItemModel filter = new CheckedItemModel("Filter", IconsSmall.FILTER);
	filter.setSelected(true);

	//create save action (with constructor)
	final ActionItemModel save = new ActionItemModel("Save", IconsSmall.DISK);
	save.setAccelerator(VirtualKey.S, Modifier.CTRL);

	//create copy action (with constructor)
	final ActionItemModel copy = new ActionItemModel("Copy", IconsSmall.COPY);
	copy.setAccelerator(VirtualKey.C, Modifier.CTRL);

	//create paste action (with builder)
	final IActionItemModel paste 
		= ActionItemModel
			.builder()
			.setText("Paste")
			.setIcon(IconsSmall.PASTE)
			.setAccelerator(VirtualKey.V,Modifier.CTRL)
			.build();

	//create a menu and add items
	final MenuModel menu = new MenuModel("Menu");
	menu.addItem(save);
	menu.addItem(copy);
	menu.addItem(paste);
	menu.addSeparator();
	menu.addItem(filter);
	menu.addSeparator();

	//create a sub menu and add some items
	final IMenuModel subMenu = menu.addMenu("Sub Menu");
	final IActionItemModel action1 = subMenu.addActionItem("Action 1");
	final IActionItemModel action2 = subMenu.addActionItem("Action 2");
	final IActionItemModel action3 = subMenu.addActionItem("Action 3");

	//add the menu to the menu bar
	menuBar.addMenu(menu);

	//sets the menu as popup menu on the composite
	composite.setPopupMenu(menu);

	//add some actions and items to the toolbar
	toolBar.addItem(save);
	toolBar.addItem(copy);
	toolBar.addItem(paste);
	toolBar.addSeparator();
	toolBar.addItem(filter);
	toolBar.addSeparator();

	//add menu to the toolbar
	toolBar.addItem(menu);
~~~

Anschließend werden zu einigen Items Listener hinzugefügt, welche deren Funktion mit Hilfe einer Konsolenausgabe belegen:

~~~{.java .numberLines startFrom="1"}
	//add listeners to the items
	filter.addItemListener(new SysoutSelectionListener(filter));
	save.addActionListener(new SysoutActionListener(save));
	copy.addActionListener(new SysoutActionListener(copy));
	paste.addActionListener(new SysoutActionListener(paste));
	action1.addActionListener(new SysoutActionListener(action1));
	action2.addActionListener(new SysoutActionListener(action2));
	action3.addActionListener(new SysoutActionListener(action3));
~~~

Die folgenden Abbildungen zeigen das Ergebnis, jeweils mit geöffnetem Main Menu, ToolBar Menu und Popup Menu:

![ItemModelSnipped - Main Menu](images/menu_models_example_1.gif "ItemModelSnipped - Main Menu")
![ItemModelSnipped - Toolbar Menu](images/menu_models_example_2.gif "ItemModelSnipped - Toolbar Menu")
![ItemModelSnipped - Popup Menu](images/menu_models_example_3.gif "ItemModelSnipped - Popup Menu")

Deaktiviert man in der Toolbar nun zum Beispiel den _Filter_ Toggle Button, wird auch das entsprechende Checked Menu Item in den drei vorhandenen Menüs deaktiviert und auf dem `filter` Item ein Selection Event _gefeuert_.

![ItemModelSnipped - Checked Item Binding](images/menu_models_example_5.gif "ItemModelSnipped - Checked Item Binding")

Im folgenden wird nun _Action2_ ausgeblendet:

~~~
	action2.setVisible(false);
~~~

Die Abbildung zeigt das Ergebnis:

![ItemModelSnipped - Sichtbarkeit](images/menu_models_example_4.gif "ItemModelSnipped - Sichtbarkeit")

Action2 wird in den anderen Menüs selbstverständlich auch nicht mehr angezeigt. Mit Hilfe von `setVisible(false)` wird der gleiche visuelle Effekt erzielt, als hätte man:

~~~
	subMenu.removeItem(action2);
~~~

ausgeführt. Allerdings müsste man hier zum wieder sichtbar machen das Folgende ausführen:

~~~
	subMenu.addItem(1, action2);
~~~

was bedeutet, dass man den Index kennen muss, wo das Item eingefügt werden soll, während man im ersten Fall mit

~~~
	action2.setVisible(true);
~~~

auskommt. 

Anmerkung: An welchem Index man eine Action wieder sichtbar machen müsste, ist bei mehr als einem ausgeblendeten Item nicht mehr trivial, und ohne genaue Kenntnis aller Menüeinträge und deren Sichtbarkeit nicht lösbar.


















### Menu Bar Model{#menu_bar_model}

Ein Menu Bar Model ist ein Model für eine [Menu Bar](#menu_bar). Es folgt eine Beschreibung der wichtigsten Methoden der Schnittstelle `IMenuBarModel`:


#### Hinzufügen von existierenden Menu Models

Mit Hilfe der folgenden Methoden können bereits existierende [Menu Models](#menu_model) hinzugefügt werden:

~~~
	void addMenu(IMenuModel menu);

	void addMenu(int index, IMenuModel menu);
~~~

#### Hinzufügen von Menu Models mit Hilfe von Menu Model Buildern

Mit den folgenden Methoden lassen sich Menus mit Hilfe von Menu Model Buildern hinzufügen:

~~~
	IMenuModel addMenu(IMenuModelBuilder menuBuilder);

	IMenuModel addMenu(int index, IMenuModelBuilder menuBuilder);
~~~

Dabei wird beim Hinzufügen die `build()` Methode aufgerufen, das gebaute Menu Model hinzugefügt und zurückgegeben. Damit können zum Beispiel Konstrukte der folgenden Art realisiert werden:

~~~{.java .numberLines startFrom="1"}
	final IMenuModel menu1 = menuBar.addMenu(
			MenuModel
				.builder()
				.setId(MENU_1_ID)
				.setText("Menu1")
				.setMnemonic('1'));
		
	final IMenuModel menu2 = menuBar.addMenu(
			MenuModel
				.builder()
				.setId(MENU_2_ID)
				.setText("Menu2")
				.setMnemonic('2'));
~~~

#### Erstellen und Hinzufügen von Menüs mit einem Aufruf

Die folgenden Methoden erlauben das erstellen und Hinzufügen von Menüs mit einem Aufruf:

~~~
	IMenuModel addMenu();

	IMenuModel addMenu(String text);
~~~

Dabei wird ein neues Menu Model erzeugt, hinzugefügt und zurückgegeben. Das folgende Beispiel demonstriert die Verwendung:

~~~{.java .numberLines startFrom="1"}
	final IMenuModel menu1 = menuBar.addMenu("Menu1");
	final IMenuModel menu2 = menuBar.addMenu("Menu2");
	final IMenuModel menu3 = menuBar.addMenu("Menu3");
~~~

#### Hinzufügen von Menüs relativ zu anderen Menüs

Die folgenden Methoden können verwendet werden, um ein Item an eine bestimmte Stelle in einer Menu Bar hinzuzufügen:

~~~
	void addBefore(IMenuModel newMenu, String id);
	
	void addAfter(IMenuModel newMenu, String id);
~~~

Der `id` gibt dabei das Menu an, bezüglich welcher das neue Menü hinzugefügt werden soll. Ist dieses nicht vorhanden, wird eine `IllegalArgumentException` geworfen. Mittels der Methode `findMenuById(String id)` kann vorab überprüft werden, ob ein solches Menü vorhanden ist. 

Die Verwendung soll anhand eines Beispiels demonstriert werden. Der folgende Code erzeugt eine Menu Bar mit drei Menüs wobei das letzte ein Hilfe Menu mit fester id ist:  

~~~{.java .numberLines startFrom="1"}
	final IMenuModel menu1 = menuBar.addMenu("Menu1");
	final IMenuModel menu2 = menuBar.addMenu("Menu2");
		
	IMenuModel helpMenu = menuBar.addMenu(
			MenuModel
				.builder()
				.setId(HELP_MENU_ID)
				.setText("Help"));
~~~

An einer anderen Stelle könnte man eine Contribution zur Menu Bar wie folgt machen:

~~~{.java .numberLines startFrom="1"}
	final IMenuModel customMenu = new MenuModel("Custom menu");
	menuBar.addBefore(customMenu, HELP_MENU_ID);
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Menu Bar Contribution Beispiel](images/menu_bar_contrib_example.gif "Menu Bar Contribution Beispiel")

#### Hinzufügen der Menüs einer anderen Menu Bar

Mit Hilfe der folgenden Methode werden alle Menüs der übergebenen Menu Bar hinzugefügt:

~~~
	void addMenusOfModel(IMenuBarModel model);
~~~

Dabei wird eine Referenz der Items hinzugefügt und keine Kopie!

#### Entfernen von Menüs

Mit Hilfe der folgenden Methoden können Menüs aus einer Menu Bar entfernt werden:

~~~
	void removeMenu(final IMenuModel item);

	void removeMenu(int index);

	void removeAllMenus();
~~~

#### ListModelListener

Um sich über das Hinzufügen oder Entfernen von Menüs informieren zu lassen, kann ein `IListModelListener` verwendet werden:

~~~
	void addListModelListener(IListModelListener listener);

	void removeListModelListener(IListModelListener listener);
~~~

Dieser hat die folgenden Methoden:

~~~
	void afterChildAdded(int index);

	void beforeChildRemove(int index);

	void afterChildRemoved(int index);
~~~

#### Zugriff auf die Menüs einer Menu Bar

Die folgende Methode liefert eine nicht modifizierbare Kopie der aktuell vorhandenen Menüs der Menu Bar.

~~~
	List<IMenuModel> getMenus();
~~~

Um ein Menu Anhand einer id zu finden, kann die folgende Methode verwendet werden:

~~~
	IMenuModel findMenuById(String id);
~~~

Existiert kein solches Menü in der Menu Bar, wird null zurückgegeben.

#### Kopieren einer Menu Bar

Mit Hilfe der folgenden Methode kann eine Kopie (_Deep Copy_) einer Menu Bar erzeugt werden. Die Kopie hat dabei die _gleichen_ Menüs wie das Original (jedoch nicht die _Selben_). Registrierte Listener werden nicht mitkopiert. Dadurch sind bereits gebundene Items nicht an die Kopie gebunden.
 
~~~
	IMenuBarModel createCopy();
~~~

#### Menu Bar Model Instanzen

Die Klasse `org.jowidgets.tools.model.item.MenuBarModel` liefert eine Implementierung der `IMenuBarModel` Schnittstelle. Eine neu Instanz kann entweder mittels `new` wie folgt erzeugt werden:

~~~
	IMenuBarModel menuBar = new MenuBarModel();
~~~

oder man verwendet die statische `create()` Methode:

~~~	
	IMenuBarModel menuBar = MenuBarModel.create()
~~~ 

Man kann eine Instanz aber auch direkt von einem [Frame](#frame_widget) mittels 

~~~
	IMenuBarModel getMenuBarModel();
~~~

oder von einer [Menu Bar](#mebu_bar) mittels

~~~
	IMenuBarModel getModel();
~~~

erhalten.

Um ein existierendes Menu Bar Model auf einem Frame zu setzen, kann die folgende Methode verwendet werden:

~~~
	void setMenuBar(IMenuBarModel model);
~~~























### Die Schnittstelle IItemModel{#item_model_interface}

Die Schnittstelle IItemModel liefert die Basisfunktionen für alle Item Models. Es werden nicht für jeden Item Typ alle Attribute ausgewertet, bzw. an ein konkretes Item gebunden. So kann zum Beispiel der `enabled` State nur für Action Items gesetzt werden, weil andere Items ein `disable` nicht ermöglichen. Es folgt eine kurze Beschreibung der wichtigsten Methoden:

#### Die Item ID

Mit Hilfe der folgenden Methode erhält man die id eines Items: 

~~~
	String getId();
~~~

Die id darf weder `null` noch ein Leerstring sein. Die id sollte möglichst global eindeutig sein. Wird keine id definiert, wird per default eine UUID verwendet. Die Item id wird für die Implementierung von `equals()` und `hashCode()` herangezogen. Zwei Items sind genau dann gleich, wenn ihre id's gleich sind. Es ist nicht möglich, zwei Items mit der gleichen id zu einem Menü hinzuzufügen. 

#### Label, Tooltip und Icon

Mit Hilfe der folgenden Methoden können der Label Text, das Tooltip und das Icon gesetzt und ausgelesen werden:

~~~
	void setText(final String text);

	String getText();

	void setToolTipText(String toolTipText);

	String getToolTipText();

	void setIcon(IImageConstant icon);

	IImageConstant getIcon();
~~~

#### Tastatursteuerung

Mit den folgenden Methoden erhält man Zugriff auf Mnemonic und Key Accelerator:

~~~
	void setAccelerator(Accelerator accelerator);

	void setAccelerator(final VirtualKey key, final Modifier... modifier);

	Accelerator getAccelerator();

	void setMnemonic(Character mnemonic);

	void setMnemonic(char mnemonic);

	Character getMnemonic();
~~~

#### Der Enabled State

Mit den folgenden Methoden kann der `enabled` State gesetzt und ausgelesen werden:

~~~
	void setEnabled(boolean enabled);

	boolean isEnabled();
~~~

Aktionen, welche nicht enabled sind, können nicht ausgeführt werden, und werden in der Regel _ausgegraut_ dargestellt.

#### Der Visible State

Mit Hilfe der folgenden Methoden kann der `visible` State gesetzt und ausgelesen werden:

~~~
	void setVisible(boolean visible);

	boolean isVisible();
~~~

Items welche nicht `visible` sind, werden im zugehörigen Menü nicht angezeigt, ohne das das Item aus dem Menü Model entfernt werden muss.

#### ItemModelListener

Mit Hilfe der folgenden Methoden kann ein `IItemModelListener` registriert oder deregistriert werden:

~~~
	void addItemModelListener(IItemModelListener listener);

	void removeItemModelListener(IItemModelListener listener);
~~~

Dieser hat die folgende Methode:

~~~
	void itemChanged(IItemModel item);
~~~

Diese wird immer aufgerufen, wenn ein Item geändert wurde, wie zum Beispiel der Label Text, das Icon oder der `enabled` oder `visible` State.

#### Kopieren von Item Models

Mit Hilfe der folgenden Methode kann eine Kopie (_Deep Copy_) eines Item Models erzeugt werden. Die Kopie hat dabei die _gleichen_ Items wie das Original (jedoch nicht die _Selben_). Registrierte Listener werden nicht mitkopiert. Dadurch sind bereits gebundene Items nicht an die Kopie gebunden.
 
~~~
	IItemModel createCopy();
~~~

### Die Schnittstelle IItemModelBuilder{#item_model_builder}

Jedes Item Model kann mit Hilfe eines Builders erzeugt werden. Die Schnittstelle `IItemModelBuilder` liefert die gemeinsamen Methoden aller Item Model Builder. Sie hat die folgenden Methoden:

~~~
	BUILDER_TYPE setId(String id);

	BUILDER_TYPE setText(final String text);

	BUILDER_TYPE setToolTipText(String toolTipText);

	BUILDER_TYPE setIcon(IImageConstant icon);

	BUILDER_TYPE setAccelerator(Accelerator accelerator);

	BUILDER_TYPE setAccelerator(final VirtualKey key, final Modifier... modifier);

	BUILDER_TYPE setAccelerator(final char key, final Modifier... modifier);

	BUILDER_TYPE setMnemonic(Character mnemonic);

	BUILDER_TYPE setMnemonic(char mnemonic);

	BUILDER_TYPE setEnabled(boolean enabled);

	ITEM_TYPE build();
~~~ 

Der Builder Type, welchen die oberen Methoden als Rückgabewert haben, ist dabei der konkrete Typ des Builders, welchen man verwendet. Dadurch können die Methoden, wie beim Builder Pattern üblich, verkettet aufgerufen werden. Die Semantik der Methoden ist analog zur Schnittstelle [`IItemModel`](#item_model_interface).

Die Methode `build()` liefert ein neues Item zurück. Der Typ hängt vom konkreten Builder ab. Zum Beispiel gibt ein `IMenuModelBuilder` ein `IMenuModel` zurück.

Das folgende Beispiel soll die Verwendung demonstrieren:

~~~{.java .numberLines startFrom="1"}
	final IActionItemModel pasteActionItem 
		= ActionItemModel
			.builder()
			.setText("Paste")
			.setIcon(IconsSmall.PASTE)
			.setAccelerator(VirtualKey.V,Modifier.CTRL)
			.build();
~~~




















### Menu Model{#menu_model}

Ein Menu Model ist ein Model für Menüs. Dazu zählen die [Main Menüs](#main_menu) in einer [Menu Bar](#menu_bar), [Sub Menüs](#sub_menu), [Popup Menüs](#popup_menu) oder [Toolbar Menüs](#tool_bar_menu). Es folgt eine Beschreibung der zusätzlich zu [`IItemModel`](#item_model_interface) vorhandenen Methoden der Schnittstelle `IMenuModel`:

#### Hinzufügen von existierenden Items

Mit Hilfe der folgenden Methoden können bereits existierende Items hinzugefügt werden:

~~~
	void addItem(final IMenuItemModel item);

	void addItem(final int index, final IMenuItemModel item);
~~~

#### Hinzufügen von Items mit Hilfe von Item Model Buildern

Mit den folgenden Methoden lassen sich Items mit Hilfe von Item Model Buildern hinzufügen:

~~~
	<MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> 
		MODEL_TYPE addItem(final BUILDER_TYPE itemBuilder);

	<MODEL_TYPE extends IMenuItemModel, BUILDER_TYPE extends IItemModelBuilder<?, MODEL_TYPE>> 
		MODEL_TYPE addItem(int index, final BUILDER_TYPE itemBuilder);
~~~

Dabei wird beim Hinzufügen die `build()` Methode aufgerufen, das gebaute Item hinzugefügt und zurückgegeben. Damit können zum Beispiel Konstrukte der folgenden Art realisiert werden:

~~~{.java .numberLines startFrom="1"}
	IActionItemModel action = menu.addItem(ActionItemModel.builder().setText("Action"));
	IMenuModel subMenu = menu.addItem(MenuModel.builder().setText("SubMenu"));
~~~

#### Erstellen und Hinzufügen von Items mit einem Aufruf

Die folgenden Methoden erlauben das Erstellen und Hinzufügen von Items mit einem Aufruf:

~~~
	IActionItemModel addAction(IAction action);

	IActionItemModel addAction(final int index, IAction action);

	IActionItemModel addActionItem();

	IActionItemModel addActionItem(String text);

	IActionItemModel addActionItem(String text, String toolTipText);

	IActionItemModel addActionItem(String text, IImageConstant icon);

	IActionItemModel addActionItem(String text, String toolTipText, IImageConstant icon);

	ICheckedItemModel addCheckedItem();

	ICheckedItemModel addCheckedItem(String text);

	ICheckedItemModel addCheckedItem(String text, String toolTipText);

	ICheckedItemModel addCheckedItem(String text, IImageConstant icon);

	ICheckedItemModel addCheckedItem(String text, String toolTipText, IImageConstant icon);

	IRadioItemModel addRadioItem();

	IRadioItemModel addRadioItem(String text);

	IRadioItemModel addRadioItem(String text, String toolTipText);

	IRadioItemModel addRadioItem(String text, IImageConstant icon);

	IRadioItemModel addRadioItem(String text, String toolTipText, IImageConstant icon);

	ISeparatorItemModel addSeparator();

	ISeparatorItemModel addSeparator(String id);

	ISeparatorItemModel addSeparator(int index);

	IMenuModel addMenu();

	IMenuModel addMenu(String text);

	IMenuModel addMenu(String text, String toolTipText);

	IMenuModel addMenu(String text, IImageConstant icon);

	IMenuModel addMenu(String text, String toolTipText, IImageConstant icon);
~~~

Dabei wird (mit Hilfe der Parameter) ein neues Item Model erzeugt, hinzugefügt und zurückgegeben. Das folgende Beispiel demonstriert die Verwendung:

~~~{.java .numberLines startFrom="1"}
	MenuModel menu = new MenuModel("Menu");

	IActionItemModel action1 = menu.addActionItem("Action1");
	IActionItemModel action2 = menu.addActionItem("Action2");
	IActionItemModel action3 = menu.addActionItem("Action3");

	menu.addSeparator();
	ICheckedItemModel option1 = menu.addCheckedItem("Option1");
	ICheckedItemModel option2 = menu.addCheckedItem("Option2");
	option2.setSelected(true);

	menu.addSeparator();
	IRadioItemModel radio1 = menu.addRadioItem("Radio1");
	radio1.setSelected(true);
	IRadioItemModel radio2 = menu.addRadioItem("Radio2");
	IRadioItemModel radio3 = menu.addRadioItem("Radio3");

	IMenuModel subMenu = menu.addMenu("Submenu");
	IActionItemModel action4 = subMenu.addActionItem("Action4");
	IActionItemModel action5 = subMenu.addActionItem("Action5");
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Menu Model Beispiel](images/menu_model_fluent_example.gif "Menu Model Beispiel")

#### Hinzufügen von Items relativ zu anderen Items

Die folgenden Methoden können verwendet werden, um ein Item an eine bestimmte Stelle in einem Menu (inklusive Sub Menüs) hinzuzufügen:

~~~
	void addBefore(IMenuItemModel newItem, String... idPath);
	
	void addAfter(IMenuItemModel newItem, String... idPath);
~~~

Der `idPath` gibt dabei den Pfad der id´s an, über welche die Einfügestelle lokalisiert werden kann. Ist diese nicht vorhanden, wird eine `IllegalArgumentException` geworfen. Mittels der Methode `findItemByPath(String... idPath)` kann vorab überprüft werden, ob der Pfad vorhanden ist. 

Die Verwendung soll anhand eines Beispiels demonstriert werden. Der folgende Code erzeugt ein Menü und fügt einen Separator hinzu (Zeile 3), zu dem man Custom Actions hinzugefügen können soll: 

~~~{.java .numberLines startFrom="1"}
	final MenuModel menu = new MenuModel("Menu");	
	menu.addItem(save);
	menu.addSeparator(CUSTOM_ACTIONS);
	menu.addSeparator(EDIT_ACTIONS);
	menu.addItem(copy);
	menu.addItem(paste);
~~~

An einer anderen Stelle könnte man eine Contribution zum Menü wie folgt machen:

~~~{.java .numberLines startFrom="1"}
	final ActionItemModel customAction = new ActionItemModel("Custom action");
	menu.addAfter(customAction, CUSTOM_ACTIONS);
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Menu Contribution Beispiel](images/menu_contribution_example.gif "Menu Contribution Beispiel")

#### Hinzufügen von Items eines anderen Menüs

Mit Hilfe der folgenden Methode werden alle Items der übergebenen Menüs hinzugefügt:

~~~
	void addItemsOfModel(IMenuModel menuModel);
~~~

Dabei wird eine Referenz der Items hinzugefügt und keine Kopie!

#### Entfernen von Items

Mit Hilfe der folgenden Methoden können Items aus einem Menü entfernt werden:

~~~
	void removeItem(final IMenuItemModel item);

	void removeItem(int index);

	void removeAllItems();
~~~

#### ListModelListener

Um sich über das Hinzufügen oder Entfernen von Items informieren zu lassen, kann ein `IListModelListener` verwendet werden:

~~~
	void addListModelListener(IListModelListener listener);

	void removeListModelListener(IListModelListener listener);
~~~

Dieser hat die folgenden Methoden:

~~~
	void afterChildAdded(int index);

	void beforeChildRemove(int index);

	void afterChildRemoved(int index);
~~~

#### Zugriff auf die Items eines Menüs

Die folgende Methode liefert eine nicht modifizierbare Kopie der aktuell vorhandenen Menu Item Models des Menu Model.

~~~
	List<IMenuItemModel> getChildren();
~~~

Um ein Item Anhand eines id Pfades zu finden, kann die folgende Methode verwendet werden:

~~~
	IMenuItemModel findItemByPath(String... idPath);
~~~

Existiert kein solches Item im Menü, wird null zurückgegeben.

#### Action Decorator

Mit Hilfe der folgenden Methoden kann ein Decorator hinzugefügt werden, der alle Actions des Menüs (inklusive aller Untermenüs) dekoriert:
 
~~~
	void addDecorator(IDecorator<IAction> decorator);

	void removeDecorator(IDecorator<IAction> decorator);
~~~

Die Dekorierung betrifft alle derzeit vorhanden und zukünftig hinzugefügten Actions. Für weitere Details zu Actions siehe auch [Actions und Commands](#actions_and_commands).

#### Menu Model Builder 

Die Schnittstelle `IMenuModelBuilder` ist von [`IItemModelBuilder`](#item_model_builder) abgeleitet und liefert einen konkreten Builder für Menu Models. Sie hat keine zusätzlichen Methoden. Eine Instanz erhält man von der Klasse `org.jowidgets.tools.model.item.MenuModel`.

#### Menu Model Instanzen

Die Klasse `org.jowidgets.tools.model.item.MenuModel` liefert zum Einen statische Methoden für die Erzeugung eines `IMenuModelBuilder`. Zum Anderen implementiert die Klasse die Schnittstelle `IMenuModel`. Das folgende Beispiel zeigt die Verwendung des Builders:

~~~{.java .numberLines startFrom="1"}
	final IMenuModel menu 
			= MenuModel
				.builder()
				.setText("Edit menu")
				.setIcon(IconsSmall.EDIT)
				.build();
~~~

Mit Hilfe einer Instantiierung mittels `new` kann das gleiche so erreicht werden:

~~~{.java .numberLines startFrom="1"}
	IMenuModel menu = new MenuModel("Edit menu", IconsSmall.EDIT);
~~~















### Action Item Model{#action_item_model}

Ein Action Item Model ist ein Model für Items welche Aktionen auslösen. Dazu zählen [Action Menu Items](#action_menu_item) und [Tool Bar Buttons](#tool_bar_button). Die Schnittstelle `IActionItemModel` ist von [`IItemModel`](#item_model_interface) abgeleitet. Es folgt eine Beschreibung der wichtigsten weiteren Methoden:

#### Action binding

Mit Hilfe der folgenden Methoden kann eine Action an ein Action Item Model gebunden, sowie die gebundene Action abgefragt werden:

~~~
	void setAction(IAction action);

	IAction getAction();
~~~ 

Ein Action Item Model muss nicht zwingend an eine Action gebunden sein, so dass `null` sowohl gesetzt werden kann, als auch als Rückgabewert möglich ist. Für weitere Details zu Actions siehe auch [Actions und Commands](#actions_and_commands).

#### Action Dekorierung

Die folgenden Methoden können verwendet werden, um einen Action Dekorierer hinzuzufügen oder zu entfernen. 

~~~
	void addDecorator(IDecorator<IAction> decorator);

	void removeDecorator(IDecorator<IAction> decorator);
~~~

Dieser wird verwendet, um eine gebundene Action zu dekorieren. Für weitere Details zu Actions siehe auch [Actions und Commands](#actions_and_commands).


#### ActionListener

Mit Hilfe der folgenden Methoden kann ein `IActionListener` hinzugefügt oder entfernt werden:

~~~
	void addActionListener(final IActionListener actionListener);

	void removeActionListener(final IActionListener actionListener);
~~~

Dieser hat die folgende Methode: 

~~~
	void actionPerformed();
~~~

Die Methode wird aufgerufen, wenn auf einem gebundenen Item eine Aktion (z.B. durch Klicken oder Tastaturkürzel) ausgelöst wurde.

#### Action Item Model Builder 

Die Schnittstelle `IActionItemModelBuilder` ist von [`IItemModelBuilder`](#item_model_builder) abgeleitet und liefert einen konkreten Builder für Action Item Models. Sie hat folgende zusätzliche Methoden.

~~~
	IActionItemModelBuilder setAction(IAction action);

	IActionItemModelBuilder addVisibilityAspect(IActionItemVisibilityAspect visibilityAspect);
~~~

Die erste Methode dient zum Binding einer [Action](#actions_and_commands), die zweite Methode fügt einen [Sichtbarkeitsaspket](#action_item_visibility_aspect) hinzu.

Eine Instanz erhält man von der Klasse `org.jowidgets.tools.model.item.ActionItemModel`.

#### Action Item Model Instanzen

Die Klasse `org.jowidgets.tools.model.item.ActionItemModel` liefert zum Einen statische Methoden für die Erzeugung eines `IActionItemModelBuilder`. Zum Anderen implementiert die Klasse die Schnittstelle `IActionItemModel`. Das folgende Beispiel zeigt die Verwendung des Builders:

~~~{.java .numberLines startFrom="1"}
	final IActionItemModel paste 
		= ActionItemModel
			.builder()
			.setText("Paste")
			.setIcon(IconsSmall.PASTE)
			.setAccelerator(VirtualKey.V,Modifier.CTRL)
			.build();
~~~

Mit Hilfe einer Instantiierung mittels `new` kann das gleiche so erreicht werden:

~~~{.java .numberLines startFrom="1"}
	final IActionItemModel paste = new ActionItemModel("Paste", IconsSmall.PASTE);
	paste.setAccelerator(VirtualKey.V, Modifier.CTRL);
~~~


### Die Schnittstelle ISelectableMenuItemModel{#selectable_menu_item_model_interface}

Die Schnittstelle `ISelectableItemModel` liefert die Funktionen für ein [Checked Item Model](#checked_item_model) und ein [Radio Item Model](#radio_item_model). Die Schnittstelle ist von [`IItemModel`](#item_model_interface) abgeleitet. Es folgt eine Beschreibung der weiteren Methoden:

#### Der Selected State

Mit Hilfe der folgenden Methoden kann der `selected` State gesetzt und ausgelesen werden:

~~~
	boolean isSelected();

	void setSelected(boolean selected);
~~~

Um sich über Änderungen des `selected` State informieren zu lassen, kann ein `IItemStateListener` verwendet werden:

~~~
	void addItemListener(final IItemStateListener listener);

	void removeItemListener(final IItemStateListener listener);
~~~

Dieser hat die folgende Methode:

~~~
	void itemStateChanged();
~~~











### Checked Item Model{#checked_item_model}

Ein Checked Item Model ist ein Model für Items welche eine unabhängige Option anzeigen. Dazu zählen [Checked Menu Items](#checked_menu_item) und [Toolbar Toggle Buttons](#tool_bar_toogle_button). Die Schnittstelle `ICheckedItemModel` ist von [`ISelectableMenuItemModel`](#selectable_menu_item_model_interface) abgeleitet und hat keine weiteren Methoden.

#### Checked Item Model Builder 

Die Schnittstelle `ICheckedItemModelBuilder` ist von [`IItemModelBuilder`](#item_model_builder) abgeleitet und liefert einen konkreten Builder für Checked Item Models. Sie hat die folgende zusätzliche Methode:

~~~
	ICheckedItemModelBuilder setSelected(boolean selected);
~~~

Eine Instanz erhält man von der Klasse `org.jowidgets.tools.model.item.CheckedItemModel`.

#### Checked Item Model Instanzen

Die Klasse `org.jowidgets.tools.model.item.CheckedItemModel` liefert zum Einen statische Methoden für die Erzeugung eines `ICheckedItemModelBuilder`. Zum Anderen implementiert die Klasse die Schnittstelle `ICheckedItemModel`. Das folgende Beispiel zeigt die Verwendung des Builders:

~~~{.java .numberLines startFrom="1"}
	final ICheckedItemModel filter
			= CheckedItemModel
				.builder()
				.setText("Filter")
				.setToolTipText("Indicates if filter is active or not")
				.setIcon(IconsSmall.FILTER)
				.setAccelerator(VirtualKey.F, Modifier.ALT)
				.build();
~~~

Mit Hilfe einer Instantiierung mittels `new` kann das gleiche so erreicht werden:

~~~{.java .numberLines startFrom="1"}
	final ICheckedItemModel filter = new CheckedItemModel(
			"Filter", 
			"Indicates if filter is used or not", 
			IconsSmall.FILTER);
	filter.setAccelerator(VirtualKey.F, Modifier.ALT);
~~~










### Radio Item Model{#radio_item_model}

Ein Radio Item Model ist ein Model für Items welche eine Option innerhalb einer Optionsliste anzeigen. Dazu zählt das [Radio Menu Item](#radio_menu_item). Die Schnittstelle `IRadioItemModel` ist von [`ISelectableMenuItemModel`](#selectable_menu_item_model_interface) abgeleitet und hat keine weiteren Methoden.

#### Radio Item Model Builder 

Die Schnittstelle `IRadioItemModelBuilder` ist von [`IItemModelBuilder`](#item_model_builder) abgeleitet und liefert einen konkreten Builder für Radio Item Models. Sie hat die folgende zusätzliche Methode:

~~~
	IRadioItemModelBuilder setSelected(boolean selected);
~~~

Eine Instanz erhält man von der Klasse `org.jowidgets.tools.model.item.RadioItemModel`.

#### Radio Item Model Instanzen

Die Klasse `org.jowidgets.tools.model.item.RadioItemModel` liefert zum Einen statische Methoden für die Erzeugung eines `IRadioItemModelBuilder`. Zum Anderen implementiert die Klasse die Schnittstelle `IRadioItemModel`. Das folgende Beispiel zeigt die Verwendung des Builders:

~~~{.java .numberLines startFrom="1"}
	final IRadioItemModel low 
			= RadioItemModel
				.builder()
				.setText("Low latency")
				.setToolTipText("Uses low latency which may lead to high workload")
				.setAccelerator(VirtualKey.L, Modifier.CTRL)
				.build();

	final IRadioItemModel med 
			= RadioItemModel
				.builder()
				.setText("Medium latency")
				.setToolTipText("Uses medium latency which may lead to balanced workload")
				.setAccelerator(VirtualKey.M, Modifier.CTRL)
				.setSelected(true)
				.build();

	final IRadioItemModel high
			= RadioItemModel
				.builder()
				.setText("High latency")
				.setToolTipText("Uses high latency which may lead to low workload")
				.setAccelerator(VirtualKey.H, Modifier.CTRL)
				.build();
~~~

Mit Hilfe einer Instantiierung mittels `new` kann das gleiche so erreicht werden:

~~~{.java .numberLines startFrom="1"}
	final IRadioItemModel low = new RadioItemModel(
			"Low latency",
			"Uses low latency which may lead to high workload");
	low.setAccelerator(VirtualKey.L, Modifier.CTRL);
		
	final IRadioItemModel med = new RadioItemModel(
			"Medium latency",
			"Uses medium latency which may lead to balanced workload");
	med.setAccelerator(VirtualKey.M, Modifier.CTRL);
		
	final IRadioItemModel high = new RadioItemModel(
			"High latency",
			"Uses high latency which may lead to low workload");
	high.setAccelerator(VirtualKey.H, Modifier.CTRL);
~~~





### Separator Item Model

Ein Separator Item Model ist ein Model für Separator Items. Dazu zählen [Separator Menu Items](#separator_menu_item) und [Toolbar Separators](#tool_bar_separator). Die Schnittstelle `ISeparatorItemModel` ist von [`IItemModel`](#item_model_interface) abgeleitet und hat keine weiteren Methoden.

#### Separator Item Model Builder 

Die Schnittstelle `ISeparatorItemModelBuilder` ist von [`IItemModelBuilder`](#item_model_builder) abgeleitet und liefert einen konkreten Builder für Separator Item Models. Sie hat keine zusätzlichen Methoden. Eine Instanz erhält man von der Klasse `org.jowidgets.tools.model.item.ISeparatorItemModel`.

#### Separator Item Model Instanzen

Die Klasse `org.jowidgets.tools.model.item.SeparatorItemModel` liefert zum Einen statische Methoden für die Erzeugung eines `ISeparatorItemModelBuilder`. Zum Anderen implementiert die Klasse die Schnittstelle `ISeparatorItemModel`. Das folgende Beispiel zeigt die Verwendung des Builders:

~~~{.java .numberLines startFrom="1"}
	ISeparatorItemModel separator
			= SeparatorItemModel
				.builder()
				.setId(CUSTOM_ACTIONS)
				.build();
~~~

Mit Hilfe einer Instantiierung mittels `new` kann das gleiche so erreicht werden:

~~~{.java .numberLines startFrom="1"}
	ISeparatorItemModel separator = new SeparatorItemModel(CUSTOM_ACTIONS);
~~~





### Menu Model Key Binding
TODO org.jowidgets.tools.model.item.MenuModelKeyBinding





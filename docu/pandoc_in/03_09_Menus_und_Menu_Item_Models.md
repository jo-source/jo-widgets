## Menü und Item Models{#menu_models}

Im Abschnitt [Menus und Items](#menus_and_items) wurde die native Verwendung von Menus und Menu Items in jowidgets beschrieben. Dort existiert eine Menü oder Menü Item Instanz genau ein Mal an einer Stelle. Eine Wiederverwendung auf Instanzebene ist __nicht__ möglich^[In Swt und Swing ist auch nicht möglich!]. 

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

Es wird ein Frame verwendet, welches mittels eines [Border Layout](#border_layout) eine [Toolbar](#toolbar_widget) und ein [Composite](#composite) darstellt. Zudem wird eine [Menu Bar](#menu_bar) erzeugt. Der folgende Code beinhaltet die Erzeugung der Items und Menüs:

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

### Die Schnittstelle IItemModel

### Menu Model{#menu_model}

### Action Item Model{#action_item_model}

### Die Schnittstelle ISelectableMenuItemModel

### Checked Item Model{#checked_item_model}

### Radion Item Model

### Separator Item Model

### Menu Model Key Binding
TODO org.jowidgets.tools.model.item.MenuModelKeyBinding


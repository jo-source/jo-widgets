### Die Schnittstelle IContainer{#container_interface}

Ein Container ist eine Komponente, welche eine Liste von [Controls](#control_interface) enthält. Die Schnittstelle `IContainer` erweitert [`IComponent`](#component_interface) und somit auch [`IWidget`](#widget_interface). Die Controls eines Containers werden mit Hilfe eines [Layouters](#layouting) angeordnet. Es folgt eine Beschreibung der wichtigsten Methoden.

#### Hinzufügen von Controls mit Hilfe von BluePrints

Folgende Methoden können verwendet werden, um Controls mit Hilfe eines [BluePrints](#blue_prints) zu einem Container hinzuzufügen:

~~~
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		int index,
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		Object layoutConstraints);
		
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		int index,
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor, 
		Object layoutConstraints);

	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);
~~~

Ein Control wird hinzugefügt, indem man ein BluePrint hinzufügt. Jedes BluePrint implementiert die Schnittstelle `IWidgetDescriptor` für einen konkreten `WIDGET_TYPE`. Die `add()` Methode liefert das erzeugte Control als Rückgabewert.

Der Index bestimmt, an welcher Stelle das Control in die interne Liste eingefügt werden soll. Wird kein Index angegeben, wird das Control am Ende hinzugefügt. 

Die `layoutConstraints` werden vom verwendeten Layouter ausgewertet und sind somit _layout spezifisch_. Da ein Layouter nicht immer Constraints benötigt, können diese auch weggelassen werden. Zudem ist es möglich, die Constraints nachträglich auf dem Control zu setzen.

Es folgt ein Beispiel für die Verwendung der `add()` Methode:

~~~{.java .numberLines startFrom="1"} 
	final String growCC = "growx, w 0::";
		
	final IInputField<Date> dateField = container.add(BPF.inputFieldDate(), growCC);

	final IComboBoxSelectionBluePrint<Gender> genderCmbBp = BPF.comboBoxSelection(Gender.values());
	final IComboBox<Gender> genderCmb = container.add(genderCmbBp, growCC);

	final IButton okButton = container.add(BPF.buttonOk());

	final ICheckBoxBluePrint licenceCbBp = BPF.checkBox().setText("Accept");
	final ICheckBox licenceCb = container.add(2, licenceCbBp);
~~~
 
In Zeile 3-8 wird ein Datumsfeld, eine `ComboBox` für die Enum `Gender` sowie ein `OkButton` hinzugefügt. Das Datumsfeld sowie die Combobox haben ein [MigLayout](#mig_layout) Constraint, dass sie den ganzen horizontalen Platz ausfüllen sollen (`growx`).

In Zeile 10-11 wird dann eine Checkbox an Position zwei (zwischen Combobox und Botton) eingefügt.

#### Hinzufügen von eigenen (Custom) Controls

Will man selbst Controls erstellen, hat man folgende Möglichkeiten:

   *  Man erstellt eine eigene Widget Bibliothek, siehe [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries). Dabei wird auch ein BluePrint für das Control definiert welches man wie weiter oben beschrieben verwenden kann. Dies ist das empfohlene Vorgehen, wenn das Control wiederverwendet werden soll, da ein solch erstelltes Widget alle Vorzüge von jowidgets bietet.

   *  Man leitet von einem [Basis Widget](#base_widgets) ab, um ein Widget zu kapseln.

   *  Man kapselt ein Widgets mit Hilfe eines `ICustomWidgetCreator`. 

Folgende Methoden können verwendet werden, um Controls mit Hilfe eines `ICustomWidgetCreator` zu einem Container hinzuzufügen:

~~~
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		int index, 
		ICustomWidgetCreator<WIDGET_TYPE> creator, 
		Object layoutConstraints);
		
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		int index, 
		ICustomWidgetCreator<WIDGET_TYPE> creator);

	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		ICustomWidgetCreator<WIDGET_TYPE> creator, 
		Object layoutConstraints);

	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		ICustomWidgetCreator<WIDGET_TYPE> creator);
~~~

Die Verwendung des `index` und der `layoutConstraints` ist analog wie beider Verwendung von BluePrints.

Es folgt ein Beispiel für eine Implementierung der `ICustomWidgetCreator` Schnittstelle:

~~~{.java .numberLines startFrom="1"}
public final class ErrorLabel implements ICustomWidgetCreator<ILabel> {

	private final String errorText;

	public ErrorLabel(final String errorText) {
		this.errorText = errorText;
	}

	@Override
	public ILabel create(final ICustomWidgetFactory widgetFactory) {
		final ILabelBluePrint labelBp = BPF.label();
		labelBp.setText(errorText).setIcon(IconsSmall.ERROR);
		final ILabel label = widgetFactory.create(labelBp);
		return label;
	}
}
~~~

In Zeile 13 wird die `ICustomWidgetFactory` verwendet, um ein `ILabel` Widget zu erzeugen.

Das `ErrorLabel` Control kann nun wie folgt einem Container hinzugefügt werden:

~~~{.java .numberLines startFrom="1"}
	final ILabel label = container.add(new ErrorLabel("Ups!!!"));
~~~

Das folgende Beispiel Implementiert ein Control, welches den gesamten Platz mit schwarzer Farbe ausfüllt:

~~~{.java .numberLines startFrom="1"}
public final class BlackBox implements ICustomWidgetCreator<IControl> {

	@Override
	public IControl create(final ICustomWidgetFactory widgetFactory) {
		final ICanvas canvas = widgetFactory.create(BPF.canvas());

		canvas.addPaintListener(new IPaintListener() {
			@Override
			public void paint(final IPaintEvent paintEvent) {
				final IGraphicContext gc = paintEvent.getGraphicContext();
				final Rectangle bounds = gc.getBounds();
				gc.setBackgroundColor(Colors.BLACK);
				gc.clearRectangle(
					bounds.getX(), 
					bounds.getY(), 
					bounds.getWidth(), 
					bounds.getHeight());
			}
		});

		return canvas;
	}
}
~~~

Hier wird in Zeile 5 die `ICustomWidgetFactory` verwendet, um ein Canvas zu erzeugen welches im `IPaintListener` ab Zeile 7 den gesammten Bereich mit schwarzer Farbe ausfüllt.

Das `BlackBox` Control kann dann die folgt einem Container hinzugefügt werden:

~~~{.java .numberLines startFrom="1"}
	final IControl = frame.add(new BlackBox(), "growx, w 0::");
~~~

#### Entfernen von Controls 

Controls können entweder auf dem Container mittels der folgenden Methoden entfernt werden:

~~~
	boolean remove(IControl control);
	
	void removeAll();
~~~

Oder man ruft auf dem Control selbst die `dispose()` Methode auf:

~~~{.java .numberLines startFrom="1"}
	final IInputField<Date> dateField = container.add(BPF.inputFieldDate(), growCC);
	dateField.dispose();
~~~

In Zeile 1 wird ein `DateField` zum Container hinzugefügt, in Zeile 2 wird es wieder entfernt.

Im folgenden Beispiel wird der gleiche Effekt erzielt, nur dass das Entfernen über den Container stattfindet.

~~~{.java .numberLines startFrom="1"}
	final IInputField<Date> dateField = container.add(BPF.inputFieldDate(), growCC);
	container.remove(dateField);
~~~

In beiden Fällen wird das `DateField` disposed und kann anschließend nicht mehr verwendet werden.

#### Verwendung von Layouts

Folgende Methoden können verwendet werden, um das Layout für den Container festzulegen.

~~~
	void setLayout(ILayoutDescriptor layoutDescriptor);

	<LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(ILayoutFactory<LAYOUT_TYPE> layoutFactory);
~~~	

Das Layout kann entweder über ein `ILayoutDescriptor` oder über eine `ILayoutFactory` festgelegt werden. Der resultierende `ILayouter` ist dafür verantwortlich, die Controls eines Containers in der _richtigen_ Größe an die _richtige_ Position zu Zeichnen. 

Mittels der folgenden Methoden kann der Layoutprozess _manuell_ angestoßen werden:

~~~
	void layoutBegin();

	void layoutEnd();
	
	void layout();

	void layoutLater();
~~~

Die Methode `layout()` berechnet das Layout für den Container neu.

Die Methode `layoutBegin()` kann verwendet werden, um anzuzeigen, dass mehrere Änderungen des Containers folgen, welche das Layout beeinflussen. Dadurch wird verhindert, dass der Container neu gezeichnet wird, bis die Methode `layoutEnd()` aufgerufen wird. Diese kann verwendet werden, um _Flackereffekte_ zu vermeiden.

Die Methode `layoutLater()` führt ein Layout in einem späteren UI Event durch. __Mehrere__ Aufrufe der Methode `layoutLater()` im aktuellen Event bewirken dabei nur __einen__ späteren Aufruf der Methode `layout()`. 

Damit lässt sich ein in der Praxis gelegentlich auftretendes Problem lösen, welches bewirken kann, dass viele _unabhängige_ Änderungen eines Containers  in einem Ui Event ein sofortiges `layout()` nach sich ziehen. Dann wird in einem Event mehrfach ein layout() durchgeführt, obwohl das Ergebnis nicht mehr als ein Mal pro UI Event sichtbar werden kann. De facto wird also nur der letzte `layout()` Aufruf für den Nutzer sichtbar, die vielen anderen `layout()` Aufrufe haben dabei unnötiger Weise den UI Thread blockiert. Durch den mehrfachen Aufruf der Methode `layoutLater()` anstatt `layout()` tritt das Problem dann nicht mehr auf, weil nur ein mal zu einem späteren Zeitpunkt ein `layout()` durchgeführt wird.

Um einen [eigenen Layouter](#custom_layouts) zu implementieren, sind die folgenden Methoden relevant:

~~~
	Rectangle getClientArea();

	Dimension computeDecoratedSize(Dimension clientAreaSize);
~~~

Die Methode `getClientArea()` gibt genau den Bereich zurück, welcher für das Zeichnen der Controls zur Verfügung steht.

Die Methode `computeDecoratedSize()` berechnet für eine gewünschte `clientAreaSize` die notwendige Größe des gesamten Containers. 

Für weitere Details zu diesem Thema sei auf den Abschnitt [Layouting](#layouting) verwiesen.

#### Tab Order

Um die Reihenfolge festzulegen, in welcher durch die Controls durch Verwendung der Tabulator Taste navigiert wird, können die folgenden Methoden verwendet werden:

~~~
	void setTabOrder(Collection<? extends IControl> tabOrder);

	void setTabOrder(IControl... controls);
~~~

Wird null übergeben, wird die _default_ Reihenfolge verwendet

#### Zugriff auf die Controls eines Containers 

Mit Hilfe der folgenden Methode erhält man alle derzeit vorhanden Kind Controls eines Containers:

~~~
	List<IControl> getChildren();
~~~

Dabei handelt es sich um eine nicht modifizierbare (unmodifieable) Kopie der aktuellen Liste der Kind Controls. Dadurch ist zum Beispiel das folgende möglich, ohne eine `ConcurrentModificationException` zu bekommen:

~~~{.java .numberLines startFrom="1"}
	container.layoutBegin();
	for (final IControl childControl : container.getChildren()) {
		if (!filter.accept(childControl)) {
			container.remove(childControl);
		}
	}
	container.layoutEnd();
~~~

Um sich darüber informieren zu lassen, wenn Kinder hinzugefügt oder entfernt werden, kann ein `IContainerListener` verwendet werden:

~~~
	void addContainerListener(IContainerListener listener);

	void removeContainerListener(IContainerListener listener);
~~~

Dieser hat die folgenden Methoden:

~~~
	void afterAdded(IControl control);

	void beforeRemove(IControl control);
~~~

Die Methode `afterAdded()` wird aufgerufen, nachdem ein Control zum Container hinzugefügt wurde. Die Methode `beforeRemove()` wird aufgerufen, bevor ein Control vom Container entfernt wird. Dabei spielt es keine Rolle, ob das Control mittels `dispose()` oder mittels `remove()` entfernt wird. Das Control ist zum Zeitpunkt des Aufrufs der Methode `beforeRemove()` __noch nicht__ disposed. 

#### Rekursiver Zugriff auf die Controls eines Containers 

Ein Container kann Controls enthalten, welche selbst wiederum Container sein können. Dies ist zum Beispiel mit Hilfe eines [Composite](#composite) möglich, welches ein `IContainer` und ein `IControl` zugleich ist. 

In der Praxis kann es vorkommen, dass zu einem eigenen Control Controls hinzugefügt werden, deren interner Aufbau einem über die Zeit (Controls können kommen und gehen) verborgen ist. Dennoch hätte man eventuell gerne Kenntnis über alle Kinder eines Containers (auch rekursiv), zum Beispiel um einen Listener hinzuzufügen, welcher ein PopupMenu öffnet. 

Zu diesem Zweck kann eine `IContainerRegistry` verwendet werden, welche dem Container hinzugefügt wird:

~~~
	void addContainerRegistry(IContainerRegistry registry);

	void removeContainerRegistry(IContainerRegistry registry);
~~~

Die Schnittstelle `IContainerRegistry` hat die folgenden Methoden:

~~~
	void register(IControl control);

	void unregister(IControl control);
~~~

Die Methode `register()` wird für alle bereits vorhandenen (zum Zeitpunkt des Aufrufes von `addContainerRegistry()`) Controls sowie für alle in der Zukunft hinzugefügten Controls aufgerufen. Das ganze wird _rekursiv_ durchgeführt, wodurch auch alle Kind Controls sowie deren Kinder usw. erfasst werden.

Werden Controls (oder Kind Controls, usw.) entfernt, wird die Methode `unregister()` aufgerufen. Kind Controls welche bereits vor dem Aufruf der Methode `addContainerRegistry()` entfernt wurden, werden dabei nicht berücksichtigt.

Zum rekursiven hinzufügen von Listenern zu einem Container und dessen Kindern stehen folgende Methoden zur Verfügung:

~~~
	void addComponentListenerRecursive(IListenerFactory<IComponentListener> listenerFactory);

	void removeComponentListenerRecursive(IListenerFactory<IComponentListener> listenerFactory);

	void addFocusListenerRecursive(IListenerFactory<IFocusListener> listenerFactory);

	void removeFocusListenerRecursive(IListenerFactory<IFocusListener> listenerFactory);

	void addKeyListenerRecursive(IListenerFactory<IKeyListener> listenerFactory);

	void removeKeyListenerRecursive(IListenerFactory<IKeyListener> listenerFactory);

	void addMouseListenerRecursive(IListenerFactory<IMouseListener> listenerFactory);

	void removeMouseListenerRecursive(IListenerFactory<IMouseListener> listenerFactory);

	void addPopupDetectionListenerRecursive(IListenerFactory<IPopupDetectionListener> listenerFactory);

	void removePopupDetectionListenerRecursive(IListenerFactory<IPopupDetectionListener> listenerFactory);
~~~

Die Schnittstelle `IListenerFactory` sieht wie folgt aus:

~~~
public interface IListenerFactory<LISTENER_TYPE> {

	LISTENER_TYPE create(IComponent component);
}
~~~

Durch das Hinzufügen eines rekursiven Listeners wird für den Container selbst und für jedes aktuelle und in der Zukunft hinzugefügte Control die `create()` Methode aufgerufen. Wird durch den Implementierer der `IListenerFactory` Schnittstelle ein Listener erzeugt, wird dieser der Component hinzugefügt, und vor dem dispose der Component wieder entfernt. Die `create()` Methode kann auch `null` zurückgeben, wodurch für diese Component kein Listener hinzugefügt wird.

Das folgende Beispiel soll dies verdeutlichen:

~~~{.java .numberLines startFrom="1"}
	final IPopupMenu labelMenu = container.createPopupMenu();
	labelMenu.setModel(labelMenuModel);

	IListenerFactory<IPopupDetectionListener> listenerFactory;
	listenerFactory = new IListenerFactory<IPopupDetectionListener>() {
		@Override
		public IPopupDetectionListener create(final IComponent component) {
			if (component instanceof ILabel) {
				final ILabel label = (ILabel) component;
				return new IPopupDetectionListener() {
					@Override
					public void popupDetected(final Position position) {
						labelMenuModel.setLabel(label);
						labelMenu.show(label.toComponent(position, container));
					}
				};
			}
			return null;
		}
	};
		
	container.addPopupDetectionListenerRecursive(listenerFactory);
~~~

Für alle Labels eines Containers wird ein Kontextmenü (`labelMenu`) hinzugefügt. Bevor das Menü angezeigt wird, wird das Label auf dem zugehörigen Model (Zeile 13) gesetzt, um etwaige Aktionen bezüglich des ausgewählten Labels durchführen zu können. 

Das Kontextmenü könnte zum Beispiel Aktionen enthalten, welche den Inhalt des Labels in die Zwischenablage kopieren oder dessen Farbe ändern, etc.

 
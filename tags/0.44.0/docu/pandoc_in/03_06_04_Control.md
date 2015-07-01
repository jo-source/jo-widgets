### Die Schnittstelle IControl{#control_interface}

Die Schnittstelle `IControl` stellt die gemeinsamen Funktionen für alle Controls bereits. `IControl` erweitert [`IComponent`](#component_interface) und somit auch [`IWidget`](#widget_interface). 

Controls stellen Schnittstellen zur Nutzerinteraktion dar. Beispiele für elementare Controls sind Buttons, Checkboxen, Comboboxen, Eingabefelder, Slider etc. Controls können aber auch komplex sein, wie zum Beispiel ein [InputComposite](#input_composite).

Controls werden immer zu [Containern](#container_interface) hinzugefügt. Es folgt eine kurze Übersicht der wichtigsten Methoden:

#### Tooltip

Mit der folgenden Methode kann der Tooltip Text gesetzt werden:

~~~
	void setToolTipText(String toolTip);
~~~

Wird `null` gesetzt, dann wird für das Control kein Tooltip angezeigt. 

#### Layout Constraints

Mit den folgenden Methoden können die Layout Constraints für ein Control gesetzt und ausgelesen werden.

~~~
	void setLayoutConstraints(Object layoutConstraints);

	Object getLayoutConstraints();
~~~

Die Layout Constraints können `null` sein. In der Regel werden die Layout Constraints bereits beim Hinzufügen zum Container gesetzt:

~~~{.java .numberLines startFrom="1"}
	final IInputField<Date> dateField = container.add(BPF.inputFieldDate(), "growx, w 0::");
~~~

In diesem Fall würde die Methode `dateField.getLayoutConstraints()` den String `"growx, w 0::"` zurückgeben.

Die Layout Constraints sind _Layouter spezifisch_, das bedeutet, ein Layouter definiert, welche Constraints gültig sind. 

Werden die Layout Constraints nach dem _layouten_ des zugehörigen Containers geändert, so muss dieser neu _gelayoutet_ werden.

Weitere Informationen finden sich im Abschnitt [Layouting](#layouting)

#### Default Größen

Mittels der folgenden Methoden können die Default Größen für ein Control ermittelt werden.

~~~
	Dimension getMinSize();

	Dimension getPreferredSize();

	Dimension getMaxSize(); 
~~~

Die Methoden dürfen nicht `null` zurückgeben. Werden eigene Controls entworfen, sollte darauf geachtet werden, _sinnvolle_ Default Größen zu liefern.

Die `MinSize` definiert die minimale Größe, die ein Control haben soll, damit es sinnvoll angezeigt werden kann. Die `PreferredSize` gibt an, wie groß ein Control sein soll, um es optimal anzuzeigen. Die `MaxSize` definiert, wie groß ein Control maximal angezeigt werden soll. Die Default Größen können sich zur Laufzeit ändern. So ändert sich Beispielsweise die `PreferredSize` eines Textfeldes abhängig vom gesetzten Text.

Einige Layouter wie zum Beispiel [Mib Layout](#mib_layout) bieten die Möglichkeit, die Default Größen mit Hilfe von Constraints zu überschreiben. Die MigLayout Constraints `"w 0:100:200"` setzen zum Beispiel die `MinWidth` auf `0`, die `PreferredWidth` auf `100` und die `MaxWidth` auf `200`, unabhängig davon, was die Methoden `getMinSize()`, `getPreferredSize()` und `getMaxSize()` für Werte zurückliefern. Andere Layouter wie zum Beispiel das FillLayout ignorieren die Default Größen vollständig.

Mit Hilfe der folgenden Methoden können die Default Größen eines Controls geändert werden:

~~~
	void setMinSize(final Dimension minSize);

	void setPreferredSize(Dimension preferredSize);

	void setMaxSize(Dimension maxSize);
~~~

Nach dem Umsetzen einer Default Größe geben die zugehörigen Getter Methoden den neuen Wert zurück. Wird eine Default Größe auf `null` gesetzt, wird vom zugehörigen Getter wieder der __Default Wert__ und nicht `null` zurückgegeben.

#### Drag and Drop

Mit den folgenden Methoden kann einem Control _Drag and Drop_ Funktionalität hinzugefügt werden:

~~~
	IDragSource getDragSource();

	IDropTarget getDropTarget();
~~~

Für weitere Informationen sei auf den Abschnitt [Drag and Drop](#drag_and_drop) verwiesen.


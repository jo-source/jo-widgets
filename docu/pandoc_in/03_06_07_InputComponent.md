### Die Schnittstelle IInputComponent{#input_component_interface}

InputComponents liefern eine Nutzereingabe für einen definierten Datentyp und stellen diesen Wert dar. Dieser kann sowohl einfach sein (z.B. String, Integer, Date, …) als auch komplex (z.B. Person, Company, Rule, …). Die Schnittstelle `IInputComponent<VALUE_TYPE>` erweitert [`IComponent`](#component_interface) und somit auch [`IWidget`](#widget_interface). Zudem ist ein InputComponent von [`IValidatable`](#validatetable_interface) abgeleitet und kann somit seinen aktuellen Zustand selbst validieren. Konkrete Implementierungen sind zum Beispiel [InputField](#input_field), [InputComposite](#input_composite), [InputDialog](#input_dialog), [ComboBox](#combobox_widget), [CheckBox](#checkbox_widget), [Slider](#slider_widget), [Slider Viewer](#slider_viewer) etc..

Es folgt eine kurze Beschreibung der wichtigsten Methoden:

#### Value Access

Mittels der folgenden Methoden kann der Wert gesetzt und ausgelesen werden:

~~~
	void setValue(VALUE_TYPE value);

	VALUE_TYPE getValue();
~~~

Der Wert `null` ist beim Setzen erlaubt und daher auch beim `getValue()` möglich. 

#### InputListener

Um sich über Änderungen des Wertes informieren zu lassen, kann ein `IInputListener` verwendet werden:

~~~
	void addInputListener(IInputListener listener);

	void removeInputListener(IInputListener listener);
~~~

Dieser hat die folgende Methode:

~~~
	void inputChanged();
~~~


#### Validierung

Ein InputComponent ist von [`IValidatable`](#validatetable_interface) abgeleitet. Es kann also ein `IValidationConditionListener` registriert werden, der aufgerufen wird, wenn sich die Validierungsbedingungen geändert haben.

Mit Hilfe der folgenden Methode kann ein externer Validierer hinzugefügt werden.

~~~  
	void addValidator(IValidator<VALUE_TYPE> validator);
~~~

Er wird immer ausgewertet, wenn sich die Validierungsbedingungen geändert haben. Sein Ergebnis fließt in die Implementierung der Methode `validate()` von [`IValidatable`](#validatetable_interface) ein. Ein `IInputComponent` kann zusätzliche interne Validierer besitzen.


#### InputChanged Events vs. ValidationConditionChanged Events{#inputChangeVsValidationCondChanged}

Wenn der Nutzer die Eingabe eines `IInputComponent` ändert, kann sowohl ein `inputChanged()` auf den registrierten `IInputListener` Objekten als auch ein `validationConditionChanged()` auf den registrierten `IValidationConditionListener` Objekten aufgerufen werden. 

Dabei wird __immer__ zuerst das `inputChanged()` aufgerufen, und anschließend das `validationConditionChanged()`. Da InputComponents aus anderen InputComponents bestehen können, kann man dadurch sicherstellen, das erst das Binding mit Hilfe von `inputChanged()` durchgeführt wird, und anschließend die Validierung.  __Implementierer der Schnittstelle `IInputComponent` müssen darauf achten, dies einzuhalten.__

Es kann vorkommen, dass `validationConditionChanged()` aufgerufen wird, ohne das ein Aufruf von `inputChanged()` vorausgeht. Dies soll an einem Beispiel verdeutlicht werden. Ein Eingabefeld zur Eingabe eines Datums verwendet ein Textfeld zur Eingabe von Zeichenketten (String) für die Implementierung, welche intern in ein Date Objekt konvertiert werden. Ein interner Validierer verwendet den Eingabetext für Validierungen. Obwohl sich der Text geändert hat, und dadurch ein `validationConditionChanged()` ausgelöst wird, kann es sein, das `getValue()` vorher und nachher `null` ist, weil die Zeichenkette sich weder vor noch nach der Änderung in ein Date Objekt konvertieren lässt. Es wäre aber möglich, dass der Validierer für die Zeichenkette unterschiedliche Ergebnisse hat, da es vor der Änderung durch das Hinzufügen von Zeichen noch richtig werden könnte (INFO_ERROR), danach aber nicht mehr (ERROR).

#### Modfication State

Zum Auslesen und zurücksetzten des `modifcationState` können die folgenden Methoden verwendet werden:

~~~
	boolean hasModifications();

	void resetModificationState();
~~~

Der ModificationState gibt an, ob es seit der Erzeugung oder seit dem letzten Aufruf von `resetModificationState()` Änderungen an der Komponente gab. Die Methode `resetModifciationState()` macht __nicht__ die eigentliche Modifikation rückgängig, sondern bewirkt, dass der `modificationState` auf `false` zurückgesetzt wird.

Der `modificationState` bezieht sich im Allgemeinen __nicht__ auf den per `getValue()` verfügbaren Wert, sondern darauf, ob der Nutzer irgend eine Änderung vorgenommen hat. Siehe dazu auch [InputChanged Events vs. ValidationConditionChanged Events](#inputChangeVsValidationCondChanged). Der `modificationState` ist zum Beispiel relevant, wenn der Nutzer unterschiedliche [Validation Messages](#validation_message) angezeigt bekommen soll, abhängig ob etwas durch Ihn geändert wurde oder nicht.

#### Editierbarkeit

Mit Hilfe der folgenden Methoden kann die Editierbarkeit gesetzt und abgefragt werden:

~~~
	void setEditable(boolean editable);

	boolean isEditable();
~~~


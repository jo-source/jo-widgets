## Die Validation API{#validation_api}

Die Validation API bietet Schnittstellen und Funktionen für die Validierung von Objekten und findet zum Beispiel Verwendung beim [InputField](#input_field), dem [InputComposite](#input_composite), dem [InputDialog](#input_dialog) oder dem [ValidationLabel](#validation_label). Darüber hinaus kann die Validation API auch für die Verwendung eigener [Komponenten](#component_interface) herangezogen werden. 

Die Validation API befindet sich im Modul `org.jowidgets.validation`. Dieses hat weder jowidgets interne noch externe transitive Abhängigkeiten. Insbesondere hat die API dadurch auch keine Abhängigkeiten auf UI Aspekte und kann somit auch für die serverseitige Validierung herangezogen werden. 

Sie bildet die Basis für die [jo-client-platform](http://code.google.com/p/jo-client-platform/) Bean Validation, welche Adapter für die [Javax Bean Validation (JSR 303)](http://beanvalidation.org/) bereitstellt. 

Im Vergleich zu einer Bean Validation API liefert die Validation API keine besonderen Aspekte bezüglich der Validierung von Properties eines Beans, sondern ausschließlich Aspekte für die Validierung von Objekten. Diese können natürlich sowohl Beans als Bean Properties sein. Die Validation API ist damit allgemeiner als eine Bean Validation API.

Die jowidgets Validation API unterstützt im Vergleich zur Javax Bean Validation API eine differenzierte Unterscheidung von Fehlertypen. Neben `ok` und `error` gibt es weitere Typen wie `warning`, `info`, etc. (siehe auch [Message Types](#validation_message_type)).

Die Validation API wurde nicht entworfen um Javax Bean Validation zu ersetzen, sondern um damit zu _koexistieren_. Javax Bean Validatoren lassen sich zum Beispiel einfach auf [jo-client-platform](http://code.google.com/p/jo-client-platform/) Bean Validatoren adaptieren. Siehe zum Beispiel [BeanPropertyValidatorAdapter](https://code.google.com/p/jo-client-platform/source/browse/trunk/modules/core/org.jowidgets.cap.common/src/main/java/org/jowidgets/cap/common/impl/BeanPropertyValidatorAdapter.java) 

### Die Schnittstelle IValidator{#validator_interface}

Ein Validator validiert eine oder mehrere Bedingungen für einen gegebenen Wert. Die Schnittstelle sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public interface IValidator<VALUE_TYPE> {

	IValidationResult validate(VALUE_TYPE value);

}
~~~

Der zu validierende Wert kann `null` sein. Das Ergebnis einer Validierung muss __ungleich null__ sein. Ein [Validation Result](#validation_result) besteht aus einer Liste von [Validation Messages](#validation_message) welche einen [Message Type](#validation_message_type) haben.

Das folgende Beispiel implementiert einen `NotNullValidator`:

~~~{.java .numberLines startFrom="1"}
public final class NotNullValidator<VALUE_TYPE> implements IValidator<VALUE_TYPE> {

	@Override
	public IValidationResult validate(final VALUE_TYPE value) {
		if (value == null){
			return ValidationResult.error("Must not be null");
		}
		else{
			return ValidationResult.ok();
		}
	}

}
~~~

#### OkValidator{#ok_validator}

Die statische Accessor Klasse `Validator` liefert einen OkValidator mit Hilfe der folgenden statische Methode:

~~~
	public static <VALUE_TYPE> IValidator<VALUE_TYPE> okValidator() {...}
~~~ 

Dieser Validator liefert für alle Werte `ValidationResult.ok()` zurück.

### Message Type{#validation_message_type}

Die Enum `org.jowidgets.validation.MessageType` liefert die möglichen Message Typen. Diese werden in der folgenden Tabelle dargestellt:

     Type           Valid    
----------------  -----------   
OK                   yes
INFO                 yes
WARNING              yes
INFO_ERROR            no
ERROR                 no

Die Message Typen sind nach ihrem Schweregrad (Severity) sortiert, wobei `Error` der _schwerste_ Fehler ist. In der Spalte _Valid_ wird angegeben, ob der validierte Wert als _valide_ einstuft wird. Valide Werte können weiter verarbeitet werden, zum Beispiel indem sie in die Datenbank geschrieben, oder für eine andersartige Transaktion verwendet werden. Nicht valide Werte sind abzulehnen. 

Es folgt eine kurze Beschreibung der Semantik der einzelnen Message Typen:

*  Ein [Validation Result](#validation_result) hat gar keine oder genau eine __OK__ Message ohne Text, wenn die Validierung erfolgreich war und es keine weiteren Informationen oder Warnungen zur Validierung gibt. Per Konvention hat eine OK Message keinen Message Text und Context.

*  Der Typ __INFO__ kann verwendet werden, um Informationen anzuzeigen, wie zum Beispiel: "Die Angabe ist freiwillig", "Eingabe korrekt" oder "Gut gemacht!".

*  Messages vom Typ __WARNING__ können verwendet werden, wenn der Wert zwar (technisch) valide ist, es sich aber eventuell um einen Irrtum handelt, zum Beispiel weil der Wert ungewöhnlich erscheint. Auch kann der Nutzer über zusätzliche Folgen gewarnt werden, falls die Eingabe so übernommen wird. Beispiele für Message Texte sind: "Möchten die wirklich 100 Fernseher bestellen?", "Die Maße liegen außerhalb des Normbereichs", "Bei dieser Bestellmenge fallen zusätzliche Gebühren an!", "Für diesen Artikel kann keine UsSt ausgewiesen werden!".

*  Der Typ __INFO_ERROR__ kann für Fehler verwendet werden, bei denen der Wert zwar technisch nicht valide ist (und somit auch nicht akzeptiert wird), der Nutzer aber bisher nichts falsch gemacht hat. Dies gilt zum Beispiel für Pflichtfelder, deren Editierung noch nicht begonnen wurde oder für Eingaben, die zum Beispiel durch Ergänzung noch richtig werden könnten. Beispiele wären: "Pflichtangabe", "Bitte vervollständigen Sie die Eingabe".  

*  Der Typ __ERROR__ gilt für alle anderen Fehler.

Die Typen `INFO` und `INFO_ERROR` wurden Aufgrund von Kundenfeedback eingeführt. So wollten Kunden neben negativen auch positives Feedback beim Ausfüllen einer Maske haben, um sich bestätigt zu fühlen, alles richtig gemacht zu haben. Des Weiteren wurde bemängelt, dass eine Maske zum Erzeugen eines Datensatzes mit Pflichtfeldern bereits beim Öffnen mit etlichen Fehlern angezeigt wurde, obwohl man offensichtlich doch noch gar nichts falsch gemacht habe. Meldungen vom Typ `INFO_ERROR` enthalten daher eher Information, was zu tun ist, damit es richtig wird und werden in der Regel (konfigurierbar) nicht mit roter Farbe angezeigt. Meldungen von Typ `ERROR` geben eher an, was falsch ist. Ein gutes Beispiel für einen __schlechten__ `ERROR` Text wäre: "Geben sie einen gültigen Wert ein", besser wäre "Es sind nicht mehr als 20 Zeichen erlaubt".

Die Existenz der Fehlertypen bedeutet nicht, dass auch allen Typen Verwendung finden müssen. Für einige Anwendungsfälle reicht eventuell `OK==richtig` und `ERROR==falsch` aus.

#### Der Valid Status

Die Enum `MessageType` bietet folgende Methode, um zu prüfen, ob der Typ zur Klasse der validen Messages gehört oder nicht.

~~~
	public boolean isValid() {...}
~~~

Die Werte werden anhand der oben angezeigten Tabelle zurückgegeben.

#### Vergleichsmethoden der Enum MessageType

Die Enum `MessageType` bietet folgende Methoden, um den Schweregrad (Severity) zweier Message Types zu vergleichen.

~~~
	public boolean equalOrWorse(final MessageType messageType) {...}

	public boolean worse(final MessageType messageType) {...}
~~~

Die Methode `equalOrWorse` liefert `true` zurück, falls der MessageType die gleiche oder eine höhere Severity hat, als der übergebene. Die Methode `worse()` liefert `true` zurück, falls der MessageType eine höhere Severity hat, als der übergebene.

Beispiel:

~~~
	System.out.println(MessageType.ERROR.equalOrWorse(MessageType.WARNING));
	System.out.println(MessageType.WARNING.equalOrWorse(MessageType.ERROR));
~~~

Ergebnis:

~~~
	true
	false
~~~






### Validation Message{#validation_message}

Eine `IValidationMessage` stellt eine einzelne Nachricht eines [`IValidationResult`](#validation_result) bereit. Eine Validation Message ist imutable, das heißt die Properties können nachträglich nicht mehr geändert werden. Die Schnittstelle sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public interface IValidationMessage {

	MessageType getType();

	String getText();

	String getContext();

	IValidationMessage withContext(String context);

	boolean equalOrWorse(final IValidationMessage message);

	boolean worse(final IValidationMessage message);
}
~~~

Die Methode `getType()` liefert den [`MessageType`](#validation_message_type) zurück. Dieser ist nie `null`. Der Message Text (`getText()`) liefert den eigentliche Fehler oder Info Text. Dieser kann `null` sein, was jedoch nur für Fehler vom Typ `OK` angeraten wird. Der `context` gibt an, wo der Fehler aufgetreten ist. Das kann zum Beispiel der Name des Attributes einer Eingabemaske sein. Auch der Context kann `null` sein.

Mit Hilfe der Methode withContext() kann eine Kopie der Message erstellt werden, welche den übergeben `context` als neuen `context` bekommt.

Die Methode `equalOrWorse()` und `worse()` vergleichen den Schweregrad (Severity) zweier Messages, analog zu den Methoden gleichen Namens auf der Enum [MessageType](#validation_message_type).







### Validation Result{#validation_result}

Ein Validation Result bündelt das Ergebnis einer Validierung und besteht aus 0 bis n [Validation Messages](#validation_message), welche alle einen unterschiedlichen [Message Type](#validation_message_type) haben können. Ein ValidationResult ist imutable, wodurch sichergestellt ist, dass sich ein einmal ausgewertetes Ergebnis nicht mehr ändern kann.

#### Zugriff auf die Validation Messages

Für den Zugriff auf die einzelnen Messages bietet die Schnittstelle `IValidationResult` die folgenden Methoden:

~~~
	List<IValidationMessage> getAll();

	List<IValidationMessage> getErrors();

	List<IValidationMessage> getInfoErrors();

	List<IValidationMessage> getWarnings();

	List<IValidationMessage> getInfos();
~~~

Die Methode getAll() liefert alle Messages, die anderen Methoden liefern die Messages des entsprechenden Typs. Die Messages sind in der Reihenfolge angeordnet, wie sie hinzugefügt wurden. Die Ergebnisliste ist nicht modifizierbar (_unmodifieable_). Die Default Implementierung erzeugt die Listen _lazy_ bei der ersten Anfrage. (Das gilt auch für die `All` Liste.) Falls man mit der [First Worst Message](#first_worst_validation_message) auskommt, kann das Rechenleistung und Speicher sparen.

#### First Worst Message{#first_worst_validation_message} 

In einigen Anwendungsfällen kann es ausreichen, nur die erste aufgetretenen Message mit höchstem Schweregrad zu kennen. Dazu kann die folgende Methode auf einem `IValidationResult` verwendet werden:

~~~
	IValidationMessage getWorstFirst();
~~~

Diese Methode liefert __immer__ eine Message zurück. Gibt es keine __echten__ Messages, wird eine Message vom Typ `OK` zurückgegeben. Diese hat per Konvention keinen Message Text und Message Context. Ansonsten wird die Message zurückgegeben, welche den höchsten Schweregrad hat. Existieren mehrere Messages mit diesem Schweregrad, wird die herangezogen, welche als erstes aufgetreten ist, bzw. dem Ergebnis hinzugefügt wurde. 

Die Default Implementierung aktualisiert den Wert immer direkt beim Hinzufügen neuer Messages (Bei der [Message Verkettung](#validation_message_chaining) oder mit Hilfe des [Validation Result Builder](#validation_result_builder)), so dass dieser nicht explizit berechnet werden muss. 

__Die Abfrage der First Worst Message ist also effizienter als die Verwendung der Message Listen__

#### Gesamtergebnis

In bestimmten Fällen sind die eigentlichen Messages gar nicht relevant, sondern nur, ob das Ergebnis valide ist oder nicht. Dafür kann die folgende Methode verwendet werden:

~~~
	boolean isValid();
~~~

Diese gibt `false` zurück, falls es mindestens eine Messages gibt, welche nicht valid ist und ansonsten `true`.

Die Methode:

~~~
	boolean isOk();
~~~

liefert `false` zurück, falls es mindestens eine Message mit Schweregrad `INFO` oder höher gibt, und sonst `true`

#### Die Validation Result Accessor Klasse

Die Accessor Klasse `ValidationResult` liefert folgende statische Methoden zur Erzeugung eines `IValidationResult` mit genau einer [Validation Message](#validation_message):

~~~
	public static IValidationResult create() {...}

	public static IValidationResult ok() {...}

	public static IValidationResult create(final IValidationMessage message) {...}

	public static IValidationResult warning(final String text) {...}

	public static IValidationResult infoError(final String text) {...}

	public static IValidationResult error(final String text){...}

	public static IValidationResult warning(final String context, final String text) {...}

	public static IValidationResult infoError(final String context, final String text) {...}

	public static IValidationResult error(final String context, final String text){...}
~~~

Mit Hilfe der folgenden Methode kann ein [`IValidationResultBuilder`](#validation_result_builder) erzeugt werden:

~~~
	public static IValidationResultBuilder builder() {...}
~~~


#### Message Verkettung{#validation_message_chaining}

Ein Validation Result ist immutable. Daher können zu einem Ergebnis nachträglich auch keine Messages hinzugefügt werden. Es ist jedoch möglich, zu einem Validation Result eine Message hinzuzufügen, indem man das Ergebnis kopiert und dabei die neue Nachricht hinzufügt. Dazu können die folgenden Methoden verwendet werden:

~~~
	IValidationResult withMessage(final IValidationMessage message);

	IValidationResult withError(final String text);

	IValidationResult withInfoError(final String text);

	IValidationResult withWarning(final String text);

	IValidationResult withInfo(final String text);

	IValidationResult withError(final String context, final String text);

	IValidationResult withInfoError(final String context, final String text);

	IValidationResult withWarning(final String context, final String text);

	IValidationResult withInfo(final String context, final String text);
~~~

Das resultierende Validation Result ist eine Kopie des aktuellen Validation Results, welchem die übergebene Message hinzugefügt wurde. Die Methode `withMessage()` verlangt eine `IValidationMessage`, die anderen Methoden sind Convenience Methoden, welche die [Validation Message](#validation_message) mit Hilfe des Parameter `text` (und optional `context`) erzeugen.


Das folgende Beispiel soll das verdeutlichen:

~~~{.java .numberLines startFrom="1"}
	IValidationResult result = ValidationResult.create();
	result = result.withInfo("Info message");
	result = result.withError("Error message");
	result = result.withWarning("Warn message");
~~~

Es werden dem initialen Validation Result (Zeile 1) drei weitere Messages hinzugefügt. Bei dieser Methode ist darauf zu achten, dass das `result` immer wieder neue zugewiesen werden muss. Um diese potentielle Fehlerquelle zu vermeiden, kann auch ein [`IValidationResultBuilder`](#validation_result_builder) verwendet werden.

#### Ändern des Context

Mit Hilfe der folgenden Methode kann der Context für alle [Validation Messages](#validation_message) eines `IValidationResult` geändert werden, indem eine Kopie erzeugt wird und auf dieser der Context geändert wird:

~~~
	IValidationResult withContext(final String context);
~~~

#### Validation Result Builder{#validation_result_builder}

Die Schnittstelle `IValidationResultBuilder` hat die folgenden Methoden:

~~~
	IValidationResultBuilder addMessage(final IValidationMessage message);

	IValidationResultBuilder addInfo(final String text);

	IValidationResultBuilder addWarning(final String text);

	IValidationResultBuilder addInfoError(final String text);

	IValidationResultBuilder addError(final String text);

	IValidationResultBuilder addInfo(final String context, final String text);

	IValidationResultBuilder addWarning(final String context, final String text);

	IValidationResultBuilder addInfoError(final String context, final String text);

	IValidationResultBuilder addError(final String context, final String text);

	IValidationResultBuilder addResult(final IValidationResult result);

	IValidationResult build();
~~~

Der Builder liefert Methoden zum Hinzufügen von [Validation Messages](#validation_message). Mit Hilfe der Methode `addResult()` kann man alle Messages eines anderen `IValidationResult` hinzufügen. Mit Hilfe der Methode `build()` wird das erzeugte `IValidationResult` zurückgegeben.

Das folgende Beispiel verwendet den `IValidationResultBuilder` für die Erzeugung eines Validation Result:

~~~{.java .numberLines startFrom="1"}
	final IValidationResultBuilder builder = ValidationResult.builder();
	builder
		.addInfo("Info message")
		.addError("Error message")
		.addWarning("Warn message");
		
	final IValidationResult result = builder.build();
~~~ 

Das resultierende Validation Result ist identisch mit dem obigen Beispiel bei der [Message Verkettung](#validation_message_chaining). Der Vorteil beim Builder Ansatz ist, dass die Neuzuweisung entfällt (welche bei der Message Verkettung versehentlich vergessen werden könnte). 

### Validator Composite

Ein Validator Composite ist ein `IValidator`, welche mehrere `IValidator` verwendet, um seine `validate()` Methode zu implementieren.  Die statische Accessor Klasss `ValidatorComposite` liefert folgende Methoden zur Erzeugung eines Validator Composite:

~~~
	public static <VALUE_TYPE> IValidator<VALUE_TYPE> create(
		final IValidator<VALUE_TYPE> validator1,
		final IValidator<VALUE_TYPE> validator2) {...}
		
	public static <VALUE_TYPE> IValidatorCompositeBuilder<VALUE_TYPE> builder() {...}
~~~

Die erste Methode erzeugt aus zwei Validatoren einen _neuen_ `IValidator`. Dabei können sowohl `validator1` als auch `validator2` als auch beide `null` sein. Der resultierende Validator fügt die [Validation Results](#validation_result) beider Validatoren zu einem neuen Validation Result zusammen. Ist ein Parameter (`validator1`, `validator2`) `null` wird stellvertretend ein  [OkValidator](#ok_validator) verwendet.

Die Methode `builder()` liefert ein `IValidatorCompositeBuilder`. Dieser hat die folgenden Methoden:

~~~
	IValidatorCompositeBuilder<VALUE_TYPE> add(IValidator<VALUE_TYPE> validator);

	IValidatorCompositeBuilder<VALUE_TYPE> addAll(Iterable<? extends IValidator<VALUE_TYPE>> validators);

	IValidator<VALUE_TYPE> build();
~~~

Die Methode `add()` fügt einen einzelen Validator hinzu, die Methode `addAll()` eine Liste von Validatoren. Die Methode build() erzeugt einen neuen Composite Validator. Das folgende Beispiel demonstriert die Verwendung:

~~~{.java .numberLines startFrom="1"}
	final IValidatorCompositeBuilder<Person> builder = ValidatorComposite.builder();
	builder
		.add(notNullValidator)
		.add(adultValidator)
		.add(maleValidator);
		
	IValidator<Person> maleAdultPersonValidator = builder.build();
~~~

### IValidatable{#validatetable_interface}

Ein `IValidatable` ist ein Objekt, was in der Lage ist, seinen eigenen Zustand zu validieren. Die Schnittstelle sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public interface IValidateable {

	IValidationResult validate();

	void addValidationConditionListener(IValidationConditionListener listener);

	void removeValidationConditionListener(IValidationConditionListener listener);

}
~~~

Ein `IValidationConditionListener` wird aufgerufen, wenn sich die Bedingungen für die Validierung geändert haben, zum Beispiel weil der zu validierende Wert sich geändert hat, oder weil sich die Validierungsregeln geändert haben. Der Listener sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public interface IValidationConditionListener {

	void validationConditionsChanged();

}
~~~ 


## Observable Values - Übersicht{#observable_values}

Ein [Observable Value](#observable_value_interface_and_impls) ist ein Container für einen Wert, der sich zur Laufzeit ändern kann. Mit Hilfe eines `IObservableValueListener` kann man sich über Änderungen des Wertes informieren lassen. Observable Values können an andere Observable Values [gebunden](#observable_value_binding) werden. Ein [Observable Value Viewer](#observable_value_viewer) ist ein Widget, welches einen Observable Value visualisiert und (optional) eine Modifikation des Wertes ermöglicht.


### Observable Value Schnittstelle und Implementierungen{#observable_value_interface_and_impls}

Im Folgenden wird die Schnittstelle `IObservableValue` vorgestellt und es werden existierende Default Implementierungen gezeigt. 


#### Die Schnittstelle IObservableValue{#observable_value_interface}

Die Schnittstelle `IObservableValue` sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public interface IObservableValue<VALUE_TYPE> {

	void setValue(VALUE_TYPE value);

	VALUE_TYPE getValue();

	void addValueListener(IObservableValueListener<?> listener);

	void removeValueListener(IObservableValueListener<?> listener);
}
~~~

Ein `IObservableValueListener` hat die folgende Methode:

~~~
	void changed(IObservableValue<VALUE_TYPE> observableValue, VALUE_TYPE value);
~~~

Man bekommt sowohl den Observable Value der sich geändert hat, als auch den neuen Wert (`value`) übergeben. Der Listener feuert nur dann, wenn sich der Wert tatsächlich geändert hat. Wird zum Beispiel ein zweites mal der gleiche Wert gesetzt, wird kein ChangedEvent geworfen.

#### ObservableValue Default Implementierung

Die Klasse `ObservableValue` bietet eine Default Implementierung der Schnittstelle `IObservableValue`. 

Das folgende Beispiel demonstriert die Verwendung der Klasse `ObservableValue`:

~~~{.java .numberLines startFrom="1"}
	final IObservableValue<IPerson> forkliftDriver = new ObservableValue<IPerson>(dieter);
		
	forkliftDriver.addValueListener(new IObservableValueListener<IPerson>() {
		@Override
		public void changed(final IObservableValue<IPerson> observableValue, final IPerson value) {	
			diaphone.setActive(value == klaus);
		}
	});
		
	forkliftDriver.setValue(klaus);
~~~

Die Default Implementierung implementiert __nicht__ equals() und hashCode(). Zwei `ObservableValue` Objekte sind insbesondere __nicht__ `equal`, wenn ihre values gleich sind. Der folgende UnitTest soll verdeutlichen, warum:

~~~{.java .numberLines startFrom="1"}
	final ObservableValue<String> value = new ObservableValue<String>();
	value.setValue(STRING_1);

	final Set<ObservableValue<String>> set = new HashSet<ObservableValue<String>>();
	set.add(value);

	value.setValue(STRING_2);

	Assert.assertTrue(set.remove(value));
~~~

Durch das Ändern des Wertes in Zeile 7 würde sich der hasCode() ändern, wodurch der `value` nicht mehr aus der Liste entfernt werden könnte.

Die Klasse `ObservableValue` wurde so entworfen, dass davon abgeleitet werden kann. Das folgenden Beispiel zeigt eine `ObservablePerson`, welche `equals()` und `hashCode()` mit Hilfe einer id implementiert: 

~~~{.java .numberLines startFrom="1"}
import org.jowidgets.util.Assert;
import org.jowidgets.util.ObservableValue;

public final class ObservablePerson extends ObservableValue<IPerson> {

	private final Object id;

	public ObservablePerson(final Object id) {
		Assert.paramNotNull(id, "id");
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ObservablePerson)) {
			return false;
		}
		final ObservablePerson other = (ObservablePerson) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
~~~

#### ObservableValueWrapper

Um ein `IObservableValue` mit Hilfe des Wrapper Patters zu _wrappen_, zum Beispiel um den Wert zu dekorieren, kann die Klasse `ObservableValueWrapper` verwendet werden. Das folgende Beispiel soll das verdeutlichen:

~~~{.java .numberLines startFrom="1"}
import org.jowidgets.util.IDecorator;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.ObservableValueWrapper;

public final class ObservableValueDecorator<VALUE_TYPE> 
	implements IDecorator<IObservableValue<VALUE_TYPE>> {

	//injected
	private IAuthorizationService authorizationService;

	@Override
	public IObservableValue<VALUE_TYPE> decorate(final IObservableValue<VALUE_TYPE> original) {
		if (authorizationService == null) {
			return original;
		}
		else {
			return new ObservableValueWrapper<VALUE_TYPE>(original) {
				@Override
				public void setValue(final VALUE_TYPE value) {
					if (!authorizationService.hasAuthorization(Authorizations.UPDATE)) {
						throw new SecurityException("No authorization for update");
					}
					else {
						super.setValue(value);
					}
				}
			};
		}
	}
}
~~~

#### MandatoryObservableValue{#mandatory_observable_value}

Die Klasse `MandatoryObservableValue` liefert eine Implementierung von `IObservableValue` welche nicht den Wert `null` annehmen kann. Die Implementierung sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public class MandatoryObservableValue<VALUE_TYPE> extends ObservableValue<VALUE_TYPE> {

	private final VALUE_TYPE defaultValue;

	public MandatoryObservableValue(final VALUE_TYPE defaultValue) {
		Assert.paramNotNull(defaultValue, "defaultValue");
		this.defaultValue = defaultValue;
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		if (value != null) {
			super.setValue(value);
		}
		else {
			super.setValue(defaultValue);
		}
	}

	@Override
	public VALUE_TYPE getValue() {
		final VALUE_TYPE superResult = super.getValue();
		if (superResult != null) {
			return superResult;
		}
		else {
			return defaultValue;
		}
	}
}
~~~

Ein `MandatoryObservableValue` hat einen Default Value (Zeile 3), welcher verwendet wird, sobald  `null` gesetzt wird. 

#### ObservableBoolean

Ein `ObservableBoolean` liefert eine Implementierung von `IObservableValue` für ein `Boolean` bei dem nicht gewünscht ist, dass der Wert `null` angenommen werden kann, sondern nur `true` und `false`. Die Implementierung sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public final class ObservableBoolean extends ObservableValue<Boolean> {

	public ObservableBoolean() {
		setValue(false);
	}

	public ObservableBoolean(final boolean value) {
		set(value);
	}

	public boolean get() {
		return getValue().booleanValue();
	}

	public void set(final boolean value) {
		super.setValue(Boolean.valueOf(value));
	}

	@Override
	public void setValue(final Boolean value) {
		Assert.paramNotNull(value, "value");
		super.setValue(value);
	}
}
~~~

Die Methoden `get()` und `set()` bieten einen komfortablen Zugriff auf den _kleinen_ boolean ohne _Autoboxing_.^[Warum Atoboxing __evil__ ist, wird unter anderem auch hier diskutiert: [https://pboop.wordpress.com/2010/09/22/autoboxing-is-evil/]] Der Ausdruck `boolean b = get()` kann (im Vergleich zu `boolean b = getValue()` auf einen herkömmlichen Observable Value) nie eine `NullPointerException` werfen, da man das Setzen von `null` explizit verhindert (Zeile 21). Man sollte einen solchen Wert nur an Observable Values binden, die ebenfalls Mandatory sind. Man könnte den Code ab Zeile 21 auch wie folgt ändern:

~~~{.java .numberLines startFrom="1"}
	@Override
	public void setValue(final Boolean value) {
		if (value != null){
			super.setValue(value);
		}
		else{
			super.setValue(Boolean.FALSE);
		}
	}
~~~

Dies würde dem Verhalten des [MandatoryObservableValue](#mandatory_observable_value) entsprechen, wobei man zusätzlich noch die sichere get() Methode hat.





























### Observable Value Binding{#observable_value_binding}

Die Klasse `org.jowidgets.util.binding.Bind` kann verwendet werden, um zwei Observable Values aneinander zu binden binden. Sie hat die folgenden statischen Methoden:

~~~
	public static <VALUE_TYPE> IBinding bind(
		final IObservableValue<VALUE_TYPE> source,
		final IObservableValue<VALUE_TYPE> destination) {...}

	public static <SOURCE_TYPE, DESTINATION_TYPE> IBinding bind(
		final IObservableValue<SOURCE_TYPE> source,
		final IObservableValue<DESTINATION_TYPE> destination,
		final IBindingConverter<SOURCE_TYPE, DESTINATION_TYPE> converter) {...}
~~~

Die erste Methode kann verwendet werden, um typgleiche Values aneinander zu binden, die zweite Methode erlaubt das Binden von unterschiedlichen Typen. Die Bindung ist __immer bidirektional__, das bedeutet, Änderungen auf `source` ändern die `destination` und Änderungen auf `destination` ändern die `source`. Haben `source` und `destination` initial einen unterschiedlichen Wert, nimmt durch das Binden `destination` den Wert von `source` an. Beide Methoden liefern eine `IBinding` Referenz zurück. Diese hat die folgenden Methoden:

~~~
	void setBindingState(boolean bind);

	void unbind();

	void bind();
	
	boolean isBound();

	void dispose();
	
	boolean isDisposed();
~~~

Die ersten drei Methoden dienen zum Setzen, die vierte zum Auslesen des `binding` State. Dadurch kann das Binding temporär gelöst und später wieder aktiviert werden. Initial ist der `binding` State `true`. Durch den Aufruf von `dispose()` wird das Binding dauerhaft gelöst, und die internen Referenzen auf `source` und `destination` verworfen. Nach einem Aufruf von `dispose()` kann nur noch die Methode `isDisposed()` aufgerufen werden. Alle anderen Methodenaufrufe führen dann zu einer `IllegalStateException`.

#### Binding Converter

Um Obervable Values unterschiedlichen Typs zu binden, kann ein `IBindingConverter` verwendet werden. Dieser ist wie folgt definiert:

~~~{.java .numberLines startFrom="1"}
public interface IBindingConverter<SOURCE_TYPE, DESTINATION_TYPE> {

	DESTINATION_TYPE convertSource(SOURCE_TYPE sourceValue);

	SOURCE_TYPE convertDestination(DESTINATION_TYPE destinationValue);
}
~~~

Das folgende Beispiel implementiert einen Binding Converter, welcher eine Liste in ein Array und zurück konvertiert:

~~~{.java .numberLines startFrom="1"}
public final class ListArrayBindingConverter implements IBindingConverter<List<String>, String[]> {

	@Override
	public String[] convertSource(final List<String> list) {
		if (list != null) {
			return list.toArray(new String[list.size()]);
		}
		else {
			return null;
		}
	}

	@Override
	public List<String> convertDestination(final String[] destinationValue) {
		if (destinationValue != null) {
			return new ArrayList<String>(Arrays.asList(destinationValue));
		}
		else {
			return null;
		}
	}
}
~~~

Dieser könnte wie folgt verwendet werden können:

~~~{.java .numberLines startFrom="1"}
	final ObservableValue<List<String>> source = new ObservableValue<List<String>>();
	final ObservableValue<String[]> destination = new ObservableValue<String[]>();

	Bind.bind(source, destination, new ListArrayBindingConverter());
~~~

#### Binding Test Beispiel

Der folgende JUnitTest demonstriert die Verwendung der Klasse `Bind`. Der Test wurde etwas verkürzt, der vollständige Test findet sich [hier](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/core/org.jowidgets.util/src/test/java/org/jowidgets/util/binding/BindingTest.java):

~~~{.java .numberLines startFrom="1"}
public class BindingTest {

	private static String STRING_1 = "STRING_1";
	private static String STRING_2 = "STRING_2";
	private static String STRING_3 = "STRING_3";
	private static String STRING_4 = "STRING_4";
	private static String STRING_5 = "STRING_5";
	private static String STRING_6 = "STRING_6";
	private static String STRING_7 = "STRING_7";
	private static String STRING_8 = "STRING_8";
	private static String STRING_9 = "STRING_9";
	private static String STRING_10 = "STRING_10";

	@Test
	public void testBinding() {
		//Create two observable values
		final ObservableValue<String> source = new ObservableValue<String>(STRING_1);
		final ObservableValue<String> destination = new ObservableValue<String>(STRING_2);

		//create a new binding
		final IBinding binding = Bind.bind(source, destination);

		//must be equal and must be STRING_1 (source before binding)
		testEquality(source, destination, STRING_1);

		//change source must change destination
		source.setValue(STRING_3);

		//must be equal and STRING_3
		testEquality(source, destination, STRING_3);

		//change destination must change source
		destination.setValue(STRING_4);

		//must be equal and STRING_4
		testEquality(source, destination, STRING_4);

		//unbind the values
		binding.unbind();

		//after unbind, change source, destination changes not
		source.setValue(STRING_5);
		Assert.assertEquals(STRING_5, source.getValue());
		Assert.assertEquals(STRING_4, destination.getValue());

		//after unbind, change destination, source changes not
		destination.setValue(STRING_6);
		Assert.assertEquals(STRING_6, destination.getValue());
		Assert.assertEquals(STRING_5, source.getValue());

		//bind the values again
		binding.bind();

		//must be equal and STRING_5 (last source value)
		testEquality(source, destination, STRING_5);
	}

	private void testEquality(
		final IObservableValue<String> source,
		final IObservableValue<String> destination,
		final String expectedValue) {

		//the values of the observable value must be equal
		Assert.assertEquals(source.getValue(), destination.getValue());

		//the source must be the expected value
		Assert.assertEquals(expectedValue, source.getValue());

		//the destination must be the expected value
		Assert.assertEquals(expectedValue, destination.getValue());
	}

}
~~~

























### Observable Value Viewer{#observable_value_viewer}

Ein Observable Value Viewer ist ein Widget, welches einen Observable Value visualisiert und (optional) eine Modifikation des Wertes ermöglicht.


#### Die Schnittstelle IObservableValueViewer{#observable_value_viewer_interface}

Die Schnittstelle `IObservableValueViewer` ist wie folgt definiert:

~~~
public interface IObservableValueViewer<VALUE_TYPE> {

	IObservableValue<VALUE_TYPE> getObservableValue();
	
}
~~~

#### Abgeleitete Widgets

Die Schnittstelle wird derzeit von den folgenden Widgets implementiert:

* [CheckBox](#checkbox_widget)
* [ToggleButton](#toggle_button_widget)
* [ComboBox](#combobox_widget)
* [Combobox Selection](#combobox_selection_widget)
* [InputField](#input_field)
* [Slider](#slider_widget)
* [Slider Viewer](#slider_viewer)

Die BluePrints dieser Widgets haben jeweils die Methode:

~~~
	BLUE_PRINT_TYPE setObservableValue(IObservableValue<VALUE_TYPE> value);
~~~

Dadurch wird der übergebene Obserservable Value an den neu erzeugten Viewer gebunden. Das Binding wird wieder gelöst, wenn das Widget disposed wird. Man kann dadurch anstatt:

~~~{.java .numberLines startFrom="1"}
	final IObservableValue<Double> value = new ObservableValue<Double>(0.0d);

	//add slider
	final ISliderViewerBluePrint<Double> sliderBp = BPF.sliderViewerDouble(-1.0d, 1.0d);
	final ISliderViewer<Double> sliderViewer = frame.add(sliderBp, "w 50::");

	final IBinding binding = Bind.bind(value, sliderViewer.getObservableValue());
	sliderViewer.addDisposeListener(new IDisposeListener() {
		@Override
		public void onDispose() {
			binding.dispose();
		}
	});
~~~

einfach das folgende Schreiben:

~~~{.java .numberLines startFrom="1"}
	final IObservableValue<Double> value = new ObservableValue<Double>(0.0d);
	
	//add slider
	final ISliderViewerBluePrint<Double> sliderBp = BPF.sliderViewerDouble(-1.0d, 1.0d);	
	sliderBp.setObservableValue(value);
	frame.add(sliderBp, "w 50::");
~~~
 

#### Observable Value Viewer Snipped

Das [ObservableValueSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/ObservableValueViewerSnipped.java) verwendet einen Observable Value, um ein [SliderViewer]((#slider_viewer)) und ein [InputField](#input_field) aneinander zu binden:

~~~{.java .numberLines startFrom="1"}
public final class ObservableValueViewerSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrameBluePrint frameBp = BPF.frame("Observable value viewer snipped");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		frame.setLayout(new MigLayoutDescriptor("wrap", "[][][]", "[]"));

		//create panorama observable value 
		final IObservableValue<Double> panorama = new ObservableValue<Double>(0.0d);

		//add panorama label
		final ITextLabelBluePrint labelBp = BPF.textLabel("Panorama");
		labelBp.setForegroundColor(Colors.STRONG).setMarkup(Markup.STRONG);
		frame.add(labelBp);

		//add panorama slider
		final ISliderViewerBluePrint<Double> sliderBp = BPF.sliderViewerDouble(-1.0d, 1.0d);
		sliderBp.setObservableValue(panorama);
		frame.add(sliderBp, "growx, w 150!");

		//add panorama input field
		final IInputFieldBluePrint<Double> inputFieldBp = BPF.inputFieldDoubleNumber();
		inputFieldBp.setObservableValue(panorama);
		frame.add(inputFieldBp, "w 50!");

		//set the root frame visible
		frame.setVisible(true);
	}
}
~~~

Änderungen am Slider modifizieren das Eingabefeld und umgekehrt. Die folgende Abbildung zeigt das Ergebnis:

![Observable Value Viewer Snipped](images/observable_slid_vwr_snipped.gif "Observable Value Viewer Snipped")



#### Binding Snipped

Das [BindingSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/BindingSnipped.java) verwendet mehrere Observable Values, um jeweils ein [SliderViewer]((#slider_viewer)) und ein [InputField](#input_field) aneinander zu binden. Zudem kann man über eine Checkbox das Binding aller Observable Values untereinander aktivieren oder deaktivieren, wodurch sich die Slider und Eingabefelder synchron oder unabhängig voneinander sind:

~~~{.java .numberLines startFrom="1"}
public final class BindingSnipped implements IApplication {

	private static final int COLUMNS = 10;
	private static final double MIN_VALUE = -1.0d;
	private static final double MAX_VALUE = 1.0d;
	private static final double DEFAULT_VALUE = 0.0d;

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrame frame = Toolkit.createRootFrame(BPF.frame("Binding snipped"), lifecycle);
		frame.setLayout(new MigLayoutDescriptor(
			"wrap", 
			StringUtils.loop("[]10", COLUMNS - 1) + "[]", 
			"[]0[]20[]"));

		//create observable values and bindings
		final ArrayList<IObservableValue<Double>> observableValues
			= new ArrayList<IObservableValue<Double>>(COLUMNS);
		final ArrayList<IBinding> bindings = new ArrayList<IBinding>(COLUMNS - 1);
		for (int i = 0; i < COLUMNS; i++) {
			final IObservableValue<Double> observableValue = new ObservableValue<Double>();
			observableValues.add(observableValue);
			if (i > 0) {
				//bind next value to the previous
				final IBinding binding = Bind.bind(
					observableValues.get(i - 1), 
					observableValue);
				bindings.add(binding);
			}
		}

		//add sliders
		for (int i = 0; i < COLUMNS; i++) {
			final ISliderViewerBluePrint<Double> sliderBp 
				= BPF.sliderViewerDouble(MIN_VALUE, MAX_VALUE);
			sliderBp.setVertical();
			sliderBp.setDefaultValue(DEFAULT_VALUE);
			sliderBp.setObservableValue(observableValues.get(i));
			frame.add(sliderBp, "w 50::");
		}

		//add input fields
		for (int i = 0; i < COLUMNS; i++) {
			final IInputFieldBluePrint<Double> inputFieldBp 
				= BPF.inputFieldDoubleNumber();
			inputFieldBp.setObservableValue(observableValues.get(i));
			frame.add(inputFieldBp, "w 50::");
		}

		//add binding checkbox
		final ICheckBox bindingCb = frame.add(BPF.checkBox().setText("Bind"));
		bindingCb.setSelected(true);
		bindingCb.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				for (final IBinding binding : bindings) {
					binding.setBindingState(bindingCb.isSelected());
				}
			}
		});

		//set the root frame visible
		frame.setVisible(true);
	}
}
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Binding Snipped](images/binding_snipped.gif "Binding Snipped")


## Die Generic Widget Factory - Übersicht{#generic_widget_factory}

Die Generic Widget Factory ist zum einen eine Registry für konkrete Widget Factories. Dabei wird für jeden Widget Typ (welcher durch sein BluePrint Typ definiert ist, und nicht durch die Widget Schnittstelle) genau eine IWidgetFactory registriert. Zum anderen dient die Generic Widget Factory zur Erzeugung aller Widgets. Dies wird mit Hilfe der registrierten `IWidgetFatory` Instanzen implementiert. 

Die Generic Widget Factory wird durch Client Code in der Regel nur direkt bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) verwendet.  


### Die Generic Widget Factory Methoden

Es folgt eine Beschreibung der Methoden der Schnittstelle `IGenericWidgetFactory`:


#### Methoden zur Erzeugung von Widgets

Die folgenden Methoden dienen der Erzeugung von Widgets:

~~~
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		WIDGET_TYPE create(DESCRIPTOR_TYPE descriptor);

    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		WIDGET_TYPE create(Object parentUiReference, DESCRIPTOR_TYPE descriptor);
~~~

Die erste Methode erzeugt Root Widgets, welche keinen Parent hat, die zweite Methode erzeugt ein Kind Widget, wobei die native UI Refrenz des Vaters übergeben wird. Client Code ruft die zweite Methode im Normalfall nicht direkt auf, außer bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) in einer `IWidgetFactory`.


Die erste Methode kann zum Beispiel für die Erzeugung eines Root Frame verwendet werden:

~~~
	final IFrame rootFrame = Toolkit.getWidgetFactory().create(BPF.frame());
~~~

Das gleiche kann man aber auch wie folgt schreiben:

~~~
	final IFrame rootFrame = Toolkit.createRootFrame(BPF.frame());
~~~

#### Methoden zur Registrierung und Deregistrierung von Widgets

~~~
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		void register(
			Class<? extends DESCRIPTOR_TYPE> descriptorClass,
			IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE> widgetFactory);

    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		void unRegister(Class<? extends DESCRIPTOR_TYPE> descriptorClass);
~~~

Die erste Methode registriert eine `IWidgetFactory` für einen definierten BluePrint Typ. Dabei darf für diesen Typ noch kein Widget registriert sein. Möchte man explizit eine Widget Factory ersetzen, um zum Beispiel eine [vorhandene Widget Implementierung auszutauschen](#substitude_and_decorate_widgets), muss man den BluePrint Typ vorab per `unRegister()` deregistrieren.

Die Schnittstelle `IWidgetFactory` ist wie folgt definiert:

~~~{.java .numberLines startFrom="1"}
public interface IWidgetFactory
	<WIDGET_TYPE extends IWidgetCommon,
	DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> {

    WIDGET_TYPE create(Object parentUiReference, DESCRIPTOR_TYPE descriptor);
}
~~~

Das folgende Beispiel zeigt eine typische Implementierung:

~~~{.java .numberLines startFrom="1"}
final class BeanFormFactory<BEAN_TYPE> implements 
	IWidgetFactory<IBeanForm<BEAN_TYPE>, IBeanFormBluePrint<BEAN_TYPE>> {

	@Override
	public IBeanForm<BEAN_TYPE> create(
		final Object parentUiReference, 
		final IBeanFormBluePrint<BEAN_TYPE> bluePrint) {
		
		final IComposite composite = Toolkit.getWidgetFactory().create(
			parentUiReference, 
			BPF.composite());
		
		return new BeanFormImpl<BEAN_TYPE>(composite, bluePrint);
	}
}
~~~

In Zeile 9 wird mit Hilfe der Generic Widget Factory ein Composite erzeugt. Dieses wird in Zeile 13 der `BeanFormImpl` übergeben. Diese verwendet das `IComposite` um das Formular darin darzustellen.


#### Abfragen einer Widget Factory

Die folgende Methode liefert eine `IWidgetFactory` für einen BluePrint Typ:

~~~
  <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
	IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> getFactory(
		Class<? extends DESCRIPTOR_TYPE> descriptorClass);
~~~

Falls für den BluePrint Typ keine Factory registriert ist, wird `null` zurückgegeben.


#### Dekorieren von Widgets

Die folgenden Methoden können verwendet werden, um alle Widgets oder Widget Factories für einen bestimmten Typ zu dekorieren:

~~~
	<WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		void addWidgetDecorator(
			Class<? extends DESCRIPTOR_TYPE> descriptorClass,
			IDecorator<WIDGET_TYPE> decorator);

    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		void addWidgetFactoryDecorator(
			Class<? extends DESCRIPTOR_TYPE> descriptorClass,
			IDecorator<IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE>> decorator);
~~~

Weitere Information finden sich im Abschnitt [Austauschen und Dekorieren von Widgets](#substitude_and_decorate_widgets).


#### Widget Factory Listener

Die folgenden Methoden können verwendet werden, um einen Widget Factory Listener hinzuzufügen und zu entfernen:

~~~
    void addWidgetFactoryListener(IWidgetFactoryListener listener);

    void removeWidgetFactoryListener(IWidgetFactoryListener listener);
~~~

Ein `IWidgetFactoryListener` hat die folgende Methode:

~~~
	void widgetCreated(IWidgetCommon widget);
~~~

Diese Methode wird immer aufgerufen, wenn ein Widget erzeugt wird. Dies kann zum Beispiel für Debugging Zwecke oder für JUnit Tests interessant sein. Der folgende Listener wurde einer Applikation vor dem Start hinzugefügt:

~~~{.java .numberLines startFrom="1"}
public final class WidgetFactoryListener implements IWidgetFactoryListener {

	private final Set<IWidget> allWidgets;
	private final Set<IWidget> undisposedWidgets;

	public WidgetFactoryListener() {
		this.allWidgets = new HashSet<IWidget>();
		this.undisposedWidgets = new HashSet<IWidget>();
	}

	@Override
	public void widgetCreated(final IWidgetCommon widgetCommon) {
		if (widgetCommon instanceof IWidget) {
			final IWidget widget = (IWidget) widgetCommon;
			allWidgets.add(widget);
			undisposedWidgets.add(widget);
			widget.addDisposeListener(new IDisposeListener() {
				@Override
				public void onDispose() {
					undisposedWidgets.remove(widget);
				}
			});
		}
	}

	@Override
	public String toString() {
		return "allWidgetsCount="
				+ allWidgets.size() 
				+ ", undisposedWidgetsCount=" 
				+ undisposedWidgets.size();
	}

}
~~~ 


Nach dem Beenden wurde die `toString()` Methode aufgerufen. Das führte zu folgender Ausgabe:

~~~
	allWidgetsCount=48, undisposedWidgetsCount=0
~~~
 
Anschließend wurde in der Implementierung von `IContainer` ein Fehler eingebaut, so dass die Kinder eines Containers nicht mehr disposed wurden. Nach erneutem Start und Beenden ergab sich die folgende Ausgabe:

~~~
	allWidgetsCount=48, undisposedWidgetsCount=45
~~~


 
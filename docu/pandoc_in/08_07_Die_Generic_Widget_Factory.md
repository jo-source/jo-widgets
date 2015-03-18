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
			final Class<? extends DESCRIPTOR_TYPE> descriptorClass,
			final IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE> widgetFactory);

    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> 
		void unRegister( final Class<? extends DESCRIPTOR_TYPE> descriptorClass);
~~~

Die erste Methode registriert eine `IWidgetFactory` für einen definierten BluePrint Typ. Dabei darf für diesen Typ noch kein Widget registriert sein. Möchte man explizit eine Widget Factory ersetzen, um zum Beispiel eine Vorhandene Widget Implementierung auszutauschen, muss man den BluePrint Typ vorab per `unRegister()` deregistrieren.

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


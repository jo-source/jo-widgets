## Der Toolkit Interceptor - Übersicht{#toolkit_interceptor}

Ein Toolkit Interceptor wird meist für die folgenden Aufgaben verwendet:

* [Registrierung oder Substitution von Images](#register_or_substitude_images) in der [Image Registry](#image_registry)
* Überschreiben der [Widget Defaults](#widget_defaults) in der [BluePrint Proxy Factory](#blue_print_proxy_factory)
* Registrierung von Widgets in der [Generic Widget Factory](#generic_widget_factory)
* Registrierung von Convertern im [IConverterProvider](#jowidget_converter)
* Zum Setzen von [Toolkit Properties](#toolkit_properties)
* Registrierung von [Convenience Methoden Implementierungen](#custom_widget_libraries) für [Setup Builder](#widget_setup_builder) in der [BluePrint Proxy Factory](#blue_print_proxy_factory)
* Registrierung von Widget Decorators zum Dekorieren von Widgets in der [Generic Widget Factory](#generic_widget_factory)
* Registrierung von WidgetFactory Decorators zum Dekorieren der Factories von Widgets in der [Generic Widget Factory](#generic_widget_factory)

### Der Toolkit Interceptor

Es folgt eine Beschreibung der relevanten Schnittstellen sowie konkrete Beispiele:

#### Die Schnittstelle IToolkitInterceptor

Die Schnittstelle `IToolkitInterceptor` hat die folgende Methode:

~~~
	void onToolkitCreate(IToolkit toolkit);
~~~

Diese Methode wird aufgerufen, nachdem das Toolkit erzeugt wurde, jedoch bevor die Toolkit Instanz über die Klasse Toolkit verfügbar ist. Aus diesem Grund wird die neu erzeugte Instanz als Parameter übergeben. Dadurch ist sicher gestellt, dass alle Toolkit Interceptoren vor der ersten Benutzung des Toolkit ausgeführt werden. Wenn `onToolkitCreate()` aufgerufen wird, hat jowidgets bereits seine Core Widgets, Icons und Defaults registriert. 

__Achtung:__

Der Aufruf von `Toolkit.getInstance()` sowie allen anderen indirekten Aufrufen davon, wie zum Beispiel `Toolkit.getBluePrintProxyFactory()` führen zu einem Fehler. Stattdessen muss man zum Beispiel `toolkit.getBluePrintProxyFactory()` schreiben. Zudem sollten in einem Toolkit Interceptor __keine Widgets erzeugt werden!!!__

#### Toolkit Interceptor Beispiel

Das folgende Beispiel zeigt eine gekürzte Version des Toolkit Interceptor für die [jo-client-platform](http://code.google.com/p/jo-client-platform/). Der vollständige Code findet sich [hier](http://code.google.com/p/jo-client-platform/source/browse/trunk/modules/core/org.jowidgets.cap.ui/src/main/java/org/jowidgets/cap/ui/impl/widgets/CapToolkitInterceptor.java).

~~~{.java .numberLines startFrom="1"}
final class CapToolkitInterceptor implements IToolkitInterceptor {

	@Override
	public void onToolkitCreate(final IToolkit toolkit) {
		registerWidgets(toolkit);
		registerIcons(toolkit);
		addDefaultsInitializer(toolkit);
		setBuilderConvenience(toolkit);
		registerConverter(toolkit);
	}

	@SuppressWarnings("unchecked")
	private void registerWidgets(final IToolkit toolkit) {
		final IGenericWidgetFactory factory = toolkit.getWidgetFactory();
		
		factory.register(
			IBeanTableBluePrint.class, 
			new BeanTableFactory());
		
		//... removed some widgets in this example
		
		factory.register(
			IBeanLinkDialogBluePrint.class,
			new BeanLinkDialogFactory());
	}

	private void registerIcons(final IToolkit toolkit) {
		final IImageRegistry registry = toolkit.getImageRegistry();
	
		registry.registerImageConstant(
			CapIcons.TABLE_HIDE_COLUMN, 
			IconsSmall.SUB);
		
		//... removed some icons in this example

		registerImage(registry, CapIcons.NODE_CONTRACTED, "node_contracted.png");
	}

	private void registerImage(
		final IImageRegistry registry, 
		final IImageConstant imageConstant, 
		final String relPath) {
		
		String path = "org/jowidgets/cap/ui/icons/" + relPath;
		final URL url = getClass().getClassLoader().getResource(path);
		
		registry.registerImageConstant(imageConstant, url);
	}

	private void registerConverter(final IToolkit toolkit) {
		final IConverterProvider converterProvider = toolkit.getConverterProvider();
		converterProvider.register(IDocument.class, new DocumentConverter());
	}

	private void addDefaultsInitializer(final IToolkit toolkit) {
		final IBluePrintProxyFactory bppf = toolkit.getBluePrintProxyFactory();
		
		bppf.addDefaultsInitializer(
			IBeanTableSetupBuilder.class, 
			new BeanTableDefaults());
			
		//... removed some defaults in this example
			
		bppf.addDefaultsInitializer(
			ILookUpComboBoxSelectionBluePrint.class, 
			new LookUpComboBoxSelectionDefaults());
	}

	private void setBuilderConvenience(final IToolkit toolkit) {
		final IBluePrintProxyFactory bppf = toolkit.getBluePrintProxyFactory();
		
		bppf.setSetupBuilderConvenience(
			IBeanTableSetupBuilder.class, 
			new BeanTableSetupConvenience());
		
		//... removed some convenience methods in this example
		
		bppf.setSetupBuilderConvenience(
			IBeanRelationTreeDetailSetupBuilder.class, 
			new BeanRelationTreeDetailSetupConvenience());
	}

}
~~~


#### Die Schnittstelle IToolkitInterceptorHolder

Um einen Toolkit Interceptor zu registrieren, benötigt man einen `IToolkitInterceptorHolder`. Die Schnittstelle sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public interface IToolkitInterceptorHolder {

    int DEFAULT_ORDER = 2;

	int getOrder();
	
    IToolkitInterceptor getToolkitInterceptor(); 
}
~~~

Mit Hilfe der Order kann die Reihenfolge beeinflusst werden, in der Toolkit Interceptoren aufgerufen werden. Ein Interceptor mit einer kleineren Order wird vor einem Interceptor mit einer größeren Order ausgeführt. Die Methode `getToolkitInterceptor()` wird erst aufgerufen, bevor der Interceptor tatsächlich aufgerufen wird, und nicht bereits bei der Registrierung des Holders.


#### Die Klasse AbstractToolkitInterceptorHolder

Die Klasse `AbstractToolkitInterceptorHolder` liefert eine Basisimplementierung der Schnittstelle `IToolkitInterceptorHolder`. Das folgende Beispiel zeigt die Verwendung anhand des Toolkit Interceptor Holder's des UI Security Plugin der [jo-client-platform](http://code.google.com/p/jo-client-platform/). ^[Das UI Plugin liefert keine echten Security Aspekte, denn die liefert das Service Security Plugin. Das Plugin verhindert aber zum Beispiel die Ausführung von Diensten für die keine Rechte vorhanden sind, um die Usability zu erhöhen, oder dekoriert Widgets, so dass zum Beispiel eine BeanTable, falls man keine Leserechte hat, durch ein Composite mit Informationen darüber an den Nutzer, ersetzt wird.]:

~~~{.java .numberLines startFrom="1"}
public final class CapSecurityUiToolkitInterceptorHolder 
	extends AbstractToolkitInterceptorHolder {

	public CapSecurityUiToolkitInterceptorHolder() {
		super(Integer.MAX_VALUE);
	}

	@Override
	protected IToolkitInterceptor createToolkitInterceptor() {
		return new CapSecurityUiToolkitInterceptor();
	}

}
~~~



#### Registrierung eines Toolkit Interceptor Holder mittels Java ServiveLoader

Eine Implementierung der Schnittstelle `IToolkitInterceptorHolder` kann mit Hilfe des Java [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Mechanismus registriert werden. Dadurch wird sichergestellt, dass der Interceptor vor der ersten Verwendung des Toolkit aufgerufen wird.

Die folgende Abbildung verdeutlicht dabei das Vorgehen beispielhaft Anhand der Swt Implementierung des Addon Browser Widgets:

![Toolkit Interceptor Holder mit Service Loader](images/toolkit_interceptor_holder.gif "Toolkit Interceptor Holder mit Service Loader")

Im Ordner `META-INF/services` muss eine Datei mit dem Namen `org.jowidgets.api.toolkit.IToolkitInterceptorHolder` abgelegt werden, welche den voll qualifizierten Klassennamen aller Implementierungen beinhaltet. Im Beispiel hat die Datei den folgenden Inhalt:

~~~
	org.jowidgets.addons.widgets.browser.impl.swt.BrowserToolkitInterceptorHolder
~~~




#### Explizite Registrierung eines Toolkit Interceptor Holder

Die Klasse `ToolkitInterceptor` liefert die folgende statische Methode zur Registrierung eines `IToolkitInterceptorHolder`:

~~~
	public static void registerToolkitInterceptorHolder(final IToolkitInterceptorHolder holder) {...}
~~~

__Achtung:__ Die explizite Registrierung muss __immer__ vor der ersten Verwendung des Toolkit erfolgen, ansonsten hat sie keinen Effekt. Das explizite Registrieren sollte also nur verwendet werden, wenn man diese Bedingung garantieren kann. Man könnte zum Beispiel vermuten, dass in einem RCP Projekt ein IStartup (earlyStartup()) ein guter Zeitpunkt für die Registrierung ist. Allerdings ist unklar, ob der UI Thread erst gestartet wird, nachdem alle `earlyStartup()` Aufrufe stattgefunden haben (Die [API Spezifikation](http://help.eclipse.org/luna/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fui%2FIStartup.html) macht dazu jedenfalls keine Aussage.) Falls dies nicht der Fall ist, könnte ein anderes Plugin, welches ebenfalls `IStartup` verwendet mittels `Display.asynExcec()` auf das Toolkit zugreifen, bevor der Inteceptor aufgerufen wurde. Aus diesem Grund wird davon dringend abgeraten.

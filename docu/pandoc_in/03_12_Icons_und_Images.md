## Images und Icons{#icons_and_images}

Die folgende Abschnitte liefern eine Übersicht über die Verwendung Images und Icons. 

Es gibt zum einen die Möglichkeit, Images von der [`IImageFactory`](#image_factory) erzeugen zu lassen, was dem Vorgehen klassischer UI Frameworks entspricht.

Zudem bietet jowidgets auch die Möglichkeit, [Image Konstanten](#image_constant) zu definieren, welche über eine [Image Registry](#image_registry) registriert werden. Dadurch wird sicher gestellt, dass ein Image für eine Konstante nur ein Mal erzeugt wird, unabhängig davon, wie oft man die Konstante verwendet.  Die Definition der Konstanten und die Registrierung konkreter Images kann dabei in unterschiedlichen Modulen erfolgen, so dass zum Beispiel für unterschiedliche Implementierungen einer API auch unterschiedliche Icons verwendet werden können. 

Zudem ist es möglich, Images zu einer Image Konstante beliebig [auszutauschen](#substitude_icons). In Software Unternehmen werden oft kommerzielle Icon Bibliotheken eingesetzt, welche von professionellen Grafik Designern erstellt wurden. Jowidgets verwendet für einige Widgets bereits [Icon Konstanten](#icons_small) als Default Icon. ^[Die konkreten Default Icons dafür wurden jedoch nicht von einem professionellen Grafik Designer, sondern von einem Informatiker entworfen, zumindest hat der Ersteller der Default Icons, welcher auch gleichzeitig Autor dieses Textes ist, kein Grafikdesign studiert :-)] Das [Validation Label](#validation_label) verwendet z.B. das Icon `OK` als Default Icon für den MessageType OK. Hat man nun in einer firmeninternen Icon Bibliothek ein _besseres_ Ok Icon, oder will man aus Gründen der Konsistenz, dass überall das gleiche Ok Icon verwendet wird, kann man das Icon einfach _umregistrieren_. ^[Man könnte im konkreten Fall das gleiche auch erreichen, indem man die Widget Defaults des Validation Label global [überschreibt](#widget_defaults_override), allerdings könnte es sein, dass ein Icon von mehreren Widgets (und vielleicht sogar innerhalb der firmeneigenen Widgets) verwendet wird. Das Edit Icon wird zum Beispiel von mehreren Actions der [jo-client-platform](http://code.google.com/p/jo-client-platform/) sowie vom [CollectionInputField](#collection_input_field) verwendet.]

Mit Hilfe von [Image Providern](#image_provider) ist auch eine implizite Registrierung von Images möglich. Im Abschnitt: [Eigene Icon Bibliotheken mit Hilfe von Image Provider Enums](#icon_library_with_image_providers) wird gezeigt, wie dadurch sehr einfach eigene Icon Bibliotheken erstellt werden können.













### Image Konstanten{#image_constant}

Images werden in jowidgets mit Hilfe einer Image Konstante auf einem Widget gesetzt. Die Schnittstelle `IImageConstant` ist die Basisschnittstelle für alle Image Konstanten. Sie hat selbst keine Methoden. ^[Die Schnittstelle sollte treffender `IImageKey` heißen. Aufrund der zahlreichen Verwendung von Images in abgeleiteten Projekten wurde bisher jedoch von einem Refactoring abgesehen.]

Eine Image Konstante muss über die [Image Registry](#image_registry) verfügbar sein, um verwendet werden zu können. Dies ist der Fall, wenn:

* Für die Image Konstante explizit ein [Image Handle](#image_handle) in der [Image Registry](#image_registry) registriert wurde.
* Sie ein selbstbeschreibender [ImageProvider](#image_provider) ist.
* Sie implizit von der [Image Factory](#image_factory) beim Erzeugen eines [Images](#image_factory_images) registriert wurde. 


#### Beispiel für die Verwendung von Enum Konstanten

Die folgenden Beispiele demonstriert die Verwendung vom Image Konstanten, welche durch Enums definiert sind:

~~~
	label.setIcon(IconsSmall.OK);
	
	button.setIcon(SilkIcons.HELP);
	
	tabItem.setIcon(SilkIcons.APPLICATION);
	
	actionBuilder.setIcon(IconsSmall.DISK);
~~~

Ein expliziter Zugriff auf die [Image Registry](#image_registry) ist bei der Verwendung nicht notwendig. Die Adapter Klassen der jeweiligen SPI Implementierung verwenden die Konstante, um aus der [Image Registry](#image_registry) das registrierte [Image Handle](#image_handle) zu erhalten.

#### Image Konstanten und die ICacheable Schnittstelle

Wenn eine Image Konstante die Schnittstelle `ICacheable` implementiert, wird diese aus der Image Registry entfernt und das zugehörige [Image Handle](#image_handle) disposed, sobald auf dem `ICacheable` die Methode:

~~~
	void release();
~~~

aufgerufen wird. Das sollte jedoch nur dann gemacht werden, wenn man sich sicher ist, __dass die Konstante aktuell nicht verwendet wird__. Das _Disposen_ von nativen Images, welche in Verwendung sind, kann andernfalls zu Fehlern führen.

















### Image Handle{#image_handle}

Ein Image Handle stellt das native (UI Framework abhängige) Image zur Verfügung. Des Weiteren kennt ein Image Handle seinen [Image Descriptor](#image_descriptor) wenn es mit Hilfe eines solchen erzeugt wurde. Die Schnittstelle `IImageHandle` hat die folgenden Methoden:

~~~
	Object getImage();

	boolean isInitialized();

	IImageDescriptor getImageDescriptor();
~~~

Die Methode `getImage()` liefert das native Image Objekt. Der Typ kann je nach SPI Implementierung variieren. Das Image wird _lazy_, also beim ersten Aufruf der Methode `getImage()`  erzeugt. Durch das Registrieren eines Image Handle in der Image Registry mit Hilfe eines [Image Descriptor](#image_descriptor) wird daher noch __keine__ Image Resource erzeugt. Die Methode `getImage()` liefert nie `null` zurück. Es könnte jedoch eine Exception geworfen werden, wenn das Image nicht erzeugt werden kann. Dies passiert folglich auch nicht beim Registrieren, sondern bei der ersten Verwendung. Durch die _lazy_ Erzeugung von Resourcen kann man große Icon Bibliotheken mit vielen Icons einbinden, und benötigt zur Laufzeit nur den Speicher für die Icons, die man tatsächlich verwendet. 

Will man wissen, ob das Image bereits erzeugt wurde, kann man dies mit Hilfe der Methode `isInitialized()` prüfen. 

Image Handles, welche mit Hilfe eines [Image Descriptor](#image_descriptor) erstellt wurden, liefern diesen mit Hilfe der Methode `getImageDescriptor()` zurück. Die [Betriebssystem Message Icons](#system_message_icons) und [Buffered Images](#buffered_images) werden zum Beispiel nicht mit Hilfe eines Image Descriptors erstellt, weshalb die Methode dort `null` zurückliefert.

Normalerweise benötigt man Image Handles nicht direkt. Sie werden indirekt von der SPI Implementierung bei der [Image Registry](#image_registry) angefragt.




















### Image Descriptor{#image_descriptor}

Mit Hilfe eines Image Descriptor kann ein [Image Handle](#image_handle) erzeugt werden. Normalerweise registriert man in der [Image Registry](#image_registry) nicht direkt ein [Image Handle](#image_handle), sondern verwendet für die Registrierung zum Beispiel einen Image Descriptor. Derzeit werden zwei Descriptor Typen unterstützt, [`IUrlImageDescriptor`](#url_image_descriptor) und [`IStreamFactoryImageDescriptor`](#stream_factory_image_descriptor). ^[Die Schnittstelle `IStreamImageDescriptor` wird auch unterstützt, ist aber wegen des missverständlichen Methodennamens `getInputStream()` deprecated und soll langfristig entfernt werden.] 

#### Url Image Descriptor{#url_image_descriptor}

Die Schnittstelle `IUrlImageDescriptor` hat die folgende Methode:

~~~
	URL getImageUrl();
~~~

Diese liefert die URL, über welche man das Image einlesen kann. Die Methode darf nicht `null` zurückgeben. Die Klasse `UrlImageDescriptor` liefert eine Implementierung der Schnittstelle `IUrlImageDescriptor`.

Das folgende Beispiel zeigt eine Methode, welche eine Image Konstante mit Hilfe eines `UrlImageDescriptor` in der [Image Registry](#image_registry) registriert:

~~~{.java .numberLines startFrom="1"}
	final void registerImage(final IImageConstant imageConstant, final String relativePath) {
		final String path = rootPath + relativePath;
		final URL url = getClass().getClassLoader().getResource(path);
		final IImageDescriptor descriptor = new UrlImageDescriptor(url);
		Toolkit.getImageRegistry().registerImageConstant(imageConstant, descriptor);
	}
~~~

#### Stream Factory Image Descriptor{#stream_factory_image_descriptor}

Die Schnittstelle `IStreamFactoryImageDescriptor` hat die folgende Methode:

~~~
	InputStream createInputStream();
~~~

Der Input Stream wird genau dann erzeugt, wenn ein natives Image benötigt wird. Jeder Aufruf muss einen neuen Input Stream liefern, der insbesondere nicht `null` sein darf. Der Stream wird vom Aufrufer der Methode geschlossen (`close()`), wenn er nicht mehr benötigt wird.














### Image Provider{#image_provider}

Ein Image Provider ist eine [Image Konstante](#image_constant) und ein [Image Descriptor](#image_descriptor) zugleich. Ein Image Provider muss nicht explizit in der [Image Registry](#image_registry) registriert werden. Bei der ersten Verwendung wird mit Hilfe des [Image Descriptor](#image_descriptor) Anteil ein [Image Handle](#image_handle) erzeugt und für die [Image Konstante](#image_constant) registriert. Dadurch spart man sich zum Beispiel das Halten von Schlüsseln in der Image Registry für Icons, welche gar nicht zur Laufzeit verwendet, aber in einer Icon Bibliothek vorhanden sind. Derzeit werden zwei Image Provider Typen unterstützt, der [`IImageUrlProvider`](#image_url_provider) und der [`IImageStreamFactoryProvider`](#image_stream_factory_provider).

#### Image Url Provider{#image_url_provider}

Die Schnittstelle `IImageUrlProvider` hat die folgende Methode:

~~~
	URL getImageUrl();
~~~

Die Klasse `ImageUrlProvider` implementiert diese Schnittstelle und zudem auch die Schnittstelle `ICacheable`, wodurch ein `ImageUrlProvider` mittels `release()` aus der [Image Registry](#image_registry) entfernt werden kann. Das folgende Beispiel demonstriert die Verwendung der Klasse `ImageUrlProvider`:

~~~{.java .numberLines startFrom="1"}
	final ImageUrlProvider icon = new ImageUrlProvider(iconFile);

	button1.setIcon(icon);
	button2.setIcon(icon);
	
	...
	
	button1.dispose();
	button2.dispose();
	
	//release the icon from the image registry if no longer used
	icon.release();
~~~

Der `ImageUrlProvider` wird in Zeile 1 erzeugt und in Zeile 12 wieder released. Da es sich um einen [Image Provider](#image_provider) handelt, ist ein explizites registrieren in der [Image Registry](#image_registry) nicht notwendig. Für beide Widgets wird nur ein [Image Handle](#image_handle) erzeugt, welches bei der ersten Verwendung automatisch in der Image Registry registriert wird. __Man muss sich selbst darum kümmern, die Konstante, wenn sie nicht mehr benötigt wird, aus der Image Registry zu entfernen__. Mit dem folgenden Code würde man das gleiche wie mit `icon.release()` erreichen:

~~~
	Toolkit.getImageRegistry().unRegisterImageConstant(icon);
~~~

__Anmerkung:__ Gemeinsam genutzte Icons sollten besser in einer dauerhaft gültigen Konstante, wie zu Beispiel einer Enum gehalten, und nur in Ausnahmefällen disposed werden. Durch das Disposen von Icons welche man mehrfach verwendet, können Fehler auftreten, welche unter Umständen nur schlecht reproduzierbar sind. Da man in der Regel (auch in großen Applikationen) nicht Unmengen von unterschiedlichen Icons verwendet, und diese somit auch nicht unverhältnismäßig viel Speicher benötigen, könnte man das Disposen von momentan nicht verwendeten Icons unter Umständen als _verfrühte Optimierung (premature optimization)_ im Sinne von [Knuth](http://en.wikiquote.org/wiki/Donald_Knuth) betrachten. Es gibt jedoch Fälle, wo es sinnvoll sein mag aufzuräumen, zum Beispiel wenn mit Hilfe eines Icon Chooser alle verfügbaren Icons einer Bibliothek angezeigt und somit auch geladen wurden. Hier ist es durchaus sinnvoll, diese Icons anschließend wieder aus der Image Registry zu entfernen.

#### Image Stream Factory Provider{#image_stream_factory_provider}

Die Schnittstelle `IImageStreamFactoryProvider` hat die folgende Methode:

~~~
	InputStream createInputStream();
~~~

Der Input Stream wird genau dann erzeugt, wenn ein natives Image benötigt wird. Jeder Aufruf muss einen neuen Input Stream liefern, der insbesondere nicht `null` sein darf. Der Stream wird vom Aufrufer der Methode geschlossen (`close()`), wenn er nicht mehr benötigt wird.











### Die Image Registry{#image_registry}

Die Image Registry liefert für eine [Image Konstante](#image_constant) ein [Image Handle](#image_handle). In der Regel fragt man das Image Handle nicht selbst ab, sondern verwendet eine [Image Konstante](#image_constant) zur Definition eines Images oder Icons für ein Widget. Die Adapter Klassen in der SPI Implementierung lösen dann mit Hilfe der Image Registry diese [Image Konstante](#image_constant) in ein [Image Handle](#image_handle) auf. 

#### Image Registry Instanz

Eine Image Registry Instanz bekommt man vom Toolkit mittels der folgenden Methode:

~~~
	IImageRegistry getImageRegistry();
~~~

Es folgt eine kurze Auflistung der wichtigsten Methoden der Schnittstelle `IImageRegistry`:


#### Methoden zur Registrierung und zum Austauschen von Image Handles()

Die folgenden Methoden dienen der Registrierung eines [`Image Handle`](#image_handle) zu einer [`Image Konstante`](#image_constant):

~~~{.java .numberLines startFrom="1"}
	void registerImageConstant(IImageConstant key, IImageHandle imageHandle);

	void registerImageConstant(IImageConstant key, IImageDescriptor descriptor);
	
	void registerImageConstant(IImageConstant key, URL url);

	void registerImageConstant(IImageConstant key, IImageConstant substitude);
	
	void registerImageConstant(IImageConstant key, IImageProvider provider);
~~~

Die erste Methode macht die Registrierung explizit. Diese wird normalerweise nur von der SPI Implementierung verwendet, welche das Image Handle erzeugt, zum Beispiel mit Hilfe eines [Image Descriptors](#image_descriptor), welchen man mit der Methode in Zeile 3 übergeben kann. 

Die Methode in Zeile 5 bietet eine verkürzte Schreibweise zur Verwendung eines [UrlImageDescriptor](#url_image_descriptor).

Die nächste Methode in Zeile 7 ersetzt ein Image durch ein anderes, welches durch eine Image Konstante (`substitude`) definiert ist. Die Image Konstante für das `substitude` muss zum Zeitpunkt des Aufrufs [available](#image_constant_available_state) sein, was bedeutet, dass bereits ein Image Handle dafür registriert sein muss. ^[Oder die Image Konstante des `substitude` muss ein selbstbeschreibender [Image Provider](#image_provider) sein. Wird dieser jedoch nicht explizit in eine `IImageConstant` _gecastet_, wird in diesem Fall die Signatur in Zeile 9 verwendet.]. Es ist für die Ausführung der Methode nicht notwendig, dass für den Quell `key` bereits ein Handle registriert ist.

Die letzte Methode in Zeile 9 ersetzt auch eine vorhandene Registrierung. Dazu wird bei Bedarf vorab der Image Provider `provider` (mit seinem eigenen Schlüssel) registriert, wenn er noch nicht vorhanden ist. Das Image, welches der Provider liefert ist dadurch sowohl für die Image Konstante `key` als auch für die Image Konstante des `provider` verfügbar. Es ist für die Ausführung der Methode nicht notwendig, dass für den Quell `key` bereits ein Handle registriert ist.



#### Austauschen von Images{#substitude_icons}

Mit allen oben aufgeführten Methoden kann auch ein bereits registriertes Image Handle durch ein anderes ersetzt (substituiert) werden, jedoch nur, wenn dieses nicht  bereits [initialisiert](#image_constant_intitialized_state) wurde (nicht initialisierte aber registrierte oder verfügbare Handles stellen kein Problem dar). 

Würde man ein bereits initialisiertes Image aus der Registry entfernen (indem man ein neues Image Handle für den `key` setzt), ohne das bisherige, initialisierte Handle vorab zu [disposen](#unregister_images), würde man dadurch ein potentielles Speicherleck provozieren, da zum Beispiel unter SWT das native Image nicht mehr disposed werden würde, wodurch das SWT Display eine Referenz darauf behält. Würde man das alte Handle automatisch disposen, würde man einen potentiellen Folgefehler provozieren, dessen Ursache nur schwer zu finden ist, da er sich erst später auswirken kann. Man kann wie folgt prüfen, ob ein Image Handle für eine Image Konstante initialisiert ist:

~~~
	if (!registry.isImageInitialized(IconsSmall.DISK)) {
		registry.registerImageConstant(IconsSmall.DISK, SilkIcons.DISK);
	}
~~~

__Hinweis:__ Um das Problem grundsätzlich zu vermeiden, wird empfohlen, Registrierungen und Substituierungen immer mit Hilfe eines [Toolkit Interceptor](#toolkit_interceptor) durchzuführen. Dadurch ist sichergestellt, dass die Registrierung stattfindet, bevor ein Image initialisiert werden kann.

Das folgende Beispiel zeigt, wie logische Icons einer Bibliothek durch konkrete ersetzt werden:

~~~{.java .numberLines startFrom="1"}
	registry.registerImageConstant(IconsSmall.OK, IconLib_16x16.TICK);
	registry.registerImageConstant(IconsSmall.EDIT, IconLib_16x16.PENCIL);
	registry.registerImageConstant(IconsSmall.REFRESH, IconLib_16x16.RECYCLE);
	registry.registerImageConstant(IconsSmall.UNDO, IconLib_16x16.ARROW_UNDO);
	registry.registerImageConstant(IconsSmall.CANCEL, IconLib_16x16.CANCEL);
	registry.registerImageConstant(IconsSmall.ERROR, IconLib_16x16.ERROR);
~~~


#### Zugriff auf ein Image Handle

Die folgende Methode liefert ein [Image Handle](#image_handle) für eine [Image Konstante](#image_constant):

~~~
	IImageHandle getImageHandle(IImageConstant key);
~~~


Diese Methode wird in der Regel nur von der SPI Implementierung verwendet, um für eine [Image Konstante](#image_constant) das registrierte [Image Handle](#image_handle) zu erhalten.



#### Deregistrierung von Objekten{#unregister_images}

Die folgenden Methoden können verwendet werden, um Einträge aus der Image Registry zu entfernen. 

~~~
	void unRegisterImageConstant(IImageConstant key);

	void unRegisterImage(IImageCommon image);
~~~

Dabei werden auch immer die zugehörigen Image Handles disposed, falls diese bereits initialisiert wurden. Man sollte daher nur Images deregistrieren, wenn diese nicht mehr verwendet werden. 

Die Methode `unregisterImageConstant()` kann auch für [Images](#image_factory_images) verwendet werden.

#### Der Available Status{#image_constant_available_state}

Mit Hilfe der folgenden Methode kann geprüft werden, ob eine [Image Konstante](#image_constant) verfügbar ist:

~~~
	boolean isImageAvailable(IImageConstant key);
~~~

Dies ist der Fall, wenn entweder ein [Image Handle](#image_handle) für den `key` registriert, oder die Konstante ein [IImageProvider](#image_provider) ist.

#### Der Registered Status{#image_constant_registered_state}

Mit Hilfe der folgenden Methode kann geprüft werden, ob für eine [Image Konstante](#image_constant) ein Image Handle registriert wurde:

~~~
	boolean isImageRegistered(IImageConstant key);
~~~

__Hinweis:__ Wenn die Methode `false` zurückliefert, folgt daraus __nicht__, dass der `key` auch nicht `available` ist (da es sich um einen `IImageProvider` handeln könnte).

#### Der Initialized Status{#image_constant_intitialized_state}

Mit Hilfe der folgenden Methode kann geprüft werden, ob für eine [Image Konstante](#image_constant) ein initialisiertes Image Handle existiert:

~~~
	boolean isImageInitialized(IImageConstant key);
~~~

Wird `true` zurückgegeben, kann das Image nicht mehr umregistriert werden, ohne es vorher aus der Image Registry zu entfernen.

__Hinweis:__ Die Methode führt die Prüfung durch, ohne dabei einen [Image Provider](#image_provider) implizit zu registrieren. Der folgende Aufruf ist daher __keine geeignete Alternative__:

~~~{.java .numberLines startFrom="1"}
	IImageHandle imageHandle = registry.getImageHandle(SilkIcons.ACCEPT);
	if (imageHandle.isInitialized()){
		//do something
	}
~~~

Durch den Aufruf in Zeile 1 wird, wenn noch kein Image Handle existiert, einer erzeugt, da die [Image Konstanten](#image_constant) der [Silk Icons](#silk_icons) Bibliothek [Image Provider](#image_provider) sind.








### Icon Bibliotheken

In großen Projekten spart der Einsatz bereits vorhandener Icon Bibliotheken Zeit und somit Geld. Zudem wird gefördert, dass nicht jeder Entwickler eine eigene Instanz eines Image Files erstellt. Man kann (muss aber nicht :-) bei Icon Bibliotheken zwischen [logischen](#logical_icon_libs) und [konkreten](#concrete_icon_libs) unterscheiden.

#### Logische Icon Bibliotheken{#logical_icon_libs}


Logische Icons beschreiben eher wofür das Icon eingesetzt wird, konkrete Icons eher das, was das Icon abbildet, wobei diese Regel nur als grober Leitfaden, den man nicht zu dogmatisch betrachten sollte, zu verstehen ist. Der logischen Konstante `Icons.EDIT` könnte man dann zum Beispiel das konkrete Icon `IconLib_16x16.PENCIL` als Default zuweisen und der logischen Konstante `Icons.Save` das konkrete Icon `IconLib_16x16.DISK`. Logische Icon Bibliotheken liefern eher genau die Icons Konstanten, welche für einen bestimmten Modul-, Produkt- oder Firmenkontext relevant sind, inklusive eines konkreten Default Icon dafür.

Verwendet man bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) logische Konstanten, können die Default Icons später leicht durch andere Icons ausgetauscht werden. 

Würde man zum Beispiel für den Edit Button in einem firmeninternen Widget die Konstante `IconLib_16x16.PENCIL` verwenden, anstatt `OdzIcons.EDIT` ^[Odz steht hier für ein (fast:-) frei erfundenes Kürzel der Firma, welches die Icon Bibliothek definiert hat.], dann könnte man dieses nicht so einfach umdefinieren. Angenommen in einem Produkt sollen anstatt der `IconLib` die `SmartIcons` verwendet werden, dann würde der folgende Code

~~~{.java .numberLines startFrom="1"}
	registry.registerImageConstant(IconLib_16x16.PENCIL, SmartIcons_16x16.PENCIL);
~~~

alle Pencils ändern. Hat man im gleichen Produkte auch eine Komponente zum Zeichnen mit verschiedenen Stiften, könnte dies zu einem unerwünschten Nebeneffekt führen. 

Mit dem folgenden Code:

~~~{.java .numberLines startFrom="1"}
	registry.registerImageConstant(OdzIcons.EDIT, SmartIcons_16x16.PENCIL);
~~~

würde man nur die Stellen ändern, wo es um Editierung geht.


Der Name der weiter unten beschriebenen Enum [`IconsSmall`](#icons_small) wurde bewusst so und nicht etwa `Icons_16x16` gewählt. Für bestimmte SPI Implementierungen wie zum Beispiel RWT könnte es durchaus Sinn machen, für diese Icons eine andere Auflösung zu wählen, zum Beispiel wenn man per StyleSheet alles etwas größer darstellt. 



#### Konkrete Icon Bibliotheken{#concrete_icon_libs}

Konkrete Icon Bibliotheken enthalten eher eine große Auswahl vieler unterschiedlicher Icons und dienen dadurch sowohl als Pool möglicher Default Icons bei der Erstellung logischer Icon Bibliotheken, also auch als mögliche Substitute für das Ersetzen der logischen Konstanten verschiedenster Module, zum Beispiel auch zur Vereinheitlichung des Look And Feel.

Im Vergleich zu logischen Icons macht es für konkrete Icons durchaus Sinn, die zugehörigen Enums oder Konstanten entsprechend der Auflösung zu benennen, insbesonderer wenn verschiedene Auflösungen verfügbar sind. Für einen SWT oder Swing Client könnte man folgende Icons der Enum [`IconsSmall`](#icons_small) dann zum Beispiel wie folgt Substituieren:

~~~{.java .numberLines startFrom="1"}
	registry.registerImageConstant(IconsSmall.OK, IconLib_16x16.TICK);
	registry.registerImageConstant(IconsSmall.EDIT, IconLib_16x16.PENCIL);
	registry.registerImageConstant(IconsSmall.REFRESH, IconLib_16x16.RECYCLE);
	registry.registerImageConstant(IconsSmall.UNDO, IconLib_16x16.ARROW_UNDO);
	registry.registerImageConstant(IconsSmall.CANCEL, IconLib_16x16.CANCEL);
	registry.registerImageConstant(IconsSmall.ERROR, IconLib_16x16.ERROR);
~~~

Für einen Web Client mit _luftigem_ Layout wie folgt:

~~~{.java .numberLines startFrom="1"}
	registry.registerImageConstant(IconsSmall.OK, IconLib_24x24.TICK);
	registry.registerImageConstant(IconsSmall.EDIT, IconLib_24x24.PENCIL);
	registry.registerImageConstant(IconsSmall.REFRESH, IconLib_24x24.RECYCLE);
	registry.registerImageConstant(IconsSmall.UNDO, IconLib_24x24.ARROW_UNDO);
	registry.registerImageConstant(IconsSmall.CANCEL, IconLib_24x24.CANCEL);
	registry.registerImageConstant(IconsSmall.ERROR, IconLib_24x24.ERROR);
~~~








### Eigene Icon Bibliotheken mit Hilfe von IImageUrlProvider Enums{#icon_library_with_image_providers}

Das folgende Beispiel zeigt, wie man sich eine eigenen Icon Bibliothek mit Hilfe einer [Image Provider](#image_provider) Enum erstellen kann:

~~~{.java .numberLines startFrom="1"}
import java.net.URL;
import org.jowidgets.common.image.IImageUrlProvider;

public enum SilkIcons implements IImageUrlProvider {

	ACCEPT(getIconsPath() + "accept.png"),
	ADD(getIconsPath() + "add.png"),
	ANCHOR(getIconsPath() + "anchor.png"),
	APPLICATION(getIconsPath() + "application.png"),
	APPLICATION_ADD(getIconsPath() + "application_add.png"),

	//...removed most of the icon constants in this code example

	XHTML_GO(getIconsPath() + "xhtml_go.png"),
	XHTML_VALID(getIconsPath() + "xhtml_valid.png"),
	ZOOM(getIconsPath() + "zoom.png"),
	ZOOM_IN(getIconsPath() + "zoom_in.png"),
	ZOOM_OUT(getIconsPath() + "zoom_out.png");

	private static final String ICONS_PATH 
		= "org/jowidgets/addons/icons/silkicons/icons/silkicons/";

	private final URL url;

	private SilkIcons(final String defaultPath) {
		this.url = getClass().getClassLoader().getResource(defaultPath);
	}

	private static String getIconsPath() {
		return ICONS_PATH;
	}

	@Override
	public URL getImageUrl() {
		return url;
	}

}
~~~

Die Enum implementiert die Schnittstelle `IImageUrlProvider`, wodurch die Konstanten vor deren Verwendung nicht explizit registriert werden müssen. 

Die zugehörigen Icon Dateien sind in diesem Beispiel im `resources` Ordner des Moduls unter dem Pfad: `org/jowidgets/addons/icons/silkicons/icons/silkicons/` abgelegt, wie es die folgende Abbildung verdeutlicht:

![Icon Resources](images/icon_resources.gif "Icon Resources")

Das folgende Beispiel zeigt, wie die oben definierten Icons verwendet werden können:

~~~{.java .numberLines startFrom="1"}
	final IButton zoomInButton = container.add(BPF.button().setIcon(SilkIcons.ZOOM_IN));
	final IButton zoomOutButton = container.add(BPF.button().setIcon(SilkIcons.ZOOM_OUT));
~~~

Dieser Ansatz eignet sich sowohl für die Erstellung [logischer-](#logical_icon_libs) als auch für die Erstellung [konkreter](#concrete_icon_libs) Icon Bibliotheken. 

Anmerkung: Beinhaltet eine konkreten Icon Bibliothek alle Icons einer unter Umständen größeren Icon Sammlung, kann es hilfreich sein, die Enum Klasse mit Hilfe eines Skriptes zu generieren. Für die [Silk Icons Bibliothek](#silk_icons) wurde das so gemacht.




### Eigene Icon Bibliotheken - Trennung von API und Implementierung

Im folgenden soll beschrieben werden, wie man eine Icon Bibliothek erstellt, bei der die Definition der Image Konstanten (API) und die Bereitstellung der konkreten Icons (Implementierung) getrennt ist.

Dieser Ansatz eignet sich sowohl für die Erstellung [logischer-](#logical_icon_libs), als auch für die Erstellung [konkreter](#concrete_icon_libs) Icon Bibliotheken. Die Trennung könnte wie folgt motiviert sein:

* __Aufteilung API und Implementierung bei der Erstellung einer Widget Bibliothek:__ Man möchte die API und die Implementierung in unterschiedliche Module packen, eventuell weil es mehrere Implementierungen gibt. Dann gehört die Icon Konstante zur API, das konkrete Icon zur Implementierung.

* __Einsparung von Resourcen im ausgelieferten Produkt:__ Eine Icon API beinhaltet sehr viele Icons einer großer Icon Sammlung. Mehrere Produkte verwenden diese Icon API, welche nur Konstanten enthält, um einen schnellen Zugriff auf die möglichen Icons zu haben. Für ein konkretes Produkt wird dann ein konkretes Plugin erstellt, welches nur die Icons registriert, welche in dem Produkt tatsächlich verwendet werden. Die Menge der verwendeten Icons und somit auch das Plugin könnte man mit einem Tool erstellen (Source Code Generierung). 

#### Definition der Icon Konstanten

Das folgende Beispiel zeigt die Definition der Icons für eine Audio Player Widget Bibliothek. 

~~~{.java .numberLines startFrom="1"}
public enum AudioIcons implements IImageConstant {

	PLAY,
	STOP,
	PAUSE,
	FORWARD,
	REWIND,

	NEXT,
	PREVIOUS,
	FIRST,
	LAST,

	MUTED,
	NOT_MUTED,

	LOUDSPEAKER,
	EDIT_AUDIO_SETTINGS

}
~~~

Die Icon Konstanten können dann für das Default Setup der Widgets und / oder innerhalb der Implementierung verwendet werden.


#### Registrierung der konkreten Icons

Die folgende Klasse verwendet die Klasse [`AbstractResourceImageInitializer`](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/core/org.jowidgets.tools/src/main/java/org/jowidgets/tools/image/AbstractResourceImageInitializer.java) für die Registrierung der konkreten Icons:

~~~{.java .numberLines startFrom="1"}
package org.mycompany.audio.impl;

import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.tools.image.AbstractResourceImageInitializer;

public final class AudioIconsInitializer extends AbstractResourceImageInitializer {

	public AudioIconsInitializer(final IImageRegistry registry) {
		super(AudioIconsInitializer.class, registry, "org/mycompany/audio/impl/icons/");
	}

	@Override
	public void doRegistration() {
		registerResourceImage(AudioIcons.PLAY, "play.png");
		registerResourceImage(AudioIcons.STOP, "stop.png");
		registerResourceImage(AudioIcons.PAUSE, "pause.png");
		registerResourceImage(AudioIcons.FORWARD, "forward.png");
		registerResourceImage(AudioIcons.REWIND, "rewind.png");

		registerResourceImage(AudioIcons.NEXT, "next.png");
		registerResourceImage(AudioIcons.PREVIOUS, "previous.png");
		registerResourceImage(AudioIcons.FIRST, "first.png");
		registerResourceImage(AudioIcons.LAST, "last.png");

		registerResourceImage(AudioIcons.MUTED, "muted.png");
		registerResourceImage(AudioIcons.NOT_MUTED, "not_muted.png");

		registerResourceImage(AudioIcons.LOUDSPEAKER, "loudspeaker.png");
		registerResourceImage(AudioIcons.EDIT_AUDIO_SETTINGS, "edit_audio_settings.png");

		//do this, if your are assuming that this initializer should initialize all icons
		checkEnumAvailability(AudioIcons.class);
	}

}
~~~

Der Test in Zeile 32 ist optional und würde für den Fall, dass man z.B. vergessen hätte, ein Icon zu registrieren, eine `IllegalStateException` werfen. Wenn man zum Beispiel die Konstanten der Bibliothek erweitert, und die konkrete Registrierung vergisst, oder z.B. aus Versehen zwei mal den gleichen `key` für unterschiedliche Icons verwendet, tritt der Fehler bereits beim Starten der Applikation auf, was z.B. für einen _Smoketest_ hilfreich ist, und nicht erst später, wenn das fehlende Icon verwendet wird.  

Die folgende Abbildung zeigt die zugehörigen Icon Dateien innerhalb der Resourcen der Implementierung:

![Audio Icon Resourcen](images/audio_icon_resources.gif   "Audio Icon Resourcen")


#### Registrierung mittels Toolkit Interceptor

Um sicher zu Stellen, dass die Icons vor der ersten Verwendung registriert sind, wird empfohlen, die Registrierung innerhalb eines [Toolkit Interceptors](#toolkit_interceptor) durchzuführen. Das folgende Beispiel nutzt dazu die abstrakte Klasse [`AbstractToolkitInterceptorHolder`](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/core/org.jowidgets.tools/src/main/java/org/jowidgets/tools/toolkit/AbstractToolkitInterceptorHolder.java):

~~~{.java .numberLines startFrom="1"}
package org.mycompany.audio.impl;

import org.jowidgets.api.toolkit.IToolkit;
import org.jowidgets.api.toolkit.IToolkitInterceptor;
import org.jowidgets.tools.toolkit.AbstractToolkitInterceptorHolder;

public final class AudioIconsInitializerToolkitInterceptor 
	extends AbstractToolkitInterceptorHolder {

	@Override
	protected IToolkitInterceptor createToolkitInterceptor() {
		return new IToolkitInterceptor() {
			@Override
			public void onToolkitCreate(final IToolkit toolkit) {
				final AudioIconsInitializer initializer 
					= new AudioIconsInitializer(toolkit.getImageRegistry());
				initializer.doRegistration();
			}
		};
	}

}
~~~

Um diesen mit Hilfe des Java [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Mechanismus zu registrieren, kann man unter `META-INF/services` eine Datei mit dem Namen `org.jowidgets.api.toolkit.IToolkitInterceptorHolder` und dem Inhalt:

~~~
org.jowidgets.helloworld.common.AudioIconsInitializerToolkitInterceptor
~~~

ablegen. Die folgende Abbildung soll das verdeutlichen:

![Icon Toolkit Interceptor](images/icon_toolkit_interceptor.gif   "Icon Toolkit Interceptor")

Dadurch werden die Icons automatisch registriert, wenn man das Modul `org.mycompany.audio.impl` im Classpath hat, und zwar bevor das jowidgets Toolkit verwendet wird, da der Interceptor bei der Erzeugung des Toolkit ausgeführt wird.





### Überblick über vorhandene Icon Bibliotheken 

Jowidgets liefert bereits vorgefertigte Icon Bibliotheken. 

Die Enums [`Icons`](#system_message_icons) und [`IconsSmall`](#icons_small) liefern Konstanten, welche von jowidgets und der [jo-client-platform](http://code.google.com/p/jo-client-platform/) verwendet werden. Das Modul `org.jowidget.impl` liefert konkrete Icons für diese, welcher unter der [BSD Lizenz](https://www.freebsd.org/copyright/freebsd-license.html) stehen, und somit frei verwendbar sind.

Das Addon Modul [`org.jowidgets.addons.icons.silkicons`](#silk_icons) liefert eine [IImageUrlProvider Enum](#icon_library_with_image_providers) für die [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) von FamFamFam. 




### Betriebssystem Message Icons{#system_message_icons}

Die Enum `org.jowidgets.api.image.Icons` enthält Image Konstanten für Betriebssystem Message Icons. Die folgende Tabelle zeigt die Icons mit dem zugehörigen Namen der Konstanten in der Enum.

![Icons](images/system_icons.gif "Icons")

Das Aussehen dieser Icons hängt vom jeweiligen Betriebssystem ab. Die obere Tabelle wurde unter Windows 8 erstellt.












### Icons Small{#icons_small}

Die Enum `org.jowidgets.api.image.IconsSmall` enthält Icons, welche von jowidgets und der [jo-client-platform](http://code.google.com/p/jo-client-platform/) verwendet werden. In der API finden sich nur die Konstanten ohne Default Icons, das Modul `org.jowidgets.impl` registriert für die Konstanten konkrete Icons in einer Auflösung von 16x16 Pixeln. Diese können wie in [Austauschen von Icons](#substitude_icons) beschrieben durch beliebige andere ersetzt (substituiert) werden. Die `IconsSmall` werden dort verwendet, wo in aktuellen (nicht Web) UI Frameworks eine 16x16 Auflösung üblich ist. Dazu zählen insbesondere Menüs, Labels, die Header von Fenstern, Tabellen, TabFoldern, etc..

Die folgende Tabelle zeigt die Default Icons der Enum `IconsSmall` mit dem zugehörigen Namen der Konstanten:

![Icons Small](images/icons_small.gif "Icons Small")

Diese obige Enum beinhalten auch die Betriebsystem Message Icons in einer kleiner Variante, welche in der folgenden Tabelle noch einmal gesondert dargestellt sind:

![System Icons Small](images/system_icons_small.gif "System Icons Small")







### Silk Icons{#silk_icons}

Das Addon Modul `org.jowidgets.addons.icons.silkicons` liefert Icon Konstanten für die [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) von FamFamFam. Diese stehen unter der [Creative Commons Attribution 2.5](http://creativecommons.org/licenses/by/2.5/) oder wahlweise [Creative Commons Attribution 3.0](http://creativecommons.org/licenses/by/3.0/) Lizenz. Weitere Informationen zur Lizenz finden sich auf der Seite von [Silk Icons]([Silk Icons](http://www.famfamfam.com/lab/icons/silk/)). 

Um die SilkIcons zu verwenden, muss das folgende Modul hinzugefügt werden:

~~~{.java .numberLines startFrom="1"}
	<dependency>
		<groupId>org.jowidgets</groupId>
		<artifactId>org.jowidgets.addons.icons.silkicons</artifactId>
		<version>${jowidgets.version}</version>
	</dependency>
~~~

Mit Hilfe des [IconTableSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/IconTableSnipped.java) werden alle SilkIcons in einer Tabelle angezeigt:

![IconTableSnipped mit SilkIcons](images/silk_icons_table.gif "IconTableSnipped mit SilkIcons")

Eine Auflistung aller Icons findet sich auch [hier](http://www.famfamfam.com/lab/icons/silk/previews/index_abc.png).


__Hinweis:__ Jowidgets verwendet bewusst keine Silk Icon Konstanten für die eigenen Widgets, unter anderem weil unklar ist, inwieweit die Lizenzbedingungen von [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) mit der [BSD Lizenz](https://www.freebsd.org/copyright/freebsd-license.html) kompatibel sind.^[Das bedeutet jedoch nicht, dass sie nicht kompatibel sind, sondern nur, dass dies bisher nicht geprüft wurde. Es soll insbesondere vermieden werden, dass man, wenn man jowidgets verwendet, automatisch an die Lizenzbedingungen von Silk Icons gebunden ist, sogar dann, wenn man diese Icon Konstanten durch eigene Images substiuiert.] Jowidgets verwendet in einigen Beispielapplikationen die Silk Icons. Diese Beispiele gehören jedoch nicht zum Kern und sind somit auch nicht Teil einer mit jowidgets erstellten Applikation. 

Bei der Nutzung von jowidgets als UI Framework kann man somit frei entscheiden, ob man SilkIcons verwenden möchte oder nicht. Im ersten Fall fügt man das Silk Icons Addon zu seinem Projekt hinzu, im zweiten Fall muss man nichts zusätzliches unternehmen.















### Die Image Factory{#image_factory}

Die Image Factory ermöglicht die Erzeugung von [Images](#image_factory_images) oder [Buffered Images](#buffered_images).

Die Schnittstelle `IImageFactory' hat die folgenden Methoden:

~~~
	IImage createImage(File file);

	IImage createImage(URL url);

	IImage createImage(IFactory<InputStream> inputStream);

	IBufferedImage createBufferedImage(int width, int height);
~~~





#### Image Factory Instanz

Eine Instanz erhält man vom Toolkit mit Hilfe der folgenden Methode:

~~~
	IImageFactory getImageFactory();
~~~





#### Die Schnittstelle IImage{#image_factory_images}

Die Schnittstelle `IImage` hat die folgenden Methoden:

~~~
	Dimension getSize();

	void initialize();

	void dispose();
	
	boolean isDisposed();
	
	void addDisposeListener(IDisposeListener listener);

	void removeDisposeListener(IDisposeListener listener);
~~~

Ein `IImage` ist von [`IImageConstant`](#image_constant) abgeleitet, und kann somit überall verwendet werden, wo Image Konstanten verwendet werden können.^[Hier macht es sich besonders unangenehm bemerkbar, dass der Name `IImageConstant` und nicht `IImageKey` gewählt wurde, da ein `IImage` nicht zwingend eine Konstante sein muss und das `dispose()` explizit zum Vertrag gehört.]

Wird ein Image (oder [Buffered Image](#buffered_images)) von der Image Factory erzeugt, wird es automatisch in der [Image Registry](#image_registry) registriert, und ist somit für die Verwendung in Widgets unmittelbar verfügbar. 

Das native Image (z.B. `org.eclipse.swt.graphics.Image`) wird erst erzeugt, wenn es einem Widget zugewiesen, oder die Methode `getSize()` oder `initialize()` aufgerufen wurde. Wird das Image mehrfach (für unterschiedliche Widgets) verwendet, wird trotzdem nur eine native Image Instanz erzeugt. 

Wird die `dispose()` Methode auf dem Image aufgerufen, wird das Image automatisch aus der [Image Registry](#image_registry) entfernt und das zugehörige native Image disposed, falls es bereits initialisiert wurde. Das gleiche passiert, wenn auf der [Image Registry](#image_registry) die Methode `unRegisterImage(...)` für das Image aufgerufen wird. In beiden Fällen führen anschließend alle Operation auf dem Image (außer `isDisposed()`) zu einem Fehler.

Images können in jedem beliebigen Thread erzeugt werden. Für Images, welche explizit außerhalb des Ui Thread geladen werden sollen, sollte man darauf achten, dass _ohne_ expliziten Aufruf der Methode `initialize()` nur ein [Image Handle](#image_handle) erzeugt wird, und das eigentliche Laden erst bei der ersten Verwendung im UI Thread stattfindet. 





#### Die Schnittstelle IBufferedImage{#buffered_images}

Die Schnittstelle `IBufferedImage` ist von `IImage` abgeleitet. Zusätzlich zu den geerbten Methoden hat ein Bufferd Image die folgende weitere Methode:

~~~
	IGraphicContext getGraphicContext();
~~~

Ein Buffered Image liefert einen [Graphic Context](#graphic_context) zum Zeichnen des Bildes.




#### Das ImageIconSnipped

Das [`ImageIconSnipped`](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/ImageIconSnipped.java) zeigt die Verwendung eines Images in Verbindung mit einem [Icon Widget](#icon_widget):

~~~{.java .numberLines startFrom="1"}
public final class ImageIconSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrame frame = Toolkit.createRootFrame(
			BPF.frame("Image Icon Snipped"), 
			lifecycle);
		frame.setLayout(FillLayout.get());

		//create a scroll composite
		final IScrollComposite container = frame.add(BPF.scrollComposite());
		container.setLayout(FillLayout.get());

		//create a image from url
		String url = "http://www.jowidgets.org/docu/images/widgets_hierarchy_1.gif";
		final IImage image = ImageFactory.createImage(UrlFactory.create(url));

		//use the icon widget to display the image
		final IIcon imageIcon = container.add(BPF.icon(image));

		//remove the icon on double click from its container to test dispose
		imageIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClicked(final IMouseButtonEvent mouseEvent) {
				imageIcon.dispose();
				container.layoutLater();
			}
		});

		//dispose the image if it was removed from its container
		imageIcon.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				image.dispose();
				//CHECKSTYLE:OFF
				System.out.println("DISPOSED IMAGE");
				//CHECKSTYLE:ON
			}
		});

		//set the root frame visible
		frame.setVisible(true);
	}
}
~~~

Das `image` wird auf einem [Icon Widget](#icon_widget) gesetzt (Zeile 21). Auf dem Icon Widget wird ein Listener registriert, welcher dieses bei einem Doppeklick aus seinem Container entfernt, wodurch es disposed wird. 

In Zeile 16 bis 18 könnte man zum Beispiel auch das Folgende schreiben, um das Image aus einem File einzulesen:

~~~{.java .numberLines startFrom="1"}
	//create a image from file
	final String path = "C:/projects/jo-widgets/trunk/docu/images/widgets_hierarchy_1.gif";
	final IImage image = ImageFactory.createImage(new File(path));
~~~

#### Das ImageCanvasSnipped

Das [`ImageCanvasSnipped`](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/ImageCanvasSnipped.java) zeigt die Verwendung eines Images in Verbindung mit einem [Canvas](#canvas_widget):

~~~{.java .numberLines startFrom="1"}
public final class ImageCanvasSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrame frame = Toolkit.createRootFrame(BPF.frame("Image Canvas Snipped"), lifecycle);
		frame.setLayout(FillLayout.get());

		//create a scroll composite
		final IScrollComposite container = frame.add(BPF.scrollComposite());
		container.setLayout(FillLayout.get());

		//create a image from url
		final String url = "http://www.jowidgets.org/docu/images/widgets_hierarchy_1.gif";
		final IImage image = ImageFactory.createImage(UrlFactory.create(url));

		//use a canvas to display the image
		final ICanvas canvas = container.add(BPF.canvas());

		//set the preferred size of the canvas to the image size
		canvas.setPreferredSize(image.getSize());

		//add a paint listener to draw the image and an arrow
		canvas.addPaintListener(new IPaintListener() {
			@Override
			public void paint(final IPaintEvent event) {
				final IGraphicContext gc = event.getGraphicContext();

				//draw the image
				gc.drawImage(image);

				//draw with green color
				gc.setForegroundColor(Colors.GREEN);

				//define a polygon that shapes an arrow
				final Point p1 = new Point(438, 205);
				final Point p2 = new Point(464, 205);
				final Point p3 = new Point(464, 199);
				final Point p4 = new Point(486, 211);
				final Point p5 = new Point(464, 223);
				final Point p6 = new Point(464, 217);
				final Point p7 = new Point(438, 217);
				final Point[] polygon = new Point[] {p1, p2, p3, p4, p5, p6, p7, p1};

				//fill the polygon 
				gc.fillPolygon(polygon);
			}
		});

		//set the root frame visible
		frame.setVisible(true);
	}
}
~~~

Das Image wird mit Hilfe eine Canvas gezeichnet (Zeile 31). Anschließend wird auf der Grafik ein grüner Pfeil, welcher durch ein Polygon definiert wird, gezeichnet (Zeile 47). Die folgende Abbildung zeigt das Ergebnis:

![ImageCanvasSnipped](images/image_canvas_snipped.gif "ImageCanvasSnipped")

#### Das BufferedImageSnipped

Das [`BufferedImageSnipped`](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/BufferedImageSnipped.java) zeigt die Verwendung eines [Buffered Image](#buffered_images):

~~~{.java .numberLines startFrom="1"}
public final class BufferedImageSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrameBluePrint frameBp = BPF.frame("Buffered Image Snipped");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		frame.setSize(300, 200);
		frame.setBackgroundColor(Colors.WHITE);
		frame.setLayout(FillLayout.builder().margin(10).build());

		//create a arrow buffered image
		final IBufferedImage image = createArrowImage();

		//create a label using the buffered image as icon
		frame.add(BPF.label().setIcon(image).setText("Hello world"));

		//set the root frame visible
		frame.setVisible(true);
	}

	private static IBufferedImage createArrowImage() {
		//create a buffered image
		final IBufferedImage image = ImageFactory.createBufferedImage(52, 26);
		final IGraphicContext gc = image.getGraphicContext();

		//use anti aliasing
		gc.setAntiAliasing(AntiAliasing.ON);

		//define a polygon that shapes an arrow
		final Point p1 = new Point(0, 6);
		final Point p2 = new Point(26, 6);
		final Point p3 = new Point(26, 0);
		final Point p4 = new Point(48, 12);
		final Point p5 = new Point(26, 24);
		final Point p6 = new Point(26, 18);
		final Point p7 = new Point(0, 18);
		final Point[] polygon = new Point[] {p1, p2, p3, p4, p5, p6, p7, p1};

		//use white background for the image
		gc.setBackgroundColor(Colors.WHITE);
		gc.clear();

		//draw with green color
		gc.setForegroundColor(Colors.GREEN);

		//fill the polygon 
		gc.fillPolygon(polygon);

		return image;
	}
}
~~~

Die folgende Abbildung zeigt das Ergebnis:

![BufferedImageSnipped](images/buffered_image_snipped.gif "BufferedImageSnipped")






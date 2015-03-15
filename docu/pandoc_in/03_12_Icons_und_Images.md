## Images und Icons{#icons_and_images}

Die folgende Abschnitte liefern eine Übersicht über die Verwendung Images und Icons. 

Es gibt zum einen die Möglichkeit, Images von der [`IImageFactory`](#image_factory) erzeugen zu lassen, was dem Vorgehen klassischer UI Frameworks entspricht.

Zudem bietet jowidgets auch die Möglichkeit, logische [Image Konstanten](#image_constant) zu definieren, welche über eine [Image Registry](#image_registry) registriert werden. Dadurch wird sicher gestellt, dass ein Image für eine Konstante nur ein Mal erzeugt wird, unabhängig davon, wie oft man die Konstante verwendet.  Die Definition der Konstanten und die Registrierung konkreter Images kann dabei in unterschiedlichen Modulen erfolgen, so dass zum Beispiel für unterschiedliche Implementierungen einer API auch unterschiedliche Icons verwendet werden können. 

Zudem ist es möglich, Images zu einer Image Konstante beliebig [auszutauschen](#substitude_icons). In Software Unternehmen werden oft kommerzielle Icon Bibliotheken eingesetzt, welche von professionellen Grafik Designern erstellt wurden. Jowidgets verwendet für einige Widgets bereits [Icon Konstanten](#icons_small) als Default Icon. ^[Die Default Icons dafür wurden jedoch nicht von einem professionellen Grafik Designer, sondern von einem Informatiker entworfen, zumindest hat der Ersteller der Default Icons, welcher auch gleichzeitig Autor dieses Textes ist, kein Grafikdesign studiert :-)] Das [Validation Label](#validation_label) verwendet z.B. das Icon `OK` als Default Icon für den MessageType OK. Hat man nun in einer firmeninternen Icon Bibliothek ein _besseres_ Ok Icon, oder will man aus Gründen der Konsistenz, dass überall das gleiche Ok Icon verwendet wird, kann man das Icon einfach _umregistrieren_. ^[Man könnte im konkreten Fall das gleiche auch erreichen, indem man die Widget Defaults des Validation Label global [überschreibt](#widget_defaults_override), allerdings könnte es sein, dass ein Icon von mehreren Widgets (und vielleicht sogar innerhalb der firmeneigenen Widgets) verwendet wird. Das Edit Icon wird zum Beispiel von mehreren Actions der [jo-client-platform](http://code.google.com/p/jo-client-platform/) sowie vom [CollectionInputField](#collection_input_field) verwendet.]

Mit Hilfe von [Image Providern](#image_provider) ist auch eine implizite Registrierung von Images möglich. Im Abschnitt: [Eigene Icon Bibliotheken mit Hilfe von Image Provider Enums](#icon_library_with_image_providers) wird gezeigt, wie dadurch sehr einfach eigene Icon Bibliotheken erstellt werden können.



### Image Konstanten{#image_constant}

Images werden in jowidgets mit Hilfe einer Image Konstante auf einem Widget gesetzt. Die Schnittstelle `IImageConstant` ist die Basisschnittstelle für alle Image Konstanten. Sie hat selbst keine Methoden. ^[Die Schnittstelle sollte treffender `IImageKey` heißen. Aufrund der zahlreichen Verwendung von Images in abgeleiteten Projekten wurde bisher jedoch von einem Refactoring abgesehen.]

Eine Image Konstante muss über die [Image Registry](#image_registry) verfügbar sein, um verwendet werden zu können. Dies ist der Fall, wenn:

* Für die Image Konstante explizit ein [Image Handle](#image_handle) in der [Image Registry](#image_registry) registriert wurde.
* Sie ein selbstbeschreibender [ImageProvider](#image_provider) ist.
* Sie implizit von der [Image Factory](#image_factory) beim Erzeugen eines [Images](#image_factory_images) registriert wurde. 

#### Image Konstanten und die ICacheable Schnittstelle

Wenn eine Image Konstante die Schnittstelle `ICacheable` implementiert, wird diese aus der Image Registry entfernt und das zugehörige [Image Handle](#image_handle) disposed, sobald auf dem `ICacheable` die Methode:

~~~
	void release();
~~~

aufgerufen wird. Das sollte jedoch nur dann gemacht werden, wenn man sich sicher ist, __dass die Konstante aktuell nicht verwendet wird__. Das _Disposen_ von nativen Images, welche in Verwendung sind, kann andernfalls zu Fehlern führen.

#### Beispiel für die Verwendung von Enum Konstanten

Die folgenden Beispiele demonstriert die Verwendung vom Image Konstanten, welche durch Enums definiert sind:

~~~
	label.setIcon(IconsSmall.OK);
	
	button.setIcon(SilkIcons.HELP);
	
	tabItem.setIcon(SilkIcons.APPLICATION);
	
	actionBuilder.setIcon(IconsSmall.DISK);
~~~

Ein expliziter Zugriff auf die [Image Registry](#image_registry) ist bei der Verwendung nicht notwendig. Die Adapter Klassen der jeweiligen SPI Implementierung verwenden die Konstante, um aus der [Image Registry](#image_registry) das registrierte [Image Handle](#image_handle) zu erhalten.

#### Beispiel für die Verwendung eines ImageUrlProvider

Das folgende Beispiel zeigt die Verwendung eines ImageUrlProvider. Dieser ist eine Image Konstante und ein [Image Descriptor](#image_descriptor) zugleich:

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

Der ImageUrlProvider wird in Zeile 1 erzeugt und in Zeile 12 wieder released. Da es sich um einen [Image Provider](#image_provider) handelt, ist ein explizites registrieren in der [Image Registry](#image_registry) nicht notwendig. Für beide Widgets nur ein [Image Handle](#image_handle) erzeugt, welches bei der ersten Verwendung automatisch in der Image Registry registriert wird. __Man muss sich in diesem Fall selbst darum kümmern, die Konstante, wenn sie nicht mehr benötigt wird, aus der Image Registry zu entfernen__. Mit dem folgenden Code würde man das gleiche wie mit `icon.release()` erreichen:

~~~
	Toolkit.getImageRegistry().unRegisterImageConstant(icon);
~~~

__Anmerkung:__ Gemeinsam benutzte Icons sollten besser in einer dauerhaft gültigen Konstante, wie zu Beispiel einer Enum gehalten, und nur in Ausnahmefällen disposed werden. Durch das Disposen von Icons welche man mehrfach verwendet, können Fehler auftreten, welche unter Umständen nur schlecht reproduzierbar sind. Da man in der Regel (auch in großen Applikationen) nicht Unmengen von unterschiedlichen Icons verwendet, und diese somit auch nicht unverhältnismäßig viel Speicher benötigen, könnte man das Disposen von nicht verwendeten Icons unter Umständen als _verfrühte Optimierung (premature optimization)_ im Sinne von [Knuth](http://en.wikiquote.org/wiki/Donald_Knuth) betrachten. Es gibt jedoch Fälle, wo es sinnvoll sein mag aufzuräumen, zum Beispiel wenn mit Hilfe eines Icon Chooser alle verfügbaren Icons einer Bibliothek angezeigt und somit auch geladen werden. Hier ist es durchaus Sinnvoll, diese Icons anschließend wieder aus der Image Registry zu entfernen.





### Image Handle{#image_handle}

Ein Image Handle stellt das native (UI Framework abhängige) Image zur Verfügung. Des Weiteren kennt ein Image Handle seinen [Image Descriptor](#image_descriptor) wenn es mit Hilfe eines solchen erzeugt wurde. Die Schnittstelle `IImageHandle` hat die folgenden Methoden:

~~~
	Object getImage();

	boolean isInitialized();

	IImageDescriptor getImageDescriptor();
~~~

Die Methode `getImage()` liefert das native Image Objekt. Der Typ kann je nach SPI Implementierung variieren. Das Image wird _lazy_, also beim ersten Aufruf der Methode `getImage()`  erzeugt. Die Methode liefert nie `null` zurück. Es könnte jedoch eine Exception geworfen werden, wenn das Image nicht erzeugt werden kann. 

Will man wissen, ob das Image bereits erzeugt wurde, kann man dies mit Hilfe der Methode `isInitialized()` prüfen. 

Image Handles, welche mit Hilfe eines [Image Descriptor](#image_descriptor) erstellt wurden, liefern diesen mit Hilfe der Methode `getImageDescriptor()` zurück. Die [Betriebssystem Message Icons](#system_message_icons) und [Buffered Images](#buffered_images) werden nicht mit Hilfe eines Image Descriptors erstellt, weshalb die Methode dort `null` zurückliefert.

Normalerweise benötigt man Image Handles nicht direkt. Sie werden indirekt von der SPI Implementierung bei der [Image Registry](#image_registry) angefragt, um Images zu einer Image Konstante nicht mehrfach zu erzeugen.








### Image Descriptor{#image_descriptor}

Mit Hilfe eines Image Descriptor kann ein natives Image erzeugt werden. Derzeit werden zwei Descriptor Typen unterstützt, [`IUrlImageDescriptor`](#url_image_descriptor) und [`IStreamFactoryImageDescriptor`](#stream_factory_image_descriptor)^[Die Schnittstelle `IStreamImageDescriptor` wird auch unterstützt, ist aber wegen des missverständlichen Methodennamens `getInputStream()` deprecated und soll langfristig entfernt werden.]. 

#### Url Image Descriptor{#url_image_descriptor}

Die Schnittstelle `IUrlImageDescriptor` hat die folgende Methode:

~~~
	URL getImageUrl();
~~~

Diese liefert die URL, über welche man das Image einlesen kann. Die Methode darf nicht Null zurückgeben. Die Klasse `org.jowidgets.tools.image.UrlImageDescriptor` liefert ein Implementierung der Schnittstelle `IUrlImageDescriptor`.


#### Stream Factory Image Descriptor{#stream_factory_image_descriptor}

Die Schnittstelle `IStreamFactoryImageDescriptor` hat die folgende Methode:

~~~
	InputStream createInputStream();
~~~

Die Input Stream wird genau dann erzeugt, wenn ein natives Image benötigt wird. Jeder Aufruf muss einen neuen Input Stream liefern, der insbesondere nicht `null` sein darf. Der Stream wird vom Aufrufer der Methode geschlossen (`close()`), wenn er nicht mehr benötigt wird.





### Image Provider{#image_provider}

Ein Image Provider ist eine Image Konstante und ein 





### Die Image Registry{#image_registry}



### Eigene Icon Bibliotheken mit Hilfe von Image Provider Enums{#icon_library_with_image_providers}


### Eigene Icon Bibliotheken - Allgemeiner Ansatz




### Vordefinierte Icon Bibliotheken

Es folgt eine Auflistung verfügbarer Icon Bibliotheken.

#### Betriebssystem Message Icons{#system_message_icons}

Die Enum `org.jowidgets.api.image.Icons` enthält Image Konstanten für Betriebssystem Message Icons:

![Icons](images/system_icons.gif "Icons")

Das Aussehen hängt vom jeweiligen Betriebssystem ab. Die obere Tabelle wurde unter Windows 8 erstellt.

#### Icons Small{#icons_small}

Die Enum `org.jowidgets.api.image.IconsSmall` enthält Icons, welche von jowidgets und der [jo-client-platform](http://code.google.com/p/jo-client-platform/) verwendet werden. In der API finden sich nur die Konstanten ohne Icons, das Modul `org.jowidgets.impl` registriert für die Konstanten konkrete Icons. Diese können wie in [Austauschen von Icons](#substitude_icons) beschrieben durch beliebige andere ersetzt (substituiert) werden. 

Die folgende Tabelle zeigt die Icons der Enum `IconsSmall`. Dabei handelt es sich um 16 x 16 Pixel Icons:

![Icons Small](images/icons_small.gif "Icons Small")

Diese beinhalten auch die Betriebsystem Message Icons in einer 16 x 16 Pixel Auflösung:

![System Icons Small](images/system_icons_small.gif "System Icons Small")

#### Silk Icons

Das Addon Modul `org.jowidgets.addons.icons.silkicons` liefert Icon Konstanten für die [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) von FamFamFam. Diese stehen unter der [Creative Commons Attribution 2.5](http://creativecommons.org/licenses/by/2.5/) oder wahlweise [Creative Commons Attribution 3.0](http://creativecommons.org/licenses/by/3.0/) Lizenz. Weitere Informationen zur Lizenz finden sich auf der Seite von [SilkIcons]([Silk Icons](http://www.famfamfam.com/lab/icons/silk/)). 

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

Eine Auflistung aller Icons findet sich [hier](http://www.famfamfam.com/lab/icons/silk/previews/index_abc.png).

### Austauschen von Icons{#substitude_icons}

### Die Image Factory{#image_factory}

#### Images{#image_factory_images}

#### Buffered Images{#buffered_images}


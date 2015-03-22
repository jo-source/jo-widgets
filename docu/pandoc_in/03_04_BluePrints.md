## BluePrints - Übersicht{#blue_prints}

BluePrints werden benötigt, um Widgets zu erzeugen. Siehe auch [HelloWorldApplication - Der common Ui Code](#hello_world_common_code)

Mit Hilfe eines BluePrint wird das (Default) Setup eines Widgets festgelegt. Wenn ein Widget erzeugt wird, werden alle Eigenschaften, welche auf dem Setup definiert sind, für das Widget übernommen. Einige Parameter eines Setups sind Pflichtparameter. Diese sind sind mit der `@Mandatory` Annotation gekennzeichnet. Für Pflichtparameter existieren, wenn dies _sinnvoll_ möglich ist, Defaultwerte, welche beliebig überschrieben werden können. Siehe dazu auch [Widget Defaults](#widget_defaults). Die Setter Methoden eines BluePrint haben, wie beim Builder Pattern üblich, immer die Instanz als Rückgabewert. Dadurch lassen sich die Methodenaufrufe einfach verketten. 

Ein BluePrint kann Eigenschaften enthalten, welche für das Widget nicht mehr veränderbar sind. Zum Beispiel muss die `Orientation` eines `SplitComposite` initial auf horizontal oder vertikal festgelegt werden, und ist nachträglich nicht mehr änderbar. Durch die klare Trennung der Setup und Widget Schnittstellen lassen sich (auch für eigene Widgets) sogenannte [imutable Member](http://www.javapractices.com/topic/TopicAction.do?Id=29) einfach umsetzen, ohne dabei lange Parameterlisten in Konstruktoren zu benötigen. Dies kann die Implementierung eines Widgets erleichtern, weil nicht alle Eigenschaften modifizierbar implementiert werden müssen. 

Die Frage, ob eine Eigenschaft imutable ist oder nicht ist eine Designfrage. Man könnte sich zum Beispiel auf den Standpunkt stellen, dass ein Fenster entweder _closeable_ ist oder nicht. Diesen Zustand während der Anzeige des Fensters zu ändern wäre ungewöhnlich, daher kann man sich den Implementierungsaufwand dafür auch einsparen. Das man den Titel eines Fensters zur Laufzeit ändert, ist jedoch üblich. Beispielsweise zeigt das Explorer Fenster von Windows immer den ausgewählten Ordner im Titel an. Diese Eigenschaft sollte also eher veränderbar entworfen werden. Das Prinzip, dass Widgets unveränderbare Eigenschaften haben, findet sich auch bei SWT wieder. Dort werden diese über den Style, welcher eine Bitmaske darstellt, gesetzt. 

Für die [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) kann der BluePrint Mechanismus verwendet werden. Dabei müssen die BluePrint Schnittstellen nicht selbst implementiert werden, da die Implementierung von der [Blue Print Proxy Factory](#blue_print_proxy_factory) mit Hilfe von [Java Proxies](http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Proxy.html) umgesetzt wird. Das Definieren der BluePrint Schnittstelle reicht also aus. 



### Die BluePrint Factory{#blue_print_factory}

Für die [Core Widgets](#core_widgets) kann man sich die Instanzen der BluePrints von der BluePrintFactory erzeugen lassen. Diese erhält man vom Toolkit wie folgt:

~~~
	IBluePrintFactory bluePrintFactory = Toolkit.getBluePrintFactory();
~~~

Andere [Widget Bibliotheken](#custom_widget_libraries) oder auch die [Addon Widgets](#addon_widgets) stellen jeweils ihre eigene BluePrint Factory zur Verfügung. Die Klasse [BrowserBPF](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/addons/org.jowidgets.addons.widgets.browser.api/src/main/java/org/jowidgets/addons/widgets/browser/api/BrowserBPF.java) liefert zum Beispiel die BluePrints für die Browser Widgets.

#### Die Abbreviation Accessor Klasse BPF{#bpf_accessor_class}

Alle Methoden der Schnittstelle IBluePrintFactory sind auch über die statische Abbreviation Accessor Klasse org.jowidgets.tools.widgets.blueprint.BPF verfügbar. Dadurch kann man zum Beispiel anstatt:

~~~
	final IFrameBluePrint frameBp = Toolkit.getBluePrintFactory().frame();
~~~

auch das Folgende schreiben:

~~~
	final IFrameBluePrint frameBp = BPF.frame();
~~~




#### Beispiel für die Verwendung des IFrameBluePrint {#blue_prints_examples}


Im folgenden Beispiel wird ein BluePrint für ein Frame mit dem Titel "My Frame" erzeugt, welches immer beim Anzeigen zentriert werden soll (bezüglich des Vaterfensters, und falls es ein Root Fenster ist, bezüglich des Bildschirms), welches beim Schließen automatisch `disposed` wird und dessen Größe nicht durch den Nutzer geändert werden kann:

~~~{.java .numberLines startFrom="1"} 
	final IFrameBluePrint frameBp = BPF.frame();
	frameBp
		.setTitle("My Frame")
		.setAutoCenterPolicy(AutoCenterPolicy.ALWAYS)
		.setAutoDispose(true)
		.setResizable(false);
~~~

Mit diesem Frame BluePrint kann man zum Beispiel mit Hilfe des Toolkit ein Root Frame

~~~
	IFrame frame = Toolkit.createRootFrame(frameBp);
~~~

oder auch wie folgt ein Kind Fenster erzeugen

~~~
	IFrame childFrame = frame.createChildWindow(frameBp);
~~~



#### Beispiel für die Verwendung des ISliderViewerBluePrint

Im folgenden Beispiel wird ein BluePrint für ein `SliderViewer` erzeugt, welcher `Double` Werte von 0.0 bis 1.0 annimmt, vertikal ausgerichtet ist, keine Scala anzeigt, das Tooltip "Brigthness" hat und an den `ObservabelValue` `brightness` gebunden ist. 

~~~{.java .numberLines startFrom="1"} 
	final ISliderViewerBluePrint<Double> sliderViewerBp = BPF.sliderViewer();
	sliderViewerBp
		.setConverter(SliderConverterFactory.linearConverter(1.0))
		.setOrientation(Orientation.VERTICAL)
		.setRenderTicks(false)
		.setToolTipText("Brigthness")
		.setObservableValue(brightness);
~~~

Mit Hilfe dieses BluePrint könnte zum Beispiel wie folgt ein `SliderViewer` Widget erzeugt werden:

~~~
	final ISliderViewer<Double> sliderViewer = frame.add(sliderViewerBp);
~~~


#### Beispiele für die Verwendung des ILabelBluePrint

Das folgende Beispiel zeigt die Wiederverwendung eines Label BluePrint:

~~~{.java .numberLines startFrom="1"} 
	final ILabelBluePrint labelBp	= BPF.label();
	labelBp
		.setAlignment(AlignmentHorizontal.RIGHT)
		.setForegroundColor(Colors.ERROR)
		.setIcon(IconsSmall.ERROR);
		
	container.add(labelBp.setText("Error 1"));
	container.add(labelBp.setText("Error 2"));
	container.add(labelBp.setText("Error 3"));
~~~

Das Alignment, die Farbe sowie das Icon wird nur ein Mal definiert und daraus anschließend drei Label Widgets mit unterschiedlichem Text erzeugt.














### Setup, Setup Builder, Descriptor und Widget Schnittstellen

Ein Widget (der Typ, nicht die Instanz) ist durch seine [BluePrint Schnittstelle](#blue_print_interface) und seine [Widget Schnittstelle](#blue_prints_widget_interface) eindeutig festgelegt. 

Die [Widget Schnittstelle](#blue_prints_widget_interface) definiert die Schnittstelle des Widgets, die nach seiner Erzeugung (zum Beispiel für Modifikationen, Abfragen und Funktionen) zur Verfügung steht. 

Die [BluePrint Schnittstelle](#blue_print_interface) vereint die folgenden Aspekte:

* [Widget Setup](#widget_setup)

* [Widget Descriptor](#widget_descriptor)

* [Widget Setup Builder](#widget_setup_builder)


Für die [Core Widgets](#core_widgets) sind diese Aspekte in unterschiedliche Schnittstellen aufgeteilt.^[Dies muss bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) nicht zwingend so gemacht werden. Das Zusammenfassung von Setup und Setup Builder in eine Schnittstelle hat sich durchaus als praktikabel erwiesen und wird zum Beispiel auch bei den Widgets der [jo-client-platform](http://code.google.com/p/jo-client-platform/) so umgesetzt.] 




#### Label Widget Beispiel

Die Aufteilung der Schnittstellen soll anhand des Label Widgets verdeutlicht werden. Ein Label Widget setzt sich aus einem Icon und einem Text Label zusammen. Ein Label Widget hat die [BluePrint Schnittstelle](#blue_print_interface) `ILabelBluePrint` und die [Widget Schnittstelle](#blue_prints_widget_interface) `ILabel`. 





#### Widget Schnittstelle{#blue_prints_widget_interface}

Die Widget Schnittstelle legt den Vertrag des Widgets (nach seiner Erzeugung) fest. Ein Widget Interface muss mindestens von [IWidget](#widget_interface) abgeleitet sein. Widgets welche zu einem Container hinzugefügt werden sollen, sind mindestens von [IControl](#control_interface) abgeleitet usw..

Die Schnittstelle `ILabel` ist von `ITextLabel` und `IIcon` abgeleitet, welche wiederum von [IControl](#control_interface), [IComponent](#component_interface) und [IWidget](#widget_interface) abgeleitet sind, und hat damit die folgenden Methoden:

~~~{.java .numberLines startFrom="1"}
	//inherited from IIcon
	
	void setIcon(IImageConstant icon);
	
	IImageConstant getIcon();

	//inherited from ITextLabel
	
	void setFontSize(int size);

    void setFontName(String fontName);

    void setMarkup(Markup markup);

    void setText(String text);
	
	String getText();
	
	//inherited from IWidget
	
	Object getUiReference()
	
	//...and a lot more
	
	//inherited from IComponent
	
	void setForegroundColor(final IColorConstant colorValue);
	
	//...and a lot more
	
	//inherited from IControl
	
	void setToolTipText(String toolTip);
	 
	//...and a lot more
~~~

Anmerkung: Die von [IControl](#control_interface), [IComponent](#component_interface) und [IWidget](#widget_interface) geerbten Methoden wurden dabei nicht vollständig aufgezählt.






#### Widget Setup{#widget_setup}

Das Widget Setup liefert der Widget Implementierung die Konfiguration eines Widgets. Ein Setup besteht immer aus Properties, welche mit `get()`, `is()` oder `has()` abgefragt werden können.
Setup Schnittstellen können von anderen abgeleitet sein. Zum Beispiel leitet `ILabelSetup` von `IIconSetup` und von `ITextLabelSetup` ab, und hat keine zusätzlichen Methoden. 

Ein `ILabelSetup` hat dadurch die folgenden Methoden:

~~~
	IImageConstant getIcon();
	
	String getText();

    String getToolTipText();

    @Mandatory
    Markup getMarkup();

    AlignmentHorizontal getAlignment();
	
	Integer getFontSize();

    String getFontName();
	
	Boolean isVisible();

    IColorConstant getForegroundColor();

    IColorConstant getBackgroundColor();
~~~

Die `@Mandatory` gibt an, dass diese Properties nicht `null` sein dürfen. Wenn es (sinnvoll) möglich ist, haben solche Properties einen Defaultwert. Für das Markup und das Alignment ist ein sinvoller Default möglich (`Markup.DEFAULT` und `AlignmentHorizontal.LEFT`). Stellt eine Property zum Beispiel ein Interface dar, welches der Nutzer des Widgets implementiert (sozusagen als SPI), ist es unter umständen nicht möglich, dafür einen sinnvollen Default anzubieten. Dann führt das Weglassen dieser Property zu einem Fehler, wenn das Widget erzeugt wird.



#### Widget Setup Builder{#widget_setup_builder}

Ein Widget Setup Builder liefert die Setter Methoden zu einen [Widget Setup](#widget_setup). Diese sind alle nach dem folgenden Muster aufgebaut:

~~~
	BLUE_PRINT_TYPE set(PROPERTY_TYPE property);
~~~

Dabei ist `PROPERTY_TYPE` der Java Typ der Property, die gesetzt werden soll. Der BLUE_PRINT_TYPE ist der Typ des BluePrint, um verkettete Aufrufe zu ermöglichen. 


Die Schnittstelle `ILabelSetupBuilder` ist von `IIconSetupBuilder` und `ITextLabelSetupBuilder` abgeleitet und hat die folgenden einfachen Setter Methoden:

~~~
	BLUE_PRINT_TYPE setIcon(IImageConstant icon);
	
	BLUE_PRINT_TYPE setText(String text);

    BLUE_PRINT_TYPE setToolTipText(String text);

    BLUE_PRINT_TYPE setMarkup(Markup markup);

    BLUE_PRINT_TYPE setAlignment(AlignmentHorizontal alignmentHorizontal);
	
	BLUE_PRINT_TYPE setFontSize(Integer size);

    BLUE_PRINT_TYPE setFontName(String fontName);

	BLUE_PRINT_TYPE setVisible(Boolean visible);

    BLUE_PRINT_TYPE setForegroundColor(final IColorConstant foregroundColor);

    BLUE_PRINT_TYPE setBackgroundColor(final IColorConstant backgroundColor);
~~~


Zusätzlich zu den Bean Property Setter Methoden kann ein Setup Builder weitere Convenience Methoden haben. Die Schnittstelle `ILabelSetupBuilder` hat folgende Convenience Methoden:

~~~
	INSTANCE_TYPE alignLeft();

    INSTANCE_TYPE alignCenter();

    INSTANCE_TYPE alignRight();

    INSTANCE_TYPE setStrong();
	
	INSTANCE_TYPE setEmphasized();
~~~

Diese Methoden ermöglichen eine verkürzte Schreibweise. So kann man zum Beispiel anstatt: 

~~~
	final ILabelBluePrint labelBp = 
		BPF.label().setAlignment(AlignmentHorizontal.RIGHT).setMarkup(Markup.STRONG);
~~~

auch 

~~~
	 final ILabelBluePrint labelBp = BPF.label().alignRight().setStrong();
~~~

Sowohl die Getter Methoden eines [Widget Setup](#widget_setup) als auch die einfachen Setter Methoden eines Widget Setup Builders werden mit Hilfe eines Java Proxy implementiert. Einen solchen BluePrintProxy erhält man von der [BluePrint Proxy Factory](#blue_print_proxy_factory). Insbesondere bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) reicht also die Definition der Setup und Setup Builder Schnittstelle aus. Die Convenience Methoden sind durch (eine oder mehrere) eigene Schnittstellen definiert. Eine Implementierung der Convenience Schnittstelle muss bei der [BluePrint Proxy Factory](#blue_print_proxy_factory) registriert werden. 


Die Widget Setup Builder von jowidgets sind von der Schnittstelle `ISetupBuilder` abgeleitet. Diese hat die folgende Methode:

~~~
	 INSTANCE_TYPE setSetup(IComponentSetupCommon descriptor);
~~~

Dadurch kann ein anderes Setup, welches auch einen anderen Typ haben kann, auf dem Builder gesetzt werden. Dabei werden alle Properties mit gleichen Namen und gleichem Typ auf dem Builder gesetzt. Auf diese Weise lassen sich leicht Kopien von Setups erzeugen. 


#### Widget Descriptor{#widget_descriptor}

Jeder Widget Typ benötigt seinen eigenen Widget Descriptor Typ, welcher mit Hilfe einer Schnittstelle definiert wird, die von `IWidgetDescriptor` abgeleitet ist.

Die Schnittstelle `ILabelDescriptor` ist zum Beispiel wie folgt definiert:

~~~
	public interface ILabelDescriptor extends ILabelSetup, IWidgetDescriptor<ILabel> {}
~~~

Für die Schnittstelle ILabelDescriptor kann genau eine Implementierung (zur selben Zeit) in der [Generic Widget factory](#generic_widget_factory) registriert sein. Der Widget Descriptor ist gleichzeitig auch ein Widget Setup (im konkreten Fall ein `ILabelSetup`). Ein Widget Descriptor liefert somit alles, was von der [Generic Widget Factory](#generic_widget_factory) für die Erzeugung eines Widget benötigt wird. 


#### BluePrint Schnittstelle{#blue_print_interface}

Die BluePrint Schnittstelle fügt dem [Widget Descriptor](#widget_descriptor) noch den [Widget Setup Builder](#widget_setup_builder) Aspekt hinzu.

Die Schnittstelle `ILabelBluePrint` ist zum Beispiel wie folgt definiert:

~~~
	public interface ILabelBluePrint extends ILabelSetupBuilder<ILabelBluePrint>, ILabelDescriptor {}
~~~


__Hinweis:__ Für jedes Widget existiert genau eine eigene BluePrint Schnittstelle, während unterschiedliche Widgets sich die gleiche Widget Schnittstelle Teilen können (z.B. `IFrame` für das Frame Widget (`IFrameBluePrint`) und das Dialog Widget (`IDialogBluePrint`). Die Hierarchien der Setup und Widget Schnittstellen müssen nicht zwingend gleich sein. So ist ein `LoginDialogSetup` zum Beispiel vom `ITitledWindowSetup` abgeleitet, obwohl ein `ILoginDialog` nicht von `IWindow` abgeleitet ist.

__Hinweis:__ Der Begriff BluePrint wird oft stellvertretend für den Begriff WidgetDescriptor verwendet. Dies liegt unter anderem daran, dass ein BluePrint einen Widget Typ genauso eindeutig spezifiziert wie ein WidgetDescriptor und in der Praxis beide Aspekte oft in einer Schnittstelle vereint sind. Ein BluePrint fügt zu einem separierten (in einer eigenen Schnittstelle befindlichen) WidgetDescriptor noch den Aspekt des Setup Builder hinzu, weshalb der Begriff WidgetDescriptor nicht synonym für BluePrint verwendet werden sollte, wenn der Builder Aspekt in diesem Kontext auch relevant ist.


#### Zusammenfassen des Builder und Setup Aspekts

Die ursprüngliche Idee, den Builder und Setup Aspekt zu trennen, hat sich in der Praxis nicht bewährt. Es wird daher empfohlen, bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) die Builder und Setup Methoden in einer Schnittstelle unterzubringen. Für das Label würde man dann zum Beipiel die `ILabelSetupBuilder` Schnittstelle wie folgt definieren:

~~~
	IImageConstant getIcon();

	BLUE_PRINT_TYPE setIcon(IImageConstant icon);
	
	String getText();
	
	BLUE_PRINT_TYPE setText(String text);

	String getToolTipText();
	
    BLUE_PRINT_TYPE setToolTipText(String text);

	@Mandatory
    Markup getMarkup();
	
    BLUE_PRINT_TYPE setMarkup(Markup markup);

	 AlignmentHorizontal getAlignment();
	
    BLUE_PRINT_TYPE setAlignment(AlignmentHorizontal alignmentHorizontal);
	
	Integer getFontSize();
	
	BLUE_PRINT_TYPE setFontSize(Integer size);

	String getFontName();
	
    BLUE_PRINT_TYPE setFontName(String fontName);

	Boolean isVisible();
	
	BLUE_PRINT_TYPE setVisible(Boolean visible);

	IColorConstant getForegroundColor();
	
    BLUE_PRINT_TYPE setForegroundColor(final IColorConstant foregroundColor);

	IColorConstant getBackgroundColor();
~~~

Dann wäre auch die Trennung von Descriptor und BluePrint überflüssig und die Schnittstelle `ILabelBluePrint` würde also wie folgt aussehen:

~~~
	public interface ILabelBluePrint extends 
		ILabelSetupBuilder<ILabelBluePrint>, 
		IWidgetDescriptor<ILabel> {}
~~~

Die Trennung der BluePrint Schnittstelle und er SetupBuilder Schnittstelle wird jedoch nach wie vor empfohlen, um die Möglichkeit zu haben, von einem Setup Builder ableiten zu können. Von einem BluePrint sollte nicht abgeleitet werden, unter Anderem weil für das abgeleitete BluePrint der Descriptor Typ nicht mehr eindeutig sein könnte.

### Die BluePrint Proxy Factory{#blue_print_proxy_factory}

Die Blue Print Proxy Factory implementiert eine [BluePrint Schnittstelle](#blue_print_interface) mit Hilfe von [Java Proxies](http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Proxy.html). Wenn man bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) die BluePrints auch auf diese Weise erzeugt, lassen sich dadurch die Default Setups einfach [überschreiben](#widget_defaults_override).


Man erhält die Instanz der `IBluePrintProxyFactory` wie folgt von Toolkit:

~~~
	IBluePrintProxyFactory bluePrintProxyFactory = Toolkit.getBluePrintProxyFactory();
~~~

Es folgt eine kurze Beschreibung der Methoden:


#### Erzeugen eines BluePrint Proxy

TODO


#### Hinzufügen oder Setzen eines Default Initializer 

TODO

#### Setzen einer Convenience Methoden Implementierung

TODO


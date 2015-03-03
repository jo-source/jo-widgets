## BluePrints{#blue_prints}

BluePrints werden benötigt, um Widgets zu erzeugen. Siehe auch [HelloWorldApplication - Der common Ui Code](#hello_world_common_code)

Mit Hilfe eines BluePrint wird das (Default) Setup eines Widgets festgelegt. Wenn ein Widget erzeugt wird, werden alle Eigenschaften, welche auf dem Setup definiert sind, für das Widget übernommen. Einige Parameter eines Setups sind Pflichtparameter. Diese sind sind mit der `@Mandatory` Annotation gekennzeichnet. Für Pflichtparameter existieren, wenn dies _sinnvoll_ möglich ist, Defaultwerte, welche beliebig überschrieben werden können. Siehe dazu auch [Widget Defaults](#widget_defaults). 

Ein BluePrint kann Eigenschaften enthalten, welche für das Widget nicht mehr veränderbar sind. Zum Beispiel muss die `Orientation` eines `SplitComposite` initial auf horizontal oder vertikal festgelegt werden, und ist nachträglich nicht mehr änderbar. Durch die klare Trennung der Setup und Widget Schnittstellen lassen sich (auch für eigene Widgets) sogenannte [imutable Member](http://www.javapractices.com/topic/TopicAction.do?Id=29) einfach umsetzen, ohne dabei lange Parameterlisten in Konstruktoren zu benötigen. Dies kann die Implementierung eines Widgets erleichtern, weil nicht alle Eigenschaften modifizierbar implementiert werden müssen. 

Die Frage, ob eine Eigenschaft imutable ist oder nicht ist eine Design (oder auch Philosophie) Frage. Man könnte sich zum Beispiel auf den Standpunkt stellen, dass ein Fenster entweder _closeable_ ist oder nicht. Diesen Zustand während der Anzeige des Fensters zu ändern wäre ungewöhnlich, daher kann man sich den Implementierungsaufwand dafür auch einsparen. Das man den Titel eines Fensters zur Laufzeit ändert, ist jedoch üblich. Beispielsweise zeigt das Explorer Fenster von Windows immer den ausgewählten Ordner im Titel an. Diese Eigenschaft sollte also eher veränderbar entworfen werden. Bei dem Entwurf eigener Widgets kann man auch, wenn es einfacher ist, Eigenschaften erst mal _imutable_ entwerfen, und diese erst bei Bedarf modifizierbar machen, wenn sich dadurch der Implementierungsaufwand erst einmal spürbar reduziert. Das Prinzip, dass Widgets unveränderbare Eigenschaften haben findet sich auch bei SWT wieder. Dort werden diese über den Style, welcher eine Bitmaske darstellt, gesetzt. 

Für die Erstellung eigener Widget Bibliotheken kann der Blueprint Mechanismus verwendet werden. Dabei müssen die BluePrint Schnittstellen nicht selbst implementiert werden da die Implementierung mit Hilfe von [Java Proxies](http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Proxy.html) umgesetzt wird. Das Definieren der BluePrint Schnittstelle reicht also aus. Siehe dazu auch [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries).

### Beispiele für die Verwendung von BluePrints {#blue_prints_examples}

BluePrints erhält man von der BluePrintFactory. Dazu kann entweder die _Abreviation Accessor Klasse_ `org.jowidgets.tools.widgets.blueprint.BPF` verwendet werden, oder man holt sich die Instanz der `IBluePrintFactory` Schnittstelle vom Toolkit mittels `Toolkit.getBluePrintFactory()`. Die Setup Methoden eines BluePrint haben, wie beim Builder Pattern üblich, immer die Instanz als Rückgabewert. Dadurch lassen sich die Methodenaufrufe einfach _verketten_.

Im folgenden Beispiel wird ein BluePrint für ein Frame mit dem Titel "My Frame" erzeugt, welches immer beim Anzeigen zentriert werden soll (bezüglich des Vaterfensters, und falls es ein Root Fenster ist, bezüglich des Bildschirms), welches beim Schließen automatisch `disposed` wird und dessen Größe nicht durch den Nutzer geändert werden kann:

~~~{.java .numberLines startFrom="1"} 
	final IFrameBluePrint frameBp = BPF.frame();
	frameBp
		.setTitle("My Frame")
		.setAutoCenterPolicy(AutoCenterPolicy.ALWAYS)
		.setAutoDispose(true)
		.setResizable(false);
~~~

Mit diesem Frame Blue Print kann man zum Beispiel mit Hilfe des Toolkit ein Root Frame

~~~
	IFrame frame = Toolkit.createRootFrame(frameBp);
~~~

oder auch wie folgt ein Kind Fenster erzeugen

~~~
	IFrame childFrame = frame.createChildWindow(frameBp);
~~~

Im weiteren Beispiel wird ein BluePrint für ein `SliderViewer` erzeugt, welcher `Double` Werte von 0.0 bis 1.0 annimmt, vertikal ausgerichtet ist, keine Scala anzeigt, das Tooltip "Brigthness" hat und an den `ObservabelValue` `brightness` gebunden ist. 

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


## Layouting{#layouting}

Um die [Controls](#control_interface) eines [Containers](#container_interface) anzuordnen benötigt man einen Layouter. Man kann entweder vorgefertigte Layouter verwenden, oder selbst einen [Custom Layouter](#container_interface) implementieren.   

Anfangs unterstütze jowidgets ausschließlich [Mig Layout](#mig_layout) als Layout Mechanismus. Dieses musste von einer SPI Implementierung unterstützt werden ^[Was nicht problematisch war, da MigLayout Implementierungen bereits für Swing und Swt existierten.]. Für die Definition des Layouts wird dabei die Klasse `MigLayoutDescriptor` verwendet, welche das Tagging Interface `ILayoutDescriptor` implementiert.

Später wurde im Rahmen einer [Bacheloarbeit](ba_nm.pdf) jowidgets um die Möglichkeit erweitert, eigene Layouter zu erstellen. Dabei wurde die Schnittstelle `ILayouter` eingeführt, welche ebenfalls von `ILayoutDescriptor` abgeleitet ist. Zudem wurde [Mig Layout](#mig_layout) für diese Schnittstelle portiert, so dass eine SPI Implementierung nicht mehr zwingend eine Mig Layout Implementierung anbieten muss. Außerdem wurden weitere vorgefertigte Layouter hinzugefügt. 

Zur besseren Unterscheidung wird das portierte Mig Layout im folgenden als __Mib Layout__^[In Anlehnung an den Entwickler, der die Portierung durchgeführt hat.] bezeichnet. Im Gegensatz dazu wird Mig Layout als __Natives Mig Layout__ bezeichnet, wenn betont werden soll, dass es sich __nicht__ um Mib Layout handelt.

Um ein Layout auf einem Container zu setzen, bietet dieser die folgenden Methoden an:

~~~
    void setLayout(ILayoutDescriptor layoutDescriptor);

	<LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(
		ILayoutFactory<LAYOUT_TYPE> layoutFactory);
~~~

Mit Hilfe der ersten Methode wird entweder ein `ILayouter` oder der native [`MigLayoutDescriptor`](#mig_layout) gesetzt. 

Die zweite Methode bietet die Möglichkeit, einen Layouter mit Hilfe einer `ILayoutFactory` zu setzen. Der Layouter wird dabei erzeugt und zurückgegeben. Die Layout Factory sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public interface ILayoutFactory<LAYOUTER_TYPE extends ILayouter> {

	LAYOUTER_TYPE create(IContainer container);

}
~~~  

Layout Factories lassen sich für verschiedene Container wiederverwenden. Die vordefinierten Layouts von Jowidgets (mit Ausnahme des nativen Mig Layout) bieten eine Implementierung der `ILayoutFactory` Schnittstelle.

### Mig Layout (nativ){#mig_layout}

Mig Layout ist ein freier, von Mikael Grev, Inhaber der Firma MiG InfoCom AB, entwickelter Layout Manager ([http://www.miglayout.com/](http://www.miglayout.com/)), der unter BSD Lizenz steht. Es existieren Implementierungen für Swing, Swt und Java FX 2. Der Layout Manager hat einen sehr flexiblen Grid basierten Layout Ansatz und eignet sich für sehr viele Anwendungsfälle. Insbesondere ist damit die Erstellung von formularbasierten Masken sehr intuitiv und einfach umzusetzen.   

Um Mig Layout mit jowidgets zu verwenden, wird empfohlen, vorab den [Mig Layout Quick Start Guide](#http://www.miglayout.com/QuickStart.pdf) zu studieren. 

Die Klasse `org.jowidgets.common.widgets.layout.MigLayoutDescriptor` bietet die Möglichkeit, die `LayoutConstraints`, `RowConstraints` und `ColumnConstraints` für das Layout festzulegen. Die Constraints haben die gleiche Bedeutung wie in den Klassen `net.miginfocom.swt.MigLayout` oder `net.miginfocom.swing.MigLayout`.

Im folgenden Beispiel wird ein MigLayout definiert. 

~~~{.java .numberLines startFrom="1"}
	container.setLayout(new MigLayoutDescriptor("wrap", "[][grow, 0::]", "[][]"));
~~~

Das Layout hat zwei Spalten und zwei Zeilen, wobei die zweite Spalte wächst und eine `MinSize` von `0` hat. Die `LayoutConstraints="wrap"` bedeuten, dass automatisch eine neue Zeile begonnen wird, wenn die aktuelle Zeile voll ist (in diesem Fall also nach zwei Controls).

Die Cell Constraints werden im folgenden Beispiel beim Hinzufügen der Controls zum Container gesetzt:

~~~{.java .numberLines startFrom="1"}
	final String textFieldCC = "growx, w 0::";

	//row 0, column 0
	container.add(BPF.textLabel("Field 1"));

	//row 0, column 1
	container.add(BPF.textField(), textFieldCC);

	//row 1 (autowrap has wraped to row 1), column 0 
	container.add(BPF.textLabel("Field 2"));

	//row 1, column 1
	container.add(BPF.textField(), textFieldCC);
~~~

Die Textfelder wachsen horizontal und haben eine `MinSize` von `0`.

Die folgende Abbildung zeigt das Ergebnis:

![Mig Layout Beispiel](images/mig_layout_example.gif "Mig Layout Beispiel")

__Hinweise:__ 

* Da native Swt Controls keine `MinSize` haben, verwendet die Swt Mig Layout Implementierung als `MinSize` die `PreferredSize`. Das kann unter Umständen zu Problemen führen. Es wird daher empfohlen, die `MinSize` immer explizit anzugeben, wenn diese von der `PreferredSize` abweicht.

* Wenn eine SPI Implementierung kein natives Mig Layout unterstützt, wird bei der Verwendung des nativen `MigLayoutDescriptor` automatisch [Mib Layout](#mib_layout) verwendet.
	
* Wenn [Mib Layout](#mib_layout) verwendet wird, wird __kein__ natives Mig Layout verwendet, auch wenn die verwendete SPI Implementierung ein natives Mig Layout bereitstellt.
	
* Die auf einem Control gesetzten Default Größen `MinSize`, `PreferredSize` und `MaxSize` werden vom nativen MigLayout __nicht__ ausgewertet. Es wird daher empfohlen, die Methoden zum Ändern der Default Größen auf Controls nicht in Kombination mit MigLayout zu verwenden, um eine größt mögliche Kompatibilität zu gewährleisten!

### Custom Layouts{#custom_layouts}

Bevor die vordefinierten Layouts von jowidgets vorgestellt werden, soll vorab die `ILayouter` Schnittstelle besprochen werden, um ein besseres Verständnis zu schaffen, was beim Layouten eines Containers passiert. 

Um einen eigenes (custom) Layout zu verwenden, muss diese Schnittstelle implementiert werden:

~~~{.java .numberLines startFrom="1"}
public interface ILayouter extends ILayoutDescriptor {

	void layout();

	Dimension getMinSize();

	Dimension getPreferredSize();

	Dimension getMaxSize();

	void invalidate();

}
~~~

Die Methode `layout()` ist dafür zuständig, auf den Controls eines Containers die Größe und die Position zu setzen.

Die `ClientArea` ist der Bereich des Containers, welcher für das Zeichnen der Controls zur Verfügung steht. Die `DecoratedSize` ist die gesamte Größe des Containers bei einer gegebenen `ClientArea` Größe. Bei einem Fenster kommen zum Beispiel bei der `DecoratedSize` noch der Rahmen oder ein eventuelles Menü hinzu.

Die Methoden `getMinSize()`, `getPreferredSize()` und `getMaxSize()` geben die minimale, bevorzugte und maximale Größe des Containers zum aktuellen Zeitpunkt zurück, die der Layouter benötigt, um seine Controls zu layouten. Die zurückgegebenen Größen beziehen sich dabei auf die `DecoratedSize` und __nicht__ auf die Größe der `ClientArea`. 

Die Methode `invalidate()` wird aufgerufen, wenn sich die Struktur des Layouts geändert haben _könnte_. Dies kann ein guter Zeitpunkt sein, um _gecachte_ Werte zu löschen.

Die Verwendung der Schnittstelle soll Anhand eines Beispiels verdeutlicht werden. Es wird ein vereinfachtes [Fill Layout](#fill_layout) implementiert. Ein Fill Layout zeichnet das erste sichtbare Control eines Containers so, dass es die `ClientArea` voll ausfüllt. Eine Implementierung könnte wie folgt aussehen:

~~~{.java .numberLines startFrom="1"}
final class FillLayout implements ILayouter {

	private final IContainer container;

	private Dimension minSize;
	private Dimension preferredSize;

	FillLayout(final IContainer container) {
		this.container = container;
	}

	@Override
	public void layout() {
		final IControl control = getFirstVisibleControl();
		if (control != null) {
			final Rectangle clientArea = container.getClientArea();
			control.setPosition(clientArea.getPosition());
			control.setSize(clientArea.getSize());
		}
	}

	@Override
	public Dimension getMinSize() {
		if (minSize == null) {
			this.minSize = calcMinSize();
		}
		return minSize;
	}

	@Override
	public Dimension getPreferredSize() {
		if (preferredSize == null) {
			this.preferredSize = calcPreferredSize();
		}
		return preferredSize;
	}

	@Override
	public Dimension getMaxSize() {
		return Dimension.MAX;
	}

	@Override
	public void invalidate() {
		minSize = null;
		preferredSize = null;
	}

	private Dimension calcMinSize() {
		final IControl control = getFirstVisibleControl();
		if (control != null) {
			return container.computeDecoratedSize(control.getMinSize());
		}
		else {
			return container.computeDecoratedSize(new Dimension(0, 0));
		}
	}

	private Dimension calcPreferredSize() {
		final IControl control = getFirstVisibleControl();
		if (control != null) {
			return container.computeDecoratedSize(control.getPreferredSize());
		}
		else {
			return container.computeDecoratedSize(new Dimension(0, 0));
		}
	}
	
	private IControl getFirstVisibleControl() {
		for (final IControl control : container.getChildren()) {
			if (control.isVisible()) {
				return control;
			}
		}
		return null;
	}
}
~~~

Folgendes ist dabei zu beachten:

* Die Werte für die `MinSize` und `PreferredSize` werden, solange `invalidate()` nicht aufgerufen wird, nur ein Mal berechnet. Dadurch kann die Performance gesteigert werden. Gerade bei verschachtelten Containern kann das Berechnen der `PreferredSize` unter Umständen _teuer_ sein. 

* Die Methoden `calcMinSize()` und `calcPreferredSize()` verwenden die Methode `computeDecoratedSize()` des Containers um aus der für die `ClientArea` gültigen Große die `DecoratedSize` zu berechnen (Zeile 52, 55, 62, 65). Wird dies nicht berücksichtigt, funktioniert das Layout nur für die Container korrekt, wo die `ClientArea` Größe mit der `DecoratedSize` übereinstimmt.

Um einen eigenen (custom) Layouter zu implementieren, ist es eventuell hilfreich, den Source Code der vordefinierten Layout Manager zu studieren. Dieser findet sich unter anderem [hier](http://code.google.com/p/jo-widgets/source/browse/#svn%2Ftrunk%2Fmodules%2Fcore%2Forg.jowidgets.impl%2Fsrc%2Fmain%2Fjava%2Forg%2Fjowidgets%2Fimpl%2Flayout).

### Flow Layout

Bei einem Flow Layout werden alle Controls nebeneinander oder untereinander gezeichnet. Die Größe der Controls wird auf deren `PreferredSize` gesetzt, wenn genügend Platz vorhanden ist, ansonsten werden die Controls gleichmäßig verkleinert, jedoch nicht kleiner als ihre `MinSize`.

Die Accessor Klasse `org.jowidgets.api.layout.FlowLayout` liefert einen Zugriff auf ein Flow Layout. Sie hat folgende Methoden:

~~~
	public static ILayoutFactory<ILayouter> get(){...}
	
	public static IFlowLayoutFactoryBuilder builder(){...}
~~~

Ein `IFlowLayoutFactoryBuilder` hat die folgenden Methoden:

~~~
	IFlowLayoutFactoryBuilder gap(int gap);

	IFlowLayoutFactoryBuilder orientation(Orientation orientation);

	IFlowLayoutFactoryBuilder vertical();

	IFlowLayoutFactoryBuilder horizontal();

	ILayoutFactory<ILayouter> build();
~~~

Die Orientation gibt an, ob die Elemente nebeneinander oder untereinander angeordnet werden. Die default `Orientation` ist `HORIZONTAL`. 
Die Methoden `vertical()` und `horizontal()` setzen ebenfalls die `Orientation`, jedoch mit verkürzter Schreibweise. Der `gap` definiert den _Freiraum_ zwischen den Controls. Der default `gap` beträgt 4 Pixel. Die Methode `build()` liefert eine neue `ILayoutFactory` zurück. 

Folgendes Beispiel demonstriert Verwendung:

~~~{.java .numberLines startFrom="1"}
	container.setLayout(FlowLayout.get());
	container.add(BPF.textLabel().setText("Attribute1"));
	final ITextControl textField = container.add(BPF.textField());
	textField.setText("This is the most common attribute");
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Flow Layout Beispiel 1](images/flow_layout_example_1.gif "Flow Layout Beispiel 1")

Im nächsten Beispiel wird das FlowLayout vertikal ausgerichtet:

~~~{.java .numberLines startFrom="1"}
	container.setLayout(FlowLayout.builder().gap(0).vertical().build());
	container.add(BPF.textLabel().setText("Attribute1"));
	final ITextControl textField = container.add(BPF.textField());
	textField.setText("This is the most common attribute");
~~~	

Die folgende Abbildung zeigt das Ergebnis:

![Flow Layout Beispiel 2](images/flow_layout_example_2.gif "Flow Layout Beispiel 2")

### Border Layout{#border_layout}

Ein Border Layout teilt einen Container in fünf mögliche Bereiche auf: Center, Top, Bottom, Left und Right. In jedem Bereich kann sich genau ein Control befinden. Die folgende Abbildung zeigt ein typisches Border Layout:

![Border Layout Beispiel](images/border_layout_example_1.gif "Border Layout Beispiel")

In der Mitte (center) befindet sich eine TextArea, oben, rechts und links je eine Toolbar und unten ein TextFeld.

Die Accessor Klasse `org.jowidgets.api.layout.BorderLayout` liefert einen Zugriff auf ein Border Layout. Sie hat folgende Methoden:

~~~
	public static ILayoutFactory<ILayouter> get(){...}
	
	public static IBorderLayoutFactoryBuilder builder(){...}
~~~

Ein `IBorderLayoutFactoryBuilder` hat die folgenden Methoden:

~~~
	IBorderLayoutFactoryBuilder margin(int margin);

	IBorderLayoutFactoryBuilder gap(int gap);

	IBorderLayoutFactoryBuilder gapX(int gapX);

	IBorderLayoutFactoryBuilder gapY(int gapY);

	IBorderLayoutFactoryBuilder marginLeft(int marginLeft);

	IBorderLayoutFactoryBuilder marginRight(int marginRight);

	IBorderLayoutFactoryBuilder marginTop(int marginTop);

	IBorderLayoutFactoryBuilder marginBottom(int marginBottom);

	ILayoutFactory<ILayouter> build();
~~~

Der `margin` definiert den äußeren Abstand zur `ClientArea` des Containers. Er kann separat für recht, links, oben und unten oder für alle Seiten zusammen gesetzt werden. Der default `margin` ist `0`. Der `gap` definiert den Abstand zwischen den einzelnen Bereichen. Er kann separat für die x-Richtung und y-Richtung oder für beide zusammen gesetzt werden. Der default `gap` ist 4. Die Methode `build()` liefert eine neue `ILayoutFactory` zurück. 

Um festzulegen, in welchem Bereich ein Control hinzugefügt wird, muss auf diesem der LayoutConstraint `BorderLayoutConstraints` gesetzt werden. Das folgende Beispiel verdeutlicht dies: 

~~~{.java .numberLines startFrom="1"}
	IToolBar top = container.add(BPF.toolBar(), BorderLayout.TOP);
	IToolBar left = container.add(BPF.toolBar().setVertical(), BorderLayout.LEFT);
	ITextArea center = container.add(BPF.textArea(), BorderLayout.CENTER);
	IToolBar right = container.add(BPF.toolBar().setVertical(), BorderLayout.RIGHT);
	ITextControl bottom = container.add(BPF.textField(), BorderLayout.BOTTOM);
~~~

### Fill Layout{#fill_layout}

Ein Fill Layout zeichnet ausschließlich das erste (sichtbare) Control eines Containers. Dabei wird unabhängig von der `MinSize`, `PreferredSize` oder `MaxSize` die komplette `ClientArea` abzüglich des `margin` für das Control verwendet. 

Die Accessor Klasse `org.jowidgets.api.layout.BorderLayout` liefert einen Zugriff auf ein Fill Layout. Sie hat folgende Methoden:

~~~
	public static ILayoutFactory<ILayouter> get(){...}
	
	public static IFillLayoutFactoryBuilder builder(){...}
~~~

Ein `IFillLayoutFactoryBuilder` hat die folgenden Methoden:

~~~
	IFillLayoutFactoryBuilder margin(int margin);
	
	IFillLayoutFactoryBuilder marginLeft(int marginLeft);

	IFillLayoutFactoryBuilder marginRight(int marginRight);

	IFillLayoutFactoryBuilder marginTop(int marginTop);

	IFillLayoutFactoryBuilder marginBottom(int marginBottom);

	ILayoutFactory<ILayouter> build();
~~~

Der `margin` definiert den äußeren Abstand zur `ClientArea` des Containers. Er kann separat für recht, links, oben und unten oder für alle Seiten zusammen gesetzt werden. Der default `margin` ist `0`.  Die Methode `build()` liefert eine neue `ILayoutFactory` zurück. 
	
Folgendes Beispiel demonstriert Verwendung:

~~~{.java .numberLines startFrom="1"}
	container.setLayout(FillLayout.builder().margin(5).build());
	final ITextArea textArea = container.add(BPF.textArea());
	textArea.setText("Some text in this text area");
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Fill Layout Beispiel 1](images/fill_layout_example_1.gif "Fill Layout Beispiel 1")

### Cached Fill Layout

Das Cached Fill Layout wurde entworfen, um das Layouten komplexer Controls zu optimieren. Immer wenn sich zum Beispiel die Größe eines Fensters oder eines Bereichs innerhalb eines Split Composites ändert, wird auf einem Layouter die `invalidate()` Methode aufgerufen. Wenn man keine Kenntnis über die darunter liegenden Controls hat, ist dieses Vorgehen auch sinnvoll, denn durch das Ändern der Größe könnte sich auch das Layout ändern. Allerdings ist dieses Vorgehen auch _teuer_ und unter Umständen werden dabei die immer gleichen `PreferredSize` Werte berechnet.

Es gibt Situationen, in denen man weiß, dass das Ändern der Größe keinen Einfluss auf die `PreferredSize` der Kind Controls hat. In diesem Fall kann man ein Cached Fill Layout verwenden. Dies berechnet die `MinSize`, `PreferredSize` und `MaxSize` bei einem `invalidate()` nicht neu. Ansonsten ist es mit dem [Fill Layout](#fill_layout) zu vergleichen, da auch hier die gesamte `ClientArea` ausgenutzt wird.

Die Accessor Klasse `org.jowidgets.api.layout.CachedFillLayout` liefert einen Zugriff auf ein Cached Fill Layout. Sie hat folgende Methode:

~~~
	public static ILayoutFactory<ICachedFillLayout> get(){...}
~~~

Um den Cache explizit zu löschen, kann auf dem `ICachedFillLayout` die folgende Methode verwendet werden:

~~~
	void clearCache();
~~~


### Mib Layout{#mib_layout}

Im Rahmen einer [Bacheloarbeit](ba_nm.pdf) wurde [Mig Layout (http://www.miglayout.com/)](http://www.miglayout.com/) für jowidgets portiert. Die Portierung implementiert die `ILayouter` Schnittstelle. Es wurden die meisten Funktionen portiert. Zudem wurde auch die original MigLayout Demo Applikation auf Basis der Portierung umgesetzt, der Source Code findet sich [hier](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/demo/DemoMigLayoutFrame.java). Die folgende Abbildung zeigt die Demo Applikation:

![Mib Layout Demo Applikation](images/mib_layout_demo_application.gif "Mib Layout Demo Applikation")


#### Mib Layout Verwendung in jowidgets

Um Mib Layout in jowidgets zu verwenden, wird empfohlen, vorab den [Mig Layout Quick Start Guide](#http://www.miglayout.com/QuickStart.pdf) zu studieren.

Die Accessor Klasse `org.jowidgets.api.layout.miglayout.MigLayout` liefert einen Zugriff auf das Mib Layout. Sie hat folgende Methoden:

~~~
	public static ILayoutFactory<IMigLayout> get(){...}

	public static ILayoutFactory<IMigLayout> create(
		final String layoutConstraints) {...}

	public static ILayoutFactory<IMigLayout> create(
		final String columnConstraints,
		final String rowConstraints) {...}

	public static ILayoutFactory<IMigLayout> create(
		final String layoutConstraints,
		final String columnConstraints,
		final String rowConstraints) {...}

	public static ILayoutFactory<IMigLayout> create(
		final MigLayoutDescriptor descriptor) {...}

	public static IMigLayoutFactoryBuilder builder() {...}
~~~

Ein `IMigLayoutFactoryBuilder` hat die folgenden Methoden:

~~~
	IMigLayoutFactoryBuilder descriptor(MigLayoutDescriptor descriptor);

	IMigLayoutFactoryBuilder columnConstraints(String constraints);

	IMigLayoutFactoryBuilder constraints(String constraints);

	IMigLayoutFactoryBuilder rowConstraints(IAC constraints);

	IMigLayoutFactoryBuilder columnConstraints(IAC constraints);

	IMigLayoutFactoryBuilder constraints(ILC constraints);

	ILayoutFactory<IMigLayout> build();
~~~

Constraints können wie im original Mig Layout mit Hilfe von Strings oder durch Builder (siehe [Mib Layout Constraints Builder](#mib_layout_constraints_builder)) erzeugt werden. Die Methode `build()` liefert eine neue `ILayoutFactory<IMigLayout>` zurück. Die Schnittstelle `IMiglayout` hat die folgenden Methoden:

~~~
	void setLayoutConstraints(Object constraints);

	Object getLayoutConstraints();

	void setColumnConstraints(Object constraints);

	Object getColumnConstraints();

	void setRowConstraints(Object constraints);

	Object getRowConstraints();

	void setConstraintMap(Map<IControl, Object> map);

	Map<IControl, Object> getConstraintMap();

	boolean isManagingComponent(IControl control);
~~~

Die Methoden sind identisch zur Klasse `net.miginfocom.swt.MigLayout`.

#### Mib Layout Constraints Builder{#mib_layout_constraints_builder}

Die Accessor Klasse org.jowidgets.api.layout.miglayout.LC kann für die Builder basierte Erzeugung von `LayoutConstraints` verwendet werden. Sie hat die folgende Methode:

~~~
	public static ILC create() {...}
~~~

Die Schnittstelle `org.jowidgets.api.layout.miglayout.ILC` hat die gleichen Methoden wie die original Mig Layout Klasse `net.miginfocom.layout.LC`.

Die Accessor Klasse org.jowidgets.api.layout.miglayout.AC kann für die Builder basierte Erzeugung von `AxisConstraints` (also `ColumnConstraints` und `RowConstraints`) verwendet werden. Sie hat die folgende Methode:

~~~
	public static IAC create() {...}
~~~

Die Schnittstelle `org.jowidgets.api.layout.miglayout.IAC` hat die gleichen Methoden wie die original Mig Layout Klasse `net.miginfocom.layout.AC`.

Die Accessor Klasse org.jowidgets.api.layout.miglayout.CC kann für die Builder basierte Erzeugung von `ComponentConstraints` verwendet werden. Sie hat die folgende Methode:

~~~
	public static ICC create() {...}
~~~

Die Schnittstelle `org.jowidgets.api.layout.miglayout.ICC` hat die gleichen Methoden wie die original Mig Layout Klasse `net.miginfocom.layout.CC`.

#### Mib Layout Platform Defaults

Die Accessor Klasse `org.jowidgets.api.layout.miglayout.PlatformDefaults` bietet den Zugriff auf die Portierung der Klasse    `net.miginfocom.layout.PlatformDefaults`.

#### Mib Layout Beispiel

Im folgenden wurde das Beispiel aus dem Abschnitt [Mig Layout (nativ)](#mig_layout) mit Mib Layout umgesetzt:

~~~{.java .numberLines startFrom="1"}
	container.setLayout(MigLayout.create("wrap", "[][grow, 0::]", "[][]"));

	final String textFieldCC = "growx, w 0::";

	//row 0, column 0
	container.add(BPF.textLabel("Field 1"));

	//row 0, column 1
	container.add(BPF.textField(), textFieldCC);

	//row 1 (autowrap has wraped to row 1), column 0 
	container.add(BPF.textLabel("Field 2"));

	//row 1, column 1
	container.add(BPF.textField(), textFieldCC);
~~~

Der einzige Unterschied findet sich in Zeile 1 bei der Definition des Layouts, der Rest ist identisch. Die folgende Abbildung zeigt das Ergebnis:

![Mib Layout Beispiel](images/mib_layout_example.gif "Mib Layout Beispiel")

Im folgenden Beispiel wird das gleiche wie oben mit Hilfe der Constraints Builder umgesetzt: 

~~~{.java .numberLines startFrom="1"}
	container.setLayout(
		MigLayout.builder()
			.constraints(LC.create().wrap())
			.columnConstraints(AC.create().index(1).grow().size("0::")).build());

	final ICC textFieldCC = CC.create().growX().width("0::");

	//row 0, column 0
	container.add(BPF.textLabel("Field 1"));

	//row 0, column 1
	container.add(BPF.textField(), textFieldCC);

	//row 1 (autowrap has wraped to row 1), column 0 
	container.add(BPF.textLabel("Field 2"));

	//row 1, column 1
	container.add(BPF.textField(), textFieldCC);
~~~

### Null Layout{#null_layout}

Der Null Layout Layouter implementiert eine _leere_ `layout()` Methode. Die Methoden `getMinSize()`, `getPreferredSize()` und `getMaxSize()` liefern die aktuelle Größe des Containers. Bei einem NullLayout müssen die Position und die Größe der Controls daher manuell gesetzt werden.

Die Accessor Klasse `org.jowidgets.api.layout.NullLayout` liefert einen Zugriff auf ein Null Layout. Sie hat die folgende Methode:

~~~
	public static ILayoutFactory<ILayouter> get(){...}
~~~

Das folgende Beispiel zeigt die Verwendung eines Null Layout:

~~~{.java .numberLines startFrom="1"}
	container.setLayout(NullLayout.get());

	final int x = 10;
	final int y = 10;

	for (int i = 0; i < 5; i++) {
		final IButton button = container.add(BPF.button());
		button.setPosition(x + i * 20, y + i * 40);
		button.setText("Button A - " + i);
		button.setSize(button.getPreferredSize());
	}

	for (int i = 0; i < 5; i++) {
		final IButton button = container.add(BPF.button());
		button.setPosition(x + 160 + i * 20, y + (4 - i) * 40);
		button.setText("Button B - " + i);
		button.setSize(200, 30);
	}
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Null Layout Beispiel](images/null_layout_example.gif "Null Layout Beispiel")

### Preferred Size Layout

Bei einem Preferred Size Layout wird in der `layout()` Methode nur die `PreferredSize` der Controls gesetzt. Die Position muss wie beim [Null Layout](#null_layout) manuell gesetzt werden.

Die Accessor Klasse `org.jowidgets.api.layout.PreferredSizeLayout` liefert einen Zugriff auf ein Preferred Size Layout. Sie hat die folgende Methode:

~~~
	public static ILayoutFactory<ILayouter> get(){...}
~~~

Das folgende Beispiel zeigt die Verwendung eines Preferred Size Layout:

~~~{.java .numberLines startFrom="1"}
	container.setLayout(PreferredSizeLayout.get());

	final int x = 10;
	final int y = 10;

	for (int i = 0; i < 5; i++) {
		final IButton button = container.add(BPF.button());
		button.setPosition(x + i * 20, y + i * 40);
		button.setText("Button A - " + i);
	}

	for (int i = 0; i < 5; i++) {
		final IButton button = container.add(BPF.button());
		button.setPosition(x + 160 + i * 20, y + (4 - i) * 40);
		button.setText("Button B - " + i);
	}
~~~

Die folgende Abbildung zeigt das Ergebnis:

![Preferred Size Layout Beispiel](images/pref_size_layout_example.gif "Preferred Size Layout Beispiel")



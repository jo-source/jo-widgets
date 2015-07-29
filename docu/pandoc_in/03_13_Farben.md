## Farben{#colors}

Farben werden in jowidgets mit Hilfe einer `IColorConstant` gesetzt. Die Schnittstelle hat die folgende Methode:

~~~
	ColorValue getDefaultValue();
~~~

Ein `ColorValue` Objekt hat den folgenden Konstruktor:

~~~
	public ColorValue(final int red, final int green, final int blue) {...}
~~~

Dabei kann der rot, grün und blau Anteil festgelegt werden. Die Klasse `ColorValue` implementiert die Schnittstelle `IColorConstant` und kann somit zum Setzen von Farben verwendet werden. Das folgende Beispiel soll das demonstrieren:

~~~{.java .numberLines startFrom="1"}
	frame.setBackgroundColor(new ColorValue(7, 106, 3));
~~~

Die Idee hinter der Schnittstelle `IColorConstant` ist, dass man neben konkreten Farben auch _logische_ Farbkonstanten definieren kann. Zudem sollte mit Hilfe einer `IColorRegistry` der `defaultValue` überschrieben werden können. Derzeit ist eine Color Registry jedoch nicht implementiert^[Wobei es sich um eine Erweiterung handeln würde, die sich relativ einfach umsetzen ließe und nicht invasiv wäre.].

Die enum `org.jowidgets.api.color.Colors` definiert einige Farbkonstanten:

~~~{.java .numberLines startFrom="1"}
public enum Colors implements IColorConstant {

	//logical colors

	/**
	 * Default foreground color
	 */
	DEFAULT(new ColorValue(0, 0, 0)),

	/**
	 * Error color
	 */
	ERROR(new ColorValue(220, 0, 0)),

	/**
	 * Warning color
	 */
	WARNING(new ColorValue(209, 124, 34)),

	/**
	 * Color for a strong label markup
	 */
	STRONG(new ColorValue(0, 70, 213)),

	/**
	 * Color for disabled items
	 */
	DISABLED(new ColorValue(130, 130, 130)),

	/**
	 * Background color for even table rows when using striped rendering
	 */
	DEFAULT_TABLE_EVEN_BACKGROUND_COLOR(new ColorValue(222, 235, 235)),

	/**
	 * Color for selected backgrounds
	 */
	SELECTED_BACKGROUND(new ColorValue(16, 63, 149)),

	//named colors

	BLACK(new ColorValue(0, 0, 0)),
	
	WHITE(new ColorValue(255, 255, 255)),
	
	DARK_GREY(new ColorValue(80, 80, 80)),
	
	GREY(new ColorValue(140, 140, 140)),
	
	LIGHT_GREY(new ColorValue(225, 225, 225)),
	
	GREEN(new ColorValue(7, 106, 3));

	private ColorValue colorValue;

	private Colors(final ColorValue colorValue) {
		this.colorValue = colorValue;
	}

	@Override
	public ColorValue getDefaultValue() {
		return colorValue;
	}

}
~~~

Diese werden hauptsächlich von jowidgets Widgets und von [jo-client-platform](http://jo-source.github.io/jo-client-platform/) Widgets verwendet. Nach dem gleichen Schema könnte man sich eigene Color Enums erstellen, zum Beispiel für die Verwendung in einer applikations- oder firmeninternen Bibliothek. 

Mit Hilfe der `Colors` Klasse könnte man die Hintergrundfarbe der Frames auch wie folgt setzen:

~~~
	frame.setBackgroundColor(Colors.GREEN);
~~~

Mit Hilfe des [ColorTableSnipped](https://github.com/jo-source/jo-widgets/tree/master/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/ColorTableSnipped.java) werden alle Colors in einer Tabelle angezeigt:

![ColorTableSnipped](images/colors_table.gif "ColorTableSnipped")

### Farben unter SWT

Unter SWT werden für Farben Systemresourcen verwendet. Diese werden mit Hilfe des SWT Display erzeugt, und sollten disposed werden, sobald man sie nicht mehr benötigt. 

Für ein `ColorValue` wird erst dann eine Systemresource erzeugt, wenn sie tatsächlich benutzt wird und wenn die gleiche Farbe nicht bereits existiert. Dazu wird ein Color Cache verwendet, welcher für `ColorValue` Keys SWT Farben liefert. Um für ein `ColorValue` Objekt eine _gecachte_ SWT Farbe aus dem Cache zu entfernen und anschließend zu disposen, kann man auf dem `ColorValue` Objekt die Methode

~~~
	void release();
~~~

aufgerufen werden. Dies ist aber nur dann wirklich sinnvoll, wenn viele unterschiedliche Farben angelegt werden, welche man später nicht mehr benötigt, wie zum Beispiel für einen Color Chooser.

Die Farben der weiter vorne beschriebenen Klasse `Colors` nach deren Verwendung wieder zu _releasen_ ist zwar möglich, bringt aber nicht wirklich eine nennenswerte Speichereinsparung (alle 13 Farben benötigen zusammen grob überschlagen 130 Byte).

Das folgende Beispiel zeigt eine Animation bei der 32768 unterschiedliche Farben verwendet werden. Diese würden ca. 320 kByte benötigen, weshalb sie direkt nach deren Verwendung wieder aus dem Cache released werden (Zeile 19):

~~~{.java .numberLines startFrom="1"}
	final IAnimationRunner animationRunner = AnimationRunner.create();
	animationRunner.setDelay(20, TimeUnit.MILLISECONDS);

	final Runnable animationStep = new Runnable() {

		private int red = 0;
		private int redIncrement = 1;

		private int green = 0;
		private int greenIncrement = 2;

		private int blue = 0;
		private int blueIncrement = 3;

		@Override
		public void run() {
			final ColorValue color = new ColorValue(red, green, blue);
			frame.setBackgroundColor(color);
			color.release();

			red = red + redIncrement;
			if (red >= 255) {
				red = 255;
				redIncrement = redIncrement * -1;
			}
			else if (red <= 0) {
				red = 0;
				redIncrement = redIncrement * -1;
			}

			green = green + greenIncrement;
			if (green >= 255) {
				green = 255;
				greenIncrement = greenIncrement * -1;
			}
			else if (green <= 0) {
				green = 0;
				greenIncrement = greenIncrement * -1;
			}

			blue = blue + blueIncrement;
			if (blue >= 255) {
				blue = 255;
				blueIncrement = blueIncrement * -1;
			}
			else if (blue <= 0) {
				blue = 0;
				blueIncrement = blueIncrement * -1;
			}

			animationRunner.run(this);
		}
	};

	animationRunner.start();
	animationRunner.run(animationStep);
~~~
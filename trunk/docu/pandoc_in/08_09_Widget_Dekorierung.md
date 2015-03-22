## Austauschen und Dekorieren von Widgets - Übersicht {#substitude_and_decorate_widgets}

Neben dem globalen [Anpassen der Widget Defaults](#widget_defaults_override) ist es auch möglich, Widget Implementierungen komplett auszutauschen oder zu dekorieren. Das globale Ändern eines Widgets kann durch viele Aspekte motiviert sein: 

__Beispiel 1:__

Man möchte in einer Applikation Widgets, für welche man keine Leserecht besitzt, durch _Fake_ Widgets ersetzen, welche die gleiche Schnittstelle implementieren, jedoch anstatt dem original Widget ein Label anzeigen, das Nutzter darauf hinweist, dass ein Recht fehlt. Zudem sollen keine Daten von dem zugehörigen Dienst eingelesen werden, weil dies sowieso zu einer Security Exception führen würde. Die [jo-client-platform](http://code.google.com/p/jo-client-platform/) nutzt diese Möglichkeit zur Dekoration der BeanTable, des BeanRelationTree und weiteren Widgets. Der zugehörige `IToolkitInterceptor` findet sich [hier](http://code.google.com/p/jo-client-platform/source/browse/trunk/modules/addons/org.jowidgets.cap.security.ui/src/main/java/org/jowidgets/cap/security/ui/tools/CapSecurityUiToolkitInterceptor.java)

__Beispiel 2:__

Angenommen man möchte ein Testtool entwickeln, welches die Möglichkeit bieten soll, bestimmte Methodenaufrufe auf bestimmten Widgets eines bestimmten Typs zu protokollieren, während man eine Applikation bedient, um daraus Eingabedaten für einen automatisierten JUnitTest zu erstellen. Dann könnte man diese Widgets einfach durch einen Wrapper ersetzen, welcher die gewünschte Protokollierung durchführt, nachdem er die Methode auf dem Original Widget aufgerufen hat. Im Rahmen einer [Bachelorarbeit](ba_lg.pdf) wurde ein solches Testtool entwickelt.

__Beispiel 3:__

Ein weiterer Anwendungsfall könnte sein, dass man für das Debugging beim Layouten von verschachtelten Composites einen farbigen Border um alle Composites zeichnen möchte, um zu sehen, welcher Container in welche Schachtelungsebene welche Größe hat.

Im nächsten Abschnitt folgt ein weiteres, zusammenhängendes Beispiel.

### Austauschen und Dekorieren von Widgets mit Hilfe der Generic Widget Factory

Die [Generic Widget Factory](#generic_widget_factory) bietet die folgenden Möglichkeiten zum Austauschen und Dekorieren von Widgets:

* Eine registrierte [Widget Implementierung durch eine andere ersetzen](#substitude_widgets)
* Eine registrierte [Widget Implementierung dekorieren](#dekorate_widgets)
* Eine registrierte [Widget Factory dekorieren](#decorate_widget_factory)

#### Beispiel

Diese Möglichkeiten sollen Anhand eines durchgängigen Beispiels erläutert werden:

Für ein existierendes Produkt wird bei einem Neukunden Akquise gemacht. Das Produkt bietet die Möglichkeit, Entitäten und deren Beziehungen individuell zu definieren, um diese in einer Datenbank zu verwalten und Workflows auf den Daten durchzuführen. Nachdem der Kunde eine Demoversion des Produktes getestet, und dabei einige für ihn spezifische Masken für Maschinenteile und Baugruppen modelliert hat, kommt der folgende Wunsch auf: 

In seinem System werden sehr viele komplexe technische Parameter Bezeichnungen und Identifikationsnummern als Attributname für Teile und Baugruppen verwendet. Der Kunde würde diese Informationen gelegentlich gerne in einem Legacy System für Suchanfragen verwenden. Dies gilt sowohl für die Attributnamen als auch für den Inhalt. Da die Attributnamen derzeit mit Hilfe von Text Label Widgets angezeigt werden, kann man Sie nicht in die Zwischenablage kopieren, sondern muss diese abschreiben. Die folgende Abbildung zeigt eine für den Kunden typische Datenmaske für ein Maschinenteil^[Diese ist frei erfunden. Jede Ähnlichkeit mit realen Produkten ist rein zufällig.]: 

![Beispiel Eingabemaske](images/widget_decorate_example_1.gif  "Beispiel Eingabemaske")

Die Anpassung sollte so kostengünstig wie möglich sein (Kundenwunsch), und möglichst wenig Auswirkungen auf das zugrundeliegende Produkt und somit Systeme bei andere Kunden haben (Wunsch des Auftragnehmer). Dem Kunden werden die folgende Lösungsmöglichkeiten angeboten:

* Alle Text Label werden durch ein ITextField ersetzt, welche wie ein Text Label aussehen, weil sie keinen Border haben und die Hintergrundfarbe vom Parent Container erben. Diese sind readonly, können also nicht geändert werden, es ist aber möglich, den Text zu markieren und so in die Zwischenablage zu kopieren. Dies könnte umgesetzt werden, indem die registrierte Widget Implementierung für `ITextLabelBluePrint` durch eine andere Implementierung [ersetzt](#substitude_widgets) wird.

* Alle Text Label erhalten ein Kontextmenü, welches eine _Copy to clipboard_ Aktion enthält. Dies könnte umgesetzt werden, indem für `ITextLabelBluePrint` die registrierte Implementierung des [Widget dekoriert](#dekorate_widgets) wird.

Der Kunde stellt beide Lösungen einer Gruppe von Benutzern vor, wobei der eine Teil die Erste, der andere Teil die zweite Lösung bevorzugt. Der Kunde fragt, ob man auf einfache und kostengünstige Weise beide Lösungen anbieten können.

Es wird angeboten, das man in den Einstellungen einen Parameter hinzufügt, der das Verhalten festlegt. Um die Lösung möglichst kostengünstig zu halten, wird vom Kunden akzeptiert, das sich die Änderung nur auf neu erstellte Masken und nicht auf bereits erzeugte auswirken, und somit u.U. ein Neustart der Client Applikation notwendig ist. Da laut Nutzer dieser Parameter nach einmaliger Konfiguration in der Regel nicht mehr geändert wird, stellt dies für den Kunden kein Problem dar. Diese Lösung könnte umgesetzt werden, indem für `ITextLabelBluePrint` die [Widget Factory  dekoriert](#decorate_widget_factory) wird.


#### Widget Implementierungen austauschen{#substitude_widgets}

Die folgende Klasse implementiert die Schnittstelle `ITextLabel` mit Hilfe eines Textfeldes:

~~~{.java .numberLines startFrom="1"}
public final class TextFieldLabel extends ControlWrapper implements ITextLabel {

	private final ITextControl textField;

	public TextFieldLabel(final ITextControl textField) {
		super(textField);
		this.textField = textField;
	}

	@Override
	public void setFontSize(final int size) {
		textField.setFontSize(size);
	}

	@Override
	public void setFontName(final String fontName) {
		textField.setFontName(fontName);
	}

	@Override
	public void setMarkup(final Markup markup) {
		textField.setMarkup(markup);
	}

	@Override
	public void setText(final String text) {
		textField.setText(text);
	}

	@Override
	public String getText() {
		return textField.getText();
	}

}
~~~

Die meisten Methoden werden vom [Control Wrapper] geerbt, die anderen werden an das `textField` delegiert.

Die folgende Factory erzeugt Instanzen dieser TextFieldLabel's:

~~~{.java .numberLines startFrom="1"}
public final class TextFieldLabelFactory 
	implements IWidgetFactory<ITextLabel, ITextLabelBluePrint> {

	@Override
	public ITextLabel create(
		final Object parentUiReference, 
		final ITextLabelBluePrint textLabelBp) {
		
		final ITextFieldBluePrint textFieldBp = BPF.textField();
		
		textFieldBp.setSetup(textLabelBp);
		textFieldBp.setBorder(false).setEditable(false).setInheritBackground(true);

		final ITextControl textField = Toolkit.getWidgetFactory().create(
			parentUiReference, 
			textFieldBp);

		return new TextFieldLabel(textField);
	}

}
~~~

In Zeile 9 wird ein neues BluePrint für ein Text Field erzeugt. In Zeile 11 wird das Setup des Textlabels auf dem BluePrint des Texfeldes gesetzt. Dabei werden alle Properties, welche das Textfeld und das Textlabel gemeinsam haben (wie zum Beispiel die Vordergrundfarbe, die Schriftart, etc,) auch für das Textfeld übernommen. In Zeile 14 wird dann ein Texfeld mit Hilfe des `textFieldBp` erzeugt. Dieses wird der Klasse `TextFieldLabel` übergeben, welche die Schnittstelle `ITextLabel` implementiert (sie weiter oben).

Der folgende Code tauscht in der [Generic Widget Factory](#generic_widget_factory) die aktuelle Implementierung für Text Labels durch die `TextFieldLabelFactory` aus. Dies passiert mit Hilfe eines [Toolkit Interceptors](#toolkit_interceptor):

~~~{.java .numberLines startFrom="1"}
@Override
public void onToolkitCreate(final IToolkit toolkit) {
	toolkit.getWidgetFactory().unRegister(ITextLabelDescriptor.class);
	
	toolkit.getWidgetFactory().register(
		ITextLabelDescriptor.class, 
		new TextFieldLabelFactory());
}
~~~

Die folgende Abbildung zeigt den Effekt:

![TextFieldLabel](images/widget_decorate_example_2.gif  "TextFieldLabel")

Durch dass globale Austauschen der Label Implementierung wird nun für alle Labels ein Textfeld für die Darstellung verwendet. Insbesondere lassen sich die Attribute nun markieren und zum Beispiel per STRG-C in die Zwischenablage kopieren.

#### Widget Implementierungen dekorieren{#dekorate_widgets}

Bei der zweiten Variante soll allen Labels ein Kontextmenü hinzufügen, welche eine Copy Action enthält, die den Text des Labels in die Zwischenablage kopiert. Um dies umzusetzen wird zuerst ein `IDecorator<ITextLabel>` wie folgt implementiert:

~~~{.java .numberLines startFrom="1"}
public final class LabelPopupDecorator implements IDecorator<ITextLabel> {

	@Override
	public ITextLabel decorate(final ITextLabel original) {
		final IMenuModel copyMenu = new MenuModel();

		final IActionItemModelBuilder copyActionBuilder = ActionItemModel.builder();
		copyActionBuilder
			.setText("Copy to Clipboard")
			.setToolTipText("Copies the label text to the system clipboard")
			.setIcon(IconsSmall.COPY);
		
		final IActionItemModel copyAction = copyMenu.addItem(copyActionBuilder);
		copyAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				Clipboard.setContents(new StringTransfer(original.getText()));
			}
		});
		
		original.setPopupMenu(copyMenu);
		return original;
	}
}
~~~  

Der Dekorierer wird dann mit Hilfe eines [Toolkit Interceptors](#toolkit_interceptor) wie folgt hinzugefügt:

~~~{.java .numberLines startFrom="1"}
@Override
public void onToolkitCreate(final IToolkit toolkit) {
	toolkit.getWidgetFactory().addWidgetDecorator(
		ITextLabelDescriptor.class, 
		new LabelPopupDecorator());
}
~~~

Die folgende Abbildung zeigt den Effekt:

![LabelPopupDecorator](images/widget_decorate_example_3.gif  "LabelPopupDecorator")

Alle Labels haben jetzt ein Kontextmenü zum Kopieren des Labelinhalts.


#### Widget Factories dekorieren{#decorate_widget_factory}

Nun soll noch gezeigt werden, wie man mit Hilfe eines Widget Factory Decorator je nach Konfiguration die eine oder andere Variante wählen kann.

~~~{.java .numberLines startFrom="1"}
public final class LabelFactoryDecorator 
	implements IDecorator<IWidgetFactory<ITextLabel, ITextLabelBluePrint>> {

	private final LabelPopupDecorator labelPopupDecorator;
	private final TextFieldLabelFactory textFieldLabelFactory;

	//will be injected
	private ILabelConfig labelConfig;

	public LabelFactoryDecorator() {
		this.textFieldLabelFactory = new TextFieldLabelFactory();
		this.labelPopupDecorator = new LabelPopupDecorator();
	}

	@Override
	public IWidgetFactory<ITextLabel, ITextLabelBluePrint> decorate(
		final IWidgetFactory<ITextLabel, ITextLabelBluePrint> originalFactory) {

		//no config or default type so use original
		if (labelConfig == null || LabelType.DEFAULT == labelConfig.getLabelType()) {
			return originalFactory;
		}
		//use TextFieldLabel
		else if (LabelType.TEXT_FIELD == labelConfig.getLabelType()) {
			return textFieldLabelFactory;
		}
		//use copy action
		else if (LabelType.COPY_ACTION == labelConfig.getLabelType()) {
			return new IWidgetFactory<ITextLabel, ITextLabelBluePrint>() {
				@Override
				public ITextLabel create(
					final Object parentUiReference,
					final ITextLabelBluePrint descriptor) {
					
					//create the original widget with the original factory
					final ITextLabel originalWidget = originalFactory.create(
						parentUiReference, 
						descriptor);
					
					//decorate the popup menus
					return labelPopupDecorator.decorate(originalWidget);
				}
			};
		}
		else {
			throw new IllegalStateException(
				"Lable type '" + labelConfig.getLabelType() + "' is not supported.");
		}
	}

}
~~~

Dieser Dekorierer kann wie folgt registriert werden:

~~~{.java .numberLines startFrom="1"}
@Override
public void onToolkitCreate(final IToolkit toolkit) {
	toolkit.getWidgetFactory().addWidgetFactoryDecorator(
		ITextLabelDescriptor.class, 
		new LabelFactoryDecorator());
}	
~~~

Das Überschreiben und Dekorieren aus den beiden vorigen Abschnitten wird dadurch obsolet.




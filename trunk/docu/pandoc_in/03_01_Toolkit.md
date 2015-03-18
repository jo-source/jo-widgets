## Das Jowidgets Toolkit {#jowidgets_toolkit}

Die Klasse `org.jowidgets.api.toolkit.Toolkit` liefert eine Instanz der Schnittstelle `org.jowidgets.api.toolkit.IToolkit`.

~~~
    IToolkit toolkit = Toolkit.getInstance();
~~~

Sie stellt den __Zugriffspunkt auf die Jowidgets API__ dar. Die Klasse Toolkit bietet zusätzlich zu der Methode `getInstance()` statische _Abreviation Accessor Methoden_ für alle Methoden der Schnittstelle `org.jowidgets.api.toolkit.IToolkit`. Dadurch kann man zum Beispiel anstatt:

~~~
    IUiThreadAccess uiThreadAccess = Toolkit.getInstance().getUiThreadAccess();
~~~

einfach
 
~~~
    IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
~~~

schreiben.

Es sei an dieser Stelle darauf hingewiesen, das das Toolkit den Kapselungsmechanismus auf die API darstellt. Eine Aufruf einer Funktion über das Toolkit ist u.U. nicht immer intuitiv. Daher existieren zum Teil auch weitere _Abreviation Accessor Klassen_, um Zugriffe auf das Toolkit abzukürzen. So kann man zum Beispiel anstatt:

~~~
   ITreeExpansionAction action = Toolkit.getDefaultActionFactory().expandTreeAction(tree);
~~~

auch 

~~~
   ITreeExpansionAction action = ExpandTreeAction.create(tree);
~~~

schreiben.

Per Konvention hat die _Abreviation Accessor Klasse_ dann meist den Namen der Schnittstelle ohne vorgestelltes `I`. Handelt es sich um eine Factory Methode heißt diese `create()`. Existiert ein Builder, erhält man diesen auf der selben Accessor Klasse mit Hilfe der Methode `builder()`. Bezogen auf die `TreeExpandionAction` würde dass dann so aussehen:

~~~
  ITreeExpansionActionBuilder builder = ExpandTreeAction.builder(tree);
~~~

Hinweise auf weitere _Abreviation Accesor Klassen_ werden jeweils in den entsprechenden Abschnitten gegeben.

Im folgenden Text ist, soweit nicht gesondert anders vermerkt, mit __Toolkit__ immer das jowidgets Toolkit __`org.jowidgets.api.toolkit.Toolkit`__ gemeint.

### Die Toolkit Initialisierung

Das Toolkit wird beim ersten Zugriff über den [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Mechanismus vom Modul `org.jowidgets.impl` erzeugt (falls nicht vorab manuell ein anderer `IToolkitProvider` initialisiert wurde). 

Für die SWT, Swing und JavaFx Implementierung existiert zur Laufzeit genau eine Toolkit Instanz, für die RWT Implementierung existiert genau eine Toolkit Instanz pro User Session. Im Fat / Rich Client Kontext kann das Toolkit also als Singleton betrachtet werden, im Web Kontext als Session Singleton. 

Mit Hilfe des [Toolkit Interceptors](#toolkit_interceptor) kann man sich benachrichtigen lassen, wenn das / ein Toolkit erzeugt wird, zum Beispiel um Widget Defaults zu überschreiben, eigene Widgets (Factories) zu registrieren, Icons zu überschreiben, Converter anzupassen oder zu registrieren usw..

### Übersicht der Toolkit Methoden

Es folgt eine kurze Übersicht über alle Methoden der Schnittstelle `org.jowidgets.api.toolkit.IToolkit` mit kurzen Erläuterungen oder Verweisen auf die zugehörigen Abschnitte. 

#### Application Runner

~~~
	IApplicationRunner getApplicationRunner();	
~~~

Der `IApplicationRunner` wird zum Starten einer jowidgets standalone Applikation benötigt. Sie dazu auch [Der Application Runner](#application_runner).

#### UI Thread Access

~~~
	IUiThreadAccess getUiThreadAccess();	
~~~

Liefert den Zugriff auf den UI Thread. Der Zugriff muss im UI Thread erfolgen. Sie auch [Der Ui Thread Access](#ui_thread_access)


#### BluePrintFactory

~~~
	IBluePrintFactory getBluePrintFactory();
~~~

Liefert den Zugriff auf die BluePrintFactory. Sie auch [BluePrints](#blue_prints)

#### ConverterProvider

~~~
	IConverterProvider getConverterProvider();
~~~

Liefert den jowidgets Converter Provider. Siehe auch [Jowidgets Converter](#jowidget_converter)

#### SliderConverterFactory

~~~
	ISliderConverterFactory getSliderConverterFactory();
~~~
	
Liefert eine Factory für Slider Converter. Sie auch [Das Slider Viewer Widget](#slider_viewer)	

#### RootFrame Erzeugung

~~~
	IFrame createRootFrame(IFrameDescriptor descriptor);
~~~

Erzeugt ein root Frame für ein Frame Descriptor (BluePrint). 

~~~
	IFrame createRootFrame(IFrameDescriptor descriptor, IApplicationLifecycle lifecycle);
~~~
	
Erzeugt ein root Frame für ein Frame Descriptor (BluePrint). Zusätzlich wird ein Window Listener auf dem erzeugten Frame hinzugefügt, welches auf dem lifecycle die Methode `finish()` aufruft, sobald das Frame geschlossen wird.
	
#### WidgetFactory
	
~~~
	IGenericWidgetFactory getWidgetFactory();
~~~

Liefert die Widget Factory von jowidgets. Siehe auch [Die GenericWidgetFactory](#generic_widget_factory)

#### WidgetWrapperFactory

~~~
	IWidgetWrapperFactory getWidgetWrapperFactory();
~~~
	
Die WidgetWrapperFactory kann verwendet werden, um aus nativen Widgets (z.B. JFrame, Shell, JPanel, Composite) Wrapper zu erstellen, welche die zugehörigen jowidgets Schnittstellen implementieren (z.B. IFrame und IComposite). Siehe auch [Jowidgets Code in native Projekte integrieren](#integrate_jowidgets_in_native_code).
	
#### Images
	
~~~
	IImageFactory getImageFactory();
	
	IImageRegistry getImageRegistry();
~~~	

Liefert die Image Factory und Image Registry. Siehe auch [Icons und Images](#icons_and_images).

#### Dialog Panes

~~~
	IMessagePane getMessagePane();
~~~	

Das Message Pane liefert einen vereinfachten Zugriff auf den [MessageDialog](#message_dialog).

~~~
	IQuestionPane getQuestionPane();
~~~	

Das Question Pane liefert einen vereinfachten Zugriff auf den [QuestionDialog](#question_dialog).

~~~
	ILoginPane getLoginPane();
~~~	

Das Login Pane liefert einen vereinfachten Zugriff auf den [LoginDialog](#login_dialog).

#### Layouting{#toolkit_layouting}

~~~
	ILayoutFactoryProvider getLayoutFactoryProvider();
~~~	

Der Layout Factory Provider bietet einen Zugriff auf verschiedene Layouts. Siehe auch [Layouting](#layouting)

#### ActionBuilderFactory

~~~
	IActionBuilderFactory getActionBuilderFactory();
~~~	

Stellt die Action Builder Factory zur Verfügung. Siehe auch [Actions und Commands](#actions_and_commands)

#### Default Actions

~~~
	IDefaultActionFactory getDefaultActionFactory();
~~~	

Liefert diverse default Actions. Derzeit existieren ausschließlich default Actions für Bäume. Siehe daher auch [Das Tree Widget](#tree_widget)

#### ModelFactory

~~~
	IModelFactoryProvider getModelFactoryProvider();
~~~	

Liefert diverse Model Factories für [Menu Items](#menu_models), die [Tabelle](#table_widget) und das [Levelmeter Widget](#levelmeter_widget)

#### TextMaskBuilder

~~~
	ITextMaskBuilder createTextMaskBuilder();
~~~

Liefert einen Builder für Textmasken. Siehe auch [Maskierte Texteingaben](#text_masks) 

#### InputContentCreator

~~~
	IInputContentCreatorFactory getInputContentCreatorFactory();
~~~

Liefert spezielle Content Creator für ein [InputComposite](#input_composite) oder ein [InputDialog](#input_dialog)

#### WaitAnimation

~~~
	IWaitAnimationProcessor getWaitAnimationProcessor();
~~~

Liefert Zugriff auf die [Wait Animation](#wait_animation)

#### AnimationRunnerBuilder

~~~
	IAnimationRunnerBuilder getAnimationRunnerBuilder();
~~~

Erzeugt einen Builder für einen `AnimationRunner`. Siehe auch [Animationen](#animations)

#### Delayed Events

~~~
	IDelayedEventRunnerBuilder getDelayedEventRunnerBuilder();
~~~

Erzeugt einen Builder für einen `DelayedEventRunner`. Siehe auch [Verzögerte Events](#delayed_events)

#### Drag and Drop / Copy and Paste

~~~
	IClipboard getClipboard();
~~~

Liefert Zugriff auf das System Clipboard. Siehe auch [Copy and Paste](#copy_and_paste) und [Drag and Drop](#drag_and_drop).

~~~
	ITransferableBuilder createTransferableBuilder();
~~~

Erzeugt einen `TransferableBuilder`. Siehe auch [Copy and Paste](#copy_and_paste) und [Drag and Drop](#drag_and_drop). 

#### Widget Utils

~~~
	IWidgetUtils getWidgetUtils();
~~~

Liefert Zugriff auf die [Widget Utils](#widget_utils)

~~~
	IWindow getActiveWindow();
~~~

Gibt das aktive Fenster zurück. U.A. hilfreich bei der Erzeugung von Dialogen, welche über dem aktiven Fenster geöffnet werden sollen.

~~~
	List<IWindow> getAllWindows();
~~~

Gibt eine Liste aller Fenster zurück.

#### Toolkit Properties

~~~
	<VALUE_TYPE> void setValue(ITypedKey<VALUE_TYPE> key, VALUE_TYPE value);
	
	<VALUE_TYPE> VALUE_TYPE getValue(ITypedKey<VALUE_TYPE> key);
~~~

Mit Hilfe dieser Methoden kann man Properties auf dem Toolkit setzen und auslesen. Wenn man jowidgets Applikation schreibt, die auch Web kompatibel sein sollen, muss man sehr vorsichtig mit Singletons sein, da sich die JVM mehrere Nutzer teilen. Ansonsten könnte sich das Ändern eines globalen Zustands eines Nutzers auch auf einen anderen Nutzer auswirken.

Falls man dennoch _globale Variablen_ benötigt, zum Beispiel zum Speichern des Security Context oder ähnlichem, kann man mit Hilfe dieser Methoden ein _Session Singleton_ realisieren, da pro User Session genau ein Toolkit existiert. Siehe auch [Typed Properties](#typed_properties).

#### Umrechnungsmethoden

~~~
	Position toScreen(final Position localPosition, final IComponent component);
~~~

Transformiert eine lokale Position einer Komponente in eine absolute Bildschirmposition.

~~~
	Position toLocal(final Position screenPosition, final IComponent component);
~~~

Transformiert eine Bildschirmposition in eine lokale Position einer Komponente.

#### SupportedWidgets

~~~
	ISupportedWidgets getSupportedWidgets();
~~~

Liefert die Info, ob bestimmte Widgets unterstützt werden. Derzeit betrift dies ausschließlich den FileChooser und DirectoryChooser. Diese werden in Webapplikationen nicht unterstützt.

#### SpiMigLayoutSupport

~~~
	boolean hasSpiMigLayoutSupport();
~~~

Liefert die Information, ob die verwendete SPI Implementierung eine native Mig Layout Implementierung bietet. Nur für interne Zwecke relevant. Siehe dazu auch [Mig Layout](#mig_layout)



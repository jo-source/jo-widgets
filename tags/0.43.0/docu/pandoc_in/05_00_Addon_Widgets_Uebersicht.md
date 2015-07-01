# Addon Widgets - Übersicht{#addon_widgets} 

Addon Widgets gehören nicht zum Kern von jowidgets. Diese sind unter Umständen nicht mit allen SPI Implementierungen oder Betriebssystemen einsetzbar und sind daher in separaten Modulen untergebracht, welche mit `org.jowidgets.addons.widgets...` beginnen. Die API und die Implementierung befindet sich dabei in getrennten Modulen. Es folgt eine kurze Übersicht der Addon Widgets:

* Die __[Swt-Awt Bridge Widgets](#swt_awt_bridge_widgets)__ bieten einen komfortablen Zugang zur `org.eclipse.swt.awt_SWT_AWT` Bridge und lösen dabei bekannte Probleme der nativen Bridge durch (zum Teil aus dem Web bekannte) Workarounds. Falls ein UI Event Dispatching erforderlich ist, wird die [BridgetSwtEventLoop](#bridged_swt_event_loop) benötigt. Diese sorgt dafür, dass alle Events im _selben_ UI Thread stattfinden. Dazu wird u.A. das Swt Display im Swing Event Dispatcher Thread erzeugt.

* Die __[OleControl](#ole_control), [OfficeControl](#office_control)__ und __[Mediaplayer](#ole_media_player)__ Widgets sind nur für Windows relevant und benötigen unter Swing die [BridgetSwtEventLoop](#bridged_swt_event_loop). Die Widgets ermöglichen die Einbettung von allgemeinen Windows OLE Applikationen, sowie von MS-Word und MS-Excel in jowidgets.

* Der __[Browser](#browser_widget)__ ist ohne Bridge derzeit nur für Swt verfügbar, für Swing wird die [BridgetSwtEventLoop](#bridged_swt_event_loop) benötigt. ^[Mit einer JavaFx Implementierung könnte man die BridgetSwtEventLoop für Swing vermutlich überflüssig machen, da JavaFX besser in Swing integriert werden kann, als Swt.]

* Für den __[PdfReader](#pdf_reader)__ existiert derzeit genau eine Implementierung auf Basis des [Browser Widget](#browser_widget).

* Der __[DownloadButton](#download_button)__ bietet die Möglichkeit, eine Datei auf dem Client zu Speichern. Es gibt eine Implementierung, welche das [Browser Widget](#browser_widget) (z.B. für RWT), und eine, welche den [FileChooser](#file_chooser) verwendet. Die zweite kann für alle SPI Implementierungen verwendet werden, die einen FileChooser unterstützen (was der RWT Web Ajax Client explizit nicht tut). Dies ist derzeit für Swt und Swing der Fall. Für SWT kann man dabei die Option auswählen, die einem mehr zusagt (Browser oder FileChooser). Mit Hilfe der Download Button API kann man somit Code zum Speichern einer Datei schreiben, der sowohl für Web Clients als auch für Rich Client funktioniert. Reicht die Funktionalität des Download Buttons aus, also zum Beispiel das Speichern einer einzelnen Datei anstatt mehrerer, sollte der Download Button dem File Chooser vorgezogen werden, da so das Feature auch für Web Clients funktioniert.  

__Hinweis:__

Zum aktuellen Zeitpunkt existiert __noch nicht__ für jedes der oben aufgeführten Widgets eine ausführliche Beschreibung in dieser Dokumentation. Allerdings gibt es für __alle Widgets__ Beispielapplikation, welche die Verwendung demonstrieren. Möchte man ein Widget nutzen, welches nicht in diesem Dokument detailliert beschrieben ist, wird empfohlen per `References Workspace` in Eclipse für das zugehörige __BluePrint__^[Für jedes Widget existiert genau eine eigene BluePrint Schnittstelle, während unterschiedliche Widgets sich die gleiche Widget Schnittstelle Teilen können (z.B. `IFrame` für das Frame Widget (`IFrameBluePrint`) und das Dialog Widget (`IDialogBluePrint`)).] nach den Beispielen zu suchen. Dazu wird empfohlen, jowidgets komplett auszuchecken und die Module unter `trunk\modules` (__und NICHT `trunk\bundles`!!!__) in Eclipse zu importieren. Die Module (`trunk\modules`) enthalten auch die Beispiele.

Für die Widgets der Addon Module `org.jowidgets.addons.widgets.abc...xyz.api` findet man die BluePrints zum Beispiel, indem man in den API's nach der BluePrint Accessor Klasse sucht, welche normalerweise mit ...BPF endet, also zum Beispiel `BrowserBPF`, `MediaPlayerBPF`, usw.. 






## Austauschen und Dekorieren von Widgets - Übersicht {#substitude_and_decorate_widgets}

Neben dem globalen Anpassen der Widget Defaults ist es auch möglich, Widget Implementierungen komplett auszutauschen oder zu dekorieren. Das globale Ändern eines Widgets kann viele Aspekte motiviert sein: 

__Beispiel 1:__

Man möchte in einer Applikation Widgets, für welche man keine Leserecht besitzt, durch _Fake_ Widgets ersetzen, welche die gleiche Schnittstelle implementieren, jedoch anstatt dem original Widget ein Label anzeigen, das Nutzter darauf hinweist, dass ein Recht fehlt. Zudem sollen keine Daten von dem zugehörigen Dienst eingelesen werden, weil dies sowieso zu einer Security Exception führen würde. Die [jo-client-platform](http://code.google.com/p/jo-client-platform/) nutzt diese Möglichkeit zur Dekoration der BeanTable, des BeanRelationTree und weiteren Widgets. Der zugehörige `IToolkitInterceptor` findet sich [hier](http://code.google.com/p/jo-client-platform/source/browse/trunk/modules/addons/org.jowidgets.cap.security.ui/src/main/java/org/jowidgets/cap/security/ui/tools/CapSecurityUiToolkitInterceptor.java)

__Beispiel 2:__

Angenommen man möchte ein Testtool entwickeln, welches die Möglichkeit bieten soll, bestimmte Methodenaufrufe auf bestimmten Widgets eines bestimmten Typs zu protokollieren, während man eine Applikation bedient, um daraus Eingabedaten für einen automatisierten JUnitTest zu erstellen. Dann könnte man diese Widgets einfach durch einen Wrapper ersetzen, welcher die gewünschte Protokollierung durchführt, nachdem er die Methode auf dem Original Widget aufgerufen hat. Im Rahmen einer [Bachelorarbeit](ba_lg.pdf) wurde ein solches Testtool entwickelt.

__Beispiel 3:__

Ein weiterer Anwendungsfall könnte sein, dass man für das Debugging beim Layouten von verschachtelten Composites einen farbigen Border um alle Composites zeichnen möchte, um zu sehen, welcher Container in welche Schachtelungsebene welche Größe hat.

Im nächsten Abschnitt folgt ein weiteres, zusammenhängendes Beispiel.

### Austauschen und Dekorieren von Widgets mit Hilfe der Generic Widget Factory

Die [Generic Widget Factory](#generic_widget_factory) bietet die folgenden Möglichkeiten zum Austauschen und Dekorieren von Widgets:

* Eine registrierte Widget Implementierung durch eine andere ersetzen
* Eine registrierte Widget Implementierung dekorieren
* Eine registrierte Widget Factory dekorieren

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

#### Widget Implementierungen dekorieren{#dekorate_widgets}

#### Widget Factories dekorieren{#decorate_widget_factory}


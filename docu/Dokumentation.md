% Jowidgets Nutzerhandbuch
% Michael Grossmann
% 02. Februar 2015

# Einführung

## Lizenz

Jowidgets steht unter der [BSD Lizenz](https://www.freebsd.org/copyright/freebsd-license.html) und kann somit sowohl für kommerzielle als auch für nicht kommerzielle Zwecke frei verwendet werden.

## Übersicht

Jowidgets ist eine API zur Erstellung von graphischen Benutzeroberflächen mit Java.

Der kritische Leser stellt sich nun eventuell die Frage: "_Warum noch ein UI Framework, gibt es da nicht schon genug?_"

Die Erfahrung bei der Entwicklung von graphischen Oberflächen in unterschiedlichen Unternehmen hat gezeigt, das für viele in Unternehmen typischen Anwendungsfälle oft keine Lösungen in Standard UI Frameworks existieren, und aus dieser Not heraus das _Rad_ für diese immer wieder _neu erfunden_ wird. 

Hierzu ein kleines Beispiel. In einer Eingabemaske soll in ein Eingabefeld eine Zahl eingegeben werden. Die Eingabe soll validiert werden, und der Nutzer soll ein möglichst für ihn verständliches Feedback bekommen, wenn etwas falsch ist. Für die Eingabe bietet Swing ein `JTextField`. Dieses liefert aber einen String und keine Zahl zurück, das heißt der Wert muss erst konvertiert werden. Für die Anzeige des Validierungsfeedback könnte man in Swing ein `JLabel` verwenden. Da man Anwendungsfälle wie die Eingabe von Zahlen, Eingabe eines Datum, Anzeige von Validierungsfehlern, etc. in sehr vielen Softwarehäusern vorfindet, existieren vermutlich auch in jeder dieser firmeninternen UI Bibliothek Widgets wie: `InputNumberField`, `ValidatedInputNumberField`, `ValidatedDateField`, `ValidationLabel` usw.. Wer sich hier _wieder findet_ oder wer vielleicht selbst schon mal solch ein Widget implementiert oder verwendet hat, für den könnte jowidgets möglicherweise genau _das richtige_ UI Framework sein. 

Im Vergleich zu herkömmlichen Technologien wie _Swing_, oder _JavaFx_ liefert jowidgets kein eigenständiges Rendering für Basiswidgets ^[Wie zum Beispiel Frame, Dialog, Composite, Button, TextField, ...], sondern lediglich Adapter, welche die jowidgets Widget Schnittstellen implementieren. Dadurch ist es möglich, UI Code, welcher gegen das jowidgets API implementiert wurde, mit quasi jedem Java UI Framework auszuführen. Derzeit existieren Adapter (SPI Implementierungen, siehe  [Architektur](#architecture)) für [Swing](http://www.java-tutorial.org/swing.html), [SWT](http://eclipse.org/swt/) und [RWT](http://eclipse.org/rap/). Dadurch ist es sogar möglich, eine jowidgets Applikation als [Webapplikation](#jowidgets_rap) im Browser auszuführen. Eine _JavaFx_ Adapter Implementierung wurde im Rahmen einer Diplomarbeit prototypisch umgesetzt. Es existieren Bundle Manifeste für OSGi, so dass auch ein Einsatz in [Eclipse RCP](https://eclipse.org/) (siehe [Jowidgets und RCP](#jowidgets_rcp)) ohne weiters möglich ist.

Aufbauend auf den Basiswidgets existieren wie bereits weiter oben angedeutet zusätzliche Composite Widgets und Features, welche sich in herkömmlichen UI-Frameworks nicht finden. Zwei davon sollen an dieser Stelle exemplarisch vorgestellt werden.  

Beispielsweise gibt es ein generisches [`InputField<VALUE_TYPE>`](#input_field). Dieses hat Methoden wie `VALUE_TYPE getValue()` oder `setValue(VALUE_TYPE value)`. Für Standarddatentypen wie `Integer`, `Long`, `Double`, `Float`, `Date`, etc. existieren bereits Defaultimplementierungen. Im obigen Beispiel würde das Eingabefeld also bereits eine Zahl liefern. Mit Hilfe von Convertern kann aber auch jeder beliebige andere Datentyp unterstützt werden. Für ein `InputField` lassen sich auch beliebige Validatoren definieren. Zudem kann die UI an [`ObservableValues`](#observable_values) gebunden werden. 

Für das nächste Feature betrachten wir noch einmal das vorige Beispiel mit dem Eingabefeld von Zahlen. Für die Validierungsausgabe könnte ein ValidationLabel Widget erstellt worden sein, welches Warnungen und Fehler mit entsprechenden Icons und / oder Fehlertext anzeigen kann. Dabei kann man sowohl die Icons als auch die Farbe konfigurieren, weil Kunde A Fehler nur mit _dezenten_ Farben angzeigt bekommen möchte (laut Aussage seiner Mitarbeiter bekommt man von der standardmäßig verwendeten Farbe _Augenkrämpfe_), Kunde B kann aber Validierungsfehler, die nicht _Fett_ und _Rot_ angezeigt werden, gar nicht wahrnehmen, was die Applikation für ihn unbrauchbar macht. Stellen wir uns nun weiter vor, diese Eingabefelder und Validierungslabels sind in ein Widget mit dem Namen `GenericInputForm` eingebettet, welches sowohl von Kunde A als auch von Kunde B verwendet wird, und stellen wir uns weiter vor, dass die verwendeten Widgets noch weitere Konfigurationsattribute haben, dann müssten diese alle auch für das `GenericInputForm` konfigurierbar sein. Spätestens wenn dieses in ein weiteres Modul eingebettet ist, wird klar, dass dieses Vorgehen schnell unpraktikabel wird. 

In jowidgets ist es u.A. für solche Anwendungsfälle möglich, für jedes beliebige Widget die Defaulteinstellungen global _umzudefinieren_ (siehe dazu [Widget Defaults](#widget_defaults)). So könnte man im genannten Beispiel die gleiche Applikation einmal mit _roten_ und einmal mit _grauen_ Validierungslabels ausliefern, ohne dazu die eigentliche Applikation anpassen zu müssen. Mit der gleichen Methode könnte man zum Beispiel auch die Reihenfolge für Buttons in Eingabedialogen anpassen, definieren in welchem Format Datumswerte angezeigt werden sollen, definieren ob Buttons für Speichern und Abbrechen auf jedem Formular oder nur in der Toolbar vorhanden sind, uvm..


## Architektur {#architecture}

Die folgende Abbildung zeigt die jowidgets Architektur.

![Architektur](images/architecture.gif "Jowidgets Architektur")

UI Code wird gegen das Widget API implementiert. Die Default Implementierung von jowidgets verwendet das Widget SPI ^[Service Provider Interface], um die Features der API zu implementieren. Für die SPI existieren Implementierungen für [Swing](http://www.java-tutorial.org/swing.html), [SWT](http://eclipse.org/swt/) und [RWT](http://eclipse.org/rap/). Die SPI Implementierungen enthalten Adapter, welche die SPI Widget Schnittstellen implementieren. 

Die Schnittstellen der SPI Widgets wurden bewusst _schlank_ gehalten, um das Hinzufügen einer neuen UI Technologie möglichst einfach zu gestalten, ohne dabei Abstriche bei der _Mächtigkeit_ der API machen zu müssen. 

Dies soll am Vergleich mit dem Eclipse Standard Widget Toolkit (SWT) erläutert werden. Dort findet man eine ähnliche Architekur. Es gibt eine API, welche für verschiedenen Platformen implementiert ist. Die Implementierungen stellen im Prinzip Adapter bereit, welche mittels JNI Methodenaufrufe an das jeweilige native UI Toolkit delegieren.  

Wer SWT kennt, weiß jedoch, dass sich die Verwendung der API zum Teil sehr _"low level" anfühlt_. Will man hingegen eine API Implementierung machen (zum Bespiel für Swing oder JavaFX), sieht aus dieser Perspektive betrachtet die API sehr _mächtig_ und komplex aus. Bei der Definition der Schnittstellen musste immer ein Kompromiss aus einfacher Implementierbarkeit und komfortabler Nutzbarkeit gemacht werden. 

Durch die Aufteilung in API und SPI kann die API _"mächtige"_ Funktionen enthalten, welche aber nur ein mal implementiert werden müssen. Auf der anderen Seite ist eine SPI Implementierung für eine neue UI Technologie keine allzu komplexes Aufgabe, was die Option bietet, geschrieben UI Code auch für zukünftige UI Technologien wiederzuverwenden.

Im diesem Kontext liegt vielleicht der Vergleich nahe, die jowidgets SPI mit SWT zu vergleichen, und die jowidgets API mit [JFace](https://wiki.eclipse.org/JFace). Dieser Vergleich ist jedoch nicht ganz korrekt, denn:

* Die jowidgets SPI muss man nicht direkt verwenden, sondern man verwendet die _high level_ Schnittstellen der API. Es gibt jedoch nicht für alle SWT Wigdets ein JFace _Pondon_. 

* Die jowidgets API bietet Funktionen, welche JFace nicht bietet. JFace bietet allerdings auch Funktionen, welche jowidgets _noch_ nicht bietet.

* Jowidgets kann auch mit Swing oder zukünftigen Ui Technologien verwendet werden. Es gibt für SWT zwar auch eine [Swing Implementierung](http://swtswing.sourceforge.net/main/index.html), diese ist aber nicht vollständig und wird seit 2007 nicht mehr gepflegt. Für zukünftige UI Technologien ist eine jowidgets SPI Implementierung weniger aufwändig als eine SWT Implementierung. Code der gegen das jowidgets API implementiert wurde, ist dadurch in gewisser Hinsicht robust gegen technologische Neuerungen. 


## Widget Paradigma

In diesem Abschnitt soll der Begriff des _Widget_ im Kontext von jowidgets definiert werden. Dazu wird zunächst ein kleiner Abstecher zur Entstehungsgeschichte gemacht.

In einem gemeinsamen Projekt zweier Unternehmen sollte ein bereits vorhandenes firmeninternes Framework neu entwickelt werden. Dieses Framework lieferte unter anderem Tabellen und Formulare für CRUD Anwendungen mit Datenbankanbindung sowie Sortierung und Filterung in der Datenbank (und nicht im Client, weil das für große Datenmengen nicht möglich ist). Bei der Neuentwicklung sollten bisherige Schwachpunkte wie eine fehlende 3 Tier Architektur, Security und eine nicht austauschbare Datenschicht optimiert werden. ^[Dieses Framework ist auch unter BSD Lizenz und findet sich hier [jo-client-platform](http://code.google.com/p/jo-client-platform/)]

Die eine Firma setzte Eclipse RCP ein und wollte daran auch nichts ändern, die andere Swing. Die neu entwickelten Module sollten aber auch für die Erweiterung bisheriger (Swing) Applikation _kompatibel_ sein und ob man zukünftig anstatt Swing SWT einsetzten wollte, war auch nicht geklärt.

So entstand die Idee, für die UI relevanten Anteile wie die `BeanTable` und das `BeanForm` Schnittstellen zu definieren. Die eine Firma implementierte dann das dazugehörige Widget für Swing, die andere für SWT. Schnell wurde klar, das diese Aufteilung zu _grob_ war, denn es entstand hauptsächlich für das Formular viel redundanter Code. Es wuchs die Begehrlichkeit, auch für Eingabefelder, Comboboxen, etc. Schnittstellen zu definieren, um das `BeanForm` auf Basis dieser implementieren zu können. Das war die _Geburtsstunde_ von jowidgets. 

Man kam zu dem Schluss, dass jede Interaktion mit dem Nutzer (HCI) durch eine Java Schnittstelle spezifiziert werden kann und soll. Dies gilt für einfache Controls wie ein `Button`, eine `ComboBox` oder ein `Eingabefeld` genauso wie für zusammengesetzte Widgets wie Beispielsweise ein Formular oder ein Dialog zur Eingabe einer Person. Während das einfache Control vielleicht eine Zahl oder einen Text liefert, liefert ein `BeanForm` ein Bean und ein `PersonDialog` ein Personenobjekt.

Das API sollte nicht nur diese einfachen und zusammengesetzten Widgets Schnittstellen bereitstellen, sondern auch eine Platform bieten um selbst eigene Widget Bibliotheken nach dem gleichen Konzept und mit dem gleichen Benefit wie anpassbaren Defaultwerten, Dekorierbarkeit, Testbarkeit, etc. zu erstellen. Jowidgets sollte eine __offene__, __erweiterbare__ API für Widgets in Java bieten. Der Name jowidges steht für Java Open Widgets.

>>>_Defintion: 
>>>Ein Widget ist eine Schnittstelle für den Austausch von Informationen zwischen Nutzer und Applikation_

In jowidgets werden diese Schnittstellen immer durch Java Interfaces abgebildet.




## Widget Hierarchie

Folgende Abbildungen zeigt die konzeptionelle (nicht vollständige) Widget Hierarchie von jowidgets. Widgets teilen sich auf oberster Ebene in Komponenten (Component) und Items mit Menüs auf.


![Widget Hierarchie / Components](images/widgets_hierarchy_1.gif "Jowidgets Widget Hierarchie / Components")

Komponenten sind Fenster, Controls, Container oder InputComponents. Container enthalten Controls bzw. Controls sind Elemente von Containern. InputComponents liefern eine Nutzereingabe für einen definierten Datentyp und stellen diesen Wert dar. Dieser kann ein sowohl einfach sein (`String`, `Integer`,`Date`, ...) als auch komplex (`Person`, `Company`, `Rule`, ...). 

![Widget Hierarchie / Items](images/widgets_hierarchy_2.gif "Jowidgets Widget Hierarchie / Items")

Items sind die Elemente von Menüs oder Toolbars. Eine MenuBar enthält Menüs.



# Getting started

## Maven

## Hello World

## BluePrints

## Layouting

### Mig Layout

### Custom Layout Managers

## Menu Items

## Menu Models

## Actions und Commands

## ObervableValues {#observable_values}



# Basis und Composite Widgets 

## Frame

## Dialog

## Composite

## ScrollComposite

## SplitComposite

## ExpandComposite

## TabFolder

## TextLabel

## Label

## Icon

## Button

## ToggleButton

## TextField

## TextArea

## InputField {#input_field}

## InputDialog

## ValidationLabel

## CheckBox

## Combobox

## ComboboxSelection

## Slider

## SliderViewer

## Tree

## Table

## Calendar

## Separator

## TextSeparator

## ProgressBar

## Levelmeter

## Canvas

## Toolbar

## MessageDialog

## QuestionDialog

## PopupDialog

## FileChooser

## DirectoryChooser

## LoginDialog

## PasswordChangeDialog

## Menu

## PopupMenu

## Toolbar

## MenuItems




# Addon Widgets 

## SwtAwtControl

## AwtSwt Control

## OleControl

## OfficeControl

## PdfReader

## Mediaplayer

## Browser

## DownloadButton

## Map

# Jowidgets Workbench

# Jowidgets Utils

# Weiterführende Themen

## Validierung

## i18n

## Widget Defaults {#widget_defaults}

## Widget Dekorierung

## Erstellung eigener Widget Bibliotheken

## Automatisierte GUI Tests

## Jowidgets und RAP {#jowidgets_rap}

## Jowidgets und RCP {#jowidgets_rcp}


# Anhang

## Modulübersicht













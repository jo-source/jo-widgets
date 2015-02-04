% Jowidgets Dokumentation
% Michael Grossmann
% 02. Februar 2015

# Einführung

## Lizenz

Jowidgets steht unter der BSD Lizenz und kann somit sowohl für kommerzielle als auch für sonstige Zwecke frei verwendet werden.

## Übersicht

Jowidgets ist eine API zur Erstellung von graphischen Benutzeroberflächen mit Java.

Der kritische Leser stellt sich nun eventuell die Frage: "Warum noch ein UI Framework, gibt es da nicht schon genug?"

Die Erfahrung bei der Entwicklung von graphischen Oberflächen in unterschiedlichen Unternehmen hat jedoch gezeigt, das für viele in Unternehmen typischen Anwendungsfälle oft keine Lösungen in Standard UI Framework existieren, und aus dieser Not heraus das Rad für diese immer wieder neu erfunden wird. 

Hierzu ein kleines Beispiel. In einer Eingabemaske soll in ein Textfeld eine Zahl eingegeben werden. Die Eingabe soll validiert werden, und der Nutzer soll ein möglichst für ihn verständliches Feedback bekommen, wenn etwas falsch ist. Für die Eingabe bietet Swing ein _JTextField_. Dieses liefert aber ein String und keine Zahl zurück, das heißt der Wert muss erst konvertiert werden. Für die Anzeige des Feedback könnte man in Swing ein JLabel verwenden. Da man Anwendungsfälle wie die Eingabe von Zahlen, Eingabe eines Datum, etc. quasi in jeder Anwendung hat, existieren vermutlich auch in jeder firmeninternen Bibliothek Widgets wie: _InputNumberField_ oder _ValidatedInputNumberField_ oder _ValidatedDateField_ usw.. Wer sich jetzt dabei ertappt fühlt und wer vielleicht auch selbst schon mal solch ein _ValidationXYInputField_ oder einen _ValidatedInputDialog_ erstellt hat, für den könnte jowidgets womöglich sehr hilfreich sein. 

Im Vergleich zu herkömmlichen UI Frameworks wie _Swing_, _SWT_, _JavaFx_ oder _QT_ liefert jowidgets kein eigenständiges Rendering für Basiswidgets, sondern lediglich Adapter, welche die jowidgets Widget Schnittstellen implementieren. Dadurch ist es möglich, UI Code, welcher gegen die jowidgets API implementiert wurde, mit quasi jedem Java UI Framework auszuführen. Derzeit existieren Adapter (SPI Implementierungen, siehe  [Architektur](#architecture)) für Swing, Swt und RWT. Dadurch ist es sogar möglich, eine jowidgets Applikation als Webapplikation im Browser auszuführen. Eine JavaFx Implementierung wurde im Rahmen einer Diplomarbeit prototypisch erstellt.

Aufbauend auf diesen Basiswidgets existieren wie bereits weiter oben angedeutet zusätzliche Widgets und Features, welche sich in herkömmlichen UI-Frameworks nicht finden. 

So gibt es in jowidgets Beispielsweise ein InputField\<VALUE_TYPE\> (Siehe dazu (TODO link). Dieses hat Methoden wie VALUE_TYPE getValue() oder setValue(VALUE_TYPE value). Für viele Standarddatentypen wie Integer, Long, Double, Float, Date, etc. existieren bereits Defaultimplementierungen. Im obigen Beispiel würde das Eingabefeld also bereits eine Zahl liefern. Mit Hilfe von Convertern kann auch jeder beliebige andere Datentyp unterstützt werden. Für ein InputField lassen sich Validatoren definieren. 

Exemplarisch soll noch ein weiteres Feature dargestellt werden. Betrachten wir dazu noch einmal das vorige Beispiel mit dem Textfeld. Für die Validierungsausgabe wurde ein ValidationLabel erstellt, welches Warnungen und Fehler mit entsprechenden Icons und oder Fehlertext anzeigen kann. Dabei kann man die sowohl die Icons als auch die Farbe konfigurieren, weil Kunde A Fehler nur mit "dezenten" Farben angzeigt bekommen möchte (laut Aussage seiner Mitarbeiter bekommt man von der standardmäßig verwendeten Farbe "Augenkrämpfe"), Kunde B kann aber Validierungsfehler, die nicht "Fett" und "Rot" angezeigt werden, gar nicht wahrnehmen, was die Applikation für ihn unbrauchbar macht. Stellen wir uns nun weiter vor, diese Eingabfelder und Validierungslabels sind in ein Widget mit dem Namen GenericInputForm eingebaut, welches sowohl von Kunde A als auch von Kunde B verwendet wird, und stellen wir uns weiter vor, dass die verwendeten Widgets noch weitere Konfigurationsattribute haben, dann müssten diese alle auch für das Eingabemaskenwidget konfigurierbar sein. Spätestens wenn dieses in ein weiteres Modul eingebettet ist, wird klar, dass dieses Vorgehen schnell unpraktikabel wird. 

In jowidgets ist es für solche Anwendungsfälle möglich, für jedes beliebige Widget die Defaultseinstellungen global "umzudefinieren". Siehe dazu (TODO link). So könnte man im genannten Beispiel die gleiche Apllikation einmal mit "roten" und einmal mit "grauen" Validierungslabels ausliefern, ohne dazu die eigentliche Applikation anpassen zu müssen. Mit der gleichen Methode kann man zum Beispiel auch die Reihenfolge für Buttons in Eingedialogen anpassen, oder definieren in welchem Format Datumswerte angezeigt werden sollen.


## Architektur {#architecture}

# Getting started

## Maven

## Hello World

## BluePrints

## Layouting

### Mig Layout

### Custom Layout Managers

## Actions und Commands

## Menu Items und Models

## ObervableValues

# Basis Widgets 

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

## InputField

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

## Widget Defaults

## Widget Dekorierung

## Erstellung eigener Widget Bibliotheken

## Jowidgets und RAP

## Jowidgets und RCP











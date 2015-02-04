% Jowidgets Dokumentation
% Michael Grossmann
% 02. Februar 2015

# Einführung

## Lizenz

Jowidgets steht unter der BSD Lizenz und kann somit sowohl für kommerzielle als auch für sonstige Zwecke frei verwendet werden.

## Übersicht

Jowidgets ist eine API zur Erstellung von graphischen Benutzeroberflächen mit Java.

Der kritische Leser stellt sich nun eventuell die Frage: "_Warum noch ein UI Framework, gibt es da nicht schon genug?_"

Die Erfahrung bei der Entwicklung von graphischen Oberflächen in unterschiedlichen Unternehmen hat jedoch gezeigt, das für viele in Unternehmen typischen Anwendungsfälle oft keine Lösungen in Standard UI Framework existieren, und aus dieser Not heraus das _Rad_ für diese immer wieder _neu erfunden_ wird. 

Hierzu ein kleines Beispiel. In einer Eingabemaske soll in ein Textfeld eine Zahl eingegeben werden. Die Eingabe soll validiert werden, und der Nutzer soll ein möglichst für ihn verständliches Feedback bekommen, wenn etwas falsch ist. Für die Eingabe bietet Swing ein `JTextField`. Dieses liefert aber einen String und keine Zahl zurück, das heißt der Wert muss erst konvertiert werden. Für die Anzeige des Validierungsfeedback könnte man in Swing ein `JLabel` verwenden. Da man Anwendungsfälle wie die Eingabe von Zahlen, Eingabe eines Datum, Anzeige von Validierungsfehlern, etc. quasi in jeder Anwendung hat, existieren vermutlich auch in jeder firmeninternen UI Bibliothek Widgets wie: `InputNumberField` oder `ValidatedInputNumberField`, `ValidatedDateField`, `ValidationLabel` usw.. Wer sich hier _wieder findet_ oder wer vielleicht selbst schon mal solch ein `ValidationXYInputField` oder einen `ValidatedInputDialog` implementiert oder verwendet hat, für den könnte jowidgets möglicherweise genau _das richtige_ sein. 

Im Vergleich zu herkömmlichen UI Frameworks wie _Swing_, _SWT_, _JavaFx_ oder _QT_ liefert jowidgets kein eigenständiges Rendering für Basiswidgets ^[Zum Beispiel Frame, Dialog, Composite, Button, TextField, ...], sondern lediglich Adapter, welche die jowidgets Widget Schnittstellen implementieren. Dadurch ist es möglich, UI Code, welcher gegen die jowidgets API implementiert wurde, mit quasi jedem Java UI Framework auszuführen. Derzeit existieren Adapter (SPI Implementierungen, siehe  [Architektur](#architecture)) für __Swing__, __Swt__ und __RWT__. Dadurch ist es sogar möglich, eine jowidgets Applikation als __Webapplikation__ im Browser auszuführen. Eine _JavaFx_ Implementierung wurde im Rahmen einer Diplomarbeit prototypisch umgesetzt.

Aufbauend auf den Basiswidgets existieren wie bereits weiter oben angedeutet zusätzliche Widgets und Features, welche sich in herkömmlichen UI-Frameworks nicht finden. 

So gibt es in jowidgets Beispielsweise ein `InputField<VALUE_TYPE>` ([InputField](#input_field)). Dieses hat Methoden wie `VALUE_TYPE getValue()` oder `setValue(VALUE_TYPE value)`. Für Standarddatentypen wie `Integer`, `Long`, `Double`, `Float`, `Date`, etc. existieren bereits Defaultimplementierungen. Im obigen Beispiel würde das Eingabefeld also bereits eine Zahl liefern. Mit Hilfe von Convertern kann aber auch jeder beliebige andere Datentyp unterstützt werden. Für ein `InputField` lassen sich auch beliebige Validatoren definieren und die UI kann an [`ObservableValues`](#observable_values) gebunden werden. 

Exemplarisch soll noch ein weiteres Feature dargestellt werden. Betrachten wir dazu noch einmal das vorige Beispiel mit dem Textfeld. Für die Validierungsausgabe wurde ein ValidationLabel erstellt, welches Warnungen und Fehler mit entsprechenden Icons und oder Fehlertext anzeigen kann. Dabei kann man sowohl die Icons als auch die Farbe konfigurieren, weil Kunde A Fehler nur mit _dezenten_ Farben angzeigt bekommen möchte (laut Aussage seiner Mitarbeiter bekommt man von der standardmäßig verwendeten Farbe _Augenkrämpfe_), Kunde B kann aber Validierungsfehler, die nicht _Fett_ und _Rot_ angezeigt werden, gar nicht wahrnehmen, was die Applikation für ihn unbrauchbar macht. Stellen wir uns nun weiter vor, diese Eingabefelder und Validierungslabels sind in ein Widget mit dem Namen `GenericInputForm` eingebettet, welches sowohl von Kunde A als auch von Kunde B verwendet wird, und stellen wir uns weiter vor, dass die verwendeten Widgets noch weitere Konfigurationsattribute haben, dann müssten diese alle auch für das `GenericInputForm` konfigurierbar sein. Spätestens wenn dieses in ein weiteres Modul eingebettet ist, wird klar, dass dieses Vorgehen schnell unpraktikabel wird. 

In jowidgets ist es u.A. für solche Anwendungsfälle möglich, für jedes beliebige Widget die Defaulteinstellungen global _umzudefinieren_ (Siehe dazu [Widget Defaults](#widget_defaults)). So könnte man im genannten Beispiel die gleiche Applikation einmal mit _roten_ und einmal mit _grauen_ Validierungslabels ausliefern, ohne dazu die eigentliche Applikation anpassen zu müssen. Mit der gleichen Methode kann man zum Beispiel auch die Reihenfolge für Buttons in Eingabedialogen anpassen, definieren in welchem Format Datumswerte angezeigt werden sollen, uvm..


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

## ObervableValues {#observable_values}

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

## Jowidgets und RAP

## Jowidgets und RCP











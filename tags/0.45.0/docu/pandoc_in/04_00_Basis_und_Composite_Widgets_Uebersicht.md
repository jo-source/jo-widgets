# Core Widgets - Übersicht{#core_widgets} 

Bei den Core Widgets handelt es sich (im Abgrenzung zu den [Addon Widgets](#addon_widgets)) um die Widgets, welche für alle SPI Implementierungen und Betriebssysteme verfügbar sind.^[Oder deren Verfügbarkeit über die API abfragbar ist, was jedoch ausschließlichen den [FileChooser](#file_chooser) und [DirectoryChooser](#directory_chooser) betrifft, welche von Ajax Web Clients (RWT) nicht unterstützt werden. Dies läßt sich zum Beispiel mittels `Toolkit.getSupportedWidgets().hasFileChooser()` abfragen.]

Bei den Core Widgets kann man zwischen Basis Widgets und Composite Widgets unterscheiden. Basis Widgets erweitern dabei direkt ein SPI Widget und fügen zum Beispiel Convenience Methoden hinzu, während Composite Widgets aus anderen (u.U. nur einem) Basis Widgets und / oder Composite Widgets zusammengesetzt sind, und ganz neue Funktionen anbieten. 

Zu den Basis Widgets zählen zum Beispiel das TextField, die Combobox, der Slider, der Tree, die Tabelle, usw., zu den Composite Widgets zählen zum Beispiel das InputField, das UnitValueField, das CollectionInputField, das ExpandComposite, der PasswordChangeDialog, der SliderViewer, und weitere. 

Für die Verwendung der Widgets ist diese Unterscheidung allerdings nicht relevant, weshalb die Widgets in diesem Kapitel auch nicht nach diesem Kriterium gruppiert sind.


__Hinweis:__

Zum aktuellen Zeitpunkt existiert __noch nicht__ für jedes Core Widget eine ausführliche Beschreibung in dieser Dokumentation. Allerdings gibt es für __alle Widgets__ Beispielapplikation, welche die Verwendung demonstrieren. Möchte man ein Widget nutzen, welches nicht in diesem Dokument detailliert beschrieben ist, wird empfohlen per `References Workspace` in Eclipse für das zugehörige __BluePrint__^[Für jedes Widget existiert genau eine eigene BluePrint Schnittstelle, während unterschiedliche Widgets sich die gleiche Widget Schnittstelle Teilen können (z.B. `IFrame` für das Frame Widget (`IFrameBluePrint`) und das Dialog Widget (`IDialogBluePrint`)).] nach den Beispielen zu suchen. Dazu wird empfohlen, jowidgets komplett auszuchecken und die Module unter `trunk\modules` (__und NICHT `trunk\bundles`!!!__) in Eclipse zu importieren. Die Module (`trunk\modules`) enthalten auch die Beispiele. Um die BluePrints für unterschiedlichen Widgets zur identifizieren, kann man sich zum Beispiel die Accessor Klasse `org.jowidgets.tools.widgets.blueprint.BPF` anschauen. Mit dieser lassen sich die BluePrints für alle Widgets erzeugen.





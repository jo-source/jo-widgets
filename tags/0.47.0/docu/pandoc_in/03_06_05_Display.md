### Die Schnittstelle IDisplay{#display_interface}

Die Schnittstelle `IDisplay` leitet von [`IWidget`](#widget_interface) ab und ist die Basisschnittstelle für alle eigenständig anzeigbaren Widgets. Dazu zählen alle Arten von Fenstern und Dialogen. Die Schnittstelle `IDisplay` hat selbst keine zusätzlichen Methoden, um beim Entwurf von Dialog oder Fensterschnittstellen eine größtmögliche Freiheit zu haben, und nicht Methoden zu erben, deren Implementierung keinen Sinn macht und dadurch zu einer `UnsupportedOpperationException` führen würde.

Einige Dialoge leiten daher bewusst nicht von [IWindow](#window_interface) ab, weil zum Beispiel das Erzeugen von Kind Fenstern nicht möglich sein soll, oder der Aufruf der Methode `pack()` keinen Effekt hätte. Dazu zählen zum Beispiel der [File Chooser](#file_chooser), der [Directory Chooser](#directory_chooser), der [Message Dialog](#message_dialog), der [Question Dialog](#question_dialog) und weitere.



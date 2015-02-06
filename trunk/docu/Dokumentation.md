% Jowidgets Nutzerhandbuch
% Michael Grossmann
% 02. Februar 2015

# Einführung

## Lizenz

Jowidgets steht unter der [BSD Lizenz](https://www.freebsd.org/copyright/freebsd-license.html) und kann somit sowohl für kommerzielle als auch für nicht kommerzielle Zwecke frei verwendet werden.

## Motivation

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

In einem gemeinsamen Projekt zweier Unternehmen sollte ein bereits vorhandenes firmeninternes Framework neu entwickelt werden. Dieses Framework lieferte unter anderem Tabellen und Formulare für CRUD Anwendungen mit Datenbankanbindung sowie Sortierung und Filterung in der Datenbank (und nicht im Client, weil das für große Datenmengen nicht möglich ist). Bei der Neuentwicklung sollten bisherige Schwachpunkte wie eine fehlende 3 Tier Architektur, Security in der Service Schicht und eine nicht austauschbare Datenschicht optimiert werden. ^[Dieses Framework ist auch unter BSD Lizenz veröffentlicht und findet sich hier [jo-client-platform](http://code.google.com/p/jo-client-platform/)]

Die eine Firma setzte Eclipse RCP ein und wollte daran auch nichts ändern, die andere Swing. Die neu entwickelten Module sollten aber auch für die Erweiterung bisheriger (Swing) Applikation _kompatibel_ sein und ob man zukünftig anstatt Swing SWT einsetzten wollte, war auch nicht geklärt.

So entstand die Idee, für die UI relevanten Anteile wie die `BeanTable` und das `BeanForm` Schnittstellen zu definieren. Die eine Firma implementierte dann das dazugehörige Widget für Swing, die andere für SWT. Schnell wurde klar, das diese Aufteilung zu _grob_ war, denn es entstand hauptsächlich für das Formular viel redundanter Code. Es wuchs die Begehrlichkeit, auch für Eingabefelder, Comboboxen, etc. Schnittstellen zu definieren, um das `BeanForm` auf Basis dieser implementieren zu können. Das war die _Geburtsstunde_ von jowidgets. 

Man kam zu dem Schluss, dass jede Interaktion mit dem Nutzer (HCI) durch eine Java Schnittstelle spezifiziert werden kann und soll. Dies gilt für einfache Controls wie ein `Button`, eine `ComboBox` oder ein `Eingabefeld` genauso wie für zusammengesetzte Widgets wie Beispielsweise ein Formular oder ein Dialog zur Eingabe einer Person. Während das einfache Control vielleicht eine Zahl oder einen Text liefert, liefert ein `BeanForm` ein Bean und ein `PersonDialog` ein Personenobjekt.

Das API sollte nicht nur diese einfachen und zusammengesetzten Widgets Schnittstellen bereitstellen, sondern auch eine Platform bieten um selbst eigene Widget Bibliotheken nach dem gleichen Konzept und mit dem gleichen Benefit wie anpassbaren Defaultwerten, Dekorierbarkeit, Testbarkeit, etc. zu erstellen. 

Jowidgets sollte eine __offene__, __erweiterbare__ API für Widgets in Java bieten. Der Name jowidges steht ursprünglich für _Java Open Widgets_.

>>>_Defintion: 
>>>Ein Widget ist eine Schnittstelle für den Austausch von Informationen zwischen Nutzer und Applikation_

In jowidgets werden diese Schnittstellen immer durch Java Interfaces abgebildet. Es kann generell mehrere Implementierungen für die gleiche Schnittstelle geben. So könnte zum Beispiel die Eingabe von Personendaten auf einem Tablet anders aussehen als auf einem Desktop PC. Eine Widget Implementierung kann in der WidgetFactory überschrieben oder dekoriert (Decorator Pattern) werden. Zudem läßt sich das Default Setup eines jeden Widgets überschreiben.




## Widget Hierarchie

Folgende Abbildungen zeigt die konzeptionelle (nicht vollständige) Widget Hierarchie von jowidgets. Widgets teilen sich auf oberster Ebene in Komponenten (Component) und Items mit Menüs auf.


![Widget Hierarchie / Components](images/widgets_hierarchy_1.gif "Jowidgets Widget Hierarchie / Components")

Komponenten sind Fenster, Controls, Container oder InputComponents. Container enthalten Controls bzw. Controls sind Elemente von Containern. InputComponents liefern eine Nutzereingabe für einen definierten Datentyp und stellen diesen Wert dar. Dieser kann ein sowohl einfach sein (`String`, `Integer`,`Date`, ...) als auch komplex (`Person`, `Company`, `Rule`, ...). 

![Widget Hierarchie / Items](images/widgets_hierarchy_2.gif "Jowidgets Widget Hierarchie / Items")

Items sind die Elemente von Menüs oder Toolbars. Eine MenuBar enthält Menüs.



# Getting started

## Maven

Für die Verwendung von jowidgets mit Maven muss folgendes Repository hinzugefügt werden:

~~~ {.xml}
<repositories>
    <!-- The jowidgets maven repository -->
    <repository>
        <id>jowidgets</id>
        <url>http://jowidgets.org/maven2/</url>
    </repository>
</repositories>
~~~

## Hello World

Das Hello World Beispiel soll als Einstieg in jowidgets dienen. Dabei wird bewußt etwas mehr in die Tiefe gegangen, als nötig wäre, um ein Fenster mit einem Button in jowidgets anzuzeigen. Das Beispiel kann gut als Grundlage für weitere Experimente dienen, daher wird empfohlen, es auszuchecken oder nach zu implementieren. 

Um das Hello World Beispiel zu compilieren und zu starten sollten folgende Tools vorhanden sein:

* Java (mindestens 1.6)
* Maven
* SVN
* Eclipse (inklusive M2e Maven Integration)

Das Hello World Beispiel kann hier [http://jo-widgets.googlecode.com/svn/trunk/modules/helloworld](https://jo-widgets.googlecode.com/svn/trunk/modules/helloworld) per SVN ausgecheckt werden. 

Es findet sich dann die folgende Verzeichnisstruktur:

![Hello World Module](images/hello_world_exlorer.gif "Hello World Module")

Die Module können mit __Import Existing Maven Projects__ in eclipse importiert werden. 

### Das parent Modul

Im _parent_ Verzeichnis findet sich das parent pom.xml. 

~~~ {.xml .numberLines startFrom="1"}
<project 
	xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
	http://maven.apache.org/xsd/maven-4.0.0.xsd">
	
	<modelVersion>4.0.0</modelVersion>
	
	<groupId>org.jowidgets.helloworld</groupId>
	<artifactId>org.jowidgets.helloworld.parent</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>pom</packaging>

	<properties>
		<!-- jowidgets needs java 1.6 or higher -->
		<java.version>1.6</java.version>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<jowidgets.version>0.40.0</jowidgets.version>
	</properties>
	
	<repositories>
	    <!-- The jowidgets maven repository -->
		<repository>
			<id>jowidgets</id>
			<url>http://jowidgets.org/maven2/</url>
		</repository>
	</repositories>
	
	<modules>
		<!-- Hold the ui technology independend hello world code -->
		<module>../org.jowidgets.helloworld.common</module>
		
		<!-- Holds a starter that uses Java Swing -->
		<module>../org.jowidgets.helloworld.starter.swing</module>
		
		<!-- Holds a starter that uses Eclipse SWT (win32) -->
		<module>../org.jowidgets.helloworld.starter.swt</module>
		
		<!-- This module creates a war that uses Eclipse RWT -->
		<module>../org.jowidgets.helloworld.starter.rwt</module>
	</modules>
	
	...

</project>
~~~

Die Hello World Applikation besteht aus vier Untermodulen.

* org.jowidgets.helloworld.common
* org.jowidgets.helloworld.starter.swing
* org.jowidgets.helloworld.starter.swt
* org.jowidgets.helloworld.starter.rwt

Das Modul `org.jowidgets.helloworld.common` enthält den SPI unabhängigen Code, die drei anderen Module beinhalten die Starter für die jeweilige SPI Implementierung sowie die zugehörigen Maven Abhängigkeiten. 

In einem _realen_ Projekt hat man normalerweise nicht Starter für alle möglichen SPI Implementierungen. Dennoch ist es eine gute Idee, den SPI unabhängigen Code in ein separates Modul zu packen, um ihn in anderen Projekten, welche eventuell eine andere UI Technologie voraussetzen, besser wiederverwenden zu können.

### Das common Modul

Betrachten wir zunächst das _common_ pom.xml File im Ordner `org.jowidgets.helloworld.common`. Will man die Kernfunktion von jowidgets nutzen, muss man das Modul `org.jowidgets.tools` hinzufügen.

~~~ {.xml .numberLines startFrom="1"}
<project 
	xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
	http://maven.apache.org/xsd/maven-4.0.0.xsd">
	
	<modelVersion>4.0.0</modelVersion>
	<artifactId>org.jowidgets.helloworld.common</artifactId>

	<parent>
		<groupId>org.jowidgets.helloworld</groupId>
		<artifactId>org.jowidgets.helloworld.parent</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<relativePath>../parent/pom.xml</relativePath>
	</parent>
	
	<dependencies>	
		<!-- The jowidgets api and tools -->
		<dependency>
			<groupId>org.jowidgets</groupId>
			<artifactId>org.jowidgets.tools</artifactId>
			<version>${jowidgets.version}</version>
		</dependency>
	</dependencies>

</project>
~~~

### Transitive Abhängigkeiten der common Module

In der Praxis ist immer gut zu wissen, welche transitiven Abhängigkeiten man sich einhandelt, wenn man eine neue Technologie einführt. Jowidgets wurde bewusst so entworfen, dass (außer jowidgets selbst) __möglichst keine weiteren externen Abhängigkeiten__ existieren. 

Das Modul __`org.jowidgets.tools`__ hat die folgenden interen transitiven Abhängigkeiten (siehe auch [Jowidgets Modulübersicht](#module_overview) im Anhang) und keine externen.

__org.jowidgets.util__

:   UI unabhängige Utilities und Datenstrukturen. Siehe auch [Jowidgets Utils](#jowidgets_utils).

__org.jowidgets.i18n__

:   Eine API für (Multi User Locale) Internationalisierung. Siehe auch [i18n](#jowidgets_i18n).

__org.jowidgets.classloading.api__

:   API für Classloading Aspekte. Dies ist hauptsächlich für die OSGi Kompatibilität notwendig. Siehe auch [Jowidgets Classloading](#jowidgets_classloading).

__org.jowidgets.validation__

:   Eine (UI unabhängige) API für Validierung.

__org.jowidgets.validation.tools__

:   Vorgefertigte Validatoren.

__org.jowidgets.unit__

:   Eine (UI unabhängige) API für den Umgang mit Einheiten (z.B. Hz, Byte, KG, etc.).

__org.jowidgets.common__

:   Gemeinsame Schnittstellen und Klassen der jowidgets API und der jowidgets SPI.

__org.jowidgets.api__

:   Die jowidgets API (überwiegend Java Interfaces).

__org.jowidgets.tools__

:   Während die API überwiegend aus Schnittstellen besteht, finden sich hier nützliche Klassen, welche sich aus den Schnittstellen ergeben, wie zum Beispiel Default Implementierungen, abstrakte Basisklassen, Wrapper, Listener Adapter und weitere. 


### HelloWorldApplication - Der common Ui Code

Die Klasse `HelloWorldApplication` implementiert die Schnittstelle `IApplication`. Diese hat eine Startmethode, welche den `Lifecycle` als Parameter liefert. Wird auf diesem die Methode `finish()`aufgerufen, wird die Applikation beendet. 

Will man jowidgets in nativen Ui Code (z.B. Swing oder SWT) integrieren, hat man in der Regel keine IApplication zum starten. Stattdessen werden das Root Fenster  (z.B. Shell oder JFrame) und weitere Widgets (z.B. Composite, JPanel) nativ erzeugt. In [Jowidgets Code in native Projekte integrieren](#integrate_jowidgets_in_native_code) wird erläutert, wie man in diesem Fall vorgehen kann.

```{.java .numberLines startFrom="1"} 
package org.jowidgets.helloworld.common;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class HelloWorldApplication implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//Create a frame BluePrint with help of the BluePrintFactory (BPF)
		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(400, 300)).setTitle("Hello World");

		//Create a frame with help of the Toolkit and BluePrint. 
		//This convenience method finishes the ApplicationLifecycle when 
		//the root frame will be closed.
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

		//Use a simple MigLayout with one column and one row 
		frame.setLayout(new MigLayoutDescriptor("[]", "[]"));

		//Create a button BluePrint with help of the BluePrintFactory (BPF)
		final IButtonBluePrint buttonBp = BPF.button().setText("Hello World");

		//Add the button defined by the BluePrint to the frame
		final IButton button = frame.add(buttonBp);

		//Add an ActionListener to the button
		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				System.out.println("Hello World");
			}
		});

		//set the root frame visible
		frame.setVisible(true);
	}
}
```

Im Vergleich zu anderern UI Frameworks gibt es in jowidgets einen wesentlichen Unterschied bei der Widget Erzeugung. Widgets werden nicht direkt, also mit Hilfe des `new` Schlüßelwortes instantiiert, sondern von einer Factory erzeugt. Dies hat einige Vorteile, siehe dazu auch [Die GenericWidgetFactory](#generic_widget_factory). 

Die Wigdet Factory benötigt für die Erzeugung eines Widgets ein sogenanntes _BluePrint_ (Blaupause). Siehe dazu auch [BluePrints](#blue_prints). BluePrints erhält man von der BluePrintFactory. Die Klasse `BPF` liefert einen Zugriffspunkt auf alle BluePrints. In Zeile 21 wird so das BluePrint für ein Frame und in Zeile 33 das BluePrint für einen Button erzeugt. BluePrints sind Setup und Builder zugleich. Sie werden dazu verwendet, das Widgets zu Konfigurieren und liefern dadurch der Implementierung das initiale Setup des Widgets. In Zeile 22 wird so die Größe und der Titel des Fensters gesetzt. In Zeile 33 wird so das Button Label definiert. BluePrints sind nach dem Prinzip des _BuilderPattern_ entworfen, das heißt sie liefern die eigene Instanz als Rückgabewert, um verkettete Aufrufe zu ermöglichen.

In der Regel wird die Widget Factory nicht explizit verwendet. Um das root Fenster zu erhalten, wird in Zeile 27 ein IFrame Widget mit Hilfe des `frameBp` und des Toolkit's erzeugt. Bei der verwendeten Methode handelt es sich um eine _convenience Methode_ die beim Schließen des Fensters den `ApplicationLifecycle` beendet. Das Frame ist gleichzeitig auch ein Container. Um ein Widget zu einem Container hinzuzufügen, fügt man das BluePrint hinzu und erhält dafür das Widget. In Zeile 36 wird so der Button erzeugt. 

In Zeile 30 wird das [Layout](#layouting) mit einer Spalte und einer Zeile für das Frame gesetzt. Zeile 39 fügt dem erzeugten Button einen Listener hinzu, welcher beim _Klicken_ eine Konsolenausgabe macht. In Zeile 47 wird das Fenster schlußendlich angezeigt.

__Fassen wir noch einmal zusammen:__

:     Um Widgets zu erhalten, benötigt man vorab ein BluePrint. Dieses erhält man von der BluePrint Factory. Auf dem BluePrint kann man das Setup konfigurieren. Für ein BluePrint bekommt man dann ein Widget.

Auch wenn sich das vielleicht erst mal ungewohnt anhört, ist das auch schon alles, was im Vergleich zu anderen UI Frameworks generell anders ist. Man wird zudem schnell feststellen, dass man dadurch sehr kompakten und zudem gut lesbaren UI Code erzeugen kann. Den im Vergleich zu langen Konstruktoraufrufen ist beim Builder Pattern immer klar, welchen Parameter man konfiguriert. Außerdem kann die Erzeugung von BluePrints meist auch implizit passieren. So könnte man den Code von Zeile 32 bis 36 auch so aufschreiben:

```{.java} 
	final IButton button = frame.add(BPF.button().setText("Hello World"));
```

Zudem können BluePrints für die Erzeugung mehrerer Instanzen wiederverwendet werden, was sich oft als sehr hilfreich erweißt.

__Anbei folgen noch ein paar Anmerkungen zum Code Style:__

Unter dem Begriff [CleanCode](#http://de.wikipedia.org/wiki/Clean_Code) findet sich der Hinweis, immer _sprechende_ Variablennamen zu verwenden, und insbesondere kryptische Variablennamen wie: "is, os, hfu, jkl, ..." etc. zu vermeiden. Im Allgemeinen ist dem auch voll zuzustimmen. 

Da jedoch BluePrints in jowidgets eine so fundamentale Rolle spielen, wird hier dennoch bewusst von dieser Regel abgewichen. Der Name der _Abbreviation_ Accessor Klasse `BPF` wurde bewußt kurz gewählt, um eine bessere _inline_ Verwendung (siehe oben) zu ermöglichen. (Wer das trotzdem nicht mag, kann anstatt `BPF.button()` auch `Toolkit.getBluePrintFactory().button()` schreiben.)

Um BluePrint Variablen besser von den eigentlichen Widget Variablen unterscheiden zu können, sollten diese per Konvention immer die Endung `bp` oder `bluePrint` haben. Wird das BluePrint nur für die Erzeugung eines Einzelnen Widgets verwendet, bietet es sich an, für BluePrint und Widget den gleichen Präfix zu wählen, also zum Beispiel `buttonBp` und `button`. 

### Der Swing Starter {#hello_world_swing}

Um die Applikation mit Swing zu starten, betrachten wir das Modul `org.jowidgets.helloworld.starter.swing` und dort zunächst das folgende pom.xml.

~~~{.xml .numberLines startFrom="1"}
<project 
	xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
	http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<artifactId>org.jowidgets.helloworld.starter.swing</artifactId>

	<parent>
		<groupId>org.jowidgets.helloworld</groupId>
		<artifactId>org.jowidgets.helloworld.parent</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<relativePath>../parent/pom.xml</relativePath>
	</parent>
	
	<dependencies>

        <!-- The ui technology independend hello world module -->
		<dependency>
			<groupId>org.jowidgets.helloworld</groupId>
			<artifactId>org.jowidgets.helloworld.common</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

        <!-- The default implementation of the jowidgets api -->
		<dependency>
			<groupId>org.jowidgets</groupId>
			<artifactId>org.jowidgets.impl</artifactId>
			<version>${jowidgets.version}</version>
		</dependency>

        <!-- The Swing implementation of the jowidgets spi -->
		<dependency>
			<groupId>org.jowidgets</groupId>
			<artifactId>org.jowidgets.spi.impl.swing</artifactId>
			<version>${jowidgets.version}</version>
		</dependency>

	</dependencies>

</project>
~~~

Ab Zeile 20 wird das common Modul der HelloWorld Applikation hinzugefügt. Ab Zeile 27 folgt die Defaultimplementierung der jowidgtes API und ab Zeile 34 wird die SPI Implementierung für Swing hinzugefügt.

Dadurch ergeben sich die folgenden (jowidgets internen) transienten Modulabhängigkeiten: 

__org.jowidgets.test.common, org.jowidgets.test.spi, org.jowidgets.test.api__

:   Fügt den Widgets spezielle Testmethoden hinzu, wie zum Beispiel die Methode `push()` für einen Button. Dadurch können zum etwa in automatisierten JUnit GUI Tests Nutzereingaben simuliert werden. Siehe auch [Automatisierte GUI Tests](#automated_ui_tests). 

__org.jowidgets.spi.impl.common__

:   Gemeinsamer Code der von allen SPI Implementierungen genutzt wird.

__org.jowidgets.spi.impl.swing.common__

:   Die Swing SPI Implementierung.

__org.jowidgets.spi.impl.swing__

:   Die Swing SPI Implementierung als [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Plugin


Die Swing SPI Implementierung hat zudem eine externe Abhängigkeit auf [MigLayout](http://www.miglayout.com/) für Swing


Die Klasse `HelloWorldStarterSwing` ist für das Starten der Applikation zuständig.

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.helloworld.starter.swing;

import javax.swing.UIManager;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.helloworld.common.HelloWorldApplication;

public final class HelloWorldStarterSwing {

	private HelloWorldStarterSwing() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		Toolkit.getApplicationRunner().run(new HelloWorldApplication());
		System.exit(0);
	}
}
~~~

In Zeile 13 wird das System System Look and Feel für Swing gesetzt, Zeile 14 enthält eine iOS spezifische Property um Menüs _Apple typisch_ anzuzeigen. Um die `HelloWorldApplication`, welche `IApplication` implementiert, zu starten, wird ein IApplicationRunner benötigt. Dieser kann vom [Jowidgets Toolkit](#jowidgets_toolkit) geholt werden. In Zeile 15 wird so die Applikation gestartet. Der Aufruf blockiert, bis die IApplication Implementierung auf dem übergebenen `ILifecycle` Object die Methode `finish()` aufruft. Anschließend wird die VM beendet.

Die folgende Abbildung zeigt das Hello World Fenster für die Swing SPI Implementierung:

![Hello World Swing](images/hello_world_swing.gif "Hello World Swing")






### Der Swt Starter

Um die Applikation mit Swt zu starten, betrachten wir das Modul `org.jowidgets.helloworld.starter.swt` und dort zunächst das folgende pom.xml.

~~~{.xml .numberLines startFrom="1"}
<project 
	xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
	http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<artifactId>org.jowidgets.helloworld.starter.swt</artifactId>

	<parent>
		<groupId>org.jowidgets.helloworld</groupId>
		<artifactId>org.jowidgets.helloworld.parent</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<relativePath>../parent/pom.xml</relativePath>
	</parent>
	
	<dependencies>

		<!-- The ui technology independend hello world module -->
		<dependency>
			<groupId>org.jowidgets.helloworld</groupId>
			<artifactId>org.jowidgets.helloworld.common</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

		<!-- The default implementation of the jowidgets api -->
		<dependency>
			<groupId>org.jowidgets</groupId>
			<artifactId>org.jowidgets.impl</artifactId>
			<version>${jowidgets.version}</version>
		</dependency>

		<!-- The SWT implementation of the jowidgets spi -->
		<dependency>
			<groupId>org.jowidgets</groupId>
			<artifactId>org.jowidgets.spi.impl.swt</artifactId>
			<version>${jowidgets.version}</version>
		</dependency>
		
		<!-- The SWT implementation for win32 -->
		<dependency>
            <groupId>org.eclipse</groupId>
            <artifactId>swt-win32-win32-x86</artifactId>
            <version>4.3</version>
        </dependency>

	</dependencies>

</project>
~~~

Ab Zeile 20 wird das common Modul der HelloWorld Applikation hinzugefügt. Ab Zeile 27 folgt die Defaultimplementierung der jowidgtes API und ab Zeile 34 wird die SPI Implementierung für Swt hinzugefügt.

Dadurch ergeben sich die folgenden (jowidgets internen) transienten Modulabhängigkeiten: 

__org.jowidgets.test.common, org.jowidgets.test.spi, org.jowidgets.test.api__, __org.jowidgets.spi.impl.swing.common__

:   Analog zum [Hello World Swing Starter](#hello_world_swing)

__org.jowidgets.spi.impl.swt.common__

:   Die Swt SPI Implementierung.

__org.jowidgets.spi.impl.swt__

:   Die Swt SPI Implementierung als [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Plugin

Die Swt SPI Implementierung hat zudem eine externe Abhängigkeit auf [MigLayout](http://www.miglayout.com/) für Swt.

Selbstverständlich wird auch eine externe Abhängigkeit auf Swt benötigt (ab Zeile 41).

__Hinweis: Bei der Verwendung von 64Bit Java unter Windows ist die SWT Abhängigkeit wie folgt auszutauschen__

~~~{.xml .numberLines startFrom="1"}
	<!-- The SWT implementation for win32 -->
	<dependency>
        <groupId>org.eclipse</groupId>
        <artifactId>swt-win32-win32-x86_64</artifactId>
        <version>4.3</version>
    </dependency>
~~~

Weitere SWT Maven Artefakte für andere Betriebsysteme finden sich unter anderem hier: [http://www.jowidgets.org/maven2/org/eclipse/](http://www.jowidgets.org/maven2/org/eclipse/)


Die Klasse `HelloWorldStarterSwt` ist für das Starten der Applikation zuständig.

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.helloworld.starter.swt;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.helloworld.common.HelloWorldApplication;

public final class HelloWorldStarterSwt {

	private HelloWorldStarterSwt() {}

	public static void main(final String[] args) throws Exception {
		Toolkit.getApplicationRunner().run(new HelloWorldApplication());
		System.exit(0);
	}
}
~~~

Das Starten verhält sich analog zum [Hello World Swing Starter](#hello_world_swing)

Die folgende Abbildung zeigt das Hello World Fenster für die Swt SPI Implementierung:

![Hello World Swt](images/hello_world_swt.gif "Hello World Swt")


### Der Rwt Starter

Um die Applikation mit Rwt als Webapplikation zu starten, betrachten wir das Modul `org.jowidgets.helloworld.starter.rwt` und dort zunächst das folgende pom.xml.

~~~{.xml .numberLines startFrom="1"}
<project 
	xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
	http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<artifactId>org.jowidgets.helloworld.starter.rwt</artifactId>
	<packaging>war</packaging>

	<parent>
		<groupId>org.jowidgets.helloworld</groupId>
		<artifactId>org.jowidgets.helloworld.parent</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<relativePath>../parent/pom.xml</relativePath>
	</parent>

	<dependencies>

		<!-- The ui technology independend hello world module -->
		<dependency>
			<groupId>org.jowidgets.helloworld</groupId>
			<artifactId>org.jowidgets.helloworld.common</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

		<!-- The default implementation of the jowidgets api -->
		<dependency>
			<groupId>org.jowidgets</groupId>
			<artifactId>org.jowidgets.impl</artifactId>
			<version>${jowidgets.version}</version>
		</dependency>

		<!-- The RWT implementation of the jowidgets spi -->
		<dependency>
			<groupId>org.jowidgets</groupId>
			<artifactId>org.jowidgets.spi.impl.rwt</artifactId>
			<version>${jowidgets.version}</version>
		</dependency>

		<!-- The RWT dependency -->
		<dependency>
			<groupId>org.eclipse</groupId>
			<artifactId>org.eclipse.rap.rwt</artifactId>
			<version>2.0.0.20130205-1612</version>
		</dependency>

	</dependencies>

	<build>
		<finalName>helloWorldRwt</finalName>
	</build>

</project>
~~~

Ab Zeile 20 wird das common Modul der HelloWorld Applikation hinzugefügt. Ab Zeile 27 folgt die Defaultimplementierung der jowidgtes API und ab Zeile 34 wird die SPI Implementierung für Rwt hinzugefügt.

Dadurch ergeben sich die folgenden (jowidgets internen) transienten Modulabhängigkeiten: 

__org.jowidgets.test.common, org.jowidgets.test.spi, org.jowidgets.test.api__, __org.jowidgets.spi.impl.swing.common__

:   Analog zum [Hello World Swing Starter](#hello_world_swing)

__org.jowidgets.spi.impl.swt.common__

:   Die Swt SPI Implementierung.

__org.jowidgets.spi.impl.rwt__

:   Die Rwt SPI Implementierung

Selbstverständlich wird auch eine externe Abhängigkeit auf Rwt benötigt (ab Zeile 41).

Die Swt SPI Implementierung hat zudem eine externe Abhängigkeit auf [MigLayout](http://www.miglayout.com/) für Swt.

Betrachten wir als nächstes die Klassen `HelloWorldConfiguration` und `HelloWorldStarterRwt`

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.helloworld.starter.rwt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;

public final class HelloWorldConfiguration implements ApplicationConfiguration {

	@Override
	public void configure(final Application application) {
		application.setOperationMode(OperationMode.SWT_COMPATIBILITY);
		final Map<String, String> properties = new HashMap<String, String>();
		properties.put(WebClient.PAGE_TITLE, "Hello World");
		application.addEntryPoint("/HelloWorld", HelloWorldStarterRwt.class, properties);
	}
}
~~~

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.helloworld.starter.rwt;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.helloworld.common.HelloWorldApplication;
import org.jowidgets.spi.impl.rwt.RwtEntryPoint;

public final class HelloWorldStarterRwt extends RwtEntryPoint {

	//URL: http://127.0.0.1:8080/helloWorldRwt/HelloWorld
	public HelloWorldStarterRwt() {
		super(new Runnable() {
			@Override
			public void run() {
				Toolkit.getApplicationRunner().run(new HelloWorldApplication());
			}
		});
	}

}
~~~

In Zeile 14 wird analog zu Swing und Swt die Applikation mit Hilfe des ApplicationRunners gestartet.

Für die Webapplikation ist noch folgendes Web.xml erforderlich.

~~~{.xml .numberLines startFrom="1"}
<?xml version="1.0" encoding="UTF-8"?>
<web-app 
	xmlns="http://java.sun.com/xml/ns/j2ee" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee 
	http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
	version="2.4">

	<display-name>Hello World Rwt</display-name>

	<context-param>
		<param-name>org.eclipse.rap.applicationConfiguration</param-name>
		<param-value>org.jowidgets.helloworld.starter.rwt.HelloWorldConfiguration</param-value>
	</context-param>

	<listener>
		<listener-class>org.eclipse.rap.rwt.engine.RWTServletContextListener</listener-class>
	</listener>

	<servlet>
		<servlet-name>HelloWorld</servlet-name>
		<servlet-class>org.eclipse.rap.rwt.engine.RWTServlet</servlet-class>
	</servlet>

	<servlet-mapping>
		<servlet-name>HelloWorld</servlet-name>
		<url-pattern>/HelloWorld</url-pattern>
	</servlet-mapping>
</web-app>
~~~

Für ein tieferes Verständnis von Rwt sei auf die [RAP / RWT Dokumentation](http://eclipse.org/rap/documentation/) verwiesen.

## Das Jowidgets Toolkit {#jowidgets_toolkit}

## BluePrints {#blue_prints}

## Container

## Layouting {#layouting}

### Mig Layout

### Custom Layout Managers

## Menu Items

## Menu Models

## Actions und Commands

## Icons und Images

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

# Jowidgets Utils {#jowidgets_utils}

# Weiterführende Themen

## Jowidgets Code in native Projekte integrieren {#integrate_jowidgets_in_native_code}

### Jowidgets Code in Swt / RCP Projekte integrieren

### Jowidgets Code in Swing Projekte integrieren

## Validierung

## i18n {#jowidgets_i18n}

## Die GenericWidgetFactory {#generic_widget_factory}

## Widget Defaults {#widget_defaults}

## Widget Dekorierung

## Erstellung eigener Widget Bibliotheken

## Automatisierte GUI Tests {#automated_ui_tests} 

## Jowidgets und RAP {#jowidgets_rap}

## Jowidgets Classloading{#jowidgets_classloading}

## Jowidgets und RCP {#jowidgets_rcp}

# Anhang

## Modulübersicht {#module_overview}













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

## Das Hello World Beispiel{#hello_world_example}

Das Hello World Beispiel soll als Einstieg in jowidgets dienen. Dabei wird bewußt etwas mehr in die Tiefe gegangen, als nötig wäre, um ein Fenster mit einem Button in jowidgets anzuzeigen. Das Beispiel kann gut als Grundlage für die weitere Arbeit dienen. Daher wird empfohlen, es auszuchecken oder nachzuimplementieren. 

Um das Hello World Beispiel zu compilieren und zu starten sollten folgende Tools vorhanden sein:

* Java (mindestens 1.6)
* Maven
* GIT
* Eclipse (inklusive M2e Maven Integration)
* Tomcat (für das Starten im Browser)

Jowidgets kann hier [https://github.com/jo-source/jo-widgets.git](https://github.com/jo-source/jo-widgets.git) per GIT ausgecheckt werden. 

Das Hello World Beispiel befindet sich hier [https://github.com/jo-source/jo-widgets/tree/master/modules/helloworld](https://github.com/jo-source/jo-widgets/tree/master/modules/helloworld) und hat die folgende Verzeichnisstruktur:

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

### Transitive Abhängigkeiten des common Moduls

In der Praxis ist immer gut zu wissen, welche transitiven Abhängigkeiten man sich _einhandelt_, wenn man eine neue Technologie einführt. Jowidgets wurde bewusst so entworfen, dass (außer jowidgets selbst) __möglichst keine weiteren externen Abhängigkeiten__ notwendig sind. 

Durch das Modul __`org.jowidgets.tools`__ hat man die folgenden internen (siehe auch [Jowidgets Modulübersicht](#module_overview) im Anhang) und __keine externen transitiven Abhängigkeiten__.

__org.jowidgets.util__

:   UI unabhängige Utilities und Datenstrukturen. Siehe auch [Jowidgets Utils](#jowidgets_utils).

__org.jowidgets.i18n__

:   Eine API für (Multi User Locale) Internationalisierung. Siehe auch [i18n](#jowidgets_i18n).

__org.jowidgets.classloading.api__

:   API für Classloading Aspekte. Dies ist hauptsächlich für die OSGi Kompatibilität notwendig. Siehe auch [Jowidgets Classloading](#jowidgets_classloading).

__org.jowidgets.validation__

:   Eine (UI unabhängige) API für Validierung. Siehe auch [Die Validation API](#validation_api)

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


### HelloWorldApplication - Der common Ui Code{#hello_world_common_code}

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

Im Vergleich zu anderern UI Frameworks gibt es in jowidgets einen wesentlichen Unterschied bei der Widget Erzeugung. Widgets werden nicht direkt, also mit Hilfe des `new` Schlüßelwortes instantiiert, sondern von einer Factory erzeugt. Dies hat einige Vorteile, siehe dazu auch [Die Generic Widget Factory](#generic_widget_factory). 

Die Wigdet Factory benötigt für die Erzeugung eines Widgets ein sogenanntes _BluePrint_ (Blaupause). Siehe dazu auch [BluePrints](#blue_prints). BluePrints erhält man von der [BluePrint Factory](#blue_print_factory). Die Klasse [`BPF`](#bpf_accessor_class) liefert einen Zugriffspunkt auf alle BluePrints der BluePrint Factory. In Zeile 21 wird so das BluePrint für ein Frame und in Zeile 33 das BluePrint für einen Button erzeugt. BluePrints sind [Setup](#widget_setup) und [Builder](#widget_setup_builder) zugleich. Sie werden dazu verwendet, das Widgets zu Konfigurieren und liefern dadurch der Implementierung das initiale Setup des Widgets. In Zeile 22 wird so die Größe und der Titel des Fensters gesetzt. In Zeile 33 wird so das Button Label definiert. BluePrints sind nach dem Prinzip des _BuilderPattern_ entworfen, das heißt sie liefern die eigene Instanz als Rückgabewert, um verkettete Aufrufe zu ermöglichen.

In der Regel wird die [Generic Widget Factory](#generic_widget_factory) nicht explizit verwendet. Um das root Fenster zu erhalten, wird in Zeile 27 ein IFrame Widget mit Hilfe des `frameBp` und des [Toolkit's](#toolkit_create_root_frame) erzeugt. Bei der verwendeten Methode handelt es sich um eine _convenience Methode_ die beim Schließen des Fensters den `ApplicationLifecycle` beendet. Das Frame ist gleichzeitig auch ein Container. Um ein Widget zu einem Container hinzuzufügen, fügt man das BluePrint hinzu und erhält dafür das Widget. In Zeile 36 wird so der Button erzeugt. 

In Zeile 30 wird das [Layout](#mig_layout) mit einer Spalte und einer Zeile für das Frame gesetzt. Zeile 39 fügt dem erzeugten Button einen Listener hinzu, welcher beim _Klicken_ eine Konsolenausgabe macht. In Zeile 47 wird das Fenster schlußendlich angezeigt.

__Fassen wir noch einmal zusammen:__

:     Um Widgets zu erhalten, benötigt man vorab ein [BluePrint](#blue_prints). Dieses erhält man von der [BluePrint Factory](#blue_print_factory). Auf dem BluePrint kann man das [Setup](#widget_setup) konfigurieren. Für ein BluePrint bekommt man dann ein Widget.

Auch wenn sich das vielleicht erst mal ungewohnt anhört, ist das auch schon alles, was im Vergleich zu anderen UI Frameworks generell anders ist.

Man wird zudem schnell feststellen, dass man dadurch sehr kompakten und zudem gut lesbaren UI Code erzeugen kann. Den im Vergleich zu langen Konstruktoraufrufen ist beim Builder Pattern immer klar, welchen Parameter man konfiguriert. Außerdem kann die Erzeugung von BluePrints meist auch implizit passieren. So könnte man den Code von Zeile 32 bis 36 auch so aufschreiben:

~~~
	final IButton button = frame.add(BPF.button().setText("Hello World"));
~~~

Zudem können BluePrints für die Erzeugung mehrerer Instanzen wiederverwendet werden, was sich oft als sehr hilfreich erweißt.

__Anbei folgen noch ein paar Anmerkungen zum Code Style:__

Unter dem Begriff [CleanCode](#http://de.wikipedia.org/wiki/Clean_Code) findet sich der Hinweis, immer _sprechende_ Variablennamen zu verwenden, und insbesondere kryptische Variablennamen wie: "is, os, hfu, jkl, ..." etc. zu vermeiden. Im Allgemeinen ist dem auch voll zuzustimmen. 

Da jedoch BluePrints in jowidgets eine so fundamentale Rolle spielen, wird hier dennoch bewusst von dieser Regel abgewichen. Der Name der _Abbreviation_ Accessor Klasse `BPF` wurde bewußt kurz gewählt, um eine bessere _inline_ Verwendung (siehe oben) zu ermöglichen. (Wer das trotzdem nicht mag, kann anstatt `BPF.button()` auch `Toolkit.getBluePrintFactory().button()` schreiben.)

Um BluePrint Variablen besser von den eigentlichen Widget Variablen unterscheiden zu können, sollten diese per Konvention immer die Endung `bp` oder `bluePrint` haben. Wird das BluePrint nur für die Erzeugung eines einzelnen Widgets verwendet, bietet es sich an, für BluePrint und Widget den gleichen Präfix zu wählen, also zum Beispiel `buttonBp` und `button`. 

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

__org.jowidgets.spi.impl.common__

:   Gemeinsamer Code der von allen SPI Implementierungen genutzt wird.

__org.jowidgets.spi.impl.swing.common__

:   Die Swing SPI Implementierung.

__org.jowidgets.spi.impl.swing__

:   Die Swing SPI Implementierung als [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Plugin


Die Swing SPI Implementierung hat zudem eine __externe Abhängigkeit auf [MigLayout](http://www.miglayout.com/) für Swing__.


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

In Zeile 13 wird das System System Look and Feel für Swing gesetzt, Zeile 14 enthält eine iOS spezifische Property um Menüs _Apple typisch_ anzuzeigen. Um die `HelloWorldApplication`, welche `IApplication` implementiert, zu starten, wird ein IApplicationRunner benötigt. Dieser kann vom [Jowidgets Toolkit](#jowidgets_toolkit) geholt werden. In Zeile 15 wird so die Applikation gestartet. Der Aufruf blockiert, bis die IApplication Implementierung auf dem übergebenen `IApplicationLifecycle` Object die Methode `finish()` aufruft. Anschließend wird die VM beendet.

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

__org.jowidgets.spi.impl.common__

:   Gemeinsamer Code der von allen SPI Implementierungen genutzt wird.

__org.jowidgets.spi.impl.swt.common__

:   Die Swt SPI Implementierung.

__org.jowidgets.spi.impl.swt__

:   Die Swt SPI Implementierung als [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Plugin

Selbstverständlich wird auch eine __externe Abhängigkeit auf Swt__ benötigt (ab Zeile 41).

Die Swt SPI Implementierung hat zudem eine __externe Abhängigkeit auf [MigLayout](http://www.miglayout.com/) für Swt__.

__Hinweis: Bei der Verwendung von 64Bit Java unter Windows ist die SWT Abhängigkeit wie folgt auszutauschen__

~~~{.xml .numberLines startFrom="1"}
	<!-- The SWT implementation for win64 -->
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

__org.jowidgets.spi.impl.common__

:   Gemeinsamer Code der von allen SPI Implementierungen genutzt wird.

__org.jowidgets.spi.impl.swt.common__

:   Die Swt SPI Implementierung.

__org.jowidgets.spi.impl.rwt__

:   Die Rwt SPI Implementierung

Selbstverständlich wird auch eine __externe Abhängigkeit auf Rwt__ benötigt (ab Zeile 41).

Die Swt SPI Implementierung hat zudem eine __externe Abhängigkeit auf [MigLayout](http://www.miglayout.com/) für Swt__.

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

Für die Webapplikation ist zudem noch die folgende web.xml Datei erforderlich.

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

Um die Applikation zu starten, muss zunächst das `helloWorldRwt.war` gebaut werden. Dazu kann man in der Konsole im Verzeichnis `helloworld/parent` den Befehl

~~~
    mvn clean install
~~~

eingeben. Im Ordner `helloworld/org.jowidgets.helloworld.starter.rwt/target` findet sich dann die Datei `helloWorldRwt.war`. Diese kann zum Beispiel in einem [Tomcat](http://tomcat.apache.org/) deployed werden. Macht man das lokal, kann man die Applikation dann mit der Url: `http://127.0.0.1:8080/helloWorldRwt/HelloWorld` im Browser starten.

Die folgende Abbildung zeigt das Hello World Fenster im Firefox Browser.

![Hello World Rwt](images/hello_world_rwt.gif "Hello World Rwt")

Für ein tieferes Verständnis von Rwt sei auf die [RAP / RWT Dokumentation](http://eclipse.org/rap/documentation/) verwiesen.

Weitere Information zum Thema jowidgets und RAP / RWT finden sich im Abschnitt [Jowidgets und RAP](#jowidgets_rap)



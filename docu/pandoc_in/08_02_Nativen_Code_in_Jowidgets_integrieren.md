## Nativen Code in jowidgets Code integrieren {#integrate_native_code_in_jowidgets}

Unter gewissen Umständen kann es wünschenswert sein, nativen UI Code innerhalb von jowidgets Code zu integrieren. Dabei wird vorausgesetzt, dass die native UI Technologie der der verwendeten SPI Implementierung entspricht. 


Mögliche Gründe für die Einbettung von nativem Code könnten sein:

* Es existieren komplexe native Widgets, welche man verwenden möchte und für die in jowidgets kein Pendant existiert.
* Ein Jo Widget bietet eine bestimmte Funktion nicht an, obwohl es das native Widget tut.

__Hinweis:__ 

Man sollte sich bewusst machen, dass man durch die direkte Einbettung von nativem Code später nicht mehr __einfach__ in der Lage ist, das Modul oder Widget in Kombination mit einer anderen UI Technologie zu verwenden. 

Anstatt nativen Code direkt einzubetten, könnte man für die oben genannten Fälle auch wie folgt vorgehen:

* [Kapseln eines nativen Widgets durch eine Jo Widget Schnittstelle](#create_api_for_native_widget)
* [Erweitern einer existierenden Jo Widget Schnittstelle um nativ verfügbare Funktionen](#extend_jo_interface_for_nativ_available_methods)

Falls dieses Vorgehen nicht in Frage kommt, kann man den nativen Code direkt Einbetten, indem man die [native UI Referenz](#use_native_ui_reference) verwendet.













### Verwendung der nativen UI Referenz{#use_native_ui_reference}

Jedes Jo Widget liefert mit Hilfe der folgenden Methode die native UI Referenz:

~~~
	Object getUiReference();
~~~

Der Typ hängt dabei von der verwendeten SPI Implementierung ab. Für [Basis Widgets](#core_widgets) ist dies in der Regel das direkte native Pendant, also zum Beispiel unter Swing ein `JPanel ` für ein `IComposite`, ein `JButton` für ein `IButton` und so weiter. Es gibt jedoch Ausnahmen, so liefert eine `ITable` unter Swing ein `JScrollPane` welches eine `JTable` enthält und unter Swt eine `org.eclipse.swt.widget.Table`. Im Zweifelsfall sollte man einfach in der aktuellen SPI Implementierung nachschauen, oder es mittels `getClass().getName()` ausprobieren. [Composite Widgets](#core_widgets) liefern meist ein `JPanel` für Swing und ein `org.eclipse.swt.widget.Composite` für Swt, aber auch hier gibt es Ausnahmen. 

Man erhält als UI Referenz immer das native Root Widget, welches von der Spi für die Erzeugung des Widget angelegt wurde. Dadurch ist immer eindeutig, wo die native Widget Hierarchie für das Jo Widget beginnt. Für die Swing SPI Implementierung wäre es also nicht zulässig, die `JTable` als UI Referenz zurückzugeben. Will man eine Referenz auf die zugehörige `JTable` haben, kann man diese aus dem `JScrollPane` herausholen. 









### Verwendung der nativen UI Referenz unter Swt

Das [JoToSwtSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.swt/src/main/java/org/jowidgets/examples/swt/snipped/JoToSwtSnipped.java) demonstriert die Verwendung der nativen Swt UI Referenz in einer jowidgets Applikation: 

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.examples.swt.snipped;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class JoToSwtSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {
        //create the root frame
        final IFrame frame = Toolkit.createRootFrame(BPF.frame("JoToSwt Snipped"), lifecycle);
        frame.setSize(1024, 768);
        frame.setLayout(FillLayout.get());

        //create a regular jo composite
        final IComposite joComposite = frame.add(BPF.composite());

        //get the native ui reference which must be a swt composite
        //because swt SPI impl is used
        final Composite swtComposite = (Composite) joComposite.getUiReference();
        swtComposite.setLayout(new org.eclipse.swt.layout.FillLayout());

        //create a swt browser
        final Browser browser = new Browser(swtComposite, SWT.NONE);
        browser.setUrl("http://www.jowidgets.org/");

        //set the root frame visible
        frame.setVisible(true);
    }

    public static void main(final String[] args) throws Exception {
        Toolkit.getApplicationRunner().run(new JoToSwtSnipped());
        System.exit(0);
    }
}
~~~




### Verwendung der nativen UI Referenz unter Swing

Das [JoToSwingSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.swing/src/main/java/org/jowidgets/examples/swing/snipped/JoToSwingSnipped.java) demonstriert die Verwendung der nativen Swing UI Referenz in einer jowidgets Applikation: 

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.examples.swing.snipped;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class JoToSwingSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {
        //create the root frame
        final IFrame frame = Toolkit.createRootFrame(BPF.frame("JoToSwing Snipped"), lifecycle);
        frame.setSize(800, 600);
        frame.setLayout(FillLayout.get());

        //create a regular jo composite
        final IComposite joComposite = frame.add(BPF.composite());

        //get the native ui reference which must be a JPanel
        //because swing SPI impl is used
        final JPanel panel = (JPanel) joComposite.getUiReference();
        panel.setLayout(new BorderLayout());

        //create a JTextPane and add it to the panel
        final JTextPane textPane = new JTextPane();
        panel.add(textPane);

        //set the root frame visible
        frame.setVisible(true);
    }

    public static void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        Toolkit.getApplicationRunner().run(new JoToSwingSnipped());
        System.exit(0);
    }
}
~~~










### Kapseln eines nativen Widgets durch eine Jo Widget Schnittstelle{#create_api_for_native_widget}

Man definiert zum Beispiel für das komplexe native Widget W_SWT, welches man in sein Modul X einbetten möchte, eine Widgets Schnittstelle (API_Y) inklusive Setup, bzw. BluePrint wie in [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) gezeigt. 

Gegen diese API_Y kann man dann im Modul X implementieren, ohne Abhängigkeiten auf die native UI Technologie SWT haben zu müssen. 

Man erstellt zusätzlich für die API_Y ein Impl_Y_SWT Modul, welches die nativen UI Abhängigkeiten beinhalten, und die Widget Schnittstelle mit Hilfe von Widget W_SWT adaptiert. 

Will man das Modul X später mit anderen UI Technologien verwenden, muss man nur eine weitere Implementierung z.B. Impl_Y_SWING für API_Y anbieten, ohne dass man Modul X nochmal anfassen muss. 

Je öfter man das native Widget W_SWT einbetten möchte, desto mehr rechtfertigt dies das im ersten Schritte aufwändigere Vorgehen. 

Will man beispielsweise für das native Widget W_SWT von vornherein für Swing und Swt eine Implementierung haben haben, ohne Wigdet W_SWT neu mit Swing zu Implementieren, kann man dazu auch die [Swt-Awt Bridge Widgets](#swt_awt_bridge_widgets) verwenden. 



#### Das Addon Browser Widget

Das Browser Addon Widget `IBrowser` kann dabei als Beispiel dienen. Es besteht aus vier Modulen:

* __org.jowidgets.addons.widgets.browser.api__ beinhaltet die API für ein Browser Widget

* __org.jowidgets.addons.widgets.browser.impl.swt.common__ beinhalten eine Implementierung, welche die Browser Widget Schnittstellen der API mit Hilfe des nativen Swt Browser implementiert.

* __org.jowidgets.addons.widgets.browser.impl.swt__ beinhaltet einen Toolkit Interceptor, welcher das Browser Widget aus dem `...impl.swt.common` Modul in der Generic Widget Factory registriert. 

* __org.jowidgets.addons.widgets.browser.impl.swing__ beinhaltet einen Toolkit Interceptor, welcher das Browser Widget aus dem `...impl.swt.common` Modul mit Hilfe des [AwtSwtControl](#awt_swt_control) nach Swing _bridged_ und es in der Generic Widget Factory registriert. 


Der folgende Code zeigt die `BrowserFactory` aus `org.jowidgets.addons.widgets.browser.impl.swt`:

~~~{.java .numberLines startFrom="1"} 
final class BrowserFactory implements IWidgetFactory<IBrowser, IBrowserBluePrint> {

    @Override
    public IBrowser create(final Object parentUiReference, final IBrowserBluePrint bluePrint) {
        final IComposite composite = Toolkit.getWidgetFactory().create(
			parentUiReference,
			BPF.composite());
        return SwtBrowserFactory.createBrowser(
			composite, 
			(Composite) composite.getUiReference(), 
			bluePrint);
    }
}
~~~

Der folgende Code zeigt die `BrowserFactory` aus `org.jowidgets.addons.widgets.browser.impl.swing`:

~~~{.java .numberLines startFrom="1"} 
final class BrowserFactory implements IWidgetFactory<IBrowser, IBrowserBluePrint> {

    @Override
    public IBrowser create(final Object parentUiReference, final IBrowserBluePrint bluePrint) {
        final IAwtSwtControl awtSwtControl 
			= AwtSwtControlFactory.getInstance().createAwtSwtControl(parentUiReference);
        return SwtBrowserFactory.createBrowser(
			awtSwtControl, 
			awtSwtControl.getSwtComposite(), 
			bluePrint);
    }

}
~~~

In beiden Fällen wird das Widget wie folgt mittels eines `IToolkitInterceptor` registriert:

~~~{.java .numberLines startFrom="1"} 
final class BrowserToolkitInterceptor implements IToolkitInterceptor {

    @Override
    public void onToolkitCreate(final IToolkit toolkit) {
        final IGenericWidgetFactory widgetFactory = toolkit.getWidgetFactory();
        widgetFactory.register(IBrowserBluePrint.class, new BrowserFactory());
        toolkit.getBluePrintProxyFactory().addDefaultsInitializer(
			IBrowserSetupBuilder.class, 
			new BrowserDefaults());
    }
}
~~~




#### Definition der Schnittstelle bei Verwendung der SWT - AWT Bridge

Wenn man ein natives Widget mit Hilfe der [Swt-Awt Bridge Widgets](#swt_awt_bridge_widgets) integrieren möchte, sollte man bei der Definition der Schnittstelle darauf achten, den Anteil, der _gebridget_ wird, möglichst __minmal__ zu halten. Dies soll an einem Beispiel verdeutlicht werden: 

In einem SWT Projekt soll der legacy Text Editor `MyEditorPanel`, der in Swing Implementiert ist, verwendet werden. Das `MyEditorPanel` hat eine Toolbar, mit der bestimmte Aktion ausgeführt werden können, wie zum Beispiel dem Ändern der Textfarbe oder Schriftart. Der _komplexe_ Anteil der Implementierung steckt jedoch `MyEditorTextPane`, welches das `JTextPane` erweitert, und komplexe Textdekorierungen umsetzt. Dann wäre es eventuell sinnvoll, nur für das `MyEditorTextPane` die Jo Widget Schnittstelle `IMyEditorTextPane` zu definieren und für die Implementierung die [SwtAwtBridge](#swt_awt_control) zu verwenden. Anschließend könnte man die Schnittstelle `IMyEditorPanel` definieren, wodurch der Toolbar Aspekt und eventuell weitere hinzukommen. Dieses Widget müsste man dann nur ein Mal implementieren und könnte dafür die Schnittstelle `IMyEditorTextPane` verwenden, wodurch man dann insbesondere keine Abhängigkeiten zu Swing hat. Dieses Vorgehen hat zwei Vorteile:

1. Die Toolbar wir nicht _gebridged_. Das bedeutet, die Toolbar Buttons sind unter Swing JButtons und unter Swt SwtButtons. Ein `JTextPane` sieht unter Swt weniger wie ein Fremdkörper aus, als zum Beispiel ein JButton oder eine JComboBox.

2. Will man später auf die Bridge verzichten, und die Funktion nativ für Swt portieren, muss man nur das `MyEditorTextPane` portieren, und nicht das `MyEditorPanel` mit der Toolbar und evetuell noch weiteren, UI unabhängigen Funktionen. Zieht man sowieso eine spätere Portierung in Betracht, kann man auch gleich _richtig_ schneiden.









### Erweitern einer existierenden Jo Widget Schnittstelle um nativ verfügbare Funktionen{#extend_jo_interface_for_nativ_available_methods}

Wenn man eine vorhandene Widget Schnittstelle erweitern möchte, gibt es folgende Möglichkeiten:

* Eventuell ist die Erweiterung auch für alle anderen SPI Implementierungen möglich und sinnvoll
	* Man führt selbst die Erweiterung durch und stellt den resultierenden _Patch_ zur Verfügung, damit er integriert werden kann.^[Vorab abzustimmen was man vor hat, kann dabei nichts schaden]
	* Man wünscht sich die Erweiterung, indem man einen Issue erstellt^[Oder man beauftragt eine Erweiterung, was unter Umständen die Bearbeitungszeit verkürzt.]
* Eventuell ist die Erweiterung nur für bestimmte SPI Implementierungen oder Betriebssysteme möglich und sinnvoll
	* Man verfährt wie beim vorigen Punkt, nur dass das Widget als [Addon Widgets](#addon_widget) entworfen wird, und somit nicht zum Kern gehört. Zudem muss man prüfen, ob man von der vorhandenen Schnittstelle ableitet, oder ob man eine neue Schnittstelle definiert.
* Eventuell kann (darf) oder will man die Erweiterung nicht veröffentlichen.
	* Man verfährt wie oben und fügt das Widget einer passenden oder neuen firmeninternen [Widget Bibliothek](#custom_widget_libraries) hinzu.
	
	
	
	
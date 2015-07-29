## Jowidgets Code in native Projekte integrieren {#integrate_jowidgets_in_native_code}

Möchte man jowidgets in einem bereits existierenden Projekt verwenden, in welchem die Applikation nicht durch einen [Application Runner](#application_runner) erzeugt wird, bzw. das Root Fenster kein `IFrame` ist, benötigt man entweder an der Stelle, wo jowidgets eingebunden werden soll, einen [nativen Wrapper](#jowidgets_wrapper_factory), oder man erzeugt ein komplettes jowidgets [Root-](#create_root_window_in_native_app), oder [Kind Fenster](#create_child_window_in_native_app) innerhalb der nativen Applikation. 

In beiden Fällen wird vorausgesetzt, dass für die UI Technologie, welche im nativen Projekt verwendet wird, eine SPI Implementierung von jowidgets existiert, was derzeit für Swing, Swt und Rwt der Fall ist. ^[Für JavaFx existiert eine [prototypische Implementierung](https://github.com/jo-source/jo-fx).] Eine Abhängigkeit auf die SPI Implementierung muss spätestens zur Laufzeit vorhanden sein. Verwendet man die Addon Module `org.jowidgets.spi.impl.swt.addons` oder `org.jowidgets.spi.impl.swing.addons` hat man diese Abhängigkeit bereits transitiv zur Compilezeit. 

Der Aspekt des _Mischens_ von unterschiedlichen nativen UI Technologien, wie zum Beispiel Swing Widgets in einer Swt Applikation, wird nicht hier, sondern wird in [Swt-Awt Bridge Widgets](#swt_awt_bridge_widgets) behandelt.




### Erzeugen von Fenstern

In bestimmten Anwendungsfällen mochte man eventuell ein komplettes jowidgets Root- oder Kind Fenster erzeugen. 

#### Erzeugen eines Root Fensters{#create_root_window_in_native_app}

Ein Root Fenster kann mit Hilfe des Toolkit wie folgt erzeugt werden: 

~~~
	final IFrame frame = Toolkit.createRootFrame(BPF.frame("Jowidgets Root Frame"));
~~~

#### Erzeugen eines Kind Fensters{#create_child_window_in_native_app}

Ein Kind Fenster kann mit Hilfe der GenericWidgetFactory wie folgt erzeugt werden: 

~~~
	final IDialogBluePrint dialogBp = BPF.dialog("Jowidgets dialog");
	final IFrame dialog = Toolkit.getWidgetFactory().create(nativeUiReference, dialogBp);
~~~

Die Ui Referenz ist dabei die Referenz des nativen Vaterfensters, also zum Beispiel eine Shell oder ein JFrame.











### Jowidgets Wrapper Factory{#jowidgets_wrapper_factory}

Eine SPI Implementierung kann für ein IFrame und ein IComposite Wrapper zur Verfügung stellen. Diese können mit Hilfe der `IWidgetWrapperFactory` erzeugt werden, welche man mit Hilfe der folgenden Methode vom Toolkit erhält:

~~~
	IWidgetWrapperFactory getWidgetWrapperFactory();
~~~

Die Schnittstelle `IWidgetWrapperFactory` hat die folgenden Methoden:

~~~
	boolean isConvertibleToFrame(final Object uiReference);

	IFrame createFrame(final Object uiReference);

	boolean isConvertibleToComposite(final Object uiReference);

	IComposite createComposite(final Object uiReference);
~~~

Mit Hilfe der Methoden `isConvertibleTo...()` kann geprüft werden, ob für ein natives Widget ein Wrapper für ein IFrame oder ein IComposite erzeugt werden kann. Die create() Methoden erzeugen einen konkreten Wrapper.

__Achtung:__ Die folgenden Punkte sollten dabei unbedingt beachtet werden:

* Wenn man ein IFrame oder ein IComposite aus einem nativen Widget wie zum Beispiel einem `JFrame` (Swing), `Shell` (swt), `JPanel` (Swing), `Composite` (Swt) erzeugt, dann wird auf dem Wrapper __nicht__ automatisch dispose aufgerufen, wenn das native Widget disposed wird. Das liegt daran, dass es nicht für jedes UI Framework ein Dispose Konzept gibt. Während ein Swt Composite disposed wird, sobald man es aus seinem Container entfernt, kann ein JPanel beliebig wiederverwendet werden, nachdem man es aus seinem Container entfernt hat. Es liegt daher in der Verantwortung des Entwicklers zu entscheiden, ob und wann der Wrapper disposed werden soll. Die SPI Implementierung für SWT liefert jedoch im Addon Modul `org.jowidgets.spi.impl.swt.addons` die Utility Klasse `SwtToJoWrapper`, welche genau dieses Problem für den Standardfall löst.

* Ein Jowidgets Wrapper Widget hat keinen Parent, wenngleich einer gesetzt werden kann.

* Man sollte auf der UI Referenz, die man _wrapped_ , keine zusätzlichen modifizierenden nativen Operation wie zu Beispiels dem Hinzufügen oder Entfernen von Kind Controls durchführen. Stattdessen sollte man eine __eigene__ UI Referenz, welche ausschließlich dem Wrappen dient, erzeugen, eventuell durch das Verschachteln von JPanel's oder Composite's.



















### Jowidgets Code in Swt / RCP Projekte integrieren{#integrate_jo_in_swt}

Es folgen spezielle Informationen und Beispiele für die Integration von jowidgets Code in Projekte, welche auf dem Standard Widget Toolkit (Swt) basieren.



#### SwtToJoFramesSnipped

Das [SwtToJoFramesSnipped](https://github.com/jo-source/jo-widgets/tree/master/modules/examples/org.jowidgets.examples.swt/src/main/java/org/jowidgets/examples/swt/snipped/SwtToJoFramesSnipped.java) demonstriert die Erzeugung von Fenstern innerhalb einer nativen SWT Applikation: 

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.examples.swt.snipped;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class SwtToJoFramesSnipped {

	private SwtToJoFramesSnipped() {}

	public static void main(final String[] args) throws Exception {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setSize(1024, 768);
		shell.setLayout(new MigLayout("", "[]", "[][]"));

		//add button to open a jowidgets root frame
		final Button frameButton = new Button(shell, SWT.NONE);
		frameButton.setText("Create root frame");
		frameButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final IFrameBluePrint frameBp = BPF.frame("Jowidgets Root Frame");
				frameBp.setSize(new Dimension(400, 300));
				frameBp.setAutoDispose(true);
				final IFrame frame = Toolkit.createRootFrame(frameBp);
				frame.add(BPF.textLabel("This is a jowidgets root frame"));
				frame.setVisible(true);
			}

		});

		//add button to open a jowidgets modal dialog
		final Button dialogButton = new Button(shell, SWT.NONE);
		dialogButton.setText("Create dialog");
		dialogButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final IDialogBluePrint dialogBp = BPF.dialog("Jowidgets dialog");
				dialogBp.setSize(new Dimension(400, 300));
				final IFrame dialog = Toolkit.getWidgetFactory().create(shell, dialogBp);
				dialog.add(BPF.textLabel("This is a jowidgets modal dialog"));
				dialog.setVisible(true);
			}

		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
~~~






#### SwtToJoWrapper

Um für ein Swt `Composite` ein `IComposite` Wrapper zu erzeugen, kann man die Utility Klasse `SwtToJoWrapper` verwenden, welche sich im Modul `org.jowidgets.spi.impl.swt.addons` befindet. Der folgende Code zeigt einen Teil der Implementierung:

~~~{.java .numberLines startFrom="1"} 
public final class SwtToJoWrapper {

	private SwtToJoWrapper() {}

	public static IComposite create(final Composite composite) {
		Assert.paramNotNull(composite, "composite");
		final IComposite result = Toolkit.getWidgetWrapperFactory().createComposite(composite);
		composite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				result.dispose();
			}
		});
		return result;
	}

}
~~~

In Zeile 8 wird auf dem SWT Composite ein `DisposeListener` hinzugefügt, der das`IComposite` Wrapper disposed, sobald das SWT Composite disposed wird. Dies ist im Normalfall genau da gewünschte Verhalten.

Das folgende Beispiel demonstriert die Verwendung:

~~~{.java .numberLines startFrom="1"} 
	final IComposite joComposite = SwtToJoWrapper.create(swtComposite);
~~~

#### SwtToJoCompositeSnipped

Das [SwtToJoCompositeSnipped](https://github.com/jo-source/jo-widgets/tree/master/modules/examples/org.jowidgets.examples.swt/src/main/java/org/jowidgets/examples/swt/snipped/SwtToJoCompositeSnipped.java) demonstriert die Erzeugung eines `IComposite` Wrapper innerhalb einer nativen SWT Applikation: 

~~~{.java .numberLines startFrom="1"} 
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.swt.addons.SwtToJoWrapper;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class SwtToJoCompositeSnipped {

	private SwtToJoCompositeSnipped() {}

	public static void main(final String[] args) throws Exception {
		//create a swt display
		final Display display = new Display();

		//create a swt shell
		final Shell shell = new Shell(display);
		shell.setSize(400, 300);
		shell.setText("SwtToJo composite snipped");
		shell.setLayout(new FillLayout());

		//create a swt composite
		final Composite swtComposite = new Composite(shell, SWT.NONE);

		//create a jowidgets composite wrapper and do some jowidgets stuff
		final IComposite joComposite = SwtToJoWrapper.create(swtComposite);
		joComposite.setLayout(new MigLayoutDescriptor("[][grow]", "[]"));

		//add a label
		joComposite.add(BPF.textLabel("Name"));

		//add a input field for double values
		final IInputField<String> nameField = joComposite.add(BPF.inputFieldString(), "grow x");
		nameField.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				//CHECKSTYLE:OFF
				System.out.println("Hello " + nameField.getValue());
				//CHECKSTYLE:ON
			}
		});

		//open the swt shell and start event dispatching
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
~~~ 





### Jowidgets Code in Swing Projekte integrieren{#integrate_jo_in_swing}

Die Integration in Swing funktioniert analog zu SWT. Es folgen die gleichen Beispiele wie für SWT jetzt für Swing.


#### SwingToJoFramesSnipped

Das [SwingToJoFramesSnipped](https://github.com/jo-source/jo-widgets/tree/master/modules/examples/org.jowidgets.examples.swing/src/main/java/org/jowidgets/examples/swing/snipped/SwingToJoFramesSnipped.java) demonstriert die Erzeugung von Fenstern innerhalb einer nativen Swing Applikation: 

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.examples.swing.snipped;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class SwingToJoFramesSnipped {

	private SwingToJoFramesSnipped() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowJFrame();
			}
		});
	}

	private static void createAndShowJFrame() {
		//create the root frame with swing
		final JFrame frame = new JFrame();
		frame.setSize(1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new MigLayout("", "[]", "[][]"));

		//add button to open a jowidgets root frame
		final JButton frameButton = new JButton("Create root frame");
		frame.add(frameButton);
		frameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final IFrameBluePrint frameBp = BPF.frame("Jowidgets Root Frame");
				frameBp.setSize(new Dimension(400, 300));
				frameBp.setAutoDispose(true);
				final IFrame frame = Toolkit.createRootFrame(frameBp);
				frame.add(BPF.textLabel("This is a jowidgets root frame"));
				frame.setVisible(true);
			}
		});

		//add button to open a jowidgets modal dialog
		final JButton dialogButton = new JButton("Create dialog");
		frame.add(dialogButton);
		dialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final IDialogBluePrint dialogBp = BPF.dialog("Jowidgets dialog");
				dialogBp.setSize(new Dimension(400, 300));
				final IFrame dialog = Toolkit.getWidgetFactory().create(frame, dialogBp);
				dialog.add(BPF.textLabel("This is a jowidgets modal dialog"));
				dialog.setVisible(true);
			}
		});

		//show the frame
		frame.setVisible(true);
	}

}
~~~

#### SwingToJoCompositeSnipped

Das [SwingToJoCompositeSnipped](https://github.com/jo-source/jo-widgets/tree/master/modules/examples/org.jowidgets.examples.swing/src/main/java/org/jowidgets/examples/swing/snipped/SwingToJoCompositeSnipped.java) demonstriert die Erzeugung eines `IComposite` Wrapper innerhalb einer nativen Swing Applikation: 

~~~{.java .numberLines startFrom="1"} 
package org.jowidgets.examples.swing.snipped;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.swing.addons.SwingToJoWrapper;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class SwingToJoCompositeSnipped {

	private SwingToJoCompositeSnipped() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowJFrame();
			}
		});
	}

	private static void createAndShowJFrame() {
		//create the root frame with swing
		final JFrame frame = new JFrame("SwingToJo composite snipped");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new MigLayout("", "0[grow]0", "0[grow]0"));

		//create a jpanel
		final JPanel panel = new JPanel();
		frame.add(panel, "growx, growy");

		//create a jowidgets composite wrapper and do some jowidgets stuff
		final IComposite joComposite = SwingToJoWrapper.create(panel);
		joComposite.setLayout(new MigLayoutDescriptor("[][grow]", "[]"));

		//add a label
		joComposite.add(BPF.textLabel("Name"));

		//add a input field for double values
		final IInputField<String> nameField = joComposite.add(BPF.inputFieldString(), "grow x");
		nameField.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				//CHECKSTYLE:OFF
				System.out.println("Hello " + nameField.getValue());
				//CHECKSTYLE:ON
			}
		});

		//show the frame
		frame.setVisible(true);
	}

}
~~~ 






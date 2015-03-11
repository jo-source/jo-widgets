## Actions und Commands{#actions_and_commands}

Eine `IAction` dient dazu, eine Nutzeraktion zu kapseln. Dabei soll sowohl der Action Code als eine Action Instanz wiederverwendet werden können. Actions können zu einem [Menu](#menu_interface) hinzugefügt werden oder an ein [Button](#button_widget) oder [Toolbar Button](#tool_bar_button) gebunden werden.






### Die Schnittstelle IAction{#action_interface}

Die Schnittstelle [`IAction`](#action_interface) liefert die Methoden, welche jowidgets benötigt, um Aktionen anzuzeigen, zu binden und auszuführen. Sie stellt somit in gewisser Weise die SPI^[Service Provider Interface] für Aktionen dar. Wenn man [Command Actions](#command_action_interface) verwendet, muss man diese Schnittstelle nicht selbst implementieren. Dennoch sollen die Methoden zum besseren Verständnis kurz beschrieben werden:

#### Action Description

Die folgenden Methoden liefern die beschreibenden Attribute der Action für die Anzeige und das Key Binding: 

~~~
	String getText();

	String getToolTipText();

	IImageConstant getIcon();

	Character getMnemonic();

	Accelerator getAccelerator();
~~~

#### Enabled State

Die folgende Methode liefert den `enabled` State. Ein Action, welche nicht `enabled` ist, kann nicht ausgeführt werden und wird _ausgegraut_ dargestellt.

~~~
	boolean isEnabled();
~~~

#### Execute

Die folgende Methode wird aufgerufen, wenn die Action ausgeführt werden soll:

~~~
	void execute(IExecutionContext executionContext) throws Exception;
~~~

#### Exception Handler

Eine `IAction` Implementierung kann einen `IExceptionHandler` liefern:

~~~
	IExceptionHandler getExceptionHandler();
~~~

Dieser wird verwendet, wenn bei der `execute()` Methode eine Exception auftritt. Wird `null` zurückgegeben, wird der `UncaughtExceptionHandler` des aktuellen Threads aufgerufen.


#### Change Observable

Wird eine Action _mutable_ entworfen, was bedeutet, dass sich die Attribute `text`, `tooltipText`, `icon` oder `enabled` State zur Laufzeit ändern können, dann liefert die folgenden Methode ein `IActionObservable` zurück, auf dem man sich als Listener über Änderungen informieren kann:

~~~
	IActionChangeObservable getActionChangeObservable();
~~~

Ist die Action _immutable_ entworfen, kann die Implementierung `null` zurückliefern. Die Schnittstelle `IActionObservable` hat die folgenden Methoden:

~~~
	void addActionChangeListener(IActionChangeListener listener);

	void removeActionChangeListener(IActionChangeListener listener);
~~~

Ein `IActionChangeListener` hat die folgenden Methoden:

~~~
	void textChanged();

	void toolTipTextChanged();

	void iconChanged();

	void enabledChanged();
~~~

Der Accelerator Key und Mnemonic Key sind somit immer _immutable_ zu entwerfen. Nur die oben angegebenen Attribute können sich zur Laufzeit ändern.



### Die Schnittstelle ICommand{#command_interface}

Eine [Command Action](#command_action_interface) teilt eine Action in den beschreiben Anteil wie Label Text, Tooltip Text oder Icon, und die Business Logik auf. Ein `ICommand` stellt dabei die Business Logik dar. Diese hat drei Aspekte:

* __Enabled Checking__ prüft, ob die Aktion ausführbar ist. 

* __Execution__ führt die eigentliche Aktion aus

* __Exception Handling__ behandelt Ausnahmen

Die Schnittstelle `ICommand` wird häufig selbst implementiert, um die Business Logik abzubilden. Diese hat die folgenden Methoden:

~~~
	ICommandExecutor getCommandExecutor();

	IExceptionHandler getExceptionHandler();
	
	IEnabledChecker getEnabledChecker();
~~~

Dabei kann jede der Methoden `null` zurückliefern, wenn der jeweilige Aspekt nicht unterstützt wird (einen Enabled Checker oder Exception Handler anzubieten, ohne einen Executor zu haben ist allerdings nicht unbedingt sinnvoll).

#### Die Schnittstelle ICommandExecutor 

Die Schnittstelle `ICommandExecutor` hat die folgende Methode:

~~~
	void execute(IExecutionContext executionContext) throws Exception;
~~~

Diese wird aufgerufen, wenn der Command ausgeführt werden soll. Die Schnittstelle `IExecutionContext` liefert dabei die folgenden Methoden:

~~~
	IAction getAction();
	
	IWidget getSource();
	
	<VALUE_TYPE> VALUE_TYPE getValue(final ITypedKey<VALUE_TYPE> key);
~~~

Damit kann man eine Referenz auf die auslösende Action, das Widget, für welches die Action ausgeführt wurde, und weitere Properties erhalten. Über die ausführende Action kann man sich Beispielsweise das Action Label und Icon beschaffen, um diese etwa in einem Dialog anzuzeigen. Das folgende Beispiel soll das verdeutlichen:

~~~{.java .numberLines startFrom="1"}
public final class ExampleExecutor implements ICommandExecutor {

	@Override
	public void execute(final IExecutionContext executionContext) throws Exception {
		final IAction action = executionContext.getAction();
		final String title = action.getText();
		final IImageConstant icon = action.getIcon();
		final String message = "Execution was successful";
		MessagePane.showInfo(title, icon, message);
	}

}
~~~

Das dies ein häufiger Anwendungsfall ist, unterstützt die Accessor Klasse `MessagePane` auch das direkte Übergeben des `executionContext`. Die folgende Implementierung hat daher den gleichen Effekt:

~~~{.java .numberLines startFrom="1"}
public final class ExampleExecutor implements ICommandExecutor {

	@Override
	public void execute(final IExecutionContext executionContext) throws Exception {
		final String message = "Execution was successful";
		MessagePane.showInfo(executionContext, message);
	}

}
~~~

#### Die Schnittstelle IExceptionHandler

Die Schnittstelle `IExceptionHandler` hat die folgende Methode:

~~~
	void handleException(
		IExecutionContext executionContext, 
		final Exception exception) throws Exception;
~~~

Auch hier wird der `IExecutionContext` der auslösenden Action übergeben. Zudem bekommt man die aufgetretene Exception übergeben. Wenn man diese nicht selbst  behandeln kann, kann man sie erneut werfen. Sie wird dann als nächstes vom Exception Handler der [Action](#action_interface) behandelt, falls ein solcher existiert.  

#### Die Schnittstelle IEnabledChecker{#enabled_checker_interface}

Die Schnittstelle `IEnabledChecker` liefert die folgenden Methoden:

~~~
	IEnabledState getEnabledState();
	
	void addChangeListener(IChangeListener listener);

	void removeChangeListener(IChangeListener listener);
~~~

Die Methode `getEnabledState()` liefert die Information, ob die Aktion ausführbar ist. Immer wenn sich der EnabledState ändert, müssen die registrierten Listener darüber informiert werden.

Die Schnittstelle `IEnabledState` hat die folgenden Methoden:

~~~
	boolean isEnabled();

	String getReason();
~~~

Das bedeutet, es wird nicht nur die Information geliefert, ob eine Aktion ausführbar ist, sondern auch der Grund warum nicht. Die Methode `getReason()` sollte einen internationalisierten String zurückliefern, welche dem Nutzer Auskunft darüber gibt, warum die Aktion nicht ausführbar ist. Beispiele sind:

* __Speichern__ - "Es gibt keine Änderungen"
* __Speichern__ - "Es existiert bereits ein Datensatz mit gleicher Artikelnummer"
* __Undo__ - "Es gibt keine Änderungen"
* __Nachricht versenden__ - "Die Nachricht hat keinen Betreff"
* __Löschen__ - "Fehlendes Recht"

Es ist jedoch auch erlaubt `null` oder einen `Leerstring` zurück zu liefern. 

Die Default Implementierung der [Command Action](#command_action_interface) __tauscht__, wenn der `EnabledState` disabled und der `reason` nicht leer ist, __das Tooltip__ des gebundenen MenuItem, Button oder ToolbarButton __gegen den `reason` Text aus__. 

Dies hat sich in großen und komplexen Enterprise Anwendungen als äußerst nützlich herausgestellt und wurde von Kunden mehrfach gelobt. So wurde sogar die Aussage getätigt, das man dieses Feature in vielen anderen Applikationen vermisse, seit dem man mit dieser Applikation arbeiten würde. 

Wenn man bei Google den Text _"why is that button greyed out in"_ eingibt, liefert einem die Autovervollständigung unzählige Fortführungen dieses Satzes, woraus sich vermuten lässt, dass sich Nutzer diese Frage häufig zu stellen scheinen. Da der Entwickler den Grund für das Ausgrauen in der Regel kennt, wäre es doch auch hilfreich, diesen an den Nutzer weiter zu geben, um die Usability zu erhöhen.

Die Klasse `EnabledState` liefert folgende statische Methode zur Erzeugung eines `disabled` State:

~~~
	public static EnabledState disabled(final String reason) {...}
~~~

Sowie die folgenden Konstanten:

~~~
	public static final EnabledState ENABLED = new EnabledState();

	public static final EnabledState DISABLED = new EnabledState(false, null);
~~~

Das folgende Beispiel demonstriert die Implementierung eines `IEnabledChecker`:

~~~{.java .numberLines startFrom="1"}
public final class ModifiedEnabledChecker extends AbstractEnabledChecker {

	private final IInputComponent<?> inputComponent;

	private ModifiedEnabledChecker(final IInputComponent<?> inputComponent) {
		this.inputComponent = inputComponent;

		inputComponent.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				fireEnabledStateChanged();
			}
		});
	}

	@Override
	public IEnabledState getEnabledState() {
		if (!inputComponent.hasModifications()) {
			return EnabledState.disabled("There is no modification");
		}
		else {
			return EnabledState.ENABLED;
		}
	}

}
~~~

Dieser erlaubt die Ausführung nur, wenn die referenzierte [Input Component](#input_component_interface) Modifikationen hat. Für die Implementierung wird von der abstrakten Klasse `AbstractEnabledChecker` abgeleitet. Dadurch spart man sich die Implementierung Methoden `addChangeListener()` und `removeChangeListener()`. Um Änderungen anzuzeigen muss nur die Methode `fireEnabledStateChanged()` aufgerufen werden (Zeile 11).

Mit Hilfe der Klasse `EnabledChecker` könnte man das gleiche wie oben auch so erreichen:

~~~{.java .numberLines startFrom="1"}
	final EnabledChecker enabledChecker = new EnabledChecker();

	inputComponent.addInputListener(new IInputListener() {
		@Override
		public void inputChanged() {
			if (!inputComponent.hasModifications()) {
				enabledChecker.setDisabled("There is no modification");
			}
			else {
				enabledChecker.setEnabled();
			}
		}
	});
~~~

Je nach Anwendungsfall kann die eine oder andere Variante besser geeignet sein.














### Die Schnittstelle ICommandAction{#command_action_interface}

Die Schnittstelle `ICommandAction` erweitert die Schnittstelle [`IAction`](#action_interface) um den Aspekt von [Commands](#command_interface). Jowidgets liefert dafür eine Defaultimplementierung, so dass die Schnittstelle `ICommandAction` normalerweise nicht selbst implementiert wird, sondern nur die zugehörigen [Commands](#command_interface). Es folgt eine Beschreibung der zusätzlich zu [`IAction`](#action_interface) vorhandenen Methoden:

#### Descriptor Methoden

Mit Hilfe der folgenden Methoden können `text`, `tooltipText` und `icon` geändert werden. 

~~~
	void setText(String text);

	void setToolTipText(final String toolTipText);

	void setIcon(IImageConstant icon);
~~~
 
#### Enabled State

Mit der folgenden Methode kann der _gloable_ `enabled` State geändert werden:

~~~
	void setEnabled(boolean enabled);
~~~

Wird ein [`IEnabledChecker`](#enabled_checker_interface) verwendet, wird dieser nur ausgewertet, wenn der globale `enabled` State `true` ist. 

#### Setzen, Ändern und Auslesen des Command

Mit Hilfe der folgenden Methoden kann der Command gesetzt und geändert werden:

~~~
	void setCommand(ICommand command);

	void setCommand(ICommandExecutor executor);

	void setCommand(ICommandExecutor executor, IEnabledChecker enabledChecker);

	void setCommand(ICommandExecutor executor, IExceptionHandler exceptionHandler);

	void setCommand(ICommandExecutor executor, IEnabledChecker enabledChecker, IExceptionHandler exceptionHandler);
~~~

Die erste Methode verwendet dazu die [`ICommand`](#command_interface) Schnittstelle, die anderen Methoden erlauben das Setzen eines Command mit Hilfe der einzelnen Aspekte (Executor, EnabledChecker, ExceptionHandler). Dabei werden immer alle Aspekte neu gesetzt (die nicht angegebenen werden Aspkete werden zu `null`). Ein Command kann explizit auf `null` gesetzt werden. Die Action wird dadurch automatisch `disabled`.

Die folgende Methode liefert das derzeit gesetzte Command:

~~~
	ICommand getCommand();
~~~

#### Exception Handling

Für alle Exceptions, welche nicht durch den Command behandelt wurden, kann ein Action ExceptionHandler wie folgt gesetzt werden:

~~~
	void setActionExceptionHandler(IExceptionHandler exceptionHandler);
~~~


#### Action Builder

Um eine Implementierung der Schnittstelle ICommandAction zu erhalten, kann ein `IActionBuilder` verwendet werden. Diesen erhält man u.A. von der Accessor Klasse `org.jowidgets.api.command.Action` mit Hilfe der statischen Methode:

~~~
	public static IActionBuilder builder() {...}
~~~

Die Schnittstelle `IActionBuilder` hat die folgenden Methoden:

~~~
	IActionBuilder setText(String text);

	IActionBuilder setToolTipText(String toolTipText);

	IActionBuilder setIcon(IImageConstant icon);

	IActionBuilder setMnemonic(Character mnemonic);

	IActionBuilder setMnemonic(char mnemonic);

	IActionBuilder setAccelerator(Accelerator accelerator);

	IActionBuilder setAccelerator(char key, Modifier... modifier);

	IActionBuilder setAccelerator(VirtualKey virtualKey, Modifier... modifier);

	IActionBuilder setEnabled(boolean enabled);

	IActionBuilder setCommand(ICommand command);

	IActionBuilder setCommand(ICommandExecutor command);

	IActionBuilder setCommand(ICommandExecutor command, IEnabledChecker executableStateChecker);

	IActionBuilder setCommand(ICommandExecutor command, IExceptionHandler exceptionHandler);

	IActionBuilder setCommand(ICommandExecutor command, IEnabledChecker enabledChecker, IExceptionHandler exceptionHandler);

	IActionBuilder setActionExceptionHandler(IExceptionHandler exceptionHandler);

	ICommandAction build();
~~~

Die Parameter haben die gleiche Semantik wie die der Schnittstelle `ICommandAction`.

Das folgende Beispiel zeigt die Verwendung:

~~~{.java .numberLines startFrom="1"}
public final class SaveActionFactory {

	private SaveActionFactory() {}

	public static IAction create(final IDataModel dataModel) {
		final IActionBuilder builder = Action.builder();

		builder.setText("Save");
		builder.setToolTipText("Saves the text");
		builder.setAccelerator(VirtualKey.S, Modifier.CTRL);
		builder.setIcon(IconsSmall.DISK);

		builder.setCommand(SaveCommandFactory.create(dataModel));

		return builder.build();
	}
}
~~~

__Anmerkung:__ Im obigen Beispiel wird bewusst eine `IAction` und keine `ICommandAction` zurückgegeben. Dadurch wird signalisiert, dass die Action nicht durch den Nutzer der Methode nachträglich modifiziert werden soll.


### Command Action Snipped{#command_action_snipped}

Im folgenden soll die Verwendung von Command Actions noch einmal anhand eines Beispiels verdeutlicht werden. Die folgenden Abbildung zeigen vorab fertige Ergebnis:

![Command Action Snipped - Screenshot 1](images/actions_snipped_1.gif "Command Action Snipped - Screenshot 1")

In dem Textfeld wurde der Text _"Hello World"_ durch den Benutzer eingegeben. Dadurch ist der Save Button neben dem Text Feld sowie in der Toolbar aktiviert. Zudem befindet sich die Action auch noch im File Menu (nicht auf dem Screenshot sichtbar). 

Nachdem die Save Action ausgelöst wurde, ist das folgende zu sehen:

![Command Action Snipped - Screenshot 2](images/actions_snipped_2.gif "Command Action Snipped - Screenshot 2")

Ein Dialog erscheint, dass die Aktion ausgeführt wurde. Wird dieser geschlossen und öffnet man das File Menu, sieht man das folgende:

![Command Action Snipped - Screenshot 3](images/actions_snipped_3.gif "Command Action Snipped - Screenshot 3")

Der Tooltip zu der Save Action im File Menü zeigt den Grund für die Deaktivierung im Tooltip. Die Save Action soll genau dann `enabled` sein, wenn der Text im Textfeld sich seit dem letzten Speichern geändert hat. 

Der folgende Code zeigt die Implementierung (das vollständige Snipped inklusive Imports befindet sich [hier](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/CommandActionSnipped.java)):

~~~{.java .numberLines startFrom="1"}
public final class CommandActionSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create a root frame
		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(400, 300)).setTitle("Command actions");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

		//Create the menu bar
		final IMenuBarModel menuBar = frame.getMenuBarModel();

		//Use a border layout
		frame.setLayout(BorderLayout.builder().gap(0).build());

		//add a toolbar to the top
		final IToolBarModel toolBar = frame.add(BPF.toolBar(), BorderLayout.TOP).getModel();

		//add a composite to the center
		final IComposite composite = frame.add(BPF.composite().setBorder(), BorderLayout.CENTER);
		composite.setLayout(new MigLayoutDescriptor("[grow][]", "[]"));

		//add a input field and save button to the composite
		final IInputField<String> inputField = composite.add(BPF.inputFieldString(), "growx");
		final IButton saveButton = composite.add(BPF.button());

		//create save action 
		final IAction saveAction = createSaveAction(inputField);

		//create a menu and add save action
		final MenuModel menu = new MenuModel("File");
		menu.addAction(saveAction);

		//add the menu to the menu bar
		menuBar.addMenu(menu);

		//add the action to the toolbar
		toolBar.addAction(saveAction);

		//bind the action to the save button
		saveButton.setAction(saveAction);

		//set the root frame visible
		frame.setVisible(true);
	}

	private static IAction createSaveAction(final IInputComponent<String> inputComponent) {
		final IActionBuilder builder = Action.builder();
		builder.setText("Save");
		builder.setToolTipText("Saves the text");
		builder.setAccelerator(VirtualKey.S, Modifier.CTRL);
		builder.setIcon(IconsSmall.DISK);

		//save command implements ICommandExecutor and IEnabledChecker,
		//so set them both
		final SaveCommand saveCommand = new SaveCommand(inputComponent);
		builder.setCommand(saveCommand, saveCommand);

		return builder.build();
	}

	private static final class SaveCommand 
		extends AbstractEnabledChecker implements ICommandExecutor, IEnabledChecker {

		private final IInputComponent<?> inputComponent;

		private SaveCommand(final IInputComponent<?> inputComponent) {
			this.inputComponent = inputComponent;

			inputComponent.addInputListener(new IInputListener() {
				@Override
				public void inputChanged() {
					fireEnabledStateChanged();
				}
			});
		}

		@Override
		public void execute(final IExecutionContext executionContext) throws Exception {
			inputComponent.resetModificationState();
			fireEnabledStateChanged();
			final String message = "'" + inputComponent.getValue() + "' saved!";
			MessagePane.showInfo(executionContext, message);
		}

		@Override
		public IEnabledState getEnabledState() {
			if (!inputComponent.hasModifications()) {
				return EnabledState.disabled("No changes to save");
			}
			else {
				return EnabledState.ENABLED;
			}
		}

	}

}
~~~

Die Klasse `SaveCommand` implementiert sowohl die Schnittstelle `ICommandExecutor` als auch `IEnabledChecker`. Dies ist im Beispiel einfacher, da sich nach dem Zurücksetzen des Modification State in Zeile 81 auch der EnabledState ändert. In Zeile 82 werden daher die registrierten Listener darüber informiert. Es ist bei diesem Vorgehen zu beachten, dass in Zeile 58 die `saveCommand` Instanz doppelt angegeben wird, einmal als `ICommandExecutor` und einmal als `IEnabledChecker`. 

Beide Schnittstellen in einer Klasse zu implementieren ist nicht ungewöhnlich. In manchem Fällen lassen sich aber auch `IEnabledChecker` für unterschiedliche Actions wiederverwenden, was eine Trennung der Implementierung nahelegt. 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 

### ActionItemVisibilityAspect{#action_item_visibility_aspect}

Ein `IActionItemVisibilityAspect` definiert die Sichtbarkeit für eine oder mehrere [Action Item Models](#action_item_model). Mit Hilfe der folgenden Methode kann auf einem `IActionItemModelBuilder` ein `IActionItemVisibilityAspect` für ein einzelnes [Action Item Model](#action_item_model) gesetzt, bzw. hinzugefügt werden:

~~~
	IActionItemModelBuilder addVisibilityAspect(IActionItemVisibilityAspect visibilityAspect);
~~~

Mit Hilfe eines [`IActionItemVisibilityAspectPlugin`](#action_item_visibility_aspect_plugin) kann ein Aspekt zu allen Action Items Models hinzugefügt werden.

#### Die Schnittstelle IActionItemVisibilityAspect

Die Schnittstelle IActionItemVisibilityAspect hat die folgende Methode:

~~~
	IPriorityValue<Boolean, LowHighPriority> getVisibility(IAction action);
~~~

Dabei hat ein `IPriorityValue<Boolean, LowHighPriority>` folgenden Methoden:

~~~
	Boolean getValue();

	LowHighPriority getPriority();
~~~

Der `value` definiert, ob die Action sichtbar sein soll oder nicht. Die enum `LowHighPriority` hat zwei mögliche Werte:

~~~
	LOW,

	HIGH;
~~~

Man kann also festlegen, mit welcher Priorität die Action sichtbar sein soll oder nicht. Dies kann hilfreich sein, wenn mehr als ein Aspekt definiert ist. 

Zum Beispiel könnte ein Aspekt alle Actions ausblenden wollen, für die der angemeldete Nutzer kein Ausführungsrecht in der Rechteverwaltung besitzt. Ein solcher Aspekte würden für `visible == true` die Priorität `LOW` setzen, was soviel bedeutet wie: "Aus meiner Sicht sichtbar, außer jemand anders sagt mit höherer Priorität `visible=false`. Für `visible == false` würde die Priorität `HIGH` verwendet werden, was soviel bedeutet wie: "Egal was andere nach mir sagen, die Action soll nicht sichtbar sein". 

Hat die Sichtbarkeit von zwei Aspekten beide Male die Priorität `HIGH`, wird der Aspekt berücksichtigt, welcher zuerst hinzugefügt wurde. Beim [Action Item Visibility Aspect Plugin](#action_item_visibility_aspect_plugin) kann dafür eine `order` angegeben werden.

Um das obere Beispiel zu erweitern, könnte ein Aspekt die Aufgabe haben, bestimmte Actions immer anzuzeigen, auch wenn man kein Recht zur Ausführung hat (z.B. weil keine Lizenz erworben wurde). Dennoch soll der Nutzer, sozusagen als _Teaser_, sehen dass eine bestimmte Aktion existiert, um den Wunsch zu wecken, eine Lizenz zu erwerben. Ein solcher Aspekt würde für `visible == true` die Priorität `HIGH` und für `visible == false` die Priorität `LOW` verwenden.

#### Enabled State Visibility Aspect

Die Klasse `org.jowidgets.tools.model.item.EnabledStateVisibilityAspect` implementiert einen Visibility Aspekt, welcher alle Actions ausblendet, die nicht enabled sind. Der Source Code soll als Beispiel für die Implementierung eines `IActionItemVisibilityAspect` dienen:

~~~{.java .numberLines startFrom="1"}
public final class EnabledStateVisibilityAspect implements IActionItemVisibilityAspect {

	private static final IPriorityValue<Boolean, LowHighPriority> NOT_VISIBLE_HIGH 
		= new PriorityValue<Boolean, LowHighPriority>(Boolean.FALSE, LowHighPriority.HIGH);

	private static final IPriorityValue<Boolean, LowHighPriority> VISIBLE_LOW 
		= new PriorityValue<Boolean, LowHighPriority>(Boolean.TRUE, LowHighPriority.LOW);

	@Override
	public IPriorityValue<Boolean, LowHighPriority> getVisibility(final IAction action) {
		if (action != null) {
			final boolean enabled = action.isEnabled();
			if (!enabled) {
				return NOT_VISIBLE_HIGH;
			}
			else {
				return VISIBLE_LOW;
			}
		}
		return VISIBLE_LOW;
	}
}
~~~

#### Secure Action Item Visiblity Aspect

Der folgende Sichtbarkeitsaspekt stammt aus der [jo-client-platform](http://code.google.com/p/jo-client-platform/), und versteckt alle Actions, für welche der angemeldete Nutzer kein Ausführungsrecht besitzt^[Dabei handelt es sich um einen reinen Convenience Aspekt. In der Service Schicht wird eine solche Aktion zusätzlich abgelehnt.]. 

~~~{.java .numberLines startFrom="1"}
private final class SecureActionItemVisibilityAspect implements IActionItemVisibilityAspect {

	@Override
	public IPriorityValue<Boolean, LowHighPriority> getVisibility(final IAction action) {
		final ISecureObject<AUTHORIZATION_TYPE> secureObject 
			= WrapperUtil.tryToCast(action, ISecureObject.class);
		if (secureObject != null) {
			if (!authorizationChecker.hasAuthorization(secureObject.getAuthorization())) {
				return NOT_VISIBLE_HIGH;
			}
		}
		return null;
	}
}
~~~

Das zugehörige Plugin findet sich [hier](http://code.google.com/p/jo-client-platform/source/browse/trunk/modules/addons/org.jowidgets.cap.security.ui/src/main/java/org/jowidgets/cap/security/ui/impl/SecureActionItemVisibilityAspectPluginImpl.java)

#### Die Schnittstelle IActionItemVisibilityAspectPlugin{#action_item_visibility_aspect_plugin}

Um ein Visibilitätsaspekt global hinzuzufügen, kann das `IActionItemVisibilityAspectPlugin` verwendet werden. Die Schnittstelle hat die folgenden Methoden:

~~~
	IActionItemVisibilityAspect getVisibilityAspect();

	int getOrder();
~~~

Die erste Methode liefert den Aspekt, die zweite die Order. Alle Plugins werden nach ihrer Order ausgeführt. Um ein Plugin zu registrieren, kann der [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) Mechanismus verwendet werden, dazu muss eine Datei mit dem Namen `org.jowidgets.api.model.item.IActionItemVisibilityAspectPlugin` in META-INF/services abgelegt werden, welche die Implementierungen auflistet.

Im [Command Action Snipped](#command_action_snipped) könnte man zum Beispiel das `org.jowidgets.tools.model.item.EnabledStateVisibilityAspectPlugin` hinzufügen, dann würde die Save Action nur erscheinen, wenn man speichern kann^[Dies soll nur der Anschauung dienen. Nach Meinung des Autors ist es besser, den `disabled` Reason anzuzeigen, anstatt den Save Button auszublenden.].

Neben der Möglichkeit, das Plugin mit Hilfe eines [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) zu registrieren, könnte man auch den folgenden Code hinzufügen (z.B. bevor das Frame erzeugt wird):

~~~{.java .numberLines startFrom="1"}
	ActionItemVisibilityAspectPlugin.registerPlugin(new EnabledStateVisibilityAspectPlugin());
~~~

Das Ergebnis würde, unabhängig mit welcher Methode das Plugin registriert wurde, dann so aussehen (einmal mit und einmal ohne Save Action):

![Action Visibility Beispiel](images/action_visibility1.gif "Action Visibility Beispiel")
![Action Visibility Beispiel](images/action_visibility2.gif "Action Visibility Beispiel")

Es ist zu beachten, dass der Button nicht automatisch ausgeblendet wurde. Das Ausblenden ist derzeit auf Menüs und Toolbars beschränkt. Um die Funktion selbst zu implementieren, könnte die folgende Methode auf der Klasse `ActionItemVisibilityAspectPlugin` verwendet werden, um die Sichtbarkeit der Action zu erhalten:

~~~
	public static IPriorityValue<Boolean, LowHighPriority> getVisibility(final IAction action) {...}
~~~ 

Diese liefert die Visibilität auf Basis aller registrierten Plugins. Um den Button im [Command Action Snipped](#command_action_snipped) ebenfalls automatisch auszublenden, könnte man die folgende Methode hinzufügen:

~~~{.java .numberLines startFrom="1"}
private void setButtonVisibility(final IButton button, final IAction action) {
	final IPriorityValue<Boolean, LowHighPriority> visibility 
		= ActionItemVisibilityAspectPlugin.getVisibility(action);
	if (visibility != null) {
		button.setVisible(visibility.getValue().booleanValue());
	}
	else {
		button.setVisible(true);
	}
}
~~~

Und diese wie folgt aufrufen:

~~~{.java .numberLines startFrom="1"}
	...
	
	setButtonVisibility(saveButton, saveAction);
	saveAction.getActionChangeObservable().addActionChangeListener(new ActionChangeAdapter() {
		@Override
		public void enabledChanged() {
			setButtonVisibility(saveButton, saveAction);
			frame.layoutLater();
		}
	});
	
	...
~~~





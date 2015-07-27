## Jowidgets API (Java open widget API) ##

Jowidgets is a single sourcing GUI API for Java.

### Main goals ###

  * High level GUI API
    * Make it more easy to develop enterprise GUI applications

  * Single Sourcing
    * Write applications only once for different UI platforms

### Documentation ###

  * [User guide (german)](http://www.jowidgets.org/docu)
  * [API Specification](http://www.jowidgets.org/api_doc)
  * [jo-projects (slides, german)](http://www.jowidgets.org/documents/jo-projects.pdf)




### Depending projects ###

  * [jo-client-platform](http://code.google.com/p/jo-client-platform/)
  * [jo-client-platform-samples](http://code.google.com/p/jo-client-platform-samples/)
  * [jo-useradmin](http://code.google.com/p/jo-useradmin/)
  * [jo-modeler](http://code.google.com/p/jo-modeler/downloads/list)
  * [jo-fx](http://code.google.com/p/jo-fx/)

### Architecture ###

![http://jowidgets.org/pics/architecture.gif](http://jowidgets.org/pics/architecture.gif)

### API ###

The jowidgets API is a high level GUI API with a focus on enterprise gui application development.

### SPI ###

The default implementation of this API uses an quite simple SPI (Service Provider Interface) that has less features, so implementations of the SPI have not to (re-)implement all features of the jowidgets API. Hence it's not too hard to support a new UI technology although the API offers much convenience.

### SPI implementations ###

  * Swing
  * SWT
  * RWT (uses SWT impl)
  * Java FX (Experimental, not for productive use so far)
  * Dummy (for JUnit tests)


---


### Core widgets ###

  * Frame
  * Dialog
  * Menu
  * PopupMenu
  * Toolbar
  * MessageDialog
  * QuestionDialog
  * PopupDialog
  * FileChooser
  * DirectoryChooser
  * Calendar
  * InputDialog (validated)
  * LoginDialog
  * PasswordChangeDialog
  * Composite
  * ScrollComposite
  * SplitComposite
  * ExpandComposite
  * TextLabel
  * Icon
  * Label
  * TextField
  * TextArea
  * InputField (validated)
  * Button
  * ToggleButton
  * CheckBox
  * ComboBox (with auto completion)
  * ComboBoxSelection (with auto completion)
  * ProgressBar
  * Separator
  * TextSeparator
  * ValidationLabel
  * TabFolder
  * Tree
  * Table
  * Slider
  * Canvas
  * Levelmeter

### Addon widgets ###

  * SwtAwtControl (Bridge to use AWT in SWT)
  * AwtSwtControl (Bridge to use SWT in AWT)
  * OleControl
  * OfficeControl (document and table calculation, uses OleControl)
  * PdfReader (Uses Browser)
  * Mediaplayer (Uses Windows Media Player)
  * Browser (HTML / WEB)
  * Download Button
  * MAP (uses Browser and Google Earth Plugin)

### Planned core widgets ###

  * MultiSplitComposite
  * TreeTable
  * RadioGroup
  * List
  * Spinner
  * ...


---


### Features ###

#### Widget Creation ####

  * All widgets will be created by factories
  * Widget decoration possible for all widgets
  * Builder for all widgets
  * Builder default setup can be overriden for all widgets
  * Widget creation is observable
  * Creation of custom widgets

#### Dialogs and windows ####

  * Auto pack modes (ONCE, ALWAYS, OFF)
  * Auto center modes (ONCE, ALWAYS, OFF)
  * Auto position correction possible (corrects the position if the window will not be displayed completely, e.g. because it's on an not connected second monitor)
  * Min pack size and max pack size can be defined

#### IWidget ####

  * Dispose state can be obtained and observed for all widgets

#### IComponent ####

  * Showing state can be obtained and observed for all components

#### IContainer ####

  * Will be used to compose controls (add and remove, as usual in other UI frameorks)
  * Recursive listener registration
  * Adding and removing of controls can be observed easiliy
  * Custom control factories can be added

#### Layouting ####

  * Flow layout
  * Fill layout
  * Border layout
  * List layout
  * Table layout
  * [Mig layout](http://www.miglayout.com/)
  * Null layout
  * Custom layout managers

#### Validation ####

  * Generic validatable input components, examples:
```
      IInputField<Date>
      IComboBox<String>
      IInputDialog<Person>
```
  * Converter pattern for easy binding and validation



#### Action command model ####

  * Reusable actions and commands
  * Creation with builder pattern
  * Advanced enabled checking (Solves e.g.: Why is that button grey?)
  * Lazy initialization of actions and commands

#### Menu and item models ####

  * Observable models for menus, items, menu bars, tool bars, ...
  * Can be bound and composed arbitrarily (E.g a menu model can be bound to a popup menu and to an menu bar at the same time. If the model changes, all bound widgets change without the need of an explicit binding)
  * Builder pattern for all models
  * Item models can be set invisible without the need to remove them from their menu model


#### i18n ####

  * Full i18n support for english and german (feel free to add support for other languages)
  * Multi user locale support (e.g in RWT applications)

#### Workbench ####

  * Workbench API for
    * Workbench
    * Workbench application
    * Workbench component (hierarchical)
    * Perspectives, layouts and views
  * Default workbench implementation
  * RCP workbench implementation, uses RCP workbench to implement the API

#### Bridging ####

  * ISwtAwtControl (to use native awt widgets when swt spi is plugged)
  * IAwtSwtControl (to use native swt widgets when swing spi is plugged)
  * BridgedSwtEventLoop (ensures that all swt and awt events run in the same event dispatcher thread)
  * JoToSwing (to use swing code in a plain jowidgets application)
  * SwingToJo (to use jowidgets code in an plain swing application)
  * JoToSwt (to use swt code in a plain jowidgets application)
  * SwtToJo (to use jowidgets code in an plain swt / rcp application)

#### Testing ####

  * Dummy spi implementation to allow JUnit test in headless environment
  * A lot of unit test already exists
  * Test interfaces (e.g. button.push())
  * Test tool to record user interaction and generate unit test (prototype)



---


## Silk icons ##

**Jowidgets uses the [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) library for it's demo applications. If you want to use these icons for your applications too, please respect the licence conditions of [Silk Icons](http://www.famfamfam.com/lab/icons/silk/).**


---


## Screenshots ##

### Swing SPI implementation ###

![http://jowidgets.org/screenshots/screenshot_swing.jpg](http://jowidgets.org/screenshots/screenshot_swing.jpg)

### SWT SPI implementation ###

![http://jowidgets.org/screenshots/screenshot_swt.jpg](http://jowidgets.org/screenshots/screenshot_swt.jpg)


### RWT SPI implementation (AJAX, rendered with IE) ###

![http://jowidgets.org/screenshots/screenshot_rwt.jpg](http://jowidgets.org/screenshots/screenshot_rwt.jpg)


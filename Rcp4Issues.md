# jowidgets and Eclipse RCP 4.x #

There are two examples how to use jowidgets with Eclipse RCP 4.x. They can be found here [Rcp4Examples](http://code.google.com/p/jo-widgets/source/browse/#svn%2Ftrunk%2Frcp%2Fexamples)

To make them work, do the following steps:

  * Generell information how to create Eclipse 4.x model based applications and what preconditions are necessary can be found here [Vogella Eclipse RCP Tutorial](http://www.vogella.com/articles/EclipseRCP/article.html)
  * Check out the example projects from [here](http://code.google.com/p/jo-widgets/source/browse/#svn%2Ftrunk%2Frcp%2Fexamples)
  * Use _import existing projects ..._ to import them into eclipse
  * Set the target definition `org.jowidgets.examples.rcp.target` as target platform
  * To launch example1 use the product definition `org.jowidgets.examples.rcp.example1.product` that can be found in the project `org.jowidgets.examples.rcp.example1`
  * To launch example2 use the product definition `org.jowidgets.examples.rcp.example2.swt.product` that can be found in the project `org.jowidgets.examples.rcp.example2.swt`

## Example 1 ##

This example shows how to use jowidgets with the (default) workbench SWT renderer. Because of that, there is a dependendy to the SWT SPI implementation of jowidgets (SwtToJo). So example 1 is not ideal, if you want to make single sourcing, but it shows how to use the jowidgets API in a simple way.

## Example 2 ##

In this example the SWT dependency from the Example2Part was removed. For that a custom PartRenderer was created that was derived from the ContributedPartRenderer of the SWT Workbench Renderer. This SwtToJoPartRenderer let the created composite implement an `IProvider<IComposite>` that can be used to receive the jowidgets composite. So the `Example2Part` needs no dependency to SWT. The project `org.jowidgets.examples.rcp.example2.swt` has the launcher to start the model of `org.jowidgets.examples.rcp.example2.common` with SWT. For other renderers (e.g. Swing) this can be handeled in the same way.

If there will be created an `JowidgetsEclipseWorkbenchRenderer` it will be even possible to use a `IComposite` instead of an `IProvider<IComposite>`. This is planned to do in future.
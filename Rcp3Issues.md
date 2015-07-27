# jowidgets and Eclipse RCP 3.x #

## The jowidgets workbench ##

Jowidgets provides a workbench api to logical define a workbench with applications, components and views. An example (the common part) how to do that can be found here: [WorkbenchDemo3](http://code.google.com/p/jo-widgets/source/browse/#svn%2Ftrunk%2Fmodules%2Fexamples%2Forg.jowidgets.examples.common%2Fsrc%2Fmain%2Fjava%2Forg%2Fjowidgets%2Fexamples%2Fcommon%2Fworkbench%2Fdemo3) This example can be started with an workbench implementation and an jowidgets spi implementation. An example that uses the default workbench implementation and swing spi implementation can be found here: [SwingWorkbenchDemo3Starter.java](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.swing/src/main/java/org/jowidgets/examples/swing/SiwngWorkbenchDemo3Starter.java). There is also an workbench implementation RCPWorkbenchImpl that uses the Eclipse 3.x workbench to render the defined workbench components.

## The RCP 3.x example ##

This [RcpJoClientPlatformExample](http://code.google.com/p/jo-client-platform-samples/source/browse/trunk/rcp/example1/tycho/plugins/org.jowidgets.samples.rcp.sample1.starter.client.rcp.plugin/src/main/java/org/jowidgets/samples/rcp/sample1/) uses the RcpWorkbenchImplementation? and also the jo-client-platform framework, so it also has aspects of client server issues and persistence. Also the example can only be started with RCP with some necessary modifications after checkout. I not yet get it to create a working product (i am not so close with rcp yet), so i can run the rcp client only inside the rcp workbench. Let me give a litle overview what the example does and how it can be started:

The example is a tiny user administration with users, authorizations and roles, that can be linked. All data will be persistet, in the example the h2 db will be used for that. The jo-client-platform provides a three layer (ui ,service, persistence) architecture. The example uses the jowidgets workbench api. The example can be started without RCP, but there is also an starter for RCP that can be found here: [RcpSample1StarterClientRcp.java](http://code.google.com/p/jo-client-platform-samples/source/browse/trunk/rcp/example1/tycho/plugins/org.jowidgets.samples.rcp.sample1.starter.client.rcp.plugin/src/main/java/org/jowidgets/samples/rcp/sample1/starter/client/rcp/RcpSample1StarterClientRcp.java)



## How to make it run with rcp ##

  * Check out the jo-client-platform samples: http://code.google.com/p/jo-client-platform-samples/source/checkout
  * Switch to the folder to some like this C:\projects\jo-client-platform-samples\modules\rcp\example1\parent
  * mvn clean install (that builds all modules and bundles)
  * Switch to a folder like that C:\projects\jo-client-platform-samples\modules\rcp\example1\tycho\target
  * Modify the target file (there is a local path inside, you change this depending on your root folder). I know this is ugly, but at the moment i do not know better
  * Switch to the folder to some like this: C:\projects\jo-client-platform-samples\modules\rcp\example1\tycho\parent
  * mvn clean install (you need maven3 for that)
  * Create a new workspace inside eclipse (you need the m2e plugin at least)
  * Import all modules (import existing maven projects)
  * Try some starter, e.g. the standalone swing starter org.jowidgets.samples.rcp.sample1.starter.standalone.swing (Run as plain java application)
  * Import all tycho modules
  * Do NOT import the bundles (i get problems when having tycho and felix stuff in the same workspace)
  * Set the target defininition in the module org.jowidgets.samples.rcp.sample1.target as target platform for eclipse
  * Stop earlier startet applications (e.g. the standalone starter). Because of the h2 db, there can only be one service layer active at the same time accessing the database file
  * Start the application server inside eclipse (org.jowidgets.samples.rcp.sample1.starter.server.RcpSample1StarterServer?)
  * In the module org.jowidgets.samples.rcp.sample1.starter.client.rcp.plugin you find a launch configuration, launch this
  * uses admin, admin for login
  * If not works, let me now
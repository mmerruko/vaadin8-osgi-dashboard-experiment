# OSGi Vaadin Dashboard Experiment

## Introduction

This is a Vaadin 8.1.0 and OSGi demo. The goal of this application was to experiment with Vaadin 8 and OSGi to create a dashboard which could accept widget contributions through OSGi bundles, would offer drag and drop to organize the widgets on the dashboard canvas and is capable of saving and storing the arrangement of widgets and its own configuration.

This demo consists of 6 bundles and is a continuation of Matti Hosio's https://github.com/mhosio/vaadin-osgi-demo.

### osgidashboard-dashboard

This can be considered the core bundle of the demo. It implements the following :

1. the dashboard itself
1. the drag and drop functionality for it
1. the mechanisms for contributing additional widgets as `IWidgetContribution` to be used by an `IWidgetRegistry` set on the dashboard to create the widgets
1. the model that can be serialized and deserialized and contains the necessary information to be used by the registry to recreate the dashboard
1. disablement/enablement of widgets that have a specific type of widget identifiers

### osgidashboard-demo

This bundle has the demo application using the `osgidashboard-dashboard`. It has the theme used by the application, a `WidgetToolbar` which is a prototype OSGi service (meaning that each service request will create a new instance), and the `DemoUI` itself.

The `WidgetToolbar` tracks the registration/deregistration of `IWidgetContribution` services and updates itself to offer them for dragging and dropping to create a widget on the dashboard.

The `DemoUI` creates and configures the `osgidashboard-dashboard`, gets access to a `WidgetToolbar`, `IWidgetRegistry` and `DashboardService` services, and creates some basic controls using the dashboard component and the OSGi services to resize the dashboard, load and save it.

It also contains the registration of the `VaadinServlet` that will be picked up by the `http-whiteboard` service.

At the moment the Vaadin-OSGi integration is not in a state where a UI can be used as a DS component so some lower-level methods were used to access the necessary services.

### osgidashboard-osgi

This bundle contains a simple service implementation for saving, loading and listing dashboards. Dashboard models are stored as JSon files in a folder called `.dashboards` under the user's home folder. The 'user' folder in this case is the Java `user.home` property.

A default implementation of `IWidgetRegistry` is also part of this bundle. The `DefaultWidgetRegistry` is a DS component which tracks widget contributions, notifies status listeners for the enablement and disablement of a widget contribution type and given a type ID it delegates the creation of a default widget and/or widget component to the underlying `IWidgetContribution`.

### Dynamic widget contributions and limitations - The other bundles

The widget contributions for this demo are offered through the bundles `osgidashboard-basic-widgets` and `osgidashboard-olmap`. The widgets in `osgidashboard-basic-widgets` are fairly simple, they are just a `Label` and a `Button` wrapped in a `VerticalLayout`. As these are Vaadin server side-only contributions the bundle has all the necessary parts to make them available to the demo application as OSGi services.

The `osgidashboard-olmap` highlights a limitation in the use of OSGi and Vaadin. The map component has Vaadin client side additions, and they are necessary to render the components. The Widgetset used by the demo application must be compiled with these client-side modules included in order to render the widgets in the browser. Vaadin 8 uses GWT for the client side of the framework, and while GWT supports the use of modules, the modules are used during compilation to produce a single Widgetset. They can't be added and removed to the Widgetset at runtime in the same way that you would expect of an OSGi module.

The `DemoUI` though uses a widgetset contributed by the `osgidashboard-osgi-widgetset` module, and this makes it possible to add bundles with client side dependencies. The widgetset module should also be updated and recompiled though before the contributions can be used.

This solution is far from ideal, however it adds some flexibility. The `osgidashboard-osgi-widgetset` pom.xml has dependencies to the dashboard module, and the `osgidashboard-olmap`, the two bundles that have client-side additions. If one wants to add yet another module with client-side additions, that module should be added to the dependencies of the `osgidashboard-osgi-widgetset` pom.xml and update both modules.

### About Vaadin Open Layers Map

The Vaadin OpenLayers wrappers found in https://github.com/VOL3/v-ol3 contain a demo application and some additional dependencies that are not necessary for using the core wrappers. To simplify this and remove additional OSGi dependencies (for example to the Vaadin compatibility bundles) the repository was forked and the demo application was moved to a separate maven module.

The fork can be found at https://github.com/mmerruko/v-ol3 and is versioned as '2.0.1'. Since this is not in the process of being integrated in the main tree (it will probably be in the future) **it is necessary to clone the fork and run `mvn clean install`** at the root of the git repository to have the dependency of `osgidashboard-olmap` available in the local maven repository.

## Installation

The target for this has been [Apache Karaf 4.1.1](http://karaf.apache.org/download.html#container-411), so for starters go the linked page and follow the instructions to install karaf and then launch it.

Once karaf is running then do the following :

1. run `feature:install http`
1. run `feature:install http-whiteboard`
1. Go to the root of the demo example and run `mvn clean package` which produces the necessary bundles and a .kar file under `osgidashboard-karaf/target`
1. run `kar:install file://${path-to-kar-file}` where `${path-to-kar-file}` should be replaced with the absolute path to the kar file produced in the previous step

The kar file is a convenient packaging of the Vaadin bundles, their dependencies and the demo bundles and dependencies.

With this you should find the application running under `http://localhost:8181`.

Typically when uninstalling and reinstalling the kar file (`kar:uninstall osgidashboard-karaf-1.0-SNAPSHOT` and `kar:install file://${path-to-kar-file}`) there might be some error with the order that the bundles are initialized causing problems when running the demo application.

To solve that run `bundle:list` and then `restart ${bundleId}` where `${bundleId}` is the id of the bundle to restart, and pass in the bundles that were not properly initialized. For me that is usually the `vaadin-push` and `vaadin-server`, and more rarely the `osgidashboard-osgi-widgetset`.

## TODOs

1. Improve building time, it now unnecessarily builds several widgetsets while only one is needed
1. Additional features:
  1. Extend the widget model to include widget properties that can be used to persist widget specific state


## Feedback

If you're reading this documentation and trying out the demo to get ideas for your own project using Vaadin and OSGi please send any feedback for things that you don't understand or improvement suggestions and so on. I can't promise that I'll look them in a timely fashion but when time allows I can use your feedback to improve the documentation and enrich this example to help future users find their way faster.

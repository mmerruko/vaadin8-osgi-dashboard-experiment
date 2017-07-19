package org.vaadin.mmerruko.osgidashboard.widgetset;

import org.osgi.service.component.annotations.Component;

import com.vaadin.osgi.resources.OsgiVaadinWidgetset;

@Component
public class DashboardWidgetset implements OsgiVaadinWidgetset {

    public static final String NAME = "org.vaadin.mmerruko.osgidashboard.widgetset.DashboardDemoWidgetset";

    @Override
    public String getName() {
        return NAME;
    }

}

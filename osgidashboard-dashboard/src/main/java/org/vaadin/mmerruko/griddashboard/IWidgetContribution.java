package org.vaadin.mmerruko.griddashboard;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

public interface IWidgetContribution {
    Component createWidgetComponent();
    
    Resource getWidgetIcon();
    String getWidgetTitle();
}

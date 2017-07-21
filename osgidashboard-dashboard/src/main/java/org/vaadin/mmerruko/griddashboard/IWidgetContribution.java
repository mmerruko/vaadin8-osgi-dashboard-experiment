package org.vaadin.mmerruko.griddashboard;

import org.vaadin.mmerruko.griddashboard.model.Widget;

import com.vaadin.ui.Component;

public interface IWidgetContribution {
    Widget createDefaultWidget();

    Component createWidgetComponent();

    String getTypeIdentifier();
    
    String getDefaultWidgetTitle();
}

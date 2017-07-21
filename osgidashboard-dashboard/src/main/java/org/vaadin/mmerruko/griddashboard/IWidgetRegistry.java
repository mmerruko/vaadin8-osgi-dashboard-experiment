package org.vaadin.mmerruko.griddashboard;

import org.vaadin.mmerruko.griddashboard.model.Widget;

import com.vaadin.ui.Component;

public interface IWidgetRegistry {
    Widget createDefaultWidget(String typeID);

    Component createWidgetComponent(String typeID);
}
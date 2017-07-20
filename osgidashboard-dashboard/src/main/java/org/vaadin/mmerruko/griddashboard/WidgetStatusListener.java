package org.vaadin.mmerruko.griddashboard;

public interface WidgetStatusListener {
    void widgetTypeDisabled(String typeID);
    void widgetTypeEnabled(String typeID);
}
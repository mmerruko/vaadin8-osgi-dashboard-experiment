package org.vaadin.mmerruko.osgidashboard.ui;

import org.vaadin.mmerruko.osgidashboard.GridDashboard.IWidgetFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

public class DefaultWidgetFactory implements IWidgetFactory {
    private static final String LABEL = "label";
    private static final String BUTTON = "button";

    @Override
    public com.vaadin.ui.Component createWidgetComponent(Object data) {
        if (BUTTON.equals(data)) {
            return new Button(System.currentTimeMillis() + "");
        } else if (LABEL.equals(data)) {
            return new Label(System.currentTimeMillis() + "");
        }
        return null;
    }
}

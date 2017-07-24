package org.vaadin.mmerruko.widgets.basic;

import java.util.UUID;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.vaadin.mmerruko.griddashboard.IWidgetContribution;
import org.vaadin.mmerruko.griddashboard.model.Widget;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@org.osgi.service.component.annotations.Component(immediate = true)
public class ButtonWidgetContribution implements IWidgetContribution {

    private static final String BUTTON_TITLE = "Button Widget";
    private static final String BUTTON_TYPE = "button";

    @Override
    public Component createWidgetComponent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Button button = new Button("Hello", e -> {
            Notification.show("Hello OSGi!");
        });
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        content.addComponent(button);
        content.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
        return content;
    }

    @Override
    public Widget createDefaultWidget() {
        return new Widget(UUID.randomUUID().toString(), getTypeIdentifier());
    }

    @Override
    public String getTypeIdentifier() {
        Bundle bundle = FrameworkUtil.getBundle(ButtonWidgetContribution.class);
        String symbolicName = bundle.getSymbolicName();

        return String.format("%s_%s", symbolicName, BUTTON_TYPE);
    }

    @Override
    public String getDefaultWidgetTitle() {
        return BUTTON_TITLE;
    }

}

package org.vaadin.mmerruko.osgidashboard.ui;

import org.vaadin.mmerruko.osgidashboard.IWidgetContribution;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@org.osgi.service.component.annotations.Component(immediate = true)
public class ButtonWidgetContribution implements IWidgetContribution {

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
    public Resource getWidgetIcon() {
        return VaadinIcons.BUTTON;
    }

    @Override
    public String getWidgetTitle() {
        return "Hello Button!";
    }

}

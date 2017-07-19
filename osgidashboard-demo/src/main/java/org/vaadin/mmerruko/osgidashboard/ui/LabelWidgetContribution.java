package org.vaadin.mmerruko.osgidashboard.ui;

import org.vaadin.mmerruko.osgidashboard.IWidgetContribution;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@org.osgi.service.component.annotations.Component(immediate = true)
public class LabelWidgetContribution implements IWidgetContribution {

    @Override
    public Component createWidgetComponent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Label greetingLabel = new Label("Hello OSGi!");
        greetingLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
        
        content.addComponent(greetingLabel);
        content.setComponentAlignment(greetingLabel, Alignment.MIDDLE_CENTER);

        return content;
    }

    @Override
    public Resource getWidgetIcon() {
        return VaadinIcons.LAPTOP;
    }

    @Override
    public String getWidgetTitle() {
        return "Greeting Widget";
    }

}

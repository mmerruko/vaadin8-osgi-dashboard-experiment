package org.vaadin.mmerruko.widgets.basic;

import java.util.UUID;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.vaadin.mmerruko.griddashboard.IWidgetContribution;
import org.vaadin.mmerruko.griddashboard.model.Widget;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@org.osgi.service.component.annotations.Component(immediate = true)
public class LabelWidgetContribution implements IWidgetContribution {

    private static final String LABEL_WIDGET = "Label Widget";
    private static final String LABEL_TYPE = "label";

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
    public String getTypeIdentifier() {
        Bundle bundle = FrameworkUtil.getBundle(ButtonWidgetContribution.class);
        String symbolicName = bundle.getSymbolicName();

        return String.format("%s_%s", symbolicName, LABEL_TYPE);
    }

    @Override
    public Widget createDefaultWidget() {
        return new Widget(UUID.randomUUID().toString(), getTypeIdentifier());
    }

    @Override
    public String getDefaultWidgetTitle() {
        return LABEL_WIDGET;
    }

}

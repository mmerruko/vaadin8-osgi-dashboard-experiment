package org.vaadin.mmerruko.osgidashboard.ui;

import org.vaadin.mmerruko.osgidashboard.IWidgetContribution;
import org.vaadin.mmerruko.osgidashboard.GridDashboard.IWidgetFactory;

import com.vaadin.server.Resource;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class DefaultWidgetFactory implements IWidgetFactory {

    @Override
    public com.vaadin.ui.Component createWidgetComponent(Object data) {
        if (data instanceof IWidgetContribution) {
            return ((IWidgetContribution) data).createWidgetComponent();
        }

        Label contribution = new Label("Unknown Contribution!");
        contribution.addStyleName(ValoTheme.LABEL_FAILURE);

        return contribution;
    }

    @Override
    public String getWidgetTitle(Object data) {
        if (data instanceof IWidgetContribution) {
            return ((IWidgetContribution) data).getWidgetTitle();
        }
        return null;
    }

    @Override
    public Resource getWidgetIcon(Object data) {
        if (data instanceof IWidgetContribution) {
            return ((IWidgetContribution) data).getWidgetIcon();
        }
        return null;
    }
}

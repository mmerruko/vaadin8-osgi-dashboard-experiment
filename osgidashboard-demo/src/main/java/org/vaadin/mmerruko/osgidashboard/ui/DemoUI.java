package org.vaadin.mmerruko.osgidashboard.ui;

import javax.servlet.annotation.WebServlet;

import org.osgi.service.component.annotations.Component;
import org.vaadin.mmerruko.osgidashboard.DashboardWidgetFrame;
import org.vaadin.mmerruko.osgidashboard.GridDashboard;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme(DemoTheme.THEME_NAME)
public class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        
        GridDashboard dashboard = new GridDashboard();
        layout.addComponent(dashboard);
        
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setSizeFull();
        
        Label greeting = new Label("Hello Dashboard!");
        wrapper.addComponent(greeting);
        wrapper.setComponentAlignment(greeting, Alignment.MIDDLE_CENTER);
        
        DashboardWidgetFrame widget = new DashboardWidgetFrame(wrapper);
        widget.addMenuAction("Greet", VaadinIcons.ALARM, () -> {
            Notification.show("Hello!");
        });
        widget.setWidgetTitle("Hello World!");
        
        dashboard.addComponent(widget);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "DemoUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = false)
    @Component(service = VaadinServlet.class)
    public static class DemoUIServlet extends VaadinServlet {
    }
}

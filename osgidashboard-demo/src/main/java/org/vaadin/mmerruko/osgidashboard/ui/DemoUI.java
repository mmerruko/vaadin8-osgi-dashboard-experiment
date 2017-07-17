package org.vaadin.mmerruko.osgidashboard.ui;

import javax.servlet.annotation.WebServlet;

import org.osgi.service.component.annotations.Component;
import org.vaadin.mmerruko.osgidashboard.DashboardModel;
import org.vaadin.mmerruko.osgidashboard.DashboardWidget;
import org.vaadin.mmerruko.osgidashboard.DashboardWidgetFrame;
import org.vaadin.mmerruko.osgidashboard.GridDashboard;
import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@Theme(DemoTheme.THEME_NAME)
public class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        SideMenu contents = new SideMenu();
        contents.setUserName("User Name");
        contents.setUserIcon(VaadinIcons.USER);

        DashboardModel dashboardModel = new DashboardModel();
        dashboardModel.setColumns(8);

        DashboardWidget buttonWidget = new DashboardWidget("button");
        buttonWidget.setWidth(6);
        buttonWidget.setHeight(2);

        DashboardWidget labelWidget = new DashboardWidget("label");
        labelWidget.setWidth(2);
        labelWidget.setHeight(5);

        DashboardWidget buttonWidget2 = new DashboardWidget("button");
        buttonWidget2.setWidth(4);

        DashboardWidget labelWidget2 = new DashboardWidget("label");
        labelWidget2.setWidth(2);
        labelWidget2.setHeight(3);

        dashboardModel.addWidget(buttonWidget);
        dashboardModel.addWidget(labelWidget);
        dashboardModel.addWidget(buttonWidget2);
        dashboardModel.addWidget(labelWidget2);

        GridDashboard dashboard = new GridDashboard();
        dashboard.renderDashboardModel(dashboardModel);
        dashboard.setMargin(true);

        contents.addMenuItem("Add Item", VaadinIcons.ACCORDION_MENU, () -> {
            DashboardWidgetFrame widgetFrame = new DashboardWidgetFrame(
                    new Label("Hello!"));
            dashboard.addComponent(widgetFrame);
        });
        contents.addMenuItem("Add Row", VaadinIcons.ACCORDION_MENU, () -> {
            int rows = dashboard.getRows();
            dashboard.insertRow(rows - 1);
        });
        contents.addMenuItem("Add Column", VaadinIcons.ACCORDION_MENU, () -> {
            int columns = dashboard.getColumns();
            dashboard.setColumns(columns + 1);
        });

        contents.setContent(dashboard);

        setContent(contents);
    }

    @WebServlet(urlPatterns = "/*", name = "DemoUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = false)
    @Component(service = VaadinServlet.class)
    public static class DemoUIServlet extends VaadinServlet {
    }
}

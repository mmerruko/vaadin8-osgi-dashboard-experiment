package org.vaadin.mmerruko.osgidashboard.ui;

import javax.servlet.annotation.WebServlet;

import org.osgi.service.component.annotations.Component;
import org.vaadin.mmerruko.osgidashboard.DashboardModel;
import org.vaadin.mmerruko.osgidashboard.DashboardWidgetset;
import org.vaadin.mmerruko.osgidashboard.GridDashboard;
import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme(DemoTheme.THEME_NAME)
@Widgetset(DashboardWidgetset.NAME)
public class DemoUI extends UI {

    private Label mapWidget;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setUserName("User Name");
        sideMenu.setUserIcon(VaadinIcons.USER);

        DashboardModel dashboardModel = new DashboardModel();
        dashboardModel.setColumns(8);

        GridDashboard dashboard = new GridDashboard();
        dashboard.setSizeFull();

        VerticalLayout contents = new VerticalLayout();
        contents.setMargin(false);
        contents.setSpacing(false);
        contents.addComponent(createDashboardToolbar(dashboard));
        contents.addComponent(dashboard);
        contents.setSizeFull();
        contents.setExpandRatio(dashboard, 1);

        dashboard.setWidgetFactory(new DefaultWidgetFactory());

        mapWidget = new Label("Map Component");
        mapWidget.setSizeFull();
        showWidgetToolbarWindow();

        sideMenu.setContent(contents);

        setContent(sideMenu);
    }

    private HorizontalLayout createDashboardToolbar(GridDashboard dashboard) {
        MenuBar dashboardMenu = new MenuBar();

        HorizontalLayout toolbarWrapper = new HorizontalLayout();
        toolbarWrapper.setHeight("50px");
        toolbarWrapper.setWidth("100%");
        toolbarWrapper.setMargin(new MarginInfo(false, true));
        
        dashboardMenu.addItem("Resize", VaadinIcons.RESIZE_H, item -> {
            
        });
        
        dashboardMenu.addItem("Save", FontAwesome.FLOPPY_O, item -> {
            Notification.show("Not Implemented");
        });
        dashboardMenu.addItem("Load", FontAwesome.FOLDER_OPEN_O, item -> {
            Notification.show("Not Implemented");
        });

        toolbarWrapper.addComponent(dashboardMenu);
        toolbarWrapper.setComponentAlignment(dashboardMenu, Alignment.MIDDLE_RIGHT);

        return toolbarWrapper;
    }

    private void showWidgetToolbarWindow() {
        WidgetToolbar toolbar = new WidgetToolbar();
        addWindow(toolbar);
    }

    @WebServlet(urlPatterns = "/*", name = "DemoUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = false)
    @Component(service = VaadinServlet.class)
    public static class DemoUIServlet extends VaadinServlet {
    }
}

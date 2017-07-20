package org.vaadin.mmerruko.osgidashboard.ui;

import javax.servlet.annotation.WebServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.vaadin.mmerruko.griddashboard.GridDashboard;
import org.vaadin.mmerruko.griddashboard.SizeDialog;
import org.vaadin.mmerruko.osgidashboard.widgetset.DashboardWidgetset;
import org.vaadin.mmerruko.osgidashboard.widgetset.DefaultWidgetFactory;
import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.Push;
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
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme(DemoTheme.THEME_NAME)
@Widgetset(DashboardWidgetset.NAME)
@Push
public class DemoUI extends UI {

    private ServiceReference<WidgetToolbar> toolbarServiceRef;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setUserName("User Name");
        sideMenu.setUserIcon(VaadinIcons.USER);
        sideMenu.addMenuItem("Show Toolbar", VaadinIcons.TOOLBOX, () -> {
            showWidgetToolbarWindow();
        });

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
            int rows = dashboard.getRows();
            int columns = dashboard.getColumns();

            SizeDialog dialog = new SizeDialog();
            dialog.setResizeCallback((width, height) -> {
                if (!dashboard.canSetDimensions(width, height)) {
                    Notification.show("Invalid dashboard dimensions!",
                            Notification.Type.ERROR_MESSAGE);
                    return false;
                }
                dashboard.setDimensions(width, height);
                return true;
            });
            dialog.show(columns, rows);
        });

        dashboardMenu.addItem("Save", FontAwesome.FLOPPY_O, item -> {
            Notification.show("Not Implemented");
        });
        dashboardMenu.addItem("Load", FontAwesome.FOLDER_OPEN_O, item -> {
            Notification.show("Not Implemented");
        });

        toolbarWrapper.addComponent(dashboardMenu);
        toolbarWrapper.setComponentAlignment(dashboardMenu,
                Alignment.MIDDLE_RIGHT);

        return toolbarWrapper;
    }

    @Override
    public void detach() {
        super.detach();
        if (toolbarServiceRef != null) {
            Bundle bundle = FrameworkUtil.getBundle(DemoUI.class);
            BundleContext context = bundle.getBundleContext();
            try {
                context.ungetService(toolbarServiceRef);
            } catch (IllegalStateException e) {

            }
        }

    }

    private WidgetToolbar getToolbar() {
        Bundle bundle = FrameworkUtil.getBundle(DemoUI.class);
        BundleContext context = bundle.getBundleContext();
        if (toolbarServiceRef != null) {
            try {
                context.ungetService(toolbarServiceRef);
            } catch (IllegalStateException e) {

            }
        }

        toolbarServiceRef = context.getServiceReference(WidgetToolbar.class);
        if (toolbarServiceRef != null) {
            return context.getService(toolbarServiceRef);
        }
        return null;
    }

    private void showWidgetToolbarWindow() {
        WidgetToolbar toolbar = getToolbar();
        if (toolbar != null) {
            addWindow(toolbar);
        } else {
            Notification.show("No Toolbar Service Registered!",
                    Type.ERROR_MESSAGE);
        }
    }

    @WebServlet(urlPatterns = "/*", name = "DemoUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = false)
    @Component(service = VaadinServlet.class)
    public static class DemoUIServlet extends VaadinServlet {
    }
}

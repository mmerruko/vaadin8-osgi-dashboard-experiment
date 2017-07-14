package org.vaadin.mmerruko.osgidashboard;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

public class DashboardWidgetFrame extends CssLayout {
    
    private Label widgetTitle;
    private MenuItem root;

    public DashboardWidgetFrame(Component content) {
        addStyleName("dashboard-widget");
        addStyleName(ValoTheme.LAYOUT_CARD);
        
        setWidth("100%");
        
        HorizontalLayout titleBar = new HorizontalLayout();
        titleBar.setWidth("100%");
        titleBar.setSpacing(false);
        
        widgetTitle = new Label();
        widgetTitle.addStyleName(ValoTheme.LABEL_COLORED);
        widgetTitle.addStyleName(ValoTheme.LABEL_H3);
        widgetTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        widgetTitle.setWidthUndefined();
        
        titleBar.addComponent(widgetTitle);
        titleBar.setExpandRatio(widgetTitle, 1);
        titleBar.setComponentAlignment(widgetTitle, Alignment.MIDDLE_CENTER);
        
        MenuBar toolbar = new MenuBar();
        toolbar.addStyleName(ValoTheme.MENUBAR_SMALL);
        toolbar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        
        titleBar.addComponent(toolbar);
        titleBar.setComponentAlignment(toolbar, Alignment.MIDDLE_RIGHT);
        
        root = toolbar.addItem("", VaadinIcons.COG, null);
        
        addComponent(titleBar);
        addComponent(content);
    }
    
    public void setWidgetTitle(String title) {
        this.widgetTitle.setValue(title);
    }
    
    public void addMenuAction(String caption, Resource icon, Runnable action) {
        root.addItem(caption, icon, (item) -> action.run());
    }
}

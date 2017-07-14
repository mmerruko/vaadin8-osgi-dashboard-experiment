package org.vaadin.mmerruko.osgidashboard.ui;

import org.osgi.service.component.annotations.Component;

import com.vaadin.osgi.resources.OsgiVaadinTheme;
import com.vaadin.ui.themes.ValoTheme;

@Component
public class DemoTheme extends ValoTheme implements OsgiVaadinTheme {
    public static final String THEME_NAME = "demotheme";
    
    @Override
    public String getName() {
        return THEME_NAME;
    }

}

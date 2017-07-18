package org.vaadin.mmerruko.osgidashboard.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.themes.ValoTheme;

public class WidgetToolbar extends Window {
    private static final String LABEL = "label";
    private static final String BUTTON = "button";

    public WidgetToolbar() {
        Panel panel = new Panel();
        FormLayout content = new FormLayout();
        content.setSpacing(false);
        content.setSizeFull();
        content.addStyleName(ValoTheme.LAYOUT_WELL);

        setWidth("150px");
        setHeight("300px");

        Component buttonItem = createToolbarItem("Button Widget",
                VaadinIcons.BUTTON, BUTTON);
        Component labelItem = createToolbarItem("Label Widget",
                VaadinIcons.TEXT_LABEL, LABEL);

        content.addComponent(buttonItem);
        content.addComponent(labelItem);

        panel.setContent(content);
        panel.setSizeFull();

        setContent(panel);
    }

    private Label createToolbarItem(String caption, Resource icon,
            Object dragData) {
        Label toolbarItem = new Label(caption);
        toolbarItem.setWidth("100%");
        toolbarItem.addStyleName(ValoTheme.LABEL_COLORED);
        toolbarItem.setIcon(icon);

        DragSourceExtension<Label> sourceExtension = new DragSourceExtension<>(
                toolbarItem);
        sourceExtension.addDragStartListener(e -> {
            sourceExtension.setDragData(dragData);
        });
        sourceExtension.addDragEndListener(e -> {
            sourceExtension.setDragData(null);
        });

        return toolbarItem;
    }

}

package org.vaadin.mmerruko.osgidashboard.ui;

import java.util.List;

import org.vaadin.mmerruko.osgidashboard.IWidgetContribution;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.themes.ValoTheme;

public class WidgetToolbar extends Window {
    private FormLayout content;

    public WidgetToolbar() {
        Panel panel = new Panel();
        content = new FormLayout();
        content.setSpacing(false);
        content.setSizeFull();
        content.addStyleName(ValoTheme.LAYOUT_WELL);

        setWidth("150px");
        setHeight("300px");

        panel.setContent(content);
        panel.setSizeFull();

        setContent(panel);
    }

    public void setWidgets(List<IWidgetContribution> widgets) {
        content.removeAllComponents();
        for (IWidgetContribution widget : widgets) {
            Label item = createToolbarItem(widget);
            content.addComponent(item);
        }
    }

    private Label createToolbarItem(IWidgetContribution contribution) {
        Label toolbarItem = new Label(contribution.getWidgetTitle());
        toolbarItem.setWidth("100%");
        toolbarItem.addStyleName(ValoTheme.LABEL_COLORED);
        toolbarItem.setIcon(contribution.getWidgetIcon());

        DragSourceExtension<Label> sourceExtension = new DragSourceExtension<>(
                toolbarItem);
        sourceExtension.addDragStartListener(e -> {
            sourceExtension.setDragData(contribution);
        });
        sourceExtension.addDragEndListener(e -> {
            sourceExtension.setDragData(null);
        });

        return toolbarItem;
    }

}

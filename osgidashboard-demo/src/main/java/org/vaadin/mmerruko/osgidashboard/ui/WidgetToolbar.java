package org.vaadin.mmerruko.osgidashboard.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;
import org.vaadin.mmerruko.griddashboard.IWidgetContribution;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.themes.ValoTheme;

@org.osgi.service.component.annotations.Component(scope = ServiceScope.PROTOTYPE, service = WidgetToolbar.class)
public class WidgetToolbar extends Window {
    private FormLayout content;
    private Map<IWidgetContribution, Component> widgets = Collections
            .synchronizedMap(new HashMap<>());

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

    @Activate
    void start() {
        for (Component widget : widgets.values()) {
            if (widget.getParent() != content) {
                UI ui = getUI();
                if (ui != null) {
                    ui.access(() -> {
                        content.addComponent(widget);
                    });
                } else {
                    content.addComponent(widget);
                }
            }
        }
    }

    @Deactivate
    void stop() {

    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, service = IWidgetContribution.class, policy = ReferencePolicy.DYNAMIC)
    void bindWidgetContribution(IWidgetContribution widget) {
        Label createToolbarItem = createToolbarItem(widget);
        widgets.put(widget, createToolbarItem);
        UI ui = getUI();
        if (ui != null) {
            ui.access(() -> {
                content.addComponent(createToolbarItem);
            });
        } else {
            content.addComponent(createToolbarItem);
        }
    }

    void unbindWidgetContribution(IWidgetContribution widget) {
        Component remove = widgets.remove(widget);
        UI ui = getUI();
        if (remove != null) {
            if (ui != null) {
                ui.access(() -> {
                    content.removeComponent(remove);
                });
            } else {
                content.removeComponent(remove);
            }
        }

    }

    private Label createToolbarItem(IWidgetContribution contribution) {
        Label toolbarItem = new Label(contribution.getDefaultWidgetTitle());
        toolbarItem.setWidth("100%");
        toolbarItem.addStyleName(ValoTheme.LABEL_COLORED);

        DragSourceExtension<Label> sourceExtension = new DragSourceExtension<>(
                toolbarItem);
        sourceExtension.addDragStartListener(e -> {
            sourceExtension.setDragData(contribution.getTypeIdentifier());
        });
        sourceExtension.addDragEndListener(e -> {
            sourceExtension.setDragData(null);
        });

        return toolbarItem;
    }

}

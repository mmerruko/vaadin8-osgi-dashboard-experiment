package org.vaadin.mmerruko.osgidashboard.widgetset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.vaadin.mmerruko.griddashboard.IWidgetContribution;
import org.vaadin.mmerruko.griddashboard.IWidgetRegistry;
import org.vaadin.mmerruko.griddashboard.WidgetStatusListener;
import org.vaadin.mmerruko.griddashboard.model.Widget;

import com.vaadin.ui.Component;

@org.osgi.service.component.annotations.Component(service = IWidgetRegistry.class)
public class DefaultWidgetFactory implements IWidgetRegistry {
    private Map<String, IWidgetContribution> contributions = Collections
            .synchronizedMap(new LinkedHashMap<>());
    private List<WidgetStatusListener> listeners = Collections
            .synchronizedList(new ArrayList<>());

    @Override
    public Widget createDefaultWidget(String typeID) {
        IWidgetContribution contribution = contributions.get(typeID);
        if (contribution != null) {
            return contribution.createDefaultWidget();
        }
        return null;
    }

    @Override
    public Component createWidgetComponent(String typeID) {
        IWidgetContribution contribution = contributions.get(typeID);
        if (contribution != null) {
            return contribution.createWidgetComponent();
        }
        return null;
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, service = IWidgetContribution.class, policy = ReferencePolicy.DYNAMIC)
    void bindContribution(IWidgetContribution contribution) {
        if (contributions.containsKey(contribution.getTypeIdentifier())) {
            // TODO LOG
        }
        contributions.put(contribution.getTypeIdentifier(), contribution);

        for (WidgetStatusListener l : new ArrayList<>(listeners)) {
            l.widgetTypeEnabled(contribution.getTypeIdentifier());
        }
    }

    void unbindContribution(IWidgetContribution contribution) {
        contributions.remove(contribution.getTypeIdentifier());
        for (WidgetStatusListener l : new ArrayList<>(listeners)) {
            l.widgetTypeDisabled(contribution.getTypeIdentifier());
        }
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, service = WidgetStatusListener.class, policy = ReferencePolicy.DYNAMIC)
    void bindWidgetStatusListener(WidgetStatusListener listener) {
        listeners.add(listener);
    }

    void unbindWidgetStatusListener(WidgetStatusListener listener) {
        listeners.remove(listener);
    }
}

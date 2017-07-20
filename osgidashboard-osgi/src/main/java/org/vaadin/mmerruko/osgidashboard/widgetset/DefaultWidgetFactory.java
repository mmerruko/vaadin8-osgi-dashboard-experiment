package org.vaadin.mmerruko.osgidashboard.widgetset;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.vaadin.mmerruko.griddashboard.GridDashboard.IWidgetRegistry;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.vaadin.mmerruko.griddashboard.IWidgetContribution;
import org.vaadin.mmerruko.griddashboard.Widget;

import com.vaadin.ui.Component;

@org.osgi.service.component.annotations.Component(service = IWidgetRegistry.class)
public class DefaultWidgetFactory implements IWidgetRegistry {
    private Map<String, IWidgetContribution> contributions = Collections
            .synchronizedMap(new LinkedHashMap<>());

    @Override
    public boolean isKnownType(String typeID) {
        return contributions.containsKey(typeID);
    }

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
    }
    
    void unbindContribution(IWidgetContribution contribution) {
        contributions.remove(contribution.getTypeIdentifier());
    }
}

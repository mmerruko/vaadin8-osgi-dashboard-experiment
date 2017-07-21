package org.vaadin.mmerruko.osgidashboard.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.vaadin.addon.vol3.OLMap;
import org.vaadin.addon.vol3.OLView;
import org.vaadin.addon.vol3.OLViewOptions;
import org.vaadin.addon.vol3.client.OLCoordinate;
import org.vaadin.addon.vol3.client.OLExtent;
import org.vaadin.addon.vol3.client.Projections;
import org.vaadin.addon.vol3.client.control.OLMousePositionControl;
import org.vaadin.addon.vol3.client.control.OLScaleLineControl;
import org.vaadin.addon.vol3.feature.OLFeature;
import org.vaadin.addon.vol3.feature.OLGeometry;
import org.vaadin.addon.vol3.feature.OLLineString;
import org.vaadin.addon.vol3.feature.OLPoint;
import org.vaadin.addon.vol3.interaction.OLDrawInteraction;
import org.vaadin.addon.vol3.interaction.OLDrawInteractionOptions;
import org.vaadin.addon.vol3.interaction.OLInteraction;
import org.vaadin.addon.vol3.interaction.OLModifyInteraction;
import org.vaadin.addon.vol3.interaction.OLSelectInteraction;
import org.vaadin.addon.vol3.layer.OLTileLayer;
import org.vaadin.addon.vol3.layer.OLVectorLayer;
import org.vaadin.addon.vol3.source.OLSource;
import org.vaadin.addon.vol3.source.OLTileWMSSource;
import org.vaadin.addon.vol3.source.OLTileWMSSourceOptions;
import org.vaadin.addon.vol3.source.OLVectorSource;
import org.vaadin.mmerruko.griddashboard.IWidgetContribution;
import org.vaadin.mmerruko.griddashboard.model.Widget;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@org.osgi.service.component.annotations.Component(immediate = true)
public class MapWidgetContribution implements IWidgetContribution {
    private static final String MAP_WIDGET_TITLE = "Map Widget";
    
    private static final String SELECT_MODE = "select mode";
    private static final String EDIT_MODE = "edit mode";
    private static final String DRAW_MODE = "draw mode";

    private static final int ZOOM = 4;
    private static final int LON = -10997148;
    private static final int LAT = 4569099;

    private OLMap map;
    private OLTileLayer baseLayer;
    private OLVectorLayer vectorLayer;

    @Override
    public Component createWidgetComponent() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        // create map instance
        map = new OLMap();
        map.setView(createView());
        map.setSizeFull();
        layout.addComponent(map);
        layout.setExpandRatio(map, 1.0f);
        // add layers to map
        baseLayer = new OLTileLayer(createTileSource());
        baseLayer.setExtent(new OLExtent(new OLCoordinate(-13884991, 2870341),
                new OLCoordinate(-7455066, 6338219)));

        map.addLayer(baseLayer);
        vectorLayer = new OLVectorLayer(createVectorSource());
        vectorLayer.setLayerVisible(false);
        map.addLayer(vectorLayer);
        // create footer controls
        layout.addComponent(createFooterControls());
        // add some map controls
        createMapControls();
        // add initial interaction
        map.addInteraction(new OLSelectInteraction());
        return layout;
    }

    private OLView createView() {
        OLViewOptions options = new OLViewOptions();
        OLView view = new OLView(options);
        view.setZoom(ZOOM);
        view.setCenter(LON, LAT);

        return view;
    }

    private OLSource createTileSource() {
        OLTileWMSSourceOptions options = new OLTileWMSSourceOptions();
        options.setUrl("https://ahocevar.com/geoserver/wms");

        Map<String, String> params = new HashMap<>();
        params.put("LAYERS", "topp:states");
        params.put("TILED", "true");

        options.setParams(params);
        options.setServerType("geoserver");

        OLTileWMSSource source = new OLTileWMSSource(options);
        return source;
    }

    private OLSource createVectorSource() {
        OLVectorSource source = new OLVectorSource();
        source.addFeature(createPointFeature(-50, 0));
        source.addFeature(createPointFeature(50, 0));
        source.addFeature(createPointFeature(-50, 50));
        source.addFeature(createPointFeature(50, 50));
        source.addFeature(createRectangleFeature("rect", -50, 0, 100, 50));
        return source;
    }

    private void createMapControls() {
        map.setMousePositionControl(new OLMousePositionControl());
        map.getMousePositionControl().projection = Projections.EPSG4326;
        map.setScaleLineControl(new OLScaleLineControl());
    }

    private ComponentContainer createFooterControls() {
        HorizontalLayout controls = new HorizontalLayout();
        controls.setSpacing(true);
        controls.setMargin(true);
        // create button that toggles vector layer
        Button toggleVector = new Button("toggle vector layer");
        toggleVector.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                vectorLayer.setLayerVisible(!vectorLayer.isLayerVisible());
            }
        });
        controls.addComponent(toggleVector);
        // create button that resets view
        Button resetView = new Button("reset view");
        resetView.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                map.getView().setCenter(0, 0);
                map.getView().setZoom(1);
            }
        });
        controls.addComponent(resetView);
        // create chooser for current interaction mode
        ComboBox<String> interactionMode = new ComboBox<>();
        List<String> modes = Arrays.asList(SELECT_MODE, EDIT_MODE, DRAW_MODE);
        interactionMode.setItems(modes);
        interactionMode.setEmptySelectionAllowed(false);
        interactionMode.setTextInputAllowed(false);
        interactionMode.addValueChangeListener(e -> {
            toggleInteractionMode(e.getValue());
        });
        interactionMode.setValue(SELECT_MODE);
        controls.addComponent(interactionMode);
        return controls;
    }

    private void toggleInteractionMode(String mode) {
        clearInteractions();
        if (mode.equals(SELECT_MODE)) {
            map.addInteraction(new OLSelectInteraction());
        } else if (mode.equals(EDIT_MODE)) {
            map.addInteraction(new OLModifyInteraction(vectorLayer));
        } else if (mode.equals(DRAW_MODE)) {
            map.addInteraction(new OLDrawInteraction(vectorLayer,
                    OLDrawInteractionOptions.DrawingType.LINESTRING));
        } else {
            Notification.show("Unknown interaction mode : " + mode);
        }
    }

    private void clearInteractions() {
        for (OLInteraction interaction : map.getInteractions()) {
            map.removeInteraction(interaction);
        }
    }

    private OLGeometry createPointFeature(double x, double y) {
        return new OLPoint(x, y);
    }

    private OLFeature createRectangleFeature(String id, double x, double y,
            double width, double height) {
        OLFeature testFeature = new OLFeature(id);
        OLLineString lineString = new OLLineString();
        lineString.add(new OLCoordinate(x, y));
        lineString.add(new OLCoordinate(x + width, y));
        lineString.add(new OLCoordinate(x + width, y + height));
        lineString.add(new OLCoordinate(x, y + height));
        lineString.add(new OLCoordinate(x, y));
        testFeature.setGeometry(lineString);
        return testFeature;
    }

    @Override
    public Widget createDefaultWidget() {
        return new Widget(UUID.randomUUID().toString(), getTypeIdentifier());
    }

    @Override
    public String getTypeIdentifier() {
        Bundle bundle = FrameworkUtil.getBundle(MapWidgetContribution.class);
        return String.format("%s_%s", bundle.getSymbolicName(),
                MapWidgetContribution.class.getSimpleName());
    }

    @Override
    public String getDefaultWidgetTitle() {
        return MAP_WIDGET_TITLE;
    }

}

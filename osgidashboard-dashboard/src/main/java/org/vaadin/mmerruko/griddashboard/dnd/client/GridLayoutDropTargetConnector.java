package org.vaadin.mmerruko.griddashboard.dnd.client;

import java.util.List;
import java.util.Map;

import org.vaadin.mmerruko.griddashboard.dnd.GridLayoutDropTargetExtension;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.DropTargetExtensionConnector;
import com.vaadin.client.ui.VGridLayout.Cell;
import com.vaadin.client.ui.gridlayout.GridLayoutConnector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.dnd.DropTargetState;
import com.vaadin.shared.ui.gridlayout.GridLayoutState.ChildComponentData;

@Connect(GridLayoutDropTargetExtension.class)
public class GridLayoutDropTargetConnector
        extends DropTargetExtensionConnector {

    private static final String DRAG_OVER_SLOT_STYLE = "v-gridlayout-slot-over";

    private String currentStyleName;

    private GridLayoutConnector gridLayoutConnector;

    private Element latestTargetElement;

    @Override
    protected void extend(ServerConnector target) {
        gridLayoutConnector = (GridLayoutConnector) target;

        super.extend(target);
    }

    @Override
    protected void sendDropEventToServer(List<String> types,
            Map<String, String> data, String dropEffect,
            NativeEvent dropEvent) {

        ChildComponentData cellData = getTargetElementContainerSlot(
                (Element) dropEvent.getEventTarget().cast().cast());
        Cell cell = gridLayoutConnector.getWidget().getCell(cellData.row1,
                cellData.column1);
        Element slotElement = cell.slot.getWrapperElement();

        MouseEventDetails mouseEventDetails = MouseEventDetailsBuilder
                .buildMouseEventDetails(dropEvent, slotElement);

        getRpcProxy(GridLayoutDropTargetRpc.class).drop(types, data, dropEffect,
                cellData.column1, cellData.row1, mouseEventDetails);
    }

    @Override
    protected void addDragOverStyle(NativeEvent event) {
        ChildComponentData data = getTargetElementContainerSlot(
                (Element) event.getEventTarget().cast());
        Cell cell = gridLayoutConnector.getWidget().getCell(data.row1,
                data.column1);

        Element slotElement = cell.slot.getWrapperElement();

        if (latestTargetElement != null && slotElement != latestTargetElement) {
            removeStyles(latestTargetElement);
        }

        latestTargetElement = slotElement;

        // Add or replace class name if changed
        if (!slotElement.hasClassName(DRAG_OVER_SLOT_STYLE)) {
            if (currentStyleName != null) {
                slotElement.removeClassName(currentStyleName);
            }
            slotElement.addClassName(DRAG_OVER_SLOT_STYLE);
            currentStyleName = DRAG_OVER_SLOT_STYLE;
        }
    }

    @Override
    protected void removeDragOverStyle(NativeEvent event) {
        ChildComponentData data = getTargetElementContainerSlot(
                (Element) event.getEventTarget().cast());
        if (data != null) {
            Cell cell = gridLayoutConnector.getWidget().getCell(data.row1,
                    data.column1);

            Element slotElement = cell.slot.getWrapperElement();

            removeStyles(slotElement);
        }
    }

    private void removeStyles(Element element) {
        element.removeClassName(DRAG_OVER_SLOT_STYLE);
    }

    protected ChildComponentData getTargetElementContainerSlot(Element source) {
        for (ComponentConnector child : gridLayoutConnector
                .getChildComponents()) {
            Cell cell = gridLayoutConnector.getWidget().widgetToCell
                    .get(child.getWidget());
            Element slotElement = cell.slot.getWrapperElement();

            if (cell != null && slotElement.isOrHasChild(source)) {
                return gridLayoutConnector.getState().childData.get(child);
            }

        }
        return null;
    }

    @Override
    public DropTargetState getState() {
        return (DropTargetState) super.getState();
    }
}

package org.vaadin.mmerruko.griddashboard.ui.dnd;

import java.util.LinkedHashMap;
import java.util.Map;

import org.vaadin.mmerruko.griddashboard.ui.dnd.client.GridLayoutDropTargetRpc;

import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.grid.GridDropTargetState;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.dnd.DropTargetExtension;

public class GridLayoutDropTargetExtension
        extends DropTargetExtension<GridLayout> {

    public GridLayoutDropTargetExtension(GridLayout target) {
        super(target);
    }

    public Registration addGridLayoutDropListener(
            GridLayoutDropListener listener) {
        return addListener(GridLayoutDropEvent.class, listener,
                GridLayoutDropListener.DROP_METHOD);
    }

    public void setDropThreshold(int threshold) {
        getState().dropThreshold = threshold;
    }

    public int getDropThreshold() {
        return getState(false).dropThreshold;
    }

    @Override
    protected void registerDropTargetRpc() {
        registerRpc((GridLayoutDropTargetRpc) (types, data, dropEffect, column,
                row, mouseEventDetails) -> {

            // Create a linked map that preserves the order of types
            Map<String, String> dataPreserveOrder = new LinkedHashMap<>();
            types.forEach(type -> dataPreserveOrder.put(type, data.get(type)));

            GridLayoutDropEvent event = new GridLayoutDropEvent(getParent(),
                    dataPreserveOrder,
                    DropEffect.valueOf(dropEffect.toUpperCase()),
                    getUI().getActiveDragSource(), column, row,
                    mouseEventDetails);

            fireEvent(event);
        });
    }

    @Override
    protected GridDropTargetState getState() {
        return (GridDropTargetState) super.getState();
    }

    @Override
    protected GridDropTargetState getState(boolean markAsDirty) {
        return (GridDropTargetState) super.getState(markAsDirty);
    }
}

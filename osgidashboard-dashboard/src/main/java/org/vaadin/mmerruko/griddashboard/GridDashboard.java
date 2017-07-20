package org.vaadin.mmerruko.griddashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.vaadin.mmerruko.griddashboard.dnd.GridLayoutDropTargetExtension;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.gridlayout.GridLayoutState.ChildComponentData;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.dnd.DragSourceExtension;

public class GridDashboard extends GridLayout {
    public interface IWidgetRegistry {
        Widget createDefaultWidget(String typeID);

        Component createWidgetComponent(String typeID);

        boolean isKnownType(String typeID);
    }

    private Optional<IWidgetRegistry> registry = Optional.empty();

    private Map<Widget, DashboardWidgetFrame> componentToWidget = new LinkedHashMap<>();
    private Map<String, List<DashboardWidgetFrame>> typeToWidget = new LinkedHashMap<>();

    private class Placeholder extends CssLayout {
        public Placeholder() {
            addStyleName("dashboard-slot-placeholder");
        }
    }

    public GridDashboard() {
        super(3, 2);
        addStyleName("dashboard");

        setSpacing(true);
        setSizeFull();

        fillWithPlaceholders();
        configureDrop();
    }

    private void configureDrop() {
        GridLayoutDropTargetExtension dropTarget = new GridLayoutDropTargetExtension(
                this);
        dropTarget.addGridLayoutDropListener(dropEvent -> {
            dropEvent.getDragData().ifPresent(data -> {
                if (data instanceof DashboardWidgetFrame) {
                    changeWidgetLocationRequest(dropEvent.getColumn(),
                            dropEvent.getRow(), (DashboardWidgetFrame) data);
                } else if (data instanceof String) {
                    acceptDrop(dropEvent.getColumn(), dropEvent.getRow(),
                            (String) data);
                } else {
                    String type = data != null ? data.getClass().getName()
                            : "null";
                    String message = String
                            .format("Unrecognized type of data '%s'", type);
                    Notification.show(message, Type.ERROR_MESSAGE);
                }
            });
        });
    }

    private void acceptDrop(int column, int row, String data) {
        registry.ifPresent(f -> {
            if (canAddWidget(column, row)) {
                Widget widget = f.createDefaultWidget(data);
                addWidget(widget, column, row);
            }

        });
    }

    private DashboardWidgetFrame wrap(Widget widget, Component component) {
        DashboardWidgetFrame widgetFrame = new DashboardWidgetFrame(component);
        widgetFrame.setWidgetTitle(widget.getTitle());
        widgetFrame.addMenuAction("Resize", VaadinIcons.RESIZE_V, () -> {
            SizeDialog dialog = new SizeDialog();

            dialog.setResizeCallback((width, height) -> {
                if (!isNewWidgetSizeValid(widget, width, height)) {
                    Notification.show("Ivalid Size!",
                            Notification.Type.ERROR_MESSAGE);
                    return false;
                }

                widget.setHeight(height);
                widget.setWidth(width);

                setWidgetSize(widget, width, height);
                return true;

            });

            Area area = getComponentArea(widgetFrame);
            dialog.show(area.getColumn2() - area.getColumn1() + 1,
                    area.getRow2() - area.getRow1() + 1);

        });
        configureInternalDashboardDnd(widgetFrame);
        return widgetFrame;
    }

    private void configureInternalDashboardDnd(DashboardWidgetFrame widget) {
        DragSourceExtension<DashboardWidgetFrame> dragSource = new DragSourceExtension<>(
                widget);
        dragSource.addDragStartListener(e -> {
            dragSource.setDragData(widget);
        });
        dragSource.addDragEndListener(e -> {
            dragSource.setDragData(null);
        });
    }

    private void changeWidgetLocationRequest(int column, int row,
            DashboardWidgetFrame sourceWidget) {
        Area sourceArea = getComponentArea(sourceWidget);
        int sourceWidth = sourceArea.getColumn2() - sourceArea.getColumn1() + 1;
        int sourceHeight = sourceArea.getRow2() - sourceArea.getRow1() + 1;

        if (isTargetAreaEmpty(sourceWidget, column, row,
                column + sourceWidth - 1, row + sourceHeight - 1)) {
            removeComponent(sourceWidget);
            removePlaceholders();
            addComponent(sourceWidget, column, row, column + sourceWidth - 1,
                    row + sourceHeight - 1);
            fillWithPlaceholders();
        } else {
            DashboardWidgetFrame targetWidget = (DashboardWidgetFrame) getComponent(
                    column, row);
            Area targetArea = getComponentArea(targetWidget);

            int targetWidth = targetArea.getColumn2() - targetArea.getColumn1()
                    + 1;
            int targetHeight = targetArea.getRow2() - targetArea.getRow1() + 1;
            if (sourceWidth == targetWidth && sourceHeight == targetHeight) {
                removePlaceholders();
                removeComponent(targetWidget);
                removeComponent(sourceWidget);

                addComponent(sourceWidget, targetArea.getColumn1(),
                        targetArea.getRow1(), targetArea.getColumn2(),
                        targetArea.getRow2());
                addComponent(targetWidget, sourceArea.getColumn1(),
                        sourceArea.getRow1(), sourceArea.getColumn2(),
                        sourceArea.getRow2());
                fillWithPlaceholders();
            } else {
                // Check if there is enough space for a swap
                boolean targetAreaEmpty = isTargetAreaEmpty(
                        Arrays.asList(sourceWidget, targetWidget),
                        sourceArea.getColumn1(), sourceArea.getRow1(),
                        sourceArea.getColumn1() + targetWidth - 1,
                        sourceArea.getRow1() + targetHeight - 1);

                boolean sourceAreaEmpty = isTargetAreaEmpty(
                        Arrays.asList(sourceWidget, targetWidget),
                        targetArea.getColumn1(), targetArea.getRow1(),
                        targetArea.getColumn1() + sourceWidth - 1,
                        targetArea.getRow1() + sourceHeight - 1);
                if (targetAreaEmpty && sourceAreaEmpty) {
                    removePlaceholders();
                    removeComponent(targetWidget);
                    removeComponent(sourceWidget);

                    addComponent(sourceWidget, targetArea.getColumn1(),
                            targetArea.getRow1(),
                            targetArea.getColumn1() + sourceWidth - 1,
                            targetArea.getRow1() + sourceHeight - 1);

                    addComponent(targetWidget, sourceArea.getColumn1(),
                            sourceArea.getRow1(),
                            sourceArea.getColumn1() + targetWidth - 1,
                            sourceArea.getRow1() + targetHeight - 1);
                    fillWithPlaceholders();
                }
            }
        }
    }

    /**
     * Check if the he new dimensions are legal given the state of the
     * dashboard.
     * 
     * @param columns
     *            the new number of columns in the dashboard
     * @param rows
     *            the new number of rows in the dashboard
     * @return false if any existing components would fall outside the new
     *         bounds
     */
    public boolean canSetDimensions(int columns, int rows) {
        for (Entry<Connector, ChildComponentData> data : getState().childData
                .entrySet()) {
            if (data.getKey() instanceof Placeholder) {
                continue;
            }

            if (data.getValue().column2 >= columns
                    || data.getValue().row2 >= rows) {
                return false;
            }
        }

        return true;
    }

    /**
     * Set the dimentions of the dashboard
     * 
     * @param columns
     *            the new number of columns
     * @param rows
     *            the new number of rows
     * @throws IllegalArgumentException
     *             when the number of rows or columns would make existing
     *             components fall outside the new bounds
     */
    public void setDimensions(int columns, int rows) {
        removePlaceholders();
        setRows(rows);
        setColumns(columns);
        fillWithPlaceholders();
    }

    public void setWidgetRegistry(IWidgetRegistry factory) {
        this.registry = Optional.ofNullable(factory);
    }

    private void fillWithPlaceholders() {
        for (int row = 0; row < getRows(); row++) {
            for (int column = 0; column < getColumns(); column++) {
                Component component = getComponent(column, row);
                if (component == null) {
                    addComponent(new Placeholder(), column, row);
                }
            }
        }
    }

    /**
     * Returns true if the widget is contained in the dashboard and if resizing
     * it will not overlap with an existing component
     * 
     * @param widget
     * @param width
     * @param height
     * @return
     */
    public boolean isNewWidgetSizeValid(Widget widget, int width, int height) {
        DashboardWidgetFrame widgetFrame = componentToWidget.get(widget);
        if (widgetFrame == null) {
            return false;
        }

        Area area = getComponentArea(widgetFrame);
        if (area == null) {
            throw new IllegalStateException(
                    "Internal Error : Can't find an area for the given component's widget");
        }

        int column1 = area.getColumn1();
        int newColumn2 = area.getColumn1() + width - 1;

        int row1 = area.getRow1();
        int newRow2 = area.getRow1() + height - 1;

        return isTargetAreaEmpty(widgetFrame, column1, row1, newColumn2,
                newRow2);
    }

    private boolean isTargetAreaEmpty(DashboardWidgetFrame widget, int column1,
            int row1, int column2, int row2) {
        return isTargetAreaEmpty(Collections.singletonList(widget), column1,
                row1, column2, row2);
    }

    private boolean isTargetAreaEmpty(List<DashboardWidgetFrame> list,
            int column1, int row1, int column2, int row2) {
        if (column2 >= getColumns() || row2 >= getRows()) {
            return false;
        }

        for (Entry<Connector, ChildComponentData> data : getState().childData
                .entrySet()) {
            /*
             * This is the entry for the widget we are checking there's no point
             * for checking for an overlap
             */
            if (list.contains(data.getKey())) {
                continue;
            }

            // Ignore placeholders
            if (data.getKey() instanceof Placeholder) {
                continue;
            }

            ChildComponentData dataArea = data.getValue();
            /*
             * We know that column1/row1 for the existing column don't overlap
             * with existing areas.
             * 
             * We need to check if newColumn2 and newRow2 can cause an overlap.
             */
            if (column1 <= dataArea.column2 && row1 <= dataArea.row2
                    && column2 >= dataArea.column1 && row2 >= dataArea.row1) {
                return false;
            }
        }
        return true;
    }

    public void setWidgetSize(Widget widget, int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException(
                    "Width and height have to be equal to or greater than 1!");
        }
        if (!isNewWidgetSizeValid(widget, width, height)) {
            throw new IllegalArgumentException(
                    "The component can't be set to the requested size!");
        }

        DashboardWidgetFrame widgetFrame = componentToWidget.get(widget);

        Area area = getComponentArea(widgetFrame);
        removeComponent(widgetFrame);
        removePlaceholders();

        addComponent(widgetFrame, area.getColumn1(), area.getRow1(),
                area.getColumn1() + width - 1, area.getRow1() + height - 1);
        fillWithPlaceholders();

    }

    private void removePlaceholders() {
        for (int row = 0; row <= getRows(); row++) {
            for (int column = 0; column <= getColumns(); column++) {
                Component component = getComponent(column, row);
                if (component instanceof Placeholder) {
                    removeComponent(component);
                }
            }
        }
    }

    public boolean canAddWidget(int column, int row) {
        return isTargetAreaEmpty(Collections.emptyList(), column, row, column,
                row);
    }

    public boolean canAddWidget(int column, int row, int width, int height) {
        return isTargetAreaEmpty(Collections.emptyList(), column, row, column,
                row);
    }

    public void addWidget(Widget widget, int column, int row) {
        if (!canAddWidget(column, row, widget.getWidth(), widget.getHeight())) {
            String message = String.format(
                    "Can't add widget with width %d and height %d in column %d and row %d! Widget already exists in that area!",
                    widget.getWidth(), widget.getHeight(), column, row);
            throw new IllegalArgumentException(message);
        }

        String typeID = widget.getWidgetTypeIdentifier();

        final Component widgetComponent;
        if (registry.isPresent()) {
            widgetComponent = registry.get().createWidgetComponent(typeID);
        } else {
            widgetComponent = createUnknownWidget();
        }

        DashboardWidgetFrame widgetControls = wrap(widget, widgetComponent);
        componentToWidget.put(widget, widgetControls);

        List<DashboardWidgetFrame> list = typeToWidget.get(typeID);
        if (list == null) {
            list = new ArrayList<>();
            typeToWidget.put(typeID, list);
        }
        list.add(widgetControls);

        Component placeholder = getComponent(column, row);
        if (placeholder instanceof Placeholder) {
            removeComponent(placeholder);
        }

        addComponent(widgetControls, column, row,
                column + widget.getWidth() - 1, row + widget.getHeight() - 1);
    }

    private Component createUnknownWidget() {
        return new Label("Unknown");
    }

    public void disableWidgetsByType(String typeID) {
        Notification.show("Disable Not Implemented!");
    }
}

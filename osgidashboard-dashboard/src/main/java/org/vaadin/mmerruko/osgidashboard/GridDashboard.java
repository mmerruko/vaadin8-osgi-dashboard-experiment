package org.vaadin.mmerruko.osgidashboard;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public class GridDashboard extends GridLayout {
    private int occupiedWidthInCurrentRow = 0;
    private int currentRow = 0;
    private int lastItemHeight;

    public GridDashboard() {
        super(1, 1);
        addStyleName("dashboard");
        setWidth("100%");
        setSpacing(true);
    }

    public void addWidget(String widgetTitle, Component component) {
        DashboardWidgetFrame widget = wrap(widgetTitle, component);
        addComponent(widget);
    }

    public void addWidget(String widgetTitle, Component component, int column,
            int row) {
        Component existing = getComponent(column, row);
        if (existing == null) {
            DashboardWidgetFrame widget = wrap(widgetTitle, component);
            addComponent(widget, column, row);
        } else {
            DashboardWidgetFrame wrap = wrap(widgetTitle, component);
        }
    }

    private DashboardWidgetFrame wrap(String widgetTitle, Component component) {
        DashboardWidgetFrame widget = new DashboardWidgetFrame(component);
        widget.setWidgetTitle(widgetTitle);
        return widget;
    }

    public void renderDashboardModel(DashboardModel dashboardModel) {
        removeAllComponents();
        occupiedWidthInCurrentRow = 0;
        currentRow = 0;

        setColumns(dashboardModel.getColumns());

        for (DashboardWidget widget : dashboardModel.getWidgets()) {
            switch (widget.getWidgetId()) {
            case "button":
                Button button = new Button("BWidget");
                insertComponent(button, widget.getWidth(), widget.getHeight());
                break;
            case "label":
                Label label = new Label("LWidget");
                insertComponent(label, widget.getWidth(), widget.getHeight());
                break;
            }
        }

        setHeight(getRows() * 200, Unit.PIXELS);
    }

    private void insertComponent(Component button, int width, int height) {
        if (getColumns() < width) {
            width = getColumns();
        }

        if (occupiedWidthInCurrentRow + width > getColumns()) {
            occupiedWidthInCurrentRow = 0;
            currentRow += lastItemHeight;
        }

        if (currentRow + height >= getRows()) {
            setRows(currentRow + height);
        }

        addComponent(wrap("Button", button), occupiedWidthInCurrentRow,
                currentRow, occupiedWidthInCurrentRow + width - 1,
                currentRow + height - 1);

        occupiedWidthInCurrentRow += width;
        lastItemHeight = Math.max(height, lastItemHeight);
    }
}

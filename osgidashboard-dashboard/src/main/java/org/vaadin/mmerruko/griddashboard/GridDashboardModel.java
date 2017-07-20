package org.vaadin.mmerruko.griddashboard;

import java.util.ArrayList;
import java.util.List;

public class GridDashboardModel {
    public class WidgetLocation {
        private int column;
        private int row;

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }
    }

    public class WidgetData {
        private WidgetLocation location;
        private Widget widget;

        public WidgetLocation getLocation() {
            return location;
        }

        public Widget getWidget() {
            return widget;
        }
    }

    private List<WidgetData> widgets = new ArrayList<>();
    private int dashboardWidth;
    private int dashboardHeight;

    public GridDashboardModel() {
    }

    public int getDashboardHeight() {
        return dashboardHeight;
    }

    public void setDashboardHeight(int dashboardHeight) {
        this.dashboardHeight = dashboardHeight;
    }

    public int getDashboardWidth() {
        return dashboardWidth;
    }

    public void setDashboardWidth(int dashboardWidth) {
        this.dashboardWidth = dashboardWidth;
    }

    public void addWidget(Widget widget, int column, int row) {
        WidgetData data = new WidgetData();
        data.location.column = column;
        data.location.row = row;

        data.widget = widget;

        widgets.add(data);
    }
}

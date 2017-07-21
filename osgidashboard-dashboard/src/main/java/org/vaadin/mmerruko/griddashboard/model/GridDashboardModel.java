package org.vaadin.mmerruko.griddashboard.model;

import java.util.ArrayList;
import java.util.List;

public class GridDashboardModel {
    public static class WidgetLocation {
        private int column;
        private int row;

        public WidgetLocation() {
        }
        
        public WidgetLocation(int column, int row) {
            this.column = column;
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }
        
        public int getRow() {
            return row;
        }
        
        public void setRow(int row) {
            this.row = row;
        }
    }

    public static class WidgetData {
        private WidgetLocation location;
        private Widget widget;

        public WidgetData() {
        }
        
        public WidgetLocation getLocation() {
            return location;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setLocation(WidgetLocation location) {
            this.location = location;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
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
        data.location = new WidgetLocation(column, row);
        data.location.column = column;
        data.location.row = row;

        data.widget = widget;

        widgets.add(data);
    }

    public List<WidgetData> getWidgets() {
        return widgets;
    }
}

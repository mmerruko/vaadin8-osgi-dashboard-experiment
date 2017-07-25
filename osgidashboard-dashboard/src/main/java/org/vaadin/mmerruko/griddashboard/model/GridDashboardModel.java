package org.vaadin.mmerruko.griddashboard.model;

import java.util.ArrayList;
import java.util.List;

public class GridDashboardModel {
    public static class CanvasWidgetLocation {
        private int column;
        private int row;

        public CanvasWidgetLocation() {
        }

        public CanvasWidgetLocation(int column, int row) {
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

    public static class CanvasWidgetData {
        private CanvasWidgetLocation location;
        private Widget widget;

        public CanvasWidgetData() {
        }

        public CanvasWidgetLocation getLocation() {
            return location;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setLocation(CanvasWidgetLocation location) {
            this.location = location;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }
    }

    public static class FloatingWidgetData {
        private Widget widget;
        private FloatingWidgetLocation location;

        public Widget getWidget() {
            return widget;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }

        public FloatingWidgetLocation getLocation() {
            return location;
        }

        public void setLocation(FloatingWidgetLocation location) {
            this.location = location;
        }
    }

    public static class FloatingWidgetLocation {
        private float relativeX;
        private float relativeY;

        private float relativeWidth;
        private float relativeHeight;

        public void setRelativeX(float relativeX) {
            this.relativeX = relativeX;
        }

        public float getRelativeX() {
            return relativeX;
        }

        public void setRelativeY(float relativeY) {
            this.relativeY = relativeY;
        }

        public float getRelativeY() {
            return relativeY;
        }

        public void setRelativeWidth(float relativeWidth) {
            this.relativeWidth = relativeWidth;
        }

        public float getRelativeWidth() {
            return relativeWidth;
        }

        public void setRelativeHeight(float relativeHeight) {
            this.relativeHeight = relativeHeight;
        }

        public float getRelativeHeight() {
            return relativeHeight;
        }
    }

    private List<CanvasWidgetData> widgets = new ArrayList<>();
    private List<FloatingWidgetData> floatingWidgets = new ArrayList<>();

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
        CanvasWidgetData data = new CanvasWidgetData();
        data.location = new CanvasWidgetLocation(column, row);
        data.location.column = column;
        data.location.row = row;

        data.widget = widget;

        widgets.add(data);
    }

    public List<CanvasWidgetData> getWidgets() {
        return widgets;
    }

    public List<FloatingWidgetData> getFloatingWidgets() {
        return floatingWidgets;
    }

    public void addFloatingWindowWidget(Widget widget, float relativeX,
            float relativeY, float relativeWidth, float relativeHeight) {
        FloatingWidgetData data = new FloatingWidgetData();
        FloatingWidgetLocation location = new FloatingWidgetLocation();
        
        location.setRelativeX(relativeX);
        location.setRelativeY(relativeY);
        
        location.setRelativeWidth(relativeWidth);
        location.setRelativeHeight(relativeHeight);

        data.location = location;
        data.widget = widget;

        floatingWidgets.add(data);
    }
}

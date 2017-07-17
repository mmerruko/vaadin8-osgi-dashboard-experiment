package org.vaadin.mmerruko.osgidashboard;

public class DashboardWidget {
    private int width = 1;
    private int height = 1;
    private String widgetId;

    public DashboardWidget(String widgetId) {
        this.widgetId = widgetId;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
}

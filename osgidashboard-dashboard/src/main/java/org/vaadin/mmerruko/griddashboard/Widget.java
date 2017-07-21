package org.vaadin.mmerruko.griddashboard;

public class Widget {
    private String widgetIdentifier;
    private String widgetTypeIdentifier;

    private String title;

    private int width = 1;
    private int height = 1;

    public Widget() {
    }

    public Widget(String identifier, String typeIdentifier) {
        widgetIdentifier = identifier;
        widgetTypeIdentifier = typeIdentifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getWidgetIdentifier() {
        return widgetIdentifier;
    }

    public void setWidgetIdentifier(String widgetIdentifier) {
        this.widgetIdentifier = widgetIdentifier;
    }

    public String getWidgetTypeIdentifier() {
        return widgetTypeIdentifier;
    }

    public void setWidgetTypeIdentifier(String widgetTypeIdentifier) {
        this.widgetTypeIdentifier = widgetTypeIdentifier;
    }
}

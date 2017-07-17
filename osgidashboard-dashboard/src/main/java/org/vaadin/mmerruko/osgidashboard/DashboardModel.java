package org.vaadin.mmerruko.osgidashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardModel {
    private int columns = 4;
    private List<DashboardWidget> widgets = new ArrayList<>();
    
    public List<DashboardWidget> getWidgets() {
        return Collections.unmodifiableList(widgets);
    }
    
    public void addWidget(DashboardWidget widget) {
        widgets.add(widget);
    }
    
    public void removeWidget(DashboardWidget widget) {
        widgets.remove(widget);
    }
    
    public int getColumns() {
        return columns;
    }
    
    public void setColumns(int columns) {
        this.columns = columns;
    }
}

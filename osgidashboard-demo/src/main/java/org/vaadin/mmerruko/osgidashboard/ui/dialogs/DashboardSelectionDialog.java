package org.vaadin.mmerruko.osgidashboard.ui.dialogs;

import java.util.List;
import java.util.Optional;

import org.vaadin.mmerruko.griddashboard.Dialog;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class DashboardSelectionDialog extends Dialog {
    public interface Callback {
        boolean dashboardSelected(String dashboardName);
    }

    private ComboBox<String> comboBox;
    private Optional<Callback> callback = Optional.empty();
    
    @Override
    protected Component createContents() {
        FormLayout contents = new FormLayout();
        contents.setMargin(true);
        contents.setSizeUndefined();
        
        comboBox = new ComboBox<>("Dashboard name");
        comboBox.setEmptySelectionAllowed(false);
        
        contents.addComponent(comboBox);
        return contents;
    }
    
    public void setDashboards(List<String> dashboards) {
        comboBox.setItems(dashboards);
    }
    
    @Override
    protected boolean okClicked() {
        if (callback.isPresent()) {
            return callback.get().dashboardSelected(comboBox.getValue());
        }
        return true;
    }

    public void setCallback(Callback callback) {
        this.callback = Optional.ofNullable(callback);
    }
}

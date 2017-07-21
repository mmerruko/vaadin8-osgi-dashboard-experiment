package org.vaadin.mmerruko.osgidashboard.ui.dialogs;

import java.util.Optional;

import org.vaadin.mmerruko.griddashboard.Dialog;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class NameDialog extends Dialog {
    public interface Callback {
        boolean okClicked(String name);
    }

    private Optional<Callback> callback = Optional.empty();
    private TextField name;

    @Override
    protected Component createContents() {
        FormLayout contents = new FormLayout();
        contents.setSizeUndefined();
        contents.setMargin(true);

        name = new TextField("Name");
        contents.addComponent(name);
        return contents;
    }

    public void setCallback(Callback callback) {
        this.callback = Optional.ofNullable(callback);
    }

    @Override
    protected boolean okClicked() {
        if (callback.isPresent()) {
            return callback.get().okClicked(name.getValue());
        }
        return true;
    }
}

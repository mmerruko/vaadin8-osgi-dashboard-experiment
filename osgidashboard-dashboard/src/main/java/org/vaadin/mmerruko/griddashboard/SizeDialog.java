package org.vaadin.mmerruko.griddashboard;

import java.util.Optional;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;

public class SizeDialog extends Dialog {
    public interface ResizeCallback {
        boolean setSize(int width, int height);
    }

    private IntStepper widthStepper;
    private IntStepper heightStepper;

    private Optional<ResizeCallback> callback = Optional.empty();

    public void setResizeCallback(ResizeCallback callback) {
        this.callback = Optional.ofNullable(callback);
    }

    @Override
    protected boolean okClicked() {
        if (callback.isPresent()) {
            return callback.get().setSize(widthStepper.getValue(),
                    heightStepper.getValue());
        }
        return false;
    }

    protected void cancelClicked(ClickEvent e) {
        close();
    }

    public void show(int width, int height) {
        widthStepper.setValue(width);
        heightStepper.setValue(height);

        UI.getCurrent().addWindow(this);
    }

    @Override
    protected Component createContents() {
        FormLayout form = new FormLayout();

        widthStepper = new IntStepper("Width");
        widthStepper.setMinValue(1);

        heightStepper = new IntStepper("Height");
        heightStepper.setMinValue(1);

        form.addComponent(widthStepper);
        form.addComponent(heightStepper);
        form.setMargin(true);
        form.setSizeUndefined();
        return form;
    }
}

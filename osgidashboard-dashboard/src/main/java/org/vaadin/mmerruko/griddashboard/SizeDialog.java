package org.vaadin.mmerruko.griddashboard;

import java.util.Optional;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class SizeDialog extends Window {
    public interface ResizeCallback {
        boolean setSize(int width, int height);
    }
    
    private IntStepper widthStepper;
    private IntStepper heightStepper;

    private Optional<ResizeCallback> callback = Optional.empty();
    
    public SizeDialog() {
        setWidth("40%");
        setHeight("40%");
        setModal(true);
        center();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        FormLayout form = new FormLayout();

        widthStepper = new IntStepper("Width");
        widthStepper.setMinValue(1);
        
        heightStepper = new IntStepper("Height");
        heightStepper.setMinValue(1);

        form.addComponent(widthStepper);
        form.addComponent(heightStepper);

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setWidth("100%");

        CssLayout buttonWrapper = new CssLayout();
        buttonWrapper.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button okButton = new Button("OK", e -> {
            if (okClicked(e, widthStepper.getValue(),
                    heightStepper.getValue())) {
                close();
            }
        });
        okButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        buttonWrapper.addComponent(okButton);

        buttonWrapper.addComponent(new Button("Cancel", e -> {
            cancelClicked(e);
            close();
        }));

        buttonBar.addComponent(buttonWrapper);
        buttonBar.setComponentAlignment(buttonWrapper, Alignment.MIDDLE_RIGHT);

        mainLayout.addComponent(form);
        mainLayout.addComponent(buttonBar);
        mainLayout.setExpandRatio(form, 1);

        setContent(mainLayout);

    }
    
    public void setResizeCallback(ResizeCallback callback) {
        this.callback = Optional.ofNullable(callback);
    }

    protected boolean okClicked(ClickEvent e, int width, int height) {
        if (callback.isPresent()) {
            return callback.get().setSize(width, height);
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
}

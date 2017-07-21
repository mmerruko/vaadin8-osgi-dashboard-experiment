package org.vaadin.mmerruko.griddashboard;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public abstract class Dialog extends Window {

    public Dialog() {
        setModal(true);
        setSizeUndefined();
        center();

        VerticalLayout mainLayout = new VerticalLayout();
        Panel contents = new Panel();
        contents.setContent(createContents());

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setWidth("100%");

        CssLayout buttonWrapper = new CssLayout();
        buttonWrapper.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button okButton = new Button("OK", e -> {
            if (okClicked()) {
                close();
            }
        });
        okButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        buttonWrapper.addComponent(okButton);

        buttonWrapper.addComponent(new Button("Cancel", e -> {
            cancelClicked(e);
        }));

        buttonBar.addComponent(buttonWrapper);
        buttonBar.setComponentAlignment(buttonWrapper, Alignment.MIDDLE_RIGHT);

        mainLayout.addComponent(contents);
        mainLayout.addComponent(buttonBar);
        mainLayout.setExpandRatio(contents, 1);

        setContent(mainLayout);

    }

    protected abstract Component createContents();

    protected boolean okClicked() {
        return true;
    }

    protected void cancelClicked(ClickEvent e) {
        close();
    }
}

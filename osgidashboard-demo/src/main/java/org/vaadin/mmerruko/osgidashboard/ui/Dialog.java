package org.vaadin.mmerruko.osgidashboard.ui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class Dialog extends Window {

    private Panel mainPanel;

    public Dialog() {
        setWidth("40%");
        setHeight("40%");
        setModal(true);
        center();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        mainPanel = new Panel();
        mainPanel.setSizeFull();

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setWidth("100%");

        CssLayout buttonWrapper = new CssLayout();
        buttonWrapper.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button okButton = new Button("OK", e -> {
            if (okClicked(e)) {
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

        mainLayout.addComponent(mainPanel);
        mainLayout.addComponent(buttonBar);
        mainLayout.setExpandRatio(mainPanel, 1);

        setContent(mainLayout);

    }

    protected boolean okClicked(ClickEvent e) {
        return false;
    }

    protected void cancelClicked(ClickEvent e) {

    }

    public void setDialogContents(Component contents) {
        mainPanel.setContent(contents);
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }
}

package org.vaadin.easyuploads.demoandtestapp;


import org.vaadin.addonhelpers.AbstractTest;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.easyuploads.UploadField;

@Theme("valo")
public class LimitingSizeAndType extends AbstractTest {

    @SuppressWarnings("serial")
    @Override
    public Component getTestComponent() {
        VerticalLayout mainWindow = new VerticalLayout();

        final UploadField uploadFieldHtml5Configured = new UploadField();
        uploadFieldHtml5Configured.setCaption(" fieldType: just images, max 1000000");
        uploadFieldHtml5Configured.setAcceptFilter("image/*");
        uploadFieldHtml5Configured.setMaxFileSize(1000000);


        Button b = new Button("Show value");
        b.addClickListener((ClickEvent event) -> {
            Object value = uploadFieldHtml5Configured.getValue();
            Notification.show("Value:" + value);
        });
        mainWindow.addComponent(uploadFieldHtml5Configured);
        mainWindow.addComponent(b);
        return mainWindow;
    }

}

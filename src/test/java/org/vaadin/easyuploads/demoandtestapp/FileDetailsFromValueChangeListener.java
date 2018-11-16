package org.vaadin.easyuploads.demoandtestapp;


import org.vaadin.addonhelpers.AbstractTest;
import com.vaadin.annotations.Theme;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.easyuploads.UploadField;

@Theme("valo")
public class FileDetailsFromValueChangeListener extends AbstractTest {

    @SuppressWarnings("serial")
    @Override
    public Component getTestComponent() {
        VerticalLayout mainWindow = new VerticalLayout();

        final UploadField uploadField = new UploadField();
 
        uploadField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {

                String lastFileName = uploadField.getLastFileName();
                String lastMimeType = uploadField.getLastMimeType();

                Notification.show(lastFileName + " " + lastMimeType);

            }
        });

        Button b = new Button("Show value");
        b.addClickListener((ClickEvent event) -> {
            Object value = uploadField.getValue();
            Notification.show("Value:" + value);
        });
        mainWindow.addComponent(uploadField);
        mainWindow.addComponent(b);
        return mainWindow;
    }

}

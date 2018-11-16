package org.vaadin.easyuploads.demoandtestapp;

import org.vaadin.addonhelpers.AbstractTest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.easyuploads.UploadField;

public class ClearTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        VerticalLayout layout = new VerticalLayout();
        final UploadField uplDocument = new UploadField();
        uplDocument.setButtonCaption("...Select File");
        uplDocument.setDisplayUpload(false);
        
        uplDocument.addValueChangeListener(event -> Notification.show("File uploaded" + uplDocument.getLastFileName() + " IsEmpty:" + uplDocument.isEmpty()));
        
        layout.addComponent(uplDocument);
        
        Button button = new Button("Test is clear + isEmtpy");
        button.addClickListener(event -> {
            uplDocument.clear();
            Notification.show("IsEmpty: " + uplDocument.isEmpty());
        });
        layout.addComponent(button);
        
        return layout;
    }


}

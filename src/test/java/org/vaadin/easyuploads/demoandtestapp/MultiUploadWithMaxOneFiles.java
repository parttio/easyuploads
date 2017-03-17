package org.vaadin.easyuploads.demoandtestapp;

import com.vaadin.v7.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.io.File;

import org.vaadin.easyuploads.*;

import org.vaadin.addonhelpers.AbstractTest;

public class MultiUploadWithMaxOneFiles extends AbstractTest {

    @Override
    public Component getTestComponent() {
        VerticalLayout layout = new VerticalLayout();
        
        MultiFileUpload multiFileUpload = new MultiFileUpload() {
            @Override
            protected void handleFile(File file, String fileName, String mimeType, long length) {
                Notification.show("File handled");
            }
        };
        
        multiFileUpload.setMaxFileCount(1);
        
        layout.addComponent(multiFileUpload);
        
        return layout;
    }


}

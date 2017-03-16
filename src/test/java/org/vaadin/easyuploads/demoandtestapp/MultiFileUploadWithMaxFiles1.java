package org.vaadin.easyuploads.demoandtestapp;

import java.io.*;

import org.vaadin.easyuploads.*;
import com.vaadin.ui.*;
import org.vaadin.addonhelpers.AbstractTest;

public class MultiFileUploadWithMaxFiles1 extends AbstractTest {

    @Override
    public Component getTestComponent() {
        VerticalLayout layout = new VerticalLayout();
        
        MultiFileUpload multiFileUpload = new MultiFileUpload() {
            @Override
            protected void handleFile(File file, String fileName, String mimeType, long length) {
                Notification.show("File receved");
            }
            
        };
        multiFileUpload.setMaxFileCount(1);
        
        multiFileUpload.setUploadButtonCaption("Upload files...");
        
        layout.addComponent(multiFileUpload);
        
        return layout;
    }

}

package org.vaadin.easyuploads.demoandtestapp;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;


import org.vaadin.addonhelpers.AbstractTest;

@Theme("valo")
public class BasicUploadTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "https://github.com/vaadin/framework/issues/8859";
    }


    @Override
    public Component getTestComponent() {
        VerticalLayout layout = new VerticalLayout();
        
        final Upload upload = new Upload();
        upload.setCaption("Raw Vaadin upload");
        
        upload.setReceiver(new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                return new ByteArrayOutputStream();
            }
        });
        
        final VerticalLayout wrap = new VerticalLayout(upload);
        layout.addComponents(wrap);

        return layout;
    }

}

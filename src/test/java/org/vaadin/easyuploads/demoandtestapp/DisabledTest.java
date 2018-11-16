package org.vaadin.easyuploads.demoandtestapp;

import java.io.File;

import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.easyuploads.UploadField;

@Theme("valo")
public class DisabledTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "See https://github.com/vaadin/framework/issues/7930";
    }

    public static class MyBean {

        private File file;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return "MyBean{" + "file=" + (file == null ? "null" : file.getName()) + '}';
        }

    }

    final UploadField file = new UploadField();
    
    MyBean bean = new MyBean();

    @Override
    public Component getTestComponent() {
        VerticalLayout layout = new VerticalLayout();
        file.setEnabled(false);
        file.setButtonCaption("...Select File");
        file.setDisplayUpload(false);

        file.addValueChangeListener(event -> Notification.show("File uploaded" + file.getLastFileName() + " IsEmpty:" + file.isEmpty()));

        layout.addComponent(file);
        
        
        final Upload upload = new Upload();
        upload.setCaption("Raw Vaadin upload");
        
        final Button b = new Button("Button");
 
        final VerticalLayout wrap = new VerticalLayout(upload, b);
        wrap.setEnabled(false);
 
        CheckBox cb = new CheckBox("Enabled");
        cb.setValue(false);
        cb.addValueChangeListener(event -> {
            final boolean nextEnabled = !file.isEnabled();
            file.setEnabled(nextEnabled);
            wrap.setEnabled(nextEnabled);
            boolean enabled = upload.isEnabled();
            Notification.show("Enabled: " + enabled);
        });
        
        layout.addComponents(wrap, cb);

        return layout;
    }

}

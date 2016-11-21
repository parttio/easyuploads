package org.vaadin.easyuploads.demoandtestapp;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.io.File;

import org.vaadin.easyuploads.*;

import org.vaadin.addonhelpers.AbstractTest;

public class DisabledTest extends AbstractTest {

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

        file.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show("File uploaded" + file.getLastFileName() + " IsEmpty:" + file.isEmpty());
            }
        });

        layout.addComponent(file);
        
        CheckBox cb = new CheckBox("Enabled");
        cb.setValue(false);
        cb.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                final boolean name = !file.isEnabled();
                file.setEnabled(name);
            }
        });
        
        layout.addComponents(cb);

        return layout;
    }

}

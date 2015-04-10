package org.vaadin.easyuploads.demoandtestapp;

import java.io.*;

import org.vaadin.easyuploads.*;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.easyuploads.UploadField.StorageMode;

import com.google.common.io.*;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.*;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.addonhelpers.AbstractTest;

@Theme("valo")
public class ByteArrayWithBeanBinding extends AbstractTest {

    public static class Entity {

        byte[] value;

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }

    }

    UploadField value = new UploadField(StorageMode.MEMORY);

    @Override
    public Component getTestComponent() {

        value.setFieldType(FieldType.BYTE_ARRAY);

        final Entity entity = new Entity();
        
        entity.setValue("Foobar".getBytes());

        BeanFieldGroup.bindFieldsUnbuffered(entity, this);

        Button b = new Button("Show entity value");
        b.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (entity.getValue() == null) {
                    Notification.show("NULL");
                } else {
                    String string = new String(entity.getValue());
                    if (string.length() > 100) {
                        string = string.substring(0, 100);
                    }
                    Notification.show(string);
                }
            }
        });

        return new VerticalLayout(value, b);
    }

    @Override
    protected void setup() {
        super.setup();
        content.setSizeUndefined();
    }

    class SlowMultiFileUpload extends MultiFileUpload {

        @Override
        protected void handleFile(File file, String fileName, String mimeType,
                long length) {
            String msg = fileName + " uploaded.";
            Notification.show(msg);
        }

        @Override
        protected FileBuffer createReceiver() {
            return new FileBuffer() {
                @Override
                public FileFactory getFileFactory() {
                    return SlowMultiFileUpload.this.getFileFactory();
                }

                @Override
                public OutputStream receiveUpload(String filename,
                        String MIMEType) {
                    final OutputStream receiveUpload = super.receiveUpload(
                            filename, MIMEType);
                    OutputStream slow = new OutputStream() {
                        private int slept;
                        private int written;

                        @Override
                        public void write(int b) throws IOException {
                            receiveUpload.write(b);
                            written++;
                            if (slept < 60000 && written % 1024 == 0) {
                                int sleep = 5;
                                slept += sleep;
                                try {
                                    Thread.sleep(sleep);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    return slow;
                }

            };
        }

    }

    private Component hr() {
        Label label = new Label("<hr>", Label.CONTENT_XHTML);
        return label;
    }

}

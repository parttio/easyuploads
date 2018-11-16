package org.vaadin.easyuploads.demoandtestapp;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.imageio.*;

import org.vaadin.easyuploads.*;

import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.vaadin.addonhelpers.AbstractTest;

public class CustomDisplayTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        VerticalLayout layout = new VerticalLayout();
        ImagePreviewField upload = new ImagePreviewField();
        upload.setCaption("Custom prview for images.");
        upload.setAcceptFilter("image/*");
        layout.addComponent(upload);
        return layout;
    }

    public class ImagePreviewField extends UploadField {
        @Override
        protected Component createDisplayComponent() {
            Image image = new Image();
            image.setHeight("100px");
            return image;
        }

        @Override
        protected void updateDisplayComponent() {
            try {
                Image image = (Image) display;
                // check if upload is an image
                if (getValue() != null && ImageIO.read(new ByteArrayInputStream((byte[]) getValue())) != null) {
                    // Update the image according to
                    // https://vaadin.com/book/vaadin7/-/page/components.embedded.html
                    SimpleDateFormat df = new SimpleDateFormat(
                            "yyyyMMddHHmmssSSS");
                    String filename = df.format(new Date()) + getLastFileName();
                    StreamResource resource = new StreamResource(
                            new ImageSource((byte[]) getValue()), filename);
                    resource.setCacheTime(0);
                    image.setSource(resource);
                } else {
                    image.setSource(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public class ImageSource implements StreamResource.StreamSource {
            private byte[] buffer;

            public ImageSource(byte[] image) {
                this.buffer = image;
            }

            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(buffer);
            }
        }
    }
}

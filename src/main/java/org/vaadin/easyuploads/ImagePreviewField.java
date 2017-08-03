/*
 * Copyright 2016 Vaadin Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.easyuploads;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

/**
 * A simple field to edit images stored as byte arrays (e.g. in database).
 */
public class ImagePreviewField extends UploadField {

    public ImagePreviewField() {
        setAcceptFilter("image/*");
        setFieldType(FieldType.BYTE_ARRAY);
        setStorageMode(StorageMode.MEMORY);
    }

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
            if (ImageIO.read(new ByteArrayInputStream(getValue())) != null) {
                // Update the image according to
                // https://vaadin.com/book/vaadin7/-/page/components.embedded.html
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String filename = df.format(new Date()) + getLastFileName();
                StreamResource resource = new StreamResource(new ImageSource(getValue()), filename);
                resource.setCacheTime(0);
                image.setSource(resource);
            } else {
                image.setSource(null);
                setValue(null);
            }
            image.markAsDirty();
            if (display.getParent() == null) {
                getRootLayout().addComponent(display);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachDeleteButton(Button b) {
        b.setIcon(FontAwesome.TRASH_O);
        b.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        b.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        getRootLayout().addComponent(b, 1);
    }

    @Override
    protected String getDeleteCaption() {
        return null;
    }

    public class ImageSource implements StreamResource.StreamSource {

        private final byte[] buffer;

        public ImageSource(byte[] image) {
            this.buffer = image;
        }

        @Override
        public InputStream getStream() {
            return new ByteArrayInputStream(buffer);
        }
    }

}

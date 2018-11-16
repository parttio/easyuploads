/*
 * Copyright 2018 Vaadin Community.
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

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 *
 * @author mstahv
 */
public class UploadField extends CustomField<byte[]> implements
        Upload.Receiver {

    private byte[] data;

    private Upload upload = new Upload(null, this);
    private String mimeType;
    private String filename;

    protected Component display;
    public Label defaultDisplay;

    private ProgressBar progress = new ProgressBar();
    private final static int MAX_SHOWN_BYTES = 5;
    private ByteArrayOutputStream byteArrayOutputStream;

    private Button clearBtn = new Button(VaadinIcons.TRASH);
    private VerticalLayout rootLayout;

    public UploadField() {

        clearBtn.addClickListener(e -> {
            data = null;
            fireValueChange();
            updateDisplay();
        });

        upload.addStartedListener(e -> {
            progress.setVisible(true);
            progress.setValue(0f);
            progress.setCaption(e.getFilename() + " " + e.getContentLength() + "B");
        });
        upload.addFinishedListener(e -> {
            progress.setVisible(false);
            filename = e.getFilename();
            data = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream = null;
            updateDisplay();
            fireValueChange();

        });
        upload.addProgressListener((long readBytes, long contentLength) -> {
            progress.setValue((float) readBytes / contentLength);

        });
        upload.setButtonCaption("Choose File");

        progress.setVisible(false);
        display = createDisplayComponent();
    }

    @Override
    protected Component initContent() {
        rootLayout = new VerticalLayout(progress,
                new MHorizontalLayout(display, upload, clearBtn)
                        .alignAll(Alignment.MIDDLE_LEFT)
        );
        return rootLayout;
    }
    
    protected VerticalLayout getRootLayout() {
        return rootLayout;
    }

    @Override
    protected void doSetValue(byte[] value) {
        this.data = value;
        updateDisplay();
    }

    @Override
    public byte[] getValue() {
        return data;
    }

    private void updateDisplay() {
        updateDisplayComponent();
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        byteArrayOutputStream = new ByteArrayOutputStream();
        this.mimeType = mimeType;
        this.filename = filename;
        return byteArrayOutputStream;
    }

    protected Component createDisplayComponent() {
        defaultDisplay = new Label("", ContentMode.HTML);
        return defaultDisplay;
    }

    private void fireValueChange() {
        fireValueChange(false);
    }

    /*
     * Emits the value change event. The value contained in the field is
     * validated before the event is created.
     */
    protected void fireValueChange(boolean repaintIsNotNeeded) {
        fireEvent(new HasValue.ValueChangeEvent(this, this, null, !repaintIsNotNeeded));
        if (!repaintIsNotNeeded) {
            requestRepaint();
            updateDisplay();
        }
    }

    /**
     * @return the string representing the file. The default implementation
     * shows name, size and first characters of the file if in UTF8 mode.
     */
    protected String getDisplayDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("<em>");
        byte[] ba = getValue();
        if (ba == null) {
            ba = new byte[]{};
        }
        int shownBytes = MAX_SHOWN_BYTES;
        if (ba.length < MAX_SHOWN_BYTES) {
            shownBytes = ba.length;
        }
        for (int i = 0; i < shownBytes; i++) {
            byte b = ba[i];
            sb.append(Integer.toHexString(b));
        }
        if (ba.length > MAX_SHOWN_BYTES) {
            sb.append("... ");
        }
        sb.append("(").append(ba.length).append(" B)");
        sb.append("</em>");
        return sb.toString();
    }

    protected void updateDisplayComponent() {
        defaultDisplay.setValue(getDisplayDetails());
    }

    private Html5FileInputSettings html5FileInputSettings;

    private Html5FileInputSettings getHtml5FileInputSettings() {
        if (html5FileInputSettings == null) {
            html5FileInputSettings = new Html5FileInputSettings(upload);
        }
        return html5FileInputSettings;
    }

    /**
     * @param acceptString the accept filter
     */
    public void setAcceptFilter(String acceptString) {
        getHtml5FileInputSettings().setAcceptFilter(acceptString);
    }

    /**
     * @param maxFileSize the maximum file size to accept
     */
    public void setMaxFileSize(int maxFileSize) {
        getHtml5FileInputSettings().setMaxFileSize(maxFileSize);
    }

    public String getLastFileName() {
        return filename;
    }

    public void setLastFilename(String filename) {
        this.filename = filename;
    }

    public String getLastMimeType() {
        return mimeType;
    }

    public void setLastMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setButtonCaption(String uploadButtonCaption) {
        upload.setButtonCaption(uploadButtonCaption);
    }

    public void setDisplayUpload(boolean displayUpload) {
        display.setVisible(displayUpload);
    }
    
    public void setClearButtonVisible(boolean clearButtonVisible) {
        clearBtn.setVisible(clearButtonVisible);
    }

    @Override
    public void clear() {
        mimeType = null;
        filename = null;
        super.clear();
    }
}

package org.vaadin.easyuploads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringEscapeUtils;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;

/**
 * UploadField is a helper class that uses rather low level {@link Upload}
 * component in core Vaadin. Instead of implementing own {@link Receiver}
 * developer can just access the contents of the file or the {@link File} where
 * the contents was streamed to. In addition to easier access to the content,
 * UploadField also provides built in {@link ProgressIndicator} and displays
 * (partly) the uploaded file. What/how is displayed can be easily modified by
 * overriding {@link #updateDisplay()} method.
 * 
 * <p>
 * UploadField can also be used in a form to edit a property - that's where the
 * name Field comes from. Property can be of type (set with
 * {@link #setFieldType(FieldType)}, but also automatically based on property
 * data source) :
 * <ul>
 * <li>byte[] ({@link FieldType#BYTE_ARRAY}) (Example: image file saved as blob
 * in DB)
 * <li>String ({@link FieldType#UTF8_STRING}) (Example: field of type String in
 * a pojo, the text is in a text file on end users computer)
 * <li>File ({@link FieldType#FILE}) (Example: fied of type File in a pojo)
 * </ul>
 * <p>
 * Typical use cases: TODO
 * <p>
 * {@link #setStorageMode(StorageMode)} controls how upload file handles the
 * received data. {@link StorageMode#FILE} mode streams uploaded file contents
 * to a file (except possibly committed value to property), the
 * {@link StorageMode#MEMORY} mode keeps everything in memory. If the Field is
 * in {@link StorageMode#FILE} mode, the file creation can be controlled with
 * {@link #setFileFactory(FileFactory)}.
 * 
 * <p>
 * Limitations:
 * <ul>
 * <li>Read through modes are not supported properly.
 * <li> {@link StorageMode#FILE} does not support Buffered properly(?).
 * </ul>
 * 
 * @author Matti Tahvonen
 * 
 */
@SuppressWarnings({ "serial" })
public class UploadField extends CssLayout implements HasValue<Object>, Focusable, StartedListener,
        FinishedListener, ProgressListener {
    private static final int MAX_SHOWN_BYTES = 5;

    private UploadFieldReceiver receiver;
    private boolean displayUpload = true;
    private Upload upload;
    protected Component display;

    private ProgressBar progress = new ProgressBar();

    private StorageMode storageMode;

    public UploadField() {
        this(StorageMode.FILE);
    }

    public UploadField(StorageMode mode) {
        setWidth("200px");
        setStorageMode(mode);
        upload = new Upload(null, receiver);
        upload.addStartedListener(this);
        upload.addFinishedListener(this);
        upload.addProgressListener(this);
        upload.setButtonCaption("Choose File");
        progress.setVisible(false);
        display = createDisplayComponent();
        buildDefaulLayout();
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setReadOnly(boolean)
     * @see com.vaadin.data.HasValue#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean readOnly) {
    	super.setReadOnly(readOnly);
    }

    /**
     * @see com.vaadin.ui.AbstractComponent#isReadOnly()
     * @see com.vaadin.data.HasValue#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {
    	return super.isReadOnly();
    }
    
    public void setButtonCaption(String caption) {
        upload.setButtonCaption(caption);
    }

    public String getButtonCaption() {
        return upload.getButtonCaption();
    }

    public final CssLayout getRootLayout() {
        return this;
    }

    protected void buildDefaulLayout() {
        getRootLayout().removeAllComponents();
        getRootLayout().addComponent(upload);
        getRootLayout().addComponent(progress);
    }

    public void setStorageMode(StorageMode mode) {
        if (receiver == null || storageMode != mode) {
            switch (mode) {
            case MEMORY:
                if (getFieldType() == FieldType.FILE) {
                    throw new IllegalArgumentException(
                            "Storage mode cannot be memory if fields type is File!");
                }
                receiver = new MemoryBuffer();
                break;
            default:
                receiver = new FileBuffer() {
                    @Override
                    public FileFactory getFileFactory() {
                        return UploadField.this.getFileFactory();
                    }

                    @Override
                    public FieldType getFieldType() {
                        return UploadField.this.getFieldType();
                    };

                @Override
                public void setLastMimeType(String mimeType) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void setLastFileName(String fileName) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                };
                break;
            }
            if (upload != null) {
                upload.setReceiver(receiver);
            }
            storageMode = mode;

        }
    }

    @Override
    public void clear() {
        receiver.setValue(null);
    }

    public void setLastMimeType(String mimeType) {
        receiver.setLastMimeType(mimeType);
    }

    public enum StorageMode {
        MEMORY, FILE
    }

    /**
	 */
    public enum FieldType {
        UTF8_STRING, BYTE_ARRAY, FILE;

        public Class<?> getRawType() {
            switch (this) {
            case FILE:
                return File.class;
            case UTF8_STRING:
                return String.class;
            default:
                return Byte[].class;
            }
        }
    }

    private FieldType fieldType;

    private String lastFileName;

    public void setFieldType(FieldType type) {
        fieldType = type;
        if (type == FieldType.FILE) {
            setStorageMode(StorageMode.FILE);
        }
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public class MemoryBuffer implements UploadFieldReceiver {
        ByteArrayOutputStream outputBuffer = null;

        String mimeType;

        String fileName;

        public MemoryBuffer() {

        }

        /**
         * @see com.vaadin.ui.Upload.Receiver#receiveUpload(String, String)
         */
        @Override
		public OutputStream receiveUpload(String filename, String MIMEType) {
            fileName = filename;
            mimeType = MIMEType;
            outputBuffer = new ByteArrayOutputStream();
            return outputBuffer;
        }

        @Override
		public Object getValue() {
            if (outputBuffer == null) {
                return null;
            }
            if (outputBuffer.size() == 0) {
            	return null;
            }
            byte[] byteArray = outputBuffer.toByteArray();
            if (getFieldType() == FieldType.BYTE_ARRAY) {
                return byteArray;
            } else {
                return new String(byteArray);
            }
        }

        @Override
		public InputStream getContentAsStream() {
            byte[] byteArray = outputBuffer.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    byteArray);
            return byteArrayInputStream;
        }

        @Override
		public void setValue(Object newValue) {
            mimeType = null;
            fileName = null;
            outputBuffer = new ByteArrayOutputStream();
            if (newValue != null) {
                FieldType fieldType2 = getFieldType();
                switch (fieldType2) {
                case BYTE_ARRAY:
                    byte[] newValueBytes = (byte[]) newValue;
                    try {
                        outputBuffer.write(newValueBytes);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                case UTF8_STRING:
                    try {
                        String newValueStr = (String) newValue;
                        outputBuffer.write(newValueStr.getBytes());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new IllegalStateException();
                }
            }
        }

        @Override
        public boolean isEmpty() {
            return outputBuffer == null || outputBuffer.size() == 0;
        }

        @Override
        public long getLastFileSize() {
            return outputBuffer.size();
        }

        @Override
        public String getLastMimeType() {
            return mimeType;
        }

        @Override
        public String getLastFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
        }

        @Override
        public void setLastMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        @Override
        public void setLastFileName(String fileName) {
            this.fileName = fileName;
        }

    }

    private FileFactory fileFactory;

    private Button deleteButton;

    public void setFileFactory(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    public FileFactory getFileFactory() {
        if (fileFactory == null) {
            fileFactory = new TempFileFactory();
        }
        return fileFactory;
    }

    @Override
	public void setValue(Object newValue) {
        receiver.setValue(newValue);
        fireValueChange();
    }

    @Override
	public Object getValue() {
        return receiver.getValue();
    }

    public InputStream getContentAsStream() {
        return receiver.getContentAsStream();
    }

    @Override
	public void uploadStarted(StartedEvent event) {
        progress.setVisible(true);
        progress.setValue(0f);
    }

    @Override
	public void uploadFinished(FinishedEvent event) {
        progress.setVisible(false);
        lastFileName = event.getFilename();
        updateDisplay();
        fireValueChange();
    }

    protected void fireValueChange() {
        fireValueChange(true);
    }

    @Override
    public void requestRepaint() {
        super.requestRepaint();
    }

    protected void updateDisplay() {
        if (!receiver.isEmpty() && displayUpload) {
            updateDisplayComponent();
            addDeleteButton();
        } else if (display.getParent() != null) {
            buildDefaulLayout();
        }
    }

    protected Component createDisplayComponent() {
        return new Label("");
    }

    protected void updateDisplayComponent() {
        ((Label) display).setValue(getDisplayDetails());
        if (display.getParent() == null) {
            getRootLayout().addComponent(display);
        }
    }

    /**
     * Adds file delete button, if file deletes are allowed.
     */
    protected void addDeleteButton() {
        if (isFileDeletesAllowed()) {
            if (deleteButton == null) {
                deleteButton = new Button(getDeleteCaption());
                deleteButton.addClickListener(new Button.ClickListener() {
                    @Override
					public void buttonClick(ClickEvent arg0) {
                        setValue(null);
                        getRootLayout().removeComponent(arg0.getButton());
                        updateDisplay();
                    }
                });
            }
            if (deleteButton.getParent() == null) {
                attachDeleteButton(deleteButton);
            }
        }
    }

    protected void attachDeleteButton(Button b) {

        getRootLayout().addComponent(b);
    }

    protected String getDeleteCaption() {
        return "X";
    }

    /**
     * @return the string representing the file. The default implementation
     * shows name, size and first characters of the file if in UTF8 mode.
     */
    protected String getDisplayDetails() {
        StringBuilder sb = new StringBuilder();
        if(getFieldType() == FieldType.FILE) {
        sb.append("File: ");
        sb.append(StringEscapeUtils.escapeXml11(lastFileName));
            sb.append("</br> ");
        }
        sb.append("<em>");
        Object value = getValue();
        if (getFieldType() == FieldType.BYTE_ARRAY) {
            byte[] ba = (byte[]) value;
            int shownBytes = MAX_SHOWN_BYTES;
            if (ba.length < MAX_SHOWN_BYTES) {
                shownBytes = ba.length;
            }
            for (int i = 0; i < shownBytes; i++) {
                byte b = ba[i];
                sb.append(Integer.toHexString(b));
            }
            if (ba.length > MAX_SHOWN_BYTES) {
                sb.append("...");
                sb.append("(" + ba.length + " bytes)");
            }
        } else {
            String string = value == null ? null : value.toString();
            if (string.length() > 200) {
                string = string.substring(0, 199) + "...";
            }
            sb.append(StringEscapeUtils.escapeXml11(string));
        }
        sb.append("</em>");
        return sb.toString();
    }

    @Override
	public void updateProgress(long readBytes, long contentLength) {
        progress.setValue((float) readBytes / contentLength);
    }

    public void setFileDeletesAllowed(boolean fileDeletesAllowed) {
        this.fileDeletesAllowed = fileDeletesAllowed;
    }

    public boolean isFileDeletesAllowed() {
        return fileDeletesAllowed;
    }

    private boolean fileDeletesAllowed = true;

    // FIELD RELATED FIELDS

    /**
     * The tab order number of this field.
     */
    private int tabIndex = 0;

    /**
     * Required field.
     */
    private boolean required = false;

    private static final Method VALUE_CHANGE_METHOD;

    static {
        try {
            VALUE_CHANGE_METHOD = HasValue.ValueChangeListener.class
                    .getDeclaredMethod("valueChange",
                            new Class[] { HasValue.ValueChangeEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in AbstractField");
        }
    }

    /*
     * Adds a value change listener for the field. Don't add a JavaDoc comment
     * here, we use the default documentation from the implemented interface.
     */
    @Override
	public Registration addValueChangeListener(HasValue.ValueChangeListener listener) {
        return addListener(HasValue.ValueChangeEvent.class, listener,
                VALUE_CHANGE_METHOD);
    }

    /**
     * Emits the value change event. The value contained in the field is
     * validated before the event is created.
     */
    protected void fireValueChange(boolean repaintIsNotNeeded) {
        fireEvent(new HasValue.ValueChangeEvent(this, this, null, ! repaintIsNotNeeded));
        if (!repaintIsNotNeeded) {
            requestRepaint();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Component.Focusable#focus()
     */
    @Override
	public void focus() {
        upload.focus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Component.Focusable#getTabIndex()
     */
    @Override
	public int getTabIndex() {
        return tabIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Component.Focusable#setTabIndex(int)
     */
    @Override
	public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Is this field required. Required fields must filled by the user.
     * 
     * If the field is required, it is visually indicated in the user interface.
     * Furthermore, setting field to be required implicitly adds "non-empty"
     * validator and thus isValid() == false or any isEmpty() fields. In those
     * cases validation errors are not painted as it is obvious that the user
     * must fill in the required fields.
     * 
     * On the other hand, for the non-required fields isValid() == true if the
     * field isEmpty() regardless of any attached validators.
     * 
     * 
     * @return <code>true</code> if the field is required .otherwise
     *         <code>false</code>.
     */
    @Override
    public boolean isRequiredIndicatorVisible() {
        return required;
    }

    /**
     * Sets the field required. Required fields must filled by the user.
     * 
     * If the field is required, it is visually indicated in the user interface.
     * Furthermore, setting field to be required implicitly adds "non-empty"
     * validator and thus isValid() == false or any isEmpty() fields. In those
     * cases validation errors are not painted as it is obvious that the user
     * must fill in the required fields.
     * 
     * On the other hand, for the non-required fields isValid() == true if the
     * field isEmpty() regardless of any attached validators.
     * 
     * @param required
     *            Is the field required.
     */
    @Override
	public void setRequiredIndicatorVisible(boolean required) {
        this.required = required;
        requestRepaint();
    }

    /**
     * Is the field empty?
     * 
     * In general, "empty" state is same as null..
     */
    @Override
	public boolean isEmpty() {
        return receiver.isEmpty();
    }

    /**
     * Tests the current value against all registered validators.
     * 
     * @return <code>true</code> if all registered validators claim that the
     *         current value is valid, <code>false</code> otherwise.
     */
	public boolean isValid() {

        if (isEmpty()) {
            if (isRequiredIndicatorVisible()) {
                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    protected long getLastFileSize() {
        return receiver.getLastFileSize();
    }

    public String getLastMimeType() {
        return receiver.getLastMimeType();
    }

    public String getLastFileName() {
        return receiver.getLastFileName();
    }

    private Html5FileInputSettings html5FileInputSettings;

    private Html5FileInputSettings getHtml5FileInputSettings() {
        if (html5FileInputSettings == null) {
            html5FileInputSettings = new Html5FileInputSettings(upload);
        }
        return html5FileInputSettings;
    }

    /**
     * @see {@link Html5FileInputSettings#setAcceptFilter(String)}
     * @param acceptString
     */
    public void setAcceptFilter(String acceptString) {
        getHtml5FileInputSettings().setAcceptFilter(acceptString);
    }

    /**
     * @see {@link Html5FileInputSettings#setMaxFileSize(Integer)}
     * @param maxFileSize
     */
    public void setMaxFileSize(int maxFileSize) {
        getHtml5FileInputSettings().setMaxFileSize(maxFileSize);
    }

    public boolean isDisplayUpload() {
        return displayUpload;
    }

    /**
     * If set to true, the uploaded file is displayed within the component.
     * 
     * @param displayUpload
     */
    public void setDisplayUpload(boolean displayUpload) {
        this.displayUpload = displayUpload;
    }

}

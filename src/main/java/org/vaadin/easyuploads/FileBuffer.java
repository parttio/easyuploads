package org.vaadin.easyuploads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.vaadin.easyuploads.UploadField.FieldType;

@SuppressWarnings("serial")
public abstract class FileBuffer implements UploadFieldReceiver {
    String mimeType;

    String fileName;

    private File file;

    private FieldType fieldType;

    private boolean deleteFiles = true;

    public FileBuffer() {
        this(FieldType.UTF8_STRING);
    }

    public FileBuffer(FieldType fieldType) {
        setFieldType(fieldType);
    }

    /**
     * @see com.vaadin.ui.Upload.Receiver#receiveUpload(String, String)
     */
    public OutputStream receiveUpload(String filename, String MIMEType) {
        fileName = filename;
        mimeType = MIMEType;
        try {
            if (file == null) {
                file = getFileFactory().createFile(filename, mimeType);
            }
            return new FileOutputStream(file);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method for UploadField.
     * 
     * @see org.vaadin.easyuploads.UploadFieldReceiver#getValue()
     */
    public Object getValue() {
        if (file == null || !file.exists()) {
            return null;
        }

        if (getFieldType() == FieldType.FILE) {
            return file;
        }

        InputStream valueAsStream = getContentAsStream();

        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream(
                    (int) file.length());
            Streams.copy(valueAsStream, bas);
            byte[] byteArray = bas.toByteArray();
            if (getFieldType() == FieldType.BYTE_ARRAY) {
                return byteArray;
            } else {
                return new String(byteArray);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * contains default impelmentation to allow usage of this class elsewhere
     * (lazy programmer don't have proper class heirarchy...)
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    public InputStream getContentAsStream() {
        try {
            return new FileInputStream(getFile());
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(Object newValue) {
        if (getFieldType() == FieldType.FILE) {
            if (isDeleteFiles() && file != null && file.exists()) {
                file.delete();
            }
            file = (File) newValue;
            fileName = file != null ? file.getName() : null;
        } else {
            if (isDeleteFiles() && file != null && file.exists()) {
                file.delete();
            }
            if (newValue == null) {
                return;
            }
            // we set the contents of the file
            if (file == null || !file.exists()) {
                // TODO attributes may be nulls
                file = getFileFactory().createFile(fileName, mimeType);
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream is;
                if (getFieldType() == FieldType.UTF8_STRING) {
                    is = new ByteArrayInputStream(
                            ((String) newValue).getBytes());
                } else {
                    is = new ByteArrayInputStream((byte[]) newValue);
                }
                Streams.copy(is, fileOutputStream);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    abstract public FileFactory getFileFactory();

    public boolean isEmpty() {
        return file == null || !file.exists();
    }

    public long getLastFileSize() {
        return file.length();
    }

    public String getLastMimeType() {
        return mimeType;
    }

    public String getLastFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * @param deleteFiles
     *            true if file should be deleted when setting value to null or
     *            any other new value
     */
    public void setDeleteFiles(boolean deleteFiles) {
        this.deleteFiles = deleteFiles;
    }

    /**
     * @return true if files should be deleted when setting value to null/new
     *         value
     */
    public boolean isDeleteFiles() {
        return deleteFiles;
    }
}
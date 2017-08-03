package org.vaadin.easyuploads;

import java.io.InputStream;

import com.vaadin.ui.Upload.Receiver;

interface UploadFieldReceiver extends Receiver {

	byte[] getValue();

	InputStream getContentAsStream();

	void setValue(byte[] newValue);

	boolean isEmpty();

	long getLastFileSize();

	String getLastMimeType();

	void setLastMimeType(String mimeType);

	String getLastFileName();

	void setLastFileName(String fileName);
}
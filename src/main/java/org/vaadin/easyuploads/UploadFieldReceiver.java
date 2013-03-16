package org.vaadin.easyuploads;

import java.io.InputStream;

import com.vaadin.ui.Upload.Receiver;

interface UploadFieldReceiver extends Receiver {
	Object getValue();

	InputStream getContentAsStream();

	void setValue(Object newValue);

	boolean isEmpty();

	long getLastFileSize();

	String getLastMimeType();

	String getLastFileName();
}
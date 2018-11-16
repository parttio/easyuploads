package org.vaadin.easyuploads;

import com.vaadin.ui.Upload.Receiver;
import java.io.InputStream;


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
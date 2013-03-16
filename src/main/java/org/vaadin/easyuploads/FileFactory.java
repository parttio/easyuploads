package org.vaadin.easyuploads;

import java.io.File;

public interface FileFactory {
	public File createFile(String fileName, String mimeType);
}
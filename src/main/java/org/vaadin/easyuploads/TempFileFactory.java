package org.vaadin.easyuploads;

import java.io.File;
import java.io.IOException;

class TempFileFactory implements FileFactory {

	public File createFile(String fileName, String mimeType) {
		final String tempFileName = "upload_tmpfile_"
				+ System.currentTimeMillis();
		try {
			return File.createTempFile(tempFileName, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
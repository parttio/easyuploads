package org.vaadin.easyuploads;

import java.io.File;

public class DirectoryFileFactory implements FileFactory {

	private File directory;

	public DirectoryFileFactory(File directory) {
		if (directory.isDirectory() && directory.canWrite()) {
			this.directory = directory;
		} else {
			throw new IllegalArgumentException(
					"The directory does not exist or is not writeable!");
		}
	}

	public File createFile(String fileName, String mimeType) {
		File file = new File(directory.getPath() + "/" + fileName);
		if(file.exists()) {
			int i = 0;
			int lastIndexOf = fileName.lastIndexOf(".");
			String corepart = null;
			String extension = "";
			if(lastIndexOf > 0) {
				extension = fileName.substring(lastIndexOf);
				corepart = fileName.substring(0, lastIndexOf);
			} else {
				corepart = fileName;
			}
			
			while(file.exists()) {
				i++;
				file = new File(directory.getPath() + "/" + corepart + "_" + i + extension);
			}
			
		}
		return file;
	}

}

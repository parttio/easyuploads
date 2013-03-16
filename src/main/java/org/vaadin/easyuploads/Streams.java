package org.vaadin.easyuploads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {

	private static final int BUF_SIZE = 4*1024;

	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		int readBytes;
		while(true) {
			readBytes = input.read(buf);
			if(readBytes == -1) {
				return;
			}
			output.write(buf, 0, readBytes);
		}
	}

}

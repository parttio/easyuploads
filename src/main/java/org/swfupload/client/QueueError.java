package org.swfupload.client;

public interface QueueError {
	final int QUEUE_LIMIT_EXTENDED = -100;
	final int FILE_EXCEEDS_SIZE_LIMIT = -110;
	final int ZERO_BYTE_FILE = -120;
	final int INVALID_FILETYPE = -130;
}
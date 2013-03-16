package org.swfupload.client;

public interface UploadError {
	final int HTTP_ERROR = -200;
	final int MISSING_UPLOAD_URL = -210;
	final int IO_ERROR = -220;
	final int SECURITY_ERROR = -230;
	final int UPLOAD_LIMIT_EXCEEDED = -240;
	final int UPLOAD_FAILED = -250;
	final int SPECIFIED_FILE_ID_NOT_FOUND = -260;
	final int FILE_VALIDATION_FAILED = -270;
	final int FILE_CANCELLED = -280;
	final int UPLOAD_STOPPED = -290;
}
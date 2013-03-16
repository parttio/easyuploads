package org.swfupload.client;

public interface FileStatus {
	final int QUEUED = -1;
	final int IN_PROGRESS = -2;
	final int ERROR = -3;
	final int COMPLETE = -4;
	final int CANCELLED = -5;
}
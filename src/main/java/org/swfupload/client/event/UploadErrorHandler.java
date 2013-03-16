package org.swfupload.client.event;

import org.swfupload.client.File;

public interface UploadErrorHandler {
  void onUploadError(UploadErrorEvent e);

  static final class UploadErrorEvent {
    private final File file;
    private final int uploadError;
    private final String message;

    public UploadErrorEvent(File file, int uploadError, String message) {
      this.file = file;
      this.uploadError = uploadError;
      this.message = message;
    }

    public File getFile() {
      return file;
    }

    public int getUploadError() {
      return uploadError;
    }

    public String getMessage() {
      return message;
    }
  }
}

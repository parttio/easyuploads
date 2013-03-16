package org.swfupload.client.event;

import org.swfupload.client.File;

public interface UploadCompleteHandler {
  void onUploadComplete(UploadCompleteEvent e);

  static final class UploadCompleteEvent {
    private final File file;

    public UploadCompleteEvent(File file) {
      this.file = file;
    }

    public File getFile() {
      return file;
    }
  }
}

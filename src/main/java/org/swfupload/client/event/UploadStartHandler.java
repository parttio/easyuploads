package org.swfupload.client.event;

import org.swfupload.client.File;

public interface UploadStartHandler {
  void onUploadStart(UploadStartEvent e);

  static final class UploadStartEvent {
    private final File file;

    public UploadStartEvent(File file) {
      this.file = file;
    }
    
    public File getFile() {
      return file;
    }
  }
}

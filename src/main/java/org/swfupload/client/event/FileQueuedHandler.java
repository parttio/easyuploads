package org.swfupload.client.event;

import org.swfupload.client.File;

public interface FileQueuedHandler {
  void onFileQueued(FileQueuedEvent event);

  static final class FileQueuedEvent {
    private final File file;

    public FileQueuedEvent(File file) {
      this.file = file;
    }

    public File getFile() {
      return file;
    }
  }
}

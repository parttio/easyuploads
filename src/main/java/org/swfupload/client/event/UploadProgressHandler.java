package org.swfupload.client.event;

import org.swfupload.client.File;

public interface UploadProgressHandler {
  void onUploadProgress(UploadProgressEvent e);

  static final class UploadProgressEvent {
    private final File file;
    private final long complete;
    private final long total;

    public UploadProgressEvent(File file, double complete, double total) {
      this.file = file;
      this.complete = (long) complete;
      this.total = (long) total;
    }

    public File getFile() {
      return file;
    }

    public long getBytesComplete() {
      return complete;
    }

    public long getBytesTotal() {
      return total;
    }
  }
}

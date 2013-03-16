package org.swfupload.client.event;

import org.swfupload.client.File;

public interface UploadSuccessHandler {
  void onUploadSuccess(UploadSuccessEvent e);

  static final class UploadSuccessEvent {
    private final File file;
    private final String serverData;

    public UploadSuccessEvent(File file, String serverData) {
      this.file = file;
      this.serverData = serverData;

    }

    public File getFile() {
      return file;
    }

    public String getServerData() {
      return serverData;
    }
  }
}

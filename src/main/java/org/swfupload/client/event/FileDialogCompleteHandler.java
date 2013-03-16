package org.swfupload.client.event;

public interface FileDialogCompleteHandler {
  void onFileDialogComplete(FileDialogCompleteEvent e);

  static final class FileDialogCompleteEvent {
    private final int filesSelected;
    private final int filesQueued;

    public FileDialogCompleteEvent(int filesSelected, int filesQueued) {
      this.filesSelected = filesSelected;
      this.filesQueued = filesQueued;
    }

    public int getFilesSelected() {
      return filesSelected;
    }

    public int getFilesQueued() {
      return filesQueued;
    }
  }
}

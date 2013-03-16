package org.swfupload.client.event;

public interface FileDialogStartHandler {
  void onFileDialogStart(FileDialogStartEvent e); 

  static final class FileDialogStartEvent {
  }
}

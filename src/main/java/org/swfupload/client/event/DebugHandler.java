package org.swfupload.client.event;

public interface DebugHandler {
  void onDebug(DebugEvent e);

  static final class DebugEvent {
    private final String message;

    public DebugEvent(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}

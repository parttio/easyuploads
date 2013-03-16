package org.swfupload.client.event;

import org.swfupload.client.File;
import org.swfupload.client.QueueError;

public interface FileQueueErrorHandler {
  void onFileQueueError(FileQueueErrorEvent e);
  
  /**
   * Contains information about a failure in adding a file to the upload queue
   */
  static final class FileQueueErrorEvent {
    private final File file;
    private final int errorCode;
    private final String message;

    public FileQueueErrorEvent(File file, int errorCode, String message) {
      this.file = file;
      this.errorCode = errorCode;
      this.message = message;
    }
    
    /**
     * Get the file which failed to be queued
     * @return the file which failed to be queued
     */
    public File getFile() {
      return file;
    }
    
    /**
     * Get the code for the error
     * @return An int value matching one of the errors defined in {@link QueueError QueueError}
     */
    public int getErrorCode() {
      return errorCode;
    }
    
    /**
     * Get the message for the error
     * @return a message describing the error
     */
    public String getMessage() {
      return message;
    }
  }
}

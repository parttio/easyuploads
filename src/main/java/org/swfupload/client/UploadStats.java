package org.swfupload.client;

import com.google.gwt.core.client.JavaScriptObject;

public final class UploadStats extends JavaScriptObject {

  protected UploadStats() {
    // Required by overlay types
  }

  /**
   * Determine if a file upload is currently in progress.
   * 
   * @return true if a file upload is currently in progress
   */
  public native boolean isInProgress() /*-{
    return this.in_progress == 1;
  }-*/;

  /**
   * Get the number of files currently in the queue.
   * 
   * @return the number of files currently in the queue
   */
  public native int getFilesQueued() /*-{
    return this.files_queued;
  }-*/;

  /**
   * Get the number of files that have uploaded successfully (uploads where the
   * uploadSuccess event was generated)
   * 
   * @return the number of files that have uploaded successfully (uploads where
   *         the uploadSuccess event was generated)
   */
  public native int getSuccessfulUploads() /*-{
    return this.successful_uploads;
  }-*/;

  /**
   * Get the number of files that have had errors (excluding cancelled files).
   * 
   * @return the number of files that have had errors (excluding cancelled files)
   */
  public native int getUploadErrors() /*-{
    return this.upload_errors;
  }-*/;

  /**
   * Get the number of files that have been cancelled.
   * 
   * @return the number of files that have been cancelled
   */
  public native int getUploadsCancelled() /*-{
    return this.upload_cancelled;
  }-*/;


  /**
   * Get the number of files that caused fileQueueError to be fired.
   * @return the number of files that caused fileQueueError to be fired
   */
  public native int getQueueErrors() /*-{
    return this.queue_errors;
  }-*/;
}

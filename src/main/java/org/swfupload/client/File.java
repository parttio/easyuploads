package org.swfupload.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public final class File extends JavaScriptObject {
  
  protected File() {
    // Required for overlay types
  }
  
  public native String getId() /*-{
    return this.id;
  }-*/;
  
  public native int getIndex() /*-{
    return this.index;
  }-*/;
  
  public native String getName() /*-{
    return this.name;
  }-*/;

  private native double nativeGetSize() /*-{
    return this.size;
  }-*/;
  
  public long getSize() {
    return (long) nativeGetSize();
  }

  public native String getType() /*-{
    return this.type;
  }-*/;
  
  private native double nativeGetCreationDate() /*-{
    return this.creationdate.getTime();
  }-*/;
  
  /**
   * Get the date the file was created
   * 
   * @return the date the file was created
   */
  public Date getCreationDate() {
    return new Date((long) nativeGetCreationDate());
  }
  
  private native double nativeGetModificationDate() /*-{
    return this.modificationdate.getTime();
  }-*/;
  
  /**
   * Get the date the file was last modified
   * 
   * @return the date the file was last modified
   */
  public Date getModificationDate() {
    return new Date((long) nativeGetModificationDate());
  }

  /**
   * Get the file's current status (see {@link FileStatus} for values)
   * 
   * @return the file's current status (see {@link FileStatus} for values)
   */
  public native int getStatus() /*-{
    return this.filestatus;
  }-*/;
}

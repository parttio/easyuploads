package org.swfupload.client;

import com.google.gwt.core.client.JavaScriptObject;

public final class SWFUpload extends JavaScriptObject {

	static native SWFUpload create(JavaScriptObject settings)
	/*-{
	return new $wnd.SWFUpload(settings);
	}-*/;

	protected SWFUpload() {
		// Required for overlay types
	}

	public static native String getVersion()
	/*-{
	return $wnd.SWFUpload.version;
	}-*/;

	/**
	 * Removes the SWF DOM elements and then destroys SWFUpload internal
	 * references. Used for removing a SWFUpload instance from a page. It also
	 * attempts to prevent memory leaks in IE.
	 * 
	 * @return true if the elements were removed successfully or false if any
	 *         error occurs leaving the SWFUpload instance in an inconsistent
	 *         state.
	 * @since 2.1.0
	 */
	public native boolean destroy()
	/*-{
	return this.destroy();
	}-*/;

	/**
	 * Output the SWFUpload settings using the debug event. This function is
	 * automatically called after initialization if the debug setting is 'true'
	 */
	public native void displayDebugInfo()
	/*-{
	this.displayDebugInfo();
	}-*/;

	/**
	 * Cause the Flash Control to display a File Selection Dialog window. A
	 * single file may be selected from the Dialog window.
	 * <p>
	 * Calling selectFile begins the File Event Chain.
	 * 
	 * @deprecated Not compatible with Flash Player 10.
	 */
	@Deprecated
	public native void selectFile()
	/*-{
	this.selectFile();
	}-*/;

	/**
	 * Cause the Flash Control to display a File Selection Dialog window.
	 * Multiple files may be selected from the Dialog window
	 * <p>
	 * Calling selectFile begins the File Event Chain.
	 * 
	 * @deprecated Not compatible with Flash Player 10.
	 */
	@Deprecated
	public native void selectFiles()
	/*-{
	this.selectFile();
	}-*/;

	/**
	 * Cause the first file in the queue to start the upload process
	 */
	public native void startUpload()
	/*-{
	this.startUpload();
	}-*/;

	/**
	 * Cause the file with the specified Id to start the upload process
	 * 
	 * @param fileId
	 *            The id of the file the begin uploading
	 */
	public native void startUpload(String fileId)
	/*-{
	this.startUpload(fileId);
	}-*/;

	/**
	 * Cancel the file and remove from the queue.
	 * 
	 * @param fileId
	 *            The fileId to cancel
	 * @param triggerErrorEvent
	 *            if true, an uploadError event will be issued
	 */
	public native void cancelUpload(String fileId, boolean triggerErrorEvent)
	/*-{
	this.cancelUpload(fileId, triggerErrorEvent);
	}-*/;

	/**
	 * Cancel the first file in the queue. An uploadError event will be issued.
	 */
	public native void cancelUpload()
	/*-{
	this.cancelUpload(fileId, triggerErrorEvent);
	}-*/;

	/**
	 * Cancel the the file with the supplied id. An uploadError event will be
	 * issued.
	 */
	public native void cancelUpload(String fileId)
	/*-{
	this.cancelUpload(fileId, triggerErrorEvent);
	}-*/;

	/**
	 * Cancel the first file in the queue.
	 * 
	 * @param triggerErrorEvent
	 *            if true, an uploadError event will be issued
	 */
	public native void cancelUpload(boolean triggerErrorEvent)
	/*-{
	this.cancelUpload(fileId, triggerErrorEvent);
	}-*/;

	/**
	 * Stop and re-queues the file currently being uploaded.
	 * 
	 * @param triggerErrorEvent
	 *            if true, an uploadError event will be issued
	 */
	public native void stopUpload(String fileId, boolean triggerErrorEvent)
	/*-{
	this.cancelUpload(fileId, triggerErrorEvent);
	}-*/;

	/**
	 * Get the stats
	 * 
	 * @return Stats
	 */
	public native UploadStats getStats()
	/*-{
	return this.getStats();
	}-*/;

	// The Stats Object may be modified. This is useful if you wish to change
	// the
	// number of successful uploads or upload errors after an upload has
	// completed.
	//
	// void setStats(stats_object)

	/**
	 * Retrieve a File Object from the queue, by id.
	 * 
	 * @param fileId
	 *            The id of the file to retrieve
	 * @return a File Object or null if not found
	 */
	public native File getFile(String fileId)
	/*-{
	return this.getFile(fileId);
	}-*/;

	/**
	 * Retrieve a File Object from the queue, by index.
	 * 
	 * @param index
	 *            The index of the file to retrieve
	 * @return a File Object or null if not found, or null if the index is out
	 *         of range
	 */
	public native File getFile(int index)
	/*-{
	return this.getFile(index);
	}-*/;

	/**
	 * Adds a name/value pair that will be sent in the POST for all files
	 * uploaded.
	 * 
	 * @param name
	 *            The parameter name
	 * @param value
	 *            The parameter value
	 */
	public native void addPostParam(String name, String value)
	/*-{
	this.addPostParam(name, value);
	}-*/;

	/**
	 * Adds a name/value pair that will be sent in the POST for all files
	 * uploaded.
	 * 
	 * @param fileId
	 *            The id of the file to add the parameter to
	 * @param name
	 *            The parameter name
	 * @param value
	 *            The parameter value
	 */
	public native boolean addFileParam(String fileId, String name, String value)
	/*-{
	return this.addFileParam(fileId, name, value);
	}-*/;

	/**
	 * Removes a name/value pair from a file upload that was added using
	 * addFileParam.
	 * 
	 * @param fileId
	 *            The id of the file to remove the parameter from
	 * @param name
	 *            The parameter name to remove
	 */
	public native boolean removeFileParam(String fileId, String name)
	/*-{
	return this.removeFileParam(fieldId, name, value);
	}-*/;

	/**
	 * Dynamically modifies the upload_url setting.
	 * 
	 * @param url
	 *            the URL to receive the upload(s)
	 */
	public native void setUploadURL(String url)
	/*-{
	this.setUploadURL(url);
	}-*/;

	// Dynamically modifies the post_params setting. Any previous values are
	// over-written. The param_object should be a simple JavaScript object. All
	// names and values must be strings.
	//
	// void setPostParams(param_object)

	/**
	 * Dynamically updates the file_types and file_types_description settings.
	 * Both parameters are required.
	 */
	public native void setFileTypes(String types, String description)
	/*-{
		this.setFileTypes(types, description);
	}-*/;

	/**
	 * Modify the maximum file size. This applies to all future files that are
	 * queued. Input may specify a unit, valid units are: B, KB, MB, and GB. The
	 * default unit is KB.
	 * <p>
	 * Examples: 2147483648 B, 2097152, 2097152KB, 2048 MB, 2 GB
	 */
	public native void setFileSizeLimit(String limit)
	/*-{
	this.setFileSizeLimit(limit);
	}-*/;

	/**
	 * Set the maximum number uploaded files. The special value 0 means an
	 * unlimited files may be queued.
	 * 
	 * @param limit
	 *            the maximum number of uploaded files
	 */

	public native void setFileUploadLimit(int limit)
	/*-{
	this.setFileUploadLimit(limit);
	}-*/;

	/**
	 * Set the maximum number of queued files. The special value 0 means an
	 * unlimited files may be queued.
	 * 
	 * @param limit
	 *            the maximum number of queued files
	 */
	public native void setFileQueueLimit(int limit)
	/*-{
	this.setFileQueueLimit(limit);
	}-*/;

	/**
	 * Modify the name used for the form field containing uploaded file data.
	 * <p>
	 * The default is "Filedata".
	 * <p>
	 * <b>NOTE: Changing this value has no effect on Flash player for Linux.</b>
	 * 
	 * @param name
	 *            The form field name to be used for File data
	 */
	public native void setFilePostName(String name)
	/*-{
	this.setFilePostName(name);
	}-*/;

	/**
	 * Change whether SWFUpload sends post parameters on the query string rather
	 * than in the post.
	 * 
	 * @param useQueryString
	 *            whether SWFUpload sends post parameters on the query string
	 *            rather than in the post
	 */
	public native void setUseQueryString(boolean useQueryString)
	/*-{
	this.setUseQueryString(useQueryString);
	}-*/;

	/**
	 * Dynamically enables or disable debug output.
	 * 
	 * @param debugEnabled
	 *            Whether debugging information is output
	 */
	public native void setDebugEnabled(boolean debugEnabled)
	/*-{
	this.setDebugEnabled(debugEnabled);
	}-*/;

	/**
	 * Change the image used in the Flash Button. The image url may be relative
	 * to the Module (use GWT.getModuleBaseURL()) or an absolute path or fully
	 * qualified URL.
	 * <p>
	 * Any image format supported by Flash can be loaded. The most notable
	 * formats are <tt>JPG</tt>, <tt>GIF</tt>, and <tt>PNG</tt>.
	 * <p>
	 * <p>
	 * The image is expected to contain four variations combined into one file,
	 * with each one stacked vertically below the other. Each version represents
	 * a different button state. The order of the states are: normal, hover,
	 * click, disabled.
	 * <p>
	 * The total dimensions of the image file should be 1x the width of the
	 * button and 4x the height.
	 * 
	 * @param url
	 *            The url for an image to use in displaying the Flash button
	 * @since 2.2.0
	 */
	public native void setButtonImageURL(String url)
	/*-{
	this.setButtonImageURL(url);
	}-*/;

	/**
	 * Sets the width and height of a the displayed button. The height should
	 * equal one fourth of the total image size to account for the different
	 * variations.
	 * 
	 * @param width
	 *            The width of the displayed button
	 * @param height
	 *            The height of the displayed button
	 * @since 2.2.0
	 */
	public native void setButtonDimensions(int width, int height)
	/*-{
	this.setButtonDimensions(width, height);
	}-*/;

	/**
	 * Sets the text that should be displayed over the Flash button. Text that
	 * is too large and overflows the button size will be clipped.
	 * <p>
	 * The text can be styled using HTML <a href=
	 * "http://livedocs.adobe.com/flash/9.0/ActionScriptLangRefV3/flash/text/TextField.html#htmlText"
	 * >supported by the Flash Player</a>.
	 * 
	 * @param text
	 *            The text to display over the Flash button
	 * @since 2.2.0
	 */
	public native void setButtonText(String text)
	/*-{
	this.setButtonText(text);
	}-*/;

	/**
	 * Sets the CSS styles used to style the Flash Button Text. CSS should be
	 * formatted according to the <a href=
	 * "http://livedocs.adobe.com/flash/9.0/ActionScriptLangRefV3/flash/text/StyleSheet.html"
	 * >Flash Player documentation</a>.
	 * 
	 * Style classes defined here can then be referenced by HTML supplied via
	 * {@link #setButtonText}.
	 * 
	 * @param cssStyle
	 */
	public native void setButtonTextStyle(String cssStyle)
	/*-{
	this.setButtonTextStyle(cssStyle);
	}-*/;

	/**
	 * Sets the top and left padding of the Flash button text. The values may be
	 * negative.
	 * 
	 * @param left
	 *            The left padding for the flash button text
	 * @param top
	 *            The top padding for the flash button text
	 */
	public native void setButtonTextPadding(int left, int top)
	/*-{
	this.setButtonTextPadding(left, top);
	}-*/;

	/**
	 * When 'true' changes the Flash Button state to disabled and ignores any
	 * clicks.
	 * 
	 * @param disabled
	 *            whether the flash button should be disabled
	 */
	public native void setButtonDisabled(boolean disabled)
	/*-{
		this.setButtonDisabled(disabled);
	}-*/;

	/**
	 * Sets the action taken when the Flash button is clicked. Valid action
	 * values are taken from the {@link ButtonAction} constants.
	 */
	public native void setButtonAction(int buttonAction)
	/*-{
		this.setButtonAction(buttonAction);
	}-*/;

	/**
	 * Sets the mouse cursor shown when hovering over the Flash button. Valid
	 * cursor values are taken from the {@link ButtonCursor} constants.
	 */
	public native void setButtonCursor(int cursor)
	/*-{
		this.setButtonCursor(cursor);
	}-*/;
}

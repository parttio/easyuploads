package org.swfupload.client;

import org.swfupload.client.event.DebugHandler;
import org.swfupload.client.event.FileDialogStartHandler;
import org.swfupload.client.event.FileDialogCompleteHandler;
import org.swfupload.client.event.FileQueueErrorHandler;
import org.swfupload.client.event.FileQueuedHandler;
import org.swfupload.client.event.SWFUploadLoadedHandler;
import org.swfupload.client.event.UploadCompleteHandler;
import org.swfupload.client.event.UploadErrorHandler;
import org.swfupload.client.event.UploadProgressHandler;
import org.swfupload.client.event.UploadStartHandler;
import org.swfupload.client.event.UploadSuccessHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.ScriptInjector;

public final class UploadBuilder {

	@SuppressWarnings("unused")
	private static void fireDebugEvent(DebugHandler handler, String message) {
		DebugHandler.DebugEvent event = new DebugHandler.DebugEvent(message);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onDebug(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onDebug(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireFileDialogStartEvent(FileDialogStartHandler handler) {
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		FileDialogStartHandler.FileDialogStartEvent event = new FileDialogStartHandler.FileDialogStartEvent();
		if (ueh != null) {
			try {
				handler.onFileDialogStart(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onFileDialogStart(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireFileDialogCompleteEvent(
			FileDialogCompleteHandler handler, int selected, int queued) {
		FileDialogCompleteHandler.FileDialogCompleteEvent event = new FileDialogCompleteHandler.FileDialogCompleteEvent(
				selected, queued);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onFileDialogComplete(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onFileDialogComplete(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireFileQueuedEvent(FileQueuedHandler handler, File file) {
		FileQueuedHandler.FileQueuedEvent event = new FileQueuedHandler.FileQueuedEvent(
				file);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onFileQueued(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onFileQueued(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireFileQueueErrorEvent(FileQueueErrorHandler handler,
			File f, int queueError, String message) {
		FileQueueErrorHandler.FileQueueErrorEvent event = new FileQueueErrorHandler.FileQueueErrorEvent(
				f, queueError, message);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onFileQueueError(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onFileQueueError(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireSWFUploadLoadedEvent(SWFUploadLoadedHandler handler) {
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onSWFUploadLoaded();
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onSWFUploadLoaded();
		}
	}

	@SuppressWarnings("unused")
	private static void fireUploadCompleteEvent(UploadCompleteHandler handler,
			File file) {
		UploadCompleteHandler.UploadCompleteEvent event = new UploadCompleteHandler.UploadCompleteEvent(
				file);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onUploadComplete(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onUploadComplete(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireUploadErrorEvent(UploadErrorHandler handler,
			File file, int uploadError, String message) {
		UploadErrorHandler.UploadErrorEvent event = new UploadErrorHandler.UploadErrorEvent(
				file, uploadError, message);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onUploadError(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onUploadError(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireUploadProgressEvent(UploadProgressHandler handler,
			File file, double bytesComplete, double totalBytes) {
		UploadProgressHandler.UploadProgressEvent event = new UploadProgressHandler.UploadProgressEvent(
				file, bytesComplete, totalBytes);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onUploadProgress(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onUploadProgress(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireUploadStartEvent(UploadStartHandler handler,
			File file) {
		UploadStartHandler.UploadStartEvent event = new UploadStartHandler.UploadStartEvent(
				file);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onUploadStart(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onUploadStart(event);
		}
	}

	@SuppressWarnings("unused")
	private static void fireUploadSuccessEvent(UploadSuccessHandler handler,
			File file, String data) {
		UploadSuccessHandler.UploadSuccessEvent event = new UploadSuccessHandler.UploadSuccessEvent(
				file, data);
		UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
		if (ueh != null) {
			try {
				handler.onUploadSuccess(event);
			} catch (Throwable t) {
				ueh.onUncaughtException(t);
			}
		} else {
			handler.onUploadSuccess(event);
		}
	}

	private static native JavaScriptObject copyOf(JavaScriptObject src) /*-{
																		var dst = {};
																		for (property in src) 
																		dst[property] = src[property];
																		return dst;
																		}-*/;

	private JavaScriptObject settings;

	private static boolean injected = false;

	public UploadBuilder() {
		if (!injected) {
			String text = Resources.INSTANCE.script().getText();
			ScriptInjector.fromString(text)
					.setWindow(ScriptInjector.TOP_WINDOW).inject();
			injected = true;
		}
		this.settings = JavaScriptObject.createObject();
	}

	public static native String getVersion() /*-{
												return $wnd.SWFUpload.version;
												}-*/;

	public SWFUpload build() {
		return SWFUpload.create(copyOf(settings));
	}

	public void clearSettings() {
		this.settings = JavaScriptObject.createObject();
	}

	public native void setDebug(boolean debug) /*-{
												this.@org.swfupload.client.UploadBuilder::settings['debug'] = debug;
												}-*/;

	public native void setUploadURL(String url) /*-{
												this.@org.swfupload.client.UploadBuilder::settings['upload_url'] = url;
												}-*/;

	// DOES NOT WORK ON LINUX
	public native void setFilePostName(String postName) /*-{
														this.@org.swfupload.client.UploadBuilder::settings['file_post_name'] = postName;
														}-*/;

	public native void setPostParams(JavaScriptObject params) /*-{
																this.@org.swfupload.client.UploadBuilder::settings['post_params'] = params;
																}-*/;

	public native void setUseQueryString(boolean useQueryString) /*-{
																	this.@org.swfupload.client.UploadBuilder::settings['use_query_string'] = useQueryString;
																	}-*/;

	public native void requeueOnError(boolean requeueOnError) /*-{
																this.@org.swfupload.client.UploadBuilder::settings['requeue_on_error'] = requeueOnError;
																}-*/;

	public void setHTTPSuccessCodes(int... codes) {
		JsArrayString as = JavaScriptObject.createArray().cast();
		for (int i = 0; i < codes.length; i++) {
			as.set(i, String.valueOf(codes[i]));
		}
		setHTTPSuccessCodes(as);
	}

	public native void setHTTPSuccessCodes(JsArrayString codes) /*-{
																this.@org.swfupload.client.UploadBuilder::settings['http_success'] = codes;
																}-*/;

	public native void setFileTypes(String fileTypes) /*-{
														this.@org.swfupload.client.UploadBuilder::settings['file_types'] = fileTypes;
														}-*/;

	public native void setFileTypesDescription(String description) /*-{
																	this.@org.swfupload.client.UploadBuilder::settings['file_types_description'] = description;
																	}-*/;

	// double or long wrapper method with cast?
	public native void setFileSizeLimit(double limit) /*-{
														this.@org.swfupload.client.UploadBuilder::settings['file_size_limit'] = limit;
														}-*/;

	public native void setFileUploadLimit(int limit) /*-{
														this.@org.swfupload.client.UploadBuilder::settings['file_upload_limit'] = limit;
														}-*/;

	public native void setFileQueueLimit(int limit) /*-{
													this.@org.swfupload.client.UploadBuilder::settings['file_queue_limit'] = limit;
													}-*/;

	public native void preventSWFCaching(boolean prevent) /*-{
															this.@org.swfupload.client.UploadBuilder::settings['prevent_swf_caching'] = prevent;
															}-*/;

	public native void setButtonImageURL(String url) /*-{
														this.@org.swfupload.client.UploadBuilder::settings['button_image_url'] = url;  
														}-*/;

	public native void setButtonWidth(int width) /*-{
													this.@org.swfupload.client.UploadBuilder::settings['button_width'] = width;  
													}-*/;

	public native void setButtonHeight(int height) /*-{
													this.@org.swfupload.client.UploadBuilder::settings['button_height'] = height;  
													}-*/;

	public native void setButtonText(String text) /*-{
													this.@org.swfupload.client.UploadBuilder::settings['button_text'] = text;  
													}-*/;

	/**
	 * Default: "color: #000000; font-size: 16pt;"
	 * 
	 * @param style
	 */
	public native void setButtonTextStyle(String style) /*-{
														this.@org.swfupload.client.UploadBuilder::settings['button_text_style'] = style;  
														}-*/;

	/**
	 * Default: 0
	 * 
	 * @param padding
	 */
	public native void setButtonTextTopPadding(int padding) /*-{
															this.@org.swfupload.client.UploadBuilder::settings['button_text_top_padding'] = padding;  
															}-*/;

	/**
	 * Default: 0
	 * 
	 * @param padding
	 */
	public native void setButtonTextLeftPadding(int padding) /*-{
																this.@org.swfupload.client.UploadBuilder::settings['button_text_left_padding'] = padding;  
																}-*/;

	/**
	 * Default: {@link ButtonAction#SELECT_FILES}
	 * 
	 * @param action
	 */
	public void setButtonAction(ButtonAction action) {
		nativeSetButtonAction(action.getValue());
	}

	private native void nativeSetButtonAction(int action) /*-{
															this.@org.swfupload.client.UploadBuilder::settings['button_action'] = action;
															}-*/;

	public native void setButtonDisabled(boolean disabled) /*-{
															this.@org.swfupload.client.UploadBuilder::settings['button_disabled'] = disabled;
															}-*/;

	/**
	 * The element id to replace with the flash button
	 * 
	 * @param id
	 */
	public native void setButtonPlaceholderID(String id) /*-{
															this.@org.swfupload.client.UploadBuilder::settings['button_placeholder_id'] = id;
															}-*/;

	/**
	 * Default: {@link ButtonCursor#ARROW}
	 * 
	 * @param cursor
	 */
	public void setButtonCursor(ButtonCursor cursor) {
		nativeSetButtonCursor(cursor.getValue());
	}

	private native void nativeSetButtonCursor(int cursor) /*-{
															this.@org.swfupload.client.UploadBuilder::settings['button_cursor'] = cursor;
															}-*/;

	/**
	 * Default: {@link WindowMode#WINDOW}
	 * 
	 * @param mode
	 */
	public void setWindowMode(WindowMode mode) {
		nativeSetWindowMode(mode.getValue());
	}

	private native void nativeSetWindowMode(String mode) /*-{
															this.@org.swfupload.client.UploadBuilder::settings['button_window_mode'] = mode;
															}-*/;

	public native void setSwfUrl(String url) /*-{
												this.@org.swfupload.client.UploadBuilder::settings['flash_url'] = url;
												}-*/;

	public native void setSWFUploadLoadedHandler(SWFUploadLoadedHandler handler) /*-{
																					this.@org.swfupload.client.UploadBuilder::settings['swfupload_loaded_handler'] = function() {
																					@org.swfupload.client.UploadBuilder::fireSWFUploadLoadedEvent(Lorg/swfupload/client/event/SWFUploadLoadedHandler;)(handler);
																					};
																					}-*/;

	public native void setFileDialogStartHandler(FileDialogStartHandler handler) /*-{
																					this.@org.swfupload.client.UploadBuilder::settings['file_dialog_start_handler'] = function() {
																					@org.swfupload.client.UploadBuilder::fireFileDialogStartEvent(Lorg/swfupload/client/event/FileDialogStartHandler;)(handler);
																					};
																					}-*/;

	public native void setFileDialogCompleteHandler(
			FileDialogCompleteHandler handler) /*-{
												this.@org.swfupload.client.UploadBuilder::settings['file_dialog_complete_handler'] = function(selected, queued) {
												@org.swfupload.client.UploadBuilder::fireFileDialogCompleteEvent(Lorg/swfupload/client/event/FileDialogCompleteHandler;II)(handler, selected, queued);
												};
												}-*/;

	public native void setFileQueuedHandler(FileQueuedHandler handler) /*-{
																		this.@org.swfupload.client.UploadBuilder::settings['file_queued_handler'] = function(file) {
																		@org.swfupload.client.UploadBuilder::fireFileQueuedEvent(Lorg/swfupload/client/event/FileQueuedHandler;Lorg/swfupload/client/File;)(handler, file);
																		};
																		}-*/;

	public native void setFileQueueErrorHandler(FileQueueErrorHandler handler) /*-{
																				this.@org.swfupload.client.UploadBuilder::settings['file_queue_error_handler'] = function(file, queueError, message) {
																				@org.swfupload.client.UploadBuilder::fireFileQueueErrorEvent(Lorg/swfupload/client/event/FileQueueErrorHandler;Lorg/swfupload/client/File;ILjava/lang/String;)(handler, file, queueError, message);
																				};
																				}-*/;

	public native void setUploadCompleteHandler(UploadCompleteHandler handler) /*-{
																				this.@org.swfupload.client.UploadBuilder::settings['upload_complete_handler'] = function(file) {
																				@org.swfupload.client.UploadBuilder::fireUploadCompleteEvent(Lorg/swfupload/client/event/UploadCompleteHandler;Lorg/swfupload/client/File;)(handler, file);
																				};
																				}-*/;

	public native void setUploadErrorHandler(UploadErrorHandler handler) /*-{
																			this.@org.swfupload.client.UploadBuilder::settings['upload_error_handler'] = function(file, error, message) {
																			@org.swfupload.client.UploadBuilder::fireUploadErrorEvent(Lorg/swfupload/client/event/UploadErrorHandler;Lorg/swfupload/client/File;ILjava/lang/String;)(handler, file, error, message);
																			};
																			}-*/;

	public native void setUploadProgressHandler(UploadProgressHandler handler) /*-{
																				this.@org.swfupload.client.UploadBuilder::settings['upload_progress_handler'] = function(file, complete, total) {
																				@org.swfupload.client.UploadBuilder::fireUploadProgressEvent(Lorg/swfupload/client/event/UploadProgressHandler;Lorg/swfupload/client/File;DD)(handler, file, complete, total);
																				};
																				}-*/;

	public native void setUploadStartHandler(UploadStartHandler handler) /*-{
																			this.@org.swfupload.client.UploadBuilder::settings['upload_start_handler'] = function(file) {
																			@org.swfupload.client.UploadBuilder::fireUploadStartEvent(Lorg/swfupload/client/event/UploadStartHandler;Lorg/swfupload/client/File;)(handler, file);
																			};
																			}-*/;

	public native void setUploadSuccessHandler(UploadSuccessHandler handler) /*-{
																				this.@org.swfupload.client.UploadBuilder::settings['upload_success_handler'] = function(file, data) {
																				@org.swfupload.client.UploadBuilder::fireUploadSuccessEvent(Lorg/swfupload/client/event/UploadSuccessHandler;Lorg/swfupload/client/File;Ljava/lang/String;)(handler, file, data);
																				};
																				}-*/;

	public native void setDebugHandler(DebugHandler handler) /*-{
																this.@org.swfupload.client.UploadBuilder::settings['debug_handler'] = function(message) {
																@org.swfupload.client.UploadBuilder::fireDebugEvent(Lorg/swfupload/client/event/DebugHandler;Ljava/lang/String;)(handler, message);
																};
																}-*/;
}

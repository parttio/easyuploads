package org.vaadin.easyuploads;

import org.vaadin.easyuploads.client.Html5FileInputState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;

public class Html5FileInputSettings extends AbstractExtension {

	public Html5FileInputSettings(Upload tf) {
		extend(tf);
		tf.addFinishedListener(new FinishedListener() {
			@Override
			public void uploadFinished(FinishedEvent event) {
				getState().dummycounter++;
			}
		});
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);
	}

	/**
	 * Sets the maximum size of file that should be accepted. This check is done
	 * by the client side and not supported by all browsers. Note that using
	 * this method does not invalidate need for server side checks.
	 * 
	 * @param maxSize
	 */
	public void setMaxFileSize(Integer maxSize) {
		getState().maxSize = maxSize;
	}

	/**
	 * Sets mime types that browser should let users upload. This check is done
	 * by the client side and not supported by all browsers. Some browser us the
	 * accept filter just as a initial filter for their file chooser dialog.
	 * Note that using this method does not invalidate need for server side
	 * checks.
	 * 
	 * See https://developer.mozilla.org/en-US/docs/HTML/Element/Input
	 * 
	 * @param accept
	 */
	public void setAcceptFilter(String accept) {
		getState().accept = accept;
	}

	@Override
	protected Html5FileInputState getState() {
		return (Html5FileInputState) super.getState();
	}

}
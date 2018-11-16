package org.vaadin.easyuploads;

import org.vaadin.easyuploads.client.Html5FileInputState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Upload;

public class Html5FileInputSettings extends AbstractExtension {

    public Html5FileInputSettings(Upload tf) {
        extend(tf);
        tf.addFinishedListener(new Upload.FinishedListener() {
            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                getState().dummycounter++;
            }
        });
    }

    public Html5FileInputSettings(MultiUpload tf) {
        extend(tf);
        tf.addFinishedListener(
                new org.vaadin.easyuploads.MultiUpload.FinishedListener() {
            @Override
            public void uploadFinished() {
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
     * @param maxSize the max file size
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
     * @param accept the accept filter
     */
    public void setAcceptFilter(String accept) {
        getState().accept = accept;
    }

    /**
     * Sets the maximum file count that should be accepted.
     *
     * @param maxCount the max number files that can be uploaded
     */
    public void setMaxFileCount(Integer maxCount) {
        getState().maxFileCount = maxCount;
    }

    @Override
    protected Html5FileInputState getState() {
        return (Html5FileInputState) super.getState();
    }

    public void setMaxFileSizeText(String maxSizeText) {
        getState().maxSizeText = maxSizeText;
    }

}

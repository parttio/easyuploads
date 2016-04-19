package org.vaadin.easyuploads;

@SuppressWarnings("serial")
public class MaxFileSizeExceededException extends RuntimeException {

    private final long contentLength;
    private final long maxFileSize;
    
    public MaxFileSizeExceededException(long contentLength, int maxFileSize) {
        this.contentLength = contentLength;
        this.maxFileSize = maxFileSize;
    }

    public long getContentLength() {
        return contentLength;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }
}

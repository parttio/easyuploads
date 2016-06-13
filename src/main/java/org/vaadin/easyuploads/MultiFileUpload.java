package org.vaadin.easyuploads;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.vaadin.easyuploads.MultiUpload.FileDetail;
import org.vaadin.easyuploads.UploadField.FieldType;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.StreamVariable;
import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingProgressEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;
import com.vaadin.server.UploadException;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.PushConfiguration;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * MultiFileUpload makes it easier to upload multiple files. MultiFileUpload
 * releases upload button for new uploads immediately when a file is selected
 * (aka parallel uploads). It also displays progress indicators for pending
 * uploads.
 * <p>
 * MultiFileUpload always streams straight to files to keep memory consumption
 * low. To temporary files by default, but this can be overridden with
 * {@link #setFileFactory(FileFactory)} (eg. straight to target directory on the
 * server).
 * <p>
 * Developer handles uploaded files by implementing the abstract
 * {@link #handleFile(File, String, String, long)} method.
 * <p>
 * TODO Field version (type == Collection<File> or File where isDirectory() ==
 * true).
 * <p>
 * TODO a super progress indicator (total transferred per total, including
 * queued files)
 * <p>
 * TODO Time remaining estimates and current transfer rate
 *
 */
@SuppressWarnings("serial")
public abstract class MultiFileUpload extends CssLayout implements DropHandler {

    private int maxFileSize = -1;

    private Layout progressBars;
    private CssLayout uploads = new CssLayout() {

        @Override
        protected String getCss(Component c) {
            if (getComponent(uploads.getComponentCount() - 1) != c) {
                return "overflow: hidden; position: absolute;";
            }
            return "overflow:hidden;";
        }

    };
    private String uploadButtonCaption = "...";
    private String areatext = "<small>DROP<br/>FILES</small>";

    public MultiFileUpload() {
        setWidth("200px");
    }

    protected void initialize() {
        addComponent(getprogressBarsLayout());
        uploads.setStyleName("v-multifileupload-uploads");
        addComponent(uploads);
        prepareUpload();
        if (supportsFileDrops()) {
            prepareDropZone();
        }
    }

    protected Layout getprogressBarsLayout() {
        if (progressBars == null) {
            progressBars = new VerticalLayout();
        }
        return progressBars;
    }

    private void prepareUpload() {
        final FileBuffer receiver = createReceiver();

        final MultiUpload upload = new MultiUpload();
        MultiUploadHandler handler = new MultiUploadHandler() {
            private LinkedList<ProgressBar> indicators;

            public void streamingStarted(StreamingStartEvent event) {
                if (maxFileSize > 0 && event.getContentLength() > maxFileSize) {
                    throw new MaxFileSizeExceededException(event.
                            getContentLength(), maxFileSize);
                }
            }

            public void streamingFinished(StreamingEndEvent event) {
                if (!indicators.isEmpty()) {
                    getprogressBarsLayout().
                            removeComponent(indicators.remove(0));
                }
                File file = receiver.getFile();
                handleFile(file, event.getFileName(), event.getMimeType(),
                        event.getBytesReceived());
                receiver.setValue(null);
                if (upload.getPendingFileNames().isEmpty()) {
                    uploads.removeComponent(upload);
                }
            }

            public void streamingFailed(StreamingErrorEvent event) {
                Logger.getLogger(getClass().getName()).log(Level.FINE,
                        "Streaming failed", event.getException());

                for (ProgressBar progressIndicator : indicators) {
                    getprogressBarsLayout().removeComponent(progressIndicator);
                }

            }

            public void onProgress(StreamingProgressEvent event) {
                long readBytes = event.getBytesReceived();
                long contentLength = event.getContentLength();
                float f = (float) readBytes / (float) contentLength;
                indicators.get(0).setValue(f);
            }

            public OutputStream getOutputStream() {
                FileDetail next = upload.getPendingFileNames().iterator()
                        .next();
                return receiver.receiveUpload(next.getFileName(),
                        next.getMimeType());
            }

            public void filesQueued(Collection<FileDetail> pendingFileNames) {
                if (indicators == null) {
                    indicators = new LinkedList<ProgressBar>();
                }
                for (FileDetail f : pendingFileNames) {
                    ensurePushOrPollingIsEnabled();
                    ProgressBar pi = createProgressIndicator();
                    getprogressBarsLayout().addComponent(pi);
                    pi.setCaption(f.getFileName());
                    pi.setVisible(true);
                    indicators.add(pi);
                }
                upload.setHeight("0px");
                prepareUpload();
            }

            @Override
            public boolean isInterrupted() {
                return false;

            }
        };
        upload.setHandler(handler);
        upload.setButtonCaption(getUploadButtonCaption());
        uploads.addComponent(upload);

    }

    protected void ensurePushOrPollingIsEnabled() {
        UI current = UI.getCurrent();
        PushConfiguration pushConfiguration = current.getPushConfiguration();
        PushMode pushMode = pushConfiguration.getPushMode();
        if (pushMode != PushMode.AUTOMATIC) {
            if (current.getPollInterval() == -1 || current.getPollInterval() > 1000) {
                current.setPollInterval(500);
            }
        }
    }

    protected ProgressBar createProgressIndicator() {
        ProgressBar progressIndicator = new ProgressBar();
        progressIndicator.setWidth("100%");
        progressIndicator.setValue(0f);
        return progressIndicator;
    }

    public String getUploadButtonCaption() {
        return uploadButtonCaption;
    }

    public void setUploadButtonCaption(String uploadButtonCaption) {
        this.uploadButtonCaption = uploadButtonCaption;
        Iterator<Component> componentIterator = uploads.getComponentIterator();
        while (componentIterator.hasNext()) {
            Component next = componentIterator.next();
            if (next instanceof MultiUpload) {
                MultiUpload upload = (MultiUpload) next;
                if (upload.isVisible()) {
                    upload.setButtonCaption(getUploadButtonCaption());
                }
            }
        }
    }

    private FileFactory fileFactory;

    public FileFactory getFileFactory() {
        if (fileFactory == null) {
            fileFactory = new TempFileFactory();
        }
        return fileFactory;
    }

    public void setFileFactory(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    protected FileBuffer createReceiver() {
        FileBuffer receiver = new FileBuffer(FieldType.FILE) {
            @Override
            public FileFactory getFileFactory() {
                return MultiFileUpload.this.getFileFactory();
            }
        };
        return receiver;
    }

    protected int getPollinInterval() {
        return 500;
    }

    @Override
    public void attach() {
        super.attach();
        if (getComponentCount() == 0) {
            initialize();
        }
    }

    private DragAndDropWrapper dropZone;

    /**
     * Sets up DragAndDropWrapper to accept multi file drops.
     */
    private void prepareDropZone() {
        if (dropZone == null) {
            Component label = new Label(getAreaText(), Label.CONTENT_XHTML);
            label.setSizeUndefined();
            dropZone = new DragAndDropWrapper(label);
            dropZone.setStyleName("v-multifileupload-dropzone");
            dropZone.setSizeUndefined();
            addComponent(dropZone, 1);
            dropZone.setDropHandler(this);
            addStyleName("no-horizontal-drag-hints");
            addStyleName("no-vertical-drag-hints");
        }
    }

    protected String getAreaText() {
        return areatext;
    }

    public void setAreaText(String areatext) {
        this.areatext = areatext;
    }

    @SuppressWarnings("deprecation")
    protected boolean supportsFileDrops() {
        WebBrowser browser = getUI().getPage().getWebBrowser();
        if (browser.isChrome()) {
            return true;
        } else if (browser.isFirefox()) {
            return true;
        } else if (browser.isSafari()) {
            return true;
        } else if (browser.isIE() && browser.getBrowserMajorVersion() >= 11) {
            return true;
        }
        return false;
    }

    abstract protected void handleFile(File file, String fileName,
            String mimeType, long length);

    /**
     * A helper method to set DirectoryFileFactory with given pathname as
     * directory.
     *
     * @param directoryWhereToUpload the path to directory where files should be
     * uploaded
     */
    public void setRootDirectory(String directoryWhereToUpload) {
        setFileFactory(new DirectoryFileFactory(
                new File(directoryWhereToUpload)));
    }

    public AcceptCriterion getAcceptCriterion() {
        // TODO accept only files
        // return new And(new TargetDetailIs("verticalLocation","MIDDLE"), new
        // TargetDetailIs("horizontalLoction", "MIDDLE"));
        return AcceptAll.get();
    }

    public void drop(DragAndDropEvent event) {
        DragAndDropWrapper.WrapperTransferable transferable = (WrapperTransferable) event.
                getTransferable();
        Html5File[] files = transferable.getFiles();
        for (final Html5File html5File : files) {

            if (maxFileSize != -1 && html5File.getFileSize() > maxFileSize) {
                onMaxSizeExceeded(html5File.getFileSize());
                continue;
            }

            final ProgressBar pi = createProgressIndicator();
            ensurePushOrPollingIsEnabled();
            pi.setCaption(html5File.getFileName());
            getprogressBarsLayout().addComponent(pi);
            final FileBuffer receiver = createReceiver();
            html5File.setStreamVariable(new StreamVariable() {

                private String name;
                private String mime;

                public OutputStream getOutputStream() {
                    return receiver.receiveUpload(name, mime);
                }

                public boolean listenProgress() {
                    return true;
                }

                public void onProgress(StreamingProgressEvent event) {
                    float p = (float) event.getBytesReceived()
                            / (float) event.getContentLength();
                    pi.setValue(p);
                }

                public void streamingStarted(StreamingStartEvent event) {
                    name = event.getFileName();
                    mime = event.getMimeType();

                }

                public void streamingFinished(StreamingEndEvent event) {
                    getprogressBarsLayout().removeComponent(pi);
                    handleFile(receiver.getFile(), html5File.getFileName(),
                            html5File.getType(), html5File.getFileSize());
                    receiver.setValue(null);
                }

                public void streamingFailed(StreamingErrorEvent event) {
                    getprogressBarsLayout().removeComponent(pi);
                }

                public boolean isInterrupted() {
                    return false;
                }
            });
        }

    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
        if (maxFileSize > 0) {
            // TODO figure out if this could be done somehow else, now this 
            // might override the error hanler set by user, maybe set for 
            // individual multiuploads instead ??
            setErrorHandler(new DefaultErrorHandler() {
                @Override
                public void error(com.vaadin.server.ErrorEvent event) {
                    if (event.getThrowable() != null && event.getThrowable() instanceof UploadException) {
                        Throwable cause = event.getThrowable().getCause();
                        if (cause != null && cause instanceof MaxFileSizeExceededException) {
                            onMaxSizeExceeded(
                                    ((MaxFileSizeExceededException) cause).
                                    getContentLength());
                            return;
                        }
                    }
                    super.error(event);
                }
            });
        } else {
            setErrorHandler(null);
        }
    }

    public void onMaxSizeExceeded(long contentLength) {
        Notification.show(
                "Max size exceeded " + FileUtils.byteCountToDisplaySize(
                        contentLength) + " > "
                + FileUtils.byteCountToDisplaySize(maxFileSize),
                Notification.Type.ERROR_MESSAGE);
    }
}

package org.vaadin.easyuploads.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.easyuploads.client.AcceptUtil;
import org.vaadin.easyuploads.client.Html5FileInputSettingsConnector.HasFileUpload;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VCssLayout;
import com.vaadin.client.ui.VDragAndDropWrapper;
import com.vaadin.client.ui.VNotification;
import com.vaadin.client.ui.dd.VHtml5File;

/**
 * Upload counterpart with "multiple" support.
 *
 * Not finished enough for extension.
 */
public class VMultiUpload extends SimplePanel
        implements Paintable, HasFileUpload {

    private HandlerRegistration clikcReg;
    private Integer maxFileSize;
    private String maxFileSizeText;
    private String accept;
    private List<String> accepted = new ArrayList<String>();
    private Integer maxFileCount;

    private final class MyFileUpload extends FileUpload {

        // Starting from Chrome 30 we should prevent the onFocus hack or
        // otherwise the native file dialog is opened twice.
        private final boolean isChrome30 = (BrowserInfo.get().isChrome()
                && BrowserInfo.get().getBrowserMajorVersion() >= 30);

        public MyFileUpload() {
            getElement().setPropertyString("multiple", "multiple");
        }

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            if (event.getTypeInt() == Event.ONCHANGE) {
                if (hasFiles()) {
                    submit();
                }
            } else if (!isChrome30 && event.getTypeInt() == Event.ONFOCUS) {
                // IE and user has clicked on hidden textarea part of upload
                // field. Manually open file selector, other browsers do it by
                // default.
                fireNativeClick(fu.getElement());
                // also remove focus to enable hack if user presses cancel
                // button
                fireNativeBlur(fu.getElement());
            }
        }

        public boolean hasFiles() {
            return getFileCount(getElement()) > 0;
        }
    }

    public static final native int getFileCount(Element el)
    /*-{
     	return el.files.length;
    }-*/
    ;

    public static final native VHtml5File[] getFiles(Element el)
    /*-{
    	return el.files;
    }-*/
    ;

    public static final String CLASSNAME = "v-upload";

    private static final String DELIM = "---xXx---";

    /**
     * FileUpload component that opens native OS dialog to select file.
     */
    MyFileUpload fu = new MyFileUpload();

    Panel panel = new FlowPanel();

    ApplicationConnection client;

    private String paintableId;

    /**
     * Button that initiates uploading
     */
    private final VButton submitButton;

    private boolean enabled = true;

    private String receiverUri;

    private ReadyStateChangeHandler readyStateChangeHandler = new ReadyStateChangeHandler() {
        @Override
		public void onReadyStateChange(XMLHttpRequest xhr) {
            if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                xhr.clearOnReadyStateChange();
                VConsole.log("Ready state + " + xhr.getReadyState());

                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
					public void execute() {
                        if (isAttached() && !fileQueue.isEmpty()) {
                            client.updateVariable(paintableId, "ready", true,
                                    true);
                            postNextFileFromQueue();
                        }
                    }
                });
            }
        }
    };

    public VMultiUpload() {
        super(com.google.gwt.dom.client.Document.get().createDivElement());

        setWidget(panel);
        panel.add(fu);
        submitButton = new VButton();
        submitButton.addClickHandler(new ClickHandler() {
            @Override
			public void onClick(ClickEvent event) {
                // fire click on upload (eg. focused button and hit space)
                fireNativeClick(fu.getElement());
            }
        });
        panel.add(submitButton);

        setStyleName(CLASSNAME);
        fu.sinkEvents(Event.ONCHANGE);
        fu.sinkEvents(Event.ONFOCUS);
        addStyleName(CLASSNAME + "-immediate");
    }

    @Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }
        addStyleName(CLASSNAME + "-immediate");

        this.client = client;
        paintableId = uidl.getId();
        receiverUri = client
                .translateVaadinUri(uidl.getStringVariable("target"));
        submitButton.setText(uidl.getStringAttribute("buttoncaption"));
        fu.setName(paintableId + "_file");

        if (uidl.hasAttribute("disabled") || uidl.hasAttribute("readonly")) {
            disableUpload();
        } else if (!uidl.getBooleanAttribute("state")) {
            // Enable the button only if an upload is not in progress
            enableUpload();
        }
        if (uidl.hasAttribute("ready")) {
            VConsole.log(
                    "The server knows about coming files. Start posting files");
            postNextFileFromQueue();
        }

        Widget parent = getParent();
        if (clikcReg == null && parent instanceof VCssLayout) {
            VCssLayout vCssLayout = (VCssLayout) parent;
            if (vCssLayout.getStyleName()
                    .contains("v-multifileupload-uploads")) {
                VCssLayout multifileupload = (VCssLayout) vCssLayout
                        .getParent();
                for (Widget widget : multifileupload) {
                    if (widget.getStyleName()
                            .contains("v-multifileupload-dropzone")) {
                        final VDragAndDropWrapper ddw = (VDragAndDropWrapper) widget;
                        clikcReg = ddw.addDomHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                if (isAttached()) {
                                    fireNativeClick(fu.getElement());
                                }
                            }
                        }, ClickEvent.getType());
                    }
                }
            }
        }
    }

    private void postNextFileFromQueue() {
        if (!fileQueue.isEmpty()) {
            final VHtml5File file = fileQueue.remove(0);
            VConsole.log(
                    "Posting file " + file.getName() + " to " + receiverUri);
            ExtendedXHR extendedXHR = (ExtendedXHR) ExtendedXHR.create();
            extendedXHR.setOnReadyStateChange(readyStateChangeHandler);
            extendedXHR.open("POST", receiverUri);
            extendedXHR.postFile(file);
            new Timer() {
                @Override
                public void run() {
                    // TODO poll for start or modify response so that we
                    // receive headers received
                    client.sendPendingVariableChanges();
                }
            }.schedule(700);
        }
    }

    static class ExtendedXHR extends XMLHttpRequest {

        protected ExtendedXHR() {
        }

        public final native void postFile(VHtml5File file)
        /*-{
            this.setRequestHeader('Accept', 'text/html,vaadin/filexhr');
            this.setRequestHeader('Content-Type', 'multipart/form-data');
            this.send(file);
        }-*/;

    }

    private static native void fireNativeClick(Element element)
    /*-{
        element.click();
    }-*/;

    private static native void fireNativeBlur(Element element)
    /*-{
        element.blur();
    }-*/;

    protected void disableUpload() {
        submitButton.setEnabled(false);
        // Cannot disable the fileupload while submitting or the file won't
        // be submitted at all
        fu.getElement().setPropertyBoolean("disabled", true);
        enabled = false;
    }

    protected void enableUpload() {
        submitButton.setEnabled(true);
        fu.getElement().setPropertyBoolean("disabled", false);
        enabled = true;
    }

    /**
     * Re-creates file input field and populates panel. This is needed as we
     * want to clear existing values from our current file input field.
     */
    private void rebuildPanel() {
        panel.remove(submitButton);
        panel.remove(fu);
        fu = new MyFileUpload();
        fu.setName(paintableId + "_file");
        fu.getElement().setPropertyBoolean("disabled", !enabled);
        panel.add(fu);
        panel.add(submitButton);
        fu.sinkEvents(Event.ONCHANGE);
    }

    private void submit() {
        VHtml5File[] files = getFiles(fu.getElement());
        List<String> filedetails = new ArrayList<String>();
        List<VHtml5File> tooBigs = new ArrayList<VHtml5File>();
        List<VHtml5File> noMatches = new ArrayList<VHtml5File>();
        List<VHtml5File> tooMany = new ArrayList<VHtml5File>();
        for (VHtml5File file : files) {
            if (maxFileSize != null && file.getSize() > maxFileSize) {
                tooBigs.add(file);
            } else if (!AcceptUtil.accepted(file.getName(), file.getType(),
                    accepted)) {
                noMatches.add(file);
            } else if (maxFileCount != null && filedetails.size() > maxFileCount) {
                tooMany.add(file);
            } else {
                queueFilePost(file);
                filedetails.add(serialize(file));
            }
        }
        if (!tooBigs.isEmpty() || !noMatches.isEmpty() || !tooMany.isEmpty()) {
            handleBouncingFiles(files, tooBigs, noMatches, tooMany);
        }
        if (!filedetails.isEmpty()) {
            client.updateVariable(paintableId, "filequeue",
                    filedetails.toArray(new String[filedetails.size()]), true);

            disableUpload();
        } else {
            ((InputElement) fu.getElement().cast()).setValue(null);
        }
    }

    protected void handleBouncingFiles(VHtml5File[] files,
            List<VHtml5File> tooBigs, List<VHtml5File> noMatches,
            List<VHtml5File> tooMany) {
        String notificationText = null;
        String errorText = null;
        if (!tooBigs.isEmpty()) {
            if (noMatches.isEmpty()) {
                if (tooMany.isEmpty()) { // tooBigs only
                    if (tooBigs.size() == 1) {
                        notificationText = "File is too big! (max "
                                + maxFileSizeText + ")";
                        errorText = "cancelled an upload because of too large file size";
                    } else {
                        notificationText = "Files are too big! (max "
                                + maxFileSizeText + ")";
                        errorText = "cancelled " + tooBigs.size()
                                + " uploads because of too large file size";
                    }
                } else { // tooBigs and tooMany
                    if (tooMany.size() == 1) {
                        notificationText = "Files are too big and/or there are too many of them! (max "
                                + maxFileSizeText
                                + ", one file over the file count limit)";
                        errorText = "cancelled " + (tooBigs.size() + 1)
                                + " uploads because of too large file size and/or file count exceeded";
                    } else {
                        notificationText = "Files are wrong type and/or there are too many of them! (max "
                                + maxFileSizeText + ", " + tooMany.size()
                                + " files over the file count limit)";
                        errorText = "cancelled "
                                + (tooBigs.size() + tooMany.size())
                                + " uploads because of too large file size and/or file count exceeded";
                    }
                }
            } else {
                if (tooMany.isEmpty()) { // tooBigs and noMatches
                    notificationText = "Files are too big and/or wrong type! (max "
                            + maxFileSizeText + ", accepted: " + accept + ")";
                    errorText = "cancelled " + (tooBigs.size() + noMatches.size())
                            + " uploads because of too large file size and/or wrong file type";
                } else { // tooBigs, noMatches and tooMany
                    if (tooMany.size() == 1) {
                        notificationText = "Files are too big and/or wrong type and/or there are too many of them! (max "
                                + maxFileSizeText + ", accepted: " + accept
                                + ", one file over the file count limit)";
                    } else {
                        notificationText = "Files are too big and/or wrong type and/or there are too many of them! (max "
                                + maxFileSizeText + ", accepted: " + accept
                                + ", " + tooMany.size()
                                + " files over the file count limit)";
                    }
                    errorText = "cancelled "
                            + (tooBigs.size() + noMatches.size()
                                    + tooMany.size())
                            + " uploads because of too large file size and/or wrong file type and/or file count exceeded";
                }
            }
        } else if (!noMatches.isEmpty()) {
            if (tooMany.isEmpty()) { // noMatches only
                if (noMatches.size() == 1) {
                    notificationText = "File is wrong type! (accepted: "
                            + accept + ")";
                    errorText = "cancelled an upload because of wrong file type";
                } else {
                    notificationText = "Files are wrong type! (accepted: "
                            + accept + ")";
                    errorText = "cancelled " + noMatches.size()
                            + " uploads because of wrong file types";
                }
            } else { // noMatches and tooMany
                if (tooMany.size() == 1) {
                    notificationText = "Files are wrong type and/or there are too many of them! (accepted: "
                            + accept + ", one file over the file count limit)";
                    errorText = "cancelled " + (tooBigs.size() + 1)
                            + " uploads because of wrong file type and/or file count exceeded";
                } else {
                    notificationText = "Files are wrong type and/or there are too many of them! (accepted: "
                            + accept + ", " + tooMany.size()
                            + " files over the file count limit)";
                    errorText = "cancelled " + (tooBigs.size() + tooMany.size())
                            + " uploads because of wrong file type and/or file count exceeded";
                }
            }
        } else if (!tooMany.isEmpty()) { // tooMany only
            if (tooMany.size() == 1) {
                notificationText = "There are too many files! (one file over the file count limit)";
                errorText = "cancelled an upload because of file count exceeded";
            } else {
                notificationText = "There are too many files! ("
                        + tooMany.size() + " files over the file count limit)";
                errorText = "cancelled " + tooMany.size()
                        + " uploads because of file count exceeded";
            }
        }
        if (notificationText != null) {
            VNotification
                    .createNotification(1000,
                            client.getUIConnector().getWidget())
                    .show(notificationText, VNotification.CENTERED, "error");
        }
        if (errorText != null) {
            VConsole.error(errorText);
        }
    }

    /**
     * TODO should be part of VHtml5File
     *
     * @param file
     * @return
     */
    private String serialize(VHtml5File file) {
        long size = (long) file.getSize();
        String name = file.getName();
        String type = file.getType();
        return size + DELIM + name + DELIM + type + DELIM;
    }

    private List<VHtml5File> fileQueue = new ArrayList<VHtml5File>();

    private void queueFilePost(VHtml5File file) {
        fileQueue.add(file);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        new Timer() {
            @Override
            public void run() {
                if (!isAttached()) {
                    deregisterFromMultiFileUploadDropArea();
                }
            }
        }.schedule(100);
    }

    private void deregisterFromMultiFileUploadDropArea() {
        if (clikcReg != null) {
            clikcReg.removeHandler();
            clikcReg = null;
        }
    }

    @Override
    public FileUpload getFileUpload() {
        return fu;
    }

    @Override
    public Integer getMaxSize() {
        return maxFileSize;
    }

    @Override
    public void setMaxSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    @Override
    public void setMaxSizeText(String maxFileSizeText) {
        this.maxFileSizeText = maxFileSizeText;
    }

    @Override
    public void setAccept(String accept) {
        this.accept = accept;
        accepted.clear();
        accepted.addAll(AcceptUtil.unpack(accept));
    }

    @Override
    public void setMaxFileCount(Integer maxCount) {
        maxFileCount = maxCount;
    }

}

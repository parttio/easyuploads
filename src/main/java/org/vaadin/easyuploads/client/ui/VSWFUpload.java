package org.vaadin.easyuploads.client.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.swfupload.client.ButtonAction;
import org.swfupload.client.ButtonCursor;
import org.swfupload.client.File;
import org.swfupload.client.SWFUpload;
import org.swfupload.client.UploadBuilder;
import org.swfupload.client.WindowMode;
import org.swfupload.client.event.FileDialogCompleteHandler;
import org.swfupload.client.event.FileQueuedHandler;
import org.swfupload.client.event.UploadCompleteHandler;
import org.swfupload.client.event.UploadErrorHandler;
import org.swfupload.client.event.UploadProgressHandler;
import org.swfupload.client.event.UploadSuccessHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VButton;

/**
 * Upload counterpart with "multiple" support.
 * 
 * Not finished enough for extension.
 */
public class VSWFUpload extends SimplePanel implements Paintable {

    public static final String CLASSNAME = "v-upload";

    private static final String DELIM = "---xXx---";

    ApplicationConnection client;

    private String paintableId;

    List<String> queuedFileIds = new LinkedList<String>();

    private List<File> files = new ArrayList<File>();
    private List<File> newFiles = new ArrayList<File>();

    private String receiverUri;

    private String buttoncaption;

    private DivElement placeholder;

    private DivElement fakeButtonEl;

    private VButton fakebutton = new VButton();

    public VSWFUpload() {
        placeholder = Document.get().createDivElement();
        fakeButtonEl = Document.get().createDivElement();
        getElement().appendChild(placeholder);
        getElement().appendChild(fakeButtonEl);

        setWidget(fakebutton);

        getElement().getStyle().setProperty("minHeight", "26px");

        setStyleName(CLASSNAME);

    }

    @Override
    protected Element getContainerElement() {
        return fakeButtonEl.cast();
    }

    public void updateFromUIDL(UIDL uidl, final ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }
        this.client = client;
        VConsole.log("Building");
        paintableId = uidl.getId();

        // because of bug in flash/swfupload, we need to force session id to url
        String cookie = Cookies.getCookie("JSESSIONID");
        receiverUri = client.translateVaadinUri(uidl
                .getStringVariable("target")) + ";jsessionid=" + cookie;
        buttoncaption = uidl.getStringAttribute("buttoncaption");
        fakebutton.setText(buttoncaption);
        placeholder.setId(paintableId + "-swupph");

        if (upload == null) {
            constructSWFUpload();
        }

        // upload.setUploadURL(receiverUri);

        if (uidl.hasAttribute("disabled") || uidl.hasAttribute("readonly")) {
            // disableUpload();
        } else if (!uidl.getBooleanAttribute("state")) {
            // Enable the button only if an upload is not in progress
            // enableUpload();
        }
        if (uidl.hasAttribute("ready")) {
            VConsole.log("The server knows about coming files. Start posting files");
            sendNextInQueue();
        }
    }

    private SWFUpload upload;

    protected void constructSWFUpload() {
        VConsole.log("Building");

        final UploadBuilder builder1 = new UploadBuilder();
        // builder1.setDebug(true);
        builder1.setHTTPSuccessCodes(200, 201);
        builder1.setSwfUrl(GWT.getModuleBaseURL() + "swfupload.swf");

        builder1.setFileTypes("*.*");
        builder1.setFileTypesDescription("All files");

        builder1.setButtonPlaceholderID(paintableId + "-swupph");

        builder1.setWindowMode(WindowMode.TRANSPARENT);
        builder1.setButtonCursor(ButtonCursor.HAND);

        builder1.setButtonDisabled(false);
        builder1.setButtonCursor(ButtonCursor.HAND);
        // builder1.setButtonCursor(ButtonCursor.ARROW);

        final int offsetWidth = fakebutton.getOffsetWidth();
        builder1.setButtonWidth(offsetWidth);
        final int offsetHeight = fakebutton.getOffsetHeight();
        builder1.setButtonHeight(offsetHeight);
        builder1.setButtonAction(ButtonAction.SELECT_FILES);

        builder1.setUploadProgressHandler(new UploadProgressHandler() {

            public void onUploadProgress(UploadProgressEvent e) {
                File f = e.getFile();
                f.getName();
                VConsole.log(e.getBytesComplete() + "; " + f.getName());
            }
        });

        builder1.setUploadSuccessHandler(new UploadSuccessHandler() {
            public void onUploadSuccess(UploadSuccessEvent e) {
                files.remove(0);
                sendNextInQueue();
            }
        });

        builder1.setUploadErrorHandler(new UploadErrorHandler() {
            public void onUploadError(UploadErrorEvent e) {
                String message = e.getMessage();
                if (message == null || message.trim().length() == 0) {
                    message = "upload failed";
                }
                VConsole.log(message);
            }
        });

        // "http://localhost:8080/swfupload-gwt-demo/upload"
        VConsole.log("Building" + receiverUri);

        builder1.setUploadURL(receiverUri);

        builder1.setUploadCompleteHandler(new UploadCompleteHandler() {
            public void onUploadComplete(UploadCompleteEvent e) {
                File f = e.getFile();
                VConsole.log("done : " + f.getId() + ", " + f.getName());
            }
        });

        builder1.setFileQueuedHandler(new FileQueuedHandler() {
            public void onFileQueued(FileQueuedEvent event) {
                VConsole.log("ofq: " + event.getFile().getId() + "; "
                        + event.getFile().getName());
                newFiles.add(event.getFile());
            }
        });

        builder1.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {
            public void onFileDialogComplete(FileDialogCompleteEvent e) {
                if (newFiles.size() > 0) {
                    String[] filedetails = new String[newFiles.size()];
                    int i = 0;
                    for (File file : newFiles) {
                        filedetails[i++] = serialize(file);
                    }
                    client.updateVariable(paintableId, "filequeue",
                            filedetails, true);
                    files.addAll(newFiles);
                    newFiles.clear();
                }
            }
        });
        VConsole.log("About to build...");
        upload = builder1.build();
        VConsole.log("Built");

    }

    private void sendNextInQueue() {
        VConsole.log("Starting file upload");
        boolean filesInQueue = files.size() > 0;

        if (filesInQueue) {
            VConsole.error("sending first from queue");
            File ff = files.get(0);
            String id = ff.getId();
            VConsole.log("start: " + id);
            upload.startUpload(id);
            VConsole.log("Started file upload");
            new Timer() {
                @Override
                public void run() {
                    // TODO poll for start or modify response so that we
                    // receive headers received
                    client.sendPendingVariableChanges();
                }
            }.schedule(700);
        } else {
            VConsole.log("File queue empty");
        }
    }

    /**
     * 
     * @param file
     * @return
     */
    private String serialize(File file) {
        VConsole.log("File");
        long size = file.getSize();
        VConsole.log("" + size);
        String name = file.getName();
        VConsole.log("" + name);
        String type = file.getType();
        VConsole.log("" + type);
        return size + DELIM + name + DELIM + type + DELIM;
    }

}

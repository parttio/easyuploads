package org.vaadin.easyuploads.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.dd.VHtml5File;

/**
 * Upload counterpart with "multiple" support.
 * 
 * Not finished enough for extension.
 */
public class VMultiUpload extends SimplePanel implements Paintable {

	private final class MyFileUpload extends FileUpload {

		//  Starting from Chrome 30 we should prevent the onFocus hack or
		//  otherwise the native file dialog is opened twice.
		private final boolean isChrome30 = (BrowserInfo.get().isChrome() && BrowserInfo
				.get().getBrowserMajorVersion() >= 30);
	        
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

	public static final native VHtml5File getFile(Element el, int i)
	/*-{
		return el.files[i];
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

	private Hidden maxfilesize = new Hidden();

	private com.google.gwt.dom.client.Element synthesizedFrame;

	private String receiverUri;

	private ReadyStateChangeHandler readyStateChangeHandler = new ReadyStateChangeHandler() {
		public void onReadyStateChange(XMLHttpRequest xhr) {
			if (xhr.getReadyState() == XMLHttpRequest.DONE) {
				xhr.clearOnReadyStateChange();
				VConsole.log("Ready state + " + xhr.getReadyState());
				
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
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
	};;

	public VMultiUpload() {
		super(com.google.gwt.dom.client.Document.get().createDivElement());

		setWidget(panel);
		panel.add(maxfilesize);
		panel.add(fu);
		submitButton = new VButton();
		submitButton.addClickHandler(new ClickHandler() {
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

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (client.updateComponent(this, uidl, true)) {
			return;
		}
		addStyleName(CLASSNAME + "-immediate");


		this.client = client;
		paintableId = uidl.getId();
		receiverUri = client.translateVaadinUri(uidl.getStringVariable("target"));
		submitButton.setText(uidl.getStringAttribute("buttoncaption"));
		fu.setName(paintableId + "_file");

		if (uidl.hasAttribute("disabled") || uidl.hasAttribute("readonly")) {
			disableUpload();
		} else if (!uidl.getBooleanAttribute("state")) {
			// Enable the button only if an upload is not in progress
			enableUpload();
		}
		if (uidl.hasAttribute("ready")) {
			VConsole.log("The server knows about coming files. Start posting files");
			postNextFileFromQueue();
		}
	}

	private void postNextFileFromQueue() {
		if (!fileQueue.isEmpty()) {
			final VHtml5File file = fileQueue.remove(0);
			VConsole.log("Posting file " + file.getName() + " to "
					+ receiverUri);
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
		int files = getFileCount(fu.getElement());
		String[] filedetails = new String[files];
		for (int i = 0; i < files; i++) {
			VHtml5File file = getFile(fu.getElement(), i);
			queueFilePost(file);
			filedetails[i] = serialize(file);
		}
		client.updateVariable(paintableId, "filequeue", filedetails, true);

		disableUpload();
	}

	/**
	 * TODO should be part of VHtml5File
	 * 
	 * @param file
	 * @return
	 */
	private String serialize(VHtml5File file) {
		int size = file.getSize();
		String name = file.getName();
		String type = file.getType();
		return size + DELIM + name + DELIM + type + DELIM;
	}

	private List<VHtml5File> fileQueue = new ArrayList<VHtml5File>();

	private void queueFilePost(VHtml5File file) {
		fileQueue.add(file);
	}

}

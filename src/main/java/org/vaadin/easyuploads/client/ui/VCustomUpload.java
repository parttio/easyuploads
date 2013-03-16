package org.vaadin.easyuploads.client.ui;

import com.google.gwt.user.client.Element;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VNotification;
import com.vaadin.client.ui.VUpload;

import elemental.html.File;
import elemental.html.FileList;
import elemental.html.InputElement;

public class VCustomUpload extends VUpload {

	private Integer maxSize;

	@Override
	public void submit() {
		VConsole.error("Submit");
		if (maxSize == null || checkSize(maxSize)) {
			super.submit();
		} else {
			VConsole.error("cancelled upload due to too large file");
			((InputElement) fu.getElement()).setValue(null);
		}
	}

	private boolean checkSize(int maxSize) {
		try {
			InputElement ie = (InputElement) fu.getElement();

			FileList files = ie.getFiles();
			for (int i = 0; i < files.getLength(); i++) {
				File item = files.item(i);
				if (item.getSize() > maxSize) {
					VNotification.createNotification(1000,
							client.getUIConnector().getWidget()).show(
							"File is too big! (max " + maxSize + ")",
							VNotification.CENTERED, "warning");
					return false;
				}
			}
		} catch (Exception e) {
			VConsole.error("Detecting file size failed");
			// ActiveX check might work with older win, but not with modern &&
			// default settings so disabled for now

			// try {
			// checkSizeIe(fu.getElement(), maxSize);
			// } catch (Exception e2) {
			// VConsole.error("Failed with IE hack");
			// }
		}
		return true;
	}

	/**
	 * @param element
	 * @param maxSize
	 * @return
	 */
	private static native boolean checkSizeIe(Element element, int maxSize)
	/*-{
	var myFSO = new $wnd.ActiveXObject("Scripting.FileSystemObject");
	var filepath = element.value;
	var thefile = myFSO.getFile(filepath);
	var size = thefile.size;
	    
	if(size > maxSize) {
		return false;
	}
	return true;
	}-*/;

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

}

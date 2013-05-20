package org.vaadin.easyuploads.client;

import org.vaadin.easyuploads.Html5FileInputSettings;
import org.vaadin.easyuploads.client.ui.VCustomUpload;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(Html5FileInputSettings.class)
public class Html5FileInputSettingsConnector extends AbstractExtensionConnector {

	private InputElement input;

	private InputElement getInput() {
		final VCustomUpload widget = getUpload();
		if (input == null || !widget.getElement().isOrHasChild((Node) input)) {
			VConsole.error("Registering " + (input == null));
			input = (InputElement) widget.fu.getElement().cast();
		}
		return input;
	}

	private VCustomUpload getUpload() {
		AbstractComponentConnector parent3 = (AbstractComponentConnector) getParent();
		final VCustomUpload widget = (VCustomUpload) parent3.getWidget();
		return widget;
	}

	@Override
	protected void extend(ServerConnector target) {
		// TODO WTF should be done here??
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		VConsole.error("statechange");
		String accept = getState().accept;
		getUpload().setMaxSize(getState().maxSize);
		getInput().setAccept(accept);

	}

	@Override
	public Html5FileInputState getState() {
		return (Html5FileInputState) super.getState();
	}

}

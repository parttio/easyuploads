package org.vaadin.easyuploads.client;

import org.vaadin.easyuploads.Html5FileInputSettings;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Widget;
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
        final HasFileUpload widget = getUpload();
        if (input == null || !((Widget) widget).getElement().isOrHasChild(input)) {
            VConsole.error("Registering " + (input == null));
            input = (InputElement) widget.getFileUpload().getElement().cast();
        }
        return input;
    }

    private HasFileUpload getUpload() {
        AbstractComponentConnector parent3 = (AbstractComponentConnector) getParent();
        HasFileUpload widget = (HasFileUpload) parent3.getWidget();
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
        HasFileUpload upload = getUpload();
        upload.setMaxSize(getState().maxSize);
        upload.setMaxSizeText(getState().maxSizeText);
        upload.setMaxFileCount(getState().maxFileCount);
        upload.setAccept(accept);
        getInput().setAccept(accept);

    }

    @Override
    public Html5FileInputState getState() {
        return (Html5FileInputState) super.getState();
    }

    public interface HasFileUpload {

        public FileUpload getFileUpload();

        public void setAccept(String accept);

        public void setMaxSize(Integer maxSize);

        public Integer getMaxSize();

        void setMaxSizeText(String maxFileSizeText);

        void setMaxFileCount(Integer maxCount);
    }

}

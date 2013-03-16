package org.vaadin.easyuploads.client.ui;

import org.vaadin.easyuploads.MultiUpload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(MultiUpload.class)
public class Multiuploadconnector extends AbstractComponentConnector implements Paintable {

    
    @Override
    protected Widget createWidget() {
    	return GWT.create(VMultiUpload.class);
    }

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		((Paintable)getWidget()).updateFromUIDL(uidl, client);
	}

}

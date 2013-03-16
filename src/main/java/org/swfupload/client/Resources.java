package org.swfupload.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {
	
	static Resources INSTANCE = GWT.create(Resources.class);
	
    @Source("swfupload.js")
    TextResource script();

}

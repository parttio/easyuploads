package org.vaadin.easyuploads.demoandtestapp;

import com.vaadin.annotations.Widgetset;
import org.vaadin.addonhelpers.TServer;

@Widgetset("org.vaadin.easyuploads.demoandtestapp.TestWidgetset")
public class UiRunner extends TServer {

	/**
     * Starts and embedded server for the tests
     * @param args
     * @throws java.lang.Exception
	 */
	public static void main(String[] args) throws Exception {
        new UiRunner().startServer();
	}
}

package org.swfupload.client;

public enum WindowMode {
	WINDOW("window"), TRANSPARENT("transparent"), OPAQUE("opaque");

	private String value;

	private WindowMode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
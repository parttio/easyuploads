package org.swfupload.client;

public enum ButtonCursor {
	ARROW(-1), HAND(-2);

	private int value;

	private ButtonCursor(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
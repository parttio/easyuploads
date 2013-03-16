package org.swfupload.client;

public enum ButtonAction {
	SELECT_FILE(-100), SELECT_FILES(-110), START_UPLOAD(-120);

	private int value;

	private ButtonAction(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
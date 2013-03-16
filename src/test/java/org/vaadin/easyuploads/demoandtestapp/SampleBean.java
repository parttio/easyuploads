package org.vaadin.easyuploads.demoandtestapp;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class SampleBean {

	// private DateTime dateTime;
	private LocalDate localDate;
	private String text;

	// /**
	// * @return the dateTime
	// */
	// public DateTime getDateTime() {
	// return dateTime;
	// }
	//
	// /**
	// * @param dateTime
	// * the dateTime to set
	// */
	// public void setDateTime(DateTime dateTime) {
	// this.dateTime = dateTime;
	// }

	/**
	 * @return the localDate
	 */
	public LocalDate getLocalDate() {
		return localDate;
	}

	/**
	 * @param localDate
	 *            the localDate to set
	 */
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Text: " + text + " , DateTime: "
		// + dateTime + ","
				+ " LocalDate: " + localDate;
	}

}

package com.example.demo.config;

import java.sql.Timestamp;
import java.text.*;
import java.util.Date;

public class CustomDateFormat extends DateFormat {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3148360618415929477L;

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		try {
			if(date instanceof Timestamp) {
				return new StringBuffer(timeFormat.format(date));
			} else {
				return new StringBuffer(dateFormat.format(date));
			}
		} catch (Exception e) {
			return new StringBuffer();
		}
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		try {
			if(null != source && source.length() > 10) {
				return timeFormat.parse(source);
			} else {
				return dateFormat.parse(source);
			}
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public Date parse(String source) throws ParseException {
		try {
			if(null != source && source.length() > 10) {
				return timeFormat.parse(source);
			} else {
				return dateFormat.parse(source);
			}
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public Object clone() {
		return this;
	}

}

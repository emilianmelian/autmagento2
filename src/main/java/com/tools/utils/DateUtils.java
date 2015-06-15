package com.tools.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String getLastDayOfTheCurrentMonth(String format) {
		Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date lastDayOfMonth = calendar.getTime();
		DateFormat sdf = new SimpleDateFormat(format);

		return String.valueOf(sdf.format(lastDayOfMonth));
	}

	public static String getCurrentDate(String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		Date today = new Date();

		return String.valueOf(sdf.format(today));
	}

}

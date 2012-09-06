package com.icelero.ias.ad.shared.utilities;

import java.util.ArrayList;
import java.util.List;

public class TimeUtilities {
	public static final String[] HOURS = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
			"14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
	public static final String[] MINUTES = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
			"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
			"34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
			"55", "56", "57", "58", "59" };

	private TimeUtilities() {
	}

	public static List<String> getHourList() {
		return asList(HOURS);
	}

	public static List<String> getMinuteList() {
		return asList(MINUTES);
	}

	public static boolean isValidHour(String text) {
		boolean valid = true;
		try {
			int value = Integer.parseInt(text);
			if (value < 0 || value > 23) {
				valid = false;
			}
		} catch (Exception ex) {
			valid = false;
		}
		return valid;
	}

	public static boolean isValidMinute(String text) {
		boolean valid = true;
		try {
			int value = Integer.parseInt(text);
			if (value < 0 || value > 59) {
				valid = false;
			}
		} catch (Exception ex) {
			valid = false;
		}
		return valid;
	}

	public static String getFormattedHour(int hour) {
		if (hour < 0 || hour > 23) {
			return null;
		}
		if (hour < 10) {
			return "0" + hour;
		}
		return "" + hour;
	}

	public static String getFormattedMinute(int minute) {
		if (minute < 0 || minute > 59) {
			return null;
		}
		if (minute < 10) {
			return "0" + minute;
		}
		return "" + minute;
	}

	public static List<String> asList(String[] elements) {
		List<String> list = new ArrayList<String>();
		for (String element : elements) {
			list.add(element);
		}
		return list;
	}

	public static String getHoursFromSeconds(int seconds) {
		int hours = 0;
		if (seconds > 0) {
			hours = seconds / 3600;
		}
		return getFormattedHour(hours);
	}

	public static String getMinutesFromSeconds(int seconds) {
		int minutes = 0;
		if (seconds > 0) {
			minutes = (seconds % 3600) / 60;
		}
		return getFormattedMinute(minutes);
	}

	public static int setHoursToSeconds(String stringHours) {
		int seconds = 0;
		int hours = Integer.parseInt(stringHours);
		if (hours > 0) {
			seconds = hours * 60 * 60;
		}
		return seconds;
	}

	public static int setMinutesToSeconds(String StringMinutes) {
		int seconds = 0;
		int minutes = Integer.parseInt(StringMinutes);
		if (minutes > 0) {
			seconds = minutes * 60;
		}
		return seconds;
	}

}

package com.mmt.flights.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

	public static final long LAY_OVER_TIME = 120;
	public static final String ONE_ZERO = "0";
	public static final String TWO_ZERO = "00";
	public static final String THREE_ZERO = "000";
	public static final String DTF = "yyyy-MM-ddHHmm";

	public static LocalDateTime getNextValidTimeForConnectingFlight(LocalDateTime time) {
		return time.plusMinutes(LAY_OVER_TIME);
	}

	public static LocalDateTime getLocalDateTime(String time, int noOfDayToAdd) {
		int length = 4 - time.length();

		if (length == 1) {
			time = ONE_ZERO + time;
		}
		else if (length == 2) {
			time = TWO_ZERO + time;
		}
		else if(length == 3) {
			time = THREE_ZERO + time;
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DTF);
		LocalDate localDate = LocalDate.now();
		localDate = localDate.plusDays(noOfDayToAdd);
		String todayDate = localDate+time;
		return LocalDateTime.parse(todayDate,dtf);
	}


}

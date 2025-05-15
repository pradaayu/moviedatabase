package utils;

import java.time.Instant;
import java.util.Date;

public class ISODateConverter {
	/**
	 * Converts a string representation of a date in ISO 8610 format to {@link Date}
	 * @param isoDate
	 * @return
	 */
	public static Date toDate(String isoDate) {
		if (isoDate.isBlank()) {
			return null;
		}
		Instant instant = Instant.parse(isoDate);
		Date date = Date.from(instant);
		return date;
	}
    /**
     * Converts to a string representation of {@link Date} in ISO 8610 format
     * @param date
     * @return
     */
	public static String toISOString(Date date) {
		return date.toInstant().toString();
	}
}

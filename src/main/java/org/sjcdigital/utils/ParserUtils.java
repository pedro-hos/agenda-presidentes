/**
 * 
 */
package org.sjcdigital.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author pedro-hos@outlook.com
 *
 */
public class ParserUtils {
	
	public static DateTimeFormatter DATA_PATTERN_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static DateTimeFormatter DATA_PATTERN_DD_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public static LocalDate convertToLocalDate(final String value) {
		return convertToLocalDate(value, DATA_PATTERN_YYYY_MM_DD);
	}
	
	public static LocalDate convertToLocalDate(final String value, final DateTimeFormatter pattern) {
		return Objects.isNull(value) ? null : LocalDate.parse(value, pattern);
	}

	public static Integer convertToInteger(final String value) {
		return Objects.isNull(value) ? null : Integer.valueOf(value);
	}

	public static boolean empty(final String s) {
		// Null-safe, short-circuit evaluation.
		return s == null || s.trim().isEmpty();
	}
}

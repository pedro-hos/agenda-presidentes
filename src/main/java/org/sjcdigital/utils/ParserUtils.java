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

	public static LocalDate convertToLocalDate(final String value) {
		return Objects.isNull(value) ? null : LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public static Integer convertToInteger(final String value) {
		return Objects.isNull(value) ? null : Integer.valueOf(value);
	}

	public static boolean empty(final String s) {
		// Null-safe, short-circuit evaluation.
		return s == null || s.trim().isEmpty();
	}
}

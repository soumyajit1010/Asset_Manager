package assetmamager.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for resource-related helper methods.
 */
public final class Resources {

	private static final Map<String, Integer> MONTH_MAP = new HashMap<>();

	static {
		MONTH_MAP.put("Jan", 1);
		MONTH_MAP.put("Feb", 2);
		MONTH_MAP.put("Mar", 3);
		MONTH_MAP.put("Apr", 4);
		MONTH_MAP.put("May", 5);
		MONTH_MAP.put("Jun", 6);
		MONTH_MAP.put("Jul", 7);
		MONTH_MAP.put("Aug", 8);
		MONTH_MAP.put("Sep", 9);
		MONTH_MAP.put("Oct", 10);
		MONTH_MAP.put("Nov", 11);
		MONTH_MAP.put("Dec", 12);
	}

	// Private constructor to prevent instantiation
	private Resources() {}

	/**
	 * Returns the numeric month for a 3-letter month abbreviation.
	 *
	 * @param month 3-letter month string (e.g., "Jan", "Feb")
	 * @return month number (1-12), 0 if invalid
	 */
	public static int getMonth(String month) {
		if (month == null || month.length() != 3) return 0;
		return MONTH_MAP.getOrDefault(month, 0);
	}
}

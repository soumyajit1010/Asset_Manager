package assetmamager.service;

// Resources class
public class Resources {
	public static int getMonth(String month) {
		if (month == null || month.length() != 3 || !Character.isUpperCase(month.charAt(0))) {
			return 0;
		}
		switch (month) {
			case "Jan": return 1;
			case "Feb": return 2;
			case "Mar": return 3;
			case "Apr": return 4;
			case "May": return 5;
			case "Jun": return 6;
			case "Jul": return 7;
			case "Aug": return 8;
			case "Sep": return 9;
			case "Oct": return 10;
			case "Nov": return 11;
			case "Dec": return 12;
			default: return 0;
		}
	}
}

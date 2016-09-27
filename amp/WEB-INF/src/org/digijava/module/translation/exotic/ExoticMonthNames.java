package org.digijava.module.translation.exotic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class containing month names with locales unsupported by Java 8. 
 * To add a new one, create a file ExoticMonthNames_{two-letter-code}.properties in this package. 
 * Originally created for Tetum (Timor). 
 * @author acartaleanu
 *
 */
public class ExoticMonthNames {
	
	private final Locale locale;
	private final List<String> names;
	
	private ExoticMonthNames(List<String> names, Locale loc) {
		this.names = Collections.unmodifiableList(names);
		this.locale = loc;
	}
	
	static ExoticMonthNames forLocale(Locale loc) {
		return localesToMonthNames.computeIfAbsent(loc, z -> new ExoticMonthNames(loadMonthNamesForLocale(loc), loc));
	}
	
	private static ConcurrentHashMap<Locale, ExoticMonthNames> localesToMonthNames = new ConcurrentHashMap<>();
	
	private static List<String> loadMonthNamesForLocale(Locale loc) {
		List<String> res = new ArrayList<>();
		ResourceBundle rb = ResourceBundle.getBundle("org.digijava.module.translation.exotic.ExoticMonthNames", loc);
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			res.add(value);
		}
		return res;
	}

	
	List<String> getMonthNames() {
		return names;
	}
	
	/**
	 * 
	 * @param monthNumber number of the month, starting from 1
	 * @return capitalized month name in that language
	 */
	public String getFullMonthName(int monthNumber) {
		return getMonthNames().get(monthNumber - 1);

	}
	
	private static String shortenMonthName(String in) {
		return in.substring(0, 3);
	}
	
	public String getShortMonthName(int monthNumber) {
		return shortenMonthName(getFullMonthName(monthNumber));
	}
	
	public int getMonthNumber(String shortMonthName) {
		List<String> monthNames = getMonthNames();
		for (int i = 0; i < monthNames.size(); i++) {
			if (shortenMonthName(monthNames.get(i)).equals(shortMonthName))
				return i + 1;
		}
		throw new RuntimeException("Couldn't find month with short name" + shortMonthName + " in locale " + locale.toString());
	}

}

package org.digijava.module.translation.exotic;

import java.util.Locale;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;

public class AmpDateFormatterFactory {
	
	private static boolean isLangCodeSupported(String langCode) {
		Locale[] supportedLocales = Locale.getAvailableLocales();
		for (Locale supLoc : supportedLocales) {
			if (langCode.equals(supLoc.getLanguage())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the locale if none is specified. 
	 * The default is English to avoid weird behaviour in the case of 
	 * non-English systems.
	 * @return
	 */
	public static AmpDateFormatter getDefaultFormatter() {
		return new AmpSimpleDateFormatter(DateTimeUtil.getGlobalPattern(), Locale.ENGLISH);
	}
	
	public static AmpDateFormatter getLocalizedFormatter() {
		return getLocalizedFormatter(DateTimeUtil.getGlobalPattern());
	}

	public static AmpDateFormatter getLocalizedFormatter(String format) {
		String langCode = TLSUtils.getEffectiveLangCode();
		if (isLangCodeSupported(langCode))
			return new AmpSimpleDateFormatter(format, langCode);
		else 
			return new ExoticDateFormatter(format, langCode);
	}
	

}

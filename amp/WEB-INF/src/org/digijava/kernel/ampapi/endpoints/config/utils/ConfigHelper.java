/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.config.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.common.util.DateTimeUtil;


/**
 * Helper methods for config save
 * 
 * @author apicca
 */
public class ConfigHelper {

	private static final String SECTION = "section";
	private static final String DESCRIPTION = "description";
	private static final String SETTINGS_VALUE = "settingValue";
	private static final String POSSIBLE_VALUES = "possibleValues";
	private static final String SETTINGS_NAME = "settingName";
	private static final String VALUE_TRANSLATABLE = "valueTranslatable";
	private static final String ORG_DIGIJAVA_MODULE_AIM_HELPER_GLOBAL_SETTINGS_CONSTANTS = "org.digijava.module.aim.helper.GlobalSettingsConstants";

	public static final String T_BOOLEAN = "t_Boolean";
	public static final String T_INTEGER = "t_Integer";
	public static final String T_INTEGER_NON_NEGATIVE = "t_Integer_non_negative";
	public static final String T_INTEGER_POSITIVE = "t_Integer_positive";
	public static final String T_YEAR_DEFAULT_START = "t_year_default_start";
	public static final String T_YEAR_DEFAULT_END = "t_year_default_end";
	public static final String T_STATIC_RANGE = "t_static_range";
	public static final String T_DOUBLE = "t_Double";
	public static final String T_STATIC_YEAR = "t_static_year";
	public static final String T_YEAR = "t_year";
	public static final String T_AUDIT_TRIAL_CLENAUP = "t_audit_trial_clenaup";
	public static final String T_COMPONENTS_SORT = "t_components_sort";
	public static final String T_DAILY_CURRENCY_UPDATE_HOUR = "t_daily_currency_update_hour";
	public static final String T_SECURE_VALUES = "t_secure_values";
	public static final String T_TIMEOUT_CURRENCY_UPDATE = "t_timeout_currency_update";
	public static final String T_DATE = "t_Date";

	private static final String NULL_VALUE = "null";
	private static final String TIMEOUT_CURRENCY_UPDATE_PATTERN = "(1[012]|0[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
	private static final Logger logger = Logger.getLogger(ConfigHelper.class);
	/**
	 * Retrieves the class specified as type for Generics
	 * @param field
	 * @return
	 */
	public static ArrayList<String> getValidSettings() {
		ArrayList<String> list = new ArrayList<String>();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(ORG_DIGIJAVA_MODULE_AIM_HELPER_GLOBAL_SETTINGS_CONSTANTS);

			Field[] fields = clazz.getDeclaredFields();

			for (Field f : fields) {
				if ("string".equalsIgnoreCase(f.getType().getSimpleName())) {
					list.add((String) f.get(null));
					System.err.printf("%s: %s\n", f, (String) f.get(null));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Retrieves AmpGlobalSettings from request
	 * @param object
	 * @return AmpGlobalSettings
	 */
	public static AmpGlobalSettings getGlobalSetting(AmpGlobalSettings set,LinkedHashMap<String, Object> object) {
		set.setGlobalSettingsName(String.valueOf(object.get(SETTINGS_NAME)));
		set.setGlobalSettingsPossibleValues(String.valueOf(object.get(POSSIBLE_VALUES)));
		set.setGlobalSettingsValue(String.valueOf(object.get(SETTINGS_VALUE)));
		if ("null".equalsIgnoreCase(set.getGlobalSettingsPossibleValues())) {
			set.setGlobalSettingsPossibleValues("");
		}
		set.setGlobalSettingsDescription(String.valueOf(object.get(DESCRIPTION)));
		set.setSection(String.valueOf(object.get(SECTION)));
		set.setValueTranslatable(Boolean.valueOf(String.valueOf(object.get(VALUE_TRANSLATABLE))));
		return set;
	}
	
	/**
	 * Retrieves JsonBean from AmpGlobalSettings
	 * @param object
	 * @return JsonBean
	 */
	public static JsonBean getGlobalSettingJson(AmpGlobalSettings ampGlobalSetting) {
		JsonBean globalSetting = new JsonBean();
		List<KeyValue> possiblesValues = ConfigHelper.getPossibleValues(ampGlobalSetting.getGlobalSettingsPossibleValues());
		JsonBean pValues = new JsonBean();
		if (possiblesValues!=null) {
			for (KeyValue value : possiblesValues) {
				pValues.set(value.getValue(), value.getKey());
			}
		}
		globalSetting.set("settingName", ampGlobalSetting.getGlobalSettingsName());
		globalSetting.set("settingValue", ampGlobalSetting.getGlobalSettingsValue());
		globalSetting.set("possibleValues", ampGlobalSetting.getGlobalSettingsPossibleValues()); 
		globalSetting.set("description", ampGlobalSetting.getGlobalSettingsDescription()); 
		globalSetting.set("section", ampGlobalSetting.getSection()); 
		globalSetting.set("valueTranslatable", ampGlobalSetting.getValueTranslatable());
		globalSetting.set("possibleValuesIds", pValues);
		
		return globalSetting;
	}
	
	/**
	 * Validate settingValue
	 * @param object
	 * @return boolean
	 */

	public static boolean validateGlobalSetting(AmpGlobalSettings ampGlobalSetting) {
		boolean isValid = false;
		List<KeyValue> possiblesValues = ConfigHelper.getPossibleValues(ampGlobalSetting.getGlobalSettingsPossibleValues());
		switch (ampGlobalSetting.getGlobalSettingsPossibleValues()) {
			case T_BOOLEAN:
				isValid = "true".equalsIgnoreCase(ampGlobalSetting.getGlobalSettingsValue()) || "false".equalsIgnoreCase(ampGlobalSetting.getGlobalSettingsValue());
				break;
			case T_SECURE_VALUES:
				isValid = "on".equalsIgnoreCase(ampGlobalSetting.getGlobalSettingsValue()) || "off".equalsIgnoreCase(ampGlobalSetting.getGlobalSettingsValue());
				break;
			case T_DATE:
				try {
					Date testDate = DateTimeUtil.parseDate(ampGlobalSetting.getGlobalSettingsValue());
					isValid = true;
				} catch (Exception e) { // value is not an Date
					isValid = false;
				}
				break;
			case T_INTEGER:
			case T_INTEGER_NON_NEGATIVE:
			case T_INTEGER_POSITIVE:
			case T_TIMEOUT_CURRENCY_UPDATE:
			case T_AUDIT_TRIAL_CLENAUP:
			case T_STATIC_RANGE:
				isValid = isValidNumber(Integer.class, ampGlobalSetting.getGlobalSettingsValue());
				int integerValue = Integer.parseInt(ampGlobalSetting.getGlobalSettingsValue());
				if ((T_INTEGER_NON_NEGATIVE.equals(ampGlobalSetting.getGlobalSettingsPossibleValues()) && isValid && integerValue < 0) ||
						(T_INTEGER_POSITIVE.equals(ampGlobalSetting.getGlobalSettingsPossibleValues()) && isValid && integerValue <= 0)) {
					isValid = false;
				}
				break;
			case T_YEAR:
			case T_YEAR_DEFAULT_START:
			case T_YEAR_DEFAULT_END:
			case T_STATIC_YEAR:
				isValid = isValidNumber(Integer.class, ampGlobalSetting.getGlobalSettingsValue());
				int currentValue = Integer.parseInt(ampGlobalSetting.getGlobalSettingsValue());
				if (isValid && currentValue != -1 && (currentValue < 1000 || currentValue > 2999)) {
					isValid = false;
				} else {
					isValid = true;
				}
				break;
			case T_DOUBLE:
				isValid = isValidNumber(Double.class, ampGlobalSetting.getGlobalSettingsValue());
				break;
			case NULL_VALUE:
			case "":
			case T_COMPONENTS_SORT:
				isValid = true;
				break;
			case T_DAILY_CURRENCY_UPDATE_HOUR:
				Pattern pattern = Pattern.compile(TIMEOUT_CURRENCY_UPDATE_PATTERN);
				Matcher matcher = pattern.matcher(ampGlobalSetting.getGlobalSettingsValue());
				isValid = matcher.matches();
				break;
			default:
				if (possiblesValues != null) {
					for (KeyValue value : possiblesValues) {
						if (ampGlobalSetting.getGlobalSettingsValue().equals(value.getKey())) {
							isValid = true;
						}
					}
				}
				break;
		}
		return isValid;
	}
	/**
	* Does a try catch (Exception) on parsing the given string to the given type
	*
	* @param c the number type. Valid types are (wrappers included): double, int, float, long.
	* @param numString number to check
	* @return false if there is an exception, true otherwise
	*/
	public static boolean isValidNumber(Class c, String numString) {
	  try {
	    if (c == double.class || c == Double.class) {
	      Double.parseDouble(numString);
	    } else if (c == int.class || c == Integer.class) {
	      Integer.parseInt(numString);
	    } else if (c == float.class || c == Float.class) {
	      Float.parseFloat(numString);
	    } else if (c == long.class || c == Long.class) {
	      Long.parseLong(numString);
	    }
	  } catch (Exception ex) {
	    return false;
	  }
	  return true;
	}
	
	public static String getGlobalSettingName(LinkedHashMap<String, Object> object) {
		String name = String.valueOf(object.get(SETTINGS_NAME));
		return name;
	}
	
	public static List<KeyValue> getPossibleValues(String tableName) {
		List<KeyValue> ret = new ArrayList<>();

		if (tableName == null || tableName.length() == 0 || tableName.startsWith("t_") )
			return ret;

		List<Object[]> ls = null;
		try {
			ls = PersistenceManager.getSession().createSQLQuery("select id, value from " + tableName).list();
		} catch (Exception e) {
			return null;
		}
		for (Object[] obj : ls) {
			KeyValue keyValue = new KeyValue(PersistenceManager.getString(obj[0]), PersistenceManager.getString(obj[1]));
			ret.add(keyValue);
		}
		return ret;
	}
}

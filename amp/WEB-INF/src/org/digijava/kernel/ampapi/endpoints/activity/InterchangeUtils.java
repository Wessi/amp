package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.PathSegment;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;

import com.sun.jersey.spi.container.ContainerRequest;

/**
 * Activity Import/Export Utility methods 
 * 
 */
public class InterchangeUtils {

	public static final Logger LOGGER = Logger.getLogger(InterchangeUtils.class);
	private static Map<String, String> underscoreToTitleMap = new HashMap<String, String>();
	private static Map<String, String> titleToUnderscoreMap = new HashMap<String, String>();
	
	/**map from discriminator title (i.e. "Primary Sectors") to actual field name (i.e. "Sectors")
	 */
	private static Map<String, String> discriminatorMap = new HashMap<String, String> ();
	static {
		addUnderscoredTitlesToMap(AmpActivityFields.class);
	}
	
	private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<SimpleDateFormat>();
	
	public static String getDiscriminatedFieldTitle(String fieldName) {
		return discriminatorMap.get(deunderscorify(fieldName));
	}
	
	

	public static boolean isFieldEnumerable(Field inputField) {
		Class clazz = getClassOfField(inputField);
		if (isSimpleType(clazz))
			return false;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class); 
			if ( ant != null && ant.id())
				return true;
		}
		return false;
	}
	
	private static void addUnderscoredTitlesToMap(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			Interchangeable ant = field.getAnnotation(Interchangeable.class);
			if (ant != null) {
				if (!isCompositeField(field))
				{
					underscoreToTitleMap.put(underscorify(ant.fieldTitle()), ant.fieldTitle());
					titleToUnderscoreMap.put(ant.fieldTitle(), underscorify(ant.fieldTitle()));
				} else {
					InterchangeableDiscriminator antd = field.getAnnotation(InterchangeableDiscriminator.class);
					Interchangeable[] settings = antd.settings();
					if (settings.length == 0) {
						underscoreToTitleMap.put(underscorify(ant.fieldTitle()), ant.fieldTitle());
						titleToUnderscoreMap.put(ant.fieldTitle(), underscorify(ant.fieldTitle()));
					}
					for (Interchangeable ants : settings) {
						underscoreToTitleMap.put(underscorify(ants.fieldTitle()), ants.fieldTitle());
						titleToUnderscoreMap.put(ants.fieldTitle(), underscorify(ants.fieldTitle()));
						discriminatorMap.put(ants.fieldTitle(), ant.fieldTitle());
					}
				}
				if (!isSimpleType(getClassOfField(field)) && !ant.pickIdOnly())
					addUnderscoredTitlesToMap(getClassOfField(field));
			}
		}
	}
	

	/**
	 * transforms a Map<String,String> to a JsonBean with equal structure
	 * 
	 * @param map the map to be transformed
	 * @return a JsonBean of the structure {"\<code1\>":"\<translation1\>", "\<code2\>":"\<translation2\>", ...}
	 */
	public static JsonBean mapToBean(Map<String, String> map) {
		if (map.isEmpty())
			return null;
		JsonBean bean = new JsonBean();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			bean.set(entry.getKey(), entry.getValue());
		}
		return bean;
	}
	
	/**
	 * checks whether a Field is assignable from a Collection
	 * 
	 * @param field a Field
	 * @return true/false
	 */
	public static boolean isCollection(Field field) {
		return Collection.class.isAssignableFrom(field.getType());
	}

	/**
	 * returns the generic class defined within a Collection, e.g.
	 * Collection<Class_returned>
	 * 
	 * @param field
	 * @return the generic class
	 */
	public static Class<?> getGenericClass(Field field) {
		if (!isCollection(field))
			throw new RuntimeException("Not a collection: " + field.toString());
		ParameterizedType collectionType = null;
		collectionType = (ParameterizedType) field.getGenericType();
		Type[] genericTypes = collectionType.getActualTypeArguments();
		if (genericTypes.length > 1) {
			// dealing with a map or anything else having > 1 parameterized
			// types
			// throw an exception, this is a very unexpected case
			throw new RuntimeException("Only collections with one generic type expected!");
		}
		if (genericTypes.length == 0) {
			// return null;
			// dealing with a raw type
			// throw an exception, it won't be complete with no parameterization
			throw new RuntimeException("Raw types are not allowed!");
		}
		return ((Class<?>) genericTypes[0]);
	}

	/**
	 * converts the uppercase letters of a string to underscore + lowercase (except for first one)
	 * 
	 * @param input String to be converted
	 * @return converted string
	 */
	public static String underscorify(String input) {
		if (titleToUnderscoreMap.containsKey(input))
			return titleToUnderscoreMap.get(input);
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ' ')
				bld.append('_');
			else
				bld.append(Character.toLowerCase(input.charAt(i)));
		}
		return bld.toString();
	}

	public static String deunderscorify(String input) {
		if (underscoreToTitleMap.containsKey(input)) 
			return underscoreToTitleMap.get(input);
		StringBuilder bld = new StringBuilder();
		boolean upcaseMarker = true;
		for (int i = 0; i < input.length(); i++) {
			if (upcaseMarker) {
				bld.append(Character.toUpperCase(input.charAt(i)));
				upcaseMarker = false;
			} else
				if (input.charAt(i) == '_') {
					upcaseMarker = true;
				} else {
					bld.append(input.charAt(i));
				}
			
		}
		return bld.toString();
	}
	
	public static Method getCustomInterchangeableMethod (Field field) {
		InterchangeableDiscriminator disc = field.getAnnotation(InterchangeableDiscriminator.class);
		if (disc != null) {
			String methodName = disc.method();
			try {
				InterchangeUtils.class.getDeclaredMethod(methodName);
			} catch (Exception exc) {
				
				return null;
			}
		}
		return null;
	}
	
	public static boolean hasCustomInterchangeableMethod(Field field) {
		InterchangeableDiscriminator disc = field.getAnnotation(InterchangeableDiscriminator.class);
		return (disc != null) && disc.method().length() > 0;
	}

	public static Class<? extends FieldsDiscriminator> getDiscriminatorClass(Field field) throws ClassNotFoundException {
		InterchangeableDiscriminator ant = field.getAnnotation(InterchangeableDiscriminator.class);
		if (ant != null && !ant.discriminatorClass().equals("")) {
			String className = ant.discriminatorClass();
			@SuppressWarnings("unchecked")
			Class<? extends FieldsDiscriminator> result = (Class<? extends FieldsDiscriminator>) Class.forName(className);
			return result;
		}
		return null;
		
	}
	
	public static boolean isCompositeField(Field field) {
		return field.getAnnotation(InterchangeableDiscriminator.class) != null;
	}
	
	public static boolean isTranslatbleClass(Class<?> classEntity) {
		return classEntity.getAnnotation(TranslatableClass.class) != null;
	}
	
	public static boolean isTranslatbleField(Field field) {
		return field.getAnnotation(TranslatableField.class) != null;
	}
	
	public static boolean isVersionableTextField(Field field) {
		return field.getAnnotation(VersionableFieldTextEditor.class) != null;
	}
	
	/**
	 * Activity Export as JSON
	 * 
	 * @param projectId is amp_activity_id
	 * @return
	 */
	public static JsonBean getActivity(Long projectId) {
		return getActivity(projectId, null);
	}
	
	/**
	 * Activity Export as JSON 
	 * 
	 * @param projectId is amp_activity_id
	 * @param filter is the JSON with a list of fields
	 * @return
	 */
	public static JsonBean getActivity(Long projectId, JsonBean filter) {
		try {
			AmpActivityVersion activity = ActivityUtil.loadActivity(projectId);
			
			return getActivity(activity, filter);
		} catch (DgException e) {
			LOGGER.error("Coudn't load activity with id: " + projectId + ". " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Activity Export as JSON 
	 * 
	 * @param AmpActivityVersion is the activity
	 * @param filter is the JSON with a list of fields
	 * @return Json Activity
	 */
	public static JsonBean getActivity(AmpActivityVersion activity, JsonBean filter) {
		try {
			ActivityExporter exporter = new ActivityExporter();
		
			return exporter.getActivity(activity, filter);
		} catch (Exception e) {
			LOGGER.error("Error in loading activity. " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get the translation values of the field.
	 * @param field
	 * @param class used for retrieving translation
	 * @param fieldValue 
	 * @param parentObject is the parent that contains the object in order to retrieve translations throu parent object id
	 * @return object with the translated values
	 */
	public static Object getTranslationValues(Field field, Class<?> clazz, Object fieldValue, Long parentObjectId) throws NoSuchMethodException, 
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, EditorException {
		
		TranslationSettings translationSettings = InterchangeUtils.getTranslationSettings();
		
		// check if this is translatable field
		boolean isTranslatable = translationSettings.isTranslatable(field);
		boolean isEditor = InterchangeUtils.isVersionableTextField(field);
		
		// provide map for translatable fields
		if (isTranslatable) {
			String fieldText = (String) fieldValue;
			if (isEditor) {
				Map<String, Object> fieldTrnValues = new HashMap<String, Object>();
				for (String translation : translationSettings.getTrnLocaleCodes()) {
					// AMP-20884: no html tags cleanup so far
					//String translatedText = DgUtil.cleanHtmlTags(DbUtil.getEditorBodyEmptyInclude(SiteUtils.getGlobalSite(), fieldText, translation));
					String translatedText = DbUtil.getEditorBodyEmptyInclude(SiteUtils.getGlobalSite(), fieldText, 
							translation);
					fieldTrnValues.put(translation, getJsonStringValue(translatedText));
				}
				return fieldTrnValues;
			} else {
				return loadTranslationsForField(clazz, field.getName(), fieldText, parentObjectId, translationSettings.getTrnLocaleCodes());
			}
		}
		
		// for reach text editors
		if (isEditor) {
			// AMP-20884: no html tags cleanup so far
			return DbUtil.getEditorBodyEmptyInclude(SiteUtils.getGlobalSite(), (String) fieldValue,
					translationSettings.getDefaultLangCode());
		}
		
		// other translatable options
		if (fieldValue instanceof String) {
			boolean toTranslate = clazz.equals(AmpCategoryValue.class) && field.getName().equals("value");
			
			// now we check if is only a CategoryValue field and the field name is value
			String translatedText = toTranslate ? TranslatorWorker.translateText((String) fieldValue) : (String) fieldValue;
			return getJsonStringValue(translatedText);
		} else if (fieldValue instanceof Date) {
			return InterchangeUtils.formatISO8601Date((Date) fieldValue);
		}
		
		return fieldValue;
	}

	
	public static Field getIdFieldOfEntity(Class entityClass) {
		Class workingClass = entityClass;
		while (workingClass != Object.class) {
			Field[] fields = workingClass.getDeclaredFields();
			for (Field field : fields) {
				Interchangeable ant = field.getAnnotation(Interchangeable.class);
				if (ant != null && ant.id()) 
					return field;
			}
			workingClass = workingClass.getSuperclass();
		}
		return null;

	}
	
	public static Object getObjectById(Class<?> entityClass, Long id) {
		// TODO: cache it
		return PersistenceManager.getSession().get(entityClass.getName(), id);
	}
	
	/**
	 * 
	 * @param value 
	 * @return value if is not blank or null (in order to have for empty strings null values in result JSON) 
	 */
	private static String getJsonStringValue(String value) {
		return StringUtils.isBlank(value) ? null : value;
	}
	
	public static TranslationSettings getTranslationSettings() {
		HttpServletRequest requestAttributes = TLSUtils.getRequest();
		
		return (TranslationSettings) requestAttributes.getAttribute(EPConstants.TRANSLATIONS);
	}
	
	/**
	 * Gets a date formatted in ISO 8601 format. If the date is null, returns null.
	 * 
	 * @param date the date to be formatted
	 * @return String, date in ISO 8601 format
	 */
	public static String formatISO8601Date(Date date) {
		return date == null ? null : getDateFormatter().format(date);
	}
	
	/**
	 * Rebuilds the date from the source
	 * @param date the source
	 * @return Date object
	 */
	public static Date parseISO8601Date(String date) {
		try {
			return date == null ? null : getDateFormatter().parse(date);
		} catch (ParseException e) {
			LOGGER.warn(e.getMessage());
			return null;
		}
	}	
	
	public static String getGetterMethodName(String fieldName) {

		if (fieldName.length() == 1)
			return "get" + Character.toUpperCase(fieldName.charAt(0));
		return "get" + Character.toUpperCase(fieldName.charAt(0)) + 
				((fieldName.length() > 1) ? fieldName.substring(1) : "");
	}	
	
	
	/**
	 * Gets the ID of an enumerable object (used in Possible Values EP)
	 * @param obj
	 * @return ID if it's identifiable, null otherwise
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	//TODO: optimize it. it doesn't have to be n*n for field descent
	public static Long getId(Object obj) throws NoSuchMethodException,	SecurityException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (!Identifiable.class.isAssignableFrom(obj.getClass())) {
//			System.out.println("URURU: " + obj.getClass() + "isn't identifiable!");
			return null;
//			return null;

		}
		Identifiable identifiableObject = (Identifiable) obj;
		Long id = (Long) identifiableObject.getIdentifier(); 
		return id;
		
	}
	

	
	
	/**
	 * Gets the field required value. 
	 * 
	 * @param Field the field to get its required value
	 * @return String with Y|ND|N, where Y (yes) = always required, ND=for draft status=false, 
	 * N (no) = not required. .
	 */
	public static String getRequiredValue(Field field, Interchangeable interchangeable) {
		String requiredValue = ActivityEPConstants.FIELD_NOT_REQUIRED;
		String required = interchangeable.required();
		
		if (required.equals(ActivityEPConstants.REQUIRED_ALWAYS)) {
			requiredValue = ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
		}
		else if (required.equals(ActivityEPConstants.REQUIRED_ND) 
				|| (!required.equals(ActivityEPConstants.REQUIRED_NONE) && FMVisibility.isFmPathEnabled(required))
				|| (hasRequiredValidatorEnabled(field, interchangeable))) {
			requiredValue = ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED;
		}
		return requiredValue;
	}

	public static boolean isSimpleType(Class<?> clazz) {
		return InterchangeableClassMapper.containsSimpleClass(clazz);
	}

	@SuppressWarnings("rawtypes")
	public static Class getClassOfField(Field field) {
		if (!isCollection(field))
			return field.getType();
		else
			return getGenericClass(field);
	}
		
	/**
	 * Imports or Updates an activity
	 * @param json new activity configuration
	 * @param update flags whether this is an import or an update request
     * @param endpointContextPath full API method path where this method has been called
     *
	 * @return latest project overview or an error if invalid configuration is received 
	 */
	public static JsonBean importActivity(JsonBean newJson, boolean update, String endpointContextPath) {
		ActivityImporter importer = new ActivityImporter();
		List<ApiErrorMessage> errors = importer.importOrUpdate(newJson, update, endpointContextPath);
		
		return getImportResult(importer.getNewActivity(), importer.getNewJson(), errors);
	}
	
	public static Map<Integer, String> getTransactionTypeValues() {
		Map<Integer, String> transactionTypeToString = new HashMap<Integer, String>();
		for (Map.Entry<String, Integer> entry : ArConstants.TRANSACTION_TYPE_NAME_TO_ID.entrySet()) {
			transactionTypeToString.put(entry.getValue(), entry.getKey());
		}
		return transactionTypeToString;
	}
	
	protected static JsonBean getImportResult(AmpActivityVersion newActivity, JsonBean newJson, 
			List<ApiErrorMessage> errors) {
		JsonBean result = null;
		if (errors.size() == 0 && newActivity == null) {
			result = ApiError.toError(ApiError.UNKOWN_ERROR); 
		} else if (errors.size() > 0) {
			result = ApiError.toError(errors);
			result.set(ActivityEPConstants.ACTIVITY, newJson);
		} else {
			List<JsonBean> activities = null;
			if (newActivity != null && newActivity.getAmpActivityId() != null) {
				// editable, viewable, since was just created/updated
				activities = Arrays.asList(ProjectList.getActivityInProjectListFormat(newActivity, true, true));
			}
			if (activities == null || activities.size() == 0) {
				result = ApiError.toError(ApiError.UNKOWN_ERROR);
				result.set(ActivityEPConstants.ACTIVITY, newJson);
			} else {
				result = activities.get(0);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean validateFilterActivityFields(JsonBean filterJson, JsonBean result) {
		List<String> filteredItems = new ArrayList<String>();
		String message = "Invalid filter. The usage should be {\"" + ActivityEPConstants.FILTER_FIELDS + "\" : [\"field1\", \"field2\", ..., \"fieldn\"]}";
		JsonBean errorBean = ApiError.toError(message);
		
		if (filterJson != null) {
			try {
				filteredItems = (List<String>) filterJson.get(ActivityEPConstants.FILTER_FIELDS);
				if (filteredItems == null) {
					result.set(ApiError.JSON_ERROR_CODE, errorBean.get(ApiError.JSON_ERROR_CODE));
					
					return false;
				}
			} catch (Exception e) {
				LOGGER.warn("Error in validating fields filter JSON. " + e.getMessage());
				result.set(ApiError.JSON_ERROR_CODE, errorBean.get(ApiError.JSON_ERROR_CODE));
				
				return false;
			}
		}
		
		for (String filteredItem : filteredItems) {
			List<JsonBean> possibleValues = PossibleValuesEnumerator.getPossibleValuesForField(filteredItem, AmpActivityFields.class, null);
			if (possibleValues.size() == 1) {
				Object error = possibleValues.get(0).get(ApiError.JSON_ERROR_CODE);
				if( error != null) {
					result.set(ApiError.JSON_ERROR_CODE, error);
					
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @param containerReq current request
	 * @return true if request is valid to edit an activity
	 */
	public static boolean isEditableActivity(ContainerRequest containerReq) {
		if (!TeamUtil.isUserInWorkspace()) {
			return false;
		}
		JsonBean input = (JsonBean) containerReq.getEntity(JsonBean.class);
		String idStr = input == null ? null : String.valueOf(input.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME));
		Long id = NumberUtils.isNumber(idStr) ? Long.valueOf(idStr) : null;
		// we reuse the same approach as the one done by Project List EP
		return id != null && ProjectList.getEditableActivityIds().contains(id);
	}
	
	/**
	 * @param containerReq
	 * @return true if request is valid to view an activity
	 */
	public static boolean isViewableActivity(ContainerRequest containerReq) {
		if (!TeamUtil.isUserInWorkspace()) {
			return false;
		}
		List<PathSegment> paths = containerReq.getPathSegments();
		Long id = null;
		if (paths != null && paths.size() > 0) {
			PathSegment segment = paths.get(paths.size() - 1);
			if (StringUtils.isNumeric(segment.getPath())) {
				id = Long.valueOf(segment.getPath());
			}
		}
		// we reuse the same approach as the one done by Project List EP
		/* disabling for now due to AMP-20496
		return id != null && ProjectList.getViewableActivityIds(TeamUtil.getCurrentMember()).contains(id);
		*/
		// remove this and turn on previous when AMP-20496 is fixed
		return id != null;
	}
	
	public static Object loadTranslationsForField(Class<?> clazz, String propertyName, String fieldValue, Long id, Set<String> languages) {
		
		Map<String, String> translations = new LinkedHashMap<String, String>(); 
		String defLangCode = TranslationSettings.getDefault().getDefaultLangCode();
		
		for (String l : languages) {
			translations.put(l, null);
		}
		
		if (id == null)
			return translations; 
		
		if (ContentTranslationUtil.multilingualIsEnabled()) {
			List<AmpContentTranslation> trns = ContentTranslationUtil.loadFieldTranslations(clazz.getName(), id, propertyName);
			for(AmpContentTranslation trn:trns){
				if (languages.contains(trn.getLocale())) {
					translations.put(trn.getLocale(), trn.getTranslation());
				}
			}
		} else {
			return fieldValue;
		}
		
		if (translations.get(defLangCode) == null){
				translations.put(defLangCode, fieldValue);
		}
		
		return translations;
	}
	
	protected static SimpleDateFormat getDateFormatter() {
		if (DATE_FORMATTER.get() == null) {
			DATE_FORMATTER.set(new SimpleDateFormat(ISO8601_DATE_FORMAT));
		}
		return DATE_FORMATTER.get();
	}
	
	/**
	 * @param apb AmpAnnualProject budget having amount, currency and date
	 * @parm toCurrCode target currency code to which apb amount would be calculated
	 * @return double the amount in toCurrCode
	 */
	public static Double doPPCCalculations(AmpAnnualProjectBudget apb, String toCurrCode) {
		DecimalWraper calculatedAmount = new DecimalWraper();
		
		if (apb.getAmpCurrencyId() != null && apb.getYear() != null && apb.getAmount() != null && toCurrCode != null) {
			String frmCurrCode = apb.getAmpCurrencyId().getCurrencyCode();
			java.sql.Date dt = new java.sql.Date(apb.getYear().getTime());
			Double amount = apb.getAmount();
		
			double frmExRt = Util.getExchange(frmCurrCode, dt);
			double toExRt = frmCurrCode.equalsIgnoreCase(toCurrCode) ? frmExRt : Util.getExchange(toCurrCode, dt);
	
			calculatedAmount = CurrencyWorker.convertWrapper(amount, frmExRt, toExRt, dt);
		} else {
			LOGGER.error("Some info is missed in PPC Calculations");
		} 
		
		return calculatedAmount.doubleValue();
	}

    public static Long extractActivityId(JsonBean activityBean) {
        if (activityBean != null
                && (activityBean.get("error") == null || ((Map)activityBean.get("error")).isEmpty())) {
            Object idValue = activityBean.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME);
            return idValue == null ? null : Long.decode(idValue.toString());
        }
        return null;
    }


	
	
	/**
	 * Determine if this is an AmpActivityVersion field reference
	 * @param field
	 * @return
	 */
	public static boolean isAmpActivityVersion(Class<?> clazz) {
		return clazz.isAssignableFrom(AmpActivityVersion.class);
	}
	
	public static boolean hasUniqueValidatorEnabled(Field field, Interchangeable interchangeable) {
		return hasValidatorEnabled(field, interchangeable, ActivityEPConstants.UNIQUE_VALIDATOR_NAME);
	}
	
	public static boolean hasMaxSizeValidatorEnabled(Field field, Interchangeable interchangeable) {
		return hasValidatorEnabled(field, interchangeable, ActivityEPConstants.MAX_SIZE_VALIDATOR_NAME);
	}
	
	public static boolean hasRequiredValidatorEnabled(Field field, Interchangeable interchangeable) {
		return hasValidatorEnabled(field, interchangeable, ActivityEPConstants.MIN_SIZE_VALIDATOR_NAME);
	}
	
	public static boolean hasPercentageValidatorEnabled(Field field, Interchangeable interchangeable) {
		return hasValidatorEnabled(field, interchangeable, ActivityEPConstants.PERCENTAGE_VALIDATOR_NAME);
	}
	
	private static boolean hasValidatorEnabled(Field field, Interchangeable interchangeable, String validatorName) {
		boolean isEnabled = false;
		Validators validators = interchangeable.validators();
		
		if (validators != null) {
			String validatorFmPath = "";
			
			if (ActivityEPConstants.UNIQUE_VALIDATOR_NAME.equals(validatorName)) {
				validatorFmPath = validators.unique();
			} else if (ActivityEPConstants.MAX_SIZE_VALIDATOR_NAME.equals(validatorName)) {
				validatorFmPath = validators.maxSize();
			} else if (ActivityEPConstants.MIN_SIZE_VALIDATOR_NAME.equals(validatorName)) {
				validatorFmPath = validators.minSize();
			} else if (ActivityEPConstants.PERCENTAGE_VALIDATOR_NAME.equals(validatorName)) {
				validatorFmPath = validators.percentage();
			}
			
			if (StringUtils.isNotBlank(validatorFmPath)) {
				isEnabled = FMVisibility.isFmPathEnabled(validatorFmPath);
			}
		}
		
		return isEnabled;
	}
	
	/**
	 * This is a special adjusted Session with FlusMode = Commit so that Hiberante doesn't try to commit intermediate 
	 * changes while we still query some information
	 * TODO: AMP-20869: we'll need to give it a more thought during refactoring if either rewrite related queries as JDBC queries
	 * or investigate more for 
	 * 
	 * @return Session with no AutoFlush mode
	 */
	public static Session getSessionWithPendingChanges() {
		Session session = PersistenceManager.getSession();
		session.setFlushMode(FlushMode.COMMIT);
		return session;
	}
} 

/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * Class used for exporting an activity as a JSON
 * 
 * @author Viorel Chihai
 */
public class ActivityExporter {
	
	private static final Logger logger = Logger.getLogger(ActivityImporter.class);
	
	private List<String> filteredFields = new ArrayList<String>();
	
	/**
	 * Activity Export as JSON 
	 * 
	 * @param activity actual activity to export
	 * @param filter is the JSON with a list of fields
	 * @param translations 
	 * @param language 
	 * @return
	 * @throws DgException 
	 */
	public JsonBean getActivity(AmpActivityVersion activity, JsonBean filter) throws DgException {
		JsonBean activityJson = new JsonBean();
			
		activityJson.set("amp_activity_id", activity.getAmpActivityId());
		activityJson.set("amp_id", activity.getAmpId());
		
		if (filter != null) {
			this.filteredFields = (List<String>) filter.get(ActivityEPConstants.FILTER_FIELDS);
		}
		
		Field[] fields = activity.getClass().getSuperclass().getDeclaredFields();

		for (Field field : fields) {
			try {
				readFieldValue(field, activity, activity, activityJson, null);
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchMethodException | SecurityException
					| InvocationTargetException e) {
				logger.error("Coudn't read activity fields with id: " + activity.getAmpActivityId() + ". " 	+ e.getMessage());
				throw new RuntimeException(e);
			}
		}
		
		return activityJson;
	}
	
	/**
	 * 
	 * @param field
	 * @param fieldInstance
	 * @param resultJson
	 * @param filteredFields
	 * @param fieldPath
	 * @return
	 */
	private void readFieldValue(Field field, Object fieldInstance, Object parentObject, JsonBean resultJson, String fieldPath) throws IllegalArgumentException, 
	IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
		
		if (interchangeable != null && FMVisibility.isVisible(field)) {
			field.setAccessible(true);
			String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());
			
			String filteredFieldPath = fieldPath == null ? fieldTitle : fieldPath + "~" + fieldTitle;
			Object object = field.get(fieldInstance);
			
			if (!interchangeable.pickIdOnly()) {
				// check if the member is a collection
				
				if (InterchangeUtils.isCompositeField(field)) {
					generateCompositeCollection(field, object, resultJson, fieldPath);
				} if (isFiltered(filteredFieldPath)) {
					if (InterchangeUtils.isCollection(field)) {
						Collection<Object> collectionItems = (Collection<Object>) object;
						// if the collection is not empty, it will be parsed and a JSON with member details will be generated
						List<JsonBean> collectionJson = new ArrayList<JsonBean>();
						if (collectionItems != null) {
							// iterate over the objects of the collection
							for (Object item : collectionItems) {
								collectionJson.add(getObjectJson(item, filteredFieldPath));
							}
						}
						// put the array with object values in the result JSON
						resultJson.set(fieldTitle, collectionJson);
					} else {

						if (InterchangeableClassMapper.containsSupportedClass(field.getType()) || object == null) {
							resultJson.set(fieldTitle, InterchangeUtils.getTranslationValues(field, field.getDeclaringClass(), object, InterchangeUtils.getId(parentObject)));
						} else {
							if (interchangeable.pickIdOnly()) {
								resultJson.set(fieldTitle, InterchangeUtils.getId(object));
							} else {
								resultJson.set(fieldTitle, getObjectJson(object, filteredFieldPath));
							}
						}
					}
				}
			}
		}		
	}
	
	/**
	 * 
	 * @param item
	 * @param filteredFields
	 * @param fieldPath
	 * @return itemJson
	 */
	private JsonBean getObjectJson(Object item, String fieldPath) throws IllegalArgumentException, IllegalAccessException, 
	NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		Field[] itemFields = item.getClass().getDeclaredFields();
		JsonBean itemJson = new JsonBean();
		
		// iterate the fields of the object and generate the JSON
		for (Field itemField : itemFields) {
			readFieldValue(itemField, item, item, itemJson, fieldPath);	
		}
		
		return itemJson;
	}
	
	/**
	 * Generate the composite collection. E.g: we have a list of sectors, 
	 * in JSON the list should be written by classification 
	 * (primary programs, secondary programs, etc.)
	 * @param field
	 * @param fieldInstance
	 * @param resultJson
	 * @param filteredFields
	 * @param fieldPath
	 */
	private void generateCompositeCollection(Field field, Object object, JsonBean resultJson, String fieldPath) throws IllegalArgumentException, 
	IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
		Interchangeable[] settings = discriminator.settings();
		
		Map<String, List<JsonBean>> compositeMap = new HashMap<String, List<JsonBean>>();
		Map<String, String> filteredFieldsMap = new HashMap<String, String>();
		
		// create the map containing the correlation between the discriminatorOption and the JSON generated objects
		for (Interchangeable setting : settings) {
			compositeMap.put(setting.discriminatorOption(), new ArrayList<JsonBean>());
			String fieldTitle = InterchangeUtils.underscorify(setting.fieldTitle());
			String filteredFieldPath = fieldPath == null ? fieldTitle : fieldPath + "~" + fieldTitle;
			
			filteredFieldsMap.put(setting.discriminatorOption(), filteredFieldPath);
		}
		
		if (InterchangeUtils.isCollection(field) && object != null) {
			Collection<Object> compositeCollection = (Collection <Object>) object;
			if (compositeCollection.size() > 0) {
				for (Object obj : compositeCollection) {
					if (obj instanceof AmpActivitySector) {
						AmpActivitySector sector = (AmpActivitySector) obj;
						String filteredFieldPath = filteredFieldsMap.get(sector.getClassificationConfig().getName());
						compositeMap.get(sector.getClassificationConfig().getName()).add(getObjectJson(sector, filteredFieldPath));
					} else if (obj instanceof AmpActivityProgram) {
						AmpActivityProgram program = (AmpActivityProgram) obj;
						String filteredFieldPath = filteredFieldsMap.get(program.getProgramSetting().getName());
						compositeMap.get(program.getProgramSetting().getName()).add(getObjectJson(program, filteredFieldPath));
					}
				}
			}
		}
		
		// put in the result JSON the generated structure
		for (Interchangeable setting : settings) {
			String fieldTitle = InterchangeUtils.underscorify(setting.fieldTitle());
			if (isFiltered(fieldTitle)) {
				resultJson.set(InterchangeUtils.underscorify(setting.fieldTitle()), compositeMap.get(setting.discriminatorOption()));
			}
		}
	}
	
	/**
	 * 
	 * @param filteredFieldPath
	 * @param filteredFields
	 * @return boolean, if the field should be exported in the result Json 
	 */
	private boolean isFiltered(String filteredFieldPath) {
		if (filteredFields.isEmpty()) 
			return true;
			
		for (String s : filteredFields) {
			if (s.startsWith(filteredFieldPath) || filteredFieldPath.startsWith(s)) 
				return true;
		}
		
		return false;
	}
}

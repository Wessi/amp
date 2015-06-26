/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * Activity Endpoint related constants
 * @author Nadejda Mandrescu
 */
public class ActivityEPConstants {
	// JSON fields
	public static final String ACTIVITY = "activity";
	public static final String FIELD_NAME = "field_name";
	public static final String FIELD_TYPE = "field_type";
	public static final String FIELD_LABEL = "field_label";
	public static final String IMPORTABLE = "importable";
	public static final String MULTIPLE_VALUES = "multiple_values";
	public static final String CHILDREN = "children";
	public static final String UNIQUE = "unique";
	public static final String REQUIRED = "required";	
	public static final String ID_ONLY = "id_only";
	public static final String TRANSLATABLE = "translatable";
	public static final String INVALID = "invalid";
	public static final String INPUT = "input";
	
	
	// fields constants
	public static final String AMP_ACTIVITY_ID_FIELD_NAME = InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID);
	
	// field types
	public static final String FIELD_TYPE_LIST = "list";
	
}

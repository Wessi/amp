/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.util.ActivityVersionUtil;

/**
 * Validates amp_activity_id and amp_id to be valid
 * 
 * @author Nadejda Mandrescu
 */
public class AmpActivityIdValidator extends InputValidator {

	public AmpActivityIdValidator() {
		this.continueOnSuccess = false;
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_INVALID_VALUE;
	}

	@Override
	public boolean isValid(ActivityImporter importer, JsonBean newFieldParent, JsonBean oldFieldParent,
			JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		String ampId = (String) newFieldParent.get(ActivityFieldsConstants.AMP_ID);
		String ampActivityId = (String) newFieldParent.get(ActivityFieldsConstants.AMP_ACTIVITY_ID);
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		if (InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID).equals(fieldName)) {
			if (importer.isUpdate()) {
				Long latestAmpActivityId = ActivityVersionUtil.getLastVersionForVersion(Long.valueOf(ampActivityId));
				if (importer.getOldActivity() == null 
						|| !latestAmpActivityId.equals(importer.getOldActivity().getAmpActivityId())) {
					isValid = false;
				}
			} else {
				if ((ampActivityId != null)) { //must not be configurable even to 0 (updated design) && Long.valueOf(ampActivityId).longValue() != 0l)) {
					isValid = false;
				}
			}
		} else if (InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID).equals(fieldName)) {
			if (importer.isUpdate()) {
				if (importer.getOldActivity() == null || !importer.getOldActivity().getAmpId().equals(ampId)) {
					isValid = false;
				}
			} else {
				if (ampId != null) {
					isValid = false;
				}
			}
		}
		return isValid;
	}
}

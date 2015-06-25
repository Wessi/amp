/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * Validates that project title field value is unique across AMP
 * 
 * @author Nadejda Mandrescu
 */
public class ActivityTitleValidator extends InputValidator {

	public ActivityTitleValidator() {
		this.continueOnSuccess = false;
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.UNIQUE_ACTIVITY_TITLE;
	}

	@Override 
	public  boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldParent, JsonBean oldFieldParent, 
			JsonBean fieldDescription, String fieldPath, boolean update) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		//this validator only validates project title
		if (InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_TITLE).equals(fieldName)) {
			HttpServletRequest request = TLSUtils.getRequest();
			String title = newFieldParent.getString(fieldName);
			String langCode = null;
			if (ContentTranslationUtil.multilingualIsEnabled()) {
				langCode = TLSUtils.getEffectiveLangCode ();
			}
			List<AmpActivity> list = LuceneUtil.findActivitiesMoreLikeThis(request.getServletContext().getRealPath("/")
					+ LuceneUtil.ACTVITY_INDEX_DIRECTORY, title, langCode, 2);
			isValid = !isTitleExistent(oldActivity, list,update);
		}

		return isValid;
	}

	/**
	 * Checks if the Activity title already exists on the system.
	 * 
	 * @param oldActivity, null if it is an insert, the activity already on the system on updates.
	 * @param list the List <AmpActivity> with matching titles
	 * @param update, whether this is an update operation or an insert
	 * @return if there is already an activity with a matching title.
	 */
	private boolean isTitleExistent(AmpActivityVersion oldActivity, List<AmpActivity> list,boolean update) {
		boolean isExistent = false;
		if (!list.isEmpty()) {
			for (AmpActivity activity : list)
				if (!update  || !oldActivity.getName().equals(activity.getName())) {
					isExistent = true;
				}

		}
		return isExistent;
	}

}

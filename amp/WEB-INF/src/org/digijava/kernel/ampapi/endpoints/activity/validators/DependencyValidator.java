package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeDependencyResolver;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

import clover.com.google.common.base.Joiner;
import bsh.StringUtil;

public class DependencyValidator extends InputValidator {

	@Override
	public boolean isValid(ActivityImporter importer,
			Map<String, Object> newFieldParent,
			Map<String, Object> oldFieldParent, JsonBean fieldDescription,
			String fieldPath) {
		Object value = newFieldParent.get(fieldDescription.get(ActivityEPConstants.FIELD_NAME));
		if (value == null)
			return true;
		String[] deps =  (String[]) fieldDescription.get(ActivityEPConstants.DEPENDENCIES);
		if (deps != null)
		{
			boolean result = true;
			for (String dep : deps) {
				if (!InterchangeDependencyResolver.checkDependency(value, importer.getNewJson(), dep)) {
					result = false;
					errors.add(dep);
				}
			}
			return result;
		}
		return true;
	}

	private ArrayList<String> errors = new ArrayList<String>();;
	@Override
	public ApiErrorMessage getErrorMessage() {
		
		return new ApiErrorMessage(ActivityErrors.DEPENDENCY_NOT_MET, Joiner.on("; ").join(errors) );
	}

}

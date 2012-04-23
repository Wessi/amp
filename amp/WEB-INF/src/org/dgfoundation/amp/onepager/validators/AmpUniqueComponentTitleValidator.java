/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.digijava.module.aim.util.ComponentsUtil;

/**
 * @author aartimon@developmentgateway.org
 * @since 19 Mar 2012
 *
 */

public class AmpUniqueComponentTitleValidator implements IValidator<String> {

	private PropertyModel<Long> idModel;

	/**
	 * @param propertyModel 
	 * 
	 */
	public AmpUniqueComponentTitleValidator(PropertyModel<Long> propertyModel) {
		this.idModel = propertyModel;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<String> validatable) {
		if(validatable.getValue().trim().length()==0) 
			return;
		
		boolean exists = ComponentsUtil.checkComponentNameExists(validatable.getValue(), idModel.getObject());
		if(exists){
			ValidationError error = new ValidationError();
			error.addMessageKey("AmpUniqueComponentTitleValidator");
			validatable.error(error);
		}
	}

}

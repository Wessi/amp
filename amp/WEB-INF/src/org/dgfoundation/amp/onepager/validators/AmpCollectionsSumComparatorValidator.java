package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * @author Armen
 *
 */
public class AmpCollectionsSumComparatorValidator extends AmpSemanticValidator<Double> {

	String messageKey;
	
	public AmpCollectionsSumComparatorValidator(String messageKey) {
		super();
		this.messageKey=messageKey;
	}

	@Override
	public void semanticValidate(IValidatable<Double> validatable) {
		Double value = validatable.getValue();
		
		if(value.longValue()<0)
		{
			ValidationError error = new ValidationError();
			error.addMessageKey(messageKey);
			validatable.error(error);
		}
		
	}

}

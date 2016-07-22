package org.dgfoundation.amp.nireports.schema;

import java.util.function.Predicate;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;

/**
 * a trivial measure defined as a transaction 
 * @author Dolghier Constantin
 *
 */
public class NiTransactionMeasure extends NiPredicateTransactionMeasure {
	
	public final Predicate<CategAmountCell> criterion;
	
	public NiTransactionMeasure(String measureName, Predicate<CategAmountCell> criterion, String description) {
		this(measureName, criterion, TrivialMeasureBehaviour.getInstance(), description, false);
	}
	
	public NiTransactionMeasure(String measureName, Predicate<CategAmountCell> criterion, Behaviour<?> behaviour, String description, boolean ignoreFilters) {
		super(measureName, behaviour, description, ignoreFilters);
		this.criterion = criterion;
	}

	@Override
	public CategAmountCell processCell(CategAmountCell src) {
		return criterion.test(src) ? src : null;
	}
}

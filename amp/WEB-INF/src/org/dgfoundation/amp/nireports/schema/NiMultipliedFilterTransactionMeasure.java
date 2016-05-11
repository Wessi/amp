package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.function.Function;
import org.dgfoundation.amp.nireports.CategAmountCell;

/**
 * a measure defined as a filtered transaction multiplied by a number
 * @author Dolghier Constantin
 *
 */
public class NiMultipliedFilterTransactionMeasure extends NiPredicateTransactionMeasure {
	
	public final Function<CategAmountCell, BigDecimal> multiplierCalculator;
	
	public NiMultipliedFilterTransactionMeasure(String measureName, Function<CategAmountCell, BigDecimal> multiplierCalculator, String description) {
		this(measureName, multiplierCalculator, TrivialMeasureBehaviour.getInstance(), description);
	}
	
	public NiMultipliedFilterTransactionMeasure(String measureName, Function<CategAmountCell, BigDecimal> multiplierCalculator, Behaviour<?> behaviour, String description) {
		super(measureName, behaviour, description);
		this.multiplierCalculator = multiplierCalculator;
	}

	public static NiMultipliedFilterTransactionMeasure filteredOnMeasure(String measureName, NiTransactionMeasure srcMeasure, Function<CategAmountCell, BigDecimal> multiplier, String description) {
		return new NiMultipliedFilterTransactionMeasure(measureName, cell -> srcMeasure.criterion.test(cell) ? multiplier.apply(cell) : null, description);
	}
		
	@Override
	public CategAmountCell processCell(CategAmountCell src) {
		BigDecimal multiplier = multiplierCalculator.apply(src);
		if (multiplier != null)
			return src.multiply(multiplier);
		return null;
	}	
}

package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;

/**
 * a trivial measure defined as a transaction 
 * @author Dolghier Constantin
 *
 */
public class NiLinearCombinationTransactionMeasure extends NiPredicateTransactionMeasure {
	
	public final Map<NiTransactionMeasure, BigDecimal> terms;
	protected final NiTransactionMeasure[] measures;
	protected final BigDecimal[] prods;
	
	/**
	 * Specifies whether coordinates have to be stripped from cells before being calculated.
	 * Stripped coords imply insubordination to hierarchies. 
	 */
	protected final boolean stripCoords;
	
	public NiLinearCombinationTransactionMeasure(String measureName, Map<NiTransactionMeasure, BigDecimal> terms,  
			Behaviour<?> behaviour, boolean ignoreFilters, boolean stripCoords, String description) {
		super(measureName,  behaviour, description, ignoreFilters);
		NiUtils.failIf(terms.isEmpty(), () -> String.format("while defining measure %s: you supplied an empty terms list", measureName));
		this.terms = Collections.unmodifiableMap(new LinkedHashMap<>(terms));
		this.measures = terms.keySet().toArray(new NiTransactionMeasure[0]);
		this.prods = terms.values().toArray(new BigDecimal[0]);
		this.stripCoords = stripCoords;
	}
	
	public NiLinearCombinationTransactionMeasure(String measureName, Map<NiTransactionMeasure, BigDecimal> terms, 
			boolean ignoreFilters, boolean stripCoords, String description) {
		this(measureName, terms, TrivialMeasureBehaviour.getInstance(), ignoreFilters, stripCoords, description);
	}
	
	@Override
	public CategAmountCell processCell(CategAmountCell src) {
		for(int i = 0; i < measures.length; i++) {
			if (measures[i].criterion.test(src)) {
				CategAmountCell c = multiply(src, prods[i]);
				if (c != null)
					return stripCoords ? c.withStrippedCoords() : c;
				break;
			}
		}
		return null;
	}
	
	protected CategAmountCell multiply(CategAmountCell cell, BigDecimal multiplier) {
		if (multiplier == BigDecimal.ONE)
			return cell;
		return cell.multiply(multiplier);
	}
}

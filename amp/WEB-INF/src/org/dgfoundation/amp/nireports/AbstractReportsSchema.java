package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiCombinationContextTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.NiFormulaicAverageMeasure;
import org.dgfoundation.amp.nireports.schema.NiFormulaicMeasure;
import org.dgfoundation.amp.nireports.schema.NiLinearCombinationTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiTransactionContextMeasure;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class AbstractReportsSchema implements NiReportsSchema {
	protected Map<String, NiReportColumn<?>> columns = new HashMap<>();
	protected Map<String, NiReportMeasure<?>> measures = new HashMap<>();
	
	@Override
	public Map<String, NiReportColumn<? extends Cell>> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	@Override
	public Map<String, NiReportMeasure<?>> getMeasures() {
		return Collections.unmodifiableMap(measures);
	}
		
	public AbstractReportsSchema addColumn(NiReportColumn<?> col) {
		failIf(columns.containsKey(col.name), "double definition of column with name " + col.name);
		failIf(col.getBehaviour() == null, "no behaviour specified for column with name " + col.name);
		columns.put(col.name, col);
		return this;
	}
	
	public AbstractReportsSchema addMeasure(NiReportMeasure<?> meas) {
		failIf(measures.containsKey(meas.name), "double definition of measure with name " + meas.name);
		failIf(meas.getBehaviour() == null, "no behaviour specified for measure with name " + meas.name);
		measures.put(meas.name, meas);
		return this;
	}
	
	/**
	 * accepts an array of (measureName, Number)
	 * @param def
	 * @return
	 */
	public AbstractReportsSchema addDerivedLinearFilterMeasure(String compMeasureName, String description, Behaviour<?> behaviour, Object...def) {
		failIf(def.length % 2 != 0, "you should supply an even number of arguments");
		@SuppressWarnings("rawtypes")
		Map<NiTransactionContextMeasure, BigDecimal> defMap = parseContextFilterMap(String.format("while defining measure %s", compMeasureName), def);
		return addMeasure(new NiCombinationContextTransactionMeasure(compMeasureName, defMap, behaviour, description));
	}
	
	/**
	 * accepts an array of (measureName, Number)
	 * @param def
	 * @return
	 */
	public AbstractReportsSchema addLinearFilterMeasure(String compMeasureName, String description, Behaviour<?> behaviour, 
			boolean ignoreFilters, boolean stripCoords, Object...def) {
		failIf(def.length % 2 != 0, "you should supply an even number of arguments");
		Map<NiTransactionMeasure, BigDecimal> defMap = parseMap(String.format("while defining measure %s", compMeasureName), def);
		return addMeasure(new NiLinearCombinationTransactionMeasure(compMeasureName, defMap, behaviour, ignoreFilters, stripCoords, description));
	}
	
	/**
	 * adds an instance of {@link NiFormulaicMeasure} to the schema
	 * @param compMeasureName
	 * @param description
	 * @param formula
	 * @return
	 */
	public AbstractReportsSchema addFormulaComputedMeasure(String compMeasureName, String description, NiFormula formula, boolean average) {
		Map<String, NiReportMeasure<CategAmountCell>> depMeas = new HashMap<>();
		for(String measName:formula.getDependencies()) {
			NiReportMeasure<CategAmountCell> meas = (NiReportMeasure) measures.get(measName);
			failIf(meas == null, () -> String.format("measure <%s> defined as dependency of measure <%s> does not exist", measName, compMeasureName));
			depMeas.put(measName, meas);
		}
		NiReportMeasure<CategAmountCell> res;
		if (average)
			res = new NiFormulaicAverageMeasure(compMeasureName, description, depMeas, formula, true);
		else
			res = new NiFormulaicMeasure(compMeasureName, description, depMeas, formula);
		return addMeasure(res);
		//return addMeasure(meas)
	}
	
	@SuppressWarnings("rawtypes")
	public Map<NiTransactionContextMeasure, BigDecimal> parseContextFilterMap(String errPrefix, Object...def) {
		Map<NiTransactionContextMeasure, BigDecimal> res = new HashMap<>();
		for(int i = 0; i < def.length / 2; i++) {
			String measureName = (String) def[i * 2];
			Number factor = (Number) def[i * 2 + 1];
			
			NiReportMeasure meas = (NiTransactionContextMeasure) getMeasures().get(measureName);
			failIf(meas == null, () -> String.format("%s: measure %s not found in the schema", errPrefix, measureName));
			res.put((NiTransactionContextMeasure) meas, toBigDecimal(factor));
		}
		return res;
	}
	
	public Map<NiTransactionMeasure, BigDecimal> parseMap(String errPrefix, Object...def) {
		Map<NiTransactionMeasure, BigDecimal> res = new HashMap<>();
		for(int i = 0; i < def.length / 2; i++) {
			String measureName = (String) def[i * 2];
			Number factor = (Number) def[i * 2 + 1];
			
			NiTransactionMeasure meas = (NiTransactionMeasure) getMeasures().get(measureName);
			failIf(meas == null, () -> String.format("%s: measure %s not found in the schema", errPrefix, measureName));
			res.put(meas, toBigDecimal(factor));
		}
		return res;
	}
	
	public TrivialMeasureBehaviour byMeasureDividingBehaviour(TimeRange tr, String measureName) {
		return new TrivialMeasureBehaviour(tr, TrivialMeasureBehaviour.buildMeasureTotalDivider(measureName));
	}
	
	public static Map<String, Boolean> singletonMap(String k, Boolean v) {
		Map<String, Boolean> res = new HashMap<>();
		res.put(k, v);
		return res;
	}
	
	/**
	 * converts a numeric value into BigDecimal
	 * @param n
	 * @return
	 */
	public static BigDecimal toBigDecimal(Number n) {
		if (n instanceof BigDecimal)
			return ((BigDecimal) n);
		
		if (n instanceof Integer || n instanceof Long)
			return BigDecimal.valueOf(n.longValue());
		
		if (n instanceof Double || n instanceof Float)
			return BigDecimal.valueOf(n.doubleValue());
		
		throw new RuntimeException(String.format("cannot convert instances of class %s to BigDecimal", n.getClass().getName()));
	}
}

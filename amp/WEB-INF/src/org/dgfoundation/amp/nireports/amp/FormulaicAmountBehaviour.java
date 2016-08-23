package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.behaviours.AbstractComputedBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiFormulaicMeasure;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * the behaviour of an entity which has value = formula(tokens). The trail cells get their tokem from V-reduction
 * @author Dolghier Constantin
 *
 */
public class FormulaicAmountBehaviour extends AbstractComputedBehaviour<NiFormulaicAmountCell> {
	
	/**
	 * the functions (one for each dependency-variable) used to reduce multiple values to a single one
	 */
	final Map<String, Function<List<BigDecimal>, BigDecimal>> reductors;
	
	/**
	 * the expression tree which drives the behaviour
	 */
	final NiFormula formula;
	
	/**
	 * the callback which is used to build "undefined" cells (ones where {@link #formula} returns one of the undefined values)
	 */
	final BiFunction<BigDecimal, Map<String, BigDecimal>, NiFormulaicAmountCell> undefinedBuilder;
	
	public FormulaicAmountBehaviour(TimeRange timeRange, 
			Map<String, Function<List<BigDecimal>, BigDecimal>> reductors,
			BiFunction<BigDecimal, Map<String, BigDecimal>, NiFormulaicAmountCell> undefinedBuilder,
			NiFormula formula) {
		super(timeRange);
		this.reductors = reductors;
		this.formula = formula;
		this.undefinedBuilder = undefinedBuilder == null ? 
				(val, vals) -> new NiFormulaicAmountCell(vals, null, NiPrecisionSetting.IDENTITY_PRECISION_SETTING) : 
					undefinedBuilder;
	}
	
	/**
	 * combines multiple elementary cells into one {@link NiFormulaicAmountCell}. It has 3 functions to fulfill: 
	 * <ul>
	 * <li>group NiCell by the tag of the enclosed cell and separate them into lists</li>
	 * <li>call the respective reductor on each of the lists, thus forming the vars-values</li>
	 * <li>evaluate the expression tree on the given vars-values and return a cell with the built value</li>
	 * </ul>
	 */
	@Override
	public NiFormulaicAmountCell doHorizontalReduce(List<NiCell> cells) {
		Map<String, BigDecimal> vals = AmpCollections.remap(
				cells.stream().collect(Collectors.groupingBy(this::extractCellTag)), 
				(entity, list) -> reductors.get(entity).apply(AmpCollections.relist(list, NiCell::getAmount)), 
				null);

		NiPrecisionSetting precision = cells.isEmpty() ? NiPrecisionSetting.IDENTITY_PRECISION_SETTING : ((NumberedCell) cells.get(0).getCell()).getPrecision();
		return buildCell(vals, precision);
	}

	protected NiFormulaicAmountCell buildCell(Map<String, BigDecimal> vals, NiPrecisionSetting precision) {
		BigDecimal numericValue = formula.evaluate(vals);
		if (NiFormulaicAmountCell.isDefined(numericValue))
			return new NiFormulaicAmountCell(vals, formula.evaluate(vals), precision);
		else
			return undefinedBuilder.apply(numericValue, vals);
	}
	
	protected String extractCellTag(NiCell cell) {
		CategAmountCell cac = (CategAmountCell) cell.getCell();
		return cac.metaInfo.getMetaInfo(NiFormulaicMeasure.METAINFO_KEY_UNDERLYING_MEASURE).v.toString();
	}
	
	/**
	 * does vertical reduction based on a formula. This is a 3-step process: 
	 * <ul>
	 * <li>collect the respective values from cells, grouping them by tag</li>
	 * <li>call the respective reductor on each of the lists, thus forming the vars-values</li>
	 * <li>evaluate the expression tree on the given vars-values and return a cell with the built value</li>
	 * </ul>
	 * You might notice that steps 2-3 are identical to the ones of {@link #doHorizontalReduce(List)}
	 */
	@Override
	public NiFormulaicAmountCell doVerticalReduce(Collection<NiFormulaicAmountCell> cells) {
		Map<String, List<BigDecimal>> valsA = new HashMap<>();
		for(NiFormulaicAmountCell cell:cells)
			if (cell != null) {
				cell.values.forEach((cat, val) -> {
					valsA.computeIfAbsent(cat, z -> new ArrayList<>()).add(val);
				});
			}
		Map<String, BigDecimal> vals = AmpCollections.remap(valsA, (entity, list) -> reductors.get(entity).apply(list), null);
		NiFormulaicAmountCell res = buildCell(vals, NiPrecisionSetting.IDENTITY_PRECISION_SETTING);
		return res == null ? getZeroCell() : res;
	}

	@Override
	public NiFormulaicAmountCell getZeroCell() {
		return NiFormulaicAmountCell.FORMULAIC_ZERO;
	}
	
	/**
	 * a reductor which sums
	 * @param l
	 * @return
	 */
	public static BigDecimal REDUCE_SUM(List<BigDecimal> l) {
		if (l.isEmpty()) return BigDecimal.ZERO;
		
		if (l.size() == 1) return l.get(0);
		
		BigDecimal res = BigDecimal.ZERO;
		for(BigDecimal term:l) {
			res = res.add(term);
		}
		return res;
	}
	
	/**
	 * a reductor which multiplies
	 * @param l
	 * @return
	 */
	public static BigDecimal REDUCE_MUL(List<BigDecimal> l) {
		if (l.isEmpty()) return BigDecimal.ONE;
		
		if (l.size() == 1) return l.get(0);
		
		BigDecimal res = BigDecimal.ONE;
		for(BigDecimal term:l) {
			res = res.multiply(term);
		}
		return res;
	}
}

package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.workers.ComputedAmountColWorker;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.Values;
import org.digijava.module.aim.helper.FormatHelper;

public class ComputedAmountCell extends CategAmountCell {

	Values values = null;

	private static String COMPUTED_VALUE = "COMPUTED_VALUE";

	// if is a trail cell the value will be set by the Column
	public void setComputedVaule(BigDecimal value) {
		getValues().put(COMPUTED_VALUE, value);
	}

	public Class getWorker() {
		return ComputedAmountColWorker.class;
	}

	public double getAmount() {
		BigDecimal ret = new BigDecimal(0);
		if (id != null)
			return (convert() * (getPercentage() / 100));

		// get values from mergedCells

		MathExpression expression = null;
		
		if (getValues().containsKey(COMPUTED_VALUE)) {
			BigDecimal val = getValues().get(COMPUTED_VALUE);
			if (val != null) {
				return val.doubleValue();
			} else {
				return 0d;
			}

		} else {
			// if the cell should return the value, do it only if the row
			// expression is set

			if (this.getColumn().getExpression() != null) {
				expression = MathExpressionRepository.get(this.getColumn().getExpression());
			} else if (this.getColumn().getWorker().getRelatedColumn().getTokenExpression() != null) {
				expression = MathExpressionRepository.get(this.getColumn().getWorker().getRelatedColumn().getTokenExpression());
			}

			String totalExpression = null;
			if (this.getColumn().getWorker().getRelatedColumn().getTotalExpression() != null) {
				totalExpression = this.getColumn().getWorker().getRelatedColumn().getTotalExpression();
			}
			Boolean showRowCalculation = false;
			if (this.getColumn().getWorker().getRelatedColumn().isShowRowCalculations() != null) {
				showRowCalculation = this.getColumn().getWorker().getRelatedColumn().isShowRowCalculations();
			}

			// if rowsExpression is present so return the expression result
			// value
			if ((expression != null) && (totalExpression == null || showRowCalculation)) {
				
				getValues().prepareCountValues();
				BigDecimal result = expression.result(getValues());
				if (result != null) {
					return result.doubleValue();
				}

			}

			return 0d;

		}
	}

	public ComputedAmountCell(Long ownerId) {
		super(ownerId);
		// TODO Auto-generated constructor stub
	}

	public ComputedAmountCell(AmountCell ac) {
		super(ac.getOwnerId());
		this.setColumn(ac.getColumn());
		this.mergedCells = ac.getMergedCells();
	}

	public ComputedAmountCell() {
		super();
	}

	public void merge(Cell c1, Cell c2) {
		super.merge(c1, c2);
		CategAmountCell categ1 = (CategAmountCell) c1;
		CategAmountCell categ2 = (CategAmountCell) c2;
		categ1.getMetaData().addAll(categ2.getMetaData());
	}

	public Cell merge(Cell c) {
		AmountCell ret = (AmountCell) super.merge(c);
		ComputedAmountCell realRet = new ComputedAmountCell(ret.getOwnerId());
		realRet.getMergedCells().addAll(ret.getMergedCells());
		CategAmountCell categ = (CategAmountCell) c;
		realRet.getMetaData().addAll(categ.getMetaData());
		this.getValues().mergeTrailValues(((ComputedAmountCell) c).getValues());
		realRet.setValues(this.getValues());
		return realRet;

	}

	public Cell newInstance() {
		return new ComputedAmountCell();
	}

	@Override
	public Cell filter(Cell metaCell, Set ids) {
		Cell ret = super.filter(metaCell, ids);
		if (ret != null) {
			ret.setColumn(this.getColumn());
		}
		return ret;
	}

	public void setValuesFromCell(CategAmountCell categ) {
		ComputedAmountCell cell = new ComputedAmountCell();

		this.setId(categ.getId());
		this.setOwnerId(categ.getOwnerId());
		this.setValue(categ.getValue());
		this.setFromExchangeRate(categ.getFromExchangeRate());
		this.setCurrencyDate(categ.getCurrencyDate());
		this.setCurrencyCode(categ.getCurrencyCode());
		this.setToExchangeRate(categ.getToExchangeRate());
		this.setColumn(categ.getColumn());
		this.setColumnCellValue(categ.getColumnCellValue());
		this.setColumnPercent(categ.getColumnPercent());
		this.setCummulativeShow(categ.isCummulativeShow());
		this.setShow(categ.isShow());
		this.setRenderizable(categ.isRenderizable());
		this.setCummulativeShow(categ.isCummulativeShow());
		this.setMetaData(categ.getMetaData());

	}

	public Values getValues() {
		if (values == null) {
			values = new Values(ownerId); // getAmount();
			Iterator<CategAmountCell> i = mergedCells.iterator();
			while (i.hasNext()) {
				values.collectCellVariables((CategAmountCell) i.next());
			}
		}
		
		
		return values;
	}

	public void setValues(Values values) {
		this.values = values;
	}

	public String toString() {
		if ((this.getAmount() == 0d) && (this.getColumn().getWorker().getRelatedColumn().getTotalExpression() != null)) {
			return "";
		}

		return FormatHelper.formatNumberUsingCustomFormat(getAmount());
	}
}

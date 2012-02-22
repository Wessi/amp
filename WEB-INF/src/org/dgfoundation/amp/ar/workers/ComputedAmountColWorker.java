/**
 * CummulativeColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.TotalComputedAmountColumn;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;

public class ComputedAmountColWorker extends ColumnWorker {

	protected Map metaInfoCache;

	protected MetaInfo getCachedMetaInfo(String category, Comparable value) {

		if (metaInfoCache == null) {
			metaInfoCache = new HashMap();
		}

		Map valuesMap = (Map) metaInfoCache.get(category);
		if (valuesMap == null) {
			valuesMap = new HashMap();
			metaInfoCache.put(category, valuesMap);
		}
		MetaInfo mi = (MetaInfo) valuesMap.get(value);
		if (mi != null)
			return mi;
		mi = new MetaInfo(category, value);
		valuesMap.put(value, mi);
		return mi;
	}

	/**
	 * @param destName
	 * @param sourceGroup
	 */
	public ComputedAmountColWorker(String condition, String viewName, String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
	}

	public ComputedAmountColWorker(String columnName, GroupColumn rawColumns, ReportGenerator generator) {
		super(columnName, rawColumns, generator);
		sourceName = ArConstants.COLUMN_FUNDING;
	}

	public CellColumn newColumnInstance(int initialCapacity) {
		TotalComputedAmountColumn cc = new TotalComputedAmountColumn(columnName, false, initialCapacity);
		cc.setWorker(this);
		cc.setDescription(this.getRelatedColumn().getDescription());
		cc.setExpression(this.getRelatedColumn().getTokenExpression());
		cc.setTotalExpression(this.getRelatedColumn().getTokenExpression());
		return cc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.
	 * ResultSet)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		AmpARFilter filter = (AmpARFilter) generator.getFilter();
		Long ownerId = new Long(rs.getLong(1));
		Long id = new Long(rs.getLong(2));
		BigDecimal value = rs.getBigDecimal(3);
		String currencyCode = rs.getString(4);
		Date currencyDate = rs.getDate(5);
		double exchangeRate = rs.getDouble(6);
		

		ComputedAmountCell ret = new ComputedAmountCell(ownerId);
		ret.setId(id);
		ret.setAmount(value.doubleValue());
		ret.setFromExchangeRate(exchangeRate);
		ret.setCurrencyDate(currencyDate);
		ret.setCurrencyCode(currencyCode);
		ret.setToExchangeRate(1);
		MetaInfo costMs = getCachedMetaInfo(this.getColumnName(), null);
		ret.getMetaData().add(costMs);
		
		MetaInfo dateMi	= getCachedMetaInfo(ArConstants.TRANSACTION_DATE, currencyDate);
		ret.getMetaData().add(dateMi);

		if ( this.columnsMetaData.containsKey("donor_name") ) {
			String donorName		= rs.getString("donor_name");
			MetaInfo donorNameMi	= getCachedMetaInfo(ArConstants.DONOR, donorName);
			ret.getMetaData().add(donorNameMi);
		}
		if (columnsMetaData.containsKey("terms_assist_name")){
			String termsAssist = rs.getString("terms_assist_name");
			MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.TERMS_OF_ASSISTANCE,
					termsAssist);
			ret.getMetaData().add(termsAssistMeta);
		}
		if (columnsMetaData.containsKey("financing_instrument_name")){			
		    String financingInstrument = rs.getString("financing_instrument_name");
			MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.FINANCING_INSTRUMENT,
					financingInstrument);
			ret.getMetaData().add(termsAssistMeta);
		}
		if(columnsMetaData.containsKey("donor_type_name")) {
			String donorTypeName=rs.getString("donor_type_name");
			MetaInfo donorTypeMeta = this.getCachedMetaInfo(ArConstants.DONOR_TYPE_COL,
					donorTypeName );
			ret.getMetaData().add(donorTypeMeta);
		}
		if (columnsMetaData.containsKey("org_grp_name")) {
			String donorGroupName	= rs.getString("org_grp_name");
			MetaInfo donorGroupMeta = this.getCachedMetaInfo(ArConstants.DONOR_GROUP,
					donorGroupName );
			ret.getMetaData().add(donorGroupMeta);
		}

		// UGLY get exchage rate if cross-rates are needed (if we need to
		// convert from X to USD and then to Y)
		if (filter.getCurrency() != null && !"USD".equals(filter.getCurrency().getCurrencyCode()))
			ret.setToExchangeRate(Util.getExchange(filter.getCurrency().getCurrencyCode(), currencyDate));

		return ret;
	}

	protected Cell getCellFromCell(Cell src) {
		CategAmountCell categ = (CategAmountCell) src;
		ComputedAmountCell cell = new ComputedAmountCell();
		cell.setValuesFromCell((CategAmountCell) src);
		return cell;
	}

	public Cell newCellInstance() {
		return new ComputedAmountCell();

	}

	protected Column generateCellColumn() {
		CellColumn dest = null;
		Column sourceCol = sourceGroup.getColumn(sourceName);
		dest = newColumnInstance(sourceCol.getItems().size());
		Iterator i = sourceCol.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			Cell destCell = getCellFromCell(element);
			if (destCell != null)
				dest.addCell(destCell);
		}

		// check injected funding
		if (sourceGroup.getColumn(ArConstants.COSTING_GRAND_TOTAL) != null) {
			CellColumn injectedDest = null;
			Column injectedSourceCol = sourceGroup.getColumn(ArConstants.COSTING_GRAND_TOTAL);
			i = injectedSourceCol.iterator();
			while (i.hasNext()) {
				ComputedAmountCell element = (ComputedAmountCell) i.next();
				dest.addCell(element);
			}
		}
		return dest;
	}
}

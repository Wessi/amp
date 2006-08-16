/**
 * CategAmountColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.dgfoundation.amp.ar.AmountCellColumn;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 13, 2006
 * 
 */
public class CategAmountColWorker extends ColumnWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 */
	public CategAmountColWorker(String condition, String viewName,
			String columnName,ReportGenerator generator) {
		super(condition, viewName, columnName,generator);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		Long ownerId = new Long(rs.getLong(1));
		Long id = new Long(rs.getLong(3));
		CategAmountCell acc = new CategAmountCell(ownerId);

		acc.setId(id);

		int tr_type = rs.getInt("transaction_type");
		int adj_type = rs.getInt("adjustment_type");
		double tr_amount = rs.getDouble("transaction_amount");
		java.sql.Date td = rs.getDate("transaction_date");
		double exchangeRate=rs.getDouble("exchange_rate");
		String currencyCode=rs.getString("currency_code");

		


		try {
			String termsAssist = rs.getString("terms_assist_name");
			MetaInfo termsAssistMeta = new MetaInfo("Terms of Assistance",
					termsAssist);
			acc.getMetaData().add(termsAssistMeta);
		} catch (SQLException e) {

		}
		try {
			String regionName = rs.getString("region_name");
			MetaInfo regionMeta = new MetaInfo("Region", regionName);
			acc.getMetaData().add(regionMeta);
		} catch (SQLException e) {
		}

		try {
		
		String componentName = rs.getString("component_name");
		MetaInfo compMeta = new MetaInfo("Component", componentName);
		acc.getMetaData().add(compMeta);
		} catch (SQLException e) {
		}

		acc.setAmount(tr_amount);
		acc.setFromExchangeRate(exchangeRate);
		acc.setCurrencyCode(currencyCode);
		//put toExchangeRate
		acc.setToExchangeRate(1);
		
		MetaInfo adjMs = new MetaInfo("Adjustment Type",
				adj_type == 0 ? "Planned" : "Actual");
		String trStr = null;

		switch (tr_type) {
		case 0:
			trStr = "Commitment";
			break;
		case 1:
			trStr = "Disbursement";
			break;
		case 2:
			trStr = "Expenditure";
			break;
		}

		MetaInfo trMs = new MetaInfo("Transaction Type", trStr);
		MetaInfo fundMs = new MetaInfo("Funding Type", (String) adjMs
				.getValue()
				+ " " + (String) trMs.getValue());

		Calendar c = Calendar.getInstance();
		c.setTime(td);

		MetaInfo qMs = new MetaInfo("Quarter", "Q"
				+ new Integer(c.get(Calendar.MONTH) / 4 + 1));
		MetaInfo aMs = new MetaInfo("Year", new Integer(c.get(Calendar.YEAR)));

		acc.getMetaData().add(adjMs);
		acc.getMetaData().add(trMs);
		acc.getMetaData().add(fundMs);
		acc.getMetaData().add(aMs);
		acc.getMetaData().add(qMs);

		return acc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */
	protected Cell getCellFromCell(Cell src) {
		// TODO Auto-generated method stub
		return null;
	}

	public CellColumn newColumnInstance() {
		return new AmountCellColumn(columnName);
	}

}

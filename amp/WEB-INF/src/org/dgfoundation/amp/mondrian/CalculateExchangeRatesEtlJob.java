package org.dgfoundation.amp.mondrian;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.jobs.Fingerprint;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

import static org.dgfoundation.amp.mondrian.MondrianTablesRepository.FACT_TABLE;
import static org.dgfoundation.amp.mondrian.MondrianETL.MONDRIAN_EXCHANGE_RATES_TABLE;

public class CalculateExchangeRatesEtlJob {
	
	protected final Connection conn;
	protected final MonetConnection monetConn;
	protected final List<Long> currencyIds;
	
	protected final Set<Long> activityIds;
	protected final Set<Long> dateCodes;
	
	protected static Logger logger = Logger.getLogger(MondrianETL.class);
	
	public CalculateExchangeRatesEtlJob(List<Long> currencyIds, Connection conn, MonetConnection monetConn, Set<Long> activityIds, Set<Long> dateCodes) {
		this.conn = conn;
		this.monetConn = monetConn;
		this.currencyIds = Collections.unmodifiableList(currencyIds == null ? SQLUtils.fetchLongs(monetConn.conn, "SELECT amp_currency_id FROM amp_currency") : currencyIds);
		this.activityIds = activityIds;
		this.dateCodes = dateCodes;
	}
			
	public void work() throws SQLException {
		generateExchangeRatesTable();
		generateExchangeRatesColumns();
	}

	public void generateExchangeRatesColumns() {
		Set<String> factTableColumns = monetConn.getTableColumns(FACT_TABLE.tableName);
		for (long currId:currencyIds) {
			if (!factTableColumns.contains(getColumnName(currId)))
				monetConn.executeQuery(String.format("ALTER TABLE %s ADD %s DOUBLE", FACT_TABLE.tableName, getColumnName(currId)));
		}

		StringBuilder conditionBuilder = new StringBuilder("(entity_id IN (" + Util.toCSStringForIN(activityIds) + "))");
		if (dateCodes != null)
			conditionBuilder.append(" OR (mondrian_fact_table.date_code IN (" + Util.toCSStringForIN(dateCodes) + "))");
		
		if (dateCodes != null && dateCodes.isEmpty() && activityIds.isEmpty())
			return; 
		
		String condition = conditionBuilder.toString();
		
		if (currencyIds.size() > 2) {
			// smart
			if (!factTableColumns.contains("amount_base_currency")) {
				monetConn.executeQuery(String.format("ALTER TABLE %s ADD %s DOUBLE", FACT_TABLE.tableName, "amount_base_currency"));
			}
			monetConn.executeQuery(String.format("UPDATE mondrian_fact_table SET amount_base_currency = transaction_amount * (select mer.exchange_rate from mondrian_exchange_rates mer WHERE mer.day_code = mondrian_fact_table.date_code AND mer.currency_id = mondrian_fact_table.currency_id) WHERE (%s)", condition));
			for (long currId:currencyIds) {
				String query = String.format(
						"UPDATE %s SET %s = " +  
						"amount_base_currency / (select mer.exchange_rate from mondrian_exchange_rates mer WHERE mer.day_code = mondrian_fact_table.date_code AND mer.currency_id = %d) WHERE (%s)",
						FACT_TABLE.tableName, getColumnName(currId), currId, condition); 
				monetConn.executeQuery(query);
			}
		} else {
			// stupid
			for (long currId:currencyIds) {
				String query = String.format(
					"UPDATE %s SET %s = transaction_amount * (select mer.exchange_rate from %s mer WHERE mer.day_code = %s.date_code AND mer.currency_id = %s.currency_id) / (select mer2.exchange_rate from %s mer2 WHERE mer2.day_code = %s.date_code AND mer2.currency_id = %s) WHERE (%s)",
					FACT_TABLE.tableName, getColumnName(currId), MONDRIAN_EXCHANGE_RATES_TABLE, FACT_TABLE.tableName, FACT_TABLE.tableName, MONDRIAN_EXCHANGE_RATES_TABLE, FACT_TABLE.tableName, currId, condition);
				monetConn.executeQuery(query);
			}
			return;
		}
	}
	
	public String getColumnName(long currId) {
		return "transaction_exch_" + currId;
	}
	
	/**
	 * generates exchange rate entries for the cartesian product (transaction date, currency)
	 * to be called every time:
	 * 1) an exchange rate changes
	 * 2) a new currency is added
	 * 3) a new transaction date appears in the database
	 */
	protected void generateExchangeRatesTable() throws SQLException {
		logger.warn("generating exchange rates ETL...");
		if (dateCodes == null) {
			monetConn.dropTable(MONDRIAN_EXCHANGE_RATES_TABLE);
			monetConn.executeQuery("CREATE TABLE " + MONDRIAN_EXCHANGE_RATES_TABLE + "(" + 
										"day_code integer NOT NULL," + 
										"currency_id bigint NOT NULL," + 
										"exchange_rate double)");
			monetConn.copyTableFromPostgres(this.conn, "amp_currency");
		}
		
		SortedSet<Long> usedDates =
				dateCodes == null ?
						new TreeSet<>(SQLUtils.fetchLongs(monetConn.conn, "select distinct(date_code) as day_code from mondrian_raw_donor_transactions"))
						: new TreeSet<>(dateCodes);
		if (usedDates.isEmpty())
			return;
		List<Long> allCurrencies = SQLUtils.fetchLongs(monetConn.conn, "SELECT amp_currency_id FROM amp_currency");
		for (Long currency:allCurrencies) {
			generateExchangeRateEntriesForCurrency(CurrencyUtil.getAmpcurrency(currency), usedDates);
		}		
		logger.warn("... done generating exchange rates ETL...");
		//monetConn.copyTableFromPostgres(this.conn, MONDRIAN_EXCHANGE_RATES_TABLE);
	}
	
	/**
	 * generates the exchange rates for the said currency for all the given dates
	 * @param ampCurrencyId
	 * @param allDates
	 * @throws SQLException
	 */
	protected void generateExchangeRateEntriesForCurrency(AmpCurrency currency, SortedSet<Long> allDates) throws SQLException {
		List<List<Object>> entries = new CurrencyETL(currency, conn).work(allDates);
		monetConn.executeQuery("DELETE FROM " + MONDRIAN_EXCHANGE_RATES_TABLE + " WHERE (currency_id = " + currency.getAmpCurrencyId() + ") AND (day_code IN (" + Util.toCSStringForIN(allDates) + "))");
		SQLUtils.insert(monetConn.conn, MONDRIAN_EXCHANGE_RATES_TABLE, null, null, Arrays.asList("day_code", "currency_id", "exchange_rate"), entries);
	}
	
//	public static Fingerprint getFingerprint() {
//		return new Fingerprint("exchange_rates",
//					Arrays.asList(
//							"select now()"
///*							Fingerprint.buildTableHashingQuery("amp_currency"),
//							"select max(modification_date) FROM amp_currency_rate",
//							"select settingsvalue from amp_global_settings where settingsname='Base Currency'"*/
//				));
//	}
}

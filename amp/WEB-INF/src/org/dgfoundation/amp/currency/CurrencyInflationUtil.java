/**
 * 
 */
package org.dgfoundation.amp.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.dbentity.AmpInflationSource;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.hibernate.Session;

/**
 * Common currency inflation rates utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyInflationUtil {
	protected static final Logger logger = Logger.getLogger(CurrencyInflationUtil.class);
	
	public static List<AmpInflationSource> getInflationDataSources() {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select o from " +  AmpInflationSource.class.getName() + " o").list();
	}
	
	public static AmpInflationSource getInflationDataSource(Long sourceId) {
		return (AmpInflationSource) PersistenceManager.getRequestDBSession().get(AmpInflationSource.class, sourceId);
	}
	
	public static List<AmpInflationRate> getInflationRates() {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select o from " +  AmpInflationRate.class.getName() + " o").list();
	}
	
	public static List<AmpInflationRate> getInflationRates(String currencyCode) {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select r from " +  AmpInflationRate.class.getName() + " r, "
				+ AmpCurrency.class.getName() + " c "
						+ "where r.currency = c and c.currencyCode = " + currencyCode).list();
	}
	
	public static void deleteAllInflationRates() {
		Session session = PersistenceManager.getRequestDBSession();
		for (AmpInflationRate air : getInflationRates()) {
			session.delete(air);
		}
		session.flush();
	}
	
	public static List<AmpCurrency> getConstantAmpCurrencies() {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select o from " +  AmpCurrency.class.getName() + " o where o.activeFlag = 1 and virtual = true").list();
	}
	
	public static void deleteConstantCurrencies(List<AmpCurrency> constantCurrencies) {
		for (AmpCurrency ampConstCurrency : constantCurrencies) {
			deleteConstantCurrencies(ampConstCurrency);
		}
	}
	
	/**
	 * @return a valid map of constant currencies per calendar
	 */
	public static Map<AmpFiscalCalendar, List<ConstantCurrency>> getConstantCurrenciesByCalendar() {
		Map<AmpFiscalCalendar, List<ConstantCurrency>> constCurrencies = new HashMap<AmpFiscalCalendar, List<ConstantCurrency>>();
		List<AmpCurrency> ampCurrencies = getConstantAmpCurrencies();
		for (AmpCurrency ampCurrency : ampCurrencies) {
			ConstantCurrency cc = new ConstantCurrency(ampCurrency);
			if (cc.year == null || cc.calendar == null) {
				// we need to understand why we have invalid entries to fix it properly, for now just skipping
				logger.error(String.format("Skipping invalid constant currency id=%d, code=%s, please review!!!", 
						ampCurrency.getAmpCurrencyId(), ampCurrency.getCurrencyCode()));
				continue;
			}
			List<ConstantCurrency> calConstCurrencies = constCurrencies.get(ampCurrency.getCalendar());
			if (calConstCurrencies == null) {
				calConstCurrencies = new ArrayList<ConstantCurrency>();
				constCurrencies.put(ampCurrency.getCalendar(), calConstCurrencies);
			}
			calConstCurrencies.add(cc);
		}
		return constCurrencies;
	}
	
	public static ConstantCurrency createOrActivateConstantCurrency(AmpCurrency standardCurrency, 
			AmpFiscalCalendar calendar, int year) {
		String constCurrencyCode = ConstantCurrency.buildConstantCurrencyCode(standardCurrency, calendar, year);
		AmpCurrency constCurrency = CurrencyUtil.getCurrencyByCode(constCurrencyCode);
		if (constCurrency == null) {
			constCurrency = new AmpCurrency();
			constCurrency.setVirtual(true);
			constCurrency.setCalendar(calendar);
			constCurrency.setCurrencyCode(constCurrencyCode);
			constCurrency.setCountryName(ConstantCurrency.buildConstantCurrencyName(standardCurrency, calendar, year));
			constCurrency.setCountryName(standardCurrency.getCountryName());
			constCurrency.setCountryLocation(standardCurrency.getCountryLocation());
		}
		constCurrency.setActiveFlag(1);
		return new ConstantCurrency(constCurrency);
	}
	
	public static void deleteConstantCurrencies(AmpCurrency ampConstCurrency) {
		ConstantCurrency cc = new ConstantCurrency(ampConstCurrency);
		if (cc.calendar == null || cc.year == null) {
			logger.error(String.format("Skipping invalid constant currency id=%d, code=%s, please review!!!", 
					ampConstCurrency.getAmpCurrencyId(), ampConstCurrency.getCurrencyCode()));
		} else {
			// replace all references with standard currency or default is standard one is removed
			AmpCurrency standarCurrency = CurrencyUtil.getCurrencyByCode(cc.standardCurrencyCode);
			if (standarCurrency == null)
				standarCurrency = CurrencyUtil.getDefaultCurrency();
			PersistenceManager.getSession().createQuery("update " + AmpFilterData.class.getName() + " o set o.value = " +
					standarCurrency.getAmpCurrencyId() + " where o.propertyName = 'currency' and o.value = '" + 
					ampConstCurrency.getAmpCurrencyId() + "'").executeUpdate();
			// delete currency rates (since not relevant to keep) and the constant currency itself
			try {
				CurrencyUtil.deleteCurrencyRates(ampConstCurrency.getCurrencyCode());
				CurrencyUtil.deleteCurrency(ampConstCurrency.getCurrencyCode());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
}

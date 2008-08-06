/*
 * CurrencyUtil.java
 * Created: 01-May-2005
 */
package org.digijava.module.aim.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.JDBCException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;

public class CurrencyUtil {

	private static Logger logger = Logger.getLogger(CurrencyUtil.class);
	public static DecimalFormat df = new DecimalFormat("###,###,###,###,###") ;

	public static final int RATE_FROM_FILE					= 0;
	public static final int RATE_FROM_WEB_SERVICE			= 1;
	public static final int RATE_BY_HAND					= 2;

    public static final int ORDER_BY_CURRENCY_CODE			=-1;
    public static final int ORDER_BY_CURRENCY_NAME			= 2;
    public static final int ORDER_BY_CURRENCY_COUNTRY_NAME	= 3;
    public static final int ALL_ACTIVE						= 1;
    public static final String BASE_CODE                    = "USD";


	public static Collection getAllActiveRates() {
		Collection col = new ArrayList();
		Session session = null;
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select currency from " + AmpCurrency.class.getName() + "" +
					" currency where currency.activeFlag='1'";
			qry = session.createQuery(qryStr);
			Collection res = qry.list();
			if (res.size() > 0) {
				logger.debug("Active currencies found");
				Iterator itr = res.iterator();
				AmpCurrency currencies[] = new AmpCurrency[res.size()];
				int index = 0;
				while (itr.hasNext()) {
					currencies[index++] = (AmpCurrency) itr.next();
					logger.debug(currencies[index-1].getCurrencyName());
				}
				qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
				"where cRate.toCurrencyCode in ('";

				for (int i = 0;i < index;i ++) {
					qryStr += currencies[i].getCurrencyCode() + "'";
					if ((i+1) < index) {
						qryStr += ",'";
					}
				}
				qryStr += ") order by cRate.exchangeRateDate desc,cRate.toCurrencyCode";
				qry = session.createQuery(qryStr);
				itr = qry.list().iterator();
				AmpCurrencyRate cRate = null;

				CurrencyRates currencyRates = null;
				while (itr.hasNext()) {
					currencyRates = new CurrencyRates();
					cRate = (AmpCurrencyRate) itr.next();

					currencyRates.setCurrencyCode(cRate.getToCurrencyCode());
					for (int i = 0;i < currencies.length;i ++) {
						if (currencies[i].getCurrencyCode().equals(
								cRate.getToCurrencyCode())) {
							currencyRates.setCurrencyName(currencies[i].
									getCurrencyName());
							break;
						}
					}
					currencyRates.setExchangeRate(cRate.getExchangeRate());
					currencyRates.setExchangeRateDate(DateConversion.
							ConvertDateToString(cRate.getExchangeRateDate()));
					currencyRates.setId(cRate.getAmpCurrencyRateId());
					col.add(currencyRates);
				}

			}

		} catch (Exception e) {
			logger.error("Exception from getAllActiveRates");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		logger.info("returning a collection of size get all active rates function" + col.size());
		return col;
	}

	public static Collection getActiveRates(Date fromDate,Date toDate) {
		Collection col = new ArrayList();
		Session session = null;
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select currency from " + AmpCurrency.class.getName() + "" +
					" currency where currency.activeFlag='1'";
			qry = session.createQuery(qryStr);
			Collection res = qry.list();
			if (res.size() > 0) {
				logger.debug("Active currencies found");
				Iterator itr = res.iterator();
				AmpCurrency currencies[] = new AmpCurrency[res.size()];
				int index = 0;
				while (itr.hasNext()) {
					currencies[index++] = (AmpCurrency) itr.next();
					logger.debug(currencies[index-1].getCurrencyName());
				}
				qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
				"where cRate.toCurrencyCode in ('";

				for (int i = 0;i < index;i ++) {
					qryStr += currencies[i].getCurrencyCode() + "'";
					if ((i+1) < index) {
						qryStr += ",'";
					}
				}
				qryStr += ") and cRate.exchangeRateDate between :fromDate and :toDate order by " +
						"cRate.exchangeRateDate desc,cRate.toCurrencyCode";
				qry = session.createQuery(qryStr);
				qry.setParameter("fromDate",fromDate,Hibernate.DATE);
				qry.setParameter("toDate",toDate,Hibernate.DATE);

				itr = qry.list().iterator();
				AmpCurrencyRate cRate = null;

				CurrencyRates currencyRates = null;
				while (itr.hasNext()) {
					currencyRates = new CurrencyRates();
					cRate = (AmpCurrencyRate) itr.next();

					currencyRates.setId(cRate.getAmpCurrencyRateId());
					currencyRates.setCurrencyCode(cRate.getToCurrencyCode());
					for (int i = 0;i < currencies.length;i ++) {
						if (currencies[i].getCurrencyCode().equals(
								cRate.getToCurrencyCode())) {
							currencyRates.setCurrencyName(currencies[i].
									getCurrencyName());
							break;
						}
					}
					currencyRates.setExchangeRate(cRate.getExchangeRate());
					currencyRates.setExchangeRateDate(DateConversion.
							ConvertDateToString(cRate.getExchangeRateDate()));
					currencyRates.setId(cRate.getAmpCurrencyRateId());
					col.add(currencyRates);
				}

			}

		} catch (Exception e) {
			logger.error("Exception from getActiveRates");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		logger.info("returning a collection of size...get active rates... " + col.size());
		return col;
	}
	/**
	 * AMP-2805
	 * @deprecated use Util.getExchange(currency, currencyDate)
	 * @param currCode
	 * @param date
	 * @return
	 */
	public static Double getExchangeRate(String currCode,Date date) {
		Double exchRate = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select cr.exchangeRate from " + AmpCurrencyRate.class.getName() + "" +
					" cr where (cr.toCurrencyCode=:code) and " +
					"(cr.exchangeRateDate=:date)";
			qry = session.createQuery(qryStr);
			qry.setParameter("code",currCode,Hibernate.STRING);
			qry.setParameter("date",date,Hibernate.DATE);

			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				exchRate = (Double) itr.next();
			}
		} catch (Exception e) {
			logger.error("Exception from getExchangeRate()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return exchRate;
	}

	/**
	 * Saves an AmpCurrencyRate object to the database
	 * @param cRate The AmpCurrencyRate object
	 */
	public static void saveCurrencyRate(AmpCurrencyRate cRate) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		String qryStr = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
					"where (cRate.toCurrencyCode=:code) and " +
					"(cRate.exchangeRateDate=:date)";
			qry = session.createQuery(qryStr);
			qry.setParameter("code",cRate.getToCurrencyCode(),Hibernate.STRING);
			qry.setParameter("date",cRate.getExchangeRateDate(),Hibernate.DATE);

			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				// if the currency rate already exist update the rate
				AmpCurrencyRate actRate = (AmpCurrencyRate) itr.next();
				actRate.setExchangeRate(cRate.getExchangeRate());
				//System.out.println("updating......................");
				session.update(actRate);
			} else {
				// add the currency rate object if it does not exist
				//System.out.println("saving......................");
				session.save(cRate);
			}

			tx.commit();
		} catch (Exception e) {
			logger.error("Exception from saveCurrencyRate");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	public static Collection getAllCurrencies(int active) {
		Collection col = new ArrayList();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		try {
			session = PersistenceManager.getSession();
			if (active == CurrencyUtil.ORDER_BY_CURRENCY_CODE) {
				qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"order by curr.currencyCode";
				qry = session.createQuery(qryStr);
			}else if(active == CurrencyUtil.ORDER_BY_CURRENCY_NAME){
				qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
				"order by curr.currencyName";
			qry = session.createQuery(qryStr);
			}else if(active == CurrencyUtil.ORDER_BY_CURRENCY_COUNTRY_NAME){
				qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
				"order by curr.countryName";
			qry = session.createQuery(qryStr);
			}else {
				qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"where (curr.activeFlag=:flag) order by curr.currencyCode";
				qry = session.createQuery(qryStr);
				qry.setParameter("flag",new Integer(active),Hibernate.INTEGER);
			}
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return col;
	}

	public static void updateCurrencyStatus(String code,int status) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"where (curr.currencyCode=:code)";
			qry = session.createQuery(qryStr);
			qry.setParameter("code",code,Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpCurrency curr = (AmpCurrency) itr.next();
				curr.setActiveFlag(new Integer(status));
				tx = session.beginTransaction();
				session.update(curr);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Exception from updateCurrencyStatus");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	public static boolean currencyCodeExist(String currCode,Long id) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		boolean exist = false;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select count(*) from " + AmpCurrency.class.getName() + " c where " +
					"(c.currencyCode=:code)";
			if (id != null) {
				qryStr += " and (c.ampCurrencyId!=:id)";

			}
			qry = session.createQuery(qryStr);
			qry.setParameter("code",currCode,Hibernate.STRING);
			if (id != null) {
				qry.setParameter("id",id,Hibernate.LONG);
			}
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Integer count = (Integer) itr.next();
				if (count.intValue() > 0) {
					exist = true;
				}
			}

		} catch (Exception e) {
			logger.error("Exception from currencyCodeExist()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		return exist;
	}

	public static void saveCurrency(AmpCurrency curr) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			session.save(curr);
			tx.commit();

		} catch (Exception e) {
			logger.error("Exception from saveCurrency");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}
	public static void updateCurrency(AmpCurrency curr) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			session.update(curr);
			tx.commit();

		} catch (Exception e) {
			logger.error("Exception from saveCurrency");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}


	/**
	 * Save an AmpCurrency object
	 * @param currency The AmpCurrency Object to be saved
	 * @param cRate The initial currency rates in an AmpCurrencyRate object
	 */
	public static void saveCurrency(AmpCurrency currency,AmpCurrencyRate cRate) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"where (curr.ampCurrencyId=:id)";
			qry = session.createQuery(qryStr);
			logger.debug("Checking with the id " + currency.getAmpCurrencyId());
			qry.setParameter("id",currency.getAmpCurrencyId(),Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				// currency object already exist, update the object
				logger.debug("Updating the existing currency id ...");
				AmpCurrency curr = (AmpCurrency) itr.next();
				curr.setCountryName(currency.getCountryName());
				curr.setCurrencyCode(currency.getCurrencyCode());
				curr.setCurrencyName(currency.getCurrencyName());
                curr.setCountryId(currency.getCountryId());
				tx = session.beginTransaction();
				session.update(curr);
				tx.commit();
			} else {
				logger.debug("Creating new currency id ...");
				tx = session.beginTransaction();
				session.save(currency);
				session.save(cRate);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Exception from saveCurrency");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	public static void deleteCurrencyRates(Long cRates[]) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			for (int i = 0;i < cRates.length; i++) {
				if (cRates[i] != null) {
					AmpCurrencyRate cRate = (AmpCurrencyRate) session.load(AmpCurrencyRate.class,cRates[i]);
					session.delete(cRate);
				}
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Exception from deleteCurrencyRates");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	/**
	 * Saves currency rates to the database
	 * @param currRates Collection of CurrencyRates object which need
	 * to be saved
	 */
	public static void saveCurrencyRates(Collection currRates) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			Iterator itr = currRates.iterator();
			logger.debug("currency rates size :" + currRates.size());
			while (itr.hasNext()) {
				CurrencyRates cr = (CurrencyRates) itr.next();
				qryStr = "select crate from " + AmpCurrencyRate.class.getName() +
					" crate where (crate.toCurrencyCode=:code) and " +
					"(crate.exchangeRateDate=:date)";
				qry = session.createQuery(qryStr);
				qry.setParameter("code",cr.getCurrencyCode(),Hibernate.STRING);
				Date exRtDate = DateConversion.getDate(cr.getExchangeRateDate());
				qry.setParameter("date",exRtDate,Hibernate.DATE);
				Iterator tmpItr = qry.list().iterator();
				if (tmpItr.hasNext()) {
					AmpCurrencyRate currencyRate = (AmpCurrencyRate) tmpItr.next();
					currencyRate.setExchangeRate(cr.getExchangeRate());
					session.update(currencyRate);
					logger.debug("Updating " + currencyRate.getToCurrencyCode());
				} else {
					AmpCurrencyRate currencyRate = new AmpCurrencyRate();
					currencyRate.setExchangeRate(cr.getExchangeRate());
					currencyRate.setExchangeRateDate(exRtDate);
					currencyRate.setToCurrencyCode(cr.getCurrencyCode());
					currencyRate.setDataSource(CurrencyUtil.RATE_FROM_FILE);
					logger.debug("Saving " + currencyRate.getToCurrencyCode());
					session.save(currencyRate);
				}
			}
			tx.commit();
			DigiCacheManager.getInstance().getCache("EXCHANGE_RATES_CACHE").clear();
		} catch (Exception e) {
			logger.error("Exception from saveCurrencyRates");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	public static void addCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) throws AimException {
		DbUtil.add(ampCurr);
		DbUtil.add(ampCurrRate);
	}

	public static void deleteCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) {
		try {
			DbUtil.delete(ampCurr);
			DbUtil.delete(ampCurrRate);
		} catch (JDBCException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
	}

	public static void updateCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) {
		DbUtil.update(ampCurr);
		DbUtil.update(ampCurrRate);
	}

	public static AmpCurrency getCurrencyByCode(String currCode) {
		Session session = null;
		Query qry = null;
		AmpCurrency ampCurrency = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c where (c.currencyCode=:currCode)";
			qry = session.createQuery(queryString);
			qry.setParameter("currCode", currCode, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampCurrency = (AmpCurrency) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get currency");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampCurrency;
	}

	public static Currency getCurrency(Long id) {
		logger.debug("in getCurrency" + id);
		Session sess = null;
		Query qry1 = null;
		Query qry2 = null;
		Currency curr = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c where (c.ampCurrencyId=:id)";
			qry1 = sess.createQuery(queryString);
			qry1.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry1.list().iterator();
			if (itr.hasNext()) {
				AmpCurrency ampCurr = (AmpCurrency) itr.next();
				String qryStr = "select cr from "
						+ AmpCurrencyRate.class.getName()
						+ " cr where (cr.toCurrencyCode=:currCode)";
				qry2 = sess.createQuery(qryStr);
				qry2.setParameter("currCode", ampCurr.getCurrencyCode(),
						Hibernate.STRING);
				Iterator itr1 = qry2.list().iterator();
				if (itr1.hasNext()) {
					AmpCurrencyRate ampCurrRate = (AmpCurrencyRate) itr1.next();
					curr = new Currency();
					curr.setCurrencyId(ampCurr.getAmpCurrencyId());
					curr.setCurrencyRateId(ampCurrRate.getAmpCurrencyRateId());
					curr.setCurrencyCode(ampCurr.getCurrencyCode());
					curr.setCountryName(ampCurr.getCountryName());
					curr.setExchangeRate(ampCurrRate.getExchangeRate());
				}
			}

		} catch (Exception e) {
			logger.debug("Exception from getCurrency()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		logger.debug("out");
		return curr;
	}

	public static AmpCurrency getAmpcurrency(Long id) {
		AmpCurrency ampCurrency = null;
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c " + "where (c.ampCurrencyId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampCurrency = (AmpCurrency) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get currency " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return ampCurrency;
	}

	public static ArrayList getAmpCurrency() {
		AmpCurrency ampCurrency = null;
		Session session = null;
		Query q = null;
		Iterator iter = null;
		String queryString = null;
		ArrayList currency = new ArrayList();
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select c from " + AmpCurrency.class.getName()
					+ " c where c.activeFlag='1' order by c.currencyName";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampCurrency = (AmpCurrency) iter.next();
				currency.add(ampCurrency);
			}
		} catch (Exception ex) {
			logger.error("Unable to get currency " + ex);
		}
//		finally {
//			try {
//				PersistenceManager.releaseSession(session);
//			} catch (Exception ex2) {
//				logger.debug("releaseSession() failed", ex2);
//			}
//		}
		return currency;
	}

	public static double getExchangeRate(String currencyCode,
			int adjustmentType, Date exchangeRateDate) {
		//if (logger.isDebugEnabled())
			//logger.debug("getExchangeRate with currencyCode" + currencyCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		Double exchangeRate = null;

		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(exchangeRateDate);
			String currencyDate=calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-01";
		/*	logger.debug("Date: " + exchangeRateDate);
			logger.debug("Exchange Rate Month: " + calendar.get(Calendar.MONTH));
			logger.debug("Exchange Rate Year: " + calendar.get(Calendar.YEAR));
			logger.debug("Currency Code: " + currencyCode);*/
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.exchangeRate from "
					+ AmpCurrencyRate.class.getName()
					+ " f where (f.toCurrencyCode='" + currencyCode + "') and (f.exchangeRateDate='" + currencyDate + "')";
//			logger.debug("queryString:" + queryString);
			q = session.createQuery(queryString);
//			q.setParameter("currencyCode", currencyCode, Hibernate.STRING);

//			q.setParameter("exchangeRateDate", exchangeRateDate,Hibernate.DATE);
			boolean searchOther = false;
			if (q.list().size() > 0){
				exchangeRate = (Double) q.list().get(0);
				if (exchangeRate == null)
					searchOther = true;
			}
			else
				searchOther = true;

			if (searchOther){
				queryString = "select f.exchangeRate from "
						+ AmpCurrencyRate.class.getName()
						+ " f where (f.toCurrencyCode=:currencyCode) and (f.exchangeRateDate<:exchangeRateDate) order by f.exchangeRateDate desc";
				q = session.createQuery(queryString);
					q.setParameter("currencyCode", currencyCode,
							Hibernate.STRING);
					q.setParameter("exchangeRateDate", exchangeRateDate,
							Hibernate.DATE);
					if (q.list().size() > 0){
						exchangeRate = (Double) q.list().get(0);
						Iterator itr = q.list().iterator();
						while ((exchangeRate == null)&&(itr.hasNext())) //fix for null currency
							exchangeRate = (Double) itr.next();
					}
					else {
						queryString = "select f.exchangeRate from "
								+ AmpCurrencyRate.class.getName()
								+ " f where (f.toCurrencyCode=:currencyCode) and (f.exchangeRateDate>:exchangeRateDate) order by f.exchangeRateDate";
						q = session.createQuery(queryString);
						q.setParameter("currencyCode", currencyCode,
								Hibernate.STRING);
						q.setParameter("exchangeRateDate", exchangeRateDate,
								Hibernate.DATE);
						if (q.list().size() > 0){
							exchangeRate = (Double) q.list().get(0);
							Iterator itr = q.list().iterator();
							while ((exchangeRate == null)&&(itr.hasNext())) //fix for null currency
								exchangeRate = (Double) itr.next();
						}
					}
				}

		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database " + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
/*		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate() for currency code :" + currencyCode
					+ "returns " + exchangeRate); */
		if(exchangeRate!=null)
			return exchangeRate.doubleValue();
		return 0;
	}

	public static Collection getAmpCurrencyRate() {

		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select currency from "
					+ AmpCurrencyRate.class.getName() + " currency";
			q = session.createQuery(queryString);
			c = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return c;
	}

	public static double getExchangeRate(Long ampFundingId, String orgRoleCode) {

		//if (logger.isDebugEnabled())
			//logger.debug("getExchangeRate with ampFundingId=" + ampFundingId
				//	+ " orgRoleCode=" + orgRoleCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		AmpCurrency ampCurrency = null;
		String currencyCode = "";
		double exchangeRate = 1.0;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.ampCurrencyId from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) and (f.orgRoleCode=:orgRoleCode)"
					+ " group by f.ampFundingId";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("orgRoleCode", orgRoleCode, Hibernate.STRING);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					ampCurrency = (AmpCurrency) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger
							.debug("Unable to get ampCurrencyId from table AmpFunding");
			}

			if (ampCurrency != null) {
				currencyCode = ampCurrency.getCurrencyCode();
				exchangeRate = getExchangeRate(currencyCode);
			}
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("getExchangeRate(id,orgRoleCode " + exchangeRate);
		return exchangeRate;
	}

	public static double getExchangeRate(String currencyCode) {
		//if (logger.isDebugEnabled())
			//logger.debug("getExchangeRate with currencyCode" + currencyCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		Double exchangeRate = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.exchangeRate from "
					+ AmpCurrencyRate.class.getName()
					+ " f where (f.toCurrencyCode=:currencyCode) order by f.exchangeRateDate desc";
			q = session.createQuery(queryString);
			q.setParameter("currencyCode", currencyCode, Hibernate.STRING);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					exchangeRate = (Double) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger
							.debug("Unable to get exchange rate for currency code :"
									+ currencyCode);
				exchangeRate = new Double(1.0);
			}
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
/*		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate() for currency code :" + currencyCode
					+ "returns " + exchangeRate); */
		return exchangeRate.doubleValue();
	}

	/**
	 * Returns true if currency have at leat one value added.
	 * Used in addfunding.
	 * @param currencyCode currency Code
	 * @return boolean
	 * @author Irakli Kobiashvili
	 */
	public static Boolean isRate(String currencyCode) throws AimException {
		Session session = null;
		Query q = null;
		Date todate;
		Date fromdate;
		try {
			Calendar cal=Calendar.getInstance();
			todate = cal.getTime();
			cal.add(Calendar.YEAR, -1);
			fromdate = cal.getTime();
			if (currencyCode.equalsIgnoreCase("USD") ){
				return true;
			}
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f.exchangeRate from "
					+ AmpCurrencyRate.class.getName()
					+ " f where (f.toCurrencyCode=:currencyCode) and f.exchangeRateDate between :fromDate and :toDate order by f.exchangeRateDate desc";
			q = session.createQuery(queryString);
			q.setString("currencyCode", currencyCode);
			q.setParameter("fromDate",fromdate,Hibernate.DATE);
			q.setParameter("toDate",todate,Hibernate.DATE);
			List rates = q.list();
			boolean result = false;
			for (Iterator iter = rates.iterator(); iter.hasNext();) {
				Double element = (Double) iter.next();
				if (element != null){
					result = true;
					break;
				}
			}
			return result;
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
			throw new AimException("Error retriving currency exchange rate for "+ currencyCode,ex);
		}
	}




	/**
	 * Returns Latest Exchange rate for currency specified in parameter by code.
	 * Used in NPD, may be temporarry
	 * @param currencyCode currency Code
	 * @return exchange rate, latest value
	 * @author Irakli Kobiashvili
	 */
        public static double getLatestExchangeRate(String currencyCode) throws AimException {
                Session session = null;
                Query q = null;
                try {
                        logger.debug("retrivieving latest exchange rate for currency:"+currencyCode);
                        session = PersistenceManager.getRequestDBSession();
                        String queryString = "select f.exchangeRate from "
                                        + AmpCurrencyRate.class.getName()
                                        + " f where (f.toCurrencyCode=:currencyCode) order by f.exchangeRateDate desc limit 1";
                        q = session.createQuery(queryString);
                        q.setString("currencyCode", currencyCode);
                        List rates = q.list();
                        Double result = null;
                        if (rates == null || rates.isEmpty()){
                                logger.debug("No exchange rate value found for currency: "+currencyCode);
                                result = new Double(1.0);
                        }else{
                                result = (Double)rates.iterator().next();
                        }
                        return result.doubleValue();
                } catch (Exception ex) {
                        logger.debug("Unable to get exchange rate from database", ex);
                        throw new AimException("Error retriving currency exchange rate for "+ currencyCode,ex);
                }
        }


	public static String getCurrencyName(Long currencyId) {
		Session session = null;
		String queryString = null;
		Query q = null;

		try {
			session = PersistenceManager.getSession();
			queryString = "select a.currencyCode from "+AmpCurrency.class.getName()+" a where a.ampCurrencyId=:currencyId";
			q = session.createQuery(queryString);
			q.setLong("currencyId", currencyId);
			logger.info("Got currency name : "+q.list().get(0).toString());
			return q.list().get(0).toString();
		}
		catch (Exception ex) {
			logger.error("Unable to get currency name", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return null;
	}

	public static List getAmpCurrency(Long ampActivityId) {
		List currency = null;
		Query q = null;
		Session session = null;
		String queryString = null;

		try {
			session = PersistenceManager.getSession();
			//queryString = " select Progress from " +
			// AmpPhysicalPerformance.class.getName() + " Progress where
			// (Progress.ampActivityId=:ampActivityId )";
			queryString = "select distinct ac.currencyCode from "
					+ AmpCurrency.class.getName()
					+ " ac, "
					+ AmpFundingDetail.class.getName()
					+ " afd, "
					+ AmpFunding.class.getName()
					+ " af where (afd.ampFundingId=af.ampFundingId) and (afd.ampCurrencyId=ac.ampCurrencyId) and (af.ampActivityId=:ampActivityId)";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			currency = q.list();
			Iterator iter = currency.iterator();
			while (iter.hasNext()) {
				iter.next();
				logger.debug("Size :" + currency.size());
				logger.debug("Currency 1:" + (String) currency.get(0));
				logger.debug("Currency 2:" + (String) currency.get(1));

			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			//////System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger
				.debug("Getting funding Executed successfully "
						+ currency.size());
		return currency;

	}

	/*
	 * searching for a currency Rate using the code
	 * added by Govind
	 */
	public static Collection getCurrencyRate(String code) {
		Collection col = new ArrayList();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		try {
			session = PersistenceManager.getSession();

				qryStr = "select curr from " + AmpCurrencyRate.class.getName() + " curr where curr.toCurrencyCode=:code ";
				qry = session.createQuery(qryStr);
				qry.setParameter("code",code,Hibernate.STRING);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return col;
	}
	public static Collection getCurrencyRateByDataSource(Integer id) {
		Collection col = new ArrayList();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		try {
			session = PersistenceManager.getSession();

				qryStr = "select curr from " + AmpCurrencyRate.class.getName() + " curr where curr.dataSource=:id ";
				qry = session.createQuery(qryStr);
				qry.setParameter("id",id,Hibernate.INTEGER);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return col;
	}
	public static Collection getCurrencyRateValues(Long id) {
		Collection col = new ArrayList();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		try {
			session = PersistenceManager.getSession();

				qryStr = "select curr from " + AmpCurrencyRate.class.getName() + " curr where curr.ampCurrencyRateId=:id ";
				qry = session.createQuery(qryStr);
				qry.setParameter("id",id,Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return col;
	}
	/*
	 * For Deleting a Currency...
	 */
	public static void deleteCurrency(String Code) throws Exception {
		/*try
		{
		*/	AmpCurrency ampC = new AmpCurrency();
			ampC = CurrencyUtil.getCurrencyByCode(Code);
			if (ampC != null)
				DbUtil.delete(ampC);
		/*}
		catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		}*/
	}

}

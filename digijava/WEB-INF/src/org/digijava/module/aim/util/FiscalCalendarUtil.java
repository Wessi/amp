package org.digijava.module.aim.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.DateConversion;

public class FiscalCalendarUtil {
	
	private static Logger logger = Logger.getLogger(FiscalCalendarUtil.class);
	
	public static Date getCalendarStartDate(Long id,int year) {
		Date d = null;
		Session session = null;
		
		try {
			session = PersistenceManager.getSession();
			AmpFiscalCalendar fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,
					id);
			
			year += fisCal.getYearOffset().intValue();
			
			String stDate = fisCal.getStartDayNum() + "/" + fisCal.getStartMonthNum() + "/" + year;
			d = DateConversion.getDate(stDate);
		} catch (Exception e) {
			logger.error("Exception from getAmpFiscalCalendar() :" + e.getMessage());
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
		return d;		
	}
	
	public static Date getCalendarEndDate(Long id,int year) {
		Date d = null;
		Session session = null;
		
		try {
			session = PersistenceManager.getSession();
			AmpFiscalCalendar fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,
					id);
			String stDate = fisCal.getStartDayNum() + "/" + fisCal.getStartMonthNum() + "/" + (year + 1);
			d = DateConversion.getDate(stDate);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(d);
			gc.add(Calendar.DATE,-1);
			d = gc.getTime();
		} catch (Exception e) {
			logger.error("Exception from getAmpFiscalCalendar() :" + e.getMessage());
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
		return d;		
	}	
	
	public static AmpFiscalCalendar getAmpFiscalCalendar(Long id) {
		Session session = null;
		AmpFiscalCalendar fisCal = null;
		
		try {
			session = PersistenceManager.getSession();
			fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,
					id);
		} catch (Exception e) {
			logger.error("Exception from getAmpFiscalCalendar() :" + e.getMessage());
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
		return fisCal;
	}
	
	public static int getYear(Long fisCalId,String date) {
		Session session = null;
		int fiscalYr = 0;
		
		try {
			session = PersistenceManager.getSession();
			AmpFiscalCalendar fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,fisCalId);
			int year = DateConversion.getYear(date);
			int stDay = fisCal.getStartDayNum().intValue();
			int stMnt = fisCal.getStartMonthNum().intValue();
			String bsDate = stDay + "/" + stMnt + "/" + year;
			Date baseDate = DateConversion.getDate(bsDate); 
			Date transDate = DateConversion.getDate(date);

			if (transDate.after(baseDate) || transDate.equals(baseDate) ||
					(stDay == 1 && stMnt == 1)) {
				fiscalYr = year;	
			} else {
				fiscalYr = year-1;
			}
			
		} catch (Exception e) {
			logger.error("Exception from getYear() :" + e.getMessage());
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
		return fiscalYr;
	}
	
}
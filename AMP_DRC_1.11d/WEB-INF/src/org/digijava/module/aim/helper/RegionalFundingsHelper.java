/*
 * RegionalFundingsHelper.java
 * Created: 17-Nov-2005
 */

package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;

/**
 * Helper class for RegionalFundings manipulation
 
 * @author priyajith
 */
public class RegionalFundingsHelper {
	
	private static Logger logger = Logger.getLogger(RegionalFundingsHelper.class); 
	
	/**
	 * Generates a collection of RegionalFundings objects
	 * @param ampRegFunds The collection containing AmpRegionalFundingObjects
	 * @param currCode The currency code to which the currency of the fundings 
	 * need to be converted
	 * @param calCode The calendar in which the calendar field of RegionalFunding 
	 * objects need to be converted   
	 * @return
	 */
	public static ArrayList getRegionalFundings(Collection ampRegFunds,
			String currCode,long calCode) {
		
		Iterator itr = ampRegFunds.iterator();
		
		ArrayList temp = new ArrayList();
		while (itr.hasNext()) {
			AmpRegionalFunding regFund = (AmpRegionalFunding) itr.next();
			RegionalFunding rf = new RegionalFunding();
			rf.setRegionId(regFund.getRegion().getAmpRegionId());
			rf.setRegionName(regFund.getRegion().getName());
			int index = -1;
			if (temp.contains(rf) == true) {
				index = temp.indexOf(rf);
				rf = (RegionalFunding) temp.get(index);
			}
			FundingDetail fd = new FundingDetail();
			
			fd.setCurrencyName(regFund.getCurrency().getCurrencyName());
			fd.setCurrencyCode(regFund.getCurrency().getCurrencyCode());
			fd.setTransactionAmount(FormatHelper.formatNumber(regFund.getTransactionAmount().doubleValue()));
			
			String tsDate = DateConversion.ConvertDateToString(regFund.getTransactionDate());
			fd.setTransactionDate(tsDate);
			
			
			fd.setTransactionType(regFund.getTransactionType().intValue());

			Date dt = regFund.getTransactionDate();
			double frmExRt = Util.getExchange(fd.getCurrencyCode(),new java.sql.Date(dt.getTime()));
			double toExRt = Util.getExchange(currCode,new java.sql.Date(dt.getTime()));
			double amt = CurrencyWorker.convert1(regFund.getTransactionAmount().doubleValue(),frmExRt,toExRt);
			fd.setTransactionAmount(FormatHelper.formatNumber(amt));
			fd.setCurrencyCode(currCode);
			fd.setAdjustmentType(regFund.getAdjustmentType().intValue());
			if (fd.getAdjustmentType() == Constants.PLANNED) {
				fd.setAdjustmentTypeName("Planned");
				amt = 0;
			} else if (fd.getAdjustmentType() == Constants.ACTUAL) {
				fd.setAdjustmentTypeName("Actual");
			}
			
			if (fd.getTransactionType() == Constants.COMMITMENT) {
				if (rf.getCommitments() == null) {
					rf.setCommitments(new ArrayList());
				}
				rf.getCommitments().add(fd);
				if (fd.getAdjustmentType() == Constants.ACTUAL) {
					amt += rf.getTotCommitments();
					rf.setTotCommitments(amt);						
				}
			} else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
				if (rf.getDisbursements() == null) {
					rf.setDisbursements(new ArrayList());
				}
				rf.getDisbursements().add(fd);
				if (fd.getAdjustmentType() == Constants.ACTUAL) {
					amt += rf.getTotDisbursements();
					rf.setTotDisbursements(amt);						
				}					
			} else if (fd.getTransactionType() == Constants.EXPENDITURE) {
				if (rf.getExpenditures() == null) {
					rf.setExpenditures(new ArrayList());
				}
				rf.getExpenditures().add(fd);
				if (fd.getAdjustmentType() == Constants.ACTUAL) {
					amt += rf.getTotExpenditures();
					rf.setTotExpenditures(amt);						
				}					
			}
			
			List list = null;
			if (rf.getCommitments() != null) {
				list = new ArrayList(rf.getCommitments());
				Collections.sort(list,FundingValidator.dateComp);
			}
			rf.setCommitments(list);
			list = null;
			if (rf.getDisbursements() != null) {
				list = new ArrayList(rf.getDisbursements());
				Collections.sort(list,FundingValidator.dateComp);
			}
			rf.setDisbursements(list);
			list = null;
			if (rf.getExpenditures() != null) {
				list = new ArrayList(rf.getExpenditures());
				Collections.sort(list,FundingValidator.dateComp);
			}
			rf.setExpenditures(list);
			
			if (index > -1) {
				temp.set(index,rf);
			} else {
				temp.add(rf);
			}
		}		
		
		return temp;
	}
}
/*
 * FundingValidator.java
 * Created : 01-Dec-2005
 */

package org.digijava.module.aim.helper;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;

import org.digijava.module.aim.util.DbUtil;

public class FundingValidator {
	
	private static ArrayList list1;
	private static ArrayList list2;
	
	public static Comparator dateComp = new Comparator() {
		public int compare(Object e1,Object e2) {
			if (e1 instanceof FundingDetail && e2 instanceof FundingDetail) {
				FundingDetail fd1 = (FundingDetail) e1;
				FundingDetail fd2 = (FundingDetail) e2;
				Date dt1 = DateConversion.getDate(fd1.getTransactionDate());
				Date dt2 = DateConversion.getDate(fd2.getTransactionDate());
				if (dt1.equals(dt2)) {
					return fd2.getAdjustmentType() - fd1.getAdjustmentType();
				} else {
					return (dt1.compareTo(dt2));
				}
				
			}
			throw new ClassCastException();
		}
	};
	
	private static void makeCumulativeAmounts() {
		double value = 0;
		for (int i = 0;i < list1.size();i ++) {
			FundingDetail fd = (FundingDetail)list1.get(i);
			String tempAmt = fd.getTransactionAmount().replaceAll(",","");
			double amt = Double.parseDouble(tempAmt);
			
			Date dt = DateConversion.getDate(fd.getTransactionDate());
			double frmExRt = DbUtil.getExchangeRate(fd.getCurrencyCode(),1,dt);
			double toExRt = DbUtil.getExchangeRate("USD",1,dt);
			amt = CurrencyWorker.convert1(amt,frmExRt,toExRt);			
			
			value += amt;
			fd.setTransactionAmount(""+value);
			list1.set(i,fd);				
		}
		
		value = 0;
		for (int i = 0;i < list2.size();i ++) {
			FundingDetail fd = (FundingDetail)list2.get(i);
			String tempAmt = fd.getTransactionAmount().replaceAll(",","");
			double amt = Double.parseDouble(tempAmt);
			
			Date dt = DateConversion.getDate(fd.getTransactionDate());
			double frmExRt = DbUtil.getExchangeRate(fd.getCurrencyCode(),1,dt);
			double toExRt = DbUtil.getExchangeRate("USD",1,dt);
			amt = CurrencyWorker.convert1(amt,frmExRt,toExRt);			
			
			value += amt;
			fd.setTransactionAmount(""+value);
			list2.set(i,fd);
		}
	}
	
	private static FundingDetail getFundDetail(Date date) {
		for (int i = list1.size() -1 ;i >= 0;i --) {
			FundingDetail fd = (FundingDetail) list1.get(i);
			Date dt = DateConversion.getDate(fd.getTransactionDate());
			if (dt.before(date)) return fd;
		}
		return null;
	}	
	
	private static int validate() {
		for (int i = 0;i < list2.size();i ++) {
			FundingDetail fd = (FundingDetail) list2.get(i);
			Date dt = DateConversion.getDate(fd.getTransactionDate());
			FundingDetail temp = getFundDetail(dt);
			
			if (temp != null) {
				double tempAmt = Double.parseDouble(temp.getTransactionAmount());
				double fdAmt = Double.parseDouble(fd.getTransactionAmount());
				
				if (tempAmt < fdAmt) {
					return (i+1);
				}
			} else {
				return (i+1);
			}
		}
		return -1;
	}	
	
	public static int validateFundings(Collection col1,Collection col2) {
		if (col2 == null || col2.size() == 0) return -1;
		ArrayList temp = new ArrayList();
		if (col1 != null) {
			temp = new ArrayList(col1);
		}
		list1 = new ArrayList();
		for (int i = 0;i < temp.size();i ++) {
			FundingDetail fd = (FundingDetail) temp.get(i);
			FundingDetail newFd = new FundingDetail();
			newFd.setCurrencyCode(fd.getCurrencyCode());
			newFd.setTransactionAmount(fd.getTransactionAmount());
			newFd.setTransactionDate(fd.getTransactionDate());
			if (fd.getAdjustmentType() == Constants.ACTUAL) {
				list1.add(newFd);
			}
		}

		temp = new ArrayList(col2);
		list2 = new ArrayList();
		for (int i = 0;i < temp.size();i ++) {
			FundingDetail fd = (FundingDetail) temp.get(i);
			FundingDetail newFd = new FundingDetail();
			newFd.setCurrencyCode(fd.getCurrencyCode());
			newFd.setTransactionAmount(fd.getTransactionAmount());
			newFd.setTransactionDate(fd.getTransactionDate());
			list2.add(newFd);
		}		
		
		Collections.sort(list1,dateComp);
		Collections.sort(list2,dateComp);
		makeCumulativeAmounts();
		return validate();
	}
}

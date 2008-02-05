/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 *
 */
public class IPAContractDisbursement implements Serializable {
	private static final long serialVersionUID = -4688757182074104911L;
	private Long id;
	private Integer adjustmentType;
	private Double amount;
	private AmpCurrency currency;
	private Date date;
        private IPAContract contract;

        public IPAContract getContract() {
            return contract;
        }

        public void setContract(IPAContract contract) {
            this.contract = contract;
        }
        
        public String getDisbDate() {
        String disbDate = "";
        try {
            if (date != null) {
                disbDate = DateTimeUtil.parseDateForPicker2(date);
            }
        } catch (Exception ex) {
            Logger.getLogger(IPAContractDisbursement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return disbDate;
    }
        
         public void setDisbDate(String date){
        try {
            this.date = DateTimeUtil.parseDateForPicker(date);
        } catch (Exception ex) {
            Logger.getLogger(IPAContractDisbursement.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

        public String getCurrCode() {
            String currCode="";
            if(currency!=null){
                currCode=currency.getCurrencyCode();
            }
            return currCode;
        }

        public void setCurrCode(String currCode) {
           currency= CurrencyUtil.getCurrencyByCode(currCode);
        }
	public Integer getAdjustmentType() {
		return adjustmentType;
	}
	public void setAdjustmentType(Integer adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public AmpCurrency getCurrency() {
		return currency;
	}
	public void setCurrency(AmpCurrency currency) {
		this.currency = currency;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}

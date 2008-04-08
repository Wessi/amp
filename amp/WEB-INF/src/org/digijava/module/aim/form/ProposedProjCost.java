package org.digijava.module.aim.form;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.FormatHelper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ProposedProjCost extends ActionForm{

    private String currencyCode;
    private String funAmount;
    private String funDate;

    public String getFunAmount() {
        return funAmount;
    }

    public Double getFunAmountAsDouble() {
        return FormatHelper.parseDouble(funAmount);
    }

    public String getFunDate() {
        return funDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setFunDate(String funDate) {
        this.funDate = funDate;
    }

    public void setFunAmount(String funAmount) {
    	this.funAmount = funAmount;
    }

    public void setFunAmountAsDouble(Double funAmount) {
            this.funAmount =  FormatHelper.formatNumber(funAmount);
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}

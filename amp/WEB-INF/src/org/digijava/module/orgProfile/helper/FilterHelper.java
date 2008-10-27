/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.orgProfile.helper;

import java.io.Serializable;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.orgProfile.form.OrgProfileFilterForm;

/**
 *
 * @author medea
 */
public class FilterHelper implements Serializable{
    private Long orgId;
    private Long currId;
    private Long year;
    private int transactionType;

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }
    
    public FilterHelper(OrgProfileFilterForm form){
        this.orgId=form.getOrg();
        this.currId=form.getCurrency();
        this.year=form.getYear();
        this.transactionType=form.getTransactionType();
    }

    public Long getCurrId() {
        return currId;
    }

    public void setCurrId(Long currId) {
        this.currId = currId;
    }


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
    
    public AmpOrganisation getOrganization(){
        return DbUtil.getOrganisation(orgId);
        
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.orgProfile.form;

import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.springframework.beans.BeanWrapperImpl;

/**
 *
 * @author medea
 */
public class OrgProfileFilterForm extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    private Long year;
    private Long currencyId;
    private Boolean workspaceOnly;
    private List<AmpCurrency>currencies;
    private List<AmpOrganisation>organizations;
    private Collection<BeanWrapperImpl> years;
    private int transactionType;
    private List<AmpOrgGroup> orgGroups;
    private Long orgGroupId;
    private List<AmpFiscalCalendar> fiscalCalendars;
    private Long fiscalCalendarId;
    private Long[] orgIds;
    public List<AmpFiscalCalendar> getFiscalCalendars() {
        return fiscalCalendars;
    }

    public void setFiscalCalendars(List<AmpFiscalCalendar> fiscalCalendars) {
        this.fiscalCalendars = fiscalCalendars;
    }

    public Long getFiscalCalendarId() {
        return fiscalCalendarId;
    }

    public void setFiscalCalendarId(Long fiscalCalendarId) {
        this.fiscalCalendarId = fiscalCalendarId;
    }

    public Long getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(Long orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public List<AmpOrgGroup> getOrgGroups() {
        return orgGroups;
    }

    public void setOrgGroups(List<AmpOrgGroup> orgGroups) {
        this.orgGroups = orgGroups;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }
    

    public List<AmpCurrency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<AmpCurrency> currencies) {
        this.currencies = currencies;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currency) {
        this.currencyId = currency;
    }

  

    public List<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Collection<BeanWrapperImpl> getYears() {
        return years;
    }

    public void setYears(Collection<BeanWrapperImpl> years) {
        this.years = years;
    }
    
     public Boolean getWorkspaceOnly() {
        return workspaceOnly;
    }

    public void setWorkspaceOnly(Boolean workspaceOnly) {
        this.workspaceOnly = workspaceOnly;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        workspaceOnly=false;
        setOrgIds(null);  
    }

    public String getOrgGroupName() {
        AmpOrgGroup group= DbUtil.getAmpOrgGroup(orgGroupId);
        String name=null;
        if(group!=null){
            name=group.getOrgGrpName();
        }
        return name;
    }

    public String getOrgsName() {
        String name = "";
        if (orgIds != null) {
            for (Long id : orgIds) {
                AmpOrganisation organization = DbUtil.getOrganisation(id);
                name += organization.getName() + ", ";
            }
            if (name.length() > 0) {
                name = name.substring(0, name.length() - 1);
            }
        }
        return name;

    }
      public String getCurrencyCode() {
        String name = "USD";
        if (currencyId != null && currencyId != -1) {
            AmpCurrency curr = CurrencyUtil.getAmpcurrency(currencyId);
            name = curr.getCurrencyCode();
        }
        return name;

    }
     public Long[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Long[] orgIds) {
        this.orgIds = orgIds;
    }

}
	
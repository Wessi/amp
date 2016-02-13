/*
 * UpdateCurrency.java
 */

package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import java.util.ArrayList;

public class UpdateCurrency extends Action {

    private static Logger logger = Logger.getLogger(UpdateCurrency.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        try {

            CurrencyForm crForm = (CurrencyForm) form;
            crForm.setCloseFlag("false");
            crForm.setErrors(new ArrayList());
            Collection errors =crForm.getErrors();

            AmpCurrency curr = null;

            if (crForm.getCurrencyCode() != null &&
                !crForm.getCurrencyCode().equals("") &&
                crForm.getAllCurrencies()!=null &&
                crForm.getDoAction().equalsIgnoreCase("show")) {

                Iterator itr = crForm.getAllCurrencies().iterator();
                while (itr.hasNext()) {
                    curr = (AmpCurrency) itr.next();
                    if (curr.getCurrencyCode().equals(crForm.getCurrencyCode())) {
                        if (curr.getCountryId() != null) {
                            crForm.setCountryId(curr.getCountryId().getCountryId());
                            crForm.setCountryName(curr.getCountryId().getCountryName());
                        }
                        crForm.setCurrencyName(curr.getCurrencyName());
                        crForm.setId(curr.getAmpCurrencyId());
                        break;
                    }
                }
            } else if(crForm.getDoAction().equalsIgnoreCase("new")){
                crForm.setId(new Long( -1));
                crForm.setCountryName(null);
                crForm.setCountryId(Long.valueOf( -1));
                crForm.setCurrencyCode(null);
                crForm.setCurrencyName(null);
                crForm.setExchangeRate(null);
                crForm.setExchangeRateDate(null);
            }

            Collection<CountryBean> countries = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);
            crForm.setCountries(countries);

            if (crForm.getCurrencyCode() != null && crForm.getDoAction().equals("add")) {
                curr = CurrencyUtil.getCurrencyByCode(crForm.getCurrencyCode());
                if (curr != null) {
                    errors.add("Currency with same code alrady exists");
                    return mapping.findForward("forward");
                }else{
                    curr = new AmpCurrency();
                }

                if (crForm.getCountryId() == null ||
                    crForm.getCountryId().equals(Long.valueOf( -1))) {
                    return mapping.findForward("forward");
                } else {
                    Country cn = DbUtil.getDgCountry(crForm.getCountryId());
                    if (cn != null) {
                        curr.setCountryId(cn);
                    }
                }

                saveCurr(curr,crForm);
                crForm.setCloseFlag("true");
                return mapping.findForward("forward");

            } else if (crForm.getDoAction().equals("new")) {
                crForm.setDoAction("add");
            } else if (crForm.getCurrencyCode() != null && crForm.getDoAction().equals("edit")) {
                curr = CurrencyUtil.getCurrencyByCode(crForm.getCurrencyCode());
                if (curr == null) {
                    curr = new AmpCurrency();
                }

                if (crForm.getCountryId() == null ||
                    crForm.getCountryId().equals(Long.valueOf( -1))) {
                    return mapping.findForward("forward");
                } else {
                    Country cn = DbUtil.getDgCountry(crForm.getCountryId());
                    if (cn != null) {
                        curr.setCountryId(cn);
                    }
                }

                saveCurr(curr,crForm);
                crForm.setCloseFlag("true");
                return mapping.findForward("forward");
            } else if (crForm.getCurrencyCode() != null && crForm.getDoAction().equals("show")){
                crForm.setDoAction("edit");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return mapping.findForward("forward");
    }

    private void saveCurr(AmpCurrency curr, CurrencyForm crForm) {
        curr.setCurrencyCode(crForm.getCurrencyCode());
        curr.setCurrencyName(crForm.getCurrencyName());
        curr.setAmpCurrencyId(crForm.getId());
        curr.setActiveFlag(new Integer(1));
        AmpCurrencyRate cRate = new AmpCurrencyRate();
        if (crForm.getExchangeRate() != null &&
            crForm.getExchangeRateDate() != null &&
            crForm.getExchangeRateDate().trim().length() > 0) {
            cRate.setExchangeRate(crForm.getExchangeRate());
            cRate.setExchangeRateDate(
                DateConversion.getDate(crForm.getExchangeRateDate()));
            cRate.setToCurrencyCode(crForm.getCurrencyCode());

        }
        CurrencyUtil.saveCurrency(curr, cRate);
        crForm.setCountryName(null);
        crForm.setCountryId(Long.valueOf( -1));
        crForm.setCurrencyCode(null);
        crForm.setCurrencyName(null);
        crForm.setExchangeRate(null);
        crForm.setExchangeRateDate(null);
        crForm.setAllCurrencies(null);
        crForm.setDoAction(null);
        crForm.setId(null);
    }
}

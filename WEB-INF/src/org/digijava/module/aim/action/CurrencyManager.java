package org.digijava.module.aim.action;

import java.util.ArrayList;
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
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class CurrencyManager extends Action {

	private static Logger logger = Logger.getLogger(CurrencyManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		CurrencyForm crForm = (CurrencyForm) form;
			crForm.setPagesToShow(10);
		try {
			int page = crForm.getPage();
			if (page < 1) page = 1;
			String reload = request.getParameter("reload");
			crForm.setCantDelete(false);
			String cantDelete = request.getParameter("cantDelete");
			if (cantDelete != null){
				crForm.setCantDelete(true);
			}

			int sortingValue = crForm.getOrder();
			if (sortingValue == 0) {
				if ((crForm.getAllCurrencies() == null)	|| ((reload != null) && (reload.compareTo("true") == 0))) {
					crForm.setAllCurrencies(CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_CODE));
				}else{
                    crForm.setAllCurrencies(CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_CODE));
                }
			} else if (sortingValue == -1) {
				crForm.setAllCurrencies(CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_CODE));
			} else if (sortingValue == 1) {
				crForm.setAllCurrencies(CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_CODE));
			} else if (sortingValue == 2) {
				crForm.setAllCurrencies(CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_NAME));
			} else if (sortingValue == 3) {
				crForm.setAllCurrencies(CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_COUNTRY_NAME));
			}

            Collection currencies =crForm.getAllCurrencies();

            if(currencies!=null){
                for (Iterator cuIter = currencies.iterator(); cuIter.hasNext(); ) {
                    AmpCurrency cur = (AmpCurrency) cuIter.next();
                    AmpCategoryValueLocations cn=cur.getCountryLocation();
//                    if(cn!=null){
//                        cur.setCountryId(DbUtil.getTranlatedCountry(request,cn));
//                    }
                }
                crForm.setAllCurrencies(currencies);
            }

			boolean filtered = false;
			// filter the records
			ArrayList tempList = new ArrayList();
			Iterator itr = null;
			if (crForm.getFilterByCurrency() != null &&
					crForm.getFilterByCurrency().trim().length() > 0) {
				logger.debug("Filtering based on type ....");
				itr = crForm.getAllCurrencies().iterator();
				AmpCurrency curr = null;
				int type = crForm.getFilterByCurrency().equals("A")? 1 : 0;
				while (itr.hasNext()) {
					curr = (AmpCurrency) itr.next();
					if (curr.getActiveFlag() == null) {
						if (type == 0) {
							tempList.add(curr);
						}
					} else if (curr.getActiveFlag().intValue() == type) {
						tempList.add(curr);
					}
				}
				filtered = true;
			}

			if (!filtered) {
				tempList = new ArrayList(crForm.getAllCurrencies());
	 		}

			if (crForm.getNumRecords() == 0) {
				crForm.setNumRecords(Constants.NUM_RECORDS);
			}

			crForm.setCurrency(new ArrayList());
			int numPages = tempList.size() / crForm.getNumRecords();
			numPages += (tempList.size() % crForm.getNumRecords() != 0) ? 1 : 0;
			if (page > numPages) page = numPages;

			int stIndex = (page - 1) * crForm.getNumRecords();
			int edIndex = page * crForm.getNumRecords();
			if (edIndex > tempList.size()) {
				edIndex = tempList.size();
			}
			Collection pages = null;
			if (numPages > 1) {
				pages = new ArrayList();
			 	for (int i = 0;i < numPages;i ++) {
			 		Integer pageNum = new Integer(i+1);
			 		pages.add(pageNum);
				}
			}
			crForm.setPages(pages);

			logger.debug("templist size = " + tempList.size());
			logger.debug(stIndex + "," + edIndex);
			for (int i = stIndex;i < edIndex;i ++) {
				crForm.getCurrency().add(tempList.get(i));
			}

			crForm.setCurrentPage(new Integer(page));
			crForm.setCountryName(null);
			crForm.setCurrencyCode(null);
			crForm.setCurrencyName(null);
			crForm.setExchangeRate(null);
			crForm.setExchangeRateDate(null);
			crForm.setId(null);

			} catch (Exception e) {
				logger.error(e);
			}

			return mapping.findForward("forward");
	}
}

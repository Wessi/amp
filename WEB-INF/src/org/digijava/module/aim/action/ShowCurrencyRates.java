/*
 * ShowCurrencyRates.java
 * Created: 01-May-2005
 */
package org.digijava.module.aim.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.CurrencyRateForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyRateLoader;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;

public class ShowCurrencyRates extends Action {

	private static final long SEVEN_DAYS = 604800000; // in miliseconds
	// 7 * 24 * 60 * 60 * 1000

	private static Logger logger = Logger.getLogger(ShowCurrencyRates.class);



	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		CurrencyRateForm crForm = (CurrencyRateForm) form;
                Boolean isFromAdminHome=crForm.isClean();



		try {

		if (crForm.getDoAction() != null &&
				crForm.getDoAction().equals("delete")) {
			CurrencyUtil.deleteCurrencyRates(crForm.getSelectedRates());
			crForm.setAllRates(null);
			crForm.setDoAction("");
		}

		if (crForm.getDoAction() != null &&
				crForm.getDoAction().equals("loadRates")) {
			if (crForm.getRatesFile() != null &&
					crForm.getRatesFile().length() > 0) {
				logger.info("File name = " + crForm.getRatesFile());
				Collection currRates = CurrencyRateLoader.getCurrencyRates(
						crForm.getRatesFile());
				CurrencyUtil.saveCurrencyRates(currRates);
				crForm.setDoAction("");
				crForm.setAllRates(null);
			}
		}

        int page = 1;
		String temp = request.getParameter("page");
		if (temp != null) {
			page = Integer.parseInt(temp.trim());
		}



		if (crForm.getFilterByDateFrom() == null
				|| crForm.getFilterByDateFrom().trim().length() == 0||(isFromAdminHome!=null&&isFromAdminHome)) {
//			crForm.setFilterByDateFrom(Constants.CURRENCY_RATE_DEAFULT_END_DATE);//AMP-1421
			SimpleDateFormat sdf= new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
			crForm.setFilterByDateFrom(sdf.format(new Date()));
                        crForm.setClean(false);
		}

        if((!(crForm.getFilterByDateFrom().equals(crForm.getPrevFromDate()))) || crForm.getAllRates() == null) {
            crForm.setPrevFromDate(crForm.getFilterByDateFrom());
            Date toDate = DateConversion.getDate(crForm.getFilterByDateFrom());
            long stDt = toDate.getTime();
            stDt -= SEVEN_DAYS;
            Date fromDate = new Date(stDt);
            crForm.setAllRates(CurrencyUtil.getActiveRates(fromDate, toDate));
        } else {
            //crForm.setAllRates(CurrencyUtil.getAllActiveRates());
        }

		ArrayList tempList = new ArrayList();
		Iterator itr = null;

		boolean filtered = false;
		if (crForm.getFilterByCurrCode() != null &&
				crForm.getFilterByCurrCode().trim().length() > 0)  {
			logger.debug("Filtering based on currency code ....");
			itr = crForm.getAllRates().iterator();
			CurrencyRates cRates = null;
			while (itr.hasNext()) {
				cRates = (CurrencyRates) itr.next();
				if (cRates.getCurrencyCode().equals(
						crForm.getFilterByCurrCode())) {
					tempList.add(cRates);
				}
			}
			filtered = true;
		}

		if (!filtered) {
			tempList = new ArrayList(crForm.getAllRates());
		}

		if (crForm.getNumResultsPerPage() == 0) {
			crForm.setNumResultsPerPage(Constants.NUM_RECORDS);
		}

        if(tempList.size() > 0) {
            int numPages = tempList.size() / crForm.getNumResultsPerPage();
            numPages += (tempList.size() % crForm.getNumResultsPerPage() != 0) ? 1 : 0;
            if(page > numPages)
                page = numPages;

            int stIndex = (page - 1) * crForm.getNumResultsPerPage();
            int edIndex = page * crForm.getNumResultsPerPage();
            if(edIndex > tempList.size()) {
                edIndex = tempList.size();
            }
            Collection pages = null;
            if(numPages > 1) {
                pages = new ArrayList();
                for(int i = 0; i < numPages; i++) {
                    Integer pageNum = new Integer(i + 1);
                    pages.add(pageNum);
                }
            }
            crForm.setPages(pages);

            crForm.setCurrencyRates(new ArrayList());
            for(int i = stIndex; i < edIndex; i++) {
                crForm.getCurrencyRates().add(tempList.get(i));
            }
        } else {
            crForm.setCurrencyRates(null);
        }

		crForm.setCurrentPage(new Integer(page));
		crForm.setCurrencyCodes(CurrencyUtil.getAmpCurrency());

		crForm.setUpdateCRateAmount(null);
		crForm.setUpdateCRateCode(null);
		crForm.setUpdateCRateDate(null);
		crForm.setUpdateCRateId(null);

		} catch (Exception e) {
			logger.error("Exception " + e);
			e.printStackTrace(System.out);
		}
		return mapping.findForward("forward");
	}

}


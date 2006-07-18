/*
 * UpdateCurrencyRate.java
 * Created : 03-May-2005
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyRateForm;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;


public class UpdateCurrencyRate extends Action {
	private static Logger logger = Logger.getLogger(UpdateCurrencyRate.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		CurrencyRateForm crForm = (CurrencyRateForm) form;
		CurrencyRates currencyRates = null;
		Collection col = new ArrayList();
		
		logger.debug("Reset :" + crForm.isReset());
		
		/*	
		 * 	Starts here
		 * 	Reads the data from the uploaded CSV file
		 * 	the data should be in the following format
		 * 	the 3-letter Currency code,the exchange rate,
		 * 	the date of the exchange rate in dd/mm/yyyy format
		 * 	all these three data in a single line - example :
		 * 	JPY,105.0,10/07/2006
		 *	CAD,1.1264,10/07/2006
		 *	....goes on
		 */
		if (crForm.getDoAction() == null ||
				crForm.getDoAction().equals("file")) 
		{
			FormFile f = crForm.getCurrRateFile();
			InputStream is = f.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringTokenizer st = null;
			
			while ((line = in.readLine()) != null)
			{
				st = new StringTokenizer(line,",");
				if(st.countTokens()==3)
				{
					String code = st.nextToken().trim();
					double rate = Double.parseDouble(st.nextToken().trim());
					String date = st.nextToken().trim();
					
					currencyRates = new CurrencyRates();
					currencyRates.setCurrencyCode(code);
					currencyRates.setExchangeRate(new Double(rate));
					currencyRates.setExchangeRateDate(date);
					
					col.add(currencyRates);
				}
			}
			CurrencyUtil.saveCurrencyRates(col);

			return mapping.findForward("forward");
		}
		/*	
		 * 	Ends here
		 */
		if (crForm.getDoAction() == null ||
				crForm.getDoAction().equals("showRates")) {
			if (crForm.getUpdateCRateCode() != null && 
					crForm.getUpdateCRateDate() != null) {
				Date date = DateConversion.getDate(crForm.getUpdateCRateDate());
				crForm.setUpdateCRateAmount(CurrencyUtil.getExchangeRate(
						crForm.getUpdateCRateCode(),date).toString());
				crForm.setReset(false);
			} else {
				crForm.setUpdateCRateAmount(null);
			}
			if (crForm.getCurrencyCodes() == null) {
				crForm.setCurrencyCodes(CurrencyUtil.getAmpCurrency());
				crForm.setUpdateCRateCode(null);
				crForm.setUpdateCRateDate(null);
				crForm.setUpdateCRateId(null);				
			}

		} else {
			AmpCurrencyRate cRate = new AmpCurrencyRate();
			Double rate = new Double(
					Double.parseDouble(crForm.getUpdateCRateAmount()));
			cRate.setExchangeRate(rate);
			cRate.setExchangeRateDate(
					DateConversion.getDate(crForm.getUpdateCRateDate()));
			cRate.setToCurrencyCode(crForm.getUpdateCRateCode());
			CurrencyUtil.saveCurrencyRate(cRate);
			
			crForm.setAllRates(null);
			crForm.setUpdateCRateAmount(null);
			crForm.setUpdateCRateCode(null);
			crForm.setUpdateCRateDate(null);
			crForm.setUpdateCRateId(null);
			crForm.setDoAction(null);
		}
		return mapping.findForward("forward");
	}
}
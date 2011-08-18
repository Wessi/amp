/*
 * UpdateCurrencyRate.java
 * Created : 03-May-2005
 */
package org.digijava.module.aim.action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyRateForm;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;


public class UpdateCurrencyRate extends Action {

	private static final long SEVEN_DAYS = 604800000; // in miliseconds
	// 7 * 24 * 60 * 60 * 1000

	private static Logger logger = Logger.getLogger(UpdateCurrencyRate.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		CurrencyRateForm crForm = (CurrencyRateForm) form;
		CurrencyRates currencyRates = null;
		Collection<CurrencyRates> col = new ArrayList<CurrencyRates>();
		
		String baseCurrency				= FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY );
        if ( baseCurrency == null )
      	  baseCurrency			= "USD";

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
				try {
							FormFile f = crForm.getCurrRateFile();
							InputStream is = f.getInputStream();
							BufferedReader in = new BufferedReader(new InputStreamReader(is));
							String line = null;
							StringTokenizer st = null;
				
							while ((line = in.readLine()) != null)
							{
								String separator=FeaturesUtil.getGlobalSettingValue("Default Exchange Rate Separator");
								if(separator==null || "".compareTo(separator)==0)
									st = new StringTokenizer(line,",");
								else st = new StringTokenizer(line, separator);
								if(st.countTokens()==3)
								{
									String code = st.nextToken().trim();
									double rate = FormatHelper.parseDouble(st.nextToken().trim());
									String date = st.nextToken().trim();
									currencyRates = new CurrencyRates();
									currencyRates.setCurrencyCode(code);
									currencyRates.setExchangeRate(new Double(rate));
									//DateTimeUtil.parseDate(date).toString();
									currencyRates.setExchangeRateDate(date);
									col.add(currencyRates);
								} 
							}
							CurrencyUtil.saveCurrencyRates(col, baseCurrency);
				
							Date toDate = DateConversion.getDate(crForm.getFilterByDateFrom());
							long stDt = toDate.getTime();
							stDt -= SEVEN_DAYS;
							Date fromDate = new Date(stDt);
							crForm.setAllRates(CurrencyUtil.getActiveRates(fromDate,toDate));
		
					//return mapping.findForward("fileload");
					} catch (Exception e) {
								// TODO: handle exception
						ActionMessages errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"error.aim.uploadCurrencyRates.fileCorrupted"));
						HttpSession httpSession = request.getSession();
						httpSession.setAttribute("CurrencyRateFileUploadError", errors);
						//saveErrors(request, errors);
						//e.printStackTrace();
						}
				}
                  /*
                   * 	Ends here
                   */
                  else{
                    if (crForm.getDoAction() == null ||
                        crForm.getDoAction().equals("showRates")) {
                      if (crForm.getUpdateCRateCode() != null &&
                          crForm.getUpdateCRateDate() != null) {
                        Date date = DateConversion.getDate(crForm.getUpdateCRateDate());
                        Double rate=Util.getExchange(crForm.getUpdateCRateCode(),
                        		new java.sql.Date(date.getTime()));
                       
                        String amount="0";
                        if((rate!=null)&&(rate!=0)){
                            //amount=FormatHelper.formatNumber(1/rate);
                        	amount=FormatHelper.formatNumberNotRounded(rate);
                        }
                        crForm.setUpdateCRateAmount(amount);
                        crForm.setReset(false);
                      }
                      else {
                        crForm.setUpdateCRateAmount(null);
                      }
                      if (crForm.getCurrencyCodes() == null) {
                        crForm.setCurrencyCodes(CurrencyUtil.getAmpCurrency());
                        crForm.setUpdateCRateCode(null);
                        crForm.setUpdateCRateDate(null);
                        crForm.setUpdateCRateId(null);
                      }

                    }
                    else {
                      AmpCurrencyRate cRate = new AmpCurrencyRate();
                      if (crForm.getUpdateCRateAmount() != null) {
                    	//String amountRate=DecimalToText.removeCommas(crForm.getUpdateCRateAmount());
                        //Double rate = new Double(Double.parseDouble(crForm.getUpdateCRateAmount()));
                	 //AMP-2600: not use removeCommas because we can use comma as decimal separator
                    	Double amountRate=FormatHelper.parseDouble(crForm.getUpdateCRateAmount());
                    	//Double rate = new Double(Double.parseDouble(amountRate));
                    	Double rate= 0d;
                    	if (amountRate!=null){
                    	    //rate= new Double(1/amountRate);
                    	    rate= new Double(amountRate);
                    	}
                	  
                        cRate.setExchangeRate(rate);
                      }
                      if (crForm.getUpdateCRateDate() != null) {
                    	 cRate.setExchangeRateDate(DateTimeUtil.parseDate(crForm.getUpdateCRateDate())); 
                        //cRate.setExchangeRateDate(DateConversion.getDate(crForm.getUpdateCRateDate()));
                      }

                      cRate.setToCurrencyCode(crForm.getUpdateCRateCode());
                      cRate.setFromCurrencyCode(baseCurrency);
                      cRate.setDataSource(CurrencyUtil.RATE_BY_HAND);
                      if(cRate.getExchangeRate()!=null && cRate.getExchangeRateDate()!=null && crForm.getDoAction().equalsIgnoreCase("saveRate"))
                    	  CurrencyUtil.saveCurrencyRate(cRate);
                      else 
                    	  logger.warn("Either exchange rate or exchange rate date is null");
                      AbstractCache ratesCache = DigiCacheManager.getInstance().getCache(ArConstants.EXCHANGE_RATES_CACHE);
                      ratesCache.clear();

                      crForm.setAllRates(null);
                      crForm.setUpdateCRateAmount(null);
                      crForm.setUpdateCRateCode(null);
                      crForm.setUpdateCRateDate(null);
                      crForm.setUpdateCRateId(null);
                      crForm.setDoAction(null);
                    }
                  }
		return mapping.findForward("forward");
	}

}

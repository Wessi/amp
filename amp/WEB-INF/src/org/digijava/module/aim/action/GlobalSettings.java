package org.digijava.module.aim.action;
/*
* @ author Govind G Dalwani
*/
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.GlobalSettingsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.services.auditcleaner.AuditCleaner;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.currencyrates.CurrencyRatesService;
import org.digijava.module.currencyrates.DailyCurrencyRateSingleton;
import org.digijava.module.aim.helper.CountryBean;

public class GlobalSettings extends Action {
	private static Logger logger 				= Logger.getLogger(GlobalSettings.class);
	private ActionErrors errors					= new ActionErrors();

	private void flushSessionObjects(HttpSession session) {
		session.removeAttribute(ArConstants.REPORTS_FILTER);
	
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		boolean refreshGlobalSettingsCache			= false;
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
					return mapping.findForward("index");
				}
			}
		
		GlobalSettingsForm gsForm = (GlobalSettingsForm) form;
		if(request.getParameter("save")!=null){
			String save = request.getParameter("save");
			logger.info(" this is the action "+save);
			flushSessionObjects(session);
	
			logger.info(" id is "+gsForm.getGlobalId()+"   name is "+gsForm.getGlobalSettingsName()+ "  value is... "+gsForm.getGsfValue());
			dailyCurrencyRatesChanges(gsForm);
			this.updateGlobalSetting(gsForm.getGlobalId(), gsForm.getGsfValue());
			//ActionErrors errors = new ActionErrors();
			auditTrialCleanerChanges(gsForm);
			refreshGlobalSettingsCache	= true;
		}
		
		
		if(request.getParameter("saveAll")!=null){
			flushSessionObjects(session);
			String allValues=gsForm.getAllValues();
			StringTokenizer token=new StringTokenizer(allValues,"&");
			
			while (token.hasMoreTokens()) {
	                    String element = token.nextToken();
	                    String[] nameValue= element.split("=");
	                    this.updateGlobalSetting(Long.parseLong(nameValue[0]), nameValue[1]);
                        }
			
			//this.updateGlobalSetting(gsForm.getGlobalId(), gsForm.getGsfValue());
			//ActionErrors errors = new ActionErrors();
			refreshGlobalSettingsCache	= true;
			dailyCurrencyRatesChanges(null);
			auditTrialCleanerChanges();
		}
		
		Collection col = FeaturesUtil.getGlobalSettings();
		if (refreshGlobalSettingsCache) {
			FeaturesUtil.setGlobalSettingsCache(col);
			FeaturesUtil.logGlobalSettingsCache();
			org.digijava.module.aim.helper.GlobalSettings globalSettings = (org.digijava.module.aim.helper.GlobalSettings) getServlet().getServletContext().getAttribute(Constants.GLOBAL_SETTINGS);
	    	globalSettings.setShowComponentFundingByYear(FeaturesUtil.isShowComponentFundingByYear());
	    	FeaturesUtil.switchLogicInstance();	    	
	     	
	    	ServletContext ampContext = this.getServlet().getServletContext();
			AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
			AmpTemplatesVisibility currentTemplate=FeaturesUtil.getTemplateById(FeaturesUtil.getGlobalSettingValueLong("Visibility Template"));
	    	ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
	    	ampContext.setAttribute("ampTreeVisibility",ampTreeVisibility);
	    	
		}
		gsForm.setGsfCol(col);
		Iterator itr = col.iterator();
		while (itr.hasNext())
		{
			AmpGlobalSettings ampGS = (AmpGlobalSettings)itr.next();
			gsForm.setGlobalSettingType(ampGS.getGlobalSettingsName(), ampGS.getGlobalSettingsPossibleValues());
			
			/**
			 *  Getting the name of the criteria for possible values:
			 *  if v_view_name => the values are taken from the specified view
			 *  if t_type => the value is checked to be of the specified type
			 */
			String possibleValuesTable		= ampGS.getGlobalSettingsPossibleValues();
			Collection possibleValues		= null;
			Map possibleValuesDictionary	= null;
			if ( possibleValuesTable != null && possibleValuesTable.length() != 0 && possibleValuesTable.startsWith("v_") ) {
				possibleValues				= this.getPossibleValues(possibleValuesTable);
				possibleValuesDictionary	= new HashMap();
				Iterator pvIterator			= possibleValues.iterator();

				while (pvIterator.hasNext()) {
					KeyValue keyValue	= (KeyValue) pvIterator.next();
					possibleValuesDictionary.put(keyValue.getKey(), keyValue.getValue());
				}
			}
			gsForm.setPossibleValues( ampGS.getGlobalSettingsName(), possibleValues );
			gsForm.setPossibleValuesDictionary( ampGS.getGlobalSettingsName(), possibleValuesDictionary );
		}
		Collection<CountryBean> countries = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);
		gsForm.setCountryNameCol(countries);

		saveErrors(request, errors);
		return mapping.findForward("viewGS");
	}

	@SuppressWarnings("unchecked")
	private void dailyCurrencyRatesChanges() {
		Collection <AmpGlobalSettings> col = FeaturesUtil.getGlobalSettings();
		boolean update=false;
		String name;
		String value;
		String hour=null;
		for(AmpGlobalSettings amp: col){			
			name =amp.getGlobalSettingsName();
			value = amp.getGlobalSettingsValue();
			if(name.compareToIgnoreCase("Enabled Daily Currency Rates Update")==0
					&& value.compareToIgnoreCase("On")==0){
				update=true;
			}
			if(name.compareToIgnoreCase("Daily Currency Rates Update Hour")==0){
				hour=amp.getGlobalSettingsValue();
			}			
		}
		if(update){
			CurrencyRatesService.startCurrencyRatesService(hour);
		}
		else{
			CurrencyRatesService.stopCurrencyRatesService();
		}
	}
	
	/**
	 * 
	 */
	private void auditTrialCleanerChanges() {
		Collection<AmpGlobalSettings> col = FeaturesUtil.getGlobalSettings();
		boolean update = false;
		String name;
		String value;
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			AmpGlobalSettings ampGls = (AmpGlobalSettings) iterator.next();
			name = ampGls.getGlobalSettingsName();
			value = ampGls.getGlobalSettingsValue();
			if (name.equalsIgnoreCase("Automatic Audit Logger Cleanup")) {
				if ("-1".equalsIgnoreCase(value)) {
					AuditCleaner.getInstance().stop();
				} else {
					if (!AuditCleaner.getInstance().isRunning()) {
						AuditCleaner.getInstance().start();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param gsForm
	 */
	private void auditTrialCleanerChanges(GlobalSettingsForm gsForm){
		if (gsForm.getGlobalSettingsName().compareTo("Automatic Audit Logger Cleanup") ==0){
			if ("-1".equalsIgnoreCase(gsForm.getGsfValue())){
				AuditCleaner.getInstance().stop();
			}else{
				if(!AuditCleaner.getInstance().isRunning()){
					AuditCleaner.getInstance().start();
				}
			}
		}
	}
	
	/**
	 * @param gsForm
	 */
	@SuppressWarnings("unchecked")
	private void dailyCurrencyRatesChanges(GlobalSettingsForm gsForm) {
		if(gsForm==null){
			dailyCurrencyRatesChanges();
		}
		else{
			if(gsForm.getGlobalSettingsName().compareTo("Enabled Daily Currency Rates Update")==0){
				if(gsForm.getGsfValue().compareTo("On")==0){
					Collection<AmpGlobalSettings> ampGSCollection = (Collection<AmpGlobalSettings>)gsForm.getGsfCol();
					for(AmpGlobalSettings ampGS : ampGSCollection)
					{
						int val = ampGS.getGlobalSettingsName().compareTo("Daily Currency Rates Update Hour");
						if(val==0){
							CurrencyRatesService.startCurrencyRatesService(ampGS.getGlobalSettingsValue());
						}
					}
					
				}else{
					CurrencyRatesService.stopCurrencyRatesService();
				}
			}
			if(gsForm.getGlobalSettingsName().compareTo("Daily Currency Rates Update Hour")==0){
				Collection<AmpGlobalSettings> ampGSCollection = (Collection<AmpGlobalSettings>)gsForm.getGsfCol();
				for(AmpGlobalSettings ampGS : ampGSCollection)
				{
					int val = ampGS.getGlobalSettingsName().compareTo("Enabled Daily Currency Rates Update");
					if(val==0){
						if(ampGS.getGlobalSettingsValue().compareTo("On")==0){
							CurrencyRatesService.startCurrencyRatesService(gsForm.getGsfValue());
						}
					}
				}
			}
		}
	}



	private Collection getPossibleValues(String tableName)
	{
		Collection ret 	= new Vector();
		Session session = null;
		String qryStr = null;
		Query qry = null;
		if (tableName == null || tableName.length() == 0)
			return ret;

		try{
				session				= PersistenceManager.getSession();
				Connection	conn	= session.connection();
				Statement st		= conn.createStatement();
				qryStr 				= "select id, value from "+tableName ;
				//qry 				= session.createSQLQuery(qryStr,"kv",KeyValue.class);
				ResultSet rs		= st.executeQuery(qryStr);
				//qry.setString (0, tableName);
				//Iterator iterator 	= session.iterate(qryStr);

				//Collection coll		= qry.list();
				//Iterator iterator 	= coll.iterator();
				while (rs.next()){

					logger.info("Values:" + rs.getString(1) + "," + rs.getString(2) );
					KeyValue keyValue	= new KeyValue( rs.getObject(1)+"", rs.getString(2) );
					ret.add( keyValue );
				}
				conn.close();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ret;
	}

	private void  updateGlobalSetting(Long id, String value) {

		Session session 	= null;
		String qryStr 		= null;
		Query qry 			= null;
		Transaction tx		= null;
		try{
				session					= PersistenceManager.getSession();
				tx						= session.beginTransaction();

				qryStr 					= "select gs from "+ AmpGlobalSettings.class.getName() + " gs where gs.globalId = :id " ;
				qry 					= session.createQuery(qryStr);
				qry.setLong ("id", id.longValue());
				AmpGlobalSettings ags	= (AmpGlobalSettings) qry.list().get(0);

				boolean changeValue		= this.testCriterion(ags, value);

				if (changeValue)
						ags.setGlobalSettingsValue(value);
				tx.commit();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}
	/**
	 *
	 * @param ags the AmpGlobalSettings whos value should be changed
	 * @param value the new value that should be applied
	 * @return true if value is of the specified type (as returned by AmpGlobalSettings.getGlobalSettingsPossibleValues() )
	 */
	private boolean testCriterion (AmpGlobalSettings ags, String value) {
		String criterion		= ags.getGlobalSettingsPossibleValues();
		if ( criterion!=null && criterion.startsWith("t_")  ) {
			if (criterion.equals("t_Integer") || criterion.equals("t_static_range") ){
				try{
					Integer.parseInt(value);
					return true;
				}
				catch(Exception E) { // value is not an integer
					ActionError ae	= new ActionError("error.aim.globalSettings.valueIsNotOfType", criterion.substring(2));
					errors.add("title", ae);
					return false;
				}
			}
			if (criterion.equals("t_Year")||criterion.equals("t_static_year")||criterion.equals("t_static_range")||
					criterion.equals("t_year_default_start")||criterion.equals("t_year_default_end")){
				try{
					int intValue	= Integer.parseInt(value);
					if (intValue!=-1 && (intValue < 1000 || intValue > 2999  ))
						return false;
					return true;
				}
				catch(Exception E) { // value is not a year
					ActionError ae	= new ActionError("error.aim.globalSettings.valueIsNotOfType", criterion.substring(2));
					errors.add("title", ae);
					return false;
				}
			}
			if (criterion.equals("t_Date")){
				try{
					Date testDate	= DateTimeUtil.parseDate(value);
					return true;
				}
				catch(Exception E) { // value is not an Date
					ActionError ae	= new ActionError("error.aim.globalSettings.valueIsNotOfType", criterion.substring(2));
					errors.add("title", ae);
					return false;
				}
			}

		}
		return true;
	}
	
	public static int numOfDaysInMonth(int month) {
		if (month > 0 && month < 13) {
			int maxDays	= 0;
			switch (month) {
				case 1: ;
				case 3: ;
				case 5: ;
				case 7: ;
				case 8: ;
				case 10: ;
				case 12:
					maxDays	= 31;
					break;
				case 4: ;
				case 6: ;
				case 9: ;
				case 11: 
					maxDays = 30;
					break;
				case 2: 
					maxDays = 28;
					break;
			}
			return maxDays;
		}
		return 0;
	}
}

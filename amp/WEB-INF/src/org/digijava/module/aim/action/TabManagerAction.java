/**
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.TabManagerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * @author Alex Gartner
 *
 */
public class TabManagerAction extends Action {
	
	private static Logger logger	= Logger.getLogger(TabManagerAction.class);
	
	public static final Integer MAX_NUM_OF_TABS		= 5;
	
	public static final String GET_SELECTION_ACTION	= "get";
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		TabManagerForm myForm					= (TabManagerForm) form;
		
		if ( request.getParameter(GET_SELECTION_ACTION) != null && "true".equals(request.getParameter(GET_SELECTION_ACTION)) ) {
			try {
				myForm.setDataSuccessful(true);
				return modeGet(mapping, form, request, response);
			}
			catch(Exception e) {
				myForm.setExceptionOccurred(true);
				myForm.setDataException(true);
				myForm.setDataSuccessful(false);
				e.printStackTrace();
			}
		}
		else
			try{
				myForm.setSaveSuccessful(true);
				return modeSave(mapping, form, request, response);
			}
			catch(Exception e) {
				myForm.setExceptionOccurred(true);
				myForm.setSaveException(true);
				myForm.setSaveSuccessful(false);
				e.printStackTrace();
			}
		return mapping.findForward("forward");
	}

	public ActionForward modeGet(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		
		TabManagerForm myForm					= (TabManagerForm) form;
		ArrayList<AmpReports> reports			= new ArrayList<AmpReports>(); 
		myForm.setTabs( reports );
		
		Collection<AmpReports> myReports		= (Collection<AmpReports>) request.getSession().getAttribute( Constants.MY_REPORTS );
		if ( myReports == null ) 
				throw new Exception("No reports found in session");
		
		Iterator<AmpReports> repIter			= myReports.iterator();
		while ( repIter.hasNext() ) {
			 AmpReports report		= repIter.next();
			 if ( report.getDrilldownTab() != null && report.getDrilldownTab() ) {
				 myForm.getTabs().add(report);
			 }
		}
		Collections.sort(reports, 
				new Comparator<AmpReports> () {
					public int compare(AmpReports o1, AmpReports o2) {
						return o1.getName().compareTo( o2.getName() );
					}
				}
			);
		
		TeamMember teamMember					=(TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
		if ( teamMember == null )
			throw new Exception( "TeamMember not found in session" );
		AmpTeamMember ampTeamMember				= TeamUtil.getAmpTeamMember(teamMember.getMemberId());
		Set<AmpDesktopTabSelection> selections	= ampTeamMember.getDesktopTabSelections();
		
		TreeSet<AmpDesktopTabSelection> sortedSelection	= new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator);
		if (selections != null)
			sortedSelection.addAll( selections );
		
		myForm.setTabsId(new Long[MAX_NUM_OF_TABS]);
		for (int i=0; i<MAX_NUM_OF_TABS; i++)
				myForm.getTabsId()[i]=0L;
		
		Iterator<AmpDesktopTabSelection> iter			= sortedSelection.iterator();
		int count										= 0;
		
		while ( iter.hasNext() ) {
			myForm.getTabsId()[count]		= iter.next().getReport().getAmpReportId();
			count ++;
			if ( count == MAX_NUM_OF_TABS ) 
				break;
		}
		
		return mapping.findForward( "forward" );
	}
	
	public ActionForward modeSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		TeamMember teamMember					= (TeamMember)request.getSession().getAttribute( Constants.CURRENT_MEMBER );
		
		if ( teamMember == null )
				throw new Exception( "TeamMember not found in session" );
		
		AmpTeamMember ampTeamMember				= TeamUtil.getAmpTeamMember(teamMember.getMemberId());
		Collection<AmpReports> myReports		= (Collection<AmpReports>) request.getSession().getAttribute( Constants.MY_REPORTS );
		
		if ( myReports == null)
			throw new Exception("No reports found in session");
		
		TabManagerForm myForm						= (TabManagerForm) form;
		ArrayList<AmpDesktopTabSelection> selection	= new ArrayList<AmpDesktopTabSelection>();
		
		if ( myForm.getTabsId()!=null && myForm.getTabsId().length>0 ) {
			int counter	= 0;
			for (int i=0; i<myForm.getTabsId().length; i++) {
				if ( myForm.getTabsId()[i] > 0 ) {
					AmpDesktopTabSelection sel	= new AmpDesktopTabSelection();
					sel.setIndex( counter );
					sel.setOwner(ampTeamMember);
					sel.setReport( getReportById(myReports, myForm.getTabsId()[i]) );
					
					if ( sel.getReport() == null )
						throw new Exception("The requested tab was not found among the reports in session");
					
					selection.add(sel);
					counter++;
				}
			}
			
		}
		Session dbSession 	= PersistenceManager.getSession();
		Transaction tr		= dbSession.beginTransaction();
		try{
			AmpTeamMember atm	= (AmpTeamMember)dbSession.load(AmpTeamMember.class, teamMember.getMemberId() );
			if ( atm.getDesktopTabSelections() == null ) 
				atm.setDesktopTabSelections( new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator) );
			
			atm.getDesktopTabSelections().clear();
			Iterator<AmpDesktopTabSelection> it	= selection.iterator();
			while ( it.hasNext() ) {
				AmpDesktopTabSelection dts	= it.next();
				dbSession.save( dts );
			}
			tr.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		}
		finally {
			try {
				PersistenceManager.releaseSession(dbSession);
				
			} 
			catch (Exception ex) {
				logger.error("releaseSession() failed :" + ex);
				ex.printStackTrace();
			}
		}
		Collection<AmpReports> tabs				= new ArrayList<AmpReports>();
		if ( selection.size() == 0 ) {
			AmpApplicationSettings ampAppSettings 	= DbUtil.getTeamAppSettings(teamMember.getTeamId());
			AmpReports defaultTeamReport 			= ampAppSettings.getDefaultTeamReport();
			if ( defaultTeamReport != null )
				tabs.add( defaultTeamReport );
		}
		else{
			Iterator<AmpDesktopTabSelection> iter	= selection.iterator();
			while ( iter.hasNext() ) {
				tabs.add( iter.next().getReport() );
			}
		}
		request.getSession().setAttribute(Constants.MY_TABS, tabs);	
		
		return mapping.findForward( "forward" );
	}
	
	private static AmpReports getReportById (Collection<AmpReports> reports, Long id) {
			Iterator<AmpReports> iter	= reports.iterator();
			
			while ( iter.hasNext() ) {
				AmpReports rep		= iter.next();
				if ( rep.getAmpReportId().equals( id ) )
						return rep;
			}
			return null;
	}
	
	
}

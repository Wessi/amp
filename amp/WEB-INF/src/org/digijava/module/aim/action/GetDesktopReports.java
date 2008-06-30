package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetDesktopReports extends TilesAction {
	private static Logger logger	= Logger.getLogger( GetDesktopReports.class );
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		
		if (tm != null) {
				Collection reports = new ArrayList();
				//Adding the default team report
				AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
				
				AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
				//ArrayList userReports = TeamMemberUtil.getAllMemberReports(tm.getMemberId());
				ArrayList userReports = (ArrayList) TeamUtil.getAllTeamReports(tm.getTeamId(), null,null, null,true,tm.getMemberId());
				if (defaultTeamReport != null){
					Iterator iter = userReports.iterator();
					boolean found = false;
					while (iter.hasNext()) {
						AmpReports el = (AmpReports) iter.next();
						if (el.compareTo(defaultTeamReport) == 0){
							found = true;
							break;
						}
					}

					if ((userReports != null) && (!found))
						reports.add(defaultTeamReport);
				}
				//After Tanzania: Team Leaders should see all
//				if (tm.getTeamHead() == true) {
//					reports.addAll(TeamUtil.getAllTeamReports(userReports));
//				} else {
					reports.addAll(userReports);
//				}
				Integer reportsPerPage=0;
                Collections.sort((List<AmpReports>) reports, AmpReports.UpdatedDateComparator );
                Collections.reverse((List<AmpReports>) reports);
                                
				session.setAttribute(Constants.MY_REPORTS,reports);
				session.setAttribute(Constants.TEAM_ID,tm.getTeamId());
                                session.setAttribute(Constants.MY_REPORTS_PER_PAGE,reportsPerPage);

                /* Setting Team Members tabs */
                AmpTeamMember ampTeamMember				= TeamUtil.getAmpTeamMember(tm.getMemberId());
                Collection<AmpReports> tabs				= new ArrayList<AmpReports>();
                if ( ampTeamMember.getDesktopTabSelections()==null || ampTeamMember.getDesktopTabSelections().size()==0 ) {
                	if ( defaultTeamReport != null )
                		tabs.add( defaultTeamReport );
                }
                else {
                		TreeSet<AmpDesktopTabSelection> sortedSelection	= 
                			new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator);
                		sortedSelection.addAll( ampTeamMember.getDesktopTabSelections() );
                		Iterator<AmpDesktopTabSelection> iter	= sortedSelection.iterator();
                		while ( iter.hasNext() ) {
                			tabs.add( iter.next().getReport() );
                		}
                }
                session.setAttribute( Constants.MY_TABS , tabs);

				/* Setting default_team_report in session */
				ApplicationSettings appSettings	= tm.getAppSettings();
				if ( appSettings != null ) {
					if ( appSettings.getDefaultAmpReport() != null ) {
						
						AmpReports default_report	=  null;//appSettings.getDefaultAmpReport();
						if(session.getAttribute(Constants.DEFAULT_TEAM_REPORT)!=null)
							default_report=(AmpReports) session.getAttribute(Constants.DEFAULT_TEAM_REPORT);
						else {
							default_report	=  appSettings.getDefaultAmpReport();
							session.setAttribute(Constants.DEFAULT_TEAM_REPORT, default_report);
						}
						if (reports == null)
							reports	= new ArrayList();
						this.addReportToCollection(default_report, reports);
					}
					else
						if(session.getAttribute(Constants.DEFAULT_TEAM_REPORT)!=null)
						{
							AmpReports default_report=(AmpReports) session.getAttribute(Constants.DEFAULT_TEAM_REPORT);
							if (reports == null)
								reports	= new ArrayList();
							this.addReportToCollection(default_report, reports);
						}
						else{
							
						
//						logger.info("!!!!!!!!!!!! The default team report is null");
                                              reportsPerPage=appSettings.getDefReportsPerPage();
                                              if(reportsPerPage==null){
                                                reportsPerPage=0;
                                              }
                                              session.setAttribute(Constants.MY_REPORTS_PER_PAGE,reportsPerPage);
						}
				}
				else
					logger.info("Application settings is null");
				/*END - Setting default_team_report in session*/

				if(tm.getTeamHead()) session.setAttribute(Constants.TEAM_Head,"yes");
					else session.setAttribute(Constants.TEAM_Head,"no");
		} else {
			Collection reports=ARUtil.getAllPublicReports();
			session.setAttribute(Constants.MY_REPORTS,reports);
		}
		return null;
	}

	private void addReportToCollection(AmpReports report, Collection col) {
		Iterator iterator	= col.iterator();
		while (iterator.hasNext()) {
			AmpReports colReport	= (AmpReports) iterator.next();
			if ( colReport.getAmpReportId().longValue() == report.getAmpReportId().longValue() )
				return;
		}

		col.add(report);
	}
}

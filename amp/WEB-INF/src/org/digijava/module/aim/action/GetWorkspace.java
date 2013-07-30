package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.dbentity.AmpTeamFilterData;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetWorkspace extends Action {

	private static Logger logger = Logger.getLogger(GetWorkspace.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In GetWorkspace");
		
		ReportContextData.createWithId(request.getSession(), ReportContextData.REPORT_ID_WORKSPACE_EDITOR, false);
		request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, ReportContextData.REPORT_ID_WORKSPACE_EDITOR);

		boolean permitted = false;
		HttpSession session = request.getSession();
		TeamMember tmember = (TeamMember) session.getAttribute("currentMember");
		////System.out.println("-----------------------------------"+tmember.getTeamHead());
		if(tmember!=null)
			permitted=tmember.getTeamHead();
		if (session.getAttribute("ampAdmin") != null) {
			String key = (String) session.getAttribute("ampAdmin");
			if (key.equalsIgnoreCase("yes")) {
				permitted = true;
			} else {
				if (session.getAttribute("teamLeadFlag") != null) {
					key = (String) session.getAttribute("teamLeadFlag");
					if (key.equalsIgnoreCase("true") || key.equalsIgnoreCase("yes")) {
						permitted = true;	
					}
				}
			}
		}
		if (!permitted) {
			return mapping.findForward("justList");
		}
		String updated=request.getParameter("updated");
		String dest = request.getParameter("dest");
		String id	= request.getParameter("tId");
		String action = request.getParameter("event");
        if (updated == null || !Boolean.parseBoolean(updated)){
            UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
            uwForm.setUpdateFlag(false);
            uwForm.setFmTemplateList(FeaturesUtil.getAMPTemplatesVisibility());

            //uwForm.setOrganizations(DbUtil.getAll(AmpOrganisation.class));

            //ReportContextData.clearSession(); - cleaning session is done in SelectTeam
            // nothing to clean here, as filters etc. are stored on a per-tab basis
            // request.getSession().setAttribute( ReportWizardAction.EXISTING_SESSION_FILTER, null );
            // request.getSession().setAttribute( ReportWizardAction.SESSION_FILTER, null );
            // request.getSession().setAttribute( ArConstants.REPORTS_Z_FILTER, null );

//		/**
//		 * The ReportsFilterPickerForm needs to be cleaned before using in the wizard
//		 */
//		ReportsFilterPicker rfp		= new ReportsFilterPicker();
//		ReportsFilterPickerForm rfpForm	= (ReportsFilterPickerForm)TagUtil.getForm(request, "aimReportsFilterPickerForm");
//		if (rfpForm == null ) {
//			rfpForm		= new ReportsFilterPickerForm();
////			AmpARFilter arf = createOrFillFilter(filterForm, subsection);
////			request.setAttribute(ReportWizardAction.REPORT_WIZARD_INIT_ON_FILTERS, "true");
////			rfp.modePrepare(mapping, rfpForm, request, response);
//			TagUtil.setForm(request, "aimReportsFilterPickerForm", rfpForm, false);
//		}
//		rfpForm.setIsnewreport(true);
//		rfp.reset(rfpForm, request, mapping);
//		rfpForm.setIsnewreport(false);

            if(action!=null && "reset".compareTo(action)==0){
                uwForm.setReset(true);
                uwForm.reset(mapping, request);
                uwForm.setActionEvent("add");
                uwForm.setAddFlag(true);
                uwForm.setFmTemplate(null);
                uwForm.setWorkspacePrefix(null);

                return mapping.findForward("showAddWorkspace");
            }
            logger.debug("dest = " + dest);
            logger.debug("id = " + id);
            Long teamId = new Long(0);
            try {
                if (id != null && id.trim().length() > 0) {
                    teamId = new Long(Long.parseLong(id));
                    if (teamId.longValue() == -1) {
                        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
                        teamId = tm.getTeamId();
                    }
                    if (uwForm.isAddFlag())
                        uwForm.setAddFlag(false);
                } else {
                    // Clearing formbean properties before going to add a team
                    if (!uwForm.isAddFlag()) {
                        uwForm.setReset(true);
                        uwForm.reset(mapping, request);
                        uwForm.setAddFlag(true);
                    }
                    uwForm.setActionEvent("add");
                    return mapping.findForward("showAddWorkspace");
                }
            } catch (NumberFormatException nfe) {
                // incorrect id.
            }

            AmpTeam ampTeam = TeamUtil.getAmpTeam(teamId);
            if (ampTeam != null){
                Set<AmpTeamFilterData> fdSet	= ampTeam.getFilterDataSet();
                if ( fdSet != null && fdSet.size() > 0 ) {
                    ReportContextData.getFromRequest().initFilters(ampTeam);
//				ReportsFilterPickerForm rfpForm2	= (ReportsFilterPickerForm)TagUtil.getForm(request, "aimReportsFilterPickerForm");
//				ReportsFilterPicker.modeRefreshDropdowns(mapping, rfpForm2, request, response, getServlet().getServletContext() );
//				FilterUtil.populateForm(rfpForm2, filter, null);
                    //myForm.setUseFilters(true);
                }
            }

            Workspace workspace = TeamUtil.getWorkspace(teamId);
            if (workspace != null) {
                Long typeId		= new Long(0);
                if ( workspace.getType() != null ) {
                    typeId	= workspace.getType().getId();
                }

                Long groupId = new Long(0);
                if ( workspace.getWorkspaceGroup() != null ) {
                    groupId	= workspace.getWorkspaceGroup().getId();
                }

                uwForm.setTeamId(new Long(workspace.getId()));
                uwForm.setTeamName(workspace.getName());
                uwForm.setCategory(workspace.getTeamCategory());
                uwForm.setTypeId(typeId);
                uwForm.setWorkspaceType(workspace.getWorkspaceType());
                uwForm.setDescription(workspace.getDescription());
                uwForm.setWorkspaceGroup(groupId);
                TreeSet orgs=new TreeSet();
                if(workspace.getChildOrgs()!=null){
                    orgs.addAll(workspace.getChildOrgs());
                }

                if (uwForm.getOrganizations()==null) {
                    uwForm.setOrganizations(new ArrayList());
                }
                else
                    uwForm.getOrganizations().clear();
                //uwForm.getOrganizations().addAll(orgs);
                uwForm.setOrganizations(orgs);
                uwForm.setAddActivity(workspace.getAddActivity());
                uwForm.setComputation(workspace.getComputation());
                uwForm.setUseFilter(workspace.getUseFilter());
                uwForm.setRelatedTeam(workspace.getRelatedTeam());
                uwForm.setTeamAccessType(tmember.getTeamAccessType());
                uwForm.setHideDraftActivities(workspace.getHideDraftActivities() );
                if (workspace.getFmTemplate() != null)
                    uwForm.setFmTemplate(workspace.getFmTemplate().getId());
                else
                    uwForm.setFmTemplate(null);
                if (workspace.getWorkspacePrefix() != null)
                    uwForm.setWorkspacePrefix(workspace.getWorkspacePrefix().getId());
                else
                    uwForm.setWorkspacePrefix(null);


                if (null == uwForm.getRelatedTeam()) {
                    if ("DONOR".equalsIgnoreCase(uwForm.getCategory()) && "Donor".equalsIgnoreCase(uwForm.getWorkspaceType())) {
                        //Collection col = TeamUtil.getAllRelatedTeamsByType(uwForm.getType());
                        Collection col = TeamUtil.getAllRelatedTeams();
                        if (col.size() < 1)
                            uwForm.setRelatedTeamFlag("nil");
                        else {
                            //String uwtype = uwForm.getType();
                            Iterator itr = col.iterator();
                            while (itr.hasNext()) {
                                AmpTeam team 		= (AmpTeam) itr.next();
                                String typeValue	= null;
                                if ( team.getType() != null ) {
                                    typeValue		= team.getType().getValue();
                                }
							
						/*if (uwtype.equalsIgnoreCase(team.getType())) {
								if ("Bilateral".equalsIgnoreCase(uwtype))
									uwForm.getRelatedTeamBilatColl().add(team);
								else if ("Multilateral".equalsIgnoreCase(uwtype))
									uwForm.getRelatedTeamMutilatColl().add(team);
							} */
                                if ( Constants.TEAM_TYPE_BILATERAL.equals( typeValue ) )
                                    uwForm.getRelatedTeamBilatColl().add(team);
                                else if ( Constants.TEAM_TYPE_MULTILATERAL.equals( typeValue ) )
                                    uwForm.getRelatedTeamMutilatColl().add(team);
                            }
                            uwForm.setRelatedTeamBilatCollSize(new Integer(uwForm.getRelatedTeamBilatColl().size()));
                            uwForm.setRelatedTeamFlag("set");
                        }
                    }
                    else{
                        uwForm.setRelatedTeamFlag("nil");
                    }
                }
                else {
                    uwForm.setRelatedTeamFlag("noedit");
                    uwForm.setRelatedTeamName(TeamUtil.getAmpTeam(uwForm.getRelatedTeam()).getName());
                }
                logger.debug("uwForm.getRelatedTeamFlag() : " + uwForm.getRelatedTeamFlag());
                uwForm.setChildWorkspaces(workspace.getChildWorkspaces());
                uwForm.setActionEvent("edit");
            }
        }

        logger.debug("Dest value = " + dest);
        return mapping.findForward(dest);
    }

}

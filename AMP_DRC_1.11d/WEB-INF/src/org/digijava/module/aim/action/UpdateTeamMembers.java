package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateTeamMembers extends Action {

    private static Logger logger = Logger.getLogger(UpdateTeamMembers.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        logger.debug("In update teammembers");
        boolean permitted = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") != null) {
            String key = (String) session.getAttribute("ampAdmin");
            if (key.equalsIgnoreCase("yes")) {
                permitted = true;
            } else {
                if (session.getAttribute("teamLeadFlag") != null) {
                    key = (String) session.getAttribute("teamLeadFlag");
                    if (key.equalsIgnoreCase("true")) {
                        permitted = true;
                    }
                }
            }
        }
        if (!permitted) {
            return mapping.findForward("index");
        }

        TeamMemberForm upForm = (TeamMemberForm) form;
        ActionErrors errors = new ActionErrors();
        AmpTeam ampTeam = TeamUtil.getAmpTeam(upForm.getTeamId());
        if (upForm.getAction() != null
            && upForm.getAction().trim().equals("edit")) {
            logger.debug("In edit team member");
            AmpTeamMember ampMember = null;

            if (upForm.getTeamMemberId() != null) {
                ampMember = TeamUtil.getAmpTeamMember(upForm.getTeamMemberId());
            } else {
                ampMember = new AmpTeamMember();
            }

            //ampMember.setAmpTeamMemId(upForm.getTeamMemberId());
            AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(upForm
                .getRole());
            AmpTeamMemberRoles teamLead = TeamMemberUtil.getAmpTeamHeadRole();
            if (role.getRole().equals(teamLead.getRole())) {
                ActionErrors error = new ActionErrors();
                error.add(ActionErrors.GLOBAL_ERROR,
                          new ActionError(
                              "error.aim.addTeamMember.teamLeadRole"));
            }
            if (role.getRole().equals(teamLead.getRole())) {
                logger.info("team name = " + ampTeam.getName());
                logger.info(" team role = " + upForm.getRole());
                logger.info(" this is the team Id = " + upForm.getTeamId() + " this is the member id = " + upForm.getTeamMemberId());
                if ( (ampTeam.getTeamLead() != null) && (!ampTeam.getTeamLead().getAmpTeamMemId().equals(upForm.getTeamMemberId()))) {
                    upForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());

                    logger.info(" here");
                    String trnKey = "aim:teamLeadAlreadyExist";
                    String msg = CategoryManagerUtil.translate(trnKey, request, Constants.TEAM_LEAD_ALREADY_EXISTS);
                    errors.add(
                        ActionErrors.GLOBAL_ERROR,
                        new ActionError(
                            "error.aim.addTeamMember.teamLeadAlreadyExist", msg));
                    saveErrors(request, errors);

                    return mapping.findForward("forward");
                    //return mapping.getInputForward();

                }

            }
            logger.info(" this is the role.... " + role.getRole());
            ampMember.setAmpMemberRole(role);
            ampMember.setReadPermission(new Boolean(true));
            ampMember.setWritePermission(new Boolean(true));
            ampMember.setDeletePermission(new Boolean(true));
            ampMember.setUser(UserUtils.getUser(upForm.getUserId()));
            ampMember.setAmpTeam(ampTeam);
            Collection col = TeamMemberUtil.getAllMemberAmpActivities(upForm
                .getTeamMemberId());
            Set temp = new HashSet();
            temp.addAll(col);
            ampMember.setActivities(temp);
            logger.info(" updating the team member now....");
            DbUtil.update(ampMember);
            AmpTeamMember ampTeamHead = TeamMemberUtil.getTeamHead(ampTeam
                .getAmpTeamId());
            logger.info(" here finally");

            if (ampTeam == null) {
                logger.debug("ampTeam is null");
            }

            if (ampTeamHead != null) {
                ampTeam.setTeamLead(ampTeamHead);
            } else {
                ampTeam.setTeamLead(null);
            }
            DbUtil.update(ampTeam);

            if (ampTeam != null) {
                request.setAttribute("teamId", ampTeam.getAmpTeamId());
                return mapping.findForward("forward");
            }
        } else if (upForm.getAction() != null
                   && upForm.getAction().trim().equals("delete")) {
            logger.debug("In delete team member");
            Long selMembers[] = new Long[1];
            selMembers[0] = upForm.getTeamMemberId();
            Site site = RequestUtils.getSite(request);
            //TeamMemberUtil.updateAppSettingDefReport(selMembers[0]);
            TeamMemberUtil.removeTeamMembers(selMembers, site.getId());

            if (ampTeam != null) {
                request.setAttribute("teamId", ampTeam.getAmpTeamId());
                return mapping.findForward("forward");
            }
        }
        return mapping.findForward("forward");
    }
}

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.security.auth.Subject;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.request.Site;

public class SelectTeam extends Action {

    private static Logger logger = Logger.getLogger(Login.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        HttpSession session = request.getSession();
        LoginForm lForm = (LoginForm) form;
        String temp = request.getParameter("id");

        try {
            Long id = new Long(Long.parseLong(temp));
            AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(id);

            // ----------------------
            User user = RequestUtils.getUser(request);
            Site site = RequestUtils.getSite(request);
            Subject subject = RequestUtils.getSubject(request);
            if (subject == null) {
                subject = UserUtils.getUserSubject(user);
            }
            boolean badmin = DgSecurityManager.permitted(subject, site,
                    ResourcePermission.INT_ADMIN);
            if (badmin == true) {
                session.setAttribute("ampAdmin", new String("yes"));
            } else {
                session.setAttribute("ampAdmin", new String("no"));
            }
            // -----------------------------

           



            AmpTeamMemberRoles lead = TeamMemberUtil
                    .getAmpTeamHeadRole();

            TeamMember tm = new TeamMember();

            if (lead != null) {
                if (lead.getAmpTeamMemRoleId().equals(
                        member.getAmpMemberRole().getAmpTeamMemRoleId()) ||
                        //very ugly but we have no choice - only one team head role possible :(
                        member.getAmpMemberRole().getRole().equals("Top Management")
                        ) {
                    session.setAttribute("teamLeadFlag", new String("true"));
                    tm.setTeamHead(true);
                } else {
                    session.setAttribute("teamLeadFlag", new String("false"));
                    tm.setTeamHead(false);
                }
            } else {
                session.setAttribute("teamLeadFlag", new String("false"));
                tm.setTeamHead(false);
            }

            AmpApplicationSettings ampAppSettings = DbUtil
                    .getMemberAppSettings(member.getAmpTeamMemId());
            ApplicationSettings appSettings = new ApplicationSettings();
            appSettings.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
            appSettings.setDefRecsPerPage(ampAppSettings.getDefaultRecordsPerPage()
                    .intValue());
            appSettings.setCurrencyId(ampAppSettings.getCurrency()
                    .getAmpCurrencyId());
            if (ampAppSettings.getFiscalCalendar() == null) {
                logger.info("AmpAppSettings.getFisCal is null");
            } else {
                logger.info("AmpAppSettings.getFisCal is not null");
                if (ampAppSettings.getFiscalCalendar().getAmpFiscalCalId() == null) {
                    logger.info("AmpAppSettings.getFisCal.id is null");
                } else {
                    logger.info("AmpAppSettings.getFisCal.id is not null");
                }
            }
            appSettings.setFisCalId(ampAppSettings.getFiscalCalendar()
                    .getAmpFiscalCalId());
            appSettings.setLanguage(ampAppSettings.getLanguage());
            appSettings.setPerspective(ampAppSettings.getDefaultPerspective());
            appSettings.setDefaultAmpReport(ampAppSettings.getDefaultTeamReport());


            tm.setMemberId(member.getAmpTeamMemId());
            tm.setMemberName(member.getUser().getName());
            tm.setRoleId(member.getAmpMemberRole().getAmpTeamMemRoleId());
            tm.setRoleName(member.getAmpMemberRole().getRole());
            tm.setTeamId(member.getAmpTeam().getAmpTeamId());
            session.setAttribute(Constants.TEAM_ID, tm.getTeamId());
            tm.setTeamName(member.getAmpTeam().getName());
            tm.setTeamType(member.getAmpTeam().getTeamCategory());
            tm.setRead(member.getReadPermission().booleanValue());
            tm.setWrite(member.getWritePermission().booleanValue());
            tm.setDelete(member.getDeletePermission().booleanValue());
            tm.setAppSettings(appSettings);
            tm.setEmail(member.getUser().getEmail());

            if (DbUtil.isUserTranslator(member.getUser().getId()) == true) {
                tm.setTranslator(true);
            } else {
                tm.setTranslator(false);
            }
            session.setAttribute("currentMember", tm);
            PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
            session.setMaxInactiveInterval(-1);
            lForm.setLogin(true);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return mapping.findForward("forward");
    }
}

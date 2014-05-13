package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.UserDetailForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class ViewUserProfile
        extends Action {

    private static Logger logger = Logger.getLogger(ViewUserProfile.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        ActionMessages errors = new ActionMessages();

        UserDetailForm formBean = (UserDetailForm) form;
        HttpSession httpSession = request.getSession();
        
        User user = null;
        Long userid = null;
        String email = "";
        
        TeamMember teamMember = (TeamMember) httpSession.getAttribute("currentMember");       
        AmpTeamMember member = null;
        List<TeamMember> memberInformationn = new ArrayList<TeamMember>();
        
        if (request.getParameter("id") != null) {
            long id = Long.parseLong(request.getParameter("id"));
            userid = new Long(id);
        }
        if (request.getParameter("email") != null && request.getParameter("email") != "") {
            email = request.getParameter("email");
            user= DbUtil.getUser(email);
            if(user==null){
            	 errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.invalidUserIdLogs"));
                 saveErrors(request, errors);
                 return mapping.findForward("forward");
            }
            else{
            	userid = user.getId();
            }
            
        }
        
        if (httpSession.getAttribute("ampAdmin") == null || httpSession.getAttribute("ampAdmin").equals("no")) {
            
            if(user != null) member = TeamMemberUtil.getAmpTeamMember(user);
            else if(userid != null) member = TeamMemberUtil.getAmpTeamMember(userid);

            
            if (member == null && request.getParameter("id") != null) {
                if (userid.equals(teamMember.getMemberId())) {
                    user = DbUtil.getUser(teamMember.getMemberId());
                    memberInformationn.add(new TeamMember(teamMember.getTeamName(),teamMember.getRoleName()));
                } else {
                	user = DbUtil.getUser(userid);
                	if(user==null){
                		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.invalidUserId"));
                    	saveErrors(request, errors);
                    	mapping.findForward("forward");
                	}
                }
            } else if (member != null) {
//            	user = DbUtil.getUser(member.getUser().getId()); 
//            	if(user.getAssignedOrgId()!=null && user.getOrganizationName() == null) {
//                    AmpOrganisation organization = org.digijava.module.aim.util.DbUtil.getOrganisation(user.getAssignedOrgId());
//                    if(organization != null){
//                    	user.setOrganizationName( organization.getName() );
//                    }
    //
//                }
            	user = DbUtil.getUser(member.getUser().getId());    
                if(user!=null) 
                	memberInformationn = TeamMemberUtil.getMemberInformation(user.getId());
            }
        }else {
        		if (userid!=null) {
        			user= DbUtil.getUser(userid);
        			memberInformationn = TeamMemberUtil.getMemberInformation(user.getId());
        		}
        }


        formBean.setAddress(user.getAddress());
        formBean.setFirstNames(user.getFirstNames());
        formBean.setLastName(user.getLastName());
        formBean.setMailingAddress(user.getEmail());
        formBean.setOrganizationName(user.getOrganizationName());
        formBean.setTeamMemberTeamHelpers(memberInformationn);

        return mapping.findForward("forward");
    }
}

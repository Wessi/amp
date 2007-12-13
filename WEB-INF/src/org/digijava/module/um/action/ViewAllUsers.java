	package org.digijava.module.um.action;
	
	import org.apache.struts.action.*;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import org.digijava.module.um.form.ViewAllUsersForm;
	import java.util.Collection;
	import java.util.Collections;
	import java.util.Iterator;
	import java.util.List;
	import java.util.Vector;
	
	import org.digijava.kernel.user.User;
	import java.util.ArrayList;
	import org.digijava.module.aim.util.DbUtil;
	import java.util.*;
	import org.digijava.module.aim.util.TeamMemberUtil;
	import org.digijava.module.um.util.AmpUserUtil;
	import org.digijava.module.aim.dbentity.AmpOrganisation;
	import org.digijava.module.aim.dbentity.AmpTeamMember;
	import org.digijava.module.aim.helper.UserBean;
	import org.digijava.module.aim.dbentity.AmpTeam;
	import org.digijava.module.aim.util.TeamUtil;
	
	public class ViewAllUsers
	    extends Action {
	    public ActionForward execute(ActionMapping mapping, ActionForm form,
	                                 HttpServletRequest request,
	                                 HttpServletResponse response) throws Exception {
	    	
	    	ViewAllUsersForm vwForm = (ViewAllUsersForm) form;
	    	
	    	if(request.getParameter("reset")!=null && request.getParameter("reset").equals(true)){
	    		vwForm.reset(mapping, request);    		
	    	}
	        
	    	vwForm.setNumResults(vwForm.getTempNumResults());
	        Collection ubCol = getUsers(vwForm,request);
	        vwForm.setSelectedNoLetter(true);
	        String alpha=vwForm.getCurrentAlpha();
	
	        if(ubCol == null) {
	            return mapping.findForward("forward");
	        }
	
	        if(vwForm.getType() == 0) {
	            for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
	                UserBean ub = (UserBean) ubIter.next();
	                if(ub.getTeams() != null) {
	                    ubIter.remove();
	                }
	            }
	
	        } else if(vwForm.getType() == 1) {
	            for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
	                UserBean user = (UserBean) ubIter.next();
	                if(user.getTeams() == null) {
	                    ubIter.remove();
	                }
	            }
	        }
	
	        if(vwForm.getKeyword() != null && ubCol != null) {
	            for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
	                UserBean ub = (UserBean) ubIter.next();
	
	                String firstAndLastName = ub.getFirstNames() + ub.getLastName();
	                if(ub.getEmail().indexOf(vwForm.getKeyword()) == -1 &&
	                   firstAndLastName.indexOf(vwForm.getKeyword()) == -1 ) {
	                	
	                    ubIter.remove();
	                }
	            }
	        }
	        
	        
	        if (ubCol != null && ubCol.size() > 0) {
	            if(alpha == null || alpha.trim().length() == 0){
	          	  if (vwForm.getCurrentAlpha() != null) {
	                    vwForm.setCurrentAlpha(null);
	                  } 
	            }else {
	          	  vwForm.setCurrentAlpha(alpha);
	            }            
	
	
	            String[] alphaArray = new String[26];
	            int i = 0;
	            for (char c = 'A'; c <= 'Z'; c++) {
	              Iterator itr = ubCol.iterator();
	              while (itr.hasNext()) {
	            	  UserBean us = (UserBean) itr.next();
	                if (us.getFirstNames().toUpperCase().indexOf(c) == 0) {
	                  alphaArray[i++] = String.valueOf(c);
	                  break;
	                }
	              }
	            }
	            vwForm.setAlphaPages(alphaArray);
	          }
	          else {
	            vwForm.setAlphaPages(null);
	          }
	        
	        vwForm.setNumResults(vwForm.getTempNumResults());
	        
	        int stIndex = 1;
	        int edIndex = vwForm.getNumResults();
	
	        //If ALL was selected in pagination dropdown
	        if (edIndex < 0) {
	          edIndex = ubCol.size();
	        }
	
	        Vector vect = new Vector();
	        int numPages;
	
	        if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
	          if (edIndex > ubCol.size()) {
	            edIndex = ubCol.size();
	          }
	          vect.addAll(ubCol);
	          numPages = ubCol.size() / vwForm.getNumResults();
	          numPages += (ubCol.size() % vwForm.getNumResults() != 0) ? 1 : 0;
	        }
	        else {
	          if (edIndex > vwForm.getAlphaUsers().size()) {
	            edIndex = vwForm.getAlphaUsers().size();
	          }
	          vect.addAll(vwForm.getAlphaUsers());
	          numPages = vwForm.getAlphaUsers().size() / vwForm.getNumResults();
	          numPages += (vwForm.getAlphaUsers().size() % vwForm.getNumResults() != 0) ? 1 : 0;
	        }
	
	        Collection tempCol = new ArrayList();
	        for (int i = (stIndex - 1); i < edIndex; i++) {
	          tempCol.add(vect.get(i));
	        }
	
	        Collection pages = null;
	
	        if (numPages > 1) {
	          pages = new ArrayList();
	          for (int i = 0; i < numPages; i++) {
	            Integer pageNum = new Integer(i + 1);
	            pages.add(pageNum);
	          }
	        }     
	        
	       
	        
	        vwForm.setUsers(ubCol);
	        vwForm.setPagedUsers(tempCol);
	        vwForm.setPages(pages);
	        vwForm.setCurrentPage(new Integer(1));
	          
	        
	        return mapping.findForward("forward");
	    }
	
	    private Collection<UserBean> getUsers(ViewAllUsersForm vwForm,HttpServletRequest request) {
	    	
	    	vwForm.setAlphaUsers(new ArrayList<UserBean>());
	    	Collection<User> users=null;
	    	 String alpha = vwForm.getCurrentAlpha(); //request.getParameter("alpha");
	    	    if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
	    	    	users = AmpUserUtil.getAllUsers();
	    	    	vwForm.setSelectedNoLetter(true);
	    	    }else if(alpha!=null && !alpha.equals("viewAll")){
	    	    	users=new  ArrayList<User>();
	    	    	Iterator iter=AmpUserUtil.getAllUsers().iterator();
	    	    	while (iter.hasNext()){
	    	    		User us=(User)iter.next();
	    	    		if(us.getFirstNames().toUpperCase().startsWith(alpha)){
	    	    			users.add(us);
	    	    		}
	    	    	}
	    	    	vwForm.setSelectedNoLetter(false);
	    	    }
	    	
	            if(users != null) {
	            List<User> sortedUser = new ArrayList(users);
	            
	            //sorting users
	            if(request.getParameter("sortBy")!=null){
	            	vwForm.setSortBy(request.getParameter("sortBy"));
	            }
	            String sortBy=vwForm.getSortBy();
	            
	           if(sortBy!=null && sortBy.equals("name")){
	        	   Collections.sort(sortedUser, new DbUtil.HelperUserNameComparator());
	           } else if(sortBy!=null && sortBy.equals("email")){
	        	   Collections.sort(sortedUser, new DbUtil.HelperEmailComparator());
	           }	        	   
	           else {
	        	   Collections.sort(sortedUser, new DbUtil.HelperUserNameComparator());
	           }
	           
	
	            Collection<UserBean> ubCol = new ArrayList();
	
	            for(Iterator userIter = sortedUser.iterator(); userIter.hasNext(); ) {
	                User user = (User) userIter.next();
	                if(user != null) {
	                    UserBean ub = new UserBean();
	                    ub.setId(user.getId());
	                    ub.setEmail(user.getEmail());
	                    ub.setFirstNames(user.getFirstNames());
	                    ub.setLastName(user.getLastName());
	                    ub.setBan(user.isBanned());
	
	                    Collection members = TeamMemberUtil.getTeamMembers(user.getEmail());
	                    if(members != null) {
	                        List<AmpTeam> teams = new ArrayList<AmpTeam>();
	                        for(Iterator teamMemberIter = members.iterator(); teamMemberIter.hasNext(); ) {
	                            AmpTeamMember teamMember = (AmpTeamMember) teamMemberIter.next();
	                            if(teamMember != null && teamMember.getAmpTeam() != null) {
	                                teams.add(teamMember.getAmpTeam());
	                            }
	                        }
	                        if(teams != null && teams.size() > 0) {
	                            Collections.sort(teams, new TeamUtil.HelperAmpTeamNameComparator());
	                            ub.setTeams(teams);
	                        }
	                    }
	                    ubCol.add(ub);
	                }
	            }
	            
	            if(alpha!=null && !alpha.equals("viewAll")){
	            	vwForm.setAlphaUsers(ubCol);
	            }
	                        
	            return ubCol;
	        } else {
	            return null;
	        }
	    }
	
	    public ViewAllUsers() {
	    }
	}

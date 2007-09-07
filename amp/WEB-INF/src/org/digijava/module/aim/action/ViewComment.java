/*
 * Created on 28/04/2005
 * @author akashs
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class ViewComment extends Action {

		  private static Logger logger = Logger.getLogger(ViewComment.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

					 logger.debug("In view comment");
					 
					 HttpSession session = request.getSession();

					 // if user is not logged in, forward him to the home page
					 if (session.getAttribute("currentMember") == null)
					 	return mapping.findForward("index");
					 HashMap commentColInSession=new HashMap();
					 if(session.getAttribute("commentColInSession")==null)
					 {
						 commentColInSession=new HashMap();
						 session.setAttribute("commentColInSession",commentColInSession);
					 }
					 else commentColInSession=(HashMap)session.getAttribute("commentColInSession");

					 EditActivityForm editForm = (EditActivityForm) form;
					 String action = editForm.getActionFlag();
					 if(action==null ||action.equals("") )
					 {
						 action="create";
						 editForm.setActionFlag("create");
					 }
					 String comment = request.getParameter("comment");
					 
					 TeamMember member = (TeamMember) request.getSession().getAttribute("currentMember");
					 logger.debug("CommentFlag[before IF] : " + editForm.isCommentFlag());
					 if (comment != null && comment.trim().length() != 0) {
					 	AmpField field = null;
						ArrayList col = new ArrayList();
						//if (editForm.isCommentFlag() == false) {
							if (comment.equals("ccd") || comment.equals("viewccd")){
								editForm.setFieldName("current completion date");
								field = DbUtil.getAmpFieldByName("current completion date");
							}
							else
								if (comment.equals("objAssumption")||comment.equals("viewobjAssumption")){
									editForm.setFieldName("Objective Assumption");
									field = DbUtil.getAmpFieldByName("Objective Assumption");
								}
								else
									if (comment.equals("objVerification")||comment.equals("viewobjVerification")){
										editForm.setFieldName("Objective Verification");
										field = DbUtil.getAmpFieldByName("Objective Verification");
									}
									else
										if (comment.equals("purpAssumption")||comment.equals("viewpurpAssumption")){
											editForm.setFieldName("Purpose Assumption");
											field = DbUtil.getAmpFieldByName("Purpose Assumption");
										}
										else
											if (comment.equals("purpVerification")||comment.equals("viewpurpVerification")){
												editForm.setFieldName("Purpose Verification");
												field = DbUtil.getAmpFieldByName("Purpose Verification");
											}else
												if (comment.equals("resAssumption")||comment.equals("viewresAssumption")){
													editForm.setFieldName("Results Assumption");
													field = DbUtil.getAmpFieldByName("Results Assumption");
												}
												else
													if (comment.equals("resVerification")||comment.equals("viewresVerification")){
														editForm.setFieldName("Results Verification");
														field = DbUtil.getAmpFieldByName("Results Verification");
													}
													else
														if (comment.equals("resObjVerIndicators")||comment.equals("viewresObjVerIndicators")){
															editForm.setFieldName("Results Objectively Verifiable Indicators");
															field = DbUtil.getAmpFieldByName("Results Objectively Verifiable Indicators");
														}
														else
															if (comment.equals("objObjVerIndicators")||comment.equals("viewObjObjVerIndicators")){
																editForm.setFieldName("Objective Objectively Verifiable Indicators");
																field = DbUtil.getAmpFieldByName("Objective Objectively Verifiable Indicators");
															}
															else
																if (comment.equals("purpObjVerIndicators")||comment.equals("viewpurpObjVerIndicators")){
																	editForm.setFieldName("Purpose Objectively Verifiable Indicators");
																	field = DbUtil.getAmpFieldByName("Purpose Objectively Verifiable Indicators");
																}
							editForm.setField(field);
						//}
						 	
						logger.debug("editForm.getCommentsCol().size() [At Start-I]: " + editForm.getCommentsCol().size());
						if (editForm.isEditAct()) {
							if (comment.equals("viewccd")||comment.equals("ccd")
									||comment.equals("viewobjAssumption")||comment.equals("objAssumption")||comment.equals("viewobjVerification")||comment.equals("objVerification")||comment.equals("viewobjObjVerIndicators")||comment.equals("objObjVerIndicators")
									||comment.equals("viewpurpAssumption")||comment.equals("purpAssumption")||comment.equals("viewpurpVerification")||comment.equals("purpVerification")||comment.equals("viewresObjVerIndicators")||comment.equals("resObjVerIndicators")
									||comment.equals("viewresAssumption")||comment.equals("resAssumption")||comment.equals("viewresVerification")||comment.equals("resVerification")||comment.equals("viewpurpObjVerIndicators")||comment.equals("purpObjVerIndicators")) {
								String activityId = request.getParameter("actId");
								Long id = null;
								if (activityId != null && activityId.length() != 0)
									id = new Long(Integer.parseInt(activityId));
								if (id == null)
									id = editForm.getActivityId();
								col = DbUtil.getAllCommentsByField(editForm.getField().getAmpFieldId(),id);
								
							if(!commentColInSession.isEmpty())	//if there was some comments added before in this session...
								
								{
									ArrayList colAux=new ArrayList();
									colAux.addAll(col);
									ArrayList colFromSession=(ArrayList)commentColInSession.get(editForm.getField().getAmpFieldId());
									if(colFromSession!=null)
										if(!colFromSession.isEmpty())
											for(Iterator it=colFromSession.iterator();it.hasNext();)
											{
												AmpComments ampCom=(AmpComments)it.next();
												boolean found=false;
												for(Iterator jt=colAux.iterator();jt.hasNext();)
												{
													AmpComments ampComAux=(AmpComments)jt.next();
													if( ampCom.getComment().equals(ampComAux.getComment()) && ampCom.getCommentDate().compareTo(ampComAux.getCommentDate())==0 )
													{
														found=true;
														continue;
													}
												}
												if(!found) col.add(ampCom);
											}
								}
								editForm.setCommentsCol(col);
								editForm.setCommentFlag(false);
								return mapping.findForward("forward");
							}
							else 
								if (editForm.isCommentFlag() == false) {
									col = DbUtil.getAllCommentsByField(editForm.getField().getAmpFieldId(),editForm.getActivityId());
									editForm.setCommentsCol(col);
								}	
						}
						logger.debug("editForm.getCommentsCol().size() [At Start-II]: " + editForm.getCommentsCol().size());
						 	
						editForm.setActionFlag("create");
						editForm.setAmpCommentId(null);			 // Clearing the ampCommentId property
						if (editForm.getCommentText() != null)	// Clearing the commentText property
							editForm.setCommentText(null);
						//editForm.setSerializeFlag(false);  //To make sure comment(s) not added to database without user's knowledge
						if (comment.equals("ccd")||comment.equals("objAssumption")||comment.equals("objVerification")||comment.equals("purpAssumption")
								||comment.equals("purpVerification")||comment.equals("resAssumption")||comment.equals("resVerification"))
							editForm.setCommentFlag(true);
						logger.debug("CommentFlag[forwarding] : " + editForm.isCommentFlag());
						return mapping.findForward("forward");
					}
					 
					 if ("create".equals(action)) {
					 	AmpComments com = new AmpComments();
						com.setAmpFieldId(editForm.getField());
						if (editForm.getCommentText().trim().equals("") || editForm.getCommentText() == null)
							com.setComment(" ");
						else
							com.setComment(editForm.getCommentText());
						com.setCommentDate(new Date());
						com.setMemberId(TeamMemberUtil.getAmpTeamMember(member.getMemberId()));
						 	
						editForm.getCommentsCol().add(com);  // for setting activityId in saveAvtivity.java
					 	commentColInSession.put(editForm.getField().getAmpFieldId(),editForm.getCommentsCol());
					 	session.setAttribute("commentColInSession",commentColInSession);
						logger.debug("editForm.getCommentsCol().size() [After Create]: " + editForm.getCommentsCol().size());
						editForm.setCommentText("");  	// Clear the commentText
						logger.debug("Comment added");
						return mapping.findForward("forward");
					} 
					 else if ("edit".equals(action)){						
						AmpComments com = (AmpComments) editForm.getCommentsCol().get(Integer.parseInt(request.getParameter("ampCommentId")));
							if (editForm.getCommentText() == null || editForm.getCommentText().trim().length() == 0) {
								logger.debug("Inside IF [EDIT]");
								editForm.setCommentText(com.getComment());
								return mapping.findForward("forward");
							 }
							 else {
							 	if (editForm.getCommentText().trim().equals("") || editForm.getCommentText() == null)
							 		com.setComment(" ");
							 	else
							 		com.setComment(editForm.getCommentText());
							 	
							 	editForm.getCommentsCol().set(Integer.parseInt(request.getParameter("ampCommentId")),com);  // for setting activityId in saveAvtivity.java
							 	commentColInSession.put(editForm.getField().getAmpFieldId(),editForm.getCommentsCol());
							 	session.setAttribute("commentColInSession",commentColInSession);
							 	logger.debug("editForm.getCommentsCol().size() [After Edit]: " + editForm.getCommentsCol().size());
							 	editForm.setCommentText("");  	// Clear the commentText
							 	editForm.setActionFlag("create");  // Clear the actionFlag
							 	logger.debug("Comment updated");
								return mapping.findForward("forward");
							 }
					    } 
					 	 else if ("delete".equals(action)){	
					 	 		editForm.getCommentsCol().remove(Integer.parseInt(request.getParameter("ampCommentId")));
							 	commentColInSession.put(editForm.getField().getAmpFieldId(),editForm.getCommentsCol());
							 	session.setAttribute("commentColInSession",commentColInSession);
					 	 		logger.debug("editForm.getCommentsCol().size() [After Delete]: " + editForm.getCommentsCol().size());
								editForm.setCommentText("");      // Clear the commentText
								editForm.setActionFlag("create");  // Clear the actionFlag
								logger.debug("Comment deleted");
								return mapping.findForward("forward");
							}
					return mapping.findForward("forward");
				}
}
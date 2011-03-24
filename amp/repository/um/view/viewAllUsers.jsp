<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<c:set var="translationBan">
	<digi:trn key="um:confirmBanMsg">Do you really want to ban the user ?</digi:trn>
</c:set>

<c:set var="translationUnban">
	<digi:trn key="um:confirmUnbanMsg">Do you really want to remove the ban ?</digi:trn>
</c:set>

<script language="JavaScript">


function banUser(txt) {
  var ban=confirm("${translationBan}");
  return ban;
  }
  
 function unbanUser(txt) {
  var ban=confirm("${translationUnban}");
  return ban;
  }
  
  
  function searchAlpha(val) {
		     document.umViewAllUsersForm.action = "/um/viewAllUsers.do?currentAlpha="+val;
		     document.umViewAllUsersForm.submit();
			 return true;		
	}
	

</script>



<!--  AMP Admin Logo -->
<!-- jsp:include page="/repositoryteamPagesHeader.jsp" flush="true" /-->
<!-- End of Logo -->


  <table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
    <tr>
      <td align=left valign="top">
        <table cellPadding=5 cellspacing="0" width="100%">
          <tr>
            <!-- Start Navigation -->
            <td height=33 colspan=5>
              <span class=crumb>
                <c:set var="translation">
                  <digi:trn key="um:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="um:AmpAdminHome">
                  Admin Home
                  </digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;

                <digi:trn key="um:users">
                Users
                </digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td bgcolor=#c7d4db align=center colspan=5>
              <digi:errors/> 
              <span style="font-size:12px; font-weight:bold;">
                <digi:trn key="um:viewAllUsers:ListOfUsers">
                List of users
                </digi:trn>
              </span>
            </td>
          </tr>
			<tr>
				<td align="left" colspan=5><!--  please note that this page contains form and you can not nested it inside other form -->
				<jsp:include page="/repository/aim/view/exportTable.jsp" /></td>
			</tr>
			<digi:instance property="umViewAllUsersForm" />
		<digi:context name="digiContext" property="context" />
		<digi:form action="/viewAllUsers.do" method="post">
          <tr style="width:50%;">
          	<c:choose>
          		<c:when test="${umViewAllUsersForm.showBanned}">
          		<td width="160" colspan=5>
		              <digi:trn key="um:viewAllUsers:filter">
		              Filter by:
		              </digi:trn>
		              <html:select property="type" style="font-family:verdana;font-size:11px;" disabled="true">
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:all">
		                  -All-
		                  </digi:trn>
		                </c:set>
		                <html:option value="-1">${translation}</html:option>
		              </html:select>
	              </td>
          		</c:when>
          		<c:otherwise>
	            <td width="160">
		              <digi:trn key="um:viewAllUsers:filter">
		              Filter by:
		              </digi:trn>
		              <html:select property="type" style="font-family:verdana;font-size:11px;" >
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:all">
		                  -All-
		                  </digi:trn>
		                </c:set>
		                <html:option value="-1">${translation}</html:option>
		
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:regisetred">
		                  Registered
		                  </digi:trn>
		                </c:set>
		                <html:option value="0">${translation}</html:option>
		
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:workspaceMembers">
		                  Workspace members
		                  </digi:trn>
		                </c:set>
		                <html:option value="1">${translation}</html:option>	                
		              </html:select>
	              </td>
	              </c:otherwise>
              </c:choose>
              <td width="200">
	              <digi:trn key="um:viewAllUsers:keyword">
	              keyword:
	              </digi:trn>
	              <html:text property="keyword" style="font-family:verdana;font-size:11px;"/>
              </td>
              <td width="100">
              	<digi:trn key="aim:results">Results</digi:trn>&nbsp;
				<html:select property="tempNumResults" styleClass="inp-text">
					<html:option value="10">10</html:option>
					<html:option value="20">20</html:option>
					<html:option value="50">50</html:option>
					<html:option value="-1">ALL</html:option>
				</html:select>
              </td>
			  <td width="260">
	              <c:set var="translation">
	                <digi:trn key="um:viewAllUsers:showButton">
	                Show
	                </digi:trn>
	              </c:set>
	              <input type="submit" value="${translation}"  class="dr-menu" style="font-family:verdana;font-size:11px;" />
            </td>
          </tr>
          <tr>
            <td noWrap width=967 vAlign="top" colspan="7">
              <table width="100%" cellspacing="1" cellspacing="1">
					<tr>
						<td noWrap width=700 vAlign="top">
							<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">&nbsp;
										
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
																<digi:trn key="um:users">Users</digi:trn>
                                                              </b>
															</td>
															<!-- end header -->
														</tr>		
 														<tr>
															<td width="100%" class="report">
																<table width="734" border="1" bordercolor="cccccc" RULES=ALL FRAME=VOID  bgColor="#f4f4f2">
																	
																	<c:if test="${empty umViewAllUsersForm.pagedUsers}">
								                                         <tr>
																			<td colspan="5">
				                                                   				<b><digi:trn key="um:viewAllUsers:NoUsers">No users present</digi:trn>
				                                                       			</b>
																			</td>
																		</tr>
							                                        </c:if>
																	<c:if test="${not empty umViewAllUsersForm.pagedUsers}">
																	<thead>
																		<tr>
																			<td height="30" width="220">
																				<digi:link href="/viewAllUsers.do?sortBy=name&reset=false"><b>
																					<digi:trn key="um:viewAllUsers:UsersNames">Name</digi:trn></b>
																				</digi:link>
																			</td>	
																			<td height="30" width="220">
																				<digi:link href="/viewAllUsers.do?sortBy=email&reset=false"><b>
																					<digi:trn key="um:viewAllUsers:UsersEmails">Email</digi:trn></b>
																				</digi:link>
																			</td>																	
																			<td height="30" width="220"><b>
																					<digi:trn key="um:viewAllUsers:UserWorkspace">Workspace</digi:trn></b>
																			</td>
																			<td height="30"width="150" colspan="3" class="ignore"><b>
																				<digi:trn key="aim:viewAllUsers:action">Actions</digi:trn></b>
																			</td>																		
																		</tr>
																		</thead>
																		<tbody class="yui-dt-data">
																	<c:forEach var="us" items="${umViewAllUsersForm.pagedUsers}">
	                                                           			<tr>
		                                                           			<td height="30">
																			  ${us.firstNames}&nbsp;${us.lastName}
																			</td>
																			<td height="30">
																			  	${us.email}																		  
																			</td>																	
																			<td height="30">
																				<div>
								                                                  <c:if test="${!empty us.teamMembers}">
                                                                                                                       <table>
                                                                                                                           <c:forEach var="member" items="${us.teamMembers}">
                                                                                                                               <tr>
                                                                                                                                   <td nowrap><li> ${member.ampTeam.name}&nbsp;(${member.ampMemberRole.role})&nbsp;&nbsp;</li></td>
                                                                                                                               
                                                                                                                               </tr>
                                                                                                                           </c:forEach>
                                                                                                                     </table>
								                                                  </c:if>
								                                                  <c:if test="${empty us.teamMembers}">
								                                                    <digi:trn key="um:viewAllUsers:UnassignedUser">Unassigned</digi:trn>
								                                                  </c:if>
								                                                </div>
																			</td>
																			<td height="30" nowrap="nowrap" class="ignore">
																				<c:set var="translation">
								                                                  <digi:trn key="um:viewAllUsers:EditUserLink">Edit user </digi:trn>
								                                                </c:set>
								                                                <digi:link href="/viewEditUser.do?id=${us.id}">${translation}</digi:link>
																			</td>
																			
																			<td height="30" nowrap="nowrap" class="ignore">
																				<c:choose>
								                                                  <c:when test="${us.ban}">
								                                                    <c:set var="translation">
								                                                      <digi:trn key="um:viewAllUsers:unBanUserLink">Remove ban </digi:trn>
								                                                    </c:set>
								                                                    <digi:link href="/viewEditUser.do?id=${us.id}&ban=false" onclick="return unbanUser()"  >${translation}</digi:link>
								                                                  </c:when>
								                                                  <c:otherwise>
								                                                    <c:set var="translation">
								                                                      <digi:trn key="um:viewAllUsers:banUsersLink">Ban user </digi:trn>
								                                                    </c:set>
								
								                                                    <digi:link href="/viewEditUser.do?id=${us.id}&ban=true" onclick="return banUser()">${translation}</digi:link>
								                                                  </c:otherwise>
								                                                </c:choose>
																			</td>
	                                                            		</tr>
																	</c:forEach>
																	</tbody>
																</c:if>
															</table>
														</td>
													</tr>
												 <!-- end page logic -->
												 
												 <!-- page logic for pagination -->
												<logic:notEmpty name="umViewAllUsersForm" property="pages">
												<tr>
													<td colspan="4" nowrap="nowrap">
														<digi:trn key="um:userPages">Pages :</digi:trn>
														<c:if test="${umViewAllUsersForm.currentPage > 1}">
														<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsFirst}" property="page" value="1"/>
														<c:set var="translation">
															<digi:trn key="aim:firstpage">First Page</digi:trn>
														</c:set>
														
														<digi:link href="/userSearch.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
															&lt;&lt;
														</digi:link>
													
														<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsPrevious}" property="page" value="${umViewAllUsersForm.currentPage -1}"/>
														<c:set var="translation">
															<digi:trn key="aim:previouspage">Previous Page</digi:trn>
														</c:set>
														<digi:link href="/userSearch.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
															&lt;
														</digi:link>
														</c:if>
														<c:set var="length" value="${umViewAllUsersForm.pagesToShow}"></c:set>
														<c:set var="start" value="${umViewAllUsersForm.offset}"/>
														<logic:iterate name="umViewAllUsersForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page"><%=pages%>
														</c:set>
														<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
														<c:if test="${umViewAllUsersForm.currentPage == pages}">
															<font color="#FF0000"><%=pages%></font>
														</c:if>
														<c:if test="${umViewAllUsersForm.currentPage != pages}">
															<c:set var="translation">
															<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
															</c:set>
															<digi:link href="/userSearch.do" name="urlParams1" title="${translation}" >
																<%=pages%>
															</digi:link>
														</c:if>
														|&nbsp;
														</logic:iterate>	
															
														<c:if test="${umViewAllUsersForm.currentPage != umViewAllUsersForm.pagesSize}">
														<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsNext}" property="page" value="${umViewAllUsersForm.currentPage+1}"/>
														<c:set target="${urlParamsNext}" property="orgSelReset" value="false"/>
														<c:set var="translation">
															<digi:trn key="aim:nextpage">Next Page</digi:trn>
														</c:set>
														<digi:link href="/userSearch.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
															&gt;
														</digi:link>
														<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
														<c:if test="${umViewAllUsersForm.pagesSize > aimOrgManagerForm.pagesToShow}">
															<c:set target="${urlParamsLast}" property="page" value="${umViewAllUsersForm.pagesSize-1}"/>
														</c:if>
														<c:if test="${umViewAllUsersForm.pagesSize <umViewAllUsersForm.pagesToShow}">
															<c:set target="${urlParamsLast}" property="page" value="${umViewAllUsersForm.pagesSize}"/>
														</c:if>
														<c:set target="${urlParamsLast}" property="orgSelReset" value="false"/>
														<c:set var="translation">
														<digi:trn key="aim:lastpage">Last Page</digi:trn>
														</c:set>
														<digi:link href="/userSearch.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}">
															&gt;&gt; 
														</digi:link>
													</c:if>
													&nbsp;
													<c:out value="${umViewAllUsersForm.currentPage}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${umViewAllUsersForm.pagesSize}"></c:out>
													</td>
												</tr>
												</logic:notEmpty>
												
												 <logic:notEmpty name="umViewAllUsersForm" property="alphaPages">
											<tr>
												<td align="center" colspan="4">
													<table width="90%">
														<tr>
														    <td>
														    <c:if test="${not empty umViewAllUsersForm.currentAlpha}">
														    	<c:if test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
															    	<c:if test="${umViewAllUsersForm.currentAlpha!=''}">														    	
																    	<c:set var="trnViewAllLink">
																			<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
																		</c:set>
																		<a href="javascript:searchAlpha('viewAll')" title="${trnViewAllLink}">
																				<digi:trn key="aim:viewAllLink">viewAll</digi:trn></a>
																	</c:if>
																</c:if>
														    </c:if>
															
															<logic:iterate name="umViewAllUsersForm" property="alphaPages" id="alphaPages" type="java.lang.String">
															<c:if test="${alphaPages != null}">
																<c:if test="${umViewAllUsersForm.currentAlpha == alphaPages}">
																	<font color="#FF0000"><%=alphaPages %></font>
																</c:if>
																<c:if test="${umViewAllUsersForm.currentAlpha != alphaPages}">
																	<c:set var="translation">
																		<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
																	</c:set>
																	<a href="javascript:searchAlpha('<%=alphaPages%>')" title="${translation}" >
																		<%=alphaPages %></a>
																</c:if>
															|&nbsp;
															</c:if>
															</logic:iterate>
												   </td>
												 </tr>

												</table>
											</td>
										</tr>
										</logic:notEmpty>									
										<logic:notEmpty name="umViewAllUsersForm" property="alphaPages">
											<tr>
												<td bgColor=#f4f4f2>
													<c:if test="${not empty umViewAllUsersForm.currentAlpha}">
														<c:if test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
													   		<c:if test="${umViewAllUsersForm.currentAlpha!=''}">														    	
													    		<digi:trn key="um:UserMan:alphaFilterNote">
																	Click on viewAll to see all existing Users.
																</digi:trn>
															</c:if>
														</c:if>
													</c:if>										
												</td>
											</tr>
										</logic:notEmpty>	
					                         </table>
										</td>
									</tr>
									
								</table>
							</td>
						</tr>
					                    </table>
					                  </td>
									<td noWrap width="100%" vAlign="top">
										<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
											<tr>
												<td>
													<!-- Other Links -->
													<table cellpadding="0" cellspacing="0" width="10"0>
														<tr>
															<td bgColor=#c9c9c7 class=box-title>
																<digi:trn key="aim:otherLinks">
																Other links
																</digi:trn>
															</td>
															<td background="module/aim/images/corner-r.gif" 	height="17" width=17>&nbsp;
															
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table cellPadding=5 cellspacing="1" width="100%">
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
															</td>
															<td>
																<digi:link module="aim"  href="/../um/addUser.do">
											 					<digi:trn key="aim:addNewUser">
																Add new user																</digi:trn>
																</digi:link>
															</td>
														</tr>																								
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
															<td>
																<digi:link module="aim"  href="/admin.do">
																<digi:trn key="aim:AmpAdminHome">
																Admin Home
																</digi:trn>
																</digi:link>
															</td>
														</tr>
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
															<td>
																<digi:link  module="aim" href="/workspaceManager.do~page=1">
																<digi:trn key="aim:WorkspaceManager">
																Workspace Manager
																</digi:trn>
																</digi:link>
															</td>
														</tr>
														<c:choose>
														<c:when test="${umViewAllUsersForm.showBanned}">
															<tr>
																<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=false">
																	<digi:trn key="aim:ViewActiveUsers">
																	View Active Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:when>
														<c:otherwise>
															<tr>
																<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=true">
																	<digi:trn key="aim:ViewBannedUsers">
																	View Banned Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:otherwise>
														</c:choose>
														<!-- end of other links -->
													</table>
												</td>
											</tr>
										</table>
									</td>
				                </tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</digi:form>



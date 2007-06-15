<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ page language="java" import="org.digijava.module.aim.helper.TeamMember" %>

<digi:instance property="aimTeamReportsForm" />
<bean:define id="translation">
	<digi:trn key="aim:confirmDeleteReport">
		Are you sure you want to delete the selected report ?
	</digi:trn>
</bean:define>
<SCRIPT TYPE="text/javascript">
<!--
function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;

  window.open(href,windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');

return false;
}
//-->

function confirmFunc() {
	return confirm('<%= translation %>');
}
</SCRIPT>

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg vAlign=top width=750>
		
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
				<td valign="bottom" class="crumb" >
<bean:define id="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
</bean:define>
                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
                	My Desktop
                </digi:link> &gt; All Reports</td></tr>
				<tr>
		<td>&nbsp;</td></tr>
				<tr>
					<td height=16 align="center" vAlign=center><span class=subtitle-blue>
						<digi:trn key="aim:teamReports">
							Team Reports
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=650 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">	
										<tr>
										<digi:errors/>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=3 cellSpacing=3 class=box-border width="100%" >
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:publicTeamReportsList">
																List of public team reports
															</digi:trn>
															</b>
														</td>
														<!-- 
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:reportDescription">
																Description
															</digi:trn>
															</b>
														</td>
														 -->
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:reportOwnerName">
																Owner
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:reportCreationDate">
																Creation Date
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:reportType">
																Type
															</digi:trn>
															</b>
														</td>
														<% String s = (String)session.getAttribute("teamLeadFlag");
														   TeamMember tm = (TeamMember) session.getAttribute("currentMember");
														 
														   if( tm.getDelete() ){ 
														%>
															<td bgColor=#dddddb align="center" height="20">
																<b>
																<digi:trn key="aim:reportAction">
																	Action
																</digi:trn>
																</b>
															</td>
														<% } %>
													</tr>
																										
													<logic:iterate name="aimTeamReportsForm"  property="reports" id="report" indexId="idx" 
														type="org.digijava.module.aim.dbentity.AmpReports"> 
														<TR bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>">
															<TD>
															<p style="white-space: nowrap">
							                 	 			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewReport">Click here view Report</digi:trn>
															</bean:define>
															
															<digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false" paramName="report"  paramId="ampReportId" paramProperty="ampReportId" styleClass="h-box" onclick="return popup(this,'');">
															<b>
															<bean:write name="report" property="name"/>												
															</b>
															</digi:link> 
																		
															</p>
															<logic:present name="report" property="reportDescription" >
																<p style="max-width: 400px;white-space: normal">
															 		<bean:write name="report" property="reportDescription" />
															 	</p>
														 	</logic:present>
															</TD>
														 	<!-- 
														 	<td>
																<p style="width: 500px;white-space: normal">
														 		<bean:write name="report" property="reportDescription" />
														 		</p>
														 	</td>
														 	 -->
														 	<td>
														 		<p style="white-space: nowrap">
														 		<logic:present name="report" property="ownerId">
														 			 <i><bean:write name="report" property="ownerId.user.name" /></i>
														 			 <br>
														 			 <bean:write name="report" property="ownerId.ampTeam.name" />
														 		</logic:present>
														 		</p>
														 	<td>
														 		<p style="white-space: nowrap">
														 			<logic:present name="report" property="updatedDate">
														 	    		<bean:write name="report" property="formatedUpdatedDate" />
														 	    	</logic:present>
														 	    </p>
														 	</td>
														 	<td>
														 		<p style="white-space: nowrap">
														 		<% 
														 			if (report.getType().equals(new Long(1))) {
														 		%>
														 				donor
														 		<%
														 			}
														 			else
														 				if (report.getType().equals(new Long (2))){
														 		%>
														 					regional
														 		<%
														 				}
														 				else
														 					if (report.getType().equals(new Long(3))){
														 		%>
														 						component
														 		<%
														 					}
														 		%>
														 		</p>
														 	</td>													
														 	
														 	<%
														 	if( tm.getDelete() ){ %>
														
															<td>
																<p style="white-space: nowrap">
																<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams}" property="rid">
																	<bean:write name="report" property="ampReportId" />
																</c:set>
																<c:set target="${urlParams}" property="event" value="edit" />
															[ <digi:link href="/deleteAllReports.do" name="urlParams" title="<%=translation%>" onclick="return confirmFunc()" >
																<digi:trn key="aim:reportDelete">Delete</digi:trn>
															</digi:link> ]												
															[ <digi:link href="/editReport.do" name="urlParams" title="<%=translation%>" >
																<digi:trn key="aim:reportEdit">Edit</digi:trn>
															</digi:link> ]
																</p>
															</td>
															<% } %>
															
														</TR>
														
													</logic:iterate>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>			
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

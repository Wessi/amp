<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlParams}" property="tId" value="-1"/>
<c:set target="${urlParams}" property="dest" value="teamLead"/>			

<TABLE width="750" border="0" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#f4f4f2">
   <TR>
		<TD>
			<TABLE width="100%" cellspacing="1" cellpadding=0 border=0>
			  	<TR><TD bgcolor="#3754a1">
				<DIV id="navlinks">
					<UL>
						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>'>
						<digi:link href="/workspaceOverview.do" name="urlParams">
							:: <digi:trn key="aim:workspaceOverview">Workspace Overview</digi:trn>
						</digi:link></div>
						</LI>

						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToViewActivityList">Click here to view Activity List</digi:trn>'>
						<digi:link href="/teamActivityList.do">
							:: <digi:trn key="aim:activityList">Activity List</digi:trn>
						</digi:link></div>
						</LI>

						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToViewReportList">Click here to view Report List</digi:trn>'>
						<digi:link href="/teamReportList.do"> 
							:: <digi:trn key="aim:reportList">Report List</digi:trn>
						</digi:link>
						</div></LI>
						
						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToViewRelatedLinksList">Click here to view Related Links List</digi:trn>'>
						<digi:link href="/relatedLinksList.do">
							:: <digi:trn key="aim:relatedLinksList">Related Links List</digi:trn>
						</digi:link>
						</div></LI>		

						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>'>
						<digi:link href="/teamMemberList.do">
							:: <digi:trn key="aim:members">Members</digi:trn>
						</digi:link>
						</div></LI>							
					</UL>
				</DIV>
				</TD></TR>
			  	<TR><TD bgcolor="#3754a1">
				<DIV id="navlinks">
					<UL>
						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToCustomizeTeamSettings">Click here to Customize Team Settings</digi:trn>'>
						<digi:link href="/defaultSettings.do">
							:: <digi:trn key="aim:customizedTeamSettings">Customized Team Settings</digi:trn>
						</digi:link>
						</div></LI>
						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Pages</digi:trn>'>
						<digi:link href="/configureTeamPage.do">
							:: <digi:trn key="aim:configureTeamPages">Configure Team Pages</digi:trn>
						</digi:link>
						</div></LI>
						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToViewDonorActivityList">Click here to view donors activity list</digi:trn>'>
						<digi:link href="/donorActivityList.do">
							:: <digi:trn key="aim:donorActivityList">Donor Activity List</digi:trn>
						</digi:link>
						</div></LI>
						<logic:notEmpty name="ME" scope="application">
						<LI>
						<div id="gen" title='<digi:trn key="aim:clickToViewM&E">Click here to view M & E</digi:trn>'>
						<digi:link href="/getTeamActivities.do">
							:: <digi:trn key="aim:m&e">M&E</digi:trn>
						</digi:link>
						</div></LI>
						</logic:notEmpty>
					</UL>		
				</DIV>	
				</TD></TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<%--
<table width="750" cellPadding=0 cellSpacing=1 align="left" valign="top">
	<tr><td height="20">
		<table bgColor=#ffffff align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
			<tr height="20">
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
					</bean:define>

				</td>											
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewActivityList">Click here to view Activity List</digi:trn>
					</bean:define>
					<digi:link href="/teamActivityList.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:activityList">Activity List</digi:trn>
					</digi:link>
				</td>										
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewReportList">Click here to view Report List</digi:trn>
					</bean:define>
					<digi:link href="/teamReportList.do" styleClass="sub-nav" title="<%=translation%>" > 
						:: <digi:trn key="aim:reportList">Report List</digi:trn>
					</digi:link>
				</td>											
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewRelatedLinksList">Click here to view Related Links List</digi:trn>
					</bean:define>
					<digi:link href="/relatedLinksList.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:relatedLinksList">Related Links List</digi:trn>
					</digi:link>
				</td>
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
					</bean:define>
					<digi:link href="/teamMemberList.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:members">Members</digi:trn>
					</digi:link>
				</td>
				<td class="sub-nav" width="112">
					&nbsp;
				</td>				
			</tr>
		</table>	
	</td></tr>
	<tr><td>
		<table bgcolor="#f4f4f2" align="left" valign="top" cellPadding=0 cellspacing=1>
			<tr>
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToCustomizeTeamSettings">Click here to Customize Team Settings</digi:trn>
					</bean:define>
					<digi:link href="/defaultSettings.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:customizedTeamSettings">Customized Team Settings</digi:trn>
					</digi:link>
				</td>	
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Pages</digi:trn>
					</bean:define>
					<digi:link href="/configureTeamPage.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:configureTeamPages">Configure Team Pages</digi:trn>
					</digi:link>
				</td>
				<td noWrap>
					<digi:link href="/donorActivityList.do" styleClass="sub-nav">
						:: <digi:trn key="aim:donorActivityList">Donor Activity List</digi:trn>
					</digi:link>
				</td>			
				<logic:notEmpty name="ME" scope="application">				
				<td noWrap>
					<digi:link href="/getTeamActivities.do" styleClass="sub-nav">
						:: <digi:trn key="aim:m&e">M&E</digi:trn>
					</digi:link>
				</td>
				</logic:notEmpty>
				<td class="sub-nav" width="140">
					&nbsp;
				</td>
			</tr>
		</table>													
	</td></tr>
</table>

--%>

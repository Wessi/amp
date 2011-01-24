<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>




<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<script type="text/javascript">

<!--


	function validate() {
		if (document.aimTeamReportsForm.selReports.checked != null) {
			if (document.aimTeamReportsForm.selReports.checked == false) {
				alert("Please choose a report to add");
				return false;
			}				  
		} else {
			var length = document.aimTeamReportsForm.selReports.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimTeamReportsForm.selReports[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose a report to add");
				return false;					  
			}				  
		}
		return true;			  
	}

-->

</script>


<digi:errors/>
<digi:instance property="aimTeamReportsForm" />
<digi:form action="/updateTeamReports.do" method="post">

<html:hidden property="teamId" />

<table width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33>
						
						<div class="breadcrump_cont">
							<span class="sec_name">
								<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
							</span>
							
							<span class="breadcrump_sep">|</span>
							<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
								<digi:trn key="aim:portfolio">Portfolio</digi:trn>
							</digi:link>
							<span class="breadcrump_sep"><b>�</b></span>
							<c:set var="translation">
								<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
							</c:set>
							<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
							<span class="breadcrump_sep"><b>�</b></span>
							
							<c:set var="translation">
							<digi:trn key="aim:clickToViewReportList">Click here to view Report List</digi:trn>
							</c:set>
							<digi:link href="/teamReportList.do" styleClass="l_sm" title="${translation}" >
							<digi:trn key="aim:reportList">
							Report List
							</digi:trn>
							</digi:link>
							
							<span class="breadcrump_sep"><b>�</b></span>
							
							<span class="bread_sel">
								<c:if test="${aimTeamReportsForm.showReportList == true}">
									<digi:trn key="aim:addReports">
										Add Reports
									</digi:trn>
								</c:if>
								<c:if test="${aimTeamReportsForm.showReportList == false}">
									<digi:trn key="aim:addTabs">
										Add Tabs
									</digi:trn>
								</c:if>
							</span>
						</div>
					
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<c:if test="${aimTeamReportsForm.showReportList == true}">
										<c:set var="selectedTab" value="3" scope="request"/>
									</c:if>
									<c:if test="${aimTeamReportsForm.showReportList == false}">
										<c:set var="selectedTab" value="8" scope="request"/>
									</c:if>	
									<c:set var="selectedSubTab" value="1" scope="request"/>
										
									<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
										<tr>
											<td valign=top>
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">	
										
									<jsp:include page="teamSetupMenu.jsp" flush="true" />								
										
										
										

								
						
									<table class="inside normal" width=100% cellpadding="0" cellspacing="0">
										<tr>
									  	<td width="5" align="center" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
									  		<input type="checkbox" id="checkAll">
									  	</td>
									    <td width=35% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
									    	<b class="ins_title">
									    		<c:if test="${aimTeamReportsForm.showReportList == true}">
														<digi:trn key="aim:reportListUnassignedReports">
															List of unassigned reports
														</digi:trn>
													</c:if>
													<c:if test="${aimTeamReportsForm.showReportList == false}">
														<digi:trn key="aim:reportListUnassignedTabs">
															List of unassigned tabs
														</digi:trn>
													</c:if>
									    	</b>
									    </td>
									    <td width=15% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
									    	<b class="ins_title">
									    		<digi:trn key="aim:reportOwnerName">Owner</digi:trn>
									    	</b>
									    </td>
									    <td width=15% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
									    	<b class="ins_title">
									    		<digi:trn key="aim:reportType">Type</digi:trn>
									    	</b>
									    </td>
									    <td width=15% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
									    	<b class="ins_title">
									    		<digi:trn key="aim:hierarchies">Hierarchies</digi:trn>
									    	</b>
									    </td>
									    <td width=18% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
									    	&nbsp;
									    </td>
										</tr>
										<logic:empty name="aimTeamReportsForm" property="reports">
											<tr>
												<td class=inside>
													<c:if test="${aimTeamReportsForm.showReportList == true}">
														<digi:trn key="aim:noReportsPresent">No reports present</digi:trn>
													</c:if>
													<c:if test="${aimTeamReportsForm.showReportList == false}">
														<digi:trn key="aim:noTabsPresent">No tabs present</digi:trn>
													</c:if>															
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="aimTeamReportsForm" property="reports">
											<logic:iterate name="aimTeamReportsForm" property="reports" id="reports" type="org.digijava.module.aim.dbentity.AmpReports">
													<tr>
														<td width="5" align="center" class=inside>
															<html:multibox property="selReports" >
																<bean:write name="reports" property="ampReportId" />
															</html:multibox>
														</td>
														<td class=inside>
															<bean:write name="reports" property="name" />
														</td>
														<td class=inside>
															<logic:present name="reports" property="ownerId">
                                 <bean:write name="reports" property="ownerId.user.name" />
                              </logic:present>
														</td>
														<td class=inside>
															<li>
                                  <%
                                    if (reports.getType()!=null && reports.getType().equals(new Long(1))) {
                                  %>
                                      <digi:trn key="aim:donorType">donor</digi:trn>
                                <%
                                    }
                                    else if (reports.getType()!=null && reports.getType().equals(new Long (3))){
                                %>
                                      <digi:trn key="aim:regionalType">regional</digi:trn>
                                <%
                                    }
                                    else if (reports.getType()!=null && reports.getType().equals(new Long(2))){
                                %>
                                      <digi:trn key="aim:componentType">component</digi:trn>
                                <%
                                    }
                                    else if (reports.getType()!=null && reports.getType().equals(new Long(4))){
                                %>
                                      <digi:trn key="aim:contributionType">contribution</digi:trn>
                                <%}%>
                            </li>
                              <logic:equal name="reports" property="drilldownTab" value="true">
                                <li>
                                  <digi:trn key="aim:typeDrilldownTab">Desktop Tab</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="publicReport" value="true">
                                <li>
                                  <digi:trn key="aim:typePublicReport">Public Report</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="hideActivities" value="true">
                                <li>
                                  <digi:trn key="aim:typeSummaryReport">Summary Report</digi:trn>
                                </li>
                              </logic:equal>                                  
                              <logic:equal name="reports" property="options" value="A">
                                <li>
                                	<digi:trn key="aim:annualreport">Annual</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="options" value="Q">
                                <li>
                                	<digi:trn key="aim:quarterlyreport">Quarterly</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="options" value="M">
                                <li>
                                	<digi:trn key="aim:monthlyreport">Monthly</digi:trn>	
                                </li>
                              </logic:equal>
														</td>
														<td class=inside>
															<logic:iterate name="reports" property="hierarchies" id="hierarchy" >
																<li>
										            	${hierarchy.column.columnName}
										            </li>
										          </logic:iterate>
														</td>
														<td class=inside>
															<div style='position:relative;display:none;' id='report-<bean:write name="reports" property="ampReportId"/>'> 
                                <logic:iterate name="reports" property="columns" id="column" indexId="index"  >
                                  <%if (index.intValue()%2==0){ %>
                                    <li>                                      
                                    	<digi:trn key="aim:report:${column.column.columnName}">
                                      	<bean:write name="column" property="column.columnName" />
                                    	</digi:trn>
                                  <% } else {%>
                                    ,
                                    	<digi:trn key="aim:report:${column.column.columnName}">
                                      	<bean:write name="column" property="column.columnName" />
                                    	</digi:trn>
                                    </li>
                                  <%} %>
                                </logic:iterate>
                              </div>
                              <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="reports" property="ampReportId"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
                              </span>

                              <div style='position:relative;display:none;' id='measure-<bean:write name="reports" property="measures"/>'> 
                                <logic:iterate name="reports" property="measures" id="measure" indexId="index"  >
                                  <li>
                                  	<digi:trn key="aim:reportBuilder:${measure.measure.aliasName}">                                      
                                    		${measure.measure.aliasName}
                                    	</digi:trn>
                                  </li>
                                </logic:iterate>
                              </div>										                                
                              <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="reports" property="measures"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
                              </span>
														</td>
													</tr>
											</logic:iterate>
										</logic:notEmpty>
										<tr><td colspan="7"><digi:errors /></td></tr>
									</table>
									
									<br>
									<div class="buttons" align="center">
										<c:if test="${aimTeamReportsForm.showReportList == true}">
											<html:submit  styleClass="buttonx_sm btn" property="assignReports"  onclick="return validate()">
												<digi:trn key="btn:addReportsToTheWorkspace">Add Reports to the Workspace</digi:trn> 
											</html:submit>
										</c:if>
										<c:if test="${aimTeamReportsForm.showReportList == false}">
											<html:submit  styleClass="dr-menu" property="assignReports"  onclick="return validate()">
												<digi:trn key="btn:addTabsToTheWorkspace">Add Tabs to the Workspace</digi:trn> 
											</html:submit>
										</c:if>
									</div>										
										
										
								
										
										
										</div>
										</div>											
												
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
</td></tr>
</table>
</digi:form>

<script language="javascript">
		$("#checkAll").bind("change", function (obj){
		$("input[name=selReports]").attr("checked", $("#checkAll").attr("checked"));
	}
	);
</script>

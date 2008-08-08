<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter, java.util.*" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<digi:instance property="aimEditActivityForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<script language="JavaScript">

<!--

function gotoStep(value) {
	document.aimEditActivityForm.step.value = value;
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function backClicked() {
	document.aimEditActivityForm.step.value = "8";
	<digi:context name="backStep" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= backStep %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function disable() {
	document.aimEditActivityForm.submitButton.disabled = true;
	document.aimEditActivityForm.backButton.disabled = true;
	var appstatus = document.aimEditActivityForm.approvalStatus.value;
	var wTLFlag   = document.aimEditActivityForm.workingTeamLeadFlag.value;
	var msg='';
	if (appstatus == "started") {
		msg+='<digi:trn key="aim:saveActivity:started">Do you want to submit this activity for approval ?</digi:trn>';
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
		else if (confirm(msg))
				document.aimEditActivityForm.approvalStatus.value = "created";
	}
	if (appstatus == "approved") {
		msg+='<digi:trn key="aim:saveActivity:approved">Do you want to approve this activity ?</digi:trn>';
		if (wTLFlag != "yes")
			document.aimEditActivityForm.approvalStatus.value = "edited";
	}
	else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			if (confirm(msg))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
	return true;
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

function viewChanges()

{
	openNewWindow(650,200);

	<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />

	popupPointer.document.location.href = "<%= showLog %>?activityId=${aimEditActivityForm.activityId}";

}

function expandAll() {
   
	$("img[@id$='_minus']").show();
	$("img[@id$='_plus']").hide();	
	$("div[@id$='_dots']").hide();
	$("div[@id^='act_']").show('fast');
}

function collapseAll() {

	$("img[@id$='_minus']").hide();
	$("img[@id$='_plus']").show();	
	$("div[@id$='_dots']").show();
	$("div[@id^='act_']").hide();
}
-->

</script>

<%
	Long actId = (Long) request.getAttribute("actId");

	String url = "/aim/viewIndicatorValues.do?ampActivityId="+actId+"&tabIndex=6";
	String actPerfChartFileName=null;
    try{
	  actPerfChartFileName= ChartGenerator.getActivityPerformanceChartFileName(
						 actId,session,new PrintWriter(out),370,450,url,true,request);
    } catch(Exception e)
    {
      e.printStackTrace();
    }
	String actPerfChartUrl = null;
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}

	String actRiskChartFileName = null;
    try{
    
	  actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(
						 actId,session,new PrintWriter(out),370,350,url,request);
    }catch(Exception e){
      e.printStackTrace();
    }
    
	String actRiskChartUrl = null;

	if (actRiskChartFileName != null)  {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}
%>

<digi:context name="digiContext" property="context" />
<digi:form action="/saveActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="approvalStatus" />
<html:hidden property="workingTeamLeadFlag" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<logic:present name="currentMember" scope="session">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
</logic:present>
<tr><td width="100%" vAlign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="780" vAlign="top" align="left" border=0>
	<tr>
		<td class=r-dotted-lg width="10" align="left" vAlign="top">&nbsp;</td>
		<td class=r-dotted-lg align=left vAlign=top>
			<table width="100%" cellSpacing="3" cellPadding="1" vAlign="top" align="left" border=0>
					<tr><td>
					<logic:present name="currentMember" scope="session">
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 2}">
									<c:set var="trnViewMyDesktop">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${trnViewMyDesktop}" >
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;

									<jsp:useBean id="urlParam" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParam}" property="ampActivityId">
										<c:out value="${aimEditActivityForm.activityId}"/>
									</c:set>
									<c:set target="${urlParam}" property="tabIndex" value="0"/>

									<c:set var="trnViewChannelOverview">
										<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>
									</c:set>

									<digi:link href="/viewChannelOverview.do" styleClass="comment" name="urlParam" title="${trnViewChannelOverview}" >
										<digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:previewActivity">
									Preview Activity
									</digi:trn>
								</c:if>

								<c:if test="${aimEditActivityForm.pageId == 1}">
                                                                     
								  <c:forEach var="step" items="${aimEditActivityForm.steps}">
                                                                     
                                                                     <c:set property="translation" var="trans">
                                                                         <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                                                             Click here to goto Add Activity Step ${step.stepActualNumber}
                                                                         </digi:trn>
                                                                     </c:set>
                                                                      
                                                                      <c:set var="link">
                                                                          <c:if test="${step.stepNumber==9}">
                                                                              /editSurveyList.do?edit=true
                                                                              
                                                                         </c:if>
                                                                          
                                                                          <c:if test="${step.stepNumber!=9}">
                                                                          
                                                                              /addActivity.do?step=${step.stepNumber}&edit=true
                                                                              
                                                                          </c:if>
                                                                      </c:set>
                                                                           
                                                                         
                                                                         
                                                                     
                                                                     
                                                                     
                                                                    
                                                                         <c:if test="${index.first}">
                                                                            
                                                                             <digi:link href=" ${link}" styleClass="comment" title="${trans}">
                                                                                 
                                                                                 
                                                                                 <c:if test="${aimEditActivityForm.editAct == true}">
                                                                                     <digi:trn key="aim:editActivityStep1">
                                                                                         Edit Activity - Step 1
                                                                                     </digi:trn>
                                                                                 </c:if>
                                                                                 <c:if test="${aimEditActivityForm.editAct == false}">
                                                                                     <digi:trn key="aim:addActivityStep1">
                                                                                         Add Activity - Step 1
                                                                                     </digi:trn>
                                                                                 </c:if>
                                                                                 
                                                                             </digi:link>
                                                                             &nbsp;&gt;&nbsp;
                                                                         </c:if>
                                                                         <c:if test="${!index.first}">
                                                                             <digi:link href="${link}" styleClass="comment" title="${trans}">
                                                                                 <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
                                                                             </digi:link>
                                                                             &nbsp;&gt;&nbsp;
                                                                         </c:if>
                                                                         
                                                                     
                                                                 </c:forEach>
                                                                 

								
								<digi:trn key="aim:previewActivity">
									Preview Activity
								</digi:trn>
								</c:if>
								</span>
							</td>
						</tr>
					</table>
					</logic:present>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="0" cellPadding="0" vAlign="bottom">
						<tr>
							<td width="50%" align="left">
								<c:if test="${aimEditActivityForm.pageId == 1}">
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="bottom">
									<tr>
										<td height=16 vAlign="bottom" width="100%"><span class=subtitle-blue>
											<c:if test="${aimEditActivityForm.editAct == false}">
												<digi:trn key="aim:addNewActivity">
													Add New Activity
												</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.editAct == true}">
												<digi:trn key="aim:editActivity">
													Edit Activity
												</digi:trn>
											</c:if></span>											
										</td>
									</tr>
								</table>
								</c:if>
							</td>
							<logic:present name="currentMember" scope="session">
							<td width="50%" align="right">
								<table cellSpacing="1" cellPadding="1" vAlign="bottom" border=0>
								
									<tr>
										<td>
											<c:set var="tran"><digi:trn key="aim:previe:expandAll">Expand all</digi:trn> </c:set>
											<input type="button" class="dr-menu" onclick="javascript:expandAll()" value="${tran}"/>
										</td>
										<td>
										<c:set var="tran"><digi:trn key="aim:previe:collapseAll">Collapse all</digi:trn> </c:set>
										<input type="button" class="dr-menu" onclick="javascript:collapseAll()" value="${tran}"/>
										</td>
										<td height=16 vAlign=bottom align="right">
												<input type="button" class="dr-menu" onclick="window.open('/showPrinterFriendlyPage.do?edit=true', '_blank', '');" value="<digi:trn key="aim:print">Print</digi:trn>"> 
										</td>
									</tr>
								</table>
							</td>
							</logic:present>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="100%" vAlign="top">
						<table width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left">
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:previewActivity">
													Preview Activity
												</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td align="center" vAlign="top" bgcolor="#ffffff">
								<table width="100%" cellpadding=3 cellSpacing=1 bgcolor="#dddddd">
							<feature:display name="Identification" module="Project ID and Planning">
								<field:display name="AMP ID" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:ampId">
											 AMP ID</digi:trn>										</td>
										<td class="v-name" bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.ampId}"/>										</td>
									</tr>
								</field:display>
									<field:display name="Contract Number" feature="Planning">
                                        <tr>
	                                        <td align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2"  class="t-name" >                                        
	                                        	<digi:trn key="aim:convenionumcont">Contract Number</digi:trn>                                       	      
	                                        </td>
	                                        <td bgcolor="#FFFFFF">
	                                        	<c:out value="${aimEditActivityForm.convenioNumcont}"/>
	                                        </td>
                                        </tr>
                                    </field:display>
                                    
									<field:display name="Project Title" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:projectTitle">Project title</digi:trn>										</td>
										<td class="v-name"  bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.title}"/>										</td>
									</tr>
									</field:display>
								
                                  
                                  <field:display name="NPD Clasification" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:npdClasification">NPD Clasification</digi:trn>										</td>
										<td class="v-name"  bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.clasiNPD}"/>										
										</td>
									</tr>
									</field:display>
                                  
                                    
                                    <!--Begin Objectives --->
			     					<field:display feature="Identification" name="Objectives">
			      					<field:display feature="Identification" name="Objective">
								 	<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:objectives">Objectives</digi:trn>										
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.objectives!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.objectives}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										
                                        </td>
									</tr>
									</field:display>
									<logic:present name="currentMember" scope="session">
									<tr>
									
									<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
									<field:display feature="Identification" name="Objective Comments">											
									<digi:trn key="aim:objectiveComments"> 
										Objective Comments
									</digi:trn></field:display>								
									</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 <field:display feature="Identification" name="Objective Assumption">
										 	<logic:equal name="comments" property="key" value="Objective Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                         </field:display>
                                         <field:display feature="Identification" name="Objective Verification">
                                        	<logic:equal name="comments" property="key" value="Objective Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:objectiveVerification">Objective Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                          </field:display>
                                        	<field:display feature="Identification" name="Objective Objectively Verifiable Indicators">
                                        	<logic:equal name="comments" property="key" value="Objective Objectively Verifiable Indicators">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:objectivelyVerificationIndicators">Objective Objectively Verifiable Indicators</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	</field:display>
										</logic:iterate>										
										</td>
									</tr>
									</logic:present>
									</field:display>  						
									<!--END Objectives --->
 
						 			<field:display feature="Identification" name="Description">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:description">
											 Description</digi:trn>										</td>
										<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.description!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.description}" />
											<digi:edit key="${descKey}"></digi:edit>
                                            </c:if>										</td>
									</tr>
									</field:display>
									
									<field:display name="Lessons Learned" feature="Identification">
										<TR>
											<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
												<digi:trn key="aim:Lessons Learned">Lessons Learned</digi:trn>
											</td>
											<TD bgcolor="#ffffff">																			
												<c:if test="${not empty aimEditActivityForm.lessonsLearned}">
													<bean:define id="lessonsLearnedKey"><c:out value="${aimEditActivityForm.lessonsLearned}"/></bean:define>
													<digi:edit key="<%=lessonsLearnedKey%>"/>
												 </c:if>
											</TD>
										</TR>																		
									</field:display>

											<bean:define id="largeTextFeature" value="Identification" toScope="request"/>
									
											<logic:present name="aimEditActivityForm" property="projectImpact">
											<bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.projectImpact}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyView.jsp"/>
											</logic:present>
											
											<logic:present name="aimEditActivityForm" property="activitySummary">
											<bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.activitySummary}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyView.jsp"/>
											</logic:present>
											
											<logic:present name="aimEditActivityForm" property="contractingArrangements">
											<bean:define id="largeTextLabel" value="Contracting Arrangements" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.contractingArrangements}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyView.jsp"/>
											</logic:present>
			
											<logic:present name="aimEditActivityForm" property="condSeq">
											<bean:define id="largeTextLabel" value="Conditionality and Sequencing" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.condSeq}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyView.jsp"/>
											</logic:present>
			
											<logic:present name="aimEditActivityForm" property="linkedActivities">
											<bean:define id="largeTextLabel" value="Linked Activities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.linkedActivities}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyView.jsp"/>
											</logic:present>
											
											<logic:present name="aimEditActivityForm" property="conditionality">
											<bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.conditionality}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyView.jsp"/>
											</logic:present>
			
											<logic:present name="aimEditActivityForm" property="projectManagement">
											<bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.projectManagement}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyView.jsp"/>
											</logic:present>
									

 
                                
                            	<!--7 8 9 10 -->	
									<field:display feature="Identification" name="Purpose">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">											
										<digi:trn key="aim:purpose">Purpose</digi:trn>										
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.purpose!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.purpose}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										</td>
									</tr>
									<logic:present name="aimEditActivityForm" property="allComments">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">											
										<digi:trn key="aim:purposeComments">Purpose Comments</digi:trn>										
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Purpose Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Objectively Verifiable Indicators">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeObjectivelyVerifiableIndicators">Purpose Objectively Verifiable Indicators</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>										</td>
									</tr>
									</logic:present>
									</field:display>

									<field:display feature="Identification" name="Results">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">											
										<digi:trn key="aim:results">Results</digi:trn>										
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.results!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.results}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										</td>
									</tr>
									<logic:present name="aimEditActivityForm" property="allComments">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">											
										<digi:trn key="aim:resultsComments">Results Comments</digi:trn>										
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Results Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Objectively Verifiable Indicators">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsObjectivelyVerifiableIndicators">Results Objectively Verifiable Indicators</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>										</td>
									</tr>
									</logic:present>
									</field:display>
									<!--END 7 8 9 10 -->    
                                <!--Begin 11 12  -->   
                                			<field:display name="Accession Instrument" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.accessionInstrument > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.accessionInstrument}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									<field:display name="A.C. Chapter" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:acChapter">A.C. Chapter</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.acChapter > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.acChapter}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
                              <!--END 11 12  -->        

                                <field:display name="Government Agreement Number" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:step1:GovernmentAgreementNumTitle">Budget</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											${aimEditActivityForm.govAgreementNumber}
										</td>
									</tr>
								</field:display>

                               <feature:display name="Budget" module="Project ID and Planning">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:actBudget">Budget</digi:trn>										</td>
										<td bgcolor="#ffffff">

										<logic:equal name="aimEditActivityForm" property="budget" value="true">
										<digi:trn key="aim:actBudgeton">
												Activity is On Budget										</digi:trn>
										</logic:equal>

										<logic:equal name="aimEditActivityForm" property="budget" value="false">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget										</digi:trn>
										</logic:equal>

										<logic:equal name="aimEditActivityForm" property="budget" value="">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget										</digi:trn>
										</logic:equal>										</td>
									</tr>
									</feature:display>
                                   
                                    <!--END 13 -->
                                    		<field:display feature="Identification" name="Organizations and Project ID">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">											<digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs											</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedOrganizations}">
												<table cellSpacing=2 cellPadding=2 border=0>
													<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.selectedOrganizations}" >
														<c:if test="${not empty selectedOrganizations}">
															<tr><td>
																	<c:if test ="${!empty selectedOrganizations.organisation.ampOrgId}">
																			<bean:define id="selectedOrgForPopup" name="selectedOrganizations" 	type="org.digijava.module.aim.helper.OrgProjectId"  	toScope="request" />
																			<jsp:include page="previewOrganizationPopup.jsp"/>
																	</c:if>
															</td></tr>
														</c:if>
													</c:forEach>
												</table>
											</c:if>										</td>
									</tr>
									</field:display>
									<field:display name="Humanitarian Aid" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:humanitarianaid">
											 Humanitarian Aid</digi:trn></td>
										<td bgcolor="#ffffff">
											<c:if test="${!aimEditActivityForm.humanitarianAid==true}">
												<digi:trn key="aim:no">No</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.humanitarianAid==true}">
												<digi:trn key="aim:yes">Yes</digi:trn>
											</c:if>
										</td>
									</tr>
									</field:display>
                                    
                                    <!--15-->
                                    <tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="group_planning_plus"  onclick="toggleGroup('group_planning')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="group_planning_minus" onclick="toggleGroup('group_planning')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display : none"/>
											<digi:trn key="aim:planning">Planning</digi:trn>										</td><td bgcolor="#ffffff">
											<div id="group_planning_dots">...</div>
											<div id="act_group_planning" style="display: none;">
											<table width="100%" cellSpacing=2 cellPadding=1>
												<field:display feature="Planning" name="Line Ministry Rank">
												<tr>
													<td width="32%"><digi:trn key="aim:lineMinRank">
													Line Ministry Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.lineMinRank == -1}">													</c:if>
													<c:if test="${aimEditActivityForm.lineMinRank != -1}">
													${aimEditActivityForm.lineMinRank}													</c:if>													</td>
												</tr>
												</field:display>
												<field:display name="Ministry of Planning Rank" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:planMinRank">
													Ministry of Planning Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.planMinRank == -1}">													</c:if>
													<c:if test="${aimEditActivityForm.planMinRank != -1}">
													${aimEditActivityForm.planMinRank}													</c:if>													</td>
												</tr>
												</field:display>

												<field:display name="Proposed Approval Date" feature="Planning">
												<tr>
													<td width="32%">
														<digi:trn key="aim:proposedApprovalDate">Proposed Approval Date</digi:trn>
													</td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalAppDate}													</td>
												</tr>
												</field:display>
												<field:display name="Actual Approval Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:actualapprovaldate">Actual Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedAppDate}													</td>
												</tr>
												</field:display>
												<field:display name="Proposed Start Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:originalStartDate">Original Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalStartDate}													</td>
												</tr>
												</field:display>
												<field:display name="Final Date for Contracting" feature="Planning">
												<tr>
													<td width="32%">													
													<digi:trn key="aim:ContractingDateofProject1">Final Date for Contracting</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.contractingDate}"/>													</td>
												</tr>
												</field:display>
												<field:display name="Final Date for Disbursements" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:DisbursementsDateofProject1">Final Date for Disbursements</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.disbursementsDate}"/>													</td>
												</tr>
												</field:display>
												<field:display name="Actual Start Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:actualStartDate">Actual Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedStartDate}													</td>
												</tr>
												</field:display>
												<field:display name="Proposed Completion Date" feature="Planning">
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn key="aim:proposedCompletionDate">
													Proposed Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.proposedCompDate}													</td>
												</tr>
												</c:if>
												</field:display>
												<field:display name="Current Completion Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:currentCompletionDate">
													Current Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.currentCompDate}"/>													</td>
												</tr>
												</field:display>
												<field:display name="Proposed Completion Date" feature="Planning">
												<c:if test="${aimEditActivityForm.editAct}">
												<c:if test="${!empty aimEditActivityForm.activityCloseDates}">
												<tr>
													<td width="32%" valign=top><digi:trn key="aim:proposedCompletionDates">
													Proposed Completion Dates</digi:trn></td>
													<td width="1" valign=top>:</td>
													<td align="left" valign=top>
														<table cellPadding=0 cellSpacing=0>
															<c:forEach var="closeDate" items="${aimEditActivityForm.activityCloseDates}">
															<tr>
																<td>
																	<c:out value="${closeDate}"/>																</td>
															</tr>
															</c:forEach>
														</table>													</td>
												</tr>
												</c:if>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												</c:if>
												</field:display>
											</table>
											</div>										</td>
									</tr>
                                    <!--END 15-->
                                     </feature:display>
                                    <!--16-->
                                    <field:display name="Status" feature="Planning">
                                        <tr>
	                                        <td align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2"  class="t-name" >                                        <digi:trn key="aim:status">Status</digi:trn>                                       	      </td>
	                                        <td bgcolor="#FFFFFF">
	                                        <category:getoptionvalue categoryValueId="${aimEditActivityForm.statusId}"/>
	                                        </td>
                                        </tr>
                                        <tr>
	                                        <td valign="top" nowrap="nowrap" bgcolor="#f4f4f2"></td>
	                                        <td bgcolor="#ffffff" ><c:out value="${aimEditActivityForm.statusReason}"/></td>
                                        </tr>
                                    </field:display>                                   
                                   
								<module:display name="References" parentModule="PROJECT MANAGEMENT">
									<tr>
									<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><digi:trn key="aim:References">References</digi:trn>									</td>
									<td bgcolor="#ffffff">
									<c:forEach items="${aimEditActivityForm.referenceDocs}" var="refDoc" varStatus="loopstatus">
										<table border="0">
											<tr>
												<td>
													<c:if test="${!empty refDoc.comment}">
													${refDoc.categoryValue}													</c:if>												</td>
											</tr>
										</table>
									</c:forEach>									</td>
									</tr>
								</module:display>
                                    <!--END 16 -->
                                   <!--17--> 
                               
                                 <feature:display name="Location" module="Project ID and Planning">
                               		<field:display name="Implementation Location" feature="Location">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="location_plus"  onclick="toggleGroup('location')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="location_minus" onclick="toggleGroup('location')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:location">
											Location</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<div id="location_dots">...</div>
											<div id="act_location" style="display: none;">
											<c:if test="${!empty aimEditActivityForm.selectedLocs}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="locations" items="${aimEditActivityForm.selectedLocs}">
													<tr>
														<td>
															<c:if test="${!empty locations.country}">
																[<c:out value="${locations.country}"/>]													</c:if>
															<c:if test="${!empty locations.region}">
																[<c:out value="${locations.region}"/>]													</c:if>
															<c:if test="${!empty locations.zone}">
																[<c:out value="${locations.zone}"/>]													</c:if>
															<c:if test="${!empty locations.woreda}">
																[<c:out value="${locations.woreda}"/>]													</c:if>													</td>
															<td align="right">
																<c:if test="${locations.showPercent}">
																	<c:out value="${locations.percent}"/>%
																</c:if>
														</td>
													</tr>
												</c:forEach>
												</table>
											</c:if>
											</div>										</td>
									</tr>
									</field:display>
                                    
                                    <field:display name="Implementation Level" feature="Location">	  
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">											<digi:trn key="aim:level">Level</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.levelId>0}" >
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.levelId}"/>
											</c:if>										</td>
									</tr>
									</field:display>
                            </feature:display>   
                            <!--19-->
                            <feature:display name="Sectors" module="Project ID and Planning">
                                            
                                            <tr>
                                                <td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="sector_plus"  onclick="toggleGroup('sector')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
                                                    <img id="sector_minus" onclick="toggleGroup('sector')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
                                              <digi:trn key="aim:sector">	Sector</digi:trn>										</td>
                                      <td bgcolor="#ffffff">
                                                    <div id="sector_dots">...</div>
                                                    <div id="act_sector" style="display: none;">
							                            <c:forEach var="config" items="${aimEditActivityForm.classificationConfigs}" varStatus="ind">
							                               <bean:define id="emptySector" value="Sector"></bean:define>
															<c:if test="${config.name== 'Secondary' }">
																<bean:define id="auxSectorType" value="Secondary Sector"></bean:define>
															</c:if>
															<c:if test="${config.name== 'Primary' }">
																<bean:define id="auxSectorType" value="Primary Sector"></bean:define>
															</c:if>
															<field:display name="${auxSectorType}" feature="Sectors">
																				
															<c:set var="hasSectors">
																false
															</c:set>
															<c:forEach var="actSect" items="${aimEditActivityForm.activitySectors}">
																<c:if test="${actSect.configId==config.id}">
																	<c:set var="hasSectors">
																		true
																	</c:set>
																</c:if>
															</c:forEach>
															<c:if test="${hasSectors}">
							                                <strong>
								                               	<digi:trn key="aim:addactivitysectors:${auxSectorType }">
								                                <c:out value="${auxSectorType}"/>
								                                </digi:trn>
								                                </strong><br/>
							                                </c:if>
					                                        <c:if test="${!empty aimEditActivityForm.activitySectors}">
																	<c:forEach var="sectors" items="${aimEditActivityForm.activitySectors}">
	                                                            		<c:if test="${sectors.configId==config.id}">
                                                            			<field:display name="Sector Scheme Name" feature="Sectors">
                                                                     		<c:out value="${sectors.sectorScheme}" />      
                                                                     	</field:display>                                                    
                                                            			<c:if test="${!empty sectors.sectorName}">
                                                            				<field:display name="Sector Scheme Name" feature="Sectors">
                                                            			    	<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                                                            			    </field:display> 
                                                                            <c:out value="${sectors.sectorName}" />
                                                              			</c:if> 
                                                              			<c:if test="${!empty sectors.subsectorLevel1Name}">
                                                              				<field:display name="${auxSectorType} Sub-Sector" feature="Sectors">																										
	                                                              				<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
	                                                                			<c:out value="${sectors.subsectorLevel1Name}"/>
                                                                			</field:display>
                                                            			</c:if>
                                                            			<c:if test="${!empty sectors.subsectorLevel2Name}">
                                                            				<field:display name="${auxSectorType} Sub-Sub-Sector" feature="Sectors">
	                                                            				<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
	                                                                			<c:out value="${sectors.subsectorLevel2Name}"/>
	                                                                		</field:display>
                                                                		</c:if>
                                                                		&nbsp;&nbsp;
                                                                		<field:display name="Percentage" feature="Sectors">
	                                                                		<c:if test="${sector.sectorPercentage!=''}">                                                                                        
	                                                                            <c:if test="${sector.sectorPercentage!='0'}">
	                                                                                    (<c:out value="${sectors.sectorPercentage}" />)%
	                                                                            </c:if>
	                                                                        </c:if><br/>
	                                                                    </field:display>

																		</c:if>
																	</c:forEach>
				                                        </c:if>
					                                    </field:display>
													</c:forEach>
                                                    </div>
												</td>
                                            </tr>
                                    </feature:display>
                                    
                                    <c:if test="${not empty aimEditActivityForm.activityComponentes}">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="component_sector_plus"  onclick="toggleGroup('component_sector')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="component_sector_minus" onclick="toggleGroup('component_sector')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:preview:component_Sector">Components</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<div id="component_sector_dots">...</div>
											<div id="act_component_sector" style="display: none;">
												<table>
													<c:forEach var="compo" items="${aimEditActivityForm.activityComponentes}">
													<tr>
														<td width="100%">
															${compo.sectorName}														</td>
														<td align="right">
															${compo.sectorPercentage}%														</td>
													</tr>
													</c:forEach>
												</table>
											</div>										</td>
									</tr>
									</c:if>
                                    
                                    <module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
								
                                	<feature:display name="NPD Programs" module="National Planning Dashboard">
									<field:display name="National Plan Objective" feature="NPD Programs">
									<TR>
																		<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
												<img id="npd_npo_plus"  onclick="toggleGroup('npd_npo')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
												<img id="npd_npo_minus" onclick="toggleGroup('npd_npo')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display : none"/>
																		<b>
						                        <digi:trn key="aim:national Plan Objective">National Plan Objective</digi:trn></b></TD>

					      <TD bgcolor="#ffffff">
														<div id="npd_npo_dots">...</div>
														<div id="act_npd_npo" style="display: none;">
                                                                                                                                                  <c:forEach var="program" items="${aimEditActivityForm.nationalPlanObjectivePrograms}">
                                                                                                                                                  <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                                                                                                                                  </c:forEach>
                                                     	</div>																		</TD>
											  </TR>
                                      </field:display> 
                                     <field:display name="Primary Program" feature="NPD Programs">
                                           <TR>
																		<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="npd_primaryprog_plus"  onclick="toggleGroup('npd_primaryprog')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
													<img id="npd_primaryprog_minus" onclick="toggleGroup('npd_primaryprog')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
																		<b>
						                        <digi:trn key="aim:primary Programs">Primary Programs</digi:trn></b></TD>

      <TD bgcolor="#ffffff">
															<div id="npd_primaryprog_dots">...</div>
															<div id="act_npd_primaryprog" style="display: none;">
                                                                                                                                                  <c:forEach var="program" items="${aimEditActivityForm.primaryPrograms}">
                                                                                                                                                  <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                                                                                                                                  </c:forEach>
                               	                  </div>																		</TD>
											  </TR>
										</field:display>
										<field:display name="Secondary Program" feature="NPD Programs">
                                                                                                                                        <TR>
																		<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="npd_secondprog_plus"  onclick="toggleGroup('npd_secondprog')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
													<img id="npd_secondprog_minus" onclick="toggleGroup('npd_secondprog')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
																		<b>
																		<digi:trn key="aim:secondary Programs">Secondary Programs</digi:trn></b></TD>

																		<TD bgcolor="#ffffff">
															<div id="npd_secondprog_dots">...</div>
															<div id="act_npd_secondprog" style="display: none;">
                                                                <c:forEach var="program" items="${aimEditActivityForm.secondaryPrograms}">
                                                                	<c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                                                </c:forEach>
                                                            </div></TD>
											</TR>
										</field:display>
									</feature:display>
                                   </module:display>
                                    
                                   <logic:present name="currentMember" scope="session">
									<module:display name="Funding" parentModule="PROJECT MANAGEMENT">
									  <bean:define id="aimEditActivityForm" name="aimEditActivityForm" scope="page" toScope="request"></bean:define>
                                    <jsp:include  page="previewActivityFunding.jsp"/>
						
									</module:display>
								</logic:present> 
									
								
									<feature:display name="Regional Funding" module="Funding">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:regionalFunding">
										    Regional Funding</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.regionalFundings}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
												<c:forEach var="regFunds" items="${aimEditActivityForm.regionalFundings}">
													<tr><td bgcolor="#ffffff">
														<table width="100%" cellSpacing="1" cellPadding="1">
															<tr><td bgcolor="#ffffff"><b>
																<c:out value="${regFunds.regionName}"/></b>
															</td></tr>
															<feature:display module="Funding" name="Commitments">
															<c:if test="${!empty regFunds.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding" border="1">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																								<c:out value="${fd.adjustmentTypeName}"/></digi:trn></td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<!-- <FONT color=blue>*</FONT> -->
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																																																																						
																							</td>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															</feature:display>
															<feature:display module="Funding" name="Disbursements">
															<c:if test="${!empty regFunds.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																								<c:out value="${fd.adjustmentTypeName}"/>
																								</digi:trn>
																						</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<!--<FONT color=blue>*</FONT>-->
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																								
																								</td>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															</feature:display>
                                                            <feature:display module="Funding" name="Expenditures">
															<c:if test="${!empty regFunds.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>																		</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.expenditures}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																								<c:out value="${fd.adjustmentTypeName}"/>
																								</digi:trn>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<!--<FONT color=blue>*</FONT>-->
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																								
																							</td>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
                                                            </feature:display>
														</table>
													</td></tr>
												</c:forEach>
												<tr><td bgcolor="#ffffff">
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
 													<FONT color=blue>*
													<digi:trn key="aim:theAmountEnteredAreInThousands">
													The amount entered are in thousands (000)</digi:trn></FONT>
</gs:test>
													
												</td></tr>
												</table>
											</c:if>										</td>
									</tr>
									</feature:display>
									
									<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="false">
									<module:display name="Components" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="components_plus"  onclick="toggleGroup('components')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="components_minus" onclick="toggleGroup('components')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:components">
											Components</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<div id="components_dots">...</div>
											<div id="act_components" style="display: none;">
											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
															<tr><td><b>
																<c:out value="${comp.title}"/>	</b>
															</td></tr>
															<tr><td><i>
																<digi:trn key="aim:description">Description</digi:trn> :</i>
																<c:out value="${comp.description}"/>
															</td></tr>
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
															</td></tr>
															<c:if test="${!empty comp.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.commitments}">
																						<tr>
																							<field:display name="Components Actual/Planned Commitments" feature="Activity - Component Step">
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}"><c:out value="${fd.adjustmentTypeName}"/>
																								</digi:trn>
																							</td>
																							</field:display>
																							<field:display name="Components Total Amount Commitments" feature="Activity - Component Step">
																							<td align="right" width="100" bgcolor="#ffffff">
																								<!--<FONT color="blue">*</FONT>-->
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Commitments" feature="Activity - Component Step">
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Commitments" feature="Activity - Component Step">
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							</field:display>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.disbursements}">
																						<tr>
																							<field:display name="Components Actual/Planned Disbursements" feature="Activity - Component Step">
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}"><c:out value="${fd.adjustmentTypeName}"/>
																								</digi:trn>																							</td>
																							</field:display>
																							<field:display name="Components Total Amount Disbursements" feature="Activity - Component Step">
																							<td align="right" width="100" bgcolor="#ffffff">
																								<!--<FONT color="blue">*</FONT>-->
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Disbursements" feature="Activity - Component Step">
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Disbursements" feature="Activity - Component Step">
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							</field:display>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.expenditures}">
																						<tr bgcolor="#ffffff">
																							<field:display name="Components Actual/Planned Expenditures" feature="Activity - Component Step">
																							<td width="50">
																								<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}"><c:out value="${fd.adjustmentTypeName}"/>
																								</digi:trn>																							</td>
																							</field:display>
																							<field:display name="Components Total Amount Expenditures" feature="Activity - Component Step">
																							<td align="right">
																								<!--<FONT color=blue>*</FONT>-->
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Expenditures" feature="Activity - Component Step">
																							<td>
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Expenditures" feature="Activity - Component Step">
																							<td width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							</field:display>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<tr><td bgcolor="#ffffff">
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
																<FONT color="blue">*
																	<digi:trn key="aim:theAmountEnteredAreInThousands">
																		The amount entered are in thousands (000)		  															</digi:trn>
																</FONT>
</gs:test>
															</td></tr>
															<field:display name="Components Physical Progress" feature="Activity - Component Step">
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:physicalProgressOfTheComponent">
																Physical progress of the component</digi:trn></b>
															</td></tr>
															<c:if test="${!empty comp.phyProgress}">
																<c:forEach var="phyProg" items="${comp.phyProgress}">
																	<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<b>
																			<c:out value="${phyProg.title}"/></b> -
																			<c:out value="${phyProg.reportingDate}"/>
																	</td></tr>
																	<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<i>
																		<digi:trn key="aim:description">Description</digi:trn> :</i>
																		<c:out value="${phyProg.description}"/>
																	</td></tr>
																</c:forEach>
															</c:if>
															</field:display>
														</table>
													</td></tr>
													</table>
												</c:forEach>
											</c:if>
											</div>										</td>
									</tr>
                                    <!--end 26-->
									</module:display>
									</logic:equal>
									
									<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="true">									
									<module:display name="Components_Resume" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<img id="components_resume_plus"  onclick="toggleGroup('components_resume')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="components_resume_minus" onclick="toggleGroup('components_resume')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:components">
											Components</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<div id="components_resume_dots">...</div>
											<div id="act_components_resume" style="display: none;">
											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
															<tr><td><b>
																<c:out value="${comp.title}"/></b>
															</td></tr>															
															<tr><td>
																<i>
																<digi:trn key="aim:component_code">Component code</digi:trn> :</i>
																<c:out value="${comp.code}"/>
															</td></tr>
															<tr><td>
															<a href="<c:out value="${comp.url}"/>" target="_blank"><digi:trn key="aim:preview_link_to_component">Link to component</digi:trn>&nbsp;<c:out value="${comp.code}"/></a>																
															</td></tr>															
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
															</td></tr>																
																<tr>
																	<td bgcolor="#ffffff">
																		<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
																		<c:forEach var="financeByYearInfo" items="${comp.financeByYearInfo}">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<c:out value="${financeByYearInfo.key}"/>
																			</td>
																			<c:set var="financeByYearInfoMap" value="${financeByYearInfo.value}"/>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																				<fmt:timeZone value="US/Eastern">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:preview_plannedcommitments_sum">Planned Commitments Sum</digi:trn>																							
																							</td>																							
																							<td align="right" width="100" bgcolor="#ffffff">																								
																								<aim:formatNumber value="${financeByYearInfoMap['MontoProgramado']}" />	USD																				
																							</td>																																													
																						</tr>
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:preview_actualcommitments_sum">Actual Commitments Sum</digi:trn>																																															
																							</td>																							
																							<td align="right" width="100" bgcolor="#ffffff">																																															
																								<aim:formatNumber value="${financeByYearInfoMap['MontoReprogramado']}" />	USD																																														
																							</td>																						
																						</tr>																																																																		
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:preview_plannedexpenditures_sum">Actual Expenditures Sum</digi:trn>																																															
																							</td>																							
																							<td align="right" width="100" bgcolor="#ffffff">																																															
																								<aim:formatNumber value="${financeByYearInfoMap['MontoEjecutado']}" />		USD																						
																							</td>																						
																						</tr>
																				</fmt:timeZone>
																				</table>																			
																			</td>
																		</tr>
																		<tr>
																			<td>&nbsp;
																				
																			</td>
																			<td>&nbsp;
																				
																			</td>																			
																		</tr>																	
																		</c:forEach>
																	</table>																	
																	</td>
																</tr>
														</table>
													</td></tr>
													<tr>
														<td>&nbsp;</td>
													</tr>
													</table>																										
												</c:forEach>
											</c:if>
											</div>										
										</td>
									</tr>
                                    <!--end 26-->
									</module:display>									
                                    </logic:equal>

									
                                    	<module:display name="Issues" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="issues_plus"  onclick="toggleGroup('issues')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="issues_minus" onclick="toggleGroup('issues')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:issues">
											Issues</digi:trn></td>
										<td bgcolor="#ffffff">
											<div id="issues_dots">...</div>
											<div id="act_issues" style="display: none;">
											<c:if test="${!empty aimEditActivityForm.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border="0">
												<c:forEach var="issue" items="${aimEditActivityForm.issues}">
													<tr><td valign="top">
														<li class="level1"><b>
														<digi:trn key="aim:issuename:${issue.id}"> <c:out value="${issue.name}"/> <c:out value="${issue.issueDate}"/></digi:trn>
														</b></li>
													</td></tr>
													<c:if test="${!empty issue.measures}">
														<c:forEach var="measure" items="${issue.measures}">
															<tr><td>
																<li class="level2"><i>
																<digi:trn key="aim:${measure.nameTrimmed}">
																<c:out value="${measure.name}"/>
																</digi:trn>
																</i></li>
															</td></tr>
															<c:if test="${!empty measure.actors}">
																<c:forEach var="actor" items="${measure.actors}">
																	<tr><td>
																		<li class="level3">
																		<digi:trn key="aim:${actor.nameTrimmed}">
																		<c:out value="${actor.name}"/>
																		</digi:trn>
																		</li>
																	</td></tr>
																</c:forEach>
															</c:if>
														</c:forEach>
													</c:if>
												</c:forEach>
												</table>
											</c:if>
										</div>										</td>
									</tr>
									</module:display>
                             <module:display name="Document" parentModule="PROJECT MANAGEMENT">       
                                   	<feature:display name="Related Documents" module="Document">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name"><img id="related_documents_plus"  onclick="toggleGroup('related_documents')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="related_documents_minus" onclick="toggleGroup('related_documents')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:relatedDocuments">
											Related Documents</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<div id="related_documents_dots">...</div>
											<div id="act_related_documents" style="display: none;">
											<c:if test="${ (!empty aimEditActivityForm.documentList) || (!empty aimEditActivityForm.crDocuments)}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												 <logic:iterate name="aimEditActivityForm"  property="documents"
													id="docs" type="org.digijava.module.aim.helper.Documents">
													<c:if test="${docs.isFile == true}">
													<tr><td>
													 <table width="100%" class="box-border-nopadding">
													 	<tr bgcolor="#ffffff">
															<td vAlign="center" align="left">
																&nbsp;<b><c:out value="${docs.title}"/></b> -
																&nbsp;&nbsp;&nbsp;<i><c:out value="${docs.fileName}"/></i>
																
																<logic:notEqual name="docs" property="docDescription" value=" ">
																	<br />&nbsp;
																	<b><digi:trn key="aim:description">Description</digi:trn>:</b>
																	&nbsp;<bean:write name="docs" property="docDescription" />
																</logic:notEqual>
																<logic:notEmpty name="docs" property="date">
																	<br />&nbsp;
																	<b><digi:trn key="aim:date">Date</digi:trn>:</b>
																	&nbsp;<c:out value="${docs.date}"/>
																</logic:notEmpty>
																<logic:notEmpty name="docs" property="docType">
																	<br />&nbsp;
																	<b><digi:trn key="aim:documentType">Document Type</digi:trn>:</b>&nbsp;
																	<bean:write name="docs" property="docType"/>
																</logic:notEmpty>															
															</td>
														</tr>
													 </table>
													</td></tr>
													</c:if>
													</logic:iterate>
													<logic:notEmpty name="aimEditActivityForm" property="crDocuments">
														<tr>
														<td>
														<logic:iterate name="aimEditActivityForm" property="crDocuments" id="crDoc">
															<table width="100%" class="box-border-nopadding">
															 	<tr bgcolor="#ffffff">
																	<td vAlign="center" align="left">
																		&nbsp;<b><c:out value="${crDoc.title}"/></b> -
																		&nbsp;&nbsp;&nbsp;<i><c:out value="${crDoc.name}"/></i>
																		<c:set var="translation">
																			<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
																		</c:set>																		
																		<a style="cursor: pointer; text-decoration: underline; color: blue;" id="<c:out value="${crDoc.uuid}"/>" onclick="window.location='/contentrepository/downloadFile.do?uuid=<c:out value="${crDoc.uuid}"/>'" title="${translation}"><img src="/repository/contentrepository/view/images/check_out.gif" border="0"></a>									
																		<logic:notEmpty name="crDoc" property="description">
																			<br />&nbsp;
																			<b><digi:trn key="aim:description">Description</digi:trn>:</b>&nbsp;
																			<bean:write name="crDoc" property="description" />
																		</logic:notEmpty>
																		<logic:notEmpty name="crDoc" property="calendar">
																			<br />&nbsp;
																			<b><digi:trn key="aim:date">Date</digi:trn>:</b>
																			&nbsp;<c:out value="${crDoc.calendar}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															 </table>
														</logic:iterate>
														</td>
														</tr>
													</logic:notEmpty>
												</table>
											</c:if>
											<c:if test="${!empty aimEditActivityForm.linksList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="docList" items="${aimEditActivityForm.linksList}">
					   							<bean:define id="links" name="docList" property="relLink" />
													<tr><td>
														<table width="100%" class="box-border-nopadding">
															<tr>
																<td width="2">
																	<digi:img src="module/aim/images/web-page.gif"/>																</td>
																<td align="left" vAlign="center">&nbsp;
																	<b><c:out value="${links.title}"/></b> -
																	&nbsp;&nbsp;&nbsp;<i><a href="<c:out value="${links.url}"/>">
																	<c:out value="${links.url}"/></a></i>
																	<br>&nbsp;
																	<b><digi:trn key="aim:description">Description</digi:trn>:</b>
																	&nbsp;<c:out value="${links.description}"/>																</td>
															</tr>
														</table>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>
											</div>										</td>
									</tr>
									</feature:display>
                                 </module:display>   
                                     
                                     
<module:display name="Organizations" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<img id="orgz_plus"  onclick="toggleGroup('orgz')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
											<img id="orgz_minus" onclick="toggleGroup('orgz')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:relatedOrganizations">Related Organizations</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										<field:display name="Responsible Organisation" feature="Responsible Organisation">
											<logic:notEmpty name="aimEditActivityForm" property="respOrganisations">
												<img id="implementing_agency_plus"  onclick="toggleGroup('responsible_organisation')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
                                            	<img id="implementing_agency_minus" onclick="toggleGroup('responsible_organisation')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>											</logic:notEmpty>
											<b><digi:trn key="aim:responsibleorganisation">Responsible Organisation</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="respOrganisations">
												<div id="responsible_organisation_dots">...</div>
												<div id="act_responsible_organisation" style="display: none;">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="respOrganisations"
													id="respOrganisations" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="respOrganisations" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
												</div>
											</logic:notEmpty>
                                            </field:display>
											<div id="orgz_dots">...</div>
											<div id="act_orgz" style="display: none;">
											   <feature:display name="Executing Agency" module="Organizations">
	                                            <logic:notEmpty name="aimEditActivityForm" property="executingAgencies">
													<img id="executing_agency_plus"  onclick="toggleGroup('executing_agency')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
	                                            	<img id="executing_agency_minus" onclick="toggleGroup('executing_agency')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display : none"/>											</logic:notEmpty>
												<b><digi:trn key="aim:executingAgency">Executing Agency</digi:trn></b><br/>
												<logic:notEmpty name="aimEditActivityForm" property="executingAgencies">
													<div id="executing_agency_dots">...</div>
													<div id="act_executing_agency" style="display: none;">
													<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="executingAgencies"
														id="execAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
																<ul><li> 
																<bean:write name="execAgencies" property="name" />
																</li></ul>
														</logic:iterate>
														</td></tr>
													</table>
													</div>
												</logic:notEmpty>
												<br/>
												</feature:display>
											

											<feature:display name="Implementing Agency" module="Organizations">
											<logic:notEmpty name="aimEditActivityForm" property="impAgencies">
												<img id="implementing_agency_plus"  onclick="toggleGroup('implementing_agency')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
                                            	<img id="implementing_agency_minus" onclick="toggleGroup('implementing_agency')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>											</logic:notEmpty>
											<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="impAgencies">
												<div id="implementing_agency_dots">...</div>
												<div id="act_implementing_agency" style="display: none;">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="impAgencies" id="impAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="impAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
												</div>
											</logic:notEmpty>
                                            </feature:display>

											<feature:display name="Beneficiary Agency" module="Organizations">
											
											<logic:notEmpty name="aimEditActivityForm" property="benAgencies">
												<img id="benAgencies_agency_plus"  onclick="toggleGroup('benAgencies_agency')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
                                            	<img id="benAgencies_agency_minus" onclick="toggleGroup('benAgencies_agency')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
                                            </logic:notEmpty>										
											<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b><br/>									
											<logic:notEmpty name="aimEditActivityForm" property="benAgencies">
												<div id="benAgencies_dots">...</div>
												<div id="act_benAgencies_agency" style="display: none;">
													<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="benAgencies"
														id="benAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
																<ul><li> <bean:write name="benAgency" property="name" /></li></ul>
														</logic:iterate>
														</td></tr>
													</table>
												</div>
											</logic:notEmpty><br/>
											</feature:display>

											<feature:display name="Contracting Agency" module="Organizations">
											<logic:notEmpty name="aimEditActivityForm" property="conAgencies">
												<img id="contracting_agency_plus"  onclick="toggleGroup('contracting_agency')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
												<img id="contracting_agency_minus" onclick="toggleGroup('contracting_agency')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>											</logic:notEmpty>
											<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="conAgencies">
												<div id="contracting_agency_dots">...</div>
												<div id="act_contracting_agency" style="display: none;">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="conAgencies"
													id="conAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<ul><li> <bean:write name="conAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
												</div>
											</logic:notEmpty><br/>
											</feature:display>					
											
											<logic:notEmpty name="aimEditActivityForm" property="sectGroups">
												<img id="sectGroups_agency_plus"  onclick="toggleGroup('sectGroups_agency')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
												<img id="sectGroups_agency_minus" onclick="toggleGroup('sectGroups_agency')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											</logic:notEmpty>
											
											<feature:display name="Sector Group" module="Organizations">
                                            <field:display name="Sector Group" feature="Sector Group">
											<b><digi:trn key="aim:sectorGroup">Sector Group</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="sectGroups">
												<div id="sectGroups_dots">...</div>
												<div id="act_sectGroups_agency" style="display: none;">
													<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="sectGroups"
														id="sectGroup" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="sectGroup" property="name" /></li></ul>
														</logic:iterate>
														</td></tr>
													</table>
												</div>
											</logic:notEmpty><br/>
											</field:display>
										</feature:display>
        									<feature:display name="Regional Group" module="Organizations">
											
											<logic:notEmpty name="aimEditActivityForm" property="sectGroups">
												<img id="regGroups_agency_plus"  onclick="toggleGroup('regGroups_agency')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
												<img id="regGroups_agency_minus" onclick="toggleGroup('regGroups_agency')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											</logic:notEmpty>											
											
											<field:display name="Regional Group" feature="Regional Group">
											<b><digi:trn key="aim:regionalGroup">Regional Group</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="regGroups">
											<div id="regGroups_dots">...</div>
												<div id="act_regGroups_agency" style="display: none;">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="regGroups"
													id="regGroup" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<ul><li> <bean:write name="regGroup" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
												</div>
											</logic:notEmpty><br/>
											</field:display>
                                  	</feature:display> 
                                  	</div>
                                  	</td>
									</tr>
									</module:display>
									
                                      <module:display name="Contact Information" parentModule="PROJECT MANAGEMENT">
									<feature:display name="Donor Contact Information" module="Contact Information">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:donorFundingContactInformation">
											 Donor funding contact information</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.dnrCntFirstName}"/>
											<c:out value="${aimEditActivityForm.dnrCntLastName}"/> -
											<c:out value="${aimEditActivityForm.dnrCntEmail}"/>										</td>
									</tr>
									</feature:display>
									<feature:display name="Government Contact Information" module="Contact Information">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:mofedContactInformation">
											MOFED contact information</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.mfdCntFirstName}"/>
											<c:out value="${aimEditActivityForm.mfdCntLastName}"/> -
											<c:out value="${aimEditActivityForm.mfdCntEmail}"/>										</td>
									</tr>
									</feature:display>
									<feature:display name="Project Coordinator Contact Information" module="Contact Information">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:projectCoordinator">
											 Project Coordinator Contact Information</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.prjCoFirstName}"/>
											<c:out value="${aimEditActivityForm.prjCoLastName}"/> -
											<c:out value="${aimEditActivityForm.prjCoEmail}"/>										</td>
									</tr>
									</feature:display>
									<feature:display name="Sector Ministry Contact Information" module="Contact Information">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:sectorMinistryCnt">
											 Sector Ministry Contact Information</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.secMiCntFirstName}"/>
											<c:out value="${aimEditActivityForm.secMiCntLastName}"/> -
											<c:out value="${aimEditActivityForm.secMiCntEmail}"/>										</td>
									</tr>
									</feature:display>
									</module:display>

									
									<field:display name="Activity Performance"  feature="Activity Dashboard">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:meActivityPerformance">
										    Activity - Performance</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<% if (actPerfChartUrl != null) { %>
												<img src="<%= actPerfChartUrl %>" width="370" height="450" border="0" usemap="#<%= actPerfChartFileName %>"><br><br>
											<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
											    <digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>
											    </span><br><br>
											<% } %>										</td>
									</tr>
									</field:display>
									<field:display name="Project Risk" feature="Activity Dashboard">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:meActivityRisk">
										    Activity - Risk</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<% if (actRiskChartUrl != null) { %>
												<bean:define id="riskColor" name="riskColor" scope="request" toScope="page" type="java.lang.String"/>
												<bean:define id="riskName" name="overallRisk" scope="request" toScope="page" type="java.lang.String"/>
												<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn>:
												<font color="${riskColor}"/>

												<b><digi:trn key="<%=riskName%>"><%=riskName%></digi:trn></b>

												<img src="<%= actRiskChartUrl %>" width="370" height="350" border="0" usemap="#<%= actRiskChartFileName %>">
												<br><br>
											<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
										  	    <digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>
											    </span><br><br>
											<% } %>										</td>
									</tr>
									</field:display>
                                    
                                    <feature:display name="Proposed Project Cost" module="Funding">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:proposedPrjectCost"> Proposed Project Cost</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.proProjCost!=null}">
                                                  <table cellSpacing=1 cellPadding="3" bgcolor="#aaaaaa" width="100%">
                                                      <tr bgcolor="#ffffff">
															<td><digi:trn key="aim:cost">Cost</digi:trn></td>
                                                        <td bgcolor="#FFFFFF" align="left" >
                                                          <c:if test="${aimEditActivityForm.proProjCost.funAmount!=null}">
																 	<!--<FONT color=blue>*</FONT>-->
																 	 ${aimEditActivityForm.proProjCost.funAmount}                                                          </c:if>&nbsp;
																<c:if test="${aimEditActivityForm.proProjCost.currencyCode!=null}"> ${aimEditActivityForm.proProjCost.currencyCode} </c:if>                                                        </td>
												    </tr>
																		  <tr bgcolor="#ffffff">
															<td><digi:trn key="aim:proposedCompletionDate">Proposed Completion Date</digi:trn></td>
                                                        <td bgcolor="#FFFFFF" align="left" width="150">
                                                          <c:if test="${aimEditActivityForm.proProjCost.funDate!=null}">
                                                             ${aimEditActivityForm.proProjCost.funDate}                                                          </c:if>                                                        </td>
                                                      </tr>
                                                </table>
                                             </c:if>										</td>
									</tr>
									</feature:display>
											
										<!-- Costing -->
									<feature:display name="Costing" module="Activity Costing">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:costing">
										    Costing</digi:trn>										</td>
										<td bgcolor="#ffffff">
											&nbsp;&nbsp;&nbsp;
											<table width="100%">
												<tr>
													<td>
														<bean:define id="mode" value="preview" type="java.lang.String" toScope="request" />
														<jsp:include page="viewCostsSummary.jsp" flush="" />													</td>
												</tr>
											</table>										</td>
									</tr>
									</feature:display>
									<!-- End Costing -->
                                                                         
                                                                         	<!-- IPA Contracting -->
                                                                                
									<feature:display name="Contracting" module="Contracting">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
                                           <img id="contract_plus"  onclick="toggleGroup('contract')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
                                           <img id="contract_minus" onclick="toggleGroup('contract')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif"style="display : none"/>
											<digi:trn key="aim:IPAContracting">IPA Contracting</digi:trn>
										</td>
										<td bgcolor="#ffffff">&nbsp;&nbsp;&nbsp;
                                            <div id="contract_dots">...</div>
											<div id="act_contract" style="display: none;">
											<table width="100%">
												<tr>
													<td><!-- contents -->
								 						<logic:notEmpty name="aimEditActivityForm" property="contracts">
                                                        	<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
                                                                 <c:forEach items="${aimEditActivityForm.contracts}" var="contract" varStatus="idx">
                                                                       <tr><td bgColor=#f4f4f2 align="center" vAlign="top">
                                                            	           <table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
                                                            	           		<field:display name="Contract Name" feature="Contracting">
                                                                               <tr>
                                                                                  <td align="left">
                                                                                     <b><digi:trn key="aim:IPA:popup:name">Contract name:</digi:trn></b>
                                                                                   </td>
                                                                                   <td>
                                                                	                  ${contract.contractName}
                                                                                   </td>
                                                                                </tr>
                                                                                </field:display>
                                                                                
                                                                                <field:display name="Contract Description" feature="Contracting">
                                                                                 <tr>
                                                                                    <td align="left">
                                                                                       <b><digi:trn key="aim:IPA:popup:description">Description:</digi:trn></b>
                                                                                    </td>
                                                                                    <td>
                                                                                       ${contract.description}
                                                                                     </td>
                                                                                  </tr> 
                                                                                  </field:display>
                                                                                  
                                                                                  <field:display name="Contracting Activity Category" feature="Contracting">
                                                                                   <tr>
                                                                                     <td align="left">
                                                                                  	     <b><digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn></b>
                                                                                      </td>
                                                                                       <td>
                                                                                          <c:if test ="${not empty contract.activityCategory}">${contract.activityCategory.value}</c:if>
                                                                                       </td>
                                                                                    </tr>
                                                                                    </field:display>
                                                                                    
                                                                                    <field:display name="Contract type" feature="Contracting">
                                                                                     <tr>
                                                                                     <td align="left">
                                                                                  	     <b><digi:trn key="aim:IPA:popup:type">Type</digi:trn>:</b>
                                                                                      </td>
                                                                                       <td>
                                                                                          <c:if test ="${not empty contract.type}">${contract.type.value}</c:if>
                                                                                       </td>
                                                                                    </tr>
                                                                                    </field:display>
                                                                                    
                                                                                    <field:display name="Contracting Start of Tendering" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:startOfTendering">Start of Tendering:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.formattedStartOfTendering}
                                                                                           </td>                                                                                            
                                                                                        </tr>
                                                                                        </field:display>	
                                                                                        
                                                                                        <field:display name="Signature of Contract" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.formattedSignatureOfContract}
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contract Organization" feature="Contracting">
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:if test="${not empty contract.organization}">
                                                                                                     ${contract.organization.name}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting Organization Text" feature="Contracting">
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractOrg">Contract Organization</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                     ${contract.contractingOrganizationText}
                                                                                                
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contract Completion" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.formattedContractCompletion}
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting Status" feature="Contracting">
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:status">Status:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                
                                                                                                <c:if test ="${not empty contract.status}">
                                                                                 
                                                                                                    ${contract.status.value}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Total Amount" feature="Contracting">
                                                                                        	<tr>
                                                                                            	<td align="left">
                                                                                                	<b><digi:trn key="aim:IPA:popup:totalAmount">Total Amount</digi:trn>:</b>
                                                                                            	</td>
                                                                                            	<td>
                                                                                                	 ${contract.totalAmount}
                                                                               	                 ${contract.totalAmountCurrency} 
                                                                                	            </td>
                                                                                        	</tr>
                                                                                    	</field:display>
                                                                                        
                                                                                        <field:display name="Total EC Contribution" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting IB" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IB">IB</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.totalECContribIBAmount}
                                                                                                ${contract.totalECContribIBCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting INV" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:INV">INV:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalECContribINVAmount}
                                                                                               ${contract.totalECContribINVCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        
                                                                                        
                                                                                        
                                                                                        <field:display name="Contracting Total National Contribution" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting Central Amount" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:Central">Central</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribCentralAmount}
                                                                                                ${contract.totalNationalContribCentralCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting Regional Amount" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:Regional">Regional</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribRegionalAmount} 
                                                                                              ${contract.totalNationalContribRegionalCurrency}
                                                                                   
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting IFIs" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IFIs">IFIs</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribIFIAmount}
                                                                                               ${contract.totalNationalContribIFICurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        
                                                                                        <field:display name="Total Private Contribution" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Contracting IB" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IB">IB:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalPrivateContribAmount}
                                                                                                ${contract.totalPrivateContribCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        <field:display name="Total Disbursements of Contract" feature="Contracting">
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalDisbursements">Total Disbursements</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                            									${contract.totalDisbursements} &nbsp; 
                                                            									<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
                                                            										&nbsp; ${aimEditActivityForm.currCode}
                                                            									</logic:empty>
                                                            									<logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
                                                            										&nbsp; ${contract.dibusrsementsGlobalCurrency}
                                                            									</logic:notEmpty>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>    
                                                                                    <field:display name="Contract Execution Rate" feature="Contracting">
                                                                                
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                            										&nbsp; ${contract.executionRate}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                        
                                                                                        <field:display name="Disbursements" feature="Contracting">
                                                                                        <tr>
                                                                                    
                                                                                            <td colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        </field:display>
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td>&nbsp;
                                                                                            </td>
                                                                                            <td>
                                                                                               
                                                                                    
                                                                                                    <logic:notEmpty name="contract" property="disbursements">
                                                                                                         <table>
                                                                                              
                                                                                                        <c:forEach  items="${contract.disbursements}" var="disbursement" >
                                                                                                            <tr>
                                                                                          
                                                                                                                <td align="left" valign="top">
                                                                                                                    <c:if test="${disbursement.adjustmentType==0}">
                                                                                                                          <digi:trn key="aim:actual">Actual</digi:trn>
                                                                                                                   </c:if>
                                                                                                                    <c:if test="${disbursement.adjustmentType==1}">
                                                                                                                          <digi:trn key="aim:planned">Planned</digi:trn>
                                                                                                                   </c:if>
                                                                                                    
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                    ${disbursement.amount}
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                   ${disbursement.currency.currencyName} 
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                    ${disbursement.disbDate}
                                                                                                                    
                                                                                                                </td>
                                                                                                            </tr>
                                                                                                        </c:forEach>
                                                                                                        </table>
                                                                                                        
                                                                                                    </logic:notEmpty>						
                                                                                                		
                                                                                            </td>		
                                                                                        </tr>		
                                                                                        
                                                                                    </table>
                                                                                    
                                                                                    
                                                                             
                                                                                   </td></tr>
                                                                                  
                                                                                </c:forEach>
                                                                                </table>
                                                                                
                                                                            </logic:notEmpty>
								<!-- end contents -->
                                                                 </td>
										</tr>
											</table>
                                                                                         </div>
                                                                                         </td>
									</tr>
									</feature:display>
									<!-- end IPA Contracting -->
                                                                         
									<field:display name="Activity Created By" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:activityCreatedBy">
										    Activity created by</digi:trn>										</td>
										<td width="69%" bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthFirstName}"/>
											<c:out value="${aimEditActivityForm.actAthLastName}"/> -
											<c:out value="${aimEditActivityForm.actAthEmail}"/>										</td>
									</tr>
									</field:display>
									<field:display name="Workspace of Creator" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:workspaceOfCreator">Worskpace of creator</digi:trn>										</td>
										<td width="69%" bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.createdBy.ampTeam.name}"/> - <c:out value="${aimEditActivityForm.createdBy.ampTeam.accessType}"/>
										</td>
									</tr>
									</field:display>
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:computation">Computation</digi:trn>
										</td>
										<td width="69%" bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.createdBy.ampTeam.computation == 'true'}">
												  <digi:trn key="aim:yes">Yes</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.createdBy.ampTeam.computation == 'false'}">
												  <digi:trn key="aim:no">No</digi:trn>
											</c:if>
										</td>
									</tr>
									<field:display feature="Identification" name="Data Source">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:dataSource">
										    Data Source</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthAgencySource}"/>										</td>
									</tr>
									</field:display>
									<field:display name="Activity Updated On" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="updatedDate">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:activityUpdatedOn">
											 Activity updated on</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedDate}"/>
											<html:button  styleClass="dr-menu" property="submitButton" onclick="viewChanges()">
												<digi:trn key="btn:last5changestoactivity">Last 5 changes to Activity</digi:trn>
											</html:button>										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<field:display name="Activity Updated By" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="updatedBy">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:activityUpdatedBy">
											 Activity updated by</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedBy.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.updatedBy.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.updatedBy.user.email}"/>										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<field:display name="Activity Created On" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="createdDate">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:activityCreatedOn">
											 Activity created on</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.createdDate}"/>										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<logic:notEmpty name="aimEditActivityForm" property="team">
									<field:display name="Data Team Leader" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
											<digi:trn key="aim:activityTeamLeader">
											 Data Team Leader</digi:trn>										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.team.teamLead.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.team.teamLead.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.team.teamLead.user.email}"/>										</td>
									</tr>
									</field:display>
									</logic:notEmpty>
									<c:if test="${aimEditActivityForm.pageId == 1}">
									<tr><td bgColor="#ffffff" align="center" colspan="2">
										<table cellPadding=3>
											<tr>
												<td>
													<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<< <digi:trn key="btn:back">Back</digi:trn>'name="backButton"/>												</td>
												<td>
													<input type="button" class="dr-menu" onclick="disable()" value='<digi:trn key="btn:saveActivity">Save Activity</digi:trn>' name="submitButton"/>												</td>
											</tr>
										</table>
									</td></tr>
									</c:if>
									<c:if test="${aimEditActivityForm.pageId > 2}">
									<tr><td bgColor="#ffffff" align="center" colspan="2">
										<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<digi:trn key="btn:back">Back</digi:trn>' />
									</td></tr>
									</c:if>
								</table>
							</td></tr>
							</table>
							</td></tr>
							<tr>
								<td>
									<div align="center">
										<logic:notEmpty name="previousActivity" scope="session">
											<digi:link href="/viewActivityPreview.do~pageId=2" paramId="activityId" paramName="previousActivity" paramScope="session"><font size="2"><digi:trn key="aim:previous">Previous</digi:trn></font></digi:link>
												<logic:notEmpty name="nextActivity" scope="session">
													<font size="2">
														&nbsp;-&nbsp;
													</font>
												</logic:notEmpty>
										</logic:notEmpty>
										<logic:notEmpty name="nextActivity" scope="session">
											<digi:link href="/viewActivityPreview.do~pageId=2" paramId="activityId" paramName="nextActivity" paramScope="session"><font size="2"><digi:trn key="aim:next">Next</digi:trn></font></digi:link>
										</logic:notEmpty>

									</div>
								</td>
							</tr>
						</table>
						</td></tr>
					</table>
				</td></tr>
				<tr><td>&nbsp;
					
				</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td>
</tr>
</table></digi:form>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>



<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.dbentity.AmpGlobalSettings"%>
<%@page import="java.util.Collections"%>
<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<digi:instance property="aimReportsFilterPickerForm" />

<digi:form action="/reportsFilterPicker.do">

	

<div id="tabview_container" class="yui-navset">
	<ul class="yui-nav">
		<li class="selected"><a href="#keyword"><span><digi:trn key="rep:filer:tab:KeywordAndCalendar">Keyword & Calendar</digi:trn></span></a> </li>
		<li><a href="#financing"><span><digi:trn key="rep:filer:tab:FinancingAndLocation">Financing & Location</digi:trn></span></a> </li>
		<li><a href="#donors"><span><digi:trn key="rep:filer:tab:DonorsAndAgencies">Donors & Agencies</digi:trn></span></a> </li>
		<li><a href="#status"><span><digi:trn key="rep:filer:tab:StatusAndMinistryRank">Status & Ministry Rank</digi:trn></span></a> </li>
	</ul>
	<div class="yui-content" style="background-color: #EEEEEE">
		<div id="keyword" >
		<br />
		<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
		<tr valign="top"><td align="center">
			<c:set var="tooltip_translation">
				<digi:trn
						key="rep:filter:SpecifyprojectsKeywords.">Specify keywords to look for in the project data.</digi:trn>
			</c:set>
			<table align="center" cellpadding="2" cellspacing="2" onmouseover="Tip('${tooltip_translation}')">
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><b><digi:trn
						key="rep:filter:KeywordsTitle">Keyword Search</digi:trn></b><br/>
					</td>
				</tr>
				
				<tr bgcolor="#EEEEEE">
					<td colspan="5">&nbsp;</td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><digi:trn key="rep:filter:Keywords">Keywords</digi:trn> <br />
					<html:text property="indexString" style="width: 250px"	styleClass="inp-text" /></td>
				</tr>
				
			</table>
		</td>
		<td align="center">
			<c:set var="tooltip_translation">
					<digi:trn
						key="rep:filter:timePeriod">Specify the time period to limit your search within.</digi:trn>
			</c:set>
			<table align="center" cellpadding="2" cellspacing="2" onmouseover="Tip('${tooltip_translation}');">
				
				<tr bgcolor="#EEEEEE">
					<td colspan="5">
						<b><digi:trn key="rep:filter:CalendarTitle">Calendar</digi:trn></b>
						<br>
					</td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<bean:define id="calendars" name="aimReportsFilterPickerForm"
						property="calendars" type="java.util.Collection" />
					<%
						    int size = ((java.util.Collection) calendars).size();
						    boolean condition = (size != 1);
						    if (condition) {
					%>
					<td colspan="4"><digi:trn key="rep:filer:fiscalCalendar">Fiscal Calendar</digi:trn></td>
					<%
					}
					%>
					<%
					int filterByMonths=0;
					java.util.Collection col = FeaturesUtil.getGlobalSettings();
					java.util.Iterator itr = col.iterator();
					while (itr.hasNext()) {
						AmpGlobalSettings ags = (AmpGlobalSettings)itr.next();
						if (ags.getGlobalSettingsName().equals("Filter reports by month"))
								if (ags.getGlobalSettingsValue().equals("On")) {
									filterByMonths=1;
									break;
								}
					}
					%>
				</tr>
				<tr>
					<%
					if (condition) {
					%>
					<td colspan="4"><html:select property="calendar" style="width: 220px"
						styleClass="inp-text">
						<html:optionsCollection property="calendars" value="ampFiscalCalId"
							label="name" />
					</html:select></td>
					<%
					}
					else{
					%>
					<td>&nbsp;</td>
					<%
					}
					%>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>	
					<%
					if (filterByMonths==1) {
					%>
						<td align="center">
							<digi:trn key="rep:filer:FromMonth">From Month</digi:trn>
						</td>
					<%
					}
					%>
					<td align="center">
						<digi:trn key="rep:filer:FromYear"> From Year</digi:trn>
					</td>
		
					<%			
					if (filterByMonths==1) {
					%>
						<td align="center">
							<digi:trn key="rep:filer:ToMonth">To Month</digi:trn>
						</td>
					<%
					}
					%>
					<td align="center">
						<digi:trn key="rep:filer:ToYear">To Year</digi:trn>
					</td>
					<%
					if (filterByMonths == 0)
					{
					%>
						<td align="center">
						&nbsp;
						</td>	
						<td align="center">
						&nbsp;
						</td>	
					<%
					}
					%>
					
				</tr>
				<tr bgcolor="#EEEEEE">
					<%
					if (filterByMonths==1) {
					%>
					<td>
					<html:select property="fromMonth"
						styleClass="inp-text">
						<html:option value="-1">
							<digi:trn key="rep:filer:All">All</digi:trn>
						</html:option>
						<html:optionsCollection property="fromMonths" label="wrappedInstance"
							value="wrappedInstance" />
					</html:select>
					</td>
					<%
					}
					%>			
					<td>
					<html:select property="fromYear"
						styleClass="inp-text">
						<html:option value="-1">
							<digi:trn key="rep:filer:All">All</digi:trn>
						</html:option>
						<html:optionsCollection property="fromYears" label="wrappedInstance"
							value="wrappedInstance" />
					</html:select>
					</td>
					
					
					<%			
					if (filterByMonths==1) {
					%>
					<td>
					<html:select property="toMonth"
						styleClass="inp-text">
						<html:option value="-1">
							<digi:trn key="rep:filer:All">All</digi:trn>
						</html:option>
						<html:optionsCollection property="toMonths" label="wrappedInstance"
							value="wrappedInstance" />
					</html:select>
					</td>
					<%
					}
					%>
					<td>
					<html:select property="toYear"
						styleClass="inp-text">
						<html:option value="-1">
							<digi:trn key="rep:filer:All">All</digi:trn>
						</html:option>
						<html:optionsCollection property="toYears" label="wrappedInstance"
							value="wrappedInstance" />
					</html:select>
					</td>
					<%
					if (filterByMonths == 0)
					{
					%>
						<td align="center">
						&nbsp;
						</td>	
						<td align="center">
						&nbsp;
						</td>	
					<%
					}
					%>
				</tr>
		</table>
			</td> </tr>
		</table>
		</div>
		<div id="financing" style="display: none;">
		<br />
		<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px">
		<tr valign="top">
			<td align="center">
			<c:set var="tooltip_translation">
				<digi:trn
						key="rep:filter:financingDetails">Specify the financing details.</digi:trn>
			</c:set>
			<table align="center" cellpadding="1" cellspacing="1" onmouseover="Tip('${tooltip_translation}')">
				<tr bgcolor="#EEEEEE">
					<td colspan="5">
					<b><digi:trn key="rep:filter:FinancingTitle">Financing</digi:trn></b>
					<br>
					</td>
				</tr>
				
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><digi:trn key="rep:filer:Currency">Currency</digi:trn></td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><html:select property="currency"
						style="width: 250px" styleClass="inp-text">
						<html:optionsCollection property="currencies" value="ampCurrencyId"
							label="currencyName" />
					</html:select></td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><digi:trn
						key="rep:filer:financingInstrument">Financing Instrument</digi:trn></td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><category:showoptions size="3"
						outerstyle="width: 250px" styleClass="inp-text"
						name="aimReportsFilterPickerForm"
						property="selectedFinancingInstruments" multiselect="true"
						keyName="<%=org.digijava.module.aim.helper.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" />
					</td>
				</tr>
				<tr>
					<td colspan="5" valign="top"><field:display
						name="Joint Criteria" feature="Budget">
						<html:checkbox property="jointCriteria" value="true" /> &nbsp;<digi:trn
							key="rep:filter:jointCriteriaCheckDisplay">Display Only Projects Under Joint Criteria.</digi:trn>&nbsp;&nbsp;				</field:display>
					<br />
					<field:display name="Government Approval Procedures" feature="Budget">
						<html:checkbox property="governmentApprovalProcedures" value="true" />&nbsp;<digi:trn
							key="rep:filter:govAppProcCheck"> Display Only Projects Having Government Approval Procedures. </digi:trn>
					</field:display></td>
					<td>
					&nbsp;
					</td>
				</tr>
			</table>
			</td>
			<td align="center">
				<c:set var="tooltip_translation">
					<digi:trn key="rep:filter:regionandSector">Specify the region and sectors of interest.</digi:trn>
				</c:set>
				<table align="center" cellpadding="1" cellspacing="1" onmouseover="Tip('${tooltip_translation}');">
						<tr bgcolor="#EEEEEE">
					<td colspan="5">
					<b><digi:trn key="rep:filter:RegionAndSectorTitle">Region and Sector</digi:trn></b>
					<br>
					</td>
				</tr>
				
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><digi:trn key="rep:filter:Location">Region</digi:trn></td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><html:select property="regionSelected"
						style="width: 300px" styleClass="inp-text">
						<html:option value="-1">
							<digi:trn key="rep:filer:All">All</digi:trn>
						</html:option>
						<html:optionsCollection property="regionSelectedCollection"
							label="region" value="regionId" />
					</html:select></td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<td colspan="5"><digi:trn key="rep:filer:Sector">Sector</digi:trn></td>
				</tr>
				<tr bgcolor="#EEEEEE">
					<td colspan="5" styleClass="inp-text"><html:select
						multiple="true" property="selectedSectors" size="3"
						style="width: 300px" styleClass="inp-text">
						<html:optionsCollection property="sectors" value="ampSectorId"
							label="name" />
					</html:select></td>
				</tr>
				
				</table>
			</td>
		</tr>
		</table>
		</div>
		<div id="donors" style="display: none;">
			<br/>
			<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px">
				<tr valign="top">
				<td align="center">
				<c:set var="tooltip_translation">
						<digi:trn
							key="rep:filter:DonorInformation">Specify Donor Information.</digi:trn>
				</c:set>
					<table align="center" cellpadding="1" cellspacing="1" onmouseover="Tip('${tooltip_translation}');">
						<tr bgcolor="#EEEEEE">
							<td colspan="5">
							<b><digi:trn key="rep:filter:DonorsTitle">Donors</digi:trn></b>
							<br />
							</td>
						</tr>
						<tr><td colspan="5"> &nbsp;</td></tr>

						<tr bgcolor="#EEEEEE">
							<td colspan="5"><digi:trn key="rep:filer:DonorType">Donor Type</digi:trn>
								<br/>
							
								<html:select style="width: 190px"
								multiple="true" property="selectedDonorTypes" size="3"
								styleClass="inp-text">
								<html:optionsCollection property="donorTypes" value="ampOrgTypeId"
									label="orgType" />
								</html:select>
							</td>
							
						</tr>
						<tr><td colspan="5"> &nbsp;</td></tr>
						<tr><td colspan="5"> &nbsp;</td></tr>
						<tr><td colspan="5"> &nbsp;</td></tr>
						<tr bgcolor="#EEEEEE">
							<td colspan="5">
								<digi:trn key="rep:filer:DonorGroup">Donor Group</digi:trn>
								<br />
								<html:select multiple="true" style="width: 190px"
								property="selectedDonorGroups" size="3" styleClass="inp-text">
								<html:optionsCollection property="donorGroups"
									value="ampOrgGrpId" label="orgGrpName" />
								</html:select>
							</td>
						</tr>
					</table>
					</td>
					<td align="center">
						<c:set var="tooltip_translation">
							<digi:trn
							key="rep:filter:CooperatingAgencies">Specify Cooperating Agencies.</digi:trn>
						</c:set>	
						<table align="center" cellpadding="1" cellspacing="1" onmouseover="Tip('${tooltip_translation}');">
						<tr bgcolor="#EEEEEE">
							<td>
									<b><digi:trn key="rep:filer:AgenciesTitle">Agencies</digi:trn></b>
									<br />									
							</td>
						</tr>
						<tr><td colspan="5"> &nbsp;</td></tr>
						<feature:display name="Beneficiary Agency" module="Organizations">
						<tr bgcolor="#EEEEEE">
				         	<td>
										<digi:trn key="rep:filer:beneficiaryAgency">Beneficiary Agency</digi:trn>
				     		</td>
				    	</tr>
				        <tr>
				                  	<td>
					                  	<html:select style="width: 190px"
											multiple="true" property="selectedBeneficiaryAgency" size="3"
											styleClass="inp-text">
										<html:optionsCollection property="beneficiaryAgency" label="name"
											value="ampOrgId" />
										</html:select>
										<br />
									</td>
						</tr>
						</feature:display>
							
						<feature:display name="Executing Agency" module="Organizations">
				        <tr bgcolor="#EEEEEE">
								<td>
										<digi:trn key="rep:filer:executingAgency">Executing Agency</digi:trn>
								</td>
						</tr>
				        <tr>
				                <td>
					                  <html:select style="width: 190px"
									multiple="true" property="selectedExecutingAgency" size="3"
									styleClass="inp-text">
								
											<html:optionsCollection property="executingAgency" label="name"
												value="ampOrgId" />
										</html:select>
										<br />
								</td>
						</tr>
						</feature:display>
						<feature:display name="Implementing Agency" module="Organizations">
						<tr>
								<td>
									<digi:trn key="rep:filer:implementingAgency">Implementing Agency</digi:trn>
								</td>
						</tr>
						<tr>
  							<td>	
  									<html:select style="width: 190px" multiple="true"
									property="selectedImplementingAgency" size="3"
									styleClass="inp-text">
									<html:optionsCollection property="implementingAgency" label="name"
										value="ampOrgId" />
									</html:select>
							</td>
						</tr>
					   </feature:display>
				</table>
          </td></tr>
          </table>
		</div>
		<div id="status" style="display: none;">
		<br />
		<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px">
			<tr valign="top">
			<td align="center">
				<c:set var="tooltip_translation">
					<digi:trn key="rep:filter:otherCriteria">Specify other criteria to filter with.</digi:trn>
				</c:set>
				<table align="center" cellpadding="1" cellspacing="1">
				<tr bgcolor="#EEEEEE">
					<td colspan="5">
						<b><digi:trn key="rep:filter:StatusTitle">Status</digi:trn></b>
						<br />
					</td>
				</tr>
				
				<tr bgcolor="#EEEEEE">
					<td valign="top"><category:showoptions
						outerstyle="width: 190px" styleClass="inp-text"
						property="selectedStatuses" size="3"
						name="aimReportsFilterPickerForm" multiselect="true"
						keyName="<%=org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY%>" />
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<field:display name="Risk" feature="Activity">
				<tr>
						<td>
							<b><digi:trn key="rep:filer:RisksTitle">Risks</digi:trn></b> <br />
							<html:select multiple="true" style="width: 190px"
								property="selectedRisks" size="3" styleClass="inp-text">
								<html:optionsCollection property="risks"
									value="ampIndRiskRatingsId" label="ratingName" />
							</html:select>
														
						</td>
				</tr>
				</field:display>
			</table>
		</td>
		<td>
				<table align="center" cellpadding="1" cellspacing="1" >
					<tr><td><b><digi:trn key="rep:filer:MinistryRankTitle">Ministry Rank</digi:trn></b></td></tr>
					<field:display name="Line Ministry Rank" feature="Planning">
					<tr>
						<td>
							<digi:trn key="rep:filer:LineMinRank">Line Ministry Rank</digi:trn>
							<br />
							<html:select property="lineMinRank" style="width: 100px" styleClass="inp-text">
								<html:option value="-1">
									<digi:trn key="rep:filer:All">All</digi:trn>
								</html:option>
								<html:optionsCollection property="actRankCollection"
									label="wrappedInstance" value="wrappedInstance" />
							</html:select>
						</td>
					</tr>
					</field:display>
		
					<tr><td>&nbsp;</td></tr>
					<field:display name="Planning Ministry Rank" feature="Planning">
					<tr>
						<td>
							<digi:trn key="rep:filer:PlanningMinRank">Planning Ministry Rank</digi:trn></b>
							<br />
							<html:select property="planMinRank" style="width: 100px" styleClass="inp-text">
								<html:option value="-1">
									<digi:trn key="rep:filer:All">All</digi:trn>
								</html:option>
								<html:optionsCollection property="actRankCollection"
									label="wrappedInstance" value="wrappedInstance" />
							</html:select>
						</td>
					</tr>	
					</field:display>
				</table>
				<logic:notEqual name="widget" value="true" scope="request">
				<br/>
				<c:set var="tooltip_translation">
					<digi:trn key="rep:filter:pageSizeMesg">Specify the page size you want to format your report to print in.</digi:trn>
				</c:set>
				<table align="center" cellpadding="1" cellspacing="1" >
					<tr bgcolor="#EEEEEE">
						<td colspan="5"><b><digi:trn key="rep:filter:pageSizeTitle">Page Size</digi:trn></b></td>
					</tr>
					<tr bgcolor="#EEEEEE">
						<td colspan="5"><html:select property="pageSize"
							style="width: 100px" styleClass="inp-text">
							<html:optionsCollection property="pageSizes"
								label="wrappedInstance" value="wrappedInstance" />
						</html:select></td>
					</tr>
				</table>
				</logic:notEqual>
			</td>
		</tr>
		</table>
		</div>
	</div>
	


</div>
<div style="background-color: #EEEEEE; ">
	<br />
	<table width="100%">
		<tr>
			<td align="center" colspan="5">
			<html:hidden property="ampReportId" />
			<html:hidden property="defaultCurrency" />
			<html:submit styleClass="buton"
				property="apply">
				<digi:trn key="rep:filer:ApplyFiltersToReport">Apply Filters to the Report</digi:trn>
			</html:submit>&nbsp; <html:button onclick="resetFilter();" styleClass="buton"
				property="reset">
				<digi:trn key="rep:filer:ResetAndStartOver">Reset and Start Over</digi:trn>
			</html:button> </td>
		</tr>
	</table>
</div>
</digi:form>

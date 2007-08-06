<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%> 
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<digi:instance property="aimReportsFilterPickerForm"/>

<digi:form action="/reportsFilterPicker.do">

<html:hidden property="ampReportId"/>
	<table>
	<tr>
		<td class=clsTableTitleCol colspan="4" align="center">
		<digi:trn key="rep:filer:Selectfilters">Select filters</digi:trn>
		</td>
	</tr>
		
		<tr>
			<td colspan="4"><b>Keywords</b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:text property="text" style="width: 400px" styleClass="inp-text"/>
			</td>
		</tr>
		
		<tr>
			<td colspan="4"><b><digi:trn key="rep:filer:Currency">Currency</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select property="currency" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="currencies"
					value="ampCurrencyId" label="currencyName" />
			
			</html:select>
			</td>
		</tr>


		<tr>
			<td colspan="2"><b><digi:trn key="rep:filer:fiscalCalendar">Fiscal Calendar</digi:trn></b></td>
			<td align="center"><b><digi:trn key="rep:filer:FromYear">From Year</digi:trn></b></td>
			<td align="center"><b><digi:trn key="rep:filer:ToYear">To Year</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="2">
			<html:select property="calendar" style="width: 270px" styleClass="inp-text">
				<html:optionsCollection property="calendars"
					value="ampFiscalCalId" label="name" />
			</html:select>
			</td>
	
			<td align="center">
			<html:select property="fromYear" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>			
				<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>

			<td align="center">
			<html:select property="toYear" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>			
				<html:optionsCollection property="toYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>


		<tr>
			<td colspan="4"><b><digi:trn key="rep:filer:Sector">Sector</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4" styleClass="inp-text">
			<html:select multiple="true" property="selectedSectors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="sectors"
					value="ampSectorId" label="name" />
			</html:select>
			</td>
		</tr>
		
			
			
		
		<tr>
			<td colspan="4"><b><digi:trn key="rep:filer:donor">Donor</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select multiple="true" property="selectedDonors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="donors"
					value="ampOrgId" label="name" />
			</html:select>
			</td>
		</tr>

		<tr>
			<td><b><digi:trn key="rep:filer:Status">Status</digi:trn>Status</b></td>
			<td><b><digi:trn key="rep:filer:Risks">Risks</digi:trn></b></td>
			<td>
			<b><digi:trn key="rep:filer:LineMinRank">Line Min. Rank</digi:trn></b>
			</td>
			<td>
			<b><digi:trn key="rep:filer:PlanningMinRank">Planning Min. Rank</digi:trn></b>
			</td>
			
		</tr>
		<tr>
			<td valign="top" >			
					<category:showoptions outerstyle="width: 190px" styleClass="inp-text" property="selectedStatuses" size="3" name="aimReportsFilterPickerForm" multiselect="true" keyName="<%=org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY%>"/>
			</td>			
			<td valign="top" >
			<html:select multiple="true" property="selectedRisks" size="3" styleClass="inp-text">
				<html:optionsCollection property="risks"
					value="ampIndRiskRatingsId" label="ratingName" />
			</html:select>
			</td>
			<td valign="top" >
			<html:select property="lineMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>						
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			<td valign="top" >
			<html:select property="planMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>						
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			
		</tr>
	<logic:notEqual name="widget" value="true" scope="request">
		<tr>
			<td colspan="4"><b>Page Size</b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select property="pageSize" style="width: 100px" styleClass="inp-text">
				<html:optionsCollection property="pageSizes"
					 label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>
</logic:notEqual>
	<tr>
	<td>&nbsp;</td>
	</tr>
		
	<tr>
	<td align="center"  colspan="4">
	<html:submit styleClass="buton" property="apply"><digi:trn key="rep:filer:Apply">Apply</digi:trn></html:submit>&nbsp;
	<html:submit styleClass="buton" property="reset"><digi:trn key="rep:filer:Reset">Reset</digi:trn></html:submit>
	</td>
		
	</tr>
	</table>
</digi:form>
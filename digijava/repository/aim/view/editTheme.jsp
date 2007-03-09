<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--
	function validate() 
	{
		if (trim(document.aimThemeForm.programName.value).length == 0) 
		{
			alert("Please enter Program name");
			document.aimThemeForm.programName.focus();
			return false;
		}	
		if (trim(document.aimThemeForm.programCode.value).length == 0) 
		{
			alert("Please enter Program code");
			document.aimThemeForm.programCode.focus();
			return false;
		}			
		if (trim(document.aimThemeForm.programType.value).length == 0) 
		{
			alert("Please enter Program type");
			document.aimThemeForm.programType.focus();
			return false;
		}
		if(document.aimThemeForm.programType.value == -1)
		{
		  alert("Please Select Program type");
			document.aimThemeForm.programType.focus();
			return false;
		}
		return true;
	}
	function saveProgram(id,rutId,name)
	{
			var temp = validate();
			if(temp == true)
			{
				<digi:context name="editThm" property="context/module/moduleinstance/editTheme.do?event=update"/>
				document.aimThemeForm.action = "<%=editThm%>&themeId=" + id + "&rootId=" + rutId + "&indname=" + name;
				document.aimThemeForm.target = window.opener.name;
				document.aimThemeForm.submit();
				window.close();
			}
			return true;
	}
	function load(){}

	function unload(){}

	function closeWindow() 
	{
		window.close();
	}

-->
</script>

<digi:instance property="aimThemeForm" />
<digi:form action="/themeManager.do" method="post">
<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
	<tr>
		<td align=left class=r-dotted-lg vAlign=top width=300>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="10" align="center" colspan="2"><h4>
						<digi:trn key="aim:editProgram">
									Edit Program
						</digi:trn> </h4>
				</td>
				</tr>
				<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>

				<tr bgColor=#ffffff>
						<td height="10" align="center">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<digi:trn key="aim:programName">
										Program Name</digi:trn>
								<font color="red">*</font>
						</td>
						<td height="10" align="left">
								<html:text property="programName" size="20"/>
						</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programDescription">
						Description</digi:trn>
				</td>
				<td align="left">
						<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgColor=#ffffff>
					<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<digi:trn key="aim:programLeadAgency">
								Lead Agency
						</digi:trn>
					</td>
					<td align="left"><html:textarea property="programLeadAgency"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
				<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programCode">
						Program Code</digi:trn>
						<font color="red">*</font>
				</td>
				<td align="left">
						<html:text property="programCode" size="20" styleClass="inp-text"/>
					
				</td>
				</tr>
				<tr bgColor=#ffffff>
				<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programType">
						Program Type</digi:trn>
						<font color="red">*</font>
				</td>
				<td align="left">
						<html:select property="programType" styleClass="inp-text">
																	<html:option value="-1">Select Progarm Type</html:option>
																		<html:optionsCollection name="aimThemeForm" property="programTypeNames"
													 						value="title" label="title" />
																		</html:select>
						<%--<html:text property="programType" size="20" styleClass="inp-text"/>--%>
				</td>
				</tr>
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programTargetGroups">
							Target Groups
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programTargetGroups" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programBackground">
							Background
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programBackground" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programObjectives">
							Objectives
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programObjectives" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <digi:trn
						key="aim:programOutputs">
																					Outputs
																			</digi:trn></td>
					<td align="left"><html:textarea property="programOutputs" cols="35"
						rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <digi:trn
						key="aim:programBeneficiaries">
																					Beneficiaries
																			</digi:trn></td>
					<td align="left"><html:textarea property="programBeneficiaries"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <digi:trn
						key="aim:programEnvironmentConsiderations">
																					Environment Considerations
																			</digi:trn></td>
					<td align="left"><html:textarea
						property="programEnvironmentConsiderations" cols="35" rows="2"
						styleClass="inp-text" /></td>
				</tr>
				<tr bgcolor=#ffffff><td height="5"></td></tr>	
				<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="2">
						<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId"/>','<bean:write name="aimThemeForm" property="rootId"/>','<bean:write name="aimThemeForm" property="name"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">
				</td>
				</tr>	
		</table>
</digi:form>

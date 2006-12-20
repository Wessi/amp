<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
	function validate() 
	{
		if (trim(document.aimIndicatorForm.indicatorName.value).length == 0) 
		{
			alert("Please enter Indicator name");
			document.aimIndicatorForm.indicatorName.focus();
			return false;
		}	
		if (trim(document.aimIndicatorForm.indicatorCode.value).length == 0) 
		{
			alert("Please enter Indicator code");
			document.aimIndicatorForm.indicatorCode.focus();
			return false;
		}			
		return true;
	}
	
	function addIndicator() 
	{
		var temp = validate();
		if (temp == true) 
		{
			document.aimIndicatorForm.target = "_self";
			document.aimIndicatorForm.submit();			
			
		}
		return temp;
	}

	function load(){
		if (document.aimIndicatorForm.errorFlag.value == "false") {
			<digi:context name="indMan" property="context/module/moduleinstance/indicatorManager.do"/>
		   document.aimIndicatorForm.action = "<%= indMan %>";
			document.aimIndicatorForm.target = window.opener.name;
		   document.aimIndicatorForm.submit();
			window.close();
		}
	}

	function unload(){}

	function closeWindow() 
	{
		window.close();
	}
	-->
</script>

<digi:form action="/addIndicator.do" method="post">
<input type="hidden" name="create" value="false">
<html:hidden name="aimIndicatorForm" property="errorFlag"/>
<html:hidden name="aimIndicatorForm" property="indId"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="15" align="center" colspan="2"><h4>
			Monitoring and Evaluation </h4>
		</td>
	</tr>
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
	<tr bgColor=#ffffff>
		<td bgColor=#ffffff height="15" align="center" colspan="2"><h5>
			<digi:trn key="aim:CreatingNewIndicator">
				Create a New Indicator
			</digi:trn></h5>
		</td>
	</tr>
	<tr bgColor=#ffffff><td height="15" colspan="2" align="center">
		<html:errors />
	</td></tr>

	<tr bgColor=#ffffff>
		<td height="10" align="left">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<digi:trn key="aim:meIndicatorName">
			Indicator Name</digi:trn>
			<font color="red">*</font>
		</td>
		<td height="10" align="left">
			<html:text property="indicatorName" size="20"/>
		</td>
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>
	<tr bgColor=#ffffff>
		<td height="20" align="left">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<digi:trn key="aim:meIndicatorDescription">
			Description</digi:trn>
		</td>
		<td align="left">
			<html:textarea property="indicatorDesc" cols="35" rows="2" styleClass="inp-text"/>
		</td>
	</tr>	
	<tr bgColor=#ffffff>
		<td height="20" align="left">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<digi:trn key="aim:meIndicatorCode">
			Indicator Code</digi:trn>
			<font color="red">*</font>
		</td>
		<td align="left">
			<html:text property="indicatorCode" size="20" styleClass="inp-text"/>
		</td>
	</tr>
	<tr bgColor=#ffffff>
		<td height="20" align="left">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<digi:trn key="aim:meIndicatorType">
			Indicator Type</digi:trn>
			<font color="red">*</font>
		</td>
		<td align="left">
			<html:select property="ascendingInd" styleClass="inp-text">
				<html:option value="A">Ascending</html:option>
				<html:option value="D">Descending</html:option>
			</html:select>
		</td>
	</tr>	
	<tr bgcolor=#ffffff><td height="5"></td></tr>	
	<tr><td colspan="2" align="left">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<html:checkbox property="defaultFlag" />&nbsp;
		<digi:trn key="aim:meCheckToMakeIndicatorDefault">
			Check this to make the indicator Global
		</digi:trn>
	</td></tr>
	<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="25" align="center" colspan="2">
			<input class="dr-menu" type="button" value="Save" onclick="return addIndicator()">&nbsp;&nbsp;
			<input type="reset" class="dr-menu" value="Cancel">&nbsp;&nbsp;
			<input class="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">			
		</td>
	</tr>	
</table>
</digi:form>


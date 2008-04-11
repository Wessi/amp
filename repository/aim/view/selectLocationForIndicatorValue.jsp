<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<script language="javascript">

function closeWindow() {
	window.close();
}

function selectLocation(){
  document.getElementById("indAction").value="add";
  document.forms[0].target=window.opener.name;
  document.forms[0].submit();
  //window.opener.document.forms[0].submit();
  window.close();
}

function countryChanged() {
		  document.aimThemeForm.fill.value = "region";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.action = "<%= selectLoc %>";
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}

	function regionChanged() {
		  document.aimThemeForm.fill.value = "zone";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.action = "<%= selectLoc %>";
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}

	function zoneChanged() {
		  document.aimThemeForm.fill.value = "woreda";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.action = "<%= selectLoc %>";
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}
	
	function levelChanged() {		 
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.action = "<%= selectLoc %>";
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}
</script>
<digi:instance property="aimThemeForm" />
<digi:form action="/selectLocationForIndicatorValue.do" method="post">
  <html:hidden property="action" styleId="indAction" value=""/>
  <html:hidden property="fill" />
  <table width="100%" vAlign="top" border=0>
	<tr><td vAlign="top" width="100%">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr width="100%">
				<td align=left vAlign=top width="100%">
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:selectLocation">
								Select Location</digi:trn>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" cellpadding="2">
									<tr>
										<td align="right" width="50%"><digi:trn key="aim:pleaseSelectLevel">Select level</digi:trn></td>
										<td align="left" width="50%">
											<html:select name="aimThemeForm" property="locationLevelIndex" onchange="levelChanged()">
												<html:option value="-1"><digi:trn key="aim:selectFromBelow">Select From Below</digi:trn></html:option>
												<html:option value="1"><digi:trn key="aim:national">National</digi:trn></html:option>
												<html:option value="2"><digi:trn key="aim:regional">Regional</digi:trn></html:option>
												<html:option value="3"><digi:trn key="aim:district">District</digi:trn></html:option>
											</html:select>
										</td>
									</tr>
									<c:if test="${aimThemeForm.locationLevelIndex>=1}">
										<tr>
											<td align="right"><digi:trn key="aim:country">Country </digi:trn> </td>
											<td align="left"><b><c:out value="${aimThemeForm.country}"></c:out></b></td>
										</tr>
									</c:if>
									<c:if test="${aimThemeForm.locationLevelIndex==2 || aimThemeForm.locationLevelIndex==3}">
										<tr>
											<td align="right" width="50%"><digi:trn key="aim:selectRegion">Select Region</digi:trn></td>
											<td align="left" width="50%">
												<html:select name="aimThemeForm" property="impRegion" onchange="regionChanged()">
													<html:option value="-1">Select Region</html:option>
													<logic:notEmpty name="aimThemeForm" property="regions">
														<html:optionsCollection name="aimThemeForm" property="regions" value="ampRegionId" label="name" />
													</logic:notEmpty>
												</html:select>
											</td>
										</tr>
									</c:if>
									<c:if test="${aimThemeForm.locationLevelIndex==3}">
										<tr>
										<td align="right" width="50%"><digi:trn key="aim:selectZone">Select Zone</digi:trn></td>
										<td align="left" width="50%">
											<html:select property="impZone" onchange="zoneChanged()" styleClass="inp-text" >
												<html:option value="-1">Select Zone</html:option>
												<logic:notEmpty name="aimThemeForm" property="zones">
													<html:optionsCollection name="aimThemeForm" property="zones"value="ampZoneId" label="name" />
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									<tr>
										<td align="right" width="50%"><digi:trn key="aim:selectWoreda">Select Woreda</digi:trn></td>
										<td align="left" width="50%">
											<html:select property="impWoreda"  styleClass="inp-text" >
												<html:option value="-1">Select Woreda</html:option>
												<logic:notEmpty name="aimThemeForm" property="woredas">
													<html:optionsCollection name="aimThemeForm" property="woredas"value="ampWoredaId" label="name" />
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									</c:if>
									
								</table>
							</td>
						</tr>

						<tr bgcolor="#ECF3FD">
							<td align="center">
								<table cellPadding=3 cellSpacing=3>
									<tr>
										<td>
											<input type="button" value="<digi:trn key='btn:add'>Add</digi:trn>" class="dr-menu"
											onclick="selectLocation()">
										</td>
										<td>
											<input type="reset" value="<digi:trn key='btn:clear'>Clear</digi:trn>" class="dr-menu">
										</td>
										<td>
											<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closeWindow()">
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




















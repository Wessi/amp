<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--

function validate(field) {
	if (field == 1) { // validate location
		if (document.aimEditActivityForm.selLocs.checked != null) {
			if (document.aimEditActivityForm.selLocs.checked == false) {
				alert("Please choose a location to remove");
				return false;
			}				  
		} else {
			var length = document.aimEditActivityForm.selLocs.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.selLocs[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose a location to remove");
				return false;					  
			}				  
		}
		return true;
	} else { // validate sector
		if (document.aimEditActivityForm.selActivitySectors.checked != null) {
			if (document.aimEditActivityForm.selActivitySectors.checked == false) {
				alert("Please choose a sector to remove");
				return false;
			}				  
		} else {
			var length = document.aimEditActivityForm.selActivitySectors.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.selActivitySectors[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose a sector to remove");
				return false;					  
			}				  
		}
		return true;			  
	}
}

function selectLocation() {
	if (document.aimEditActivityForm.currUrl.value == "") { 		  		  		  
		openNewWindow(600, 500);
		<digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do" />
		document.aimEditActivityForm.action = "<%= selectLoc %>";
		document.aimEditActivityForm.currUrl.value = "<%= selectLoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();	
	}
} 	

function addSectors() {
	if (document.aimEditActivityForm.currUrl.value == "") { 		  		  
		openNewWindow(600, 450);
		<digi:context name="addSector" property="context/module/moduleinstance/selectSectors.do" />
	  	document.aimEditActivityForm.action = "<%= addSector %>";
		document.aimEditActivityForm.currUrl.value = "<%= addSector %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();	
	}
} 	

function removeSelSectors() {
		  var flag = validate(2);
		  if (flag == false) return false;
		   <digi:context name="remSec" property="context/module/moduleinstance/removeSelSectors.do" />
			document.aimEditActivityForm.action = "<%= remSec %>";
		  	document.aimEditActivityForm.target = "_self"
			document.aimEditActivityForm.submit();
			return true;
} 	

function removeSelLocations() {
		  var flag = validate(1);
		  if (flag == false) return false;
		   <digi:context name="remLocs" property="context/module/moduleinstance/removeSelLocations.do" />
			document.aimEditActivityForm.action = "<%= remLocs %>";
		  	document.aimEditActivityForm.target = "_self"
			document.aimEditActivityForm.submit();
			return true;
} 	

function validateForm() {
	if (document.aimEditActivityForm.selActivitySectors == null) {
		alert("Please add sectors");
		document.aimEditActivityForm.addSec.focus();
		return false;
	}
	gotoStep(3);
	return true;
}

-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">

<html:hidden property="step"/>
<html:hidden property="reset" />
<html:hidden property="country" />
<input type="hidden" name="currUrl">

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
									</bean:define>
									<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">								
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</bean:define>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>																
	
								<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams}" property="step" value="1" />
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>
								</bean:define>
								<digi:link href="/addActivity.do" name="urlParams" styleClass="comment" title="<%=translation%>" >
								<c:if test="${aimEditActivityForm.edit == true}">
									<digi:trn key="aim:editActivityStep1">
										Edit Activity - Step 1
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.edit == false}">
									<digi:trn key="aim:addActivityStep1">
										Add Activity - Step 1
									</digi:trn>
								</c:if>																
								</digi:link>&nbsp;&gt;&nbsp;						
								<digi:trn key="aim:addActivityStep2">
									Step 2
								</digi:trn>
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.edit == false}">
									<digi:trn key="aim:addNewActivity">
										Add New Activity
									</digi:trn>
								</c:if>			
								<c:if test="${aimEditActivityForm.edit == true}">
									<digi:trn key="aim:editActivity">
										Edit Activity
									</digi:trn>
								</c:if>										
							</td>
						</tr>	
					</table>
				</td></tr>
				<tr><td>
					&nbsp;
					<digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT color=red><B><BIG>*</BIG>
					</B></FONT> are required.</digi:trn>					
				</td></tr>
				<tr> <td>
					<digi:errors/>
				</td></tr>
				
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="75%" vAlign="top">	
						<table cellPadding=2 cellSpacing=1 width="100%" bgcolor="#006699">
							<tr>
								<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:step2LocationAndSectors">
								Step 2 of 7: Location | Sectors
								</digi:trn>
								</td>
							</tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="2" cellPadding="2" vAlign="top" align="left" bgcolor="#f4f4f2">							
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2">
																	
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:location">Location</digi:trn></b>
									</td></tr>
									<tr><td>
										<digi:trn key="aim:chooseLocation">
										Choose the area covered by the project.</digi:trn>
									</td></tr>									
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td vAlign="center">

									<table border=0>
									<tr>
									<td>
										<a title="<digi:trn key="aim:impleLevel">Federal and regional programs are the scope of a project. They are a classification of the sponsorship of the project or program. This works in conjunction with location</digi:trn>">
										<digi:trn key="aim:implementLevel">Implementation Level</digi:trn></a>&nbsp;
										</td>
										<td>
										<html:select property="level" styleClass="inp-text">
										<html:option value="-1">Select Level</html:option>
											<html:optionsCollection name="aimEditActivityForm" property="levelCollection" 
											value="ampLevelId" label="name" />
										</html:select>										
									</td></tr>									
					<tr>
					<td vAlign="center" colspan=5>
							<b><FONT color="blue">*</FONT></b>
								Select the appropriate Region, Zone or Woreda as needed.
					</td>
					</tr>
									<tr><td vAlign="center">
										<a title="<digi:trn key="aim:impLocation">The regions, zones and woredas in which the project is implemented</digi:trn>"> 
										<digi:trn key="aim:implementationLoc">Implementation Location </digi:trn></a>&nbsp;
										</td>
										<td vAlign="center">
										<br>
										<html:select property="implementationLevel" styleClass="inp-text">
										<html:option value="country">Country</html:option>
										<html:option value="region">Region</html:option>
										<html:option value="zone">Zone</html:option>
										<html:option value="woreda">Woreda</html:option>
										</html:select>
									</td></tr>
									</table>
									</td></tr>
									<%-- <tr><td vAlign="center">
										<digi:trn key="aim:implementationLevel2">
										Implementation Location</digi:trn> &nbsp;
										<html:radio property="implementationLevel" 
										value="country">Country</html:radio>
										&nbsp;&nbsp;
										<html:radio property="implementationLevel" 
										value="region">Region</html:radio>
										&nbsp;&nbsp;
										<html:radio property="implementationLevel" 
										value="zone">Zone</html:radio>
										&nbsp;&nbsp;
										<html:radio property="implementationLevel" 
										value="woreda">Woreda</html:radio>
									</td></tr> --%>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td >
										<table width="100%" bgcolor="#f4f4f2" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
											<logic:empty name="aimEditActivityForm" property="selectedLocs">
												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Location" class="buton" 
														onclick="selectLocation()">
													</td>
												</tr>
											</logic:empty>
											<logic:notEmpty name="aimEditActivityForm" property="selectedLocs">
												<tr>
													<td>
														<table cellSpacing=2 cellPadding=2 border=0 bgcolor="#ffffff" width="100%">
														<logic:iterate name="aimEditActivityForm" property="selectedLocs" id="selectedLocs"
														type="org.digijava.module.aim.helper.Location">
															<tr><td width="100%"> 
																<table width="100%" cellSpacing=2 cellPadding=2 vAlign="top" align="left">
																	<tr>
																		<td width="3" vAlign="center">
																			<html:multibox property="selLocs">
																				<bean:write name="selectedLocs" property="locId" />
																			</html:multibox>																		
																		</td>
																		<td vAlign="center" align="left">
																			<c:if test="${!empty selectedLocs.country}">
																				[<bean:write name="selectedLocs" property="country"/>]
																			</c:if>
																			<c:if test="${!empty selectedLocs.region}">
																				[<bean:write name="selectedLocs" property="region"/>]
																			</c:if>
																			<c:if test="${!empty selectedLocs.zone}">
																				[<bean:write name="selectedLocs" property="zone"/>]
																			</c:if>
																			<c:if test="${!empty selectedLocs.woreda}">
																				[<bean:write name="selectedLocs" property="woreda"/>]
																			</c:if>																		
																		</td>
																	</tr>	
																</table>
															</td></tr>
														</logic:iterate>
															<tr><td>
																<table cellSpacing=2 cellPadding=2>
																	<tr>
																		<td>
																			<input type="button" value="Add Location" class="buton" 
																			onclick="selectLocation()">
																		</td>
																		<td>
																			<input type="button" value="Remove Location" class="buton" 
																				onclick="return removeSelLocations()">
																		</td>
																	</tr>
																</table>
															</td></tr>
														</table>
													</td>
												</tr>
											</logic:notEmpty>
											
										</table>
										<!-- Add Location -->
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><FONT color=red>*</FONT>
										<a title="<digi:trn key="aim:impSector">The OECD/DAC Creditor Reporting System (CRS) codes are used by all 23 OECD/DAC members when they report on their aid activities to the DAC Secretariat. The complete list of CRS codes and definitions and principles can be found in Annex 3. � In the CRS, data on the sector of destination are recorded using 5-digit purpose codes. The first three digits of the code refer to the main sector or category (i.e. 112 for Basic education, or 210 for Transport and storage). The last two digits of the CRS purpose code allow providing more detailed classification (i.e. 11240 for Early childhood education, or 21020 for Rail transport). � For the purpose of AMP, if the 5-digits codification is too detailed and not relevant, only 3-digits codes may be used. � One and only one purpose code should be applied to each project. In case of multi-sector projects, use the CRS codes 400xx. � Non-sector activities (i.e. general budget support, debt, emergency aid, NGOs) are covered by the CRS, under codes 500xx to 900xx. </digi:trn>"> 
										<digi:trn key="aim:sector">Sector</digi:trn></b>
										</a>
									</td></tr>
									<tr><td>
									<FONT color=red>*</FONT>
										<digi:trn key="aim:chooseSector">
										Choose the sector.</digi:trn>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<table width="100%" bgcolor="#f4f4f2" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
										
<!-- 											<logic:empty name="aimEditActivityForm" property="activitySectors">
 -->												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Sector" class="buton" name="addSec"
														onclick="addSectors()">
													</td>
												</tr>
											<!-- </logic:empty> -->
											<logic:notEmpty name="aimEditActivityForm" property="activitySectors">
												<tr>
													<td>
														<table cellSpacing=2 cellPadding=2 border=0 bgcolor="#ffffff" width="100%">
														<logic:iterate name="aimEditActivityForm" property="activitySectors" 
														id="actSect" type="org.digijava.module.aim.helper.ActivitySector">
															<tr><td> 
																<table width="100%" cellSpacing=2 cellPadding=2 vAlign="top" align="left">
																	<tr>
																		<td width="3" vAlign="center">
																			<html:multibox property="selActivitySectors">
																				<bean:write name="actSect" property="id" />
																			</html:multibox>
																		</td>
																		<td vAlign="center" align="left">
																			<c:if test="${!empty actSect.sectorName}">
																				[<bean:write name="actSect" property="sectorName"/>]
																			</c:if>
																			<c:if test="${!empty actSect.subsectorLevel1Name}">
																				[<bean:write name="actSect" property="subsectorLevel1Name"/>]
																			</c:if>
																			<c:if test="${!empty actSect.subsectorLevel2Name}">
																				[<bean:write name="actSect" property="subsectorLevel2Name"/>]
																			</c:if>
																		</td>
																	</tr>	
																</table>
															</td></tr>
														</logic:iterate>
															<tr><td>
																<table cellSpacing=2 cellPadding=2>
																	<tr>
<!--																		
																		<td>
																			<input type="button" value="Add Sectors" class="buton" 
																			onclick="addSectors()">
																		</td>
-->																		
																		<td>
																			<input type="button" value="Remove Sector" class="buton" 
																				onclick="return removeSelSectors()">
																		</td>
																	</tr>
																</table>
															</td></tr>
														</table>
													</td>
												</tr>
											</logic:notEmpty>
										</table>
										<!-- Add Sectors -->
									</td></tr>									
									<tr><td>
										&nbsp;
									</td></tr>									
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:ProgramImp">Set of policies, projects and strategies grouped by area</digi:trn>"> 
										<b><digi:trn key="aim:program">Program</digi:trn></b>
										</a>
									</td></tr>
									<tr><td>
										<digi:trn key="aim:selectProgram">
										Select the program from the list.</digi:trn>
									</td></tr>	
									<tr><td>
										<html:select property="program" styleClass="inp-text">
											<html:option value="-1">--- Select program ---</html:option>
											<html:optionsCollection name="aimEditActivityForm" property="programCollection" 
											value="ampThemeId" label="name" />
										</html:select>
									</td></tr>
									<tr><td>
									<a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
									Description
									</a>
									</td></tr>	
									<tr><td>
									<a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
										<html:textarea property="programDescription" rows="3" cols="75" styleClass="inp-text"/>
									</a>
									</td></tr>									 
									<tr><td>&nbsp;</td></tr>
								</table>

								<!-- end contents -->
							</td></tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
							<tr><td bgColor=#f4f4f2 align="center">
								<table cellPadding=3>
									<tr>
										<td>
											<input type="submit" value=" << Back " class="dr-menu" onclick="gotoStep(1)">
										</td>
										<td>
											<input type="submit" value="Next >> " class="dr-menu" onclick="return validateForm()">
										</td>
										<td>
											<input type="reset" value="Reset" class="dr-menu">
										</td>
									</tr>
								</table>
							</td></tr>
							</table>
							</td></tr>							
						</table>						
						</td>
						<td width="25%" vAlign="top" align="right">
						<!-- edit activity form menu -->
							<jsp:include page="editActivityMenu.jsp" flush="true" />
						<!-- end of activity form menu -->							
						</td></tr>
					</table>
				</td></tr>
				<tr><td>
					&nbsp;
				</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>

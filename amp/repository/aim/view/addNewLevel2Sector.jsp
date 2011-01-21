<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Delete this Sector?");
		return flag;
	}
	
	
	function updateScheme(id) {	
		if(isEmpty(document.aimAddSectorForm.sectorName.value)==true)
		{
			<c:set var="translation">
 					<digi:trn key="aim:EnterSectorName">Please, enter a sector name</digi:trn>
			</c:set>   			 
			alert("${translation}");
		}	
		else if(isEmpty(document.aimAddSectorForm.sectorCodeOfficial.value)==true)
		{
			<c:set var="translation">
  				<digi:trn key="aim:EnterSectorCode">Please, enter a Sector Code</digi:trn>
			</c:set>             		
    		alert("${translation}");
		}
		else{	
			<digi:context name="updateSector" property="context/module/moduleinstance/editSector.do?event=update2LevelSector" />
			document.aimAddSectorForm.action = "<%= updateSector%>&id="+id;
			document.aimAddSectorForm.target = "_self";
			document.aimAddSectorForm.submit();
		}
	
	}

</script>

<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/addSector.do" method="post">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home 
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/getSectorSchemes.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:schemes">
						Schemes
						</digi:trn>
						</digi:link>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
					<digi:trn key="aim:sectormanager">
					Sector Manager
					</digi:trn>
					</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>				
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									
									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
										
										<tr>
											<td>	
												<table width="100%">
													<tr>
														<td>
														<digi:trn key="aim:sectorManagerLevel">
															Sector Name
														</digi:trn><font color="red">*</font>:
														</td>
														<td>
															<html:textarea  name ="aimAddSectorForm" property="sectorName" rows="1" cols= "35"/> 
														</td>
													</tr>
													<tr>
														<td>
														<digi:trn key="aim:sectorCode">
															Sector Code
														</digi:trn><font color="red">*</font>:
														</td>
														<td>
															<html:text name ="aimAddSectorForm" property="sectorCodeOfficial" size="5"/> 
														</td>
													</tr>
													<tr>
														<td>
														<digi:trn key="aim:sectordescription">
															Sector Description :
														</digi:trn>
														</td>
														<td>
														<html:textarea name="aimAddSectorForm" cols="60" rows="3" styleClass="inp-text" property="description"/>															 
														</td>
													</tr>

													<%--<tr>
														<td>
															Scheme Code :
														</td>
														<td>
															<html:text name ="aimSectorSchemeForm" property="secSchemeId" size="5"/> 
														</td>
													</tr>--%>

													
													<tr>
														<td>
															&nbsp;
														</td>
														<td >&nbsp;&nbsp;
								<input  type="button" name="addBtn" value="Save" onclick="updateScheme('<bean:write name="aimAddSectorForm" property="sectorId" />')"/>
														<td>
													</tr>
											</table>
											</td>
											
										</tr>	
										<field:display name="Level 2 Sectors List" feature="Sectors">
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:LeveltwoSectors">
												Level Two Sectors
											</digi:trn>
											<!-- end table title -->										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
													<logic:empty name="aimAddSectorForm" property="subSectors">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noSectorPresent">
															No Sector present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													
													<logic:notEmpty name="aimAddSectorForm" property="subSectors">
													<logic:iterate name="aimAddSectorForm" property="subSectors" id="sectorLevelTwo"
																	type="org.digijava.module.aim.dbentity.AmpSector	">
													<tr>
														<td bgcolor="#ffffff">
															<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams2}" property="ampSectorId">
															<bean:write name="sectorLevelTwo" property="ampSectorId" />
															</c:set>
															<c:set target="${urlParams2}" property="event" value="edit" />
															<c:set target="${urlParams2}" property="level" value="three" />
															<c:set var="translation">
																<digi:trn key="aim:clickToViewSector">Click here to view Sector</digi:trn>
															</c:set>
															<digi:link href="/viewSectorDetails.do" name="urlParams2" title="${translation}" >
															<bean:write name="sectorLevelTwo" property="name"/></digi:link>
														</td>
														
														<td bgcolor="#ffffff" width="75" align="center">
															<c:set var="translation">
																<digi:trn key="aim:clickToEditSector">Click here to Edit Sector</digi:trn>
															</c:set>
															<c:set var="edittext">
																<digi:trn key="aim:edit">Edit</digi:trn>
															</c:set>
															[ <digi:link href="/viewSectorDetails.do" name="urlParams2" title="${translation}" >${edittext}</digi:link> ]
														</td>
														<td bgcolor="#ffffff" width="60" align="center">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="ampSectorId">
																<bean:write name="sectorLevelTwo" property="ampSectorId" />
															</c:set>
															<c:set target="${urlParams4}" property="schemeId">
																<bean:write name="aimAddSectorForm" property="parentSectorId" />
															</c:set>
															<c:set target="${urlParams4}" property="event" value="delete"/>
															<c:set var="translation">
																<digi:trn key="aim:clickToDeleteSector">Click here to Delete Sector</digi:trn>
															</c:set>
															<c:set var="deletetext">
																<digi:trn key="aim:delete">Delete</digi:trn>
															</c:set>
															[ <digi:link href="/deleteSector.do" name="urlParams4" 
																title="${translation}" onclick="return onDelete()">${deletetext}</digi:link> ]
														</td>
													</tr>
													</logic:iterate>

																									
													
													</logic:notEmpty>
													<!-- end page logic -->													
											</table>
										</td></tr>
										</field:display>
									</table>
									
								</td>
							</tr>
						</table>
					</td>
					
					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<field:display name="Add Sector Level 2 Link" feature="Sectors">
										 <tr>
											<td>
												<jsp:useBean id="urlParams5" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams5}" property="ampSecSchemeId">
													<bean:write name="aimAddSectorForm" property="sectorId" />
												</c:set>
												<c:set target="${urlParams5}" property="parent" value="sector"/>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddSector">Click here to Add a Sector</digi:trn>
												</c:set>
												
												<digi:link href="/addSector.do" name="urlParams5" title="${translation}" >
												<digi:trn key="aim:addSector">
												Add Sector
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										</field:display>
										
										<field:display name="View Schemes Link" feature="Sectors">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewSchemes">Click here to the Schemes</digi:trn>
												</c:set>
												<digi:link href="/getSectorSchemes.do" title="${translation}" >
												<digi:trn key="aim:viewSchemes">
												View Schemes
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										</field:display>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>





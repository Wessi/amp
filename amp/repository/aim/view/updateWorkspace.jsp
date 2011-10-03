<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>


<html:javascript formName="aimUpdateWorkspaceForm"/>


<digi:instance property="aimUpdateWorkspaceForm" />
<digi:context name="digiContext" property="context" />

<digi:form action="/updateWorkspace.do" method="post" name="aimUpdateWorkspaceForm"
type="org.digijava.module.aim.form.UpdateWorkspaceForm"
onsubmit="return validateAimUpdateWorkspaceForm(this);">

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

function addChildWorkspaces() {
		if (document.aimUpdateWorkspaceForm.workspaceType.value != "Team") {
			openNewWindow(650, 380);
			<digi:context name="addChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
			document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=admin";
			document.aimUpdateWorkspaceForm.target = popupPointer.name;
			document.aimUpdateWorkspaceForm.addFlag.value = false;
			document.aimUpdateWorkspaceForm.submit();
		} else {
			alert("<digi:trn key="aim:teamcaddmanagement">Workspace type must be MANAGEMENT to add teams</digi:trn>");
			document.aimUpdateWorkspaceForm.workspaceType.focus();
			return false;
		}
}

function addChildOrgs() {
			
			openNewWindow(650, 380);
			<digi:context name="addChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
			document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=admin&childorgs=true";
			document.aimUpdateWorkspaceForm.target = popupPointer.name;
			document.aimUpdateWorkspaceForm.addFlag.value = false;
			document.aimUpdateWorkspaceForm.submit();
}


function removeChildOrg(id) {
	var temp = confirm("<digi:trn key="aim:deletelinkedorganization">Do you want to delete this linked organization ?</digi:trn>");
	if(temp == false)
	{
			document.aimUpdateWorkspaceForm.submit();
			return false;
	}
	else
	{
	<digi:context name="update" property="context/module/moduleinstance/removeChildWorkspace.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&childorgs=true&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.addFlag.value = false;
	document.aimUpdateWorkspaceForm.submit();
	}
}



function removeChildWorkspace(id) {
	var temp = confirm("<digi:trn key="aim:deletechildworkspace">Do you want to delete this child workspace ?</digi:trn>");
	if(temp == false)
	{
			document.aimUpdateWorkspaceForm.submit();
			return false;
	}
	else
	{
	<digi:context name="update" property="context/module/moduleinstance/removeChildWorkspace.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.addFlag.value = false;
	document.aimUpdateWorkspaceForm.submit();
	}
}

function chekingWorkspaceTypeAndCatValue(action){
	if(action!="reset") {
		var index2  = document.aimUpdateWorkspaceForm.workspaceType.selectedIndex;
		var val2    = document.aimUpdateWorkspaceForm.workspaceType.options[index2].value;
		var msg='';
		/*
		if(val1 == "DONOR" && (val2 == "Team"|| val2=="Management")){
			msg+="<digi:trn key="aim:workspaceManager:selectDonorType">if you choose Donor Team Category, you must choose Donor workspace type and vice versa</digi:trn>";
			alert(msg);
			return false;
		}else if(val1 == "GOVERNMENT" && val2 == "Donor"){
			msg+="<digi:trn key="aim:workspaceManager:selectGivernmentType">if you choose Government Team Category, you must choose Team or Management workspace type and vice versa</digi:trn>";
			alert(msg);
			return false;
		} else return true;
		*/
	}
	return true;
}

function update1(action, tid){

	if (action == "editreset"){
		<digi:context name="update" property="context/module/moduleinstance/getWorkspace.do" />
		//document.aimUpdateWorkspaceForm.action = "<%=update%>~dest=admin&event="+action;
		//document.aimUpdateWorkspaceForm.target = "_self";
		//document.aimUpdateWorkspaceForm.submit();
		window.location="<%=update%>~dest=admin~event=edit~tId="+tid;
	}
	return true;
}

function update(action) {
	if (action == "reset"){
		<digi:context name="update" property="context/module/moduleinstance/createWorkspace.do" />
		document.aimUpdateWorkspaceForm.action = "<%=update%>~dest=admin&event="+action;
		document.aimUpdateWorkspaceForm.target = "_self";
		//document.aimUpdateWorkspaceForm.submit();
		window.location="<%=update%>?dest=admin&event="+action;
	}
	
	//alert("comput: "+document.aimUpdateWorkspaceForm.computation.value);
	//alert("addact: "+document.aimUpdateWorkspaceForm.addActivity.checked);
	//alert("orgs: "+document.aimUpdateWorkspaceForm.organizations.value);
	if(chekingWorkspaceTypeAndCatValue(action)==true){	
		var event	= document.aimUpdateWorkspaceForm.actionEvent.value;
		var relFlag = document.aimUpdateWorkspaceForm.relatedTeamFlag.value;
		if (event == "edit" && relFlag == "noedit") {
			var name = trim(document.aimUpdateWorkspaceForm.teamName.value);
			if (name == "" || name.length == 0) {
				alert("<digi:trn key="aim:teamnamerequired">Team name required</digi:trn>");
				document.aimUpdateWorkspaceForm.teamName.focus();
				return false;
			}
			else {
			
				document.aimUpdateWorkspaceForm.relatedTeamFlag.value = "no";
				document.aimUpdateWorkspaceForm.addFlag.value = false;
				<digi:context name="update" property="context/module/moduleinstance/updateWorkspace.do" />
				document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&event="+action;
				document.aimUpdateWorkspaceForm.target = "_self";
				document.aimUpdateWorkspaceForm.submit();
			}
		}
		else
		if (action != "reset"){
			document.aimUpdateWorkspaceForm.workspaceType.disabled = false;
			if (!validateAimUpdateWorkspaceForm(document.aimUpdateWorkspaceForm)){
				return false;
			}
		    if (action != "reset"){
			if (event == "add" || event == "edit") {
				if (relFlag == "set") {
					var index2  = document.aimUpdateWorkspaceForm.workspaceType.selectedIndex;
					var index3  = document.aimUpdateWorkspaceForm.relatedTeam.selectedIndex;
					var index4  = document.aimUpdateWorkspaceForm.typeId.selectedIndex;
					var val2    = document.aimUpdateWorkspaceForm.workspaceType.options[index2].value;
					var val3    = document.aimUpdateWorkspaceForm.relatedTeam.options[index3].value;
					var val4	= document.aimUpdateWorkspaceForm.relatedTeamFlag.value;
					var val5	= document.aimUpdateWorkspaceForm.typeId.options[index4].value;
					var lab5	= document.aimUpdateWorkspaceForm.typeId.options[index4].text;
					var bsize	= parseInt(document.aimUpdateWorkspaceForm.relatedTeamBilatCollSize.value, 10);
					var val6	= document.aimUpdateWorkspaceForm.relatedTeamFlag.value;
	
					if (val1 == "DONOR" && val2 == "Donor") {
						if (val5 == "0") {
								alert("<digi:trn key="aim:selectteamtype">Please select team type</digi:trn>");
								document.aimUpdateWorkspaceForm.typeId.focus();
								return false;
							}
							else if (val3 == "-1") {
								alert("<digi:trn key="aim:selectrelatedteam">Please select a related team</digi:trn>");
								document.aimUpdateWorkspaceForm.relatedTeam.focus();
								return false;
							}
							else {
								if (lab5 == "<%= org.digijava.module.aim.helper.Constants.TEAM_TYPE_BILATERAL %>") {
									if (bsize != 0) {
										if (index3 > bsize) {
											alert("<digi:trn key="aim:selectbilateralteam">Please select bilateral team</digi:trn>");
											document.aimUpdateWorkspaceForm.relatedTeam.focus();
											return false;
										}
									}
								}
								if (lab5 == "<%= org.digijava.module.aim.helper.Constants.TEAM_TYPE_MULTILATERAL %>") {
									if (bsize != 0) {
										if (index3 <= bsize) {
											alert("<digi:trn key="aim:selectmultilateralteam">Please select multilateral team</digi:trn>");
											document.aimUpdateWorkspaceForm.relatedTeam.focus();
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
			document.aimUpdateWorkspaceForm.relatedTeamFlag.value = "no";
			document.aimUpdateWorkspaceForm.addFlag.value = false;
			<digi:context name="update" property="context/module/moduleinstance/updateWorkspace.do" />
			document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&event="+action;
			document.aimUpdateWorkspaceForm.target = "_self";
			document.aimUpdateWorkspaceForm.submit();
		}
		
	}	
}
  
function workspaceChangeType(){
	if(document.aimUpdateWorkspaceForm.workspaceType.value == "Team"){
		$("tr[id^='management_']").hide();
		$("tr[id^='team_']").show('fast');
		//document.aimUpdateWorkspaceForm.addActivity.checked = true;
		if(document.aimUpdateWorkspaceForm.computation.checked == true)
			$("tr[id^='computation_']").show('fast');
		else $("tr[id^='computation_']").hide();
	}

	if(document.aimUpdateWorkspaceForm.workspaceType.value == "Management"){
		//document.aimUpdateWorkspaceForm.addActivity.checked = false;
		//document.aimUpdateWorkspaceForm.computation.checked = false;
			$("tr[id^='team_']").hide();
			$("tr[id^='management_']").show('fast');
			$("tr[id^='computation_']").hide()
	}

}
 
function   computationChange(){
	if(document.aimUpdateWorkspaceForm.computation.checked == true)
		$("tr[id^='computation_']").show('fast');
	else $("tr[id^='computation_']").hide();
}
  
function relTeam() { 
	
	var index2  = document.aimUpdateWorkspaceForm.workspaceType.selectedIndex;
	var index3  = document.aimUpdateWorkspaceForm.typeId.selectedIndex;
	var val2    = document.aimUpdateWorkspaceForm.workspaceType.options[index2].value;
	var val3    = document.aimUpdateWorkspaceForm.typeId.options[index3].value;
	var val4	= document.aimUpdateWorkspaceForm.relatedTeamFlag.value;

	
	if (val1 == "DONOR" && val2 == "Donor") {
		if (val4 == "no") {
			if (val3 != "0") {
				document.aimUpdateWorkspaceForm.relatedTeamFlag.value = "yes";
				document.aimUpdateWorkspaceForm.addFlag.value = false;
				<digi:context name="update" property="context/module/moduleinstance/updateWorkspace.do" />
				document.aimUpdateWorkspaceForm.action = "<%=update%>";
				document.aimUpdateWorkspaceForm.target = "_self";
				document.aimUpdateWorkspaceForm.submit();
			}
			else
				return false;
		}
		if (document.getElementById("relTeamRow"))
			document.getElementById("relTeamRow").style.display = '';
		return false;
	}
	else {
		if (document.getElementById("relTeamRow"))
			document.getElementById("relTeamRow").style.display = 'none';
		return false;
	}
}
function cancel()
 {
//	document.aimUpdateWorkspaceForm.action = "/aim/workspaceManager.do";
//	document.aimUpdateWorkspaceForm = "_self";
//	document.aimUpdateWorkspaceForm.submit();
	window.location="/aim/workspaceManager.do";
	return true;
 }
-->
</script>




<html:hidden property="teamId" />
<html:hidden property="actionEvent" />
<html:hidden property="id" />
<html:hidden property="mainAction" />

<html:hidden property="stepInWizard" value="1" />

<html:hidden property="relatedTeamFlag" />
<html:hidden property="addFlag" />
<html:hidden property="relatedTeamBilatCollSize" />

<input type="hidden" name="event">
<input type="hidden" name="dest">

<input type="hidden" name="currUrl">

<table width="1000" cellPadding="0" cellSpacing="0" vAlign="top" align="center">
<tr><td vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td vAlign="top" align="left">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 vAlign="top" align="left">
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/workspaceManager.do" styleClass="comment">
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
						<digi:trn key="aim:addTeam">Add Team</digi:trn>
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
						<digi:trn key="aim:editTeam">Edit Team</digi:trn>
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
						<digi:trn key="aim:deleteTeam">Delete Team</digi:trn>
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
						<digi:trn key="aim:viewTeam">View Team</digi:trn>
						</logic:equal></span>
					</td>
				</tr>
				<!--<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:workspaceManager">Workspace Manager</digi:trn></span>
					</td>
				</tr>-->
				<tr>
					<td>
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr><td noWrap width=750 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" border="0">
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr bgColor=#f4f4f2>
														<td bgColor=#C7D4DB class=box-title height="25" align="center" style="font-size:12px; font-weight:bold;">
															<!-- Table title -->
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<digi:trn key="aim:addTeam">Add Team</digi:trn>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<digi:trn key="aim:editTeam">Edit Team</digi:trn>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<digi:trn key="aim:deleteTeam">Delete Team</digi:trn>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<digi:trn key="aim:viewTeam">View Team</digi:trn>
															</logic:equal>
															<!-- end table title -->
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border="0" cellPadding=3 cellspacing="1" width="100%" bgcolor="#dddddd">
													<tr>
														<td width="150" align="right" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<font color="red"><b>*</b></font>
															<digi:trn key="aim:teamName">Team Name</digi:trn>
														</td>
														<td align="left" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="teamName" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="teamName" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<html:text property="teamName" size="50" styleClass="inp-text" />
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<html:text property="teamName" size="50" styleClass="inp-text" />
															</logic:equal>
														</td>
													</tr>
													
													<tr>
														<td align="right" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<font color="red"><b>*</b></font>
															<digi:trn key="aim:workspaceType">Workspace Type</digi:trn>
														</td>
														<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
														<td align="left" bgcolor="#FFFFFF">
															<html:select property="workspaceType" styleClass="inp-text" onchange="workspaceChangeType()">
																<html:option value="-1"><digi:trn key="aim:selectWorkspace">-- Select Workspace --</digi:trn></html:option>
																<html:option value="Management"><digi:trn key="aim:management">Management</digi:trn></html:option>
																<html:option value="Team"><digi:trn key="aim:team">Team</digi:trn></html:option>
															</html:select>
														</td>
														</c:if>
														<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'add'}">
														<td align="left" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<c:choose>
																<c:when test="${aimUpdateWorkspaceForm.actionEvent == 'edit'}">
																	<c:choose>
																		<c:when test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'noedit'}">
																			<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceType" /></b>
																		</c:when>
																		<c:otherwise>
																			<html:select property="workspaceType" styleClass="inp-text" disabled="true" >
																				<html:option value="-1"><digi:trn key="aim:selectWorkspace">-- Select Workspace --</digi:trn></html:option>
																				<html:option value="Management" ><digi:trn key="aim:management">Management</digi:trn></html:option>
																				<html:option value="Team" ><digi:trn key="aim:team">Team</digi:trn></html:option>
																			</html:select>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceType" /></b>
																</c:otherwise>
															</c:choose>
														</td>
														</c:if>
													</tr>
													
													
													<tr>
														<td align="right" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<digi:trn key="aim:description">Description</digi:trn>
														</td>
														<td align="left" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="description" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="description" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
															</logic:equal>
														</td>
													</tr>
													
													<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
														<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag != 'no'}">
															<tr  id="relTeamRow">
																<td align="right" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																	<digi:trn key="aim:relatedTeam">Related Team</digi:trn>
																</td>
																<td align="left" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																	<html:select property="relatedTeam" styleClass="inp-text">
																		<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'nil'}">
																			<html:option value="-1">No related team available</html:option>
																		</c:if>
																		<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'set'}">
																			<html:option value="-1">-- Select Team --</html:option>
																			<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamBilatColl" >
																				<optgroup label="Bilateral" style="FONT-WEIGHT: bold;COLOR: #cc0000;">
																				<%--<html:option value="-1" style="FONT-WEIGHT: bold;COLOR: #cc0000;">-- Bilateral --</html:option>--%>
																				<html:optionsCollection name="aimUpdateWorkspaceForm" property="relatedTeamBilatColl"
																										value="ampTeamId" label="name" />
																				</optgroup>
																			</logic:notEmpty>
																			<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamMutilatColl" >
																				<optgroup label="Multilateral" style="FONT-WEIGHT: bold;COLOR: #006600">
																				<%--<html:option value="-1">-- Multilateral --</html:option>--%>
																				<html:optionsCollection name="aimUpdateWorkspaceForm" property="relatedTeamMutilatColl"
																										value="ampTeamId" label="name" />
																				</optgroup>
																			</logic:notEmpty>
																		</c:if>
																	</html:select>
																</td>
															</tr>
														</c:if>
													</c:if>
													
													<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'add'}">
														<c:if test="${aimUpdateWorkspaceForm.category == 'DONOR' && aimUpdateWorkspaceForm.workspaceType == 'Donor'}">
															<tr id="relTeamRow">
																<td align="right" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																	<digi:trn key="aim:relatedTeam">Related Team</digi:trn>
																</td>
																<td align="left" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																	<c:choose>
																		<c:when test="${aimUpdateWorkspaceForm.actionEvent == 'edit' && aimUpdateWorkspaceForm.relatedTeamFlag != 'noedit'}">
																			<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'nil'}">
																				<html:select property="relatedTeam" styleClass="inp-text">
																					<html:option value="-1">No related team available</html:option>
																				</html:select>
																			</c:if>
																			<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'set'}">
																				<html:select property="relatedTeam" styleClass="inp-text">
																					<html:option value="-1">-- Select Team --</html:option>
																					<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamBilatColl" >
																						<optgroup label="Bilateral" style="FONT-WEIGHT: bold;COLOR: #cc0000;">
																						<%--<html:option value="-1" style="FONT-WEIGHT: bold;COLOR: #cc0000;">-- Bilateral --</html:option>--%>
																						<html:optionsCollection name="aimUpdateWorkspaceForm" property="relatedTeamBilatColl"
																												value="ampTeamId" label="name" styleClass="COLOR: #cc0000;" />
																						</optgroup>
																					</logic:notEmpty>
																					<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamMutilatColl" >
																						<optgroup label="Multilateral" style="FONT-WEIGHT: bold;COLOR: #006600">
																						<%--<html:option value="-1">-- Multilateral --</html:option>--%>
																						<html:optionsCollection name="aimUpdateWorkspaceForm" property="relatedTeamMutilatColl"
																												value="ampTeamId" label="name" styleClass="COLOR: #006600" />
																						</optgroup>
																					</logic:notEmpty>
																				</html:select>
																			</c:if>
																		</c:when>
																		<c:otherwise>
																			<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeam" >
																				<b><c:out value="${aimUpdateWorkspaceForm.relatedTeamName}" /></b>
																			</logic:notEmpty>
																			<logic:empty name="aimUpdateWorkspaceForm" property="relatedTeam" >
																				<b><digi:trn key="aim:noRelatedTeam">No related team found</digi:trn></b>
																			</logic:empty>
																		</c:otherwise>
																	</c:choose>
																</td>
															</tr>
														</c:if>
													</c:if>
													
													
															
											
													
													<tr id="management_workspace" style="display: none;background: #FFFFFF">
														<td colspan="4">
																	<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																		<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag != 'noedit'}">
																			<table>
																			<tr>
																				<td align="right" width="150" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																					<digi:trn key="aim:childWorkspacesOrTeams">Child Workspaces/Teams</digi:trn>
																				</td>
																				<td align="left" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																					<c:set var="translation">
																						<digi:trn key="btn:createWorkspaceAdd">
																							Add
																						</digi:trn>
																					</c:set>
																					<input type="button" value="${translation}" class="dr-menu" onclick="addChildWorkspaces()">
																				</td>
																			</tr>
																			</table>
																		</c:if>
																	</c:if>
																	<c:if test="${!empty aimUpdateWorkspaceForm.childWorkspaces}">
																	
																			<table width="98%" cellPadding=2 cellspacing="0" valign="top" align="center"
																			class="box-border-nopadding">
																			<c:forEach var="workspaces" items="${aimUpdateWorkspaceForm.childWorkspaces}">
																				<tr>
																					<td align="left">&nbsp;
																						<c:out value="${workspaces.name}"/>
																					</td>
																					<td align="right" width="10">
																						<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																						<a href="javascript:removeChildWorkspace(<c:out value="${workspaces.ampTeamId}"/>)">
																					 	<digi:img src="../ampTemplate/images/deleteIcon.gif"
																						border="0" alt="Remove this child workspace"/></a>&nbsp;
																						</c:if>
																					</td>
																				</tr>
																			</c:forEach>
																			</table>
																		
																	</c:if>
														</td>
													</tr>
													

													<tr id="team_workspace" style="display: none;background: #FFFFFF">
														<td colspan="4">
															
															<table>
																<tr>
																	<td style="font-size:12px; font-weight:bold;">
																		<html:checkbox property="computation"  value="true" onclick="computationChange()"><digi:trn key="chk:computation">Computation</digi:trn></html:checkbox>
																	</td>
																</tr>
															</table>
															
														</td>
													</tr>
													
													<tr  id="computation_addon" style="display: none;background: #FFFFFF">
														<td colspan="4">
																<table>
																	<tr>
																		<td>
																			<html:checkbox property="addActivity" value="true"><digi:trn key="chk:addActivity">Add Activity</digi:trn></html:checkbox>
																		</td>
																	</tr>
																	
																	<tr>
																		<td>
																			<html:checkbox property="hideDraftActivities" value="true"><digi:trn>Hide draft activities</digi:trn></html:checkbox>
																		</td>
																	</tr>
																	
																</table>
																<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																	<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag != 'noedit'}">
																	<table>
																		<tr>
																			<td align="right" width="150" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																				<digi:trn key="aim:childrenOrganizations">Children (Organizations)</digi:trn>
																			</td>
																			<td align="left" bgcolor="#FFFFFF">
																				<c:set var="translation">
																					<digi:trn key="btn:createWorkspaceAdd">
																						Add
																					</digi:trn>
																				</c:set>
																				<input type="button" value="${translation}" class="dr-menu" onclick="addChildOrgs()">
																			</td>
																		</tr>
																		</table>
																	</c:if>
																</c:if>
																<c:if test="${!empty aimUpdateWorkspaceForm.organizations}">
																
																		<table width="98%" cellPadding=2 cellspacing="0" valign="top" align="center"
																		class="box-border-nopadding">
																		<c:forEach var="org" items="${aimUpdateWorkspaceForm.organizations}">
																			<tr>
																				<td align="left">&nbsp;
																					<c:out value="${org.name}"/>
																				</td>
																				<td align="right" width="10">
																					<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																					<a href="javascript:removeChildOrg(<c:out value="${org.ampOrgId}"/>)">
																				 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Remove this linked org"/></a>&nbsp;
																					</c:if>
																				</td>
																			</tr>
																		</c:forEach>
																		</table>
																	</c:if>
																
														</td>
													</tr>
													
													
													
													<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
													<tr>
														<td colspan="2" align="center" bgcolor="#FFFFFF">
															<table cellPadding=5>
																<tr>
																	<td>
																		<c:set var="translation">
																			<digi:trn key="btn:createWorkspaceSave">
																				Save
																			</digi:trn>
																		</c:set>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
																			<input type="button" value="${translation}" class="buttonx"
																			onclick="update('add')"/>
																		</c:if>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'edit'}">
																			<input type="button" value="${translation}" class="buttonx" onclick="update('edit')"/>
																		</c:if>
																	</td>
																	<td>
																		<!-- <html:reset value="Clear" styleClass="dr-menu"/> -->
																		<c:set var="translation">
																			<digi:trn key="btn:createWorkspaceReset">
																				Reset
																			</digi:trn>
																		</c:set>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'edit'}">
																			<input type="button" value="${translation}" class="buttonx" onclick="update1('editreset',${aimUpdateWorkspaceForm.teamId})"/>
																		</c:if>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
																			<input type="button" value="${translation}" class="buttonx" onclick="update('reset')"/>
																		</c:if>
																	</td>
																	<td>
																		<c:set var="translation">
																			<digi:trn key="btn:createWorkspaceCancel">
																				Cancel
																			</digi:trn>
																		</c:set>
																		<input name="" value="${translation}" onclick="cancel()" class="buttonx" type="button">
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</c:if>
												</table>
											</td>
										</tr>

									</table>
								</td>
							</tr>
							<tr><td bgColor=#FFFFFF>&nbsp;
								
							</td></tr>
						</table>
					</td>
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
							<tr>
								<td>
									<table cellpadding="0" cellspacing="0" width="100" class="inside">
										<tr>
											<td bgColor=#c9c9c7>
												<digi:trn key="aim:otherLinks">
												<b style="padding-left:5px;">Other links</b>
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%" class="inside">
										<tr>
											<td class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/workspaceManager.do">
												<digi:trn key="aim:teams">
												Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										
										
										<tr>
											<td class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/admin.do">
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
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
</td></tr>
</table>
</digi:form>
<script language="JavaScript">
	workspaceChangeType();
	computationChange();
</script>
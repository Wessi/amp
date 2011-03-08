<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">

function defaultPermsSelected() {
	document.aimTeamMemberForm.readPerms.disabled=true;
	document.aimTeamMemberForm.writePerms.disabled=true;
	document.aimTeamMemberForm.deletePerms.disabled=true;
}

function userSpecificPermsSelected() {
	document.aimTeamMemberForm.readPerms.disabled=false;
	document.aimTeamMemberForm.writePerms.disabled=false;
	document.aimTeamMemberForm.deletePerms.disabled=false;
}

function validate() {
	if (isEmpty(document.aimTeamMemberForm.email.value) == true) {
		alert("User id not entered");
		document.aimTeamMemberForm.email.focus();
		return false;
	}
	if (isEmpty(document.aimTeamMemberForm.role.value) == true) {
		alert("Role not entered");
		document.aimTeamMemberForm.role.focus();
		return false;
	}		  
	return true;
}

function cancel()
{
	// alert("aaa");
	//alert(document.aimTeamMemberForm.email.value);
//	document.aimTeamMemberForm.email.value="";
	//alert(document.aimTeamMemberForm.email.value)
	document.aimTeamMemberForm.action = "/aim/teamMembers.do";
	document.aimTeamMemberForm.target = "_self";
	document.aimTeamMemberForm.submit();
	return true;
}

function load()
{
	//aimTeamMemberForm.email.value="";
	alert("aa");
}

function clearForms()
{
  var i;
//	alert(aimTeamMemberForm.email.value);
 // for (i = 0; (i < document.forms.length); i++) {
 //   document.forms[i].reset();
 // }
 	aimTeamMemberForm.email.value="";
 	aimTeamMemberForm.role.value="";
 	//onLoad="clearForms()"
 //  alert(aimTeamMemberForm.email.value);
}	
</script>
<body  onLoad="clearForms()">
<digi:instance property="aimTeamMemberForm" />
<digi:form action="/addTeamMember.do" method="post">

<html:hidden property="teamId" />
<html:hidden property="fromPage" />

<c:if test="${aimTeamMemberForm.fromPage == 1}">
<table width="98%" cellSpacing=2 cellPadding=2 align="center" bgcolor="#ffffff">
<tr><td>
	<digi:errors />
</td></tr>
<tr><td>
</c:if>
<c:if test="${aimTeamMemberForm.fromPage != 1}">
<table width="98%" cellSpacing=2 cellPadding=2 align="center" bgcolor="#f4f4f2">
<tr><td>
	<digi:errors />
</td></tr>
<tr><td>
</c:if>
<table width="100%" cellspacing="1" cellPadding=4 align="center" bgcolor="#aaaaaa" onload="load()">
	<tr>
		<td align="center" colspan="2" bgcolor="#eeeeee"><b>
			<digi:trn key="aim:addTeamMembersFor">Add Members for</digi:trn>
			<bean:write name="aimTeamMemberForm" property="teamName" /></b>
		</td>
	</tr>
	<tr bgcolor="#ffffff">
		<td align="right" valign="top" width="50%">
			<FONT color="red">*</FONT>
			<digi:trn key="aim:userId">User Id</digi:trn>&nbsp;
		</td>
		<td align="left">
			<html:select property="email" >
			<c:forEach items="${aimTeamMemberForm.allUser}" var="members">
				<html:option value="${members.email}">
				<c:out value="${members.email}"/>
				</html:option>
			</c:forEach>
			</html:select>
		</td>
	</tr>
	<tr bgcolor="#ffffff">
		<td align="right" width="50%">
			<FONT color="red">*</FONT>		
			<digi:trn key="aim:memberRole">Role</digi:trn>&nbsp;
		</td>
		<td align="left" width="50%">
			<html:select property="role" styleClass="inp-text">
				<%@include file="teamMemberRolesDropDown.jsp" %>
				<%--<html:optionsCollection name="aimTeamMemberForm" property="ampRoles"
				value="ampTeamMemRoleId" label="role" /> --%>
			</html:select>
		</td>
	</tr>
	<tr bgcolor="#ffffff" align="center">
		<td align="center" colspan="2">
			<table cellspacing="5">
				<tr>
					<td>
						<html:radio property="permissions" value="default"
						onclick="defaultPermsSelected()">
						<digi:trn key="aim:defaultPermission">
							Set default permissions based on the roles</digi:trn>
						</html:radio>
					</td>
				</tr>
				<tr>
					<td>
						<html:radio property="permissions" value="userSpecific"
						onclick="userSpecificPermsSelected()">
							<digi:trn key="aim:userSpecificPermission">
								Manually set permissions for this user</digi:trn>
						</html:radio>
					</td>
				</tr>
				<tr>	
					<td>
						<html:checkbox property="readPerms" disabled="true">
							<digi:trn key="aim:readPerms">Read</digi:trn>
						</html:checkbox>
					</td>
				</tr>
				<tr>
					<td>
						<html:checkbox property="writePerms" disabled="true">
							<digi:trn key="aim:writePerms">Add / Update</digi:trn>
						</html:checkbox>
					</td>
				</tr>
				<tr>
					<td>
						<html:checkbox property="deletePerms" disabled="true">
							<digi:trn key="aim:deletePerms">Delete</digi:trn>	
						</html:checkbox>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr bgcolor="#ffffff">
		<td colspan="2" width="60%">
			<table width="100%" cellspacing="5">
				<tr>
					<td width="50%" align="right">
						<html:submit  styleClass="dr-menu" property="submitButton" onclick="return validate()">
							<digi:trn key="btn:save">Save</digi:trn> 
						</html:submit>
					</td>
					<td width="50%" align="left">
						<html:button  styleClass="dr-menu" property="submitButton" onclick="cancel()">
							<digi:trn key="btn:cancel">Cancel</digi:trn> 
						</html:button>

					</td>
				</tr>
			</table>
		</td>
	</tr>	
	<tr bgcolor="#ffffff">
		<td colspan="2" align="left">
			<digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT color=red><B><BIG>*</BIG>
			</B></FONT> are required.</digi:trn>			
		</td>
	</tr>		
</table>

</td></tr>
</table>


</digi:form>
</body>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/jquery-latest.pack.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/jquery.disable.text.select.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>


<script language="JavaScript">
<!--

function checkSelOrgs() {
	<c:set var="translation">
		<digi:trn key="aim:chooseOrganizationToRemove">Please choose an organization to remove</digi:trn>
	</c:set>
	if (document.aimEditActivityForm.selOrgs.checked != null) { // only one org. added
		if (document.aimEditActivityForm.selOrgs.checked == false) {
			alert("${translation}");
			return false;
		}
	} else { // many org. present
		var length = document.aimEditActivityForm.selOrgs.length;
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimEditActivityForm.selOrgs[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			alert("${translation}");
			return false;
		}
	}
	return true;
}

function selectOrganisation() {
	openNewWindow(600, 400);
	<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=1" />
	document.aimEditActivityForm.action = "<%=selectOrganization%>";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function edit(key) {
    <digi:context name="nextSetp" property="context/module/moduleinstance/addActivity.do" />
    document.aimEditActivityForm.action = "<%= nextSetp %>";
    document.aimEditActivityForm.target = "_self"

  document.aimEditActivityForm.editKey.value = key;
  document.aimEditActivityForm.step.value = "1.1";
  document.aimEditActivityForm.submit();

}

function commentWin(commentId) {


		openNewWindowWithName(600,400,commentId);

		<digi:context name="commentUrl" property="context/module/moduleinstance/viewComment.do" />
		url = "<%=commentUrl %>?comment=" + commentId + "&edit=" + "true";
		document.aimEditActivityForm.action = url;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();

}

function removeSelOrganisations() {
	var flag = checkSelOrgs();
	if (flag == false) return false;
	<digi:context name="remOrgs" property="context/module/moduleinstance/removeSelOrganisations.do?edit=true" />
	document.aimEditActivityForm.action = "<%= remOrgs %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}

function goNext() {
    if(!validateForm()){
      return false;
    }
    <digi:context name="nextSetp" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= nextSetp %>";
    document.aimEditActivityForm.target = "_self"
    document.aimEditActivityForm.submit();
    return true;
}

function resetAll(){
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}

function validateForm() {
  if (trim(document.aimEditActivityForm.title.value) == "") {
    <c:set var="translation">
    <digi:trn key="aim:pleaseEnterTitle">Please enter title</digi:trn>
    </c:set>
    alert("${translation}");
    document.aimEditActivityForm.title.focus();
    return false;
  }
  var stId=document.getElementsByName("statusId");

  var draftStatus=document.getElementById("draftFlag");
  if(draftStatus!=null && draftStatus.value!="true"){
    if(stId==null || stId[0]==null || stId[0].value==0){
      <c:set var="message">
      <digi:trn key="aim:pleaseSelectStatus">Please select status!</digi:trn>
      </c:set>
      alert("${message}");
      return false;
    }
  }
  /*	if (document.aimEditActivityForm.status.value == "-1") {
    alert("Please select status");
    document.aimEditActivityForm.status.focus();
    return false;
  }*/
  document.aimEditActivityForm.step.value="2";
  <digi:context name="commentUrl" property="context/module/moduleinstance/addActivity.do?edit=true" />
  url = "<%=commentUrl %>?comment=" + "ccd" + "&edit=" + "true";
  document.aimEditActivityForm.action = url;
  return true;
}

function goNextStep(){
  if(validateForm()){
    <digi:context name="nextStepUrl" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= nextStepUrl %>";
    document.aimEditActivityForm.target = "_self";
    document.aimEditActivityForm.submit();
  }
}

function reviseCloseDate() {
	openNewWindow(600, 150);
	<digi:context name="rev" property="context/module/moduleinstance/reviseCompDate.do?edit=true" />
	document.aimEditActivityForm.action = "<%= rev %>";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function popupwin()
{
	var wndWidth = window.screen.availWidth/3.5;
	var wndHeight = window.screen.availHeight/3.5;
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;
	winpopup=window.open('',"popup","height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes,status=no,toolbar=no");
	winpopup.document.write('<html>\n<head>\n');
	winpopup.document.write("<title><digi:trn key='aboutStatus'>About : Status</digi:trn></title>\n");
	winpopup.document.write('</head>\n');
	winpopup.document.write('<body bgcolor="#f4f4f2">\n');
	winpopup.document.write('<font face="verdana" size=1>\n');
	winpopup.document.write("<ul><li><b><digi:trn key='aim:planned'>Planned</digi:trn>:</b><digi:trn key='aim:plannedDescription'> from the conceptual stage to just prior to official commitment</digi:trn>.</li><li><b><digi:trn key='aim:onGoing'>On-going</digi:trn>:</b><digi:trn key='aim:onGoingDescription'> the project is committed, is active but not yet completed</digi:trn>.</li><li><b><digi:trn key='aim:completed'>Completed</digi:trn>:</b> <digi:trn key='aim:completedDescription'>the project is finished, with all approved assistance provided</digi:trn>.</li><li><b><digi:trn key='aim:cancelled'>Cancelled</digi:trn>:</b> <digi:trn key='aim:cancelledDescrption'>the project was committed but was terminated prior to planned completion</digi:trn> .</li><li><b> <digi:trn key='aim:suspended'>Suspended: </digi:trn></b><digi:trn key='aim:suspendedDescrption'> the project has been suspended</digi:trn>.</li></ul>\n");
	winpopup.document.write('</font>\n');
	winpopup.document.write('</font>\n</body>\n</html>\n');
	winpopup.document.close();
}



-->
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">

<html:hidden property="step"/>
<html:hidden property="editKey"/>
<html:hidden property="editAct"/>

<input type="hidden" name="edit" value="true">

<input type="hidden" name="selectedDate" value="">
<html:hidden property="reset" />
<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border=0>
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
							<td><jsp:include page="t.jsp"/>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<c:set var="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
									</c:set>
									<digi:link href="/admin.do" styleClass="comment" title="${translation}">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">
									<c:set var="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>
									</c:set>





<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

									<digi:link href="/viewMyDesktop.do" styleClass="comment"  onclick="return quitRnot1('${msg}')" title="${translation}">

										<digi:trn key="aim:portfolio">Portfolio</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivityStep1">Edit Activity - Step 1</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addActivityStep1">Add Activity - Step 1</digi:trn>
								</c:if>
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>
:
										<bean:write name="aimEditActivityForm" property="title"/>
								</c:if>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr> <td>
					<digi:errors/>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%" border=0>
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:step1of10_IdentificationAndPlanning">
													Step 1 of 10: Identification | Planning</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2" border=0>
									<feature:display name="Identification" module="Project ID and Planning">
									&nbsp;
									</feature:display>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:identification">Identification</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td><jsp:include page="addActivityStep1Identification.jsp"/>
									</td></tr>
									

									<tr><td>
										&nbsp;
									</td></tr>
									<feature:display name="Identification" module="Project ID and Planning">
										<field:display name="Organizations and Project ID" feature="Identification">
											<tr><td>
												<jsp:include page="addActivityStep1OrgAndProjects.jsp"/>
											</td></tr>
										</field:display>
									</feature:display>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>
									<feature:display name="Planning" module="Project ID and Planning">
									&nbsp;
									</feature:display>
											<jsp:include page="addActivityStep1Planning.jsp"/>

									<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>


<!--
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<%--<html:submit value="Next >>" styleClass="dr-menu" onclick="return validateForm()"/>--%>
													<html:submit value="Next >>" styleClass="dr-menu" onclick="goNextStep()"/>
												</td>
												<td>
													<html:reset value="Reset" styleClass="dr-menu" onclick="return resetAll()"/>
												</td>
											</tr>
										</table>
									</td></tr>
 -->


								</table>

								<!-- end contents -->
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
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>




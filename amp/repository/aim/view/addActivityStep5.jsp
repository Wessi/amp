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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:instance property="aimEditActivityForm" />




<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>



<script language="JavaScript">

<!--



	function validateForm() {

		return true;

	}



function checkallIssues() {

	var selectbox = document.aimEditActivityForm.checkAllIssues;

	var items = document.aimEditActivityForm.selIssues;

	if (document.aimEditActivityForm.selIssues.checked == true ||

						 document.aimEditActivityForm.selIssues.checked == false) {

			  document.aimEditActivityForm.selIssues.checked = selectbox.checked;

	} else {

		for(i=0; i<items.length; i++){

			document.aimEditActivityForm.selIssues[i].checked = selectbox.checked;

		}

	}

}



function addIssues() {

	openNewWindow(610, 160);

	<digi:context name="addIssue" property="context/module/moduleinstance/showUpdateIssue.do?edit=true" />

	document.aimEditActivityForm.action = "<%=addIssue%>&issueId=-1";

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



function updateIssues(id) {

	openNewWindow(610, 160);

	<digi:context name="addIssue" property="context/module/moduleinstance/showUpdateIssue.do?edit=true" />

	document.aimEditActivityForm.action = "<%=addIssue%>&issueId="+id;

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



function removeIssues() {

	<digi:context name="addIssue" property="context/module/moduleinstance/removeIssue.do?edit=true" />

	document.aimEditActivityForm.action = "<%=addIssue%>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}



function addMeasures(issueId) {

	openNewWindow(610, 160);

	<digi:context name="addMeasure" property="context/module/moduleinstance/showUpdateMeasure.do?edit=true" />

	document.aimEditActivityForm.action = "<%=addMeasure%>&issueId="+issueId+"&measureId=-1";

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



function updateMeasures(issueId,measureId) {

	openNewWindow(610, 160);

	<digi:context name="addMeasure" property="context/module/moduleinstance/showUpdateMeasure.do?edit=true" />

	document.aimEditActivityForm.action = "<%=addMeasure%>&issueId="+issueId+"&measureId="+measureId;

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



function removeMeasure(issueId) {

	<digi:context name="removeMeasure" property="context/module/moduleinstance/removeMeasure.do?edit=true" />

	document.aimEditActivityForm.action = "<%=removeMeasure%>&issueId="+issueId;

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}



function addActors(issueId,measureId) {

	openNewWindow(610, 160);

	<digi:context name="addActors" property="context/module/moduleinstance/showUpdateActors.do?edit=true" />

	document.aimEditActivityForm.action = "<%=addActors%>&issueId="+issueId+"&measureId="+measureId+"&actorId=-1";

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



function updateActor(issueId,measureId,actorId) {

	openNewWindow(610, 160);

	<digi:context name="addActors" property="context/module/moduleinstance/showUpdateActors.do?edit=true" />

	document.aimEditActivityForm.action = "<%=addActors%>&issueId="+issueId+"&measureId="+measureId+"&actorId="+actorId;

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



function removeActors(issueId,measureId) {

	<digi:context name="removeActors" property="context/module/moduleinstance/removeActors.do?edit=true" />

	document.aimEditActivityForm.action = "<%=removeActors%>&issueId="+issueId+"&measureId="+measureId;

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}



function validatePhyProg() {

	if (document.aimEditActivityForm.selPhyProg.checked != null) {

		if (document.aimEditActivityForm.selPhyProg.checked == false) {

			alert("Please choose a physical progress to remove");

			return false;

		}

	} else {

		var length = document.aimEditActivityForm.selPhyProg.length;

		var flag = 0;

		for (i = 0;i < length;i ++) {

			if (document.aimEditActivityForm.selPhyProg[i].checked == true) {

				flag = 1;

				break;

			}

		}



		if (flag == 0) {

			alert("Please choose a physical progress to remove");

			return false;

		}

	}

	return true;

}



function validateComponents() {

	if (document.aimEditActivityForm.selComp.checked != null) {

		if (document.aimEditActivityForm.selComp.checked == false) {

			alert("Please choose a component to remove");

			return false;

		}

	} else {

		var length = document.aimEditActivityForm.selComp.length;

		var flag = 0;

		for (i = 0;i < length;i ++) {

			if (document.aimEditActivityForm.selComp[i].checked == true) {

				flag = 1;

				break;

			}

		}



		if (flag == 0) {

			alert("Please choose a component to remove");

			return false;

		}

	}

	return true;

}



function addPhyProgess(id,comp) {

		openNewWindow(610, 255);

		<digi:context name="addPhyProg" property="context/module/moduleinstance/showAddPhyProg.do~edit=true" />

		if (id == -1) {

			document.aimEditActivityForm.action = "<%= addPhyProg %>~comp=" + comp;

		} else {

			document.aimEditActivityForm.action = "<%= addPhyProg %>~comp=" + comp + "~id=" + id;

		}

		document.aimEditActivityForm.target = popupPointer.name;

		document.aimEditActivityForm.prevId.value = id;

		document.aimEditActivityForm.submit();

}



function removeSelPhyProgress() {

	var flag = validatePhyProg();

	if (flag == false) return false;

	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeSelPhyProg.do?edit=true" />

	document.aimEditActivityForm.action = "<%= remPhyProg %>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

	return true;

}





/*

function addComponents(id) {



		<digi:context name="addComponents" property="context/module/moduleinstance/showAddComponent.do~edit=true" />

		if (id == -1) {

			document.aimEditActivityForm.action = "<%= addComponents %>";

		} else {

			document.aimEditActivityForm.action = "<%= addComponents %>~id=" + id;

		}

		openNewWindow(610, 280);

		document.aimEditActivityForm.target = popupPointer.name;

		document.aimEditActivityForm.submit();

}

*/



function addComponents()

{

	openNewWindow(650,500 );

	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=show" />

	document.aimEditActivityForm.action = "<%= addComp %>";

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



function editFunding(id)

{

	openNewWindow(650,500 );

	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=showEdit" />

	document.aimEditActivityForm.action = "<%= addComp %>&fundId="+id;

	document.aimEditActivityForm.target = popupPointer.name;

	document.aimEditActivityForm.submit();

}



/*

function removeSelComponents()

{

	<digi:context name="rem" property="context/module/moduleinstance/removeSelPhyProg.do?edit=true" />

	document.aimEditActivityForm.action = "<%= rem %>";

	document.aimEditActivityForm.target = "_self";

	document.aimEditActivityForm.submit();

}

*/



function resetAll()

{

	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />

	document.aimEditActivityForm.action = "<%= resetAll %>";

	document.aimEditActivityForm.target = "_self";

	document.aimEditActivityForm.submit();

	return true;

}





function removeSelComponents() {

	var flag = validateComponents();

	if (flag == false) return false;

	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeComponent.do?edit=true" />

	document.aimEditActivityForm.action = "<%= remPhyProg %>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

	return true;

}



-->

</script>

<digi:form action="/addActivity.do" method="post">

<html:hidden property="step" />

<html:hidden property="componentId" />

<input type="hidden" name="prevId">

<html:hidden property="editAct" />

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

									<c:set var="translation">

										<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>

									</c:set>

									<digi:link href="/admin.do" styleClass="comment" title="${translation}">

										<digi:trn key="aim:AmpAdminHome">

											Admin Home

										</digi:trn>

									</digi:link>&nbsp;&gt;&nbsp;

								</c:if>

								<c:if test="${aimEditActivityForm.pageId == 1}">

									<c:set var="translation">

										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>

									</c:set>

									<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')" title="${translation}">

										<digi:trn key="aim:portfolio">

											Portfolio

										</digi:trn>

									</digi:link>&nbsp;&gt;&nbsp;

								</c:if>

								<c:set var="translation">

									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>

								</c:set>

								<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" title="${translation}">



								<c:if test="${aimEditActivityForm.editAct == true}">

									<digi:trn key="aim:editActivityStep1">

										Edit Activity - Step 1

									</digi:trn>

								</c:if>

								<c:if test="${aimEditActivityForm.editAct == false}">

									<digi:trn key="aim:addActivityStep1">

										Add Activity - Step 1

									</digi:trn>

								</c:if>

								</digi:link>&nbsp;&gt;&nbsp;

									<c:set var="translation">

										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>

									</c:set>

									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" title="${translation}">

										<digi:trn key="aim:addActivityStep2">

											Step 2

										</digi:trn>

									</digi:link>&nbsp;&gt;&nbsp;



									<c:set var="translation">

										<digi:trn key="aim:clickToViewAddActivityStep3">Click here to goto Add Activity Step 3</digi:trn>

									</c:set>

									<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" title="${translation}">

										<digi:trn key="aim:addActivityStep3">

											Step 3

										</digi:trn>

									</digi:link>&nbsp;&gt;&nbsp;



									<c:set var="translation">

										<digi:trn key="aim:clickToViewAddActivityStep4">Click here to goto Add Activity Step 4</digi:trn>

									</c:set>

									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment" title="${translation}">

										<digi:trn key="aim:addActivityStep4">

											Step 4

										</digi:trn>

									</digi:link>&nbsp;&gt;&nbsp;



									<digi:trn key="aim:addActivityStep5">

										Step 5

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

								<c:if test="${aimEditActivityForm.editAct == false}">

									<digi:trn key="aim:addNewActivity">

										Add New Activity

									</digi:trn>

								</c:if>

								<c:if test="${aimEditActivityForm.editAct == true}">

									<digi:trn key="aim:editActivity">

										Edit Activity

									</digi:trn>:

										<bean:write name="aimEditActivityForm" property="title"/>

								</c:if>

							</td>

						</tr>

					</table>

				</td></tr>

				<tr><td>

					<digi:errors/>

				</td></tr>

				<tr><td>

					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">

						<tr><td width="75%" vAlign="top">

						<table cellPadding=0 cellSpacing=0 width="100%">

							<tr>

								<td width="100%">

									<table cellPadding=0 cellSpacing=0 width="100%" border=0>

										<tr>

											<td width="13" height="20" background="module/aim/images/left-side.gif">

											</td>

											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">

												<digi:trn key="aim:step5of9Components">Step 5 of 9: Components</digi:trn>

											</td>

											<td width="13" height="20" background="module/aim/images/right-side.gif">

											</td>

										</tr>

									</table>

								</td>

							</tr>

							<tr><td width="100%" bgcolor="#f4f4f2">

							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<feature:display name="Components" module="Components">

							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
									<jsp:include page="addActivityStep5Components.jsp"/>

							</td></tr>

							</feature:display>



								<!-- Issues , Measures and Actions -->

									<tr><td>

										&nbsp;

									</td></tr>
									<feature:display name="Issues" module="Issues">
										<jsp:include page="addActivityStep5Issues.jsp"/>
									</feature:display>
									<tr><td>

										&nbsp;

									</td></tr>
<!--

									<tr><td bgColor=#f4f4f2 align="center">

										<table cellPadding=3>

											<tr>

												<td>

													<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(4)">

														<< <digi:trn key="btn:back">Back</digi:trn>

													</html:submit>

												</td>

												<td>

													<html:submit  styleClass="dr-menu" property="submitButton"  onclick="gotoStep(6)">

															<digi:trn key="btn:next">Next</digi:trn> >>

													</html:submit>

												</td>

												<td>

													<html:reset  styleClass="dr-menu" property="submitButton" onclick="return resetAll()">

														<digi:trn key="btn:reset">Reset</digi:trn>

													</html:reset>

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


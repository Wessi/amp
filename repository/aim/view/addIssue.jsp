<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>

<digi:instance property="aimEditActivityForm" />
<digi:form  name="addIssueForm" type="aimEditActivityForm"  styleId="addIssueForm"  action="/addIssue.do" method="post"  onsubmit="return validate()">
<script language="JavaScript">

	function validate() {
		if(isEmpty(addIssueForm.issue.value) == true) {	
			var issueError = "<digi:trn key="aim:enterIssue">Please enter issue</digi:trn>"; 	
			alert(issueError);			
			addIssueForm.issue.focus();
			return false;
		}
		return true;
	}
</script>

<html:hidden property="issueId"/>
<html:hidden property="event"/>
<html:hidden property="edit" value="true"/>
<html:hidden property="editAct"/>
	<tr>
		<td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
							  <tr>
										<td align="right" valign="top">
									  <digi:trn key="aim:issue">Issue</digi:trn>
									  &nbsp; </td>
							     <td valign="top">
											 <a title="<digi:trn key="aim:issuesForTheActivity">The issues for the activity</digi:trn>">
												<html:textarea property="issue" styleClass="inp-text" rows="3" cols="60"/>
											 </a>										</td>
									</tr>								
									<tr>
									  <td align="right"><digi:trn key="aim:dateOfissue">Date of Issue</digi:trn>
								      &nbsp;</td>
								      <td><html:text property="issueDate" size="10"
																styleId="issueDate" styleClass="inp-text" readonly="true"/> 
																
																<a id="clear1" href="javascript:clearDate(issueDate, 'clear1')">
	 	    														<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0"/>
																</a>
																<a id="date1" href='javascript:pickDateWithClear("date1",issueDate,"clear1")'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a></td>
								  </tr>
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
													
														<c:if test="${aimEditActivityForm.issueId == -1}">
															<input type="submit" value="<digi:trn key='btn:add'>Add</digi:trn>" class="dr-menu">													</td>
														</c:if>
														
														<c:if test="${aimEditActivityForm.issueId != -1}">
															<input type="submit" value="<digi:trn key='btn:update'>Update</digi:trn>" class="dr-menu">													</td>
														</c:if>
														
														
													<td>
														<input type="reset" value="<digi:trn key='btn:clear'>Clear</digi:trn>" class="dr-menu">													</td>
													<td>
													</td>
												</tr>
											</table>										</td>
									</tr>
								</table>
						  </td>
						</tr>					
					</table>
				</td>
			</tr>
		</table>
	</td></tr>
</digi:form>
</table>

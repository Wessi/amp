<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.module.aim.form.FinancingBreakdownForm"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>


<script type="text/javascript">
	
function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=11&surveyFlag=true&activityId=" + id;
}

function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.location.href = "<%=addUrl%>~pageId=2~activityId=" + id;
}

	
function previewLogframe(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	var url ="<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~logframepr=true~activityId=" + id + "~actId=" + id;
	openURLinWindow(url,650,500);
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}

</script>


<html:errors />


<digi:context name="digiContext" property="context" />


<TABLE cellSpacing="0" cellPadding="0" align="center" vAlign="top"
	border="0" width="100%">
	<TR>
		<TD vAlign="top" align="center"><!-- contents -->
		<TABLE width="99%" cellSpacing="0" cellPadding="0" vAlign="top"
			align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR>
				<TD bgcolor="#f4f4f4">
				<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top"
					align="center" bgcolor="#f4f4f4">
					<TR bgColor="#f4f4f2">
						<TD align="left">
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left"
							vAlign="top">
							<TR>
								<TD align="left"><SPAN class="crumb"> <jsp:useBean
									id="urlCostsBreakdown" type="java.util.Map"
									class="java.util.HashMap" /> <c:set
									target="${urlCostsBreakdown}" property="ampActivityId">
									<bean:write name="ampActivityId" />
								</c:set> <c:set target="${urlCostsBreakdown}"
									property="tabIndex" value="7" /> <c:set var="translation">
									<digi:trn key="aim:clickToViewCosts">Click here to view Costing</digi:trn>
								</c:set> <digi:link href="/viewProjectCostsBreakdown.do"
									name="urlCostsBreakdown" styleClass="comment"
									title="${translation}">
									<digi:trn key="aim:projectCosting">Project Costing</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp; <digi:trn key="aim:actOverview">Overview</digi:trn>&nbsp;&gt;&nbsp;
								<digi:trn key="aim:viewCostsBreakdown">Costing</digi:trn> </SPAN>
								</TD>
								<TD align="right"><input type="button" value="Preview"
									class="dr-menu"
									onclick='preview(<c:out value="${ampActivityId}"/>)'>
								<input type="button" value="Edit" class="dr-menu"
									onclick='fnEditProject(<c:out value="${ampActivityId}"/>)'>
														&nbsp;
								<logic:empty name="SA" scope="application">
								<input type="button" value="Preview Logframe" class="dr-menu"
									onclick="previewLogframe(<c:out value="${ampActivityId}"/>)">
								</logic:empty>
								<logic:empty name="SA" scope="application">
								<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu'
												onclick='projectFiche(<c:out value="${ampActivityId}"/>)'>
								</logic:empty>
								</TD>


							</TR>
						</TABLE>
						</TD>
					</TR>





					<TR>
						<TD width="750" bgcolor="#F4F4F2" height="17">
						<TABLE border="0" cellpadding="0" cellspacing="0"
							bgcolor="#F4F4F2" height="17">
							<TR bgcolor="#F4F4F2" height="17">
								<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp; <digi:trn
									key="aim:projectCosting">Project Costing</digi:trn>
								</TD>
								<TD background="module/aim/images/corner-r.gif" height="17"
									width="17"></TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top"
							align="center" bgcolor="#f4f4f4">
							<TR bgColor="#f4f4f2">
								<td><bean:define id="mode" value="view" type="java.lang.String"
									toScope="request" /> <jsp:include page="viewCostsSummary.jsp"
									flush="" /></td>
							</TR>
						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
</TABLE>




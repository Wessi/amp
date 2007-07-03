<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
 
<script language="JavaScript">

	function toggleFeature(id) {
		<digi:context name="urlVal" property="context/module/moduleinstance/featureManager.do" />			  
		document.aimFeatureManagerForm.action = "<%= urlVal %>?toggle=true&fId="+id;
		document.aimFeatureManagerForm.submit();		
	}

</script>

<digi:instance property="aimVisibilityManagerForm" />

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
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:featureManager">
							Feature Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:featureManager">
							Feature Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
					</td>
				</tr>
				<logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
					<tr>
						<td noWrap width=100% vAlign="top">
							<jsp:include page="newTemplateVisibility.jsp" />
						</td>
					</tr>			
				</logic:equal>
				<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplate">
					<tr>
						<td noWrap width=100% vAlign="top">
							<jsp:include page="editTemplateVisibility.jsp" />
						</td>
					</tr>			
				</logic:equal>
				
				<tr>
					<td noWrap width=100% vAlign="top">
						<jsp:include page="manageTemplatesVisibility.jsp" />
					</td>
				</tr>

				<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">			
				<tr>
					<td noWrap width=100% vAlign="top">
						<jsp:include page="manageTreeVisibility.jsp" />
					</td>
				</tr>
				</logic:equal>	
</table>

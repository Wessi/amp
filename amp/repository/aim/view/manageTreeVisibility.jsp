<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript">

	function changeLevel(id) {
	// alert("aaaaaaaaaaa "+id);
		<digi:context name="urlVal" property="context/module/moduleinstance/visibilityManager.do" />			  
		document.aimVisibilityManagerForm.action = "<%= urlVal %>?changeLevel=true&action=edit&templateId="+id;
		document.aimVisibilityManagerForm.submit();		
	}

</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-ajax.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-folder-tree-static.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-context-menu.js"/>"></script>

	<script type="text/javascript">
		var idOfFolderTrees = ['dhtmlgoodies_tree'];
	</script>
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />



<digi:instance property="aimVisibilityManagerForm" />
<digi:form action="/visibilityManager.do" method="post" >

<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampFeatureManager">
		Feature Manager 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	
	<tr>
		<td><br/>
		</td>
	</tr>
	<tr>
		<td>
	<bean:define name="aimVisibilityManagerForm" property="ampTreeVisibility" id="template" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
	<bean:define name="template" property="items" id="modules" type="java.util.Map"  toScope="page"/>
	<bean:define name="template" property="root" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="page" toScope="request"/>
		
	 <c:set var="translation">
         <digi:trn key="aim:addNoLevel">No Level</digi:trn>
        </c:set>
	<p><digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn> <input type="text" name="templateName" size="30" value="<%=session.getAttribute("templateName")%>"/>
	<c:set var="templateId">
		<bean:write name="template" property="root.id"/>
	</c:set>
	 <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.ACTIVITY_LEVEL %>" compareWith="true" onTrueEvalBody="true">
		<category:showoptions name="aimVisibilityManagerForm" firstLine="${translation}" outeronchange="javascript:changeLevel('${templateId}')" property="levelCategory"  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_LEVEL_KEY %>" styleClass="inp-text" />
	</gs:test>
	</p>
	<ul id="dhtmlgoodies_tree" class="dhtmlgoodies_tree">
	<li><a href="#" id="<bean:write name="template" property="root.id"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none" ><bean:write name="template" property="root.name"/></a>
			<ul>
				<logic:iterate name="modules" id="module" type="java.util.Map.Entry" >
					<bean:define id="moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page" toScope="request"/>
					<bean:define id="_moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility"/>
					<bean:define id="_moduleAux2" name="_moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
					<bean:define id="size" name="_moduleAux2" property="submodules"/>
					<logic:equal name="aimVisibilityManagerForm" property="existSubmodules" value="false">
						<jsp:include page="generateTreeXLevelVisibility.jsp" />
					</logic:equal>
					<logic:equal name="aimVisibilityManagerForm" property="existSubmodules" value="true">
							<jsp:include page="generateTreeXLevelVisibility.jsp" />
					</logic:equal>
				</logic:iterate>
				
			</ul>
		</li>
	</ul>
	<c:set var="translation">
		<digi:trn key="aim:treeVisibilitiSaveChanges">Save Changes</digi:trn>
	</c:set>
	<html:submit style="dr-menu" value="${translation}" property="saveTreeVisibility"/>
		</td>
	</tr>
</table>
</digi:form>




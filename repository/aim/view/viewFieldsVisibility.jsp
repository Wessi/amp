<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	function onDeleteField() {
		var flag = confirm("Delete this Field?");
		return flag;
	}

	function onDeleteFeature() {
		var flag = confirm("Delete this Feature?");
		return flag;
	}

	function onDeleteModule() {
		var flag = confirm("Delete this Module?");
		return flag;
	}

</script>


<digi:instance property="aimVisibilityManagerForm" />
<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	

		<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampModules">
		AMP Modules
	</digi:trn>
	<!-- end table title -->										
	</td></tr>

	<tr><td>
	
	<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border="1px" >
		<tr>
			<th>Module</th>
			<th>Action</th>
		</tr>
		<jsp:useBean id="urlParamsDelModule" type="java.util.Map" class="java.util.HashMap"/>
		<logic:iterate name="aimVisibilityManagerForm" property="allModules" id="module"
			type="org.digijava.module.aim.dbentity.AmpModulesVisibility">
			<tr>
				<td align="left"><bean:write name="module" property="name"/></td>
				<c:set target="${urlParamsDelModule}" property="action" value="deleteModule"/>
				<c:set target="${urlParamsDelModule}" property="moduleId" value="<%=module.getId()%>"/>
				<td>
					<bean:define id="translation">
						<digi:trn key="aim:clickToDeleteModule">Click here to Delete Module</digi:trn>
					</bean:define>
					[ <digi:link href="/visibilityManager.do" name="urlParamsDelModule" 
						title="<%=translation%>" onclick="return onDeleteModule()">Delete</digi:link> ]
				</td>
				
			</tr>
		</logic:iterate>
	</table>
	</td></tr>
	
	
	<tr><td>&nbsp;</td></tr>
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampFeatures">
		AMP Features
	</digi:trn>
	<!-- end table title -->										
	</td></tr>

	<tr><td>
	
	<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border="1px" >
		<tr>
			<th>Feature</th>
			<th>Module</th>
			<th>Action</th>
		</tr>
		<jsp:useBean id="urlParamsDelFeature" type="java.util.Map" class="java.util.HashMap"/>
		<logic:iterate name="aimVisibilityManagerForm" property="allFeatures" id="feature"
			type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility">
			<tr>
				<bean:define id="module" name="feature" property="parent" type="org.digijava.module.aim.dbentity.AmpModulesVisibility"/>
				<td align="left"><bean:write name="feature" property="name"/></td>
				<td><i><bean:write name="module" property="name"/></i></td>
				<c:set target="${urlParamsDelFeature}" property="action" value="deleteFFM"/>
				<c:set target="${urlParamsDelFeature}" property="featureId" value="<%=feature.getId()%>"/>
				<td>
					<bean:define id="translation">
						<digi:trn key="aim:clickToDeleteFeature">Click here to Delete Feature</digi:trn>
					</bean:define>
					[ <digi:link href="/visibilityManager.do" name="urlParamsDelFeature" 
						title="<%=translation%>" onclick="return onDeleteFeature()">Delete</digi:link> ]
				</td>
				
			</tr>
		</logic:iterate>
	</table>
	</td></tr>
	<tr><td>&nbsp;</td></tr>
	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampFields">
		AMP Fields 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" >
	<tr><td>
	
	<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border="1px" >
		<tr>
			<th>Field</th>
			<th>Feature</th>
			<th>Module</th>
			<th>Action</th>
		</tr>
		<jsp:useBean id="urlParamsDelField" type="java.util.Map" class="java.util.HashMap"/>
		<logic:iterate name="aimVisibilityManagerForm" property="allFields" id="field"
			type="org.digijava.module.aim.dbentity.AmpFieldsVisibility">
			<tr>
				<bean:define id="feature" name="field" property="parent" type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility"/>
				<bean:define id="module" name="feature" property="parent" type="org.digijava.module.aim.dbentity.AmpModulesVisibility"/>
				<td align="left"><bean:write name="field" property="name"/></td>
				<td><i><bean:write name="feature" property="name"/></i></td>
				<td><i><bean:write name="module" property="name"/></i></td>
				<c:set target="${urlParamsDelField}" property="action" value="deleteFFM"/>
				<c:set target="${urlParamsDelField}" property="fieldId" value="<%=field.getId()%>"/>
				<td>
					<bean:define id="translation">
						<digi:trn key="aim:clickToDeleteField">Click here to Delete Field</digi:trn>
					</bean:define>
					[ <digi:link href="/visibilityManager.do" name="urlParamsDelField" 
						title="<%=translation%>" onclick="return onDeleteField()">Delete</digi:link> ]
				</td>
				
			</tr>
		</logic:iterate>
	</table>
	</td></tr>
	
	</digi:form>
</table>

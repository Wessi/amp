<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>


<digi:instance property="crDocToOrgForm" />
<bean:define id="myForm" name="crDocToOrgForm" toScope="page" type="org.digijava.module.contentrepository.form.DocToOrgForm" />

<logic:notEmpty name="myForm" property="orgs">
	<ul>
		<c:forEach var="org" items="${myForm.orgs}">
			<li>
				${org.acronym} ( ${org.name} )
				<c:if test="${myForm.hasAddParticipatingOrgRights}">
					<img onClick="deleteDocToOrgObj('${myForm.uuidForOrgsShown}', ${org.ampOrgId});"  style="cursor:pointer; text-decoration:underline;"
					title="<digi:trn>Click here to remove this organisation</digi:trn>"
					hspace="2" src= "/repository/contentrepository/view/images/trash_12.gif" border="0" />
				</c:if>
			</li>
		</c:forEach>
	</ul>
</logic:notEmpty>

<logic:empty name="myForm" property="orgs">
	<digi:trn>No organizations have participated in creating this document</digi:trn>
</logic:empty>


<br />


<c:if test="${myForm.hasAddParticipatingOrgRights}">
	<aim:addOrganizationButton callBackFunction="showOrgsPanel();"  refreshParentDocument="false" collection="addedOrgs" 
				form="${crDocToOrgForm}" styleClass="dr-menu">
				<digi:trn key="btn:addOrganizations">Add Organizations</digi:trn>
	</aim:addOrganizationButton>
</c:if>
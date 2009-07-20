<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

	<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
		<tr>
			<td>
				<!-- Other Links -->
				<table cellPadding=0 cellSpacing=0 width=100% height="20">
					<tr>
						<td bgColor=#c9c9c7 class=box-title>
							<digi:trn key="aim:otherLinks">
							Other links
							</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgColor=#ffffff class=box-border>
				<table cellPadding=5 cellSpacing=1 width="100%">
					<tr>
						<td>
							<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
						<td>
								<digi:link href="/editOrganisation.do?actionFlag=create&mode=resetMode" >
									<digi:trn key="aim:addNewOrganization">Add an Organization</digi:trn></digi:link>
						</td>
					</tr>
					<tr>
						<td>
							<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
						<td>
								<digi:link href="/orgTypeManager.do" >
									<digi:trn key="aim:orgTypeManager">Organization Type Manager</digi:trn></digi:link>
						</td>
					</tr>
					<tr>
						<td>
							<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
						<td>
								<digi:link href="/orgGroupManager.do" >
									<digi:trn key="aim:orgGroupManager">Organization Group Manager</digi:trn></digi:link>
						</td>
					</tr>
					<tr>
						<td>
							<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
						<td>
							<digi:link href="/admin.do">
							<digi:trn key="aim:AmpAdminHome">
							Admin Home
							</digi:trn>
							</digi:link>
						</td>
					</tr>
					<!-- end of other links -->
				</table>
			</td>
		</tr>
	</table>

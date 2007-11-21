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
									<c:if test="${!empty aimEditActivityForm.documentSpace}">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:DMDocumentsRelated">DM - Managed Document related to the project</digi:trn>">
										<b><digi:trn key="aim:DMDocumentsRelated">DM - Managed Document related to the project</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="managedDocumentList">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="managedDocumentList"
												id="selManagedDocuments">
												<tr>
													<td>
														<html:multibox property="selManagedDocs">
															<bean:write name="selManagedDocuments" property="id" />
														</html:multibox>
														<field:display name="Managed Documents Name" feature="Managed Documents">
														<bean:write name="selManagedDocuments" property="name" /> - 
														</field:display>
														<c:if test="${!empty selManagedDocuments.fileName}">
							   							<bean:define id="fileName" name="selManagedDocuments" 
															property="fileName" />
														    <%
															int index2;
															String extension = "";
															index2 = ((String)fileName).lastIndexOf(".");	
															if( index2 >= 0 ) {
															   extension = "module/cms/images/extensions/" + 
																((String)fileName).substring(
																index2 + 1,((String)fileName).length()) + ".gif";
															}
														    %>
														    <digi:img skipBody="true" src="<%=extension%>" border="0" 
															 align="absmiddle"/>	
														</c:if>
														<field:display name="Managed Documents File Name" feature="Managed Documents">
															<i>
														<bean:write name="selManagedDocuments" property="fileName" /></i>
															<br>
															</field:display>
													<field:display name="Managed Documents Description" feature="Managed Documents">
														<b>Desc:</b><bean:write name="selManagedDocuments" property="description" />
														</field:display>
													</td>
												</tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=2 cellPadding=2>
														<tr>
															<td>
															<field:display name="Components Add Managed Documents Button" feature="Managed Documents">
																<html:button  styleClass="buton" property="submitButton" onclick="addManagedDocuments()">
																	<digi:trn key="btn:addManagedDocuments">Add Managed Documents</digi:trn>
																</html:button>
															</field:display>
															</td>
															<td>
															<field:display name="Components Remove Managed Documents Button" feature="Managed Documents">
																<html:button  styleClass="buton" property="submitButton" onclick="return removeSelManagedDocuments()">
																	<digi:trn key="btn:removeDocuments">Remove Documents</digi:trn>
																</html:button>
															</field:display>
															</td>
														</tr>
													</table>
												</td></tr>
											</table>											
										</logic:notEmpty>
										
										<logic:empty name="aimEditActivityForm" property="managedDocumentList">
										<field:display name="Components Add Managed Documents Button" feature="Managed Documents">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
													
														<input type="button" value="Add Managed Documents" class="buton" 
														onclick="addManagedDocuments()" class="buton">
													</td>
												</tr>
											</table>
										</field:display>
										</logic:empty>										
									</td></tr>
									</c:if>

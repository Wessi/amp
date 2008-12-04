<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.helper.*" %>
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

<digi:instance property="aimEditActivityForm" />
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:customfields">Custom Fields</digi:trn></b>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>

											<logic:iterate name="aimEditActivityForm" property="customFields" id="customField" indexId="index">
											<logic:equal name="customField" property="step" value="${aimEditActivityForm.step}">
											<field:display name="${customField.FM_field}" feature="Step${aimEditActivityForm.step}">												
												<tr>
													<td width=200 bgcolor="#ffffff">
														<a title="<digi:trn key="aim:customfield:${customField.name}:description">${customField.description}</digi:trn>">&nbsp;
														<digi:trn key="aim:customfield:${customField.name}">${customField.name}</digi:trn>
														</a>
													</td>
													<td bgcolor="#ffffff">
														<a title="<digi:trn key="aim:customfield:${customField.name}:description">${customField.description}</digi:trn>">
														<c:choose>
															<c:when test="<%=customField instanceof ComboBoxCustomField%>">
																<html:select name="aimEditActivityForm" property="customFields[${index}].value">
																	<html:optionsCollection  name="aimEditActivityForm" property="customFields[${index}].options" value="value" label="key"/>
																</html:select>											
															</c:when>
															<c:otherwise>
																<html:text name="aimEditActivityForm" property="customFields[${index}].value" size="40"
																styleId="originalAppDate" styleClass="inp-text" />																														
															</c:otherwise>
														</c:choose>
														</a>
													</td>
												</tr>
											</field:display>
											</logic:equal>
											</logic:iterate>
										</table>
									</td></tr>


									

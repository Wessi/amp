<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimEditActivityForm" />
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:BeneficiaryAgency">The organisation that benefits from the activity</digi:trn>">
										<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="benAgencies">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="benAgencies"
												id="benAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
														<tr>
															<td width="3">
																<html:multibox property="selBenAgencies">
																	<bean:write name="benAgency" property="ampOrgId" />
																</html:multibox>
															</td>
															<td align="left">
																<bean:write name="benAgency" property="name" />
															</td>
														</tr>
													</table>
												</td></tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=1 cellPadding=1>
														<tr>
															<td>
																<html:button  styleClass="buton" property="submitButton" onclick="addOrgs(5)">
																	<digi:trn key="btn:addOrganizations">Add Organizations</digi:trn>
																</html:button>
															</td>
															<td>
																<html:button  styleClass="buton" property="submitButton" onclick="return removeSelOrgs(5)">
																	<digi:trn key="btn:removeSelectedOrganizations">Remove Selected Organizations</digi:trn>
																</html:button>

															</td>
														</tr>
													</table>
												</td></tr>
											</table>
										</logic:notEmpty>

										<logic:empty name="aimEditActivityForm" property="benAgencies">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
																<html:button  styleClass="buton" property="submitButton" onclick="addOrgs(5)">
																	<digi:trn key="btn:addOrganizations">Add Organizations</digi:trn>
																</html:button>
													</td>
												</tr>
											</table>
										</logic:empty>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>

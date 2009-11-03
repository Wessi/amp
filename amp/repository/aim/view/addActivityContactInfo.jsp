<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/aim"prefix="aim"%>

<digi:context name="digiContext" property="context"/>
<digi:instance property="aimEditActivityForm"/>

<digi:form name="contactForm" type="aimEditActivityForm" action="/activityContactInfo.do" method="post">
	<table cellpadding="2" cellspacing="5" width="100%" border="1" height="100%">
		<tr height="5px"><td colspan="2"/></tr>
		<!-- page logic row start -->
                <html:hidden property="contactInformation.temporaryId" styleId="temporaryId"/>
                <tr>
			<!-- create new contact td start -->
			<td width="50%" height="100%">
				<table cellpadding="2" cellspacing="5" width="100%" height="100%" class="box-border-nopadding" >
					<tr height="5px"><td colspan="2"/></tr>		
					<tr>
						<td align="right"><strong><digi:trn>Title</digi:trn></strong></td>
						<td align="left">
                                                    <c:set var="translation">
                                                        <digi:trn>Please select from below</digi:trn>
                                                    </c:set>
                                        <category:showoptions multiselect="false" firstLine="${translation}" name="aimEditActivityForm" property="contactInformation.title"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_TITLE_KEY%>" styleClass="selectStyle" outerid="contactTitle"/>
                                                </td>
					</tr>	
					<tr>
						<td align="right"><strong><digi:trn>Lastname</digi:trn></strong><font color="red">*</font></td>
						<td align="left"><html:text property="contactInformation.lastname" size="30" styleId="lastname"/></td>
					</tr>
					<tr>						
						<td align="right"><strong><digi:trn>Firstname</digi:trn></strong><font color="red">*</font></td>
						<td align="left"><html:text property="contactInformation.name" size="30" styleId="name"/></td>
					</tr>
					<tr>
						<td align="right"><strong><digi:trn>Email</digi:trn></strong><font color="red">*</font></td>
						<td align="left" nowrap="nowrap">
                                                    <html:text property="contactInformation.email" styleId="email" size="30"/>
						</td>					
					</tr>
					<tr>
						<td align="right"><strong><digi:trn>Function</digi:trn></strong></td>
                                                <td align="left"><html:text property="contactInformation.function" size="30" styleId="function"/></td>
					</tr>	
					<tr>
						<td align="right"><strong><digi:trn>Organization</digi:trn></strong></td>
                                                <td align="left"><html:text property="contactInformation.organisationName" size="30" styleId="organisationName"/></td>
					</tr>
                                        <tr>
                                            <td colspan="2" align="center">
                                                <c:choose>
                                                    <c:when test="${empty aimEditActivityForm.contactInformation.organizations}">
                                                <aim:addOrganizationButton refreshParentDocument="false" callBackFunction="addOrganizations2Contact()" collection="organizations"  form="${aimEditActivityForm.contactInformation}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
                                            </c:when>

                                            <c:otherwise>
                                                <table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
                                                    <c:forEach var="organization" items="${aimEditActivityForm.contactInformation.organizations}">
                                                        <tr>
                                                            <td width="3px">
                                                                <html:multibox property="contactInformation.selContactOrgs">
                                                                    <bean:write name="organization" property="ampOrgId" />
                                                                </html:multibox>
                                                            </td>

                                                            <td align="left">
                                                                <bean:write name="organization" property="name" />
                                                            </td>

                                                        </tr>
                                                    </c:forEach>
                                                    <tr>
                                                        <td colspan="2">
                                                    <aim:addOrganizationButton refreshParentDocument="false" callBackFunction="addOrganizations2Contact()" collection="organizations"  form="${aimEditActivityForm.contactInformation}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
                                                    <input type="button" class="dr-menu" onclick="javascript:removeContactOrgs();" value='<digi:trn>Remove Organization(s)</digi:trn>' />
                                                </td>
                                            </tr>

                                        </table>
                                        </c:otherwise>
                                        </c:choose>
                                        </td>
                        
                                        </tr>
					<tr>
						<td align="right"><strong><digi:trn>Phone Number</digi:trn></strong></td>
						<td align="left"><html:text property="contactInformation.phone" size="30" styleId="phone" onkeyup="checkNumber('phone')"/></td>
					</tr>		
					<tr>
						<td align="right"><strong><digi:trn>Mobile phone</digi:trn></strong></td>
						<td align="left"><html:text property="contactInformation.mobilephone" size="30" styleId="mobilephone" onkeyup="checkNumber('mobilephone')"/></td>
					</tr>	
					<tr>
						<td align="right"><strong><digi:trn>Fax</digi:trn></strong></td>
						<td align="left"><html:text property="contactInformation.fax" size="30" styleId="fax" onkeyup="checkNumber('fax')"/></td>
					</tr>			
					<tr>
						<td align="right" valign="top"><strong><digi:trn>Office Address</digi:trn></strong></td>
                                                <td align="left"><html:textarea property="contactInformation.officeaddress" cols="36" rows="3" styleId="officeaddress"/></td>
					</tr>	
					<tr height="5px"><td colspan="2"/></tr>
					<tr>
						<td colspan="6" align="center"><html:button property="" styleClass="dr-menu" onclick="saveContact()">Save</html:button> </td>			
					</tr>
				</table>
			</td>
			<!-- create new contact td end -->
			<!-- filter start -->
			<td width="50%" height="100%" bordercolor="#f4f4f2">
				<c:if test="${aimEditActivityForm.contactInformation.action=='add' || aimEditActivityForm.contactInformation.action=='search'}">		
					<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0" height="100%" class="box-border-nopadding" >
						<tr><td vAlign="top">
							<table bgcolor="#f4f4f2" cellPadding="5" cellSpacing="5" width="100%" class="box-border-nopadding">
								<tr>
									<td align="left" vAlign="top">
										<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
											<tr bgcolor="#006699">
												<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
													<digi:trn>Search Contacts</digi:trn>
												</td></tr>
											<tr>
												<td align="center" bgcolor="#ECF3FD">
													<table cellSpacing="2" cellPadding="2">
														<tr>
															<td>
																<digi:trn key="aim:enterKeyword">Enter a keyword </digi:trn>
															</td>
															<td>
																<html:text property="contactInformation.keyword"  styleClass="inp-text" styleId="keyword"/>
															</td>
														</tr>
														<tr>
															<td align="center" colspan=2>
																<html:button  styleClass="dr-menu" property="submitButton" onclick="return searchContact()">
																	<digi:trn>Search</digi:trn> 
																</html:button>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>				
								<tr>
								<td align="left" vAlign="top" width="100%">
									<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" height="100%" class="box-border-nopadding" border="0" vAlign="top">
										<tr bgcolor="#006699">
											<td vAlign="center" width="100%" align ="center" class="textalb" height="20" >
												<digi:trn>List of Contacts</digi:trn>
											</td>
				                         </tr>
				                         <tr>
				                            <td>
				                               <digi:errors/>
				                            </td>
				                         </tr>
										<c:if test="${not empty aimEditActivityForm.contactInformation.contacts}">
											<tr height="100px">
												<td rowspan="1" align="left" vAlign="top">
													<div style="overflow: scroll;height: 100px" >
														<table width="100%" cellPadding="3" cellspacing="0" border="0" style="overflow: scroll;height: 120px" >
														<c:forEach var="contact" items="${aimEditActivityForm.contactInformation.contacts}">
															<tr>
																<td bgcolor="#ECF3FD" colspan="5" style="font:11px">
																	&nbsp;&nbsp;
																	<html:multibox property="contactInformation.contactIds">
																		${contact.id}
																	</html:multibox>&nbsp;
																	${contact.name}&nbsp;${contact.lastname}&nbsp;-
																	<a	href="mailto:${contact.email}"><font color="black">${contact.email}</font></a> &nbsp;
																	<c:if test="${not empty contact.organisationName}">:&nbsp;${contact.organisationName}</c:if>													 
																</td>
															</tr>
														</c:forEach>														
													</table>
													</div>													
												</td>
											</tr>
										</c:if>
										<c:if test="${empty aimEditActivityForm.contactInformation.contacts && aimEditActivityForm.contactInformation.action=='search'}">
											<tr>
												<td>
													<br><br>&nbsp;&nbsp;&nbsp;
													<digi:trn key="aim:noRecordsFoundMatching">No records found, matching to your query......</digi:trn>
													<br><br>
												</td>
											</tr>
										</c:if>
									</table>
								</td>
							</tr>
					</table>
				</c:if>
			</td>
			<!-- filter end -->
		</tr>
		<!-- page logic row end -->
		<!-- buttons start -->
		<tr>
			<td align="center" colspan="3">
				<c:if test="${not empty aimEditActivityForm.contactInformation.contacts}">
					<table cellPadding="5">
						<tr>
							<td>
								<html:button styleClass="dr-menu" property="addButton" onclick="addSelectedContacts()">
									<digi:trn>Add</digi:trn>
								</html:button>	
							</td>
							<td>
								<html:reset  styleClass="dr-menu" property="submitButton">
									<digi:trn>Clear</digi:trn>
								</html:reset>
							</td>
							<td>
								<html:button styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
									<digi:trn>Close</digi:trn>
								</html:button>
							</td>
						</tr>
					</table>
				</c:if>				
			</td>
		</tr>
		<!-- buttons end -->
	</table>	
</digi:form>
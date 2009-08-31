<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:context name="digiContext" property="context"/>
<digi:instance property="aimAddContactForm"/>


<digi:form name="contactForm" type="aimAddContactForm" action="/addAmpContactInfo.do" method="post">
    <table cellpadding="2" cellspacing="5" width="100%">
        <tr height="5px"><td colspan="2"/></tr>
        <!-- page logic row start -->
        <tr>
            <!-- create new contact td start -->
            <td width="50%" height="100%">
                <table cellpadding="2" cellspacing="5" width="100%" height="100%" class="box-border-nopadding" >
                    <tr height="5px"><td colspan="2"/></tr>
                    <tr>
                        <td rowspan="10" width="20%"/>
                        <td align="right"><strong><digi:trn>Firstname</digi:trn></strong><font color="red">*</font></td>
                        <td align="left"><html:text property="name" styleId="contactName"/></td>
                    </tr>
                    <tr>
                        <td align="right"><strong><digi:trn>Lastname</digi:trn></strong><font color="red">*</font></td>
                        <td align="left"><html:text property="lastname" styleId="contactLastname"/></td>
                    </tr>
                    <tr>
                        <td align="right"><strong><digi:trn>Email</digi:trn></strong><font color="red">*</font></td>
                        <td align="left" nowrap="nowrap">
                            <html:text property="email" styleId="contactEmail"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><strong><digi:trn>Title</digi:trn></strong> </td>
                        <td align="left"><html:text property="title"  styleId="contactTitle"/></td>
                    </tr>
                    <tr>
						<td align="right"><strong><digi:trn>Function</digi:trn></strong></td>
						<td align="left"><html:text property="function" styleId="contactFunction"/></td>
					</tr>	
                    <tr>
						<td align="right"><strong><digi:trn>Office Address</digi:trn></strong></td>
						<td align="left"><html:text property="officeaddress" styleId="contactOfficeaddress"/></td>
					</tr>	
                    <tr>
                        <td align="right"><strong><digi:trn>Organization</digi:trn></strong></td>
                        <td align="left"><html:text property="organisationName" styleId="contactOrgName"/></td>
                    </tr>
                    <tr>
                        <td align="right"><strong><digi:trn>Phone Number</digi:trn></strong></td>
                        <td align="left"><html:text property="phone" styleId="contactPhone" onkeyup="checkNumber('phone')"/></td>
                    </tr>
                    <tr>
                        <td align="right"><strong><digi:trn>Fax</digi:trn></strong></td>
                        <td align="left"><html:text property="fax" styleId="contactFax" onkeyup="checkNumber('fax')"/></td>
                    </tr>
                    <tr>
						<td align="right"><strong><digi:trn>Mobile phone</digi:trn></strong></td>
						<td align="left"><html:text property="mobilephone" styleId="contactMobilephone" onkeyup="checkNumber('mobilephone')"/></td>
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
                <c:if test="${aimAddContactForm.action=='create' || aimAddContactForm.action=='search'}">
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
                                                                    <html:text property="keyword"  styleClass="inp-text" styleId="keyword"/>
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
                                                <c:if test="${not empty aimAddContactForm.contacts}">
                                                    <tr height="100px">
                                                        <td rowspan="1" align="left" vAlign="top">
                                                            <div style="overflow: scroll;height: 100px" >
                                                                <table width="100%" cellPadding="3" cellspacing="0" border="0" style="overflow: scroll;height: 120px" >
                                                                    <c:forEach var="contact" items="${aimAddContactForm.contacts}">
                                                                        <tr>
                                                                            <td bgcolor="#ECF3FD" colspan="5" style="font:11px">
                                                                                &nbsp;&nbsp;
                                                                                <html:multibox property="selContactIds">
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
                                                <c:if test="${empty aimAddContactForm.contacts && aimAddContactForm.action=='search'}">
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
                            <c:if test="${not empty aimAddContactForm.contacts}">
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

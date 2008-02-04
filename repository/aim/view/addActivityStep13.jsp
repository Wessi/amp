<%@ page pageEncoding="UTF-8" %>
<%@page import="org.digijava.module.aim.util.CurrencyUtil" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronousSendNotNull.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
    <!--
    
    function mapCallBack(status, statusText, responseText, responseXML){
         window.location.reload();
         
     
    }

    function callUrl(indexId){
    var async=new Asynchronous();
    async.complete=mapCallBack;
    async.call("/aim/editIPAContract.do?deleteEU&indexId="+indexId);
    
    
    }
    
    
    
    function addIPAContract() {
        openNewWindow(700, 600);
        <digi:context name="editIPAContract" property="context/module/moduleinstance/editIPAContract.do?new" />
        document.aimEditActivityForm.action = "<%= editIPAContract %>";
        document.aimEditActivityForm.target = popupPointer.name;
        document.aimEditActivityForm.submit();
    }
    
    function editContract(indexId) {
        openNewWindow(700, 600);
        <digi:context name="editIPAContract" property="context/module/moduleinstance/editIPAContract.do?editEU&indexId=" />
        document.aimEditActivityForm.action = "<%=editIPAContract%>"+indexId;
        document.aimEditActivityForm.target = popupPointer.name;
        document.aimEditActivityForm.submit();
    }
    
    function deleteContract(indexId) {
        
        <digi:context name="editIPAContract" property="context/module/moduleinstance/deleteIPAContract.do?indexId=" />
        document.aimEditActivityForm.action = "<%=editIPAContract%>"+indexId;
        document.aimEditActivityForm.target = "_self";
        document.aimEditActivityForm.submit();
    }
    
    
  
    function validateForm() {
        return true;
    }
    -->
    </script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">
    <html:hidden property="step"/>
    
    
    <html:hidden property="editAct" />
    
    <table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
    <tr>
        <td width="100%" vAlign="top" align="left">
            <!--  AMP Admin Logo -->
                 <jsp:include page="teamPagesHeader.jsp" flush="true" />
                 <!-- End of Logo -->
        </td>
    </tr>
    <tr>
    <td width="100%" vAlign="top" align="left">
    <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
        <tr>
            <td class=r-dotted-lg width="10">&nbsp;</td>
            <td align=left vAlign=top class=r-dotted-lg>
                <table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
                    <tr><td>
                            <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                                <tr>
                                    <td>
                                        <span class=crumb>
                                            <c:if test="${aimEditActivityForm.pageId == 0}">
                                                <c:set var="translation">
                                                    <digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
                                                </c:set>
                                                <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                                                    <digi:trn key="aim:AmpAdminHome">
                                                        Admin Home
                                                    </digi:trn>
                                                </digi:link>&nbsp;&gt;&nbsp;
                                            </c:if>
                                            <c:if test="${aimEditActivityForm.pageId == 1}">
                                                <c:set var="translation">
                                                    <digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
                                                </c:set>
                                                <c:set var="message">
                                                    <digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                                                </c:set>
                                                <c:set var="quote">'</c:set>
                                                <c:set var="escapedQuote">\'</c:set>
                                                <c:set var="msg">
                                                ${fn:replace(message,quote,escapedQuote)}
                                                </c:set>
                                                <digi:link href="/viewMyDesktop.do" styleClass="comment"
onclick="return quitRnot1('${msg}')" title="${translation}" >
                                                    <digi:trn key="aim:portfolio">
                                                        Portfolio
                                                    </digi:trn>
                                                </digi:link>&nbsp;&gt;&nbsp;
                                            </c:if>
                                            <c:set var="translation">
                                                <digi:trn key="aim:clickToViewAddActivityStep1">
                                                Click here to go to Add Activity Step 1</digi:trn>
                                            </c:set>
                                            <digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment"
title="${translation}" >
                                                <c:if test="${aimEditActivityForm.editAct == true}">
                                                    <digi:trn key="aim:editActivityStep1">
                                                        Edit Activity - Step 1
                                                    </digi:trn>
                                                </c:if>
                                                <c:if test="${aimEditActivityForm.editAct == false}">
                                                    <digi:trn key="aim:addActivityStep1">
                                                        Add Activity - Step 1
                                                    </digi:trn>
                                                </c:if>
                                            </digi:link>&nbsp;&gt;&nbsp;
                                            
                                            <c:set var="translation">
                                                <digi:trn key="aim:clickToViewAddActivityStep2">
                                                Click here to goto Add Activity Step 2</digi:trn>
                                            </c:set>
                                            <digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment"
title="${translation}" >
                                                <digi:trn key="aim:addActivityStep2">
                                                    Step 2
                                                </digi:trn>
                                            </digi:link>&nbsp;&gt;&nbsp;
                                            
                                            <c:set var="translation">
                                                <digi:trn key="aim:clickToViewAddActivityStep3">
                                                Click here to goto Add Activity Step 3</digi:trn>
                                            </c:set>
                                            <digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment"
title="${translation}" >
                                                <digi:trn key="aim:addActivityStep3">
                                                    Step 3
                                                </digi:trn>
                                            </digi:link>&nbsp;&gt;&nbsp;
                                            <digi:trn key="aim:addActivityStep4">
                                                Step 4
                                            </digi:trn>
                                        </span>
                                    </td>
                                </tr>
                            </table>
                    </td></tr>
                    <tr><td>
                            <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                                <tr>
                                    <td height=16 vAlign=center width="100%"><span class=subtitle-blue>
                                        <c:if test="${aimEditActivityForm.editAct == false}">
                                            <digi:trn key="aim:addNewActivity">
                                                Add New Activity
                                            </digi:trn>
                                        </c:if>
                                        <c:if test="${aimEditActivityForm.editAct == true}">
                                            <digi:trn key="aim:editActivity">
                                                Edit Activity
                                            </digi:trn>:
                                            <bean:write name="aimEditActivityForm" property="title"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                    </td></tr>
                    <tr> <td>
                            <digi:errors/>
                    </td></tr>
                    <tr><td>
                            <table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
                                <tr><td width="75%" vAlign="top">
                                        <table cellPadding=0 cellSpacing=0 width="100%" vAlign="top" >
                                            <tr>
                                                <td width="100%">
                                                    <table cellPadding=0 cellSpacing=0 width="100%">
                                                        <tr>
                                                            <td width="13" height="20" background="module/aim/images/left-side.gif"></td>
                                                            <td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
                                                                <digi:trn key="aim:step12">IPA Contracting</digi:trn>
                                                            </td>
                                                            <td width="13" height="20" background="module/aim/images/left-side.gif"></td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            
                                            <tr valign="top">
                                                <td width="100%" bgcolor="#f4f4f2">
                                                    <table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left"
                                                           bgcolor="#006699">
                                                        <tr>
                                                            <td bgColor=#f4f4f2 align="center" vAlign="top">
                                                                <table width="95%">
                                                                    <tr>
                                                                        <td>
                                                                            <IMG alt=Link height=10
                                                                                 src="../ampTemplate/images/arrow-014E86.gif" width=15>
                                                                            <a title="<digi:trn key="aim:ipacontract">IPA Contracting</digi:trn>">
                                                                                <b><digi:trn key="aim:ipacontract">IPA Contracting</digi:trn></b>
                                                                            </a>
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                     <tr><td><div id="ajaxDeleteContrcat"></div></td></tr>
                                                                    
                                                                    <tr><td valign="top">
                                                                            <logic:notEmpty name="aimEditActivityForm" property="contracts">
                                                                                <bean:define id="mode" value="form" type="java.lang.String" toScope="request"/>
                                                                                <c:forEach items="${aimEditActivityForm.contracts}" var="contract" varStatus="idx">
                                                                                
                                                                                
                                                                                
                                                                                    <table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:name">Contract name:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                <a style="cursor:pointer;color:#006699" title="Click to edit the contract" onClick='editContract(${idx.count})'>
                                                                                                ${contract.contractName}</a>
                                                                                                 [<a
                                                                                                style="cursor:pointer;color:#006699"
                                                                                                title="Click to remove the activity"
                                                                                                onClick='callUrl(${idx.count})'><digi:trn key="aim:delete">delete</digi:trn></a>]
                                                                                               
                                                                                            </td>
                                                                                            
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:description">Description:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                               ${contract.description}
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                  <c:if test ="${not empty contract.activityCategory}">
                                                                                                    ${contract.activityCategory.value}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                         
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:startOfTendering">Start of Tendering:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.formattedStartOfTendering}
                                                                                           </td>
                                                                                            
                                                                                        </tr>	
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.formattedSignatureOfContract}
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                        
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:if test="${not empty contract.organization}">
                                                                                                     ${contract.organization.name}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.formattedContractCompletion}
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                        
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:status">Status:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                
                                                                                                <c:if test ="${not empty contract.status}">
                                                                                 
                                                                                                    ${contract.status.value}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b>IB:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.totalECContribIBAmount}
                                                                                                ${contract.totalECContribIBCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b>INV:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalECContribINVAmount}
                                                                                               ${contract.totalECContribINVCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        
                                                                                        
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b>Central:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribCentralAmount}
                                                                                                ${contract.totalNationalContribCentralCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b>Regional:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribRegionalAmount} 
                                                                                              ${contract.totalNationalContribRegionalCurrency}
                                                                                   
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b>IFIs:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribIFIAmount}
                                                                                               ${contract.totalNationalContribIFICurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b>IB:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalPrivateContribAmount}
                                                                                                ${contract.totalPrivateContribCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                        
                                                                                        
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                    
                                                                                            <td colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>&nbsp;
                                                                                            </td>
                                                                                            <td>
                                                                                               
                                                                                    
                                                                                                    <logic:notEmpty name="contract" property="disbursements">
                                                                                                         <table>
                                                                                              
                                                                                                        <c:forEach  items="${contract.disbursements}" var="disbursement" >
                                                                                                            <tr>
                                                                                          
                                                                                                                <td align="left" valign="top">
                                                                                                                    <c:if test="${disbursement.adjustmentType==0}">
                                                                                                                          <digi:trn key="aim:actual">Actual</digi:trn>
                                                                                                                   </c:if>
                                                                                                                    <c:if test="${disbursement.adjustmentType==1}">
                                                                                                                          <digi:trn key="aim:planned">Planned</digi:trn>
                                                                                                                   </c:if>
                                                                                                    
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                    ${disbursement.amount}
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                   ${disbursement.currency.currencyName} 
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                    ${disbursement.disbDate}
                                                                                                                    
                                                                                                                </td>
                                                                                                            </tr>
                                                                                                        </c:forEach>
                                                                                                        </table>
                                                                                                    </logic:notEmpty>						
                                                                                                		
                                                                                            </td>		
                                                                                        </tr>		
                                                                                        
                                                                                    </table>
                                                                                    
                                                                                    &nbsp;
                                                                                   
                                                                                </c:forEach>
                                                                                
                                                                            </logic:notEmpty>
                                                                            
                                                                    </td></tr>
                                                                    
                                                                    
                                                                    <tr><td>
                                                                            &nbsp;
                                                                    </td></tr>
                                                                    <tr><td>
                                                                            <input type="button" value="Add IPA Contract" class="buton" onclick="addIPAContract()"/>
                                                                            
                                                                    </td></tr>
                                                                    
                                                                    <tr>
                                                                        <td align="left">
                                                                            
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <td width="25%" vAlign="top" align="left">
                                                    <!-- edit activity form menu -->
                                                                                         <jsp:include page="editActivityMenu.jsp" flush="true" />
                                                    <!-- end of activity form menu -->
                                            </td></tr>
                                        </table>
                                </td></tr>
                                <tr><td>
                                        &nbsp;
                                </td></tr>
                            </table>
                        </td>
                        <td width="10">&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    </digi:form>
    
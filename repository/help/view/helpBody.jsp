<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:instance property="helpForm" />
<table width="100%" align="center" cellpadding="5" cellspacing="0">
          <tr>
            <td height="16" valign="center"><c:if test="${helpForm.blankPage==false}">
              <c:if test="${not empty helpForm.helpErrors}">
                <c:forEach var="error" items="${helpForm.helpErrors}"> <font color="red">${error}</font><br />
                </c:forEach>
              </c:if>
            </c:if></td>
          </tr>
  <tr>
            <td height="16" valign="center"><span class="subtitle-blue">
              <digi:trn key="help:selectedTpc">Selected Topic</digi:trn>
            </span> </td>
  </tr>
          <tr>
            <td>
                     <c:if test="${empty helpForm.topicKey}"><b>
                       <digi:edit key="help:topic:default">no topic selected</digi:edit></b> </c:if>
                        <c:if test="${not empty helpForm.topicKey}">
                          <c:if test="${helpForm.topicKey!=''}"><b><digi:trn key="${helpForm.titleTrnKey}" ></digi:trn></b></c:if>
                      </c:if>                  
                 
                  	<c:if test="${not empty helpForm.topicKey}">
                      <c:if test="${helpForm.topicKey!=''}">
                        <digi:edit key="${helpForm.bodyEditKey}">no text preview</digi:edit>
                      </c:if>
                    </c:if>                 
                    </td>
          </tr>
        </table>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<digi:instance property="orgProfilePIForm"/>
<div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

<table border="1">
  
    <tr>
        <td colspan="2" rowspan="2">
            PARIS DECLARATION INDICATORS - DONORS</td>
        <td colspan="3" align="center">
            All donors
       </td>
       <td colspan="2" align="center">
           ${orgProfilePIForm.organization.name}
       </td>
    </tr>
    <tr>
        <td>2005 Baseline</td>
        <td>${orgProfilePIForm.fiscalYear-1} Value</td>
        <td>2010 Target</td>
        <td>2005 Baseline</td>
        <td>${orgProfilePIForm.fiscalYear-1} Value</td>
        
    </tr>
    <c:forEach var="indicator" items="${orgProfilePIForm.indicators}">
        <c:set var="percent">
             <c:if test="${indicator.prIndicator.indicatorCode!='5aii'&&indicator.prIndicator.indicatorCode!='6'}">
                %
            </c:if>  
        </c:set>
        <tr>
            <td>${indicator.prIndicator.indicatorCode}</td>
            <td>${indicator.prIndicator.name}</td>
            <td>${indicator.allDonorBaseLineValue}${percent}</td>
            <td>${indicator.allCurrentValue}${percent}</td>
            <td>${indicator.allTargetValue}${percent}</td>
            <td>${indicator.orgBaseLineValue}${percent}</td>
            <td>${indicator.orgPreviousYearValue}${percent}</td>
                
        </tr>
    </c:forEach>
    
    
</table>
</div>
 


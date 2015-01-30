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

<script type="text/javascript" src='<digi:file src="module/aim/scripts/table_utils.js"/>' />

<h1 class="admintitle"><digi:trn>Aid Effectiveness Indicator Manager</digi:trn></h1>

<%-- This is the search form --%>
<digi:form action="/aidEffectivenessIndicatorsManager.do" method="post" styleId="searchForm">
    <html:hidden property="actionParam" value="search"/>
    <table style="font-family: verdana; font-size: 11px;" border="0" width="100%">
        <tr>
            <td>
                <b><digi:trn key="aim:indicatorType">Indicator Type</digi:trn>:</b>
                &nbsp;
                <html:select style="inp-text" property="indicatorType" styleId="searchIndicatorType">
                    <html:option value="-1">
                        -<digi:trn key="aim:selectIndicatorType">Choose One</digi:trn>-
                    </html:option>
                    <html:option value="0">
                        <digi:trn key="aim:dropDownList">Drop down list</digi:trn>
                    </html:option>
                    <html:option value="1">
                        <digi:trn key="aim:radioOption">Radio option</digi:trn>
                    </html:option>
                </html:select>

                &nbsp;
                &nbsp;

                <b><digi:trn key="aim:keyword">Keyword</digi:trn>:</b>
                &nbsp;
                <html:text property="ampIndicatorName" styleId="searchAmpIndicatorName" />

                &nbsp;
                &nbsp;

                <b><digi:trn key="aim:activeOnly">Active Only</digi:trn>:</b>
                    &nbsp;
                <html:checkbox property="active" styleId="searchActive"/>

                &nbsp;
                &nbsp;

                <html:reset styleClass="dr-menu" onclick="javascript:cleanUpForm(); return false;">
                    <digi:trn key="btn:reset">Reset</digi:trn>
                </html:reset>

                &nbsp;

                <html:submit styleClass="dr-menu"><digi:trn key="btn:go">Go</digi:trn></html:submit>
            </td>

        </tr>
    </table>
</digi:form>
<%-- End of the search form --%>

<br/>
<br/>

<%-- Search result list--%>
<logic:present name="searchResult">
    <table class="inside" width="100%" cellpadding="4" id="searchResultsTableId">

        <tr align="center" bgcolor="#c7d4db" style="font-size:12px; font-weight:bold">
            <td><digi:trn>Indicator Name</digi:trn></td>
            <td><digi:trn>Indicator Tooltip</digi:trn></td>
            <td><digi:trn>Is Active</digi:trn></td>
            <td><digi:trn>Is Mandatory</digi:trn></td>
            <td><digi:trn>Indicator Type</digi:trn></td>
            <td></td>
        </tr>

        <logic:iterate name="searchResult" id="indicator" type="org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator">
            <tr>
                <td width="25%">
                    <bean:write name='indicator' property='ampIndicatorName' />
                </td>
                <td width="25%">
                    <bean:write name='indicator' property='tooltipText' />
                </td>
                <td width="9%" align="center">
                    <c:choose>
                        <c:when test="${indicator.active}">
                            <digi:trn key="aim:yes">Yes</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn key="aim:no">No</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="9%" align="center">
                    <c:choose>
                        <c:when test="${indicator.mandatory}">
                            <digi:trn key="aim:yes">Yes</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn key="aim:no">No</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="15%" align="center">
                    <c:choose>
                        <c:when test="${indicator.indicatorType == 1}">
                            <digi:trn key="aim:radioOption">Radio option</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn key="aim:dropDownList">Drop down list</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="17%" align="center">
                    edit | delete
                </td>
            </tr>
        </logic:iterate>
    </table>
</logic:present>

<script type="text/javascript">
    function cleanUpForm() {
        document.getElementById('searchIndicatorType').value = -1;
        document.getElementById('searchAmpIndicatorName').value = '';
        document.getElementById('searchActive').checked  = false;
    }

    setStripsTable("searchResultsTableId", "tableEven", "tableOdd");
    setHoveredTable("searchResultsTableId", false);
</script>
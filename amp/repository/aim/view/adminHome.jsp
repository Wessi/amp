<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<jsp:include page="allVisibilityTags.jsp" />
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width=757 align="left">
  <tr>
    <td class="r-dotted-lg" width="5">&nbsp;</td>
    <td align="left" class="r-dotted-lg" vAlign="top" width="750px">
      <table cellPadding="5" cellSpacing="0" width="100%">
        <tr>
          <td height="15">
            <span class="crumb">
              <digi:trn key="aim:AmpAdminHome">
              Admin Home
              </digi:trn>
            </span>
          </td>
        </tr>
        <tr>
          <td height="10" vAlign="middle" align="center">
            <span class="subtitle-blue">
              <digi:trn key="aim:AmpAdminTools">
              Admin Tools
              </digi:trn>
            </span>
          </td>
        </tr>
        <tr>
          <td nowrap="nowrap" vAlign="top" align="center">
            <table bgColor="#ffffff" cellPadding=20 cellSpacing="0" class="box-border-nopadding" width="100%">
              <tr align="center" bgcolor="#E1E1E1" vAlign="top">
                <td class="f-names" nowrap="nowrap">
                  <table bgColor="#ffffff" cellPadding="0" cellSpacing="0" class="box-border-nopadding" width="380" >
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class="f-names" nowrap="nowrap">
                      <digi:trn key="aim:administrative">
                      Administrative
                      </digi:trn>
                      </td>
                    </tr>
                    <tr align="center" bgcolor="#f4f4f2" valign="top">
                      <td class="f-names">
                        <table cellPadding="0" cellSpacing="0" width="320px">                      	

						<module:display name="Feature Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnFeatureManager">
                                <digi:trn key="aim:clickToAccessFeatureManager">Click here to access Feature Manager</digi:trn>
                              </c:set>
                              <digi:link href="/visibilityManager.do" title="${trnFeatureManager}" >
                                <digi:trn key="aim:theFeatureManager">Feature Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>  
                          <module:display name="Flag uploader" parentModule="ADMINISTRATIVE SECTION"> 
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnUploadFlags">
                                <digi:trn key="aim:clickToUploadFlags">Click here to upload and select flags</digi:trn>
                              </c:set>
                              <digi:link href="/flagUploader.do" title="${trnUploadFlags}" >
                                <digi:trn key="aim:flagUploaderSelector">
                                Flag uploader/selector
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <tr>
                          <module:display name="Global Permission Manager" parentModule="ADMINISTRATIVE SECTION"> 
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="gatePermLink">
                                <digi:trn key="aim:gatePermLinkTitle">Click here to manage gate permissions and assignments</digi:trn>
                              </c:set>
                              <html:link href="/gateperm/managePermMap.do" title="${gatePermLinkTitle}">
                                <digi:trn key="aim:globalPermissionManager">
                                	Global Permission Manager
                                </digi:trn>
                              </html:link>
                            </td>
                           </module:display>
                          </tr>
                          
                          <module:display name="Global Settings" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnSystemSettings">
                                <digi:trn key="aim:clickToViewSystemSettings">Click here to view System Settings</digi:trn>
                              </c:set>
                              <digi:link href="/GlobalSettings.do" title="${trnSystemSettings}" >
                                <digi:trn key="aim:GlobalSettings">Global Settings</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <feature:display name="Applied Patches" module="Applied Patches">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnSystemSettings">
                                <digi:trn key="aim:clickToViewSystemSettings">Click here to view the Patches applied</digi:trn>
                              </c:set>
                              <a href="/xmlpatcher/xmlpatches.do?mode=listPatches" title="${trnSystemSettings}">
                                <digi:trn key="aim:DbPatches">Database Patches</digi:trn>
                              </a>
                            </td>
                          </tr>
                          </feature:display>
                          <module:display name="Admin Translation Manager" parentModule="ADMINISTRATIVE SECTION"> 
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnTranslationManager">
                                <digi:trn key="aim:clickToViewTranslationManager">Click here to view Translation Manager</digi:trn>
                              </c:set>
                              <digi:link href="/translationManager.do" title="${trnTranslationManager}" >
     				                <digi:trn key="aim:translationManager">Translation Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
						</module:display>
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnWorkspaceManager">
                                <digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
                              </c:set>
                              <digi:link href="/workspaceManager.do~page=1~reset=true" title="${trnWorkspaceManager}" >
                                <digi:trn key="aim:workspaceManager">Workspace Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>

						<module:display name="Category Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewCategoryManager">Click here to view Category Manager</digi:trn>
                              </c:set>
                              <digi:link href="/categoryManager.do" title="${translation}" contextPath="/categorymanager" >
                                <digi:trn key="aim:categoryManager">Category Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <module:display name="User Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:tipUserManager">Click here to view User Manager</digi:trn>
                              </c:set>
                              <digi:link module="um" href="/viewAllUsers.do?reset=true" title="${translation}">
                                <digi:trn key="aim:UsertManager">User Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <feature:display  name="Paris Indicators Targets Manager" module="Admin Home">
                            <tr>
                              <td class="f-names" nowrap="nowrap">
                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                                <c:set var="translation">
                                  <digi:trn key="aim:tipViewAhSurveis">Click here to view Paris Indicator Manager</digi:trn>
                                </c:set>
                               <digi:link module="aim" href="/viewAhSurveyFormulas.do" title="${translation}">
                                  <digi:trn key="aim:parisIndManager">Paris Indicators Targets Manager</digi:trn>
                                </digi:link>
                              </td>
                            </tr>
                          </feature:display>
                         <feature:display name="Message Manager" module="ADMIN">
                          	<tr>
                          		<td class="f-names" nowrap="nowrap">
                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                                <c:set var="trn">
                                  <digi:trn key="message:viewSettings">Click here to view Message Settings</digi:trn>
                                </c:set>
                               <digi:link module="message" href="/msgSettings.do?actionType=getSettings" title="${trn}">
                                  <digi:trn key="message:messagesManager">Message Manager</digi:trn>
                                </digi:link>
                              </td>
                          	</tr>
                          </feature:display> 
                          	<feature:display name="Admin Topics Help" module="HELP">
                          	<tr>
                          		<td class="f-names" nowrap="nowrap">
                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                                <c:set var="trn">
                                  <digi:trn key="help:viewSettings">Click here to view Help Topics Admin</digi:trn>
                                </c:set>
                               <digi:link module="help" href="/helpActions.do~actionType=viewAdmin" title="${trn}">
                                  <digi:trn key="help:helpTopicAdmin">Help Topics Admin</digi:trn>
                                </digi:link>
                              </td>
                          	</tr>
                          	</feature:display>
                          	
                          	 <module:display name="Quartz Job Manager" parentModule="ADMINISTRATIVE SECTION"> 
                            <tr>
                                <td class="f-names" nowrap="nowrap">
                                    <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                                    <c:set var="trn">
                                        <digi:trn key="aim:viewSettings">Click here to view Job Manager</digi:trn>
                                    </c:set>
                                    <digi:link module="aim" href="/quartzJobManager.do?action=all" title="${trn}">
                                        <digi:trn key="aim:jobManager">Job Manager</digi:trn>
                                    </digi:link>
                                </td>
                            </tr>                   
                            </module:display>
                            <!-- 
                            
                            <tr>
                              <td class="f-names" nowrap="nowrap">
                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                                <c:set var="trn">
                                  <digi:trn key="message:viewSettings">Click here to view TemplateAlerts Manager</digi:trn>
                                </c:set>
                                <digi:link module="message" href="/templatesManager.do?actionType=viewTemplates" title="${trn}">
                                  <digi:trn key="message:templatesManager">TemplateAlerts Manager</digi:trn>
                                </digi:link>
                              </td>
                            </tr>
                              -->
                              <!-- 
                            <tr>
                              <td class="f-names" nowrap="nowrap">
                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                                <c:set var="trn">
                                  <digi:trn key="aim:viewSettings">Click here to view Job Manager</digi:trn>
                                </c:set>
                                <digi:link module="message" href="/quartzJobManager.do" title="${trn}">
                                  <digi:trn key="aim:jobManager">Job Manager</digi:trn>
                                </digi:link>
                              </td>
                            </tr>
                            -->
                                                       
						  <!-- hidden ECS Debug! -->
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:tipUserManager">Click here to view Lucene index</digi:trn>
                              </c:set>
                              <digi:link module="aim" href="/luceneIndex.do" title="${translation}">
                                <digi:trn key="aim:luceneDebug2">ECS Debug</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
						  <!-- -->
						  
						  <tr>
						    <td class="f-names" nowrap="nowrap">
						      <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:tipUserManager">Click here to edit Welcome screen</digi:trn>
                              </c:set>	
                               <digi:link module="um" href="/previewFirstPage.do">
                                <digi:trn key="um:welcomePageEdit">Welcome Screen</digi:trn>
                              </digi:link>                             					      
						    </td>
						  </tr>
						  
						  
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
                <td>
                  <table bgColor="#ffffff" cellPadding="0" cellSpacing="0" class="box-border-nopadding" width="380">
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class="f-names" nowrap="nowrap">
                      <digi:trn key="aim:dataFields">
                      Data Fields
                      </digi:trn>
                      </td>
                    </tr>
                    <tr align="center" bgcolor="#f4f4f2" height="120" valign="top">
                      <td class="f-names">
                        <table cellPadding="0" cellSpacing="0" width="320px">
                        
                         <module:display name="Fiscal Calendar Manager" parentModule="ADMINISTRATIVE SECTION"> 
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewFiscalCalendarManager">Click here to view Fiscal Calendar Manager</digi:trn>
                              </c:set>
                              <digi:link href="/fiscalCalendarManager.do" title="${translation}" >
                                <digi:trn key="aim:CalendarManager">Calendar Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          
                          <module:display name="Currency Manager" parentModule="ADMINISTRATIVE SECTION">
	                          <tr>
	                            <td class="f-names" nowrap="nowrap">
	                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
	                              <c:set var="translation">
	                                <digi:trn key="aim:clickToViewCurrencyManager">Click here to view Currency Manager</digi:trn>
	                              </c:set>
	                              <digi:link href="/currencyManager.do" title="${translation}" >
	                                <digi:trn key="aim:currencyManager">Currency Manager</digi:trn>
	                              </digi:link>
	                            </td>
	                          </tr>
                          </module:display>
                          <module:display name="Currency Rates Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewCurrencyRatesManager">Click here to view Currency Rates Manager</digi:trn>
                              </c:set>
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <digi:link href="/showCurrencyRates.do~clean=true~timePeriod=1" title="${translation}">
                                <digi:trn key="aim:currencyRateManager">Currency Rate Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <%--
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewRegionManager">Click here to view Region Manager</digi:trn>
                              </c:set>
                              <digi:link href="/locationManager.do" title="${translation}" >
                                <digi:trn key="aim:regionManager">Region Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          --%>
                          <module:display name="Dynamic Region Manager" parentModule="ADMINISTRATIVE SECTION">
                           <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewRegionManager">Click here to view Region Manager</digi:trn>
                              </c:set>
                              <digi:link href="/dynLocationManager.do" title="${translation}" >
                                <digi:trn key="aim:regionManager">Region Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <module:display name="Sector  Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewSectorManager">Click here to view Sector Manager</digi:trn>
                              </c:set>
                              <digi:link href="/getSectorSchemes.do" title="${translation}">
                                <digi:trn key="aim:sectorManager">Sector Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <feature:display  name="Table Widgets" module="WIDGETS">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="widgets:clickToViewTableWidgetAdmin">Click here to view Table Widgets Manager</digi:trn>
                              </c:set>
                            <digi:link module="widget" href="/adminTableWidgets.do" title="${translation}">
                                  <digi:trn key="widgets:tableWidgetManager">Table Widget Manager</digi:trn>
                                </digi:link>
                            </td>
                          </tr>
                          </feature:display>
                           <feature:display  name="Sector Table Widgets" module="WIDGETS">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn>Click here to view Sector Table Widgets Manager</digi:trn>
                              </c:set>
                            <digi:link module="widget" href="/sectorTableManager.do" title="${translation}">
                                  <digi:trn>Sector Table Widget</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </feature:display>
                           <feature:display  name="Paris Indicator Table Widgets" module="WIDGETS">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn>Click here to view Paris Indicator Table Table Widgets Manager</digi:trn>
                              </c:set>
                            <digi:link module="widget" href="/piTableWidgetManager.do" title="${translation}">
                                  <digi:trn> Paris Indicator Table Widget</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </feature:display>
                          <feature:display  name="Org Profile Widgets" module="WIDGETS">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="widgets:clickToViewOrgProfileWidgetAdmin">Click To View Org Profile Widget Admin</digi:trn>
                              </c:set>
                              <a href="/widget/orgProfileManager.do" title="${translation}">
                                <digi:trn key="widgets:orgProfileWidgetManager">Org Profile Widget Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                          </feature:display>
                          <feature:display  name="Indicator chart Widgets" module="WIDGETS">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="gis:clickToViewChartWidgetAdmin">Click here to view Indicator Chart Widgets Manager</digi:trn>
                              </c:set>
                              <a href="/widget/indicatorchartwidgets.do" title="${translation}">
                                <digi:trn key="widget:indicatorChartWidgetManager">Indicator Chart Widget Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                          </feature:display>
                            <feature:display  name="Results Dashboard Data" module="WIDGETS">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="widget:clickToViewResultsDashboardDataManager">Click here to view Results Dashboard Data Manager</digi:trn>
                              </c:set>
                              <a href="/widget/indSectRegManager.do" title="${translation}">
                                <digi:trn key="widget:ResultsDashboardDataManager">Results Dashboard Data Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                          </feature:display>
                          <feature:display  name="Widget Places" module="WIDGETS">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="widget:clickToViewWidgetPlaceAdmin">Click here to view Widgets Place Manager</digi:trn>
                              </c:set>
                              <a href="/widget/widgetplaces.do" title="${translation}">
                                <digi:trn key="widget:widgetPlaceManager">Widget Place Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                          </feature:display>
                          <!-- Satus Manager is deprecated. Use Category Manager instead. <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewStatusManager">Click here to view Status Manager</digi:trn>
                              </c:set>
                              <digi:link href="/statusManager.do" title="${translation}" >
                                <digi:trn key="aim:statusManager">Status Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>  -->
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>

              <tr align="center" bgcolor="#E1E1E1" valign="top">
                <td>
                  <table bgColor="#ffffff" cellPadding="0" cellSpacing="0" class="box-border-nopadding" width="380">
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class="f-names" nowrap="nowrap">
                      <digi:trn key="aim:programMonitoring">
                      Program Monitoring
                      </digi:trn>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2" align="center" height="80" valign="top">
                      <td class="f-names">
                        <table cellPadding="0" cellSpacing="0" width="320px">
						 <!-- 
						 <feature:display name="Admin - Component Type" module="Components">
                          <tr>
                            <td class="f-names" nowrap="nowrap"><digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewcomponentsTypeManager">Click here to view Components Types Manager</digi:trn>
                              </c:set>
                              <digi:link href="/updateComponentType.do" title="${translation}" >
                                <digi:trn key="aim:componentsTypesManager">Components Types Manager</digi:trn>
                              </digi:link>         </td>
                          </tr>
						 </feature:display>
						  -->
						 <!--
						 <feature:display name="Admin - Component" module="Components">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewcomponentsManager">Click here to view Components Manager</digi:trn>
                              </c:set>
                              <digi:link href="/getComponents.do" title="${translation}" >
                                <digi:trn key="aim:componentsManager">Components Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
						 </feature:display>
						 -->
						 <module:display name="Program Managers" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                          <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewmultiProgrammanager">Click here to view Multi Program Manager</digi:trn>
                              </c:set>
                              <digi:link href="/themeManager.do~view=multiprogram" title="${translation}" >
                                <digi:trn key="aim:multManager">Multi Program Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                          <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewindicatormanager">Click here to view Indicator Manager</digi:trn>
                              </c:set>
                              <digi:link href="/viewIndicators.do?sortBy=nameAsc" title="${translation}" >
                                <digi:trn key="aim:indManager">Indicator Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                              <tr>
                          <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:ClickToViewMultiProgramConfigurationPage">Click here to view Multi Program Configuration Page</digi:trn>
                              </c:set>
                              <digi:link href="/programConfigurationPage.do" title="${translation}" >
                                <digi:trn key="aim:multManagerConfiguration">Multi Program Configuration</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
						</module:display>

<!--
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewIndicatorManager">Click here to view Indicator Manager</digi:trn>
                              </c:set>
                              <digi:link href="/overallIndicatorManager.do" title="${translation}" >
                                <digi:trn key="aim:ProgramProjectManager">
                                Program/Project Indicator Manager
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewProgramTypeManager">Click here to view Program Type Manager</digi:trn>
                              </c:set>
                              <digi:link href="/programTypeManager.do" title="${translation}" >
                                <digi:trn key="aim:ProgramTypeManager">
                                Program Type Manager
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
-->	
						<module:display name="Audit Logger Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewAuditLoggerManager">Click here to view Audit Logger Manager</digi:trn>
                              </c:set>
                              <digi:link href="/auditLoggerManager.do" title="${translation}" >
                                <digi:trn key="aim:AuditLoggerManager">
                                	Audit Logger Manager
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
                <td>
                  <table bgColor="#ffffff" cellPadding="0" cellSpacing="0" class="box-border-nopadding" width="380" title="">
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class="f-names" nowrap="nowrap" align="center">
                      <digi:trn key="aim:projectsActivities"> Projects / Activites</digi:trn>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2" align="center" height="80" valign="top">
                      <td>
                        <table cellPadding="0" cellSpacing="0" width="320">
                        <module:display name="Activity Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnActivityManager">
                                <digi:trn key="aim:clickToViewActivityManager">Click here to view Activity Manager</digi:trn>
                              </c:set>
                              <digi:link href="/activityManager.do" title="${trnActivityManager}" >
                                <digi:trn key="aim:activityManager">Activity Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnOrganizationManager">
                                <digi:trn key="aim:clickToViewOrganizationManager">Click here to view Organization Manager</digi:trn>
                              </c:set>
                              <digi:link href="/organisationManager.do?orgSelReset=true" title="${trnOrganizationManager}" >
                                <digi:trn key="aim:organizationManager">Organization Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <module:display name="Activity Export Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
								   <c:set var="trnDataExchangeExportTitle">
								    <digi:trn key="dataexchange:trnDataExchangeExportTitle">Click here to view Data Export Manager</digi:trn>
								   </c:set>
								   <digi:link module="dataExchange"  href="/exportWizard.do?method=prepear" title="${trnDataExchangeExportTitle}" >
								     <digi:trn key="dataexchange:DataExportManager">Data Export Manager</digi:trn>
							   	   </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <module:display name="Activity Import Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>
							   <c:set var="trnDataExchangeExportTitle">
							    <digi:trn key="dataexchange:trnDataExchangeImportTitle">Click here to view Data Import Manager</digi:trn>
							   </c:set>
							   <digi:link module="dataExchange"  href="/import.do" title="${trnDataExchangeExportTitle}" >
							      <digi:trn key="dataexchange:DataImportManager">Data Import Manager</digi:trn>
							   </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <module:display name="Budget Codes Exporter" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>							   
							   <digi:link href="/export.do" title="" >
							      <digi:trn key="dataexchange:exporter">Exporter</digi:trn>
							   </digi:link>
                            </td>
                          </tr>
                          </module:display>
                          <tr>
                            <td class="f-names" nowrap="nowrap">
                              <digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-th-BABAB9.gif" width="16"/>							   
							   <digi:link href="/reportsExport.do?action=new" title="" >
							      <digi:trn>Reports Import Export</digi:trn>
							   </digi:link>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
            <br />
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

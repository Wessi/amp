<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
    function addSector(param)
    {
        
        <digi:context name="addSec" property="context/addActivity.do" />
        document.aimEditActivityForm.action = "<%= addSec %>?step=2";
        document.aimEditActivityForm.target = "_self";
        document.aimEditActivityForm.submit();
    }
    </script>

<link rel="stylesheet" href="/TEMPLATE/ampTemplate/css/activityform_style.css" type="text/css">

<digi:instance property="aimEditActivityForm" />
<tr>
    <td>
        <!-- contents -->
        <table width="100%" bgcolor="#f4f4f2">
            <tr>
                <td class="separator1">
                    <IMG alt=Link height=10 src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width=15 />
					<digi:trn key="aim:sector">Sector</digi:trn>
                    <a href="javascript:popupwin()">
                        <img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif" alt="Click to get help on Status" width=10 height=10 border=0/>
                    </a>
                </td>
            </tr>
            <tr>
                <td class="requiredField">
                    * <digi:trn key="aim:chooseSector">Choose the sector.</digi:trn>
                </td>
            </tr>
            <tr>
                <td>
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td>
                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
                        <tr>
                            <td align="left">
                                <b>
                                    <digi:trn key="aim:sector">
                                        Sector
                                    </digi:trn>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#ffffff" width="100%">
                                <table cellPadding="1" cellSpacing="1" border="0"	bgcolor="#ffffff" width="100%">
                                    <c:forEach var="config" items="${aimEditActivityForm.sectors.classificationConfigs}" varStatus="ind">
                                    	<bean:define id="generalSector" value="false"/>
										<c:if test="${config.name== 'Primary' }">
											<bean:define id="auxSectorType" value="Primary Sector" />
											<logic:equal name="aimEditActivityForm" property="sectors.primarySectorVisible" value="true">
												<bean:define id="generalSector" value="true"/>
											</logic:equal>
										</c:if>
										<c:if test="${config.name== 'Secondary' }">
											<bean:define id="auxSectorType" value="Secondary Sector" />
											<logic:equal name="aimEditActivityForm" property="sectors.secondarySectorVisible" value="true">
												<bean:define id="generalSector" value="true"/>
											</logic:equal>
										</c:if>
										<c:if test="${config.name== 'Tertiary' }">
											<bean:define id="auxSectorType" value="Tertiary Sector" />
											<bean:define id="generalSector" value="true"/>
										</c:if>
										<bean:define id="contentDisabled">false</bean:define>
										<c:set var="contentDisabled"><field:display name="${auxSectorType}" feature="Sectors">false</field:display>
										</c:set>
										
										<c:if test="${contentDisabled==''}">
											<c:set var="contentDisabled">true</c:set>
										</c:if>
										<logic:equal name="generalSector" value="true">
                                        	<c:set var="sectorAdded" value="false"/>
	                                         <tr>
	                                             <td  align="left"> 
	                                                     <FONT color="red">
	                                                         *
	                                                     </FONT>
	                                                     <b><digi:trn key="aim:addActivitySectors:${auxSectorType}">${auxSectorType}</digi:trn></b>
	                                                </td>
	                                    	</tr>
                                        	<c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
                                            	<tr>
                                                	<td>
        												<bean:define id="auxSectorType" value="empty1"/>
                                                    	<div id="config${ind.count}">  
	                                                        <c:if test="${config.primary}">
	        	                                                    <div id="primaryConfig">
																	<c:set var="percentageType" value="primary"/>
	                                                        </c:if>
                                                       		<logic:equal name="config" property="primary" value="false">
                                                        	<div id="secondaryConfig">
															<c:set var="percentageType" value="secondary"/>
															</logic:equal>
                                                    			<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                                                       				<tbody>
                                                        				<c:forEach var="activitySectors" items="${aimEditActivityForm.sectors.activitySectors}" varStatus="index">
                                                            				<c:if test="${activitySectors.configId==config.id}">
                                                                				<tr> 
                                                                    				<td width="3%" vAlign="middle">
				                                                                        <html:multibox property="sectors.selActivitySectors" styleId="selActivitySectors" disabled="${contentDisabled}">
				                                                                            <c:if test="${activitySectors.subsectorLevel1Id == -1}">
				                                                                            ${activitySectors.sectorId}
				                                                                            </c:if>
				                                                                            
				                                                                            <c:if test="${activitySectors.subsectorLevel1Id != -1 && activitySectors.subsectorLevel2Id == -1}">
				                                                                            ${activitySectors.subsectorLevel1Id}
				                                                                            </c:if>
				                                                                            
				                                                                            <c:if test="${activitySectors.subsectorLevel1Id != -1 && activitySectors.subsectorLevel2Id != -1}">
				                                                                            ${activitySectors.subsectorLevel2Id}
				                                                                            </c:if>
                                                                        				</html:multibox>
                                                                    				</td>
                                                                    				<td  width="87%" valign="middle" align="left">
                                                                        
                                                                        					[${activitySectors.sectorScheme}]
				                                                                        <c:if test="${!empty activitySectors.sectorName}">
				                                                                            [${activitySectors.sectorName}]
                                                                        				</c:if>
														                               	<field:display name="${config.name} Sector Sub-Sector" feature="Sectors">
					                                                                        <c:if test="${!empty activitySectors.subsectorLevel1Name}">
					                                                                            [${activitySectors.subsectorLevel1Name}]
					                                                                        </c:if>
																							<field:display name="${config.name} Sector Sub-Sub-Sector" feature="Sectors">
					                                                                        <c:if test="${!empty activitySectors.subsectorLevel2Name}">
					                                                                            [${activitySectors.subsectorLevel2Name}]
					                                                                        </c:if>
																							</field:display>
																						</field:display>
                                                                    				</td>
                                                                    				<td width="5%" valign="middle" align="right">
                                                                       
                                                                    					<FONT color="red">*</FONT><digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;
																					</td>
				                                                                    <td width="5%" valign="middle" align="left">
				                                                                        <html:text name="activitySectors" indexed="true" property="sectorPercentage" 
				                                                                                   size="4" maxlength="5" onkeyup="fnChk(this, 'sector')" disabled="${contentDisabled}" style="background-color: white;"/>
                                                                    				</td>
                                                                				</tr>
                                                                				<c:set var="sectorAdded" value="true"/>
                                                            				</c:if>
                                                        				</c:forEach>
                                                    				</tbody>
                                                    			</table>
                                                      		</div>
                                                		</div>
                                                	</td>
                                            	</tr>
                                        	</c:if>
                                      
                                        	<tr>
                                            	<td>
                                                	<table cellSpacing="2" cellPadding="2">
                                                    	<tr>
	                                                        <td> &nbsp;
	                                                            <c:if test="${config.multisector||sectorAdded==false}">
	                                                                <field:display name="Add Sectors Button" feature="Sectors">
	                                                                    <html:hidden name="aimEditActivityForm" property="editAct"/>
	                                                                    <html:button styleClass="dr-menu"  
	                                                                                 property="submitButton" disabled="${contentDisabled}" onclick="addSectors(${aimEditActivityForm.editAct},${config.id});">
	                                                                        <digi:trn key="btn:addSectors">Add Sectors</digi:trn>
	                                                                    </html:button>
	                                                                </field:display> 
	                                                            </c:if>
	                                                        </td>
                                                        	<c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
		                                                        <td>&nbsp;
		                                                            <field:display name="Remove Sectors Button" feature="Sectors">
		                                                                <html:button styleClass="dr-menu" property="submitButton" disabled="${contentDisabled}" onclick="return removeSelSectors(${config.id})">
		                                                                    <digi:trn key="btn:removeSector">Remove Sector</digi:trn>
		                                                                </html:button>
		                                                            </field:display>
		                                                        </td>
                                                       		 	<td> &nbsp;
	                                                               <html:button styleClass="dr-menu" property="submitButton" onclick="dividePercentages('${percentageType}');">
																		<digi:trn key="dividePercentagesEqually">Divide percentages equally</digi:trn>
																	</html:button>                                                         
                                                        		</td>
                                                        	</c:if>
                                                    	</tr>
                                                	</table>
                                            	</td>
                                        	</tr>
                                        
                                        </logic:equal>
                                    </c:forEach>
                                    
                                </table>
                            </td>
                        </tr>
                    </table>
                    <!-- Add Sectors -->
                </td>
            </tr>
        </table>
    </td>
</tr>
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




<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>



<script language="JavaScript" type="text/javascript">



</script>

		<field:display name="National Planning Objectives" feature="NPD Programs">
		
		<digi:instance property="aimEditActivityForm" />
		
			<tr>
				<td class="separator1" title="<digi:trn key="aim:ProgramImp">Set of policies, projects and strategies grouped by area</digi:trn>">
				    <digi:trn key="aim:program">
				      Program
				    </digi:trn>
				</td>
			</tr>
			<tr>
				<td>
				  <digi:trn key="aim:selectProgram">
				    Select the program from the list.
				  </digi:trn>
				</td>
			</tr>
         
            <tr>
              	<td>
                	<table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
                  		<tr>
                    		<td align="left">
			                    <b>
			                        <digi:trn key="aim:nationalplanningobjectives">
			                        National Planning Objectives
			                        </digi:trn>
                      			</b>
                    		</td>
                  		</tr>
                  		<tr>
                    		<td bgcolor="#ffffff" width="100%">
                      			<table cellPadding=1 cellSpacing=1 border=0	bgcolor="#ffffff" width="100%">
                        			<tr>
                          				<td bgcolor="#ffffff">
                          				</td>
                        			</tr>

                        			<tr>
                         	 			<td>
                            				<table cellSpacing=0 cellPadding=0 border=0 bgcolor="#ffffff" width="100%">
                           						<digi:trn key="aim:defaultprogram">Default Program</digi:trn>:
			        							<c:if test="${!empty aimEditActivityForm.programs.nationalSetting}">
			        							${aimEditActivityForm.programs.nationalSetting.defaultHierarchy.name}
        										</c:if>
												<br>
												<tr>
                                           			<td>
                                          				<c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
															<div id="nationalProgConfig">
                                            					<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                                         							<tbody>
                                         								<c:forEach var="nationalPlanObjectivePrograms" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
																			<c:set var="program" value="${nationalPlanObjectivePrograms.program}" />
																			<tr>
																				<td>
																					<html:multibox property="programs.selectedNPOPrograms" value="${program.ampThemeId}" />
																					${nationalPlanObjectivePrograms.hierarchyNames}
																				</td>
																				<td width="5%" align="right" vAlign="center" nowrap="nowrap">
																					<FONT color="red">*</FONT> <digi:trn
																					key="aim:editActivity:program_percentage">Percentage</digi:trn>
																				</td>
																				<td width="5%" vAlign="center" align="left">
																					<html:text name="nationalPlanObjectivePrograms" indexed="true"
																					property="programPercentage" size="4" maxlength="5"
																					onkeyup="fnChk(this, 'program')" style="background-color: white;"/>
																				</td>
																			</tr>
																		</c:forEach>
																	</tbody>
																</table>
															</div>
                                          				</c:if>
													</td>
												</tr>
                                          		<tr>
													<td>
														<table cellSpacing="2" cellPadding="2">
															<tr>
                                              					<td>&nbsp;
                                                					<c:if test="${!empty aimEditActivityForm.programs.nationalSetting.defaultHierarchy.name && (aimEditActivityForm.programs.nationalSetting.allowMultiple||empty aimEditActivityForm.programs.nationalPlanObjectivePrograms)}">
                                   										<field:display name="Add Programs Button - National Plan Objective" feature="Program">
                                      										<html:button styleClass="dr-menu" property="submitButton" onclick="addProgram(1,${aimEditActivityForm.programs.nationalSetting.ampProgramSettingsId});">
																			<digi:trn key="btn:addPrograms">Add Programs</digi:trn>
																			</html:button>
                                    									</field:display>
                                      								</c:if>
																</td>
																<c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
																	<td>&nbsp;
                                              							<field:display name="Remove Program Button - National Plan Objective" feature="Program">
                                         									<html:button styleClass="dr-menu" property="submitButton" onclick="remProgram(1);">
																			<digi:trn key="btn:removeProgram">Remove program</digi:trn>
																			</html:button>
                                         								</field:display>
                                    								</td>
							                                 		<td> &nbsp;
																		<html:button styleClass="dr-menu" property="submitButton" onclick="dividePercentages('nationalProg');">
																			<digi:trn key="dividePercentagesEqually">Divide percentages equally</digi:trn>
																		</html:button>                                                          
                                  									</td>
                                								</c:if>
															</tr>
														</table>
													</td>
                                         		</tr>
                                       		</table>
                                     	</td>
                                   	</tr>
                                </table>
                            </td>
                       	</tr>
                    </table>
                </td>
            </tr>
	  	</field:display>

	 	<field:display name="Primary Program" feature="NPD Programs">
            <tr>
                <td>
                    <table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
                        <tr>
                            <td align="left">
                                <b>
                                  <digi:trn key="aim:primaryprogram">
                                 Primary Program
                                  </digi:trn>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#ffffff" width="100%">
                                <table cellPadding=1 cellSpacing=1 border=0	bgcolor="#ffffff" width="100%">
                                    <tr>
                                        <td bgcolor="#ffffff">
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <table cellSpacing=0 cellPadding=0 border=0 bgcolor="#ffffff" width="100%">
                                             	<digi:trn key="aim:defaultprogram">Default Program</digi:trn>:
                                              	<c:if test="${!empty aimEditActivityForm.programs.primarySetting}">
                                              		${aimEditActivityForm.programs.primarySetting.defaultHierarchy.name}
                                              	</c:if>
                                              	<br>
                                                <tr>
                                                  	<td>
                                                		<c:if test="${!empty aimEditActivityForm.programs.primaryPrograms}">
                                                  			<div id="primaryProgConfig">
                                                  				<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                                               						<tbody>
					                                               		<c:forEach var="primaryPrograms" items="${aimEditActivityForm.programs.primaryPrograms}">
					                                                        <c:set var="program" value="${primaryPrograms.program}"/>
					                                                   		<tr>
					                                                            <td>
					                                                              <html:multibox property="programs.selectedPPrograms" value="${program.ampThemeId}"/> ${primaryPrograms.hierarchyNames}
					                                                            </td>
					                                                              <td width="5%" align="right" valign="center" nowrap="nowrap"><font color="red">*</font>
					                                                                  <digi:trn key="aim:editActivity:program_percentage">Percentage</digi:trn></td>
					                                                             <td width="5%" vAlign="center" align="left">
					                                                              <html:text name="primaryPrograms" indexed="true" property="programPercentage"
					                                                              			 size="4" maxlength="5" onkeyup="fnChk(this, 'program')" style="background-color: white;"/>
					                                                            </td>
					                                                    	</tr>
					                                                  	</c:forEach>
																	</tbody>
																</table>
															</div>
                                                		</c:if>
													</td>
												</tr>
                                                <tr>
                                                  	<td>
														<table cellSpacing="2" cellPadding="2">
															<tr>
																<td>&nbsp;
                                                      				<c:if test="${!empty aimEditActivityForm.programs.primarySetting.defaultHierarchy&&(aimEditActivityForm.programs.primarySetting.allowMultiple||empty aimEditActivityForm.programs.primaryPrograms)}">
				                                                       <field:display name="Add Programs Button - Primary Programs" feature="Program">
					                                                       <html:button styleClass="dr-menu" property="submitButton" onclick="addProgram(2,${aimEditActivityForm.programs.primarySetting.ampProgramSettingsId});">
																			<digi:trn key="btn:addPrograms">Add Programs</digi:trn>
																			</html:button>
                                                       					</field:display>
                                                      				</c:if>
																</td>
                                                      			<c:if test="${!empty aimEditActivityForm.programs.primaryPrograms}">
																	<td>&nbsp;
																		<field:display name="Remove Program Button - Primary Programs" feature="Program">
																			<html:button styleClass="dr-menu" property="submitButton" onclick="remProgram(2);">
																			<digi:trn key="btn:removeProgram">Remove program</digi:trn>
																			</html:button>
                                                        				</field:display>
																	</td>
                                                  		 			<td> &nbsp;
																		<html:button styleClass="dr-menu" property="submitButton" onclick="dividePercentages('primaryProg');">
																			<digi:trn key="dividePercentagesEqually">Divide percentages equally</digi:trn>
																		</html:button>                                                            
                                                   					</td>
                                                      			</c:if>
															</tr>
														</table>
                                                  	</td>
                                                </tr>
                                        	</table>
                                    	</td>
                                	</tr>
                            	</table>
                            </td>
                        </tr>
                    </table>
                </td>
        	</tr>
	  	</field:display>

	  	<field:display name="Secondary Program" feature="NPD Programs">
            <tr>
                <td>
                    <table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
                        <tr>
                            <td align="left">
                                <b>
                                    <digi:trn key="aim:secondaryProgram">
                                     Secondary Program
                                    </digi:trn>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#ffffff" width="100%">
                                <table cellPadding=1 cellSpacing=1 border=0	bgcolor="#ffffff" width="100%">
                                    <tr>
                                        <td bgcolor="#ffffff">
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <table cellSpacing=0 cellPadding=0 border=0 bgcolor="#ffffff" width="100%">
                                               	<digi:trn key="aim:defaultprogram">Default Program</digi:trn>:
                                              	<c:if test="${!empty aimEditActivityForm.programs.secondarySetting}">
                                              	 ${aimEditActivityForm.programs.secondarySetting.defaultHierarchy.name}
                                               	</c:if>
                                               	<br>
                                                <tr>
                                                  	<td>
                                                		<c:if test="${!empty aimEditActivityForm.programs.secondaryPrograms}">
                                                  			<div id="secondaryProgConfig">
                                                  				<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                                               						<tbody>
                                               							<c:forEach var="secondaryPrograms" items="${aimEditActivityForm.programs.secondaryPrograms}" >
                                                   							<c:set var="program" value="${secondaryPrograms.program}"/>
                                                    						<tr>
                                                            					<td>
                                                              						<html:multibox property="programs.selectedSPrograms" value="${program.ampThemeId}"/> ${secondaryPrograms.hierarchyNames}
                                                            					</td>
                                                              					<td width="5%" align="right" valign="center" nowrap="nowrap">
                                                              
                                                              						<font color="red">*</font>
                                                                  					<digi:trn key="aim:editActivity:program_percentage">Percentage</digi:trn>
																				</td>
				                                                             	<td width="5%" vAlign="center" align="left">
				                                                              		<html:text name="secondaryPrograms" indexed="true" property="programPercentage"
				                                                              			 size="4" maxlength="5" onkeyup="fnChk(this, 'program')" style="background-color: white;"/>
                                                            					</td>
                                                          					</tr>
                                                   						</c:forEach>
																	</tbody>	
																</table>
															</div>
                                                		</c:if>
													</td>
												</tr>
                                            	<tr>
                                                	<td>
														<table>
															<tr>
																<td>&nbsp;
			                                                       <c:if test="${!empty aimEditActivityForm.programs.secondarySetting.defaultHierarchy&&(aimEditActivityForm.programs.secondarySetting.allowMultiple||empty aimEditActivityForm.programs.secondaryPrograms)}">
				                                                       <field:display name="Add Programs Button - Secondary Programs" feature="Program">
				                                                       <html:button styleClass="dr-menu" property="submitButton" onclick="addProgram(3,${aimEditActivityForm.programs.secondarySetting.ampProgramSettingsId});">
																			<digi:trn key="btn:addPrograms">Add Programs</digi:trn>
																		</html:button>
				                                                      	</field:display>
			                                                       	</c:if>
																</td>
	                                                      		<c:if test="${!empty aimEditActivityForm.programs.secondaryPrograms}">
																	<td>&nbsp;
				                                                       <field:display name="Remove Program Button - Secondary Programs" feature="Program">
				                                                         	<html:button styleClass="dr-menu" property="submitButton" onclick="remProgram(3);">
																			<digi:trn key="btn:removeProgram">Remove program</digi:trn>
																			</html:button>
	                                                         			</field:display>
																	</td>
																	<td> &nbsp;
																		<html:button styleClass="dr-menu" property="submitButton" onclick="dividePercentages('secondaryProg');">
																			<digi:trn key="dividePercentagesEqually">Divide percentages equally</digi:trn>
																		</html:button>                                                            
	                                                   				</td>
	                                                      		</c:if>
	                                                  		</tr>
														</table>
													</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <field:display name="NPD Program Description" feature="Program">
                <tr>
                    <td>
                         <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                         Description
		  				</a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                        <html:textarea property="programs.programDescription" rows="3" cols="75" styleClass="inp-text"/>
		  				</a>
                    </td>
                </tr>
			</field:display>
		</field:display>
		
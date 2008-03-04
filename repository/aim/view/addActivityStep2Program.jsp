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



<digi:instance property="aimEditActivityForm" />

                                      <tr>
                                        <td>
                                          <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
                                          <a title="<digi:trn key="aim:ProgramImp">Set of policies, projects and strategies grouped by area</digi:trn>">
                                          <b>
                                            <digi:trn key="aim:program">
                                              Program
                                            </digi:trn>
                                          </b>
</a>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <digi:trn key="aim:selectProgram">
                                            Select the program from the list.
                                          </digi:trn>
                                        </td>
                                      </tr>

                                      <field:display name="National Planning Objectives" feature="NPD Programs">
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
                                  							<c:if test="${!empty aimEditActivityForm.nationalSetting}">
                                  							${aimEditActivityForm.nationalSetting.defaultHierarchy.name}
                                  							</c:if>
															<br>
                                                        <c:if test="${!empty aimEditActivityForm.nationalPlanObjectivePrograms}">
                                                          <c:forEach var="nationalPlanObjectivePrograms" items="${aimEditActivityForm.nationalPlanObjectivePrograms}">
                                                          <c:set var="program" value="${nationalPlanObjectivePrograms.program}"/>
                                                            <tr>
                                                              <td>
                                                                <table width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="left">
                                                                  <tr>
                                                                    <td>
                                                                      <html:multibox property="selectedNPOPrograms" value="${program.ampThemeId}"/>
										                                         ${nationalPlanObjectivePrograms.hierarchyNames}
                                                                    </td>
                                                                      <td width="5%" align="right" vAlign="center" nowrap="nowrap">
                                                                    	<FONT color="red">*</FONT>
                                                                        
                                                                   	<digi:trn key="aim:editActivity:program_percentage">Percentage</digi:trn></td>

                                                                    <td width="5%" vAlign="center" align="left">

                                                                      <html:text name="nationalPlanObjectivePrograms" indexed="true" property="programPercentage"
                                                                      			 size="2" maxlength="3"  onkeyup="fnChk(this)"/>

                                                                    </td>
                                                                  </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                          </c:forEach>

                                                        </c:if>
                                                        <tr>
                                                          <td>
                                                          
															
                                                          <c:if test="${!empty aimEditActivityForm.nationalSetting.defaultHierarchy.name && (aimEditActivityForm.nationalSetting.allowMultiple||empty aimEditActivityForm.nationalPlanObjectivePrograms)}">
                                                          <field:display name="Add Programs Button - National Plan Objective" feature="Program">
                                                            <html:button styleClass="buton" property="submitButton" onclick="addProgram(1);">
															<digi:trn key="btn:addPrograms">Add Programs</digi:trn>
														</html:button>
                                                          </field:display>
                                                            </c:if>
                                                            <c:if test="${!empty aimEditActivityForm.nationalPlanObjectivePrograms}">
                                                            <field:display name="Remove Program Button - National Plan Objective" feature="Program">
                                                              <html:button styleClass="buton" property="submitButton" onclick="remProgram(1);">
															<digi:trn key="btn:removeProgram">Remove program</digi:trn>
														</html:button>
                                                              </field:display>
                                                            </c:if>
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
                                                      <c:if test="${!empty aimEditActivityForm.primarySetting}">
                                                      	${aimEditActivityForm.primarySetting.defaultHierarchy.name}
                                                      </c:if>
                                                      <br>
                                                        <c:if test="${!empty aimEditActivityForm.primaryPrograms}">
                                                          <c:forEach var="primaryPrograms" items="${aimEditActivityForm.primaryPrograms}">
                                                                <c:set var="program" value="${primaryPrograms.program}"/>
                                                            <tr>
                                                              <td>
                                                                <table width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="left">
                                                                  <tr>
                                                                    <td>
                                                                      <html:multibox property="selectedPPrograms" value="${program.ampThemeId}"/>

                                                                      ${primaryPrograms.hierarchyNames}                                                                    </td>
                                                                      <td width="5%" align="right" valign="center" nowrap="nowrap"><font color="red">*</font>
                                                                          <digi:trn key="aim:editActivity:program_percentage">Percentage</digi:trn></td>
                                                                     <td width="5%" vAlign="center" align="left">
                                                                      <html:text name="primaryPrograms" indexed="true" property="programPercentage"
                                                                      			 size="2" maxlength="3" onkeyup="fnChk(this)"/>                                                                    </td>
                                                                  </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                          </c:forEach>

                                                        </c:if>
                                                        <tr>
                                                          <td>
                                                            <c:if test="${!empty aimEditActivityForm.primarySetting.defaultHierarchy&&(aimEditActivityForm.primarySetting.allowMultiple||empty aimEditActivityForm.primaryPrograms)}">
                                                            <field:display name="Add Programs Button - Primary Programs" feature="Program">
                                                            <html:button styleClass="buton" property="submitButton" onclick="addProgram(2);">
															<digi:trn key="btn:addPrograms">Add Programs</digi:trn>
														</html:button>
                                                            </field:display>
                                                            </c:if>
                                                            <c:if test="${!empty aimEditActivityForm.primaryPrograms}">
																<field:display name="Remove Program Button - Primary Programs" feature="Program">
																<html:button styleClass="buton" property="submitButton" onclick="remProgram(2);">
															<digi:trn key="btn:removeProgram">Remove program</digi:trn>
														</html:button>
                                                              </field:display>
                                                            </c:if>
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
                                                      <c:if test="${!empty aimEditActivityForm.secondarySetting}">
                                                       ${aimEditActivityForm.secondarySetting.defaultHierarchy.name}
                                                       </c:if>
                                                       <br>
                                                        <c:if test="${!empty aimEditActivityForm.secondaryPrograms}">
                                                          <c:forEach var="secondaryPrograms" items="${aimEditActivityForm.secondaryPrograms}" >
                                                           <c:set var="program" value="${secondaryPrograms.program}"/>
                                                            <tr>
                                                              <td>
                                                                <table width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="left">
                                                                  <tr>
                                                                    <td>
                                                                      <html:multibox property="selectedSPrograms" value="${program.ampThemeId}"/>

                                                                      ${secondaryPrograms.hierarchyNames}                                                                    </td>
                                                                      <td width="5%" align="right" valign="center" nowrap="nowrap">
                                                                      
                                                                      <font color="red">*</font>
                                                                          <digi:trn key="aim:editActivity:program_percentage">Percentage</digi:trn></td>
                                                                     <td width="5%" vAlign="center" align="left">
                                                                      <html:text name="secondaryPrograms" indexed="true" property="programPercentage"
                                                                      			 size="2" maxlength="3" onkeyup="fnChk(this)"/>                                                                    </td>
                                                                  </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                          </c:forEach>

                                                        </c:if>
                                                        <tr>
                                                          <td>
                                                            <c:if test="${!empty aimEditActivityForm.secondarySetting.defaultHierarchy&&(aimEditActivityForm.secondarySetting.allowMultiple||empty aimEditActivityForm.secondaryPrograms)}">
                                                            <field:display name="Add Programs Button - Secondary Programs" feature="Program">
                                                            <html:button styleClass="buton" property="submitButton" onclick="addProgram(3);">
															<digi:trn key="btn:addPrograms">Add Programs</digi:trn>
														</html:button>
                                                            </field:display>
                                                            </c:if>
                                                            <c:if test="${!empty aimEditActivityForm.secondaryPrograms}">
                                                            <field:display name="Remove Program Button - Secondary Programs" feature="Program">
                                                              <html:button styleClass="buton" property="submitButton" onclick="remProgram(3);">
															<digi:trn key="btn:removeProgram">Remove program</digi:trn>
														</html:button>
                                                              </field:display>
                                                            </c:if>
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


										<field:display name="NPD Program Description" feature="Program">
                                      <tr>
                                        <td>
                                          <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                                          Description
</a>                                    </td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                                          <html:textarea property="programDescription" rows="3" cols="75" styleClass="inp-text"/>
</a>
                                        </td>
                                      </tr>
										</field:display>

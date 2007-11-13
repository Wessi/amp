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
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">
function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimPhysicalProgressForm.action = "<%=addUrl%>";
    document.aimPhysicalProgressForm.submit();
}

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimPhysicalProgressForm.action = "<%=addUrl%>~pageId=1~step=5~action=edit~surveyFlag=true~activityId=" + id;
	document.aimPhysicalProgressForm.target = "_self";
   document.aimPhysicalProgressForm.submit();
}

function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
    document.aimPhysicalProgressForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;
	document.aimPhysicalProgressForm.target = "_self";
    document.aimPhysicalProgressForm.submit();
}
function previewLogframe(id)
{
    <digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	var url ="<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~logframepr=true~activityId=" + id + "~actId=" + id;
	openURLinWindow(url,650,500);
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}


</script>
<digi:errors/>
<digi:instance property="aimPhysicalProgressForm" />
<digi:context name="digiContext" property="context"/>
<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="false">
<digi:form action="/viewPhysicalProgress.do" name="aimPhysicalProgressForm" type="org.digijava.module.aim.form.PhysicalProgressForm"
method="post">
<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
</digi:form>
</logic:equal>
<digi:form action="/viewPhysicalProgress.do" name="aimPhysicalProgressForm" type="org.digijava.module.aim.form.PhysicalProgressForm"
method="post">
<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="true">

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR><TD vAlign="top" align="center">
		<!-- contents -->
		<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4"
		class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
				<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>
					<TR bgColor=#f4f4f2><TD align=left >
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
							<TR>
								<TD align="left">
									<SPAN class=crumb>
										<jsp:useBean id="urlPhysicalProgress" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlPhysicalProgress}" property="ampActivityId">
											<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
										</c:set>
										<c:set target="${urlPhysicalProgress}" property="tabIndex" value="3"/>
										<c:set var="translation">
											<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>
										</c:set>
										<digi:link href="/viewPhysicalProgress.do" name="urlPhysicalProgress" styleClass="comment"
										title="${translation}" >
											<digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
										</digi:link>
										&gt; <digi:trn key="aim:ppOverview">Overview</digi:trn> <logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">&gt; &nbsp;
										<digi:trn key="aim:${aimPhysicalProgressForm.perspective}"><bean:write name="aimPhysicalProgressForm" property="perspective"/></digi:trn>
										<digi:trn key="aim:ppPerspective">Perspective</digi:trn> </logic:equal>
									</SPAN>
								</TD>
								<TD align=right>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Preview Activity" module="Previews">
													<field:display feature="Preview Activity" name="Preview Button">
														<input type="button" value="<digi:trn key="aim:physical:preview">Preview</digi:trn>" class="dr-menu"
															onclick="preview(<c:out value="${aimPhysicalProgressForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Edit Activity" module="Previews">
													<field:display feature="Edit Activity" name="Edit Activity Button">
														<input type="button" value="<digi:trn key="aim:physical:edit">Edit</digi:trn>" class="dr-menu"
															onclick="fnEditProject(<c:out value="${aimPhysicalProgressForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
										<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Logframe" module="Previews">
													<field:display name="Logframe Preview Button" feature="Logframe" >
														<input type="button" value='<digi:trn key="aim:previewLogframe">Preview Logframe</digi:trn>' class="dr-menu"
															onclick="previewLogframe(<c:out value="${aimPhysicalProgressForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
										</module:display>
										<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Project Fiche" module="Previews">
													<field:display name="Project Fiche Button" feature="Project Fiche" >
														<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu'
															onclick='projectFiche(<c:out value="${aimPhysicalProgressForm.ampActivityId}"/>)'>
													</field:display>
												</feature:display>
										</module:display>


								</TD>
							</TR>
						</TABLE>
					</TD>
					</TR>

					<module:display name="Components" parentModule="PROJECT MANAGEMENT">
					<feature:display name="Components" module="Components">
					<TR bgColor=#f4f4f2><TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR><TD width="100%" bgcolor="#F4F4F2" height="17">
								<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                          	<TR bgcolor="#F4F4F2" height="17">
										<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:components">Components</digi:trn>
										</TD>
	                           <TD background="module/aim/images/corner-r.gif" height=17 width=17>
										</TD>
   	                     </TR>
      	               </TABLE>
							</TD></TR>
							<TR><TD width="100%" bgcolor="#F4F4F2" align="center">
								<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2
								class="box-border-nopadding">
									<TR><TD width="100%" vAlign="top" align="left">
										<TABLE width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="left"
										bgcolor="#ffffff">
											<logic:notEmpty name="aimPhysicalProgressForm" property="components">
                    	<logic:iterate name="aimPhysicalProgressForm" property="components"
											id="component" type="org.digijava.module.aim.helper.Components">
												<TR bgcolor="#f4f4f2">
													<TD>
														<jsp:useBean id="urlPP" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlPP}" property="ampActivityId">
															<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
														</c:set>
														<c:set target="${urlPP}" property="tabIndex" value="2"/>
														<c:set target="${urlPP}" property="compId">
															<bean:write name="component" property="componentId"/>
														</c:set>
														<digi:link href="/viewPhysicalProgressDetails.do"
														name="urlPP">
														<bean:write name="component" property="title"/></digi:link><br>
														<i>Desc:</i> <bean:write name="component" property="description"/>
													</TD>
												</TR>
											</logic:iterate>
											</logic:notEmpty>
										</TABLE>
									</TD></TR>
								</TABLE>
							</TD></TR>
						</TABLE>
					</TD>
					</TR>
					</feature:display>
					</module:display>
				</TABLE></TD></TR>
				<module:display name="Issues" parentModule="PROJECT MANAGEMENT">
				<feature:display name="Issues" module="Issues">
				<TR><TD bgcolor="#F4F4F2" vAlign="bottom" align="center" width="100%">
					<!-- issues -->
					<TABLE width="96.5%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
						<TR><TD width="100%" bgcolor="#F4F4F2" height="17">
							<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                     	<TR bgcolor="#F4F4F2" height="17">
                          	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
										<digi:trn key="aim:issues">Issues</digi:trn>
									</TD>
	                        <TD background="module/aim/images/corner-r.gif" height=17 width=17>
									</TD>
   	                  </TR>
      	            </TABLE>
						</TD></TR>
						<TR><TD width="100%" bgcolor="#F4F4F2" align="center" class="box-border">
							<TABLE width="100%" cellPadding="0" cellSpacing="1" vAlign="top" align="center" bgColor=#dddddd>
	   	            	<logic:empty name="aimPhysicalProgressForm" property="issues">
									<TR bgcolor="#f4f4f2"><TD align="center"><font color="red"><digi:trn key="aim:noIssues">No issues
									</digi:trn></font></TD></TR>
								</logic:empty>
								<logic:notEmpty name="aimPhysicalProgressForm" property="issues">
									<logic:iterate name="aimPhysicalProgressForm" property="issues" id="issue"
									type="org.digijava.module.aim.helper.Issues">
										<TR bgcolor="#f4f4f2"><TD width="100%" bgcolor="#F4F4F2" align="center">
											<TABLE width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="center"
											bgColor=#f4f4f2>
												<TR><TD width="100%" vAlign="top" align="left">
													<TABLE width="100%" cellPadding="2" cellSpacing="1" vAlign="top" align="left"
													bgcolor="#ffffff">
														<TR bgcolor="#dfdfdf"><TD>
															<font color="#0000ff"><digi:trn key="aim:issue">Issue:</digi:trn> </font><bean:write name="issue" property="name"/>
														</TD></TR>
														<logic:empty name="issue" property="measures">
															<TR><TD align="center">
																<font color="red">
																	<digi:trn key="aim:noMeasures">No measures</digi:trn>
																</font>
															</TD></TR>
														</logic:empty>
														<logic:notEmpty name="issue" property="measures">
															<logic:iterate name="issue" property="measures" id="measure"
															type="org.digijava.module.aim.helper.Measures">
																<TR><TD>
																	<TABLE width="95%" cellPadding="2" cellSpacing="1" vAlign="top"
																	align="center" bgcolor="#dddddd">
																		<TR bgcolor="#f6f6f6"><TD>
																			<font color="#0000ff"><digi:trn key="aim:measure">Measure</digi:trn>: </font>
																			<bean:write name="measure" property="name"/>
																		</TD></TR>
																		<logic:empty name="measure" property="actors">
																			<TR bgcolor="#ffffff"><TD align="center">
																			<font color="red">
																				<digi:trn key="aim:noActors">No actors</digi:trn>
																			</font>
																			</TD></TR>
																		</logic:empty>
																		<logic:notEmpty name="measure" property="actors">
																			<TR bgcolor="#ffffff"><TD>
																				<TABLE width="100%" cellPadding="2" cellSpacing="1"
																				vAlign="top" align="center" bgcolor="#ffffff">
																					<logic:iterate name="measure" property="actors" id="actor"
																					type="org.digijava.module.aim.dbentity.AmpActor">
																						<TR bgcolor="#ffffff"><TD>
														</TD></TR>									<font color="#0000ff"><digi:trn key="aim:actor">Actor</digi:trn>: </font>
																							<bean:write name="actor" property="name"/>
																						</TD></TR>
																					</logic:iterate>
																				</TABLE>
																			</TD></TR>
																		</logic:notEmpty>
																	</TABLE>
																</TD></TR>
															</logic:iterate>
														</logic:notEmpty>
													</TABLE>
												</TD></TR>
											</TABLE>
										</TD></TR>
									</logic:iterate>
								</logic:notEmpty>
							</TABLE>
						</TD></TR>
					</TABLE>
					<TR><TD bgcolor="#F4F4F2">&nbsp;</TD></TR>
				</TD></TR></feature:display></module:display>
			</TABLE>
		<!-- end -->
	</TD></TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>

</logic:equal>
</digi:form>




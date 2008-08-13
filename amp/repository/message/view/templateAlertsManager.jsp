<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 10px;
	width: 300px;
}

input.file {
	width: 300px;
	margin: 0;
}

input.file.hidden {
	position: relative;
	text-align: right;		
	width: 300px;
	opacity: 0;
	z-index: 2;
	height:10px;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}

div.fakefile input {
	margin-bottom: 2px;
	margin-left: 0;
	width: 217px;
	height:20px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
-->
</style>


<!-- Yahoo Panel --> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>"></script>


<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Delete this workspace?");
		return flag;
	}	
	
</script>
<DIV id="TipLayer"	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
	
<digi:instance property="messageForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:AmpAdminHome">
								Admin Home
							</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="message:templatesManager">Templates Manager</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
                        <span class=subtitle-blue>
                        	<digi:trn key="message:templatesManager">Templates Manager</digi:trn>
                        </span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">

									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="message:templates">Templates	</digi:trn>
											<!-- end table title -->
										</td></tr>										
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
													<logic:empty name="messageForm" property="templates">
														<tr bgcolor="#ffffff">
															<td colspan="5" align="center"><b>
																<digi:trn key="message:noTemplates">No templates present</digi:trn>
															</b></td>
														</tr>
													</logic:empty>
													<logic:notEmpty name="messageForm" property="templates">
														<logic:iterate name="messageForm" property="templates" id="template">
														<tr>
															<td bgcolor="#ffffff">
																${template.name}
															</td>															
															<td bgcolor="#ffffff" width="65" align="center">
																<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams1}" property="templateId">
																	<bean:write name="template" property="id" />
																</c:set>
																<c:set var="translation">
																	<digi:trn key="aim:clickToEditTemplate">Click here to Edit Template</digi:trn>
																</c:set>
																[ <digi:link href="/templatesManager.do?actionType=addOrEditTemplate&event=edit" name="urlParams1" title="${translation}" >
																	<digi:trn key="messgae:templateEditLink">Edit</digi:trn>
																</digi:link> ]
															</td>
															<td bgcolor="#ffffff" width="75" align="center">
																<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams2}" property="templateId">
																	<bean:write name="template" property="id" />
																</c:set>																
																<c:set var="translation">
																	<digi:trn key="message:deleteTemplate">Click here to Delete Template</digi:trn>
																</c:set>
																[ <digi:link href="/templatesManager.do?actionType=deleteTemplate" name="urlParams2" title="${translation}" onclick="return onDelete()">
																	<digi:trn key="aim:workspaceManagerDeleteLink">	Delete</digi:trn>
																</digi:link> ]
															</td>															
														</tr>
														</logic:iterate>
	
													</logic:notEmpty>
													<!-- end page logic -->
											</table>
										</td></tr>
									</table>

								</td>
							</tr>
						</table>
					</td>

					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddTemplate">Click here to Add New Template</digi:trn>
												</c:set>
												<digi:link href="/templatesManager.do?actionType=addOrEditTemplate&event=add" title="${translation}" >
													<digi:trn key="aim:addTemplate">
														Add Template
													</digi:trn>
												</digi:link>
											</td>
										</tr>										
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="message:clickToGoToExportImport">Click here to goto Templates Export/Import Manager</digi:trn>
												</c:set>
												<digi:link href="/exportImportTemplates.do?actionType=gotoExportImportPage" title="${translation}" >
													<digi:trn key="aim:exportImport">Templates Export/Import Manager</digi:trn>
												</digi:link>
											</td>
										</tr>																				
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" module="aim">
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
				<tr><td>
				<TABLE width="600px">
					<TR>
						<TD COLSPAN="2"><strong><digi:trn
							key="message:PatternsReference">Patterns Reference</digi:trn></strong></TD>
					</TR>
					<TR>
						<TD nowrap="nowrap" bgcolor="#E9E9E9">
							<digi:trn key="message:patternName"> {name} - Stands for the name of the activity &nbsp;</digi:trn>
						<br />
						</TD>
					</TR>
					<TR>
						<TD nowrap="nowrap" bgcolor="#E9E9E9">
							<digi:trn key="message:patternCreatedBy"> {Created By} - Stands for the name of the creator of the activity &nbsp;</digi:trn>
						<br />
						</TD>
					</TR>
					<TR>
						<TD nowrap="nowrap" bgcolor="#E9E9E9">
							<digi:trn key="message:patternUrl"> {url} - This will enable you to resolve the URL related to actvity or the new user registeration &nbsp;</digi:trn>
						<br />
						</TD>
					</TR>
				</TABLE>

				</td></tr>
			</table>
		</td>
	</tr>
</table>


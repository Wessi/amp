<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
			
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>

<logic:notEmpty name="checkBoxToHide" scope="request">
	<bean:define id="checkBoxToHideLocal" value="true"></bean:define>
</logic:notEmpty> 
<logic:empty name="checkBoxToHide" scope="request">
	<bean:define id="checkBoxToHideLocal" value="false"></bean:define>
</logic:empty>

<logic:notEmpty name="tabType" scope="request">
	<bean:define id="tabTypeLocal" name="tabType"></bean:define>
</logic:notEmpty>
<logic:empty name="tabType" scope="request">
	<bean:define id="tabTypeLocal" value=""></bean:define>
</logic:empty>
<logic:notEmpty name="dynamicList" scope="request">
	<bean:define id="dynamicListLocal" name="dynamicList" toScope="page" />
</logic:notEmpty>

<% pageContext.setAttribute("currentTimeInMillis", System.currentTimeMillis()); %>

<table id="team_table" bgcolor="white" width="100%">						
						<tbody>
						<logic:iterate name="documentDataCollection" id="documentData"	type="org.digijava.module.contentrepository.helper.DocumentData" indexId="counter">
							<%--
					int index2;
					String documentName = documentData.getName();
					String iconPath = "";
					index2 = ((String) documentName).lastIndexOf(".");
					if (index2 >= 0) {
						iconPath = "module/cms/images/extensions/"
								+ ((String) documentName).substring(index2 + 1,
										((String) documentName).length())
								+ ".gif";
					}

					--%>
							<logic:equal name="documentData" property="isPublic" value="true">
								<c:set var="makePublicCommand">
									<digi:trn>Rep</digi:trn>
								</c:set>
							</logic:equal>
							<logic:equal name="documentData" property="isPublic" value="false">
								<c:set var="makePublicCommand">
									<digi:trn>Pub</digi:trn>
								</c:set>
							</logic:equal>
							<tr>
								<td>
									<c:choose>
										<c:when test="${documentData.hasAnyVersionPendingApproval==true &&  documentData.hasApproveVersionRights==true}">
											<span style="color:#00CC00;font-weigth:bold;">
												<bean:write name='documentData' property='title'/>
											</span>
										</c:when>
										<c:when test="${not empty documentData.shareWith && documentData.hasShareRights==true &&  
											documentData.needsApproval==true && documentData.hasApproveVersionRights}">
											<span style="color:red;font-weigth:bold;">
												<bean:write name='documentData' property='title'/>
											</span>
										</c:when>
										<c:otherwise>
											<bean:write name='documentData' property='title'/>
										</c:otherwise>
									</c:choose>															
									
									&nbsp;
									<logic:equal name="documentData" property="lastVersionIsShared" value="true">
										<span style="color:#00CC00;font-weigth:bold;font-size:9pt;">*</span>
									</logic:equal>
								</td>
								<td>
									<digi:img skipBody="true" src="${documentData.iconPath}" border="0" alt="${documentData.contentType}" align="absmiddle"/>
									&nbsp; 
								</td>
								<td>
									<c:set var="isUrl" scope="page" value="false" />
									<c:set var="translation">
										<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to</digi:trn>
									</c:set>
									<c:if test="${documentData.webLink != null}" >
										<a onmouseover="Tip('${translation} ${documentData.webLink}')" onmouseout="UnTip()" 
												 onclick="window.open('${documentData.webLink}')"
											style="cursor:pointer; color: blue; font-size: 11px">  
									</c:if>
										 <bean:write name="documentData" property="name" />
									<c:if test="${documentData.webLink != null}" >
										<c:set var="isUrl" scope="page" value="true" />
										</a>
									</c:if>
								</td>
								<td>
									<bean:write name="documentData" property="calendar" />
								</td>
								
									<td>
									<bean:write name="documentData" property="yearofPublication" />
								</td>
								<td>
									<bean:write name="documentData" property="fileSize" />
								</td>
								<td>
									${documentData.cmDocType }
									<a  id="aflag${documentData.uuid}" style="display: none">&nbsp;</a>
								</td>
								<td>
									<c:set var="labelUUIDs" value="[" />
									<logic:notEmpty name="documentData" property="labels">
										<logic:iterate id="label" name="documentData" property="labels">
											<c:set var="labelUUIDs">${labelUUIDs}'${label.uuid}',</c:set>
											<span style="white-space: nowrap;">
												<span style="background-color: ${label.backgroundColor}; color: ${label.color};">
													${label.name}
												</span>
												<logic:equal name="documentData" property="hasVersioningRights" value="true">
													<strong>|</strong><span onclick="labelCallbackObj.dynamicList=${dynamicListLocal};labelCallbackObj.remove('${documentData.uuid}','${label.uuid}');" 
													onmouseout="switchColors(this);" onmouseover="switchColors(this);" 
													style="background-color: ${label.backgroundColor}; color: ${label.color};cursor: pointer">X</span>
												</logic:equal>
													&nbsp;&nbsp;
											</span>
										</logic:iterate>
									</logic:notEmpty>
									<c:set var="labelUUIDs">${labelUUIDs}'']</c:set>
									<%-- ><bean:write name='documentData' property='description'/>--%
									<%-- <script type="text/javascript">
										document.write(unescape("<bean:write name='documentData' property='description'/>"));
									</script> --%>
									<a name="aDocumentUUID" style="display: none"><bean:write name="documentData" property="uuid" /></a>
								</td>
								<td nowrap="nowrap">
									 		<c:set var="translation">
												<digi:trn>Click here to see possible actions</digi:trn>
											</c:set> 
											<a style="cursor:pointer; text-decoration:none; color: blue" id="Actions<bean:write name='documentData' property='uuid' />"
											onClick="showActions('Actions${documentData.uuid}', 'ActionsDiv${documentData.uuid}', ${currentTimeInMillis})" title="${translation}">
												<digi:trn>Show Actions</digi:trn>
											</a>
									<div id="ActionsDiv${documentData.uuid}" style="display:none; border:1px solid lightgray; background-color:white; padding: 3px;" 
										>
									<c:choose>
										<c:when test="${documentData.webLink == null}">
											<c:set var="translation">
												<digi:trn>Click here to download document</digi:trn>
											</c:set> 
											<a style="cursor:pointer; text-decoration:none; color: blue" id="D<bean:write name='documentData' property='uuid' />"
											onClick="downloadFile('<bean:write name='documentData' property='nodeVersionUUID' />');"title="${translation}">
												<digi:trn>Download</digi:trn>
											</a>
										</c:when>
										<c:otherwise>
		
											<a style="cursor:pointer; text-decoration:none; color: blue" id="D<bean:write name='documentData' property='uuid' />"
											onclick="window.open('${documentData.webLink }')" onmouseout="UnTip()"	onmouseover="Tip('<digi:trn>Follow link to </digi:trn> ${documentData.webLink}')">
												<digi:trn>Follow the Link</digi:trn>
											</a>
										</c:otherwise>
									</c:choose>
									
	
	
									<logic:equal name="documentData" property="hasVersioningRights" value="true">
										<br />
										<a style="cursor:pointer; text-decoration:none; color: blue" id="plus<bean:write name='documentData' property='uuid' />"
										onClick="setType('version'); configPanel(0,'${documentData.escapedAmpTitle}','${documentData.escapedAmpDescription}', <%=documentData.getCmDocTypeId() %> ,'<%=documentData.getUuid() %>', ${isUrl},'<%=documentData.getYearofPublication() %>' );showMyPanel(0, 'addDocumentDiv');"
										title="<digi:trn>Click here to add a new version of this document</digi:trn>">
											<digi:trn>Add Version</digi:trn>
									</logic:equal>
									
									<c:set var="translationForWindowTitle">
										<digi:trn>Version History</digi:trn>
									</c:set> 
									<logic:equal name="documentData" property="hasShowVersionsRights" value="true">
										<br />
										<a style="cursor:pointer; text-decoration:none; color: blue" id="H<bean:write name='documentData' property='uuid' />"
										onClick="showMyPanelCopy(1,'viewVersions'); requestVersions('<%=documentData.getUuid() %>'); setPanelHeader(1, '${translationForWindowTitle}');"
										title="<digi:trn>Click here to see a list of versions for this document</digi:trn>">
											<digi:trn>Versions</digi:trn>
										</a>
									</logic:equal>
	
									<logic:equal name="documentData" property="hasMakePublicRights" value="true">
										<c:if test="${ (!documentData.isPublic) || (!documentData.lastVersionIsPublic) }">
											<br />
											<a style="cursor:pointer; text-decoration:none; color: blue" id="Pub<bean:write name='documentData' property='uuid' />"
											onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.MAKE_PUBLIC %>' ,'<%=documentData.getUuid() %>', true);"
											title="<digi:trn>Click here to make this document public</digi:trn>">
												<digi:trn>Make Public</digi:trn>
											</a>
										</c:if>
									</logic:equal>
									
									
									<logic:equal name="documentData" property="isPublic" value="true">
										<logic:equal name="documentData" property="hasDeleteRightsOnPublicVersion" value="true">
											<br />
											<a style="cursor:pointer; text-decoration:none; color: blue" id="Priv<bean:write name='documentData' property='uuid' />"
											onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.UNPUBLISH %>', '<%=documentData.getUuid() %>');"
											title="<digi:trn>Click here to unpublish this document</digi:trn>">
												<digi:trn>Unpublish</digi:trn>
											</a>
										</logic:equal>								
									</logic:equal>
									
									<logic:notEmpty name="documentData" property="shareWith">
										<logic:equal name="documentData" property="hasShareRights" value="true">										
											
											<logic:equal name="documentData" property="needsApproval" value="false">
												<logic:equal  name="documentData" property="lastVersionIsShared" value="false">
													<br />
													<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue"
													onClick="shareDoc('<%=documentData.getUuid() %>','<%=documentData.getShareWith() %>','${tabTypeLocal}');" title="<digi:trn>Click here to Share this document</digi:trn>">
														<digi:trn>Share</digi:trn>
													</a>
												</logic:equal>
											</logic:equal>										
											<logic:equal name="documentData" property="needsApproval" value="true">
												<logic:equal name="documentData" property="hasApproveVersionRights" value="true">
													<br />
													<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue;"
													onClick="shareDoc('<%=documentData.getUuid() %>','<%=documentData.getShareWith() %>','${tabTypeLocal}');" title="<digi:trn>Click here to Share this document</digi:trn>">
													<digi:trn>Approve</digi:trn> </a>
													
													<span style="color: blue"><strong>|</strong></span>
													<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue"
													onClick="rejectDoc('<%=documentData.getUuid() %>','rejectShare');" title="<digi:trn>Click here to Reject this document</digi:trn>">
														<digi:trn>Reject</digi:trn> 
													</a>
												</logic:equal>
											</logic:equal>
											
										</logic:equal>
									</logic:notEmpty>
									
									<logic:equal name="documentData" property="hasUnshareRights" value="true">
										<logic:equal name="documentData" property="isShared" value="true">
											<br />
											<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue;" onClick="unshareDoc('<%=documentData.getUuid() %>','${tabTypeLocal}');" 
											title="<digi:trn>Click here to UnShare this document</digi:trn>">
												<digi:trn>UnShare</digi:trn>
											</a>
										</logic:equal>									
									</logic:equal>
									
	
									<logic:equal name="documentData" property="hasDeleteRights" value="true">
										<br />
										<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue;"onClick="checkDocumentUuid('<%=documentData.getUuid() %>');deleteRow('<%=documentData.getUuid() %>');"
										title="<digi:trn>Click here to delete this document</digi:trn>"><digi:trn>Delete</digi:trn></a>
									</logic:equal>
								
									<br /> 
									<a style="cursor:pointer; text-decoration:none; color: blue;" onClick="showOrgsPanel('<%=documentData.getUuid() %>');" title="<digi:trn>Show Participating Organizations</digi:trn>">
										<digi:trn>Organizations</digi:trn> 
									</a>
									
									<logic:equal name="documentData" property="hasVersioningRights" value="true">
										<br />
										<a style="cursor:pointer; text-decoration:none; color: blue" id="addLabelLink_${documentData.uuid}"
										onClick="labelCallbackObj.dynamicList=${dynamicListLocal};labelCallbackObj.docUUID='${documentData.uuid}';fAddPanel.toggleViewWithSel(${labelUUIDs});fAddPanel.reposition('addLabelLink_${documentData.uuid}');" 
										title="<digi:trn>Click here to add new labels to this document</digi:trn>">
											<digi:trn>Labels</digi:trn>
									</logic:equal>
									</div>									
								</td>
							</tr>
						</logic:iterate>
						</tbody>
					</table>

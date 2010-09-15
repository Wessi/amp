<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="java.util.List"%>
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<digi:context name="displayThumbnail" property="context/aim/default/displayThumbnail.do" />
<style>
.contentbox_border {
	border: 	1px solid #666666;
	width: 		1050px;
	background-color: #f4f4f2;
}
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}
.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;
!important padding:2px;
}
.Hovered {
	background-color:#a5bcf2;
}
</style>

<style>
.layoutTable
{
	border: 1px dotted black !important;
}
.layoutTable TD
{
	border: 1px dotted green !important;
}
</style>

<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0;
 filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
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
	margin-bottom: 5px;
	margin-left: 0;
	width: 175px;
}
div.fakefile2 {
	position: absolute;
	top: 2px;
	left: 178px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input {
	width: 83px;
}
-->
</style>
<digi:instance property="contentForm" />
<digi:form action="/contentManager.do" method="post" enctype="multipart/form-data">
<table bgColor=#ffffff cellPadding=5 cellSpacing=1 width=1000>
  <tr>
    <td width=14>&nbsp;</td>
    <td align=left vAlign=top width=1000><table cellPadding=5 cellSpacing=0 width="100%">
        <tr>
          <!-- Start Navigation -->
          <td height=33><span class=crumb>
            <c:set	var="translation">
              <digi:trn>Click here to goto Admin Home</digi:trn>
            </c:set>
            <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}">
              <digi:trn> Admin Home </digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <c:set var="translation">
              <digi:trn>Click here to go to the content manager</digi:trn>
            </c:set>
            <digi:link href="/contentManager.do" styleClass="comment"	title="${translation}">
              <digi:trn>List of Public View Content</digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <digi:trn>Edit content</digi:trn>
            </span> </td>
          <!-- End navigation -->
        </tr>
        <tr>
          <td colspan="2"><span class=subtitle-blue>
            <digi:trn>Add/Edit content</digi:trn>
            </span> </td>
        </tr>
        <tr>
          <td noWrap vAlign="top">
          <table class="contentbox_border" width="100%" border="0" bgcolor="#f4f4f2">
              <tr>
                <td align="center"><table width="100%">
                    <tr>
                      <td style="background-color: #CCDBFF;height: 18px;"><strong>Information</strong> </td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#f4f4f2" align="center"><table border=0 cellPadding=0 cellSpacing=0 width=772>
                    <tr>
                      <td width=14>&nbsp;</td>
                      <td align=left vAlign=top width=520>
                        <table border=0 cellPadding=5 cellSpacing=0 width="100%">
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=left class=title noWrap colspan="2"><!-- digi:errors /-->
                              <%--                              <logic:notEmpty name="contentForm" property="errors"> <font color="red">
                                <ul>
                                  <logic:iterate id="element" name="contentForm" property="errors">
                                    <li>
                                      <digi:trn key="${element.key}">
                                        <bean:write name="element" property="value" />
                                      </digi:trn>
                                    </li>
                                  </logic:iterate>
                                </ul>
                                </font> </logic:notEmpty>--%>
                            </td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=left class=title noWrap colspan="2"><digi:trn key="um:allMarkedRequiredField"> All fields marked with an <FONT color=red><B><BIG>*</BIG> </B></FONT> are required. </digi:trn>
                            </td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=left>
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Title</digi:trn>
                            </td>
                            <td align=left>
	                            <html:text property="title"></html:text>
                            </td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=left>
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Page Code</digi:trn>
                            </td>
                            <td align=left>
	                            <html:text property="pageCode"></html:text>
                            </td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=left>
    	                        <digi:trn>Description</digi:trn>
                            </td>
                            <td align=left>
	                            <html:text property="description"></html:text>
                            </td>
                          </tr>
                        </table>
                        
                        </td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td align="center"><table width="100%">
                    <tr>
                      <td style="background-color: #CCDBFF;height: 18px;"><strong><digi:trn>Layout</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td align="center"><table width="100%">
                    <tr>
                      <td align="center">
                          <table cellpadding="5" cellspacing="5">
                            <tr>
                              <td align="center">
                              <img src="/repository/content/view/layout_1.png"/><br />
                              <html:radio name="contentForm" property="layout" value="1" disabled="false" />
                              <br />
                              </td>
                              <td align="center">
                              <img src="/repository/content/view/layout_2.png" /><br />
                              <html:radio property="layout" value="2" disabled="false" />
                              <br />
                              </td>
                              <td align="center">
                              <img src="/repository/content/view/layout_3.png" /><br />
                              <html:radio property="layout" value="3" disabled="false" />
                              <br />
                              </td>
                            </tr>
                          </table>
					</td>                      
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td align="center"><table width="100%">
                    <tr>
                      <td style="background-color: #CCDBFF;height: 18px;"><strong><digi:trn>Content</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#f4f4f2" align="center">
                </td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#f4f4f2" align="left" style="border:1px dotted black;">
                	<c:forEach var="layoutNumber" begin="1" end="3">
                    <c:set var="displayLayout">
                        <c:choose>
                            <c:when test="${contentForm.layout eq layoutNumber}">
                            display:block
                            </c:when>
                            <c:otherwise>
                            display:none;
                            </c:otherwise>
                        </c:choose>
                    </c:set>
                    <div style="width:950px;padding:10px;background-color:#ffffff;${displayLayout}" id="layout_${layoutNumber}" name="layoutGroup">
                    <c:import url="/repository/content/view/layout_${layoutNumber}.jsp">
					  <c:param name="pageCode" value="${contentForm.pageCode}"/>
                      <c:param name="htmlblock_1" value="${contentForm.htmlblock_1}"/>
                      <c:param name="htmlblock_2" value="${contentForm.htmlblock_2}"/>
                    </c:import>
                    </div>
                    </c:forEach>
                </td>
              </tr>
              <tr>
                <td align="center"><table width="100%">
                    <tr>
                      <td style="background-color: #CCDBFF;height: 18px;"><strong><digi:trn>Thumbnails</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td valign="top" align="center">
					<table cellpadding="3" cellspacing="3" border="0" width="75%"  bgcolor="#FFFFFF" id="dataTable">
						<tr id="tr_path_thumbnail">
                            <td bgcolor="#CECECE" width="20%"><digi:trn>Thumbnail</digi:trn></td>
                            <td bgcolor="#CECECE" width="40%"><digi:trn>Label</digi:trn></td>
                            <td bgcolor="#CECECE" width="20%"><digi:trn>Related File</digi:trn></td>
                            <td bgcolor="#CECECE" width="20%"><digi:trn>Action</digi:trn></td>
                        </tr>
                    <c:forEach  var="content" items="${contentForm.sortedContentThumbnails}" varStatus="loop">
						<tr id="tr_path_thumbnail">
                        <td><img src="/content/displayThumbnail.do?index=${loop.index}&pageCode=${contentForm.pageCode}" align="middle" width="20" style="border:1px solid #cecece">
                        </td>
                        <td>${content.thumbnailLabel}</td>
                        <td>${content.optionalFileName}</td>
                        <td>
                        	<c:if test="${loop.index != 0}">
                          	<a onclick="doAction(${loop.index}, 'moveup', false)">
                              <img src="/TEMPLATE/ampTemplate/images/arrow_up.gif" border="0" title="<digi:trn>Move up</digi:trn>"/>
                            </a>
                            </c:if>
                        	<c:if test="${loop.index == 0}">
                            	&nbsp;&nbsp;&nbsp;
                            </c:if>
                        	<c:if test="${loop.index != fn:length(contentForm.sortedContentThumbnails)-1}">
                          	<a onclick="doAction(${loop.index}, 'movedown', false)">
                              <img src="/TEMPLATE/ampTemplate/images/arrow_down.gif" border="0" title="<digi:trn>Move down</digi:trn>"/>
                            </a>
                            </c:if>
                        	<c:if test="${loop.index == fn:length(contentForm.sortedContentThumbnails)-1}">
                            	&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                          	<a onclick="doAction(${loop.index}, 'deleteThumb', true">
                              <img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" title="<digi:trn>Delete thumbnail</digi:trn>"/>
                            </a>
                        </td>
                        </tr>
                    </c:forEach>
                    </table>
                    <br />
                    <br />
					<table cellpadding="3" cellspacing="3" style="border:1px solid black;">
						<tr id="tr_path_thumbnail">
						<td><strong><digi:trn>Select Thumbnail to upload:</digi:trn><font color="red">*</font></strong></td>
						<td>
                            <html:file property="tempContentThumbnail"/>
						</td>
						</tr>
						<tr id="tr_path_optional">
						<td><strong><digi:trn>Select Optional File to upload:</digi:trn><font color="red"></font></strong></td>
						<td>
                            <html:file property="tempContentFile"/>
						</td>
						</tr>
						<tr>
							<td> 
								<strong><digi:trn>Thumbnail Label:</digi:trn><font color="red"></font></strong>
							</td>
							<td> 
                            	<html:text property="tempContentThumbnailLabel"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center"> 
	                        <input type="button" styleClass="dr-menu buton" onclick="upload()" value="<digi:trn>Upload File</digi:trn>"/>&nbsp;
							</td>
						</tr>
						</tr>
					</table>
				<br />
				<br />
                </td>
              </tr>

            </table></td>
        </tr>
      </table>
		<div align="center">
		<html:submit>Save</html:submit>
		</div>
      </td>
  </tr>
</table>
<br />
<br />
<html:hidden property="htmlblock_1"></html:hidden>
<html:hidden property="htmlblock_2"></html:hidden>
<html:hidden property="editKey"></html:hidden>
<html:hidden property="action" value="save"/>


<script language="javascript">
$(document).ready( function() {


	$('input:radio[name=layout]').each( function(){
		$(this).click(function () {
			//When clicking the radio buttons hide every div with layout
			$('div[name=layoutGroup]').hide();
			//Show the selected one
			$('#layout_'+$(this).val()).show();
			
		});
	});

	//Search all editor links and adapt them
	$('div[name=layoutGroup]').find("A:contains('Edit HTML')").each( function(){
		$(this).text("<digi:trn jsFriendly='true'>Edit</digi:trn>");
		//Extract editKey
		var href = $(this).attr("href");
		href = href.substring(href.indexOf("?id=")+4, href.length)
		editKey = href.substring(0, href.indexOf("&"));
		$(this).attr("href", "javascript:edit('" + editKey + "');");
	});

});

function edit(key) {
	document.contentForm.action = "/content/contentManager.do?action=add";
	document.contentForm.target = "_self"
	document.contentForm.editKey.value = key;
	document.contentForm.submit();
}

function upload() {
	document.contentForm.action = "/content/contentManager.do?action=upload";
	document.contentForm.target = "_self"
	document.contentForm.submit();
}
function doAction(index, action, confirmation) {
	if(confirmation){
		var ret = confirm("<digi:trn jsFriendly='true'>Are you sure?</digi:trn>");
		if (!ret) return false; 
	}
	document.contentForm.action = "/content/contentManager.do?action=" + action +"&index=" + index;
	document.contentForm.target = "_self"
	document.contentForm.submit();
}

function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}

$(document).ready(function(){
	try
	{
		setStripsTable("dataTable", "tableEven", "tableOdd");
		setHoveredTable("dataTable", true);
	}
	catch(e) {}
});
</script>
</digi:form>

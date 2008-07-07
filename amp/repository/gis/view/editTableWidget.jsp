<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">
<!--
	var reallyDeleteColumn = 'Do you relally want to remove column? this will remove data in this column.';

	function addColumn(id){
		var myForm = document.getElementById('tableId').form;
		refreshThis(myForm,id);
		openURLinWindow('${contextPath}/gis/adminTableWidgets.do?actType=showColumnPopup');
	}
	function cancelEdit(){
		var myForm = document.getElementById('tableId').form;
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=cancelEdit" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
	function refreshThis(myForm,id){
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=redraw" />
		myForm.action="<%=justSubmit%>&id="+id;  
		myForm.submit();
	}
	function deleteCol(colId){
		var myForm = document.getElementById('tableId').form;
		if ( confirm(reallyDeleteColumn) ){
			<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=removeColumn" />
			myForm.action="<%=justSubmit%>&colId="+colId;  
			myForm.submit();
		}
	
	}
	function moveUp(colId){
		var myForm = document.getElementById('tableId').form;
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=reorderUp" />
		myForm.action="<%=justSubmit%>&colId="+colId;  
		myForm.submit();
	}
	function moveDown(colId){
		var myForm = document.getElementById('tableId').form;
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=reorderDown" />
		myForm.action="<%=justSubmit%>&colId="+colId;  
		myForm.submit();
	}
	function nameAsTitleSettingChanged(){
		var chk=document.getElementsByName('nameAsTitle')[0];
		chk.value=chk.checked; 
		alert('nameAsTitle='+chk.value);
	}
	$(document).ready(function(){
		var mainTextBox = document.getElementsByName('name')[0];
		mainTextBox.focus();
	});
	
//-->
</script>



<digi:instance id="wform" property="gisTableWidgetCreationForm"/>
<digi:form action="/adminTableWidgets.do?actType=save">

<table id="widgetOuter" border="0" cellpadding="15">
	<tr>
		<td colspan="2">
			<span class=crumb>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
              <digi:link href="/adminTableWidgets.do" styleClass="comment">
                <digi:trn key="admin:Navigation:WidgetList">Table Widgets</digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn key="admin:Navigation:createEditWidget">table widget form</digi:trn>		
			</span>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<span class="subtitle-blue"><digi:trn key="gis:editTableWidget:pageTitle">Edit table widget</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>

			<table id="tableNames" border="0" cellpadding="5" align="center" style="font-family:verdana;font-size:11px; border:1px solid silver;">
				<tr>
					<td align="right" nowrap="nowrap"><font color="red">*</font><strong><digi:trn key="gis:editTableWidget:nameTitle">Name:</digi:trn></strong></td>
					<td><html:text name="wform" property="name" style="width : 200px"/></td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:nameAsTitleTitle">Show name as widget title:</digi:trn></strong></td>
					<td><html:checkbox name="wform" property="nameAsTitle"/></td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:codeTitle">Code:</digi:trn></strong></td>
					<td><html:text name="wform" property="code" style="width : 200px"/></td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:cssClassTitle">CSS class:</digi:trn></strong></td>
					<td><html:text name="wform" property="cssClass" style="width : 200px"/></td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:styleTitle">Style:</digi:trn></strong></td>
					<td><html:text name="wform" property="htmlStyle" style="width : 200px"/></td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:widthTitle">Width:</digi:trn></strong></td>
					<td><html:text name="wform" property="width" style="width : 200px"/></td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:placeTitle">Place:</digi:trn></strong></td>
					<td>
						<html:select name="wform" property="selPlaces" multiple="true" size="7" style="width : 200px">
							<html:optionsCollection name="wform" property="places" label="label" value="value"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right">
						<c:set var="cancelButton"><digi:trn key="gis:cancelButton">Cancel</digi:trn></c:set>
						<input type="button" value="${cancelButton}" title="Cancel and return to list" onclick="cancelEdit()">
					</td>
					<td>
						<c:set var="saveButton"><digi:trn key="gis:saveButton">Save</digi:trn></c:set>
						<html:submit title="Save table widget" value="${saveButton}" />
					</td>
				</tr>
			</table>

		</td>
		<td width="100%" valign="top">
		
			<table id="columns_list" width="50%"  align="center" style="font-family:verdana;font-size:11px;border:1px solid silver;">
				<tr bgColor="#d7eafd">
					<td><strong><digi:trn key="gis:editTableWidget:colName">Column Name</digi:trn></strong></td>
					<td><strong><digi:trn key="gis:editTableWidget:colCode">Code</digi:trn></strong></td>
					<td><strong><digi:trn key="gis:editTableWidget:colCssClass">CSS class</digi:trn></strong></td>
					<td><strong><digi:trn key="gis:editTableWidget:colPate">Pattern</digi:trn></strong></td>
					<td colspan="3"><strong><digi:trn key="gis:editTableWidget:colOps">Operations</digi:trn></strong></td>
				</tr>
				<c:forEach var="column" items="${wform.columns}" varStatus="varStat">
					<tr>
						<td>
							${column.name}
						</td>
						<td>
							${column.code}
						</td>
						<td>
							${column.cssClass}
						</td>
						<td>
							${column.pattern}
						</td>
						<td nowrap="nowrap">
							<a href="javascript:deleteCol(${column.id})"><digi:trn key="gis:removeLink">Remove</digi:trn></a>
						</td>
						<td>
							<c:if test="${varStat.first != true}">
								<a href="javascript:moveUp(${column.id})"><digi:trn key="gis:upLink">Up</digi:trn></a>
							</c:if>
						</td>
						<td>
							<c:if test="${varStat.last != true}">
								<a href="javascript:moveDown(${column.id})"><digi:trn key="gis:downLink">Down</digi:trn></a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
			<br>
			<c:set var="addColumnButton"><digi:trn key="gis:addColumnButton">Add Column</digi:trn></c:set>
			<c:if test="${not empty wform.id}">
				<input type="button" onclick="addColumn(${wform.id})" value="${addColumnButton}" title="Submit">
			</c:if>			
			<c:if test="${empty wform.id}">
				<input type="button" onclick="addColumn(null)" value="${addColumnButton}" title="Submit">
			</c:if>			
		</td>
	</tr>
</table>
<html:hidden styleId="tableId" property="id"/>

</digi:form>
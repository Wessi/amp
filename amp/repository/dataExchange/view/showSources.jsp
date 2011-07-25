<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="manageSourceForm" />

<script language="JavaScript">
function deleteSource(sourceId) {
	 var form = document.getElementById('manageForm');
	form.action = "/dataExchange/manageSource.do?action=delete&selectedSourceId="+sourceId;
	form.target="_self"
	form.submit();
}

function editSource(sourceId) {
	 var form = document.getElementById('manageForm');
	form.action = "/dataExchange/editSource.do?action=gotoEditPage&sourceId="+sourceId;
	form.target="_self"
	form.submit();
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$('#source_'+group_id).toggle('fast');
}

</script>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<digi:form action="/manageSource.do" styleId="manageForm">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
		<!-- BREADCRUMP START -->
		<tr>
			<td height="33">
				<div class="breadcrump_cont"> 
					<span class="sec_name"><digi:trn>Partial Data Import Manager</digi:trn></span>
					<span class="breadcrump_sep">|</span> <a href="/admin.do" class="l_sm"><digi:trn>Admin Home</digi:trn></a>				
					<span class="breadcrump_sep"><b>»</b></span>
					<span class="bread_sel"><digi:trn>Partial Data Import Manager</digi:trn></span>
				</div>
				<br>
			</td>
		</tr>
		<!-- BREADCRUMP END -->
	  <tr>
	    <td class="main_side_1">
		<table width="980" border="0" cellspacing="0" cellpadding="0" style="margin:10px;">
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td align=right><a href="/dataExchange/createSource.do?htmlView=true" class="t_sm"><b>[+] Create New Source</b></a></td>
	  </tr>
	</table>
	<table class="inside" width=980 cellpadding="0" cellspacing="0" style="margin:10px;">
		<tr>
		<td colspan="5" align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside><b class="ins_header">List of sources</b></td>
		</tr>
		<tr>
		    <td width="300" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<b class="ins_title">Name</b>
		    </td>
		    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<b class="ins_title">Source</b>
		    </td>
		    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<b class="ins_title">Workspace Used</b>
		    </td>
		    <td width="100" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<b class="ins_title">Show/Hide Details</b>
		    </td>
		    <td width="100" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center>
		    	<b class="ins_title">Actions</b>
		    </td>
		</tr>
		<logic:empty name="manageSourceForm" property="pagedSources">
			<tr>
				<td colspan="5"><digi:trn>No records found</digi:trn> </td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="manageSourceForm" property="pagedSources">
			<logic:iterate id="source" name="manageSourceForm" property="pagedSources">
				<tr>
				    <td bgcolor="#FFFFFF" class="inside">${source.name}</td>
				    <td bgcolor="#FFFFFF" class="inside">${source.source}</td>
				    <td bgcolor="#FFFFFF" class="inside">${source.importWorkspace.name }</td>
				    <td bgcolor="#FFFFFF" class="inside" align="center" nowrap="nowrap">
				    	<img src="/TEMPLATE/ampTemplate/img_2/ico_plus.gif" id="${source.id}_plus" onclick="toggleGroup('${source.id}')"/>
				    	<div id="source_${source.id}" style="display: none;height: 355px;width:500px; overflow: auto;">				    		
				    		<table border="0">
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" colspan="2" rowspan="7" class="inside">				    				
				    				<img src="/TEMPLATE/ampTemplate/img_2/ico_blue_minus.gif"  id="${source.id}_minus" style="display: none; " onclick="toggleGroup('${source.id}')"/>	
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">				    				
				    				<strong><digi:trn>Name</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				${source.name}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Source</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.source}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Strategy</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.importStrategy}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Language</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.language}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Unique Identifier</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.uniqueIdentifier}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Approval Status</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.approvalStatus}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Fields</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				<logic:notEmpty name="source" property="fields">
										<ul>
											<logic:iterate id="field" name="source" property="fields">
												<li>${field}</li>
											</logic:iterate>
										</ul>
									</logic:notEmpty>
				    			</td>
				    		</tr>
				    	</table>
				    	</div>
				    </td>
				    <td bgcolor="#FFFFFF" class="inside" align="center">
				    	<!-- 
				    		<img src="/TEMPLATE/ampTemplate/img_2/ico_edit_perm.gif"  onclick="editSource(${source.id});" style="cursor:pointer;"/> &nbsp;
				    	 -->				    	 
				    	<img src="/TEMPLATE/ampTemplate/img_2/ico_del_perm.gif" onclick="deleteSource(${source.id});" style="cursor:pointer;"/>
				    </td>
				</tr>		
			</logic:iterate>
		</logic:notEmpty>
	</table>
	<!-- MAIN CONTENT PART END -->
	<!-- Pagination -->
		<div class="paging" style="font-size:11px;margin:10px;">
			<b class="ins_title"><digi:trn>Pages :</digi:trn></b>
			<c:forEach var="page" begin="1" end="${manageSourceForm.lastPage}">
				<bean:define id="currPage" name="manageSourceForm" property="currentPage" />
				<c:if test="${manageSourceForm.currentPage == page}">
					<b class="paging_sel">${page}</b>
				</c:if>
				<c:if test="${manageSourceForm.currentPage != page}">
					<c:set var="translation">
						<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
					</c:set>
					<a href="javascript:page(${page})" title="${translation}" class="l_sm">${page}</a>
				</c:if>
				|&nbsp;
			</c:forEach>
		</div>	
		<!-- end of Pagination -->
		</td>
	  </tr>
	</table>
</digi:form>
</body>
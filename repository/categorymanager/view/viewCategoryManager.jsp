<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.List"%>
 
<%@page import="org.digijava.module.categorymanager.util.CategoryManagerUtil"%>

<style type="text/css">
		.jcol{												
		padding-left:10px;												 
		}
		.jlien{
			text-decoration:none;
		}
		.tableEven {
			background-color:#dbe5f1;
			font-size:8pt;
			padding:2px;
		}

		.tableOdd {
			background-color:#FFFFFF;
			font-size:8pt;
			padding:2px;
		}
		 
		.Hovered {
			background-color:#a5bcf2;
		}
		
		.notHovered {
			background-color:#FFFFFF;
		}
		
		
</style>
<script language="JavaScript">

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

	
	function setHoveredRow(rowId) {

		var rowElement = document.getElementById(rowId);
		if(rowElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        cells      = rowElement.getElementsByTagName('td');

			for(var i = 0, n = cells.length; i < n; ++i) {
				cells[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				cells[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			cells = null;
		}
	}
	
	

</script>

<digi:instance property="cmCategoryManagerForm" />
<bean:define id="myForm" name="cmCategoryManagerForm" toScope="page" type="org.digijava.module.categorymanager.form.CategoryManagerForm" />

<!--  AMP Admin Logo -->
<%-- <jsp:include page="teamPagesHeader.jsp" flush="true" /> --%>
<c:set var="translation">
				<digi:trn key="aim:categoryDeleteConfirm">Are you sure you want to delete the category?</digi:trn>
</c:set>
<script type="text/javascript">
function confirmDelete() {
	var ret		= confirm('${translation}');
	return ret;
}
</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="80%">
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0 >
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						
						
						<digi:trn key="aim:categoryManager">
							Category Manager  
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:categoryManager">
								Category Manager 
							</digi:trn> 
						</span>
                        <br><br>
						
					</td>
				</tr>
                 <tr><td align="left">
                        <jsp:include page="/repository/aim/view/exportTable.jsp" />
                    </td>
                </tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
			</table>
			<div align="left" >
			<c:set var="translation">
				<digi:trn key="aim:categoryManagerAddNewCategoryTitle">Click here to add a new category with specified values</digi:trn>
			</c:set>
			<digi:link href="/categoryManager.do?new=true" title="${translation}">
				<img src="/TEMPLATE/ampTemplate/imagesSource/common/green_plus.png" style="height: 22px; cursor: pointer; border:0">	
				<digi:trn key="aim:categoryManagerAddNewCategory">Add New Category</digi:trn>
				</img>
			</digi:link>
			</div>

			<br/>

			<table cellpadding="5">
			<tr>
			<td>
				<logic:notEmpty name="myForm" property="categories">
                    <div align="center"  class="report">
					<table width="100%" height="100%" border="0" align=center cellPadding=0 cellSpacing=0 >
                        <thead>
						<tr style="background-color:#999999; color:#000;">
							<td height="22" width="20%" align="center" valign="center" bgcolor="#999999">
								<digi:trn key="aim:categoryName" >
									Category Name
								</digi:trn>
							</td>
							<td bgcolor="#999999" width="20%" align="center">
								<digi:trn key="aim:categoryDescription">
									Category Description
								</digi:trn>
							
							</td>
							<td bgcolor="#999999" width="35%" align="center">
								<digi:trn key="aim:categoryPossibleValues">
									Possible Values
								</digi:trn>
							</td>
							<td bgcolor="#999999" width="15%" align="center">
								<digi:trn key="aim:categoryOptions">
									Category Options
								</digi:trn>
							</td>
							<td bgcolor="#999999" width="9%" align="center" class="ignore">
								<digi:trn key="aim:categoryActions">
									Actions
								</digi:trn>
							</td>
							<td bgcolor="#999999" align="center">
							</td>

						</tr>
                         </thead>
						</table>
						</div>
						<div style = "overflow:auto;width:100%;height:220px;max-height:220px;" class="report">
						<table width="100%" BORDER=0 cellpadding="0" cellspacing="0" id="dataTable">
                        <tbody class="yui-dt-data">
						<logic:iterate name="myForm" property="categories" id="category" type="org.digijava.module.categorymanager.dbentity.AmpCategoryClass">
						<tr align="center">
							<td  width="20%">
								<digi:trn key='<%= CategoryManagerUtil.getTranslationKeyForCategoryName( category.getKeyName() ) %>'>
									<bean:write name="category" property="name" />
								</digi:trn>
								<br />
								(
								<digi:trn key="aim:categoryKeyIs">
									category key is 
								</digi:trn>
								<i><bean:write name="category" property="keyName" /></i>
								)
							</td>
							<td  width="20%" align="left">
								<digi:trn key='<%= CategoryManagerUtil.getTranslationKeyForCategoryName( category.getDescription() ) %>'>
									<bean:write name="category" property="description" /> &nbsp;
								</digi:trn>
								&nbsp;
							</td>
							<td width="35%" align="left">
								<ul>
								<logic:iterate name="category" property="possibleValues" id="categoryValue" type="org.digijava.module.categorymanager.dbentity.AmpCategoryValue">
								<logic:notEmpty name="categoryValue">
								<% String keyForValue	= CategoryManagerUtil.getTranslationKeyForCategoryValue(categoryValue); %>
									<li>
										<digi:trn key='<%=keyForValue%>'>
											<bean:write name="categoryValue" property="value" />
										</digi:trn>
									</li>
								</logic:notEmpty>
								</logic:iterate>
								</ul>
							</td>
							<td width="15%"align="left" class="ignore">
								<% if (category.isMultiselect()) {%>
									<img src= "/TEMPLATE/ampTemplate/imagesSource/common/bullet_green_sq.gif" border=0>
								<% }
									else { %>
									<img src= "/TEMPLATE/ampTemplate/imagesSource/common/bullet_red_sq.gif" border=0>
								<%} %>
								&nbsp;
								<digi:trn key='aim:categoryIsMultiselect'>
									Multiselect
								</digi:trn>
								<br />
								<% if (category.isOrdered()) {%>
									<img src= "/TEMPLATE/ampTemplate/imagesSource/common/bullet_green_sq.gif" border=0>
								<% }
									else { %>
									<img src= "/TEMPLATE/ampTemplate/imagesSource/common/bullet_red_sq.gif" border=0>
								<%} %>
								&nbsp;
								<digi:trn key='aim:categoryIsOrdered'>
									Ordered
								</digi:trn>
							</td>
                            <td align="center" class="ignore" >
								<c:set var="translation">
									<digi:trn>Edit Category</digi:trn>
								</c:set>
							
								<digi:link paramId="edit" paramName="category" paramProperty="id"  href='/categoryManager.do' title="${translation}">
								    <img src= "/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" vspace="2" border="0" />
								</digi:link>
								<br/>
								<c:set var="translation">
								<digi:trn>Delete Category</digi:trn>
								</c:set>
								
								<digi:link paramId="delete" paramName="category" paramProperty="id"  href='/categoryManager.do' title="${translation}" onclick="return confirmDelete()">
									<img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0" />
								</digi:link>
							</td>
						</tr>
						</logic:iterate>
                         </tbody>
					</table>
					</div>
				
				</logic:notEmpty>
				</td>
				<td valign="top">
					<table cellPadding=0 cellSpacing=0  border=0>
						<tr>
							<td bgColor=#999999 class="box-title" style = "color:#000; height:16px;">
								<!-- Other Links -->
								<digi:trn key="aim:otherLinks">
									Links
								</digi:trn>
							</td>
						</tr>
						<tr>
							<td bgColor=#ffffff class=box-border>
								<table cellPadding=5 cellSpacing=1 width="100%">
									<tr>
										<td>
											<div style="width:100px; ">
											<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15" height="10"/>
											<c:set var="translation">
											<digi:trn key="aim:categoryManagerAddNewCategoryTitle">Click here to add a new category with specified values</digi:trn>
											</c:set>
											<digi:link href="/categoryManager.do?new=true" title="${translation}">
											   
											   <digi:trn key="aim:categoryManagerAddNewCategory">Add New Category</digi:trn>
											</digi:link>
											</div>
										</td>
									</tr>
																			
									<tr>
										<td>
											<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" contextPath="/aim">
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
				</td>
				</tr>
				<tr>
				<td>				
					<br>
					<digi:trn key="aim:tablelegend">
						Legend :
					</digi:trn>
					<img src= "/TEMPLATE/ampTemplate/imagesSource/common/bullet_green_sq.gif" border=0>
					<digi:trn key="aim:tableunable">
						Unable 
					</digi:trn>
					<img src= "/TEMPLATE/ampTemplate/imagesSource/common/bullet_red_sq.gif" border=0>
					<digi:trn key="aim:tabledisable">
						Disable 
					</digi:trn>
					<br>
				</td>
				</tr>
		</table>
	</td>
	</tr>
</table>
</div>

<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script>

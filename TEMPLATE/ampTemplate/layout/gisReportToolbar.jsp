<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>	

<style>
<!--
.toolbar{
	width: 90px;
	background: #addadd; 
	background-color: #addadd; 
	padding: 3px 3px 3px 3px; 
	position: relative; 
	top: 10px; 
	left: 10px;
	bottom: 100px;
		
}
.toolbartable{
	border-color: #FFFFFF;
	border-width: 2px;
	border-bottom-width: 2px; 
	border-right-width: 2px;"
	border-left-width: 2px;
	border-style: solid;
}
-->
</style>

<div class="toolbar" align="center">
<table border="0" align="center" bgcolor="#addadd" class="toolbartable">
	<tr>
		<td noWrap align=left valign="center" style="cursor:pointer;">
			<a target="_blank" onclick="exportPDF(); return false;">
				<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/pdf.gif" border="0" alt='Export to PDF'/>
			</a>
		</td>
		
		<td noWrap align=left valign="center">
            <digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/printer.gif" border="0" alt="Printer Friendly"/>
		</td>

		<td noWrap align=left valign="center">
			<digi:link href="#" target="_blank" onclick="javascript:window.close(); return false;">
				<digi:img src="module/aim/images/close.gif" border="0" alt="Close"/>
			</digi:link>
		</td>					
	</tr>
</table>
</div>
<br>

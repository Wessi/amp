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
	
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}	
	}

</script>

<digi:instance property="messageForm" />
<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="center" border="0">
	<tr>
		<th width="50%"><digi:trn key="ie:import">Import</digi:trn></th>
		<th width="50%"><digi:trn key="ie:export">Export</digi:trn></th>
	</tr>
	<tr>
		<td align="center">
			<digi:form action="/exportImportTemplates.do?actionType=importTemplates" method="post" enctype="multipart/form-data">
				<table cellpadding="3" cellspacing="3">
					<tr>
						<td><digi:trn key="contentrepository:addEdit:Path">Path:</digi:trn><font color="red">*</font></td>
						<td>
						  	<div class="fileinputs">
								<input id="fileUploaded" name="fileUploaded" type="file" class="file">
						    </div>
						</td>						
					</tr>
					<tr><td></td></tr>
					<tr>
						<td>&nbsp;</td>
						<td>
							<html:submit style="dr-menu" value="Import"/>				
						</td>
					</tr>
				</table>
			</digi:form>	
		</td>
		<td align="center">
			<digi:form action="/exportImportTemplates.do?actionType=exportTemplates">
				<html:submit style="dr-menu" value="Export"/>
			</digi:form>	
		</td>
	</tr>
</table>

<script type="text/javascript">
	initFileUploads();
</script>
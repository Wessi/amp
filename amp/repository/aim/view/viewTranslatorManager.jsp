<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.Map"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script langauage="JavaScript">
	function enableChkBox(chkBox) {

		alert(chkBox);

	}
</script>


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
	-moz-opacity:0 ;
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
	width: 217px;
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

<script type="text/javascript">
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

		button.value = '<digi:trn key="aim:browse">Browse..."</digi:trn>';
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

	function initFileUploads3() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));
		var image = document.createElement('img');
		image.src='pix/button_select.gif';
		fakeFileUpload.appendChild(image);
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

<digi:instance property="aimTranslatorManagerForm" />
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
						<digi:trn key="aim:translationManager">
						Translation Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
                      <span class=subtitle-blue>
                        <digi:trn key="aim:TranslationManagerHeader">
                        Translation Manager
                        </digi:trn>
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
					<logic:empty name="aimTranslatorManagerForm" property="importedLanguages">
					<logic:notEmpty name="aimTranslatorManagerForm" property="languages">
					<digi:form action="/translationManager.do" method="post" >
							<tr>
								<td>
									<digi:trn key="aim:translationManagerLangFoundMsg">
									The following languages where found on this site:
									</digi:trn>
								</td>
							</tr>
						<c:forEach items="${aimTranslatorManagerForm.languages}" var="lang">
							<tr>
								<td>
									<html:checkbox property="selectedLanguages" value="${lang}"/>
                                    <digi:trn key="aim:TranslationManagerLangiage${lang}">
                                    ${lang}
                                    </digi:trn>
									<br/>
 								</td>
	 						</tr>
						 </c:forEach>
							 <tr>
							 	<td>
                                  <c:set var="translation">
                                    <digi:trn key="aim:TranslationManagerExportButton">
                                    Export
                                    </digi:trn>
                                  </c:set>
                                  <html:submit style="dr-menu" value="${translation}" property="export"/>
                                </td>
							 </tr>
							 <td>
									<br/>
									<digi:trn key="aim:translationManagerLangSelectMsg">
									Please select the languages you want to export
									</digi:trn>
							</td>
					 </digi:form>
					</logic:notEmpty>

					<tr>
						<td><br/><br/><br/>
							</td>
					</tr>

					<digi:form action="/translationManager.do" method="post" enctype="multipart/form-data">
						<tr>
							<td>
								<!-- <html:file property="fileUploaded"></html:file> -->
								<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
									<!-- CSS content must be put in a separated file and a class must be generated -->
									<input id="fileUploaded" name="fileUploaded" type="file" class="file">
								</div>
							</td>
						</tr>
							 <tr>
							 	<td>
                                  <c:set var="translation">
                                    <digi:trn key="btn:translationManagerImport">
                                    Import
                                    </digi:trn>
                                  </c:set>
                                  <html:submit style="dr-menu" value="${translation}" property="import"/></td>
							 </tr>
					</digi:form>

					</logic:empty>
					<logic:notEmpty name="aimTranslatorManagerForm" property="importedLanguages">
						<digi:form action="/translationManager.do" method="post" >
							<tr>
								<td colspan="2">
									<digi:trn key="aim:translationManagerLangFoundImportMsg">
									The following languages where found in the file you imported:
									</digi:trn>
									<br/>
								</td>
							</tr>
							<logic:iterate name="aimTranslatorManagerForm" property="importedLanguages" id="lang"
																	type="java.lang.String">
								<tr>
									<td width="30%">

										<html:hidden property="selectedImportedLanguages" value="<%=lang %>" />
										<bean:write name="lang" />
										</td>
										<td>
										<select name='<%="LANG:"+lang%>' >
											<option value="-1" selected>
												<digi:trn key="aim:translationManagerImportPleaseSelect">
													-- Please select --
												</digi:trn>
											</option>
											<option value="update">
												<digi:trn key="aim:translationManagerImportUpdateLocal">
													Update local translations
												</digi:trn>
											</option>
											<option value="overwrite">
												<digi:trn key="aim:translationManagerImportOverwriteLocal">
													Overwrite local translations
												</digi:trn>
											</option>
										</select>
	 								</td>
		 						</tr>
							 </logic:iterate>
								 <tr>
								 	<c:set var="translation">
								 		<digi:trn key="btn:translationManagerImport">
								 			Import
								 		</digi:trn>
								 	</c:set>
								 	<td colspan="2"><br/><html:submit style="dr-menu" value="${translation}" property="importLang"/></td>
								 </tr>
								 <tr>
								<td colspan="2">
									<br/>
									<digi:trn key="aim:translationManagerLangSelectImportMsg">
									Please select the languages you want to update or to insert
									</digi:trn>
								</td>
							</tr>
						 </digi:form>
					</logic:notEmpty>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<script type="text/javascript">
	initFileUploads();
</script>





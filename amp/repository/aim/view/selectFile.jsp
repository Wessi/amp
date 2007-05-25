<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="aimAddDocumentForm" />

<digi:errors/>

<script language="JavaScript">
	function selectFile() {
		 <digi:context name="selectFile" property="context/module/moduleinstance/selectFile.do" />
	     document.aimAddDocumentForm.action = "<%= selectFile %>";
		  document.aimAddDocumentForm.target = window.opener.name;
	     document.aimAddDocumentForm.submit();
		  window.close();
	}
</script>

<digi:form action="/selectFile.do" enctype="multipart/form-data">
<html:hidden name="aimAddDocumentForm" property="processingMode"/>

		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:selectFile">Select file</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table border="0" cellpadding="5" cellspacing="5" width="100%">
									<tr>
										<td align="center" valign="middle">
											<html:file name="aimAddDocumentForm" property="formFile" size="40"/>					
										</td>
									</tr>
									<tr>
										<td>
											<table width="100%" cellpadding="3" cellspacing="3" border="0">
												<tr>
													<td align="right">
														<input type="button" value="Select" onclick="selectFile()" style="dr-menu">
													</td>
													<td align="left">
														<input type="button" value="Close" onclick="window.close()" style="dr-menu">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>					
					</table>
				</td>
			</tr>
		</table>
</digi:form>


<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<script>
<% 
  String contextPath = request.getContextPath();
  String _editor_url = new String();
  
  if (contextPath.length()==0 || contextPath.startsWith("/")){
   _editor_url = contextPath + "/repository/editor/view/";
  } else {
   _editor_url = "/"+contextPath + "/repository/editor/view/";
  }
%>  

  _editor_url = "<%=_editor_url%>"; 
</script>
<script language="JavaScript1.2" type="text/javascript" src='<digi:file src="module/editor/init.js"/>'></script>
	<digi:form action="/saveText.do" method="post">
	<html:hidden name="editorForm" property="key"/>
	<html:hidden name="editorForm" property="lang"/>
	<html:hidden name="editorForm" property="returnUrl"/>
	<table width="80%">
		<tr>
			<td noWrap width="7%" align="right"><B style="COLOR: #004080">Title:</B>
			</td>
			<td width="93%">
				<html:text name="editorForm" property="title" size="70" style="COLOR: #004080"/>
			</td>
		</tr>
	</table><BR>
	<html:textarea name="editorForm" property="content" rows="20" cols="100" ></html:textarea><br>Editing: <b>
	<span style="color: #cc4000">
	<bean:write name="editorForm" property="key"/></span></b>&nbsp;
	<html:submit value="Save now" style="BACKGROUND: #f1efed url('images/editor/grad-btn.gif') repeat-x;cursor: hand"/>&nbsp;
	<html:select property="lang" onchange="ChangeLanguage(this)">
	<bean:define id="lid" name="editorForm" property="languages" type="java.util.Collection"/>
	<html:options collection="lid" property="code" labelProperty="name"/></html:select></digi:form>
<script language="JavaScript1.2" type="text/javascript" src='<digi:file src="module/editor/config.js"/>'></script>
<script>function ChangeLanguage (obj) {
  var lang = obj.value;
  <digi:context name="showText" property="context/module/moduleinstance/showEditText.do" />
  var szQuery = "<%= showText %>?langCode="+lang;
  ret = window.confirm ("Switching language will refresh \n page with content on that language.");
  
  if (ret) {document.location.href = szQuery;}
}
</script><!-- list of other languages -->
	<logic:present name="editorForm" property="editorList"><BR>
	<table width="100%">
		<tr>
			<td>
				<hr>
			</td>
		</tr>
	</table>	<!-- -->
	<table width="100%">
		<tr>
			<td noWrap align="center"><font color="darkblue"><b>Available Languages for this Text</b></font>
			</td>
		</tr>
		<tr>
			<td align="center">	&nbsp;
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td noWrap align="left"><small><b>Language</b></small>
			</td>
			<td noWrap align="left"><small><b>Last Modification Date</b></small>
			</td>
			<td noWrap align="left"><small><b>Last User</b></small>
			</td>
		</tr>
		<logic:iterate id="editorList" name="editorForm" property="editorList" type="org.digijava.module.editor.form.EditorForm.TextInfo">
		<tr align="left">
			<td><small>
				<bean:write name="editorList" property="langName"/></small>
				<digi:link href="/showEditText.do" paramName="editorList" paramId="langCode" paramProperty="langCode">[<small>Edit</small>]</digi:link>
			</td>
			<td align="left"><small>
				<bean:write name="editorList" property="lastModDate"/></small>
			</td>
			<td align="left"><small>
				<bean:write name="editorList" property="userName"/></small>
			</td>
		</tr></logic:iterate>
	</table></logic:present>
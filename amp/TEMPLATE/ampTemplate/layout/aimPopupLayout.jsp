<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<html>
<digi:base />
<head>
			<%
				String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
				String key=(title.replaceAll(" ",""));
			%>
	<TITLE>
		
		<logic:present name="extraTitle" scope="request">
				<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />
				<c:set var="key">aim:pagetitle:<%=key%><%=extTitle%></c:set>
				<digi:trn key="aim:pagetitle:amp">AMP </digi:trn>   
				<digi:trn key="${key}">
					<%=title%> <%=extTitle%>
				</digi:trn>
		</logic:present>
	
		<logic:notPresent name="extraTitle" scope="request">
					
					<c:set var="key">aim:pagetitle:<%=key%></c:set>
					<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
					<digi:trn key="${key}"><%=title%></digi:trn>
		</logic:notPresent>
		
	
	</TITLE>

	<style type="text/css">
           table.inside {border-color: #CCC; border-style: solid; font-size:11px;}
	.buttonx, .dr-menu {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:11px; color:#FFFFFF; font-weight:bold; padding-left:5px; padding-right:5px; padding-top:3px; padding-bottom:3px;}
	</style>
</head>
<body bgcolor="#ffffff">
<digi:insert attribute="body" />
</body>
</html>

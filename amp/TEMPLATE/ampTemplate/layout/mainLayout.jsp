<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<html>
	<digi:base />
	<digi:context name="digiContext" property="context"/>
	<script language="JavaScript" type="text/javascript">
    <!--
    function addLoadEvent(func) {
    	  var oldonload = window.onload;
    	  if (typeof window.onload != 'function') {
    	    window.onload = func;
    	  } else {
    	    window.onload = function () {
    	      if (oldonload) {
    	        oldonload();
    	      }
    	      func();
    	  }
    	}
    }  	
	-->  
    </script>

<head>
	<%
		String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
		String key=(title.replaceAll(" ",""));
	%>
	<logic:present name="extraTitle" scope="request">
		<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />
		<title>
			<c:set var="key">aim:pagetitle:<%=key%><%=extTitle%></c:set>
			<digi:trn>Aid Management Platform </digi:trn> 
			<digi:trn key="${key}">
				<%=title%> <%=extTitle%>
			</digi:trn>
		</title>
	</logic:present>
	<logic:notPresent name="extraTitle" scope="request">
		<title>
			<c:set var="key">aim:pagetitle:<%=key%></c:set>
			<digi:trn>Aid Management Platform </digi:trn> 
			<digi:trn key="${key}">
				<%=title%>
			</digi:trn>
		</title>
	</logic:notPresent>
	
	<!-- Scripts  -->
	
	<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/amp/common.js"/>"></script>
</head>

<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
	<digi:secure authenticated="false">
		<logic:notPresent name="currentMember" scope="session">
			<digi:insert attribute="headerTop" />	
		</logic:notPresent>
	</digi:secure>
	<digi:secure authenticated="true">
		<jsp:include page="headerTop_2.jsp"/>
	</digi:secure>
	<center>
	<div class="main_menu"style="height:25px;" >
		<digi:insert attribute="headerMiddle"/>
		<digi:secure authenticated="true">
			<div class="workspace_info"> <!-- I think this class should be renamed to correspong the logout item -->   						
   			<digi:link styleClass="loginWidget" href="/j_spring_logout" module="aim">
				<digi:trn key="aim:logout">LOGOUT</digi:trn>
			</digi:link>
		</div>
		</digi:secure>
		
	</div></center>

<div class="breadcrump_1">&nbsp;</div>	
			
	<table width="100%" id="homelayout">
		<tr>
			<td>
				<digi:insert attribute="body"/>
			</td>
		</tr>
	
	</table>
    <digi:insert attribute="footer"/>
</body>
</html>
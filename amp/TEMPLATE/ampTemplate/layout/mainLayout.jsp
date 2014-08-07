<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
	<%-- request.setAttribute("compatibility_shim", String) - use it to overwrite specific pages' X-UA-Compatible meta tags --%>
	<c:choose>
    	<c:when test="${empty compatibility_shim}">
			<meta http-equiv="X-UA-Compatible" content="chrome=1; IE=edge">
		</c:when>
    	<c:otherwise>
        	<meta http-equiv="X-UA-Compatible" content="<c:out value="${compatibility_shim}" />">
    	</c:otherwise>
	</c:choose>
	<digi:base />
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>
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
	<%
		String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
		String key=(title.replaceAll(" ",""));
	%>
	<logic:present name="extraTitle" scope="request">
		<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />
		<title>
			<c:set var="key">aim:pagetitle:<%=key%><%=extTitle%></c:set>
			<digi:trn>Aid Management Platform </digi:trn> 
			<digi:trn><%=title%></digi:trn> ${extTitle}
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
	
	<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>
	 <digi:ref href="css/ampPrint.css" type="text/css" rel="stylesheet" media="print" />
</head>


<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
<div class="headerTop">
	<logic:present name="currentMember" scope="session">
		<jsp:include page="headerTop_2.jsp"/>
	</logic:present>
	<logic:notPresent name="currentMember" scope="session">
		<digi:insert attribute="headerTop" />
	</logic:notPresent>
</div>
	<div class="main_menu" >
	
		  	<div  width="1000">
        	
            	<div style="width:900px; vertical-align:top;" ><digi:insert attribute="headerMiddle"/></div>
                <div><digi:secure authenticated="true">
         <div class="workspace_info"> <!-- I think this class should be renamed to correspong the logout item -->   						
   			<digi:link styleClass="loginWidget" href="/j_spring_logout" module="aim">
				<digi:trn key="aim:logout">LOGOUT</digi:trn>
			</digi:link>
		</div>	
		</digi:secure></div>
            
        </div>
		
	</div>

<logic:notPresent name="bootstrap_insert" scope="request">
	<div class="breadcrump_1">&nbsp;</div>
	<div style="width:1000px;margin:0 auto;">
		<table width="100%" id="homelayout">
			<tr><td>		
				<digi:insert attribute="body"/>
			</td></tr>
		</table>
    </div>
    <div class="footerText" >
    	<digi:insert attribute="footer"/>
    </div>    
</logic:notPresent>
<logic:present name="bootstrap_insert" scope="request">
	<digi:insert attribute="body"/>
	<!-- no footer in boostrap-iframe hacks  -->
</logic:present>
</body>
</html>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

	<script language="JavaScript" type="text/javascript" src="<digi:file src="js_2/amp/common.js"/>"></script>

	<!-- Styles -->	
	<link href='css_2/amp.css' rel='stylesheet' type='text/css'>
	<!--[if IE 6]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_6.css' rel='stylesheet' type='text/css'><![endif]-->
	<!--[if IE 7]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_7.css' rel='stylesheet' type='text/css'><![endif]-->
	<!--[if IE 8]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_8.css' rel='stylesheet' type='text/css'><![endif]-->
		<!-- Menu Source -->
		<script type="text/javascript" src="js_2/jquery/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="js_2/jquery/jquery.dimensions.js"></script>
		<script type="text/javascript" src="js_2/jquery/jquery.positionBy.js"></script>
		<script type="text/javascript" src="js_2/jquery/jquery.bgiframe.js"></script>
		<script type="text/javascript" src="js_2/jquery/jquery.jdMenu.js"></script>
		<script type="text/javascript" src="js_2/jquery/jquery.activeMenu.js"></script>
		
<logic:present name="currentMember" scope="session">
	<script language=javascript>
	function showUserProfile(id){
		<digi:context name="information" property="context/aim/default/userProfile.do" />
		//openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
		var param = "~edit=true~id="+id;
		previewWorkspaceframe('/aim/default/userProfile.do',param);
	}
	</script>
</logic:present>

<!-- Prevent Skype Highlighter -->
<META name="SKYPE_TOOLBAR" content="SKYPE_TOOLBAR_PARSER_COMPATIBLE" />
<digi:context name="displayFlag" property="context/aim/default/displayFlag.do" />
<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

<!-- HEADER START -->
<div class="header">
	<div class="centering">
		<div class="login_nav">
			<digi:insert attribute="logWidget"/>
		</div>
		<div class="logo">
			<logic:notEmpty name="defFlagExist" scope="application">
        		<logic:equal name="defFlagExist" scope="application" value="true">
        			<img src="<%=displayFlag%>" align=left>
	       		</logic:equal>
    	    </logic:notEmpty>
			<div class="amp_label"> 
				 &nbsp;<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn>
        	</div>
		</div>
	</div>
</div>

<!-- HEADER END -->

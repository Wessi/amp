<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>

<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>

	<!-- Menu Source -->
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-1.4.2.min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.dimensions.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.positionBy.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.bgiframe.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.jdMenu.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.ui.core.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.ui.widget.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.ui.tabs.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.cookie.js"/>"></script>
		
				

	<!-- Stylesheet of AMP -->
        <digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
        <digi:ref href="/TEMPLATE/ampTemplate/css_2/tabs.css" type="text/css" rel="stylesheet" />
        
        <!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"/>"> 
<link rel="stylesheet" type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/menu/assets/skins/sam/menu.css"/>"> 
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script> 
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"/>"></script> 
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"/>"></script> 
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"/>"></script> 
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"/>"></script> 
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/menu/menu-min.js"/>"></script>


<script type="text/javascript">
	var YAHOOAmp 	= YAHOO;
</script>  
        
        
        
<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>
<logic:present name="currentMember" scope="session">
	<script language=javascript>
	function showUserProfile(id){
		<digi:context name="information" property="context/module/moduleinstance/userProfile.do" />
		openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
	}
	function help(){
		 <digi:context name="rev" property="/help/help.do~blankPage=true" />
			openURLinWindow("<%=rev%>",1024,768);
		}
	function adminHelp(){
			 <digi:context name="admin" property="/help/admin/help.do~blankPage=true" />
			openURLinWindow("<%=admin%>",1024,768);
	}
	
	function canExit(){
	    if(typeof quitRnot1 == 'function') {
	        return quitRnot1('${msg}');
	    }
	    else{
	        return true;
	    }
	
	}	

	//SIMPLE MENU
	var timeout    = 500;
	var closetimer = 0;
	var ddmenuitem = 0;

	function wks_menu_open()
	{  wks_menu_canceltimer();
		wks_menu_close();
	   ddmenuitem = $(this).find('ul').css('display', 'block');}

	function wks_menu_close()
	{  if(ddmenuitem) ddmenuitem.css('display', 'none');}

	function wks_menu_timer()
	{  closetimer = window.setTimeout(wks_menu_close, timeout);}

	function wks_menu_canceltimer()
	{  if(closetimer)
	   {  window.clearTimeout(closetimer);
	      closetimer = null;}}

	$(document).ready(function()
	{  $('#wks_menu > li').bind('mouseover', wks_menu_open)
	   $('#wks_menu > li').bind('mouseout',  wks_menu_timer)});

	document.onclick = wks_menu_close;

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

 <logic:notEmpty name="currentMember" scope="session">
 	<bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
 </logic:notEmpty>

<!-- HEADER START -->
<div class="header">
	<div class="centering">
		<div class="logo">
			<img src="img_2/amp_logo.gif" align=left>
			<div class="amp_label">&nbsp;<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn></div>
		</div>
		<logic:notEmpty name="currentMember" scope="session">
			<feature:display name="Change Workspace" module="My Desktop">
				<div class="workspace_info">
					<ul class="wks_menu" id="wks_menu">
						<logic:iterate id="item"  name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
						<bean:define id="team" name="item" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
						<logic:equal name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
							<li style="cursor: pointer;margin-top:0px;"> 
								<digi:trn>Workspace</digi:trn>:
								<b><bean:write name="team" property="name"/><img src="img_2/arr_wrk.gif" border=0 class="menu_arr"></b>
								<ul style="width: auto;">
						</logic:equal>
						</logic:iterate>
						
						<logic:iterate id="item2"  name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
						<bean:define id="team2" name="item2" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
						<logic:notEqual name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
							<li>
								<a href='/selectTeam.do?id=<bean:write name="item2" property="ampTeamMemId"/>' onclick="return canExit()">
									<bean:write name="team2" property="name"/>
								</a>
							</li>
						</logic:notEqual>
						</logic:iterate>
			            	</ul>
		            	</li>
					</ul>
				</div>
			</feature:display>
		</logic:notEmpty>
		<div >
			<table class="top_nav">
				<tr>
					<td>
						<logic:notEmpty name="currentMember" scope="session">
							<a href="javascript:showUserProfile(${teamMember.memberId})">${teamMember.memberName}</a>
						</logic:notEmpty>
						<img src="img_2/top_sep.gif" class="top_sep">
					</td>
					<td>
						<module:display name="HELP">
							<ul class="wks_menu" id="wks_menu" style="text-transform: uppercase;">
								<li style="margin-top:15px;padding: 0px;height: 0px;margin-right:0px">
									<a style="cursor: pointer;">
										<digi:trn>HELP</digi:trn><img src="img_2/arr_wrk.gif" border=0 class="menu_arr">
									</a>
									<ul style="width: auto;">
										<feature:display name="Admin Help" module="HELP">
		                                 <li>
	                                        <a onClick="adminHelp();" style="cursor: pointer;">
	                                        	<digi:trn>AMP Admin Help</digi:trn>
	                                        </a>
	                                    </li>
										</feature:display>
										<feature:display name="User Help" module="HELP">
	                                     <li>
	                                     <a onClick="help();" style="cursor: pointer;">
	                                 	    <digi:trn key="aim:AMPHelp">AMP Help</digi:trn>
	                                     </a>
	                                     </li>
										</feature:display>
										<feature:display name="Glossary" module="HELP">
	                                     <li>	
		                                 	<a href="/help/glossary.do">
		                                 		<digi:trn>Glossary</digi:trn>
		                                 	</a>
	                                     </li>
										</feature:display>
										<feature:display name="Support Request Form" module="HELP">
	                                    <li>	
	                                    	<a href="http://support.ampdev.net/login.action?code=<%=FeaturesUtil.getDefaultCountryIso()%>" target="_blank">
	                                        	<digi:trn key="aim:supportrequestform">Support Request Form</digi:trn>
	                                        </a>
	                                    </li>
										</feature:display>
										<feature:display name="About AMP" module="HELP">
	                                    <li>
	                                    	<%
											siteDomain = (org.digijava.kernel.request.SiteDomain) request.getAttribute(org.digijava.kernel.Constants.CURRENT_SITE);
											session.setAttribute("site", siteDomain);
											%>
	                                        <a target="name" onClick="showAbout(); return false;" style="cursor: pointer;"> 
	                                        	<digi:trn key="aim:aboutamp">About AMP</digi:trn>
	                                        </a>
	                                    </li>
	                                    </feature:display>
									</ul>
								</li>
							</ul>
						</module:display>			
					</td>
					
					<td style="text-transform: uppercase;">
						<img src="img_2/top_sep.gif" class="top_sep">
						<digi:link styleClass="loginWidget" href="/j_acegi_logout" module="aim">
							<digi:trn key="aim:logout">LOGOUT</digi:trn>
						</digi:link>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<!-- HEADER END -->





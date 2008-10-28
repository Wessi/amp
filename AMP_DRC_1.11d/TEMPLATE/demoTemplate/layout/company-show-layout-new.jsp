

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %><html>



<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<title>Company Profile</title>
<meta name="description" content="Development Gateway market for tenders and procurement opportunities in all countries - from international development banks and government agencies worldwide. Provides free summaries of tender notices and free e-mail alerts. Created by the World Bank, UNDP and other organizations.">
<meta name="keywords" content="tender, procurement, purchasing, business opportunities, World Bank, ADB, EBRD, Asian Development Bank, AfDB, SIMAP, TED, CPV, UN, EU, consulting, e-mail alerts, email alerts, tender alerts, invitation to bid, consultants, contract award, international, competitive bidding, ICB, bid, bids, contracts, government, agency, project, development business, expressions of interest, prequalification, IFB">
<link href="./jsp/images/styles.css" type="text/css" rel="stylesheet">

<script language="Javascript">
<!--
var OK_to_leave = "yes";
//-->
</script>
</head>

<body text="#000000" vLink="#bb7700" aLink="#bb7700" link="#bb7700" bgColor="#ffffff" leftMargin="0" background="/eproc/images/onedot.gif" topMargin="0" MARGINHEIGHT="0" MARGINWIDTH="0"  onload=init() onBeforeUnload='if(OK_to_leave=="no") {return "All changes made by you on this page will be lost!"}'>
<table cellpadding=0 cellspacing=0 width="100%" border=0>



<tr>
		<!-- header -->
		
		
		<tiles:insert attribute="header" flush="true"/>
		
		
		
		

</tr>


<tr>

  		<!-- menuheader -->
  		<tiles:insert attribute="menuheader" flush="true"/>

</tr>

<tr><br></tr>

<tr>

   		<!-- body -->
   		<tiles:insert attribute="body" flush="true"/>
</tr>

<tr>
  		<!-- footer --><td>
  		<tiles:insert attribute="footer" flush="true"/>
</tr>

</table>

</body>
</html>


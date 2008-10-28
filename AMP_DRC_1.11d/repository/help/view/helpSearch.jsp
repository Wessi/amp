<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:instance property="helpForm" />
<digi:form action="/helpActions.do?actionType=searchHelpTopic">

<script language="JavaScript">

function showHint(str){
  if (str.length==0){ 
		 	document.getElementById("livesearch").innerHTML="";
		 	
		 	document.getElementById("livesearch").style.border="1px";
	 	return
	 }
	 
 xmlHttp=GetXmlHttpObject()
	if (xmlHttp==null){
 			alert ("Browser does not support HTTP Request")
 		return
 	} 
 	$("#livesearch").show();
	var url="/help/helpActions.do?actionType=vewSearchKey"
	url=url+"&loadKey="+str
	xmlHttp.open("GET",url,true)
	xmlHttp.onreadystatechange=stateChanged 
	xmlHttp.send(null)
}

function stateChanged(){
	 if (xmlHttp.readyState==4)
  { 
	 document.getElementById("livesearch").innerHTML=xmlHttp.responseText;
	 document.getElementById("livesearch").style.border="1px solid #A5ACB2";
   } 
}


function GetXmlHttpObject()	{
	 var xmlHttp=null;
	 try
 	{
 		// Firefox, Opera 8.0+, Safari
 	xmlHttp=new XMLHttpRequest();
 	}catch (e)
 		
 		{
 		
 		// Internet Explorer
 	 try	{
  		xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
  	}
 		catch (e)
  		{
  			xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
  		}
 	}
	return xmlHttp;
}

function select(title){
		document.getElementById("selected").value=document.getElementById(title.innerHTML).innerHTML;
   		$("#livesearch").hide();
}  

</script>
<style type="text/css">

.silverThing {background-color:silver; }
.whiteThing { background-color: #FFF; }

</style>


	<div id="content" class="yui-skin-sam" style="width: 100%;">
	<div id="demo" class="yui-navset"
		style="font-family: Arial, Helvetica, sans-serif;">

	<ul class="yui-nav">
		<li class="selected"><a title='<digi:trn key="aim:helpSearch">Search</digi:trn>'>
		<div>
		<digi:trn key="aim:helpSearch">Search</digi:trn>
		</div>
		</a></li>
	</ul>
	<div class="yui-content"
		style="height: auto; font-size: 11px; font-family: Verdana, Arial, Helvetica, sans-serif;">
	<div style="padding: 2; text-align: center">

	  <input type="text" name="keywords" onkeyup="showHint(this.value)" id="selected"/>
			<div style="background-color:white;overflow:auto;display: block; text-align: left;" id="livesearch" >
			</div>
			 <c:set var="searchtpc">
				<digi:trn key="help:SearchText">Search Topic</digi:trn>
			</c:set>
	  		<input type="submit" class="dr-menu" value="${searchtpc}"/></div>
		</div>
	  </div>
	</div>
</digi:form>
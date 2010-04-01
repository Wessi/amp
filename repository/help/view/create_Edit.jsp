<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.kernel.util.RequestUtils"%>
<digi:instance property="helpForm"/>
<c:set var="request" scope="session"><%= RequestUtils.getRealModuleInstance(request).getInstanceName() %></c:set>

<script type="text/javascript">
<!--

function validate(topickey){
		if(!topickey){
			alert("Default topic is not tree topic");
		return false;
	}else{
            return true;
        }
}
  function getKey(){

    var topicKey = document.getElementById("key").innerHTML;
    var key = topicKey.slice(topicKey.indexOf("y:")+2);
    return key;
  }

function edit(){
	
    if(validate(getKey())){
    	
        <digi:context name="editTopic" property="context/module/moduleinstance/helpActions.do~actionType=editHelpTopic"/>
        window.location = "<%=editTopic%>~topicKey="+getKey()+"~wizardStep=0";

    }

}

function remove(){

    if(validate(getKey())){

    	<digi:context name="removeTopic" property="context/module/moduleinstance/helpActions.do~actionType=deleteHelpTopics"/>
    	window.location = "<%=removeTopic%>~topicKey="+getKey()+"~wizardStep=0~multi=false";
		
    }

}

function reFresh(){

        <digi:context name="tree" property="context/module/moduleinstance/helpActions.do~actionType=saved"/>
        window.location = "<%=tree%>~wizardStep=0";
		

}


function create(){

        <digi:context name="editTopic" property="context/module/moduleinstance/helpActions.do~actionType=createHelpTopic"/>
        window.location = "<%=editTopic%>~wizardStep=0";
		

}

function saveTreeState(){

     var xmlString = document.getElementById("xmlString").value;
     //======= URL Encoded Character is %26 for &. and we have &amp; that need to replace there are ajax sending problrm ====== //
 replacedXmlString = xmlString.replace(/&amp;/,"%26");


     //======= URL Encoded Character is %26 for &. and we have &amp; that need to replace there are ajax sending problrm ====== //
     replacedXmlString = xmlString.replace(/&amp;/,"%26");

     xmlHttp=GetXmlHttpObj();
		if (xmlHttp==null){
	 			alert ("Browser does not support HTTP Request")
	 		return
	 	}

        showProgress('progress');
        var urlact="/help/helpActions.do?actionType=saveTreeState&Request=${request}"
	    xmlHttp.open("POST",urlact,true);
	    xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xmlHttp.send("&changedXml="+replacedXmlString);
		xmlHttp.onreadystatechange=stateChanged;
  

}

function stateChanged(){

   if (xmlHttp.readyState==4)
  {
        if(xmlHttp.status == 200)
        {
          //code to process Ajax request
          hideProgress('progress');
          reFresh();
        }
   }
}


function GetXmlHttpObj()	{
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


   function showProgress(name)
   {
      var prg = '<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader.gif" alt="loading..."/>Saving...';
       var progressViewer = document.getElementById(name);
       progressViewer.innerHTML = prg;
   }

   function hideProgress(name)
   {
      var progressViewer = document.getElementById(name);
       progressViewer.innerHTML = "";
   }

 

//-->
</script>




<div id="content"  class="yui-skin-sam" style="width:100%;">
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">

                        <ul class="yui-nav">
                          <li class="selected">
                          <a title='><digi:trn key="aim:help:editcreate"> Edit/Create</digi:trn>'>
                          <div>
                            <digi:trn key="aim:help:editcreate"> Edit/Create</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
                        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

                            <div style="padding:2px;">
                                <c:set var="topicEdit">
                                  <digi:trn>Click here to Edit Help Topic</digi:trn>
                                </c:set>
                                <c:set var="topicCreate">
                                  <digi:trn>Click here to Create Help Topic</digi:trn>
                                </c:set>
                                <c:set var="topicDelete">
                                  <digi:trn>Click here to Delete Help Topic</digi:trn>
                                </c:set>
                                <c:set var="savetreeState">
                                  <digi:trn>Click here to Delete Help Topic</digi:trn>
                                </c:set>

                                      <a class="link" onclick="edit();" title="${topicEdit}" ><digi:trn>Edit Topic</digi:trn></a> |

                                      <a class="link" onclick="create();" title="${topicCreate}" > <digi:trn>Create Topic</digi:trn></a> |

                                      <a class="link" onclick="remove();" title="${topicDelete}" ><digi:trn>Remove Topic</digi:trn></a> |


                                        <a class="link" onclick="saveTreeState();" title="${savetreeState}" ><digi:trn>Save Tree State</digi:trn></a>


                          </div>
                     </div>
	</div>
</div>



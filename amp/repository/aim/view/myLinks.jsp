<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<br/>
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	
    <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
        <ul class="yui-nav">
          <li class="selected">
          <a title='<digi:trn key="aim:ListofRelatedLinks">Frequently Used Links for Desktop</digi:trn>'>
          <div>
            <digi:trn key="aim:resources">Resources</digi:trn>
          </div>
          </a>
          </li>
        </ul>
    
        
        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
        	<logic:notEmpty name="myLinks" scope="session">
  			
	  						<div title="${translation}" style="margin: 2px">
		               			 <logic:iterate name="myLinks" id="doc" scope="session" type="org.digijava.module.contentrepository.helper.DocumentData"> 
		                    	   <div style="margin: 2px">
		                        	<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
		                          	<c:if test="${doc.webLink == null}" >
		                             	<a  href="/contentrepository/downloadFile.do?uuid=<bean:write name='doc' property='uuid'/>">
										<bean:write name="doc" property="title"/>
		                            	</a>
									</c:if>
									
									<c:if test="${doc.webLink != null}" >
		                    	       <a href="<bean:write name="doc" property="webLink"/>">
										<bean:write name="doc" property="title"/>
										</a>
									</c:if>
		                         </div>
		                </logic:iterate>
		                
		                 <c:set var="translation">
	                        <digi:trn key="aim:clickToViewMoreDocuments">Click here to view more Documents</digi:trn>
	                    </c:set>	
	               		
	               		 <div title='${translation}'  style="margin-left:12px;margin-top:5px; margin-bottom:7px">
	                          <a href="/contentrepository/documentManager.do">
	                            <digi:trn key="aim:moreResources">More Resources ...</digi:trn>
	                        </a>							
	                    </div>
	                    
              	 	 </logic:notEmpty>    
                       <logic:empty name="myLinks" scope="session">
               			 <div style="margin-left:12px;margin-top:5px; margin-bottom:7px"> <digi:trn key="aim:noResources">No Resources</digi:trn></div>
           			  </logic:empty>
		</div>
</div>
    
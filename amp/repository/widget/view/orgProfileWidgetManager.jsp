<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="widgetOrgProfileWidgetForm" />
<script type="text/javascript"><!--


   function validateDelete(type){
       var msg='<digi:trn jsFriendly="true">Do you want to delete</digi:trn> '+type+" from the org. Profile page?";
       return confirm(msg);
   }

	function validatePlaces(){
		
		for(it = 1; it<8; it++){
		
	      test = document.getElementById("place"+it);
	
			for(j = it+1; j<8; j++){
	
				testo = document.getElementById("place"+j);
	   
					for(i = 0; i < test.length; i++)
					{
					   var link = test.item(i);
					
					   	 for(k = 0; k < testo.length; k++)
						   		{
						      		var linko = testo.item(k);
						      			if(link.selected && linko.selected && link.value == linko.value){
						      			  
										  return false;
								  		   
							      		}
								      
						        }
					 }
	            }
	       }
		return true;
	}

 function save(){
	if(validatePlaces()){
			submitPlaces();
		}else{
			alert("This position is already taken, please choose another position or modify the existing position.");
	   }
 }

   function submitPlaces(){

	   var str = '';

	   var dnl = document.widgetOrgProfileWidgetForm.selPlaces; 
	   for(i = 0; i < dnl.length; i++)
	   {
	      var link = dnl.item(i);
	      
	      str+=','+link.value;
	      
	   }
		document.getElementById('selId').value = str;
       <digi:context name="addEdit" property="/widget/orgProfileManager.do~actType=save" />
        document.widgetOrgProfileWidgetForm.action = "${addEdit}";
        document.widgetOrgProfileWidgetForm.submit();
	   	
	   }

-->
</script>


<digi:form action="/orgProfileManager.do~actType=viewAll">
<html:hidden property="oldId" name="widgetOrgProfileWidgetForm" styleId="allOldId"/>

<html:hidden property="selectedId" name="widgetOrgProfileWidgetForm" styleId="selId"/>


<table width="60%" border="0" cellpadding="15">
	<tr>
		<td>
			<span class="crumb">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:orgProfileManager">Organization Profile Manager</digi:trn>
			</span>
		</td><!--
                <tr>
                    <td>
                        <digi:errors/>
                    </td>
                </tr>
              -->
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="widget:orgProfileManager:pageHeader">Organization Profile Manager</digi:trn></span>
		</td>
	</tr>
	<tr>
	<!--
		<td>
			<a href="/widget/orgProfileManager.do~actType=create"><digi:trn key="widget:orgProfileManager:createNew">Create new</digi:trn></a>
		</td>
	 -->
	</tr>
	<tr>
		<td>
	<table border="0" width="100%" align="center" style="font-family:verdana;font-size:11px;">
	<tr>
		<td bgColor="#d7eafd"; colspan='3'>
		 <a href=javascript:; style="text-decoration:none" onclick="save()">
		  		<img border="0" height="16"  src='<digi:file src="/TEMPLATE/ampTemplate/images/save.png"/>'/>
		  		<digi:trn>Save</digi:trn>
		 </a>
		</td>
	</tr>
	<tr>
	 <td>		
			<table border="0" width="100%" height="100%" align="center" style="font-family:verdana;font-size:11px;">
				<tr>
					<td nowrap="nowrap" width="30%" align="center" height="20px">
						<strong><digi:trn key="widget:orgProfileManager:profiletype">Profile Type</digi:trn></strong>
                     </td>
                     <td nowrap="nowrap" width="60%" align="center" height="20px">
						<strong><digi:trn key="widget:orgProfileManager:type">Type</digi:trn></strong>
                     </td>
                     <td nowrap="nowrap" width="50%" align="center" height="20px">
						<strong><digi:trn key="widget:orgProfileManager:position">Position</digi:trn></strong>
                     </td>
                     <!--
                     <td nowrap="nowrap">
						<strong><digi:trn key="widget:orgProfileManager:operations">Operations</digi:trn></strong>
					</td>
					 -->
				</tr>
				<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
					<tr>
			
						<td nowrap="nowrap" height="35px">
                            <c:set var="widgetType">
                                <c:choose>
                                    <c:when test="${orgProfile.type==1}">
                                        <digi:trn>Summary</digi:trn>
                                        
                                    </c:when>
                                    <c:when test="${orgProfile.type==2}">
                                        <digi:trn>Type of Aid</digi:trn>
                                    </c:when>
                                    <c:when test="${orgProfile.type==3}">
                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
                                    </c:when>
                                    <c:when test="${orgProfile.type==4}">
                                        <digi:trn>ODA Profile</digi:trn>
                                    </c:when>
                                    <c:when test="${orgProfile.type==5}">
                                        <digi:trn>Sector Breakdown</digi:trn>
                                    </c:when>
                                    <c:when test="${orgProfile.type==6}">
                                        <digi:trn>Regional Breakdown</digi:trn>
                                    </c:when>
                                     <c:when test="${orgProfile.type==7}">
                                        <digi:trn>Paris Declaration</digi:trn>
                                    </c:when>
                                    <c:otherwise>
                                        <digi:trn>Unknown</digi:trn>
                                    </c:otherwise>
                                </c:choose>
                            </c:set>
                            ${widgetType} 
                             &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;                  
						</td>
                          <td nowrap="nowrap" align="center" height="35px">
                          <c:if test="${orgProfile.type == 1 || orgProfile.type == 7||orgProfile.type == 2 ||orgProfile.type == 4 }">
                          		<digi:trn>Table</digi:trn>
                          </c:if>
                          <c:if test="${orgProfile.type == 3 ||  orgProfile.type == 6 || orgProfile.type == 5}">
                          		 <digi:trn>Chart</digi:trn>
                          </c:if>
                            &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                          </td>                     
                          <td width="50%" height="35px">
                            <html:select name="widgetOrgProfileWidgetForm" property="selPlaces" styleId="place${orgProfile.type}">
                              <c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}" >
                                <c:if test="${places.assignedWidget.id == orgProfile.id}">
	                            			<option selected="selected" value="${places.id}">${places.name}</option>
	                           	</c:if>
	                           	<c:if test="${places.assignedWidget.id != orgProfile.id}">
	                            			<option value="${places.id}">${places.name}</option> 
				               	</c:if>
				               </c:forEach> 	
			                </html:select>
                          </td>
                       <!--     
						<td nowrap="nowrap">
							<a href="/widget/orgProfileManager.do~actType=update~id=${orgProfile.id}">
								<digi:trn key="widget:editLink">Edit</digi:trn>
							</a>
                                                        
							|&nbsp;
							<a href="/widget/orgProfileManager.do~actType=delete~id=${orgProfile.id}" onClick='return validateDelete("${widgetType}")'>
								<img border="0" src='<digi:file src="images/deleteIcon.gif"/>'>
							</a>
						</td>
						 --> 
						
					</tr>
				</c:forEach>
       			</table>
			</td>
			<td>&nbsp;&nbsp;</td>
			     <td>
					<table width="100%" height="100%" align="center" style="font-family:verdana;font-size:11px;border: 1px solid black;">
							<tr>
							<td height="7%" align="center"><strong><digi:trn key="pdfop">Position Diagram For Org Profile</digi:trn></strong></td>
							</tr>
								<tr>
								 <td>
								 	<table border="0" width="100%" >
								 	<tr>
								 		
									 	<td height="60px" align="center">
										 	<table width="100%" height="100%" style="border: 1px outset silver" >
										 		<tr>
										 			<td>
												 	<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
													 	<c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}">
													 		<c:if test="${places.assignedWidget.id == orgProfile.id}">
														 	  <c:choose>
															 	<c:when test="${places.name =='orgprof_chart_place1'}">
															 	
															 			<c:set var="widgetType">
												                                <c:choose>
												                                    <c:when test="${orgProfile.type==1}">
												                                        <digi:trn>Summary</digi:trn><br>
                                       													<digi:trn>5 largest projects</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==2}">
												                                        <digi:trn>Type of Aid</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==3}">
												                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==4}">
												                                        <digi:trn>ODA Profile</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==5}">
												                                        <digi:trn>Sector Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==6}">
												                                        <digi:trn>Regional Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:otherwise>
												                                        <digi:trn>Paris Declaration</digi:trn>
												                                    </c:otherwise>
												                                </c:choose>
												                            </c:set>
												                            ${widgetType} 
															 	</c:when>	
															 	</c:choose>	
														 	</c:if>
													 	</c:forEach>
													 </c:forEach>
													 </td>	
												   </tr>
											</table> 
									 	</td>
									 	<td height="60px" align="center">
									 	<table width="100%" height="100%" style="border: 1px outset silver" >
										 <tr>
										 	<td>
											 	<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
													 	<c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}">
													 		<c:if test="${places.assignedWidget.id == orgProfile.id}">
														 	  <c:choose>
															 	<c:when test="${places.name =='orgprof_chart_place2'}">
															 	
															 			<c:set var="widgetType">
												                                <c:choose>
												                                    <c:when test="${orgProfile.type==1}">
												                                      <digi:trn>Summary</digi:trn><br>
                                       													<digi:trn>5 largest projects</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==2}">
												                                        <digi:trn>Type of Aid</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==3}">
												                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==4}">
												                                        <digi:trn>ODA Profile</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==5}">
												                                        <digi:trn>Sector Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==6}">
												                                        <digi:trn>Regional Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:otherwise>
												                                        <digi:trn>Paris Declaration</digi:trn>
												                                    </c:otherwise>
												                                </c:choose>
												                            </c:set>
												                            ${widgetType} 
															 	</c:when>	
															 	</c:choose>	
														 	</c:if>
													 	</c:forEach>
													 </c:forEach>
										     </td>
										  </tr>
										 </table>	
										</td>
								   	</tr>
								   	<tr>
								 	 	<td height="60px" align="center">
								 	 	<table width="100%" height="100%" style="border: 1px outset silver"  >
										 		<tr>
										 			<td>
											 	 		<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
													 	<c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}">
													 		<c:if test="${places.assignedWidget.id == orgProfile.id}">
														 	  <c:choose>
															 	<c:when test="${places.name =='orgprof_chart_place3'}">
															 	
															 			<c:set var="widgetType">
												                                <c:choose>
												                                    <c:when test="${orgProfile.type==1}">
												                                       <digi:trn>Summary</digi:trn><br>
                                       													<digi:trn>5 largest projects</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==2}">
												                                        <digi:trn>Type of Aid</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==3}">
												                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==4}">
												                                        <digi:trn>ODA Profile</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==5}">
												                                        <digi:trn>Sector Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==6}">
												                                        <digi:trn>Regional Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:otherwise>
												                                        <digi:trn>Paris Declaration</digi:trn>
												                                    </c:otherwise>
												                                </c:choose>
												                            </c:set>
												                            ${widgetType} 
															 	</c:when>	
															 	</c:choose>	
														 	</c:if>
													 	</c:forEach>
													 </c:forEach>
													  <td>
												</tr>
											</table>		 	
										</td>
									 	<td height="60px" align="center">
									 	<table width="100%" height="100%" style="border: 1px outset silver">
										 	<tr>
										 		<td>
												 	<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
													 	<c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}">
													 		<c:if test="${places.assignedWidget.id == orgProfile.id}">
														 	  <c:choose>
															 	<c:when test="${places.name =='orgprof_chart_place4'}">
															 	
															 			<c:set var="widgetType">
												                                <c:choose>
												                                    <c:when test="${orgProfile.type==1}">
												                                        <digi:trn>Summary</digi:trn><br>
                                       													<digi:trn>5 largest projects</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==2}">
												                                        <digi:trn>Type of Aid</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==3}">
												                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==4}">
												                                        <digi:trn>ODA Profile</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==5}">
												                                        <digi:trn>Sector Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==6}">
												                                        <digi:trn>Regional Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:otherwise>
												                                        <digi:trn>Paris Declaration</digi:trn>
												                                    </c:otherwise>
												                                </c:choose>
												                            </c:set>
												                            ${widgetType} 
															 	</c:when>	
															 	</c:choose>	
														 	</c:if>
													 	</c:forEach>
													 </c:forEach>
											     </td>
											  <tr> 
											 </table>	
										</td>
								   	</tr>
								   	<tr>
								 	 <td height="60px" align="center">
								 	   <table width="100%" height="100%" style="border: 1px outset silver">
										 	<tr>
										 		<td>
											 	 	<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
													 	<c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}">
													 		<c:if test="${places.assignedWidget.id == orgProfile.id}">
														 	  <c:choose>
															 	<c:when test="${places.name =='orgprof_chart_place5'}">
															 	
															 			<c:set var="widgetType">
												                                <c:choose>
												                                    <c:when test="${orgProfile.type==1}">
												                                        <digi:trn>Summary</digi:trn><br>
                                       													<digi:trn>5 largest projects</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==2}">
												                                        <digi:trn>Type of Aid</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==3}">
												                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==4}">
												                                        <digi:trn>ODA Profile</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==5}">
												                                        <digi:trn>Sector Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==6}">
												                                        <digi:trn>Regional Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:otherwise>
												                                        <digi:trn>Paris Declaration</digi:trn>
												                                    </c:otherwise>
												                                </c:choose>
												                            </c:set>
												                            ${widgetType} 
															 	</c:when>	
															 	</c:choose>	
														 	</c:if>
													 	</c:forEach>
													 </c:forEach>
													 </td>
												  </tr>
											</table>		 	
										</td>
									 	<td height="60px" align="center">
										 	
										 		<table width="100%" height="100%" style="border: 1px outset silver">
											 	<tr>
											 		<td>
											 		<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
													 	<c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}">
													 		<c:if test="${places.assignedWidget.id == orgProfile.id}">
														 	  <c:choose>
															 	<c:when test="${places.name =='orgprof_chart_place6'}">
															 	
															 			<c:set var="widgetType">
												                                <c:choose>
												                                    <c:when test="${orgProfile.type==1}">
												                                        <digi:trn>Summary</digi:trn><br>
                                       													<digi:trn>5 largest projects</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==2}">
												                                        <digi:trn>Type of Aid</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==3}">
												                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==4}">
												                                        <digi:trn>ODA Profile</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==5}">
												                                        <digi:trn>Sector Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==6}">
												                                        <digi:trn>Regional Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:otherwise>
												                                        <digi:trn>Paris Declaration</digi:trn>
												                                    </c:otherwise>
												                                </c:choose>
												                            </c:set>
												                            ${widgetType} 
															 	</c:when>	
															 	</c:choose>	
														 	   </c:if>
													 	     </c:forEach>
													 </c:forEach>
													 	</td>
													  </tr>
													</table>
										       
										  </td>
										  <tr>
										  <td height="60px" align="center">
										 
										 		<table width="100%" height="100%" style="border: 1px outset silver">
											 	<tr>
											 		<td>
											 		<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
													 	<c:forEach var="places" items="${widgetOrgProfileWidgetForm.places}">
													 		<c:if test="${places.assignedWidget.id == orgProfile.id}">
														 	  <c:choose>
															 	<c:when test="${places.name =='orgprof_chart_place7'}">
															 	
															 			<c:set var="widgetType">
												                                <c:choose>
												                                    <c:when test="${orgProfile.type==1}">
												                                      <digi:trn>Summary</digi:trn><br>
                                       													<digi:trn>5 largest projects</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==2}">
												                                        <digi:trn>Type of Aid</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==3}">
												                                        <digi:trn>Pledges</digi:trn>/<digi:trn>Comm</digi:trn>/<digi:trn>Disb</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==4}">
												                                        <digi:trn>ODA Profile</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==5}">
												                                        <digi:trn>Sector Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:when test="${orgProfile.type==6}">
												                                        <digi:trn>Regional Breakdown</digi:trn>
												                                    </c:when>
												                                    <c:otherwise>
												                                        <digi:trn>Paris Declaration</digi:trn>
												                                    </c:otherwise>
												                                </c:choose>
												                            </c:set>
												                            ${widgetType} 
															 	</c:when>	
															 	</c:choose>	
														 	   </c:if>
													 	     </c:forEach>
													 	   </c:forEach>
													 	</td>
													  </tr>
													</table>
										      
										    </td>
										  </tr>
									   </table>
								     </td>
								   </tr>	
								 </table>
								</td>
							</tr>
						</table>
		   </td>
	</tr>
</table>
<script type="text/javascript">
var string = '';

var dnl = document.widgetOrgProfileWidgetForm.selPlaces; 
for(i = 0; i < dnl.length; i++)
{
   var link = dnl.item(i);
   
   string+=','+link.value;
  
}
  var selxtr = document.getElementById('allOldId').value = string;


</script>
</digi:form>

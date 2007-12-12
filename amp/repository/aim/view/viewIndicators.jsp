<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="javascript">

   function setOverImg(index){
	  document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-righthover1.gif"
	}
	
   function setOutImg(index){
	  document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif"
	}
	
	function sortByVal(value){
	  if(value!=null){
	    <digi:context name="viewIndicators" property="context/module/moduleinstance/viewIndicators.do" />
	    document.getElementById("sortBy").value=value;
	    document.forms[0].submit();
	  }
	}
	
	function addIndicator(){
	  <digi:context name="addIndicator" property="context/module/moduleinstance/addNewIndicator.do" />
	  openURLinWindow("<%= addIndicator %>",500, 300);
	}
	
	function editIndicator(id,type){
	  <digi:context name="viewEditIndicator" property="context/module/moduleinstance/viewEditIndicator.do" />
	  openURLinWindow("<%= viewEditIndicator %>?id="+id+"&type="+type,500, 300);
	}
	
	function deletePrgIndicator(){
					return confirm("Do you want to delete the Indicator ? Please check whether the indicator is being used by some Program.");
			}
			
	function viewall(){
	    <digi:context name="viewIndicators" property="context/module/moduleinstance/viewIndicators.do?sector=viewall" />
	    document.aimViewIndicatorsForm.action = "<%= viewIndicators %>";
	    document.aimViewIndicatorsForm.submit();
	  
	}
</script>
<digi:instance property="aimViewIndicatorsForm" />

<digi:form action="/viewIndicators.do" method="post">
  <html:hidden property="sortBy" styleId="sortBy"/>
 
  
  
  
  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top width=750>
        <table cellPadding=5 cellSpacing=0 width="100%" border=0>
          <tr>
            <td height=33><span class=crumb>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn key="aim:nIndicatorManager">
              Indicator Manager
              </digi:trn>
            </td>
          </tr><%-- End navigation --%>
          <tr>
            <td height="16" vAlign="center" width="571">
              <span class="subtitle-blue">
                <digi:trn key="aim:nIndicatorManager">
                Indicator Manager
                </digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td height=16 vAlign="center" width="571">
              <html:errors />
            </td>
          </tr>
          <tr>
            <td noWrap width=100% vAlign="top">
              <table width="100%" cellspacing=0 cellSpacing=0 border=0>
                <tr>
                  <td noWrap width=600 vAlign="top">
                    <table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
                      <tr bgColor=#ffffff>
                        <td vAlign="top" width="100%">
                          <table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left" border="0">
                            <tr>
                              <td>
                                <table style="font-family:verdana;font-size:11px;" border="0">
                                  <tr>
                                    <td>
                                      <b><digi:trn key="aim:indsector">Sector</digi:trn>:</b>
                                    </td>
                                    <td>
                                      
                                      <html:select property="sectorId" styleClass="inp-text">
                                      			<html:option value="-1">-Select sector-</html:option>
												<c:if test="${!empty aimViewIndicatorsForm.sectors}">
														<html:optionsCollection name="aimViewIndicatorsForm" property="sectors" 
													value="ampSectorId" label="name" />						
												</c:if>
									</html:select>
                                    </td>
                                	<td nowrap="nowrap">
                                      <b><digi:trn key="aim:indsearchkey">Keyword</digi:trn>:</b>
                                    </td>
                                    <td>
                                      <html:text property="keyword" style="width:120px;font-family:verdana;font-size:11px;" />
                                    </td>
                                    <td>
                                    <c:set var="trngo">
                    					  <digi:trn key="aim:searchindbykey">Go</digi:trn>
                    				 </c:set>
                                      <input type="submit" value="${trngo}" class="buton"/>
                                    </td>
                                    <td>
                                     <c:set var="trnviewall">
                    					  <digi:trn key="aim:viewallind">View All</digi:trn>
                    				 </c:set>
                                      <input type="submit" value="${trnviewall}" onclick="viewall();" class="buton" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <td colspan="10" width="100%" align="center">
                                      <table width="100%" align="center"  border="0" style="font-family:verdana;font-size:11px;">
                                        <tr style="background-color:Silver;height:19px;">
                                          <td style="width:50%;">
                                            <c:if test="${aimViewIndicatorsForm.sortBy=='0'}">
                                              <b><digi:trn key="aim:indicator">Indicator Name
                                                </digi:trn></b><!-- <img alt="" src="../ampTemplate/images/arrow_up_down.gif" border="0" height="10" />-->
                                            </c:if>
                                            <c:if test="${aimViewIndicatorsForm.sortBy!='0'}">
                                              <a href="javascript:sortByVal('0')">
                                                <b><digi:trn key="aim:indicator">Indicator Name
                                                </digi:trn></b> <img alt="" src="../ampTemplate/images/arrow_up_down.gif" border="0" height="10" />
                                              </a>
                                            </c:if>
                                          </td>
                                          <td style="width:10%;" align="center">
                                            <c:if test="${aimViewIndicatorsForm.sortBy=='1'}">
                                              <b><digi:trn key="aim:indsector">Sector
                                                </digi:trn></b><!-- <img alt="" src="../ampTemplate/images/arrow_up_down.gif" border="0" height="10" />-->
                                            </c:if>
                                            <c:if test="${aimViewIndicatorsForm.sortBy!='1'}">
                                              <a href="javascript:sortByVal('1')">
                                                <b><digi:trn key="aim:indsector">Sector
                                                </digi:trn></b> <img alt="" src="../ampTemplate/images/arrow_up_down.gif" border="0" height="10" />
                                              </a>
                                            </c:if>
                                          </td>
                                          
                                          <td>
                                          &nbsp;
                                          </td>
                                        </tr>
                                        <c:if test="${!empty aimViewIndicatorsForm.allIndicators}">
                                          <c:forEach var="indItr" items="${aimViewIndicatorsForm.allIndicators}">
                                            <tr>
                                              <td>
                                                <c:set var="tIndType">
                                                  <c:if test="${indItr.type=='0'}">
                                                  Program
                                                  </c:if>
                                                  <c:if test="${indItr.type=='1'}">
                                                  Project
                                                  </c:if>
                                                  <c:if test="${indItr.type=='2'}">
                                                  Global
                                                  </c:if>
                                                </c:set>
                                                <a href="javascript:editIndicator('${indItr.id}','${tIndType}');">${indItr.name}</a>
                                              </td>
                                               <td nowrap="nowrap">
	                                                <c:forEach var="indItrsec" items="${indItr.sector}">
	                                            		<b>${indItrsec.sectorId} </b>| &nbsp;
	                                            	</c:forEach>
	                                            	
                                              </td>
                                              <td style="width:10%;">
                                              <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams}" property="indicatorId">
																${indItr.id}
														</c:set>
														
					                         		<c:set var="translation">
														<digi:trn key="aim:clickToDeleteIndicator">
																 Click here to Delete Indicator
														</digi:trn>
													</c:set>
														<digi:link href="/editAllIndicator.do~indicator=delete${tIndType}" name="urlParams" title="${translation}" onclick="return deletePrgIndicator()">
															<img src= "../ampTemplate/images/trash_12.gif" border=0>
														</digi:link>
											</td>
				                           </tr>
                                          </c:forEach>
                                        </c:if>
                                        <tr>
                                          <td colspan="10" align="center">
                                            <input type="button" value="Add Indicators" id="addBtn" onclick="addIndicator();" style="font-family:verdana;font-size:11px;"/>
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
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>

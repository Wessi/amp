<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

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

function validate() {


		if (document.aimIndicatorForm.indid.checked != null) { 
			if (document.aimIndicatorForm.indid.checked == false) {
				alert("Please choose an indicator to assign");
				return false;
			}
		}
		else { // many
			var length = document.aimIndicatorForm.indid.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimIndicatorForm.indid[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("Please choose an indicator to assign");
				return false;
			}
		}
		return true;
	}



function selectIndicators() {
		var flag = validate();
		if (flag == false)
			return false;

		<digi:context name="selInd" property="context/module/moduleinstance/addNewIndicatorTL.do"/>
	   	document.aimIndicatorForm.action = "<%= selInd %>";
		document.aimIndicatorForm.target = window.opener.name;
	   	document.aimIndicatorForm.submit();
		window.close();
		return true;
	}

	function createindicator(){

        openURLinWindow("/aim/addNewIndicator.do?add=true",500, 300);
	}


function editIndicator(id,type){
  <digi:context name="viewEditIndicator" property="context/module/moduleinstance/viewEditIndicator.do" />
  openURLinWindow("<%= viewEditIndicator %>?id="+id+"&type="+type,500, 300);
}


	function checkNumeric(objName,comma,period,hyphen)
	{
		var numberfield = objName;
		if (chkNumeric(objName,comma,period,hyphen) == false)
		{
			numberfield.select();
			numberfield.focus();
			return false;
		}
		else
		{
			return true;
		}
	}

	function chkNumeric(objName,comma,period,hyphen)
	{

// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters

		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = objName;
		var allValid = true;
		var decPoints = 0;
		var allNum = "";

		for (i = 0;  i < checkStr.value.length;  i++)
		{
			ch = checkStr.value.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
			if (ch == checkOK.charAt(j))
			break;
			if (j == checkOK.length)
			{
				allValid = false;
				break;
			}
			if (ch != ",")
			allNum += ch;
		}
		if (!allValid)
		{
			alertsay = "Please enter only numbers in the \"Number of results per page\"."
			alert(alertsay);
			return (false);
		}
	}

function selectIndicatorsPages(page) {
		
	   document.aimThemeForm.selectedindicatorFromPages.value=page;
	   document.aimThemeForm.submit();
	   return true;
	}	

	function searchindicators() {
		if(checkNumeric(document.aimIndicatorForm.tempNumResults	,'','','')==true)
		{
			if (document.aimIndicatorForm.tempNumResults.value == 0) {
				  alert ("Invalid value at 'Number of results per page'");
				  document.aimIndicatorForm.tempNumResults.focus();
				  return false;
			} else {
				 <digi:context name="searchInd" property="context/module/moduleinstance/selectCreateIndicators.do?search=true"/>
			    document.aimIndicatorForm.action = "<%= searchInd %>";
			    document.aimIndicatorForm.submit();
				  return true;
			}
		}
		else return false;
	}


function searchAlpha(val) {
		if (document.aimThemeForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimEditActivityForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do"/>
			 url = "<%= searchInd %>?alpha=" + val + "&orgSelReset=false&edit=true";
		     document.aimThemeForm.action = url;
		     document.aimThemeForm.submit();
			  return true;
		}
	}

	function searchAlphaAll(val) {
		if (document.aimThemeForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimThemeForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do"/>
			  document.aimThemeForm.action = "<%= searchInd %>";
		      var aux= document.aimThemeForm.tempNumResults.value;
		      document.aimThemeForm.tempNumResults.value=1000000;
		      document.aimThemeForm.submit();
		      document.aimThemeForm.tempNumResults.value=aux;
			  return true;
		}
	}
	
	function clearform(){
	 <digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?action=clear"/>
	  document.aimThemeForm.action = "<%= searchInd %>";
	  document.aimThemeForm.submit();
	}

	 function viewall(){
	  <digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?action=viewall"/>
	  document.aimThemeForm.action = "<%= searchInd %>";
	  document.aimThemeForm.submit();
      }

function closeWindow() 
	{
		window.close();
	}
</script>
<digi:instance property="aimIndicatorForm" />
<digi:form action="/selectCreateIndicators.do" method="post">
<html:hidden property="step"/>
<html:hidden property="item" />
<html:hidden property="activityId" value="${aimIndicatorForm.activityId}" />
        
<html:hidden property="selectedindicatorFromPages" />

 
  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top width=750>
        <table cellPadding=5 cellSpacing=0 width="100%" border=0>
            <table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:searchind">
								Search Indicators</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
									<td>
									<digi:trn key="aim:selsector">
											Select Sector</digi:trn>
									</td>
										<td>
										
                                      <html:select property="sectorName" styleClass="inp-text">
                                      			<html:option value="-1">-Select sector-</html:option>
												<c:if test="${!empty aimIndicatorForm.allSectors}">
									            <html:optionsCollection name="aimIndicatorForm" property="allSectors" value="name" label="name" />					
												</c:if>
									</html:select>
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:enterKeyword">
											Enter a keyword</digi:trn>
										</td>
										<td>
										<html:text property="searchkey" style="width:140px;font-family:verdana;font-size:11px;" />
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:numResultsPerPage">
											Number of results per page</digi:trn>
										</td>
										<td>
											<html:text property="tempNumResults" size="2" styleClass="inp-text" />
										</td>
									</tr>
									<tr>
										<td align="center" colspan=2>
											&nbsp;
											<!-- <input type="submit" value="Go" style="font-family:verdana;font-size:11px;" /> -->
											<html:button  styleClass="dr-menu" property="submitButton" onclick="return searchindicators()">
												<digi:trn key="btn:search">Search</digi:trn> 
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="submitButton" onclick="clearform()" >
												<digi:trn key="btn:clear">Clear</digi:trn> 
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
												<digi:trn key="btn:close">Close</digi:trn> 
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="submitButton" onclick="viewall()">
												<digi:trn key="btn:viewall">View all</digi:trn>
											</html:button>
											<html:button  styleClass="dr-menu" property="submitButton" onclick="createindicator()">
												<digi:trn key="btn:create">Create New Indicator</digi:trn>
											</html:button>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
              <table width="100%" cellspacing=0 cellSpacing=0 border="0">
                <tr>
                  <td noWrap width=600 vAlign="top">
                    <table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
                      <tr bgColor=#ffffff>
                        <td vAlign="top" width="100%">
                          <table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left">
                            <tr>
                              <td>
                                <table style="font-family:verdana;font-size:11px;" width="100%">
                                  <tr>
                                    <td colspan="11" width="100%" align="center">
                                      <table width="100%" align="center" border="0" style="font-family:verdana;font-size:11px;">
                                        <tr bgcolor="#006699">
	                                        <td vAlign="center" width="100%" align ="center" class="textalb" height="20" colspan="2">
												<digi:trn key="aim:listofprgIndicators">
													List Of Program Indicators
												</digi:trn>
											</td>
										</tr>
                                    </tr>
                              <logic:notEmpty name="aimIndicatorForm" property="pagedCol">  
                                    <logic:iterate name="aimIndicatorForm" id="indicators" property="pagedCol"
									type="org.digijava.module.aim.dbentity.AmpIndicator">
										<tr>
											<td bgcolor=#ECF3FD width="10%">
											<html:multibox property="indid">
													<bean:write name="indicators" property="indicatorId" />
											</html:multibox>
											</td>
											<td bgcolor=#ECF3FD width="90%">
											<bean:write name="indicators" property="name" />
											</td>
										</tr>
										<tr>
										<td>
										 &nbsp;
										</td>
										</tr>
										
									</logic:iterate>
									
                                        <tr>
                                          <td colspan="10" align="center">
                                            &nbsp;
                                          </td>
                                        </tr>
                                        <tr>
                                          <td colspan="10" align="center">
                                          <html:button  styleClass="dr-menu" property="submitButton"  onclick="return selectIndicators()">
															<digi:trn key="btn:add">Add</digi:trn> 
										  </html:button>
										
										                                   </td>
                                        </tr>
                                      </table>
                                          <logic:notEmpty name="aimIndicatorForm" property="pages">
											<tr>
												<td align="center">
													<table width="90%">
													<tr><td>
													<digi:trn key="aim:pages">
													Pages</digi:trn>
													<logic:iterate name="aimIndicatorForm" property="pages" id="pages" type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
														<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
														<c:set target="${urlParams1}" property="edit" value="true"/>
				
														<c:if test="${aimIndicatorForm.currentPage == pages}">
															<font color="#FF0000"><%=pages%></font>
														</c:if>
														<c:if test="${aimIndicatorForm.currentPage != pages}">
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</bean:define>
				
															<a href="javascript:selectIndicatorsPages(<%=pages%>);"><%=pages %></a>
														</c:if>
														|&nbsp;
													</logic:iterate>
													</td></tr>
													</table>
												</td>
											</tr>
										</logic:notEmpty>
									   <logic:notEmpty name="aimIndicatorForm" property="alphaPages">
											<tr>
												<td align="center">
													<table width="90%">
													<tr><td>
														<bean:define id="translation">
															<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
														</bean:define>
														<a href="javascript:searchAlphaAll('viewAll')" title="<%=translation%>">
															viewAll</a>&nbsp;|&nbsp;
														<logic:iterate name="aimIndicatorForm" property="alphaPages" id="alphaPages" type="java.lang.String">
															<c:if test="${alphaPages != null}">
																<c:if test="${aimIndicatorForm.currentAlpha == alphaPages}">
																	<font color="#FF0000"><%=alphaPages %></font>
																</c:if>
																<c:if test="${aimIndicatorForm.currentAlpha != alphaPages}">
																<bean:define id="translation">
																	<digi:trn key="aim:clickToViewNextPage">Click here to go to next page</digi:trn>
																</bean:define>
																	<a href="javascript:searchAlpha('<%=alphaPages%>')" title="<%=translation%>" >
																		<%=alphaPages %></a>
																</c:if>
															|&nbsp;
															</c:if>
														</logic:iterate>
													</td></tr>
													</table>
												</td>
											</tr>
										</logic:notEmpty>
				                     </logic:notEmpty>                
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
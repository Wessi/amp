<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>


<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />


<script language="JavaScript">
function addData(){
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=addIndValue" />
  document.forms[0].action = "<%=addEditIndicator%>";
  document.forms[0].submit();
}


function deleteData(ind){
  var flag = confirm("Delete this indicator?");
  if(flag == true){
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=delIndValue" />
  document.forms[0].action = "<%=addEditIndicator%>&index="+ind;
  document.forms[0].submit();
  }
}

function saveIndicator(id){
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=save" />
  document.forms[0].action = "<%=addEditIndicator%>";
  document.forms[0].submit();
  window.close();
  window.opener.document.forms[0].submit();
}

function selectLocation(index){
  <digi:context name="selLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do"/>
  openURLinWindow("<%=selLoc%>?index="+index,700,500);
}
</script>

<digi:instance property="aimThemeForm" />
<digi:form action="/addEditData.do" method="post">
<digi:context name="digiContext" property="context"/>
<input type="hidden" name="event">
<table  width=572 cellPadding=4 cellSpacing=1 valign=top align=left bgcolor="#ffffff" border="0">
  <tr>
    <td bgColor=#d7eafd class=box-title height="10" align="center" colspan="7">
    Add/Edit Data: ${aimThemeForm.indame}
    </td>
  </tr>
  <tr bgcolor="#003366" class="textalb">
    <td align="center" valign="middle" width="75">
      <b><font color="white">Actual/Base/<br>Target</font></b>
    </td>
    <td align="center" valign="middle" width="120">
      <b><font color="white">Value</font></b>
    </td>
    <td align="center" valign="middle" width="120">
      <b><font color="white">Creation Date</font></b>
    </td>
    <td align="center" valign="middle" width="120" colspan="3">
      <b><font color="white">Add Location</font></b>
    </td>
  </tr>

  <c:if test="${!empty aimThemeForm.prgIndValues}">
    <c:forEach var="ind" varStatus="index" items="${aimThemeForm.prgIndValues}">
        <tr>
          <td bgColor=#d7eafd  height="10" align="center" width="10%">
            <html:select name="ind" property="valueType" styleClass="inp-text">
              <html:option value="1">Actual</html:option>
              <html:option value="2">Base</html:option>
              <html:option value="0">Target</html:option>
            </html:select>
          </td>

          <td bgColor=#d7eafd height="10" align="center" width="10%">
            <html:text name="ind" property="valAmount" styleId="txtName" styleClass="amt"/>
          </td>

          <td bgColor=#d7eafd  height="10" align="center" nowrap="nowrap">
            <html:text name="ind" property="creationDate" styleId="txtDate${index.count-1}" readonly="true" style="width:80px;"/>
			<a id="date${index.count-1}" href='javascript:pickDateById("date${index.count-1}","txtDate${index.count-1}")'>
				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0> 
			</a> 

          </td>

          <td bgColor=#d7eafd width="100%">
            <c:if test="${ind.location!=null}">
            [${ind.location.name}]
            </c:if>
            <c:if test="${ind.location==null}">
              <span>[<span style="color:Red">National</span>]</span>
            </c:if>
          </td>

          <td bgColor=#d7eafd  height="10" nowrap="nowrap">
            [<a href="javascript:selectLocation('${index.count-1}')">
            	Add location
              <!-- <img src="../ampTemplate/images/closed.gif" border="0" alt="Select location" /> -->
            </a>]
          </td>

          <td bgColor=#d7eafd>
            <a href="javascript:deleteData('${index.count-1}')">
              <img src="../ampTemplate/images/trash_16.gif" border="0" alt="Delete indicator value" />
            </a>
          </td>
        </tr>
    </c:forEach>
  </c:if>

  <c:if test="${empty aimThemeForm.prgIndicators}">
    <tr align="center" bgcolor="#ffffff"><td><b>
      <digi:trn key="aim:noIndicatorsPresent">No data present</digi:trn></b></td>
    </tr>
    <tr bgColor="#d7eafd">
      <td>
      &nbsp;
      </td>
    </tr>
  </c:if>
  <tr>
    <td height="25" align="center" colspan="6">
      <input style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="Add Data" onclick="addData()">&nbsp;&nbsp;
    </td>
  </tr>
  <tr>
    <td bgColor=#dddddb height="25" align="center" colspan="6">
      <input class="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveIndicator('${aimThemeForm.themeId}')">&nbsp;&nbsp;
      <input class="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
      <input class="dr-menu" type="button" name="close" value="Close" onclick="window.close();">
    </td>
  </tr>
</table>

</digi:form>
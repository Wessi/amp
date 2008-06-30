<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="quartzJobManagerForm" />

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script type="text/javascript">
function setAction(action){
  var act=document.getElementById("hdnAction");
  if(act!=null){
    act.value=action;
  }else{
    return false;
  }
  return true;
}

function saveJob(){
  var txt=null;
  txt=document.getElementById("cmbJc");
  if(txt==null){
    txt=document.getElementById("txtClassFullname");
    if(txt==null || txt.value==""){
      alert("Please enter Class Fullname!");
      txt.focus();
      return false;
    }
  }

  txt=document.getElementById("txtName");
  if(txt==null || txt.value==""){
    alert("Please enter Name!");
    txt.focus();
    return false;
  }

  txt=document.getElementById("txtStartDateTime");
  if(txt==null || txt.value==""){
    alert("Please enter Start Date/Time!");
    txt.focus();
    return false;
  }

  var rda=document.getElementsByName("triggerType");
  if(rda!=null){
    var flag=-1;
    for(var i=0;i<rda.length;i++){
      if(rda[i].checked){
        flag=i;
        break;
      }
    }
    if(flag==-1){
      alert("Please select Job type");
      return false;
    }
  }

  txt=document.getElementById("txtTime");
  if(txt==null || txt.value==""){
    alert("Please enter time");
    txt.focus();
    return false;
  }
  if(setAction("saveJob")){
    document.quartzJobManagerForm.submit();
  }
}
function typeChanged(value){
  var cmb=document.getElementById("cmbWeekDays");
  if(cmb!=null){
    if(value==4){
      cmb.disabled=false;
    }else{
      cmb.disabled=true;
    }
  }
}
</script>

<digi:form action="/quartzJobManager.do" method="post">
  <html:hidden name="quartzJobManagerForm" property="action" styleId="hdnAction" />
  <table>
    <tr>
      <td>
      &nbsp;&nbsp;&nbsp;
      </td>
      <td>
        <table>
          <tr>
            <!-- Start Navigation -->
            <td>
              <span class="crumb">
                <c:set var="translation">
                  <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:link href="/msgSettings.do~actionType=getSettings" styleClass="comment" title="${translation}" >
                  <digi:trn key="message:messageSettings">Message Settings</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:link href="/quartzJobManager.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:jobManager">Job Manager</digi:trn>
                </digi:link>
                &nbsp;&gt;&nbsp;
                <digi:trn key="aim:addJob">Add new job</digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td style="height:53px;">
              <span class="subtitle-blue">
                <digi:trn key="aim:addJob">Add New Job</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <table style="width:400px;">
                <tr>
                  <td>
                    <c:if test="${empty quartzJobManagerForm.jcCol}">
                      <span style="color:red;">*</span>
                      <digi:trn key="aim:job:classFullname">Class fullname:</digi:trn>
                    </c:if>
                    <c:if test="${!empty quartzJobManagerForm.jcCol}">
                      <digi:trn key="aim:job:class">Class:</digi:trn>
                    </c:if>
                  </td>
                  <td>
                    <c:if test="${!empty quartzJobManagerForm.jcCol}">
                      <html:select name="quartzJobManagerForm" property="classFullname" value="classFullname" styleId="cmbJc" style="font-family:Verdana;font-size:10px;width:250px;">
                        <c:forEach var="jc" items="${quartzJobManagerForm.jcCol}">
                          <html:option value="${jc.classFullname}">${jc.name}</html:option>
                        </c:forEach>
                      </html:select>
                    </c:if>
                    <c:if test="${empty quartzJobManagerForm.jcCol}">
                      <html:text name="quartzJobManagerForm" property="classFullname" styleId="txtClassFullname" style="font-family:Verdana;font-size:10px;width:250px;" />
                    </c:if>
                  </td>
                </tr>
                <tr>
                  <td>
                    <span style="color:red;">*</span>
                    <digi:trn key="aim:job:name">name</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobManagerForm" property="name" styleId="txtName" style="font-family:Verdana;font-size:10px;width:250px;" />
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="6">
            &nbsp;
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <table style="border:dashed 1px;width:400px;">
                <tr>
                  <td>
                    <span style="color:red;">*</span>
                    <digi:trn key="aim:job:startDateTime">Start date/time</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobManagerForm" property="startDateTime" styleId="txtStartDateTime" style="font-family:Verdana;font-size:10px;width:250px;" />
                  </td>
                </tr>
                <tr>
                  <td>
                    <digi:trn key="aim:job:endDateTime">End date/time</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobManagerForm" property="endDateTime" style="font-family:Verdana;font-size:10px;wodth:250px;width:250px;" />
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <b><digi:trn key="aim:job:startEndDateTimeNote">(All date/time format should be like MM/dd/yyyy HH:mm:ss)</digi:trn></b>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="6">
            &nbsp;
            </td>
          </tr>
          <tr>
            <td colspan="2">
            <span style="color:red;">*</span>
            <digi:trn key="aim:job:jobType">Job Type</digi:trn>
            <table style="border:dashed 1px;width:400px;">
              <tr>
                <td colspan="2">
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="0" onclick="typeChanged(0);" />Secondly
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="1" onclick="typeChanged(1);" />Hourly
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="2" onclick="typeChanged(2);" />Minutely
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="3" onclick="typeChanged(3);" />Daily
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="4" onclick="typeChanged(4);" />Weekly
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <digi:trn key="aim:job:jobDayOfWeek">Select Day of week</digi:trn>
                  <html:select name="quartzJobManagerForm" property="selectedDay" value="selectedDay" styleId="cmbWeekDays" style="font-family:Verdana;font-size:10px;" disabled="true">
                    <html:option value="1">1</html:option>
                    <html:option value="2">2</html:option>
                    <html:option value="3">3</html:option>
                    <html:option value="4">4</html:option>
                    <html:option value="5">5</html:option>
                    <html:option value="6">6</html:option>
                    <html:option value="7">7</html:option>
                  </html:select>
                  <digi:trn key="aim:job:time">Time</digi:trn>
                  <html:text name="quartzJobManagerForm" property="exeTime" styleId="txtTime" style="font-family:Verdana;font-size:10px;" />
                </td>
              </tr>
            </table>
            </td>
          </tr>
          <tr>
            <td>
              <c:set var="trn">
                <digi:trn key="aim:job:btnSave">Save</digi:trn>
              </c:set>
              <input type="button" value="${trn}" onclick="saveJob()"/>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>

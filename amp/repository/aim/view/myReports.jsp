<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script type="text/javascript">
	function popup(mylink, windowname){
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string'){
   			href=mylink;
		}else{
   			href=mylink.href;
			window.open(href, windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
			return false;
		}
}
</script>
<c:set var="translation2">
	<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>
</c:set>
<div class="right_menu" style="margin-top:20px;">
	<div class="right_menu_header">
		<div class="right_menu_header_cont">	
			<digi:trn key="aim:portfolioReports">Reports</digi:trn>
		</div>
	</div>
	<div class="right_menu_box">
		<div class="right_menu_cont">
        <ul>
			<logic:notPresent name="currentMember" scope="session">
				<logic:empty name="myReports" scope="session">
						<digi:trn key="aim:noPublicReports">No public reports</digi:trn>
				</logic:empty>
			</logic:notPresent>
			<logic:notEmpty name="myReports" scope="session">
			<logic:iterate name="myReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports" length="5">
				<li class="tri">
					<digi:link  title="${report.name}" href="/viewNewAdvancedReport.do?view=reset&widget=false" paramName="report"  paramId="ampReportId" paramProperty="ampReportId" onclick="return popup(this,'');">
	                    <c:if test="${fn:length(report.name) > 50}" >
	                    	<c:out value="${fn:substring(report.name, 0, 50)}"/>...
	                    </c:if>
	                    <c:if test="${fn:length(report.name) <= 50}">
	                    	<c:out value="${report.name}"/>
	                    </c:if>
					</digi:link>
				</li>
			</logic:iterate>
			<bean:size id="repCount" name="myReports" scope="session" />
                   <div style="padding-top:10px;margin-left:12px;margin-top:5px; margin-bottom: 7px" title="<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>">
                  
                    	 <digi:link href="/viewTeamReports.do?tabs=false&reset=true">
                            <digi:trn key="aim:moreReports">More Reports...</digi:trn>
                        </digi:link>
                    </div>
           	</logic:notEmpty>
			<logic:present name="currentMember" scope="session">
            	<logic:empty name="myReports" scope="session">
                	<digi:trn key="aim:noReportHaveBeenViewed">No reports have been viewed.</digi:trn>
                 </logic:empty>
			</logic:present> 
            </ul>
		</div>
	</div>
</div>

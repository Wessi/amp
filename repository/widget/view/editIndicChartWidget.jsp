<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript">
<!--
	function cancelEdit(myForm){
		<digi:context name="justSubmit" property="context/module/moduleinstance/indicatorchartwidgets.do" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
//-->
</script>
<digi:instance property="gisIndicatorChartForm" />
<digi:form action="/indicatorchartwidgets.do~actType=save">

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
                <digi:trn key="admin:Navigation:indicatorchartWidgets">Indicator chart widgets</digi:trn>
                &nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:indicatorchartWidgetsCreateEdit">Input widget fields</digi:trn>
			</span>
		</td>
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="gis:indicatorChartWidgetCreateEdit:pageHeader">Edit widget data</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<html:hidden name="gisIndicatorChartForm" property="widgetId"/>
			<table>
				<tr>
					<td nowrap="nowrap" align="right">
						<font color="red">*</font>
						<strong>
							<digi:trn key="gis:editIndicChartWidget:widgetName">WidgetName</digi:trn>
						</strong>
					</td>
					<td>
						<html:text name="gisIndicatorChartForm" property="widgetName" style="width : 300px"/>
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">
						<font color="red">*</font>
						<strong>
							<digi:trn key="gis:editIndicChartWidget:selIndicator">Indicator</digi:trn>
						</strong>
					</td>
					<td>
						<html:select name="gisIndicatorChartForm" property="selIndicators" style="width : 300px">
							<html:option value="-1">&nbsp;</html:option>
							<html:optionsCollection name="gisIndicatorChartForm" property="indicators" label="indicator.name" value="id"/>
						</html:select>
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">
						<strong>
							<digi:trn key="gis:editIndicChartWidget:place">Place</digi:trn>
						</strong>
					</td>
					<td>
						<html:select name="gisIndicatorChartForm" property="selPlaces" multiple="true" style="width: 300px">
							<html:optionsCollection name="gisIndicatorChartForm" property="places" value="id" label="name"/>
						</html:select>
					</td>
					<td valign="top">
						<input type="button" value="Unselect All">
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<hr>
					</td>
				</tr>
				<tr>
					<td align="right">
						<html:submit>
							<digi:trn key="gis:editIndicatorChartWidget:btnSave">Save</digi:trn>
						</html:submit>
					</td>
					<td>
						<input type="button" value="Cancel" onclick="cancelEdit(this.form)">
					</td>
				</tr>
			</table>

		</td>
	</tr>
</table>
</digi:form>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimLoginForm" />


<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/jSortTable.js"></script>




<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772 height="201">
	<tr>
		<td width=4>&nbsp;
		</td>
		<td align=left vAlign=top width=600><br>
			<table border=0 cellPadding=5 cellSpacing=3 width="100%">
				
			
				 <thead>
				<tr>
					<th style="text-align: left;">&nbsp;</th>
					<th style="text-align: left; padding-left:24px;">
					<a href="" onclick="this.blur(); return sortTable('offTblBdy', 1, false);" title="Team Name" style="text-decoration:none;">
						<span class="page-title">
						<digi:trn key="aim:selectTheTeam">
						Select the team you want to use in this current session</digi:trn>
						</span>
						</a>
					</th>
				</tr>	
				</thead>
				 <tbody id="offTblBdy">
						
				<c:forEach var="members" items="${aimLoginForm.members}">
					<tr>
						<td></td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<IMG height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
							<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlParams}" property="id">
								<c:out value="${members.ampTeamMemId}"/>
							</c:set>								
							<digi:link href="/selectTeam.do" name="urlParams">
							<c:out value="${members.ampTeam.name}"/></digi:link>
					</td></tr>
				</c:forEach>
				
				  </tbody>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>

<script language="javascript">

sortTable('offTblBdy', 1, true);

</script>
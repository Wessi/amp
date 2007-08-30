<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>




<digi:context name="displayFlag" property="context/aim/default/displayFlag.do" />
<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>



<table cellspacing="0" cellPadding="0" border="0" width="100%" bgcolor="#323232" vAlign="top"

	height="34">

	<tbody>

   	<tr bgColor="#323232">

   			<td valign="center" height="34">&nbsp;



   			<digi:link href="/" module="aim" styleClass="heading" onclick="return quitRnot1('${msg}')" title="Aid Management Platform">

					<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn>

				</digi:link>

			</td>

			<logic:notEmpty name="defFlagExist" scope="application">

				<logic:equal name="defFlagExist" scope="application" value="true">

					<td valign="top" width="60">

						<img src="<%=displayFlag%>" border="0" height="34" width="50">

					</td>

				</logic:equal>

			</logic:notEmpty>

   	</tr>

   </tbody>

</table>


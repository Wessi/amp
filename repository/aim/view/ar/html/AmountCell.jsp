<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>
<logic:notPresent name="debug">
<div align="right">
</logic:notPresent>
<logic:present name="debug">
<div align="center" onMouseOver="stm(['AmountCell List',document.getElementById('<bean:write name="amountCell" property="column.name"/>-<bean:write name="amountCell" property="ownerId"/>').innerHTML],Style[1])" onMouseOut="htm()">
</logic:present>
<bean:write name="amountCell"/>&nbsp;
<logic:present name="debug">
</div>
</logic:present>
<logic:notPresent name="debug">
</div>
</logic:notPresent>
<logic:present name="debug">
<div style='position:relative;display:none;' id='<bean:write name="amountCell" property="column.name"/>-<bean:write name="amountCell" property="ownerId"/>'> 
<ul>
<li>From Rate=<bean:write name="amountCell" property="fromExchangeRate"/>
<li>To Rate=<bean:write name="amountCell" property="toExchangeRate"/>
<li>Percentage=<bean:write name="amountCell" property="percentage"/>%
<li>Currency Code=<bean:write name="amountCell" property="currencyCode"/>
<li>Currency Date=<bean:write name="amountCell" property="currencyDate"/>
<li>Activity Owner Id=<bean:write name="amountCell" property="ownerId"/>
<li>Source Column=<bean:write name="amountCell" property="column.name"/>
</ul>
</div>
	<logic:notEmpty name="amountCell" property="mergedCells">
=(<i>
<bean:write name="amountCell" property="wrappedAmount"/><i> &nbsp;
		<logic:iterate id="mergedCell" name="amountCell" property="mergedCells">
			<bean:define id="noDiv" value="true" toScope="request" />
			<bean:define id="viewable" name="mergedCell" type="org.dgfoundation.amp.ar.cell.Cell" scope="page" toScope="request"/>
			<jsp:include page="AmountCell.jsp"/>+
		</logic:iterate>
0)
</i>

</logic:notEmpty>
</logic:present>

<%@page trimDirectiveWhitespaces="true"%>
<%-- renders the funding part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div id='pledge_form_funding_data'>
	<c:if test="${empty pledgeForm.selectedFundingList}">
		<div class="text-center"><h3><digi:trn>No Pledge Details</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.selectedFundingList}">
		<c:set var="indexLoc" value="-1" />
		<c:forEach var="selectedFunding" items="${pledgeForm.selectedFundingList}" varStatus="index">
			<c:set var="indexLoc" value="${indexLoc+1}" />
			<div class="amp-subsection" id="pledge_form_row_for_funding_${selectedFunding.uniqueId}">
				<div class="amp-subsection-title">
					<digi:trn>Pledge Detail</digi:trn> nr. ${indexLoc+1}
					<button type="button" onclick="fundingsController.onDelete(${selectedFunding.uniqueId});" class="btn btn-danger btn-xs amp-btn-delete-subsection"><digi:trn>Delete</digi:trn></button></span>
				</div>		
			<div class="container-fluid"> <!-- misc data -->
				<div class="col-xs-6">
					<label for="pledgeTypeDropDown_${indexLoc}"><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></label> <br />					
					<c:set var="select_id" value="pledgeTypeDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].pledgeTypeId" data-width="100%"</c:set>
					<c:set var="select_values" value="${pledgeForm.pledgeTypes}" />
					<c:set var="select_init_value" value="${selectedFunding.pledgeTypeId}" />
					<%@include file="renderShimList.jspf" %>				
				</div>
				<div class="col-xs-6">
					<div class="form-inline" >
						<label for="pledgeAmount_${indexLoc}"><digi:trn key="aim:amount">Amount</digi:trn></label> <br />
						<input name="selectedFunding[${indexLoc}].amount" type="text" id="pledgeAmount_${indexLoc}" class="form-control input-sm validate-mandatory-number" value="${selectedFunding.amount}"/>
					
						<c:set var="select_id" value="pledgeCurrencyDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].currencyId" class="validate-mandatory"</c:set>
						<c:set var="select_values" value="${pledgeForm.validCurrencies}" />
						<c:set var="select_init_value" value="${selectedFunding.currencyId}" />
						<%@include file="renderShimList.jspf" %>
					</div>										
				</div>
				<div class="col-xs-6">
					<label for="pledgeFundingYear_${indexLoc}"><digi:trn key="aim:year">Year</digi:trn></label>
					<input name="selectedFunding[${indexLoc}].fundingYear" type="text" id="pledgeFundingYear_${indexLoc}" class="form-control input-sm validate-year" value="${selectedFunding.fundingYear}"/> 					
				</div>
				<c:if test="${pledgeForm.fundingShowTypeOfAssistance}">
					<div class="col-xs-6">
						<label for="pledgeTypeOfAssistanceDropDown_${indexLoc}"><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></label> <br />
						<c:set var="select_id" value="pledgeTypeOfAssistanceDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].typeOfAssistanceId" data-width="100%"</c:set>
						<c:set var="select_values" value="${pledgeForm.typesOfAssistance}" />
						<c:set var="select_init_value" value="${selectedFunding.typeOfAssistanceId}" />
						<%@include file="renderShimList.jspf" %>
					</div>
				</c:if>
				
				<c:if test="${pledgeForm.fundingShowAidModality}">
					<div class="col-xs-6">
						<label for="pledgeAidModalityDropDown_${indexLoc}"><digi:trn key="aim:aidModality">Aid Modality</digi:trn></label><br />				
						<c:set var="select_id" value="pledgeAidModalityDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].aidModalityId"</c:set>
						<c:set var="select_values" value="${pledgeForm.aidModalities}" />
						<c:set var="select_init_value" value="${selectedFunding.aidModalityId}" />
						<%@include file="renderShimList.jspf" %>					
					</div>
				</c:if>
			</div>
			</div>
		</c:forEach>
	</c:if>
</div>

<script type="text/javascript">
	on_element_loaded();
</script>
